package services;

import dao.IStatisticsDao;
import exceptions.DataOperationException;
import models.Statistics;
import java.util.List;

public class StatisticsService {
    private final IStatisticsDao statisticsDao;

    public StatisticsService(IStatisticsDao statisticsDao) {
        this.statisticsDao = statisticsDao;
    }

    // Process quiz results by calculating user score and saving if it's a personal best
    public boolean processQuizResults(int userId, int setId, int correctCount, int totalQuestions) throws DataOperationException {
        int userPercentage = Math.round((float) correctCount * 100 / totalQuestions);

        Statistics previousStats = statisticsDao.getStatistics(userId, setId);
        if (previousStats == null || userPercentage > previousStats.getStatsCorrectPercentage()) {
            return statisticsDao.upsertStatistics(userId, setId, userPercentage);
        }
        return true;
    }

    public List<Statistics> getStatisticsByUser(int userId) throws DataOperationException {
        return statisticsDao.getStatisticsByUser(userId);
    }

    public Statistics getStatistics(int userId, int setId) throws DataOperationException {
        return statisticsDao.getStatistics(userId, setId);
    }

    public List<Statistics> getAllStatistics() throws DataOperationException {
        return statisticsDao.getAllStatistics();
    }
}
