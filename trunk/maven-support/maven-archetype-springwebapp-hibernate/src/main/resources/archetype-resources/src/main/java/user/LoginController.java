package ${groupId}.user;

import javasupport.servlet.ServletRequestHelper;
import javasupport.spring.webmvc.validation.FieldValidator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 *
 * @author thebugslayer
 */
public class LoginController extends SimpleFormController implements UserConstants {

    protected UserDao userDao;
    protected String userSessionKey = USER_SESSION_KEY;

    public void setUserSessionKey(String userSessionKey) {
        this.userSessionKey = userSessionKey;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public LoginController() {
        setCommandClass(User.class);
        setCommandName("user");

        setSuccessView("redirect:/webapp/"+USER_SHOW_PROFILE_VIEW);
        setFormView(USER_LOGIN_VIEW);
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command,
            BindException errors) throws Exception {
        User user = (User) command;
        try {
            User validUser = userDao.authenticate(user.getUsername(), user.getPassword());
            logger.info("User authenticated: " + validUser.getUsername());

            //saving a user instance as userSession token
            User userSession = new User();
            userSession.setId(validUser.getId());
            userSession.setUsername(validUser.getUsername());
            ServletRequestHelper.setSession(request, userSessionKey, userSession);
            logger.debug("User is saved into session with " + userSessionKey);
        } catch (UsernameNotFoundException e) {
            errors.rejectValue("username", "not.found", "Username not found.");
        } catch (PasswordNotMatchException e) {
            errors.rejectValue("password", "not.matched", "Password not matched.");
        } catch (Exception e) {
            logger.error("Failed to authenticate user " + user, e);
            throw new ModelAndViewDefiningException(new ModelAndView("error", "message", e.getMessage()));
        }

        // return form if errors.
        if (errors.hasErrors()) {
            return showForm(request, response, errors);
        }

        // everything is passed, now look for where to go next.
        ModelAndView mv = null;
        String forwardTo = ServletRequestHelper.getOptionalParameter(request, USER_LOGIN_FORWARD_KEY, null);
        if (StringUtils.isEmpty(forwardTo)) {
            mv = new ModelAndView(getSuccessView());
        } else {
            mv = new ModelAndView(forwardTo);
        }
        logger.debug("Controller will now forwardTo " + forwardTo);

        return mv;
    }

    @Override
    protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws Exception {
        User user = (User) command;
        new FieldValidator(user, errors).
                notBlank("username", "This field can not be blank.").
                notBlank("password", "This field can not be blank.").
                skipIfHasError().
                length("username", 3, 64, "Input must between %d to %d characters.").
                length("password", 3, 64, "Input must between %d to %d characters.");
    }
}
