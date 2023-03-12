package util;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

public class MessageAttributeUtil {
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

	public static void setMessageAttributeWithList(HttpServletRequest req, List<String> messageKeys) {
		Optional<Object> languageOpt = Optional.ofNullable(req.getSession().getAttribute("language"));
		// System.out.println(languageOpt.toString());
		if (languageOpt.isPresent()) {
			List<String> messages = Messages.getInstance().getMessagesForLocale(messageKeys,
					Locale.forLanguageTag((String) languageOpt.get()));
			String messagesString = String.join("<br>", messages);
			req.getSession().setAttribute("message", messagesString);
		} else {
			List<String> messages = Messages.getInstance().getMessagesForLocale(messageKeys,
					Locale.ENGLISH);
			String messagesString = String.join("<br>", messages);
			req.getSession().setAttribute("message", messagesString);
		}
	}

}
