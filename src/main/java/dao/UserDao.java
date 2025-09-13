package dao;

import database.ConnectDB;
import models.User;
import java.sql.*;

// DAO class for managing user accounts in the database
public class UserDao {
    // Find a user by username
    public User findByUsername(String username) throws Exception {
        String sql = "SELECT user_id, user_name, user_password, role FROM user_accounts WHERE user_name = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
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
        }
        return null;
    }

    // TODO: Implement user creation for teachers, only students possible now.
    //  Quick fix: manually hash passwords and add teachers in the DB for testing role logic.
    // Create a new user with 'student' role
    public boolean createUser(String username, String hashedPassword) throws Exception {
        String sql = "INSERT INTO user_accounts (user_name, user_password, role) VALUES (?, ?, 'student')";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            return stmt.executeUpdate() > 0;
        }
    }
}