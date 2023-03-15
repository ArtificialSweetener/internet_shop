package util.validators.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import util.MessageAttributeUtil;
import util.validators.FormValidator;
import util.validators.InputValidator;

/**
 * The AddCategoryFormValidator class is responsible for validating the form
 * data submitted for adding a new category to the system. It implements the
 * FormValidator interface.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class AddCategoryFormValidator implements FormValidator {
	private InputValidator inputValidator;

	public AddCategoryFormValidator(InputValidator inputValidator) {
		this.inputValidator = inputValidator;
	}

	/**
	 * Validates the form data submitted for adding a new category to the system.
	 * 
	 * @param req The HTTP servlet request containing the form data.
	 * @return true if the form data is valid, false otherwise.
	 */
	@Override
	public boolean validate(HttpServletRequest req) {
		String categoryTitle = req.getParameter("catTitle").trim();
		String categoryDescription = req.getParameter("catDescription").trim();

		boolean isValid = true;

		boolean validCategoryTitle = inputValidator.isTitleValid(categoryTitle);
		boolean validCategoryDescription = inputValidator.isDescriptionValid(categoryDescription);

		List<String> messageKeyList = new ArrayList<>();

		if (!validCategoryTitle) {
			messageKeyList.add("message.cat_title_not_valid"); // done
			isValid = false;
		}
		if (!validCategoryDescription) {
			messageKeyList.add("message.cat_desc_not_valid"); // done
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
