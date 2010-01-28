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
  
  def toQueue(qname : String) = session.createQueue(qname)
  
  def createMsg(text : String) = session.createTextMessage(text)
  
  def send(dest : Destination, text : String) {
    withProducer(dest) { producer => producer.send(createMsg(text)) }
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
      println("1. createMessage " + session.createMessage.getClass)
      println("2. createObjectMessage " + session.createObjectMessage.getClass)
      println("3. createBytesMessage " + session.createBytesMessage.getClass)
      println("4. createTextMessage " + session.createTextMessage.getClass)
      println("5. createMapMessage " + session.createMapMessage.getClass)
      println("6. createStreamMessage " + session.createStreamMessage.getClass)
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

