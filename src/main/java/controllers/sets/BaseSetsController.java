package controllers.sets;

import controllers.flashcards.FlashcardSetController;
import controllers.flashcards.SetCardController;
import factory.ServiceFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import models.FlashcardSet;
import services.FlashcardSetService;
import utils.LanguageManager;
import utils.SessionManager;
import java.util.List;
import java.util.Optional;

public abstract class BaseSetsController {
    @FXML protected FlowPane setsContainer;
    @FXML protected Button createSetButton;
    @FXML protected AnchorPane contentArea;

    protected FlashcardSetService flashcardSetService;

    @FXML
    public void initialize() {
        flashcardSetService = ServiceFactory.getInstance().getFlashcardSetService();
        loadSets();
        updateUI();
    }

    protected void loadSets() {
        try {
            List<FlashcardSet> sets = flashcardSetService.getSetsByRole(getUserRole());
            setsContainer.getChildren().clear();

            for (FlashcardSet set : sets) {
                Pane setPane = createSetPane(set);
                setsContainer.getChildren().add(setPane);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void updateUI() {
        if (SessionManager.getCurrentUser() != null &&
                getUserRole().equals(SessionManager.getCurrentUser().getRole())) {
            createSetButton.setVisible(true);
        } else {
            createSetButton.setVisible(false);
        }
    }

    @FXML
    protected void handleCreateSet() {
        if (SessionManager.getCurrentUser() == null ||
                !getUserRole().equals(SessionManager.getCurrentUser().getRole())) {
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(LanguageManager.getString("baseSetsController.createNewSet"));
        dialog.setHeaderText(LanguageManager.getString("baseSetsController.enterDescription"));
        dialog.setContentText(LanguageManager.getString("baseSetsController.description"));

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

    protected Pane createSetPane(FlashcardSet set) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/setCard.fxml"), utils.LanguageManager.getResourceBundle());
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

    protected void openSetDetails(FlashcardSet set) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/flashcardSet.fxml"), utils.LanguageManager.getResourceBundle());
            Parent root = loader.load();
            FlashcardSetController controller = loader.getController();
            controller.initData(set);

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

    protected abstract String getUserRole();
    protected abstract String getViewResource();

    protected void restoreSetsView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(getViewResource()));
            Parent root = loader.load();

            contentArea.getChildren().clear();
            contentArea.getChildren().add(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}