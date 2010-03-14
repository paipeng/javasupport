package ${groupId}.user;

import javasupport.servlet.ServletRequestHelper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 *
 * @author thebugslayer
 */
public class ProfileController extends AbstractController implements UserConstants {
    protected UserDao userDao;
    private String userSessionKey = USER_SESSION_KEY;    
    private String profileView = USER_HOME_VIEW;
    
    public void setUserSessionKey(String userSessionKey) {
        this.userSessionKey = userSessionKey;
    }
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User loginUser = (User)ServletRequestHelper.getSession(request, userSessionKey);
        User fullUser = userDao.get(loginUser.getId());
        return new ModelAndView(profileView, "user", fullUser);
    }
}
