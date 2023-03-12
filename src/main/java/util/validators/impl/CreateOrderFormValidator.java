package util.validators.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import util.MessageAttributeUtil;
import util.validators.FormValidator;
import util.validators.InputValidator;

public class CreateOrderFormValidator implements FormValidator {
	private InputValidator inputValidator;

	public CreateOrderFormValidator(InputValidator inputValidator) {
		this.inputValidator = inputValidator;
	}

	@Override
	public boolean validate(HttpServletRequest req) {
		String orderCountryCode = req.getParameter("orderCountryCode").replaceAll("\\s", "");
		String orderPhone = req.getParameter("orderPhone").replaceAll("\\s", "") ;
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
