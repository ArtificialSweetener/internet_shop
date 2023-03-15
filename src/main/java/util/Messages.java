package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Messages is a utility class used for retrieving localized messages from a
 * ResourceBundle. It provides methods to retrieve a single message or a list of
 * messages for a specific locale.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */
public class Messages {

	public Messages() {
	}

	private static Messages instance = null;

	public static Messages getInstance() {
		if (instance == null) {
			instance = new Messages();
		}
		return instance;
	}

	/**
	 * Returns a message for a specified message key and locale.
	 * 
	 * @param messageKey The message key to retrieve the message for.
	 * @param locale     The locale to retrieve the message in.
	 * @return The message corresponding to the message key in the specified locale.
	 */
	public String getMessageForLocale(String messageKey, Locale locale) {
		return ResourceBundle.getBundle("message", locale).getString(messageKey);
	}

	/**
	 * Returns a list of messages for a specified list of message keys and locale.
	 * 
	 * @param messageKeys The list of message keys to retrieve the messages for.
	 * @param locale      The locale to retrieve the messages in.
	 * @return A list of messages corresponding to the message keys in the specified
	 *         locale.
	 */
	public List<String> getMessagesForLocale(List<String> messageKeys, Locale locale) {
		List<String> messages = new ArrayList<>();
		for (String messageKey : messageKeys) {
			messages.add(ResourceBundle.getBundle("message", locale).getString(messageKey));
		}
		return messages;
	}
}
