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
    IO.eachBytesBlock(input) { block => msg.writeBytes(block) }
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

object JmsTest {  
  import Jms.jmsToRichSession
  
  /** Let's see some message implementation class names. */
  def testSession {
    Jms.fromJndi().withSession { session =>
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
    Jms.fromJndi().withSession { session => 
      session.withTempQ { q => println("TempQ1: " + q.getClass) } 
      session.withTempQ { q => println("TempQ2: " + q.getClass) } 
    }
  }
  
  /** Let's create a temp q and try to send and receive msg on it. */
  def testSendToTempQ {
    def process(msg : Message) = msg match {
      case m : TextMessage => 
        println("Received msg from TempQ: " + m.getText + ", props['from'] " + m.getStringProperty("from"))
      case _ => 
        println("Received msg from TempQ: " + msg)
    }
          
    Jms.fromJndi().withSession { session => 
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
  
  /** Let's setup msg listener */
  def testMesssageListener(cf : ConnectionFactory) {
    println("Started: " + new java.util.Date)
    new Jms(cf).withSession { session =>
      val q = session.createQueue("ExampleQueue")
      session.withConsumer(q) { consumer =>
        var totalCount = 0
        var count = 0
        val listener = Jms.createMessageListener { msg => 
          totalCount += 1
          count += 1  
        }
        consumer.setMessageListener(listener)        
        println("Listener started on " + q)
        println("wait for msg...")        
        
        // print rate every 5 secs or 1000 msgs.
        var t = System.currentTimeMillis
        while (true) {
          if ((count > 0 && count % 1000 == 0) || System.currentTimeMillis - t > (5 * 1000)) {
            val startT = t
            t = System.currentTimeMillis
            val rate = count / ((t - startT) / 1000.0)
          printf(new java.util.Date() + "> rate: %.2f msgs / sec, totalCount: %d\n", rate, totalCount)  
            count = 0  
          } else { java.lang.Thread.sleep(3000) }          
        }
      }
    }
  }
  
  def testBurstMsg(n: Int, cf : ConnectionFactory) {
    println("Started: " + new java.util.Date)
    new Jms(cf).withSession { session => 
      var totalCount = 0
      var count = 0
      var t = System.currentTimeMillis
      
      val q = session.createQueue("ExampleQueue")
      
      session.withProducer(q) { producer =>         
        (1 to n).foreach { i => 
          totalCount += 1
          count += 1
          
          // session.send(q, "test" + i + ", time=" + System.currentTimeMillis)
          val msg = session.createTextMsg("test" + i + ", time=" + System.currentTimeMillis)
          producer.send(msg)
          
          // print rate every 5 secs or 1000 msgs.
          if (i == n || (count > 0 && count % 1000 == 0) || System.currentTimeMillis - t > (5 * 1000)) {
            val startT = t
            t = System.currentTimeMillis
            val rate = count / ((t - startT) / 1000.0)
            printf(new java.util.Date() + "> rate: %.2f msgs / sec, totalCount: %d\n", rate, totalCount)  
            count = 0
          }
        }
      }
      println(totalCount + " msgs sent.")  
    }
    println("Stopped: " + new java.util.Date)
  }
}

