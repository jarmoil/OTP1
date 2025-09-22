package services;

import dao.FlashcardDao;
import models.Flashcard;
import utils.validation.FlashcardData;
import utils.validation.Validator;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FlashcardServiceTest {

    @Mock
    private FlashcardDao flashcardDao;

    @Mock
    private Validator<FlashcardData> validator;

    @InjectMocks
    private FlashcardService flashcardService;

    @BeforeEach
    void setUp() throws Exception {
        flashcardDao = mock(FlashcardDao.class);
        validator = mock(Validator.class);
        flashcardService = new FlashcardService();

        java.lang.reflect.Field daoField = FlashcardService.class.getDeclaredField("flashcardDao");
        daoField.setAccessible(true);
        daoField.set(flashcardService, flashcardDao);

        java.lang.reflect.Field validatorField = FlashcardService.class.getDeclaredField("validator");
        validatorField.setAccessible(true);
        validatorField.set(flashcardService, validator);
    }

    // Test retrieving flashcards by set ID
    @Test
    void testGetFlashcardsBySetId() throws Exception {
        int setId = 1;
        List<Flashcard> mockFlashcards = Arrays.asList(
                new Flashcard(1, 1, 0, 0, "Question 1", "A", "A", "B", "C"),
                new Flashcard(2, 1, 0, 0, "Question 2", "B", "A", "B", "C")
        );
        when(flashcardDao.getFlashcardsBySetId(setId)).thenReturn(mockFlashcards);

        List<Flashcard> result = flashcardService.getFlashcardsBySetId(setId);

        assertEquals(mockFlashcards, result);
        verify(flashcardDao).getFlashcardsBySetId(setId);
    }

    // Test successful flashcard creation
    @Test
    void testCreateFlashcardSuccess() throws Exception {
        doNothing().when(validator).validate(any(FlashcardData.class));
        when(flashcardDao.createFlashcard(anyInt(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(true);

        boolean result = flashcardService.createFlashcard(1, "Question", "A", "A", "B", "C");

        assertTrue(result);
        verify(validator).validate(any(FlashcardData.class));
        verify(flashcardDao).createFlashcard(1, "Question", "A", "A", "B", "C");
    }

    // Test flashcard creation with validation failure
    @Test
    void testCreateFlashcardValidationFailure() throws Exception {
        doThrow(new IllegalArgumentException("Validation error")).when(validator).validate(any(FlashcardData.class));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            flashcardService.createFlashcard(1, "Question", "A", "A", "B", "C");
        });

        assertEquals("Validation error", exception.getMessage());
        verify(validator).validate(any(FlashcardData.class));
        verifyNoInteractions(flashcardDao);
    }

    // Test flashcard creation failure from DAO
    @Test
    void testCreateFlashcardDaoFailure() throws Exception {
        doNothing().when(validator).validate(any(FlashcardData.class));
        when(flashcardDao.createFlashcard(anyInt(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(false);

        boolean result = flashcardService.createFlashcard(1, "Question", "A", "A", "B", "C");

        assertFalse(result);
        verify(validator).validate(any(FlashcardData.class));
        verify(flashcardDao).createFlashcard(1, "Question", "A", "A", "B", "C");
    }

    // Test successful flashcard update
    @Test
    void testUpdateFlashcardSuccess() throws Exception {
        doNothing().when(validator).validate(any(FlashcardData.class));
        when(flashcardDao.updateFlashcard(anyInt(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(true);

        boolean result = flashcardService.updateFlashcard(1, "Question", "A", "A", "B", "C");

        assertTrue(result);
        verify(validator).validate(any(FlashcardData.class));
        verify(flashcardDao).updateFlashcard(1, "Question", "A", "A", "B", "C");
    }

    // Test flashcard update with validation failure
    @Test
    void testUpdateFlashcardValidationFailure() throws Exception {
        doThrow(new IllegalArgumentException("Validation error")).when(validator).validate(any(FlashcardData.class));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            flashcardService.updateFlashcard(1, "Question", "A", "A", "B", "C");
        });

        assertEquals("Validation error", exception.getMessage());
        verify(validator).validate(any(FlashcardData.class));
        verifyNoInteractions(flashcardDao);
    }

    // Test flashcard update failure from DAO
    @Test
    void testUpdateFlashcardDaoFailure() throws Exception {
        doNothing().when(validator).validate(any(FlashcardData.class));
        when(flashcardDao.updateFlashcard(anyInt(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(false);

        boolean result = flashcardService.updateFlashcard(1, "Question", "A", "A", "B", "C");

        assertFalse(result);
        verify(validator).validate(any(FlashcardData.class));
        verify(flashcardDao).updateFlashcard(1, "Question", "A", "A", "B", "C");
    }

    // Test successful flashcard deletion
    @Test
    void testDeleteFlashcardSuccess() throws Exception {
        when(flashcardDao.deleteFlashcard(1)).thenReturn(true);

        boolean result = flashcardService.deleteFlashcard(1);

        assertTrue(result);
        verify(flashcardDao).deleteFlashcard(1);
    }

    // Test flashcard deletion failure
    @Test
    void testDeleteFlashcardFailure() throws Exception {
        when(flashcardDao.deleteFlashcard(1)).thenReturn(false);

        boolean result = flashcardService.deleteFlashcard(1);

        assertFalse(result);
        verify(flashcardDao).deleteFlashcard(1);
    }
}
