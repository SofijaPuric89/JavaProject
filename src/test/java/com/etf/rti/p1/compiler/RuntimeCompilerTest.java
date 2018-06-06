package com.etf.rti.p1.compiler;

import com.etf.rti.p1.util.Utils;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * Created by zika on 09.12.2015..
 */
public class RuntimeCompilerTest {
    private CompilerGenerator gen;

    @Before
    public void setUp() throws Exception {
        File classDir = new File(Utils.getResourcePath());
        File grammarFile = new File(classDir, "bnf.g4");
        String grammar = grammarFile.getAbsolutePath();
        gen = new CompilerGenerator(grammar);
    }

    @Test
    public void testGetParser() throws Exception {
        Compiler compiler = gen.generate();
        assertEquals("bnf", compiler.getGrammarName());
        AParser parser;
        try {
            compiler.getParser();
            assertEquals(true, false);
        } catch (Exception ignored) {
        }
        InputStream in = new ByteArrayInputStream("<test>::=a".getBytes("UTF-8"));
        compiler.setInput(in);
        parser = compiler.getParser();
        assertEquals(parser.getClass().getCanonicalName(), "com.etf.rti.p1.generated.bnfParser");
        parser.init();
    }
}