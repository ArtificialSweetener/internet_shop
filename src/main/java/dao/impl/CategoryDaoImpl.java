package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dao.CategoryDao;
import dbconnection_pool.ConnectionPool;
import exception.DataProcessingException;
import models.Category;


public class CategoryDaoImpl implements CategoryDao {

	@Override
	public Category create(Category category) {
		String insertQuery = "INSERT INTO category (category_title, category_description) VALUES (?, ?)";
        try (Connection connection = ConnectionPool.getInstance().getConnection();
                PreparedStatement createCategoryStatement =
                        connection.prepareStatement(
                             insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            createCategoryStatement.setString(1, category.getCategoryTitle());
            createCategoryStatement.setString(2, category.getCategoryDescription());
            createCategoryStatement.executeUpdate();
            ResultSet resultSet = createCategoryStatement.getGeneratedKeys();
            if (resultSet.next()) {
            	category.setCategoryId(resultSet.getObject(1, Long.class));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't create category " + category, e);
        }
        System.out.println(category);
        return category;
	}

	@Override
	public Optional<Category> get(Long id) {
		String query = "SELECT * FROM category WHERE id = ?";
		try (Connection connection = ConnectionPool.getInstance().getConnection();
				PreparedStatement getCategoryStatement = connection.prepareStatement(query)) {
			getCategoryStatement.setLong(1, id);
			ResultSet resultSet = getCategoryStatement.executeQuery();
			Category category = null;
			if (resultSet.next() == false) {
				System.out.println("ResultSet in empty in Java");
			} else {
				do {
					category = parseCategoryFromResultSet(resultSet);
					System.out.println(category);
				} while (resultSet.next());
			}
			return Optional.ofNullable(category);
		} catch (SQLException e) {
			throw new DataProcessingException("Couldn't get category by id " + id, e);
		}
	}

	@Override
	public List<Category> getAll() {
		String query = "SELECT * FROM category";
		try (Connection connection = ConnectionPool.getInstance().getConnection();
				PreparedStatement getAllCategoriesStatement = connection.prepareStatement(query)) {
			List<Category> listCategory = new ArrayList<>();
			ResultSet resultSet = getAllCategoriesStatement.executeQuery();
			while (resultSet.next()) {
				listCategory.add(parseCategoryFromResultSet(resultSet));
			}
			return listCategory;
		} catch (SQLException e) {
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
		System.out.println("Method parseCategoryFromResultSet invoked");
		long catId = resultSet.getLong("id");
		String catTitle = resultSet.getString("category_title");
		String catDesc = resultSet.getString("category_description");
		Category category = new Category();
		category.setCategoryId(catId);
		category.setCategoryTitle(catTitle);
		category.setCategoryDescription(catDesc);
		return category;
	}

}
