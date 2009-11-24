object GenerateTextMsg {
  def main(args: Array[String]) = {
    val Array(queue, count)  = args    
    generate(queue, count.toInt)
  }
  
  import org.apache.activemq.ActiveMQConnectionFactory
  val connFactory = new ActiveMQConnectionFactory
  
  def generate(queue: String, count: Int) = {
    import javax.jms._
    val conn = connFactory.createConnection
    val sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)
    val dest = sess.createQueue(queue)
    
    val producer = sess.createProducer(dest)
    val rand = new java.util.Random
    for(i <- 1 to count){
      val typeNum = rand.nextInt(java.lang.Integer.MAX_VALUE) % 2
      val msg = "Test msg " + i + " with type=" + typeNum
      val txtMsg = sess.createTextMessage
      txtMsg.setText(msg)
      txtMsg.setIntProperty("type", typeNum)
      println("Sending text msg " + i + " typeNum= " + typeNum)
      producer.send(txtMsg)
    }
    
    sess.close
    conn.close
  }
}
