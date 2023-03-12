package dao.impl;

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
	private static final Logger logger = LogManager.getLogger(ItemDaoImpl.class);

	public UserSaltDaoImpl(ConnectionPool connectionPool) {
		super();
		this.connectionPool = connectionPool;
	}

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

	private UserSalt parseUserSaltFromResultSet(ResultSet resultSet) throws SQLException {
		logger.info("method parseItemFromResultSet(ResultSet resultSet) of ItemDaoImpl class is invoked");
		UserSalt userSalt = null;
		try {
			long id = resultSet.getLong("id");
			long user_id = resultSet.getLong("user_id");
			String salt = resultSet.getString("user_salt");
			userSalt = new UserSalt(id, user_id, salt);
			System.out.println(userSalt);
			logger.debug("Successfully got userSalt: {}", userSalt);
		} catch (SQLException e) {
			logger.error("Error while getting userSalt", e);
			e.printStackTrace();
		}
		return userSalt;
	}

}
