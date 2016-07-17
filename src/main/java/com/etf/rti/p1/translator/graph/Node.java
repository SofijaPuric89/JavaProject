package com.etf.rti.p1.translator.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Storing data relevant for grammar model node. Tracking children and parents of the single node, and ordered number
 * of recursive children. Class keeping an information about if the node is recursive and complete
 */
public class Node<T> {
    private List<Node<T>> children = new ArrayList<>();
    private List<Node<T>> parents = new ArrayList<>();
    private T data = null;
    private boolean isRecursive;
    private boolean isComplete;
    private List<Node<T>> ordNumOfRecursiveChildren = new ArrayList<Node<T>>();

    public Node(T data) {
        this.data = data;
    }

    public Node(T data, Node<T> parent) {
        this.data = data;
        this.parents.add(parent);
    }

    public List<Node<T>> getOrdNumOfRecursiveChildren() {
        return ordNumOfRecursiveChildren;
    }

    public void setOrdNumOfRecursiveChildren(List<Node<T>> ordNumOfRecursiveChildren) {
        this.ordNumOfRecursiveChildren = ordNumOfRecursiveChildren;
    }

    public void addOrdNumOfRecursiveChildren(Node<T> recChild) {
        ordNumOfRecursiveChildren.add(recChild);
    }

    public void setRecursive(boolean recursive) {
        isRecursive = recursive;
    }

    public List<Node<T>> getChildren() {
        return children;
    }

    public List<Node<T>> getParents() {
        return parents;
    }

    public boolean isRecursive() {
        return isRecursive;
    }

    public void setParentOnly(Node<T> parent) {
        this.parents.add(parent);
    }

    public void setParent(Node<T> parent) {

        // parent.addChild(this);
        parent.children.add(this);
        this.parents.add(parent);

    }

    public void addChild(T data) {
        Node<T> child = new Node<T>(data);
        child.setParent(this);
        this.children.add(child);
    }

    public void addChild(Node<T> child) {
        //child.setParent(this);
        child.parents.add(this);
        this.children.add(child);
    }

    public void addChildOnly(Node<T> child) {
        this.children.add(child);
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isRoot() {
        //return (this.parent == null);
        boolean b = false;
        for (Node<T> parent : parents) {
            b = true;
        }
        return b;
    }

    public boolean isLeaf() {
        return this.children.size() == 0;
    }

    public void removeParent(int num) {
        this.parents.remove(num);
    }

    @Override
    public String toString() {
        String result = "";
        for (Node<T> child : children)
            if (child != null)
                result += child + "\n";
        return result;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}