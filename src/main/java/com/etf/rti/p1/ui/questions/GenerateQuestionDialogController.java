package com.etf.rti.p1.ui.questions;

import com.etf.rti.p1.questions.QuestionGenerator;
import com.etf.rti.p1.ui.UserInterfaceObservable;

import java.util.function.Consumer;

public class GenerateQuestionDialogController implements GenerateQuestionDialogListener {

    private final QuestionGenerator questionGenerator;

    public GenerateQuestionDialogController(UserInterfaceObservable<GenerateQuestionDialogListener> myObservable, String grammar) {
        myObservable.addUIListener(this);

        questionGenerator = new QuestionGenerator(grammar);
    }

    @Override
    public void generateCorrectAnswer(int answerLength, Consumer<String> callback) {
        callback.accept(questionGenerator.generateGrammaticallyCorrectAnswer(answerLength));
    }

    @Override
    public void generateIncorrectAnswer(int answerLength, Consumer<String> callback) {
        callback.accept(questionGenerator.generateGrammaticalyIncorrectAnswer(answerLength));
    }

    @Override
    public void checkIfAnswerCorrect(String answer, Consumer<Boolean> callback) {
        callback.accept(questionGenerator.isAnswerGrammaticallyCorrect(answer));
    }
}
