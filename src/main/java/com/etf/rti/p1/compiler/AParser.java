package com.etf.rti.p1.compiler;

import com.etf.rti.p1.transformer.rules.IRule;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.TokenStream;

import java.util.LinkedList;

/**
 * Created by zika on 10.12.2015..
 */
public abstract class AParser extends Parser {

    public AParser(TokenStream input) {
        super(input);
    }

    public abstract ParserRuleContext init() throws RecognitionException;

    public abstract LinkedList<IRule> getRules();


}
