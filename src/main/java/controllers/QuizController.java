package controllers;

import factory.ServiceFactory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import models.Flashcard;
import models.Statistics;
import services.FlashcardService;
import services.FlashcardSetService;
import services.StatisticsService;
import javafx.animation.RotateTransition;
import javafx.util.Duration;
import utils.LanguageManager;

import java.util.*;

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
    private StatisticsService statisticsService;
    private FlashcardService flashcardService;
    private FlashcardSetService flashcardSetService;
    private final Map<Integer, Boolean> quizResults = new HashMap<>();
    private int userId;
    private int setId;

    @FXML
    public void initialize() {
        statisticsService = ServiceFactory.getInstance().getStatisticsService();
        flashcardService = ServiceFactory.getInstance().getFlashcardService();
        flashcardSetService = ServiceFactory.getInstance().getFlashcardSetService();
        choicesGroup = new ToggleGroup();
        choiceA.setToggleGroup(choicesGroup);
        choiceB.setToggleGroup(choicesGroup);
        choiceC.setToggleGroup(choicesGroup);
    }

    // Initialize quiz with flashcards, user ID, and set ID
    public void initQuiz(List<Flashcard> cards, int userId, int setId) {
        this.flashcards = new ArrayList<>(cards);
        this.userId = userId;
        this.setId = setId;
        Collections.shuffle(this.flashcards);
        resetQuiz();
        showCurrentQuestion();
    }

    // Reset quiz state
    private void resetQuiz() {
        currentIndex = 0;
        correctAnswers = 0;
        quizResults.clear();
        updateScore();
    }

    // Display the current question and choices
    private void showCurrentQuestion() {
        if (currentIndex >= flashcards.size()) {
            showQuizComplete();
            return;
        }

        currentCard = flashcards.get(currentIndex);
        updateQuestionDisplay();
        resetQuestionState();
    }

    // Update question and choices text
    private void updateQuestionDisplay() {
        questionLabel.setText(currentCard.getQuestion());
        choiceA.setText(LanguageManager.getString("quizController.a") + currentCard.getChoiceA());
        choiceB.setText(LanguageManager.getString("quizController.b") + currentCard.getChoiceB());
        choiceC.setText(LanguageManager.getString("quizController.c") + currentCard.getChoiceC());
    }

    // Reset state for a new question
    private void resetQuestionState() {
        choicesGroup.selectToggle(null);
        submitButton.setVisible(true);
        nextButton.setVisible(false);
        feedbackLabel.setText("");
        enableChoices(true);
    }

    // Handle answer submission
    @FXML
    private void handleSubmit() {
        RadioButton selectedButton = (RadioButton) choicesGroup.getSelectedToggle();
        if (selectedButton == null) {
            feedbackLabel.setText(LanguageManager.getString("quizController.selectAnswer"));
            return;
        }

        String userAnswer = getSelectedAnswer(selectedButton);
        boolean isCorrect = userAnswer.equals(currentCard.getAnswer());

        // Store result for statistics
        quizResults.put(currentCard.getFlashcardId(), isCorrect);

        if (isCorrect) {
            correctAnswers++;
            showCorrectFeedback();
        } else {
            showIncorrectFeedback();
        }

        updateScore();
        submitButton.setVisible(false);
        nextButton.setVisible(true);
        enableChoices(false);
    }

    // Get the answer text corresponding to the selected radio button
    private String getSelectedAnswer(RadioButton selectedButton) {
        if (selectedButton == choiceA) return currentCard.getChoiceA();
        if (selectedButton == choiceB) return currentCard.getChoiceB();
        return currentCard.getChoiceC();
    }

    // Show feedback for correct answer
    private void showCorrectFeedback() {
        feedbackLabel.setText(LanguageManager.getString("quizController.correct"));
        feedbackLabel.setStyle("-fx-text-fill: green;");
    }

    // Show feedback for incorrect answer with correct choice
    private void showIncorrectFeedback() {
        String correctChoice = getCorrectChoiceLetter();
        feedbackLabel.setText(LanguageManager.getString("quizController.incorrect") + correctChoice);
        feedbackLabel.setStyle("-fx-text-fill: red;");
    }

    // Determine which choice letter corresponds to the correct answer
    private String getCorrectChoiceLetter() {
        if (currentCard.getAnswer().equals(currentCard.getChoiceA())) return "A";
        if (currentCard.getAnswer().equals(currentCard.getChoiceB())) return "B";
        if (currentCard.getAnswer().equals(currentCard.getChoiceC())) return "C";
        return "?";
    }

    // Handle moving to the next question or finishing the quiz
    @FXML
    private void handleNext() {
        currentIndex++;
        if (currentIndex < flashcards.size()) {
            flipToNextQuestion();
        } else {
            showQuizComplete();
        }
    }

    // Flip animation to transition to the next question
    private void flipToNextQuestion() {
        Flashcard nextCard = flashcards.get(currentIndex);
        flipCard(quizPane, nextCard);
    }

    // Flip animation for question card
    private void flipCard(AnchorPane cardNode, Flashcard nextCard) {
        RotateTransition flipOut = new RotateTransition(Duration.millis(200), cardNode);
        flipOut.setFromAngle(0);
        flipOut.setToAngle(70);
        flipOut.setOnFinished(e -> {
            currentCard = nextCard;
            updateQuestionDisplay();
            resetQuestionState();
            updateScore();

            RotateTransition flipIn = new RotateTransition(Duration.millis(200), cardNode);
            flipIn.setFromAngle(70);
            flipIn.setToAngle(0);
            flipIn.play();
        });
        flipOut.play();
    }

    // Show final results and handle statistics
    private void showQuizComplete() {
        hideQuizElements();

        int percentage = calculatePercentage();
        String message = formatResultsMessage(percentage);

        // Handle statistics for logged-in users
        if (userId != -1) {
            message += processUserStatistics(percentage);
        } else {
            message += "\n " + LanguageManager.getString("quizController.signIn");
        }

        feedbackLabel.setText(message);
    }

    // Hide quiz elements and show completion message
    private void hideQuizElements() {
        questionLabel.setText(LanguageManager.getString("quizController.complete"));
        choicesBox.setVisible(false);
        submitButton.setVisible(false);
        nextButton.setVisible(false);
    }

    // Calculate percentage of correct answers
    private int calculatePercentage() {
        return Math.round((float) correctAnswers * 100 / flashcards.size());
    }

    // Format results message
    private String formatResultsMessage(int percentage) {
        String part1 = LanguageManager.getString("quizController.questionsPart1");
        String part2 = LanguageManager.getString("quizController.questionsPart2");
        String part3 = LanguageManager.getString("quizController.questionsPart3");

        return String.format("%s %d %s %d %s (%d%%)",
                part1, correctAnswers, part2, flashcards.size(), part3, percentage);
    }


    // Process and save user statistics, returning any relevant messages
    private String processUserStatistics(int percentage) {
        try {
            // Update individual flashcard stats
            for (Map.Entry<Integer, Boolean> entry : quizResults.entrySet()) {
                flashcardService.updateFlashcardStats(entry.getKey(), entry.getValue());
            }

            // Fetch previous statistics
            Statistics previousStats = statisticsService.getStatistics(userId, setId);

            boolean saved = statisticsService.processQuizResults(userId, setId, correctAnswers, flashcards.size());

            String statsMessage = buildStatisticsMessage(percentage, previousStats);

            // Update set overall percentage
            flashcardSetService.updateSetCorrectPercentage(setId);

            if (!saved) {
                statsMessage += "\n " + LanguageManager.getString("quizController.resultsNotSaved");
            }

            return statsMessage;
        } catch (Exception e) {
            e.printStackTrace();
            return "\n " + LanguageManager.getString("quizController.resultsError");
        }
    }

    // Build message about previous statistics and personal bests
    private String buildStatisticsMessage(int percentage, Statistics previousStats) {
        if (previousStats != null) {
            int best = previousStats.getStats_correct_percentage();
            String prevBestText = LanguageManager.getString("quizController.previousBest");
            String newBestText = LanguageManager.getString("quizController.newBest");

            StringBuilder sb = new StringBuilder();
            sb.append("\n").append(prevBestText).append(" ").append(best).append("%");
            if (percentage > best) {
                sb.append("\n").append(newBestText);
            }
            return sb.toString();
        } else {
            return "\n" + LanguageManager.getString("quizController.firstAttempt");
        }
    }


    // Update score display
// java
    private void updateScore() {
        String scorePrefix = LanguageManager.getString("quizController.score");
        scoreLabel.setText(String.format("%s %d/%d", scorePrefix, correctAnswers, flashcards.size()));
    }

    // Enable or disable answer choices
    private void enableChoices(boolean enable) {
        choiceA.setDisable(!enable);
        choiceB.setDisable(!enable);
        choiceC.setDisable(!enable);
    }
}
