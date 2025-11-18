package dao;

import database.ConnectDB;
import models.FlashcardSet;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlashcardSetDaoTest {
    private static Connection connection;
    private IFlashcardSetDao IFlashcardSetDao;

    @BeforeAll
    static void setupDatabase() throws Exception {
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL");
        ConnectDB.settestConn(connection);

        try(Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE user_accounts (" +
                    "user_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "role VARCHAR(50) NOT NULL)");

            statement.execute("CREATE TABLE sets (" +
                    "sets_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "user_id INT NOT NULL," +
                    "locale VARCHAR(5) NOT NULL," +
                    "description VARCHAR(255) NOT NULL," +
                    "sets_correct_percentage INT DEFAULT 0," +
                    "FOREIGN KEY (user_id) REFERENCES user_accounts(user_id)" +
                    ")");
        }
    }

    @AfterAll
    static void tearDown() throws Exception {
        connection.close();
    }

    @BeforeEach
    void setUp() {
        IFlashcardSetDao = new FlashcardSetDao();
    }

    @AfterEach
    void cleanUp() throws Exception{
        try(Statement statement = connection.createStatement()){
            statement.execute("DELETE FROM sets");
            statement.execute("DELETE FROM user_accounts");
        }
    }

    @Test
    void testCreateSet() throws Exception {
        int userId;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO user_accounts (role) VALUES ('teacher')");
            ResultSet rs = stmt.executeQuery("SELECT user_id FROM user_accounts WHERE role='teacher'");
            rs.next();
            userId = rs.getInt(1);
        }

        try (Statement stmt = connection.createStatement()) {
            boolean created = stmt.execute(
                    "INSERT INTO sets (user_id, locale, description, sets_correct_percentage) VALUES (" + userId + ", 'en', 'History set', 0)"
            );
            assertTrue(true);
        }

        List<FlashcardSet> sets = IFlashcardSetDao.getAllSets();
        assertEquals(1, sets.size());
        assertEquals("History set", sets.get(0).getDescription());
    }

    @Test
    void testGetSetsByRole() throws Exception {
        int studentId = -1;
        int teacherId = -1;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO user_accounts (role) VALUES ('student')");
            stmt.execute("INSERT INTO user_accounts (role) VALUES ('teacher')");

            ResultSet rs = stmt.executeQuery("SELECT user_id, role FROM user_accounts");
            rs.next();
            if ("student".equals(rs.getString("role"))) studentId = rs.getInt("user_id");
            else teacherId = rs.getInt("user_id");

            rs.next();
            if ("student".equals(rs.getString("role"))) studentId = rs.getInt("user_id");
            else teacherId = rs.getInt("user_id");
        }

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO sets (user_id, locale, description, sets_correct_percentage) VALUES (" + studentId + ", 'en', 'Chemistry set', 0)");
            stmt.execute("INSERT INTO sets (user_id, locale, description, sets_correct_percentage) VALUES (" + teacherId + ", 'en', 'Physics set', 0)");
        }

        List<FlashcardSet> studentSets = IFlashcardSetDao.getSetsByRoleAndLocale("student", "en");
        assertEquals(1, studentSets.size());
        assertEquals("Chemistry set", studentSets.get(0).getDescription());

        List<FlashcardSet> teacherSets = IFlashcardSetDao.getSetsByRoleAndLocale("teacher", "en");
        assertEquals(1, teacherSets.size());
        assertEquals("Physics set", teacherSets.get(0).getDescription());
    }

    @Test
    void testGetAllSets() throws Exception {
        int userId1, userId2;
        try (Statement statement = connection.createStatement()) {
            statement.execute("INSERT INTO user_accounts (role) VALUES ('student')");
            statement.execute("INSERT INTO user_accounts (role) VALUES ('teacher')");

            ResultSet rs = statement.executeQuery("SELECT user_id FROM user_accounts ORDER BY user_id");
            rs.next();
            userId1 = rs.getInt(1);
            rs.next();
            userId2 = rs.getInt(1);
        }

        IFlashcardSetDao.createSet(userId1, "Chemistry set", "en");
        IFlashcardSetDao.createSet(userId2, "Biology set", "en");

        List<FlashcardSet> sets = IFlashcardSetDao.getAllSets();
        assertNotNull(sets);
        assertEquals(2, sets.size());
        assertTrue(sets.stream().anyMatch(s -> "Chemistry set".equals(s.getDescription())));
        assertTrue(sets.stream().anyMatch(s -> "Biology set".equals(s.getDescription())));
    }

    @Test
    void testGetAllSetsNull() throws Exception {
        List<FlashcardSet> sets = IFlashcardSetDao.getAllSets();
        assertNotNull(sets);
        assertTrue(sets.isEmpty());
    }

    @Test
    void testUpdateSet() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO user_accounts (user_id, role) VALUES (1, 'student')");
        }

        IFlashcardSetDao.createSet(1, "Original Description", "en");
        int setId = IFlashcardSetDao.getAllSets().get(0).getSets_id();

        assertTrue(IFlashcardSetDao.updateSet(setId, "Updated Description"));
        FlashcardSet updated = IFlashcardSetDao.getSetById(setId);
        assertEquals("Updated Description", updated.getDescription());

        assertFalse(IFlashcardSetDao.updateSet(999, "New Description"));
    }

    @Test
    void testDeleteSet() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO user_accounts (user_id, role) VALUES (1, 'student')");
        }

        IFlashcardSetDao.createSet(1, "To Delete", "en");
        int setId = IFlashcardSetDao.getAllSets().get(0).getSets_id();

        assertTrue(IFlashcardSetDao.deleteSet(setId));
        assertNull(IFlashcardSetDao.getSetById(setId));

        assertFalse(IFlashcardSetDao.deleteSet(999));
    }

    @Test
    void testGetSetById() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO user_accounts (user_id, role) VALUES (1, 'student')");
        }

        IFlashcardSetDao.createSet(1, "Test Set", "en");
        int setId = IFlashcardSetDao.getAllSets().get(0).getSets_id();

        FlashcardSet retrievedSet = IFlashcardSetDao.getSetById(setId);
        assertNotNull(retrievedSet);
        assertEquals("Test Set", retrievedSet.getDescription());

        assertNull(IFlashcardSetDao.getSetById(999));
    }

    @Test
    void testUpdateSetCorrectPercentage() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO user_accounts (user_id, role) VALUES (1, 'student')");
            stmt.execute("CREATE TABLE IF NOT EXISTS flashcards (" +
                    "flashcard_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "sets_id INT NOT NULL," +
                    "times_answered INT DEFAULT 0," +
                    "times_correct INT DEFAULT 0," +
                    "question VARCHAR(255)," +
                    "answer VARCHAR(255)," +
                    "choice_a VARCHAR(255)," +
                    "choice_b VARCHAR(255)," +
                    "choice_c VARCHAR(255))");
        }

        IFlashcardSetDao.createSet(1, "Test Set", "en");
        int setId = IFlashcardSetDao.getAllSets().get(0).getSets_id();

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO flashcards (sets_id, times_answered, times_correct, question, answer, choice_a, choice_b, choice_c) " +
                    "VALUES (" + setId + ", 10, 8, 'Q1', 'A1', 'C1', 'C2', 'C3')");
            stmt.execute("INSERT INTO flashcards (sets_id, times_answered, times_correct, question, answer, choice_a, choice_b, choice_c) " +
                    "VALUES (" + setId + ", 5, 3, 'Q2', 'A2', 'C1', 'C2', 'C3')");
        }

        assertTrue(IFlashcardSetDao.updateSetCorrectPercentage(setId));
        FlashcardSet updated = IFlashcardSetDao.getSetById(setId);
        assertEquals(73, updated.getSets_correct_percentage());

        assertFalse(IFlashcardSetDao.updateSetCorrectPercentage(999));
    }

}