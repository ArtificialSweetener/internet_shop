package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dao.UserDao;
import dbconnection_pool.ConnectionPool;
import exception.DataProcessingException;
import models.User;
import org.apache.logging.log4j.Logger;

import org.apache.logging.log4j.LogManager;

/**
 * This class implements UserDao interface and provides functionality to perform
 * CRUD operations on User object. It uses JDBC and MySQL database.
 * 
 * @author annak
 * @version 1.0
 * @since 2023-03-13
 */

public class UserDaoImpl implements UserDao {
	private int noOfRecords;
	private ConnectionPool connectionPool;
	private static final Logger logger = LogManager.getLogger(UserDaoImpl.class);

	public UserDaoImpl(ConnectionPool connectionPool) {
		System.out.println("UserDaoImpl connection pool:" + connectionPool);
		this.connectionPool = connectionPool;
	}

	/**
	 * Gets the number of records.
	 *
	 * @return the number of records
	 */
	public int getNoOfRecords() {
		return noOfRecords;
	}

	/**
	 * Creates the specified user in the database.
	 *
	 * @param user the user to be created
	 * @return the created user
	 * @throws DataProcessingException if there is an error while creating the user
	 */
	@Override
	public User create(User user) {
		logger.info("method create of UserDaoImpl class is invoked");
		String query = "INSERT INTO users (user_name, user_surname, user_email, user_password, user_type) VALUES (?, ?, ?, ?, ?)";
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement createUserStatement = connection.prepareStatement(query,
						Statement.RETURN_GENERATED_KEYS)) {
			createUserStatement.setString(1, user.getUserName());
			createUserStatement.setString(2, user.getUserSurname());
			createUserStatement.setString(3, user.getUserEmail());
			createUserStatement.setString(4, user.getUserPassword());
			createUserStatement.setString(5, user.getUserType());
			createUserStatement.executeUpdate();
			ResultSet resultSet = createUserStatement.getGeneratedKeys();
			if (resultSet.next()) {
				user.setUserId(resultSet.getObject(1, Long.class));
				logger.debug("Successfully created user: {}", user);
			}
			return user;
		} catch (SQLException | RuntimeException e) {
			logger.error("Error while creating user:{}", user, e);
			throw new DataProcessingException("Couldn't create " + user + ". ", e);
		}
	}

	/**
	 * Gets the specified user from the database by ID.
	 *
	 * @param id the ID of the user to get
	 * @return an Optional containing the user if found, or an empty Optional
	 *         otherwise
	 * @throws DataProcessingException if there is an error while getting the user
	 */
	@Override
	public Optional<User> get(Long id) {
		logger.info("method get of UserDaoImpl class is invoked");
		String query = "SELECT * FROM users WHERE id = ?";
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement getUserStatement = connection.prepareStatement(query)) {
			getUserStatement.setLong(1, id);
			ResultSet resultSet = getUserStatement.executeQuery();
			User user = null;
			if (resultSet.next() == false) {
				logger.warn("ResultSet in empty in Java");
			} else {
				do {
					user = parseUserFromResultSet(resultSet);
					logger.debug("Successfully got user:{}", user);
				} while (resultSet.next());
			}
			return Optional.ofNullable(user);
		} catch (SQLException | RuntimeException e) {
			logger.error("Error while getting user by id:{}", id, e);
			throw new DataProcessingException("Couldn't get user by id " + id, e);
		}
	}

	/**
	 * Gets a list of User objects from the database, starting from the given offset
	 * and returning up to noOfRecords records.
	 *
	 * @param offset      the starting index of the records to return
	 * @param noOfRecords the maximum number of records to return
	 * @return a List of User objects
	 * @throws DataProcessingException if there is an error while retrieving the
	 *                                 users
	 */
	@Override
	public List<User> getAll(int offset, int noOfRecords) {
		logger.info("method getAll of UserDaoImpl class is invoked");
		String query = "select SQL_CALC_FOUND_ROWS * from users limit " + offset + ", " + noOfRecords;
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement getAllUsersStatement = connection.prepareStatement(query)) {
			List<User> listUsers = new ArrayList<>();
			ResultSet resultSet = getAllUsersStatement.executeQuery();
			while (resultSet.next()) {
				listUsers.add(parseUserFromResultSet(resultSet));
			}
			resultSet.close();
			resultSet = getAllUsersStatement.executeQuery("SELECT FOUND_ROWS()");
			if (resultSet.next()) {
				this.noOfRecords = resultSet.getInt(1);
				logger.debug("Successfully got users:{}", listUsers);
			}
			return listUsers;
		} catch (SQLException | RuntimeException e) {
			logger.error("Error while getting a list of users", e);
			throw new DataProcessingException("Couldn't get a list of users" + "from users table. ", e);
		}
	}

	/**
	 * Updates the specified User object in the database.
	 *
	 * @param user the User object to be updated
	 * @return the updated User object
	 * @throws DataProcessingException if there is an error while updating the user
	 */
	@Override
	public User update(User user) {
		logger.info("method update of UserDaoImpl class is invoked");
		String selectQuery = "UPDATE users SET user_name = ?, user_surname = ?, user_email = ? , user_password = ?, user_type =?, is_blocked =? "
				+ "WHERE id = ?";
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement updateUserStatement = connection.prepareStatement(selectQuery)) {
			updateUserStatement.setString(1, user.getUserName());
			updateUserStatement.setString(2, user.getUserSurname());
			updateUserStatement.setString(3, user.getUserEmail());
			updateUserStatement.setString(4, user.getUserPassword());
			updateUserStatement.setString(5, user.getUserType());
			int myInt = user.isIs_bloked() ? 1 : 0;
			updateUserStatement.setInt(6, myInt);
			updateUserStatement.setLong(7, user.getUserId());
			updateUserStatement.executeUpdate();
			logger.debug("Successfully updated user: {}", user);
		} catch (SQLException | RuntimeException e) {
			logger.error("Error while updating user ", e);
			throw new DataProcessingException("Can't update user " + user, e);
		}
		return user;
	}

	/**
	 * Deletes the User object with the specified ID from the database.
	 *
	 * @param id the ID of the User object to be deleted
	 * @return true if the User object was deleted, false otherwise
	 * @throws DataProcessingException if there is an error while deleting the user
	 */
	@Override
	public boolean delete(Long id) {
		logger.info("method delete of UserDaoImpl class is invoked");
		String selectQuery = "UPDATE users SET is_blocked = TRUE WHERE id = ? AND is_blocked = FALSE";
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement blockUserStatement = connection.prepareStatement(selectQuery)) {
			blockUserStatement.setLong(1, id);
			int isDone = blockUserStatement.executeUpdate();
			logger.debug("Successfully blocked user by id:{}", id);
			return isDone > 0;
		} catch (SQLException | RuntimeException e) {
			logger.error("Error while blocking user by id:{}", id, e);
			throw new DataProcessingException("Can't delete user by id " + id, e);
		}
	}

	/**
	 * Finds the User object with the specified email address in the database.
	 *
	 * @param email the email address of the User object to be found
	 * @return an Optional containing the User object if found, or an empty Optional
	 *         otherwise
	 * @throws DataProcessingException if there is an error while finding the user
	 */
	@Override
	public Optional<User> findByEmail(String email) {
		logger.info("method findByEmail of UserDaoImpl class is invoked");
		String query = "SELECT * FROM users WHERE user_email = ? AND is_blocked = FALSE";
		// System.out.println("findByEmail:" + connection);
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement getUserStatement = connection.prepareStatement(query)) {
			System.out.println("Check if we got connection:" + connection);
			getUserStatement.setString(1, email);
			try (ResultSet resultSet = getUserStatement.executeQuery()) {
				;
				User user = null;
				if (resultSet.next() == false) {
					logger.warn("ResultSet in empty in Java");
				} else {
					do {
						user = parseUserFromResultSet(resultSet);
					} while (resultSet.next());
				}
				logger.debug("Successfully found by email this user: {}", user);
				return Optional.ofNullable(user);
			} catch (SQLException e) {
				logger.error("Error while finding user by email: {}", email, e);
				throw new DataProcessingException("Couldn't get user by email " + email, e);
			}
		} catch (SQLException | RuntimeException e) {
			logger.error("Error while finding user by email: {}", email, e);
			throw new DataProcessingException("Couldn't get user by email " + email, e);
		}
	}

	/**
	 * Parses a ResultSet object into a User object.
	 *
	 * @param resultSet the ResultSet object to be parsed
	 * @return a User object parsed from the ResultSet
	 * @throws SQLException if there is an error while parsing the ResultSet
	 */
	private User parseUserFromResultSet(ResultSet resultSet) throws SQLException {
		logger.info("method parseUserFromResultSet of UserDaoImpl class is invoked");
		long userId = resultSet.getLong("id");
		String userName = resultSet.getString("user_name");
		String userSurname = resultSet.getString("user_surname");
		String userEmail = resultSet.getString("user_email");
		String userPassword = resultSet.getString("user_password");
		String userType = resultSet.getString("user_type");
		int is_blocked = resultSet.getInt("is_blocked");
		User user = new User();
		user.setUserId(userId);
		user.setUserName(userName);
		user.setUserSurname(userSurname);
		user.setUserEmail(userEmail);
		user.setUserPassword(userPassword);
		user.setUserType(userType);
		if (is_blocked == 1) {
			user.setIs_bloked(true);
		} else {
			user.setIs_bloked(false);
		}
		logger.debug("Successfully parsed this user: {}", user);
		return user;
	}

	@Override
	public List<User> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
