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
                "History",
                75);

        assertEquals(1, flashcardSet.getSets_id());
        assertEquals(1, flashcardSet.getUser_id());
        assertEquals("History", flashcardSet.getDescription());
        assertEquals(75, flashcardSet.getSets_correct_percentage());
        assertNotNull(flashcardSet.getFlashcards());
        assertTrue(flashcardSet.getFlashcards().isEmpty());

        flashcardSet.setSets_id(2);
        flashcardSet.setUser_id(3);
        flashcardSet.setDescription("Biology");
        flashcardSet.setSets_correct_percentage(80);

        assertEquals(2, flashcardSet.getSets_id());
        assertEquals(3, flashcardSet.getUser_id());
        assertEquals("Biology", flashcardSet.getDescription());
        assertEquals(80, flashcardSet.getSets_correct_percentage());
    }

    @Test
    void testFlashcardsList() {
        FlashcardSet flashcardSet = new FlashcardSet(2,2, "Chemistry", 90);
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