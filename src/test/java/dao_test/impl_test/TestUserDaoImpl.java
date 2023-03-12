package dao_test.impl_test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
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

import dao.UserDao;
import dao.impl.UserDaoImpl;
import dbconnection_pool.ConnectionPool;
import exception.DataProcessingException;
import models.User;

public class TestUserDaoImpl { // done, add more test cases
	@Mock
	private ConnectionPool connectionPool;
	@Mock
	private Connection connection;
	@Mock
	private PreparedStatement statement;
	@Mock
	private ResultSet resultSet;

	private UserDao userDao;

	@Before
	public void setUp() throws Exception {
		connectionPool = mock(ConnectionPool.class);
		connection = mock(Connection.class);
		statement = mock(PreparedStatement.class);
		resultSet = mock(ResultSet.class);

		MockitoAnnotations.openMocks(this);
		userDao = new UserDaoImpl(connectionPool);

	}

	@Test
	public void testCreate() throws SQLException { // done with all edge cases
		User user = new User();
		user.setUserName("test");
		user.setUserSurname("test");
		user.setUserEmail("test");
		user.setUserPassword("test");
		user.setUserType("test");

		String query = "INSERT INTO users (user_name, user_surname, user_email, user_password, user_type) VALUES (?, ?, ?, ?, ?)";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(statement);
		when(statement.getGeneratedKeys()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true);
		when(resultSet.getObject(1, Long.class)).thenReturn(1L);

		User createdUser = userDao.create(user);
		assertEquals(user.getUserName(), createdUser.getUserName());
		assertEquals(user.getUserSurname(), createdUser.getUserSurname());
		assertEquals(user.getUserEmail(), createdUser.getUserEmail());
		assertEquals(user.getUserPassword(), createdUser.getUserPassword());
		assertEquals(user.getUserType(), createdUser.getUserType());
	}

	@Test
	public void testCreateNullUser() throws SQLException {
		String query = "INSERT INTO users (user_name, user_surname, user_email, user_password, user_type) VALUES (?, ?, ?, ?, ?)";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(statement);
		doThrow(new NullPointerException()).when(statement).setString(1, null);

		assertThrows(DataProcessingException.class, () -> userDao.create(null));

		verify(connectionPool).getConnection();
		verify(connection).prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		verify(statement, never()).setString(anyInt(), anyString());
		verify(statement, never()).executeUpdate();
		verify(statement).close();
		verify(connection).close();
	}

	@Test
	public void testCreateUserNullUserName() throws SQLException {
		User user = new User();
		user.setUserName(null);
		user.setUserSurname("test");
		user.setUserEmail("test");
		user.setUserPassword("test");
		user.setUserType("test");

		String query = "INSERT INTO users (user_name, user_surname, user_email, user_password, user_type) VALUES (?, ?, ?, ?, ?)";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(statement);
		doThrow(new NullPointerException()).when(statement).setString(1, user.getUserName());

		assertThrows(DataProcessingException.class, () -> userDao.create(user));

		verify(connectionPool).getConnection();
		verify(connection).prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		verify(statement, never()).setString(2, user.getUserSurname());
		verify(statement, never()).setString(3, user.getUserEmail());
		verify(statement, never()).setString(4, user.getUserPassword());
		verify(statement, never()).setString(5, user.getUserType());
		verify(statement, never()).executeUpdate();
		verify(statement).close();
		verify(connection).close();
	}

	@Test
	public void testCreateUserNullUserSurname() throws SQLException {
		User user = new User();
		user.setUserName("test");
		user.setUserSurname(null);
		user.setUserEmail("test");
		user.setUserPassword("test");
		user.setUserType("test");

		String query = "INSERT INTO users (user_name, user_surname, user_email, user_password, user_type) VALUES (?, ?, ?, ?, ?)";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(statement);
		doThrow(new NullPointerException()).when(statement).setString(2, user.getUserSurname());

		assertThrows(DataProcessingException.class, () -> userDao.create(user));

		verify(connectionPool).getConnection();
		verify(connection).prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		verify(statement, never()).setString(3, user.getUserEmail());
		verify(statement, never()).setString(4, user.getUserPassword());
		verify(statement, never()).setString(5, user.getUserType());
		verify(statement, never()).executeUpdate();
		verify(statement).close();
		verify(connection).close();
	}

