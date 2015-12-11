package com.etf.rti.p1.generator;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.TokenStream;

/**
 * Created by zika on 10.12.2015..
 */
public abstract class AParser extends Parser {

    public AParser(TokenStream input) {
        super(input);
    }

    public abstract ParserRuleContext init() throws RecognitionException;


}
