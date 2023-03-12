package util.validators.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import util.MessageAttributeUtil;
import util.validators.FormValidator;
import util.validators.InputValidator;

public class RegisterFormValidator implements FormValidator {
	private InputValidator inputValidator;

	public RegisterFormValidator(InputValidator inputValidator) {
		this.inputValidator = inputValidator;
	}

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
