package controllers.analytics;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import utils.LanguageManager;

// TODO: add validation and error handling

public class CreateTeacherDialogController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    // Getters for username and password
    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    // Factory method to create and configure the dialog
    public static Dialog<ButtonType> createTeacherDialog() {
        try {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle(LanguageManager.getString("createTeacherDialogController.createTeacher"));
            dialog.setHeaderText(LanguageManager.getString("createTeacherDialogController.createNewTeacher"));

            ButtonType createButton = new ButtonType(LanguageManager.getString("createTeacherDialogController.create"), ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType(LanguageManager.getString("createTeacherDialogController.cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(createButton, cancelButton);

            FXMLLoader loader = new FXMLLoader(CreateTeacherDialogController.class.getResource("/views/createTeacherDialog.fxml"), utils.LanguageManager.getResourceBundle());
            GridPane dialogContent = loader.load();
            CreateTeacherDialogController controller = loader.getController();

            dialog.getDialogPane().setContent(dialogContent);
            dialog.getDialogPane().getProperties().put("controller", controller);

            dialog.getDialogPane().getStylesheets().add(
                    CreateTeacherDialogController.class.getResource("/css/global.css").toExternalForm()
            );

            // Validation
            Button createBtn = (Button) dialog.getDialogPane().lookupButton(createButton);
            createBtn.setDisable(true);

            controller.usernameField.textProperty().addListener((obs, oldText, newText) ->
                    createBtn.setDisable(newText.trim().isEmpty() || controller.passwordField.getText().trim().isEmpty()));
            controller.passwordField.textProperty().addListener((obs, oldText, newText) ->
                    createBtn.setDisable(newText.trim().isEmpty() || controller.usernameField.getText().trim().isEmpty()));

            return dialog;
        } catch (Exception e) {
            throw new RuntimeException(LanguageManager.getString("createTeacherDialogController.failed") + e);
        }
    }
}
