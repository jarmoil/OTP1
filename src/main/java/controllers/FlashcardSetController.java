package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import models.Flashcard;
import models.FlashcardSet;
import services.FlashcardService;
import services.FlashcardSetService;
import utils.SessionManager;
import java.util.List;
import java.util.Optional;

// Controller for managing and displaying flashcards within a flashcard set
public class FlashcardSetController {
    @FXML private Label setDescriptionLabel;
    @FXML private Button createFlashcardButton;
    @FXML private VBox flashcardsContainer;
    @FXML private Button startQuizButton;
    @FXML private Button updateSetButton;
    @FXML private Button deleteSetButton;


    // Services handle database operations
    private FlashcardService flashcardService = new FlashcardService();
    private FlashcardSet currentSet;
    private FlashcardSetService flashcardSetService = new FlashcardSetService();

    // Set is deleted -> go back to previous view
    private Runnable onSetDeletedCallback;

    public void setOnSetDeletedCallback(Runnable callback) {
        this.onSetDeletedCallback = callback;
    }

    // Initialize the controller with the given flashcard set
    public void initData(FlashcardSet set) {
        this.currentSet = set;
        setDescriptionLabel.setText(set.getDescription());
        updateUI();
        loadFlashcards();
    }

    // If owner of the set, show create flashcard, update set, delete set buttons
    private void updateUI() {
        boolean isOwner = SessionManager.getCurrentUser() != null &&
                SessionManager.getCurrentUser().getId() == currentSet.getUser_id();
        createFlashcardButton.setVisible(isOwner);
        updateSetButton.setVisible(isOwner);
        deleteSetButton.setVisible(isOwner);
    }

    // Load flashcards from the database using the service and create UI cards for each
    private void loadFlashcards() {
        try {
            List<Flashcard> flashcards = flashcardService.getFlashcardsBySetId(currentSet.getSets_id());
            flashcardsContainer.getChildren().clear();

            // Create a visual card for each flashcard
            for (Flashcard flashcard : flashcards) {
                Pane flashcardPane = createFlashcardPane(flashcard);
                flashcardsContainer.getChildren().add(flashcardPane);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Show dialog to edit set description
    @FXML
    private void handleUpdateSet() {
        TextInputDialog dialog = new TextInputDialog(currentSet.getDescription());
        dialog.setTitle("Update Set");
        dialog.setHeaderText("Update set description");
        dialog.setContentText("Description:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(description -> {
            try {
                flashcardSetService.updateSet(currentSet.getSets_id(), description);
                currentSet.setDescription(description);
                setDescriptionLabel.setText(description);
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Description");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Confirm and delete the entire set
    @FXML
    private void handleDeleteSet() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Set");
        alert.setHeaderText("Delete Flashcard Set");
        alert.setContentText("Are you sure you want to delete this set? This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                flashcardSetService.deleteSet(currentSet.getSets_id());
                // Tell parent controller to refresh and go back to sets list
                if (onSetDeletedCallback != null) {
                    onSetDeletedCallback.run();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // Show dialog to create new flashcard
    @FXML
    private void handleCreateFlashcard() {
        if (showFlashcardDialog("Create New Flashcard", "Create", null)) {
            loadFlashcards();
        }
    }

    // Create visual flashcard component with edit/delete buttons for owners
    private Pane createFlashcardPane(Flashcard flashcard) {
        boolean isOwner = SessionManager.getCurrentUser() != null &&
                SessionManager.getCurrentUser().getId() == currentSet.getUser_id();

        // Use factory method in FlashcardCardController to create the card
        FlashcardCardController controller = FlashcardCardController.createFlashcardCard(
                flashcard,
                isOwner,
                () -> handleUpdateFlashcard(flashcard),
                () -> handleDeleteFlashcard(flashcard)
        );

        return controller.getRoot();
    }

    // Show dialog to edit flashcard
    private void handleUpdateFlashcard(Flashcard flashcard) {
        if (showFlashcardDialog("Update Flashcard", "Update", flashcard)) {
            loadFlashcards();
        }
    }

    // Handle deleting a flashcard
    private void handleDeleteFlashcard(Flashcard flashcard) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Flashcard");
        alert.setHeaderText("Delete Flashcard");
        alert.setContentText("Are you sure you want to delete this flashcard? This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                flashcardService.deleteFlashcard(flashcard.getFlashcard_id());
                loadFlashcards();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Start quiz mode with all flashcards in this set
    @FXML
    private void handleStartQuiz() {
        try {
            List<Flashcard> flashcards = flashcardService.getFlashcardsBySetId(currentSet.getSets_id());

            // Can't quiz with no cards
            if (flashcards.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Quiz");
                alert.setHeaderText("Cannot Start Quiz");
                alert.setContentText("This set has no flashcards!");
                alert.showAndWait();
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/quiz.fxml"));
            Parent root = loader.load();
            QuizController controller = loader.getController();

            // Initialize quiz with flashcards, user ID, and set ID, -1 if not logged in (only logged in users can save stats)
            int userId = SessionManager.getCurrentUser() != null ?
                    SessionManager.getCurrentUser().getId() : -1;
            int setId = currentSet.getSets_id();
            controller.initQuiz(flashcards, userId, setId);

            flashcardsContainer.getChildren().clear();
            flashcardsContainer.getChildren().add(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Reusable dialog for creating/editing flashcards
    private boolean showFlashcardDialog(String title, String buttonText, Flashcard existingFlashcard) {
        // Use factory method to create dialog with form fields
        Dialog<ButtonType> dialog = FlashcardDialogController.createFlashcardDialog(title, buttonText, existingFlashcard);
        // Get controller to access form data
        FlashcardDialogController dialogController = (FlashcardDialogController) dialog.getDialogPane().getProperties().get("controller");

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            try {
                if (existingFlashcard == null) {
                    // new flashcard
                    flashcardService.createFlashcard(
                            currentSet.getSets_id(),
                            dialogController.getQuestion(),
                            dialogController.getAnswer(),
                            dialogController.getChoiceA(),
                            dialogController.getChoiceB(),
                            dialogController.getChoiceC()
                    );
                } else {
                    // update flashcard
                    flashcardService.updateFlashcard(
                            existingFlashcard.getFlashcard_id(),
                            dialogController.getQuestion(),
                            dialogController.getAnswer(),
                            dialogController.getChoiceA(),
                            dialogController.getChoiceB(),
                            dialogController.getChoiceC()
                    );
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
