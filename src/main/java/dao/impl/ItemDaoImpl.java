package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dao.ItemDao;
import dbconnection_pool.ConnectionPool;
import exception.DataProcessingException;
import models.Item;


public class ItemDaoImpl implements ItemDao {
	private int noOfRecords;

	public int getNoOfRecords() {
		return noOfRecords;
	}
	@Override
	public Item create(Item item) {
		System.out.println("Create item method invoked");
		String insertQuery = "INSERT INTO items (product_id, order_id, item_quantity, item_price) VALUES (?, ?, ?, ?)";
		try (Connection connection = ConnectionPool.getInstance().getConnection();
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
		} catch (SQLException e) {
			throw new DataProcessingException("Can't create item " + item, e);
		}
		System.out.println(item);
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
		String query = "select SQL_CALC_FOUND_ROWS * from items  WHERE order_id = ? limit " + offset + ", " + noOfRecords;
	//	String query = "SELECT * FROM items WHERE order_id = ?";
		try (Connection connection = ConnectionPool.getInstance().getConnection();
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
			}
			return listItems;
		} catch (SQLException e) {
			throw new DataProcessingException("Couldn't get a list of items" + "from items table. ", e);
		}
	}

	private Item parseItemFromResultSet(ResultSet resultSet) throws SQLException {
		// change to product
		System.out.println("Method parseOrderFromResultSet invoked");
		Item item = null;
		try {
			long id = resultSet.getLong("id");
			long productId = resultSet.getLong("product_id");
			long orderId = resultSet.getLong("order_id");
			int itemQuantity = resultSet.getInt("item_quantity");
			long itemPrice = resultSet.getLong("item_price");
			item = new Item(id, productId, orderId, itemQuantity, itemPrice);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return item;
	}

	@Override
	public Optional<List<Item>> getAllByOrderId(Long id) {
		String query = "SELECT * FROM items WHERE order_id = ?";
			try (Connection connection = ConnectionPool.getInstance().getConnection();
					PreparedStatement getAllItemsStatement = connection.prepareStatement(query)) {
				List<Item> listItems = new ArrayList<>();
				getAllItemsStatement.setLong(1, id);
				ResultSet resultSet = getAllItemsStatement.executeQuery();
				while (resultSet.next()) {
					listItems.add(parseItemFromResultSet(resultSet));
				}
				return Optional.ofNullable(listItems) ;
			} catch (SQLException e) {
				throw new DataProcessingException("Couldn't get a list of items" + "from items table. ", e);
			}
	}

}
