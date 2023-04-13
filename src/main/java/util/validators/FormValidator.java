package util.validators;

import javax.servlet.http.HttpServletRequest;

/**
 * The FormValidator interface provides a contract for validating form data
 * submitted through an HTTP request. Implementing classes must provide an
 * implementation for the validate() method, which takes a HttpServletRequest
 * object as input and returns a boolean indicating whether or not the submitted
 * form data is valid.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 *
 */
public interface FormValidator {
	/**
	 * Validates form data submitted through an HTTP request.
	 * 
	 * @param req the HttpServletRequest object containing the submitted form data.
	 * @return true if the form data is valid, false otherwise.
	 */
	public boolean validate(HttpServletRequest req);
}
