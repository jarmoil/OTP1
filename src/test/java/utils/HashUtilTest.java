package utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HashUtilTest {

    // Test successful password hashing
    @Test
    void testHashPassword() {
        String password = "testPassword123";

        String hashedPassword = HashUtil.hashPassword(password);

        assertNotNull(hashedPassword);
        assertNotEquals(password, hashedPassword);
        assertTrue(hashedPassword.startsWith("$2a$12$"));
    }

    // Test that same password produces different hashes (due to salt)
    @Test
    void testHashPasswordDifferentSalts() {
        String password = "testPassword123";

        String hash1 = HashUtil.hashPassword(password);
        String hash2 = HashUtil.hashPassword(password);

        assertNotEquals(hash1, hash2);
    }

    // Test password hashing with empty string
    @Test
    void testHashPasswordEmpty() {
        String password = "";

        String hashedPassword = HashUtil.hashPassword(password);

        assertNotNull(hashedPassword);
        assertTrue(hashedPassword.startsWith("$2a$12$"));
    }

    // Test successful password verification
    @Test
    void testCheckPasswordSuccess() {
        String password = "testPassword123";
        String hashedPassword = HashUtil.hashPassword(password);

        boolean result = HashUtil.checkPassword(password, hashedPassword);

        assertTrue(result);
    }

    // Test password verification with wrong password
    @Test
    void testCheckPasswordWrongPassword() {
        String password = "testPassword123";
        String wrongPassword = "wrongPassword456";
        String hashedPassword = HashUtil.hashPassword(password);

        boolean result = HashUtil.checkPassword(wrongPassword, hashedPassword);

        assertFalse(result);
    }

    // Test password verification with empty password
    @Test
    void testCheckPasswordEmptyPlainPassword() {
        String password = "testPassword123";
        String hashedPassword = HashUtil.hashPassword(password);

        boolean result = HashUtil.checkPassword("", hashedPassword);

        assertFalse(result);
    }

    // Test password verification with invalid hash format
    @Test
    void testCheckPasswordInvalidHash() {
        String password = "testPassword123";
        String invalidHash = "invalidhashformat";

        assertThrows(IllegalArgumentException.class, () -> {
            HashUtil.checkPassword(password, invalidHash);
        });
    }
}
