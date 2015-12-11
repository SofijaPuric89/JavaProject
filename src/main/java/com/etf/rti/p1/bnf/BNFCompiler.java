package com.etf.rti.p1.bnf;

import com.etf.rti.p1.generator.RuntimeCompiler;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.OutputStream;

/**
 * Created by zika on 10.12.2015..
 */
public class BNFCompiler extends RuntimeCompiler {
    private OutputStream output;

    public BNFCompiler(String name, String packageName, OutputStream out) {
        super(name, packageName);
        output = out;
    }

    protected <T> T getObject(String name, Class<?>[] argsType, Object[] args) throws java.lang.Exception {
        Object object;
        if ("Lexer".equals(name)) {
            object = new bnfLexer((ANTLRInputStream) args[0]);
        } else {
            object = new bnfParser((CommonTokenStream) args[0], output, getName());
        }
        return (T) object;
    }
}
