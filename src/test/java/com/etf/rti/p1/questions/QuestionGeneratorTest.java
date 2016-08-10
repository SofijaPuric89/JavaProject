package com.etf.rti.p1.questions;

import com.etf.rti.p1.util.GrammarSamples;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class QuestionGeneratorTest {

    private QuestionGenerator generator;

    @Before
    public void setUp() throws Exception {
        String grammarSample = GrammarSamples.readGrammarSample(2);
        generator = new QuestionGenerator(grammarSample);
    }

    @Test
    public void generateCorrectAnswer() throws Exception {
        for (int i = 0; i < 1000; i++) {
            String correctAnswer = generator.generateGrammaticallyCorrectAnswer(10);
            boolean isAnswerGrammaticallyCorrect = generator.isAnswerGrammaticallyCorrect(correctAnswer);
            assertTrue("Failed after " + i + " iterations", isAnswerGrammaticallyCorrect);
        }
    }

    @Test
    public void generateIncorrectAnswer() throws Exception {
        for (int i = 0; i < 1000; i++) {
            String incorrectAnswer = generator.generateGrammaticalyIncorrectAnswer(10);
            boolean isAnswerGrammaticallyCorrect = generator.isAnswerGrammaticallyCorrect(incorrectAnswer);
            assertFalse("Failed after " + i + " iterations", isAnswerGrammaticallyCorrect);
        }
    }

}