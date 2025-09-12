import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;



public class MainWindowController{
    @FXML private Pane titlePane;
    @FXML private ImageView btnMinimize, btnClose;

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
    }
}
