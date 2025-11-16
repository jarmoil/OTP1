package dao;

import models.FlashcardSet;

import java.util.List;

public interface IFlashcardSetDao {
    // Retrieve all flashcard sets from the database
    List<FlashcardSet> getAllSets() throws Exception;

    // Retrieve flashcard sets based on user role (student or teacher)
    List<FlashcardSet> getSetsByRoleAndLocale(String role, String locale) throws Exception;

    // Retrieve a flashcard set by its ID
    FlashcardSet getSetById(int setId) throws Exception;

    // Create a new flashcard set in the database
    boolean createSet(int userId, String description, String locale) throws Exception;

    // Update flashcard set information in the database with the given ID
    boolean updateSet(int setId, String description) throws Exception;

    // Delete flashcard set from the database with the given ID
    boolean deleteSet(int setId) throws Exception;

    // Update set's correct percentage based on individual flashcard statistics
    boolean updateSetCorrectPercentage(int setId) throws Exception;
}
