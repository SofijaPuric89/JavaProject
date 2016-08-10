package com.etf.rti.p1.questions;

public class QuestionGenerator {

    private final AnswerGenerator answerGenerator;

    public QuestionGenerator(String grammar) {
        answerGenerator = new AnswerGenerator(grammar);
    }


}
