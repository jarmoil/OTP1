package controllers;

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

    private FlashcardSetService flashcardSetService = new FlashcardSetService();

    @FXML
    public void initialize() {
        loadSets();
        updateUI();
    }

    // Load flashcard sets from the service and display them
    private void loadSets() {
        try {
            List<FlashcardSet> sets = flashcardSetService.getAllSets();
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
        Pane setPane = new Pane();
        setPane.setPrefSize(140, 200);
        setPane.getStyleClass().add("highlightCard");

        Label descLabel = new Label(set.getDescription());
        descLabel.setWrapText(true);
        descLabel.setLayoutX(10);
        descLabel.setLayoutY(10);
        descLabel.setPrefWidth(120);

        setPane.getChildren().add(descLabel);

        // Add click handler to open set details
        setPane.setOnMouseClicked(e -> openSetDetails(set));

        return setPane;
    }

    // Load the flashcard set details view into the content area to view flashcards in that set
    private void openSetDetails(FlashcardSet set) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/flashcardSet.fxml"));
            Parent root = loader.load();
            FlashcardSetController controller = loader.getController();
            controller.initData(set);

            setsContainer.getChildren().clear();
            setsContainer.getChildren().add(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
