package utils;

import models.User;

public class SessionManager {
    private static User currentUser;

    private SessionManager() {
        throw new UnsupportedOperationException("Utility class");
    }


    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void clear() {
        currentUser = null;
    }
}