	@Test
	public void testCreateUserNullUserEmail() throws SQLException {
		User user = new User();
		user.setUserName("test");
		user.setUserSurname("test");
		user.setUserEmail(null);
		user.setUserPassword("test");
		user.setUserType("test");

		String query = "INSERT INTO users (user_name, user_surname, user_email, user_password, user_type) VALUES (?, ?, ?, ?, ?)";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(statement);
		doThrow(new NullPointerException()).when(statement).setString(3, user.getUserEmail());

		assertThrows(DataProcessingException.class, () -> userDao.create(user));

		verify(connectionPool).getConnection();
		verify(connection).prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		verify(statement, never()).setString(4, user.getUserPassword());
		verify(statement, never()).setString(5, user.getUserType());
		verify(statement, never()).executeUpdate();
		verify(statement).close();
		verify(connection).close();
	}

	@Test
	public void testCreateUserNullUserPassword() throws SQLException {
		User user = new User();
		user.setUserName("test");
		user.setUserSurname("test");
		user.setUserEmail("test");
		user.setUserPassword(null);
		user.setUserType("test");

		String query = "INSERT INTO users (user_name, user_surname, user_email, user_password, user_type) VALUES (?, ?, ?, ?, ?)";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(statement);
		doThrow(new NullPointerException()).when(statement).setString(4, user.getUserPassword());

		assertThrows(DataProcessingException.class, () -> userDao.create(user));

		verify(connectionPool).getConnection();
		verify(connection).prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		verify(statement, never()).setString(5, user.getUserType());
		verify(statement, never()).executeUpdate();
		verify(statement).close();
		verify(connection).close();
	}

	@Test
	public void testCreateUserNullUserType() throws SQLException {
		User user = new User();
		user.setUserName("test");
		user.setUserSurname("test");
		user.setUserEmail("test");
		user.setUserPassword("test");
		user.setUserType(null);

		String query = "INSERT INTO users (user_name, user_surname, user_email, user_password, user_type) VALUES (?, ?, ?, ?, ?)";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(statement);
		doThrow(new NullPointerException()).when(statement).setString(5, user.getUserType());

		assertThrows(DataProcessingException.class, () -> userDao.create(user));

		verify(connectionPool).getConnection();
		verify(connection).prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		verify(statement, never()).executeUpdate();
		verify(statement).close();
		verify(connection).close();
	}

	@Test
	public void testCreateUserDatabaseUnavailaible() {
		when(connectionPool.getConnection()).thenThrow(RuntimeException.class);
		User user = new User(1L, "John", "Doe", "johndoe@example.com", "password", "user");
		assertThrows(DataProcessingException.class, () -> userDao.create(user));
	}

	@Test
	public void testCreateUserThrowsException() throws SQLException {
		User user = new User(1L, null, "Doe", "johndoe@example.com", "password", "user");

		String query = "INSERT INTO users (user_name, user_surname, user_email, user_password, user_type) VALUES (?, ?, ?, ?, ?)";

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		doThrow(new NullPointerException()).when(statement).setString(1, null);
		assertThrows(DataProcessingException.class, () -> userDao.create(user));
	}

	@Test
	public void testGetUserById() throws SQLException {
		User expectedUser = new User();
		expectedUser.setUserId(1L);
		expectedUser.setUserName("John");
		expectedUser.setUserSurname("Doe");
		expectedUser.setUserEmail("john.doe@example.com");
		expectedUser.setUserPassword("password");
		expectedUser.setUserType("customer");
		expectedUser.setIs_bloked(false);

		String query = "SELECT * FROM users WHERE id = ?";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true).thenReturn(false);
		when(resultSet.getLong("id")).thenReturn(expectedUser.getUserId());
		when(resultSet.getString("user_name")).thenReturn(expectedUser.getUserName());
		when(resultSet.getString("user_surname")).thenReturn(expectedUser.getUserSurname());
		when(resultSet.getString("user_email")).thenReturn(expectedUser.getUserEmail());
		when(resultSet.getString("user_password")).thenReturn(expectedUser.getUserPassword());
		when(resultSet.getString("user_type")).thenReturn(expectedUser.getUserType());
		when(resultSet.getInt("is_blocked")).thenReturn(0);

