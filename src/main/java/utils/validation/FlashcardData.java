package utils.validation;

public class FlashcardData implements FlashcardQuestionData {
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

    @Override
    public String getQuestion() { return question; }
    @Override
    public String getAnswer() { return answer; }
    @Override
    public String getChoiceA() { return choiceA; }
    @Override
    public String getChoiceB() { return choiceB; }
    @Override
    public String getChoiceC() { return choiceC; }
}
