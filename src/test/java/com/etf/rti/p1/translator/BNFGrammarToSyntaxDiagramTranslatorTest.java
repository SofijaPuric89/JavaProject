package com.etf.rti.p1.translator;

import com.etf.rti.p1.util.GrammarSamples;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class BNFGrammarToSyntaxDiagramTranslatorTest {

    private BNFGrammarToSyntaxDiagramTranslator translator;
    private String grammarSample;

    @Before
    public void setup() throws Exception {
        translator = new BNFGrammarToSyntaxDiagramTranslator();
        grammarSample = GrammarSamples.readGrammarSample(2);
    }

    @Test
    public void testTransformToSyntaxDiagram() throws Exception {
        translator.transformToSyntaxDiagram(grammarSample);

        //TODO: write some assertions when method is finally defined and implemented
    }

    @Test
    public void testGeneratingSyntaxDiagram() throws Exception {
        File pngFileLocation = translator.transformToSyntaxDiagram(grammarSample);
        System.out.println("png file location: " + pngFileLocation.getAbsolutePath());
    }
}
