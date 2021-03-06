package com.etf.rti.p1.compiler;

import com.etf.rti.p1.util.Utils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * Created by zika on 09.12.2015..
 */
public class CompilerGeneratorTest {

    private CompilerGenerator gen;

    @Before
    public void setup() throws Exception {
        File classDir = new File(Utils.getResourcePath());
        File grammarFile = new File(classDir, "bnf.g4");
        String grammar = grammarFile.getAbsolutePath();
        gen = new CompilerGenerator(grammar);
    }

    @Test
    public void testGenerateRuntimeCompiler() throws Exception {
        Compiler compiler = gen.generate();
        Assert.assertTrue(compiler instanceof RuntimeCompiler);
    }
}