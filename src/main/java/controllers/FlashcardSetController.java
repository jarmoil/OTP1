package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import models.Flashcard;
import models.FlashcardSet;
import services.FlashcardService;
import utils.SessionManager;
import java.util.List;
import java.util.Optional;

// Controller for managing and displaying flashcards within a flashcard set

public class FlashcardSetController {
    @FXML private Label setDescriptionLabel;
    @FXML private Button createFlashcardButton;
    @FXML private VBox flashcardsContainer;
    @FXML private Button startQuizButton;


    private FlashcardService flashcardService = new FlashcardService();
    private FlashcardSet currentSet;

    // Initialize the controller with the given flashcard set
    public void initData(FlashcardSet set) {
        this.currentSet = set;
        setDescriptionLabel.setText(set.getDescription());
        updateUI();
        loadFlashcards();
    }

    // If owner of the set, show create flashcard button
    private void updateUI() {
        boolean isOwner = SessionManager.getCurrentUser() != null &&
                SessionManager.getCurrentUser().getId() == currentSet.getUser_id();
        createFlashcardButton.setVisible(isOwner);
    }

    // Load flashcards from the service and display them
    private void loadFlashcards() {
        try {
            List<Flashcard> flashcards = flashcardService.getFlashcardsBySetId(currentSet.getSets_id());
            flashcardsContainer.getChildren().clear();

            for (Flashcard flashcard : flashcards) {
                flashcardsContainer.getChildren().add(createFlashcardPane(flashcard));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Handle creating a new flashcard, showing a dialog to input details and using the service to save it to the database
    @FXML
    private void handleCreateFlashcard() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Create New Flashcard");
        dialog.setHeaderText("Enter flashcard details");

        ButtonType createButton = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField questionField = new TextField();
        TextField answerField = new TextField();
        TextField choiceAField = new TextField();
        TextField choiceBField = new TextField();
        TextField choiceCField = new TextField();

        grid.add(new Label("Question:"), 0, 0);
        grid.add(questionField, 1, 0);
        grid.add(new Label("Answer:"), 0, 1);
        grid.add(answerField, 1, 1);
        grid.add(new Label("Choice A:"), 0, 2);
        grid.add(choiceAField, 1, 2);
        grid.add(new Label("Choice B:"), 0, 3);
        grid.add(choiceBField, 1, 3);
        grid.add(new Label("Choice C:"), 0, 4);
        grid.add(choiceCField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == createButton) {
            try {
                flashcardService.createFlashcard(
                        currentSet.getSets_id(),
                        questionField.getText(),
                        answerField.getText(),
                        choiceAField.getText(),
                        choiceBField.getText(),
                        choiceCField.getText()
                );
                loadFlashcards();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Create a pane representing a single flashcard with question and choices
    private Pane createFlashcardPane(Flashcard flashcard) {
        VBox cardPane = new VBox(10);
        cardPane.getStyleClass().add("flashcard");
        cardPane.setPadding(new javafx.geometry.Insets(15));

        Label questionLabel = new Label(flashcard.getQuestion());
        questionLabel.getStyleClass().add("flashcard-question");
        questionLabel.setWrapText(true);


        Label choicesLabel = new Label(String.format("Choices:\nA) %s\nB) %s\nC) %s",
                flashcard.getChoice_a(), flashcard.getChoice_b(), flashcard.getChoice_c()));
        choicesLabel.getStyleClass().add("flashcard-choices");
        choicesLabel.setWrapText(true);

        cardPane.getChildren().addAll(questionLabel, choicesLabel);
        return cardPane;
    }

    // Placeholder for starting a quiz based on the flashcards in the set
    @FXML
    private void handleStartQuiz() {
        // TODO: Implement quiz functionality
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Quiz");
        alert.setHeaderText("Starting Quiz");
        alert.setContentText("Quiz functionality is not yet ready");
        alert.showAndWait();
    }

}
