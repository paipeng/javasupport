// scala sendTextToQueue ExampleQueue "Just a test"

import javax.naming._
import javax.jms._

// Listing a context names binding
def send(queueName: String, text: String) {
	val ctx = new InitialContext()
	val cf = ctx.lookup("ConnectionFactory").asInstanceOf[ConnectionFactory]
	val conn = cf.createConnection()
	val session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)
	
	try {
		val msg = session.createTextMessage(text)
		val queue = session.createQueue(queueName)
		val producer = session.createProducer(queue)
		producer.send(msg)
	} catch {
		case e: Exception => e.printStackTrace
	} finally {
		conn.close
		session.close
	}
}
send(args(0), args(1))
println("msg sent.")
