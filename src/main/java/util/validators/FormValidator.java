package util.validators;

import javax.servlet.http.HttpServletRequest;

public interface FormValidator {
	public boolean validate(HttpServletRequest req); 
}
