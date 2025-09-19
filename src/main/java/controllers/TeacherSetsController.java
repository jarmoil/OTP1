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

public class TeacherSetsController {
    @FXML private FlowPane setsContainer;
    @FXML private Button createSetButton;
    @FXML private AnchorPane contentArea;

    private FlashcardSetService flashcardSetService = new FlashcardSetService();

    @FXML
    public void initialize() {
        loadSets();
        updateUI();
    }

    private void loadSets() {
        try {
            List<FlashcardSet> sets = flashcardSetService.getSetsByRole("teacher");
            setsContainer.getChildren().clear();

            for (FlashcardSet set : sets) {
                Pane setPane = createSetPane(set);
                setsContainer.getChildren().add(setPane);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUI() {
        if (SessionManager.getCurrentUser() != null &&
                "teacher".equals(SessionManager.getCurrentUser().getRole())) {
            createSetButton.setVisible(true);
        } else {
            createSetButton.setVisible(false);
        }
    }

    @FXML
    private void handleCreateSet() {
        if (SessionManager.getCurrentUser() == null ||
                !"teacher".equals(SessionManager.getCurrentUser().getRole())) {
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

    private void openSetDetails(FlashcardSet set) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/flashcardSet.fxml"));
            Parent root = loader.load();
            FlashcardSetController controller = loader.getController();
            controller.initData(set);

            contentArea.getChildren().clear();
            contentArea.getChildren().add(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
