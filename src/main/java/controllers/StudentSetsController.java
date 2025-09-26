package controllers;

import factory.ServiceFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import models.FlashcardSet;
import services.FlashcardSetService;
import utils.SessionManager;
import java.util.List;
import java.util.Optional;

// Controller for displaying and managing flashcard sets made by students
public class StudentSetsController {
    @FXML private FlowPane setsContainer;
    @FXML private Button createSetButton;
    @FXML private AnchorPane contentArea;

    private FlashcardSetService flashcardSetService;

    @FXML
    public void initialize() {
        flashcardSetService = ServiceFactory.getInstance().getFlashcardSetService();
        loadSets();
        updateUI();
    }

    // Load student flashcard sets from the service and display them
    private void loadSets() {
        try {
            List<FlashcardSet> sets = flashcardSetService.getSetsByRole("student");
            setsContainer.getChildren().clear();

            for (FlashcardSet set : sets) {
                Pane setPane = createSetPane(set);
                setsContainer.getChildren().add(setPane);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Show create set button only for logged-in students
    private void updateUI() {
        if (SessionManager.getCurrentUser() != null &&
                "student".equals(SessionManager.getCurrentUser().getRole())) {
            createSetButton.setVisible(true);
        } else {
            createSetButton.setVisible(false);
        }
    }

    // Handle creating a new flashcard set, showing a dialog to input description and using the service to save it to the database
    @FXML
    private void handleCreateSet() {
        if (SessionManager.getCurrentUser() == null ||
                !"student".equals(SessionManager.getCurrentUser().getRole())) {
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create New Set");
        dialog.setHeaderText("Enter set description");
        dialog.setContentText("Description:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(description -> {
            try {
                flashcardSetService.createSet(
                        SessionManager.getCurrentUser().getId(),
                        description
                );
                loadSets();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Create a pane representing a flashcard set with description and click handler to open details (show the flashcards in that set)
    private Pane createSetPane(FlashcardSet set) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/setCard.fxml"));
            Pane setPane = loader.load();
            SetCardController controller = loader.getController();
            controller.setDescription(set.getDescription());

            setPane.setOnMouseClicked(e -> openSetDetails(set));
            return setPane;
        } catch (Exception e) {
            e.printStackTrace();
            return new Pane();
        }
    }

    // Restore the main sets view after deleting a set in the details view
    private void restoreSetsView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/studentSets.fxml"));
            Parent root = loader.load();
            StudentSetsController controller = loader.getController();

            contentArea.getChildren().clear();
            contentArea.getChildren().add(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Load the flashcard set details view into the content area to view flashcards in that set
    private void openSetDetails(FlashcardSet set) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/flashcardSet.fxml"));
            Parent root = loader.load();
            FlashcardSetController controller = loader.getController();
            controller.initData(set);

            // Used when deleting a set in the details view to refresh the sets list and restore the sets view
            controller.setOnSetDeletedCallback(() -> {
                loadSets();
                restoreSetsView();
            });

            contentArea.getChildren().clear();
            contentArea.getChildren().add(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
