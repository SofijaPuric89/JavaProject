package com.etf.rti.p1.ui.questions;

import com.etf.rti.p1.app.SinGenContext;

class QuestionModelElement {
    private final QuestionGrammarGivenType questionGrammarGivenType;
    private final QuestionAskedForType questionAskedForType;
    private final String comboBoxDisplayText;
    // TODO: We don't need this
    private final String questionText;

    public QuestionModelElement(QuestionGrammarGivenType questionGrammarGivenType, QuestionAskedForType questionAskedForType, String comboBoxDisplayText) {
        this.questionGrammarGivenType = questionGrammarGivenType;
        this.questionAskedForType = questionAskedForType;
        this.comboBoxDisplayText = comboBoxDisplayText;
        this.questionText = createQuestionText(questionGrammarGivenType, questionAskedForType);
    }

    private static String createQuestionText(QuestionGrammarGivenType questionGrammarGivenType, QuestionAskedForType questionAskedForType) {
        String question = "";
        if (questionAskedForType == QuestionAskedForType.CORRECT_SEQUENCE_FOR_FIRST_NONTERMINAL && questionGrammarGivenType == QuestionGrammarGivenType.GRAMMAR_IN_BNF) {
            String firstNonterminal = SinGenContext.getFirstNonTerminalSymbol();
            question.concat("Koja od ponuđenih sekvenci odgovara sintaksnoj definiciji za "+firstNonterminal+" zadatoj u BNF notaciji");
        }
            return null;
    }

    public QuestionGrammarGivenType getQuestionGrammarGivenType() {
        return questionGrammarGivenType;
    }

    public QuestionAskedForType getQuestionAskedForType() {
        return questionAskedForType;
    }


    @Override
    public String toString() {
        return comboBoxDisplayText;
    }
}
