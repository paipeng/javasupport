package ${packageName};

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/** List and browse Controller for ${className}. */
public class ListController extends AbstractController {
	private ${className}Dao ${beanName}Dao;

	public void set${className}Dao(${className}Dao that) {
		this.${beanName}Dao = that;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<${className}> ${beanName}List = ${beanName}Dao.findAll();		
		return new ModelAndView("${classNamePath}/list", "${beanName}List", ${beanName}List);
	}
}
