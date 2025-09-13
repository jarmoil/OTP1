package services;

import dao.FlashcardDao;
import models.Flashcard;
import java.util.List;

// Service class for flashcard-related operations
public class FlashcardService {
    // TODO: Implement update and delete methods for flashcards
    private FlashcardDao flashcardDao = new FlashcardDao();

    // Retrieve all flashcards for a given flashcard set ID
    public List<Flashcard> getFlashcardsBySetId(int setId) throws Exception {
        return flashcardDao.getFlashcardsBySetId(setId);
    }

    // Create a new flashcard in the database using the DAO
    public boolean createFlashcard(int setId, String question, String answer,
                                   String choiceA, String choiceB, String choiceC) throws Exception {
        return flashcardDao.createFlashcard(setId, question, answer, choiceA, choiceB, choiceC);
    }
}
