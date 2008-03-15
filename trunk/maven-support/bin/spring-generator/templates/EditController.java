
package deng.estore.category;

import javasupport.spring.webmvc.validation.FieldValidator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.mvc.SimpleFormController;

/** Create Form Controller for Category. */
public class CategoryFormCreateController extends SimpleFormController {
	private CategoryDao categoryDao;
	
	public void setCategoryDao(CategoryDao that){ this.categoryDao = that; }
	
	public CategoryFormCreateController() {
		setCommandClass(Category.class);
		setCommandName("category");
	
		setSuccessView("category/createSuccess");
		setFormView("category/categoryForm");
	} 
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command,
					BindException errors) throws Exception {
			Category category = (Category) command;
			logger.info("Saving command object " + category);
			categoryDao.save(category);
			
			ModelAndView mv = new ModelAndView(getSuccessView());
			mv.addObject("message", "Record created. New ID=" + category.getId());
			return mv;
	} 
	
	@Override
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws Exception {
    Category category = (Category) command;
    
    //Validatte command object.
    FieldValidator validator = new FieldValidator(category, errors);
    

		validator.notBlank("name", "This field can not be blank.");
		validator.notBlank("description", "This field can not be blank.");
    }
  } 
  
