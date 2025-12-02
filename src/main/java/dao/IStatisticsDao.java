package dao;

import exceptions.DataOperationException;
import models.Statistics;

import java.util.List;

public interface IStatisticsDao {
    // Create or update user statistics for a flashcard set
    boolean upsertStatistics(int userId, int setId, int correctPercentage) throws DataOperationException;

    // Get all statistics from the database
    List<Statistics> getAllStatistics() throws DataOperationException;

    // Get statistics for a specific user and set (one record)
    Statistics getStatistics(int userId, int setId) throws DataOperationException;

    // Get all statistics for a specific user (list of records)
    List<Statistics> getStatisticsByUser(int userId) throws DataOperationException;
}
