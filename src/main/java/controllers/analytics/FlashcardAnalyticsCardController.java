package controllers.analytics;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import models.Flashcard;

// TODO: Refactor to reduce complexity and improve readability

public class FlashcardAnalyticsCardController {
    @FXML private Pane cardPane;
    @FXML private Label questionLabel;
    @FXML private Label choicesLabel;
    @FXML private Label correctAnswerLabel;
    @FXML private Label performanceLabel;

    // Update the card with flashcard data and performance rate
    public void setFlashcard(Flashcard flashcard, double performanceRate) {
        questionLabel.setText(flashcard.getQuestion());
        choicesLabel.setText(String.format("A) %s  B) %s  C) %s",
                flashcard.getChoice_a(), flashcard.getChoice_b(), flashcard.getChoice_c()));
        correctAnswerLabel.setText("Answer: " + flashcard.getAnswer());

        int timesAnswered = flashcard.getTimes_answered();
        int timesCorrect = flashcard.getTimes_correct();
        double accuracy = timesAnswered > 0 ? (double) timesCorrect / timesAnswered * 100 : 0;

        if (timesAnswered > 0) {
            performanceLabel.setText(String.format("Answered: %d | Correct: %d | Accuracy: %.1f%%",
                    timesAnswered, timesCorrect, accuracy));
            applyPerformanceColorClass((int) accuracy);
        } else {
            performanceLabel.setText("No attempts yet");
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
            FXMLLoader loader = new FXMLLoader(FlashcardAnalyticsCardController.class.getResource("/views/flashcardAnalyticsCard.fxml"));
            Pane cardPane = loader.load();
            FlashcardAnalyticsCardController controller = loader.getController();

            controller.setFlashcard(flashcard, performanceRate);

            return controller;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load flashcard analytics card", e);
        }
    }


    public Pane getRoot() {
        return cardPane;
    }
}
