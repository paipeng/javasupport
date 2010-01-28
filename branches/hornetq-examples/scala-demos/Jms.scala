import javax.jms._
import javax.naming._

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
  
  def withRichSessionFrom(conn : Connection)(func : RichSession => Unit) {      
    withSessionFrom(conn) { session => func(new RichSession(session)) }
  }  
  
  def withSession(func : Session => Unit) { 
    withConnection { conn => withSessionFrom(conn) { jmsSession => func(jmsSession) } }
  }
  def withRichSession(func : RichSession => Unit) {  
    withConnection { conn => withRichSessionFrom(conn) { richSession => func(richSession) } } 
  }
}

/** A companion object to Jms class that lookup and create JMS session */
object Jms {
  val DEFAULT_CONNECTION_FACTORY_NAME = "ConnectionFactory"
  
  def apply() = new Jms(getJndiConnectionFactory(DEFAULT_CONNECTION_FACTORY_NAME))
  
  /** Create a ConnectionFactory object from JNDI.
   */
  def getJndiConnectionFactory(name : String) = {
    lookupJndi[ConnectionFactory](name) 
  } 
  
  /** Convert and set a map values into a JMS message properties. */
  def addMsgProps[T](msg : Message, props : Map[String, T]) = {
    props.foreach { case (k,v) => msg.setObjectProperty(k, v) }
  } 
  
  /** Lookup object from JNDI by default in initial context. */
  def lookupJndi[T](name : String, jndiCtx : Context = new InitialContext) : T = {
    try { jndiCtx.lookup(name).asInstanceOf[T] } finally { jndiCtx.close }
  }
  
  def createMsgListener(func : Message => Unit) = new MessageListener { 
    def onMessage(msg : Message) = func(msg)
  }
}

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
  
  /** Get a instance of queue Destination from session. Some broker requires the
   * queue to be created (or setup during server startup) first. */
  def toQueue(name : String) = jmsSession.createQueue(name)
  
  /** Get a instance of topic Destination from session. Some broker requires the
   * topic to be created (or setup during server startup) first. */
  def toTopic(name : String) = jmsSession.createTopic(name)
  
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
      Jms.addMsgProps(msg, props)
      producer.send(msg) 
    }
  }
}

object JmsTest {  
  
  /** Let's see some message implementation class names. */
  def testSession {
    Jms().withRichSession { session =>      
      val jmsSession = session.jmsSession
      println("createTextMessage " + session.createTextMsg("foo").getClass)
      println("createMapMessage " + session.createMapMsg(Map("a"->"A", "b"->"B")).getClass)      
      println("createObjectMessage " + session.createObjectMsg(new java.util.Date).getClass)     
      println("createBytesMsg " + session.createBytesMsg(Array[Byte](0,1,2,3)).getClass)     
      
      val ins = new java.io.ByteArrayInputStream(Array[Byte](0,1,2,3))
      try { println("createBytesMsg_fromStream " + session.createBytesMsg(ins).getClass) } finally { ins.close }
      
      println("createMessage " + jmsSession.createMessage.getClass)
      println("createStreamMessage " + jmsSession.createStreamMessage.getClass)
    }
  }
  
  /** Note that we have create two temp queues under the same jms session. */
  def testTempQ {
    Jms().withRichSession { session => 
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
          
    Jms().withRichSession { session => 
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
  def testMsgListener {
    def process(msg : Message) = msg match {
      case m : TextMessage => 
        println("Received msg in msg listener: " + m.getText)
      case _ => 
        println("Received msg in msg listener: " + msg)
    }
    
    val jms = Jms()
    jms.withRichSession { session =>
      val q = session.toQueue("ExampleQueue")
      session.withConsumer(q) { consumer =>
        consumer.setMessageListener(Jms.createMsgListener { msg => process(msg) })        
        println("Listener started on " + q)
        println("wait for msg...")
        this.synchronized { this.wait }
      }
    }
  }
  
  def testBurstMsg(n: Int) {
    Jms().withRichSession { session => 
      val q = session.toQueue("ExampleQueue")
      (1 to n).foreach { i => 
        session.send(q, "test" + i + ", time=" + System.currentTimeMillis)
      }
      println(n + " msgs sent.")
    }
  }
}

