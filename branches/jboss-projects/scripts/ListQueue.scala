object ListQueue {

	import javax.naming._
	import javax.jms._
	
	// Listing a context names binding
	def list(qname : String) {
		val ctx = new InitialContext()
		val cf = ctx.lookup("ConnectionFactory").asInstanceOf[ConnectionFactory]
		val conn = cf.createConnection()
		
		try {
			val session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)		
			val queue = session.createQueue(qname)
			val browser = session.createBrowser(queue)
			val enume = browser.getEnumeration
			var counter = 0
			while (enume.hasMoreElements) {
				val msg = enume.nextElement.asInstanceOf[TextMessage]
				println("=== Message#" + counter + " ===")
				println(msg.getText)
				counter += 1
			}			
			session.close
		} finally {
			conn.close
		}
	}
	
	def main(args : Array[String]) { list(args(0)) }
}
