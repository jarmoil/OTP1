package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import models.Statistics;
import models.FlashcardSet;
import services.StatisticsService;
import services.FlashcardSetService;
import utils.SessionManager;
import java.util.List;

// Controller for displaying student statistics
public class StatsController {
    @FXML private VBox statsContainer;
    @FXML private Label noStatsLabel;

    private final StatisticsService statisticsService = new StatisticsService();
    private final FlashcardSetService flashcardSetService = new FlashcardSetService();

    @FXML
    public void initialize() {
        loadStudentStatistics();
    }

    // Load statistics for the logged-in student
    private void loadStudentStatistics() {
        if (SessionManager.getCurrentUser() == null) {
            showNoStats("Please log in to view your statistics.");
            return;
        }

        // Fetch statistics from the service based on the current user's ID
        try {
            int userId = SessionManager.getCurrentUser().getId();
            List<Statistics> userStats = statisticsService.getStatisticsByUser(userId);

            if (userStats.isEmpty()) {
                showNoStats("No quiz attempts yet. Take some quizzes to see your progress!");
                return;
            }

            statsContainer.getChildren().clear();

            for (Statistics stat : userStats) {
                try {
                    FlashcardSet set = flashcardSetService.getSetById(stat.getSets_id());
                    StatCardController cardController = StatCardController.createStatCard(stat, set);
                    statsContainer.getChildren().add(cardController.getRoot());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            showError("Error loading statistics.");
        }
    }

    // Show a message when there are no statistics to display
    private void showNoStats(String message) {
        statsContainer.getChildren().clear();
        noStatsLabel.setText(message);
        statsContainer.getChildren().add(noStatsLabel);
    }

    // Show an error alert dialog
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Statistics Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
