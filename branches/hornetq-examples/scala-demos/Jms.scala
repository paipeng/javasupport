import javax.jms._
import javax.naming._

class RichSession(val jmsSession : Session) {
  /** Create a temporary queue for processing. 
   * The queue will be deleted after func is complete. */
  def withTempQ(func : TemporaryQueue => Unit) {    
    val queue = jmsSession.createTemporaryQueue
    try { func(queue) } finally { queue.delete }
  }  
  
  /** Create a message consumer and close it after func processing. 
   * You may use #toQueue or #toTopic to convert string into Destination object. */ 
  def withConsumer(dest : Destination)(func : MessageConsumer => Unit) {
    val consumer = jmsSession.createConsumer(dest)
    try { func(consumer) } finally { consumer.close }
  }  
  
  /** Create a message producer and close it after func processing. 
   * You may use #toQueue or #toTopic to convert string into Destination object. */ 
  def withProducer(dest : Destination)(func : MessageProducer => Unit) {
    val producer = jmsSession.createProducer(dest)
    try { func(producer) } finally { producer.close }
  }
    
  /** Wrapper to session create message call */
  def createTextMsg(text : String) = jmsSession.createTextMessage(text)
    
  /** Wrapper to session create message call */
  def createMapMsg[T](map : Map[String, T]) = {
    val msg = jmsSession.createMapMessage
    map.foreach { case (k,v) => msg.setObject(k, v) }
    msg
  }  
  
  /** Wrapper to session create message call */
  def createBytesMsg(data : Array[Byte]) = {
    val msg = jmsSession.createBytesMessage
    msg.writeBytes(data)
    msg
  }   
  
  /** Wrapper to session create message call */
  def createBytesMsg(input : java.io.InputStream) = {
    val msg = jmsSession.createBytesMessage
    Utils.eachBytesBlock(input) { block => msg.writeBytes(block) }
    msg
  }
  
  /** Wrapper to session create message call */
  def createObjectMsg(obj : java.io.Serializable) = {
    val msg = jmsSession.createObjectMessage
    msg.setObject(obj)
    msg
  }
  
  /** Send a text messgage into a destination. It will optionally take a map
   * and convert and set them into the message proeperties. */
  def send(dest : Destination, text : String, props : Map[String, String] = Map()) {
    withProducer(dest) { producer =>
      val msg = createTextMsg(text)
      Jms.addMapToMsgProps(msg, props)
      producer.send(msg) 
    }
  }
}

class Jms(connectionFactory : ConnectionFactory) {  
  def withConnection(func : Connection => Unit) {
    val conn = connectionFactory.createConnection
    try { func(conn) } finally { conn.close }
  }
  
  def withSessionFrom(conn : Connection)(func : Session => Unit) {    
    val jmsSession = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)      
    try {
      conn.start
      func(jmsSession)
    } finally {
      jmsSession.close
      conn.stop
    }
  }
    
  def withSession(func : Session => Unit) { 
    withConnection { conn => withSessionFrom(conn) { jmsSession => func(jmsSession) } }
  }
}

/** A companion object to Jms class. */
object Jms {
  val DEFAULT_CONNECTION_FACTORY_NAME = "ConnectionFactory"
  
  def fromJndi(name : String = DEFAULT_CONNECTION_FACTORY_NAME) = {
    val cf = lookupJndi[ConnectionFactory](name)
    new Jms(cf)
  }
  
  def fromClassName(name : String) = {
    val cf = java.lang.Class.forName(name).newInstance().asInstanceOf[ConnectionFactory]
    new Jms(cf)
  }
  
  /** Convert and set a map values into a JMS message properties. */
  def addMapToMsgProps[T](msg : Message, props : Map[String, T]) = {
    props.foreach { case (k,v) => msg.setObjectProperty(k, v) }
  } 
  
  /** Lookup object from JNDI by default in initial context. */
  def lookupJndi[T](name : String, jndiCtx : Context = new InitialContext) : T = {
    try { jndiCtx.lookup(name).asInstanceOf[T] } finally { jndiCtx.close }
  }
  
  def createMessageListener(func : Message => Unit) = new MessageListener { 
    def onMessage(msg : Message) = func(msg)
  }
  
  implicit def jmsToRichSession(session: Session) = new RichSession(session)
}

class JmsTest(val jms : Jms) {  
  import Jms.jmsToRichSession
  
  /** See some message implementation class names. */
  def testSession {
    jms.withSession { session =>
      println("createTextMessage " + session.createTextMsg("foo").getClass)
      println("createMapMessage " + session.createMapMsg(Map("a"->"A", "b"->"B")).getClass)      
      println("createObjectMessage " + session.createObjectMsg(new java.util.Date).getClass)     
      println("createBytesMsg " + session.createBytesMsg(Array[Byte](0,1,2,3)).getClass)     
      
      val ins = new java.io.ByteArrayInputStream(Array[Byte](0,1,2,3))
      try { println("createBytesMsg_fromStream " + session.createBytesMsg(ins).getClass) } finally { ins.close }
      
      println("createMessage " + session.createMessage.getClass)
      println("createStreamMessage " + session.createStreamMessage.getClass)
    }
  }
  
