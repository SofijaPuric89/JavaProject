package com.etf.rti.p1.compiler.bnf;

import com.etf.rti.p1.compiler.RuntimeCompiler;
import com.google.common.io.NullOutputStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * Compiles input grammar in BNF format.
 */
public class BNFCompiler extends RuntimeCompiler {
    private OutputStream output;

    public BNFCompiler(String name, OutputStream out) {
        super(name, "com.etf.rti.p1.compiler.bnf");
        output = out;
    }

    public BNFCompiler(OutputStream out) {
        this("bnf" + Long.toString(System.nanoTime()), out);
    }

    protected <T> T getObject(String name, Class<?>[] argsType, Object[] args) throws Exception {
        if ("Lexer".equals(name)) {
            return (T) new bnfLexer((ANTLRInputStream) args[0]);
        }
        if ("Parser".equals(name)) {
            return (T) new bnfParser((CommonTokenStream) args[0], output, getGrammarName());
        }
        throw new Exception("Unknown object with name '" + name + "' requested");
    }
}
