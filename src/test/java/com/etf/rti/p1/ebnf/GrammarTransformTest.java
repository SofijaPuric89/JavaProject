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
public class GrammarTransformTest {
    private GrammarTransform transform;
    private static final String input = "<a> ::= <b><a> | <a><c><c> | <a><c> | <a><g><a><t> | <e> | <c> | <c><d> " +
            "| <d><e> | <g><e> | <d><g><b><e> | <d><b><e> | <j><k> | <j><t> | <j> | <z> | <z><k> | <z><t> | <t><a><t>",
            result = "a = ( t a t | { b } ( ( j | z ) [ ( k | t ) ] | [ ( d [ [ g ] b ] | g ) ] e | c [ d ] ) " +
                    "{ ( c [ c ] | g a t ) } )." + System.lineSeparator();
    private ByteArrayOutputStream outResult;

    @Before
    public void setUp() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BNFCompiler compiler = new BNFCompiler("test", "com.etf.rti.p1.bnf", out);
        compiler.setInput(new ByteArrayInputStream(input.getBytes("UTF-8")));
        AParser parser = compiler.getParser();
        parser.init();

        outResult = new ByteArrayOutputStream();
        transform = new GrammarTransform(parser.getRules(), outResult);
    }

    @Test
    public void testDoJob() throws Exception {
        transform.doJob();
        assertEquals(result, outResult.toString());

    }
}