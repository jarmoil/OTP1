package controllers.flashcards;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import models.Flashcard;

// TODO: Refactor this class to reduce complexity and improve readability

// Controller for the flashcard creation/editing dialog
public class FlashcardDialogController {
    @FXML private TextField questionField;
    @FXML private TextField choiceAField;
    @FXML private TextField choiceBField;
    @FXML private TextField choiceCField;
    @FXML private ComboBox<String> answerBox;

    @FXML
    private void initialize() {
        // Update answer choices when any choice field changes
        choiceAField.textProperty().addListener((obs, old, newVal) -> updateAnswerChoices());
        choiceBField.textProperty().addListener((obs, old, newVal) -> updateAnswerChoices());
        choiceCField.textProperty().addListener((obs, old, newVal) -> updateAnswerChoices());
    }

    public void setFlashcard(Flashcard flashcard) {
        if (flashcard != null) {
            questionField.setText(flashcard.getQuestion());
            choiceAField.setText(flashcard.getChoice_a());
            choiceBField.setText(flashcard.getChoice_b());
            choiceCField.setText(flashcard.getChoice_c());
            updateAnswerChoices();
            answerBox.setValue(flashcard.getAnswer());
        }
    }

    // Getters
    public String getQuestion() { return questionField.getText(); }
    public String getChoiceA() { return choiceAField.getText(); }
    public String getChoiceB() { return choiceBField.getText(); }
    public String getChoiceC() { return choiceCField.getText(); }
    public String getAnswer() { return answerBox.getValue(); }

    private void updateAnswerChoices() {
        answerBox.getItems().clear();
        if (!choiceAField.getText().trim().isEmpty()) answerBox.getItems().add(choiceAField.getText());
        if (!choiceBField.getText().trim().isEmpty()) answerBox.getItems().add(choiceBField.getText());
        if (!choiceCField.getText().trim().isEmpty()) answerBox.getItems().add(choiceCField.getText());
    }

    // Factory method, creates complete dialog with form and buttons

    public static Dialog<ButtonType> createFlashcardDialog(String title, String buttonText,
                                                           Flashcard existingFlashcard) {
        try {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle(title);
            dialog.setHeaderText(existingFlashcard == null ? "Enter flashcard details" : "Modify flashcard details");

            ButtonType actionButton = new ButtonType(buttonText, ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(actionButton, ButtonType.CANCEL);

            // Load the form layout from FXML file
            FXMLLoader loader = new FXMLLoader(FlashcardDialogController.class.getResource("/views/flashcardDialog.fxml"));
            GridPane dialogContent = loader.load();
            FlashcardDialogController controller = loader.getController();

            if (existingFlashcard != null) {
                controller.setFlashcard(existingFlashcard);
            }

            dialog.getDialogPane().setContent(dialogContent);
            dialog.getDialogPane().getProperties().put("controller", controller);

            return dialog;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create flashcard dialog", e);
        }
    }
}

