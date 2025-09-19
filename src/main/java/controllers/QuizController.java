package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import models.Flashcard;
import java.util.*;
import javafx.animation.RotateTransition;
import javafx.util.Duration;
import javafx.scene.Node;

public class QuizController {
    @FXML private Label questionLabel;
    @FXML private RadioButton choiceA;
    @FXML private RadioButton choiceB;
    @FXML private RadioButton choiceC;
    @FXML private Button submitButton;
    @FXML private Button nextButton;
    @FXML private Label feedbackLabel;
    @FXML private Label scoreLabel;
    @FXML private VBox choicesBox;
    @FXML private AnchorPane quizPane;


    private List<Flashcard> flashcards;
    private int currentIndex = 0;
    private int correctAnswers = 0;
    private ToggleGroup choicesGroup;
    private Flashcard currentCard;

    // Flip animation for question card
    private void flipCard(Node cardNode, Flashcard nextCard) {
        RotateTransition flipOut = new RotateTransition(Duration.millis(200), cardNode);
        flipOut.setFromAngle(0);
        flipOut.setToAngle(70);
        flipOut.setOnFinished(e -> {
            // Update all quiz elements here
            questionLabel.setText(nextCard.getQuestion());
            choiceA.setText("A) " + nextCard.getChoice_a());
            choiceB.setText("B) " + nextCard.getChoice_b());
            choiceC.setText("C) " + nextCard.getChoice_c());
            choicesGroup.selectToggle(null);
            submitButton.setVisible(true);
            nextButton.setVisible(false);
            feedbackLabel.setText("");
            enableChoices(true);
            currentCard = nextCard;
            updateScore();

            RotateTransition flipIn = new RotateTransition(Duration.millis(200), cardNode);
            flipIn.setFromAngle(70);
            flipIn.setToAngle(0);
            flipIn.play();
        });
        flipOut.play();
    }


    @FXML
    public void initialize() {
        choicesGroup = new ToggleGroup();
        choiceA.setToggleGroup(choicesGroup);
        choiceB.setToggleGroup(choicesGroup);
        choiceC.setToggleGroup(choicesGroup);
    }

    public void initQuiz(List<Flashcard> cards) {
        this.flashcards = new ArrayList<>(cards);
        Collections.shuffle(this.flashcards);
        currentIndex = 0;
        correctAnswers = 0;
        updateScore();
        showNextQuestion();
    }

    private void showNextQuestion() {
        if (currentIndex < flashcards.size()) {
            currentCard = flashcards.get(currentIndex);
            questionLabel.setText(currentCard.getQuestion());
            choiceA.setText("A) " + currentCard.getChoice_a());
            choiceB.setText("B) " + currentCard.getChoice_b());
            choiceC.setText("C) " + currentCard.getChoice_c());

            choicesGroup.selectToggle(null);
            submitButton.setVisible(true);
            nextButton.setVisible(false);
            feedbackLabel.setText("");
            enableChoices(true);
        } else {
            showQuizComplete();
        }
    }

    @FXML
    private void handleSubmit() {
        RadioButton selectedButton = (RadioButton) choicesGroup.getSelectedToggle();
        if (selectedButton == null) {
            feedbackLabel.setText("Please select an answer!");
            return;
        }

        String userAnswer;
        if (selectedButton == choiceA) userAnswer = currentCard.getChoice_a();
        else if (selectedButton == choiceB) userAnswer = currentCard.getChoice_b();
        else userAnswer = currentCard.getChoice_c();

        boolean isCorrect = userAnswer.equals(currentCard.getAnswer());

        if (isCorrect) {
            correctAnswers++;
            feedbackLabel.setText("Correct!");
            feedbackLabel.setStyle("-fx-text-fill: green;");
        } else {
            String correctChoice = "?";
            if (currentCard.getAnswer().equals(currentCard.getChoice_a())) correctChoice = "A";
            else if (currentCard.getAnswer().equals(currentCard.getChoice_b())) correctChoice = "B";
            else if (currentCard.getAnswer().equals(currentCard.getChoice_c())) correctChoice = "C";

            feedbackLabel.setText("Incorrect! The correct answer was: " + correctChoice);
            feedbackLabel.setStyle("-fx-text-fill: red;");
        }

        updateScore();
        submitButton.setVisible(false);
        nextButton.setVisible(true);
        enableChoices(false);
    }



    @FXML
    private void handleNext() {
        currentIndex++;
        if (currentIndex < flashcards.size()) {
            Flashcard nextCard = flashcards.get(currentIndex);
            flipCard(quizPane, nextCard);
            choiceA.setText("A) " + nextCard.getChoice_a());
            choiceB.setText("B) " + nextCard.getChoice_b());
            choiceC.setText("C) " + nextCard.getChoice_c());
            choicesGroup.selectToggle(null);
            submitButton.setVisible(true);
            nextButton.setVisible(false);
            feedbackLabel.setText("");
            enableChoices(true);
            currentCard = nextCard;
            updateScore();
        } else {
            showQuizComplete();
        }
    }


    private void showQuizComplete() {
        questionLabel.setText("Quiz Complete!");
        choicesBox.setVisible(false);
        submitButton.setVisible(false);
        nextButton.setVisible(false);
        feedbackLabel.setText(String.format("You got %d out of %d questions correct!",
                correctAnswers, flashcards.size()));
    }

    private void updateScore() {
        scoreLabel.setText(String.format("Score: %d/%d", correctAnswers, flashcards.size()));
    }

    private void enableChoices(boolean enable) {
        choiceA.setDisable(!enable);
        choiceB.setDisable(!enable);
        choiceC.setDisable(!enable);
    }
}
