package service.impl;

import java.util.List;
import java.util.Optional;
import dao.ProductDao;
import models.Product;

import service.ProductService;

public class ProductServiceImpl implements ProductService {
	private ProductDao productDao;

	public ProductServiceImpl(ProductDao productDao) {
		super();
		this.productDao = productDao;
	}

	@Override
	public Product create(Product product) {
		return productDao.create(product);
	}

	@Override
	public Optional<Product> get(Long id) {
		return productDao.get(id);
	}

	@Override
	public List<Product> getAll() {
		return productDao.getAll();
	}

	@Override
	public Product update(Product product) {
		return productDao.update(product);
	}

	@Override
	public boolean delete(Long id) {

		return productDao.delete(id);
	}

	@Override
	public double getMinPrice() {
		return productDao.getMinPrice();
	}

	@Override
	public double getMaxPrice() {
		return productDao.getMaxPrice();
	}

	@Override
	public List<Product> getAll(int offset, int noOfRecords) {
		return productDao.getAll(offset, noOfRecords);
	}

	@Override
	public int getNoOfRecords() {
		// TODO Auto-generated method stub
		return productDao.getNoOfRecords();
	}

	public List<Product> getAllBy(String categoryId, String colorId, String min, String max, String sort,
			int offset, int noOfRecords) {
		// TODO Auto-generated method stub
		return productDao.getAllBy(categoryId, colorId, min, max, sort, offset, noOfRecords);
	}

	@Override
	public Optional<Product> getNoDeleteCheck(Long id) {
		// TODO Auto-generated method stub
		return productDao.getNoDeleteCheck(id);
	}
	

}
