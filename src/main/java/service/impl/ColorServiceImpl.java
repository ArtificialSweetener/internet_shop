package service.impl;

import java.util.List;
import java.util.Optional;

import dao.ColorDao;
import models.Color;

import service.ColorService;

public class ColorServiceImpl implements ColorService{
	private ColorDao colorDao;
	
	
	public ColorServiceImpl(ColorDao colorDao) {
		this.colorDao = colorDao;
	}

	@Override
	public Color create(Color color) {
		return colorDao.create(color);
	}

	@Override
	public Optional<Color> get(Long id) {
		return colorDao.get(id);
	}

	@Override
	public List<Color> getAll() {
		return colorDao.getAll();
	}

	@Override
	public Color update(Color color) {
		return colorDao.update(color);
	}

	@Override
	public boolean delete(Long id) {
		return colorDao.delete(id);
	}

}
