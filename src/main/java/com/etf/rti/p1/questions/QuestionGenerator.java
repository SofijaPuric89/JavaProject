package com.etf.rti.p1.questions;

import com.etf.rti.p1.ui.questions.QuestionAskedForType;
import com.etf.rti.p1.ui.questions.QuestionGrammarGivenType;

public class QuestionGenerator {

    private static final int ANSWER_RETRY_FACTOR = 2;

    private final AnswerGenerator answerGenerator;
    private GrammarChecker grammarChecker;

    public QuestionGenerator(String grammar) {
        answerGenerator = new AnswerGenerator(grammar);
        try {
            grammarChecker = new GrammarChecker(grammar);
        } catch (Exception e) {
            // TODO: Refactor
            e.printStackTrace();
        }
    }

    public boolean isAnswerGrammaticallyCorrect(String answer) {
        return grammarChecker.isAnswerGrammaticallyCorrect(answer);
    }

    public String generateGrammaticallyCorrectAnswer(int answerLength) {
        int noOfRetries = 0;
        // TODO: When we fix the AnswerGenerator, we can just return answer (generateAnswer for now doesn't create correct answer)
        while (noOfRetries < (answerLength * ANSWER_RETRY_FACTOR)) {
            String answer = answerGenerator.generateAnswer(answerLength);
            if (isAnswerGrammaticallyCorrect(answer)) {
                return answer;
            }
            noOfRetries++;
        }
        throw new RuntimeException("Cannot generate correct answer after " + noOfRetries + " retries");
    }

    public String generateGrammaticallyIncorrectAnswer(int answerLength) {
        String answer = answerGenerator.generateAnswer(answerLength);
        if (!grammarChecker.isAnswerGrammaticallyCorrect(answer)) {
            return answer;
        }
        int noOfRetries = 0;
        while (noOfRetries < (answerLength * ANSWER_RETRY_FACTOR)) {
            String incorrectAnswer = answerGenerator.corruptCorrectAnswer(answer);
            if (!isAnswerGrammaticallyCorrect(incorrectAnswer)) {
                return incorrectAnswer;
            }
            noOfRetries++;
        }
        throw new RuntimeException("Cannot generate incorrect answer after " + noOfRetries + " retries");
    }

    public String buildQuestionString(QuestionGrammarGivenType givenType, QuestionAskedForType askedForType, String answerA, String answerB, String answerC) {
        return QuestionStringBuilder.build(givenType, askedForType, answerA, answerB, answerC);
    }
}
