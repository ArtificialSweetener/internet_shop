package util;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

/**
 * MessageAttributeUtil is a utility class used for setting localized messages
 * as attributes on an HttpServletRequest. It provides methods to set a message
 * attribute for a single message or a list of messages.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */

public class MessageAttributeUtil {
	/**
	 * Sets a message attribute on the HttpServletRequest for a specified message
	 * key. The message is retrieved from the ResourceBundle based on the language
	 * in the user's session. If no language is found in the session, English is
	 * used as the default.
	 * 
	 * @param req     The HttpServletRequest to set the message attribute on.
	 * @param message The message key to retrieve the message for.
	 */
	public static void setMessageAttribute(HttpServletRequest req, String message) {
		Optional<Object> languageOpt = Optional.ofNullable(req.getSession().getAttribute("language"));
		if (languageOpt.isPresent()) {
			req.getSession().setAttribute("message", Messages.getInstance().getMessageForLocale(message,
					Locale.forLanguageTag((String) languageOpt.get())));

		} else {
			req.getSession().setAttribute("message",
					Messages.getInstance().getMessageForLocale(message, Locale.ENGLISH));
		}
	}

	/**
	 * Sets a message attribute on the HttpServletRequest for a specified list of
	 * message keys. The messages are retrieved from the ResourceBundle based on the
	 * language in the user's session. If no language is found in the session,
	 * English is used as the default. The messages are concatenated with a line
	 * break between each message.
	 * 
	 * @param req         The HttpServletRequest to set the message attribute on.
	 * @param messageKeys The list of message keys to retrieve the messages for.
	 */
	public static void setMessageAttributeWithList(HttpServletRequest req, List<String> messageKeys) {
		Optional<Object> languageOpt = Optional.ofNullable(req.getSession().getAttribute("language"));
		if (languageOpt.isPresent()) {
			List<String> messages = Messages.getInstance().getMessagesForLocale(messageKeys,
					Locale.forLanguageTag((String) languageOpt.get()));
			String messagesString = String.join("<br>", messages);
			req.getSession().setAttribute("message", messagesString);
		} else {
			List<String> messages = Messages.getInstance().getMessagesForLocale(messageKeys, Locale.ENGLISH);
			String messagesString = String.join("<br>", messages);
			req.getSession().setAttribute("message", messagesString);
		}
	}

}
