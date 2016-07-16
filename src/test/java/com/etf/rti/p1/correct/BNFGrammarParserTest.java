package com.etf.rti.p1.correct;

import org.junit.Assert;
import org.junit.Test;

/**
 * Testing purposes of BNFGrammarParser glass by parsing input grammar setting generated graph for grammar,
 * then creating GrammarChecker and AnswerGenerator which in 1000 iterations print generated correct and corrupt answers for
 * input grammar.
 */
public class BNFGrammarParserTest {
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
            "<slovo> ::= a|b|c|d|e|f|grammarGraph|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z\n" +
            "<cifra> ::= 0|1|2|3|4|5|6|7|8|9";

    @Test
    public void testGrammarParser() {
        int correctCounter = 0;
        int corruptedCounter = 0;
        BNFGrammarParser BNFGrammarParser = new BNFGrammarParser(GRAMMAR_INPUT_TEST_1);

        Graph grammarGraph = BNFGrammarParser.parse();
        //TODO: check what these grammarGraph methods are used for
        grammarGraph.setCompositeNodesToRecursive();
        grammarGraph.setNodesToRecursive();
        grammarGraph.setNodesToInfinite();
        grammarGraph.setWidthToAllNodes();
        grammarGraph.setDifferenceLenToRecursiveNodes();

        //TODO: shouldn't provide private field as constructor parameter! Think about providing BNFGrammarParser as an argument
        GrammarChecker grammarChecker = null;
        try {
            grammarChecker = new GrammarChecker(BNFGrammarParser.getGrammar());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("GrammarChecker couldn't be initialized");
            System.exit(-1);
        }

        int answerLength = 10;
        AnswerGenerator answerGenerator = new AnswerGenerator(BNFGrammarParser.getGrammarGraph(), answerLength, false);

        for (int i = 0; i < 1000; i++) {

            String generatedAnswer = answerGenerator.generateAnswer(BNFGrammarParser.getGrammarGraph().getRoot(), answerLength);
            String corruptedAnswer = answerGenerator.corruptCorrectAnswer(generatedAnswer);

            System.out.print("***GENERISANI STRING*** ");
            String answer = generatedAnswer;
            if (corruptedAnswer.length() != 0) {
                answer = corruptedAnswer;
            }
            System.out.println(answer + " duzina: " + answer.length());


            if (grammarChecker.isAnswerCorrect(answer)) {
                correctCounter++;
            } else {
                corruptedCounter++;
            }
        }
        System.out.println("Brojac odgovora: tacni = " + correctCounter + ", netacni = " + corruptedCounter);

        Assert.assertTrue(correctCounter > corruptedCounter);
    }
}
