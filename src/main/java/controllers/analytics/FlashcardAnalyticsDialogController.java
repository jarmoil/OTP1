package controllers.analytics;

import exceptions.UILoadException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.Flashcard;
import models.Statistics;
import utils.LanguageManager;

import java.util.List;

public class FlashcardAnalyticsDialogController {
    @FXML private ScrollPane scrollPane;
    @FXML private VBox container;

    // Populate the dialog with flashcard analytics cards
    public void setFlashcards(List<Flashcard> flashcards) {
        container.getChildren().clear();

        for (Flashcard flashcard : flashcards) {
            double performanceRate = calculateFlashcardPerformance(flashcard);

            FlashcardAnalyticsCardController cardController =
                    FlashcardAnalyticsCardController.createFlashcardAnalyticsCard(flashcard, performanceRate);
            container.getChildren().add(cardController.getRoot());
        }
    }

    // Create and return a dialog displaying flashcard analytics
    public static Dialog<ButtonType> createFlashcardAnalyticsDialog(String title, List<Flashcard> flashcards, List<Statistics> setStats) {
        try {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle(title);
            dialog.setHeaderText(LanguageManager.getString("flashcardAnalyticsDialogController.detailedStats"));

            dialog.getDialogPane().getStylesheets().add(
                    FlashcardAnalyticsDialogController.class.getResource("/css/MainWindowStyle.css").toExternalForm());
            dialog.getDialogPane().setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white;");

            FXMLLoader loader = new FXMLLoader(FlashcardAnalyticsDialogController.class.getResource("/views/flashcardAnalyticsDialog.fxml"), utils.LanguageManager.getResourceBundle());
            ScrollPane dialogContent = loader.load();
            FlashcardAnalyticsDialogController controller = loader.getController();

            controller.setFlashcards(flashcards);

            dialog.getDialogPane().setContent(dialogContent);

            ButtonType closeType = new ButtonType(
                    LanguageManager.getString("flashcardAnalyticsDialogController.close"),
                    ButtonBar.ButtonData.CANCEL_CLOSE
            );
            dialog.getDialogPane().getButtonTypes().setAll(closeType);

            Button closeButton = (Button) dialog.getDialogPane().lookupButton(closeType);
            closeButton.setStyle("-fx-background-color: #4a4a4a; -fx-text-fill: white; -fx-border-color: #666666;");

            return dialog;
        } catch (Exception e) {
            throw new UILoadException(LanguageManager.getString("flashcardAnalyticsDialogController.failed"), e);
        }
    }

    // Calculate performance percentage for a flashcard
    private double calculateFlashcardPerformance(Flashcard flashcard) {
        int timesAnswered = flashcard.getTimesAnswered();
        if (timesAnswered == 0) return 0.0;

        return (double) flashcard.getTimesCorrect() / timesAnswered * 100.0;
    }

}
