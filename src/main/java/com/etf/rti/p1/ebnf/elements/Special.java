package com.etf.rti.p1.ebnf.elements;

/**
 * Created by sule on 12/12/15.
 */
public class Special extends Nonterminal {
    private String hiral;
    public Special(String value) {
        super(value);
        if ("[".equals(value)) {
            hiral = "]";
        } else if ("]".equals(value)) {
            hiral = "[";
        } else if ("{".equals(value)) {
            hiral = "}";
        } else if ("}".equals(value)) {
            hiral = "{";
        } else if ("(".equals(value)) {
            hiral = ")";
        } else if (")".equals(value)) {
            hiral = "(";
        } else {
            hiral = "";
        }
    }

    public boolean isSpecial() {
        return true;
    }

    public boolean hiralMatch(IElement e) {
        return e instanceof Special && hiral.equals(((Special) e).getValue());
    }

    public enum Direction {LEFT, RIGHT, NONE;

        public Direction oposite() {
            if (this == NONE)
                return NONE;

            if (this == LEFT)
                return RIGHT;
            return LEFT;
        }
    }

    public static Direction getDirection(IElement e) {
        if (!(e instanceof Special))
            return Direction.NONE;

        Special elem = (Special) e;

        if ("|".contains(elem.getValue()))
            return Direction.NONE;

        if ("[({".contains(elem.getValue()))
            return Direction.LEFT;

        return Direction.RIGHT;
    }
}
