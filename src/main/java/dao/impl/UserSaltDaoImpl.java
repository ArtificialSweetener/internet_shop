package dao.impl;

/**
 * Implementation of the UserSaltDao interface which provides CRUD (create, read, update, delete)
 * operations for UserSalt objects in a database. This implementation uses JDBC to interact with
 * the database and a ConnectionPool to manage connections.
 * @author annak
 * @version 1.0
 * @since 2023-03-13
*/
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.UserSaltDao;
import dbconnection_pool.ConnectionPool;
import exception.DataProcessingException;
import models.UserSalt;

public class UserSaltDaoImpl implements UserSaltDao {
	private ConnectionPool connectionPool;
	private static final Logger logger = LogManager.getLogger(UserSaltDaoImpl.class);

	public UserSaltDaoImpl(ConnectionPool connectionPool) {
		super();
		this.connectionPool = connectionPool;
	}

	/**
	 * Creates a new UserSalt in the database with the given information.
	 *
	 * @param userSalt the UserSalt to create
	 * @return the UserSalt that was created
	 * @throws DataProcessingException if there is an error creating the UserSalt
	 */
	@Override
	public UserSalt create(UserSalt userSalt) {
		logger.info("method create(UserSalt userSalt) of UserSaltDaoImpl class is invoked");
		String insertQuery = "INSERT INTO user_salt (user_id, user_salt) VALUES (?, ?)";
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement createUserSaltStatement = connection.prepareStatement(insertQuery,
						Statement.RETURN_GENERATED_KEYS)) {
			createUserSaltStatement.setLong(1, userSalt.getUserId());
			createUserSaltStatement.setString(2, userSalt.getSalt());
			createUserSaltStatement.executeUpdate();
			ResultSet resultSet = createUserSaltStatement.getGeneratedKeys();
			if (resultSet.next()) {
				userSalt.setUserSaltId(resultSet.getObject(1, Long.class));
			}
		} catch (SQLException | NullPointerException e) {
			logger.error("Error while creating userSalt:{}", userSalt, e);
			throw new DataProcessingException("Can't create userSalt " + userSalt, e);
		}
		logger.debug("Successfully created userSalt: {}", userSalt);
		return userSalt;
	}

	/**
	 * Retrieves the UserSalt with the given user_id from the database.
	 *
	 * @param id the user_id to search for
	 * @return an Optional containing the UserSalt if one was found, or an empty
	 *         Optional if not
	 * @throws DataProcessingException if there is an error retrieving the UserSalt
	 */
	@Override
	public Optional<UserSalt> getSaltByUserId(Long id) {
		logger.info("method getSaltByUserId(Long id) of UserSaltDaoImpl class is invoked");
		String query = "SELECT * FROM user_salt WHERE user_id = ?";
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement getSaltStatement = connection.prepareStatement(query)) {
			getSaltStatement.setLong(1, id);
			ResultSet resultSet = getSaltStatement.executeQuery();
			UserSalt userSalt = null;
			if (resultSet.next() == false) {
				logger.warn("ResultSet in empty in Java");
			} else {
				do {
					userSalt = parseUserSaltFromResultSet(resultSet);
					logger.debug("Successfully got userSalt: {}", userSalt);
				} while (resultSet.next());
			}
			return Optional.ofNullable(userSalt);
		} catch (SQLException | RuntimeException e) {
			logger.error("Error while getting userSalt by user_id:{}", id, e);
			throw new DataProcessingException("Couldn't get userSalt by user_id " + id, e);
		}
	}

	@Override
	public Optional<UserSalt> get(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserSalt> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserSalt update(UserSalt element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Parses a UserSalt object from the given ResultSet.
	 * 
	 * @param resultSet the ResultSet from which to parse the UserSalt object.
	 * @return a UserSalt object parsed from the ResultSet.
	 * @throws SQLException if there is an error accessing the ResultSet.
	 */
	private UserSalt parseUserSaltFromResultSet(ResultSet resultSet) throws SQLException {
		logger.info("method parseItemFromResultSet(ResultSet resultSet) of ItemDaoImpl class is invoked");
		UserSalt userSalt = null;
		try {
			long id = resultSet.getLong("id");
			long user_id = resultSet.getLong("user_id");
			String salt = resultSet.getString("user_salt");
			userSalt = new UserSalt(id, user_id, salt);
			logger.debug("Successfully got userSalt: {}", userSalt);
		} catch (SQLException e) {
			logger.error("Error while getting userSalt", e);
			e.printStackTrace();
		}
		return userSalt;
	}

}
