package dao;

import exceptions.DataOperationException;
import models.User;

public interface IUserDao {
    // Find a user by username
    User findByUsername(String username) throws DataOperationException;

    // Create a new user with 'student' role
    boolean createUser(String username, String hashedPassword) throws DataOperationException;

    // Create a new user with teacher role
    boolean createTeacher(String username, String hashedPassword) throws DataOperationException;
}
