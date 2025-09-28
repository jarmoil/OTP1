package e2e;

import app.Main;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.stage.Stage;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.*;
import static org.testfx.util.NodeQueryUtils.hasText;
import static org.hamcrest.Matchers.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;
import static org.testfx.util.WaitForAsyncUtils.waitFor;
import java.util.concurrent.TimeoutException;





public class MainE2EWorkFlowTest  extends ApplicationTest{
    @Override
    public void start(Stage primaryStage) throws Exception {
        new Main().start(primaryStage);
    }

    @Test
    public void completeCoreWorkflow() throws TimeoutException {
        // Verify main window is displayed
        verifyThat("#btnLogin", isVisible());

        // Click on Login button and perform login action
        clickOn("#btnLogin")
                .write("testuser")
                .push(javafx.scene.input.KeyCode.TAB)
                .write("password")
                .clickOn(".button");

        // Verify login was successful and username is displayed
        verifyThat("#btnLoginLabel1", isVisible());

        // Navigate to Flashcards section
        clickOn("#btnStudentSets");

        // Verify student flashcards section is displayed
        verifyThat(".label", hasText("Student sets"));

        // Create a new flashcard
        clickOn("#createSetButton").write("Test Set");
        clickOn("OK"); // Confirm creation
        waitForFxEvents(); // Wait for UI update

        // Verify the new set appears
        verifyThat("#setsContainer", node ->
                node.lookupAll("*").stream().anyMatch(hasText("Test Set"))
        );


        // Click the new set
        clickOn("Test Set");

        // Add a new flashcard to the set
        clickOn("#createFlashcardButton");

        // Fill in flashcard details
        clickOn("#questionField").write("Test Question");
        clickOn("#choiceAField").write("Test Answer A");
        clickOn("#choiceBField").write("Test Answer B");
        clickOn("#choiceCField").write("Test Answer C");

        // Select the correct answer
        clickOn("#answerBox");
        push(javafx.scene.input.KeyCode.DOWN); // Select the second answer as correct
        push(javafx.scene.input.KeyCode.ENTER);

        // Save the flashcard
        clickOn("Create");


        // Verify the new flashcard appears in the list
        verifyThat("#flashcardsContainer", node ->
                node.lookupAll("*").stream().anyMatch(hasText("Test Question"))
        );

        // Update the flashcard
        clickOn("#updateSetButton");
        write("Test Set Updated");
        clickOn("OK");

        clickOn("#updateButton");
        clickOn("#questionField");
        push(KeyCode.RIGHT);
        eraseText(20); // Clear existing text
        write("Test Question Updated");

        clickOn("Update");

        // Wait for the UI to update after clicking "Update"
        waitFor(2, java.util.concurrent.TimeUnit.SECONDS, () ->
                lookup("#flashcardsContainer").tryQuery().isPresent()
        );

        verifyThat("#flashcardsContainer", node ->
                node.lookupAll("*").stream().anyMatch(hasText("Test Question Updated"))
        );

        // Start a quiz with the flashcard
        clickOn("#startQuizButton");
        verifyThat("#questionLabel", hasText("Test Question Updated"));
        clickOn("#choiceB"); // Select B answer
        clickOn("#submitButton");

        clickOn("Next Question");


        // Verify quiz completion
        clickOn("#btnStats");
        verifyThat("#setNameLabel", hasText("Test Set Updated"));
        verifyThat("#percentageLabel", hasText("100%"));

        // Navigate to Flashcards section
        clickOn("#btnStudentSets");

        clickOn("Test Set Updated");

        // Delete the flashcard
        clickOn("#deleteSetButton");
        clickOn("OK"); // Confirm deletion

        // Verify the flashcard is removed from the list
        verifyThat("#setsContainer", node ->
                node.lookupAll("*").stream().noneMatch(hasText("Test Question Updated"))
        );

        // Verify label shows "Logout"
        verifyThat("#btnLoginLabel", hasText("Logout"));

        // Perform logout
        clickOn("#btnLogin");


        // Verify label now shows "Login"
        verifyThat("#btnLoginLabel", hasText("Login"));

        // Login as Teacher
        clickOn("#btnLogin")
                .write("teacher")
                .push(javafx.scene.input.KeyCode.TAB)
                .write("teacher")
                .clickOn(".button");

        // Click on Analytics tab
        clickOn("#btnStats");

        clickOn("#clickHint");

        // Verify hint dialog appears
        verifyThat(".dialog-pane", isVisible());
        clickOn("Close"); // Close hint dialog


        // Exit the application
        clickOn("#btnClose");
    }
}
