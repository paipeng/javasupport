package ${packageName}.auth;

import javasupport.servlet.ServletRequestHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import ${packageName}.user.ShowController;
import ${packageName}.user.User;

/**
 * Show logged in user only.
 * @author thebugslayer
 */
public class ShowProfileController extends ShowController {
	
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User loginUser = (User)ServletRequestHelper.getSession(request, UserConstants.USER_SESSION_KEY);
        User user = userDao.get(loginUser.getId());
        
        return new ModelAndView(UserConstants.USER_SHOW_PROFILE_VIEW, "user", user);
    }
}
