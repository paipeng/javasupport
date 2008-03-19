package javasupport.spring.webmvc.validation;

import java.beans.PropertyDescriptor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * This class provide a convinient way for programmatically validate
 * each field of a command object. This is designed to work in SpringMVC
 * environment where the error object is created after form binding.
 * 
 * Typical usage is create a instance of this class by using {@link #validate(Object, Errors, String)}
 * method and then calling each validation method in cascade style.
 * 
 * Custom validation can be added using the {@link FieldValidatorFunction} implementation,
 * which can be easily follow cascade {@link #call(FieldValidatorFunction, String)}
 * method using an annonymous class style.
 * 
 * @author zemian
 *
 */
public class FieldValidator implements Validator {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    public static final String PHONE_PATTERN = "\\d{3}[-\\. ]{0,1}\\d{3}[-\\. ]{0,1}\\d{4}";
    protected BeanWrapper commandBeanWrapper;
    protected Errors errors;
    
    public Errors getErrors() {
		return errors;
	}
    public Object getCommandBean() {
    	return commandBeanWrapper.getWrappedInstance();
	}

    public FieldValidator(Object command, Errors errors) {
        this.commandBeanWrapper = new BeanWrapperImpl(command);
        this.errors = errors;
    }

    public boolean supports(Class clazz) {
        return clazz.isInstance(getCommandBean());
    }

    /**
     * Validate all field as required value.
     * @param command
     * @param errors
     */
    public void validate(Object command, Errors errors) {
        //default to all field are required.
        for (PropertyDescriptor pd : commandBeanWrapper.getPropertyDescriptors()) {
            String fieldName = pd.getName();
            notBlank(fieldName, "This field can not be blank.");
        }
    }
    
    public boolean hasErrors(){
    	return errors.getErrorCount()>0;
    }
    
    public boolean isSkipable(){
    	if (this instanceof SkipableFieldValidator) {
    		return true;
    	}
        return false;
    }
    
    public FieldValidator skipIfHasError(){
    	if(isSkipable()) return this;        
        return new SkipableFieldValidator(this);
    }

    protected boolean isBlankField(Object fieldValue) {
        if (fieldValue == null) {
            return true;
        } else if (fieldValue.getClass().isArray()) {
            Object[] val = (Object[]) fieldValue;
            logger.debug("Field value is an array with size " + val.length + ", content: " + Arrays.asList(val));
            return val.length == 0;
        } else if (fieldValue instanceof Collection<?>) {
            Collection<?> val = (Collection<?>) fieldValue;
            logger.debug("Field value is a collection with size " + val.size() + ", content: " + val);
            return val.size() == 0;
        } else {
            String val = fieldValue.toString();
            return StringUtils.isBlank(val);
        }
    }

    // === simple chained field validation methods.
    public FieldValidator notBlank(String fieldName, String errorMsg) {
        if (isSkipable()) return this;

        logger.debug("Validating not blank field: " + fieldName);
        Object fieldValue = commandBeanWrapper.getPropertyValue(fieldName);
        logger.debug("Field value: " + fieldValue);
        if (isBlankField(fieldValue)) {
            errors.rejectValue(fieldName, "not.blank", String.format(errorMsg, fieldName, fieldValue));
        }
        return this;
    }

    public FieldValidator notNull(String fieldName, String errorMsg) {
        if (isSkipable()) return this;

        logger.debug("Validating not null: " + fieldName);
        Object fieldValue = commandBeanWrapper.getPropertyValue(fieldName);
        logger.debug("Field value: " + fieldValue);
        if (fieldValue == null) {
            errors.rejectValue(fieldName, "not.null", String.format(errorMsg, fieldName, fieldValue));
        }
        return this;
    }

    public FieldValidator length(String fieldName, int min, int max, String errorMsg) {
        if (isSkipable()) return this;

        logger.debug("Validating lenght: " + fieldName + " min: " + min + " max: " + max);
        int len = 0;
        Object fieldValue = commandBeanWrapper.getPropertyValue(fieldName);
        if(fieldValue == null){
        	len = 0;
        }else if (fieldValue instanceof String) {
            len = ((String) fieldValue).length();
        } else if (fieldValue instanceof Integer) {
            len = (Integer) fieldValue;
        } else if (fieldValue instanceof Long) {
            len = ((Long) fieldValue).intValue(); //downcast to int. shoudl cover most cases.
        } else {
            throw new ValidationException("Filed value type: " + fieldValue.getClass().getSimpleName() + " can not be use in method length.");
        }
        logger.debug("Field value len: " + len);
        if (len < min || len > max) {
            errors.rejectValue(fieldName, "length.out.of.range", String.format(errorMsg, min, max, fieldName, fieldValue));

        }

        return this;
    }

    public FieldValidator equalField(String fieldName, String fieldName2, String errorMsg) {
        if (isSkipable()) return this;

        logger.debug("Validating fieldName: " + fieldName + " equal to fieldName2: " + fieldName2);
        Object fieldValue = commandBeanWrapper.getPropertyValue(fieldName);
        Object fieldValue2 = commandBeanWrapper.getPropertyValue(fieldName2);

        logger.debug("Field fieldValue: " + fieldValue + ", fieldValue2: " + fieldValue2);
        if (!fieldValue.equals(fieldValue2)) {
            errors.rejectValue(fieldName, "not.equals", String.format(errorMsg, fieldName2, fieldName, fieldValue2, fieldValue));
        }

        return this;
    }

    public FieldValidator skipIfBlank(String fieldName) {
        if (isSkipable()) return this;

        logger.debug("Validating apply if not blank: " + fieldName);
        Object fieldValue = commandBeanWrapper.getPropertyValue(fieldName);
        logger.debug("Field value: " + fieldValue);
        if (isBlankField(fieldValue)) {
            return new SkipableFieldValidator(this);
        }
        return this;
    }

    public FieldValidator match(String fieldName, String regex, String errorMsg) {
        if (isSkipable()) return this;

        logger.debug("Validating matches: " + fieldName);
        Object fieldValue = commandBeanWrapper.getPropertyValue(fieldName);
        if (!(fieldValue instanceof String)) {
            throw new ValidationException("Can not match against type " + fieldValue.getClass().getSimpleName());
        }

        String val = (String) fieldValue;
        logger.debug("Field value: " + val);
        if (!Pattern.matches(regex, val)) {
            errors.rejectValue(fieldName, "not.matched", String.format(errorMsg, regex, fieldName, fieldValue));
        }

        return this;
    }

    public FieldValidator notMatch(String fieldName, String regex, String errorMsg) {
        if (isSkipable()) return this;

        logger.debug("Validating notMatched: " + fieldName);
        Object fieldValue = commandBeanWrapper.getPropertyValue(fieldName);
        if (!(fieldValue instanceof String)) {
            throw new ValidationException("Can not un-match against type " + fieldValue.getClass().getSimpleName());
        }

        String val = (String) fieldValue;
        logger.debug("Field value: " + val);
        if (Pattern.matches(regex, val)) {
            errors.rejectValue(fieldName, "matched", String.format(errorMsg, regex, fieldName, fieldValue));

        }

        return this;
    }
    
    public FieldValidator date(String fieldName, String dateFormat, String minDate, String maxDate, String errorMsg) {
        if (this instanceof SkipableFieldValidator) {
            return this;
        }

        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        logger.debug("Validating date: " + fieldName + " min: " + df.format(minDate) + " max: " + df.format(maxDate));
        Object fieldValue = commandBeanWrapper.getPropertyValue(fieldName);
        Date dateVal;
        try {            
            Date minDt = df.parse(minDate);
            Date maxDt = df.parse(maxDate);
            
            dateVal = null;

            if (fieldValue instanceof Date) {
                dateVal = (Date) fieldValue;
            } else if (fieldValue instanceof String) {
                dateVal = df.parse((String) fieldValue);
            } else {
                throw new ValidationException("Filed value type: " + fieldValue.getClass().getSimpleName() + " can not be use in method date.");
            }

            logger.debug("Field dateVal: " + dateVal);
            if (dateVal.compareTo(minDt) < 0 || dateVal.compareTo(maxDt) > 0) {
                errors.rejectValue(fieldName, "date.out.of.range", String.format(errorMsg, df.format(minDt), df.format(maxDt), fieldName, fieldValue));

            }
        } catch (ParseException e) {
            throw new ValidationException("Failed to parse date input.", e);
        }

        return this;
    }

    public <T> FieldValidator atLeastOne(String fieldName, Collection<T> collection, String errorMsg) {
        if (this instanceof SkipableFieldValidator) {
            return this;
        }

        logger.debug("Validating atLeastOne element in a collection: " + fieldName);
        logger.debug("Collection: " + collection);
        Object fieldValue = commandBeanWrapper.getPropertyValue(fieldName);
        logger.debug("Field value: " + fieldValue);
        if (!collection.contains(fieldValue)) {
            errors.rejectValue(fieldName, "not.contains.any", String.format(errorMsg, fieldName, fieldValue));

        }

        return this;
    }
    
    public FieldValidator function(String fieldName, ValidatorFunction func) {
        if (isSkipable()) return this;

        logger.debug("Validating field " + fieldName + " with custom function: " + func);
        Object fieldValue = commandBeanWrapper.getPropertyValue(fieldName);
        logger.debug("Field value: " + fieldValue);
        func.apply(fieldName, fieldValue, errors);
        
        return this;
    }
}
