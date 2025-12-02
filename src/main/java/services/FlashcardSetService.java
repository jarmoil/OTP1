package services;

import dao.IFlashcardSetDao;
import exceptions.DataOperationException;
import models.FlashcardSet;
import java.util.List;

// Service class for flashcard set-related operations
public class FlashcardSetService {
    private final IFlashcardSetDao flashcardSetDao;

    public FlashcardSetService(IFlashcardSetDao flashcardSetDao) {
        this.flashcardSetDao = flashcardSetDao;
    }

    // Retrieve all flashcard sets from the database using the DAO
    public List<FlashcardSet> getAllSets() throws DataOperationException {
        return flashcardSetDao.getAllSets();
    }

    // Retrieve flashcard sets based on user role (student or teacher) using the DAO
    public List<FlashcardSet> getSetsByRoleAndLocale(String role, String locale) throws DataOperationException {
        return flashcardSetDao.getSetsByRoleAndLocale(role, locale);
    }

    // Create a new flashcard set in the database using the DAO
    public boolean createSet(int userId, String description, String locale) throws DataOperationException {
        return flashcardSetDao.createSet(userId, description, locale);
    }

    // Update flashcard set information
    public boolean updateSet(int setId, String newDescription) throws DataOperationException {
        if (newDescription.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        return flashcardSetDao.updateSet(setId, newDescription);
    }

    // Delete a flashcard set by ID
    public boolean deleteSet(int setId) throws DataOperationException {
        return flashcardSetDao.deleteSet(setId);
    }

    // Retrieve a flashcard set by its ID
    public FlashcardSet getSetById(int setId) throws DataOperationException {
        return flashcardSetDao.getSetById(setId);
    }

    public void updateSetCorrectPercentage(int setId) throws DataOperationException {
        flashcardSetDao.updateSetCorrectPercentage(setId);
    }

}
