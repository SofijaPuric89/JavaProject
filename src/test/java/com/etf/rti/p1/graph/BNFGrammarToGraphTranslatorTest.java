package com.etf.rti.p1.graph;

import com.etf.rti.p1.translator.BNFGrammarToGraphTranslator;
import com.etf.rti.p1.translator.graph.Graph;
import com.etf.rti.p1.questions.AnswerGenerator;
import com.etf.rti.p1.questions.GrammarChecker;
import org.junit.Assert;
import org.junit.Test;

/**
 * Testing purposes of BNFGrammarToGraphTranslator glass by parsing input grammar setting generated model for grammar,
 * then creating GrammarChecker and AnswerGenerator which in 1000 iterations print generated correct and corrupt answers for
 * input grammar.
 */
public class BNFGrammarToGraphTranslatorTest {
    private static final String GRAMMAR_INPUT_TEST_1 = "<p> ::= <malo_slovo>:\\<put>\n" +
            "<put> ::= <dir> | <put>\\<dir> | \"<dir>\"\\<put>\n" +
            "<dir> ::= <malo_slovo> | <dir><pom>\n" +
            "<pom> ::= <malo_slovo> | _ | <cifra>\n" +
            "<malo_slovo> ::= a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z\n" +
            "<cifra> ::= 0|1|2|3|4|5|6|7|8|9";
    private static final String GRAMMAR_INPUT_TEST_2 = "<p> ::= <korisnik>!<domen>\n" +
            "<korisnik> ::= <rec> | <korisnik>_<rec>\n" +
            "<domen> ::= <kraj_domena> | <rec>.<domen>\n" +
            "<kraj_domena> ::= com | co.rs\n" +
            "<rec> ::= <slovo> | <slovo><rec> | <rec><cifra>\n" +
            "<slovo> ::= a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z\n" +
            "<cifra> ::= 0|1|2|3|4|5|6|7|8|9";

    @Test
    public void testGrammarParser() {
        int correctCounter = 0;
        int corruptedCounter = 0;
        BNFGrammarToGraphTranslator BNFGrammarToGraphTranslator = new BNFGrammarToGraphTranslator(GRAMMAR_INPUT_TEST_2);

        Graph grammarGraph = BNFGrammarToGraphTranslator.parse();
        //TODO: check what these grammarGraph methods are used for
        grammarGraph.setCompositeNodesToRecursive();
        grammarGraph.setNodesToRecursive();
        grammarGraph.setNodesToInfinite();
        grammarGraph.setWidthToAllNodes();
        grammarGraph.setDifferenceLenToRecursiveNodes();

        //TODO: shouldn't provide private field as constructor parameter! Think about providing BNFGrammarToGraphTranslator as an argument
        GrammarChecker grammarChecker = null;
        try {
            grammarChecker = new GrammarChecker(BNFGrammarToGraphTranslator.getGrammar());
        } catch (Exception e) {
            Assert.fail();
        }

        int answerLength = 10;
        AnswerGenerator answerGenerator = new AnswerGenerator(BNFGrammarToGraphTranslator.getGrammarGraph(), answerLength, false);

        for (int i = 0; i < 1000; i++) {
            answerGenerator.calculateCorruptAnswerParameters(answerLength, answerLength);

            String generatedAnswer = answerGenerator.generateAnswer(BNFGrammarToGraphTranslator.getGrammarGraph().getRoot(), answerLength);
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