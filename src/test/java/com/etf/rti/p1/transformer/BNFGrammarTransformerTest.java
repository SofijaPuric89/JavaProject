package com.etf.rti.p1.transformer;

import com.etf.rti.p1.compiler.bnf.BNFCompiler;
import com.etf.rti.p1.transformer.rules.IRule;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Testing grammar transformations to EBNF
 */
public class BNFGrammarTransformerTest {
    private static final String GRAMMAR_INPUT_TEST_1 =
            "<p> ::= <korisnik>!<domen>\n" +
                    "<korisnik> ::= <rec> | <korisnik>_<rec>\n" +
                    "<domen> ::= <kraj_domena> | <rec>.<domen>\n" +
                    "<kraj_domena> ::= com | co.rs\n" +
                    "<rec> ::= <slovo> | <slovo><rec> | <rec><cifra>\n" +
                    "<slovo> ::= a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z\n" +
                    "<cifra> ::= 0|1|2|3|4|5|6|7|8|9";

    private static final String GRAMMAR_INPUT_TEST_2 =
            "<start> ::= 11<a>|<b>1\n" +
                    "<a> ::= 1|<a><b>|<a><c><b>\n" +
                    "<b> ::= 101|<b>01\n" +
                    "<c> ::= 1100|<c>11|<c>00\n";

    private BNFCompiler compiler;
    private BNFGrammarTransformer transformer;

    @Before
    public void setup() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        compiler = new BNFCompiler("test", "com.etf.rti.p1.compiler.bnf", out);
        compiler.setInput(new ByteArrayInputStream(GRAMMAR_INPUT_TEST_1.getBytes("UTF-8")));
        compiler.getParser().init();
        transformer = new BNFGrammarTransformer();
    }

    @Test
    public void testTransformToEBNF() throws Exception {
        List<IRule> rules = transformer.transformToEBNF(compiler.getParser().getRules());

        assertTrue(!rules.isEmpty());
        assertEquals(7, rules.size());
        assertEquals("p = korisnik \"!\" domen.", rules.get(0).toString());
        assertEquals("korisnik = rec { \"_\" rec }.", rules.get(1).toString());
        assertEquals("domen = { rec \".\" } kraj_domena.", rules.get(2).toString());
        assertEquals("kraj_domena = ( \"co.rs\" | \"com\" ).", rules.get(3).toString());
        assertEquals("rec = { slovo } slovo { cifra }.", rules.get(4).toString());
        assertEquals("slovo = ( \"a\" | \"b\" | \"c\" | \"d\" | \"e\" | \"f\" | \"g\" | \"h\" | \"i\" | \"j\" | \"k\" | \"l\" | \"m\" | \"n\" | \"o\" | \"p\" | \"q\" | \"r\" | \"s\" | \"t\" | \"u\" | \"v\" | \"w\" | \"x\" | \"y\" | \"z\" ).", rules.get(5).toString());
    }

    @Test
    public void testTransformToSyntaxDiagram() throws Exception {
        transformer.transformToSyntaxDiagram(GRAMMAR_INPUT_TEST_2);

        //TODO: write some assertions when method is finally defined and implemented
    }
}