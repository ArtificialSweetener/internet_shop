package service_test.impl_test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dao.UserDao;
import models.User;
import service.UserService;
import service.impl.UserServiceImpl;

public class TestUserServiceImpl { //done

	@Mock
	private UserDao mockUserDao;

	private UserService userService;

	@SuppressWarnings("deprecation")
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		userService = new UserServiceImpl(mockUserDao);
	}

	@Test
	public void testCreate() {
		User user = getUser();
		when(mockUserDao.create(user)).thenReturn(user);
		User result = userService.create(user);
		verify(mockUserDao).create(user);
		assertEquals(user, result);
	}

	@Test
	public void testGet() {
		User user = getUser();
		when(mockUserDao.get(user.getUserId())).thenReturn(Optional.of(user));
		Optional<User> result = userService.get(user.getUserId());
		verify(mockUserDao).get(user.getUserId());
		assertEquals(user, result.get());
	}

	@Test
	public void testGetAll() {
		List<User> users = Arrays.asList(getUser(), getUser());
		when(mockUserDao.getAll()).thenReturn(users);
		List<User> result = userService.getAll();
		verify(mockUserDao).getAll();
		assertEquals(users, result);
	}

	@Test
	public void testUpdate() {
		User user = getUser();
		when(mockUserDao.update(user)).thenReturn(user);
		User result = userService.update(user);
		verify(mockUserDao).update(user);
		assertEquals(user, result);
	}

	@Test
	public void testDelete() {
		User user = getUser();
		when(mockUserDao.delete(user.getUserId())).thenReturn(true);
		boolean result = userService.delete(user.getUserId());
		verify(mockUserDao).delete(user.getUserId());
		assertTrue(result);
	}

	@Test
	public void testFindByEmail() {
		String email = "test@example.com";
		User user = getUser();
		user.setUserEmail(email);
		Optional<User> expected = Optional.of(user);
		when(mockUserDao.findByEmail(email)).thenReturn(expected);
		UserService userServiceImpl = new UserServiceImpl(mockUserDao);
		Optional<User> result = userServiceImpl.findByEmail(email);

		assertEquals(expected, result);
		verify(mockUserDao).findByEmail(email);
	}

	@Test
    public void testGetAllPagination() {
        User user1 = new User();
        User user2 = new User();
        
        user1.setUserId(1L);
        user1.setUserName("John");
        user1.setUserSurname("Doe");
        user1.setUserEmail("john.doe@example.com");
        
        user2.setUserId(2L);
        user2.setUserName("Jane");
        user2.setUserSurname("Doe");
        user2.setUserEmail("jane.doe@example.com");
        
        List<User> expectedUsers = Arrays.asList(user1, user2);

        when(mockUserDao.getAll(0, 2)).thenReturn(expectedUsers);

        List<User> actualUsers = userService.getAll(0, 2);

        assertEquals(expectedUsers, actualUsers);
    }
	@Test
	public void testGetNoOfRecords() {
		int expected = 10;
		when(mockUserDao.getNoOfRecords()).thenReturn(expected);

		UserService userServiceImpl = new UserServiceImpl(mockUserDao);
		int result = userServiceImpl.getNoOfRecords();

		assertEquals(expected, result);
		verify(mockUserDao).getNoOfRecords();
	}

	private User getUser() {
		User user = new User();
		user.setUserId(1L);
		user.setUserEmail("test@example.com");
		// set other properties of the User object as needed
		return user;
	}
}
