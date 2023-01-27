package dao.impl;

import java.io.IOException;
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

import dao.ProductDao;
import dbconnection_pool.ConnectionPool;
import exception.DataProcessingException;
import models.Cart;
import models.Product;

public class ProductDaoImpl implements ProductDao {

	private int noOfRecords;

	public int getNoOfRecords() {
		return noOfRecords;
	}

	@Override
	public Product create(Product product) {
		String insertQuery = "INSERT INTO products (product_name, product_desc, color_id, category_id, product_price, product_quantity_in_stock, product_date_of_addition, product_time_of_addition, product_photo, product_photo_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection connection = ConnectionPool.getInstance().getConnection();
				PreparedStatement createProductStatement = connection.prepareStatement(insertQuery,
						Statement.RETURN_GENERATED_KEYS)) {
			createProductStatement.setString(1, product.getProductName());
			createProductStatement.setString(2, product.getProductDescription());
			createProductStatement.setLong(3, product.getColorId());
			System.out.println("Checking id of the product category: " + product.getCategoryId());
			createProductStatement.setLong(4, product.getCategoryId());
			createProductStatement.setDouble(5, product.getProductPrice());
			createProductStatement.setInt(6, product.getProductQuantity());
			createProductStatement.setDate(7, Date.valueOf(product.getProductDate()));
			createProductStatement.setTime(8, Time.valueOf(product.getProductTime()));
			createProductStatement.setBlob(9, new SerialBlob(product.getProductPhoto()));
			createProductStatement.setString(10, product.getProductPhotoName());
			createProductStatement.executeUpdate();
			ResultSet resultSet = createProductStatement.getGeneratedKeys();
			if (resultSet.next()) {
				product.setProductId(resultSet.getObject(1, Long.class));
			}
		} catch (SQLException e) {
			throw new DataProcessingException("Can't create product " + product, e);
		}
		System.out.println(product);
		return product;
	}

	@Override
	public Optional<Product> get(Long id) {
		String query = "SELECT * FROM products WHERE id = ? AND is_deleted = FALSE";
		try (Connection connection = ConnectionPool.getInstance().getConnection();
				PreparedStatement getProductStatement = connection.prepareStatement(query)) {
			getProductStatement.setLong(1, id);
			ResultSet resultSet = getProductStatement.executeQuery();
			Product product = null;
			if (resultSet.next() == false) {
				System.out.println("ResultSet in empty in Java");
			} else {
				do {
					product = parseProductFromResultSet(resultSet);
					System.out.println(product);
				} while (resultSet.next());
			}
			return Optional.ofNullable(product);
		} catch (SQLException e) {
			throw new DataProcessingException("Couldn't get product by id " + id, e);
		}
	}

	@Override
	public List<Product> getAll(int offset, int noOfRecords) {
		String query = "select SQL_CALC_FOUND_ROWS * from products where is_deleted = false limit " + offset + ", "
				+ noOfRecords;
		// String query = "SELECT * FROM products WHERE is_deleted = FALSE";
		try (Connection connection = ConnectionPool.getInstance().getConnection();
				PreparedStatement getAllProductsStatement = connection.prepareStatement(query)) {

			List<Product> listProducts = new ArrayList<>();
			ResultSet resultSet = getAllProductsStatement.executeQuery();
			while (resultSet.next()) {
				listProducts.add(parseProductFromResultSet(resultSet));
			}
			resultSet.close();
			resultSet = getAllProductsStatement.executeQuery("SELECT FOUND_ROWS()");
			if (resultSet.next()) {
				this.noOfRecords = resultSet.getInt(1);
			}
			return listProducts;
		} catch (SQLException e) {
			throw new DataProcessingException("Couldn't get a list of products" + "from products table. ", e);
		}

	}

	@Override
	public Product update(Product product) {
		String selectQuery = "UPDATE products SET product_name = ?, product_desc = ?, color_id = ? , category_id = ?, product_price= ?, product_quantity_in_stock = ?, product_photo =?, product_photo_name =? "
				+ "WHERE id = ? AND is_deleted = 0";
		try (Connection connection = ConnectionPool.getInstance().getConnection();
				PreparedStatement updateProductStatement = connection.prepareStatement(selectQuery)) {
			updateProductStatement.setString(1, product.getProductName());
			updateProductStatement.setString(2, product.getProductDescription());
			updateProductStatement.setLong(3, product.getColorId());
			updateProductStatement.setLong(4, product.getCategoryId());
			updateProductStatement.setDouble(5, product.getProductPrice());
			updateProductStatement.setInt(6, product.getProductQuantity());
			updateProductStatement.setBlob(7, new SerialBlob(product.getProductPhoto()));
			updateProductStatement.setString(8, product.getProductPhotoName());

			updateProductStatement.setLong(9, product.getProductId());
			updateProductStatement.executeUpdate();

			System.out.println("updated");
		} catch (SQLException e) {
			throw new DataProcessingException("Can't update product " + product, e);
		}
		return product;
	}

	@Override
	public boolean delete(Long id) {
		System.out.println("Delete method in dao is invoked");
		String selectQuery = "UPDATE products SET is_deleted = TRUE WHERE id = ? AND is_deleted = FALSE";
		try (Connection connection = ConnectionPool.getInstance().getConnection();
				PreparedStatement deleteProductStatement = connection.prepareStatement(selectQuery)) {
			deleteProductStatement.setLong(1, id);
			int isDone = deleteProductStatement.executeUpdate();
			return isDone > 0;
		} catch (SQLException e) {
			throw new DataProcessingException("Can't delete product by id " + id, e);
		}
	}

	@Override
	public List<Product> getAllByCategory(Long categoryId) {
		String query = "SELECT * FROM products WHERE category_id = ? AND is_deleted = FALSE";
		try (Connection connection = ConnectionPool.getInstance().getConnection();
				PreparedStatement getProductsByCategoryStatement = connection.prepareStatement(query)) {
			List<Product> listProducts = new ArrayList<>();
			getProductsByCategoryStatement.setLong(1, categoryId);
			ResultSet resultSet = getProductsByCategoryStatement.executeQuery();
			while (resultSet.next()) {
				listProducts.add(parseProductFromResultSet(resultSet));
			}
			return listProducts;
		} catch (SQLException e) {
			throw new DataProcessingException("Couldn't get a list of products" + "from products table. ", e);
		}
	}

	private Product parseProductFromResultSet(ResultSet resultSet) throws SQLException {
		// change to product
		System.out.println("Method parseProductFromResultSet invoked");
		Product product = null;
		try {
			long id = resultSet.getLong("id");
			String name = resultSet.getString("product_name");
			String description = resultSet.getString("product_desc");
			long color = resultSet.getLong("color_id");
			int category_id = resultSet.getInt("category_id");
			double price = resultSet.getDouble("product_price");
			int quantity = resultSet.getInt("product_quantity_in_stock");
			LocalDate productDate = resultSet.getDate("product_date_of_addition").toLocalDate();
			LocalTime productTime = resultSet.getTime("product_time_of_addition").toLocalTime();
			byte[] productPhoto;
			productPhoto = resultSet.getBlob("product_photo").getBinaryStream().readAllBytes();
			String base64Image = Base64.getEncoder().encodeToString(productPhoto);
			String productPhotoName = resultSet.getString("product_photo_name");
			product = new Product(id, name, description, color, category_id, price, quantity, productDate, productTime,
					productPhoto, productPhotoName);
			product.setBase64Image(base64Image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return product;
	}

	@Override
	public List<Cart> getCartProduct(List<Cart> cartList) {
		// TODO Auto-generated method stub
		List<Cart> products = new ArrayList<Cart>();
		if (cartList.size() > 0) {
			for (Cart item : cartList) {
				String query = "SELECT * FROM products WHERE id = ? AND is_deleted = FALSE";
				try (Connection connection = ConnectionPool.getInstance().getConnection();
						PreparedStatement getProductStatement = connection.prepareStatement(query)) {
					getProductStatement.setLong(1, item.getProductId());
					ResultSet resultSet = getProductStatement.executeQuery();
					if (resultSet.next() == false) {
						System.out.println("ResultSet in empty in Java");
					} else {
						do {
							Cart row = new Cart();
							row.setProductId(resultSet.getLong("id"));
							row.setProductName(resultSet.getString("product_name"));
							row.setProductDescription(resultSet.getString("product_desc"));
							row.setColorId(resultSet.getLong("color_id"));
							row.setCategoryId(resultSet.getInt("category_id"));
							row.setProductPrice(resultSet.getInt("product_price"));
							row.setProductQuantity(resultSet.getInt("product_quantity_in_stock"));
							row.setQuantityInCart(item.getQuantityInCart());
							row.setTotalPrice(row.getProductPrice() * row.getQuantityInCart());
							row.setProductDate(resultSet.getDate("product_date_of_addition").toLocalDate());
							row.setProductTime(resultSet.getTime("product_time_of_addition").toLocalTime());
							row.setProductPhoto(resultSet.getBlob("product_photo").getBinaryStream().readAllBytes());
							row.setBase64Image(Base64.getEncoder().encodeToString(row.getProductPhoto()));
							row.setProductPhotoName(resultSet.getString("product_photo_name"));
							products.add(row);
						} while (resultSet.next());
					}
				} catch (Exception e) {
					throw new DataProcessingException("Couldn't get a list of products" + "from products table. ", e);
				}
			}
		}
		return products;
	}

	@Override
	public List<Product> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<List<Product>> getAllBy(String categoryId, String colorId, String min, String max, String sort,
			int offset, int noOfRecords) {
		String queryToInsert = "";
		if (categoryId != "") {
			queryToInsert += " and category_id = " + categoryId;
		}
		if (colorId != "") {
			queryToInsert += " and color_id = " + colorId;
		}

		if (min != "" && max != "") {
			queryToInsert += " and product_price between " + min + " and " + max;
		} else if (max != "") {
			queryToInsert += " and product_price < " + max;
		} else if (min != "") {
			queryToInsert += " and product_price > " + min;
		}

		if (sort != "") {
			switch (sort) {
			case "newest_first":
				queryToInsert += " order by product_date_of_addition, product_time_of_addition desc";
				break;
			case "oldest_first":
				queryToInsert += " order by product_date_of_addition, product_time_of_addition asc";
				break;
			case "cheapest_first":
				queryToInsert += " order by product_price asc";
				break;
			case "expensive_first":
				queryToInsert += " order by product_price desc";
				break;
			case "a-z":
				queryToInsert += " order by product_desc asc";
				break;
			case "z-a":
				queryToInsert += " order by product_desc desc";
				break;
			}
		}
		String query = "select SQL_CALC_FOUND_ROWS * from products where is_deleted = false" + queryToInsert + " limit "
				+ offset + ", " + noOfRecords;
		System.out.println(query);
		try (Connection connection = ConnectionPool.getInstance().getConnection();
				PreparedStatement getAllProductsStatement = connection.prepareStatement(query)) {
			List<Product> listProducts = new ArrayList<>();
			ResultSet resultSet = getAllProductsStatement.executeQuery();
			while (resultSet.next()) {
				listProducts.add(parseProductFromResultSet(resultSet));
			}
			resultSet.close();
			resultSet = getAllProductsStatement.executeQuery("SELECT FOUND_ROWS()");
			if (resultSet.next()) {
				this.noOfRecords = resultSet.getInt(1);
			}
			return Optional.ofNullable(listProducts);
		} catch (SQLException e) {
			throw new DataProcessingException("Couldn't get a list of products" + "from products table. ", e);
		}
	}

	@Override
	public double getMinPrice() {
		// TODO Auto-generated method stub
		String query = "SELECT product_price FROM products ORDER BY product_price LIMIT 1;";
		try (Connection connection = ConnectionPool.getInstance().getConnection();
				PreparedStatement getMinPrice = connection.prepareStatement(query)) {
			double min = 0;
			ResultSet resultSet = getMinPrice.executeQuery();
			if (resultSet.next() == false) {
				System.out.println("ResultSet in empty in Java");
			} else {
				System.out.println(resultSet.getDouble("product_price"));
				min = resultSet.getDouble("product_price");
				System.out.println(min);
			}
			return min;
		} catch (SQLException e) {
			throw new DataProcessingException("Couldn't get min price." , e);
		}
	}

	@Override
	public double getMaxPrice() {
		String query = "SELECT product_price FROM products ORDER BY product_price DESC LIMIT 1";
		try (Connection connection = ConnectionPool.getInstance().getConnection();
				PreparedStatement getMinPrice = connection.prepareStatement(query)) {
			double max = 0;
			ResultSet resultSet = getMinPrice.executeQuery();
			if (resultSet.next() == false) {
				System.out.println("ResultSet in empty in Java");
			} else {
				max = resultSet.getDouble("product_price");
				System.out.println(max);
			}
			return max;
		} catch (SQLException e) {
			throw new DataProcessingException("Couldn't get min price." , e);
		}
	}
}
