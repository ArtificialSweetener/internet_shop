package util.validators;

public class NameValidator {
	
	public NameValidator() {
	}

	private static NameValidator instance = null;

	public static NameValidator getInstance() {
		if (instance == null) {
			instance = new NameValidator();
		}
		return instance;
	}

	public boolean isValid(String name) {
		if (name.length()>0) {
			return true;
		} else
			System.out.println("name not valid");
			return false;
	}
}
