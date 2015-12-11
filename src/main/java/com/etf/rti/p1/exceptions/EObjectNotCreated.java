package com.etf.rti.p1.exceptions;

/**
 * Created by zika on 09.12.2015..
 */
public class EObjectNotCreated extends Exception {

    public EObjectNotCreated(String msg, java.lang.Exception e) {
        super(msg, e);
    }

    public EObjectNotCreated(String msg) {
        super(msg);
    }
}
