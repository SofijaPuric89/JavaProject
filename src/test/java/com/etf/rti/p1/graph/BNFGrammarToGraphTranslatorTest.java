package com.etf.rti.p1.graph;

import com.etf.rti.p1.translator.BNFGrammarToGraphTranslator;
import com.etf.rti.p1.translator.graph.Graph;
import com.etf.rti.p1.questions.AnswerGenerator;
import com.etf.rti.p1.questions.GrammarChecker;
import com.etf.rti.p1.util.GrammarSamples;
import org.junit.Assert;
import org.junit.Test;

/**
 * Testing purposes of BNFGrammarToGraphTranslator glass by parsing input grammar setting generated model for grammar,
 * then creating GrammarChecker and AnswerGenerator which in 1000 iterations print generated correct and corrupt answers for
 * input grammar.
 */
public class BNFGrammarToGraphTranslatorTest {

    @Test
    public void testGrammarParser() {
        int correctCounter = 0;
        int corruptedCounter = 0;
        String grammarSample = GrammarSamples.readGrammarSample(1);

        //TODO: shouldn't provide private field as constructor parameter! Think about providing BNFGrammarToGraphTranslator as an argument
        GrammarChecker grammarChecker = null;
        try {
            grammarChecker = new GrammarChecker(grammarSample);
        } catch (Exception e) {
            Assert.fail();
        }

        int answerLength = 10;
        AnswerGenerator answerGenerator = new AnswerGenerator(grammarSample);

        for (int i = 0; i < 1000; i++) {
            answerGenerator.calculateCorruptAnswerParameters(answerLength, answerLength);

            String generatedAnswer = answerGenerator.generateAnswer(answerLength);

            if (!grammarChecker.isAnswerGrammaticallyCorrect(generatedAnswer)) {
                System.out.println("****** ILLEGAL ANSWER " + generatedAnswer);
                continue;
            }

            String corruptedAnswer = answerGenerator.corruptCorrectAnswer(generatedAnswer);

            System.out.print("***GENERISANI ODGOVOR:");
            String answer = generatedAnswer;

            if (corruptedAnswer.length() != 0 && !grammarChecker.isAnswerGrammaticallyCorrect(corruptedAnswer)) {
                answer = corruptedAnswer;
                corruptedCounter++;
                System.out.print("[corrupted] ");
            } else {
                correctCounter++;
                System.out.print("[correct] ");
            }
            System.out.println(answer + " duzina: " + answer.length());
        }
        System.out.println("Brojac odgovora: tacni = " + correctCounter + ", netacni = " + corruptedCounter);

        //expect to have more generated corrupted answers
        // while 1000 trials of generating corrupted answers
        Assert.assertTrue(correctCounter < corruptedCounter);
    }
}
