package dao;

import exceptions.DataOperationException;
import models.Flashcard;

import java.util.List;

public interface IFlashcardDao {
    // Retrieve all flashcards for a given flashcard set ID
    List<Flashcard> getFlashcardsBySetId(int setId) throws DataOperationException;

    // Create a new flashcard in the database
    boolean createFlashcard(int setId, String question, String answer,
                            String choiceA, String choiceB, String choiceC) throws DataOperationException;

    // Update flashcard information in the database by its ID
    boolean updateFlashcard(int flashcardId, String question, String answer,
                            String choiceA, String choiceB, String choiceC) throws DataOperationException;

    // Delete a flashcard from the database by its ID
    boolean deleteFlashcard(int flashcardId) throws DataOperationException;

    // Update flashcard statistics after an answer attempt
    boolean updateFlashcardStats(int flashcardId, boolean isCorrect) throws DataOperationException;
}
