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
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import util.validators.impl.CreateOrderFormValidator;
import util.Messages;
import util.validators.InputValidator;

@RunWith(MockitoJUnitRunner.class)
public class TestCreateOrderFormValidator {// done completely

	@Mock
	private InputValidator inputValidator;

	@Mock
	private HttpServletRequest req;
	private HttpSession session;
	private CreateOrderFormValidator createOrderFormValidator;

	@Before
	public void setup() {
		req = mock(HttpServletRequest.class);
		session = mock(HttpSession.class);
		createOrderFormValidator = new CreateOrderFormValidator(inputValidator);
	}

	@Test
	public void testValidForm() {
		when(req.getParameter("orderCountryCode")).thenReturn("38");
		when(req.getParameter("orderPhone")).thenReturn("5555555555");
		when(req.getParameter("orderAddress")).thenReturn("123 Main St");
		when(inputValidator.isCountryCodeValid("38")).thenReturn(true);
		when(inputValidator.isMobilePhoneValid("5555555555")).thenReturn(true);
		when(inputValidator.isAddressValid("123 Main St")).thenReturn(true);

		boolean result = createOrderFormValidator.validate(req);

		assertTrue(result);
	}

	@Test
	public void testInvalidForm() {
		when(req.getParameter("orderCountryCode")).thenReturn("38");
		when(req.getParameter("orderPhone")).thenReturn("5555555555");
		when(req.getParameter("orderAddress")).thenReturn("");
		when(inputValidator.isCountryCodeValid("38")).thenReturn(true);
		when(inputValidator.isMobilePhoneValid("5555555555")).thenReturn(true);
		when(inputValidator.isAddressValid("")).thenReturn(false);
		when(req.getSession()).thenReturn(session);
		when(session.getAttribute("language")).thenReturn(null);

		String message = "message.order_address_not_valid";
		String expectedMessage = ResourceBundle.getBundle("message", Locale.ENGLISH).getString(message);
		boolean result = createOrderFormValidator.validate(req);
		assertFalse(result);
		Mockito.verify(session).setAttribute("message", expectedMessage);
	}

	@Test
	public void testInvalidFormPhone() {
		when(req.getParameter("orderCountryCode")).thenReturn("38");
		when(req.getParameter("orderPhone")).thenReturn("5555555 555");
		when(req.getParameter("orderAddress")).thenReturn("123 Main St");
		when(inputValidator.isCountryCodeValid("38")).thenReturn(true);
		when(inputValidator.isAddressValid("123 Main St")).thenReturn(true);
		when(req.getSession()).thenReturn(session);
		when(session.getAttribute("language")).thenReturn(null);

		String message = "message.order_phone_not_valid";
		String expectedMessage = ResourceBundle.getBundle("message", Locale.ENGLISH).getString(message);
		boolean result = createOrderFormValidator.validate(req);
		assertFalse(result);
		Mockito.verify(session).setAttribute("message", expectedMessage);
	}

	@Test
	public void testInvalidFormCode() {
		when(req.getParameter("orderCountryCode")).thenReturn("1234");
		when(req.getParameter("orderPhone")).thenReturn("5555555 555");
		when(req.getParameter("orderAddress")).thenReturn("123 Main St");
		when(inputValidator.isMobilePhoneValid("5555555555")).thenReturn(true);
		when(inputValidator.isAddressValid("123 Main St")).thenReturn(true);
		when(req.getSession()).thenReturn(session);
		when(session.getAttribute("language")).thenReturn(null);

		String message = "message.order_phone_not_valid";
		String expectedMessage = ResourceBundle.getBundle("message", Locale.ENGLISH).getString(message);
		boolean result = createOrderFormValidator.validate(req);
		assertFalse(result);
		Mockito.verify(session).setAttribute("message", expectedMessage);
	}

	@Test
	public void testAllValuesInvalid() {
		when(req.getParameter("orderCountryCode")).thenReturn("38");
		when(req.getParameter("orderPhone")).thenReturn("5555 555555");
		when(req.getParameter("orderAddress")).thenReturn("");
		when(inputValidator.isCountryCodeValid("38")).thenReturn(true);
		when(inputValidator.isAddressValid("")).thenReturn(false);
		when(req.getSession()).thenReturn(session);
		when(session.getAttribute("language")).thenReturn(null);

		List<String> messageKeyList = new ArrayList<>();
		messageKeyList.add("message.order_phone_not_valid");
		messageKeyList.add("message.order_address_not_valid");

		String expectedMessagesString = String.join("<br>",
				Messages.getInstance().getMessagesForLocale(messageKeyList, Locale.ENGLISH));
		boolean result = createOrderFormValidator.validate(req);
		assertFalse(result);
		Mockito.verify(session).setAttribute("message", expectedMessagesString);
	}

}
