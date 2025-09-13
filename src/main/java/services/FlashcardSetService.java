package services;

import dao.FlashcardSetDao;
import models.FlashcardSet;
import java.util.List;

public class FlashcardSetService {
    private FlashcardSetDao flashcardSetDao = new FlashcardSetDao();

    public List<FlashcardSet> getAllSets() throws Exception {
        return flashcardSetDao.getAllSets();
    }

    public boolean createSet(int userId, String description) throws Exception {
        return flashcardSetDao.createSet(userId, description);
    }
}
