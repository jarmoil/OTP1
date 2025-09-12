package utils;

import controllers.MainWindowController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import controllers.MainWindowController;

public class ViewManager {
    public static MainWindowController switchView(Stage stage, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource(fxmlPath));
            Parent root = loader.load();
            MainWindowController controller = loader.getController();
            stage.getScene().setRoot(root);
            if (controller != null) {
                controller.init(stage);
            }
            return controller;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
