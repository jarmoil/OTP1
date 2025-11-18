package controllers.analytics;

import factory.ServiceFactory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.Flashcard;
import models.Statistics;
import models.FlashcardSet;
import services.StatisticsService;
import services.FlashcardSetService;
import services.UserService;
import services.FlashcardService;
import utils.LanguageManager;
import utils.SessionManager;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;

// Controller for managing and displaying analytics data for teachers
public class AnalyticsController {
    @FXML private Label averagePerformanceLabel;
    @FXML private Label totalSetsLabel;
    @FXML private VBox analyticsContainer;
    @FXML private Button createTeacherButton;
    @FXML private ScrollPane analyticsScrollPane;

    private StatisticsService statisticsService;
    private FlashcardSetService flashcardSetService;
    private UserService userService;
    private FlashcardService flashcardService;

    @FXML
    public void initialize() {
        statisticsService = ServiceFactory.getInstance().getStatisticsService();
        flashcardSetService = ServiceFactory.getInstance().getFlashcardSetService();
        userService = ServiceFactory.getInstance().getUserService();
        flashcardService = ServiceFactory.getInstance().getFlashcardService();
        loadAnalytics();
    }

    // Load analytics data if user is a teacher
    private void loadAnalytics() {
        if (SessionManager.getCurrentUser() == null) {
            showError(LanguageManager.getString("analyticsController.login"));
            createTeacherButton.setVisible(false);
            return;
        }

        try {
            String userRole = SessionManager.getCurrentUser().getRole();

            // Only allow teachers to access analytics
            if (!"teacher".equals(userRole)) {
                showError(LanguageManager.getString("analyticsController.accessDenied"));
                createTeacherButton.setVisible(false);
                return;
            }

            createTeacherButton.setVisible(true);
            loadOverviewStats();
            loadSetAnalytics();

        } catch (Exception e) {
            e.printStackTrace();
            showError(LanguageManager.getString("analyticsController.error"));
            createTeacherButton.setVisible(false);
        }
    }

    // Load overall statistics like average performance and total sets
    private void loadOverviewStats() throws Exception {
        List<Statistics> allStats = statisticsService.getAllStatistics();
        List<FlashcardSet> allSets = flashcardSetService.getAllSets();

        double avgPerformance = allStats.isEmpty() ? 0.0 :
                allStats.stream().mapToInt(Statistics::getStats_correct_percentage).average().orElse(0.0);
        int totalSets = allSets.size();

        averagePerformanceLabel.setText(String.format("%.1f%%", avgPerformance));
        totalSetsLabel.setText(String.valueOf(totalSets));
    }

    // Load analytics for each flashcard set
    private void loadSetAnalytics() throws Exception {
        List<FlashcardSet> allSets = flashcardSetService.getAllSets();
        List<Statistics> allStats = statisticsService.getAllStatistics();

        Map<Integer, List<Statistics>> statsBySet = allStats.stream()
                .collect(Collectors.groupingBy(Statistics::getSets_id));

        analyticsContainer.getChildren().clear();

        for (FlashcardSet set : allSets) {
            List<Statistics> setStats = statsBySet.getOrDefault(set.getSetsId(), java.util.Collections.emptyList());

            // Calculate average student performance
            double avgStudentPerf = setStats.isEmpty() ? 0.0 :
                    setStats.stream().mapToInt(Statistics::getStats_correct_percentage).average().orElse(0.0);

            // Create analytics card using factory method
            AnalyticsCardController cardController = AnalyticsCardController.createAnalyticsCard(
                    set,
                    avgStudentPerf,
                    () -> showSetDetails(set)
            );

            analyticsContainer.getChildren().add(cardController.getRoot());
        }
    }

    // Show detailed analytics for a specific flashcard set
    private void showSetDetails(FlashcardSet set) {
        try {
            List<Flashcard> flashcards = flashcardService.getFlashcardsBySetId(set.getSetsId());
            List<Statistics> allStats = statisticsService.getAllStatistics();

            // Get all statistics for this specific set
            List<Statistics> setStats = allStats.stream()
                    .filter(stat -> stat.getSets_id() == set.getSetsId())
                    .toList();

            Dialog<ButtonType> dialog = FlashcardAnalyticsDialogController.createFlashcardAnalyticsDialog(
                    LanguageManager.getString("analyticsController.flashcardAnalytics") + set.getDescription(),
                    flashcards,
                    setStats
            );

            dialog.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            showError(LanguageManager.getString("analyticsController.error1"));
        }
    }

    // Handle creating a new teacher account
    @FXML
    private void handleCreateTeacher() {
        if (SessionManager.getCurrentUser() == null ||
                !"teacher".equals(SessionManager.getCurrentUser().getRole())) {
            showError(LanguageManager.getString("analyticsController.accessDenied1"));
            return;
        }

        if (showCreateTeacherDialog()) {
            showSuccess(LanguageManager.getString("analyticsController.successfulCreation"));
        }
    }

    // Show dialog to create a new teacher account
    private boolean showCreateTeacherDialog() {
        Dialog<ButtonType> dialog = CreateTeacherDialogController.createTeacherDialog();
        CreateTeacherDialogController dialogController =
                (CreateTeacherDialogController) dialog.getDialogPane().getProperties().get("controller");

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            try {
                return userService.createTeacher(
                        dialogController.getUsername(),
                        dialogController.getPassword()
                );
            } catch (Exception e) {
                e.printStackTrace();
                showError(LanguageManager.getString("analyticsController.error2"));
            }
        }
        return false;
    }

    // Show an error message in the analytics container
    private void showError(String message) {
        analyticsContainer.getChildren().clear();
        Label errorLabel = new Label(message);
        errorLabel.setTextFill(javafx.scene.paint.Color.LIGHTCORAL);
        errorLabel.setStyle("-fx-font-size: 16px;");
        analyticsContainer.getChildren().add(errorLabel);
    }

    // Show a success alert dialog
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(LanguageManager.getString("analyticsController.success"));
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
