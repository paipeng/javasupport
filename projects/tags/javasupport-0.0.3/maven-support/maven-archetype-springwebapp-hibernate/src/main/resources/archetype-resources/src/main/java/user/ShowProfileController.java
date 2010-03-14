package ${groupId}.user;

import javasupport.servlet.ServletRequestHelper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Show logged in user only.
 * @author thebugslayer
 */
public class ShowProfileController extends ShowController {

	public ShowProfileController(){
		view = USER_SHOW_PROFILE_VIEW;
	}
	
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User loginUser = (User)ServletRequestHelper.getSession(request, userSessionKey);
        User user = userDao.get(loginUser.getId());
        
        return new ModelAndView(view, "user", user);
    }
}
