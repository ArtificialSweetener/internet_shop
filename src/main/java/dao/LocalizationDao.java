package dao;

import java.util.Map;
import java.util.Optional;

public interface LocalizationDao {
	public Optional<Map<String, String>> getMapByLanguage(String language);
}
