package ${groupId}.user;

import javasupport.servlet.ServletRequestHelper;
import javasupport.spring.webmvc.validation.FieldValidator;
import javasupport.spring.webmvc.validation.EmailValidatorFunction;
import javasupport.spring.webmvc.validation.ValidatorFunction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 *
 * @author thebugslayer
 */
public class RegisterController extends SimpleFormController implements UserConstants {

    protected UserDao userDao;
    protected String userSessionKey = USER_SESSION_KEY;

    public void setUserSessionKey(String userSessionKey) {
        this.userSessionKey = userSessionKey;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public RegisterController() {
        setCommandClass(User.class);
        setCommandName("user");

        setSuccessView(USER_HOME_VIEW);
        setFormView(USER_REGISTER_VIEW);
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command,
            BindException errors) throws Exception {
        User user = (User) command;
        try {
            userDao.save(user);
            logger.info("New user created: " + user.getUsername());

            //saving a user instance as userSession token
            User userSession = new User();
            userSession.setId(user.getId());
            userSession.setUsername(user.getUsername());
            ServletRequestHelper.setSession(request, userSessionKey, userSession);
            logger.debug("User is saved into session with " + userSessionKey);
        } catch (Exception e) {
            logger.error("Failed to create new user " + user, e);
            throw new ModelAndViewDefiningException(new ModelAndView("error", "message", e.getMessage()));
        }

        return super.onSubmit(request, response, command, errors);
    }

    @Override
    protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws Exception {
        User user = (User) command;
        
        new FieldValidator(user, errors).
                notBlank("username", "This field can not be blank.").
                notBlank("password", "This field can not be blank.").
                skipIfHasError().
                function("username", new ExistingUserFunction()).
                length("username", 3, 64, "Input must between %d to %d characters.").
                length("password", 3, 64, "Input must between %d to %d characters.").
                skipIfBlank("email").
                function("email", new EmailValidatorFunction("Invalid email format."));
    }
    
    private class ExistingUserFunction implements ValidatorFunction {  
        public void apply(String fieldName, Object fieldValue, Errors errors) {
            String username = (String)fieldValue;
            if(userDao.exists(username)){
                errors.rejectValue(fieldName, "invalid.username", "Username already exists.");
            }
        }
    }
}
