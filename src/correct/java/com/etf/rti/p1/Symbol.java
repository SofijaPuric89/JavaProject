package src.correct.java.com.etf.rti.p1;

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
    private HashMap<String, List> differenceLen = new HashMap<String, List>();
    private ArrayList<Integer> differences = new ArrayList<Integer>();

    public HashMap<String, List> getDifferenceLen() {
        return differenceLen;
    }

    public List<Integer> getDifferences(String name) {
        return differenceLen.get(name);
    }

    public void setDifferenceLen(HashMap<String, List> differenceLen) {
        this.differenceLen = differenceLen;
    }

    public void setDifferencesLengths(HashMap<String, List> diffs) {
        for (HashMap.Entry<String, List> entry : diffs.entrySet()) {
            String key = entry.getKey();
            List value = entry.getValue();
            setDifferenceLenArray(key, value);
        }
    }


    public void setDifferenceLenArray(String key, List<Integer> arr) {
        if (differenceLen.get(key) != null)
            differenceLen.get(key).addAll(arr);
        else
            differenceLen.put(key, arr);
// add elements to al, including duplicates
        Set<Integer> hs = new HashSet<Integer>();
        hs.addAll(differenceLen.get(key));
        differenceLen.get(key).clear();
        differenceLen.get(key).addAll(hs);
    }

    public Symbol(String n, boolean is, int len, boolean term, boolean comp) {
        setName(n);
        setNode(is);
        setMaxLen(len);
        setNonterminal(term);
        setComposite(comp);
    }

    public int getWidth() { return width; }

    public void setWidth(int w) { width = w; }

    public void setWidths(List<Integer> w) { widths = w; }

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
