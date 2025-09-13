package controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class TeacherSetsController {
    // TODO: Implement teacher sets view logic
    @FXML private Pane btnEnglishSet, btnMathSet, btnHistorySet;

    @FXML
    public void initialize() {
        btnEnglishSet.setOnMouseClicked(e -> System.out.println("English set clicked"));
        btnMathSet.setOnMouseClicked(e -> System.out.println("Math set clicked"));
        btnHistorySet.setOnMouseClicked(e -> System.out.println("History set clicked"));
    }
}

