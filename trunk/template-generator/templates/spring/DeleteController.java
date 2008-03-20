package ${packageName};

import java.util.List;

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
		boolean confirm = ServletRequestHelper.getParameter(request, "confirm", "no");
		if("no".equals(confirm)){
			return new ModelAndView("${classNamePath}/delete", "confirm", confirm);
		}
		
		int id = ServletRequestHelper.getRequiredIntParameter(request, "id");
		${className} ${beanName} = ${beanName}Dao.delete(id);		
		return new ModelAndView("${classNamePath}/delete", "${beanName}", ${beanName});
	}
}
