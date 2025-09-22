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
        // Validate that answer matches one of the choices
        if (!answer.equals(choiceA) && !answer.equals(choiceB) && !answer.equals(choiceC)) {
            throw new IllegalArgumentException("The answer must match one of the choices");
        }

        if (question.trim().isEmpty() || answer.trim().isEmpty() ||
                choiceA.trim().isEmpty() || choiceB.trim().isEmpty() || choiceC.trim().isEmpty()) {
            throw new IllegalArgumentException("All fields must be filled out");
        }

        return flashcardDao.createFlashcard(setId, question, answer, choiceA, choiceB, choiceC);
    }

    // Update an existing flashcard in the database using the DAO
    public boolean updateFlashcard(int flashcardId, String question, String answer,
                                   String choiceA, String choiceB, String choiceC) throws Exception {
        // Validate that answer matches one of the choices
        if (!answer.equals(choiceA) && !answer.equals(choiceB) && !answer.equals(choiceC)) {
            throw new IllegalArgumentException("The answer must match one of the choices");
        }

        if (question.trim().isEmpty() || answer.trim().isEmpty() ||
                choiceA.trim().isEmpty() || choiceB.trim().isEmpty() || choiceC.trim().isEmpty()) {
            throw new IllegalArgumentException("All fields must be filled out");
        }

        return flashcardDao.updateFlashcard(flashcardId, question, answer, choiceA, choiceB, choiceC);
    }

    // Delete a flashcard from the database using the DAO
    public boolean deleteFlashcard(int flashcardId) throws Exception {
        return flashcardDao.deleteFlashcard(flashcardId);
    }

}
