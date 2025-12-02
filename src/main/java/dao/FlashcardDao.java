package dao;

import database.ConnectDB;
import exceptions.DataOperationException;
import models.Flashcard;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// DAO class for managing flashcards in the database
public class FlashcardDao implements IFlashcardDao {
    // Retrieve all flashcards for a given flashcard set ID
    @Override
    public List<Flashcard> getFlashcardsBySetId(int setId) throws DataOperationException {
        String sql = "SELECT * FROM flashcards WHERE sets_id = ?";

        // Store flashcards in a list
        List<Flashcard> flashcards = new ArrayList<>();

        try {
            Connection conn = ConnectDB.getConnection();
            boolean closeConn = conn != ConnectDB.gettestConn();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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
            } finally {
                if (closeConn) conn.close();
            }
        } catch (Exception e) {
            throw new DataOperationException("Failed to retrieve flashcards for set ID: " + setId, e);
        }

        return flashcards;
    }

    // Create a new flashcard in the database
    @Override
    public boolean createFlashcard(int setId, String question, String answer,
                                   String choiceA, String choiceB, String choiceC) throws DataOperationException {
        String sql = "INSERT INTO flashcards (sets_id, times_answered, times_correct, question, answer, choice_a, choice_b, choice_c) " +
                "VALUES (?, 0, 0, ?, ?, ?, ?, ?)";

        try {
            Connection conn = ConnectDB.getConnection();
            boolean closeConn = conn != ConnectDB.gettestConn();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, setId);
                stmt.setString(2, question);
                stmt.setString(3, answer);
                stmt.setString(4, choiceA);
                stmt.setString(5, choiceB);
                stmt.setString(6, choiceC);
                return stmt.executeUpdate() > 0;
            } finally {
                if (closeConn) conn.close();
            }
        } catch (Exception e) {
            throw new DataOperationException("Failed to create flashcard", e);
        }
    }

    // Update flashcard information in the database by its ID
    @Override
    public boolean updateFlashcard(int flashcardId, String question, String answer,
                                   String choiceA, String choiceB, String choiceC) throws DataOperationException {
        String sql = "UPDATE flashcards SET question = ?, answer = ?, choice_a = ?, choice_b = ?, choice_c = ? " +
                "WHERE flashcard_id = ?";

        try {
            Connection conn = ConnectDB.getConnection();
            boolean closeConn = conn != ConnectDB.gettestConn();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, question);
                stmt.setString(2, answer);
                stmt.setString(3, choiceA);
                stmt.setString(4, choiceB);
                stmt.setString(5, choiceC);
                stmt.setInt(6, flashcardId);
                return stmt.executeUpdate() > 0;
            } finally {
                if (closeConn) conn.close();
            }
        } catch (Exception e) {
            throw new DataOperationException("Failed to update flashcard ID: " + flashcardId, e);
        }
    }

    // Extracted method to handle flashcard deletion and stats update queries
    static boolean queryFlashcard(int flashcardId, String sql) throws SQLException {
        Connection conn = ConnectDB.getConnection();
        boolean closeConn = conn != ConnectDB.gettestConn();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, flashcardId);
            return stmt.executeUpdate() > 0;
        } finally {
            if (closeConn) conn.close();
        }
    }

    // Delete a flashcard from the database by its ID
    @Override
    public boolean deleteFlashcard(int flashcardId) throws DataOperationException {
        String sql = "DELETE FROM flashcards WHERE flashcard_id = ?";

        try {
            return queryFlashcard(flashcardId, sql);
        } catch (Exception e) {
            throw new DataOperationException("Failed to delete flashcard ID: " + flashcardId, e);
        }
    }

    // Update flashcard statistics after an answer attempt
    @Override
    public boolean updateFlashcardStats(int flashcardId, boolean isCorrect) throws DataOperationException {
        String sql = "UPDATE flashcards SET times_answered = times_answered + 1" +
                (isCorrect ? ", times_correct = times_correct + 1" : "") +
                " WHERE flashcard_id = ?";

        try {
            return queryFlashcard(flashcardId, sql);
        } catch (Exception e) {
            throw new DataOperationException("Failed to update flashcard stats for ID: " + flashcardId, e);
        }
    }
}