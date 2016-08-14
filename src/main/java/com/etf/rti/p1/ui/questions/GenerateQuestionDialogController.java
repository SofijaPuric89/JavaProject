package com.etf.rti.p1.ui.questions;

import com.etf.rti.p1.questions.QuestionGenerator;
import com.etf.rti.p1.ui.UIObservable;

import java.util.function.Consumer;

public class GenerateQuestionDialogController implements GenerateQuestionDialogListener {

    private final UIObservable<GenerateQuestionDialogListener> myObservable;

    private final QuestionGenerator questionGenerator;

    public GenerateQuestionDialogController(UIObservable<GenerateQuestionDialogListener> myObservable, String grammar) {
        this.myObservable = myObservable;
        myObservable.addUIListener(this);

        questionGenerator = new QuestionGenerator(grammar);
    }

    @Override
    public void generateCorrectAnswer(int answerLength, Consumer<String> callback) {
        callback.accept(questionGenerator.generateGrammaticallyCorrectAnswer(answerLength));
    }

    @Override
    public void generateIncorrectAnswer(int answerLengt, Consumer<String> callback) {
        callback.accept(questionGenerator.generateGrammaticalyIncorrectAnswer(answerLengt));
    }
}
