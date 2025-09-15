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
        assertEquals(1, flashcard.getFlashcard_id());
        assertEquals(1, flashcard.getSets_id());
        assertEquals(4, flashcard.getTimes_answered());
        assertEquals(3, flashcard.getTimes_correct());
        assertEquals("When did Finland gain it's independence?", flashcard.getQuestion());
        assertEquals("1917", flashcard.getAnswer());
        assertEquals("1919", flashcard.getChoice_a());
        assertEquals("1900", flashcard.getChoice_b());
        assertEquals("1917", flashcard.getChoice_c());

        flashcard.setFlashcard_id(2);
        flashcard.setSets_id(2);
        flashcard.setTimes_answered(2);
        flashcard.setTimes_correct(2);
        flashcard.setQuestion("When did Germany united?");
        flashcard.setAnswer("1990");
        flashcard.setChoice_a("1980");
        flashcard.setChoice_b("2000");
        flashcard.setChoice_c("1990");

        assertEquals(2, flashcard.getFlashcard_id());
        assertEquals(2, flashcard.getSets_id());
        assertEquals(2, flashcard.getTimes_answered());
        assertEquals(2, flashcard.getTimes_correct());
        assertEquals("When did Germany united?", flashcard.getQuestion());
        assertEquals("1990", flashcard.getAnswer());
        assertEquals("1980", flashcard.getChoice_a());
        assertEquals("2000", flashcard.getChoice_b());
        assertEquals("1990", flashcard.getChoice_c());
    }
}