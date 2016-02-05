package com.etf.rti.p1.exceptions;

/**
 * Created by zika on 05.02.2016..
 */
public class EParseError extends Exception{

    public EParseError(String msg) {
        super(msg);
    }

    public EParseError(String msg, java.lang.Exception e) {
        super(msg, e);
    }
}
