package service.impl;

import java.util.List;
import java.util.Optional;

import dao.CategoryDao;
import models.Category;

import service.CategoryService;

public class CategoryServiceImpl implements CategoryService {
	private CategoryDao categoryDao;

	public CategoryServiceImpl(CategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}

	@Override
	public Category create(Category category) {
		return categoryDao.create(category);
	}

	@Override
	public Optional<Category> get(Long id) {
		return categoryDao.get(id);
	}

	@Override
	public List<Category> getAll() {
		return categoryDao.getAll();
	}

	@Override
	public Category update(Category category) {
		return categoryDao.update(category);
	}

	@Override
	public boolean delete(Long id) {
		return categoryDao.delete(id);
	}

}
