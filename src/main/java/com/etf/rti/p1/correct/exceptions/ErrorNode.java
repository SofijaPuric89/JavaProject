package com.etf.rti.p1.correct.exceptions;


/**
 * Created by Korisnik on 12.1.2016.
 */
public class ErrorNode extends Exception{
    public String toString() {
        return "The node doesn't exist in tree!";
    }
}
