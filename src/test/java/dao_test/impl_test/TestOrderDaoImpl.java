package dao_test.impl_test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dao.OrderDao;
import dao.impl.OrderDaoImpl;
import dbconnection_pool.ConnectionPool;
import exception.DataProcessingException;
import models.Order;

public class TestOrderDaoImpl { // done, add more test cases
	@Mock
	private ConnectionPool connectionPool;
	@Mock
	private Connection connection;
	@Mock
	private PreparedStatement statement;
	@Mock
	private ResultSet resultSet;
	@Mock
	private Date date;
	@Mock
	private Time time;

	private OrderDao orderDao;

	@Before
	public void setUp() throws Exception {
		connectionPool = mock(ConnectionPool.class);
		connection = mock(Connection.class);
		statement = mock(PreparedStatement.class);
		resultSet = mock(ResultSet.class);
		date = mock(Date.class);
		time = mock(Time.class);
		MockitoAnnotations.openMocks(this);
		orderDao = new OrderDaoImpl(connectionPool);

	}

	@Test
	public void testCreate() throws Exception {
		Order order = new Order(1L, 2L, LocalDate.now(), LocalTime.now(), "Test Address 1", "+380991083238",
				"registered");

		String query = "INSERT INTO orders (user_id, order_date, order_time, order_address, order_phone, order_status) VALUES (?, ?, ?, ?, ?, ?)";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(statement);
		when(statement.getGeneratedKeys()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true);
		when(resultSet.getObject(1, Long.class)).thenReturn(1L);

		Order createdOrder = orderDao.create(order);
		assertEquals(order.getOrderId(), createdOrder.getOrderId());
		assertEquals(order.getUserId(), createdOrder.getUserId());
		assertEquals(order.getOrderDate(), createdOrder.getOrderDate());
		assertEquals(order.getOrderTime(), createdOrder.getOrderTime());
		assertEquals(order.getOrderAddress(), createdOrder.getOrderAddress());
		assertEquals(order.getOrderPhone(), createdOrder.getOrderPhone());
		assertEquals(order.getOrderStatus(), createdOrder.getOrderStatus());
	}

	@Test
	public void testCreateInvalidOrderInput() throws Exception {
		Order order = new Order(1L, 2L, LocalDate.now(), LocalTime.now(), "", "", "");

		String query = "INSERT INTO orders (user_id, order_date, order_time, order_address, order_phone, order_status) VALUES (?, ?, ?, ?, ?, ?)";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(statement);
		doThrow(new NullPointerException()).when(statement).setString(4, order.getOrderAddress());
		assertThrows(DataProcessingException.class, () -> orderDao.create(order));

		verify(connectionPool).getConnection();
		verify(connection).prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		verify(statement, never()).setString(5, order.getOrderPhone());
		verify(statement, never()).setString(6, order.getOrderStatus());
		verify(statement, never()).executeUpdate();
		verify(statement).close();
		verify(connection).close();
	}

	@Test
	public void testCreateNullOrder() throws Exception {
		String query = "INSERT INTO orders (user_id, order_date, order_time, order_address, order_phone, order_status) VALUES (?, ?, ?, ?, ?, ?)";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(statement);
		doThrow(new NullPointerException()).when(statement).setString(1, null);

		assertThrows(DataProcessingException.class, () -> orderDao.create(null));

		verify(connectionPool).getConnection();
		verify(connection).prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		verify(statement, never()).setDate(anyInt(), any());
		verify(statement, never()).setTime(anyInt(), any());
		verify(statement, never()).setLong(anyInt(), anyLong());
		verify(statement, never()).setString(anyInt(), anyString());

		verify(statement, never()).executeUpdate();
		verify(statement).close();
		verify(connection).close();
	}

	@Test
	public void testCreateDatabaseUnavailable() throws Exception {
		when(connectionPool.getConnection()).thenThrow(RuntimeException.class);
		Order order = new Order(1L, 2L, LocalDate.now(), LocalTime.now(), "Test Address 1", "+380991083238",
				"registered");
		assertThrows(DataProcessingException.class, () -> orderDao.create(order));
	}