		Optional<User> actualUser = userDao.get(1L);
		assertTrue(actualUser.isPresent());
		assertEquals(expectedUser.getUserId(), actualUser.get().getUserId());
		assertEquals(expectedUser.getUserName(), actualUser.get().getUserName());
		assertEquals(expectedUser.getUserSurname(), actualUser.get().getUserSurname());
		assertEquals(expectedUser.getUserEmail(), actualUser.get().getUserEmail());
		assertEquals(expectedUser.getUserPassword(), actualUser.get().getUserPassword());
		assertEquals(expectedUser.getUserType(), actualUser.get().getUserType());
		assertEquals(expectedUser.isIs_bloked(), actualUser.get().isIs_bloked());
	}

	@Test
	public void testGetUserByNonExistentId() throws SQLException {
		Long nonExistentId = 999L;
		String query = "SELECT * FROM users WHERE id = ?";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(false);

		Optional<User> actualUserOpt = userDao.get(nonExistentId);
		assertFalse(actualUserOpt.isPresent());
	}

	@Test
	public void testGetUserByNullId() throws SQLException {
		Long nullId = null;
		String query = "SELECT * FROM users WHERE id = ?";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		doThrow(new NullPointerException()).when(statement).setString(1, null);
		assertThrows(DataProcessingException.class, () -> userDao.get(nullId));
	}

	@Test
	public void testGetUserByIdDatabaseUnavailaible() {
		when(connectionPool.getConnection()).thenThrow(RuntimeException.class);
		User user = new User(1L, "John", "Doe", "johndoe@example.com", "password", "user");
		assertThrows(DataProcessingException.class, () -> userDao.get(user.getUserId()));
	}

	@Test
	public void testGetUserByIdThrowsException() throws SQLException {
		Long invalidId = -1L;
		String query = "SELECT * FROM users WHERE id = ?";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		doThrow(new SQLException()).when(statement).setLong(1, invalidId);

		assertThrows(DataProcessingException.class, () -> userDao.get(invalidId));
	}

	@Test
	public void testGetAllPaginationOffsetAndNumberOfRecordsValid() throws SQLException {
		int offset = 0;
		int noOfRecords = 2;
		String query = "select SQL_CALC_FOUND_ROWS * from users limit " + offset + ", " + noOfRecords;
		List<User> expectedUsers = new ArrayList<>();
		User user1 = new User();
		user1.setUserId(1L);
		user1.setUserName("John");
		user1.setUserSurname("Doe");
		user1.setUserEmail("johndoe@example.com");
		user1.setUserPassword("password");
		user1.setUserType("user");
		user1.setIs_bloked(false);

		User user2 = new User();
		user2.setUserId(2L);
		user2.setUserName("Jane");
		user2.setUserSurname("Doe");
		user2.setUserEmail("janedoe@example.com");
		user2.setUserPassword("password");
		user2.setUserType("user");
		user2.setIs_bloked(false);

		expectedUsers.add(user1);
		expectedUsers.add(user2);

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false).thenReturn(true);
		when(resultSet.getLong("id")).thenReturn(user1.getUserId(), user2.getUserId());
		when(resultSet.getString("user_name")).thenReturn(user1.getUserName(), user2.getUserName());
		when(resultSet.getString("user_surname")).thenReturn(user1.getUserSurname(), user2.getUserSurname());
		when(resultSet.getString("user_email")).thenReturn(user1.getUserEmail(), user2.getUserEmail());
		when(resultSet.getString("user_password")).thenReturn(user1.getUserPassword(), user2.getUserPassword());
		when(resultSet.getString("user_type")).thenReturn(user1.getUserType(), user2.getUserType());
		when(resultSet.getInt("is_blocked")).thenReturn(0, 0);
		when(statement.executeQuery("SELECT FOUND_ROWS()")).thenReturn(resultSet);

		List<User> actualUsers = userDao.getAll(offset, noOfRecords);

		verify(connection).prepareStatement(query);
		verify(statement).executeQuery();
		verify(resultSet, times(4)).next();
		verify(resultSet, times(2)).getLong("id");
		verify(resultSet, times(2)).getString("user_name");
		verify(resultSet, times(2)).getString("user_surname");
		verify(resultSet, times(2)).getString("user_email");
		verify(resultSet, times(2)).getString("user_password");
		verify(resultSet, times(2)).getString("user_type");
		verify(resultSet, times(2)).getInt("is_blocked");
		verify(resultSet).close();
		verify(statement).executeQuery("SELECT FOUND_ROWS()");

		assertEquals(expectedUsers.size(), actualUsers.size());

		for (int i = 0; i < expectedUsers.size(); i++) {
			assertEquals(expectedUsers.get(i).getUserId(), actualUsers.get(i).getUserId());
			assertEquals(expectedUsers.get(i).getUserName(), actualUsers.get(i).getUserName());
			assertEquals(expectedUsers.get(i).getUserSurname(), actualUsers.get(i).getUserSurname());
			assertEquals(expectedUsers.get(i).getUserEmail(), actualUsers.get(i).getUserEmail());
			assertEquals(expectedUsers.get(i).getUserPassword(), actualUsers.get(i).getUserPassword());
			assertEquals(expectedUsers.get(i).getUserType(), actualUsers.get(i).getUserType());
			assertEquals(expectedUsers.get(i).isIs_bloked(), actualUsers.get(i).isIs_bloked());
		}
	}

	@Test
	public void testGetAllPaginationNumberOfRecordsGreaterThanNumberOfUsers() throws SQLException {
		int offset = 0;
		int noOfRecords = 3;
		String query = "select SQL_CALC_FOUND_ROWS * from users limit " + offset + ", " + noOfRecords;
		List<User> expectedUsers = new ArrayList<>();
		User user1 = new User();
		user1.setUserId(1L);
		user1.setUserName("John");
		user1.setUserSurname("Doe");
		user1.setUserEmail("johndoe@example.com");
		user1.setUserPassword("password");
		user1.setUserType("user");
		user1.setIs_bloked(false);

		User user2 = new User();
		user2.setUserId(2L);
		user2.setUserName("Jane");
		user2.setUserSurname("Doe");
		user2.setUserEmail("janedoe@example.com");
		user2.setUserPassword("password");
		user2.setUserType("user");
		user2.setIs_bloked(false);

		expectedUsers.add(user1);
		expectedUsers.add(user2);

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false).thenReturn(true);
		when(resultSet.getLong("id")).thenReturn(user1.getUserId(), user2.getUserId());
		when(resultSet.getString("user_name")).thenReturn(user1.getUserName(), user2.getUserName());
		when(resultSet.getString("user_surname")).thenReturn(user1.getUserSurname(), user2.getUserSurname());
		when(resultSet.getString("user_email")).thenReturn(user1.getUserEmail(), user2.getUserEmail());
		when(resultSet.getString("user_password")).thenReturn(user1.getUserPassword(), user2.getUserPassword());
		when(resultSet.getString("user_type")).thenReturn(user1.getUserType(), user2.getUserType());
		when(resultSet.getInt("is_blocked")).thenReturn(0, 0);
		when(statement.executeQuery("SELECT FOUND_ROWS()")).thenReturn(resultSet);

		List<User> actualUsers = userDao.getAll(offset, noOfRecords);

		verify(connection).prepareStatement(query);
		verify(statement).executeQuery();
		verify(resultSet, times(4)).next();
		verify(resultSet, times(2)).getLong("id");
		verify(resultSet, times(2)).getString("user_name");
		verify(resultSet, times(2)).getString("user_surname");
		verify(resultSet, times(2)).getString("user_email");
		verify(resultSet, times(2)).getString("user_password");
		verify(resultSet, times(2)).getString("user_type");
		verify(resultSet, times(2)).getInt("is_blocked");
		verify(resultSet).close();
		verify(statement).executeQuery("SELECT FOUND_ROWS()");

		assertEquals(expectedUsers.size(), actualUsers.size());

		for (int i = 0; i < expectedUsers.size(); i++) {
			assertEquals(expectedUsers.get(i).getUserId(), actualUsers.get(i).getUserId());
			assertEquals(expectedUsers.get(i).getUserName(), actualUsers.get(i).getUserName());
			assertEquals(expectedUsers.get(i).getUserSurname(), actualUsers.get(i).getUserSurname());
			assertEquals(expectedUsers.get(i).getUserEmail(), actualUsers.get(i).getUserEmail());
			assertEquals(expectedUsers.get(i).getUserPassword(), actualUsers.get(i).getUserPassword());
			assertEquals(expectedUsers.get(i).getUserType(), actualUsers.get(i).getUserType());
			assertEquals(expectedUsers.get(i).isIs_bloked(), actualUsers.get(i).isIs_bloked());
		}
	}

	@Test
	public void testGetAllPaginationOffsetGreaterThanNumberOfUsers() throws SQLException {
		int offset = 3;
		int noOfRecords = 2;
		String query = "select SQL_CALC_FOUND_ROWS * from users limit " + offset + ", " + noOfRecords;

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(false).thenReturn(false);
		when(statement.executeQuery("SELECT FOUND_ROWS()")).thenReturn(resultSet);

		List<User> actualUsers = userDao.getAll(offset, noOfRecords);

		verify(connection).prepareStatement(query);
		verify(statement).executeQuery();
		verify(resultSet, times(2)).next();
		verify(resultSet, never()).getLong("id");
		verify(resultSet, never()).getString("user_name");
		verify(resultSet, never()).getString("user_surname");
		verify(resultSet, never()).getString("user_email");
		verify(resultSet, never()).getString("user_password");
		verify(resultSet, never()).getString("user_type");
		verify(resultSet, never()).getInt("is_blocked");
		verify(resultSet).close();
		verify(resultSet, never()).getInt(1);
		verify(statement).executeQuery("SELECT FOUND_ROWS()");
		assertTrue(actualUsers.isEmpty());
	}

	@Test
	public void testGetAllPaginationInvalidSqlQuery() throws SQLException {
		int offset = -1;
		int noOfRecords = 2;
		String query = "select SQL_CALC_FOUND_ROWS * from users limit " + offset + ", " + noOfRecords;

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		doThrow(new SQLException()).when(statement).executeQuery();

		assertThrows(DataProcessingException.class, () -> userDao.getAll(offset, noOfRecords));

		verify(connection).prepareStatement(query);
		verify(statement).executeQuery();
		verify(resultSet, never()).next();
		verify(resultSet, never()).getLong("id");
		verify(resultSet, never()).getString("user_name");
		verify(resultSet, never()).getString("user_surname");
		verify(resultSet, never()).getString("user_email");
		verify(resultSet, never()).getString("user_password");
		verify(resultSet, never()).getString("user_type");
		verify(resultSet, never()).getInt("is_blocked");
		verify(statement, never()).executeQuery("SELECT FOUND_ROWS()");

	}

	@Test
	public void testGetAllPaginationDatabaseUnavailaible() throws SQLException {
		int offset = 0;
		int noOfRecords = 2;
		when(connectionPool.getConnection()).thenThrow(RuntimeException.class);
		assertThrows(DataProcessingException.class, () -> userDao.getAll(offset, noOfRecords));
	}

	@Test
	public void testGetAllPaginationNoUsersFoundInTheDatabase() throws SQLException {
		int offset = 0;
		int noOfRecords = 2;
		String query = "select SQL_CALC_FOUND_ROWS * from users limit " + offset + ", " + noOfRecords;

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(false).thenReturn(false);
		when(statement.executeQuery("SELECT FOUND_ROWS()")).thenReturn(resultSet);

		List<User> actualUsers = userDao.getAll(offset, noOfRecords);

		verify(connection).prepareStatement(query);
		verify(statement).executeQuery();
		verify(resultSet, times(2)).next();
		verify(resultSet, never()).getLong("id");
		verify(resultSet, never()).getString("user_name");
		verify(resultSet, never()).getString("user_surname");
		verify(resultSet, never()).getString("user_email");
		verify(resultSet, never()).getString("user_password");
		verify(resultSet, never()).getString("user_type");
		verify(resultSet, never()).getInt("is_blocked");
		verify(resultSet).close();
		verify(resultSet, never()).getInt(1);
		verify(statement).executeQuery("SELECT FOUND_ROWS()");
		assertTrue(actualUsers.isEmpty());
	}

	@Test
	public void testUpdateUser() throws SQLException {
		String query = "UPDATE users SET user_name = ?, user_surname = ?, user_email = ? , user_password = ?, user_type =?, is_blocked =? WHERE id = ?";

		User originalUser = new User(1L, "Jane", "Doe", "janedoe@gmail.com", "password", "user");
		User updatedUser = new User(1L, "John", "Doe", "johndoe@gmail.com", "password", "user");

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);

		userDao.update(updatedUser);

		verify(connectionPool, times(1)).getConnection();
		verify(connection, times(1)).prepareStatement(query);

		verify(statement, times(1)).setString(1, updatedUser.getUserName());
		verify(statement, times(1)).setString(2, updatedUser.getUserSurname());
		verify(statement, times(1)).setString(3, updatedUser.getUserEmail());
		verify(statement, times(1)).setString(4, updatedUser.getUserPassword());
		verify(statement, times(1)).setString(5, updatedUser.getUserType());
		verify(statement, times(1)).setInt(6, 0);
		verify(statement, times(1)).setLong(7, 1L);
		verify(statement, times(1)).executeUpdate();
		verify(connection).close();

		assertEquals(originalUser.getUserId(), updatedUser.getUserId());
		assertEquals(originalUser.getUserName(), "Jane");
		assertEquals(originalUser.getUserSurname(), "Doe");
		assertEquals(originalUser.getUserEmail(), "janedoe@gmail.com");
		assertEquals(originalUser.getUserPassword(), "password");
		assertEquals(originalUser.getUserType(), "user");
		assertFalse(originalUser.isIs_bloked());
	}

	@Test
	public void testUpdateUserInvalidId() throws SQLException {
		String query = "UPDATE users SET user_name = ?, user_surname = ?, user_email = ? , user_password = ?, user_type =?, is_blocked =? WHERE id = ?";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		doThrow(new SQLException()).when(statement).executeUpdate();
		User userWithInvalidID = new User(-1L, "Jane", "Doe", "janedoe@example.com", "password", "user");
		userWithInvalidID.setIs_bloked(false);
		assertThrows(DataProcessingException.class, () -> userDao.update(userWithInvalidID));
	}

	@Test
	public void testUpdateUserInvalidUserName() throws SQLException {
		String query = "UPDATE users SET user_name = ?, user_surname = ?, user_email = ? , user_password = ?, user_type =?, is_blocked =? WHERE id = ?";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		doThrow(new SQLException()).when(statement).executeUpdate();
		User userWithInvalidUserName = new User(1L, "", "Doe", "janedoe@example.com", "password", "user");
		userWithInvalidUserName.setIs_bloked(false);
		assertThrows(DataProcessingException.class, () -> userDao.update(userWithInvalidUserName));
	}

	@Test
	public void testUpdateUserInvalidUserSurname() throws SQLException {
		String query = "UPDATE users SET user_name = ?, user_surname = ?, user_email = ? , user_password = ?, user_type =?, is_blocked =? WHERE id = ?";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		doThrow(new SQLException()).when(statement).executeUpdate();
		User userWithInvalidUserSurname = new User(1L, "Jane", "", "janedoe@example.com", "password", "user");
		userWithInvalidUserSurname.setIs_bloked(false);
		assertThrows(DataProcessingException.class, () -> userDao.update(userWithInvalidUserSurname));
	}

	@Test
	public void testUpdateUserInvalidUserPassword() throws SQLException {
		String query = "UPDATE users SET user_name = ?, user_surname = ?, user_email = ? , user_password = ?, user_type =?, is_blocked =? WHERE id = ?";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		doThrow(new SQLException()).when(statement).executeUpdate();
		User userWithInvalidUserPassword = new User(1L, "Jane", "Doe", "janedoe@example.com", "", "user");
		userWithInvalidUserPassword.setIs_bloked(false);
		assertThrows(DataProcessingException.class, () -> userDao.update(userWithInvalidUserPassword));
	}

	@Test
	public void testUpdateUserInvalidUserEmail() throws SQLException {
		String query = "UPDATE users SET user_name = ?, user_surname = ?, user_email = ? , user_password = ?, user_type =?, is_blocked =? WHERE id = ?";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		doThrow(new SQLException()).when(statement).executeUpdate();
		User userWithInvalidUserEmail = new User(1L, "Jane", "Doe", "", "password", "user");
		userWithInvalidUserEmail.setIs_bloked(false);
		assertThrows(DataProcessingException.class, () -> userDao.update(userWithInvalidUserEmail));
	}

	@Test
	public void testUpdateUserNonExistent() throws SQLException {
		String query = "UPDATE users SET user_name = ?, user_surname = ?, user_email = ? , user_password = ?, user_type =?, is_blocked =? WHERE id = ?";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		doThrow(new SQLException()).when(statement).executeUpdate();
		User nonExistingUser = new User(2L, "Jane", "Doe", "janedoe@example.com", "password", "user");
		nonExistingUser.setIs_bloked(false);
		assertThrows(DataProcessingException.class, () -> userDao.update(nonExistingUser));
	}

	@Test
	public void testUpdateUserUserNull() throws SQLException {
		String query = "UPDATE users SET user_name = ?, user_surname = ?, user_email = ? , user_password = ?, user_type =?, is_blocked =? WHERE id = ?";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		doThrow(new NullPointerException()).when(statement).setString(1, null);
		assertThrows(DataProcessingException.class, () -> userDao.update(null));

		verify(connectionPool).getConnection();
		verify(connection).prepareStatement(query);
		verify(statement, never()).setString(anyInt(), anyString());
		verify(statement, never()).setInt(anyInt(), anyInt());
		verify(statement, never()).setLong(anyInt(), anyLong());
		verify(statement, never()).executeUpdate();
		verify(statement).close();
		verify(connection).close();
	}

	@Test
	public void testUpdateUserDatabaseUnavailaible() throws SQLException {
		when(connectionPool.getConnection()).thenThrow(RuntimeException.class);
		User user2 = new User(1L, "John", "Doe", "johndoe@example.com", "password", "user");
		user2.setIs_bloked(false);
		assertThrows(DataProcessingException.class, () -> userDao.update(user2));
	}

	@Test
	public void testUpdateUserExceptionOccured() throws SQLException {
		when(statement.executeUpdate()).thenThrow(SQLException.class);
		User user3 = new User(1L, "John", "Doe", "johndoe@example.com", "password", "user");
		user3.setIs_bloked(false);
		assertThrows(DataProcessingException.class, () -> userDao.update(user3));
	}

	@Test
	public void testDeleteUser() throws SQLException {

		User user = new User(1L, "John", "Doe", "johndoe@example.com", "password", "user");
		String query = "UPDATE users SET is_blocked = TRUE WHERE id = ? AND is_blocked = FALSE";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		when(statement.executeUpdate()).thenReturn(1);

		boolean isDeleted = userDao.delete(user.getUserId());
		verify(statement).setLong(1, user.getUserId());
		verify(statement).executeUpdate();

		assertTrue(isDeleted);
	}

	@Test
	public void testDeleteUserUserDoesNotExist() throws SQLException {
		User user = new User(99L, "John", "Doe", "johndoe@example.com", "password", "user");
		String query = "UPDATE users SET is_blocked = TRUE WHERE id = ? AND is_blocked = FALSE";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		when(statement.executeUpdate()).thenReturn(0);

		boolean isDeleted = userDao.delete(user.getUserId());
		verify(statement).setLong(1, user.getUserId());
		verify(statement).executeUpdate();

		assertFalse(isDeleted);
	}

	@Test
	public void testDeleteUserFailAlreadyBlocked() throws SQLException {
		User user = new User(1L, "John", "Doe", "johndoe@example.com", "password", "user");
		String query = "UPDATE users SET is_blocked = TRUE WHERE id = ? AND is_blocked = FALSE";

		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		when(statement.executeUpdate()).thenReturn(0);

		boolean isDeleted = userDao.delete(user.getUserId());
		verify(statement).setLong(1, user.getUserId());
		verify(statement).executeUpdate();
		assertFalse(isDeleted);
	}

	@Test
	public void testDeleteUserExceptionOccured() throws SQLException {
		User user = new User(1L, "John", "Doe", "johndoe@example.com", "password", "user");
		String query = "UPDATE users SET is_blocked = TRUE WHERE id = ? AND is_blocked = FALSE";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		doThrow(new SQLException()).when(statement).executeUpdate();

		assertThrows(DataProcessingException.class, () -> userDao.delete(user.getUserId()));

		verify(statement).setLong(1, user.getUserId());
		verify(statement).executeUpdate();
	}

	@Test
	public void testDeleteUserDatabaseUnavailaible() throws SQLException {
		User user = new User(1L, "John", "Doe", "johndoe@example.com", "password", "user");
		when(connectionPool.getConnection()).thenThrow(RuntimeException.class);
		assertThrows(DataProcessingException.class, () -> userDao.delete(user.getUserId()));
	}

	@Test
	public void testFindByEmail() throws SQLException {
		User user = new User(1L, "John", "Doe", "johndoe@example.com", "password", "user");
		user.setIs_bloked(false);

		String query = "SELECT * FROM users WHERE user_email = ? AND is_blocked = FALSE";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);

		when(resultSet.next()).thenReturn(true, false);
		when(resultSet.getLong("id")).thenReturn(user.getUserId());
		when(resultSet.getString("user_name")).thenReturn(user.getUserName());
		when(resultSet.getString("user_surname")).thenReturn(user.getUserSurname());
		when(resultSet.getString("user_email")).thenReturn(user.getUserEmail());
		when(resultSet.getString("user_password")).thenReturn(user.getUserPassword());
		when(resultSet.getString("user_type")).thenReturn(user.getUserType());
		when(resultSet.getInt("is_blocked")).thenReturn(0);

		Optional<User> foundUser = userDao.findByEmail(user.getUserEmail());

		verify(statement).setString(1, user.getUserEmail());
		verify(statement).executeQuery();
		verify(resultSet).getInt("is_blocked");

		assertTrue(foundUser.isPresent());
		assertEquals(user.getUserId(), foundUser.get().getUserId());
		assertEquals(user.getUserName(), foundUser.get().getUserName());
		assertEquals(user.getUserSurname(), foundUser.get().getUserSurname());
		assertEquals(user.getUserEmail(), foundUser.get().getUserEmail());
		assertEquals(user.getUserPassword(), foundUser.get().getUserPassword());
		assertEquals(user.getUserType(), foundUser.get().getUserType());
	}

	@Test
	public void testFindByEmailDatabaseNotAvailaible() throws SQLException {
		when(connectionPool.getConnection()).thenThrow(RuntimeException.class);
		User user = new User(1L, "John", "Doe", "johndoe@example.com", "password", "user");
		user.setIs_bloked(false);
		assertThrows(DataProcessingException.class, () -> userDao.findByEmail(user.getUserEmail()));
	}

	@Test
	public void testFindByEmailNoUserWithThisEmailExist() throws SQLException {
		User user = new User(1L, "John", "Doe", "johndoe@example.com", "password", "user");
		user.setIs_bloked(false);

		String query = "SELECT * FROM users WHERE user_email = ? AND is_blocked = FALSE";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(false);
		Optional<User> foundUser = userDao.findByEmail(user.getUserEmail());

		verify(statement).setString(1, user.getUserEmail());
		verify(statement).executeQuery();

		assertTrue(foundUser.isEmpty());
	}

	@Test
	public void testFindByEmailThrowsSQLException() throws SQLException {
		User user = new User(1L, "John", "Doe", "johndoe@example.com", "password", "user");
		user.setIs_bloked(false);

		String query = "SELECT * FROM users WHERE user_email = ? AND is_blocked = FALSE";
		when(connectionPool.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(query)).thenReturn(statement);
		when(statement.executeQuery()).thenThrow(new SQLException());

		assertThrows(DataProcessingException.class, () -> userDao.findByEmail(user.getUserEmail()));
		verify(statement).setString(1, user.getUserEmail());
	}

}