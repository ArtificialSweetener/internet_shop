package service_test.impl_test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dao.ProductDao;
import models.Cart;
import models.Product;
import service.ProductService;
import service.impl.ProductServiceImpl;

public class TestProductServiceImpl {// done
	@Mock
	private ProductDao productDao;

	private ProductService productService;

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
	public void testGetCartProduct() {
		List<Cart> cartList = createSampleCartList();
		List<Cart> expectedProducts = createExpectedProductsList();

		when(productDao.getCartProduct(cartList)).thenReturn(expectedProducts);
		List<Cart> actualProducts = productService.getCartProduct(cartList);

		assertEquals(expectedProducts, actualProducts);
		verify(productDao).getCartProduct(cartList);
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

	private List<Cart> createSampleCartList() {
		List<Cart> cartList = new ArrayList<>();
		Cart cart1 = new Cart();
		cart1.setProductId(1L);
		cart1.setQuantityInCart(2);

		Cart cart2 = new Cart();
		cart2.setProductId(2L);
		cart2.setQuantityInCart(4);

		Cart cart3 = new Cart();
		cart3.setProductId(3L);
		cart3.setQuantityInCart(1);

		cartList.add(cart1);
		cartList.add(cart2);
		cartList.add(cart3);
		return cartList;
	}

	private List<Cart> createExpectedProductsList() {
		List<Cart> expectedProductsList = new ArrayList<>();
		Cart expectedProduct1 = new Cart();
		expectedProduct1.setProductId(1L);
		expectedProduct1.setProductName("Product 1");
		expectedProduct1.setProductDescription("This is a description of product 1");
		expectedProduct1.setColorId(1L);
		expectedProduct1.setCategoryId(1);
		expectedProduct1.setProductPrice(100);
		expectedProduct1.setProductQuantity(10);
		expectedProduct1.setQuantityInCart(2);
		expectedProduct1.setTotalPrice(200);
		expectedProduct1.setProductDate(LocalDate.of(2022, 1, 1));
		expectedProduct1.setProductTime(LocalTime.of(10, 10, 10));
		expectedProduct1.setProductPhotoName("photo1.jpeg");
		expectedProductsList.add(expectedProduct1);

		Cart expectedProduct2 = new Cart();
		expectedProduct2.setProductId(2L);
		expectedProduct2.setProductName("Product 2");
		expectedProduct2.setProductDescription("This is a description of product 2");
		expectedProduct2.setColorId(2L);
		expectedProduct2.setCategoryId(2);
		expectedProduct2.setProductPrice(200);
		expectedProduct2.setProductQuantity(5);
		expectedProduct2.setQuantityInCart(4);
		expectedProduct2.setTotalPrice(800);
		expectedProduct2.setProductDate(LocalDate.of(2022, 2, 2));
		expectedProduct2.setProductTime(LocalTime.of(11, 11, 11));
		expectedProduct2.setProductPhotoName("photo2.jpeg");
		expectedProductsList.add(expectedProduct2);

		Cart expectedProduct3 = new Cart();
		expectedProduct3.setProductId(3L);
		expectedProduct3.setProductName("Product 3");
		expectedProduct3.setProductDescription("This is a description of product 3");
		expectedProduct3.setColorId(3L);
		expectedProduct3.setCategoryId(3);
		expectedProduct3.setProductPrice(300);
		expectedProduct3.setProductQuantity(3);
		expectedProduct3.setQuantityInCart(1);
		expectedProduct3.setTotalPrice(300);
		expectedProduct3.setProductDate(LocalDate.of(2022, 3, 3));
		expectedProduct3.setProductTime(LocalTime.of(12, 12, 12));
		expectedProduct3.setProductPhotoName("photo3.jpeg");
		expectedProductsList.add(expectedProduct3);

		return expectedProductsList;
	}

}