object DrainQueue {
  def main(args: Array[String]) = {
    val Array(queue)  = args    
    drain(queue)
  }
  
  import org.apache.activemq.ActiveMQConnectionFactory
  val connFactory = new ActiveMQConnectionFactory
  
  def drain(queue: String) = {
    import javax.jms._
    val conn = connFactory.createConnection
    val sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)
    val dest = sess.createQueue(queue)
    val consumer = sess.createConsumer(dest)
    
    import javax.jms._
    var count = 0
    consumer.setMessageListener(new MessageListener(){
      def onMessage(msg: Message){
        count += 1
        //println(msg)
        println(msg.getJMSMessageID + " >>> text=" + msg.asInstanceOf[TextMessage].getText)
        println(msg.getJMSMessageID + " >>> type=" + msg.getIntProperty("type"))
      }
    });
    
    val rt = Runtime.getRuntime
    rt.addShutdownHook(new Thread(new Runnable{
      def run{
        println("Stopping JMS consumer.")
        sess.close
        conn.close
        println(count + " messages processed.")
      }
    }));
    
    conn.start
    while(true) Thread.sleep(1000)
  }
}
