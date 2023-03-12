package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.ItemDao;
import dbconnection_pool.ConnectionPool;
import exception.DataProcessingException;
import models.Item;

public class ItemDaoImpl implements ItemDao {
	private int noOfRecords;
	private ConnectionPool connectionPool;
	private static final Logger logger = LogManager.getLogger(ItemDaoImpl.class);

	public ItemDaoImpl(ConnectionPool connectionPool) {
		super();
		this.connectionPool = connectionPool;
	}

	public int getNoOfRecords() {
		return noOfRecords;
	}

	@Override
	public Item create(Item item) {
		logger.info("method create(Item item) of ItemDaoImpl class is invoked");
		String insertQuery = "INSERT INTO items (product_id, order_id, item_quantity, item_price) VALUES (?, ?, ?, ?)";
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement createItemStatement = connection.prepareStatement(insertQuery,
						Statement.RETURN_GENERATED_KEYS)) {
			createItemStatement.setLong(1, item.getProductId());
			createItemStatement.setLong(2, item.getOrderId());
			createItemStatement.setInt(3, item.getItemQuantity());
			createItemStatement.setDouble(4, item.getItemPrice());
			createItemStatement.executeUpdate();
			ResultSet resultSet = createItemStatement.getGeneratedKeys();
			if (resultSet.next()) {
				item.setId(resultSet.getObject(1, Long.class));
			}
		} catch (SQLException | NullPointerException e) {
			logger.error("Error while creating item:{}", item, e);
			throw new DataProcessingException("Can't create item " + item, e);
		}
		logger.debug("Successfully created item: {}", item);
		return item;
	}

	@Override
	public Optional<Item> get(Long id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public List<Item> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Item update(Item element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Item> getAllByOrderId(Long id, int offset, int noOfRecords) {
		logger.info("method getAllByOrderId(Long id, int offset, int noOfRecords) of ItemDaoImpl class is invoked");
		String query = "select SQL_CALC_FOUND_ROWS * from items  WHERE order_id = ? limit " + offset + ", "
				+ noOfRecords;
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement getAllItemsStatement = connection.prepareStatement(query)) {
			List<Item> listItems = new ArrayList<>();
			getAllItemsStatement.setLong(1, id);
			ResultSet resultSet = getAllItemsStatement.executeQuery();
			while (resultSet.next()) {
				listItems.add(parseItemFromResultSet(resultSet));
			}
			resultSet.close();
			resultSet = getAllItemsStatement.executeQuery("SELECT FOUND_ROWS()");
			if (resultSet.next()) {
				this.noOfRecords = resultSet.getInt(1);
				logger.debug("Successfully got item list: {}", listItems);
			}
			return listItems;
		} catch (SQLException e) {
			logger.error("Error while getting item list by order id:{}", id, e);
			throw new DataProcessingException("Couldn't get a list of items" + "from items table. ", e);
		}
	}

	private Item parseItemFromResultSet(ResultSet resultSet) throws SQLException {
		logger.info("method parseItemFromResultSet(ResultSet resultSet) of ItemDaoImpl class is invoked");
		Item item = null;
		try {
			long id = resultSet.getLong("id");
			long productId = resultSet.getLong("product_id");
			long orderId = resultSet.getLong("order_id");
			int itemQuantity = resultSet.getInt("item_quantity");
			double itemPrice = resultSet.getDouble("item_price");
			item = new Item(id, productId, orderId, itemQuantity, itemPrice);
			logger.debug("Successfully got item: {}", item);
		} catch (SQLException e) {
			logger.error("Error while getting item", e);
			e.printStackTrace();
		}
		return item;
	}

	@Override
	public List<Item> getAllByOrderId(Long id) {
		logger.info("method getAllByOrderId(Long id) of ItemDaoImpl class is invoked");
		String query = "SELECT * FROM items WHERE order_id = ?";
		List<Item> listItems = new ArrayList<>();
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement getAllItemsStatement = connection.prepareStatement(query)) {

			getAllItemsStatement.setLong(1, id);
			ResultSet resultSet = getAllItemsStatement.executeQuery();
			while (resultSet.next()) {
				listItems.add(parseItemFromResultSet(resultSet));
			}
			logger.debug("Successfully got items list: {}", listItems);
			return listItems;
		} catch (SQLException e) {
			logger.error("Error while getting list of items by order id:{}", id, e);
			throw new DataProcessingException("Couldn't get a list of items" + "from items table. ", e);
		}
	}

}
