package services;

import dao.IUserDao;
import exceptions.DataOperationException;
import models.User;
import utils.HashUtil;

// Service class for user-related operations
public class UserService {

    private final IUserDao userDao;

    public UserService(IUserDao userDao) {
        this.userDao = userDao;
    }

    // Authenticate user by username and password
    public User login(String username, String password) throws DataOperationException {
        User user = userDao.findByUsername(username);
        if (user != null && HashUtil.checkPassword(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    // Register a new user with username and password
    public boolean register(String username, String password) throws DataOperationException {
        if (userDao.findByUsername(username) != null) {
            return false;
        }

        String hashedPassword = HashUtil.hashPassword(password);
        return userDao.createUser(username, hashedPassword);
    }

    // Create a new user with teacher role. Only logged in teachers can create other teachers.
    public boolean createTeacher(String username, String password) throws DataOperationException {
        if (userDao.findByUsername(username) != null) {
            return false;
        }

        String hashedPassword = HashUtil.hashPassword(password);
        return userDao.createTeacher(username, hashedPassword);
    }
}
