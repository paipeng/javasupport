package ${groupId}.user;

import javasupport.servlet.ServletRequestHelper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Show any user with id param. if not found assumed logged in.
 * @author thebugslayer
 */
public class ShowController extends AbstractController implements UserConstants {
    protected UserDao userDao;
    protected String userSessionKey = USER_SESSION_KEY;    
    protected String view = USER_SHOW_VIEW;
    
    public void setUserSessionKey(String userSessionKey) {
        this.userSessionKey = userSessionKey;
    }
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int id = ServletRequestHelper.getOptionalParameter(request, "id", 0);
        User user = null;
        
        if(id >0){
            user = userDao.get(id);
        }else{
            User loginUser = (User)ServletRequestHelper.getSession(request, userSessionKey);
            user = userDao.get(loginUser.getId());
        }
        
        return new ModelAndView(view, "user", user);
    }
}
