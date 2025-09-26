package controllers.flashcards;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

// very cool class, actually readable and understandable
public class SetCardController {
    @FXML private Label descLabel;

    public void setDescription(String description) {
        descLabel.setText(description);
        descLabel.setTextFill(javafx.scene.paint.Color.WHITE);
    }
}
