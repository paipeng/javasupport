package ${packageName};

import javasupport.servlet.ServletRequestHelper;

import javax.servlet.http.HttpServletRequest;

/** Edit Form Controller for ${className}. */
public class EditController extends CreateController {
	public EditController() {
		setFormView("${classNamePath}/edit");
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		int id = ServletRequestHelper.getRequiredIntParameter(request, "id");
		${className} ${beanName} = ${beanName}Dao.get(id);
		return product;
	}
}

