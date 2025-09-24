package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

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
            dialog.setTitle("Create Teacher Account");
            dialog.setHeaderText("Create New Teacher");

            ButtonType createButton = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(createButton, ButtonType.CANCEL);

            FXMLLoader loader = new FXMLLoader(CreateTeacherDialogController.class.getResource("/views/createTeacherDialog.fxml"));
            GridPane dialogContent = loader.load();
            CreateTeacherDialogController controller = loader.getController();

            dialog.getDialogPane().setContent(dialogContent);
            dialog.getDialogPane().getProperties().put("controller", controller);

            // Validation
            Button createBtn = (Button) dialog.getDialogPane().lookupButton(createButton);
            createBtn.setDisable(true);

            controller.usernameField.textProperty().addListener((obs, oldText, newText) ->
                    createBtn.setDisable(newText.trim().isEmpty() || controller.passwordField.getText().trim().isEmpty()));
            controller.passwordField.textProperty().addListener((obs, oldText, newText) ->
                    createBtn.setDisable(newText.trim().isEmpty() || controller.usernameField.getText().trim().isEmpty()));

            return dialog;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create teacher dialog", e);
        }
    }
}
