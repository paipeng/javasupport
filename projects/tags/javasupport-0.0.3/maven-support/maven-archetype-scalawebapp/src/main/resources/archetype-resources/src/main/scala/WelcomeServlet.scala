package ${groupId}

import java.io.IOException
import java.util.Date

import javax.servlet.ServletException
import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}
import org.slf4j.{Logger, LoggerFactory}
	
class WelcomeServlet extends HttpServlet {
	val logger = LoggerFactory.getLogger(this.getClass)
	
	override def doGet(req:HttpServletRequest, resp:HttpServletResponse):Unit = {
		logger.debug("Received request for message")
		val serverTime = new Date()
		req.setAttribute("message", "Hello guess, the current server time is: " + serverTime)
		req.getRequestDispatcher("welcome.jsp").forward(req, resp)
	}	
}
