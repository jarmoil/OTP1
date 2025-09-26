package dao;

import models.Statistics;

import java.util.List;

public interface IStatisticsDao {
    // Create or update user statistics for a flashcard set
    boolean upsertStatistics(int userId, int setId, int correctPercentage) throws Exception;

    // Get all statistics from the database
    List<Statistics> getAllStatistics() throws Exception;

    // Get statistics for a specific user and set (one record)
    Statistics getStatistics(int userId, int setId) throws Exception;

    // Get all statistics for a specific user (list of records)
    List<Statistics> getStatisticsByUser(int userId) throws Exception;
}
