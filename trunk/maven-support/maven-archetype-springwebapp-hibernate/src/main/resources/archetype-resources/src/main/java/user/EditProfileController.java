package ${groupId}.user;

import javasupport.servlet.ServletRequestHelper;
import javasupport.spring.webmvc.validation.FieldValidator;
import javasupport.spring.webmvc.validation.EmailValidatorFunction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.mvc.SimpleFormController;

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
        User loggedinUser = (User)ServletRequestHelper.getSession(request, userSessionKey);
        User user =  userDao.get(loggedinUser.getId());
        return user;
    }
}
