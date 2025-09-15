package dao;

import database.ConnectDB;
import models.FlashcardSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// DAO class for managing flashcard sets in the database
public class FlashcardSetDao {
    // TODO: Implement update and delete methods for flashcard sets

    // Retrieve all flashcard sets from the database
    public List<FlashcardSet> getAllSets() throws Exception {
        String sql = "SELECT * FROM sets";
        List<FlashcardSet> sets = new ArrayList<>();

        Connection conn = ConnectDB.getConnection();
        boolean closeConn = conn != ConnectDB.gettestConn();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                sets.add(new FlashcardSet(
                        rs.getInt("sets_id"),
                        rs.getInt("user_id"),
                        rs.getString("description"),
                        rs.getInt("sets_correct_percentage")
                ));
            }
        } finally {
            if (closeConn) conn.close();
        }
        return sets;
    }

    // Retrieve flashcard sets based on user role (student or teacher)
    public List<FlashcardSet> getSetsByRole(String role) throws Exception {
        String sql = "SELECT s.* FROM sets s " +
                "INNER JOIN user_accounts u ON s.user_id = u.user_id " +
                "WHERE u.role = ?";
        List<FlashcardSet> sets = new ArrayList<>();

        Connection conn = ConnectDB.getConnection();
        boolean closeConn = conn != ConnectDB.gettestConn();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                sets.add(new FlashcardSet(
                        rs.getInt("sets_id"),
                        rs.getInt("user_id"),
                        rs.getString("description"),
                        rs.getInt("sets_correct_percentage")
                ));
            }
        } finally {
            if (closeConn) conn.close();
        }
        return sets;
    }

    // Create a new flashcard set in the database
    public boolean createSet(int userId, String description) throws Exception {
        String sql = "INSERT INTO sets (user_id, description, sets_correct_percentage) VALUES (?, ?, 0)";

        Connection conn = ConnectDB.getConnection();
        boolean closeConn = conn != ConnectDB.gettestConn();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, description);
            return stmt.executeUpdate() > 0;
        } finally {
            if (closeConn) conn.close();
        }
    }
}
