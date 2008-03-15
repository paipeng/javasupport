package deng.estore.category;

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

/** List and browse Controller for Category. */
public class CategoryListController extends AbstractController {
	private CategoryDao categoryDao;

	public void setCategoryDao(CategoryDao that) {
		this.categoryDao = that;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
		List<Category> ret = categoryDao.findAll();
		
		return new ModelAndView("category/list", "list", ret);
	}

}
