package services;

import dao.FlashcardDao;
import models.Flashcard;
import java.util.List;

public class FlashcardService {
    private FlashcardDao flashcardDao = new FlashcardDao();

    public List<Flashcard> getFlashcardsBySetId(int setId) throws Exception {
        return flashcardDao.getFlashcardsBySetId(setId);
    }

    public boolean createFlashcard(int setId, String question, String answer,
                                   String choiceA, String choiceB, String choiceC) throws Exception {
        return flashcardDao.createFlashcard(setId, question, answer, choiceA, choiceB, choiceC);
    }
}
