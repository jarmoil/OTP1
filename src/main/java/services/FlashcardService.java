package services;

import dao.IFlashcardDao;
import exceptions.DataOperationException;
import models.Flashcard;
import utils.validation.FlashcardData;
import utils.validation.Validator;

import java.util.List;

// Service class for flashcard-related operations
public class FlashcardService {
    private final IFlashcardDao flashcardDao;
    private final Validator<FlashcardData> validator;

    public FlashcardService(IFlashcardDao flashcardDao, Validator<FlashcardData> validator) {
        this.flashcardDao = flashcardDao;
        this.validator = validator;
    }

    // Retrieve all flashcards for a given flashcard set ID
    public List<Flashcard> getFlashcardsBySetId(int setId) throws DataOperationException {
        return flashcardDao.getFlashcardsBySetId(setId);
    }

    // Create a new flashcard in the database using the DAO
    public boolean createFlashcard(int setId, String question, String answer,
                                   String choiceA, String choiceB, String choiceC) throws DataOperationException {
        FlashcardData data = new FlashcardData(question, answer, choiceA, choiceB, choiceC);
        validator.validate(data);
        return flashcardDao.createFlashcard(setId, question, answer, choiceA, choiceB, choiceC);
    }

    // Update an existing flashcard in the database using the DAO
    public boolean updateFlashcard(int flashcardId, String question, String answer,
                                   String choiceA, String choiceB, String choiceC) throws DataOperationException {
        FlashcardData data = new FlashcardData(question, answer, choiceA, choiceB, choiceC);
        validator.validate(data);
        return flashcardDao.updateFlashcard(flashcardId, question, answer, choiceA, choiceB, choiceC);
    }

    // Delete a flashcard from the database using the DAO
    public boolean deleteFlashcard(int flashcardId) throws DataOperationException {
        return flashcardDao.deleteFlashcard(flashcardId);
    }

    public void updateFlashcardStats(int flashcardId, boolean isCorrect) throws DataOperationException {
        flashcardDao.updateFlashcardStats(flashcardId, isCorrect);
    }

}
