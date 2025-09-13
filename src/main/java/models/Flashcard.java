package models;

public class Flashcard {
    private int flashcard_id;
    private int sets_id;
    private int times_answered;
    private int times_correct;
    private String question;
    private String answer;
    private String choice_a;
    private String choice_b;
    private String choice_c;

    public Flashcard(int flashcard_id, int sets_id, int times_answered, int times_correct, String question, String answer, String choice_a, String choice_b, String choice_c) {
        this.flashcard_id = flashcard_id;
        this.sets_id = sets_id;
        this.times_answered = times_answered;
        this.times_correct = times_correct;
        this.question = question;
        this.answer = answer;
        this.choice_a = choice_a;
        this.choice_b = choice_b;
        this.choice_c = choice_c;
    }

  // getters
    public int getFlashcard_id() {
        return flashcard_id;
    }

    public int getSets_id() {
        return sets_id;
    }

    public int getTimes_answered() {
        return times_answered;
    }

    public int getTimes_correct() {
        return times_correct;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getChoice_a() {
        return choice_a;
    }

    public String getChoice_b() {
        return choice_b;
    }

    public String getChoice_c() {
        return choice_c;
    }

    // setters
    public void setFlashcard_id(int flashcard_id) {
        this.flashcard_id = flashcard_id;
    }
    public void setSets_id(int sets_id) {
        this.sets_id = sets_id;
    }
    public void setTimes_answered(int times_answered) {
        this.times_answered = times_answered;
    }
    public void setTimes_correct(int times_correct) {
        this.times_correct = times_correct;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public void setChoice_a(String choice_a) {
        this.choice_a = choice_a;
    }
    public void setChoice_b(String choice_b) {
        this.choice_b = choice_b;
    }
    public void setChoice_c(String choice_c) {
        this.choice_c = choice_c;
    }

}
