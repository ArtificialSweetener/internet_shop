package util.validators.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import util.MessageAttributeUtil;
import util.validators.FormValidator;
import util.validators.InputValidator;

/**
 * The CreateOrderFormValidator class is responsible for validating the input
 * data provided by the user while creating an order. It implements the
 * FormValidator interface. This class checks the validity of the
 * orderCountryCode, orderPhone and orderAddress using an InputValidator object,
 * and returns a boolean value indicating whether the input is valid or not.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class CreateOrderFormValidator implements FormValidator {
	private InputValidator inputValidator;

	public CreateOrderFormValidator(InputValidator inputValidator) {
		this.inputValidator = inputValidator;
	}

	/**
	 * Validates the input data provided by the user while creating an order. It
	 * checks the validity of the orderCountryCode, orderPhone and orderAddress
	 * using an InputValidator object, and returns a boolean value indicating
	 * whether the input is valid or not. If the input is invalid, a list of message
	 * keys is generated and stored in the HttpServletRequest attribute using the
	 * MessageAttributeUtil class.
	 *
	 * @param req the HttpServletRequest object containing the input data.
	 * @return true if the input data is valid, false otherwise.
	 */
	@Override
	public boolean validate(HttpServletRequest req) {
		String orderCountryCode = req.getParameter("orderCountryCode").replaceAll("\\s", "");
		String orderPhone = req.getParameter("orderPhone").replaceAll("\\s", "");
		String orderAddress = req.getParameter("orderAddress").trim();
		boolean isValid = true;

		boolean validOrderCode = inputValidator.isCountryCodeValid(orderCountryCode);
		boolean validOrderPhone = inputValidator.isMobilePhoneValid(orderPhone); // done
		boolean validOrderAddress = inputValidator.isAddressValid(orderAddress); // done

		List<String> messageKeyList = new ArrayList<>();

		if (!validOrderPhone || !validOrderCode) {
			messageKeyList.add("message.order_phone_not_valid"); // done
			isValid = false;
		}
		if (!validOrderAddress) {
			messageKeyList.add("message.order_address_not_valid"); // done
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
