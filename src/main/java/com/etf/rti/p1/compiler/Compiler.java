package com.etf.rti.p1.compiler;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zika on 09.12.2015..
 */
public interface Compiler {
    void setInput(InputStream in) throws IOException;

    Lexer getLexer() throws Exception;

    AParser getParser() throws Exception;

    CommonTokenStream getTokenStream() throws Exception;

    String getGrammarName();

    void reset();
}
