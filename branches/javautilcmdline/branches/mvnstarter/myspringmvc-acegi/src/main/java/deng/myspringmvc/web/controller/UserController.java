package deng.myspringmvc.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * The default MethodNameResolver is InternalPathMethodNameResolver.
 * 
 * @author zemian
 *
 */
public class UserController extends MultiActionController {
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("user/index");
		return mv;
	}
}
