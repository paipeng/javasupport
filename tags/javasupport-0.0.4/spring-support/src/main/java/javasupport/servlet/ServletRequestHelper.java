/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javasupport.servlet;

import javax.servlet.http.HttpServletRequest;

/**
 * Utilty to extract data from request's parameter, attribute, or session scope.
 * We typically store all String convertible values into request's parameters, and therefore
 * methods here related to parameter are used to covert from string to basic java types. 
 * 
 * While request's attribute and session scope are usually store Objects, therefore methods here
 * provide convertion between Objects/Wrapper into java basic types.
 * 
 * @author zemian
 *
 */
public class ServletRequestHelper {	
	/**
	 * Return base URL portion of original request, up to end of contextPath without slash.
	 * @param request
	 */
	public static String getBaseUrl(HttpServletRequest request){
		StringBuffer url = request.getRequestURL();
		String contextPath = request.getContextPath();
		return url.substring(0, url.indexOf(contextPath) + contextPath.length());
	}
	
	/** 
	 * Search all 3 scopes(request.parameters, request, sesion) and return obj 
	 * value if found, else return the def. Only String type is valid search
	 * with parameter
	 * 
	 * @param request
	 * @param name
	 * @param def
	 */
	@SuppressWarnings("unchecked")
	public static <T> T searchScope(HttpServletRequest request, String name, T def){
		String param = request.getParameter(name);
		if(param != null){
			return (T)param;
		}
		
		//Search request attribute
		T value = (T)request.getAttribute(name);
		if(value != null){
			return value;
		}
		
		//Search session attribute
		value = (T)request.getSession().getAttribute(name);
		if(value != null){
			return value;
		}
		
		return def;
	}
		
	/**
	 * Get a required String parameter.
	 * @param request
	 * @param name
	 */
	public static String getRequiredParameter(HttpServletRequest request, String name){
		String value = request.getParameter(name);
		if(value == null)
			throw new IllegalArgumentException("Required parameter "+name+" not found.");
		return value;
	}
	public static int getRequiredIntParameter(HttpServletRequest request, String name){
		return Integer.parseInt(getRequiredParameter(request, name));
	}
	
	/**
	 * Get a optional String parameter.
	 * @param request
	 * @param name
	 * @param def
	 */
	public static String getOptionalParameter(HttpServletRequest request, String name, String def){
		String value = request.getParameter(name);
		if(value == null)
			value = def;
		return value;
	}	
	public static int getOptionalParameter(HttpServletRequest request, String name, int def){
		return Integer.parseInt(getOptionalParameter(request, name, String.valueOf(def)));
	}
		
	/**
	 * Set a value into session scope.
	 * @param request
	 * @param name
	 * @param value
	 */
	public static <T> void setSession(HttpServletRequest request, String name, T value){
		request.getSession().setAttribute(name, value);
	}
		
	/**
	 * Get required Object from session.
	 * @param request
	 * @param name
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getSession(HttpServletRequest request, String name){
		return (T)request.getSession().getAttribute(name);
	}		
}
