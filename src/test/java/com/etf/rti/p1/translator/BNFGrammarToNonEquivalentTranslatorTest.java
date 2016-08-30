package com.etf.rti.p1.translator;

import com.etf.rti.p1.app.SinGenContext;
import com.etf.rti.p1.util.GrammarSamples;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class BNFGrammarToNonEquivalentTranslatorTest {

    @Test
    public void testSample0GetRuleCandidatesToCorrupt() {
        SinGenContext.setGrammarBNF(GrammarSamples.readGrammarSample(0));
        BNFGrammarToNonEquivalentTranslator translator = new BNFGrammarToNonEquivalentTranslator(SinGenContext.getParser(), null);
        List<Integer> rulesToCorrupt = translator.getRuleCandidatesToCorrupt(translator.getBnfParser().getRules());
        assertEquals(3, rulesToCorrupt.size());
    }

}
