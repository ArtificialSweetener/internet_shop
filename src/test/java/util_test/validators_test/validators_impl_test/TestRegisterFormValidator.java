package util_test.validators_test.validators_impl_test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import util.validators.impl.RegisterFormValidator;
import util.Messages;
import util.validators.FormValidator;
import util.validators.InputValidator;

public class TestRegisterFormValidator { // almost done, write more cases

	private HttpServletRequest request;
	private InputValidator inputValidator;
	private FormValidator registerFormValidator;
	private HttpSession session;

	@Before
	public void setUp() {
		request = mock(HttpServletRequest.class);
		session = mock(HttpSession.class);
		inputValidator = mock(InputValidator.class);
		registerFormValidator = new RegisterFormValidator(inputValidator);
	}

	@Test
	public void testValidateWithAllValidInputs() {
		// arrange
		when(request.getParameter("user_name")).thenReturn("John");
		when(request.getParameter("user_surname")).thenReturn("Doe");
		when(request.getParameter("user_email")).thenReturn("john.doe@example.com");
		when(request.getParameter("user_password")).thenReturn("Password123");
		when(inputValidator.isNameValid("John")).thenReturn(true);
		when(inputValidator.isSurnameValid("Doe")).thenReturn(true);
		when(inputValidator.isEmailValid("john.doe@example.com")).thenReturn(true);
		when(inputValidator.isPasswordValid("Password123")).thenReturn(true);
		// act
		boolean isValid = registerFormValidator.validate(request);
		// assert
		assertTrue(isValid);
	}

	@Test
	public void testValidateWithInvalidInputs() {
		// arrange
		when(request.getParameter("user_name")).thenReturn("");
		when(request.getParameter("user_surname")).thenReturn("");
		when(request.getParameter("user_email")).thenReturn("john.doe@");
		when(request.getParameter("user_password")).thenReturn("");
		when(inputValidator.isNameValid("")).thenReturn(false);
		when(inputValidator.isSurnameValid("")).thenReturn(false);
		when(inputValidator.isEmailValid("john.doe@")).thenReturn(false);
		when(inputValidator.isPasswordValid("")).thenReturn(false);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("language")).thenReturn(null);
		// act
		List<String> messageKeyList = new ArrayList<>();
		messageKeyList.add("message.user_name_not_valid");
		messageKeyList.add("message.user_surname_not_valid");
		messageKeyList.add("message.user_email_not_valid");
		messageKeyList.add("message.user_password_not_valid");

		String expectedMessagesString = String.join("<br>",
				Messages.getInstance().getMessagesForLocale(messageKeyList, Locale.ENGLISH));
		boolean isValid = registerFormValidator.validate(request);
		// assert
		assertFalse(isValid);
		Mockito.verify(session).setAttribute("message", expectedMessagesString);
	}
}