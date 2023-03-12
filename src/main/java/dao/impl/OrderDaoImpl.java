package dao.impl;

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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.OrderDao;
import dbconnection_pool.ConnectionPool;
import exception.DataProcessingException;
import models.Order;

public class OrderDaoImpl implements OrderDao {
	private int noOfRecords;
	private ConnectionPool connectionPool;
	private static final Logger logger = LogManager.getLogger(OrderDaoImpl.class);

	public OrderDaoImpl(ConnectionPool connectionPool) {
		super();
		this.connectionPool = connectionPool;
	}

	public int getNoOfRecords() {
		return noOfRecords;
	}

	@Override
	public Order create(Order order) {
		logger.info("method create(Order order) of OrderDaoImpl class is invoked");
		String insertQuery = "INSERT INTO orders (user_id, order_date, order_time, order_address, order_phone, order_status) VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement createOrderStatement = connection.prepareStatement(insertQuery,
						Statement.RETURN_GENERATED_KEYS)) {
			createOrderStatement.setLong(1, order.getUserId());
			createOrderStatement.setDate(2, Date.valueOf(order.getOrderDate()));
			createOrderStatement.setTime(3, Time.valueOf(order.getOrderTime()));
			createOrderStatement.setString(4, order.getOrderAddress());
			createOrderStatement.setString(5, order.getOrderPhone());
			createOrderStatement.setString(6, order.getOrderStatus());
			createOrderStatement.executeUpdate();
			ResultSet resultSet = createOrderStatement.getGeneratedKeys();
			if (resultSet.next()) {
				order.setOrderId(resultSet.getObject(1, Long.class));
			}
		} catch (SQLException | RuntimeException e) {
			logger.error("Error while creating order:{}", order, e);
			throw new DataProcessingException("Can't create order " + order, e);
		}
		logger.debug("Successfully created order: {}", order);
		return order;
	}

	@Override
	public Optional<Order> get(Long id) {
		logger.info("method get(Long id) of OrderDaoImpl class is invoked");
		String query = "SELECT * FROM orders WHERE id = ?";
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement getOrderStatement = connection.prepareStatement(query)) {
			getOrderStatement.setLong(1, id);
			ResultSet resultSet = getOrderStatement.executeQuery();
			Order order = null;
			if (resultSet.next() == false) {
				logger.warn("ResultSet in empty in Java");
			} else {
				do {
					order = parseOrderFromResultSet(resultSet);
					logger.debug("Successfully got order: {}", order);
				} while (resultSet.next());
			}
			return Optional.ofNullable(order);
		} catch (SQLException | RuntimeException e) {
			logger.error("Error while getting order by id:{}", id, e);
			throw new DataProcessingException("Couldn't get order by id " + id, e);
		}
	}

	@Override
	public List<Order> getAll(int offset, int noOfRecords) {
		logger.info("method getAll(int offset, int noOfRecords) of OrderDaoImpl class is invoked");
		String query = "select SQL_CALC_FOUND_ROWS * from orders limit " + offset + ", " + noOfRecords;
		List<Order> listOrders = new ArrayList<>();
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement getAllOrdersStatement = connection.prepareStatement(query)) {
			ResultSet resultSet = getAllOrdersStatement.executeQuery();
			while (resultSet.next()) {
				listOrders.add(parseOrderFromResultSet(resultSet));
			}
			resultSet.close();
			resultSet = getAllOrdersStatement.executeQuery("SELECT FOUND_ROWS()");
			if (resultSet.next()) {
				this.noOfRecords = resultSet.getInt(1);
				logger.debug("Successfully got list of orders: {}", listOrders);
			}
			return listOrders;
		} catch (SQLException e) {
			logger.error("Error while getting list of orders", e);
			throw new DataProcessingException("Couldn't get a list of orders" + "from orders table. ", e);
		}
	}

	@Override
	public Order update(Order order) {
		logger.info("method update(Order order) of OrderDaoImpl class is invoked");
		String selectQuery = "UPDATE orders SET order_status = ? " + "WHERE id = ?";
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement updateOrderStatement = connection.prepareStatement(selectQuery)) {
			updateOrderStatement.setString(1, order.getOrderStatus());
			updateOrderStatement.setLong(2, order.getOrderId());
			updateOrderStatement.executeUpdate();
			logger.debug("Successfully updated order:{}", order);
		} catch (SQLException | RuntimeException e) {
			logger.error("Error while updating order", e);
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
		logger.info("method parseOrderFromResultSet(ResultSet resultSet) of OrderDaoImpl class is invoked");
		Order order = null;
		try {
			long id = resultSet.getLong("id");
			long userId = resultSet.getLong("user_id");
			LocalDate orderDate = resultSet.getDate("order_date").toLocalDate();
			LocalTime orderTime = resultSet.getTime("order_time").toLocalTime();
			String orderAddress = resultSet.getString("order_address");
			String orderPhone = resultSet.getString("order_phone");
			String orderStatus = resultSet.getString("order_status");
			order = new Order(id, userId, orderDate, orderTime, orderAddress, orderPhone, orderStatus);
			logger.debug("Successfully parsed order:{}", order);
		} catch (SQLException e) {
			logger.error("Error while parsing order", e);
			e.printStackTrace();
		}
		return order;
	}

	@Override
	public List<Order> getAllOrdersByUserId(Long id, int offset, int noOfRecords) {
		logger.info(
				"method  getAllOrdersByUserId(Long id, int offset, int noOfRecords) of OrderDaoImpl class is invoked");
		String query = "select SQL_CALC_FOUND_ROWS * from orders WHERE user_id = ? limit " + offset + ", "
				+ noOfRecords;
		List<Order> listOrders = new ArrayList<>();
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement getAllOrdersStatement = connection.prepareStatement(query)) {

			getAllOrdersStatement.setLong(1, id);
			ResultSet resultSet = getAllOrdersStatement.executeQuery();
			while (resultSet.next()) {
				listOrders.add(parseOrderFromResultSet(resultSet));
			}
			resultSet.close();
			resultSet = getAllOrdersStatement.executeQuery("SELECT FOUND_ROWS()");
			if (resultSet.next()) {
				this.noOfRecords = resultSet.getInt(1);
				logger.debug("Successfully got list of orders:{}", listOrders);
			}
			return listOrders;
		} catch (SQLException e) {
			logger.error("Error while getting list of orders by user id:{} ", id);
			throw new DataProcessingException("Couldn't get a list of orders" + "from orders table. ", e);
		}
	}

	@Override
	public List<Order> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
