package src.correct.java.com.etf.rti.p1;

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

    public Symbol(String n, boolean is, int len, boolean term, boolean comp) {
        setName(n);
        setNode(is);
        setMaxLen(len);
        setNonterminal(term);
        setComposite(comp);
    }

    public void setDepth(int d) {
        depth = d;
    }
    public int getDepth() {
        return depth;
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
