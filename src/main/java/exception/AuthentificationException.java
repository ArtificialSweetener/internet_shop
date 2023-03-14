package exception;

/**
 * The AuthentificationException class is an Exception that is thrown when there
 * is an authentication error. It is a subclass of the Exception class and
 * includes a constructor that takes a String parameter as the error message.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class AuthentificationException extends Exception {

	private static final long serialVersionUID = 1L;

	public AuthentificationException(String message) {
		super(message);
	}
}
