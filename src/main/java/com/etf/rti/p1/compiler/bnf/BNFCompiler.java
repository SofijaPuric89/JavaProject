package com.etf.rti.p1.compiler.bnf;

import com.etf.rti.p1.compiler.RuntimeCompiler;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.OutputStream;

/**
 * Compiles input grammar in BNF format.
 */
public class BNFCompiler extends RuntimeCompiler {
    private OutputStream output;

    public BNFCompiler(String name, String packageName, OutputStream out) {
        super(name, packageName);
        output = out;
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
