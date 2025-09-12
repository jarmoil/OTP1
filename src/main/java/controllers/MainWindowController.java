package controllers;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import utils.ViewManager;


public class MainWindowController{
    @FXML private Pane titlePane;
    @FXML private ImageView btnMinimize, btnClose;
    @FXML private Pane btnStats;
    @FXML private Label btnStatsLabel;
    @FXML private Pane btnStudentSets;
    @FXML private Pane btnTeacherSets;
    @FXML private Pane btnEnglishSet;
    @FXML private Pane btnMathSet;
    @FXML private Pane btnHistorySet;
    @FXML private Pane btnLogout;
    @FXML private Label btnLogoutLabel;


    private double x, y;

    public void init(Stage stage){
        titlePane.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        titlePane.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });

        btnClose.setOnMouseClicked(mouseEvent -> stage.close());
        btnMinimize.setOnMouseClicked(mouseEvent -> stage.setIconified(true));
        btnLogout.setOnMouseClicked(mouseEvent -> {
            if(btnLogoutLabel != null) {
                String currentText = btnLogoutLabel.getText();
                if ("Logout".equals(currentText)) {
                    btnLogoutLabel.setText("Login");
                } else if ("Login".equals(currentText)) {
                    btnLogoutLabel.setText("Logout");
                }
            }
        });

        btnStudentSets.setOnMouseClicked(mouseEvent -> {
            ViewManager.switchView(stage, "/views/studentSets.fxml");
        });
        btnTeacherSets.setOnMouseClicked(mouseEvent -> {
            ViewManager.switchView(stage, "/views/teacherSets.fxml");
        });

        if (btnMathSet != null) {
            btnMathSet.setOnMouseClicked(mouseEvent -> {
                ViewManager.switchView(stage, "/views/quizMath.fxml");
            });
        }

            if (btnHistorySet != null) {
                btnHistorySet.setOnMouseClicked(mouseEvent -> {
                    ViewManager.switchView(stage, "/views/quizHistory.fxml");
                });
            }

        btnStats.setOnMouseClicked(mouseEvent -> {
            if (btnStatsLabel != null && "Main".equals(btnStatsLabel.getText())) {
                MainWindowController controller = ViewManager.switchView(stage, "/views/window.fxml");
                if ( controller != null) controller.setbtnStatsLabel("Stats");
            } else {
                MainWindowController controller = ViewManager.switchView(stage, "/views/stats.fxml");
                if (controller != null) controller.setbtnStatsLabel("Main");
            }
        });

        if (btnEnglishSet != null) {
            btnEnglishSet.setOnMouseClicked(mouseEvent -> {
                ViewManager.switchView(stage, "/views/quizEnglish.fxml");
            });
        }

    }
    public void setbtnStatsLabel(String text){
        if (btnStatsLabel != null) {
            btnStatsLabel.setText(text);
        }
    }
}
