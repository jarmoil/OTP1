package controllers.analytics;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import models.Flashcard;
import utils.LanguageManager;

// TODO: Refactor to reduce complexity and improve readability

public class FlashcardAnalyticsCardController {
    @FXML private Pane cardPane;
    @FXML private Label questionLabel;
    @FXML private Label choicesLabel;
    @FXML private Label correctAnswerLabel;
    @FXML private Label performanceLabel;

    // Update the card with flashcard data and performance rate
    public void setFlashcard(Flashcard flashcard, double performanceRate) {
        String a = LanguageManager.getString("flashcardAnalyticsCardController.a");
        String b = LanguageManager.getString("flashcardAnalyticsCardController.b");
        String c = LanguageManager.getString("flashcardAnalyticsCardController.c");
        String answerText = LanguageManager.getString("flashcardAnalyticsCardController.answer");
        String answeredText = LanguageManager.getString("flashcardAnalyticsCardController.answered");
        String correctText = LanguageManager.getString("flashcardAnalyticsCardController.correct");
        String accuracyText = LanguageManager.getString("flashcardAnalyticsCardController.accuracy");
        String noAttemptsText = LanguageManager.getString("flashcardAnalyticsCardController.noAttempts");

        questionLabel.setText(flashcard.getQuestion());
        choicesLabel.setText(String.format("%s %s  %s %s  %s %s",
                a, flashcard.getChoice_a(),
                b, flashcard.getChoice_b(),
                c, flashcard.getChoice_c()));

        correctAnswerLabel.setText(String.format("%s %s", answerText, flashcard.getAnswer()));

        int timesAnswered = flashcard.getTimes_answered();
        int timesCorrect = flashcard.getTimes_correct();
        double accuracy = timesAnswered > 0 ? (double) timesCorrect / timesAnswered * 100 : 0;

        if (timesAnswered > 0) {
            performanceLabel.setText(String.format("%s %d | %s %d | %s %.1f%%",
                    answeredText, timesAnswered,
                    correctText, timesCorrect,
                    accuracyText, accuracy));
            applyPerformanceColorClass((int) Math.round(accuracy));
        } else {
            performanceLabel.setText(noAttemptsText);
            performanceLabel.setStyle("-fx-text-fill: #888888;");
        }
    }


    // Apply CSS color class to performance label
    private void applyPerformanceColorClass(int percentage) {
        performanceLabel.getStyleClass().removeAll("performance-good", "performance-average", "performance-poor");
        String colorClass = getPerformanceColorClass(percentage);
        performanceLabel.getStyleClass().add(colorClass);
    }

    // Determine color class based on performance percentage
    private String getPerformanceColorClass(int percentage) {
        if (percentage >= 80) return "performance-good";
        if (percentage >= 60) return "performance-average";
        return "performance-poor";
    }


    // Factory method to create and initialize a FlashcardAnalyticsCardController instance
    public static FlashcardAnalyticsCardController createFlashcardAnalyticsCard(Flashcard flashcard, double performanceRate) {
        try {
            FXMLLoader loader = new FXMLLoader(FlashcardAnalyticsCardController.class.getResource("/views/flashcardAnalyticsCard.fxml"), utils.LanguageManager.getResourceBundle());
            Pane cardPane = loader.load();
            FlashcardAnalyticsCardController controller = loader.getController();

            controller.setFlashcard(flashcard, performanceRate);

            return controller;
        } catch (Exception e) {
            throw new RuntimeException(LanguageManager.getString("flashcardAnalyticsCardController.failed") + e);
        }
    }


    public Pane getRoot() {
        return cardPane;
    }
}
