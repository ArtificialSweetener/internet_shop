package util_test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

import util.Messages;

public class TestMessages {//done

	@Test
	public void testGetMessageForLocale() {
		Locale locale = new Locale("en", "US");
		String messageKey = "label.password";
		String expectedMessage = "Password";

		String actualMessage = Messages.getInstance().getMessageForLocale(messageKey, locale);

		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void testGetMessagesForLocale() {
		Locale locale = new Locale("en", "US");
		List<String> messageKeys = Arrays.asList("label.password", "label.email");
		List<String> expectedMessages = Arrays.asList("Password", "Email");

		List<String> actualMessages = Messages.getInstance().getMessagesForLocale(messageKeys, locale);

		assertEquals(expectedMessages, actualMessages);
	}
}