package models;

import java.util.ArrayList;
import java.util.List;

public class FlashcardSet {
    private int sets_id;
    private int user_id;
    private String locale;
    private String description;
    private int sets_correct_percentage;

    private List<Flashcard> flashcards;

    public FlashcardSet(int sets_id, int user_id, String locale, String description, int sets_correct_percentage) {
        this.sets_id = sets_id;
        this.user_id = user_id;
        this.locale = locale;
        this.description = description;
        this.sets_correct_percentage = sets_correct_percentage;
        this.flashcards = new ArrayList<>();
    }

    // getters
    public int getSets_id() {
        return sets_id;
    }
    public int getUser_id() {
        return user_id;
    }
    public String getLocale() {
        return locale;
    }
    public String getDescription() {
        return description;
    }
    public int getSets_correct_percentage() {
        return sets_correct_percentage;
    }
    public List<Flashcard> getFlashcards() {
        return flashcards;
    }

    // setters
    public void setFlashcards(List<Flashcard> flashcards) {
        this.flashcards = flashcards;
    }
    public void setSets_id(int sets_id) {
        this.sets_id = sets_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public void setLocale(String locale) {
        this.locale = locale;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setSets_correct_percentage(int sets_correct_percentage) {
        this.sets_correct_percentage = sets_correct_percentage;
    }
}

