package com.etf.rti.p1.questions;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: rename class and check purpose purpose, extracted from main method located in PermutationGenerator
 */
public class PermutationGeneratorTest {

    public void testPermutationGenerator() {
        PermutationGenerator pg = new PermutationGenerator(30);
        List<Integer> mins = new ArrayList<>();
        mins.add(1); //mins.add(3); mins.add(5);
        List<Integer> diffs = new ArrayList<>();
        //  diffs.add(0); diffs.add(5);
        pg.addToSet(mins, diffs);
        System.out.println(pg.getSetOfLengths());
    }
}
