package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import dao.LocalizationDao;
import dbconnection_pool.ConnectionPool;
import exception.DataProcessingException;


public class LocalizationDaoImpl implements LocalizationDao {

	@Override
	public Optional<Map<String, String>> getMapByLanguage(String language) {
		System.out.println("getMapByLanguage invoked");
		System.out.println(language);
		String query = "SELECT * FROM translations WHERE lang_code = ?";
		try (Connection connection = ConnectionPool.getInstance().getConnection();
				PreparedStatement getMapByLanguageStatement = connection.prepareStatement(query)) {
			getMapByLanguageStatement.setString(1, language);
			ResultSet resultSet = getMapByLanguageStatement.executeQuery();
			Map<String, String> messagesByLang = null;
			if (resultSet.next() == false) {
				System.out.println("ResultSet in empty in Java");
			} else {
				messagesByLang = new HashMap<>();
				do {
					String key = resultSet.getString("str_key");
					String value = resultSet.getString("str_value");
					messagesByLang.put(key, value);
				} while (resultSet.next());
			}
			return Optional.ofNullable(messagesByLang);
		} catch (SQLException e) {
			throw new DataProcessingException("Couldn't get map by language" + language, e);
		}
	}

}
