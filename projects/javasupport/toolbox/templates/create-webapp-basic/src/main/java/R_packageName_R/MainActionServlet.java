package ${project.packageName};

import javasupport.servlet.ActionServlet;

import javax.servlet.http.HttpServletRequest;

public class MainActionServlet extends ActionServlet {
	
	protected String handleAction(HttpServletRequest req, String actionName) {
		if(actionName.equals("/index")){		
			return "/index";
		}else{
			throw new RuntimeException("Unknown action "+actionName);
		}	
	}
}
