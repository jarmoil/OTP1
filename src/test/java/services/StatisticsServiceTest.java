package services;

import dao.IStatisticsDao;
import models.Statistics;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatisticsServiceTest {

    @Mock
    private IStatisticsDao statisticsDao;

    @InjectMocks
    private StatisticsService statisticsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        statisticsService = new StatisticsService(statisticsDao);
    }

    // Test processQuizResults with new record (no previous stats)
    @Test
    void testProcessQuizResultsNewRecord() throws Exception {
        int userId = 1, setId = 1, correctCount = 8, totalQuestions = 10;
        when(statisticsDao.getStatistics(userId, setId)).thenReturn(null);
        when(statisticsDao.upsertStatistics(userId, setId, 80)).thenReturn(true);

        boolean result = statisticsService.processQuizResults(userId, setId, correctCount, totalQuestions);

        assertTrue(result);
        verify(statisticsDao).getStatistics(userId, setId);
        verify(statisticsDao).upsertStatistics(userId, setId, 80);
    }

    // Test processQuizResults with better score (should update)
    @Test
    void testProcessQuizResultsBetterScore() throws Exception {
        int userId = 1, setId = 1, correctCount = 9, totalQuestions = 10;
        Statistics previousStats = new Statistics(1, 1, 1, 70);
        when(statisticsDao.getStatistics(userId, setId)).thenReturn(previousStats);
        when(statisticsDao.upsertStatistics(userId, setId, 90)).thenReturn(true);

        boolean result = statisticsService.processQuizResults(userId, setId, correctCount, totalQuestions);

        assertTrue(result);
        verify(statisticsDao).getStatistics(userId, setId);
        verify(statisticsDao).upsertStatistics(userId, setId, 90);
    }

    // Test processQuizResults with worse score (should not update)
    @Test
    void testProcessQuizResultsWorseScore() throws Exception {
        int userId = 1, setId = 1, correctCount = 6, totalQuestions = 10;
        Statistics previousStats = new Statistics(1, 1, 1, 80);
        when(statisticsDao.getStatistics(userId, setId)).thenReturn(previousStats);

        boolean result = statisticsService.processQuizResults(userId, setId, correctCount, totalQuestions);

        assertTrue(result);
        verify(statisticsDao).getStatistics(userId, setId);
        verify(statisticsDao, never()).upsertStatistics(anyInt(), anyInt(), anyInt());
    }

    // Test processQuizResults with equal score (should not update)
    @Test
    void testProcessQuizResultsEqualScore() throws Exception {
        int userId = 1, setId = 1, correctCount = 8, totalQuestions = 10;
        Statistics previousStats = new Statistics(1, 1, 1, 80);
        when(statisticsDao.getStatistics(userId, setId)).thenReturn(previousStats);

        boolean result = statisticsService.processQuizResults(userId, setId, correctCount, totalQuestions);

        assertTrue(result);
        verify(statisticsDao).getStatistics(userId, setId);
        verify(statisticsDao, never()).upsertStatistics(anyInt(), anyInt(), anyInt());
    }

    // Test processQuizResults with upsert failure
    @Test
    void testProcessQuizResultsUpsertFailure() throws Exception {
        int userId = 1, setId = 1, correctCount = 8, totalQuestions = 10;
        when(statisticsDao.getStatistics(userId, setId)).thenReturn(null);
        when(statisticsDao.upsertStatistics(userId, setId, 80)).thenReturn(false);

        boolean result = statisticsService.processQuizResults(userId, setId, correctCount, totalQuestions);

        assertFalse(result);
        verify(statisticsDao).getStatistics(userId, setId);
        verify(statisticsDao).upsertStatistics(userId, setId, 80);
    }

    // Test processQuizResults with rounding (7/10 = 70%)
    @Test
    void testProcessQuizResultsRounding() throws Exception {
        int userId = 1, setId = 1, correctCount = 7, totalQuestions = 10;
        when(statisticsDao.getStatistics(userId, setId)).thenReturn(null);
        when(statisticsDao.upsertStatistics(userId, setId, 70)).thenReturn(true);

        boolean result = statisticsService.processQuizResults(userId, setId, correctCount, totalQuestions);

        assertTrue(result);
        verify(statisticsDao).upsertStatistics(userId, setId, 70);
    }

    // Test getStatisticsByUser
    @Test
    void testGetStatisticsByUser() throws Exception {
        int userId = 1;
        List<Statistics> mockStats = Arrays.asList(
                new Statistics(1, 1, 1, 85),
                new Statistics(2, 1, 2, 90)
        );
        when(statisticsDao.getStatisticsByUser(userId)).thenReturn(mockStats);

        List<Statistics> result = statisticsService.getStatisticsByUser(userId);

        assertEquals(mockStats, result);
        verify(statisticsDao).getStatisticsByUser(userId);
    }

    // Test getStatistics
    @Test
    void testGetStatistics() throws Exception {
        int userId = 1, setId = 1;
        Statistics mockStats = new Statistics(1, 1, 1, 85);
        when(statisticsDao.getStatistics(userId, setId)).thenReturn(mockStats);

        Statistics result = statisticsService.getStatistics(userId, setId);

        assertEquals(mockStats, result);
        verify(statisticsDao).getStatistics(userId, setId);
    }

    // Test getAllStatistics
    @Test
    void testGetAllStatistics() throws Exception {
        List<Statistics> mockStats = Arrays.asList(
                new Statistics(1, 1, 1, 85),
                new Statistics(2, 2, 1, 90),
                new Statistics(3, 1, 2, 75)
        );
        when(statisticsDao.getAllStatistics()).thenReturn(mockStats);

        List<Statistics> result = statisticsService.getAllStatistics();

        assertEquals(mockStats, result);
        verify(statisticsDao).getAllStatistics();
    }

    // Test processQuizResults exception from getStatistics
    @Test
    void testProcessQuizResultsGetStatisticsException() throws Exception {
        int userId = 1, setId = 1, correctCount = 8, totalQuestions = 10;
        when(statisticsDao.getStatistics(userId, setId)).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            statisticsService.processQuizResults(userId, setId, correctCount, totalQuestions);
        });

        assertEquals("Database error", exception.getMessage());
        verify(statisticsDao).getStatistics(userId, setId);
    }
}
