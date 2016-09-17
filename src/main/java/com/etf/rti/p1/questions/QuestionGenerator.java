package com.etf.rti.p1.questions;

import com.etf.rti.p1.app.SinGenContext;
import com.etf.rti.p1.translator.BNFGrammarToNonEquivalentTranslator;
import com.etf.rti.p1.translator.ebnf.rules.IRule;
import com.etf.rti.p1.ui.questions.QuestionAskedForType;
import com.etf.rti.p1.ui.questions.QuestionGrammarGivenType;
import com.etf.rti.p1.util.Utils;

import java.util.Collections;
import java.util.List;

public class QuestionGenerator {

    private static final int ANSWER_RETRY_FACTOR = 2;

    private final AnswerGenerator answerGenerator;
    private List<IRule> corruptedRulesFromRule;
    private int indexToCorruptRule;
    private GrammarChecker grammarChecker;
    private BNFGrammarToNonEquivalentTranslator toNonEquivalentTranslator;

    public QuestionGenerator(String grammar) {
        answerGenerator = new AnswerGenerator(grammar);
        try {
            grammarChecker = new GrammarChecker(grammar);
            toNonEquivalentTranslator =
                    new BNFGrammarToNonEquivalentTranslator(
                            SinGenContext.getParser(),
                            SinGenContext.getFirstNonTerminalSymbol());
            indexToCorruptRule = toNonEquivalentTranslator.findIndexToCorruptRule();
            corruptedRulesFromRule = toNonEquivalentTranslator.corruptedRulesFromRule(indexToCorruptRule);
        } catch (Exception e) {
            // TODO: Refactor
            e.printStackTrace();
        }
    }

    public boolean isAnswerGrammaticallyCorrect(String answer) {
        return grammarChecker.isAnswerGrammaticallyCorrect(answer);
    }

    public String generateGrammaticallyCorrectSequence(int answerLength) {
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

    public String generateGrammaticallyIncorrectSequence(int answerLength) {
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

    public String buildQuestionString(QuestionGrammarGivenType givenType, QuestionAskedForType askedForType, String givenParameter, String answerA, String answerB, String answerC) {
        return QuestionStringBuilder.build(givenType, askedForType, givenParameter, answerA, answerB, answerC);
    }

    public String generateCorrectRuleAnswer() {
        return SinGenContext.getParser().getRules().get(indexToCorruptRule).toBNFString();
    }

    public String generateIncorrectRuleAnswer() {
        if (corruptedRulesFromRule == null || corruptedRulesFromRule.isEmpty()) {
            return null;
        }
        Collections.shuffle(corruptedRulesFromRule);
        return corruptedRulesFromRule.get(0).toBNFString();
    }

    public boolean isMissingRuleCorrect(String rule) {
        return !corruptedRulesFromRule.stream().anyMatch(r -> r.toBNFString().equals(rule));
    }
}
