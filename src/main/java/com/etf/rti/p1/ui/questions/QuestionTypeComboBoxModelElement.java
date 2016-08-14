package com.etf.rti.p1.ui.questions;

class QuestionTypeComboBoxModelElement {
    private final QuestionGivenType questionGivenType;
    private final QuestionAskedForType questionAskedForType;
    private final String text;

    public QuestionTypeComboBoxModelElement(QuestionGivenType questionGivenType, QuestionAskedForType questionAskedForType, String text) {
        this.questionGivenType = questionGivenType;
        this.questionAskedForType = questionAskedForType;
        this.text = text;
    }

    public QuestionGivenType getQuestionGivenType() {
        return questionGivenType;
    }

    public QuestionAskedForType getQuestionAskedForType() {
        return questionAskedForType;
    }

    @Override
    public String toString() {
        return text;
    }
}
