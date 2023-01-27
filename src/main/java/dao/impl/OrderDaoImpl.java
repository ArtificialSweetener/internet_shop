package dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dao.OrderDao;
import dbconnection_pool.ConnectionPool;
import exception.DataProcessingException;
import models.Order;

public class OrderDaoImpl implements OrderDao {
	private int noOfRecords;

	public int getNoOfRecords() {
		return noOfRecords;
	}

	@Override
	public Order create(Order order) {
		System.out.println("Create order method invoked");
		String insertQuery = "INSERT INTO orders (user_id, order_date, order_time, order_address, order_phone, order_status) VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection connection = ConnectionPool.getInstance().getConnection();
				PreparedStatement createOrderStatement = connection.prepareStatement(insertQuery,
						Statement.RETURN_GENERATED_KEYS)) {
			createOrderStatement.setLong(1, order.getUserId());
			createOrderStatement.setDate(2, order.getOrderDate());
			createOrderStatement.setTime(3, order.getOrderTime());
			createOrderStatement.setString(4, order.getOrderAddress());
			createOrderStatement.setString(5, order.getOrderPhone());
			createOrderStatement.setString(6, order.getOrderStatus());
			createOrderStatement.executeUpdate();
			ResultSet resultSet = createOrderStatement.getGeneratedKeys();
			if (resultSet.next()) {
				order.setOrderId(resultSet.getObject(1, Long.class));
			}
		} catch (SQLException e) {
			throw new DataProcessingException("Can't create order " + order, e);
		}
		System.out.println(order);
		return order;
	}

	@Override
	public Optional<Order> get(Long id) {
		String query = "SELECT * FROM orders WHERE id = ?";
		try (Connection connection = ConnectionPool.getInstance().getConnection();
				PreparedStatement getOrderStatement = connection.prepareStatement(query)) {
			getOrderStatement.setLong(1, id);
			ResultSet resultSet = getOrderStatement.executeQuery();
			Order order = null;
			if (resultSet.next() == false) {
				System.out.println("ResultSet in empty in Java");
			} else {
				do {
					order = parseOrderFromResultSet(resultSet);
					System.out.println(order);
				} while (resultSet.next());
			}
			return Optional.ofNullable(order);
		} catch (SQLException e) {
			throw new DataProcessingException("Couldn't get order by id " + id, e);
		}
	}

	@Override
	public List<Order> getAll(int offset, int noOfRecords) {
		String query = "select SQL_CALC_FOUND_ROWS * from orders limit " + offset + ", " + noOfRecords;
		try (Connection connection = ConnectionPool.getInstance().getConnection();
				PreparedStatement getAllOrdersStatement = connection.prepareStatement(query)) {
			List<Order> listOrders = new ArrayList<>();
			ResultSet resultSet = getAllOrdersStatement.executeQuery();
			while (resultSet.next()) {
				listOrders.add(parseOrderFromResultSet(resultSet));
			}
			resultSet.close();
			resultSet = getAllOrdersStatement.executeQuery("SELECT FOUND_ROWS()");
			if (resultSet.next()) {
				this.noOfRecords = resultSet.getInt(1);
			}
			return listOrders;
		} catch (SQLException e) {
			throw new DataProcessingException("Couldn't get a list of orders" + "from orders table. ", e);
		}
	}

	@Override
	public Order update(Order order) {
		String selectQuery = "UPDATE orders SET order_status = ? " + "WHERE id = ?";
		try (Connection connection = ConnectionPool.getInstance().getConnection();
				PreparedStatement updateOrderStatement = connection.prepareStatement(selectQuery)) {
			updateOrderStatement.setString(1, order.getOrderStatus());
			updateOrderStatement.setLong(2, order.getOrderId());
			updateOrderStatement.executeUpdate();
			System.out.println("updated");
		} catch (SQLException e) {
			throw new DataProcessingException("Can't update order " + order, e);
		}
		return order;
	}

	@Override
	public boolean delete(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	private Order parseOrderFromResultSet(ResultSet resultSet) throws SQLException {
		// change to product
		System.out.println("Method parseOrderFromResultSet invoked");
		Order order = null;
		try {
			long id = resultSet.getLong("id");
			long userId = resultSet.getLong("user_id");
			Date orderDate = resultSet.getDate("order_date");
			Time orderTime = resultSet.getTime("order_time");
			String orderAddress = resultSet.getString("order_address");
			String orderPhone = resultSet.getString("order_phone");
			String orderStatus = resultSet.getString("order_status");
			order = new Order(id, userId, orderDate, orderTime, orderAddress, orderPhone, orderStatus);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return order;
	}

	@Override
	public Optional<List<Order>> getAllOrdersByUserId(Long id, int offset, int noOfRecords) {
		
		String query = "select SQL_CALC_FOUND_ROWS * from orders WHERE user_id = ? limit " + offset + ", " + noOfRecords;
		
		try (Connection connection = ConnectionPool.getInstance().getConnection();
				PreparedStatement getAllOrdersStatement = connection.prepareStatement(query)) {
			
			List<Order> listOrders = new ArrayList<>();
			getAllOrdersStatement.setLong(1, id);
			ResultSet resultSet = getAllOrdersStatement.executeQuery();
			
			while (resultSet.next()) {
				listOrders.add(parseOrderFromResultSet(resultSet));
			}
			
			resultSet.close();
			resultSet = getAllOrdersStatement.executeQuery("SELECT FOUND_ROWS()");
			
			if (resultSet.next()) {
				this.noOfRecords = resultSet.getInt(1);
			}
			return Optional.ofNullable(listOrders);
		} catch (SQLException e) {
			throw new DataProcessingException("Couldn't get a list of orders" + "from orders table. ", e);
		}
	}

	@Override
	public List<Order> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
