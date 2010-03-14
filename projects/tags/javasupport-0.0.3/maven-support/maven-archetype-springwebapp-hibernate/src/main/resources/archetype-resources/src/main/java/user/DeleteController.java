package ${groupId}.user;

import javasupport.servlet.ServletRequestHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/** Delete Controller for User. */
public class DeleteController extends AbstractController implements UserConstants {
	private UserDao userDao;

    protected String userSessionKey = USER_SESSION_KEY;

    public void setUserSessionKey(String userSessionKey) {
        this.userSessionKey = userSessionKey;
    }
    
	public void setUserDao(UserDao that) {
		this.userDao = that;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int id = ServletRequestHelper.getRequiredIntParameter(request, "id");
		String confirm = ServletRequestHelper.getOptionalParameter(request, "confirm", "no");
		if("no".equals(confirm)){
			ModelAndView mv = new ModelAndView(USER_DELETE_VIEW);
			mv.addObject("confirm", confirm);
			mv.addObject("id", id);
			return mv;
		}
		
		User user = userDao.delete(id);		
        
        //do we force out loggedin user?
        User loggedInUser = (User)ServletRequestHelper.getSession(request, userSessionKey);
        if(loggedInUser.getId() == user.getId()){
            request.getSession().removeAttribute(userSessionKey);
            logger.info("User " + user.getUsername() + " session has been destroyed.");
        }
        
		return new ModelAndView(USER_DELETE_VIEW, "user", user);
	}
}