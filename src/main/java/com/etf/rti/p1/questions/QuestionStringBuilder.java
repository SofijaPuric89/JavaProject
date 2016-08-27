package com.etf.rti.p1.questions;

import com.etf.rti.p1.app.SinGenContext;
import com.etf.rti.p1.ui.questions.QuestionAskedForType;
import com.etf.rti.p1.ui.questions.QuestionGrammarGivenType;

/**
 * Helper class for creating string for questions according to given question type, asked for question type and answers
 */
class QuestionStringBuilder {

    static String build(QuestionGrammarGivenType givenType, QuestionAskedForType askedForType, String answerA, String answerB, String answerC) {
        return buildQuestionPart(givenType, askedForType) + buildAnswerPart(answerA, answerB, answerC);
    }

    private static String buildAnswerPart(String answerA, String answerB, String answerC) {
        StringBuilder sb = new StringBuilder();
        sb.append("A)   ").append(answerA).append("\n");
        sb.append("B)   ").append(answerB).append("\n");
        sb.append("C)   ").append(answerC).append("\n");
        return sb.toString();
    }

    private static String buildQuestionPart(QuestionGrammarGivenType givenType, QuestionAskedForType askedForType) {
        switch (givenType) {
            case GRAMMAR_IN_BNF:
                switch (askedForType) {
                    case CORRECT_SEQUENCE_FOR_FIRST_NONTERMINAL:
                        return buildQuestionPartForFirstNonterminal("BNF", SinGenContext.getGrammarBNF());
                }
            case GRAMMAR_IN_EBNF:
                switch (askedForType) {
                    case CORRECT_SEQUENCE_FOR_FIRST_NONTERMINAL:
                        return buildQuestionPartForFirstNonterminal("EBNF", SinGenContext.getGrammarEBNF());
                }
        }
        throw new IllegalArgumentException("Unsupported question type combination");
    }

    private static String buildQuestionPartForFirstNonterminal(String grammarNotation, String grammar) {
        return "Koja od ponuđenih sekvenci odgovara sintaksnoj definiciji za izraz <"
                + SinGenContext.getFirstNonTerminalSymbol() + "> zadat u "
                + grammarNotation + " notaciji?\n\n"
                + grammar + "\n\n";
    }


}
