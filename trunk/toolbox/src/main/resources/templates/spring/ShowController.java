package ${packageName};
<% if(origModelClassName){ %>
import ${origModelClassName};
<% } %>
import javasupport.servlet.ServletRequestHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.mvc.AbstractController;

/**  Controller to show single instance of Product. */
public class ShowController extends AbstractController {
	protected ${className}Dao ${beanName}Dao;

	public void set${className}Dao(${className}Dao that) {
		this.${beanName}Dao = that;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		${className} ${beanName} = (${className})request.getAttribute("${beanName}");
		if(${beanName} != null){
			return new ModelAndView("${classNamePath}/show", "${beanName}", ${beanName});
		}
		int id = ServletRequestHelper.getRequiredIntParameter(request, "id");
		${beanName} = ${beanName}Dao.get(id);
		if(${beanName} == null){
			throw new ModelAndViewDefiningException(new ModelAndView("error", "message", "${className} ID "+id+" not found"));
		}
		return new ModelAndView("${classNamePath}/show", "${beanName}", ${beanName});
	}
}

