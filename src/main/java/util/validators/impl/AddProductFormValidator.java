package util.validators.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import util.MessageAttributeUtil;
import util.validators.FormValidator;
import util.validators.InputValidator;

/**
 * This class is responsible for validating the add product form submitted by
 * the user. It implements the FormValidator interface and validates the form by
 * using an InputValidator object.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class AddProductFormValidator implements FormValidator {
	private InputValidator inputValidator;

	public AddProductFormValidator(InputValidator inputValidator) {
		this.inputValidator = inputValidator;
	}

	/**
	 * Validates the add product form submitted by the user.
	 * 
	 * @param req the HttpServletRequest object containing the form data
	 * @return true if the form is valid, false otherwise
	 */
	@Override
	public boolean validate(HttpServletRequest req) {
		String productName = req.getParameter("pName").trim();
		String productDescription = req.getParameter("pDescription").trim();
		String productPrice = req.getParameter("pPrice").trim();
		String productQuantity = req.getParameter("pQuantity").trim();

		boolean isValid = true;

		boolean validProductName = inputValidator.isTitleValid(productName);
		boolean validProductDesc = inputValidator.isDescriptionValid(productDescription);
		boolean validProductPrice = inputValidator.isPriceValid(productPrice);
		boolean validProductQuantity = inputValidator.isNumberValid(productQuantity);

		List<String> messageKeyList = new ArrayList<>();

		if (!validProductName) {
			messageKeyList.add("message.product_name_not_valid"); // done
			isValid = false;
		}
		if (!validProductDesc) {
			messageKeyList.add("message.product_desc_not_valid");// done
			isValid = false;
		}
		if (!validProductPrice) {
			messageKeyList.add("message.product_price_not_valid");// done
			isValid = false;
		}
		if (!validProductQuantity) {
			messageKeyList.add("message.product_quantity_not_valid"); // done
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
