package ${project.packageName};

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class MainController extends MultiActionController{
	
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response){
        return new ModelAndView("/main/index", "message", 
        		"Welcome guest! Today date is " + new Date());
	}
	
	public String about(HttpServletRequest request, HttpServletResponse response){
		return "/main/about";
	}
}
