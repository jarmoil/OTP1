package utils.validation;

public class FlashcardValidator implements Validator<FlashcardData> {

    @Override
    public void validate(FlashcardData data) throws IllegalArgumentException {
        validateFieldsNotEmpty(data);
        validateAnswerMatchesChoice(data);
    }

    private void validateFieldsNotEmpty(FlashcardData data) {
        if (isFieldEmpty(data.getQuestion()) || isFieldEmpty(data.getAnswer()) ||
                isFieldEmpty(data.getChoiceA()) || isFieldEmpty(data.getChoiceB()) ||
                isFieldEmpty(data.getChoiceC())) {
            throw new IllegalArgumentException("All fields must be filled out");
        }
    }

    private void validateAnswerMatchesChoice(FlashcardData data) {
        String answer = data.getAnswer();
        if (!answer.equals(data.getChoiceA()) && !answer.equals(data.getChoiceB()) &&
                !answer.equals(data.getChoiceC())) {
            throw new IllegalArgumentException("The answer must match one of the choices");
        }
    }

    private boolean isFieldEmpty(String field) {
        return field == null || field.trim().isEmpty();
    }
}
