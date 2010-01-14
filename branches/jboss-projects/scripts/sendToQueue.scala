
//Zemian@Zemian-PC /jbs
//$ mkcpjb '/apps/hornetq-2.0.0.GA/lib/*'
//export CLASSPATH="C:\apps\jboss-5.1.0.GA\client\*;C:\apps\common\lib\*;target\dependency\*;target\classes;.\;C:\apps\hornetq-2.0.0.GA\lib\*"

/*
Zemian@Zemian-PC /jbs
$ scalac -cp $CLASSPATH -d "target\classes" sendToQueue.scala 

Zemian@Zemian-PC /jbs
$ scala -cp $CLASSPATH SendToQueue ExampleQueue "just a test"
$ scala -cp $CLASSPATH SendToQueue ExampleQueue --file "C:\cygwin\tmp\test-input.txt"

*/

object SendToQueue {

	import javax.naming._
	import javax.jms._
	
	// Listing a context names binding
	def send(qname : String, text : String) {
		val ctx = new InitialContext()
		val cf = ctx.lookup("ConnectionFactory").asInstanceOf[ConnectionFactory]
		val conn = cf.createConnection()
		val session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)
		val queue = session.createQueue(qname)
		val producer = session.createProducer(queue)
		val msg = session.createTextMessage(text)
		
		producer.send(msg)
		
		session.close
		conn.close
	}
	
	def readFile(fname : String) = scala.io.Source.fromString(fname).getLines().mkString

	def main(args : Array[String]) {
		val (opts, args2) = args.partition { e => e.startsWith("--") }
		val qname = args2(0)
		val text = if (opts.exists { e => e == "--file" }) readFile(args2(1)) else args2(1)
		send(qname, text)
		println("msg sent.")
		println(text)
	}
}
