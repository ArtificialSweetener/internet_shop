package dao_test.impl_test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dao.ProductDao;
import dao.impl.ProductDaoImpl;
import dbconnection_pool.ConnectionPool;
import exception.DataProcessingException;
import models.Product;

public class TestProductDaoImpl { // done, add more test cases
	@Mock
	private ConnectionPool connectionPool;
	@Mock
	private Connection connection;
	@Mock
	private PreparedStatement statement;
	@Mock
	private ResultSet resultSet;

	@Mock
	private Blob blob;

	@Mock
	private InputStream inputStream;
	@Mock
	private Date date;
	@Mock
	private Time time;

	private ProductDao productDao;

	@Before
	public void setUp() throws Exception {
		connectionPool = mock(ConnectionPool.class);
		connection = mock(Connection.class);
		statement = mock(PreparedStatement.class);
		resultSet = mock(ResultSet.class);
		blob = mock(Blob.class);
		inputStream = mock(InputStream.class);
		date = mock(Date.class);
		time = mock(Time.class);
		MockitoAnnotations.openMocks(this);
		productDao = new ProductDaoImpl(connectionPool);

	}

	@Test
	public void testCreate() throws Exception {
		Product product = new Product("Test Product", "A product used for testing", 1L, 2L, 10.0, 50, LocalDate.now(),
				LocalTime.now(), new byte[0], "test.jpg", 0);

		String query = "INSERT INTO products (product_name, product_desc, color_id, category_id, product_price, product_quantity_in_stock, product_date_of_addition, product_time_of_addition, product_photo, product_photo_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(statement);
		when(statement.getGeneratedKeys()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true);
		when(resultSet.getObject(1, Long.class)).thenReturn(1L);

		Product createdProduct = productDao.create(product);
		assertEquals(product.getProductId(), createdProduct.getProductId());
		assertEquals(product.getProductName(), createdProduct.getProductName());
		assertEquals(product.getProductDescription(), createdProduct.getProductDescription());
		assertEquals(product.getColorId(), createdProduct.getColorId());
		assertEquals(product.getCategoryId(), createdProduct.getCategoryId());
		assertEquals(product.getProductPrice(), createdProduct.getProductPrice(), 0);
		assertEquals(product.getProductQuantity(), createdProduct.getProductQuantity());
		assertEquals(product.getProductDate(), createdProduct.getProductDate());
		assertEquals(product.getProductTime(), createdProduct.getProductTime());
		assertEquals(product.getProductPhoto(), createdProduct.getProductPhoto());
		assertEquals(product.getProductPhotoName(), createdProduct.getProductPhotoName());
		assertEquals(product.getIsdeleted(), createdProduct.getIsdeleted());
	}

	@Test
	public void testCreateNullProduct() throws Exception {
		String query = "INSERT INTO products (product_name, product_desc, color_id, category_id, product_price, product_quantity_in_stock, product_date_of_addition, product_time_of_addition, product_photo, product_photo_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(statement);
		doThrow(new NullPointerException()).when(statement).setString(1, null);

		assertThrows(DataProcessingException.class, () -> productDao.create(null));

		verify(connectionPool).getConnection();
		verify(connection).prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		verify(statement, never()).setString(anyInt(), anyString());
		verify(statement, never()).executeUpdate();
		verify(statement).close();
		verify(connection).close();
	}

	@Test
	public void testCreateProductNullProductName() throws SQLException {
		Product product = new Product("Test Product", "A product used for testing", 1L, 2L, 10.0, 50, LocalDate.now(),
				LocalTime.now(), new byte[0], "test.jpg", 0);

		String query = "INSERT INTO products (product_name, product_desc, color_id, category_id, product_price, product_quantity_in_stock, product_date_of_addition, product_time_of_addition, product_photo, product_photo_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(statement);
		doThrow(new NullPointerException()).when(statement).setString(1, product.getProductName());

		assertThrows(DataProcessingException.class, () -> productDao.create(product));

		verify(connectionPool).getConnection();
		verify(connection).prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		verify(statement, never()).setString(2, product.getProductDescription());
		verify(statement, never()).setLong(3, product.getColorId());
		verify(statement, never()).setLong(4, product.getCategoryId());
		verify(statement, never()).setDouble(5, product.getProductPrice());
		verify(statement, never()).setInt(6, product.getProductQuantity());
		verify(statement, never()).setDate(7, Date.valueOf(product.getProductDate()));
		verify(statement, never()).setTime(8, Time.valueOf(product.getProductTime()));
		verify(statement, never()).setBlob(9, new SerialBlob(product.getProductPhoto()));
		verify(statement, never()).setString(10, product.getProductPhotoName());
		verify(statement, never()).setInt(11, product.getIsdeleted());
		verify(statement, never()).executeUpdate();
	}

	@Test
	public void testCreateProductNullProductDescription() throws SQLException {
		Product product = new Product("Test Product", "A product used for testing", 1L, 2L, 10.0, 50, LocalDate.now(),
				LocalTime.now(), new byte[0], "test.jpg", 0);

		String query = "INSERT INTO products (product_name, product_desc, color_id, category_id, product_price, product_quantity_in_stock, product_date_of_addition, product_time_of_addition, product_photo, product_photo_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(statement);
		doThrow(new NullPointerException()).when(statement).setString(2, product.getProductDescription());

		assertThrows(DataProcessingException.class, () -> productDao.create(product));

		verify(connectionPool).getConnection();
		verify(connection).prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		verify(statement, never()).setLong(3, product.getColorId());
		verify(statement, never()).setLong(4, product.getCategoryId());
		verify(statement, never()).setDouble(5, product.getProductPrice());
		verify(statement, never()).setInt(6, product.getProductQuantity());
		verify(statement, never()).setDate(7, Date.valueOf(product.getProductDate()));
		verify(statement, never()).setTime(8, Time.valueOf(product.getProductTime()));
		verify(statement, never()).setBlob(9, new SerialBlob(product.getProductPhoto()));
		verify(statement, never()).setString(10, product.getProductPhotoName());
		verify(statement, never()).setInt(11, product.getIsdeleted());
		verify(statement, never()).executeUpdate();
	}

	@Test
	public void testCreateProductNullColorId() throws SQLException {
		Product product = new Product("Test Product", "A product used for testing", 1L, 2L, 10.0, 50, LocalDate.now(),
				LocalTime.now(), new byte[0], "test.jpg", 0);

		String query = "INSERT INTO products (product_name, product_desc, color_id, category_id, product_price, product_quantity_in_stock, product_date_of_addition, product_time_of_addition, product_photo, product_photo_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(statement);
		doThrow(new NullPointerException()).when(statement).setLong(3, product.getColorId());

		assertThrows(DataProcessingException.class, () -> productDao.create(product));

		verify(connectionPool).getConnection();
		verify(connection).prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		verify(statement, never()).setLong(4, product.getCategoryId());
		verify(statement, never()).setDouble(5, product.getProductPrice());
		verify(statement, never()).setInt(6, product.getProductQuantity());
		verify(statement, never()).setDate(7, Date.valueOf(product.getProductDate()));
		verify(statement, never()).setTime(8, Time.valueOf(product.getProductTime()));
		verify(statement, never()).setBlob(9, new SerialBlob(product.getProductPhoto()));
		verify(statement, never()).setString(10, product.getProductPhotoName());
		verify(statement, never()).setInt(11, product.getIsdeleted());
		verify(statement, never()).executeUpdate();
	}

	@Test
	public void testCreateProductNullCategoryId() throws SQLException {
		Product product = new Product("Test Product", "A product used for testing", 1L, 2L, 10.0, 50, LocalDate.now(),
				LocalTime.now(), new byte[0], "test.jpg", 0);

		String query = "INSERT INTO products (product_name, product_desc, color_id, category_id, product_price, product_quantity_in_stock, product_date_of_addition, product_time_of_addition, product_photo, product_photo_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(statement);
		doThrow(new NullPointerException()).when(statement).setLong(4, product.getCategoryId());

		assertThrows(DataProcessingException.class, () -> productDao.create(product));

		verify(connectionPool).getConnection();
		verify(connection).prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		verify(statement, never()).setDouble(5, product.getProductPrice());
		verify(statement, never()).setInt(6, product.getProductQuantity());
		verify(statement, never()).setDate(7, Date.valueOf(product.getProductDate()));
		verify(statement, never()).setTime(8, Time.valueOf(product.getProductTime()));
		verify(statement, never()).setBlob(9, new SerialBlob(product.getProductPhoto()));
		verify(statement, never()).setString(10, product.getProductPhotoName());
		verify(statement, never()).setInt(11, product.getIsdeleted());
		verify(statement, never()).executeUpdate();
	}

	@Test
	public void testCreateProductNullProductPrice() throws SQLException {
		Product product = new Product("Test Product", "A product used for testing", 1L, 2L, 10.0, 50, LocalDate.now(),
				LocalTime.now(), new byte[0], "test.jpg", 0);

		String query = "INSERT INTO products (product_name, product_desc, color_id, category_id, product_price, product_quantity_in_stock, product_date_of_addition, product_time_of_addition, product_photo, product_photo_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(statement);
		doThrow(new NullPointerException()).when(statement).setDouble(5, product.getProductPrice());

		assertThrows(DataProcessingException.class, () -> productDao.create(product));

		verify(connectionPool).getConnection();
		verify(connection).prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		verify(statement, never()).setInt(6, product.getProductQuantity());
		verify(statement, never()).setDate(7, Date.valueOf(product.getProductDate()));
		verify(statement, never()).setTime(8, Time.valueOf(product.getProductTime()));
		verify(statement, never()).setBlob(9, new SerialBlob(product.getProductPhoto()));
		verify(statement, never()).setString(10, product.getProductPhotoName());
		verify(statement, never()).setInt(11, product.getIsdeleted());
		verify(statement, never()).executeUpdate();
	}

	@Test
	public void testCreateProductNullProductQuantity() throws SQLException {
		Product product = new Product("Test Product", "A product used for testing", 1L, 2L, 10.0, 50, LocalDate.now(),
				LocalTime.now(), new byte[0], "test.jpg", 0);

		String query = "INSERT INTO products (product_name, product_desc, color_id, category_id, product_price, product_quantity_in_stock, product_date_of_addition, product_time_of_addition, product_photo, product_photo_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(statement);
		doThrow(new NullPointerException()).when(statement).setInt(6, product.getProductQuantity());

		assertThrows(DataProcessingException.class, () -> productDao.create(product));

		verify(connectionPool).getConnection();
		verify(connection).prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		verify(statement, never()).setDate(7, Date.valueOf(product.getProductDate()));
		verify(statement, never()).setTime(8, Time.valueOf(product.getProductTime()));
		verify(statement, never()).setBlob(9, new SerialBlob(product.getProductPhoto()));
		verify(statement, never()).setString(10, product.getProductPhotoName());
		verify(statement, never()).setInt(11, product.getIsdeleted());
		verify(statement, never()).executeUpdate();
	}

	@Test
	public void testCreateProductNullProductDate() throws SQLException {
		Product product = new Product("Test Product", "A product used for testing", 1L, 2L, 10.0, 50, LocalDate.now(),
				LocalTime.now(), new byte[0], "test.jpg", 0);

		String query = "INSERT INTO products (product_name, product_desc, color_id, category_id, product_price, product_quantity_in_stock, product_date_of_addition, product_time_of_addition, product_photo, product_photo_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(statement);
		doThrow(new NullPointerException()).when(statement).setDate(7, Date.valueOf(product.getProductDate()));

		assertThrows(DataProcessingException.class, () -> productDao.create(product));

		verify(connectionPool).getConnection();
		verify(connection).prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		verify(statement, never()).setTime(8, Time.valueOf(product.getProductTime()));
		verify(statement, never()).setBlob(9, new SerialBlob(product.getProductPhoto()));
		verify(statement, never()).setString(10, product.getProductPhotoName());
		verify(statement, never()).setInt(11, product.getIsdeleted());
		verify(statement, never()).executeUpdate();
	}

