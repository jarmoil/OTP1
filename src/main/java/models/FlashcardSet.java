package models;

import java.util.ArrayList;
import java.util.List;

public class FlashcardSet {
    private int setsId;
    private int userId;
    private String locale;
    private String description;
    private int setsCorrectPercentage;

    private List<Flashcard> flashcards;

    public FlashcardSet(int setsId, int userId, String locale, String description, int setsCorrectPercentage) {
        this.setsId = setsId;
        this.userId = userId;
        this.locale = locale;
        this.description = description;
        this.setsCorrectPercentage = setsCorrectPercentage;
        this.flashcards = new ArrayList<>();
    }

    // getters
    public int getSetsId() {
        return setsId;
    }
    public int getUserId() {
        return userId;
    }
    public String getLocale() {
        return locale;
    }
    public String getDescription() {
        return description;
    }
    public int getSetsCorrectPercentage() {
        return setsCorrectPercentage;
    }
    public List<Flashcard> getFlashcards() {
        return flashcards;
    }

    // setters
    public void setFlashcards(List<Flashcard> flashcards) {
        this.flashcards = flashcards;
    }
    public void setSetsId(int setsId) {
        this.setsId = setsId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public void setLocale(String locale) {
        this.locale = locale;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setSetsCorrectPercentage(int setsCorrectPercentage) {
        this.setsCorrectPercentage = setsCorrectPercentage;
    }
}

