package controllers.flashcards;

import controllers.QuizController;
import factory.ServiceFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import models.Flashcard;
import models.FlashcardSet;
import services.FlashcardService;
import services.FlashcardSetService;
import utils.LanguageManager;
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

    private static final String GLOBAL_CSS = "/css/global.css";

    // Services handle database operations
    private FlashcardService flashcardService;
    private FlashcardSet currentSet;
    private FlashcardSetService flashcardSetService;

    // Set is deleted -> go back to previous view
    private Runnable onSetDeletedCallback;

    @FXML
    public void initialize() {
        flashcardService = ServiceFactory.getInstance().getFlashcardService();
        flashcardSetService = ServiceFactory.getInstance().getFlashcardSetService();
    }

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
        dialog.setTitle(LanguageManager.getString("flashcardSetController.updateSet"));
        dialog.setHeaderText(LanguageManager.getString("flashcardSetController.updateSetDesc"));
        dialog.setContentText(LanguageManager.getString("flashcardSetController.description"));

        dialog.getDialogPane().getStylesheets().add(getClass().getResource(GLOBAL_CSS).toExternalForm());

        ButtonType okButton = new ButtonType(LanguageManager.getString("flashcardSetController.ok"), ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType(LanguageManager.getString("flashcardSetController.cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().setAll(okButton, cancelButton);

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(description -> {
            try {
                flashcardSetService.updateSet(currentSet.getSets_id(), description);
                currentSet.setDescription(description);
                setDescriptionLabel.setText(description);
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(LanguageManager.getString("flashcardSetController.error"));
                alert.setHeaderText(LanguageManager.getString("flashcardSetController.invalidDesc"));
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
        alert.setTitle(LanguageManager.getString("flashcardSetController.deleteSet"));
        alert.setHeaderText(LanguageManager.getString("flashcardSetController.deleteFlashcardSet"));
        alert.setContentText(LanguageManager.getString("flashcardSetController.delSetConfirmationText"));

        alert.getDialogPane().getStylesheets().add(getClass().getResource(GLOBAL_CSS).toExternalForm());

        ButtonType okButton = new ButtonType(LanguageManager.getString("flashcardSetController.ok"), ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType(LanguageManager.getString("flashcardSetController.cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getDialogPane().getButtonTypes().setAll(okButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
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
        if (showFlashcardDialog(LanguageManager.getString("flashcardSetController.createNewFlashcard"), LanguageManager.getString("flashcardSetController.create"), null)) {
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
        if (showFlashcardDialog(LanguageManager.getString("flashcardSetController.updateFlashcard"), LanguageManager.getString("flashcardSetController.update"), flashcard)) {
            loadFlashcards();
        }
    }

    // Handle deleting a flashcard
    private void handleDeleteFlashcard(Flashcard flashcard) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(LanguageManager.getString("flashcardSetController.deleteFlashcard"));
        alert.setHeaderText(LanguageManager.getString("flashcardSetController.deleteFlashcard"));
        alert.setContentText(LanguageManager.getString("flashcardSetController.delFlashcardConfirmationText"));

        alert.getDialogPane().getStylesheets().add(getClass().getResource(GLOBAL_CSS).toExternalForm());

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
                alert.setTitle(LanguageManager.getString("flashcardSetController.quiz"));
                alert.setHeaderText(LanguageManager.getString("flashcardSetController.cannotStart"));
                alert.setContentText(LanguageManager.getString("flashcardSetController.setNoFlashcards"));

                alert.getDialogPane().getStylesheets().add(getClass().getResource(GLOBAL_CSS).toExternalForm());
                alert.showAndWait();
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/quiz.fxml"), utils.LanguageManager.getResourceBundle());
            Parent root = loader.load();
            QuizController controller = loader.getController();

            // Initialize quiz with flashcards, user ID, and set ID, -1 if not logged in (only logged in users can save stats)
            int userId = SessionManager.getCurrentUser() != null ?
                    SessionManager.getCurrentUser().getId() : -1;
            int setId = currentSet.getSets_id();
            controller.initQuiz(flashcards, userId, setId);

            flashcardsContainer.getChildren().clear();
            flashcardsContainer.getChildren().add(root);
            startQuizButton.setVisible(false);
            createFlashcardButton.setVisible(false);
            updateSetButton.setVisible(false);
            deleteSetButton.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Reusable dialog for creating/editing flashcards
    private boolean showFlashcardDialog(String title, String buttonText, Flashcard existingFlashcard) {
        // Use factory method to create dialog with form fields
        Dialog<ButtonType> dialog = FlashcardDialogController.createFlashcardDialog(title, existingFlashcard);
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
