package src.correct.java.com.etf.rti.p1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private HashMap<String, ArrayList> differenceLen = new HashMap<String, ArrayList>();

    public HashMap<String, ArrayList> getDifferenceLen() {
        return differenceLen;
    }

    public void setDifferenceLen(HashMap<String, ArrayList> differenceLen) {
        this.differenceLen = differenceLen;
    }

    public void addDifferenceLen(String key, int d) {
        differenceLen.get(key).add(d);
    }

    public void setDifferenceLenArray(String key, ArrayList<Integer> arr) {
        differenceLen.get(key).addAll(arr);
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