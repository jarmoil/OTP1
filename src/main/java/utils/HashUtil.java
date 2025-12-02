package utils;

import org.mindrot.jbcrypt.BCrypt;

public class HashUtil {

    private HashUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
