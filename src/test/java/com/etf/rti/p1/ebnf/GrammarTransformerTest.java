package com.etf.rti.p1.ebnf;

import com.etf.rti.p1.bnf.BNFCompiler;
import com.etf.rti.p1.generator.AParser;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.Assert.*;

/**
 * Created by sule on 12/12/15.
 */
public class GrammarTransformerTest {
    private GrammarTransformer transform;
    private static final String input =
            "<p> ::= <korisnik>!<domen>\n" +
                    "<korisnik> ::= <rec> | <korisnik>_<rec>\n" +
                    "<domen> ::= <kraj_domena> | <rec>.<domen>\n" +
                    "<kraj_domena> ::= com | co.rs\n" +
                    "<rec> ::= <slovo> | <slovo><rec> | <rec><cifra>\n" +
                    "<slovo> ::= a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z\n" +
                    "<cifra> ::= 0|1|2|3|4|5|6|7|8|9",
            result = "a = ( t a t | { b } ( ( j | z ) [ ( k | t ) ] | [ ( d [ [ g ] b ] | g ) ] e | c [ d ] ) " +
                    "{ ( c [ c ] | g a t ) } )." + System.lineSeparator();
    private ByteArrayOutputStream outResult;

    @Before
    public void setup() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BNFCompiler compiler = new BNFCompiler("test", "com.etf.rti.p1.bnf", out);
        compiler.setInput(new ByteArrayInputStream(input.getBytes("UTF-8")));
        AParser parser = compiler.getParser();
        parser.init();

        outResult = new ByteArrayOutputStream();
        transform = new GrammarTransformer(parser.getRules(), outResult);
    }

    @Test
    public void testTransform() throws Exception {
        transform.transform();
        assertEquals(result, outResult.toString());
    }
}