package com.etf.rti.p1.generator;

import com.etf.rti.p1.util.Util;

import java.io.File;

/**
 * Created by zika on 09.12.2015..
 */
public class GeneratorTest {

    private Generator gen;

    @org.junit.Before
    public void setUp() throws Exception {
        File classDir = new File(Util.getInstance().getResourcePath());
        File grammarFile = new File(classDir, "bnf.g4");
        String grammar = grammarFile.getAbsolutePath();
        gen = new Generator(grammar);
    }

    @org.junit.Test
    public void testGenerate() throws Exception {
        gen.generate();
    }
}