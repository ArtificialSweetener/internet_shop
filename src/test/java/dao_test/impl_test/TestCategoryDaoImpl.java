package dao_test.impl_test;

//import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dao.CategoryDao;
import dao.impl.CategoryDaoImpl;
import dbconnection_pool.ConnectionPool;
import exception.DataProcessingException;
import models.Category;

public class TestCategoryDaoImpl { // done, add more test cases
	@Mock
	private ConnectionPool connectionPool;
	@Mock
	private Connection connection;
	@Mock
	private PreparedStatement statement;
	@Mock
	private ResultSet resultSet;

	private CategoryDao categoryDao;

	@Before
	public void setUp() throws Exception {
		connectionPool = mock(ConnectionPool.class);
		connection = mock(Connection.class);
		statement = mock(PreparedStatement.class);
		resultSet = mock(ResultSet.class);
		MockitoAnnotations.openMocks(this);
		categoryDao = new CategoryDaoImpl(connectionPool);

	}

	@Test
	public void testCreate() throws Exception {
		Category category = new Category(1L, "Category Title", "This is a category description.");

		String query = "INSERT INTO category (category_title, category_description) VALUES (?, ?)";

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(statement);
		when(statement.getGeneratedKeys()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true);
		when(resultSet.getObject(1, Long.class)).thenReturn(1L);

		Category createdCategory = categoryDao.create(category);
		assertEquals(category.getCategoryId(), createdCategory.getCategoryId());
		assertEquals(category.getCategoryTitle(), createdCategory.getCategoryTitle());
		assertEquals(category.getCategoryDescription(), createdCategory.getCategoryDescription());
	}

	@Test
	public void testCreateNullCategory() throws Exception {
		String query = "INSERT INTO category (category_title, category_description) VALUES (?, ?)";

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(statement);
		doThrow(new NullPointerException()).when(statement).setString(1, null);

		assertThrows(DataProcessingException.class, () -> categoryDao.create(null));

		verify(connectionPool).getConnection();
		verify(connection).prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

		verify(statement, never()).setLong(anyInt(), anyLong());
		verify(statement, never()).setString(anyInt(), anyString());

		verify(statement, never()).executeUpdate();
		verify(statement).close();
		verify(connection).close();
	}

	@Test
	public void testGetCategory() throws SQLException {
		Category expectedCategory = new Category(1L, "Category Title", "This is a category description.");
		String query = "SELECT * FROM category WHERE id = ?";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true).thenReturn(false);

		when(resultSet.getLong("id")).thenReturn(expectedCategory.getCategoryId());

		when(resultSet.getString("category_title")).thenReturn(expectedCategory.getCategoryTitle());
		when(resultSet.getString("category_description")).thenReturn(expectedCategory.getCategoryDescription());

		Optional<Category> actualCategoryOpt = categoryDao.get(1L);
		assertTrue(actualCategoryOpt.isPresent());
		Category actualCategory = actualCategoryOpt.get();

		assertEquals(expectedCategory.getCategoryId(), actualCategory.getCategoryId());
		assertEquals(expectedCategory.getCategoryTitle(), actualCategory.getCategoryTitle());
		assertEquals(expectedCategory.getCategoryDescription(), actualCategory.getCategoryDescription());

	}

	@Test
	public void testGetCategoryByNonExistentId() throws SQLException {
		Long nonExistentId = 999L;
		String query = "SELECT * FROM category WHERE id = ?";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(false);
		Optional<Category> actualCategoryOpt = categoryDao.get(nonExistentId);
		assertFalse(actualCategoryOpt.isPresent());
	}

	@Test(expected = DataProcessingException.class)
	public void testGetUserByIdThrowsException() throws SQLException {
		Long invalidId = -1L;
		String query = "SELECT * FROM colors WHERE id = ?";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		doThrow(new SQLException()).when(statement).setLong(1, invalidId);

		categoryDao.get(invalidId);
	}

	@Test
	public void testGetAll() throws SQLException {
		String query = "SELECT * FROM category";

		List<Category> expectedСategories = new ArrayList<>();

		Category expectedCategory1 = new Category(1L, "Category Title 1", "This is a first  category description.");
		Category expectedCategory2 = new Category(2L, "Category Title 2", "This is a second category description.");
		expectedСategories.add(expectedCategory1);
		expectedСategories.add(expectedCategory2);

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);

		when(resultSet.getLong("id")).thenReturn(expectedCategory1.getCategoryId(), expectedCategory2.getCategoryId());
		when(resultSet.getString("category_title")).thenReturn(expectedCategory1.getCategoryTitle(),
				expectedCategory2.getCategoryTitle());
		when(resultSet.getString("category_description")).thenReturn(expectedCategory1.getCategoryDescription(),
				expectedCategory2.getCategoryDescription());

		List<Category> actualCategories = categoryDao.getAll();

		verify(connection).prepareStatement(query);
		verify(statement).executeQuery();
		verify(resultSet, times(3)).next();

		verify(resultSet, times(2)).getLong("id");
		verify(resultSet, times(2)).getString("category_title");
		verify(resultSet, times(2)).getString("category_description");

		assertEquals(expectedСategories.size(), actualCategories.size());

		for (int i = 0; i < expectedСategories.size(); i++) {
			assertEquals(expectedСategories.get(i).getCategoryId(), actualCategories.get(i).getCategoryId());
			assertEquals(expectedСategories.get(i).getCategoryTitle(), actualCategories.get(i).getCategoryTitle());
			assertEquals(expectedСategories.get(i).getCategoryDescription(),
					actualCategories.get(i).getCategoryDescription());
		}
	}

}