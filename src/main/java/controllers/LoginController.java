package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.User;
import services.UserService;
import utils.SessionManager;

public class LoginController {
    // TODO: Add documentation

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private UserService userService = new UserService();
    private Stage modalStage;


    public void setStage(Stage stage) {
        this.modalStage = stage;
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            User user = userService.login(username, password);
            if (user != null) {
                // Set the logged-in user in session
                SessionManager.setCurrentUser(user);

                if (modalStage != null) {
                    modalStage.close();
                }

            } else {
                errorLabel.setText("Invalid username or password");
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Login error");
        }
    }

    @FXML
    private void handleShowRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/registration.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            modalStage.setScene(scene);

            RegisterController controller = loader.getController();
            controller.setStage(modalStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
