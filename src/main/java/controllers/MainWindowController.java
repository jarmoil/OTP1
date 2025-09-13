package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.SessionManager;

public class MainWindowController {

    // Sidebar buttons
    @FXML private Pane btnStudentSets;
    @FXML private Pane btnTeacherSets;
    @FXML private Pane btnStats;
    @FXML private Pane btnLogin;
    @FXML private Label btnLoginLabel;

    //  minimize, close
    @FXML private Pane titlePane;
    @FXML private ImageView btnClose;
    @FXML private ImageView btnMinimize;


    // Content area
    @FXML private AnchorPane contentArea;

    private Stage mainStage;

    public void init(Stage stage) {
        this.mainStage = stage;

        btnStudentSets.setOnMouseClicked(e -> loadContent("/views/studentSets.fxml"));
        btnTeacherSets.setOnMouseClicked(e -> loadContent("/views/teacherSets.fxml"));
        btnStats.setOnMouseClicked(e -> loadContent("/views/stats.fxml"));

        btnLogin.setOnMouseClicked(this::handleLoginLogout);

        btnClose.setOnMouseClicked(e -> mainStage.close());
        btnMinimize.setOnMouseClicked(e -> mainStage.setIconified(true));

        updateLoginUI();
    }

    private void loadContent(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent content = loader.load();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(content);
            AnchorPane.setTopAnchor(content, 0.0);
            AnchorPane.setBottomAnchor(content, 0.0);
            AnchorPane.setLeftAnchor(content, 0.0);
            AnchorPane.setRightAnchor(content, 0.0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleLoginLogout(MouseEvent event) {
        if (SessionManager.getCurrentUser() != null) {
            // Logout
            SessionManager.clear();
            updateLoginUI();
        } else {
            // Open login modal
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
                Stage loginStage = new Stage();
                loginStage.initOwner(mainStage);
                loginStage.initModality(Modality.APPLICATION_MODAL);
                loginStage.setScene(new javafx.scene.Scene(loader.load()));

                controllers.LoginController controller = loader.getController();
                controller.setStage(loginStage);

                loginStage.showAndWait();
                updateLoginUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateLoginUI() {
        if (SessionManager.getCurrentUser() != null) {
            btnLoginLabel.setText(SessionManager.getCurrentUser().getName() + " (Logout)");
        } else {
            btnLoginLabel.setText("Login");
        }
    }
}
