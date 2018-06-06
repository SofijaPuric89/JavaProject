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
    private static List<String> grammarSampleFileNames;

    private GrammarSamples() {
    }

    private static List<String> loadGrammarSampleFileNames() {
        try {
            grammarSampleFileNames = readLines(
                    GrammarSamples.class.getClassLoader()
                            .getResourceAsStream(GRAMMARS_RESOURCE_ROOT),
                    "UTF-8");
            return grammarSampleFileNames;
        } catch (IOException e) {
            System.err.println("Couldn't load grammar sample files");
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> getGrammarSampleFileNames() {
        if (grammarSampleFileNames == null) {
            loadGrammarSampleFileNames();
        }
        return grammarSampleFileNames;
    }

    public static String getGrammarSampleFileName(int index) {
        if (grammarSampleFileNames == null) {
            loadGrammarSampleFileNames();
        }
        if (grammarSampleFileNames == null || index < 0 || index >= grammarSampleFileNames.size()) {
            return null;
        }
        return grammarSampleFileNames.get(index);
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
        if (grammarSampleFileNames == null) {
            loadGrammarSampleFileNames();
        }
        if (grammarSampleFileNames == null) {
            return null;
        }
        int index = new Random(System.currentTimeMillis()).nextInt(grammarSampleFileNames.size());
        return readGrammarSample(index);
    }

    public static String readGrammarSample(String readFileName) {
        if (grammarSampleFileNames == null) {
            loadGrammarSampleFileNames();
        }
        if (grammarSampleFileNames == null) {
            return null;
        }
        try {
            return readGrammarSample(grammarSampleFileNames.indexOf(readFileName));
        } catch (Exception e) {
            System.err.println("Couldn't read grammar sample file " + readFileName);
            e.printStackTrace();
            return null;
        }
    }
}
