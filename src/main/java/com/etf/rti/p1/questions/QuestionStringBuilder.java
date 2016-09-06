package com.etf.rti.p1.questions;

import com.etf.rti.p1.app.SinGenContext;
import com.etf.rti.p1.ui.questions.QuestionAskedForType;
import com.etf.rti.p1.ui.questions.QuestionGrammarGivenType;
import com.sun.xml.internal.bind.v2.runtime.IllegalAnnotationException;

/**
 * Helper class for creating string for questions according to given question type, asked for question type and answers
 */
class QuestionStringBuilder {

    static String build(QuestionGrammarGivenType givenType, QuestionAskedForType askedForType, String givenParameter,String answerA, String answerB, String answerC) {
        return buildQuestionPart(givenType, askedForType, givenParameter) + buildAnswerPart(answerA, answerB, answerC);
    }

    private static String buildAnswerPart(String answerA, String answerB, String answerC) {
        StringBuilder sb = new StringBuilder();
        sb.append("A)   ").append(answerA).append("\n");
        sb.append("B)   ").append(answerB).append("\n");
        sb.append("C)   ").append(answerC).append("\n");
        return sb.toString();
    }

    private static String buildQuestionPart(QuestionGrammarGivenType givenType, QuestionAskedForType askedForType, String givenParameter) {
        switch (givenType) {
            case GRAMMAR_IN_BNF:
                switch (askedForType) {
                    case CORRECT_SEQUENCE_FOR_FIRST_NONTERMINAL:
                        return buildQuestionPartForGivenGrammarAskedForFirstNonterminal("BNF", SinGenContext.getGrammarBNF());
                }
            case GRAMMAR_IN_EBNF:
                switch (askedForType) {
                    case CORRECT_SEQUENCE_FOR_FIRST_NONTERMINAL:
                        return buildQuestionPartForGivenGrammarAskedForFirstNonterminal("EBNF", SinGenContext.getGrammarEBNF());
                }
            case CORRECT_SEQUENCE_FOR_FIRST_NON_TERMINAL:
                switch (askedForType) {
                    case CORRECT_GRAMMAR_FOR_FIRST_NONTERMINAL_SEQUENCE:
                        return buildQuestionPartForGivenNonterminalAskedForCorrectGrammar(givenParameter);
                }
        }
        throw new IllegalArgumentException("Unsupported question type combination");
    }

    private static String buildQuestionPartForGivenNonterminalAskedForCorrectGrammar(String sequence) {
        if (sequence == null || sequence.isEmpty()) {
            throw new IllegalArgumentException("Couldn't create this question type without correct sequence being set");
        }

        return "Kojim od ponuđenih sintaksnih pravila za startni neterminal <" + SinGenContext.getFirstNonTerminalSymbol()
                + "> odgovara izraz " + sequence + "?\n\n";
    }

    private static String buildQuestionPartForGivenGrammarAskedForFirstNonterminal(String grammarNotation, String grammar) {
        return "Koja od ponuđenih sekvenci odgovara sintaksnoj definiciji za izraz <"
                + SinGenContext.getFirstNonTerminalSymbol() + "> zadat u "
                + grammarNotation + " notaciji?\n\n"
                + grammar + "\n\n";
    }

}
