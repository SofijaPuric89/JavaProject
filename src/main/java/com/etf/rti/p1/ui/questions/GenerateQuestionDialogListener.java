package com.etf.rti.p1.ui.questions;

import java.util.function.Consumer;

public interface GenerateQuestionDialogListener {
    void generateCorrectAnswer(int answerLength, Consumer<String> callback);

    void generateIncorrectAnswer(int answerLength, Consumer<String> callback);

    void checkIfAnswerCorrect(String answer, Consumer<Boolean> callback);
}
