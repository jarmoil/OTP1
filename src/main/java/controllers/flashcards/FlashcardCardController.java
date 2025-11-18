package controllers.flashcards;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Flashcard;
import utils.LanguageManager;

// Controller for displaying a single flashcard card with question, choices, and owner controls
public class FlashcardCardController {
    @FXML private VBox cardPane;
    @FXML private Label questionLabel;
    @FXML private Label choicesLabel;
    @FXML private HBox buttonBox;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;

    // Data and callbacks from parent controller
    private Runnable onUpdateCallback;
    private Runnable onDeleteCallback;

    public void setFlashcard(Flashcard flashcard) {
        questionLabel.setText(flashcard.getQuestion());

        String choicesTitle = utils.LanguageManager.getString("flashcardCardController.choices");
        String aPrefix = utils.LanguageManager.getString("flashcardCardController.a");
        String bPrefix = utils.LanguageManager.getString("flashcardCardController.b");
        String cPrefix = utils.LanguageManager.getString("flashcardCardController.c");


        choicesLabel.setText(String.format("%s%n%s %s%n%s %s%n%s %s", choicesTitle,
                aPrefix, flashcard.getChoice_a(),
                bPrefix, flashcard.getChoice_b(),
                cPrefix, flashcard.getChoice_c()));
                }

    // Show/hide edit buttons based on ownership
    public void setOwnerControlsVisible(boolean visible) {
        buttonBox.setVisible(visible);
        buttonBox.setManaged(visible);
    }

    public void setOnUpdateCallback(Runnable callback) {
        this.onUpdateCallback = callback;
    }

    public void setOnDeleteCallback(Runnable callback) {
        this.onDeleteCallback = callback;
    }

    @FXML
    private void handleUpdate() {
        if (onUpdateCallback != null) {
            onUpdateCallback.run();
        }
    }

    @FXML
    private void handleDelete() {
        if (onDeleteCallback != null) {
            onDeleteCallback.run();
        }
    }

    // Factory method, creates complete flashcard card with all setup
    public static FlashcardCardController createFlashcardCard(Flashcard flashcard, boolean isOwner,
                                                              Runnable onUpdate, Runnable onDelete) {
        try {
            // Load FXML layout and get controller
            FXMLLoader loader = new FXMLLoader(FlashcardCardController.class.getResource("/views/flashcardCard.fxml"), utils.LanguageManager.getResourceBundle());
            VBox flashcardPane = loader.load();
            FlashcardCardController controller = loader.getController();

            flashcardPane.getStylesheets().add(FlashcardCardController.class.getResource("/css/global.css").toExternalForm());

            // Configure the card with data and callbacks
            controller.setFlashcard(flashcard);
            controller.setOwnerControlsVisible(isOwner);
            controller.setOnUpdateCallback(onUpdate);
            controller.setOnDeleteCallback(onDelete);

            return controller;
        } catch (Exception e) {
            throw new RuntimeException(LanguageManager.getString("flashcardCardController.failed") + e);
        }
    }

    public VBox getRoot() {
        return cardPane;
    }
}
