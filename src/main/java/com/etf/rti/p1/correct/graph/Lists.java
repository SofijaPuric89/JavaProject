package com.etf.rti.p1.correct.graph;

import java.util.List;

/**
 * Created by Korisnik on 21.1.2016.
 */
public class Lists {
    private List<Integer> minimums;
    private List<Integer> differences;

    public Lists(List<Integer> m, List<Integer> d) {
        minimums = m;
        differences = d;
    }

    public List<Integer> getMinimums() {
        return minimums;
    }

    public void setMinimums(List<Integer> minimums) {
        this.minimums = minimums;
    }

    public List<Integer> getDifferences() {
        return differences;
    }

    public void setDifferences(List<Integer> differences) {
        this.differences = differences;
    }

    public String toString() {
        return "differences: " + differences + " minimums: " + minimums;
    }
}
