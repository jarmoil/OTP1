package controllers.login;

import factory.ServiceFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.UserService;
import utils.LanguageManager;

public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;

    private UserService userService;
    private Stage stage;

    @FXML
    public void initialize() {
        userService = ServiceFactory.getInstance().getUserService();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Handle registration button click and create new user
    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!password.equals(confirmPassword)) {
            errorLabel.setText(LanguageManager.getString("registerController.noMatch"));
            return;
        }

        try {
            if (userService.register(username, password)) {
                showLogin();
            } else {
                errorLabel.setText(LanguageManager.getString("registerController.userExists"));
            }
        } catch (Exception e) {
            errorLabel.setText(LanguageManager.getString("registerController.registerError"));
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackToLogin() {
        showLogin();
    }

    // Load and display the login view after successful registration or when navigating back
    private void showLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"), LanguageManager.getResourceBundle());
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
