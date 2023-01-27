package util;

import java.util.Map;
import java.util.Optional;

import service.LocalizationService;

public class GlobalStringsProvider {
	private Map<String, String> localizationStrings;
	private String language;

	private static GlobalStringsProvider instance = null;

	public GlobalStringsProvider() {
	}

	public static GlobalStringsProvider getInstance() {
		if (instance == null) {
			instance = new GlobalStringsProvider();
		}
		return instance;
	}

	public void setStringsMapByLanguage(LocalizationService localizationService) {
		Optional<Map<String, String>> localizationStringsOptional = localizationService.getMapByLanguage(this.language);
		if (localizationStringsOptional.isPresent()) {
			this.localizationStrings = localizationStringsOptional.get();
		} else {
			System.out.println("No translations for this language availaible");
		}
	}

	public Map<String, String> getLocalizationMap() {
		return localizationStrings;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language.toUpperCase();
	}

}
