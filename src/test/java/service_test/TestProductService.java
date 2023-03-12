package service_test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import dao.ProductDao;
import models.Cart;
import models.Product;
import service.ProductService;
import service.impl.ProductServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TestProductService { // refactor and add methods ??
	@Mock
	private ProductDao productDao;

	@InjectMocks
	private ProductService productService = new ProductServiceImpl(productDao);

	@Test
	public void testCreate() {
		Product expectedProduct = new Product();
		when(productService.create(any(Product.class))).thenReturn(expectedProduct);

		Product actualProduct = productService.create(new Product());

		assertEquals(expectedProduct, actualProduct);
	}

	@Test
	public void testGetProductById_Success() {
		long productId = 1L;
		Product expectedProduct = new Product();
		expectedProduct.setProductId(productId);
		expectedProduct.setProductName("Product 1");
		expectedProduct.setProductPrice(10.00);
		Optional<Product> expectedProductOpt = Optional.of(expectedProduct);
		when(productDao.get(productId)).thenReturn(expectedProductOpt);

		Optional<Product> actualProduct = productService.get(productId);

		assertNotNull(actualProduct.get());
		assertEquals(expectedProduct, actualProduct.get());
		verify(productDao, times(1)).get(productId);
	}

	@Test
	public void testGetProductById_NotFound() {
		long productId = 1L;
		Optional<Product> expectedProduct = Optional.empty();
		when(productDao.get(productId)).thenReturn(expectedProduct);

		Optional<Product> actualProduct = productService.get(productId);

		assertTrue(actualProduct.isEmpty());
		verify(productDao, times(1)).get(productId);
	}

	@Test
	public void testGetAllProducts_Success() {
		List<Product> expectedProducts = new ArrayList<>();
		Product product1 = new Product();
		product1.setProductId(1L);
		product1.setProductName("Product 1");
		product1.setProductPrice(10.00);
		expectedProducts.add(product1);

		Product product2 = new Product();
		product2.setProductId(2L);
		product2.setProductName("Product 2");
		product2.setProductPrice(20.00);
		expectedProducts.add(product2);
		when(productDao.getAll()).thenReturn(expectedProducts);

		List<Product> actualProducts = productService.getAll();

		assertNotNull(actualProducts);
		assertEquals(expectedProducts.size(), actualProducts.size());
		assertEquals(expectedProducts, actualProducts);
		verify(productDao, times(1)).getAll();
	}

	@Test
	public void testGetAllProducts_NoProducts() {
		List<Product> expectedProducts = new ArrayList<>();
		when(productDao.getAll()).thenReturn(expectedProducts);

		List<Product> products = productService.getAll();

		assertNotNull(products);
		assertTrue(products.isEmpty());
		verify(productDao, times(1)).getAll();
	}

	@Test
	public void testGetAllProductsWithPaginationSuccessfully() {
		Product product1 = new Product();
		product1.setProductId(1L);
		product1.setProductName("Test Product 1");
		product1.setProductPrice(20.00);

		Product product2 = new Product();
		product2.setProductId(1L);
		product2.setProductName("Test Product 2");
		product2.setProductPrice(30.00);
		List<Product> expectedProducts = Arrays.asList(product1, product2);
		when(productDao.getAll(0, Integer.MAX_VALUE)).thenReturn(expectedProducts);

		List<Product> actualProducts = productService.getAll(0, Integer.MAX_VALUE);

		assertNotNull(actualProducts);
		assertEquals(expectedProducts.size(), actualProducts.size());
		assertEquals(expectedProducts, actualProducts);
		verify(productDao, times(1)).getAll(0, Integer.MAX_VALUE);
	}

	@Test
	public void testUpdateProduct() {
		Product product = new Product();
		product.setProductId(1L);
		product.setProductName("Test Product");
		product.setProductPrice(20.00);

		when(productDao.update(product)).thenReturn(product);

		Product updatedProduct = productService.update(product);

		assertNotNull(updatedProduct);
		assertEquals(1L, updatedProduct.getProductId());
		assertEquals("Test Product", updatedProduct.getProductName());
		assertEquals(20.00, updatedProduct.getProductPrice(), 0);
		verify(productDao, times(1)).update(product);
	}

	@Test
	public void testDeleteProduct() {
		when(productDao.delete(1L)).thenReturn(true);

		boolean isProductDeleted = productService.delete(1L);

		assertTrue(isProductDeleted);
		verify(productDao, times(1)).delete(1L);
	}

	@Test
	public void testGetMinPrice() {
		double expectedMinPrice = 10.0;
		when(productDao.getMinPrice()).thenReturn(expectedMinPrice);

		double actualMinPrice = productService.getMinPrice();

		assertEquals(expectedMinPrice, actualMinPrice, 0.001);
	}

	@Test
	public void testGetMaxPrice() {
		double expectedMaxPrice = 20.0;
		when(productDao.getMaxPrice()).thenReturn(expectedMaxPrice);

		double actualMaxPrice = productService.getMaxPrice();

		assertEquals(expectedMaxPrice, actualMaxPrice, 0.001);
	}

	@Test
	public void testGetCartProduct() {
		List<Cart> expectedResult = new ArrayList<>();

		when(productDao.getCartProduct(anyList())).thenReturn(expectedResult);
		List<Cart> actualResult = productService.getCartProduct(new ArrayList<>());
		assertEquals(expectedResult, actualResult);
		Mockito.verify(productDao).getCartProduct(anyList());
	}

	@Test
	public void testGetNoOfRecords() {
		int expectedNoOfRecords = 5;
		when(productDao.getNoOfRecords()).thenReturn(expectedNoOfRecords);
		int actualNoOfRecords = productService.getNoOfRecords();

		assertEquals(expectedNoOfRecords, actualNoOfRecords);
		verify(productDao).getNoOfRecords();
	}

	@Test
	public void testGetAllBy() {
		String categoryId = "1";
		String colorId = "3";
		String min = "10";
		String max = "100";
		String sort = "price";
		int offset = 0;
		int noOfRecords = 10;

		Product product1 = new Product();
		product1.setCategoryId(1);
		product1.setProductName("product1");
		product1.setColorId(3);
		product1.setProductPrice(11.0);

		Product product2 = new Product();
		product2.setCategoryId(1);
		product2.setProductName("product2");
		product2.setColorId(3);
		product2.setProductPrice(20.0);

		List<Product> expectedProducts = Arrays.asList(product1, product2);

		when(productDao.getAllBy(eq(categoryId), eq(colorId), eq(min), eq(max), eq(sort), eq(offset), eq(noOfRecords)))
				.thenReturn(expectedProducts);

		List<Product> result = productService.getAllBy(categoryId, colorId, min, max, sort, offset, noOfRecords);

		assertEquals(expectedProducts, result);
	}
	
	 @Test
	  public void testGetNoDeleteCheck() {
	    Long id = 1L;
	    Product expectedProduct = new Product();
	    expectedProduct.setProductId(id);
	    expectedProduct.setProductName("product");
	    expectedProduct.setProductPrice(10.0);

	    when(productDao.getNoDeleteCheck(anyLong())).thenReturn(Optional.of(expectedProduct));

	    Optional<Product> result = productService.getNoDeleteCheck(id);

	    assertEquals(expectedProduct, result.get());
	  }

}
