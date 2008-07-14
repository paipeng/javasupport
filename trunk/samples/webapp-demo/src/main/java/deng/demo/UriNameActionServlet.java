package deng.demo;

import javax.servlet.http.HttpServletRequest;

public class UriNameActionServlet extends ActionServlet {	
	protected String handleAction(HttpServletRequest req, String actionName) {
		return actionName;
	}
}
