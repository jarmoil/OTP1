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

    private IFlashcardDao flashcardDao;
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
    void setUp() throws Exception {
        flashcardDao = new FlashcardDao();
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("TRUNCATE TABLE flashcards");
        }
    }

    @Test
    void testCreateAndGetFlashcard() throws Exception {
        // Test creation
        boolean created = flashcardDao.createFlashcard(
                1, "What is MVC?", "Model View Controller", "Tea", "Candy", "Coffee"
        );
        assertTrue(created);

        // Test retrieval
        List<Flashcard> flashcards = flashcardDao.getFlashcardsBySetId(1);
        assertEquals(1, flashcards.size());

        Flashcard flashcard = flashcards.get(0);
        assertEquals(1, flashcard.getSets_id());
        assertEquals("What is MVC?", flashcard.getQuestion());
        assertEquals("Model View Controller", flashcard.getAnswer());
        assertEquals(0, flashcard.getTimes_answered());
        assertEquals(0, flashcard.getTimes_correct());
    }

    @Test
    void testGetFlashcards_emptyAndMultiple() throws Exception {
        // Test empty result
        List<Flashcard> empty = flashcardDao.getFlashcardsBySetId(99);
        assertTrue(empty.isEmpty());

        // Test multiple flashcards
        flashcardDao.createFlashcard(1, "Q1", "A1", "C1A", "C1B", "C1C");
        flashcardDao.createFlashcard(1, "Q2", "A2", "C2A", "C2B", "C2C");
        flashcardDao.createFlashcard(2, "Q3", "A3", "C3A", "C3B", "C3C");

        assertEquals(2, flashcardDao.getFlashcardsBySetId(1).size());
        assertEquals(1, flashcardDao.getFlashcardsBySetId(2).size());
    }

    @Test
    void testUpdateFlashcard() throws Exception {
        // Create flashcard
        flashcardDao.createFlashcard(1, "Original", "Original Answer", "A", "B", "C");
        int flashcardId = flashcardDao.getFlashcardsBySetId(1).get(0).getFlashcard_id();

        // Test successful update
        assertTrue(flashcardDao.updateFlashcard(flashcardId, "Updated", "Updated Answer", "UA", "UB", "UC"));

        Flashcard updated = flashcardDao.getFlashcardsBySetId(1).get(0);
        assertEquals("Updated", updated.getQuestion());
        assertEquals("Updated Answer", updated.getAnswer());

        // Test update non-existent ID
        assertFalse(flashcardDao.updateFlashcard(999, "Question", "Answer", "A", "B", "C"));
    }

    @Test
    void testDeleteFlashcard() throws Exception {
        // Create flashcard
        flashcardDao.createFlashcard(1, "Question", "Answer", "A", "B", "C");
        int flashcardId = flashcardDao.getFlashcardsBySetId(1).get(0).getFlashcard_id();

        // Test successful deletion
        assertTrue(flashcardDao.deleteFlashcard(flashcardId));
        assertTrue(flashcardDao.getFlashcardsBySetId(1).isEmpty());

        // Test delete non-existent ID
        assertFalse(flashcardDao.deleteFlashcard(999));
    }

    @Test
    void testUpdateFlashcardStats() throws Exception {
        // Create flashcard
        flashcardDao.createFlashcard(1, "Question", "Answer", "A", "B", "C");
        int flashcardId = flashcardDao.getFlashcardsBySetId(1).get(0).getFlashcard_id();

        // Test correct answer
        assertTrue(flashcardDao.updateFlashcardStats(flashcardId, true));
        Flashcard updated = flashcardDao.getFlashcardsBySetId(1).get(0);
        assertEquals(1, updated.getTimes_answered());
        assertEquals(1, updated.getTimes_correct());

        // Test incorrect answer
        assertTrue(flashcardDao.updateFlashcardStats(flashcardId, false));
        updated = flashcardDao.getFlashcardsBySetId(1).get(0);
        assertEquals(2, updated.getTimes_answered());
        assertEquals(1, updated.getTimes_correct());

        // Test non-existent ID
        assertFalse(flashcardDao.updateFlashcardStats(999, true));
    }

    @Test
    void testEdgeCases() throws Exception {
        // Test with empty strings
        assertTrue(flashcardDao.createFlashcard(1, "", "", "", "", ""));
        assertEquals(1, flashcardDao.getFlashcardsBySetId(1).size());

        // Test with special characters
        assertTrue(flashcardDao.createFlashcard(2, "What's 2+2?", "It's 4!", "A&B", "<C>", "\"D\""));
        Flashcard special = flashcardDao.getFlashcardsBySetId(2).get(0);
        assertEquals("What's 2+2?", special.getQuestion());
        assertEquals("\"D\"", special.getChoice_c());
    }
}
