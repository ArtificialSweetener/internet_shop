package dao_test.impl_test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dao.ItemDao;

import dao.impl.ItemDaoImpl;
import dbconnection_pool.ConnectionPool;
import exception.DataProcessingException;

import models.Item;

public class TestItemDaoImpl { // done, add more test cases
	@Mock
	private ConnectionPool connectionPool;
	@Mock
	private Connection connection;
	@Mock
	private PreparedStatement statement;
	@Mock
	private ResultSet resultSet;

	private ItemDao itemDao;

	@Before
	public void setUp() throws Exception {
		connectionPool = mock(ConnectionPool.class);
		connection = mock(Connection.class);
		statement = mock(PreparedStatement.class);
		resultSet = mock(ResultSet.class);
		MockitoAnnotations.openMocks(this);
		itemDao = new ItemDaoImpl(connectionPool);

	}

	@Test
	public void testCreate() throws Exception {
		Item item = new Item(1L, 2L, 3L, 3, 10000.00);

		String query = "INSERT INTO items (product_id, order_id, item_quantity, item_price) VALUES (?, ?, ?, ?)";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(statement);
		when(statement.getGeneratedKeys()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true);
		when(resultSet.getObject(1, Long.class)).thenReturn(1L);

		Item createdItem = itemDao.create(item);
		assertEquals(item.getId(), createdItem.getId());
		assertEquals(item.getProductId(), createdItem.getProductId());
		assertEquals(item.getOrderId(), createdItem.getOrderId());
		assertEquals(item.getItemQuantity(), createdItem.getItemQuantity());
		assertEquals(item.getItemPrice(), createdItem.getItemPrice(), 0.0);
	}

	@Test
	public void testCreateNullItem() throws Exception {
		String query = "INSERT INTO items (product_id, order_id, item_quantity, item_price) VALUES (?, ?, ?, ?)";

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(statement);
		doThrow(new NullPointerException()).when(statement).setString(1, null);

		assertThrows(DataProcessingException.class, () -> itemDao.create(null));

		verify(connectionPool).getConnection();
		verify(connection).prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

		verify(statement, never()).setLong(anyInt(), anyLong());
		verify(statement, never()).setInt(anyInt(), anyInt());
		verify(statement, never()).setDouble(anyInt(), anyDouble());

		verify(statement, never()).executeUpdate();
		verify(statement).close();
		verify(connection).close();
	}

	@Test
	public void testGetAllByOrderIdPagination() throws SQLException {
		int offset = 0;
		int noOfRecords = 2;
		long orderId = 3L;
		String query = "select SQL_CALC_FOUND_ROWS * from items  WHERE order_id = ? limit " + offset + ", "
				+ noOfRecords;

		List<Item> expectedItems = new ArrayList<>();

		Item expectedItem1 = new Item(1L, 2L, orderId, 3, 10000.00);
		Item expectedItem2 = new Item(2L, 1L, orderId, 3, 11000.00);
		Item expectedItem3 = new Item(3L, 3L, orderId, 3, 12000.00);

		expectedItems.add(expectedItem1);
		expectedItems.add(expectedItem2);

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false).thenReturn(true);

		when(resultSet.getLong("id")).thenReturn(expectedItem1.getId(), expectedItem2.getId(), expectedItem3.getId());
		when(resultSet.getLong("product_id")).thenReturn(expectedItem1.getProductId(), expectedItem2.getProductId(),
				expectedItem3.getProductId());
		when(resultSet.getLong("order_id")).thenReturn(expectedItem1.getOrderId(), expectedItem2.getOrderId(),
				expectedItem3.getOrderId());
		when(resultSet.getInt("item_quantity")).thenReturn(expectedItem1.getItemQuantity(),
				expectedItem2.getItemQuantity(), expectedItem3.getItemQuantity());

		when(resultSet.getDouble("item_price")).thenReturn(expectedItem1.getItemPrice(), expectedItem2.getItemPrice(),
				expectedItem3.getItemPrice());

