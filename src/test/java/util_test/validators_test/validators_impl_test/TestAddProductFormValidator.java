package util_test.validators_test.validators_impl_test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import util.Messages;
import util.validators.FormValidator;
import util.validators.InputValidator;
import util.validators.impl.AddProductFormValidator;

public class TestAddProductFormValidator { // done and is a good model for others
	private FormValidator addProductFormValidator;

	@Mock
	private InputValidator inputValidator;

	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpSession session;

	@Before
	public void setUp() {
		inputValidator = mock(InputValidator.class);
		addProductFormValidator = new AddProductFormValidator(inputValidator);
		request = mock(HttpServletRequest.class);
		session = mock(HttpSession.class);
	}

	@Test
	public void testValidate_whenAllValuesAreValid_thenReturnTrue() {
		when(request.getParameter("pName")).thenReturn("Valid Product Name");
		when(request.getParameter("pDescription")).thenReturn("Valid Product Description");
		when(request.getParameter("pPrice")).thenReturn("10.00");
		when(request.getParameter("pQuantity")).thenReturn("100");
		when(inputValidator.isTitleValid("Valid Product Name")).thenReturn(true);
		when(inputValidator.isDescriptionValid("Valid Product Description")).thenReturn(true);
		when(inputValidator.isPriceValid("10.00")).thenReturn(true);
		when(inputValidator.isNumberValid("100")).thenReturn(true);

		assertTrue(addProductFormValidator.validate(request));
	}

	@Test
	public void testValidate_whenProductNameIsInvalid_thenReturnFalse() {
		when(request.getParameter("pName")).thenReturn("");
		when(request.getParameter("pDescription")).thenReturn("Valid Product Description");
		when(request.getParameter("pPrice")).thenReturn("10");
		when(request.getParameter("pQuantity")).thenReturn("100");
		when(inputValidator.isTitleValid("")).thenReturn(false);
		when(inputValidator.isDescriptionValid("Valid Product Description")).thenReturn(true);
		when(inputValidator.isPriceValid("10")).thenReturn(true);
		when(inputValidator.isNumberValid("100")).thenReturn(true);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("language")).thenReturn(null);

		String message = "message.product_name_not_valid";
		String expectedMessage = ResourceBundle.getBundle("message", Locale.ENGLISH).getString(message);