  /** Note that we have create two temp queues under the same jms session. */
  def testTempQ {
    jms.withSession { session => 
      session.withTempQ { q => println("TempQ1: " + q.getClass) } 
      session.withTempQ { q => println("TempQ2: " + q.getClass) } 
    }
  }
  
  /** Create a temp q and try to send and receive msg on it. */
  def testSendToTempQ {
    def process(msg : Message) = msg match {
      case m : TextMessage => 
        println("Received msg from TempQ: " + m.getText + ", props['from'] " + m.getStringProperty("from"))
      case _ => 
        println("Received msg from TempQ: " + msg)
    }
          
    jms.withSession { session => 
      session.withTempQ { q => 
        println("Created TempQ: " + q.getClass) 
        session.send(q, "test msg")
        session.send(q, "test2 msg", Map("from" -> "Zemian"))
        println("2 msgs sent to TempQ. Will try to receive it now...")
        session.withConsumer(q) { consumer => 
          process(consumer.receive)
          process(consumer.receive)          
        }
      } 
    }
  }
  
  /** Listen and consume messages from a q. */
  def testMesssageListener(qname : String = "ExampleQueue") {
    println("Started: " + new java.util.Date)
        
    jms.withConnection { conn =>
    	// Allow user hit CTRL+C to bring down listener
    	var isRunning = true
    	Utils.shutdownHook {
    		println("Shutting down listener.")
    		conn.stop
    		isRunning = false
    	}
    	
			jms.withSessionFrom(conn) { session =>
				val q = session.createQueue(qname)
				session.withConsumer(q) { consumer =>
					var totalCount = 0   // Total number of messages received
					var count = 0        // Number of messages received within a print repeat cycle.
					var zeroCount = 0    // Number of consecutive rate == 0.0 counts
					val maxZeros = 3     // We allow max of 3 zero rate line printed, then supress until rate > 0.
					
					// Prepare an instance of jms MessageListener: it will be run on a separate thread.
					val listener = Jms.createMessageListener { msg => 
						totalCount += 1
						count += 1  
					}
					consumer.setMessageListener(listener)
					
					// Listener is ready.
					println("Listener started on " + q + ". Hit CTRL+C to stop.")
					println("wait for msg...")        
					
					// The main thread will check on message count and print rate every 5 secs or 1000 msgs.
					val period = 5 * 1000   //Max time period before print the rate value.
					val repeatN = 1000      //Max number of message received before print the rate value.
					var t = Utils.ts        //current timestamp
					
					while (isRunning) {
						val tstamp = Utils.ts
						//printf("zeroCount %d, count mod repeatN %d, elapse %d\n", zeroCount, (count % repeatN), (Utils.ts - t))
						// Let's print rate if:
						//   1. There no more than maxZeros rate of zero consecutively
						//   2. Or there are more than repeatN number of message received since last printed rate
						//   3. Or time elapsed longer than period amount.
						if (zeroCount <= maxZeros && (count > 0 && count % repeatN == 0) || (Utils.ts - t) > period) {
							// Calculate rate value on this period cycle.
							val elapse = tstamp - t
							var rate = if (count == 0 || elapse == 0) {
								zeroCount += 1
								0.0
							} else {
								zeroCount = 0
								count / (elapse / 1000.0)
							}
							
							// Print only if it not zeros more than maxZeros times
							if (zeroCount <= maxZeros) {
								printf(new java.util.Date() + "> received rate: %.2f msgs/sec, totalCount: %d\n", rate, totalCount)  
							}
							
							// Reset cycle values
							count = 0  
							t = tstamp
						} else { java.lang.Thread.sleep(2000) } // freq to check rate.						
					} // end while
				} // withConsumer
			} // withSessionFrom
    } // withConnection
  }
  
  /** Create burst of messages to a q */
  def testBurstMsg(qname : String = "ExampleQueue", n: Int = 100) {
    println("Started: " + new java.util.Date)
    
    jms.withConnection { conn =>				
    	// Allow user hit CTRL+C to bring down listener
    	var isRunning = true
    	Utils.shutdownHook {
    		println("Shutting down producer.")
    		isRunning = false
    		Thread.sleep(1000) // allow main thread to clean up work
    	}
    	
			jms.withSessionFrom(conn) { session =>
				var totalCount = 0
				var count = 0
				var t = Utils.ts
				
				val q = session.createQueue(qname)
				
				session.withProducer(q) { producer =>      
					var i = 0
					while (isRunning && i < n) {
						i += 1
						totalCount += 1
						count += 1
						
						// session.send(q, "test" + i + ", time=" + Utils.ts)
						val msg = session.createTextMsg("test" + i + ", time=" + Utils.ts)
						producer.send(msg)
						
						// print rate every 5 secs or 1000 msgs.
						if (i == n || (count > 0 && count % 1000 == 0) || Utils.ts - t > (5 * 1000)) {
							val tstamp = Utils.ts
							val elapse = tstamp - t
							val rate = if (elapse == 0) 0.0 else (count / (elapse / 1000.0))
							printf(new java.util.Date() + "> sent rate: %.2f msgs/sec, totalCount: %d\n", rate, totalCount)  
							count = 0
							t = tstamp
						}
					}					
					println(totalCount + " msgs sent.")  
				}
			}
    }
    println("Stopped: " + new java.util.Date)
  }
}

