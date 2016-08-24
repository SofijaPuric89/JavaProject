package com.etf.rti.p1.ui.questions;

import com.etf.rti.p1.questions.QuestionGenerator;
import com.etf.rti.p1.ui.UIObservable;

import java.util.function.Consumer;

public class GenerateQuestionDialogController implements GenerateQuestionDialogListener {

    private final QuestionGenerator questionGenerator;

    public GenerateQuestionDialogController(UIObservable<GenerateQuestionDialogListener> myObservable, String grammar) {
        myObservable.addUIListener(this);

        questionGenerator = new QuestionGenerator(grammar);
    }

    @Override
    public void generateCorrectAnswer(int answerLength, Consumer<String> callback) {
        callback.accept(questionGenerator.generateGrammaticallyCorrectAnswer(answerLength));
    }

    @Override
    public void generateIncorrectAnswer(int answerLength, Consumer<String> callback) {
        callback.accept(questionGenerator.generateGrammaticallyIncorrectAnswer(answerLength));
    }

    @Override
    public void checkIfAnswerCorrect(String answer, Consumer<Boolean> callback) {
        callback.accept(questionGenerator.isAnswerGrammaticallyCorrect(answer));
    }

    @Override
    public void buildQuestion(QuestionModelElement element, String answerA, String answerB, String answerC, Consumer<String> callback) {
        callback.accept(questionGenerator.buildQuestionString(element.getQuestionGrammarGivenType(), element.getQuestionAskedForType(), answerA, answerB, answerC));
    }
}
