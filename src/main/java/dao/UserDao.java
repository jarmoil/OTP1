package dao;

import database.ConnectDB;
import models.User;
import java.sql.*;

// DAO class for managing user accounts in the database
public class UserDao implements IUserDao {
    // Find a user by username
    @Override
    public User findByUsername(String username) throws Exception {
        String sql = "SELECT user_id, user_name, user_password, role FROM user_accounts WHERE user_name = ?";
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
        return null;
    }

    // Create a new user with 'student' role
    @Override
    public boolean createUser(String username, String hashedPassword) throws Exception {
        String sql = "INSERT INTO user_accounts (user_name, user_password, role) VALUES (?, ?, 'student')";
        Connection conn = ConnectDB.getConnection();
        boolean closeConn = conn != ConnectDB.gettestConn();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            return stmt.executeUpdate() > 0;
        }
        finally {
            if (closeConn) conn.close();
        }
    }

    // Create a new user with teacher role
    @Override
    public boolean createTeacher(String username, String hashedPassword) throws Exception {
        String sql = "INSERT INTO user_accounts (user_name, user_password, role) VALUES (?, ?, 'teacher')";
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
}