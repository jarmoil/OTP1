package dao;

import database.ConnectDB;
import models.Statistics;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatisticsDaoTest {
    private static Connection connection;
    private IStatisticsDao statisticsDao;

    @BeforeAll
    static void setupDatabase() throws Exception {
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL");
        ConnectDB.settestConn(connection);

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE user_accounts (" +
                    "user_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "user_name VARCHAR(255) UNIQUE NOT NULL," +
                    "user_password VARCHAR(255) NOT NULL," +
                    "role VARCHAR(50) NOT NULL)");

            stmt.execute("CREATE TABLE sets (" +
                    "sets_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "user_id INT NOT NULL," +
                    "description VARCHAR(255) NOT NULL," +
                    "sets_correct_percentage INT DEFAULT 0," +
                    "FOREIGN KEY (user_id) REFERENCES user_accounts(user_id))");

            stmt.execute("CREATE TABLE statistics (" +
                    "stats_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "user_id INT NOT NULL," +
                    "sets_id INT NOT NULL," +
                    "stats_correct_percentage INT DEFAULT 0," +
                    "FOREIGN KEY (user_id) REFERENCES user_accounts(user_id)," +
                    "FOREIGN KEY (sets_id) REFERENCES sets(sets_id))");
        }
    }

    @AfterAll
    static void tearDown() throws Exception {
        connection.close();
    }

    @BeforeEach
    void setUp() {
        statisticsDao = new StatisticsDao();
    }

    @AfterEach
    void cleanUp() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM statistics");
            stmt.execute("DELETE FROM sets");
            stmt.execute("DELETE FROM user_accounts");
        }
    }

    @Test
    void testUpsertStatisticsInsert() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO user_accounts (user_id, user_name, user_password, role) VALUES (1, 'student1', 'pass1', 'student')");
            stmt.execute("INSERT INTO sets (sets_id, user_id, description) VALUES (1, 1, 'Test Set 1')");
        }

        assertTrue(statisticsDao.upsertStatistics(1, 1, 85));

        Statistics stats = statisticsDao.getStatistics(1, 1);
        assertNotNull(stats);
        assertEquals(1, stats.getUserId());
        assertEquals(1, stats.getSetsId());
        assertEquals(85, stats.getStatsCorrectPercentage());
    }

    @Test
    void testUpsertStatisticsUpdate() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO user_accounts (user_id, user_name, user_password, role) VALUES (1, 'student1', 'pass1', 'student')");
            stmt.execute("INSERT INTO sets (sets_id, user_id, description) VALUES (1, 1, 'Test Set 1')");
        }

        statisticsDao.upsertStatistics(1, 1, 75);
        assertTrue(statisticsDao.upsertStatistics(1, 1, 90));

        Statistics stats = statisticsDao.getStatistics(1, 1);
        assertEquals(90, stats.getStatsCorrectPercentage());
    }

    @Test
    void testGetAllStatistics() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO user_accounts (user_id, user_name, user_password, role) VALUES (1, 'student1', 'pass1', 'student')");
            stmt.execute("INSERT INTO user_accounts (user_id, user_name, user_password, role) VALUES (2, 'teacher1', 'pass2', 'teacher')");
            stmt.execute("INSERT INTO sets (sets_id, user_id, description) VALUES (1, 1, 'Test Set 1')");
            stmt.execute("INSERT INTO sets (sets_id, user_id, description) VALUES (2, 2, 'Test Set 2')");
        }

        statisticsDao.upsertStatistics(1, 1, 80);
        statisticsDao.upsertStatistics(2, 2, 75);

        List<Statistics> allStats = statisticsDao.getAllStatistics();
        assertEquals(2, allStats.size());
    }

    @Test
    void testGetStatistics() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO user_accounts (user_id, user_name, user_password, role) VALUES (1, 'student1', 'pass1', 'student')");
            stmt.execute("INSERT INTO sets (sets_id, user_id, description) VALUES (1, 1, 'Test Set 1')");
        }

        statisticsDao.upsertStatistics(1, 1, 85);

        Statistics stats = statisticsDao.getStatistics(1, 1);
        assertNotNull(stats);
        assertEquals(85, stats.getStatsCorrectPercentage());

        assertNull(statisticsDao.getStatistics(999, 999));
    }

    @Test
    void testGetStatisticsByUser() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO user_accounts (user_id, user_name, user_password, role) VALUES (1, 'student1', 'pass1', 'student')");
            stmt.execute("INSERT INTO sets (sets_id, user_id, description) VALUES (1, 1, 'Test Set 1')");
            stmt.execute("INSERT INTO sets (sets_id, user_id, description) VALUES (2, 1, 'Test Set 2')");
        }

        statisticsDao.upsertStatistics(1, 1, 80);
        statisticsDao.upsertStatistics(1, 2, 90);

        List<Statistics> userStats = statisticsDao.getStatisticsByUser(1);
        assertEquals(2, userStats.size());
        assertTrue(userStats.stream().allMatch(s -> s.getUserId() == 1));

        assertTrue(statisticsDao.getStatisticsByUser(999).isEmpty());
    }
}
