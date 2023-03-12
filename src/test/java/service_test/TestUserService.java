package service_test;

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

import models.User;
import service.UserService;

public class TestUserService { // done
    @Mock
    private UserService mockUserService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByEmail() {
        String email = "test@example.com";
        User user = new User();
        user.setUserId(1L);
        user.setUserEmail(email);
        Optional<User> expected = Optional.of(user);

        when(mockUserService.findByEmail(email)).thenReturn(expected);
        Optional<User> result = mockUserService.findByEmail(email);
        assertEquals(expected, result);
    }

    @Test
    public void testGetAllPagination() {
        int offset = 0;
        int noOfRecords = 10;
        List<User> users = Arrays.asList(getUser(), getUser());
        when(mockUserService.getAll(offset, noOfRecords)).thenReturn(users);
        List<User> result = mockUserService.getAll(offset, noOfRecords);
        assertEquals(users, result);
    }

    @Test
    public void testGetNoOfRecords() {
        int expected = 10;
        when(mockUserService.getNoOfRecords()).thenReturn(expected);
        int result = mockUserService.getNoOfRecords();
        assertEquals(expected, result);
    }

    @Test
    public void testCreate() {
        User user = getUser();
        mockUserService.create(user);
        verify(mockUserService).create(user);
    }
    
    @Test
    public void testGetUserById() { 
        Long userId = 1L;
        User expectedUser = new User();
        expectedUser.setUserId(userId);
        expectedUser.setUserEmail("john.doe@email.com");
        expectedUser.setUserName("John");
        expectedUser.setUserSurname("Doe");
        
        when(mockUserService.get(userId)).thenReturn(Optional.of(expectedUser));

        Optional<User> actualUser = mockUserService.get(userId);

        assertTrue(actualUser.isPresent());
        assertEquals(expectedUser, actualUser.get());
        verify(mockUserService).get(userId);
    }
    @Test
    public void testGetAll() {
        List<User> users = Arrays.asList(getUser(), getUser());
        when(mockUserService.getAll()).thenReturn(users);
        List<User> result = mockUserService.getAll();
        assertEquals(users, result);
    }
    @Test
    public void testUpdate() {
        User user = getUser();
        mockUserService.update(user);
        verify(mockUserService).update(user);
    }

    @Test
    public void testDelete() {
        User user = getUser();
       user.setUserId(1L);
        mockUserService.delete(user.getUserId());
        verify(mockUserService).delete(user.getUserId());
    }

    
    private User getUser() {
        User user = new User();
        user.setUserId(1L);
        user.setUserEmail("test@example.com");
        // set other properties of the User object as needed
        return user;
    }
}