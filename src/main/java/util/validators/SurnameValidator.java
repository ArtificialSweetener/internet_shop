package util.validators;

public class SurnameValidator {
	
	public SurnameValidator() {
	}

	private static SurnameValidator instance = null;

	public static SurnameValidator getInstance() {
		if (instance == null) {
			instance = new SurnameValidator();
		}
		return instance;
	}

	public boolean isValid(String surname) {
		if (surname.length()>0) {
			return true;
		} else
			System.out.println("surname not valid");
			return false;
	}
}
