package util.validators;

import java.util.regex.Pattern;

public class PasswordValidator {
	private static final String regex = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}" ;
//	^ represents starting character of the string.
//	(?=.*[0-9]) represents a digit must occur at least once.
//	(?=.*[a-z]) represents a lower case alphabet must occur at least once.
//	(?=.*[A-Z]) represents an upper case alphabet that must occur at least once.
//	(?=\\S+$) white spaces don’t allowed in the entire string.
//	.{8, } represents at least 8 characters
//	$ represents the end of the string.

	public PasswordValidator() {
	}

	private static PasswordValidator instance = null;

	public static PasswordValidator getInstance() {
		if (instance == null) {
			instance = new PasswordValidator();
		}
		return instance;
	}

	public boolean isValid(String password) {
		if (Pattern.compile(regex).matcher(password).matches()) {
			return true;
		} else
			System.out.println("password not valid");
			return false;
	}
}
