package utils.validation;

public class FlashcardData {
    private final String question;
    private final String answer;
    private final String choiceA;
    private final String choiceB;
    private final String choiceC;

    public FlashcardData(String question, String answer, String choiceA, String choiceB, String choiceC) {
        this.question = question;
        this.answer = answer;
        this.choiceA = choiceA;
        this.choiceB = choiceB;
        this.choiceC = choiceC;
    }

    // Getters
    public String getQuestion() { return question; }
    public String getAnswer() { return answer; }
    public String getChoiceA() { return choiceA; }
    public String getChoiceB() { return choiceB; }
    public String getChoiceC() { return choiceC; }
}
