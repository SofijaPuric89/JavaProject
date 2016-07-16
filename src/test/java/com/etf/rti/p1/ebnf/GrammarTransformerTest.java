package com.etf.rti.p1.ebnf;

import com.etf.rti.p1.bnf.BNFCompiler;
import com.etf.rti.p1.ebnf.rules.IRule;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Testing grammar transformations to EBNF
 */
public class GrammarTransformerTest {
    private GrammarTransformer transform;
    private static final String GRAMMAR_INPUT_TEST_1 =
            "<p> ::= <korisnik>!<domen>\n" +
                    "<korisnik> ::= <rec> | <korisnik>_<rec>\n" +
                    "<domen> ::= <kraj_domena> | <rec>.<domen>\n" +
                    "<kraj_domena> ::= com | co.rs\n" +
                    "<rec> ::= <slovo> | <slovo><rec> | <rec><cifra>\n" +
                    "<slovo> ::= a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z\n" +
                    "<cifra> ::= 0|1|2|3|4|5|6|7|8|9";

    @Before
    public void setup() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BNFCompiler compiler = new BNFCompiler("test", "com.etf.rti.p1.bnf", out);
        compiler.setInput(new ByteArrayInputStream(GRAMMAR_INPUT_TEST_1.getBytes("UTF-8")));
        compiler.getParser().init();

        transform = new GrammarTransformer(compiler);
    }

    @Test
    public void testTransformToEBNF() throws Exception {
        LinkedList<IRule> rules = transform.transformToEBNF();

        assertTrue(!rules.isEmpty());
        assertEquals(7, rules.size());
        assertEquals("p = korisnik \"!\" domen.", rules.get(0).toString());
        assertEquals("korisnik = rec { \"_\" rec }.", rules.get(1).toString());
        assertEquals("domen = { rec \".\" } kraj_domena.", rules.get(2).toString());
        assertEquals("kraj_domena = ( \"co.rs\" | \"com\" ).", rules.get(3).toString());
        assertEquals("rec = { slovo } slovo { cifra }.", rules.get(4).toString());
        assertEquals("slovo = ( \"a\" | \"b\" | \"c\" | \"d\" | \"e\" | \"f\" | \"g\" | \"h\" | \"i\" | \"j\" | \"k\" | \"l\" | \"m\" | \"n\" | \"o\" | \"p\" | \"q\" | \"r\" | \"s\" | \"t\" | \"u\" | \"v\" | \"w\" | \"x\" | \"y\" | \"z\" ).", rules.get(5).toString());
    }
}