package utils.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlashcardDataTest {

    @Test
    void testConstructorAndGetters() {
        FlashcardData data = new FlashcardData("What is 2+2?", "4", "3", "4", "5");

        assertEquals("What is 2+2?", data.getQuestion());
        assertEquals("4", data.getAnswer());
        assertEquals("3", data.getChoiceA());
        assertEquals("4", data.getChoiceB());
        assertEquals("5", data.getChoiceC());
    }

    @Test
    void testWithNullValues() {
        FlashcardData data = new FlashcardData(null, null, null, null, null);

        assertNull(data.getQuestion());
        assertNull(data.getAnswer());
        assertNull(data.getChoiceA());
        assertNull(data.getChoiceB());
        assertNull(data.getChoiceC());
    }

    @Test
    void testWithEmptyStrings() {
        FlashcardData data = new FlashcardData("", "", "", "", "");

        assertEquals("", data.getQuestion());
        assertEquals("", data.getAnswer());
        assertEquals("", data.getChoiceA());
        assertEquals("", data.getChoiceB());
        assertEquals("", data.getChoiceC());
    }
}
