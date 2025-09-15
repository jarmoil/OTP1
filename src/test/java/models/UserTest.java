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
        assertEquals(user.getId(), 1);
        assertEquals(user.getName(), "Test1");
        assertEquals(user.getPassword(), "Test1");
        assertEquals(user.getRole(), "student");
    }
}