	@Test
	public void testCreateThrowsException() throws Exception {
		Order order = new Order(1L, 2L, LocalDate.now(), LocalTime.now(), "Test Address 1", "+380991083238",
				"registered");
		String query = "INSERT INTO orders (user_id, order_date, order_time, order_address, order_phone, order_status) VALUES (?, ?, ?, ?, ?, ?)";

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		doThrow(new SQLException()).when(statement).executeUpdate();
		assertThrows(DataProcessingException.class, () -> orderDao.create(order));
	}

	@Test
	public void testGetOrderById() throws SQLException {

		Order expectedOrder = new Order(1L, 2L, LocalDate.now(), LocalTime.now(), "Test Address 1", "+380991083238",
				"registered");

		String query = "SELECT * FROM orders WHERE id = ?";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true).thenReturn(false);

		when(resultSet.getLong("id")).thenReturn(expectedOrder.getOrderId());
		when(resultSet.getLong("user_id")).thenReturn(expectedOrder.getUserId());

		when(resultSet.getDate("order_date")).thenReturn(date);
		when(date.toLocalDate()).thenReturn(expectedOrder.getOrderDate());
		when(resultSet.getTime("order_time")).thenReturn(time);
		when(time.toLocalTime()).thenReturn(expectedOrder.getOrderTime());

		when(resultSet.getString("order_address")).thenReturn(expectedOrder.getOrderAddress());
		when(resultSet.getString("order_phone")).thenReturn(expectedOrder.getOrderPhone());
		when(resultSet.getString("order_status")).thenReturn(expectedOrder.getOrderStatus());

		Optional<Order> actualOrderOpt = orderDao.get(1L);
		assertTrue(actualOrderOpt.isPresent());
		Order actualOrder = actualOrderOpt.get();

