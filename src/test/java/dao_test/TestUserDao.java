package dao_test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import dao.UserDao;
import models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TestUserDao {
	@Mock
	UserDao userDao;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testCreate() {
		User user = new User("Test", "User", "test@example.com", "password", "user");
		when(userDao.create(user)).thenReturn(user);
		User createdUser = userDao.create(user);
		assertNotNull(createdUser.getUserId());
		assertEquals(user.getUserName(), createdUser.getUserName());
		assertEquals(user.getUserSurname(), createdUser.getUserSurname());
		assertEquals(user.getUserEmail(), createdUser.getUserEmail());
		assertEquals(user.getUserPassword(), createdUser.getUserPassword());
		assertEquals(user.getUserType(), createdUser.getUserType());
	}

	@Test
	public void testGet() {
		User user = new User("Test", "User", "test@example.com", "password", "user");
		when(userDao.get(user.getUserId())).thenReturn(Optional.of(user));
		Optional<User> foundUser = userDao.get(user.getUserId());

		assertTrue(foundUser.isPresent());

		User retrievedUser = foundUser.get();
		assertEquals(user.getUserId(), retrievedUser.getUserId());
		assertEquals(user.getUserName(), retrievedUser.getUserName());
		assertEquals(user.getUserSurname(), retrievedUser.getUserSurname());
		assertEquals(user.getUserEmail(), retrievedUser.getUserEmail());
		assertEquals(user.getUserPassword(), retrievedUser.getUserPassword());
		assertEquals(user.getUserType(), retrievedUser.getUserType());
	}

	@Test
	public void testUpdate() {
		User user = new User("Test", "User", "test@example.com", "password", "user");

		when(userDao.create(user)).thenReturn(user);

		User createdUser = userDao.create(user);

		createdUser.setUserName("Updated Test");
		createdUser.setUserSurname("Updated User");
		createdUser.setUserEmail("updated@example.com");
		createdUser.setUserPassword("updatedpassword");
		createdUser.setUserType("admin");

		when(userDao.update(createdUser)).thenReturn(createdUser);

		User updatedUser = userDao.update(createdUser);
		assertEquals(createdUser.getUserId(), updatedUser.getUserId());
		assertEquals(createdUser.getUserName(), updatedUser.getUserName());
		assertEquals(createdUser.getUserSurname(), updatedUser.getUserSurname());
		assertEquals(createdUser.getUserEmail(), updatedUser.getUserEmail());
		assertEquals(createdUser.getUserPassword(), updatedUser.getUserPassword());
		assertEquals(createdUser.getUserType(), updatedUser.getUserType());
	}

	@Test
	public void testDelete() {

		User user = new User("Test", "User", "test@example.com", "password", "user");

		when(userDao.create(user)).thenReturn(user);

		userDao.create(user);

		when(userDao.get(user.getUserId())).thenReturn(Optional.of(user));

		Optional<User> retrievedUser = userDao.get(user.getUserId());

		assertNotNull(retrievedUser.get());

		when(userDao.delete(user.getUserId())).thenReturn(true);

		boolean isDeleted = userDao.delete(user.getUserId());

		assertTrue(isDeleted);
		// Try to retrieve the user again and check that it doesn't exist
		when(userDao.get(user.getUserId())).thenReturn(Optional.empty());

		retrievedUser = userDao.get(user.getUserId());
		assertTrue(retrievedUser.isEmpty());
	}

	@Test
	public void testFindByEmail() {
		User user = new User("test", "user", "test@example.com", "password", "regular");
		when(userDao.findByEmail(user.getUserEmail())).thenReturn(Optional.of(user));

		Optional<User> result = userDao.findByEmail(user.getUserEmail());
		assertTrue(result.isPresent());
		assertEquals(user, result.get());
	}

	@Test
	public void testGetAll() {
		List<User> userList = new ArrayList<>();
		userList.add(new User("test1", "user1", "test1@example.com", "password", "regular"));
		userList.add(new User("test2", "user2", "test2@example.com", "password", "regular"));
		when(userDao.getAll(0, 2)).thenReturn(userList);

		List<User> result = userDao.getAll(0, 2);
		assertEquals(userList, result);
		assertEquals(userList.size(), result.size());
	}

	@Test
	public void testGetNoOfRecords() {
		when(userDao.getNoOfRecords()).thenReturn(10);

		int result = userDao.getNoOfRecords();
		assertEquals(10, result);
	}
}