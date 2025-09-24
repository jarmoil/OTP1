package services;

import dao.FlashcardSetDao;
import models.FlashcardSet;
import java.util.List;

// Service class for flashcard set-related operations
public class FlashcardSetService {
    private FlashcardSetDao flashcardSetDao = new FlashcardSetDao();

    // Retrieve all flashcard sets from the database using the DAO
    public List<FlashcardSet> getAllSets() throws Exception {
        return flashcardSetDao.getAllSets();
    }

    // Retrieve flashcard sets based on user role (student or teacher) using the DAO
    public List<FlashcardSet> getSetsByRole(String role) throws Exception {
        return flashcardSetDao.getSetsByRole(role);
    }

    // Create a new flashcard set in the database using the DAO
    public boolean createSet(int userId, String description) throws Exception {
        return flashcardSetDao.createSet(userId, description);
    }

    // Update flashcard set information
    public boolean updateSet(int setId, String newDescription) throws Exception {
        if (newDescription.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        return flashcardSetDao.updateSet(setId, newDescription);
    }

    // Delete a flashcard set by ID
    public boolean deleteSet(int setId) throws Exception {
        return flashcardSetDao.deleteSet(setId);
    }

    // Retrieve a flashcard set by its ID
    public FlashcardSet getSetById(int setId) throws Exception {
        return flashcardSetDao.getSetById(setId);
    }

}
