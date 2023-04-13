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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.ProductDao;
import dbconnection_pool.ConnectionPool;
import exception.DataProcessingException;
import models.Product;

public class ProductDaoImpl implements ProductDao { // check by real db interaction
	private int noOfRecords;
	private ConnectionPool connectionPool;
	private static final Logger logger = LogManager.getLogger(ProductDaoImpl.class);

	public ProductDaoImpl(ConnectionPool connectionPool) {
		super();
		this.connectionPool = connectionPool;
	}

	public int getNoOfRecords() {
		return noOfRecords;
	}

	@Override
	public Product create(Product product) {
		logger.info("method create(Product product) of ProductDaoImpl class is invoked");
		String insertQuery = "INSERT INTO products (product_name, product_desc, color_id, category_id, product_price, product_quantity_in_stock, product_date_of_addition, product_time_of_addition, product_photo, product_photo_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement createProductStatement = connection.prepareStatement(insertQuery,
						Statement.RETURN_GENERATED_KEYS)) {
			createProductStatement.setString(1, product.getProductName());
			createProductStatement.setString(2, product.getProductDescription());
			createProductStatement.setLong(3, product.getColorId());
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
		} catch (SQLException | RuntimeException e) {
			logger.error("Error while creating product:{}", product, e);
			throw new DataProcessingException("Can't create product " + product, e);
		}
		logger.debug("Successfully created product:{}", product);
		return product;
	}

	@Override
	public Optional<Product> get(Long id) {
		logger.info("method get(Long id) of ProductDaoImpl class is invoked");
		String query = "SELECT * FROM products WHERE id = ? AND is_deleted = 0";
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement getProductStatement = connection.prepareStatement(query)) {
			getProductStatement.setLong(1, id);
			ResultSet resultSet = getProductStatement.executeQuery();
			Product product = null;
			if (resultSet.next() == false) {
				logger.warn("ResultSet in empty in Java");
			} else {
				do {
					product = parseProductFromResultSet(resultSet);
					logger.debug("Successfully got product:{}", product);
				} while (resultSet.next());
			}
			return Optional.ofNullable(product);
		} catch (SQLException | RuntimeException e) {
			logger.error("Error while getting product by id:{}", id, e);
			throw new DataProcessingException("Couldn't get product by id " + id, e);
		}
	}

	@Override
	public Optional<Product> getNoDeleteCheck(Long id) {
		logger.info("method getNoDeleteCheck(Long id) of ProductDaoImpl class is invoked");
		String query = "SELECT * FROM products WHERE id = ?";
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement getProductStatement = connection.prepareStatement(query)) {
			getProductStatement.setLong(1, id);
			ResultSet resultSet = getProductStatement.executeQuery();
			Product product = null;
			if (resultSet.next() == false) {
				logger.warn("ResultSet in empty in Java");
			} else {
				do {
					product = parseProductFromResultSet(resultSet);
					logger.debug("Successfully got product:{}", product);
				} while (resultSet.next());
			}
			return Optional.ofNullable(product);
		} catch (SQLException | RuntimeException e) {
			logger.error("Error while getting product by id:{}", id, e);
			throw new DataProcessingException("Couldn't get product by id " + id, e);
		}
	}

	@Override
	public List<Product> getAll(int offset, int noOfRecords) {
		logger.info("method getNoDeleteCheck(int offset, int noOfRecords) of ProductDaoImpl class is invoked");
		String query = "select SQL_CALC_FOUND_ROWS * from products where is_deleted = 0 limit " + offset + ", "
				+ noOfRecords;
		try (Connection connection = connectionPool.getConnection();
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
				logger.debug("Successfully got product list:{}", listProducts);
			}
			return listProducts;
		} catch (SQLException | RuntimeException e) {
			logger.error("Error while getting product list", e);
			throw new DataProcessingException("Couldn't get a list of products" + "from products table. ", e);
		}

	}

	@Override
	public Product update(Product product) {
		logger.info("method update(Product product) of ProductDaoImpl class is invoked");
		String selectQuery = "UPDATE products SET product_name = ?, "
				+ "product_desc = ?, "
				+ "color_id = ? , "
				+ "category_id = ?, product_price= ?, product_quantity_in_stock = ?, product_photo =?, product_photo_name =? "
				+ "WHERE id = ? AND is_deleted = 0";
		try (Connection connection = connectionPool.getConnection();
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
			updateProductStatement.setInt(10, product.getIsdeleted());
			updateProductStatement.executeUpdate();
			logger.debug("Successfully updated product:{}", product);
		} catch (SQLException | RuntimeException e) {
			logger.error("Error while updating product:{}", product, e);
			throw new DataProcessingException("Can't update product " + product, e);
		}
		return product;
	}

	@Override
	public boolean delete(Long id) {
		logger.info("method delete(Long id) of ProductDaoImpl class is invoked");
		String selectQuery = "UPDATE products SET is_deleted = TRUE WHERE id = ? AND is_deleted = 0";
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement deleteProductStatement = connection.prepareStatement(selectQuery)) {
			deleteProductStatement.setLong(1, id);
			int isDone = deleteProductStatement.executeUpdate();
			logger.debug("Successfully deleted product by id:{}", id);
			return isDone > 0;
		} catch (SQLException | RuntimeException e) {
			logger.error("Error while deleting product by id:{}", id, e);
			throw new DataProcessingException("Can't delete product by id " + id, e);
		}
	}

	@Override
	public List<Product> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> getAllBy(String categoryId, String colorId, String min, String max, String sort, int offset,
			int noOfRecords) {
		logger.info("method getAllBy() of ProductDaoImpl class is invoked");

		String query = buildQuery(categoryId, colorId, min, max, sort, offset, noOfRecords);
		List<Product> listProducts = new ArrayList<>();

		try (Connection connection = connectionPool.getConnection();
				PreparedStatement getAllProductsStatement = connection.prepareStatement(query)) {
			int parameterIndex = 1;
			if (!categoryId.isEmpty()) {
				getAllProductsStatement.setObject(parameterIndex++, categoryId);
			}
			if (!colorId.isEmpty()) {
				getAllProductsStatement.setObject(parameterIndex++, colorId);
			}
			if (!min.isEmpty() && !max.isEmpty()) {
				getAllProductsStatement.setObject(parameterIndex++, Double.parseDouble(min));
				getAllProductsStatement.setObject(parameterIndex++, Double.parseDouble(max));
			} else if (!max.isEmpty()) {
				getAllProductsStatement.setObject(parameterIndex++, Double.parseDouble(max));
			} else if (!min.isEmpty()) {
				getAllProductsStatement.setObject(parameterIndex++, Double.parseDouble(min));
			}
			getAllProductsStatement.setObject(parameterIndex++, offset);
			getAllProductsStatement.setObject(parameterIndex++, noOfRecords);

			ResultSet resultSet = getAllProductsStatement.executeQuery();

			while (resultSet.next()) {
				listProducts.add(parseProductFromResultSet(resultSet));
			}
			resultSet.close();

			resultSet = getAllProductsStatement.executeQuery("SELECT FOUND_ROWS()");
			if (resultSet.next()) {
				this.noOfRecords = resultSet.getInt(1);
				logger.debug("Successfully got list of products:{}", listProducts);
			}

			return listProducts;
		} catch (SQLException | RuntimeException e) {
			logger.error("Error while getting a list of products", e);
			throw new DataProcessingException("Couldn't get a list of products from products table. ", e);
		}
	}

	private String buildQuery(String categoryId, String colorId, String min, String max, String sort, int offset,
			int noOfRecords) {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("select SQL_CALC_FOUND_ROWS * from products where is_deleted = 0");

		if (!categoryId.isEmpty()) {
			queryBuilder.append(" and category_id = ?");
		}
		if (!colorId.isEmpty()) {
			queryBuilder.append(" and color_id = ?");
		}
		if (!min.isEmpty() && !max.isEmpty()) {
			queryBuilder.append(" and product_price between ? and ?");
		} else if (!max.isEmpty()) {
			queryBuilder.append(" and product_price < ?");
		} else if (!min.isEmpty()) {
			queryBuilder.append(" and product_price > ?");
		}
		if (!sort.isEmpty()) {
			switch (sort) {
			case "newest_first":
				queryBuilder.append(" order by product_date_of_addition, product_time_of_addition desc");
				break;

			case "oldest_first":
				queryBuilder.append(" order by product_date_of_addition, product_time_of_addition asc");
				break;
			case "cheapest_first":
				queryBuilder.append(" order by product_price asc");
				break;
			case "expensive_first":
				queryBuilder.append(" order by product_price desc");
				break;
			case "a-z":
				queryBuilder.append(" order by product_desc asc");
				break;
			case "z-a":
				queryBuilder.append(" order by product_desc desc");
				break;
			default:
				logger.warn("Invalid sort parameter: {}", sort);
				break;
			}
		}
		queryBuilder.append(" limit ?, ?");

		return queryBuilder.toString();
	}

	private Product parseProductFromResultSet(ResultSet resultSet) throws SQLException {
		logger.info("method parseProductFromResultSet(ResultSet resultSet) of ProductDaoImpl class is invoked");
		Product product = null;
		try {
			long id = resultSet.getLong("id");
			String name = resultSet.getString("product_name");
			String description = resultSet.getString("product_desc");
			long color = resultSet.getLong("color_id");
			long category_id = resultSet.getLong("category_id");
			double price = resultSet.getDouble("product_price");
			int quantity = resultSet.getInt("product_quantity_in_stock");
			LocalDate productDate = resultSet.getDate("product_date_of_addition").toLocalDate();
			LocalTime productTime = resultSet.getTime("product_time_of_addition").toLocalTime();
			byte[] productPhoto;
			productPhoto = resultSet.getBlob("product_photo").getBinaryStream().readAllBytes();
			String base64Image = Base64.getEncoder().encodeToString(productPhoto);
			String productPhotoName = resultSet.getString("product_photo_name");
			int isdeleted = resultSet.getInt("is_deleted");
			product = new Product(id, name, description, color, category_id, price, quantity, productDate, productTime,
					productPhoto, productPhotoName, isdeleted);
			product.setBase64Image(base64Image);
		} catch (IOException e) {
			logger.error("Error while parsing product", e);
			e.printStackTrace();
		} catch (SQLException e) {
			logger.error("Error while parsing product", e);
			e.printStackTrace();
		}
		logger.debug("Successfully parsed product:{}", product);
		return product;
	}

	@Override
	public double getMinPrice() {
		logger.info("method getMinPrice() of ProductDaoImpl class is invoked");
		String query = "SELECT product_price FROM products WHERE is_deleted = 0 ORDER BY product_price LIMIT 1;";
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement getMinPrice = connection.prepareStatement(query)) {
			double min = 0;
			ResultSet resultSet = getMinPrice.executeQuery();
			if (resultSet.next() == false) {
				logger.warn("ResultSet in empty in Java");
			} else {
				min = resultSet.getDouble("product_price");
				logger.debug("Successfully got the minimum price:{}", min);
			}
			return min;
		} catch (SQLException | RuntimeException e) {
			logger.error("Error while getting the minimum price", e);
			throw new DataProcessingException("Couldn't get min price.", e);
		}
	}

	@Override
	public double getMaxPrice() {
		logger.info("method getMaxPrice() of ProductDaoImpl class is invoked");
		String query = "SELECT product_price FROM products WHERE is_deleted = 0 ORDER BY product_price DESC LIMIT 1";
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement getMinPrice = connection.prepareStatement(query)) {
			double max = 0;
			ResultSet resultSet = getMinPrice.executeQuery();
			if (resultSet.next() == false) {
				logger.warn("ResultSet in empty in Java");
			} else {
				max = resultSet.getDouble("product_price");
				logger.debug("Successfully got the maximum price:{}", max);
			}
			return max;
		} catch (SQLException | RuntimeException e) {
			logger.error("Error while getting the maximum price", e);
			throw new DataProcessingException("Couldn't get max price.", e);
		}
	}
}
