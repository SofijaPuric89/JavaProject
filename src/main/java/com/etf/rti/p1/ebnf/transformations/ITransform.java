package com.etf.rti.p1.ebnf.transformations;

import com.etf.rti.p1.ebnf.rules.IRule;

/**
 * Created by sule on 12/12/15.
 */
public interface ITransform {
    IRule transform(IRule t);
}
