package services;

import dao.UserDao;
import models.User;
import utils.HashUtil;

// Service class for user-related operations
public class UserService {

    private UserDao userDao = new UserDao();

    // Authenticate user by username and password
    public User login(String username, String password) throws Exception {
        User user = userDao.findByUsername(username);
        if (user != null && HashUtil.checkPassword(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    // Register a new user with username and password
    public boolean register(String username, String password) throws Exception {
        // Check if username exists
        if (userDao.findByUsername(username) != null) {
            return false;
        }

        String hashedPassword = HashUtil.hashPassword(password);
        return userDao.createUser(username, hashedPassword);
    }
}
