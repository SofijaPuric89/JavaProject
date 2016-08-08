package com.etf.rti.p1.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import static org.apache.commons.io.IOUtils.readLines;

/**
 * Helper class for loading sample grammars
 */
public class GrammarSamples {

    private static final String GRAMMARS_RESOURCE_ROOT = "grammars/";
    private static List<String> grammarExampleFileNames;

    private GrammarSamples() {
    }

    private static List<String> loadGrammarSampleFileNames() {
        try {
            grammarExampleFileNames = readLines(
                    GrammarSamples.class.getClassLoader()
                            .getResourceAsStream(GRAMMARS_RESOURCE_ROOT),
                    "UTF-8");
            return grammarExampleFileNames;
        } catch (IOException e) {
            System.err.println("Couldn't load grammar sample files");
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> getGrammarSampleFileNames() {
        if (grammarExampleFileNames == null) {
            loadGrammarSampleFileNames();
        }
        return grammarExampleFileNames;
    }

    public static String getGrammarSampleFileName(int index) {
        if (grammarExampleFileNames == null) {
            loadGrammarSampleFileNames();
        }
        if (grammarExampleFileNames == null || index < 0 || index >= grammarExampleFileNames.size()) {
            return null;
        }
        return grammarExampleFileNames.get(index);
    }

    public static String readGrammarSample(int index) {
        String grammarSampleFileName = getGrammarSampleFileName(index);

        if (grammarSampleFileName == null || grammarSampleFileName.isEmpty()) {
            return null;
        }
        try {
            return IOUtils.toString(GrammarSamples.class.getClassLoader().getResourceAsStream(GRAMMARS_RESOURCE_ROOT + grammarSampleFileName));
        } catch (IOException e) {
            System.err.println("Couldn't load grammar sample file " + grammarSampleFileName);
            e.printStackTrace();
            return null;
        }
    }

    public static String readRandomGrammarSample() {
        if (grammarExampleFileNames == null) {
            loadGrammarSampleFileNames();
        }
        if (grammarExampleFileNames == null) {
            return null;
        }
        int index = new Random(System.currentTimeMillis()).nextInt(grammarExampleFileNames.size());
        return readGrammarSample(index);
    }

    public static String readGrammarSample(String readFileName) {
        if (grammarExampleFileNames == null) {
            loadGrammarSampleFileNames();
        }
        if (grammarExampleFileNames == null) {
            return null;
        }
        try {
            return readGrammarSample(grammarExampleFileNames.indexOf(readFileName));
        } catch (Exception e) {
            System.err.println("Couldn't read grammar sample file " + readFileName);
            e.printStackTrace();
            return null;
        }
    }
}
