package com.etf.rti.p1.exceptions;

/**
 * Created by zika on 09.12.2015..
 */
public class Exception extends java.lang.Exception {
    public Exception(String msg) {
        super(msg);
    }

    public Exception(String msg, java.lang.Exception e) {
        super(msg, e);
    }
}
