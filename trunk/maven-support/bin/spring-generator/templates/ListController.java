package ${packageName};

import java.util.List;

import javasupport.spring.webmvc.validation.FieldValidator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.mvc.SimpleFormController;

/** List and browse Controller for ${className}. */
public class ListController extends AbstractController {
	private ${className}Dao ${beanName}Dao;

	public void set${className}Dao(${className}Dao that) {
		this.${beanName}Dao = that;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<${className}> ret = ${beanName}Dao.findAll();
		
		return new ModelAndView("${classNamePath}/list", "list", ret);
	}
}
