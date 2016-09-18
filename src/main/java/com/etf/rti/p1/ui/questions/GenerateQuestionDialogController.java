package com.etf.rti.p1.ui.questions;

import com.etf.rti.p1.app.SinGenContext;
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
    public void generateCorrectAnswer(QuestionModelElement selectedQuestionType, int answerLength, Consumer<String> callback) {
        String answer;
        if (selectedQuestionType.getQuestionAskedForType() == QuestionAskedForType.CORRECT_RULE_WHICH_SHOULD_BE_ADDED) {
            answer = questionGenerator.generateCorrectRuleAnswer();
        } else {
            answer = questionGenerator.generateGrammaticallyCorrectSequence(answerLength);
        }
        callback.accept(answer);
    }

    @Override
    public void generateCorrectSequence(QuestionModelElement selectedQuestionType, int answerLength, Consumer<String> callback) {
        String correctSequence = questionGenerator.generateGrammaticallyCorrectSequence(answerLength);
        if (selectedQuestionType.getQuestionAskedForType() == QuestionAskedForType.CORRECT_RULE_WHICH_SHOULD_BE_ADDED) {
            questionGenerator.setCorrectSequence(correctSequence);
        }
        callback.accept(correctSequence);
    }

    @Override
    public void generateIncorrectAnswer(QuestionModelElement selectedQuestionType, int answerLength, Consumer<String> callback) {
        String answer;
        if (selectedQuestionType.getQuestionAskedForType() == QuestionAskedForType.CORRECT_RULE_WHICH_SHOULD_BE_ADDED) {
            answer = questionGenerator.generateIncorrectRuleAnswer();
        } else {
            answer = questionGenerator.generateGrammaticallyIncorrectSequence(answerLength);
        }
        callback.accept(answer);
    }

    @Override
    public void checkIfAnswerCorrect(QuestionModelElement selectedQuestionType, String answer, Consumer<Boolean> callback) {
        boolean isAnswerCorrect;
        if (selectedQuestionType.getQuestionAskedForType() == QuestionAskedForType.CORRECT_RULE_WHICH_SHOULD_BE_ADDED) {
            isAnswerCorrect = questionGenerator.isMissingRuleCorrect(answer);
        } else {
            isAnswerCorrect = questionGenerator.isAnswerGrammaticallyCorrect(answer);
        }
        callback.accept(isAnswerCorrect);
    }

    @Override
    public void buildQuestion(QuestionModelElement element, String givenParameter, String answerA, String answerB, String answerC, Consumer<String> callback) {
        callback.accept(questionGenerator.buildQuestionString(element.getQuestionGrammarGivenType(), element.getQuestionAskedForType(), givenParameter, answerA, answerB, answerC));
    }
}
