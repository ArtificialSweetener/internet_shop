package dao_test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import dao.UserSaltDao;
import models.UserSalt;

@RunWith(MockitoJUnitRunner.class)
public class TestUserSaltDao {
	 	@Mock
	    private UserSaltDao userSaltDao;

	    @BeforeEach
	    public void setup() {
	        MockitoAnnotations.openMocks(this);
	    }

	    @Test
	    public void testCreateUserSalt() {
	        // given
	        UserSalt userSalt = new UserSalt();
	        userSalt.setUserId(1L);
	        userSalt.setSalt("abcd");

	        // when
	        when(userSaltDao.create(userSalt)).thenReturn(userSalt);

	        // then
	        assertEquals(userSalt, userSaltDao.create(userSalt));
	    }

	    @Test
	    public void testGetUserSaltById() {
	        // given
	        Long userId = 1L;
	        UserSalt userSalt = new UserSalt();
	        userSalt.setUserId(userId);
	        userSalt.setSalt("abcd");

	        // when
	        when(userSaltDao.get(userId)).thenReturn(Optional.of(userSalt));

	        // then
	        assertTrue(userSaltDao.get(userId).isPresent());
	        assertEquals(userSalt, userSaltDao.get(userId).get());
	    }

	    @Test
	    public void testGetAllUserSalts() {
	        // given
	        List<UserSalt> userSalts = new ArrayList<>();
	        UserSalt userSalt1 = new UserSalt();
	        userSalt1.setUserId(1L);
	        userSalt1.setSalt("abcd");
	        UserSalt userSalt2 = new UserSalt();
	        userSalt2.setUserId(2L);
	        userSalt2.setSalt("efgh");
	        userSalts.add(userSalt1);
	        userSalts.add(userSalt2);

	        // when
	        when(userSaltDao.getAll()).thenReturn(userSalts);

	        // then
	        assertEquals(userSalts.size(), userSaltDao.getAll().size());
	        assertTrue(userSaltDao.getAll().contains(userSalt1));
	        assertTrue(userSaltDao.getAll().contains(userSalt2));
	    }

	    @Test
	    public void testUpdateUserSalt() {
	        // given
	        Long userId = 1L;
	        UserSalt userSalt = new UserSalt();
	        userSalt.setUserId(userId);
	        userSalt.setSalt("abcd");

	        // when
	        when(userSaltDao.update(userSalt)).thenReturn(userSalt);

	        // then
	        assertEquals(userSalt, userSaltDao.update(userSalt));
	    }

	    @Test
	    public void testDeleteUserSaltById() {
	        // given
	        Long userId = 1L;

	        // when
	        when(userSaltDao.delete(userId)).thenReturn(true);

	        // then
	        assertTrue(userSaltDao.delete(userId));
	    }
}
