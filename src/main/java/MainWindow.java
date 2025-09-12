import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindow extends Application {

    public static void run(String[] args) {
        Application.launch(MainWindow.class, args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/window.fxml"));
        Scene scene = new Scene(loader.load());
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setResizable(true);
        primaryStage.setTitle("Quizzy - Quiz Application");
        ((MainWindowController)loader.getController()).init(primaryStage);
        // primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.png")));
        primaryStage.show();
    }
}