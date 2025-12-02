package dao;

import database.ConnectDB;
import exceptions.DataOperationException;
import models.User;
import java.sql.*;

// DAO class for managing user accounts in the database
public class UserDao implements IUserDao {
    // Find a user by username
    @Override
    public User findByUsername(String username) throws DataOperationException {
        String sql = "SELECT user_id, user_name, user_password, role FROM user_accounts WHERE user_name = ?";
        try {
            Connection conn = ConnectDB.getConnection();
            boolean closeConn = conn != ConnectDB.gettestConn();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("user_name"),
                            rs.getString("user_password"),
                            rs.getString("role")
                    );
                }
            } finally {
                if (closeConn) conn.close();
            }

        } catch (Exception e) {
            throw new DataOperationException("Failed to find user by username: " + username, e);
        }
        return null;
    }

    // Extracted method to handle user creation queries
    private boolean queryUser(String username, String hashedPassword, String sql) throws SQLException {
        Connection conn = ConnectDB.getConnection();
        boolean closeConn = conn != ConnectDB.gettestConn();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            return stmt.executeUpdate() > 0;
        } finally {
            if (closeConn) conn.close();
        }
    }

    // Create a new user with 'student' role
    @Override
    public boolean createUser(String username, String hashedPassword) throws DataOperationException {
        try {
            String sql = "INSERT INTO user_accounts (user_name, user_password, role) VALUES (?, ?, 'student')";
            return queryUser(username, hashedPassword, sql);

        } catch (Exception e) {
            throw new DataOperationException("Failed to create user: " + username, e);
        }
    }

    // Create a new user with teacher role
    @Override
    public boolean createTeacher(String username, String hashedPassword) throws DataOperationException {
        String sql = "INSERT INTO user_accounts (user_name, user_password, role) VALUES (?, ?, 'teacher')";

        try {
            return queryUser(username, hashedPassword, sql);

        } catch (Exception e) {
            throw new DataOperationException("Failed to create teacher: " + username, e);
        }
    }
}