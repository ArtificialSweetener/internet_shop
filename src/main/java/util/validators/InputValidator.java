package util.validators;

import java.util.regex.Pattern;

public class InputValidator {

	private static final String nameRegex = "^(?!\\s+$)[a-zA-Zа-яА-ЯіІїЇ\\s'-]+[a-zA-Zа-яА-ЯіІїЇ\\s'-]*$"; //?
	private static final String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"; //done

	private static final String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d@#$%^&+=]{8,}$"; //done

	//This pattern requires at least one upper case letter ((?=.*[A-Z])), one lower case letter ((?=.*[a-z])), and one digit ((?=.*\\d)). It also requires a minimum length of 8 characters ({8,}). The pattern allows upper and lower case letters, digits, and special characters ([A-Za-z\\d@#$%^&+=]), but does not allow whitespaces. The special characters are optional, so they don't have to be present in the password.
	private static final String priceRegex = "^([^.]+\\.[^.]+|[0-9]+)$"; // done

	private static final String numberRegex = "^(0|[1-9][0-9]*)$"; // done
	
	private static final String countryCodeRegex = "^\\d{1,3}$";
	private static final String mobilePhoneRegex = "^\\d{10,}$";// done
			//"^(\\+\\d{1,3}[\\s-()])?\\d{7,14}$" ; 
			//"^(\\+\\d{1,3}[\\s-])?\\d{8,10}$"

	private static final String addressRegex = "^.{2,}$"; //done

	private static final String titleRegex = "^.{2,}$"; //done

	private static final String descriptionRegex = "^.{2,}$"; //done

	public InputValidator() {
	}

	private static InputValidator instance = null;

	public static InputValidator getInstance() {
		if (instance == null) {
			instance = new InputValidator();
		}
		return instance;
	}

	public boolean isNameValid(String name) {// done
		if (Pattern.compile(nameRegex).matcher(name).matches()) {
			return true;
		} else
			System.out.println("name not valid");
		return false;
	}

	public boolean isSurnameValid(String surname) { // done
		if (Pattern.compile(nameRegex).matcher(surname).matches()) {
			return true;
		} else
			System.out.println("surname not valid");
		return false;
	}

	public boolean isEmailValid(String email) { // done
		if (Pattern.compile(emailRegex).matcher(email).matches()) {
			return true;
		} else
			System.out.println("email not valid");
		return false;
	}

	public boolean isPasswordValid(String password) { // done
		if (Pattern.compile(passwordRegex).matcher(password).matches()) {
			return true;
		} else
			System.out.println("password not valid");
		return false;
	}

	public boolean isNumberValid(String number) { // done
		if (Pattern.compile(numberRegex).matcher(number).matches()) {
			return true;
		} else
			System.out.println("number not valid");
		return false;
	}

	public boolean isCountryCodeValid(String code) { // done
		if (Pattern.compile(countryCodeRegex).matcher(code).matches()) {
			return true;
		} else
			System.out.println("country code not valid");
		return false;
	}
	
	public boolean isMobilePhoneValid(String phone) { // done
		if (Pattern.compile(mobilePhoneRegex).matcher(phone).matches()) {
			return true;
		} else
			System.out.println("mobile phone number not valid");
		return false;
	}

	public boolean isAddressValid(String address) { // done
		if (Pattern.compile(addressRegex).matcher(address).matches()) {
			return true;
		} else
			System.out.println("address not valid");
		return false;
	}

	public boolean isTitleValid(String title) {// done
		System.out.println(title);
		if (Pattern.compile(titleRegex).matcher(title).matches()) { 
			return true;
		} else
			System.out.println("Title not valid");
		return false;
	}

	public boolean isDescriptionValid(String desc) {//done
		System.out.println(desc);
		if (Pattern.compile(descriptionRegex).matcher(desc).matches()) { 
			return true;
		} else
			System.out.println("Description not valid");
		return false;
	}

	public boolean isPriceValid(String productPrice) { //done
		if (Pattern.compile(priceRegex).matcher(productPrice).matches()) {
			return true;
		} else
			System.out.println("product price not valid");
		return false;
	}

}
