
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dao.UserDao;
import models.User;
import service.UserService;
import service.impl.UserServiceImpl;

public class UserServiceImplTest {

	@Mock
	private UserDao userDao;

	private UserService userService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		userService = new UserServiceImpl(userDao);
	}

	@Test
	public void testCreate() {
		User user = new User();
		when(userDao.create(any(User.class))).thenReturn(user);
		assertEquals(user, userService.create(user));
	}

	@Test
	public void testGet() {
		User user = new User();
		when(userDao.get(any(Long.class))).thenReturn(Optional.of(user));
		assertEquals(user, userService.get(1L));
	}

	@Test
	public void testGetAll() {
		List<User> users = new ArrayList<>();
		when(userDao.getAll()).thenReturn(users);
		assertEquals(users, userService.getAll());
	}

	@Test
	public void testUpdate() {
		User user = new User();
		when(userDao.update(any(User.class))).thenReturn(user);
		assertEquals(user, userService.update(user));
	}

	@Test
	public void testDelete() {
		when(userDao.delete(any(Long.class))).thenReturn(true);
		assertTrue(userService.delete(1L));
	}

	@Test
	public void testFindByEmail() {
		User user = new User();
		when(userDao.findByEmail(any(String.class))).thenReturn(Optional.of(user));
		assertEquals(Optional.of(user), userService.findByEmail("test@email.com"));
	}

	@Test
	public void testGetAllWithOffsetAndNoOfRecords() {
		List<User> users = new ArrayList<>();
		when(userDao.getAll(any(Integer.class), any(Integer.class))).thenReturn(users);
		assertEquals(users, userService.getAll(0, 10));
	}

	@Test
	    public void testGetNoOfRecords() {
	        when(userDao.getNoOfRecords()).thenReturn(10);
	        assertEquals(10, userService.getNoOfRecords());
	}    
}