		when(statement.executeQuery("SELECT FOUND_ROWS()")).thenReturn(resultSet);
		when(resultSet.getLong(1)).thenReturn(1L);

		List<Item> actualItems = itemDao.getAllByOrderId(orderId, offset, noOfRecords);

		verify(connection).prepareStatement(query);
		verify(statement).executeQuery();
		verify(resultSet, times(4)).next();

		verify(resultSet, times(2)).getLong("id");
		verify(resultSet, times(2)).getLong("product_id");
		verify(resultSet, times(2)).getLong("order_id");
		verify(resultSet, times(2)).getInt("item_quantity");
		verify(resultSet, times(2)).getDouble("item_price");
		verify(resultSet).close();
		verify(statement).executeQuery("SELECT FOUND_ROWS()");

		assertEquals(expectedItems.size(), actualItems.size());

		for (int i = 0; i < expectedItems.size(); i++) {
			assertEquals(expectedItems.get(i).getId(), actualItems.get(i).getId());
			assertEquals(expectedItems.get(i).getProductId(), actualItems.get(i).getProductId());
			assertEquals(expectedItems.get(i).getOrderId(), actualItems.get(i).getOrderId());
			assertEquals(expectedItems.get(i).getItemQuantity(), actualItems.get(i).getItemQuantity());
			assertEquals(expectedItems.get(i).getItemPrice(), actualItems.get(i).getItemPrice(), 0.0);
		}
	}

	@Test
	public void testGetAllByOrderId() throws SQLException {
		long orderId = 3L;
		String query = "SELECT * FROM items WHERE order_id = ?";
		List<Item> expectedItems = new ArrayList<>();

		Item expectedItem1 = new Item(1L, 2L, orderId, 3, 10000.00);
		Item expectedItem2 = new Item(2L, 1L, orderId, 3, 11000.00);

		expectedItems.add(expectedItem1);
		expectedItems.add(expectedItem2);

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);

		when(resultSet.getLong("id")).thenReturn(expectedItem1.getId(), expectedItem2.getId());
		when(resultSet.getLong("product_id")).thenReturn(expectedItem1.getProductId(), expectedItem2.getProductId());
		when(resultSet.getLong("order_id")).thenReturn(expectedItem1.getOrderId(), expectedItem2.getOrderId());

		when(resultSet.getInt("item_quantity")).thenReturn(expectedItem1.getItemQuantity(),
				expectedItem2.getItemQuantity());

		when(resultSet.getDouble("item_price")).thenReturn(expectedItem1.getItemPrice(), expectedItem2.getItemPrice());

		when(statement.executeQuery("SELECT FOUND_ROWS()")).thenReturn(resultSet);
		when(resultSet.getLong(1)).thenReturn(1L);

		List<Item> actualItems = itemDao.getAllByOrderId(orderId);

		verify(connection).prepareStatement(query);
		verify(statement).executeQuery();
		verify(resultSet, times(3)).next();

		verify(resultSet, times(2)).getLong("id");
		verify(resultSet, times(2)).getLong("product_id");
		verify(resultSet, times(2)).getLong("order_id");
		verify(resultSet, times(2)).getInt("item_quantity");
		verify(resultSet, times(2)).getDouble("item_price");

		assertEquals(expectedItems.size(), actualItems.size());

		for (int i = 0; i < expectedItems.size(); i++) {
			assertEquals(expectedItems.get(i).getId(), actualItems.get(i).getId());
			assertEquals(expectedItems.get(i).getProductId(), actualItems.get(i).getProductId());
			assertEquals(expectedItems.get(i).getOrderId(), actualItems.get(i).getOrderId());
			assertEquals(expectedItems.get(i).getItemQuantity(), actualItems.get(i).getItemQuantity());
			assertEquals(expectedItems.get(i).getItemPrice(), actualItems.get(i).getItemPrice(), 0.0);
		}
	}

}