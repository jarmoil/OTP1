package dao;

import database.ConnectDB;
import models.FlashcardSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlashcardSetDao {
    public List<FlashcardSet> getAllSets() throws Exception {
        String sql = "SELECT * FROM sets";
        List<FlashcardSet> sets = new ArrayList<>();

        try (Connection conn = ConnectDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                sets.add(new FlashcardSet(
                        rs.getInt("sets_id"),
                        rs.getInt("user_id"),
                        rs.getString("description"),
                        rs.getInt("sets_correct_percentage")
                ));
            }
        }
        return sets;
    }

    public boolean createSet(int userId, String description) throws Exception {
        String sql = "INSERT INTO sets (user_id, description, sets_correct_percentage) VALUES (?, ?, 0)";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, description);
            return stmt.executeUpdate() > 0;
        }
    }
}
