package ${groupId};

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	
public class WelcomeServlet extends HttpServlet {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logger.debug("Received request for message");
		Date serverTime = new Date();
		req.setAttribute("message", "Hello guess, the current server time is: " + serverTime);
		req.getRequestDispatcher("welcome.jsp").forward(req, resp);
	}	
}
