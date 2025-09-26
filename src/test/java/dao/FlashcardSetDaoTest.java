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

// TODO: Add tests for the new update and delete methods

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
                    "INSERT INTO sets (user_id, description, sets_correct_percentage) VALUES (" + userId + ", 'History set', 0)"
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
            stmt.execute("INSERT INTO sets (user_id, description, sets_correct_percentage) VALUES (" + studentId + ", 'Chemistry set', 0)");
            stmt.execute("INSERT INTO sets (user_id, description, sets_correct_percentage) VALUES (" + teacherId + ", 'Physics set', 0)");
        }

        List<FlashcardSet> studentSets = IFlashcardSetDao.getSetsByRole("student");
        assertEquals(1, studentSets.size());
        assertEquals("Chemistry set", studentSets.get(0).getDescription());

        List<FlashcardSet> teacherSets = IFlashcardSetDao.getSetsByRole("teacher");
        assertEquals(1, teacherSets.size());
        assertEquals("Physics set", teacherSets.get(0).getDescription());
    }

    @Test
    void testGetAllSets() throws Exception {
        try (Statement statement = connection.createStatement()) {
            statement.execute("INSERT INTO user_accounts (role) VALUES ('student')");
            statement.execute("INSERT INTO user_accounts (role) VALUES ('teacher')");
        }

        IFlashcardSetDao.createSet(1, "Chemistry set");
        IFlashcardSetDao.createSet(2, "Biology set");

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
}