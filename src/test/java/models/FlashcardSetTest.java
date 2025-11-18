package models;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlashcardSetTest {
    @Test
    void testFlashcardSetGettersandSetters() {
        FlashcardSet flashcardSet = new FlashcardSet(
                1,
                1,
                "en",
                "History",
                75);

        assertEquals(1, flashcardSet.getSetsId());
        assertEquals(1, flashcardSet.getUserId());
        assertEquals("History", flashcardSet.getDescription());
        assertEquals(75, flashcardSet.getSetsCorrectPercentage());
        assertNotNull(flashcardSet.getFlashcards());
        assertTrue(flashcardSet.getFlashcards().isEmpty());

        flashcardSet.setSetsId(2);
        flashcardSet.setUserId(3);
        flashcardSet.setDescription("Biology");
        flashcardSet.setSetsCorrectPercentage(80);

        assertEquals(2, flashcardSet.getSetsId());
        assertEquals(3, flashcardSet.getUserId());
        assertEquals("Biology", flashcardSet.getDescription());
        assertEquals(80, flashcardSet.getSetsCorrectPercentage());
    }

    @Test
    void testFlashcardsList() {
        FlashcardSet flashcardSet = new FlashcardSet(2,2, "en", "Chemistry", 90);
        Flashcard card1 = new Flashcard(1, 1, 10, 9, "Question 1", "Answer 1", "A", "B","C");
        Flashcard card2 = new Flashcard(2, 2, 10, 9, "Question 2", "Answer 2", "A", "B","C");

        List<Flashcard> flashcards = new ArrayList<>();
        flashcards.add(card1);
        flashcards.add(card2);

        flashcardSet.setFlashcards(flashcards);
        assertEquals(2, flashcardSet.getFlashcards().size());
        assertEquals("Question 1", flashcardSet.getFlashcards().get(0).getQuestion());
        assertEquals("Question 2", flashcardSet.getFlashcards().get(1).getQuestion());
    }
}