		assertEquals(expectedOrder.getOrderId(), actualOrder.getOrderId());
		assertEquals(expectedOrder.getUserId(), actualOrder.getUserId());
		assertEquals(expectedOrder.getOrderDate(), actualOrder.getOrderDate());
		assertEquals(expectedOrder.getOrderTime(), actualOrder.getOrderTime());
		assertEquals(expectedOrder.getOrderAddress(), actualOrder.getOrderAddress());
		assertEquals(expectedOrder.getOrderPhone(), actualOrder.getOrderPhone());
		assertEquals(expectedOrder.getOrderStatus(), actualOrder.getOrderStatus());
	}

	@Test
	public void testGetOrderByNonExistentId() throws SQLException {
		Long nonExistentId = 999L;
		String query = "SELECT * FROM orders WHERE id = ?";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(false);

		Optional<Order> actualOrderOpt = orderDao.get(nonExistentId);
		assertFalse(actualOrderOpt.isPresent());
	}

	@Test
	public void testGetOrderByIdThrowsException() throws SQLException {
		Long invalidId = -1L;
		String query = "SELECT * FROM orders WHERE id = ?";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		doThrow(new SQLException()).when(statement).setLong(1, invalidId);
		assertThrows(DataProcessingException.class, () -> orderDao.get(invalidId));
	}

	@Test
	public void testGetOrderByIdDatabaseUnavailaible() throws SQLException {
		Long Id = 1L;
		when(connectionPool.getConnection()).thenThrow(RuntimeException.class);
		assertThrows(DataProcessingException.class, () -> orderDao.get(Id));
	}

	@Test
	public void testGetAllPagination() throws SQLException {
		int offset = 0;
		int noOfRecords = 10;
		String query = "select SQL_CALC_FOUND_ROWS * from orders limit " + offset + ", " + noOfRecords;
		List<Order> expectedOrders = new ArrayList<>();

		Order expectedOrder = new Order(1L, 2L, LocalDate.now(), LocalTime.now(), "Test Address 1", "+380991083238",
				"registered");

		expectedOrders.add(expectedOrder);

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true).thenReturn(false).thenReturn(true);

		when(resultSet.getLong("id")).thenReturn(expectedOrder.getOrderId());
		when(resultSet.getLong("user_id")).thenReturn(expectedOrder.getUserId());
		when(resultSet.getDate("order_date")).thenReturn(date);
		when(date.toLocalDate()).thenReturn(expectedOrder.getOrderDate());
		when(resultSet.getTime("order_time")).thenReturn(time);
		when(time.toLocalTime()).thenReturn(expectedOrder.getOrderTime());
		when(resultSet.getString("order_address")).thenReturn(expectedOrder.getOrderAddress());
		when(resultSet.getString("order_phone")).thenReturn(expectedOrder.getOrderPhone());
		when(resultSet.getString("order_status")).thenReturn(expectedOrder.getOrderStatus());

		when(statement.executeQuery("SELECT FOUND_ROWS()")).thenReturn(resultSet);
		when(resultSet.getLong(1)).thenReturn(1L);

		List<Order> actualOrders = orderDao.getAll(offset, noOfRecords);

		verify(connection).prepareStatement(query);
		verify(statement).executeQuery();
		verify(resultSet, times(3)).next();

		verify(resultSet).getLong("id");
		verify(resultSet).getLong("user_id");
		verify(resultSet).getDate("order_date");
		verify(resultSet).getTime("order_time");
		verify(resultSet).getString("order_address");
		verify(resultSet).getString("order_phone");
		verify(resultSet).getString("order_status");

		verify(resultSet).close();
		verify(statement).executeQuery("SELECT FOUND_ROWS()");

		System.out.println(expectedOrders);
		System.out.println(actualOrders);

		assertEquals(expectedOrders.size(), actualOrders.size());

		for (int i = 0; i < expectedOrders.size(); i++) {
			assertEquals(expectedOrders.get(i).getOrderId(), actualOrders.get(i).getOrderId());
			assertEquals(expectedOrders.get(i).getUserId(), actualOrders.get(i).getUserId());
			assertEquals(expectedOrders.get(i).getOrderDate(), actualOrders.get(i).getOrderDate());
			assertEquals(expectedOrders.get(i).getOrderTime(), actualOrders.get(i).getOrderTime());
			assertEquals(expectedOrders.get(i).getOrderAddress(), actualOrders.get(i).getOrderAddress());
			assertEquals(expectedOrders.get(i).getOrderPhone(), actualOrders.get(i).getOrderPhone());
			assertEquals(expectedOrders.get(i).getOrderStatus(), actualOrders.get(i).getOrderStatus());
		}
	}

	@Test
	public void testUpdateOrder() throws SQLException {
		String query = "UPDATE orders SET order_status = ? " + "WHERE id = ?";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);

		Order order = new Order(1L, 2L, LocalDate.now(), LocalTime.now(), "Test Address 1", "+380991083238",
				"registered");

		Order updatedOrder = orderDao.update(order);

		assertNotNull(updatedOrder);
		verify(connectionPool, times(1)).getConnection();
		verify(connection, times(1)).prepareStatement(query);
		verify(statement, times(1)).setString(1, order.getOrderStatus());
		verify(statement, times(1)).setLong(2, order.getOrderId());
		verify(statement, times(1)).executeUpdate();
		verify(statement, times(1)).close();
		verify(connection).close();

		assertEquals(order, updatedOrder);
	}

	@Test
	public void testUpdateOrderNonExistent() throws SQLException {
		String query = "UPDATE orders SET order_status = ? " + "WHERE id = ?";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		doThrow(new SQLException()).when(statement).executeUpdate();
		Order nonExistingOrder = new Order(1L, 2L, LocalDate.now(), LocalTime.now(), "Test Address 1", "+380991083238",
				"registered");
		assertThrows(DataProcessingException.class, () -> orderDao.update(nonExistingOrder));
	}

	@Test
	public void testUpdateOrderInvalidId() throws SQLException {
		String query = "UPDATE orders SET order_status = ? " + "WHERE id = ?";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		doThrow(new SQLException()).when(statement).executeUpdate();
		Order orderWithInvalidID = new Order(1L, 2L, LocalDate.now(), LocalTime.now(), "Test Address 1",
				"+380991083238", "registered");
		assertThrows(DataProcessingException.class, () -> orderDao.update(orderWithInvalidID));
	}

	@Test
	public void testUpdateOrderOrderNull() throws SQLException {
		String query = "UPDATE orders SET order_status = ? " + "WHERE id = ?";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		doThrow(new NullPointerException()).when(statement).setString(1, null);
		assertThrows(DataProcessingException.class, () -> orderDao.update(null));

		verify(connectionPool).getConnection();
		verify(connection).prepareStatement(query);

		verify(statement, never()).setString(anyInt(), anyString());
		verify(statement, never()).setLong(anyInt(), anyLong());
		verify(statement, never()).executeUpdate();
		verify(statement).close();
		verify(connection).close();
	}

	@Test
	public void testUpdateOrderDatabaseUnavailaible() throws SQLException {
		when(connectionPool.getConnection()).thenThrow(RuntimeException.class);
		Order order = new Order(1L, 2L, LocalDate.now(), LocalTime.now(), "Test Address 1", "+380991083238",
				"registered");
		assertThrows(DataProcessingException.class, () -> orderDao.update(order));
	}

	@Test
	public void testUpdateOrderExceptionOccured() throws SQLException {
		String query = "UPDATE orders SET order_status = ? " + "WHERE id = ?";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		when(statement.executeUpdate()).thenThrow(SQLException.class);
		Order order = new Order(1L, 2L, LocalDate.now(), LocalTime.now(), "Test Address 1", "+380991083238",
				"registered");
		assertThrows(DataProcessingException.class, () -> orderDao.update(order));
	}

	@Test
	public void testGetAllOrdersByUserId() throws SQLException {
		int offset = 0;
		int noOfRecords = 10;
		long userId = 1;
		String query = "select SQL_CALC_FOUND_ROWS * from orders WHERE user_id = ? limit " + offset + ", "
				+ noOfRecords;

		List<Order> expectedOrders = new ArrayList<>();

		Order expectedOrder = new Order(1L, 2L, LocalDate.now(), LocalTime.now(), "Test Address 1", "+380991083238",
				"registered");

		expectedOrders.add(expectedOrder);

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true).thenReturn(false).thenReturn(true);

		when(resultSet.getLong("id")).thenReturn(expectedOrder.getOrderId());
		when(resultSet.getLong("user_id")).thenReturn(expectedOrder.getUserId());
		when(resultSet.getDate("order_date")).thenReturn(date);
		when(date.toLocalDate()).thenReturn(expectedOrder.getOrderDate());
		when(resultSet.getTime("order_time")).thenReturn(time);
		when(time.toLocalTime()).thenReturn(expectedOrder.getOrderTime());
		when(resultSet.getString("order_address")).thenReturn(expectedOrder.getOrderAddress());
		when(resultSet.getString("order_phone")).thenReturn(expectedOrder.getOrderPhone());
		when(resultSet.getString("order_status")).thenReturn(expectedOrder.getOrderStatus());

		when(statement.executeQuery("SELECT FOUND_ROWS()")).thenReturn(resultSet);
		when(resultSet.getLong(1)).thenReturn(1L);

		List<Order> actualOrders = orderDao.getAllOrdersByUserId(userId, offset, noOfRecords);

		verify(connection).prepareStatement(query);
		verify(statement).executeQuery();
		verify(resultSet, times(3)).next();

		verify(resultSet).getLong("id");
		verify(resultSet).getLong("user_id");
		verify(resultSet).getDate("order_date");
		verify(resultSet).getTime("order_time");
		verify(resultSet).getString("order_address");
		verify(resultSet).getString("order_phone");
		verify(resultSet).getString("order_status");

		verify(resultSet).close();
		verify(statement).executeQuery("SELECT FOUND_ROWS()");

		System.out.println(expectedOrders);
		System.out.println(actualOrders);

		assertEquals(expectedOrders.size(), actualOrders.size());

		for (int i = 0; i < expectedOrders.size(); i++) {
			assertEquals(expectedOrders.get(i).getOrderId(), actualOrders.get(i).getOrderId());
			assertEquals(expectedOrders.get(i).getUserId(), actualOrders.get(i).getUserId());
			assertEquals(expectedOrders.get(i).getOrderDate(), actualOrders.get(i).getOrderDate());
			assertEquals(expectedOrders.get(i).getOrderTime(), actualOrders.get(i).getOrderTime());
			assertEquals(expectedOrders.get(i).getOrderAddress(), actualOrders.get(i).getOrderAddress());
			assertEquals(expectedOrders.get(i).getOrderPhone(), actualOrders.get(i).getOrderPhone());
			assertEquals(expectedOrders.get(i).getOrderStatus(), actualOrders.get(i).getOrderStatus());
		}
	}

}