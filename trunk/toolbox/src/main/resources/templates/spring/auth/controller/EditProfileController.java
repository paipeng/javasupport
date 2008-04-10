package ${packageName}.auth;

import javasupport.servlet.ServletRequestHelper;

import javax.servlet.http.HttpServletRequest;

import ${packageName}.user.EditController;
import ${packageName}.user.User;

/**
 * Eddit logged in user only.
 * @author thebugslayer
 */
public class EditProfileController extends EditController {
	public EditProfileController() {
        setFormView(UserConstants.USER_EDIT_PROFILE_VIEW);
    }
	
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        User loggedinUser = (User)ServletRequestHelper.getSession(request, UserConstants.USER_SESSION_KEY);
        User user =  userDao.get(loggedinUser.getId());
        return user;
    }
}
