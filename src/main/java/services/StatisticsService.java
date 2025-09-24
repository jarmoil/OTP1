package services;

import dao.FlashcardDao;
import dao.FlashcardSetDao;
import dao.StatisticsDao;
import models.Statistics;
import java.util.List;
import java.util.Map;

public class StatisticsService {
    private StatisticsDao statisticsDao = new StatisticsDao();
    private FlashcardDao flashcardDao = new FlashcardDao();
    private FlashcardSetDao flashcardSetDao = new FlashcardSetDao();

    // Process quiz results. update flashcard stats, set percentage, and user statistics
    public boolean processQuizResults(int userId, int setId, Map<Integer, Boolean> results) throws Exception {
        // Update individual flashcard statistics
        for (Map.Entry<Integer, Boolean> entry : results.entrySet()) {
            flashcardDao.updateFlashcardStats(entry.getKey(), entry.getValue());
        }

        // Update set's overall percentage
        flashcardSetDao.updateSetCorrectPercentage(setId);

        // Calculate and save user's score if it's a personal best
        int correctCount = results.values().stream().mapToInt(correct -> correct ? 1 : 0).sum();
        int userPercentage = Math.round((float) correctCount * 100 / results.size());

        Statistics previousStats = statisticsDao.getStatistics(userId, setId);
        if (previousStats == null || userPercentage > previousStats.getStats_correct_percentage()) {
            return statisticsDao.upsertStatistics(userId, setId, userPercentage);
        }
        return true;
    }

    // Retrieve all statistics for a specific user by user ID
    public List<Statistics> getStatisticsByUser(int userId) throws Exception {
        return statisticsDao.getStatisticsByUser(userId);
    }

    // Retrieve statistics for a specific user and flashcard set (single record)
    public Statistics getStatistics(int userId, int setId) throws Exception {
        return statisticsDao.getStatistics(userId, setId);
    }

    // Retrieve all statistics from the database for teacher analytics
    public List<Statistics> getAllStatistics() throws Exception {
        return statisticsDao.getAllStatistics();
    }
}
