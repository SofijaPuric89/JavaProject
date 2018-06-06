package com.etf.rti.p1.translator.corruptbnf;

import com.etf.rti.p1.app.SinGenContext;
import com.etf.rti.p1.util.GrammarSamples;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ExcludeFirstChildNodeStrategyTest {

    private ExcludeFirstChildNodeStrategy strategy;

    @Before
    public void setUp() throws Exception {
        SinGenContext.setGrammarBNF(GrammarSamples.readGrammarSample("sample_feb2016.bnf"));
        strategy = new ExcludeFirstChildNodeStrategy();
    }

    @Test
    public void testTryToCorruptWithOneChildNode() throws Exception {
        assertNull(strategy.tryToCorrupt(SinGenContext.getParser().getRules(), 3));
    }

    @Test
    public void testTryToCorruptWithTwoChildNode() throws Exception {
        assertNotNull(strategy.tryToCorrupt(SinGenContext.getParser().getRules(), 2));
    }

    @Test
    public void testTryToCorruptWithMoreThanTwoChildNode() throws Exception {
        assertNotNull(strategy.tryToCorrupt(SinGenContext.getParser().getRules(), 1));
    }
}