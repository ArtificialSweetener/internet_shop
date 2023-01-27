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

public class UserDaoImpl implements UserDao {
	private int noOfRecords;

	public int getNoOfRecords() {
		return noOfRecords;
	}

	@Override
	public User create(User user) {
		String query = "INSERT INTO users (user_name, user_surname, user_email, user_password, user_type) VALUES (?, ?, ?, ?, ?)";
		try (Connection connection = ConnectionPool.getInstance().getConnection();
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
			}
			return user;
		} catch (SQLException e) {
			throw new DataProcessingException("Couldn't create " + user + ". ", e);
		}
	}

	@Override
	public Optional<User> get(Long id) {
		String query = "SELECT * FROM users WHERE id = ?";
		try (Connection connection = ConnectionPool.getInstance().getConnection();
				PreparedStatement getUserStatement = connection.prepareStatement(query)) {
			getUserStatement.setLong(1, id);
			ResultSet resultSet = getUserStatement.executeQuery();
			User user = null;
			if (resultSet.next() == false) {
				System.out.println("ResultSet in empty in Java");
			} else {
				do {
					user = parseUserFromResultSet(resultSet);
					System.out.println(user);
				} while (resultSet.next());
			}
			return Optional.ofNullable(user);
		} catch (SQLException e) {
			throw new DataProcessingException("Couldn't get user by id " + id, e);
		}
	}

	@Override
	public List<User> getAll(int offset, int noOfRecords) {
		String query = "select SQL_CALC_FOUND_ROWS * from users limit " + offset + ", " + noOfRecords;
		try (Connection connection = ConnectionPool.getInstance().getConnection();
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
			}
			
			return listUsers;
		} catch (SQLException e) {
			throw new DataProcessingException("Couldn't get a list of users" + "from users table. ", e);
		}
	}

	@Override
	public User update(User user) {
		String selectQuery = "UPDATE users SET user_name = ?, user_surname = ?, user_email = ? , user_password = ?, user_type =?, is_blocked =? "
				+ "WHERE id = ?";
		try (Connection connection = ConnectionPool.getInstance().getConnection();
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

			System.out.println("updated");
		} catch (SQLException e) {
			throw new DataProcessingException("Can't update user " + user, e);
		}
		return user;
	}

	@Override
	public boolean delete(Long id) {
		System.out.println("Delete method in dao is invoked");
		String selectQuery = "UPDATE users SET is_blocked = TRUE WHERE id = ? AND is_blocked = FALSE";
		try (Connection connection = ConnectionPool.getInstance().getConnection();
				PreparedStatement blockUserStatement = connection.prepareStatement(selectQuery)) {
			blockUserStatement.setLong(1, id);
			int isDone = blockUserStatement.executeUpdate();
			return isDone > 0;
		} catch (SQLException e) {
			throw new DataProcessingException("Can't delete product by id " + id, e);
		}
	}

	@Override
	public Optional<User> findByEmail(String email) {
		System.out.println("Method find by email invoked");
		String query = "SELECT * FROM users WHERE user_email = ? AND is_blocked = FALSE";
		try (Connection connection = ConnectionPool.getInstance().getConnection();
				PreparedStatement getUserStatement = connection.prepareStatement(query)) {
			getUserStatement.setString(1, email);
			ResultSet resultSet = getUserStatement.executeQuery();
			User user = null;
			if (resultSet.next() == false) {
				System.out.println("ResultSet in empty in Java");
			} else {
				do {
					user = parseUserFromResultSet(resultSet);
					System.out.println(user);
				} while (resultSet.next());
			}
			return Optional.ofNullable(user);
		} catch (SQLException e) {
			throw new DataProcessingException("Couldn't get user by email " + email, e);
		}
	}

	private User parseUserFromResultSet(ResultSet resultSet) throws SQLException {
		System.out.println("Method parseUserFromResultSet invoked");
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
		return user;
	}

	@Override
	public List<User> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
