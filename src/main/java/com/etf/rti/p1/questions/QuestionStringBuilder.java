package com.etf.rti.p1.questions;

import com.etf.rti.p1.app.SinGenContext;
import com.etf.rti.p1.ui.questions.QuestionAskedForType;
import com.etf.rti.p1.ui.questions.QuestionGrammarGivenType;

/**
 * Helper class for creating string for questions according to given question type, asked for question type and answers
 */
class QuestionStringBuilder {

    static String build(QuestionGrammarGivenType givenType, QuestionAskedForType askedForType, String givenParameter, String answerA, String answerB, String answerC) {
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
            case GRAMMAR_AS_SYNTAX_DIAGRAM:
                switch (askedForType) {
                    case CORRECT_SEQUENCE_FOR_FIRST_NONTERMINAL:
                        return "Koja od ponuđenih sekvenci odgovara sintaksnoj definiciji za izraz <"
                                + SinGenContext.getFirstNonTerminalSymbol() + "> zadat u vidu sintaksnog dijagrama?\n\n";
                }
            case CORRECT_SEQUENCE_FOR_FIRST_NON_TERMINAL:
                switch (askedForType) {
                    case CORRECT_GRAMMAR_FOR_FIRST_NONTERMINAL_SEQUENCE:
                        return buildQuestionPartForGivenNonterminalAskedForCorrectGrammar(givenParameter);
                    case CORRECT_RULE_WHICH_SHOULD_BE_ADDED:
                        return "Koje od ponudjenih pravila je potrebno dodati gramatici u BNF formatu "
                                + "tako da izrazu <" + SinGenContext.getFirstNonTerminalSymbol() + "> odgovara " +
                                "sekvenca " + givenParameter + "?\n\n"
                                + SinGenContext.getGrammarBNF() + "\n\n";
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
