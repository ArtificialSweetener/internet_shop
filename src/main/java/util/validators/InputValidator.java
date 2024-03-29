package util.validators;

import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * The InputValidator class provides methods to validate different types of user
 * inputs such as name, email, password, number, country code, mobile phone,
 * address, title, description, and price. It contains static final regular
 * expressions for each type of validation. The class uses the Singleton
 * pattern, where only one instance of the class can be created.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class InputValidator {

	private static final String nameRegex = "^(?!\\s+$)[a-zA-Zа-яА-ЯіІїЇ\\s'-]+[a-zA-Zа-яА-ЯіІїЇ\\s'-]*$"; // ?
	private static final String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"; // done

	private static final String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d@#$%^&+=]{8,}$"; // done

	// This pattern requires at least one upper case letter ((?=.*[A-Z])), one lower
	// case letter ((?=.*[a-z])), and one digit ((?=.*\\d)). It also requires a
	// minimum length of 8 characters ({8,}). The pattern allows upper and lower
	// case letters, digits, and special characters ([A-Za-z\\d@#$%^&+=]), but does
	// not allow whitespaces. The special characters are optional, so they don't
	// have to be present in the password.
	private static final String priceRegex = "^([^.]+\\.[^.]+|[0-9]+)$"; // done

	private static final String numberRegex = "^(0|[1-9][0-9]*)$"; // done

	private static final String countryCodeRegex = "^\\d{1,3}$";
	private static final String mobilePhoneRegex = "^\\d{10,}$";// done
	// "^(\\+\\d{1,3}[\\s-()])?\\d{7,14}$" ;
	// "^(\\+\\d{1,3}[\\s-])?\\d{8,10}$"

	private static final String addressRegex = "^.{2,}$"; // done

	private static final String titleRegex = "^.{2,}$"; // done

	private static final String descriptionRegex = "^.{2,}$"; // done

	private static final Logger logger = LogManager.getLogger(InputValidator.class);

	public InputValidator() {
	}

	private static InputValidator instance = null;

	public static InputValidator getInstance() {
		if (instance == null) {
			instance = new InputValidator();
		}
		return instance;
	}

	/**
	 * Method to check if the name input is valid.
	 * 
	 * @param name the name input.
	 * @return true if the name input is valid, false otherwise.
	 */
	public boolean isNameValid(String name) {// done
		if (Pattern.compile(nameRegex).matcher(name).matches()) {
			return true;
		} else
			logger.info("name not valid");
		return false;
	}

	/**
	 * Method to check if the surname input is valid.
	 * 
	 * @param surname the surname input.
	 * @return true if the surname input is valid, false otherwise.
	 */
	public boolean isSurnameValid(String surname) { // done
		if (Pattern.compile(nameRegex).matcher(surname).matches()) {
			return true;
		} else
			logger.info("surname not valid");
		return false;
	}

	/**
	 * Method to check if the email input is valid.
	 * 
	 * @param email the email input.
	 * @return true if the email input is valid, false otherwise.
	 */
	public boolean isEmailValid(String email) { // done
		if (Pattern.compile(emailRegex).matcher(email).matches()) {
			return true;
		} else
			logger.info("email not valid");
		return false;
	}

	/**
	 * Validates a password string.
	 * 
	 * @param password the password string to validate
	 * @return true if the password string is valid; otherwise, false
	 */
	public boolean isPasswordValid(String password) { // done
		if (Pattern.compile(passwordRegex).matcher(password).matches()) {
			return true;
		} else
			logger.info("password not valid");
		return false;
	}

	/**
	 * Validates a number string.
	 * 
	 * @param number the number string to validate
	 * @return true if the number string is valid; otherwise, false
	 */
	public boolean isNumberValid(String number) { // done
		if (Pattern.compile(numberRegex).matcher(number).matches()) {
			return true;
		} else
			logger.info("number not valid");
		return false;
	}

	/**
	 * Validates if the provided string is a valid country code (up to 3 digits)
	 * format.
	 *
	 * @param code the country code to validate
	 * @return true if the country code is valid, false otherwise
	 */
	public boolean isCountryCodeValid(String code) { // done
		if (Pattern.compile(countryCodeRegex).matcher(code).matches()) {
			return true;
		} else
			logger.info("country code not valid");
		return false;
	}

	/**
	 * Validates if the provided string is a valid mobile phone number format (10
	 * digits).
	 *
	 * @param phone the mobile phone number to validate
	 * @return true if the mobile phone number is valid, false otherwise
	 */
	public boolean isMobilePhoneValid(String phone) { // done
		if (Pattern.compile(mobilePhoneRegex).matcher(phone).matches()) {
			return true;
		} else
			logger.info("mobile phone number not valid");
		return false;
	}

	/**
	 * Validates if the provided string is a valid address (at least 2 characters).
	 *
	 * @param address the address to validate
	 * @return true if the address is valid, false otherwise
	 */
	public boolean isAddressValid(String address) { // done
		if (Pattern.compile(addressRegex).matcher(address).matches()) {
			return true;
		} else
			logger.info("address not valid");
		return false;
	}

	/**
	 * Validates if the provided string is a valid title (at least 2 characters).
	 *
	 * @param title the title to validate
	 * @return true if the title is valid, false otherwise
	 */
	public boolean isTitleValid(String title) {// done
		System.out.println(title);
		if (Pattern.compile(titleRegex).matcher(title).matches()) {
			return true;
		} else
			logger.info("Title not valid");
		return false;
	}

	/**
	 * Validates if the provided string is a valid description (at least 2
	 * characters).
	 *
	 * @param desc the description to validate
	 * @return true if the description is valid, false otherwise
	 */
	public boolean isDescriptionValid(String desc) {// done
		System.out.println(desc);
		if (Pattern.compile(descriptionRegex).matcher(desc).matches()) {
			return true;
		} else
			logger.info("Description not valid");
		return false;
	}

	/**
	 * Checks if the given string is a valid price value. A valid price is a
	 * positive number with up to two decimal places or an integer.
	 * 
	 * @param productPrice the string to be validated as a price
	 * @return true if the string is a valid price, false otherwise
	 */
	public boolean isPriceValid(String productPrice) { // done
		if (Pattern.compile(priceRegex).matcher(productPrice).matches()) {
			return true;
		} else
			logger.info("product price not valid");
		return false;
	}

}
