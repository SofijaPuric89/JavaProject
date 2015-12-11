// Generated from C:/Users/zika/IdeaProjects/JavaProject/src/main/resources\bnf.g4 by ANTLR 4.5.1
package com.etf.rti.p1.bnf;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class bnfLexer extends Lexer {
    public static final int
            ASSIGN = 1, LPAREN = 2, RPAREN = 3, LBRACE = 4, RBRACE = 5, LEND = 6, REND = 7, BAR = 8,
            GT = 9, LT = 10, TERMINAL = 11, ID = 12, LETTER = 13, WS = 14;
    public static final String[] ruleNames = {
            "ASSIGN", "LPAREN", "RPAREN", "LBRACE", "RBRACE", "LEND", "REND", "BAR",
            "GT", "LT", "TERMINAL", "ID", "LETTER", "WS"
    };
    /**
     * @deprecated Use {@link #VOCABULARY} instead.
     */
    @Deprecated
    public static final String[] tokenNames;
    public static final String _serializedATN =
            "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\20B\b\1\4\2\t\2\4" +
                    "\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t" +
                    "\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\3\2\3\2\3\2\3\2\3\3\3\3\3\4\3" +
                    "\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3" +
                    "\r\3\r\3\16\3\16\3\17\6\17=\n\17\r\17\16\17>\3\17\3\17\2\2\20\3\3\5\4" +
                    "\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\3\2\6\t" +
                    "\2#$))..\60\60<<AA^^\4\2\62;aa\4\2C\\c|\5\2\13\f\17\17\"\"B\2\3\3\2\2" +
                    "\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3" +
                    "\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2" +
                    "\2\2\33\3\2\2\2\2\35\3\2\2\2\3\37\3\2\2\2\5#\3\2\2\2\7%\3\2\2\2\t\'\3" +
                    "\2\2\2\13)\3\2\2\2\r+\3\2\2\2\17-\3\2\2\2\21/\3\2\2\2\23\61\3\2\2\2\25" +
                    "\63\3\2\2\2\27\65\3\2\2\2\31\67\3\2\2\2\339\3\2\2\2\35<\3\2\2\2\37 \7" +
                    "<\2\2 !\7<\2\2!\"\7?\2\2\"\4\3\2\2\2#$\7+\2\2$\6\3\2\2\2%&\7*\2\2&\b\3" +
                    "\2\2\2\'(\7\177\2\2(\n\3\2\2\2)*\7}\2\2*\f\3\2\2\2+,\7_\2\2,\16\3\2\2" +
                    "\2-.\7]\2\2.\20\3\2\2\2/\60\7~\2\2\60\22\3\2\2\2\61\62\7@\2\2\62\24\3" +
                    "\2\2\2\63\64\7>\2\2\64\26\3\2\2\2\65\66\t\2\2\2\66\30\3\2\2\2\678\t\3" +
                    "\2\28\32\3\2\2\29:\t\4\2\2:\34\3\2\2\2;=\t\5\2\2<;\3\2\2\2=>\3\2\2\2>" +
                    "<\3\2\2\2>?\3\2\2\2?@\3\2\2\2@A\b\17\2\2A\36\3\2\2\2\4\2>\3\b\2\2";
    public static final ATN _ATN =
            new ATNDeserializer().deserialize(_serializedATN.toCharArray());
    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache =
            new PredictionContextCache();
    private static final String[] _LITERAL_NAMES = {
            null, "'::='", "')'", "'('", "'}'", "'{'", "']'", "'['", "'|'", "'>'",
            "'<'"
    };
    private static final String[] _SYMBOLIC_NAMES = {
            null, "ASSIGN", "LPAREN", "RPAREN", "LBRACE", "RBRACE", "LEND", "REND",
            "BAR", "GT", "LT", "TERMINAL", "ID", "LETTER", "WS"
    };
    public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);
    public static String[] modeNames = {
            "DEFAULT_MODE"
    };

    static {
        RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION);
    }

    static {
        tokenNames = new String[_SYMBOLIC_NAMES.length];
        for (int i = 0; i < tokenNames.length; i++) {
            tokenNames[i] = VOCABULARY.getLiteralName(i);
            if (tokenNames[i] == null) {
                tokenNames[i] = VOCABULARY.getSymbolicName(i);
            }

            if (tokenNames[i] == null) {
                tokenNames[i] = "<INVALID>";
            }
        }
    }

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }

    public bnfLexer(CharStream input) {
        super(input);
        _interp = new LexerATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    @Override
    @Deprecated
    public String[] getTokenNames() {
        return tokenNames;
    }

    @Override

    public Vocabulary getVocabulary() {
        return VOCABULARY;
    }

    @Override
    public String getGrammarFileName() {
        return "bnf.g4";
    }

    @Override
    public String[] getRuleNames() {
        return ruleNames;
    }

    @Override
    public String getSerializedATN() {
        return _serializedATN;
    }

    @Override
    public String[] getModeNames() {
        return modeNames;
    }

    @Override
    public ATN getATN() {
        return _ATN;
    }
}