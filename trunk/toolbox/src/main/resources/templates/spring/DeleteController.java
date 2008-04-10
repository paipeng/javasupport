package ${packageName};
<% if(origModelClassName){ %>
import ${origModelClassName};
<% } %>
import javasupport.servlet.ServletRequestHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/** Delete Controller for ${className}. */
public class DeleteController extends AbstractController {
	private ${className}Dao ${beanName}Dao;

	public void set${className}Dao(${className}Dao that) {
		this.${beanName}Dao = that;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int id = ServletRequestHelper.getRequiredIntParameter(request, "id");
		String confirm = ServletRequestHelper.getOptionalParameter(request, "confirm", "no");
		
		ModelAndView mv = new ModelAndView("${classNamePath}/delete");
		mv.addObject("confirm", confirm);
		mv.addObject("id", id);
		
		if("yes".equals(confirm)){
			${className} ${beanName} = ${beanName}Dao.delete(id);	
			mv.addObject("${beanName}", ${beanName});
		}
		
		return mv;
	}
}
