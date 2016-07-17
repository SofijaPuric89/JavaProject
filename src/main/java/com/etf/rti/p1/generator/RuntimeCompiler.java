package com.etf.rti.p1.generator;

import com.etf.rti.p1.exceptions.EMethodCallOrder;
import com.etf.rti.p1.exceptions.EObjectNotCreated;
import com.etf.rti.p1.exceptions.Exception;
import org.antlr.v4.runtime.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;

/**
 * Created by zika on 09.12.2015..
 */
public class RuntimeCompiler implements Compiler {

    private String grammarName;
    private String packageName;

    private ANTLRInputStream input = null;
    private Lexer lexer = null;
    private CommonTokenStream tokenStream = null;
    private AParser parser = null;

    public RuntimeCompiler(String grammarName, String packageName) {
        this.grammarName = grammarName;
        this.packageName = packageName;
    }

    public void reset() {
        lexer = null;
        tokenStream = null;
        parser = null;
    }

    public void setInput(InputStream in) throws IOException {
        input = new ANTLRInputStream(in);
    }

    public Lexer getLexer() throws Exception {
        if (input == null) {
            throw new EMethodCallOrder("The method getLexer() called before set input");
        }
        if (lexer == null) {
            try {
                lexer = getObject("Lexer", new Class<?>[]{CharStream.class}, new Object[]{input});
            } catch (java.lang.Exception e) {
                throw new EObjectNotCreated("Lexer object cannot be created", e);
            }
        }
        return lexer;
    }

    public AParser getParser() throws Exception {
        if (tokenStream == null) {
            tokenStream = getTokenStream();
        }
        if (parser == null) {
            try {
                parser = getObject("Parser", new Class<?>[]{TokenStream.class}, new Object[]{tokenStream});
            } catch (java.lang.Exception e) {
                throw new EObjectNotCreated("Parser object cannot be created", e);
            }
        }
        return parser;
    }

    public CommonTokenStream getTokenStream() throws Exception {
        if (lexer == null) {
            lexer = getLexer();
        }
        return new CommonTokenStream(lexer);
    }

    public String getGrammarName() {
        return grammarName;
    }


    protected <T> T getObject(String name, Class<?>[] argsType, Object[] args) throws java.lang.Exception {
        assert (argsType.length == args.length);
        Class<?> c = Class.forName(packageName + "." + this.grammarName + name);
        Constructor<?> ctor = c.getConstructor(argsType);
        return (T) ctor.newInstance(args);
    }
}
