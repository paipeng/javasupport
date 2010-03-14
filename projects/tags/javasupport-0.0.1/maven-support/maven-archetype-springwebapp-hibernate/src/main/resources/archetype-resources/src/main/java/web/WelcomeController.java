package ${groupId}.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.mvc.AbstractController;

public class WelcomeController extends AbstractController {

    public ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String testErrorPage = request.getParameter("testErrorPage");
        if (testErrorPage != null) {
            ModelAndView mav = new ModelAndView("error");
            mav.addObject("message", "You are testing a error page on purpose.");
            throw new ModelAndViewDefiningException(mav);
        }

        ModelAndView mav = new ModelAndView("welcome");
        mav.addObject("message", "Welcome guest! Today date is " + new Date());
        return mav;
    }
}
