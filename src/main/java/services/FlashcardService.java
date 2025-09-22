package services;

import dao.FlashcardDao;
import models.Flashcard;
import utils.validation.FlashcardData;
import utils.validation.FlashcardValidator;
import utils.validation.Validator;

import java.util.List;

// Service class for flashcard-related operations
public class FlashcardService {
    private FlashcardDao flashcardDao = new FlashcardDao();

    // Validator for flashcard data
    private Validator<FlashcardData> validator = new FlashcardValidator();


    // Retrieve all flashcards for a given flashcard set ID
    public List<Flashcard> getFlashcardsBySetId(int setId) throws Exception {
        return flashcardDao.getFlashcardsBySetId(setId);
    }

    // Create a new flashcard in the database using the DAO
    public boolean createFlashcard(int setId, String question, String answer,
                                   String choiceA, String choiceB, String choiceC) throws Exception {
        FlashcardData data = new FlashcardData(question, answer, choiceA, choiceB, choiceC);
        validator.validate(data);
        return flashcardDao.createFlashcard(setId, question, answer, choiceA, choiceB, choiceC);
    }

    // Update an existing flashcard in the database using the DAO
    public boolean updateFlashcard(int flashcardId, String question, String answer,
                                   String choiceA, String choiceB, String choiceC) throws Exception {
        FlashcardData data = new FlashcardData(question, answer, choiceA, choiceB, choiceC);
        validator.validate(data);
        return flashcardDao.updateFlashcard(flashcardId, question, answer, choiceA, choiceB, choiceC);
    }

    // Delete a flashcard from the database using the DAO
    public boolean deleteFlashcard(int flashcardId) throws Exception {
        return flashcardDao.deleteFlashcard(flashcardId);
    }

}
