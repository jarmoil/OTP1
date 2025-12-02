package utils;

import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SessionManagerTest {

    @BeforeEach
    void setUp() {
        // Clear session before each test to ensure clean state
        SessionManager.clear();
    }

    // Just to get 100% coverage
    @Test
    void testConstructor() {
        SessionManager sessionManager = new SessionManager();
        assertNotNull(sessionManager);
    }

    // Test setting and getting current user
    @Test
    void testSetAndGetCurrentUser() {
        User user = new User(1, "testUser", "password123", "USER");

        SessionManager.setCurrentUser(user);
        User currentUser = SessionManager.getCurrentUser();

        assertNotNull(currentUser);
        assertEquals(1, currentUser.getId());
        assertEquals("testUser", currentUser.getName());
        assertEquals("password123", currentUser.getPassword());
        assertEquals("USER", currentUser.getRole());
        assertSame(user, currentUser);
    }

    // Test getting current user when none is set
    @Test
    void testGetCurrentUserWhenNull() {
        User currentUser = SessionManager.getCurrentUser();

        assertNull(currentUser);
    }

    // Test setting current user to null
    @Test
    void testSetCurrentUserNull() {
        SessionManager.setCurrentUser(null);
        User currentUser = SessionManager.getCurrentUser();

        assertNull(currentUser);
    }

    // Test clearing current user
    @Test
    void testClear() {
        User user = new User(1, "testUser", "password123", "USER");

        SessionManager.setCurrentUser(user);
        assertNotNull(SessionManager.getCurrentUser());

        SessionManager.clear();
        assertNull(SessionManager.getCurrentUser());
    }

    // Test overwriting current user
    @Test
    void testOverwriteCurrentUser() {
        User user1 = new User(1, "user1", "password1", "USER");
        User user2 = new User(2, "user2", "password2", "ADMIN");

        SessionManager.setCurrentUser(user1);
        assertEquals("user1", SessionManager.getCurrentUser().getName());

        SessionManager.setCurrentUser(user2);
        assertEquals("user2", SessionManager.getCurrentUser().getName());
        assertEquals(2, SessionManager.getCurrentUser().getId());
        assertEquals("ADMIN", SessionManager.getCurrentUser().getRole());
    }

    // Test static behavior across multiple calls
    @Test
    void testStaticBehavior() {
        User user = new User(1, "staticTest", "password123", "USER");

        SessionManager.setCurrentUser(user);

        // Verify the same instance is returned on multiple calls
        User retrieved1 = SessionManager.getCurrentUser();
        User retrieved2 = SessionManager.getCurrentUser();

        assertSame(retrieved1, retrieved2);
        assertSame(user, retrieved1);
    }
}
