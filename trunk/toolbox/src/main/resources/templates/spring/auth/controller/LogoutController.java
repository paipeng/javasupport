package ${packageName}.auth;

import javasupport.servlet.ServletRequestHelper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import ${packageName}.user.User;

/**
 *
 * @author zemian
 */
public class LogoutController extends AbstractController implements UserConstants{
    private String userSessionKey = USER_SESSION_KEY;
    private String logoutView = USER_LOGOUT_VIEW;

    public void setLogoutView(String logoutView) {
        this.logoutView = logoutView;
    }    
    
    public void setUserSessionKey(String userSessionKey) {
        this.userSessionKey = userSessionKey;
    }
    
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("Attempt to get userSession object to signout.");
        User user = (User)ServletRequestHelper.getSession(request, userSessionKey);
        request.getSession().removeAttribute(userSessionKey);
        logger.info("User " + user.getUsername() + " session has been destroyed.");
        
        return new ModelAndView(logoutView, "user", user); //give a view a change to dipslay username info.
    }
}
