package javasupport.spring.webmvc.validation;

import org.springframework.validation.Errors;

public class EmailValidatorFunction implements ValidatorFunction {
    public static final String EMAIL_PATTERN = "[\\w-\\.]+@[\\w-\\.]+";
    private String errorMsg = "Invalid Email format.";
    public EmailValidatorFunction(){    	
    }
    public EmailValidatorFunction(String errorMsg){
    	this.errorMsg = errorMsg;
    }
	public void apply(String fieldName, Object fieldValue, Errors errors) {
		String email = (String)fieldValue;
		if(!email.matches(EMAIL_PATTERN)){
			errors.rejectValue(fieldName, "invalid.email", String.format(errorMsg, fieldName, fieldValue));
        }
	}
}
