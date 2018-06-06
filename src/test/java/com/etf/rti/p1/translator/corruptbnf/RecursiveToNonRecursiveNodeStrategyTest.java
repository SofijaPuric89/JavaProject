package com.etf.rti.p1.translator.corruptbnf;

import com.etf.rti.p1.app.SinGenContext;
import com.etf.rti.p1.util.GrammarSamples;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecursiveToNonRecursiveNodeStrategyTest {

    private RecursiveToNonRecursiveNodeStrategy strategy;

    @Before
    public void setUp() throws Exception {
        SinGenContext.setGrammarBNF(GrammarSamples.readGrammarSample(0));
        strategy = new RecursiveToNonRecursiveNodeStrategy();
    }

    @Test
    public void testTryToCorruptNonRecursive() throws Exception {
        assertNull(strategy.tryToCorrupt(SinGenContext.getParser().getRules(), 3));
    }

    @Test
    public void testTryToCorruptWithOneRecursive() throws Exception {
        assertNotNull(strategy.tryToCorrupt(SinGenContext.getParser().getRules(), 2));
    }

    @Test
    public void testTryToCorruptWithMoreThanOneRecursive() throws Exception {
        assertNotNull(strategy.tryToCorrupt(SinGenContext.getParser().getRules(), 1));
    }
}