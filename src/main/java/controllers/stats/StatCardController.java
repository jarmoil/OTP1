package controllers.stats;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import models.Statistics;
import models.FlashcardSet;

// Controller for displaying a single statistic card with performance color coding
public class StatCardController {
    @FXML private Pane cardPane;
    @FXML private Label setNameLabel;
    @FXML private Label percentageLabel;
    @FXML private Label bestScoreLabel;

    // Set statistic data and update UI
    public void setStatistic(Statistics stat, FlashcardSet set) {
        String setName = set != null ? set.getDescription() : "Unknown Set";
        setNameLabel.setText(setName);
        percentageLabel.setText(stat.getStats_correct_percentage() + "%");
        bestScoreLabel.setText("Best Score");

        // Apply performance color styling
        String colorClass = getPerformanceColorClass(stat.getStats_correct_percentage());
        percentageLabel.getStyleClass().add(colorClass);
    }

    // Determine color class based on performance percentage
    private String getPerformanceColorClass(int percentage) {
        if (percentage >= 80) return "performance-good";
        if (percentage >= 60) return "performance-average";
        return "performance-poor";
    }

    // Factory method to create and initialize a StatCardController instance
    public static StatCardController createStatCard(Statistics stat, FlashcardSet set) {
        try {
            FXMLLoader loader = new FXMLLoader(StatCardController.class.getResource("/views/statCard.fxml"));
            Pane statPane = loader.load();
            StatCardController controller = loader.getController();
            controller.setStatistic(stat, set);
            return controller;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load stat card", e);
        }
    }

    public Pane getRoot() {
        return cardPane;
    }
}
