package com.etf.rti.p1.exceptions;

/**
 * Created by zika on 09.12.2015..
 */
public class ERegexFail extends Exception {
    public ERegexFail(String msg) {
        super(msg);
    }

    public ERegexFail(String msg, java.lang.Exception e) {
        super(msg, e);
    }
}
