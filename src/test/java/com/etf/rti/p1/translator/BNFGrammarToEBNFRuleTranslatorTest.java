package com.etf.rti.p1.translator;

import com.etf.rti.p1.compiler.bnf.BNFCompiler;
import com.etf.rti.p1.translator.ebnf.rules.IRule;
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
public class BNFGrammarToEBNFRuleTranslatorTest {
    private static final String GRAMMAR_INPUT_TEST_1 =
            "<p> ::= <korisnik>!<domen>\n" +
                    "<korisnik> ::= <rec> | <korisnik>_<rec>\n" +
                    "<domen> ::= <kraj_domena> | <rec>.<domen>\n" +
                    "<kraj_domena> ::= com | co.rs\n" +
                    "<rec> ::= <slovo> | <slovo><rec> | <rec><cifra>\n" +
                    "<slovo> ::= a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z\n" +
                    "<cifra> ::= 0|1|2|3|4|5|6|7|8|9";

    private BNFCompiler compiler;
    private BNFGrammarToEBNFRuleTranslator translator;

    @Before
    public void setup() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        compiler = new BNFCompiler("test", "com.etf.rti.p1.compiler.bnf", out);
        compiler.setInput(new ByteArrayInputStream(GRAMMAR_INPUT_TEST_1.getBytes("UTF-8")));
        compiler.getParser().init();
        translator = new BNFGrammarToEBNFRuleTranslator();
    }

    @Test
    public void testTransformToEBNF() throws Exception {
        List<IRule> rules = translator.transformToEBNF(compiler.getParser().getRules());

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