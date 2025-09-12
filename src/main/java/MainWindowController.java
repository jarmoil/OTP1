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
    @FXML private Pane btnStudentSets;
    @FXML private Pane btnTeacherSets;


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
        btnStudentSets.setOnMouseClicked(mouseEvent -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/studentSets.fxml"));
                Parent studentSetsRoot = loader.load();
                MainWindowController studentSetsController = loader.getController();
                stage.getScene().setRoot(studentSetsRoot);
                studentSetsController.init(stage);
                studentSetsController.setbtnStatsLabel("Main");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        btnTeacherSets.setOnMouseClicked(mouseEvent -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/teacherSets.fxml"));
                Parent teacherSetsRoot = loader.load();
                MainWindowController teacherSetsController = loader.getController();
                stage.getScene().setRoot(teacherSetsRoot);
                teacherSetsController.init(stage);
                teacherSetsController.setbtnStatsLabel("Main");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

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
