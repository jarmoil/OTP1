package controllers.login;

import factory.ServiceFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.User;
import services.UserService;
import utils.FXMLLoaderUtil;
import utils.SessionManager;

// TODO: Refresh main window after login/logout to update UI elements

// Controller for the login modal and logic
public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private UserService userService;
    private Stage modalStage;

    @FXML
    public void initialize() {
        userService = ServiceFactory.getInstance().getUserService();
    }


    public void setStage(Stage stage) {
        this.modalStage = stage;
    }

    // Handle login button click and authenticate user
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
                    showWindow();
                }

            } else {
                errorLabel.setText("Invalid username or password");
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Login error");
        }
    }

    // Switch to registration view by loading registration FXML with hyperlink directing to that
    @FXML
    private void handleShowRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/registration.fxml"), utils.LanguageManager.getResourceBundle());
            Parent root = loader.load();
            Scene scene = new Scene(root);
            modalStage.setScene(scene);

            RegisterController controller = loader.getController();
            controller.setStage(modalStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // After login, open main window to refresh UI
    private void showWindow() {
        try {
            FXMLLoader loader = FXMLLoaderUtil.createLoader("/views/window.fxml");
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            Stage mainStage = controllers.MainWindowController.getMainStage();
            mainStage.setScene(scene);

            controllers.MainWindowController controller = loader.getController();
            controller.init(mainStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
