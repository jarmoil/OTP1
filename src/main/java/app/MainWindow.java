package app;

import controllers.MainWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.LanguageManager;

public class MainWindow extends Application {

    public static void run(String[] args) {
        Application.launch(MainWindow.class, args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Waiting for FXMLLoader with resource bundle support util class
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/window.fxml"));
        loader.setResources(LanguageManager.getResourceBundle());
        Scene scene = new Scene(loader.load());
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Quizzy - Quiz Application");
        ((MainWindowController)loader.getController()).init(primaryStage);
        primaryStage.show();
    }
}