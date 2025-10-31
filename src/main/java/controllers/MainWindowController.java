package controllers;

import controllers.login.LoginController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.LanguageManager;
import utils.SessionManager;

// Controller for the main application window, handling navigation and stuff
public class MainWindowController {
    // TODO: Make window scalable and fullscreen ???

    // MAKE IT POSSIBLE TO DRAG THE WINDOW AROUND1?!?!!?!?
    private double xOffset;
    private double yOffset;

    private void initializeDragListeners() {
        titlePane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        titlePane.setOnMouseDragged(event -> {
            mainStage.setX(event.getScreenX() - xOffset);
            mainStage.setY(event.getScreenY() - yOffset);
        });
    }


    // Sidebar buttons
    @FXML private Pane btnStudentSets;
    @FXML private Pane btnTeacherSets;
    @FXML private Pane btnStats;
    @FXML private Pane btnLogin;
    @FXML private Label btnLoginLabel;
    @FXML private Label btnLoginLabel1; // For showing username when logged in
    @FXML private Label btnStatsLabel;

    //  minimize, close
    @FXML private Pane titlePane;
    @FXML private ImageView btnClose;
    @FXML private ImageView btnMinimize;
    @FXML private ComboBox<String> languageSelector;


    // Content area (Flashcard sets, stats, etc.)
    @FXML private AnchorPane contentArea;

    private Stage mainStage;
    private static Stage mainStageRef;


    private void initializeLanguageSelector() {
        languageSelector.getItems().addAll("EN", "JA", "RU");

        String currentLang = switch (LanguageManager.getCurrentLocale().getLanguage()) {
            case "ja" -> "JA";
            case "ru" -> "RU";
            default -> "EN";
        };
        languageSelector.setValue(currentLang);

        languageSelector.setOnAction(e -> {
            String selectedLang = languageSelector.getValue();
            if (selectedLang != null) {
                changeLanguage(selectedLang);
            }
        });
    }

    private void changeLanguage(String language) {
        // TODO: Show confirmation dialog before changing language and inform user that app will reload
        LanguageManager.setLocale(language);

        // Reload the main window to apply new language
        try {
            // Waiting for FXMLLoader with resource bundle support util class
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/window.fxml"));
            loader.setResources(LanguageManager.getResourceBundle());
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            mainStage.setScene(scene);

            MainWindowController controller = loader.getController();
            controller.init(mainStage);

            // Set value without triggering the event
            controller.languageSelector.setValue(language);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init(Stage stage) {
        this.mainStage = stage;
        mainStageRef = stage;

        // Navigation handlers
        btnStudentSets.setOnMouseClicked(e -> {
            setActiveSidebarButton(btnStudentSets);
            loadContent("/views/studentSets.fxml");
        });
        btnTeacherSets.setOnMouseClicked(e -> {
            setActiveSidebarButton(btnTeacherSets);
            loadContent("/views/teacherSets.fxml");
        });

        // Load different stats view based on user role
        btnStats.setOnMouseClicked(e -> {
            setActiveSidebarButton(btnStats);
            if (SessionManager.getCurrentUser() != null &&
                    "teacher".equals(SessionManager.getCurrentUser().getRole())) {
                loadContent("/views/analytics.fxml");
            } else {
                loadContent("/views/stats.fxml");
            }
        });

        btnLogin.setOnMouseClicked(this::handleLoginLogout);

        // Window control handlers
        btnClose.setOnMouseClicked(e -> mainStage.close());
        btnMinimize.setOnMouseClicked(e -> mainStage.setIconified(true));

        initializeDragListeners();
        initializeLanguageSelector();
        updateLoginUI();
    }

    // Load FXML content into the content area of the main window
    private void loadContent(String fxmlPath) {
        try {
            // Waiting for FXMLLoader with resource bundle support util class
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setResources(LanguageManager.getResourceBundle());
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

            // Reload main window after logout to reset UI
            try {
                // Waiting for FXMLLoader with resource bundle support util class
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/window.fxml"));
                loader.setResources(LanguageManager.getResourceBundle());
                Parent root = loader.load();
                Scene scene = new Scene(root);
                scene.setFill(Color.TRANSPARENT);
                mainStage.setScene(scene);

                MainWindowController controller = loader.getController();
                controller.init(mainStage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            // Open login modal
            try {
                // Waiting for FXMLLoader with resource bundle support util class
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
                loader.setResources(LanguageManager.getResourceBundle());
                Stage loginStage = new Stage();
                loginStage.initOwner(mainStage);
                loginStage.initModality(Modality.APPLICATION_MODAL);
                loginStage.setScene(new javafx.scene.Scene(loader.load()));

                LoginController controller = loader.getController();
                controller.setStage(loginStage);

                loginStage.showAndWait();
                updateLoginUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Update login button text based on session state
    private void updateLoginUI() {
        if (SessionManager.getCurrentUser() != null) {
            btnLoginLabel.setText("Logout");
            btnLoginLabel1.setText(SessionManager.getCurrentUser().getName());
            btnLoginLabel1.setVisible(true);
        } else {
            btnLoginLabel.setText("Login");
            btnLoginLabel1.setVisible(false);
        }
        updateStatsButtonText();
    }

    // Change "Stats" to "Analytics" when teacher is logged in
    private void updateStatsButtonText() {
        if (SessionManager.getCurrentUser() != null &&
                "teacher".equals(SessionManager.getCurrentUser().getRole())) {
            btnStatsLabel.setText("Analytics");
        } else {
            btnStatsLabel.setText("Stats");
        }
    }

    private Pane activateSidebarButton;

    private void setActiveSidebarButton(Pane selectedButton) {
        if (activateSidebarButton != null) {
            activateSidebarButton.setStyle(""); // Remove highlight from previous button
        }

        activateSidebarButton = selectedButton;
        activateSidebarButton.setStyle("-fx-background-color: #9e9e9e;");
    }

    // Provide access to main stage for other controllers
    public static Stage getMainStage() {
        return mainStageRef;
    }
}
