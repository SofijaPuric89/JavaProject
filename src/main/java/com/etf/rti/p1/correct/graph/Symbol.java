package com.etf.rti.p1.correct.graph;

import java.util.*;

/**
 * Created by Korisnik on 8.1.2016.
 */
public class Symbol {
    private String name;
    private boolean isNode;
    private int maxLen;
    private boolean isNonterminal;
    private boolean isComposite;
    private int depth;
    private int inverseDepth;
    private List<Integer> widths = new ArrayList<Integer>();
    private int width;
    private boolean isInfinite;
    private HashMap<String, Lists> differenceLen = new HashMap<String, Lists>();
    private ArrayList<Integer> differences = new ArrayList<Integer>();

    public HashMap<String, Lists> getDifferenceLen() {
        return differenceLen;
    }

    public List<Integer> getDifferences(String name) {
        return differenceLen.get(name).getDifferences();
    }

    public void setDifferenceLen(HashMap<String, Lists> differenceLen) {
        this.differenceLen = differenceLen;
    }

    public void setDifferencesLengths(HashMap<String, Lists> diffs) {
        for (Map.Entry<String, Lists> entry : diffs.entrySet()) {
            String key = entry.getKey();
            Lists value = entry.getValue();
            setDifferenceLenArray(key, value);
        }
    }


    public void setDifferenceLenArray(String key, Lists arr) {
        if (differenceLen.get(key) != null) {
            differenceLen.get(key).getDifferences().addAll(arr.getDifferences());
            differenceLen.get(key).getMinimums().addAll(arr.getMinimums());
        } else {
            Lists l = new Lists(arr.getMinimums(), arr.getDifferences());
            differenceLen.put(key, l);
        }
// add elements to all, including duplicates
        Set<Integer> hs = new HashSet<Integer>();
        hs.addAll(differenceLen.get(key).getDifferences());
        differenceLen.get(key).getDifferences().clear();
        differenceLen.get(key).getDifferences().addAll(hs);
        hs.clear();
        hs.addAll(differenceLen.get(key).getMinimums());
        differenceLen.get(key).getMinimums().clear();
        differenceLen.get(key).getMinimums().addAll(hs);
    }

    public Symbol(String n, boolean is, int len, boolean term, boolean comp) {
        setName(n);
        setNode(is);
        setMaxLen(len);
        setNonterminal(term);
        setComposite(comp);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int w) {
        width = w;
    }

    public void setWidths(List<Integer> w) {
        widths = w;
    }

    public void setDepth(int d) {
        depth = d;
    }

    public int getDepth() {
        return depth;
    }

    public void addWidth(int w) {
        widths.add(w);
    }

    public List<Integer> getWidths() {
        return widths;
    }

    public void setInverseDepth(int d) {
        inverseDepth = d;
    }

    public int getInverseDepth() {
        return inverseDepth;
    }

    public void setNonterminal(boolean term) {
        isNonterminal = term;
    }

    public boolean isNonterminal() {
        return isNonterminal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNode() {
        return isNode;
    }

    public void setNode(boolean node) {
        isNode = node;
    }

    public boolean isInfinite() {
        return isInfinite;
    }

    public void setInfinite(boolean inf) {
        isInfinite = inf;
    }

    public int getMaxLen() {
        return maxLen;
    }

    public void setMaxLen(int maxLen) {
        this.maxLen = maxLen;
    }

    public void setComposite(boolean composite) {
        this.isComposite = composite;
    }

    public boolean isComposite() {
        return isComposite;
    }


}
