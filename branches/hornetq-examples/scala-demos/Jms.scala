import javax.jms._
import javax.naming._
import java.util.logging._

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
    Jms.withJms { jms =>
      val session = jms.session
      println("createMessage " + session.createMessage.getClass)
      println("createObjectMessage " + session.createObjectMessage.getClass)
      println("createTextMessage " + session.createTextMessage.getClass)
      println("createMapMessage " + session.createMapMessage.getClass)
      println("createStreamMessage " + session.createStreamMessage.getClass)
    }
  }
  
  def testTempQ {
    Jms.withJms { jms => jms.withTempQ { q => println("TempQ: " + q.getClass) } }
    Jms.withJms { jms => jms.withTempQ { q => println("TempQ: " + q.getClass) } }
  }
  
  def testSendToTempQ {
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
JmsTest.testSendToTempQ

