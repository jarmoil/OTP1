package dao;

import database.ConnectDB;
import models.Flashcard;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlashcardDao {
    public List<Flashcard> getFlashcardsBySetId(int setId) throws Exception {
        String sql = "SELECT * FROM flashcards WHERE sets_id = ?";
        List<Flashcard> flashcards = new ArrayList<>();

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, setId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                flashcards.add(new Flashcard(
                        rs.getInt("flashcard_id"),
                        rs.getInt("sets_id"),
                        rs.getInt("times_answered"),
                        rs.getInt("times_correct"),
                        rs.getString("question"),
                        rs.getString("answer"),
                        rs.getString("choice_a"),
                        rs.getString("choice_b"),
                        rs.getString("choice_c")
                ));
            }
        }
        return flashcards;
    }

    public boolean createFlashcard(int setId, String question, String answer,
                                   String choiceA, String choiceB, String choiceC) throws Exception {
        String sql = "INSERT INTO flashcards (sets_id, times_answered, times_correct, question, answer, choice_a, choice_b, choice_c) " +
                "VALUES (?, 0, 0, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, setId);
            stmt.setString(2, question);
            stmt.setString(3, answer);
            stmt.setString(4, choiceA);
            stmt.setString(5, choiceB);
            stmt.setString(6, choiceC);
            return stmt.executeUpdate() > 0;
        }
    }
}
