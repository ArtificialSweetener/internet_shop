package service.impl;

import java.util.Map;
import java.util.Optional;

import dao.LocalizationDao;
import service.LocalizationService;

public class LocalizationServiceImpl implements LocalizationService {
	private LocalizationDao localizationDao;

	public LocalizationServiceImpl(LocalizationDao localizationDao) {
		this.localizationDao = localizationDao;
	}

	@Override
	public Optional<Map<String, String>> getMapByLanguage(String language) {
		// TODO Auto-generated method stub
		return localizationDao.getMapByLanguage(language);
	}

}
