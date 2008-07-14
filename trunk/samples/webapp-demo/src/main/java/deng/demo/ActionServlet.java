package deng.demo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** A base class for Action based Controller Servlet. 
 * 
 *  <p>Subclass just need to handler actionName that comes from URI parsed right
 *  after this servlet/controller name. So in your web.xml the url-mapping of
 *  an instance of this subclass will be the controllerName follow by
 *  any actionName.</p>
 *  
 *  <p>For example: <code>http://localhost:8080/context-name/controller-name/action-name</code></p>
 *  
 */
public abstract class ActionServlet extends HttpServlet {
	protected String viewPrefix = "/view";
	protected String viewSuffix = ".jsp";
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String urlPath = req.getRequestURI();
		String ctxName = req.getContextPath();
		log("Request urlPath " + urlPath);
		
		//Parse dispatcher(this) servlet path and name
		String controllerName = req.getServletPath();		
		int controllerPos = (ctxName+controllerName).length();
		String actionName = urlPath.substring(controllerPos);
		if(actionName.equals("") || actionName.equals("/")){
			actionName = "/index";
		}
		//log("Controller name "+ controllerName);	
		log("Action name "+ actionName);		
				
		String viewName = handleAction(req, resp, controllerName, actionName);
		//log("View name "+viewName);
		renderView(req, resp, controllerName, viewName);
	}	
	
	protected void renderView(HttpServletRequest req, HttpServletResponse resp, String controllerName, String viewName) {
		String viewFilename = viewPrefix+controllerName+viewName+viewSuffix;
		log("Rendering viewFilename " + viewFilename);
		try {
			req.getRequestDispatcher(viewFilename).forward(req, resp);
		}
		catch (Exception e) {
			throw new RuntimeException("Unable to render view " + viewFilename);
		}
	}
	
	protected String handleAction(HttpServletRequest req, HttpServletResponse resp, String controllerName, String actionName){
		return handleAction(req, actionName);
	}

	protected abstract String handleAction(HttpServletRequest req, String actionName);
}
