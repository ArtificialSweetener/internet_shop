package util_test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import util.MessageAttributeUtil;

public class TestMessageAttributeUtil { // done
	private HttpServletRequest req;
	private HttpSession session;

	@Before
	public void setUp() {
		req = Mockito.mock(HttpServletRequest.class);
		session = Mockito.mock(HttpSession.class);
		Mockito.when(req.getSession()).thenReturn(session);
	}

	@Test
	public void testSetMessageAttribute_WithLanguageAttribute() {
		String message = "label.email";
		String languageTag = "en";
		Locale locale = Locale.forLanguageTag(languageTag);
		String expectedMessage = ResourceBundle.getBundle("message", locale).getString(message);
		Mockito.when(session.getAttribute("language")).thenReturn(languageTag);
		// System.out.println(expectedMessage);
		MessageAttributeUtil.setMessageAttribute(req, message);

		Mockito.verify(session).setAttribute("message", expectedMessage);
	}

	@Test
	public void testSetMessageAttribute_WithoutLanguageAttribute() {
		String message = "label.email";
		Locale locale = Locale.ENGLISH;
		String expectedMessage = ResourceBundle.getBundle("message", locale).getString(message);
		Mockito.when(session.getAttribute("language")).thenReturn(null);

		MessageAttributeUtil.setMessageAttribute(req, message);

		Mockito.verify(session).setAttribute("message", expectedMessage);
	}

	@Test
	public void testSetMessageAttributeWithList_WithLanguageAttribute() {
		List<String> messageKeys = new ArrayList<>();
		messageKeys.add("label.email");
		messageKeys.add("label.password");
		String languageTag = "en";
		Locale locale = Locale.forLanguageTag(languageTag);
		List<String> expectedMessages = new ArrayList<>();
		for (String messageKey : messageKeys) {
			expectedMessages.add(ResourceBundle.getBundle("message", locale).getString(messageKey));
		}
		String expectedMessagesString = String.join("<br>", expectedMessages);
		Mockito.when(session.getAttribute("language")).thenReturn(languageTag);

		MessageAttributeUtil.setMessageAttributeWithList(req, messageKeys);

		Mockito.verify(session).setAttribute("message", expectedMessagesString);
	}

	@Test
	public void testSetMessageAttributeWithList_WithoutLanguageAttribute() {
		List<String> messageKeys = new ArrayList<>();
		messageKeys.add("label.email");
		messageKeys.add("label.password");
		Locale locale = Locale.ENGLISH;
		List<String> expectedMessages = new ArrayList<>();
		for (String messageKey : messageKeys) {
			expectedMessages.add(ResourceBundle.getBundle("message", locale).getString(messageKey));
		}
		String expectedMessagesString = String.join("<br>", expectedMessages);
		Mockito.when(session.getAttribute("language")).thenReturn(null);

		MessageAttributeUtil.setMessageAttributeWithList(req, messageKeys);

		Mockito.verify(session).setAttribute("message", expectedMessagesString);
	}

}
