package controllers.stats;

import factory.ServiceFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import models.Statistics;
import models.FlashcardSet;
import services.StatisticsService;
import services.FlashcardSetService;
import utils.LanguageManager;
import utils.SessionManager;
import java.util.List;

// Controller for displaying student statistics
public class StatsController {
    @FXML private VBox statsContainer;
    @FXML private Label noStatsLabel;

    private StatisticsService statisticsService;
    private FlashcardSetService flashcardSetService;

    @FXML
    public void initialize() {
        statisticsService = ServiceFactory.getInstance().getStatisticsService();
        flashcardSetService = ServiceFactory.getInstance().getFlashcardSetService();
        loadStudentStatistics();
    }

    private void addStatCard(Statistics stat) {
        try {
            FlashcardSet set = flashcardSetService.getSetById(stat.getSets_id());
            StatCardController cardController = StatCardController.createStatCard(stat, set);
            statsContainer.getChildren().add(cardController.getRoot());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load statistics for the logged-in student
    private void loadStudentStatistics() {
        if (SessionManager.getCurrentUser() == null) {
            showNoStats(LanguageManager.getString("statsController.noStatsLoaded"));
            return;
        }

        // Fetch statistics from the service based on the current user's ID
        try {
            int userId = SessionManager.getCurrentUser().getId();
            List<Statistics> userStats = statisticsService.getStatisticsByUser(userId);

            if (userStats.isEmpty()) {
                showNoStats(LanguageManager.getString("statsController.statsEmpty"));
                return;
            }

            statsContainer.getChildren().clear();

            for (Statistics stat : userStats) {
                addStatCard(stat);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showError(LanguageManager.getString("statsController.loadingError"));
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
        alert.setTitle(LanguageManager.getString("statsController.error"));
        alert.setHeaderText(LanguageManager.getString("statsController.statsError"));
        alert.setContentText(message);
        alert.showAndWait();
    }
}
