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
 *
 * @author thebugslayer
 */
public class EditController extends SimpleFormController implements UserConstants {

    protected UserDao userDao;
    protected String userSessionKey = USER_SESSION_KEY;

    public void setUserSessionKey(String userSessionKey) {
        this.userSessionKey = userSessionKey;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public EditController() {
        setCommandClass(User.class);
        setCommandName("user");

        setSuccessView(USER_HOME_VIEW);
        setFormView(USER_EDIT_VIEW);
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command,
            BindException errors) throws Exception {
        
        User loggedInUser = (User)ServletRequestHelper.getSession(request, userSessionKey);
        
        User user = (User) command;
        logger.debug("Updating user info " + user);
        
        try {
            userDao.save(user);
            logger.info("User " + user.getUsername() + " updated.");
            
            if(!loggedInUser.getUsername().equals(user.getUsername())){
                logger.info("Username has changed. Updating user session object.");
                loggedInUser.setUsername(user.getUsername());
                ServletRequestHelper.setSession(request, userSessionKey, loggedInUser);
            }
        } catch (Exception e) {
            logger.error("Failed to edit user " + user, e);
            throw new ModelAndViewDefiningException(new ModelAndView("error", "message", e.getMessage()));
        }

        return super.onSubmit(request, response, command, errors);
    }

    
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        User loggedinUser = (User)ServletRequestHelper.getSession(request, userSessionKey);
        User user =  userDao.get(loggedinUser.getId());
        return user;
    }
        

    @Override
    protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws Exception {
        User user = (User) command;        
        
        FieldValidator validator = new FieldValidator(user, errors);
        
        validator.
                notBlank("username", "This field can not be blank.").
                notBlank("password", "This field can not be blank.");
        
        if(validator.hasErrors())
            return;
        
        validator.
                length("username", 3, 64, "Input must between %d to %d characters.").
                length("password", 3, 64, "Input must between %d to %d characters.").
                skipIfBlank("email").
                function("email", new EmailValidatorFunction("Invalid email format."));
    }
}
