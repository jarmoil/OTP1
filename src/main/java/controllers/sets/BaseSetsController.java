package controllers.sets;

import controllers.flashcards.FlashcardSetController;
import controllers.flashcards.SetCardController;
import factory.ServiceFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
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
            String langCode = LanguageManager.getCurrentLocale().getLanguage();

            List<FlashcardSet> sets = flashcardSetService.getSetsByRoleAndLocale(getUserRole(), langCode);

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
        createSetButton.setVisible(SessionManager.getCurrentUser() != null &&
                getUserRole().equals(SessionManager.getCurrentUser().getRole()));
    }

    @FXML
    protected void handleCreateSet() {
        String rawCode = LanguageManager.getCurrentLocale().getLanguage();

        String langCode;
        if (rawCode.startsWith("ja")) {
            langCode = "ja";
        } else if (rawCode.startsWith("ru")) {
            langCode = "ru";
        } else {
            langCode = "en";
        }

        if (SessionManager.getCurrentUser() == null ||
                !getUserRole().equals(SessionManager.getCurrentUser().getRole())) {
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(LanguageManager.getString("baseSetsController.createNewSet"));
        dialog.setHeaderText(LanguageManager.getString("baseSetsController.enterDescription"));
        dialog.setContentText(LanguageManager.getString("baseSetsController.description"));

        if (contentArea != null && contentArea.getScene() != null) {
            dialog.initOwner(contentArea.getScene().getWindow());
        }

        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/css/global.css").toExternalForm());

        ButtonType okButton = new ButtonType(
                LanguageManager.getString("baseSetsController.ok"),
                ButtonBar.ButtonData.OK_DONE
        );
        ButtonType cancelButton = new ButtonType(
                LanguageManager.getString("baseSetsController.cancel"),
                ButtonBar.ButtonData.CANCEL_CLOSE
        );
        dialog.getDialogPane().getButtonTypes().setAll(okButton, cancelButton);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton != null && dialogButton.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                return dialog.getEditor().getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(description -> {
            try {
                flashcardSetService.createSet(
                        SessionManager.getCurrentUser().getId(),
                        description,langCode
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource(getViewResource()), utils.LanguageManager.getResourceBundle());
            Parent root = loader.load();

            contentArea.getChildren().clear();
            contentArea.getChildren().add(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}