		assertFalse(addProductFormValidator.validate(request));
		Mockito.verify(session).setAttribute("message", expectedMessage);
	}

	@Test
	public void testValidate_whenProductDescriptionIsInvalid_thenReturnFalse() {
		when(request.getParameter("pName")).thenReturn("Valid Product Name");
		when(request.getParameter("pDescription")).thenReturn("");
		when(request.getParameter("pPrice")).thenReturn("10");
		when(request.getParameter("pQuantity")).thenReturn("100");
		when(inputValidator.isTitleValid("Valid Product Name")).thenReturn(true);
		when(inputValidator.isDescriptionValid("")).thenReturn(false);
		when(inputValidator.isPriceValid("10")).thenReturn(true);
		when(inputValidator.isNumberValid("100")).thenReturn(true);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("language")).thenReturn(null);

		String message = "message.product_desc_not_valid";
		String expectedMessage = ResourceBundle.getBundle("message", Locale.ENGLISH).getString(message);

		assertFalse(addProductFormValidator.validate(request));
		Mockito.verify(session).setAttribute("message", expectedMessage);
	}

	@Test
	public void testValidate_whenProductPriceIsInvalid_thenReturnFalse() {
		when(request.getParameter("pName")).thenReturn("Valid Product Name");
		when(request.getParameter("pDescription")).thenReturn("Valid Product Description");
		when(request.getParameter("pPrice")).thenReturn(".10");
		when(request.getParameter("pQuantity")).thenReturn("100");
		when(inputValidator.isTitleValid("Valid Product Name")).thenReturn(true);
		when(inputValidator.isDescriptionValid("Valid Product Description")).thenReturn(true);
		when(inputValidator.isPriceValid(".10")).thenReturn(false);
		when(inputValidator.isNumberValid("100")).thenReturn(true);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("language")).thenReturn(null);

		String message = "message.product_price_not_valid";
		String expectedMessage = ResourceBundle.getBundle("message", Locale.ENGLISH).getString(message);

		assertFalse(addProductFormValidator.validate(request));
		Mockito.verify(session).setAttribute("message", expectedMessage);
	}

	@Test
	public void testValidate_whenProductQuantityIsInvalid_thenReturnFalse() {
		when(request.getParameter("pName")).thenReturn("Valid Product Name");
		when(request.getParameter("pDescription")).thenReturn("Valid Product Description");
		when(request.getParameter("pPrice")).thenReturn("10.00");
		when(request.getParameter("pQuantity")).thenReturn(" ");
		when(inputValidator.isTitleValid("Valid Product Name")).thenReturn(true);
		when(inputValidator.isDescriptionValid("Valid Product Description")).thenReturn(true);
		when(inputValidator.isPriceValid("10.00")).thenReturn(true);
		when(inputValidator.isNumberValid(" ")).thenReturn(false);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("language")).thenReturn(null);

		String message = "message.product_quantity_not_valid";
		String expectedMessage = ResourceBundle.getBundle("message", Locale.ENGLISH).getString(message);

		assertFalse(addProductFormValidator.validate(request));
		Mockito.verify(session).setAttribute("message", expectedMessage);
	}

	@Test
	public void testValidate_whenAllValuesAreNotValid_thenReturnFalse() {
		when(request.getParameter("pName")).thenReturn(" ");
		when(request.getParameter("pDescription")).thenReturn(" ");
		when(request.getParameter("pPrice")).thenReturn(".00");
		when(request.getParameter("pQuantity")).thenReturn("a");
		when(inputValidator.isTitleValid(" ")).thenReturn(false);
		when(inputValidator.isDescriptionValid(" ")).thenReturn(false);
		when(inputValidator.isPriceValid(".00")).thenReturn(false);
		when(inputValidator.isNumberValid("a")).thenReturn(false);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("language")).thenReturn(null);

		List<String> messageKeyList = new ArrayList<>();
		messageKeyList.add("message.product_name_not_valid");
		messageKeyList.add("message.product_desc_not_valid");
		messageKeyList.add("message.product_price_not_valid");
		messageKeyList.add("message.product_quantity_not_valid");

		String expectedMessagesString = String.join("<br>",
				Messages.getInstance().getMessagesForLocale(messageKeyList, Locale.ENGLISH));

		assertFalse(addProductFormValidator.validate(request));
		Mockito.verify(session).setAttribute("message", expectedMessagesString);
	}

	@Test
	public void testValidate_withMixedValues_thenReturnFalse() {
		when(request.getParameter("pName")).thenReturn("Valid Product Name");
		when(request.getParameter("pDescription")).thenReturn(" ");
		when(request.getParameter("pPrice")).thenReturn("10.00");
		when(request.getParameter("pQuantity")).thenReturn("a");
		when(inputValidator.isTitleValid("Valid Product Name")).thenReturn(true);
		when(inputValidator.isDescriptionValid(" ")).thenReturn(false);
		when(inputValidator.isPriceValid("10.00")).thenReturn(true);
		when(inputValidator.isNumberValid("a")).thenReturn(false);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("language")).thenReturn(null);

		List<String> messageKeyList = new ArrayList<>();
		messageKeyList.add("message.product_desc_not_valid");
		messageKeyList.add("message.product_quantity_not_valid");

		String expectedMessagesString = String.join("<br>",
				Messages.getInstance().getMessagesForLocale(messageKeyList, Locale.ENGLISH));

		assertFalse(addProductFormValidator.validate(request));
		Mockito.verify(session).setAttribute("message", expectedMessagesString);
	}
}
