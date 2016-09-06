package com.etf.rti.p1.ui.questions;

import com.etf.rti.p1.app.SinGenContext;

class QuestionModelElement {
    private final QuestionGrammarGivenType questionGrammarGivenType;
    private final QuestionAskedForType questionAskedForType;
    private final String comboBoxDisplayText;

    public QuestionModelElement(QuestionGrammarGivenType questionGrammarGivenType, QuestionAskedForType questionAskedForType, String comboBoxDisplayText) {
        this.questionGrammarGivenType = questionGrammarGivenType;
        this.questionAskedForType = questionAskedForType;
        this.comboBoxDisplayText = comboBoxDisplayText;
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
