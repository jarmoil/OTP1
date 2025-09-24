package dao;

import database.ConnectDB;
import models.Statistics;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatisticsDao {

    // Create or update user statistics for a flashcard set
    public boolean upsertStatistics(int userId, int setId, int correctPercentage) throws Exception {
        String checkSql = "SELECT stats_id FROM statistics WHERE user_id = ? AND sets_id = ?";
        String insertSql = "INSERT INTO statistics (user_id, sets_id, stats_correct_percentage) VALUES (?, ?, ?)";
        String updateSql = "UPDATE statistics SET stats_correct_percentage = ? WHERE user_id = ? AND sets_id = ?";

        Connection conn = ConnectDB.getConnection();
        boolean closeConn = conn != ConnectDB.gettestConn();

        try {
            // Check if record exists
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, userId);
                checkStmt.setInt(2, setId);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    // Update existing record
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setInt(1, correctPercentage);
                        updateStmt.setInt(2, userId);
                        updateStmt.setInt(3, setId);
                        return updateStmt.executeUpdate() > 0;
                    }
                } else {
                    // Insert new record
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                        insertStmt.setInt(1, userId);
                        insertStmt.setInt(2, setId);
                        insertStmt.setInt(3, correctPercentage);
                        return insertStmt.executeUpdate() > 0;
                    }
                }
            }
        } finally {
            if (closeConn) conn.close();
        }
    }

    // Get all statistics from the database
    public List<Statistics> getAllStatistics() throws Exception {
        String sql = "SELECT * FROM statistics";
        List<Statistics> stats = new ArrayList<>();

        Connection conn = ConnectDB.getConnection();
        boolean closeConn = conn != ConnectDB.gettestConn();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                stats.add(new Statistics(
                        rs.getInt("stats_id"),
                        rs.getInt("user_id"),
                        rs.getInt("sets_id"),
                        rs.getInt("stats_correct_percentage")
                ));
            }
        } finally {
            if (closeConn) conn.close();
        }

        return stats;
    }

    // Get statistics for a specific user and set (one record)
    public Statistics getStatistics(int userId, int setId) throws Exception {
        String sql = "SELECT * FROM statistics WHERE user_id = ? AND sets_id = ?";

        Connection conn = ConnectDB.getConnection();
        boolean closeConn = conn != ConnectDB.gettestConn();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, setId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Statistics(
                        rs.getInt("stats_id"),
                        rs.getInt("user_id"),
                        rs.getInt("sets_id"),
                        rs.getInt("stats_correct_percentage")
                );
            }
            return null;
        } finally {
            if (closeConn) conn.close();
        }
    }

    // Get all statistics for a specific user (list of records)
    public List<Statistics> getStatisticsByUser(int userId) throws Exception {
        String sql = "SELECT * FROM statistics WHERE user_id = ?";
        List<Statistics> stats = new ArrayList<>();

        Connection conn = ConnectDB.getConnection();
        boolean closeConn = conn != ConnectDB.gettestConn();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                stats.add(new Statistics(
                        rs.getInt("stats_id"),
                        rs.getInt("user_id"),
                        rs.getInt("sets_id"),
                        rs.getInt("stats_correct_percentage")
                ));
            }
        } finally {
            if (closeConn) conn.close();
        }

        return stats;
    }
}
