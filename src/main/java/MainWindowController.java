import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;



public class MainWindowController{
    @FXML private Pane titlePane;
    @FXML private ImageView btnMinimize, btnClose;
    @FXML private Pane btnStats;
    @FXML private Label btnStatsLabel;


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
        btnStats.setOnMouseClicked(mouseEvent -> {
            if (btnStatsLabel != null && "Main".equals(btnStatsLabel.getText())) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/window.fxml"));
                    Parent mainRoot = loader.load();
                    MainWindowController mainController = loader.getController();
                    stage.getScene().setRoot(mainRoot);
                    mainController.init(stage);
                    mainController.setbtnStatsLabel("Stats");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/stats.fxml"));
                    Parent statsRoot = loader.load();
                    MainWindowController statsController = loader.getController();
                    stage.getScene().setRoot(statsRoot);
                    statsController.init(stage);
                    statsController.setbtnStatsLabel("Main");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void setbtnStatsLabel(String text){
        if (btnStatsLabel != null) {
            btnStatsLabel.setText(text);
        }
    }
}