	@Test
	public void testCreateProductNullProductTime() throws SQLException {
		Product product = new Product("Test Product", "A product used for testing", 1L, 2L, 10.0, 50, LocalDate.now(),
				LocalTime.now(), new byte[0], "test.jpg", 0);

		String query = "INSERT INTO products (product_name, product_desc, color_id, category_id, product_price, product_quantity_in_stock, product_date_of_addition, product_time_of_addition, product_photo, product_photo_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(statement);
		doThrow(new NullPointerException()).when(statement).setTime(8, Time.valueOf(product.getProductTime()));

		assertThrows(DataProcessingException.class, () -> productDao.create(product));

		verify(connectionPool).getConnection();
		verify(connection).prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		verify(statement, never()).setBlob(9, new SerialBlob(product.getProductPhoto()));
		verify(statement, never()).setString(10, product.getProductPhotoName());
		verify(statement, never()).setInt(11, product.getIsdeleted());
		verify(statement, never()).executeUpdate();
	}

	@Test
	public void testCreateProductNullProductPhoto() throws SQLException {
		Product product = new Product("Test Product", "A product used for testing", 1L, 2L, 10.0, 50, LocalDate.now(),
				LocalTime.now(), new byte[0], "test.jpg", 0);

		String query = "INSERT INTO products (product_name, product_desc, color_id, category_id, product_price, product_quantity_in_stock, product_date_of_addition, product_time_of_addition, product_photo, product_photo_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(statement);
		doThrow(new NullPointerException()).when(statement).setBlob(9, new SerialBlob(product.getProductPhoto()));

		assertThrows(DataProcessingException.class, () -> productDao.create(product));

		verify(connectionPool).getConnection();
		verify(connection).prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		verify(statement, never()).setString(10, product.getProductPhotoName());
		verify(statement, never()).setInt(11, product.getIsdeleted());
		verify(statement, never()).executeUpdate();
	}

	@Test
	public void testCreateProductNullProductPhotoName() throws SQLException {
		Product product = new Product("Test Product", "A product used for testing", 1L, 2L, 10.0, 50, LocalDate.now(),
				LocalTime.now(), new byte[0], "test.jpg", 0);

		String query = "INSERT INTO products (product_name, product_desc, color_id, category_id, product_price, product_quantity_in_stock, product_date_of_addition, product_time_of_addition, product_photo, product_photo_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(statement);
		doThrow(new NullPointerException()).when(statement).setString(10, product.getProductPhotoName());

		assertThrows(DataProcessingException.class, () -> productDao.create(product));

		verify(connectionPool).getConnection();
		verify(connection).prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		verify(statement, never()).setInt(11, product.getIsdeleted());
		verify(statement, never()).executeUpdate();
	}

	@Test
	public void testCreateProductDatabaseUnavailaible() {
		when(connectionPool.getConnection()).thenThrow(RuntimeException.class);
		Product product = new Product("Test Product", "A product used for testing", 1L, 2L, 10.0, 50, LocalDate.now(),
				LocalTime.now(), new byte[0], "test.jpg", 0);
		assertThrows(DataProcessingException.class, () -> productDao.create(product));
	}

	@Test
	public void testCreateProductThrowsException() throws SQLException {
		Product product = new Product("Test Product", "A product used for testing", 1L, 2L, 10.0, 50, LocalDate.now(),
				LocalTime.now(), new byte[0], "test.jpg", 0);
		String query = "INSERT INTO products (product_name, product_desc, color_id, category_id, product_price, product_quantity_in_stock, product_date_of_addition, product_time_of_addition, product_photo, product_photo_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		doThrow(new NullPointerException()).when(statement).setString(1, null);
		assertThrows(DataProcessingException.class, () -> productDao.create(product));
	}

	@Test
	public void testGetProductById() throws SQLException, IOException {
		Product expectedProduct = new Product(1L, "Test Product", "A product used for testing", 1L, 2L, 10.0, 50,
				LocalDate.now(), LocalTime.now(), new byte[0], "test.jpg", 0);
		expectedProduct.setBase64Image(Base64.getEncoder().encodeToString(expectedProduct.getProductPhoto()));

		String query = "SELECT * FROM products WHERE id = ? AND is_deleted = 0";

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true).thenReturn(false);

		when(resultSet.getLong("id")).thenReturn(expectedProduct.getProductId());
		when(resultSet.getString("product_name")).thenReturn(expectedProduct.getProductName());
		when(resultSet.getString("product_desc")).thenReturn(expectedProduct.getProductDescription());
		when(resultSet.getLong("color_id")).thenReturn(expectedProduct.getColorId());
		when(resultSet.getLong("category_id")).thenReturn(expectedProduct.getCategoryId());
		when(resultSet.getDouble("product_price")).thenReturn(expectedProduct.getProductPrice());
		when(resultSet.getInt("product_quantity_in_stock")).thenReturn(expectedProduct.getProductQuantity());

		when(resultSet.getDate("product_date_of_addition")).thenReturn(date);
		when(date.toLocalDate()).thenReturn(expectedProduct.getProductDate());
		when(resultSet.getTime("product_time_of_addition")).thenReturn(time);
		when(time.toLocalTime()).thenReturn(expectedProduct.getProductTime());

		when(resultSet.getBlob("product_photo")).thenReturn(blob);
		when(blob.getBinaryStream()).thenReturn(inputStream);
		when(inputStream.readAllBytes()).thenReturn(expectedProduct.getProductPhoto());

		when(resultSet.getString("product_photo_name")).thenReturn(expectedProduct.getProductPhotoName());
		when(resultSet.getInt("is_deleted")).thenReturn(expectedProduct.getIsdeleted());

		Optional<Product> actualProduct = productDao.get(1L);
		assertTrue(actualProduct.isPresent());

