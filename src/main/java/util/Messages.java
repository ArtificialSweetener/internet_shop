package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

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

	public String getMessageForLocale(String messageKey, Locale locale) {
		return ResourceBundle.getBundle("message", locale).getString(messageKey);
	}
	
	public List<String> getMessagesForLocale(List<String> messageKeys, Locale locale) { 
		List<String> messages = new ArrayList<>();
		for(String messageKey:messageKeys) {
			messages.add(ResourceBundle.getBundle("message", locale).getString(messageKey));
		}
		return messages;
	}
}
