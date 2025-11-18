package utils.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlashcardValidatorTest {
    private FlashcardValidator validator;

    @BeforeEach
    void setUp() {
        validator = new FlashcardValidator();
    }

    @Test
    void testValidFlashcardData() {
        FlashcardData validData = new FlashcardData("What is 2+2?", "4", "3", "4", "5");

        assertDoesNotThrow(() -> validator.validate(validData));
    }

    @Test
    void testNullQuestion() {
        FlashcardData data = new FlashcardData(null, "4", "3", "4", "5");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> validator.validate(data));
        assertEquals("All fields must be filled out", exception.getMessage());
    }

    @Test
    void testEmptyQuestion() {
        FlashcardData data = new FlashcardData("", "4", "3", "4", "5");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> validator.validate(data));
        assertEquals("All fields must be filled out", exception.getMessage());
    }

    @Test
    void testWhitespaceOnlyQuestion() {
        FlashcardData data = new FlashcardData("   ", "4", "3", "4", "5");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> validator.validate(data));
        assertEquals("All fields must be filled out", exception.getMessage());
    }

    @Test
    void testNullAnswer() {
        FlashcardData data = new FlashcardData("What is 2+2?", null, "3", "4", "5");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> validator.validate(data));
        assertEquals("All fields must be filled out", exception.getMessage());
    }

    @Test
    void testNullChoiceA() {
        FlashcardData data = new FlashcardData("What is 2+2?", "4", null, "4", "5");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> validator.validate(data));
        assertEquals("All fields must be filled out", exception.getMessage());
    }

    @Test
    void testNullChoiceB() {
        FlashcardData data = new FlashcardData("What is 2+2?", "4", "3", null, "5");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> validator.validate(data));
        assertEquals("All fields must be filled out", exception.getMessage());
    }

    @Test
    void testNullChoiceC() {
        FlashcardData data = new FlashcardData("What is 2+2?", "4", "3", "4", null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> validator.validate(data));
        assertEquals("All fields must be filled out", exception.getMessage());
    }

    @Test
    void testAnswerDoesNotMatchAnyChoice() {
        FlashcardData data = new FlashcardData("What is 2+2?", "6", "3", "4", "5");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> validator.validate(data));
        assertEquals("The answer must match one of the choices", exception.getMessage());
    }

    @Test
    void testAnswerMatchesChoiceA() {
        FlashcardData data = new FlashcardData("What is 2+2?", "3", "3", "4", "5");

        assertDoesNotThrow(() -> validator.validate(data));
    }

    @Test
    void testAnswerMatchesChoiceC() {
        FlashcardData data = new FlashcardData("What is 2+2?", "5", "3", "4", "5");

        assertDoesNotThrow(() -> validator.validate(data));
    }
}
