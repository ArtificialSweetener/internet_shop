package util.validators;

import java.util.regex.Pattern;

public class EmailValidator {
	private static final String regex ="^[a-zA-Z0-9+_.\\-]+@[a-zA-Z0-9.\\-]+$";

	public EmailValidator() {
	}

	private static EmailValidator instance = null;

	public static EmailValidator getInstance() {
		if (instance == null) {
			instance = new EmailValidator();
		}
		return instance;
	}

	public boolean isValid(String email) {
		if (Pattern.compile(regex).matcher(email).matches()) {
			return true;
		} else
			System.out.println("email not valid");
			return false;
	}

}
