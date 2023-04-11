package service_test.impl_test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dao.ProductDao;
import models.Product;
import service.ProductService;
import service.impl.ProductServiceImpl;

public class TestProductServiceImpl {// done
	@Mock
	private ProductDao productDao;

	private ProductService productService;

	@SuppressWarnings("deprecation")
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		productService = new ProductServiceImpl(productDao);
	}

	@Test
	public void testCreate() {
		Product product = new Product();
		when(productDao.create(any(Product.class))).thenReturn(product);

		Product result = productService.create(product);

		assertEquals(product, result);
	}

	@Test
	public void testGet() {
		Long id = 1L;
		Product product = new Product();
		when(productDao.get(id)).thenReturn(Optional.of(product));

		Optional<Product> result = productService.get(id);

		assertTrue(result.isPresent());
		assertEquals(product, result.get());
	}

	@Test
	public void testGetAll() {
		List<Product> products = new ArrayList<>();
		when(productDao.getAll()).thenReturn(products);

		List<Product> result = productService.getAll();

		assertEquals(products, result);
	}

	@Test
	public void testUpdate() {
		Product product = new Product();
		when(productDao.update(any(Product.class))).thenReturn(product);

		Product result = productService.update(product);

		assertEquals(product, result);
	}

	@Test
	public void testDelete() {
		Long id = 1L;

		when(productDao.delete(id)).thenReturn(true);

		boolean result = productService.delete(id);

		assertTrue(result);
		verify(productDao).delete(id);
	}

	@Test
	public void testGetMinPrice() {
		double expectedMinPrice = 10.0;

		when(productDao.getMinPrice()).thenReturn(expectedMinPrice);

		double result = productService.getMinPrice();

		assertEquals(expectedMinPrice, result, 0.0);
		verify(productDao).getMinPrice();
	}

	@Test
	public void testGetMaxPrice() {
		double expectedMaxPrice = 100.0;

		when(productDao.getMaxPrice()).thenReturn(expectedMaxPrice);

		double result = productService.getMaxPrice();

		assertEquals(expectedMaxPrice, result, 0.0);
		verify(productDao).getMaxPrice();
	}


	@Test
	public void testGetAllPagination() {
		List<Product> productList = new ArrayList<>();
		Product product1 = new Product();
		product1.setProductId(1L);
		product1.setProductName("Product1");
		productList.add(product1);
		when(productDao.getAll(0, 5)).thenReturn(productList);
		List<Product> result = productService.getAll(0, 5);
		assertEquals(productList, result);
	}

	@Test
	public void testGetNoOfRecords() {
		when(productDao.getNoOfRecords()).thenReturn(5);
		int result = productService.getNoOfRecords();
		assertEquals(5, result);
	}

	@Test
	public void testGetAllBy() {
		List<Product> productList = new ArrayList<>();
		Product product1 = new Product();
		product1.setProductId(1L);
		product1.setProductName("Product1");
		productList.add(product1);
		when(productDao.getAllBy("1", "1", "0", "10", "name", 0, 5)).thenReturn(productList);
		List<Product> result = productService.getAllBy("1", "1", "0", "10", "name", 0, 5);
		assertEquals(productList, result);
	}

	@Test
	public void testGetNoDeleteCheck_Success() {
		Product product1 = new Product();
		product1.setProductId(1L);
		product1.setProductName("Product1");
		Optional<Product> optionalProduct = Optional.of(product1);
		when(productDao.getNoDeleteCheck(1L)).thenReturn(optionalProduct);
		Optional<Product> result = productService.getNoDeleteCheck(1L);
		assertEquals(optionalProduct, result);
	}

}