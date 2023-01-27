import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import dao.ProductDao;
import models.Product;
import service.ProductService;
import service.impl.ProductServiceImpl;

@RunWith(MockitoJUnitRunner.class)

public class ProductServiceImplTest {

	@Mock
	private ProductDao productDao;

	private ProductService productService;

	@Before
	public void setUp() {
		productService = new ProductServiceImpl(productDao);
	}

	@Test
	public void testCreate() {
		Product product = new Product("Test Product", "Test Description", 1, 1, 10.0, 10, LocalDate.now(),
				LocalTime.now(), new byte[0], "test.jpg");
		when(productDao.create(product)).thenReturn(product);
		Product result = productService.create(product);
		assertEquals(product, result);
	}

	@Test
	public void testGet() {
		Product product = new Product(1L, "Test Product", "Test Description", 1, 1, 10.0, 10, LocalDate.now(),
				LocalTime.now(), new byte[0], "test.jpg");
		when(productDao.get(1L)).thenReturn(Optional.of(product));
		Product result = productService.get(1L);
		assertEquals(product, result);
	}

	@Test
	public void testGetAll() {
		Product product1 = new Product(1L, "Test Product 1", "Test Description 1", 1, 1, 10.0, 10, LocalDate.now(),
				LocalTime.now(), new byte[0], "test1.jpg");
		Product product2 = new Product(2L, "Test Product 2", "Test Description 2", 2, 2, 20.0, 20, LocalDate.now(),
				LocalTime.now(), new byte[0], "test2.jpg");
		when(productDao.getAll()).thenReturn(List.of(product1, product2));
		List<Product> result = productService.getAll();
		assertEquals(List.of(product1, product2), result);
	}

	@Test
	public void testUpdate() {
		Product product = new Product(1L, "Test Product", "Test Description", 1, 1, 10.0, 10, LocalDate.now(),
				LocalTime.now(), new byte[0], "test.jpg");
		when(productDao.update(product)).thenReturn(product);
		Product result = productService.update(product);
		assertEquals(product, result);
	}

	@Test
	public void testDelete() {
		when(productDao.delete(1L)).thenReturn(true);
		boolean result = productService.delete(1L);
		assertEquals(true, result);
	}

	@Test
	public void testGetMinPrice() {
		when(productDao.getMinPrice()).thenReturn(10.0);

		double minPrice = productService.getMinPrice();
		assertEquals(10.0, minPrice, 0.0);
	}

	@Test
	public void testGetMaxPrice() {
		when(productDao.getMaxPrice()).thenReturn(20.0);

		double maxPrice = productService.getMaxPrice();
		assertEquals(20.0, maxPrice, 0.0);

	}

//get cart product test needs to be done

	@Test
	public void testGetAllPagination() {
		int offset = 0;
		int noOfRecords = 10;
		List<Product> productList = Arrays.asList(
				new Product("product1", "description1", 1, 1, 10.0, 10, LocalDate.now(), LocalTime.now(), null,
						"photo1"),
				new Product("product2", "description2", 2, 2, 20.0, 20, LocalDate.now(), LocalTime.now(), null,
						"photo2"));
		when(productDao.getAll(offset, noOfRecords)).thenReturn(productList);
		List<Product> result = productService.getAll(offset, noOfRecords);
		assertEquals(productList, result);
	}

	@Test
	public void testGetNoOfRecords() {
		when(productDao.getNoOfRecords()).thenReturn(10);
		int result = productService.getNoOfRecords();
		assertEquals(10, result);
	}

	@Test
	public void testGetAllBy() {
		List<Product> products = Arrays.asList(new Product(1L, "product1", "description1", 1L, 1L, 10.0, 10,
				LocalDate.now(), LocalTime.now(), new byte[1], "photo1"));
		when(productDao.getAllBy("1", "1", "0", "100", "price", 0, 10)).thenReturn(Optional.of(products));
		Optional<List<Product>> result = productService.getAllBy("1", "1", "0", "100", "price", 0, 10);
		assertEquals(Optional.of(products), result);
	}

}