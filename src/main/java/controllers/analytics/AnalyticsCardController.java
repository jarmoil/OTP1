package controllers.analytics;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import models.FlashcardSet;
import utils.LanguageManager;

// TODO: Refactor to reduce complexity and improve readability

// Controller for displaying an analytics card with performance indicators
public class AnalyticsCardController {
    @FXML private Pane cardPane;
    @FXML private Label setNameLabel;
    @FXML private Label setPerformanceLabel;
    @FXML private Label avgPerfLabel;
    @FXML private Label performanceIndicator;
    @FXML private Label clickHint;

    private Runnable onClickCallback;

    // Set analytics data and update UI elements
    public void setAnalyticsData(FlashcardSet set, double avgStudentPerformance) {
        setNameLabel.setText(set.getDescription());
        setPerformanceLabel.setText(LanguageManager.getString("analyticsCardController.overall") + set.getSets_correct_percentage() + "%");
        avgPerfLabel.setText(LanguageManager.getString("analyticsCardController.avgScore") + avgStudentPerformance);

        int perfPercentage = (int) avgStudentPerformance;
        performanceIndicator.setText(getPerformanceStatus(perfPercentage));
        applyPerformanceColorClass(perfPercentage);
    }


    // Set callback for click events on the card
    public void setOnClickCallback(Runnable callback) {
        this.onClickCallback = callback;
        cardPane.setStyle("-fx-cursor: hand;");
        cardPane.setOnMouseClicked(e -> {
            if (callback != null) {
                callback.run();
            }
        });
    }

    // Determine performance status symbol based on percentage
    private String getPerformanceStatus(int percentage) {
        if (percentage >= 80) return "✓";
        if (percentage >= 60) return "~";
        return "⚠";
    }

    // Apply color class to performance indicators based on percentage
    private void applyPerformanceColorClass(int percentage) {
        performanceIndicator.getStyleClass().removeAll("performance-good", "performance-average", "performance-poor");
        avgPerfLabel.getStyleClass().removeAll("performance-good", "performance-average", "performance-poor");

        String colorClass = getPerformanceColorClass(percentage);
        performanceIndicator.getStyleClass().add(colorClass);
        avgPerfLabel.getStyleClass().add(colorClass);
    }

    // Determine color class based on performance percentage
    private String getPerformanceColorClass(int percentage) {
        if (percentage >= 80) return "performance-good";
        if (percentage >= 60) return "performance-average";
        return "performance-poor";
    }

    // Factory method to create and initialize an AnalyticsCardController instance
    public static AnalyticsCardController createAnalyticsCard(FlashcardSet set,
                                                              double avgStudentPerformance,
                                                              Runnable onClickCallback) {
        try {
            FXMLLoader loader = new FXMLLoader(AnalyticsCardController.class.getResource("/views/analyticsCard.fxml"), utils.LanguageManager.getResourceBundle());
            Pane cardPane = loader.load();
            AnalyticsCardController controller = loader.getController();

            controller.setAnalyticsData(set, avgStudentPerformance);
            controller.setOnClickCallback(onClickCallback);

            return controller;
        } catch (Exception e) {
            throw new RuntimeException(LanguageManager.getString("analyticsCardController.failed") + e);
        }
    }

    public Pane getRoot() {
        return cardPane;
    }
}
