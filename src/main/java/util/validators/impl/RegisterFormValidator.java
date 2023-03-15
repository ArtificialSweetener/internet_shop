package util.validators.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import util.MessageAttributeUtil;
import util.validators.FormValidator;
import util.validators.InputValidator;

/**
 * This class is responsible for validating the user registration form fields.
 * It implements the FormValidator interface, which requires the implementation
 * of the validate() method. The class receives an instance of an InputValidator
 * through its constructor, which is used to perform the validation of the input
 * values. If any of the fields is not valid, a list of message keys is created
 * and stored in the request attribute using the MessageAttributeUtil class, and
 * the method returns false. Otherwise, the method returns true.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */

public class RegisterFormValidator implements FormValidator {
	private InputValidator inputValidator;

	/**
	 * Constructs a RegisterFormValidator object with the given InputValidator
	 * instance
	 * 
	 * @param inputValidator the input validator to be used to validate the form
	 *                       fields
	 */
	public RegisterFormValidator(InputValidator inputValidator) {
		this.inputValidator = inputValidator;
	}

	/**
	 * Validates the user registration form fields using the InputValidator
	 * instance. If any of the fields is not valid, a list of message keys is
	 * created and stored in the request attribute using the MessageAttributeUtil
	 * class, and the method returns false. Otherwise, the method returns true.
	 * 
	 * @param req the HttpServletRequest object that contains the parameters of the
	 *            form fields to be validated
	 * @return true if all fields are valid, false otherwise
	 */
	@Override
	public boolean validate(HttpServletRequest req) {
		String userName = req.getParameter("user_name").trim();
		String userSurname = req.getParameter("user_surname").trim();
		String userEmail = req.getParameter("user_email").trim();
		String userPassword = req.getParameter("user_password").trim();

		boolean isValid = true;

		boolean validUserName = inputValidator.isNameValid(userName);
		boolean validUserSurname = inputValidator.isSurnameValid(userSurname);
		boolean validUserEmail = inputValidator.isEmailValid(userEmail);
		boolean validUserPassword = inputValidator.isPasswordValid(userPassword);

		List<String> messageKeyList = new ArrayList<>();

		if (!validUserName) {
			messageKeyList.add("message.user_name_not_valid"); // done
			isValid = false;
		}
		if (!validUserSurname) {
			messageKeyList.add("message.user_surname_not_valid"); // done
			isValid = false;
		}
		if (!validUserEmail) {
			messageKeyList.add("message.user_email_not_valid"); // done
			isValid = false;
		}
		if (!validUserPassword) {
			messageKeyList.add("message.user_password_not_valid"); // done
			isValid = false;
		}
		if (messageKeyList.isEmpty()) {
			return isValid;
		} else {
			MessageAttributeUtil.setMessageAttributeWithList(req, messageKeyList);
			return isValid;
		}
	}

}
