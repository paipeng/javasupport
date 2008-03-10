package ${groupId}.user;

import ${groupId}.user.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Check for userSession object in http session scope.
 * 
 * @author thebugslayer
 *
 */
public class MemberOnlyInterceptor extends HandlerInterceptorAdapter implements UserConstants {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected String failedView = "redirect:/webapp/"+USER_LOGIN_VIEW;
	protected String userSessionKey = USER_SESSION_KEY;

    public void setFailedView(String failedView) {
        this.failedView = failedView;
    }
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		logger.debug("Checking logged in userSession");
        User user = (User) request.getSession().getAttribute(userSessionKey);
        
		if(user == null){
			logger.info("User session not found. reject request.");
			throw new ModelAndViewDefiningException(new ModelAndView(failedView));
		}
		return true;
	}
}