		assertEquals(expectedProduct.getProductId(), actualProduct.get().getProductId());
		assertEquals(expectedProduct.getProductName(), actualProduct.get().getProductName());
		assertEquals(expectedProduct.getProductDescription(), actualProduct.get().getProductDescription());
		assertEquals(expectedProduct.getColorId(), actualProduct.get().getColorId());
		assertEquals(expectedProduct.getCategoryId(), actualProduct.get().getCategoryId());
		assertEquals(expectedProduct.getProductPrice(), actualProduct.get().getProductPrice(), 0);
		assertEquals(expectedProduct.getProductQuantity(), actualProduct.get().getProductQuantity());
		assertEquals(expectedProduct.getProductDate(), actualProduct.get().getProductDate());
		assertEquals(expectedProduct.getProductTime(), actualProduct.get().getProductTime());
		assertEquals(expectedProduct.getProductPhoto(), actualProduct.get().getProductPhoto());
		assertEquals(expectedProduct.getBase64Image(), actualProduct.get().getBase64Image());
		assertEquals(expectedProduct.getProductPhotoName(), actualProduct.get().getProductPhotoName());
		assertEquals(expectedProduct.getIsdeleted(), actualProduct.get().getIsdeleted());
	}

	@Test
	public void testGetProductByNonExistentId() throws SQLException {
		Long nonExistentId = 999L;
		String query = "SELECT * FROM products WHERE id = ? AND is_deleted = 0";

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(false);

		Optional<Product> actualProductOpt = productDao.get(nonExistentId);
		assertFalse(actualProductOpt.isPresent());
	}

	@Test
	public void testGetProductByIdDelletedProduct() throws SQLException {
		Product expectedProduct = new Product(1L, "Test Product", "A product used for testing", 1L, 2L, 10.0, 50,
				LocalDate.now(), LocalTime.now(), new byte[0], "test.jpg", 1);
		expectedProduct.setBase64Image(Base64.getEncoder().encodeToString(expectedProduct.getProductPhoto()));

		String query = "SELECT * FROM products WHERE id = ? AND is_deleted = 0";

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(false);

		Optional<Product> actualProductOpt = productDao.get(expectedProduct.getProductId());
		assertFalse(actualProductOpt.isPresent());
	}

	@Test
	public void testGetProductByNullId() throws SQLException {
		Long nullId = null;
		String query = "SELECT * FROM products WHERE id = ? AND is_deleted = 0";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		assertThrows(DataProcessingException.class, () -> productDao.get(nullId));
	}

	@Test
	public void testGetProductByIdThrowsException() throws SQLException {
		Long Id = 1L;
		String query = "SELECT * FROM products WHERE id = ? AND is_deleted = 0";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		doThrow(new SQLException()).when(statement).setLong(1, Id);
		assertThrows(DataProcessingException.class, () -> productDao.get(Id));
	}

	@Test
	public void testGetNoDeleteCheck_productExistsAndNotDeleted() throws SQLException, IOException {
		Long id = 1L;
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(anyString())).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true).thenReturn(false);

		byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };
		Product product = new Product(id, "Test Product", "A product used for testing", 1L, 2L, 10.0, 50,
				LocalDate.now(), LocalTime.now(), data, "test.jpg", 0);
		product.setBase64Image(Base64.getEncoder().encodeToString(product.getProductPhoto()));

		when(resultSet.getLong("id")).thenReturn(product.getProductId());
		when(resultSet.getString("product_name")).thenReturn(product.getProductName());
		when(resultSet.getString("product_desc")).thenReturn(product.getProductDescription());
		when(resultSet.getLong("color_id")).thenReturn(product.getColorId());
		when(resultSet.getLong("category_id")).thenReturn(product.getCategoryId());
		when(resultSet.getDouble("product_price")).thenReturn(product.getProductPrice());
		when(resultSet.getInt("product_quantity_in_stock")).thenReturn(product.getProductQuantity());

		when(resultSet.getDate("product_date_of_addition")).thenReturn(date);
		when(date.toLocalDate()).thenReturn(product.getProductDate());
		when(resultSet.getTime("product_time_of_addition")).thenReturn(time);
		when(time.toLocalTime()).thenReturn(product.getProductTime());

		when(resultSet.getBlob("product_photo")).thenReturn(blob);
		when(blob.getBinaryStream()).thenReturn(inputStream);
		when(inputStream.readAllBytes()).thenReturn(product.getProductPhoto());
		when(resultSet.getString("product_photo_name")).thenReturn(product.getProductPhotoName());
		when(resultSet.getInt("is_deleted")).thenReturn(product.getIsdeleted());

		Optional<Product> actualProductOpt = productDao.getNoDeleteCheck(id);

		assertTrue(actualProductOpt.isPresent());
		Product actualProduct = actualProductOpt.get();
		assertEquals(product.getProductId(), actualProduct.getProductId());
		assertEquals(product.getProductName(), actualProduct.getProductName());
		assertEquals(product.getProductDescription(), actualProduct.getProductDescription());
		assertEquals(product.getColorId(), actualProduct.getColorId());
		assertEquals(product.getCategoryId(), actualProduct.getCategoryId());
		assertEquals(product.getProductPrice(), actualProduct.getProductPrice(), 0);
		assertEquals(product.getProductQuantity(), actualProduct.getProductQuantity());
		assertEquals(product.getProductDate(), actualProduct.getProductDate());
		assertEquals(product.getProductTime(), actualProduct.getProductTime());
		assertEquals(product.getProductPhoto(), actualProduct.getProductPhoto());
		assertEquals(product.getProductPhotoName(), actualProduct.getProductPhotoName());
		assertEquals(product.getIsdeleted(), actualProduct.getIsdeleted());
	}

	@Test
	public void testGetNoDeleteCheck_productExistsAndDeleted() throws SQLException, IOException {
		Long id = 1L;
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(anyString())).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true).thenReturn(false);

		byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };
		Product product = new Product(id, "Test Product", "A product used for testing", 1L, 2L, 10.0, 50,
				LocalDate.now(), LocalTime.now(), data, "test.jpg", 1);
		product.setBase64Image(Base64.getEncoder().encodeToString(product.getProductPhoto()));

		when(resultSet.getLong("id")).thenReturn(product.getProductId());
		when(resultSet.getString("product_name")).thenReturn(product.getProductName());
		when(resultSet.getString("product_desc")).thenReturn(product.getProductDescription());
		when(resultSet.getLong("color_id")).thenReturn(product.getColorId());
		when(resultSet.getLong("category_id")).thenReturn(product.getCategoryId());
		when(resultSet.getDouble("product_price")).thenReturn(product.getProductPrice());
		when(resultSet.getInt("product_quantity_in_stock")).thenReturn(product.getProductQuantity());

		when(resultSet.getDate("product_date_of_addition")).thenReturn(date);
		when(date.toLocalDate()).thenReturn(product.getProductDate());
		when(resultSet.getTime("product_time_of_addition")).thenReturn(time);
		when(time.toLocalTime()).thenReturn(product.getProductTime());

		when(resultSet.getBlob("product_photo")).thenReturn(blob);
		when(blob.getBinaryStream()).thenReturn(inputStream);
		when(inputStream.readAllBytes()).thenReturn(product.getProductPhoto());
		when(resultSet.getString("product_photo_name")).thenReturn(product.getProductPhotoName());
		when(resultSet.getInt("is_deleted")).thenReturn(product.getIsdeleted());

		Optional<Product> actualProductOpt = productDao.getNoDeleteCheck(id);

		assertTrue(actualProductOpt.isPresent());
		Product actualProduct = actualProductOpt.get();

		assertEquals(product.getProductId(), actualProduct.getProductId());
		assertEquals(product.getProductName(), actualProduct.getProductName());
		assertEquals(product.getProductDescription(), actualProduct.getProductDescription());
		assertEquals(product.getColorId(), actualProduct.getColorId());
		assertEquals(product.getCategoryId(), actualProduct.getCategoryId());
		assertEquals(product.getProductPrice(), actualProduct.getProductPrice(), 0);
		assertEquals(product.getProductQuantity(), actualProduct.getProductQuantity());
		assertEquals(product.getProductDate(), actualProduct.getProductDate());
		assertEquals(product.getProductTime(), actualProduct.getProductTime());
		assertEquals(product.getProductPhoto(), actualProduct.getProductPhoto());
		assertEquals(product.getProductPhotoName(), actualProduct.getProductPhotoName());
		assertEquals(product.getIsdeleted(), actualProduct.getIsdeleted());
	}

	@Test
	public void testGetNoDeleteCheck_ProductDoesNotExist() throws SQLException {
		Long id = 3L;
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(anyString())).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(false);
		Optional<Product> actualProduct = productDao.getNoDeleteCheck(id);
		assertFalse(actualProduct.isPresent());
	}

	@Test
	public void testGetNoDeleteCheck_DatabaseUnavailaible() throws SQLException {
		Long id = 3L;
		when(connectionPool.getConnection()).thenThrow(RuntimeException.class);
		assertThrows(DataProcessingException.class, () -> productDao.getNoDeleteCheck(id));
	}

	@Test
	public void testGetNoDeleteCheck_SQLExceptionThrown() throws SQLException {
		Long id = 4L;
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(anyString())).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(statement.executeQuery()).thenThrow(new SQLException());
		assertThrows(DataProcessingException.class, () -> productDao.getNoDeleteCheck(id));
	}

	@Test
	public void testGetAllPaginationNumberOfProductsEqualsNumberOfRecords() throws SQLException, IOException {
		int offset = 0;
		int noOfRecords = 2;
		String query = "select SQL_CALC_FOUND_ROWS * from products where is_deleted = 0 limit " + offset + ", "
				+ noOfRecords;
		List<Product> expectedProducts = new ArrayList<>();
		byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };
		Product product1 = new Product(1L, "Test Product 1", "First product used for testing", 1L, 2L, 10.0, 50,
				LocalDate.now(), LocalTime.now(), data, "test1.jpg", 0);
		product1.setBase64Image(Base64.getEncoder().encodeToString(product1.getProductPhoto()));
		Product product2 = new Product(2L, "Test Product 2", "Second product used for testing", 1L, 2L, 11.0, 50,
				LocalDate.now(), LocalTime.now(), data, "test2.jpg", 0);
		product2.setBase64Image(Base64.getEncoder().encodeToString(product2.getProductPhoto()));
		expectedProducts.add(product1);
		expectedProducts.add(product2);

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false).thenReturn(true);

		when(resultSet.getLong("id")).thenReturn(product1.getProductId(), product2.getProductId());
		when(resultSet.getString("product_name")).thenReturn(product1.getProductName(), product2.getProductName());
		when(resultSet.getString("product_desc")).thenReturn(product1.getProductDescription(),
				product2.getProductDescription());
		when(resultSet.getLong("color_id")).thenReturn(product1.getColorId(), product2.getColorId());
		when(resultSet.getLong("category_id")).thenReturn(product1.getCategoryId(), product2.getCategoryId());
		when(resultSet.getDouble("product_price")).thenReturn(product1.getProductPrice(), product2.getProductPrice());
		when(resultSet.getInt("product_quantity_in_stock")).thenReturn(product1.getProductQuantity(),
				product2.getProductQuantity());

		when(resultSet.getDate("product_date_of_addition")).thenReturn(date);
		when(date.toLocalDate()).thenReturn(product1.getProductDate(), product2.getProductDate());
		when(resultSet.getTime("product_time_of_addition")).thenReturn(time);
		when(time.toLocalTime()).thenReturn(product1.getProductTime(), product2.getProductTime());

		when(resultSet.getBlob("product_photo")).thenReturn(blob);
		when(blob.getBinaryStream()).thenReturn(inputStream);
		when(inputStream.readAllBytes()).thenReturn(product1.getProductPhoto(), product2.getProductPhoto());
		when(resultSet.getString("product_photo_name")).thenReturn(product1.getProductPhotoName(),
				product2.getProductPhotoName());
		when(resultSet.getInt("is_deleted")).thenReturn(product1.getIsdeleted(), product2.getIsdeleted());

		when(statement.executeQuery("SELECT FOUND_ROWS()")).thenReturn(resultSet);
		when(resultSet.getLong(1)).thenReturn(1L);

		List<Product> actualProducts = productDao.getAll(offset, noOfRecords);

		verify(connection).prepareStatement(query);
		verify(statement).executeQuery();
		verify(resultSet, times(4)).next();

		verify(resultSet, times(2)).getLong("id");
		verify(resultSet, times(2)).getString("product_name");
		verify(resultSet, times(2)).getString("product_desc");
		verify(resultSet, times(2)).getLong("color_id");
		verify(resultSet, times(2)).getLong("category_id");
		verify(resultSet, times(2)).getDouble("product_price");
		verify(resultSet, times(2)).getInt("product_quantity_in_stock");
		verify(resultSet, times(2)).getDate("product_date_of_addition");
		verify(date, times(2)).toLocalDate();
		verify(resultSet, times(2)).getTime("product_time_of_addition");
		verify(time, times(2)).toLocalTime();
		verify(resultSet, times(2)).getBlob("product_photo");
		verify(blob, times(2)).getBinaryStream();
		verify(inputStream, times(2)).readAllBytes();
		verify(resultSet, times(2)).getString("product_photo_name");
		verify(resultSet, times(2)).getString(resultSet.getInt("is_deleted"));
		verify(resultSet).close();
		verify(statement).executeQuery("SELECT FOUND_ROWS()");

		assertEquals(expectedProducts.size(), actualProducts.size());
		assertEquals(actualProducts.size(), noOfRecords);
		for (int i = 0; i < expectedProducts.size(); i++) {
			assertEquals(expectedProducts.get(i).getProductId(), actualProducts.get(i).getProductId());
			assertEquals(expectedProducts.get(i).getProductName(), actualProducts.get(i).getProductName());
			assertEquals(expectedProducts.get(i).getProductDescription(),
					actualProducts.get(i).getProductDescription());
			assertEquals(expectedProducts.get(i).getColorId(), actualProducts.get(i).getColorId());
			assertEquals(expectedProducts.get(i).getCategoryId(), actualProducts.get(i).getCategoryId());
			assertEquals(expectedProducts.get(i).getProductPrice(), actualProducts.get(i).getProductPrice(), 0);
			assertEquals(expectedProducts.get(i).getProductQuantity(), actualProducts.get(i).getProductQuantity());
			assertEquals(expectedProducts.get(i).getProductDate(), actualProducts.get(i).getProductDate());
			assertEquals(expectedProducts.get(i).getProductTime(), actualProducts.get(i).getProductTime());
			assertEquals(expectedProducts.get(i).getProductPhoto(), actualProducts.get(i).getProductPhoto());
			assertEquals(expectedProducts.get(i).getBase64Image(), actualProducts.get(i).getBase64Image());
			assertEquals(expectedProducts.get(i).getProductPhotoName(), actualProducts.get(i).getProductPhotoName());
			assertEquals(expectedProducts.get(i).getIsdeleted(), actualProducts.get(i).getIsdeleted());
		}
	}

	@Test
	public void testGetAllPaginationNumberOfProductsLowerThanNumberOfRecords() throws SQLException, IOException {
		int offset = 0;
		int noOfRecords = 3;
		String query = "select SQL_CALC_FOUND_ROWS * from products where is_deleted = 0 limit " + offset + ", "
				+ noOfRecords;
		List<Product> expectedProducts = new ArrayList<>();
		byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };
		Product product1 = new Product(1L, "Test Product 1", "First product used for testing", 1L, 2L, 10.0, 50,
				LocalDate.now(), LocalTime.now(), data, "test1.jpg", 0);
		product1.setBase64Image(Base64.getEncoder().encodeToString(product1.getProductPhoto()));
		Product product2 = new Product(2L, "Test Product 2", "Second product used for testing", 1L, 2L, 11.0, 50,
				LocalDate.now(), LocalTime.now(), data, "test2.jpg", 0);
		product2.setBase64Image(Base64.getEncoder().encodeToString(product2.getProductPhoto()));
		expectedProducts.add(product1);
		expectedProducts.add(product2);

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false).thenReturn(true);

		when(resultSet.getLong("id")).thenReturn(product1.getProductId(), product2.getProductId());
		when(resultSet.getString("product_name")).thenReturn(product1.getProductName(), product2.getProductName());
		when(resultSet.getString("product_desc")).thenReturn(product1.getProductDescription(),
				product2.getProductDescription());
		when(resultSet.getLong("color_id")).thenReturn(product1.getColorId(), product2.getColorId());
		when(resultSet.getLong("category_id")).thenReturn(product1.getCategoryId(), product2.getCategoryId());
		when(resultSet.getDouble("product_price")).thenReturn(product1.getProductPrice(), product2.getProductPrice());
		when(resultSet.getInt("product_quantity_in_stock")).thenReturn(product1.getProductQuantity(),
				product2.getProductQuantity());

		when(resultSet.getDate("product_date_of_addition")).thenReturn(date);
		when(date.toLocalDate()).thenReturn(product1.getProductDate(), product2.getProductDate());
		when(resultSet.getTime("product_time_of_addition")).thenReturn(time);
		when(time.toLocalTime()).thenReturn(product1.getProductTime(), product2.getProductTime());

		when(resultSet.getBlob("product_photo")).thenReturn(blob);
		when(blob.getBinaryStream()).thenReturn(inputStream);
		when(inputStream.readAllBytes()).thenReturn(product1.getProductPhoto(), product2.getProductPhoto());
		when(resultSet.getString("product_photo_name")).thenReturn(product1.getProductPhotoName(),
				product2.getProductPhotoName());
		when(resultSet.getInt("is_deleted")).thenReturn(product1.getIsdeleted(), product2.getIsdeleted());

		when(statement.executeQuery("SELECT FOUND_ROWS()")).thenReturn(resultSet);
		when(resultSet.getLong(1)).thenReturn(1L);

		List<Product> actualProducts = productDao.getAll(offset, noOfRecords);

		verify(connection).prepareStatement(query);
		verify(statement).executeQuery();
		verify(resultSet, times(4)).next();

		verify(resultSet, times(2)).getLong("id");
		verify(resultSet, times(2)).getString("product_name");
		verify(resultSet, times(2)).getString("product_desc");
		verify(resultSet, times(2)).getLong("color_id");
		verify(resultSet, times(2)).getLong("category_id");
		verify(resultSet, times(2)).getDouble("product_price");
		verify(resultSet, times(2)).getInt("product_quantity_in_stock");
		verify(resultSet, times(2)).getDate("product_date_of_addition");
		verify(date, times(2)).toLocalDate();
		verify(resultSet, times(2)).getTime("product_time_of_addition");
		verify(time, times(2)).toLocalTime();
		verify(resultSet, times(2)).getBlob("product_photo");
		verify(blob, times(2)).getBinaryStream();
		verify(inputStream, times(2)).readAllBytes();
		verify(resultSet, times(2)).getString("product_photo_name");
		verify(resultSet, times(2)).getString(resultSet.getInt("is_deleted"));
		verify(resultSet).close();
		verify(statement).executeQuery("SELECT FOUND_ROWS()");

		assertEquals(expectedProducts.size(), actualProducts.size());
		assertFalse(actualProducts.size() == noOfRecords);

		for (int i = 0; i < expectedProducts.size(); i++) {
			assertEquals(expectedProducts.get(i).getProductId(), actualProducts.get(i).getProductId());
			assertEquals(expectedProducts.get(i).getProductName(), actualProducts.get(i).getProductName());
			assertEquals(expectedProducts.get(i).getProductDescription(),
					actualProducts.get(i).getProductDescription());
			assertEquals(expectedProducts.get(i).getColorId(), actualProducts.get(i).getColorId());
			assertEquals(expectedProducts.get(i).getCategoryId(), actualProducts.get(i).getCategoryId());
			assertEquals(expectedProducts.get(i).getProductPrice(), actualProducts.get(i).getProductPrice(), 0);
			assertEquals(expectedProducts.get(i).getProductQuantity(), actualProducts.get(i).getProductQuantity());
			assertEquals(expectedProducts.get(i).getProductDate(), actualProducts.get(i).getProductDate());
			assertEquals(expectedProducts.get(i).getProductTime(), actualProducts.get(i).getProductTime());
			assertEquals(expectedProducts.get(i).getProductPhoto(), actualProducts.get(i).getProductPhoto());
			assertEquals(expectedProducts.get(i).getBase64Image(), actualProducts.get(i).getBase64Image());
			assertEquals(expectedProducts.get(i).getProductPhotoName(), actualProducts.get(i).getProductPhotoName());
			assertEquals(expectedProducts.get(i).getIsdeleted(), actualProducts.get(i).getIsdeleted());
		}
	}

	@Test
	public void testGetAllPaginationNumberOfProductsBiggerThanNumberOfRecords() throws SQLException, IOException {
		int offset = 0;
		int noOfRecords = 1;
		String query = "select SQL_CALC_FOUND_ROWS * from products where is_deleted = 0 limit " + offset + ", "
				+ noOfRecords;
		List<Product> expectedProducts = new ArrayList<>();
		byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };
		Product product1 = new Product(1L, "Test Product 1", "First product used for testing", 1L, 2L, 10.0, 50,
				LocalDate.now(), LocalTime.now(), data, "test1.jpg", 0);
		product1.setBase64Image(Base64.getEncoder().encodeToString(product1.getProductPhoto()));
		Product product2 = new Product(2L, "Test Product 2", "Second product used for testing", 1L, 2L, 11.0, 50,
				LocalDate.now(), LocalTime.now(), data, "test2.jpg", 0);
		product2.setBase64Image(Base64.getEncoder().encodeToString(product2.getProductPhoto()));
		expectedProducts.add(product1);

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true).thenReturn(false).thenReturn(true);

		when(resultSet.getLong("id")).thenReturn(product1.getProductId(), product2.getProductId());
		when(resultSet.getString("product_name")).thenReturn(product1.getProductName(), product2.getProductName());
		when(resultSet.getString("product_desc")).thenReturn(product1.getProductDescription(),
				product2.getProductDescription());
		when(resultSet.getLong("color_id")).thenReturn(product1.getColorId(), product2.getColorId());
		when(resultSet.getLong("category_id")).thenReturn(product1.getCategoryId(), product2.getCategoryId());
		when(resultSet.getDouble("product_price")).thenReturn(product1.getProductPrice(), product2.getProductPrice());
		when(resultSet.getInt("product_quantity_in_stock")).thenReturn(product1.getProductQuantity(),
				product2.getProductQuantity());

		when(resultSet.getDate("product_date_of_addition")).thenReturn(date);
		when(date.toLocalDate()).thenReturn(product1.getProductDate(), product2.getProductDate());
		when(resultSet.getTime("product_time_of_addition")).thenReturn(time);
		when(time.toLocalTime()).thenReturn(product1.getProductTime(), product2.getProductTime());

		when(resultSet.getBlob("product_photo")).thenReturn(blob);
		when(blob.getBinaryStream()).thenReturn(inputStream);
		when(inputStream.readAllBytes()).thenReturn(product1.getProductPhoto(), product2.getProductPhoto());
		when(resultSet.getString("product_photo_name")).thenReturn(product1.getProductPhotoName(),
				product2.getProductPhotoName());
		when(resultSet.getInt("is_deleted")).thenReturn(product1.getIsdeleted(), product2.getIsdeleted());

		when(statement.executeQuery("SELECT FOUND_ROWS()")).thenReturn(resultSet);
		when(resultSet.getLong(1)).thenReturn(1L);

		List<Product> actualProducts = productDao.getAll(offset, noOfRecords);

		verify(connection).prepareStatement(query);
		verify(statement).executeQuery();
		verify(resultSet, times(3)).next();

		verify(resultSet, times(1)).getLong("id");
		verify(resultSet, times(1)).getString("product_name");
		verify(resultSet, times(1)).getString("product_desc");
		verify(resultSet, times(1)).getLong("color_id");
		verify(resultSet, times(1)).getLong("category_id");
		verify(resultSet, times(1)).getDouble("product_price");
		verify(resultSet, times(1)).getInt("product_quantity_in_stock");
		verify(resultSet, times(1)).getDate("product_date_of_addition");
		verify(date, times(1)).toLocalDate();
		verify(resultSet, times(1)).getTime("product_time_of_addition");
		verify(time, times(1)).toLocalTime();
		verify(resultSet, times(1)).getBlob("product_photo");
		verify(blob, times(1)).getBinaryStream();
		verify(inputStream, times(1)).readAllBytes();
		verify(resultSet, times(1)).getString("product_photo_name");
		verify(resultSet, times(1)).getString(resultSet.getInt("is_deleted"));
		verify(resultSet).close();
		verify(statement).executeQuery("SELECT FOUND_ROWS()");

		assertEquals(expectedProducts.size(), actualProducts.size());

		assertEquals(actualProducts.size(), noOfRecords);

		for (int i = 0; i < expectedProducts.size(); i++) {
			assertEquals(expectedProducts.get(i).getProductId(), actualProducts.get(i).getProductId());
			assertEquals(expectedProducts.get(i).getProductName(), actualProducts.get(i).getProductName());
			assertEquals(expectedProducts.get(i).getProductDescription(),
					actualProducts.get(i).getProductDescription());
			assertEquals(expectedProducts.get(i).getColorId(), actualProducts.get(i).getColorId());
			assertEquals(expectedProducts.get(i).getCategoryId(), actualProducts.get(i).getCategoryId());
			assertEquals(expectedProducts.get(i).getProductPrice(), actualProducts.get(i).getProductPrice(), 0);
			assertEquals(expectedProducts.get(i).getProductQuantity(), actualProducts.get(i).getProductQuantity());
			assertEquals(expectedProducts.get(i).getProductDate(), actualProducts.get(i).getProductDate());
			assertEquals(expectedProducts.get(i).getProductTime(), actualProducts.get(i).getProductTime());
			assertEquals(expectedProducts.get(i).getProductPhoto(), actualProducts.get(i).getProductPhoto());
			assertEquals(expectedProducts.get(i).getBase64Image(), actualProducts.get(i).getBase64Image());
			assertEquals(expectedProducts.get(i).getProductPhotoName(), actualProducts.get(i).getProductPhotoName());
			assertEquals(expectedProducts.get(i).getIsdeleted(), actualProducts.get(i).getIsdeleted());
		}
	}

	@Test
	public void testGetAllPaginationNoProductsInTheDatabase() throws SQLException, IOException {
		int offset = 0;
		int noOfRecords = 2;
		String query = "select SQL_CALC_FOUND_ROWS * from products where is_deleted = 0 limit " + offset + ", "
				+ noOfRecords;

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(false).thenReturn(false);
		when(statement.executeQuery("SELECT FOUND_ROWS()")).thenReturn(resultSet);

		List<Product> actualProducts = productDao.getAll(offset, noOfRecords);
		assertTrue(actualProducts.isEmpty());

		verify(connection).prepareStatement(query);
		verify(statement).executeQuery();
		verify(resultSet, times(2)).next();

		verify(resultSet, never()).getLong("id");
		verify(resultSet, never()).getString("product_name");
		verify(resultSet, never()).getString("product_desc");
		verify(resultSet, never()).getLong("color_id");
		verify(resultSet, never()).getLong("category_id");
		verify(resultSet, never()).getDouble("product_price");
		verify(resultSet, never()).getInt("product_quantity_in_stock");
		verify(resultSet, never()).getDate("product_date_of_addition");
		verify(date, never()).toLocalDate();
		verify(resultSet, never()).getTime("product_time_of_addition");
		verify(time, never()).toLocalTime();
		verify(resultSet, never()).getBlob("product_photo");
		verify(blob, never()).getBinaryStream();
		verify(inputStream, never()).readAllBytes();
		verify(resultSet, never()).getString("product_photo_name");
		verify(resultSet, never()).getString(resultSet.getInt("is_deleted"));
		verify(resultSet).close();
		verify(statement, times(1)).executeQuery("SELECT FOUND_ROWS()");
	}

	@Test
	public void testGetAllPaginationDatabaseUnavailaible() throws SQLException, IOException {
		int offset = 0;
		int noOfRecords = 2;
		String query = "select SQL_CALC_FOUND_ROWS * from products where is_deleted = 0 limit " + offset + ", "
				+ noOfRecords;

		when(connectionPool.getConnection()).thenThrow(RuntimeException.class);
		assertThrows(DataProcessingException.class, () -> productDao.getAll(offset, noOfRecords));

		verify(connection, never()).prepareStatement(query);
		verify(statement, never()).executeQuery();
		verify(resultSet, never()).next();

		verify(resultSet, never()).getLong("id");
		verify(resultSet, never()).getString("product_name");
		verify(resultSet, never()).getString("product_desc");
		verify(resultSet, never()).getLong("color_id");
		verify(resultSet, never()).getLong("category_id");
		verify(resultSet, never()).getDouble("product_price");
		verify(resultSet, never()).getInt("product_quantity_in_stock");
		verify(resultSet, never()).getDate("product_date_of_addition");
		verify(date, never()).toLocalDate();
		verify(resultSet, never()).getTime("product_time_of_addition");
		verify(time, never()).toLocalTime();
		verify(resultSet, never()).getBlob("product_photo");
		verify(blob, never()).getBinaryStream();
		verify(inputStream, never()).readAllBytes();
		verify(resultSet, never()).getString("product_photo_name");
		verify(resultSet, never()).getString(resultSet.getInt("is_deleted"));
		verify(resultSet, never()).close();
		verify(statement, never()).executeQuery("SELECT FOUND_ROWS()");
	}

	@Test
	public void testUpdateProduct() throws SQLException {
		String query = "UPDATE products SET product_name = ?, product_desc = ?, color_id = ? , category_id = ?, product_price= ?, product_quantity_in_stock = ?, product_photo =?, product_photo_name =? "
				+ "WHERE id = ? AND is_deleted = 0";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);

		Product product = new Product(1L, "Test Product", "A product used for testing", 1L, 2L, 10.0, 50,
				LocalDate.now(), LocalTime.now(), new byte[0], "test.jpg", 0);
		product.setBase64Image(Base64.getEncoder().encodeToString(product.getProductPhoto()));

		Product updatedProduct = productDao.update(product);

		assertNotNull(updatedProduct);

		verify(connectionPool, times(1)).getConnection();
		verify(connection, times(1)).prepareStatement(query);

		verify(statement, times(1)).setString(1, product.getProductName());
		verify(statement, times(1)).setString(2, product.getProductDescription());
		verify(statement, times(1)).setLong(3, product.getColorId());
		verify(statement, times(1)).setLong(4, product.getCategoryId());
		verify(statement, times(1)).setDouble(5, product.getProductPrice());
		verify(statement, times(1)).setInt(6, product.getProductQuantity());
		verify(statement, times(1)).setBlob(7, new SerialBlob(product.getProductPhoto()));
		verify(statement, times(1)).setString(8, product.getProductPhotoName());
		verify(statement, times(1)).setInt(9, product.getIsdeleted());
		verify(statement, times(1)).setLong(10, product.getProductId());

		verify(statement, times(1)).executeUpdate();
		verify(statement, times(1)).close();
		verify(connection).close();

		assertEquals(product, updatedProduct);
	}

	@Test
	public void testUpdateProductNonExistent() throws SQLException {
		String query = "UPDATE products SET product_name = ?, product_desc = ?, color_id = ? , category_id = ?, product_price= ?, product_quantity_in_stock = ?, product_photo =?, product_photo_name =? "
				+ "WHERE id = ? AND is_deleted = 0";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		doThrow(new SQLException()).when(statement).executeUpdate();
		Product notExistingProduct = new Product(1L, "Test Product", "A product used for testing", 1L, 2L, 10.0, 50,
				LocalDate.now(), LocalTime.now(), new byte[0], "test.jpg", 0);
		notExistingProduct.setBase64Image(Base64.getEncoder().encodeToString(notExistingProduct.getProductPhoto()));
		assertThrows(DataProcessingException.class, () -> productDao.update(notExistingProduct));
	}

	@Test
	public void testUpdateProductInvalidId() throws SQLException {
		String query = "UPDATE products SET product_name = ?, product_desc = ?, color_id = ? , category_id = ?, product_price= ?, product_quantity_in_stock = ?, product_photo =?, product_photo_name =? "
				+ "WHERE id = ? AND is_deleted = 0";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		doThrow(new SQLException()).when(statement).executeUpdate();
		Product invalidIdProduct = new Product(1L, "Test Product", "A product used for testing", 1L, 2L, 10.0, 50,
				LocalDate.now(), LocalTime.now(), new byte[0], "test.jpg", 0);
		invalidIdProduct.setBase64Image(Base64.getEncoder().encodeToString(invalidIdProduct.getProductPhoto()));
		assertThrows(DataProcessingException.class, () -> productDao.update(invalidIdProduct));
	}

	@Test
	public void testUpdateProductProductNull() throws SQLException {
		String query = "UPDATE products SET product_name = ?, product_desc = ?, color_id = ? , category_id = ?, product_price= ?, product_quantity_in_stock = ?, product_photo =?, product_photo_name =? "
				+ "WHERE id = ? AND is_deleted = 0";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		doThrow(new NullPointerException()).when(statement).setString(1, null);
		assertThrows(DataProcessingException.class, () -> productDao.update(null));

		verify(connectionPool).getConnection();
		verify(connection).prepareStatement(query);

		verify(statement, never()).setString(anyInt(), anyString());
		verify(statement, never()).setLong(anyInt(), anyLong());
		verify(statement, never()).setDouble(anyInt(), anyDouble());
		verify(statement, never()).setInt(anyInt(), anyInt());
		verify(statement, never()).setBlob(anyInt(), (Blob) any());

		verify(statement, never()).executeUpdate();
		verify(statement).close();
		verify(connection).close();
	}

	@Test
	public void testUpdateProductDatabaseUnavailaible() throws SQLException {
		when(connectionPool.getConnection()).thenThrow(RuntimeException.class);
		Product product = new Product(1L, "Test Product", "A product used for testing", 1L, 2L, 10.0, 50,
				LocalDate.now(), LocalTime.now(), new byte[0], "test.jpg", 0);
		product.setBase64Image(Base64.getEncoder().encodeToString(product.getProductPhoto()));
		assertThrows(DataProcessingException.class, () -> productDao.update(product));
	}

	@Test
	public void testUpdateProductExceptionOccured() throws SQLException {
		when(statement.executeUpdate()).thenThrow(SQLException.class);
		Product product = new Product(1L, "Test Product", "A product used for testing", 1L, 2L, 10.0, 50,
				LocalDate.now(), LocalTime.now(), new byte[0], "test.jpg", 0);
		product.setBase64Image(Base64.getEncoder().encodeToString(product.getProductPhoto()));

		assertThrows(DataProcessingException.class, () -> productDao.update(product));
	}

	@Test
	public void testDeleteProduct() throws SQLException {
		Product product = new Product(1L, "Test Product", "A product used for testing", 1L, 2L, 10.0, 50,
				LocalDate.now(), LocalTime.now(), new byte[0], "test.jpg", 0);
		product.setBase64Image(Base64.getEncoder().encodeToString(product.getProductPhoto()));
		String selectQuery = "UPDATE products SET is_deleted = TRUE WHERE id = ? AND is_deleted = 0";

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(selectQuery)).thenReturn(statement);
		when(statement.executeUpdate()).thenReturn(1);

		boolean isDeleted = productDao.delete(product.getProductId());

		verify(statement).setLong(1, product.getProductId());
		verify(statement).executeUpdate();

		assertTrue(isDeleted);

		when(statement.executeUpdate()).thenReturn(0);
		boolean isNotDeleted = productDao.delete(product.getProductId());
		assertFalse(isNotDeleted);
	}

	@Test
	public void testDeleteProductFailProductAlreadyDeleted() throws SQLException {
		Product product = new Product(1L, "Test Product", "A product used for testing", 1L, 2L, 10.0, 50,
				LocalDate.now(), LocalTime.now(), new byte[0], "test.jpg", 1);
		product.setBase64Image(Base64.getEncoder().encodeToString(product.getProductPhoto()));
		String selectQuery = "UPDATE products SET is_deleted = TRUE WHERE id = ? AND is_deleted = 0";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(selectQuery)).thenReturn(statement);
		when(statement.executeUpdate()).thenReturn(0);
		boolean isDeleted = productDao.delete(product.getProductId());
		verify(statement).setLong(1, product.getProductId());
		verify(statement).executeUpdate();
		assertFalse(isDeleted);
	}

	@Test
	public void testDeleteProductExceptionOccured() throws SQLException {
		Product product = new Product(1L, "Test Product", "A product used for testing", 1L, 2L, 10.0, 50,
				LocalDate.now(), LocalTime.now(), new byte[0], "test.jpg", 1);
		product.setBase64Image(Base64.getEncoder().encodeToString(product.getProductPhoto()));
		String query = "UPDATE products SET is_deleted = TRUE WHERE id = ? AND is_deleted = 0";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		doThrow(new SQLException()).when(statement).executeUpdate();

		assertThrows(DataProcessingException.class, () -> productDao.delete(product.getProductId()));

		verify(statement).setLong(1, product.getProductId());
		verify(statement).executeUpdate();
	}

	@Test
	public void testDeleteProductDatabaseUnavailaible() throws SQLException {
		Product product = new Product(1L, "Test Product", "A product used for testing", 1L, 2L, 10.0, 50,
				LocalDate.now(), LocalTime.now(), new byte[0], "test.jpg", 1);
		product.setBase64Image(Base64.getEncoder().encodeToString(product.getProductPhoto()));

		when(connectionPool.getConnection()).thenThrow(RuntimeException.class);
		assertThrows(DataProcessingException.class, () -> productDao.delete(product.getProductId()));
	}


	@Test
	public void testGetAllByAllFiltersFullNumberOfRecordsIsOne() throws SQLException, IOException {
		String categoryId = "1";
		String colorId = "2";
		String min = "10";
		String max = "20";
		String sort = "expensive_first";
		int offset = 0;
		int noOfRecords = 1;
		byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };

		List<Product> expectedProducts = new ArrayList<>();

		Product product1 = new Product(1L, "Product 1", "Description 1", 1L, Long.parseLong(categoryId), 18.0, 5,
				LocalDate.of(2022, 1, 1), LocalTime.of(10, 0), data, "product1.jpg", 0);
		Product product2 = new Product(2L, "Product 2", "Description 2", 2L, Long.parseLong(categoryId), 15.0, 10,
				LocalDate.of(2022, 1, 2), LocalTime.of(11, 0), data, "product2.jpg", 0);
		product1.setBase64Image(Base64.getEncoder().encodeToString(product1.getProductPhoto()));
		product2.setBase64Image(Base64.getEncoder().encodeToString(product2.getProductPhoto()));

		expectedProducts.add(product1);

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(anyString())).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true).thenReturn(false).thenReturn(true);

		when(resultSet.getLong("id")).thenReturn(product1.getProductId(), product2.getProductId());
		when(resultSet.getString("product_name")).thenReturn(product1.getProductName(), product2.getProductName());
		when(resultSet.getString("product_desc")).thenReturn(product1.getProductDescription(),
				product2.getProductDescription());
		when(resultSet.getLong("color_id")).thenReturn(product1.getColorId(), product2.getColorId());
		when(resultSet.getLong("category_id")).thenReturn(product1.getCategoryId(), product2.getCategoryId());
		when(resultSet.getDouble("product_price")).thenReturn(product1.getProductPrice(), product2.getProductPrice());
		when(resultSet.getInt("product_quantity_in_stock")).thenReturn(product1.getProductQuantity(),
				product2.getProductQuantity());

		when(resultSet.getDate("product_date_of_addition")).thenReturn(date);
		when(date.toLocalDate()).thenReturn(product1.getProductDate(), product2.getProductDate());
		when(resultSet.getTime("product_time_of_addition")).thenReturn(time);
		when(time.toLocalTime()).thenReturn(product1.getProductTime(), product2.getProductTime());

		when(resultSet.getBlob("product_photo")).thenReturn(blob);
		when(blob.getBinaryStream()).thenReturn(inputStream);
		when(inputStream.readAllBytes()).thenReturn(product1.getProductPhoto(), product2.getProductPhoto());
		when(resultSet.getString("product_photo_name")).thenReturn(product1.getProductPhotoName(),
				product2.getProductPhotoName());
		when(resultSet.getInt("is_deleted")).thenReturn(product1.getIsdeleted(), product2.getIsdeleted());
		when(statement.executeQuery("SELECT FOUND_ROWS()")).thenReturn(resultSet);
		when(resultSet.getLong(1)).thenReturn(1L);

		List<Product> actualProducts = productDao.getAllBy(categoryId, colorId, min, max, sort, offset, noOfRecords);

		verify(connection).prepareStatement(anyString());
		verify(statement).executeQuery();
		verify(resultSet, times(3)).next();

		verify(resultSet, times(1)).getLong("id");
		verify(resultSet, times(1)).getString("product_name");
		verify(resultSet, times(1)).getString("product_desc");
		verify(resultSet, times(1)).getLong("color_id");
		verify(resultSet, times(1)).getLong("category_id");
		verify(resultSet, times(1)).getDouble("product_price");
		verify(resultSet, times(1)).getInt("product_quantity_in_stock");
		verify(resultSet, times(1)).getDate("product_date_of_addition");
		verify(date, times(1)).toLocalDate();
		verify(resultSet, times(1)).getTime("product_time_of_addition");
		verify(time, times(1)).toLocalTime();
		verify(resultSet, times(1)).getBlob("product_photo");
		verify(blob, times(1)).getBinaryStream();
		verify(inputStream, times(1)).readAllBytes();
		verify(resultSet, times(1)).getString("product_photo_name");
		verify(resultSet, times(1)).getInt("is_deleted");
		verify(resultSet).close();
		verify(statement).executeQuery("SELECT FOUND_ROWS()");

		assertEquals(expectedProducts.size(), actualProducts.size());

		for (int i = 0; i < expectedProducts.size(); i++) {
			assertEquals(expectedProducts.get(i).getProductId(), actualProducts.get(i).getProductId());
			assertEquals(expectedProducts.get(i).getProductName(), actualProducts.get(i).getProductName());
			assertEquals(expectedProducts.get(i).getProductDescription(),
					actualProducts.get(i).getProductDescription());
			assertEquals(expectedProducts.get(i).getColorId(), actualProducts.get(i).getColorId());
			assertEquals(expectedProducts.get(i).getCategoryId(), actualProducts.get(i).getCategoryId());
			assertEquals(expectedProducts.get(i).getProductPrice(), actualProducts.get(i).getProductPrice(), 0);
			assertEquals(expectedProducts.get(i).getProductQuantity(), actualProducts.get(i).getProductQuantity());
			assertEquals(expectedProducts.get(i).getProductDate(), actualProducts.get(i).getProductDate());
			assertEquals(expectedProducts.get(i).getProductTime(), actualProducts.get(i).getProductTime());
			assertEquals(expectedProducts.get(i).getProductPhoto(), actualProducts.get(i).getProductPhoto());
			assertEquals(expectedProducts.get(i).getBase64Image(), actualProducts.get(i).getBase64Image());
			assertEquals(expectedProducts.get(i).getProductPhotoName(), actualProducts.get(i).getProductPhotoName());
			assertEquals(expectedProducts.get(i).getIsdeleted(), actualProducts.get(i).getIsdeleted());
		}
	}

	@Test
	public void testGetAllByCategory() throws SQLException, IOException {
		String categoryId = "1";
		String colorId = "";
		String min = "";
		String max = "";
		String sort = "";
		int offset = 0;
		int noOfRecords = 10;
		byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };

		List<Product> expectedProducts = new ArrayList<>();

		Product product1 = new Product(1L, "Product 1", "Description 1", 1L, Long.parseLong(categoryId), 18.0, 5,
				LocalDate.of(2022, 1, 1), LocalTime.of(10, 0), data, "product1.jpg", 0);
		Product product2 = new Product(2L, "Product 2", "Description 2", 2L, Long.parseLong(categoryId), 15.0, 10,
				LocalDate.of(2022, 1, 2), LocalTime.of(11, 0), data, "product2.jpg", 0);
		product1.setBase64Image(Base64.getEncoder().encodeToString(product1.getProductPhoto()));
		product2.setBase64Image(Base64.getEncoder().encodeToString(product2.getProductPhoto()));

		expectedProducts.add(product1);
		expectedProducts.add(product2);

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(anyString())).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false).thenReturn(true);

		when(resultSet.getLong("id")).thenReturn(product1.getProductId(), product2.getProductId());
		when(resultSet.getString("product_name")).thenReturn(product1.getProductName(), product2.getProductName());
		when(resultSet.getString("product_desc")).thenReturn(product1.getProductDescription(),
				product2.getProductDescription());
		when(resultSet.getLong("color_id")).thenReturn(product1.getColorId(), product2.getColorId());
		when(resultSet.getLong("category_id")).thenReturn(product1.getCategoryId(), product2.getCategoryId());
		when(resultSet.getDouble("product_price")).thenReturn(product1.getProductPrice(), product2.getProductPrice());
		when(resultSet.getInt("product_quantity_in_stock")).thenReturn(product1.getProductQuantity(),
				product2.getProductQuantity());

		when(resultSet.getDate("product_date_of_addition")).thenReturn(date);
		when(date.toLocalDate()).thenReturn(product1.getProductDate(), product2.getProductDate());
		when(resultSet.getTime("product_time_of_addition")).thenReturn(time);
		when(time.toLocalTime()).thenReturn(product1.getProductTime(), product2.getProductTime());

		when(resultSet.getBlob("product_photo")).thenReturn(blob);
		when(blob.getBinaryStream()).thenReturn(inputStream);
		when(inputStream.readAllBytes()).thenReturn(product1.getProductPhoto(), product2.getProductPhoto());
		when(resultSet.getString("product_photo_name")).thenReturn(product1.getProductPhotoName(),
				product2.getProductPhotoName());
		when(resultSet.getInt("is_deleted")).thenReturn(product1.getIsdeleted(), product2.getIsdeleted());
		when(statement.executeQuery("SELECT FOUND_ROWS()")).thenReturn(resultSet);
		when(resultSet.getLong(1)).thenReturn(1L);

		List<Product> actualProducts = productDao.getAllBy(categoryId, colorId, min, max, sort, offset, noOfRecords);

		verify(connection).prepareStatement(anyString());
		verify(statement).executeQuery();
		verify(resultSet, times(4)).next();

		verify(resultSet, times(2)).getLong("id");
		verify(resultSet, times(2)).getString("product_name");
		verify(resultSet, times(2)).getString("product_desc");
		verify(resultSet, times(2)).getLong("color_id");
		verify(resultSet, times(2)).getLong("category_id");
		verify(resultSet, times(2)).getDouble("product_price");
		verify(resultSet, times(2)).getInt("product_quantity_in_stock");
		verify(resultSet, times(2)).getDate("product_date_of_addition");
		verify(date, times(2)).toLocalDate();
		verify(resultSet, times(2)).getTime("product_time_of_addition");
		verify(time, times(2)).toLocalTime();
		verify(resultSet, times(2)).getBlob("product_photo");
		verify(blob, times(2)).getBinaryStream();
		verify(inputStream, times(2)).readAllBytes();
		verify(resultSet, times(2)).getString("product_photo_name");
		verify(resultSet, times(2)).getInt("is_deleted");
		verify(resultSet).close();
		verify(statement).executeQuery("SELECT FOUND_ROWS()");

		assertEquals(expectedProducts.size(), actualProducts.size());

		for (int i = 0; i < expectedProducts.size(); i++) {
			assertEquals(expectedProducts.get(i).getProductId(), actualProducts.get(i).getProductId());
			assertEquals(expectedProducts.get(i).getProductName(), actualProducts.get(i).getProductName());
			assertEquals(expectedProducts.get(i).getProductDescription(),
					actualProducts.get(i).getProductDescription());
			assertEquals(expectedProducts.get(i).getColorId(), actualProducts.get(i).getColorId());
			assertEquals(expectedProducts.get(i).getCategoryId(), actualProducts.get(i).getCategoryId());
			assertEquals(expectedProducts.get(i).getProductPrice(), actualProducts.get(i).getProductPrice(), 0);
			assertEquals(expectedProducts.get(i).getProductQuantity(), actualProducts.get(i).getProductQuantity());
			assertEquals(expectedProducts.get(i).getProductDate(), actualProducts.get(i).getProductDate());
			assertEquals(expectedProducts.get(i).getProductTime(), actualProducts.get(i).getProductTime());
			assertEquals(expectedProducts.get(i).getProductPhoto(), actualProducts.get(i).getProductPhoto());
			assertEquals(expectedProducts.get(i).getBase64Image(), actualProducts.get(i).getBase64Image());
			assertEquals(expectedProducts.get(i).getProductPhotoName(), actualProducts.get(i).getProductPhotoName());
			assertEquals(expectedProducts.get(i).getIsdeleted(), actualProducts.get(i).getIsdeleted());
		}
	}

	@Test
	public void testGetAllByColor() throws SQLException, IOException {
		// Prepare test data
		String categoryId = "";
		String colorId = "2";
		String min = "";
		String max = "";
		String sort = "";
		int offset = 0;
		int noOfRecords = 10;
		byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };

		List<Product> expectedProducts = new ArrayList<>();

		Product product1 = new Product(1L, "Product 1", "Description 1", Long.parseLong(colorId), 1L, 18.0, 5,
				LocalDate.of(2022, 1, 1), LocalTime.of(10, 0), data, "product1.jpg", 0);
		Product product2 = new Product(2L, "Product 2", "Description 2", Long.parseLong(colorId), 2L, 15.0, 10,
				LocalDate.of(2022, 1, 2), LocalTime.of(11, 0), data, "product2.jpg", 0);
		product1.setBase64Image(Base64.getEncoder().encodeToString(product1.getProductPhoto()));
		product2.setBase64Image(Base64.getEncoder().encodeToString(product2.getProductPhoto()));

		expectedProducts.add(product1);
		expectedProducts.add(product2);

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(anyString())).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false).thenReturn(true);

		when(resultSet.getLong("id")).thenReturn(product1.getProductId(), product2.getProductId());
		when(resultSet.getString("product_name")).thenReturn(product1.getProductName(), product2.getProductName());
		when(resultSet.getString("product_desc")).thenReturn(product1.getProductDescription(),
				product2.getProductDescription());
		when(resultSet.getLong("color_id")).thenReturn(product1.getColorId(), product2.getColorId());
		when(resultSet.getLong("category_id")).thenReturn(product1.getCategoryId(), product2.getCategoryId());
		when(resultSet.getDouble("product_price")).thenReturn(product1.getProductPrice(), product2.getProductPrice());
		when(resultSet.getInt("product_quantity_in_stock")).thenReturn(product1.getProductQuantity(),
				product2.getProductQuantity());

		when(resultSet.getDate("product_date_of_addition")).thenReturn(date);
		when(date.toLocalDate()).thenReturn(product1.getProductDate(), product2.getProductDate());
		when(resultSet.getTime("product_time_of_addition")).thenReturn(time);
		when(time.toLocalTime()).thenReturn(product1.getProductTime(), product2.getProductTime());

		when(resultSet.getBlob("product_photo")).thenReturn(blob);
		when(blob.getBinaryStream()).thenReturn(inputStream);
		when(inputStream.readAllBytes()).thenReturn(product1.getProductPhoto(), product2.getProductPhoto());
		when(resultSet.getString("product_photo_name")).thenReturn(product1.getProductPhotoName(),
				product2.getProductPhotoName());
		when(resultSet.getInt("is_deleted")).thenReturn(product1.getIsdeleted(), product2.getIsdeleted());
		when(statement.executeQuery("SELECT FOUND_ROWS()")).thenReturn(resultSet);
		when(resultSet.getLong(1)).thenReturn(1L);

		List<Product> actualProducts = productDao.getAllBy(categoryId, colorId, min, max, sort, offset, noOfRecords);

		verify(connection).prepareStatement(anyString());
		verify(statement).executeQuery();
		verify(resultSet, times(4)).next();

		verify(resultSet, times(2)).getLong("id");
		verify(resultSet, times(2)).getString("product_name");
		verify(resultSet, times(2)).getString("product_desc");
		verify(resultSet, times(2)).getLong("color_id");
		verify(resultSet, times(2)).getLong("category_id");
		verify(resultSet, times(2)).getDouble("product_price");
		verify(resultSet, times(2)).getInt("product_quantity_in_stock");
		verify(resultSet, times(2)).getDate("product_date_of_addition");
		verify(date, times(2)).toLocalDate();
		verify(resultSet, times(2)).getTime("product_time_of_addition");
		verify(time, times(2)).toLocalTime();
		verify(resultSet, times(2)).getBlob("product_photo");
		verify(blob, times(2)).getBinaryStream();
		verify(inputStream, times(2)).readAllBytes();
		verify(resultSet, times(2)).getString("product_photo_name");
		verify(resultSet, times(2)).getInt("is_deleted");
		verify(resultSet).close();
		verify(statement).executeQuery("SELECT FOUND_ROWS()");

		assertEquals(expectedProducts.size(), actualProducts.size());

		for (int i = 0; i < expectedProducts.size(); i++) {
			assertEquals(expectedProducts.get(i).getProductId(), actualProducts.get(i).getProductId());
			assertEquals(expectedProducts.get(i).getProductName(), actualProducts.get(i).getProductName());
			assertEquals(expectedProducts.get(i).getProductDescription(),
					actualProducts.get(i).getProductDescription());
			assertEquals(expectedProducts.get(i).getColorId(), actualProducts.get(i).getColorId());
			assertEquals(expectedProducts.get(i).getCategoryId(), actualProducts.get(i).getCategoryId());
			assertEquals(expectedProducts.get(i).getProductPrice(), actualProducts.get(i).getProductPrice(), 0);
			assertEquals(expectedProducts.get(i).getProductQuantity(), actualProducts.get(i).getProductQuantity());
			assertEquals(expectedProducts.get(i).getProductDate(), actualProducts.get(i).getProductDate());
			assertEquals(expectedProducts.get(i).getProductTime(), actualProducts.get(i).getProductTime());
			assertEquals(expectedProducts.get(i).getProductPhoto(), actualProducts.get(i).getProductPhoto());
			assertEquals(expectedProducts.get(i).getBase64Image(), actualProducts.get(i).getBase64Image());
			assertEquals(expectedProducts.get(i).getProductPhotoName(), actualProducts.get(i).getProductPhotoName());
			assertEquals(expectedProducts.get(i).getIsdeleted(), actualProducts.get(i).getIsdeleted());
		}
	}

	@Test
	public void testGetAllByPriceRange() throws SQLException, IOException {
		String categoryId = "";
		String colorId = "";
		String min = "10";
		String max = "20";
		String sort = "";
		int offset = 0;
		int noOfRecords = 10;
		byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };

		List<Product> expectedProducts = new ArrayList<>();

		Product product1 = new Product(1L, "Product 1", "Description 1", 2L, 1L, 18.0, 5, LocalDate.of(2022, 1, 1),
				LocalTime.of(10, 0), data, "product1.jpg", 0);
		Product product2 = new Product(2L, "Product 2", "Description 2", 1L, 2L, 15.0, 10, LocalDate.of(2022, 1, 2),
				LocalTime.of(11, 0), data, "product2.jpg", 0);
		product1.setBase64Image(Base64.getEncoder().encodeToString(product1.getProductPhoto()));
		product2.setBase64Image(Base64.getEncoder().encodeToString(product2.getProductPhoto()));

		expectedProducts.add(product1);
		expectedProducts.add(product2);

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(anyString())).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false).thenReturn(true);

		when(resultSet.getLong("id")).thenReturn(product1.getProductId(), product2.getProductId());
		when(resultSet.getString("product_name")).thenReturn(product1.getProductName(), product2.getProductName());
		when(resultSet.getString("product_desc")).thenReturn(product1.getProductDescription(),
				product2.getProductDescription());
		when(resultSet.getLong("color_id")).thenReturn(product1.getColorId(), product2.getColorId());
		when(resultSet.getLong("category_id")).thenReturn(product1.getCategoryId(), product2.getCategoryId());
		when(resultSet.getDouble("product_price")).thenReturn(product1.getProductPrice(), product2.getProductPrice());
		when(resultSet.getInt("product_quantity_in_stock")).thenReturn(product1.getProductQuantity(),
				product2.getProductQuantity());

		when(resultSet.getDate("product_date_of_addition")).thenReturn(date);
		when(date.toLocalDate()).thenReturn(product1.getProductDate(), product2.getProductDate());
		when(resultSet.getTime("product_time_of_addition")).thenReturn(time);
		when(time.toLocalTime()).thenReturn(product1.getProductTime(), product2.getProductTime());

		when(resultSet.getBlob("product_photo")).thenReturn(blob);
		when(blob.getBinaryStream()).thenReturn(inputStream);
		when(inputStream.readAllBytes()).thenReturn(product1.getProductPhoto(), product2.getProductPhoto());
		when(resultSet.getString("product_photo_name")).thenReturn(product1.getProductPhotoName(),
				product2.getProductPhotoName());
		when(resultSet.getInt("is_deleted")).thenReturn(product1.getIsdeleted(), product2.getIsdeleted());
		when(statement.executeQuery("SELECT FOUND_ROWS()")).thenReturn(resultSet);
		when(resultSet.getLong(1)).thenReturn(1L);

		List<Product> actualProducts = productDao.getAllBy(categoryId, colorId, min, max, sort, offset, noOfRecords);

		verify(connection).prepareStatement(anyString());
		verify(statement).executeQuery();
		verify(resultSet, times(4)).next();

		verify(resultSet, times(2)).getLong("id");
		verify(resultSet, times(2)).getString("product_name");
		verify(resultSet, times(2)).getString("product_desc");
		verify(resultSet, times(2)).getLong("color_id");
		verify(resultSet, times(2)).getLong("category_id");
		verify(resultSet, times(2)).getDouble("product_price");
		verify(resultSet, times(2)).getInt("product_quantity_in_stock");
		verify(resultSet, times(2)).getDate("product_date_of_addition");
		verify(date, times(2)).toLocalDate();
		verify(resultSet, times(2)).getTime("product_time_of_addition");
		verify(time, times(2)).toLocalTime();
		verify(resultSet, times(2)).getBlob("product_photo");
		verify(blob, times(2)).getBinaryStream();
		verify(inputStream, times(2)).readAllBytes();
		verify(resultSet, times(2)).getString("product_photo_name");
		verify(resultSet, times(2)).getInt("is_deleted");
		verify(resultSet).close();
		verify(statement).executeQuery("SELECT FOUND_ROWS()");

		assertEquals(expectedProducts.size(), actualProducts.size());

		for (int i = 0; i < expectedProducts.size(); i++) {
			assertEquals(expectedProducts.get(i).getProductId(), actualProducts.get(i).getProductId());
			assertEquals(expectedProducts.get(i).getProductName(), actualProducts.get(i).getProductName());
			assertEquals(expectedProducts.get(i).getProductDescription(),
					actualProducts.get(i).getProductDescription());
			assertEquals(expectedProducts.get(i).getColorId(), actualProducts.get(i).getColorId());
			assertEquals(expectedProducts.get(i).getCategoryId(), actualProducts.get(i).getCategoryId());
			assertEquals(expectedProducts.get(i).getProductPrice(), actualProducts.get(i).getProductPrice(), 0);
			assertEquals(expectedProducts.get(i).getProductQuantity(), actualProducts.get(i).getProductQuantity());
			assertEquals(expectedProducts.get(i).getProductDate(), actualProducts.get(i).getProductDate());
			assertEquals(expectedProducts.get(i).getProductTime(), actualProducts.get(i).getProductTime());
			assertEquals(expectedProducts.get(i).getProductPhoto(), actualProducts.get(i).getProductPhoto());
			assertEquals(expectedProducts.get(i).getBase64Image(), actualProducts.get(i).getBase64Image());
			assertEquals(expectedProducts.get(i).getProductPhotoName(), actualProducts.get(i).getProductPhotoName());
			assertEquals(expectedProducts.get(i).getIsdeleted(), actualProducts.get(i).getIsdeleted());
		}
	}

	@Test
	public void testGetAllSortExpensiveFirst() throws SQLException, IOException {
		String categoryId = "";
		String colorId = "";
		String min = "";
		String max = "";
		String sort = "expensive_first";
		int offset = 0;
		int noOfRecords = 10;
		byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };

		List<Product> expectedProducts = new ArrayList<>();

		Product product1 = new Product(1L, "Product 1", "Description 1", 2L, 1L, 18.0, 5, LocalDate.of(2022, 1, 1),
				LocalTime.of(10, 0), data, "product1.jpg", 0);
		Product product2 = new Product(2L, "Product 2", "Description 2", 1L, 2L, 15.0, 10, LocalDate.of(2022, 1, 2),
				LocalTime.of(11, 0), data, "product2.jpg", 0);
		product1.setBase64Image(Base64.getEncoder().encodeToString(product1.getProductPhoto()));
		product2.setBase64Image(Base64.getEncoder().encodeToString(product2.getProductPhoto()));

		expectedProducts.add(product1);
		expectedProducts.add(product2);

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(anyString())).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false).thenReturn(true);

		when(resultSet.getLong("id")).thenReturn(product1.getProductId(), product2.getProductId());
		when(resultSet.getString("product_name")).thenReturn(product1.getProductName(), product2.getProductName());
		when(resultSet.getString("product_desc")).thenReturn(product1.getProductDescription(),
				product2.getProductDescription());
		when(resultSet.getLong("color_id")).thenReturn(product1.getColorId(), product2.getColorId());
		when(resultSet.getLong("category_id")).thenReturn(product1.getCategoryId(), product2.getCategoryId());
		when(resultSet.getDouble("product_price")).thenReturn(product1.getProductPrice(), product2.getProductPrice());
		when(resultSet.getInt("product_quantity_in_stock")).thenReturn(product1.getProductQuantity(),
				product2.getProductQuantity());

		when(resultSet.getDate("product_date_of_addition")).thenReturn(date);
		when(date.toLocalDate()).thenReturn(product1.getProductDate(), product2.getProductDate());
		when(resultSet.getTime("product_time_of_addition")).thenReturn(time);
		when(time.toLocalTime()).thenReturn(product1.getProductTime(), product2.getProductTime());

		when(resultSet.getBlob("product_photo")).thenReturn(blob);
		when(blob.getBinaryStream()).thenReturn(inputStream);
		when(inputStream.readAllBytes()).thenReturn(product1.getProductPhoto(), product2.getProductPhoto());
		when(resultSet.getString("product_photo_name")).thenReturn(product1.getProductPhotoName(),
				product2.getProductPhotoName());
		when(resultSet.getInt("is_deleted")).thenReturn(product1.getIsdeleted(), product2.getIsdeleted());
		when(statement.executeQuery("SELECT FOUND_ROWS()")).thenReturn(resultSet);
		when(resultSet.getLong(1)).thenReturn(1L);

		List<Product> actualProducts = productDao.getAllBy(categoryId, colorId, min, max, sort, offset, noOfRecords);

		verify(connection).prepareStatement(anyString());
		verify(statement).executeQuery();
		verify(resultSet, times(4)).next();

		verify(resultSet, times(2)).getLong("id");
		verify(resultSet, times(2)).getString("product_name");
		verify(resultSet, times(2)).getString("product_desc");
		verify(resultSet, times(2)).getLong("color_id");
		verify(resultSet, times(2)).getLong("category_id");
		verify(resultSet, times(2)).getDouble("product_price");
		verify(resultSet, times(2)).getInt("product_quantity_in_stock");
		verify(resultSet, times(2)).getDate("product_date_of_addition");
		verify(date, times(2)).toLocalDate();
		verify(resultSet, times(2)).getTime("product_time_of_addition");
		verify(time, times(2)).toLocalTime();
		verify(resultSet, times(2)).getBlob("product_photo");
		verify(blob, times(2)).getBinaryStream();
		verify(inputStream, times(2)).readAllBytes();
		verify(resultSet, times(2)).getString("product_photo_name");
		verify(resultSet, times(2)).getInt("is_deleted");
		verify(resultSet).close();
		verify(statement).executeQuery("SELECT FOUND_ROWS()");

		assertEquals(expectedProducts.size(), actualProducts.size());

		for (int i = 0; i < expectedProducts.size(); i++) {
			assertEquals(expectedProducts.get(i).getProductId(), actualProducts.get(i).getProductId());
			assertEquals(expectedProducts.get(i).getProductName(), actualProducts.get(i).getProductName());
			assertEquals(expectedProducts.get(i).getProductDescription(),
					actualProducts.get(i).getProductDescription());
			assertEquals(expectedProducts.get(i).getColorId(), actualProducts.get(i).getColorId());
			assertEquals(expectedProducts.get(i).getCategoryId(), actualProducts.get(i).getCategoryId());
			assertEquals(expectedProducts.get(i).getProductPrice(), actualProducts.get(i).getProductPrice(), 0);
			assertEquals(expectedProducts.get(i).getProductQuantity(), actualProducts.get(i).getProductQuantity());
			assertEquals(expectedProducts.get(i).getProductDate(), actualProducts.get(i).getProductDate());
			assertEquals(expectedProducts.get(i).getProductTime(), actualProducts.get(i).getProductTime());
			assertEquals(expectedProducts.get(i).getProductPhoto(), actualProducts.get(i).getProductPhoto());
			assertEquals(expectedProducts.get(i).getBase64Image(), actualProducts.get(i).getBase64Image());
			assertEquals(expectedProducts.get(i).getProductPhotoName(), actualProducts.get(i).getProductPhotoName());
			assertEquals(expectedProducts.get(i).getIsdeleted(), actualProducts.get(i).getIsdeleted());
		}
	}

	@Test
	public void testGetAllByEmptyFilters() throws SQLException, IOException {
		// Prepare test data
		String categoryId = "";
		String colorId = "";
		String min = "";
		String max = "";
		String sort = "";
		int offset = 0;
		int noOfRecords = 10;
		byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };

		List<Product> expectedProducts = new ArrayList<>();

		Product product1 = new Product(1L, "Product 1", "Description 1", 1L, 1L, 18.0, 5, LocalDate.of(2022, 1, 1),
				LocalTime.of(10, 0), data, "product1.jpg", 0);
		Product product2 = new Product(2L, "Product 2", "Description 2", 2L, 2L, 15.0, 10, LocalDate.of(2022, 1, 2),
				LocalTime.of(11, 0), data, "product2.jpg", 0);
		product1.setBase64Image(Base64.getEncoder().encodeToString(product1.getProductPhoto()));
		product2.setBase64Image(Base64.getEncoder().encodeToString(product2.getProductPhoto()));

		expectedProducts.add(product1);
		expectedProducts.add(product2);

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(anyString())).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false).thenReturn(true);

		when(resultSet.getLong("id")).thenReturn(product1.getProductId(), product2.getProductId());
		when(resultSet.getString("product_name")).thenReturn(product1.getProductName(), product2.getProductName());
		when(resultSet.getString("product_desc")).thenReturn(product1.getProductDescription(),
				product2.getProductDescription());
		when(resultSet.getLong("color_id")).thenReturn(product1.getColorId(), product2.getColorId());
		when(resultSet.getLong("category_id")).thenReturn(product1.getCategoryId(), product2.getCategoryId());
		when(resultSet.getDouble("product_price")).thenReturn(product1.getProductPrice(), product2.getProductPrice());
		when(resultSet.getInt("product_quantity_in_stock")).thenReturn(product1.getProductQuantity(),
				product2.getProductQuantity());

		when(resultSet.getDate("product_date_of_addition")).thenReturn(date);
		when(date.toLocalDate()).thenReturn(product1.getProductDate(), product2.getProductDate());
		when(resultSet.getTime("product_time_of_addition")).thenReturn(time);
		when(time.toLocalTime()).thenReturn(product1.getProductTime(), product2.getProductTime());

		when(resultSet.getBlob("product_photo")).thenReturn(blob);
		when(blob.getBinaryStream()).thenReturn(inputStream);
		when(inputStream.readAllBytes()).thenReturn(product1.getProductPhoto(), product2.getProductPhoto());
		when(resultSet.getString("product_photo_name")).thenReturn(product1.getProductPhotoName(),
				product2.getProductPhotoName());
		when(resultSet.getInt("is_deleted")).thenReturn(product1.getIsdeleted(), product2.getIsdeleted());
		when(statement.executeQuery("SELECT FOUND_ROWS()")).thenReturn(resultSet);
		when(resultSet.getLong(1)).thenReturn(1L);

		List<Product> actualProducts = productDao.getAllBy(categoryId, colorId, min, max, sort, offset, noOfRecords);

		verify(connection).prepareStatement(anyString());
		verify(statement).executeQuery();
		verify(resultSet, times(4)).next();

		verify(resultSet, times(2)).getLong("id");
		verify(resultSet, times(2)).getString("product_name");
		verify(resultSet, times(2)).getString("product_desc");
		verify(resultSet, times(2)).getLong("color_id");
		verify(resultSet, times(2)).getLong("category_id");
		verify(resultSet, times(2)).getDouble("product_price");
		verify(resultSet, times(2)).getInt("product_quantity_in_stock");
		verify(resultSet, times(2)).getDate("product_date_of_addition");
		verify(date, times(2)).toLocalDate();
		verify(resultSet, times(2)).getTime("product_time_of_addition");
		verify(time, times(2)).toLocalTime();
		verify(resultSet, times(2)).getBlob("product_photo");
		verify(blob, times(2)).getBinaryStream();
		verify(inputStream, times(2)).readAllBytes();
		verify(resultSet, times(2)).getString("product_photo_name");
		verify(resultSet, times(2)).getInt("is_deleted");
		verify(resultSet).close();
		verify(statement).executeQuery("SELECT FOUND_ROWS()");

		assertEquals(expectedProducts.size(), actualProducts.size());

		for (int i = 0; i < expectedProducts.size(); i++) {
			assertEquals(expectedProducts.get(i).getProductId(), actualProducts.get(i).getProductId());
			assertEquals(expectedProducts.get(i).getProductName(), actualProducts.get(i).getProductName());
			assertEquals(expectedProducts.get(i).getProductDescription(),
					actualProducts.get(i).getProductDescription());
			assertEquals(expectedProducts.get(i).getColorId(), actualProducts.get(i).getColorId());
			assertEquals(expectedProducts.get(i).getCategoryId(), actualProducts.get(i).getCategoryId());
			assertEquals(expectedProducts.get(i).getProductPrice(), actualProducts.get(i).getProductPrice(), 0);
			assertEquals(expectedProducts.get(i).getProductQuantity(), actualProducts.get(i).getProductQuantity());
			assertEquals(expectedProducts.get(i).getProductDate(), actualProducts.get(i).getProductDate());
			assertEquals(expectedProducts.get(i).getProductTime(), actualProducts.get(i).getProductTime());
			assertEquals(expectedProducts.get(i).getProductPhoto(), actualProducts.get(i).getProductPhoto());
			assertEquals(expectedProducts.get(i).getBase64Image(), actualProducts.get(i).getBase64Image());
			assertEquals(expectedProducts.get(i).getProductPhotoName(), actualProducts.get(i).getProductPhotoName());
			assertEquals(expectedProducts.get(i).getIsdeleted(), actualProducts.get(i).getIsdeleted());
		}

	}

	@Test
	public void testGetAllByDatabaseUnavailaible() throws SQLException, IOException {
		String categoryId = "1";
		String colorId = "2";
		String min = "10";
		String max = "20";
		String sort = "expensive_first";
		int offset = 0;
		int noOfRecords = 1;

		when(connectionPool.getConnection()).thenThrow(RuntimeException.class);
		assertThrows(DataProcessingException.class,
				() -> productDao.getAllBy(categoryId, colorId, min, max, sort, offset, noOfRecords));

		verify(connection, never()).prepareStatement(anyString());
		verify(statement, never()).executeQuery();
		verify(resultSet, never()).next();

		verify(resultSet, never()).getLong("id");
		verify(resultSet, never()).getString("product_name");
		verify(resultSet, never()).getString("product_desc");
		verify(resultSet, never()).getLong("color_id");
		verify(resultSet, never()).getLong("category_id");
		verify(resultSet, never()).getDouble("product_price");
		verify(resultSet, never()).getInt("product_quantity_in_stock");
		verify(resultSet, never()).getDate("product_date_of_addition");
		verify(date, never()).toLocalDate();
		verify(resultSet, never()).getTime("product_time_of_addition");
		verify(time, never()).toLocalTime();
		verify(resultSet, never()).getBlob("product_photo");
		verify(blob, never()).getBinaryStream();
		verify(inputStream, never()).readAllBytes();
		verify(resultSet, never()).getString("product_photo_name");
		verify(resultSet, never()).getInt("is_deleted");
		verify(resultSet, never()).close();
		verify(statement, never()).executeQuery("SELECT FOUND_ROWS()");
	}

	@Test
	public void testGetAllByThrowsException() throws SQLException, IOException {
		String categoryId = "1";
		String colorId = "2";
		String min = "10";
		String max = "20";
		String sort = "expensive_first";
		int offset = 0;
		int noOfRecords = 1;

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(anyString())).thenReturn(statement);
		when(statement.executeQuery()).thenThrow(SQLException.class);

		assertThrows(DataProcessingException.class,
				() -> productDao.getAllBy(categoryId, colorId, min, max, sort, offset, noOfRecords));

		verify(connection, times(1)).prepareStatement(anyString());
		verify(statement, times(1)).executeQuery();
		verify(resultSet, never()).next();

		verify(resultSet, never()).getLong("id");
		verify(resultSet, never()).getString("product_name");
		verify(resultSet, never()).getString("product_desc");
		verify(resultSet, never()).getLong("color_id");
		verify(resultSet, never()).getLong("category_id");
		verify(resultSet, never()).getDouble("product_price");
		verify(resultSet, never()).getInt("product_quantity_in_stock");
		verify(resultSet, never()).getDate("product_date_of_addition");
		verify(date, never()).toLocalDate();
		verify(resultSet, never()).getTime("product_time_of_addition");
		verify(time, never()).toLocalTime();
		verify(resultSet, never()).getBlob("product_photo");
		verify(blob, never()).getBinaryStream();
		verify(inputStream, never()).readAllBytes();
		verify(resultSet, never()).getString("product_photo_name");
		verify(resultSet, never()).getInt("is_deleted");
		verify(resultSet, never()).close();
		verify(statement, never()).executeQuery("SELECT FOUND_ROWS()");

	}

	@Test
	public void testGetMinPriceReturnsMinimumPriceWhenResultSetIsNotEmpty() throws SQLException {
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(anyString())).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true);
		when(resultSet.getDouble("product_price")).thenReturn(10.0);

		assertEquals(10.0, productDao.getMinPrice(), 0.0);
	}

	@Test
	public void testGetMinPriceReturnsZeroWhenResultSetIsEmpty() throws SQLException {
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(anyString())).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(false);

		assertEquals(0.0, productDao.getMinPrice(), 0.0);
	}

	@Test
	public void testGetMinPriceThrowsDataProcessingExceptionWhenSQLExceptionOccurs() throws SQLException {
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(anyString())).thenReturn(statement);
		when(statement.executeQuery()).thenThrow(new SQLException());

		assertThrows(DataProcessingException.class, () -> productDao.getMinPrice());
	}

	@Test
	public void testGetMinPriceThrowsDataProcessingExceptionWhenDatabaseIsNotAvailaible() throws SQLException {
		when(connectionPool.getConnection()).thenThrow(RuntimeException.class);
		assertThrows(DataProcessingException.class, () -> productDao.getMinPrice());
	}

	@Test
	public void testGetMaxPriceReturnsMaximumPriceWhenResultSetIsNotEmpty() throws SQLException {
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(anyString())).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true);
		when(resultSet.getDouble("product_price")).thenReturn(100.0);

		double maxPrice = productDao.getMaxPrice();
		assertEquals(100.0, maxPrice, 0.0);
	}

	@Test
	public void testGetMaxPriceReturnsZeroWhenResultSetIsEmpty() throws SQLException {
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(anyString())).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(false);

		double maxPrice = productDao.getMaxPrice();
		assertEquals(0.0, maxPrice, 0.0);
	}

	@Test
	public void testGetMaxPriceThrowsDataProcessingExceptionWhenSQLExceptionOccurs() throws SQLException {
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(anyString())).thenReturn(statement);
		when(statement.executeQuery()).thenThrow(new SQLException());

		assertThrows(DataProcessingException.class, () -> productDao.getMaxPrice());
	}

	@Test
	public void testGetMaxPriceThrowsDataProcessingExceptionWhenDatabaseIsNotAvailaible() throws SQLException {
		when(connectionPool.getConnection()).thenThrow(RuntimeException.class);
		assertThrows(DataProcessingException.class, () -> productDao.getMaxPrice());
	}

}