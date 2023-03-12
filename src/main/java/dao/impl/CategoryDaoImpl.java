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

import dao.CategoryDao;
import dbconnection_pool.ConnectionPool;
import exception.DataProcessingException;
import models.Category;

public class CategoryDaoImpl implements CategoryDao {
	private ConnectionPool connectionPool;
	private static final Logger logger = LogManager.getLogger(CategoryDaoImpl.class);

	public CategoryDaoImpl(ConnectionPool connectionPool) {
		super();
		this.connectionPool = connectionPool;
	}

	@Override
	public Category create(Category category) {
		logger.info("method create(Category category) of CategoryDaoImpl class is invoked");
		String insertQuery = "INSERT INTO category (category_title, category_description) VALUES (?, ?)";
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement createCategoryStatement = connection.prepareStatement(insertQuery,
						Statement.RETURN_GENERATED_KEYS)) {
			createCategoryStatement.setString(1, category.getCategoryTitle());
			createCategoryStatement.setString(2, category.getCategoryDescription());
			createCategoryStatement.executeUpdate();
			ResultSet resultSet = createCategoryStatement.getGeneratedKeys();
			if (resultSet.next()) {
				category.setCategoryId(resultSet.getObject(1, Long.class));
			}
		} catch (SQLException | NullPointerException e) {
			logger.error("Error while creating category:{}", category, e);
			throw new DataProcessingException("Can't create category " + category, e);
		}
		logger.debug("Successfully created category: {}", category);
		return category;
	}

	@Override
	public Optional<Category> get(Long id) {
		logger.info("method get(Long id) of CategoryDaoImpl class is invoked");
		String query = "SELECT * FROM category WHERE id = ?";
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement getCategoryStatement = connection.prepareStatement(query)) {
			getCategoryStatement.setLong(1, id);
			ResultSet resultSet = getCategoryStatement.executeQuery();
			Category category = null;
			if (resultSet.next() == false) {
				logger.warn("ResultSet in empty in Java");
			} else {
				do {
					category = parseCategoryFromResultSet(resultSet);
					logger.debug("Successfully got category: {}", category);
				} while (resultSet.next());
			}
			return Optional.ofNullable(category);
		} catch (SQLException | NullPointerException e) {
			logger.error("Error while getting category by id:{}", id, e);
			throw new DataProcessingException("Couldn't get category by id " + id, e);
		}
	}

	@Override
	public List<Category> getAll() {
		logger.info("method getAll() of CategoryDaoImpl class is invoked");
		String query = "SELECT * FROM category";
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement getAllCategoriesStatement = connection.prepareStatement(query)) {
			List<Category> listCategory = new ArrayList<>();
			ResultSet resultSet = getAllCategoriesStatement.executeQuery();
			while (resultSet.next()) {
				listCategory.add(parseCategoryFromResultSet(resultSet));
			}
			logger.debug("Successfully got category list: {}", listCategory);
			return listCategory;
		} catch (SQLException e) {
			logger.error("Error while getting category list", e);
			throw new DataProcessingException("Couldn't get a list of categories" + "from category table. ", e);
		}
	}

	@Override
	public Category update(Category element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	private Category parseCategoryFromResultSet(ResultSet resultSet) throws SQLException {
		logger.info("method parseCategoryFromResultSet(ResultSet resultSet) of CategoryDaoImpl class is invoked");
		long catId = resultSet.getLong("id");
		String catTitle = resultSet.getString("category_title");
		String catDesc = resultSet.getString("category_description");
		Category category = new Category();
		category.setCategoryId(catId);
		category.setCategoryTitle(catTitle);
		category.setCategoryDescription(catDesc);
		logger.debug("Successfully parsed category: {}", category);
		return category;
	}

}
