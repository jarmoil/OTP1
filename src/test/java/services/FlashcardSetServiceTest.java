package services;

import dao.FlashcardSetDao;
import dao.IFlashcardSetDao;
import models.FlashcardSet;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FlashcardSetServiceTest {

    @Mock
    private IFlashcardSetDao flashcardSetDao;

    @InjectMocks
    private FlashcardSetService flashcardSetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        flashcardSetService = new FlashcardSetService(flashcardSetDao);
    }

    // Tests that the service gets all flashcard sets
    @Test
    void testGetAllSets() throws Exception{
        List<FlashcardSet> mockSets = Arrays.asList(
                new FlashcardSet(1, 1, "en", "Set 1", 0),
                new FlashcardSet(2, 1, "en", "Set 2", 0)
        );
        when(flashcardSetDao.getAllSets()).thenReturn(mockSets);

        List<FlashcardSet> result = flashcardSetService.getAllSets();

        assertEquals(mockSets, result);
        verify(flashcardSetDao).getAllSets();
    }

    // Tests that the service gets flashcard sets by user role
    @Test
    void testGetSetsByRole() throws Exception {
        String role = "student";
        String locale = "en";
        List<FlashcardSet> mockSets = Arrays.asList(new FlashcardSet(1, 1, "en", "Set 1", 0));
        when(flashcardSetDao.getSetsByRoleAndLocale(role, locale)).thenReturn(mockSets);

        List<FlashcardSet> result = flashcardSetService.getSetsByRoleAndLocale(role, locale);

        assertEquals(mockSets, result);
        verify(flashcardSetDao).getSetsByRoleAndLocale(role, locale);
    }

    // Tests that the service creates a new flashcard set
    @Test
    void testCreateSet() throws Exception {
        int userId = 1;
        String description = "Test Description";
        String locale = "en";
        when(flashcardSetDao.createSet(userId, description, locale)).thenReturn(true);

        boolean result = flashcardSetService.createSet(userId, description, locale);

        assertTrue(result);
        verify(flashcardSetDao).createSet(userId, description, locale);
    }

    // Tests that the service updates a flashcard set
    @Test
    void testUpdateSetSuccess() throws Exception {
        // Test validation with empty description
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
            flashcardSetService.updateSet(1, "");
        });
        assertEquals("Description cannot be empty", exception1.getMessage());

        // Test validation with whitespace-only description
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            flashcardSetService.updateSet(1, "   ");
        });
        assertEquals("Description cannot be empty", exception2.getMessage());

        int setId = 1;
        String newDescription = "Updated Description";
        when(flashcardSetDao.updateSet(setId, newDescription)).thenReturn(true);

        boolean result = flashcardSetService.updateSet(setId, newDescription);

        assertTrue(result);
        verify(flashcardSetDao).updateSet(setId, newDescription);
    }

    // Tests that the service deletes a flashcard set
    @Test
    void testDeleteSet() throws Exception {
        int setId = 1;
        when(flashcardSetDao.deleteSet(setId)).thenReturn(true);

        boolean result = flashcardSetService.deleteSet(setId);

        assertTrue(result);
        verify(flashcardSetDao).deleteSet(setId);
    }

    // Test retrieving a flashcard set by ID
    @Test
    void testGetSetById() throws Exception {
        int setId = 1;
        FlashcardSet mockSet = new FlashcardSet(1, 1, "en", "Test Set", 75);
        when(flashcardSetDao.getSetById(setId)).thenReturn(mockSet);

        FlashcardSet result = flashcardSetService.getSetById(setId);

        assertEquals(mockSet, result);
        verify(flashcardSetDao).getSetById(setId);
    }

    // Test updating set correct percentage
    @Test
    void testUpdateSetCorrectPercentage() throws Exception {
        int setId = 1;

        flashcardSetService.updateSetCorrectPercentage(setId);

        verify(flashcardSetDao).updateSetCorrectPercentage(setId);
    }

    // Test updateSetCorrectPercentage when DAO throws exception
    @Test
    void testUpdateSetCorrectPercentageException() throws Exception {
        int setId = 1;
        doThrow(new RuntimeException("Database error")).when(flashcardSetDao).updateSetCorrectPercentage(setId);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            flashcardSetService.updateSetCorrectPercentage(setId);
        });

        assertEquals("Database error", exception.getMessage());
        verify(flashcardSetDao).updateSetCorrectPercentage(setId);
    }

}
