package services;

import dao.UserDao;
import models.User;
import utils.HashUtil;
import org.junit.jupiter.api.*;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() throws Exception {
        userDao = mock(UserDao.class);
        userService = new UserService();

        java.lang.reflect.Field daoField = UserService.class.getDeclaredField("userDao");
        daoField.setAccessible(true);
        daoField.set(userService, userDao);
    }

    // Test successful login
    @Test
    void testLoginSuccess() throws Exception {
        String username = "testuser";
        String password = "testpass";
        String hashedPassword = HashUtil.hashPassword(password);
        User mockUser = new User(1, username, hashedPassword, "student");

        when(userDao.findByUsername(username)).thenReturn(mockUser);

        User result = userService.login(username, password);

        assertNotNull(result);
        assertEquals(username, result.getName());
        verify(userDao).findByUsername(username);
    }

    // Test login with wrong password
    @Test
    void testLoginWrongPassword() throws Exception {
        String username = "testuser";
        String correctPassword = "testpass";
        String wrongPassword = "wrongpass";
        String hashedPassword = HashUtil.hashPassword(correctPassword);
        User mockUser = new User(1, username, hashedPassword, "student");

        when(userDao.findByUsername(username)).thenReturn(mockUser);

        User result = userService.login(username, wrongPassword);

        assertNull(result);
        verify(userDao).findByUsername(username);
    }

    // Test login with non-existent user
    @Test
    void testLoginUserNotFound() throws Exception {
        String username = "nonexistent";
        String password = "testpass";

        when(userDao.findByUsername(username)).thenReturn(null);

        User result = userService.login(username, password);

        assertNull(result);
        verify(userDao).findByUsername(username);
    }

    // Test successful registration
    @Test
    void testRegisterSuccess() throws Exception {
        String username = "newuser";
        String password = "newpass";

        when(userDao.findByUsername(username)).thenReturn(null);
        when(userDao.createUser(eq(username), anyString())).thenReturn(true);

        boolean result = userService.register(username, password);

        assertTrue(result);
        verify(userDao).findByUsername(username);
        verify(userDao).createUser(eq(username), anyString());
    }

    // Test registration with existing username
    @Test
    void testRegisterUsernameExists() throws Exception {
        String username = "existinguser";
        String password = "testpass";
        User existingUser = new User(1, username, "hashedpass", "student");

        when(userDao.findByUsername(username)).thenReturn(existingUser);

        boolean result = userService.register(username, password);

        assertFalse(result);
        verify(userDao).findByUsername(username);
        verifyNoMoreInteractions(userDao);
    }

    // Test registration failure from DAO
    @Test
    void testRegisterDaoFailure() throws Exception {
        String username = "newuser";
        String password = "newpass";

        when(userDao.findByUsername(username)).thenReturn(null);
        when(userDao.createUser(eq(username), anyString())).thenReturn(false);

        boolean result = userService.register(username, password);

        assertFalse(result);
        verify(userDao).findByUsername(username);
        verify(userDao).createUser(eq(username), anyString());
    }

    // Test successful teacher creation
    @Test
    void testCreateTeacherSuccess() throws Exception {
        String username = "newteacher";
        String password = "teacherpass";

        when(userDao.findByUsername(username)).thenReturn(null);
        when(userDao.createTeacher(eq(username), anyString())).thenReturn(true);

        boolean result = userService.createTeacher(username, password);

        assertTrue(result);
        verify(userDao).findByUsername(username);
        verify(userDao).createTeacher(eq(username), anyString());
    }

    // Test teacher creation with existing username
    @Test
    void testCreateTeacherUsernameExists() throws Exception {
        String username = "existingteacher";
        String password = "teacherpass";
        User existingUser = new User(1, username, "hashedpass", "teacher");

        when(userDao.findByUsername(username)).thenReturn(existingUser);

        boolean result = userService.createTeacher(username, password);

        assertFalse(result);
        verify(userDao).findByUsername(username);
        verifyNoMoreInteractions(userDao);
    }

    // Test teacher creation failure from DAO
    @Test
    void testCreateTeacherDaoFailure() throws Exception {
        String username = "newteacher";
        String password = "teacherpass";

        when(userDao.findByUsername(username)).thenReturn(null);
        when(userDao.createTeacher(eq(username), anyString())).thenReturn(false);

        boolean result = userService.createTeacher(username, password);

        assertFalse(result);
        verify(userDao).findByUsername(username);
        verify(userDao).createTeacher(eq(username), anyString());
    }
}
