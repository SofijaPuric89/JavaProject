package com.etf.rti.p1.translator.ebnf.transformations;

import com.etf.rti.p1.translator.ebnf.rules.IRule;

/**
 * Created by sule on 12/12/15.
 */
public interface ITransform {
    IRule transform(IRule t);
}
