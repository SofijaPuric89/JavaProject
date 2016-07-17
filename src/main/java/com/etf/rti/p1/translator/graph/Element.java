package com.etf.rti.p1.translator.graph;

/**
 * Created by Korisnik on 21.1.2016.
 */
public class Element {
    private String parentName;
    private Node<Symbol> compositeDirectRecursiveNode;

    public Element(String n, Node<Symbol> node) {
        parentName = n;
        compositeDirectRecursiveNode = node;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Node<Symbol> getCompositeDirectRecursiveNode() {
        return compositeDirectRecursiveNode;
    }

    public void setCompositeDirectRecursiveNode(Node<Symbol> compositeDirectRecursiveNode) {
        this.compositeDirectRecursiveNode = compositeDirectRecursiveNode;
    }
}
