package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.UserService;

public class RegisterController {
    // TODO: Add documentation
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;

    private UserService userService = new UserService();
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match");
            return;
        }

        try {
            if (userService.register(username, password)) {
                showLogin();
            } else {
                errorLabel.setText("Username already exists");
            }
        } catch (Exception e) {
            errorLabel.setText("Registration error");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackToLogin() {
        showLogin();
    }

    private void showLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            LoginController controller = loader.getController();
            controller.setStage(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
