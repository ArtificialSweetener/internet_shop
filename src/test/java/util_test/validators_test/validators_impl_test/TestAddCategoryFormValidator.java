package util_test.validators_test.validators_impl_test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import util.Messages;
import util.validators.FormValidator;
import util.validators.InputValidator;
import util.validators.impl.AddCategoryFormValidator;

public class TestAddCategoryFormValidator {// done

	private FormValidator validator;
	private HttpServletRequest request;
	private InputValidator inputValidator;
	private HttpSession session;

	@Before
	public void setUp() {
		inputValidator = Mockito.mock(InputValidator.class);
		validator = new AddCategoryFormValidator(inputValidator);
		request = Mockito.mock(HttpServletRequest.class);
		session = Mockito.mock(HttpSession.class);
	}

	@Test
	public void testValidateWithValidTitleAndDescription() {
		// Given
		Mockito.when(request.getParameter("catTitle")).thenReturn("Valid title");
		Mockito.when(request.getParameter("catDescription")).thenReturn("Valid description");
		Mockito.when(inputValidator.isTitleValid("Valid title")).thenReturn(true);
		Mockito.when(inputValidator.isDescriptionValid("Valid description")).thenReturn(true);

		// When
		boolean result = validator.validate(request);

		// Then
		assertTrue(result);
		Mockito.verify(inputValidator).isTitleValid("Valid title");
		Mockito.verify(inputValidator).isDescriptionValid("Valid description");
	}

	@Test
	public void testValidateWithInvalidTitleAndValidDescription() {
		// Given
		Mockito.when(request.getParameter("catTitle")).thenReturn("a");
		Mockito.when(request.getParameter("catDescription")).thenReturn("valid description");
		Mockito.when(inputValidator.isTitleValid("a")).thenReturn(false);
		Mockito.when(inputValidator.isDescriptionValid("valid description")).thenReturn(true);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("language")).thenReturn(null);

		List<String> messageKeys = new ArrayList<>();
		messageKeys.add("message.cat_title_not_valid");
		String expectedMessagesString = String.join("<br>",
				Messages.getInstance().getMessagesForLocale(messageKeys, Locale.ENGLISH));

		// When
		boolean result = validator.validate(request);

		// Then
		assertFalse(result);
		Mockito.verify(session).setAttribute("message", expectedMessagesString);
		Mockito.verify(inputValidator).isTitleValid("a");
		Mockito.verify(inputValidator).isDescriptionValid("valid description");
	}

	@Test
	public void testValidateWithValidTitleAndInvalidDescription() {
		// Given
		Mockito.when(request.getParameter("catTitle")).thenReturn("Valid title");
		Mockito.when(request.getParameter("catDescription")).thenReturn("a");
		Mockito.when(inputValidator.isTitleValid("Valid title")).thenReturn(true);
		Mockito.when(inputValidator.isDescriptionValid("a")).thenReturn(false);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("language")).thenReturn(null);

		List<String> messageKeys = new ArrayList<>();
		messageKeys.add("message.cat_desc_not_valid");
		String expectedMessagesString = String.join("<br>",
				Messages.getInstance().getMessagesForLocale(messageKeys, Locale.ENGLISH));

		// When
		boolean result = validator.validate(request);

		// Then
		assertFalse(result);
		Mockito.verify(session).setAttribute("message", expectedMessagesString);
		Mockito.verify(inputValidator).isTitleValid("Valid title");
		Mockito.verify(inputValidator).isDescriptionValid("a");
	}

	@Test
	public void testValidateWithInvalidTitleAndInvalidDescription() {
		// Given
		Mockito.when(request.getParameter("catTitle")).thenReturn("a");
		Mockito.when(request.getParameter("catDescription")).thenReturn("b");
		Mockito.when(inputValidator.isTitleValid("a")).thenReturn(false);
		Mockito.when(inputValidator.isDescriptionValid("b")).thenReturn(false);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("language")).thenReturn(null);

		List<String> messageKeys = new ArrayList<>();
		messageKeys.add("message.cat_title_not_valid");
		messageKeys.add("message.cat_desc_not_valid");
		String expectedMessagesString = String.join("<br>",
				Messages.getInstance().getMessagesForLocale(messageKeys, Locale.ENGLISH));

		// When
		boolean result = validator.validate(request);

		// Then
		assertFalse(result);
		Mockito.verify(session).setAttribute("message", expectedMessagesString);
		Mockito.verify(inputValidator).isTitleValid("a");
		Mockito.verify(inputValidator).isDescriptionValid("b");
	}
}