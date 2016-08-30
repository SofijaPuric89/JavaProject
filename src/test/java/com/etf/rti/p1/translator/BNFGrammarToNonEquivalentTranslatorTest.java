package com.etf.rti.p1.translator;

import com.etf.rti.p1.app.SinGenContext;
import com.etf.rti.p1.util.GrammarSamples;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class BNFGrammarToNonEquivalentTranslatorTest {
    private BNFGrammarToNonEquivalentTranslator translator;

    @Before
    public void setUp() throws Exception {
        SinGenContext.setGrammarBNF(GrammarSamples.readGrammarSample(0));
        translator = new BNFGrammarToNonEquivalentTranslator(SinGenContext.getParser(), "a:\\q_238sq");
    }

    @Test
    public void testSample0GetRuleCandidatesToCorrupt() {
        List<Integer> rulesToCorrupt = translator.getRuleCandidatesToCorrupt(translator.getBnfParser().getRules());
        assertEquals(3, rulesToCorrupt.size());
    }

    @Test
    public void testSample0translateToNonEquivalentBNF() throws Exception {
        String nonEquivalentBNF = translator.translateToNonEquivalentBNF();
        assertNotNull(nonEquivalentBNF);
        assertNotEquals(nonEquivalentBNF, SinGenContext.getGrammarBNF());
    }
}
