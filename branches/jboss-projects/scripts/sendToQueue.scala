/*
Zemian@Zemian-PC /jbs
$ mkcpjb '/apps/hornetq-2.0.0.GA/lib/*'
export CLASSPATH="C:\apps\jboss-5.1.0.GA\client\*;C:\apps\common\lib\*;target\dependency\*;target\classes;.\;C:\apps\hornetq-2.0.0.GA\lib\*"

Zemian@Zemian-PC /jbs
$ scalac -cp $CLASSPATH -d "target\classes" sendTextToQueue.scala 

Zemian@Zemian-PC /jbs
$ scala -cp $CLASSPATH SendToQueue ExampleQueue "just a test"
*/

object SendToQueue {

	import javax.naming._
	import javax.jms._
	
	// Listing a context names binding
	def send(queueName: String, text: String) {
		val ctx = new InitialContext()
		val cf = ctx.lookup("ConnectionFactory").asInstanceOf[ConnectionFactory]
		val conn = cf.createConnection()
		val session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)
		
		val msg = session.createTextMessage(text)
		val queue = session.createQueue(queueName)
		val producer = session.createProducer(queue)
		producer.send(msg)
		
		session.close
		conn.close
	}

	def main(args : Array[String]){ 
		send(args(0), args(1))
		println("msg sent.")
	}
}
