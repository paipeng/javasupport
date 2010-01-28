import javax.jms._
import javax.naming._

/** A facade class that let user to quickly access JMS resources and process
 * messages in Queue or Topic. This class will take a opened session that
 * is ready for use (eg its connection should already be started.)
 *
 * Use Jms#withJms to process any func (closure) that will have a Jms instance 
 * created for you as parameter, and it contains a opened session ready for use. 
 * The session is created by connection from a JNDI ConnectionFactory lookup. The 
 * Jms#withJms will auto clean up the session after the method is completed. 
 * Many helper methods in Jms class will use this opened session for further 
 * processing, so that you dont' have to re-open new session.
 *
 * See JmsTest for more usage and examples.
 */
class Jms(val session : Session) {
  
  /** Create a temporary queue for processing. 
   * The queue will be deleted after func is complete. */
  def withTempQ(func : TemporaryQueue => Unit) {    
    val queue = session.createTemporaryQueue
    try { func(queue) } finally { queue.delete }
  }  
  
  /** Create a message consumer and close it after func processing. 
   * You may use #toQueue or #toTopic to convert string into Destination object. */ 
  def withConsumer(dest : Destination)(func : MessageConsumer => Unit) {
    val consumer = session.createConsumer(dest)
    try { func(consumer) } finally { consumer.close }
  }  
  
  /** Create a message producer and close it after func processing. 
   * You may use #toQueue or #toTopic to convert string into Destination object. */ 
  def withProducer(dest : Destination)(func : MessageProducer => Unit) {
    val producer = session.createProducer(dest)
    try { func(producer) } finally { producer.close }
  }
  
  /** Get a instance of queue Destination from session. Some broker requires the
   * queue to be created (or setup during server startup) first. */
  def toQueue(name : String) = session.createQueue(name)
  
  /** Get a instance of topic Destination from session. Some broker requires the
   * topic to be created (or setup during server startup) first. */
  def toTopic(name : String) = session.createTopic(name)
  
  /** Convert and set a map values into a JMS message properties. */
  def addMsgProps[T](msg : Message, props : Map[String, T]) = {
    props.foreach { case (k,v) => msg.setObjectProperty(k, v) }
  } 
  
  /** Wrapper to session create message call */
  def createTextMsg(text : String) = session.createTextMessage(text)
    
  /** Wrapper to session create message call */
  def createMapMsg[T](map : Map[String, T]) = {
    val msg = session.createMapMessage
    map.foreach { case (k,v) => msg.setObject(k, v) }
    msg
  }  
  
  /** Wrapper to session create message call */
  def createBytesMsg(data : Array[Byte]) = {
    val msg = session.createBytesMessage
    msg.writeBytes(data)
    msg
  }   
  
  /** Wrapper to session create message call */
  def createBytesMsg(input : java.io.InputStream) = {
    val msg = session.createBytesMessage
    IO.eachBytesBlock(input) { block => msg.writeBytes(block) }
    msg
  }
  
  /** Wrapper to session create message call */
  def createObjectMsg(obj : java.io.Serializable) = {
    val msg = session.createObjectMessage
    msg.setObject(obj)
    msg
  }
  
  /** Send a text messgage into a destination. It will optionally take a map
   * and convert and set them into the message proeperties. */
  def send(dest : Destination, text : String, props : Map[String, String] = Map()) {
    withProducer(dest) { producer =>
      val msg = createTextMsg(text)
      addMsgProps(msg, props)
      producer.send(msg) 
    }
  }
}

/** A companion object to Jms class that lookup and create JMS session */
object Jms {
  val DEFAULT_CONNECTION_FACTORY_NAME = "ConnectionFactory"
  
  def withJms(func : Jms => Unit) {
    withConnFactJms(getJndiConnFact())(func)
  }
  
  def withConnFactJms(connFact : ConnectionFactory)(func : Jms => Unit) {
    val conn = connFact.createConnection
    try { 
      withSession(conn) { session => func(new Jms(session)) }
    } finally { conn.close }
  }
   
  /** Create a ConnectionFactory object from JNDI.
   */
  def getJndiConnFact(name : String = DEFAULT_CONNECTION_FACTORY_NAME) = {
    lookupJndi[ConnectionFactory](name) 
  }
  
  /** Create new instance of JMS session from the connection. It will start
   * the connection object and stop it after func is fished. session will also
   * be close after func finished. */
  def withSession(conn : Connection)(func : Session => Unit) {      
    val session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)      
    try {
      conn.start
      func(session)
    } finally {
      session.close
      conn.stop
    }
  }  
  
  /** Lookup object from JNDI by default in initial context. */
  def lookupJndi[T](name : String, jndiCtx : Context = new InitialContext) : T = {
    try { jndiCtx.lookup(name).asInstanceOf[T] } finally { jndiCtx.close }
  }
  
  
  /** A process holder for message listener instance that setup by #withMsgListener */
  class ListenerProc(val conn : Connection, val session : Session, val consumer : MessageConsumer)
  
  /** Create and setup an instance of MessageListener and delegate process to func.
   * The return object will contain the JMS connection, session, and the MessageListener
   * implementation intance. These can be use to stop and end the listener process. */
  def createListenerProc(connFact : ConnectionFactory, dest : Destination)(func : Message => Unit) : ListenerProc = {
    val conn = connFact.createConnection
    try {
      val session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)
      val consumer = session.createConsumer(dest)
      val msgListener = new MessageListener() {
        def onMessage(msg : Message) = func(msg)
      }
      consumer.setMessageListener(msgListener)      
      conn.start
      
      // return new proc instance.
      new ListenerProc(conn, session, consumer)  
    } catch {
      case e : Exception => { 
        conn.close // close out connection if there are errors.
        throw e
      }
    }
  }
}

object JmsTest {  
  
  /** Let's see some message implementation class names. */
  def testSession {
    Jms.withJms { jms =>      
      val session = jms.session
      println("createTextMessage " + jms.createTextMsg("foo").getClass)
      println("createMapMessage " + jms.createMapMsg(Map("a"->"A", "b"->"B")).getClass)      
      println("createObjectMessage " + jms.createObjectMsg(new java.util.Date).getClass)     
      println("createBytesMsg " + jms.createBytesMsg(Array[Byte](0,1,2,3)).getClass)     
      
      val ins = new java.io.ByteArrayInputStream(Array[Byte](0,1,2,3))
      try { println("createBytesMsg_fromStream " + jms.createBytesMsg(ins).getClass) } finally { ins.close }
      
      println("createMessage " + session.createMessage.getClass)
      println("createStreamMessage " + session.createStreamMessage.getClass)
    }
  }
  
  /** Note that we have create two temp queues under the same jms session. */
  def testTempQ {
    Jms.withJms { jms => 
      jms.withTempQ { q => println("TempQ1: " + q.getClass) } 
      jms.withTempQ { q => println("TempQ2: " + q.getClass) } 
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
          
    Jms.withJms { jms => 
      jms.withTempQ { q => 
        println("Created TempQ: " + q.getClass) 
        jms.send(q, "test msg")
        jms.send(q, "test2 msg", Map("from" -> "Zemian"))
        println("2 msgs sent to TempQ. Will try to receive it now...")
        jms.withConsumer(q) { consumer => 
          process(consumer.receive)
          process(consumer.receive)          
        }
      } 
    }
  }
  
  /** Let's setup msg listener */
  def testMsgListener {
    Jms.withJms { jms =>
      jms.withTempQ { tempQ => 
        val connFact = Jms.getJndiConnFact
        val proc = Jms.createListenerProc(connFact, tempQ)
      }
    }
  }
}

