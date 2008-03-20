package ${packageName};
<% if(origModelClassName){ %>
import ${origModelClassName}
<% } %>
import javasupport.spring.webmvc.validation.FieldValidator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.mvc.SimpleFormController;

/** Create Form Controller for ${className}. */
public class CreateController extends SimpleFormController {
	protected ${className}Dao ${beanName}Dao;

	public void set${className}Dao(${className}Dao that) {
		this.${beanName}Dao = that;
	}

	public CreateController() {
		setCommandClass(${className}.class);
		setCommandName("${beanName}");
		
		setFormView("${classNamePath}/create");
		setSuccessView("forward:show");
	}

	@Override
	protected void doSubmitAction(Object command) throws Exception {
		${className} ${beanName} = (${className}) command;
		logger.info("Create new ${className} " + ${beanName});
		${beanName}Dao.create(${beanName});
	}

	@Override
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws Exception {
		if(errors.hasErrors())
			return;
		
		${className} ${beanName} = (${className}) command;

		// Validate command object.
		FieldValidator validator = new FieldValidator(${beanName}, errors);
		validator
		<% for (field in displayFields) {	
			def type = field[1].toLowerCase()
		%>
			.notBlank("${field[0]}", "This field can not be blank.")
		<% 
			} 
		%>
			.skipIfHasError()
		<% for (field in displayFields) {	
			def type = field[1].toLowerCase()	
			def maxLen = 255 // default String length
			if(type == "integer") maxLen = 9
			else if(type == "double") maxLen = 12
		%>
			.length("${field[0]}", 1, ${maxLen}, "Input must between %d to %d characters.")
		<% } %>		
			.skipIfHasError()
		<% for (field in displayFields) {	
			def type = field[1].toLowerCase()
			if(type == "integer"){
		%>
			.match("${field[0]}", "\\\\d+", "Invalid integer value.")
	  <%
			}else if(type == "double"){
		%>
			.match("${field[0]}", "\\\\d{1,9}(\\\\.\\\\d{1,2}){0,1}", "Invalid decimal value. Try #.## format.")
	  <%
			}else{
		%>
			.match("${field[0]}", "\\\\p{Alnum}+", "Invalid alpha numberic characters value.")
		<% 
			}
		}
		%>		
		;
	}
	
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		<% for (field in displayFields) {	
			def type = field[1].toLowerCase()
			if(type == "date"){
		%>
			binder.registerCustomEditor(java.util.Date.class, "${field[0]}", 
				new org.springframework.beans.propertyeditors.CustomDateEditor(new java.text.SimpleDateFormat("yyyy-MM-dd"), true));
		<% 
			} 
		}
		%>
	}
}

