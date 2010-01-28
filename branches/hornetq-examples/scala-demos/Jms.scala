import javax.jms._
import javax.naming._

/** A facade class that let user to quickly access JMS resources. 
 * This class assume to be given a session is opened (its connection started)
 * End user needs to close session as after usage of this class.
 *
 * See Jms#withJms for auto create a Jms instance with one time opened session
 * for action processing.
 *
 * See JmsTest for usage examples.
 */
class Jms(val session : Session) {
  def withTempQ(action : TemporaryQueue => Unit) {    
    val queue = session.createTemporaryQueue
    try { action(queue) } finally { queue.delete }
  }  
  def withConsumer(dest : Destination)(action : MessageConsumer => Unit) {
    val consumer = session.createConsumer(dest)
    try { action(consumer) } finally { consumer.close }
  }  
  def withProducer(dest : Destination)(action : MessageProducer => Unit) {
    val producer = session.createProducer(dest)
    try { action(producer) } finally { producer.close }
  }
  
  def toQueue(name : String) = session.createQueue(name)
  def toTopic(name : String) = session.createTopic(name)
  
  def createTextMsg(text : String) = session.createTextMessage(text)  
  
  def createMapMsg[T](map : Map[String, T]) = {
    val msg = session.createMapMessage
    map.foreach { case (k,v) => msg.setObject(k, v) }
    msg
  }  
  def createBytesMsg(data : Array[Byte]) = {
    val msg = session.createBytesMessage
    msg.writeBytes(data)
    msg
  }   
  def createBytesMsg(input : java.io.InputStream) = {
    val msg = session.createBytesMessage
    val BUFF_SIZE = 1024 * 8
    var len = -1
    var data = new Array[Byte](BUFF_SIZE)
    while ({ len = input.read(data, 0, BUFF_SIZE); len != -1 }) {
      msg.writeBytes(data, 0, len)
    }
    msg
  }
  def createObjectMsg(obj : java.io.Serializable) = {
    val msg = session.createObjectMessage
    msg.setObject(obj)
    msg
  }
  
  def send(dest : Destination, text : String) {
    withProducer(dest) { producer => producer.send(createTextMsg(text)) }
  }
}

/** A companion object to Jms class that lookup and create JMS session */
object Jms { 
  def withJms(action : Jms => Unit) {
    withJndiJmsSession("ConnectionFactory") { session => action(new Jms(session)) }  
  }
  
  def withJndiJmsSession(connFactoryName : String)(action : Session => Unit) {
    val ctx = new InitialContext
    try {
      val cf = ctx.lookup(connFactoryName).asInstanceOf[ConnectionFactory]
      val conn = cf.createConnection
      try {
        val session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)
        try {
          conn.start
          action(session)
        } finally {
          session.close
        }
      } finally {
        conn.stop
        conn.close
      }
    } finally { 
      ctx.close  
    }
  }  
}

object JmsTest {  
  
  def testSession {
    // Let's see some message implementation class names.
    Jms.withJms { jms =>      
      val session = jms.session
      println("createTextMessage " + jms.createTextMsg("foo").getClass)
      println("createMapMessage " + jms.createMapMsg(Map("a"->"A", "b"->"B")).getClass)      
      println("createObjectMessage " + jms.createObjectMsg(new java.util.Date).getClass)     
      println("createBytesMsg " + jms.createBytesMsg(Array[Byte](0,1,2,3)).getClass)     
      println("createBytesMsg_fromStream " + jms.createBytesMsg(new java.io.ByteArrayInputStream(Array[Byte](0,1,2,3))).getClass)
      
      println("createMessage " + session.createMessage.getClass)
      println("createStreamMessage " + session.createStreamMessage.getClass)
    }
  }
  
  def testTempQ {
    // Note that we have create two temp queues under the same jms session.
    Jms.withJms { jms => 
      jms.withTempQ { q => println("TempQ1: " + q.getClass) } 
      jms.withTempQ { q => println("TempQ2: " + q.getClass) } 
    }
  }
  
  def testSendToTempQ {
    // Let's create a temp q and try to send and receive msg on it.
    Jms.withJms { jms => 
      jms.withTempQ { q => 
        println("Created TempQ: " + q.getClass) 
        jms.send(q, "test msg")
        println("Msg sent to TempQ. Will try to receive it now...")
        jms.withConsumer(q) { consumer => 
          val msg = consumer.receive
          msg match {
            case m : TextMessage => 
              println("Received msg from TempQ: " + m.getText)
            case _ => 
              println("Received msg from TempQ: " + msg)
          }
        }
      } 
    }
  }
}

// Run as script (first start hornetq server)
// mkcp '/apps/hornetq/lib/*' './'
// scala Jms.scala                
JmsTest.testSession        
JmsTest.testTempQ
JmsTest.testSendToTempQ

