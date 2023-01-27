package service;

import java.util.Map;
import java.util.Optional;

public interface LocalizationService {
	Optional<Map<String, String>> getMapByLanguage(String language);
}
