package com.etf.rti.p1.questions;

import org.junit.Test;

import static org.junit.Assert.*;

public class GrammarCheckerTest {

    @Test
    public void testIsAnswerGrammaticallyCorrect() throws Exception {
        GrammarChecker checker = new GrammarChecker("<p>::=<malo_slovo>:\\<put>\n" +
                "<put>::=<dir>|<dir>\\\\<dir>|\\\"<dir>\\\"\\\\<put>\n" +
                "<dir>::=<malo_slovo>|<dir><pom>\n" +
                "<pom>::=<malo_slovo>|_|<cifra>\n" +
                "<malo_slovo>::=a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z\n" +
                "<cifra>::=0|1|2|3|4|5|6|7|8|9\n");
        assertTrue(checker.isAnswerGrammaticallyCorrect("a:\\q_238sq"));
        GrammarChecker checker2 = new GrammarChecker("<p>::=<malo_slovo>:\\<put>\n" +
                "<put>::=<dir>|<put>\\\\<dir>|\\\"<dir>\\\"\\\\<put>\n" +
                "<dir>::=<malo_slovo>|<pom><pom>\n" +
                "<pom>::=<malo_slovo>|_|<cifra>\n" +
                "<malo_slovo>::=a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z\n" +
                "<cifra>::=0|1|2|3|4|5|6|7|8|9\n");
        assertFalse(checker2.isAnswerGrammaticallyCorrect("a:\\q_238sq"));
    }
}