package models;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class FlashcardTest {
    @Test
    void testFlashcardGettersandSetters() {
        Flashcard flashcard = new Flashcard(
                1,
                1,
                4,
                3,
                "When did Finland gain it's independence?",
                "1917",
                "1919",
                "1900",
                "1917"
        );
        assertEquals(1, flashcard.getFlashcardId());
        assertEquals(1, flashcard.getSetsId());
        assertEquals(4, flashcard.getTimesAnswered());
        assertEquals(3, flashcard.getTimesCorrect());
        assertEquals("When did Finland gain it's independence?", flashcard.getQuestion());
        assertEquals("1917", flashcard.getAnswer());
        assertEquals("1919", flashcard.getChoiceA());
        assertEquals("1900", flashcard.getChoiceB());
        assertEquals("1917", flashcard.getChoiceC());

        flashcard.setFlashcardId(2);
        flashcard.setSetsId(2);
        flashcard.setTimesAnswered(2);
        flashcard.setTimesCorrect(2);
        flashcard.setQuestion("When did Germany united?");
        flashcard.setAnswer("1990");
        flashcard.setChoiceA("1980");
        flashcard.setChoiceB("2000");
        flashcard.setChoiceC("1990");

        assertEquals(2, flashcard.getFlashcardId());
        assertEquals(2, flashcard.getSetsId());
        assertEquals(2, flashcard.getTimesAnswered());
        assertEquals(2, flashcard.getTimesCorrect());
        assertEquals("When did Germany united?", flashcard.getQuestion());
        assertEquals("1990", flashcard.getAnswer());
        assertEquals("1980", flashcard.getChoiceA());
        assertEquals("2000", flashcard.getChoiceB());
        assertEquals("1990", flashcard.getChoiceC());
    }
}