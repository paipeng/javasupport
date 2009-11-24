object SendText {
  def main(args: Array[String]) = {
    val Array(queue, msg)  = args    
    send(msg, queue)
  }
  
  import org.apache.activemq.ActiveMQConnectionFactory
  val connFactory = new ActiveMQConnectionFactory
  
  def send(msg: String, queue: String) = {
    import javax.jms._
    val conn = connFactory.createConnection
    val sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)
    val dest = sess.createQueue(queue)
    
    val producer = sess.createProducer(dest)
    val txtMsg = sess.createTextMessage
    txtMsg.setText(msg)
    producer.send(txtMsg)
    
    sess.close
    conn.close
  }
}
