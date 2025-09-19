package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SetCardController {
    @FXML private Label descLabel;

    public void setDescription(String description) {
        descLabel.setText(description);
        descLabel.setTextFill(javafx.scene.paint.Color.WHITE);
    }
}
