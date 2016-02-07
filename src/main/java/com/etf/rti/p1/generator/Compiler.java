package com.etf.rti.p1.generator;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zika on 09.12.2015..
 */
public interface Compiler {
    void setInput(InputStream in) throws IOException;

    Lexer getLexer() throws com.etf.rti.p1.exceptions.Exception;

    AParser getParser() throws com.etf.rti.p1.exceptions.Exception;

    CommonTokenStream getTokenStream() throws com.etf.rti.p1.exceptions.Exception;

    String getName();

    void reset();
}
