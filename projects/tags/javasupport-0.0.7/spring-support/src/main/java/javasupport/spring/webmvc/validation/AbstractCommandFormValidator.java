package javasupport.spring.webmvc.validation;

import java.lang.reflect.ParameterizedType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Validator;

/**
 * A base class that provide logger and will support direct subclass
 * instance check.
 * 
 * @author zemian
 *
 * @param <T>
 */
public abstract class AbstractCommandFormValidator<T> implements Validator {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		Class<?> templateClass = (Class<?>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		return templateClass.equals(clazz);
	}
}
