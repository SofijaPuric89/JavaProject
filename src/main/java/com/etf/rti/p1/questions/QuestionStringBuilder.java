package com.etf.rti.p1.questions;

import com.etf.rti.p1.app.SinGenContext;
import com.etf.rti.p1.ui.questions.QuestionAskedForType;
import com.etf.rti.p1.ui.questions.QuestionGrammarGivenType;
import com.etf.rti.p1.util.Utils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.IOException;

/**
 * Helper class for creating string for questions according to given question type, asked for question type and answers
 */
class QuestionStringBuilder {

    static String build(QuestionGrammarGivenType givenType, QuestionAskedForType askedForType, String givenParameter, String answerA, String answerB, String answerC) {
        try {
            String templateFile = IOUtils.toString(QuestionStringBuilder.class.getClassLoader().getResourceAsStream("templates/question_text.html"));
            String questionPart = buildQuestionPart(givenType, askedForType, givenParameter);
            String grammarPart = buildGrammarPart(givenType, askedForType);
            return templateFile
                    .replace("{$question_part$}", escapeHtml(questionPart))
                    .replace("{$grammar_part$}", grammarPart)
                    .replace("{$answer_a_part$}", "A)   " + escapeHtmlAndReplaceLineBreaks(answerA))
                    .replace("{$answer_b_part$}", "B)   " + escapeHtmlAndReplaceLineBreaks(answerB))
                    .replace("{$answer_c_part$}", "C)   " + escapeHtmlAndReplaceLineBreaks(answerC));
        } catch (IOException e) {
            // TODO: Log
            e.printStackTrace();
            return "";
        }

    }

    private static String buildQuestionPart(QuestionGrammarGivenType givenType, QuestionAskedForType askedForType, String givenParameter) {
        switch (givenType) {
            case GRAMMAR_IN_BNF:
                switch (askedForType) {
                    case CORRECT_SEQUENCE_FOR_FIRST_NONTERMINAL:
                        return buildQuestionPartForGivenGrammarAskedForFirstNonterminal("BNF");
                }
            case GRAMMAR_IN_EBNF:
                switch (askedForType) {
                    case CORRECT_SEQUENCE_FOR_FIRST_NONTERMINAL:
                        return buildQuestionPartForGivenGrammarAskedForFirstNonterminal("EBNF");
                }
            case GRAMMAR_AS_SYNTAX_DIAGRAM:
                switch (askedForType) {
                    case CORRECT_SEQUENCE_FOR_FIRST_NONTERMINAL:
                        return "Koja od ponuđenih sekvenci odgovara sintaksnoj definiciji za izraz <"
                                + SinGenContext.getFirstNonTerminalSymbol() + "> zadat u vidu sintaksnog dijagrama?";
                }
            case CORRECT_SEQUENCE_FOR_FIRST_NON_TERMINAL:
                switch (askedForType) {
                    case CORRECT_GRAMMAR_FOR_FIRST_NONTERMINAL_SEQUENCE:
                        return buildQuestionPartForGivenNonterminalAskedForCorrectGrammar(givenParameter);
                    case CORRECT_RULE_WHICH_SHOULD_BE_ADDED:
                        return "Koje od ponudjenih pravila je potrebno dodati gramatici u BNF formatu "
                                + "tako da izrazu <" + SinGenContext.getFirstNonTerminalSymbol() + "> odgovara " +
                                "sekvenca " + givenParameter + "?";
                }
        }
        throw new IllegalArgumentException("Unsupported question type combination");
    }

    private static String buildQuestionPartForGivenNonterminalAskedForCorrectGrammar(String sequence) {
        if (sequence == null || sequence.isEmpty()) {
            throw new IllegalArgumentException("Couldn't create this question type without correct sequence being set");
        }

        return "Kojim od ponuđenih sintaksnih pravila za startni neterminal <" + SinGenContext.getFirstNonTerminalSymbol()
                + "> odgovara izraz " + sequence + "?";
    }

    private static String buildQuestionPartForGivenGrammarAskedForFirstNonterminal(String grammarNotation) {
        return "Koja od ponuđenih sekvenci odgovara sintaksnoj definiciji za izraz <"
                + SinGenContext.getFirstNonTerminalSymbol() + "> zadat u "
                + grammarNotation + " notaciji?";
    }

    private static String buildGrammarPart(QuestionGrammarGivenType givenType, QuestionAskedForType askedForType) {
        switch (givenType) {
            case GRAMMAR_IN_BNF:
                return escapeHtmlAndReplaceLineBreaks(SinGenContext.getGrammarBNF());
            case GRAMMAR_IN_EBNF:
                return escapeHtmlAndReplaceLineBreaks(SinGenContext.getGrammarEBNF());
            case GRAMMAR_AS_SYNTAX_DIAGRAM:
                try {
                    return "<img src=\"file:/" + SinGenContext.getSyntaxDiagramFile().getCanonicalPath() + "\" />";
                } catch (IOException e) {
                    return "";
                }
            case CORRECT_SEQUENCE_FOR_FIRST_NON_TERMINAL:
                if (askedForType == QuestionAskedForType.CORRECT_GRAMMAR_FOR_FIRST_NONTERMINAL_SEQUENCE) {
                    return "";
                } else {
                    return escapeHtmlAndReplaceLineBreaks(SinGenContext.getGrammarBNF());
                }
        }
        throw new IllegalArgumentException("Unsupported question type combination");
    }

    private static String escapeHtml(String unescapedString) {
        return StringEscapeUtils.escapeHtml4(unescapedString);
    }

    private static String escapeHtmlAndReplaceLineBreaks(String unescapedString) {
        return escapeHtml(unescapedString).replaceAll("\\r\\n", "<br/>");
    }
}
