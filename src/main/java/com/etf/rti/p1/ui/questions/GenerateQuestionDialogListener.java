package com.etf.rti.p1.ui.questions;

import java.util.function.Consumer;

public interface GenerateQuestionDialogListener {
    void generateCorrectAnswer(QuestionModelElement selectedQuestionType, int answerLength, Consumer<String> callback);

    void generateCorrectSequence(QuestionModelElement selectedQuestionType, int answerLength, Consumer<String> callback);

    void generateIncorrectAnswer(QuestionModelElement selectedQuestionType, int answerLength, Consumer<String> callback);

    void checkIfAnswerCorrect(QuestionModelElement selectedQuestionType, String answer, Consumer<Boolean> callback);

    void buildQuestion(QuestionModelElement element, String givenParameter, String answerA, String answerB, String answerC, Consumer<String> callback);
}
