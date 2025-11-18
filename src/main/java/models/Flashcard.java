package models;

public class Flashcard {
    private int flashcardId;
    private int setsId;
    private int timesAnswered;
    private int timesCorrect;
    private String question;
    private String answer;
    private String choiceA;
    private String choiceB;
    private String choiceC;

    public Flashcard(int flashcardId, int setsId, int timesAnswered, int timesCorrect,
                     String question, String answer,
                     String choiceA, String choiceB, String choiceC) {
        this.flashcardId = flashcardId;
        this.setsId = setsId;
        this.timesAnswered = timesAnswered;
        this.timesCorrect = timesCorrect;
        this.question = question;
        this.answer = answer;
        this.choiceA = choiceA;
        this.choiceB = choiceB;
        this.choiceC = choiceC;
    }

    public int getFlashcardId() {
        return flashcardId;
    }

    public int getSetsId() {
        return setsId;
    }

    public int getTimesAnswered() {
        return timesAnswered;
    }

    public int getTimesCorrect() {
        return timesCorrect;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getChoiceA() {
        return choiceA;
    }

    public String getChoiceB() {
        return choiceB;
    }

    public String getChoiceC() {
        return choiceC;
    }

    public void setFlashcardId(int flashcardId) {
        this.flashcardId = flashcardId;
    }

    public void setSetsId(int setsId) {
        this.setsId = setsId;
    }

    public void setTimesAnswered(int timesAnswered) {
        this.timesAnswered = timesAnswered;
    }

    public void setTimesCorrect(int timesCorrect) {
        this.timesCorrect = timesCorrect;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setChoiceA(String choiceA) {
        this.choiceA = choiceA;
    }

    public void setChoiceB(String choiceB) {
        this.choiceB = choiceB;
    }

    public void setChoiceC(String choiceC) {
        this.choiceC = choiceC;
    }
}
