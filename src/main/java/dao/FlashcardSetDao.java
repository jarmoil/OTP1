package dao;

import database.ConnectDB;
import exceptions.DataOperationException;
import models.FlashcardSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// DAO class for managing flashcard sets in the database
public class FlashcardSetDao implements IFlashcardSetDao {

    private static final String COL_SETS_ID = "sets_id";
    private static final String COL_USER_ID = "user_id";
    private static final String COL_LOCALE = "locale";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_SETS_CORRECT_PERCENTAGE = "sets_correct_percentage";

    // Retrieve all flashcard sets from the database
    @Override
    public List<FlashcardSet> getAllSets() throws DataOperationException {
        String sql = "SELECT * FROM sets";
        List<FlashcardSet> sets = new ArrayList<>();

        try {
            Connection conn = ConnectDB.getConnection();
            boolean closeConn = conn != ConnectDB.gettestConn();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    sets.add(new FlashcardSet(
                            rs.getInt(COL_SETS_ID),
                            rs.getInt(COL_USER_ID),
                            rs.getString(COL_LOCALE),
                            rs.getString(COL_DESCRIPTION),
                            rs.getInt(COL_SETS_CORRECT_PERCENTAGE)
                    ));
                }
            } finally {
                if (closeConn) conn.close();
            }

        } catch (Exception e) {
            throw new DataOperationException("Failed to retrieve all flashcard sets", e);
        }
        return sets;
    }

    // Retrieve flashcard sets based on user role (student or teacher)
    @Override
    public List<FlashcardSet> getSetsByRoleAndLocale(String role, String locale) throws DataOperationException {
        String sql = "SELECT s.* FROM sets s " +
                "INNER JOIN user_accounts u ON s.user_id = u.user_id " +
                "WHERE s.locale = ? AND u.role = ?";
        List<FlashcardSet> sets = new ArrayList<>();

        try {
        Connection conn = ConnectDB.getConnection();
        boolean closeConn = conn != ConnectDB.gettestConn();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, locale);
            stmt.setString(2, role);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                sets.add(new FlashcardSet(
                        rs.getInt(COL_SETS_ID),
                        rs.getInt(COL_USER_ID),
                        rs.getString(COL_LOCALE),
                        rs.getString(COL_DESCRIPTION),
                        rs.getInt(COL_SETS_CORRECT_PERCENTAGE)
                ));
            }
        } finally {
            if (closeConn) conn.close();
        }

        } catch (Exception e) {
            throw new DataOperationException("Failed to retrieve flashcard sets for role: " + role + " and locale: " + locale, e);
        }
        return sets;
    }

    // Retrieve a flashcard set by its ID
    @Override
    public FlashcardSet getSetById(int setId) throws DataOperationException {
        String sql = "SELECT * FROM sets WHERE sets_id = ?";

        try {
            Connection conn = ConnectDB.getConnection();
            boolean closeConn = conn != ConnectDB.gettestConn();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, setId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return new FlashcardSet(
                            rs.getInt(COL_SETS_ID),
                            rs.getInt(COL_USER_ID),
                            rs.getString(COL_LOCALE),
                            rs.getString(COL_DESCRIPTION),
                            rs.getInt(COL_SETS_CORRECT_PERCENTAGE)
                    );
                }
                return null;
            } finally {
                if (closeConn) conn.close();
            }

        } catch (Exception e) {
            throw new DataOperationException("Failed to retrieve flashcard set with ID: " + setId, e);
        }
    }


    // Create a new flashcard set in the database
    @Override
    public boolean createSet(int userId, String description, String locale) throws DataOperationException {
        String sql = "INSERT INTO sets (user_id, description, locale, sets_correct_percentage) VALUES (?, ?, ?, 0)";

        try {
            Connection conn = ConnectDB.getConnection();
            boolean closeConn = conn != ConnectDB.gettestConn();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                stmt.setString(2, description);
                stmt.setString(3, locale);
                return stmt.executeUpdate() > 0;
            } finally {
                if (closeConn) conn.close();
            }

        } catch (Exception e) {
            throw new DataOperationException("Failed to create flashcard set for user ID: " + userId, e);
        }
    }

    // Update flashcard set information in the database with the given ID
    @Override
    public boolean updateSet(int setId, String description) throws DataOperationException {
        String sql = "UPDATE sets SET description = ? WHERE sets_id = ?";

        try {
            Connection conn = ConnectDB.getConnection();
            boolean closeConn = conn != ConnectDB.gettestConn();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, description);
                stmt.setInt(2, setId);
                return stmt.executeUpdate() > 0;
            } finally {
                if (closeConn) conn.close();
            }

        } catch (Exception e) {
            throw new DataOperationException("Failed to update flashcard set with ID: " + setId, e);
        }
    }

    // Delete flashcard set from the database with the given ID
    @Override
    public boolean deleteSet(int setId) throws DataOperationException {
        String sql = "DELETE FROM sets WHERE sets_id = ?";

        try {
            Connection conn = ConnectDB.getConnection();
            boolean closeConn = conn != ConnectDB.gettestConn();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, setId);
                return stmt.executeUpdate() > 0;
            } finally {
                if (closeConn) conn.close();
            }

        } catch (Exception e) {
            throw new DataOperationException("Failed to delete flashcard set with ID: " + setId, e);
        }
    }

    // Update set's correct percentage based on individual flashcard statistics
    @Override
    public boolean updateSetCorrectPercentage(int setId) throws DataOperationException {
        // Calculate percentage as (total correct / total answered) * 100
        String sql = "UPDATE sets SET sets_correct_percentage = " +
                "(SELECT CASE " +
                "   WHEN SUM(times_answered) = 0 THEN 0 " +
                "   ELSE ROUND((SUM(times_correct) * 100.0) / SUM(times_answered)) " +
                "END " +
                "FROM flashcards WHERE sets_id = ?) " +
                "WHERE sets_id = ?";

        try {
            Connection conn = ConnectDB.getConnection();
            boolean closeConn = conn != ConnectDB.gettestConn();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, setId);
                stmt.setInt(2, setId);
                return stmt.executeUpdate() > 0;
            } finally {
                if (closeConn) conn.close();
            }

        } catch (Exception e) {
            throw new DataOperationException("Failed to update correct percentage for flashcard set ID: " + setId, e);
        }
    }

}
