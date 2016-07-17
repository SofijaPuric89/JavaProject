package com.etf.rti.p1.translator;

import org.junit.Before;
import org.junit.Test;

public class BNFGrammarToSyntaxDiagramTranslatorTest {
    private static final String GRAMMAR_INPUT_TEST_2 =
            "<start> ::= 11<a>|<b>1\n" +
                    "<a> ::= 1|<a><b>|<a><c><b>\n" +
                    "<b> ::= 101|<b>01\n" +
                    "<c> ::= 1100|<c>11|<c>00\n";

    private BNFGrammarToSyntaxDiagramTranslator translator;

    @Before
    public void setup() throws Exception {
        translator = new BNFGrammarToSyntaxDiagramTranslator();
    }

    @Test
    public void testTransformToSyntaxDiagram() throws Exception {
        translator.transformToSyntaxDiagram(GRAMMAR_INPUT_TEST_2);

        //TODO: write some assertions when method is finally defined and implemented
    }
}
