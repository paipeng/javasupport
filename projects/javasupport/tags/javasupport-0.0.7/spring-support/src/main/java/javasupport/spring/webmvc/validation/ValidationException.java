package javasupport.spring.webmvc.validation;

/**
 * Throw this exception when encounter validation errors that is not
 * recoverable.
 * 
 * @author zemian
 *
 */
public class ValidationException extends RuntimeException {
	protected static final long serialVersionUID = -8862526887923198424L;

	public ValidationException() {
	}

	public ValidationException(String message) {
		super(message);
	}

	public ValidationException(Throwable cause) {
		super(cause);
	}

	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}
}
