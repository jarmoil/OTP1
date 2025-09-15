package dao;

import database.ConnectDB;
import models.Flashcard;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FlashcardDaoTest {

    private FlashcardDao flashcardDao;
    private Connection connection;

    @BeforeAll
    void setupDatabase() throws Exception {
        connection = DriverManager.getConnection(
                "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL");

        ConnectDB.settestConn(connection);

        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE flashcards (" +
                    "flashcard_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "sets_id INT NOT NULL," +
                    "times_answered INT DEFAULT 0," +
                    "times_correct INT DEFAULT 0," +
                    "question VARCHAR(255) NOT NULL," +
                    "answer VARCHAR(255) NOT NULL," +
                    "choice_a VARCHAR(255) NOT NULL," +
                    "choice_b VARCHAR(255) NOT NULL," +
                    "choice_c VARCHAR(255) NOT NULL" +
                    ")");
        }
    }

    @AfterAll
    void tearDown() throws Exception {
        connection.close();
    }

    @BeforeEach
    void setUp() {
        flashcardDao = new FlashcardDao();
    }

    @AfterEach
    void cleanUp() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("TRUNCATE TABLE flashcards");
        }
    }

    @Test
    void testCreateFlashcard() throws Exception {
        boolean created = flashcardDao.createFlashcard(
                1, "What is an MVC?", "It's a programming model", "Tea", "Candy", "It's a programming model"
        );

        assertTrue(created);

        List<Flashcard> flashcards = flashcardDao.getFlashcardsBySetId(1);
        assertEquals(1, flashcards.size());
        assertEquals("What is an MVC?", flashcards.get(0).getQuestion());
    }

    @Test
    void testGetFlashcardsBySetId_emptyResult() throws Exception {
        List<Flashcard> flashcards = flashcardDao.getFlashcardsBySetId(99);
        assertNotNull(flashcards);
        assertTrue(flashcards.isEmpty());
    }
}
