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
import utils.SessionManager;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;

// TODO: Refactor this class to reduce complexity and improve readability

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
            showError("Please log in to view analytics.");
            createTeacherButton.setVisible(false);
            return;
        }

        try {
            String userRole = SessionManager.getCurrentUser().getRole();

            // Only allow teachers to access analytics
            if (!"teacher".equals(userRole)) {
                showError("Access denied. Only teachers can view analytics.");
                createTeacherButton.setVisible(false);
                return;
            }

            createTeacherButton.setVisible(true);
            loadOverviewStats();
            loadSetAnalytics();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Error loading analytics data.");
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
            List<Statistics> setStats = statsBySet.getOrDefault(set.getSets_id(), List.of());

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
            List<Flashcard> flashcards = flashcardService.getFlashcardsBySetId(set.getSets_id());
            List<Statistics> allStats = statisticsService.getAllStatistics();

            // Get all statistics for this specific set
            List<Statistics> setStats = allStats.stream()
                    .filter(stat -> stat.getSets_id() == set.getSets_id())
                    .collect(Collectors.toList());

            Dialog<ButtonType> dialog = FlashcardAnalyticsDialogController.createFlashcardAnalyticsDialog(
                    "Flashcard Analytics - " + set.getDescription(),
                    flashcards,
                    setStats
            );

            dialog.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Error loading flashcard details.");
        }
    }

    // Handle creating a new teacher account
    @FXML
    private void handleCreateTeacher() {
        if (SessionManager.getCurrentUser() == null ||
                !"teacher".equals(SessionManager.getCurrentUser().getRole())) {
            showError("Access denied. Only teachers can create teacher accounts.");
            return;
        }

        if (showCreateTeacherDialog()) {
            showSuccess("Teacher account created successfully!");
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
                showError("Error creating teacher account.");
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
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
