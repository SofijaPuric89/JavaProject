package com.etf.rti.p1.correct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Korisnik on 8.1.2016.
 */
public class Node<T> {
    private List<Node<T>> children = new ArrayList<Node<T>>();
    //private Node<T> parent = null;
    private List<Node<T>> parents = new ArrayList<Node<T>>();
    private T data = null;
    private boolean isRecursive;
    private boolean isComplete;
    private List<Node<T>> ordNumOfRecursiveChildren = new ArrayList<Node<T>>();

    public List<Node<T>> getOrdNumOfRecursiveChildren() {
        return ordNumOfRecursiveChildren;
    }

    public void setOrdNumOfRecursiveChildren(List<Node<T>> ordNumOfRecursiveChildren) {
        this.ordNumOfRecursiveChildren = ordNumOfRecursiveChildren;
    }

    public void addOrdNumOfRecursiveChildren(Node<T> recChild) {
        ordNumOfRecursiveChildren.add(recChild);
    }

    public Node(T data) {
        this.data = data;
    }

    public Node(T data, Node<T> parent) {
        this.data = data;
        this.parents.add(parent);
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
        if (this.children.size() == 0)
            return true;
        else
            return false;
    }

    public void removeParent(int num) {
        this.parents.remove(num);
    }

    public String print() {
        String result = "";
        for (int i = 0; i < children.size(); i++)
            if (children.get(i) != null)
                result += children.get(i).print() + "\n";
        return result;
    }  // method toString

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}