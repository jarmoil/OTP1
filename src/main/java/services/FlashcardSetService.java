package services;

import dao.FlashcardSetDao;
import models.FlashcardSet;
import java.util.List;

// Service class for flashcard set-related operations
public class FlashcardSetService {
    // TODO: Implement update and delete methods for flashcard sets
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
}
