package exception;

/**
 * The {@code DataProcessingException} class is a runtime exception that is used
 * to signal an error during data processing. This exception is thrown when
 * there is an error in processing the data and it cannot be recovered.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class DataProcessingException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DataProcessingException(String message, Throwable cause) {
		super(message, cause);
	}
}
