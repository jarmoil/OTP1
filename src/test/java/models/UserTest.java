package models;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void testUser() {
        User user = new User(
                1,
                "Test1",
                "Test1",
                "student");
        assertEquals(1, user.getId());
        assertEquals("Test1", user.getName());
        assertEquals("Test1", user.getPassword());
        assertEquals("student", user.getRole());
    }
}