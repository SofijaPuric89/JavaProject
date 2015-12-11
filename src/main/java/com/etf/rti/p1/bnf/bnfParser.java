// Generated from C:/Users/zika/IdeaProjects/JavaProject/src/main/resources\bnf.g4 by ANTLR 4.5.1
package com.etf.rti.p1.bnf;

import com.etf.rti.p1.generator.AParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class bnfParser extends AParser {
    public static final int
            ASSIGN = 1, LPAREN = 2, RPAREN = 3, LBRACE = 4, RBRACE = 5, LEND = 6, REND = 7, BAR = 8,
            GT = 9, LT = 10, TERMINAL = 11, ID = 12, LETTER = 13, WS = 14;
    public static final int
            RULE_init = 0, RULE_rulelist = 1, RULE_rule_ = 2, RULE_lhs = 3, RULE_rhs = 4,
            RULE_alternatives = 5, RULE_alternative = 6, RULE_element = 7, RULE_optional = 8,
            RULE_zeroormore = 9, RULE_oneormore = 10, RULE_text = 11, RULE_id = 12,
            RULE_ruleid = 13;
    public static final String[] ruleNames = {
            "init", "rulelist", "rule_", "lhs", "rhs", "alternatives", "alternative",
            "element", "optional", "zeroormore", "oneormore", "text", "id", "ruleid"
    };
    /**
     * @deprecated Use {@link #VOCABULARY} instead.
     */
    @Deprecated
    public static final String[] tokenNames;
    public static final String _serializedATN =
            "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\20y\4\2\t\2\4\3\t" +
                    "\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4" +
                    "\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\3\2\3\2\3\2\3\3\7\3#\n\3\f\3\16\3&" +
                    "\13\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\6\3\6\3\6\3\7\3\7\3\7" +
                    "\3\7\3\7\3\7\7\7;\n\7\f\7\16\7>\13\7\3\b\3\b\3\b\7\bC\n\b\f\b\16\bF\13" +
                    "\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\5\tW\n" +
                    "\t\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3" +
                    "\r\6\ri\n\r\r\r\16\rj\3\16\3\16\3\16\3\16\3\16\3\17\3\17\7\17t\n\17\f" +
                    "\17\16\17w\13\17\3\17\2\2\20\2\4\6\b\n\f\16\20\22\24\26\30\32\34\2\4\3" +
                    "\2\r\17\3\2\16\17s\2\36\3\2\2\2\4$\3\2\2\2\6)\3\2\2\2\b.\3\2\2\2\n\61" +
                    "\3\2\2\2\f\64\3\2\2\2\16D\3\2\2\2\20V\3\2\2\2\22X\3\2\2\2\24]\3\2\2\2" +
                    "\26b\3\2\2\2\30h\3\2\2\2\32l\3\2\2\2\34q\3\2\2\2\36\37\5\4\3\2\37 \b\2" +
                    "\1\2 \3\3\2\2\2!#\5\6\4\2\"!\3\2\2\2#&\3\2\2\2$\"\3\2\2\2$%\3\2\2\2%\'" +
                    "\3\2\2\2&$\3\2\2\2\'(\7\2\2\3(\5\3\2\2\2)*\5\b\5\2*+\7\3\2\2+,\5\n\6\2" +
                    ",-\b\4\1\2-\7\3\2\2\2./\5\32\16\2/\60\b\5\1\2\60\t\3\2\2\2\61\62\5\f\7" +
                    "\2\62\63\b\6\1\2\63\13\3\2\2\2\64\65\5\16\b\2\65<\b\7\1\2\66\67\7\n\2" +
                    "\2\678\5\16\b\289\b\7\1\29;\3\2\2\2:\66\3\2\2\2;>\3\2\2\2<:\3\2\2\2<=" +
                    "\3\2\2\2=\r\3\2\2\2><\3\2\2\2?@\5\20\t\2@A\b\b\1\2AC\3\2\2\2B?\3\2\2\2" +
                    "CF\3\2\2\2DB\3\2\2\2DE\3\2\2\2E\17\3\2\2\2FD\3\2\2\2GH\5\22\n\2HI\b\t" +
                    "\1\2IW\3\2\2\2JK\5\24\13\2KL\b\t\1\2LW\3\2\2\2MN\5\26\f\2NO\b\t\1\2OW" +
                    "\3\2\2\2PQ\5\30\r\2QR\b\t\1\2RW\3\2\2\2ST\5\32\16\2TU\b\t\1\2UW\3\2\2" +
                    "\2VG\3\2\2\2VJ\3\2\2\2VM\3\2\2\2VP\3\2\2\2VS\3\2\2\2W\21\3\2\2\2XY\7\t" +
                    "\2\2YZ\5\f\7\2Z[\b\n\1\2[\\\7\b\2\2\\\23\3\2\2\2]^\7\7\2\2^_\5\f\7\2_" +
                    "`\b\13\1\2`a\7\6\2\2a\25\3\2\2\2bc\7\5\2\2cd\5\f\7\2de\b\f\1\2ef\7\4\2" +
                    "\2f\27\3\2\2\2gi\t\2\2\2hg\3\2\2\2ij\3\2\2\2jh\3\2\2\2jk\3\2\2\2k\31\3" +
                    "\2\2\2lm\7\f\2\2mn\5\34\17\2no\7\13\2\2op\b\16\1\2p\33\3\2\2\2qu\7\17" +
                    "\2\2rt\t\3\2\2sr\3\2\2\2tw\3\2\2\2us\3\2\2\2uv\3\2\2\2v\35\3\2\2\2wu\3" +
                    "\2\2\2\b$<DVju";
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

    private PrintStream output = null;
    private boolean first = true;
    private String name;


    public bnfParser(TokenStream input, OutputStream out, String name) {
        this(input);
        this.output = new PrintStream(out);
        this.name = name;
    }

    public bnfParser(TokenStream input) {
        super(input);
        _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
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
    public ATN getATN() {
        return _ATN;
    }

    private void print(String s) {
        if (output != null)
            output.print(s);
    }

    private void println(String s) {
        print(s);
        print("\n");
    }

    private void closeOutput() {
        if (output != null)
            output.close();
    }

    private void printFirst(String s) {
        if (first) {
            println(String.format("grammar %s;", name));
            println("options {\n" +
                    "superClass=AParser;\n" +
                    "}\n" +
                    "@header {\n" +
                    "import com.etf.rti.p1.generator.AParser;\n" +
                    "}\n");
            println(String.format("init : %s ;", s));
        }
        first = false;
    }

    public final InitContext init() throws RecognitionException {
        InitContext _localctx = new InitContext(_ctx, getState());
        enterRule(_localctx, 0, RULE_init);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(28);
                rulelist();
                closeOutput();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final RulelistContext rulelist() throws RecognitionException {
        RulelistContext _localctx = new RulelistContext(_ctx, getState());
        enterRule(_localctx, 2, RULE_rulelist);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(34);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == LT) {
                    {
                        {
                            setState(31);
                            rule_();
                        }
                    }
                    setState(36);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(37);
                match(EOF);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final Rule_Context rule_() throws RecognitionException {
        Rule_Context _localctx = new Rule_Context(_ctx, getState());
        enterRule(_localctx, 4, RULE_rule_);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(39);
                ((Rule_Context) _localctx).lhs = lhs();
                setState(40);
                match(ASSIGN);
                setState(41);
                ((Rule_Context) _localctx).rhs = rhs();
                printFirst(((Rule_Context) _localctx).lhs.v);
                println(((Rule_Context) _localctx).lhs.v);
                println(((Rule_Context) _localctx).rhs.v);
                println(";");

            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final LhsContext lhs() throws RecognitionException {
        LhsContext _localctx = new LhsContext(_ctx, getState());
        enterRule(_localctx, 6, RULE_lhs);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(44);
                ((LhsContext) _localctx).id = id();
                ((LhsContext) _localctx).v = ((LhsContext) _localctx).id.v;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final RhsContext rhs() throws RecognitionException {
        RhsContext _localctx = new RhsContext(_ctx, getState());
        enterRule(_localctx, 8, RULE_rhs);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(47);
                ((RhsContext) _localctx).alternatives = alternatives("\n\t|");
                ((RhsContext) _localctx).v = "\t: " + ((RhsContext) _localctx).alternatives.v;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final AlternativesContext alternatives(String conector) throws RecognitionException {
        AlternativesContext _localctx = new AlternativesContext(_ctx, getState(), conector);
        enterRule(_localctx, 10, RULE_alternatives);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(50);
                ((AlternativesContext) _localctx).a = alternative();
                ((AlternativesContext) _localctx).v = ((AlternativesContext) _localctx).a.v;
                setState(58);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == BAR) {
                    {
                        {
                            setState(52);
                            match(BAR);
                            setState(53);
                            ((AlternativesContext) _localctx).b = alternative();
                            _localctx.v += conector + ((AlternativesContext) _localctx).b.v;
                        }
                    }
                    setState(60);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final AlternativeContext alternative() throws RecognitionException {
        AlternativeContext _localctx = new AlternativeContext(_ctx, getState());
        enterRule(_localctx, 12, RULE_alternative);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(66);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 2, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(61);
                                ((AlternativeContext) _localctx).element = element();
                                if (_localctx.v == null)
                                    ((AlternativeContext) _localctx).v = ((AlternativeContext) _localctx).element.v;
                                else _localctx.v += " " + ((AlternativeContext) _localctx).element.v;
                            }
                        }
                    }
                    setState(68);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 2, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final ElementContext element() throws RecognitionException {
        ElementContext _localctx = new ElementContext(_ctx, getState());
        enterRule(_localctx, 14, RULE_element);
        try {
            setState(84);
            switch (_input.LA(1)) {
                case REND:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(69);
                    ((ElementContext) _localctx).optional = optional();
                    ((ElementContext) _localctx).v = ((ElementContext) _localctx).optional.v;
                }
                break;
                case RBRACE:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(72);
                    ((ElementContext) _localctx).zeroormore = zeroormore();
                    ((ElementContext) _localctx).v = ((ElementContext) _localctx).zeroormore.v;
                }
                break;
                case RPAREN:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(75);
                    ((ElementContext) _localctx).oneormore = oneormore();
                    ((ElementContext) _localctx).v = ((ElementContext) _localctx).oneormore.v;
                }
                break;
                case TERMINAL:
                case ID:
                case LETTER:
                    enterOuterAlt(_localctx, 4);
                {
                    setState(78);
                    ((ElementContext) _localctx).t = text();
                    ((ElementContext) _localctx).v = "\'" + StringEscapeUtils.escapeJava((((ElementContext) _localctx).t != null ? _input.getText(((ElementContext) _localctx).t.start, ((ElementContext) _localctx).t.stop) : null)) + "\'";
                }
                break;
                case LT:
                    enterOuterAlt(_localctx, 5);
                {
                    setState(81);
                    ((ElementContext) _localctx).id = id();
                    ((ElementContext) _localctx).v = ((ElementContext) _localctx).id.v;
                }
                break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final OptionalContext optional() throws RecognitionException {
        OptionalContext _localctx = new OptionalContext(_ctx, getState());
        enterRule(_localctx, 16, RULE_optional);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(86);
                match(REND);
                setState(87);
                ((OptionalContext) _localctx).alternatives = alternatives("\n\t|");
                ((OptionalContext) _localctx).v = ((OptionalContext) _localctx).alternatives.v + "?";
                setState(89);
                match(LEND);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final ZeroormoreContext zeroormore() throws RecognitionException {
        ZeroormoreContext _localctx = new ZeroormoreContext(_ctx, getState());
        enterRule(_localctx, 18, RULE_zeroormore);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(91);
                match(RBRACE);
                setState(92);
                ((ZeroormoreContext) _localctx).alternatives = alternatives("\n\t|");
                ((ZeroormoreContext) _localctx).v = ((ZeroormoreContext) _localctx).alternatives.v + "*";
                setState(94);
                match(LBRACE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final OneormoreContext oneormore() throws RecognitionException {
        OneormoreContext _localctx = new OneormoreContext(_ctx, getState());
        enterRule(_localctx, 20, RULE_oneormore);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(96);
                match(RPAREN);
                setState(97);
                ((OneormoreContext) _localctx).alternatives = alternatives("\n\t|");
                ((OneormoreContext) _localctx).v = ((OneormoreContext) _localctx).alternatives.v + "+";
                setState(99);
                match(LPAREN);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final TextContext text() throws RecognitionException {
        TextContext _localctx = new TextContext(_ctx, getState());
        enterRule(_localctx, 22, RULE_text);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(102);
                _errHandler.sync(this);
                _alt = 1;
                do {
                    switch (_alt) {
                        case 1: {
                            {
                                setState(101);
                                _la = _input.LA(1);
                                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TERMINAL) | (1L << ID) | (1L << LETTER))) != 0))) {
                                    _errHandler.recoverInline(this);
                                } else {
                                    consume();
                                }
                            }
                        }
                        break;
                        default:
                            throw new NoViableAltException(this);
                    }
                    setState(104);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 4, _ctx);
                } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final IdContext id() throws RecognitionException {
        IdContext _localctx = new IdContext(_ctx, getState());
        enterRule(_localctx, 24, RULE_id);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(106);
                match(LT);
                setState(107);
                ((IdContext) _localctx).ruleid = ruleid();
                setState(108);
                match(GT);
                ((IdContext) _localctx).v = (((IdContext) _localctx).ruleid != null ? _input.getText(((IdContext) _localctx).ruleid.start, ((IdContext) _localctx).ruleid.stop) : null);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final RuleidContext ruleid() throws RecognitionException {
        RuleidContext _localctx = new RuleidContext(_ctx, getState());
        enterRule(_localctx, 26, RULE_ruleid);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(111);
                match(LETTER);
                setState(115);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == ID || _la == LETTER) {
                    {
                        {
                            setState(112);
                            _la = _input.LA(1);
                            if (!(_la == ID || _la == LETTER)) {
                                _errHandler.recoverInline(this);
                            } else {
                                consume();
                            }
                        }
                    }
                    setState(117);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class InitContext extends ParserRuleContext {
        public InitContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public RulelistContext rulelist() {
            return getRuleContext(RulelistContext.class, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_init;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof bnfListener) ((bnfListener) listener).enterInit(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof bnfListener) ((bnfListener) listener).exitInit(this);
        }
    }

    public static class RulelistContext extends ParserRuleContext {
        public RulelistContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode EOF() {
            return getToken(bnfParser.EOF, 0);
        }

        public List<Rule_Context> rule_() {
            return getRuleContexts(Rule_Context.class);
        }

        public Rule_Context rule_(int i) {
            return getRuleContext(Rule_Context.class, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_rulelist;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof bnfListener) ((bnfListener) listener).enterRulelist(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof bnfListener) ((bnfListener) listener).exitRulelist(this);
        }
    }

    public static class Rule_Context extends ParserRuleContext {
        public LhsContext lhs;
        public RhsContext rhs;

        public Rule_Context(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public LhsContext lhs() {
            return getRuleContext(LhsContext.class, 0);
        }

        public TerminalNode ASSIGN() {
            return getToken(bnfParser.ASSIGN, 0);
        }

        public RhsContext rhs() {
            return getRuleContext(RhsContext.class, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_rule_;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof bnfListener) ((bnfListener) listener).enterRule_(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof bnfListener) ((bnfListener) listener).exitRule_(this);
        }
    }

    public static class LhsContext extends ParserRuleContext {
        public String v;
        public IdContext id;

        public LhsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public IdContext id() {
            return getRuleContext(IdContext.class, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_lhs;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof bnfListener) ((bnfListener) listener).enterLhs(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof bnfListener) ((bnfListener) listener).exitLhs(this);
        }
    }

    public static class RhsContext extends ParserRuleContext {
        public String v;
        public AlternativesContext alternatives;

        public RhsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public AlternativesContext alternatives() {
            return getRuleContext(AlternativesContext.class, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_rhs;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof bnfListener) ((bnfListener) listener).enterRhs(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof bnfListener) ((bnfListener) listener).exitRhs(this);
        }
    }

    public static class AlternativesContext extends ParserRuleContext {
        public String conector;
        public String v;
        public AlternativeContext a;
        public AlternativeContext b;

        public AlternativesContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public AlternativesContext(ParserRuleContext parent, int invokingState, String conector) {
            super(parent, invokingState);
            this.conector = conector;
        }

        public List<AlternativeContext> alternative() {
            return getRuleContexts(AlternativeContext.class);
        }

        public AlternativeContext alternative(int i) {
            return getRuleContext(AlternativeContext.class, i);
        }

        public List<TerminalNode> BAR() {
            return getTokens(bnfParser.BAR);
        }

        public TerminalNode BAR(int i) {
            return getToken(bnfParser.BAR, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_alternatives;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof bnfListener) ((bnfListener) listener).enterAlternatives(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof bnfListener) ((bnfListener) listener).exitAlternatives(this);
        }
    }

    public static class AlternativeContext extends ParserRuleContext {
        public String v;
        public ElementContext element;

        public AlternativeContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public List<ElementContext> element() {
            return getRuleContexts(ElementContext.class);
        }

        public ElementContext element(int i) {
            return getRuleContext(ElementContext.class, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_alternative;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof bnfListener) ((bnfListener) listener).enterAlternative(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof bnfListener) ((bnfListener) listener).exitAlternative(this);
        }
    }

    public static class ElementContext extends ParserRuleContext {
        public String v;
        public OptionalContext optional;
        public ZeroormoreContext zeroormore;
        public OneormoreContext oneormore;
        public TextContext t;
        public IdContext id;

        public ElementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public OptionalContext optional() {
            return getRuleContext(OptionalContext.class, 0);
        }

        public ZeroormoreContext zeroormore() {
            return getRuleContext(ZeroormoreContext.class, 0);
        }

        public OneormoreContext oneormore() {
            return getRuleContext(OneormoreContext.class, 0);
        }

        public TextContext text() {
            return getRuleContext(TextContext.class, 0);
        }

        public IdContext id() {
            return getRuleContext(IdContext.class, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_element;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof bnfListener) ((bnfListener) listener).enterElement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof bnfListener) ((bnfListener) listener).exitElement(this);
        }
    }

    public static class OptionalContext extends ParserRuleContext {
        public String v;
        public AlternativesContext alternatives;

        public OptionalContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode REND() {
            return getToken(bnfParser.REND, 0);
        }

        public AlternativesContext alternatives() {
            return getRuleContext(AlternativesContext.class, 0);
        }

        public TerminalNode LEND() {
            return getToken(bnfParser.LEND, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_optional;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof bnfListener) ((bnfListener) listener).enterOptional(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof bnfListener) ((bnfListener) listener).exitOptional(this);
        }
    }

    public static class ZeroormoreContext extends ParserRuleContext {
        public String v;
        public AlternativesContext alternatives;

        public ZeroormoreContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode RBRACE() {
            return getToken(bnfParser.RBRACE, 0);
        }

        public AlternativesContext alternatives() {
            return getRuleContext(AlternativesContext.class, 0);
        }

        public TerminalNode LBRACE() {
            return getToken(bnfParser.LBRACE, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_zeroormore;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof bnfListener) ((bnfListener) listener).enterZeroormore(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof bnfListener) ((bnfListener) listener).exitZeroormore(this);
        }
    }

    public static class OneormoreContext extends ParserRuleContext {
        public String v;
        public AlternativesContext alternatives;

        public OneormoreContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode RPAREN() {
            return getToken(bnfParser.RPAREN, 0);
        }

        public AlternativesContext alternatives() {
            return getRuleContext(AlternativesContext.class, 0);
        }

        public TerminalNode LPAREN() {
            return getToken(bnfParser.LPAREN, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_oneormore;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof bnfListener) ((bnfListener) listener).enterOneormore(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof bnfListener) ((bnfListener) listener).exitOneormore(this);
        }
    }

    public static class TextContext extends ParserRuleContext {
        public TextContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public List<TerminalNode> TERMINAL() {
            return getTokens(bnfParser.TERMINAL);
        }

        public TerminalNode TERMINAL(int i) {
            return getToken(bnfParser.TERMINAL, i);
        }

        public List<TerminalNode> LETTER() {
            return getTokens(bnfParser.LETTER);
        }

        public TerminalNode LETTER(int i) {
            return getToken(bnfParser.LETTER, i);
        }

        public List<TerminalNode> ID() {
            return getTokens(bnfParser.ID);
        }

        public TerminalNode ID(int i) {
            return getToken(bnfParser.ID, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_text;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof bnfListener) ((bnfListener) listener).enterText(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof bnfListener) ((bnfListener) listener).exitText(this);
        }
    }

    public static class IdContext extends ParserRuleContext {
        public String v;
        public RuleidContext ruleid;

        public IdContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode LT() {
            return getToken(bnfParser.LT, 0);
        }

        public RuleidContext ruleid() {
            return getRuleContext(RuleidContext.class, 0);
        }

        public TerminalNode GT() {
            return getToken(bnfParser.GT, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_id;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof bnfListener) ((bnfListener) listener).enterId(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof bnfListener) ((bnfListener) listener).exitId(this);
        }
    }

    public static class RuleidContext extends ParserRuleContext {
        public RuleidContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public List<TerminalNode> LETTER() {
            return getTokens(bnfParser.LETTER);
        }

        public TerminalNode LETTER(int i) {
            return getToken(bnfParser.LETTER, i);
        }

        public List<TerminalNode> ID() {
            return getTokens(bnfParser.ID);
        }

        public TerminalNode ID(int i) {
            return getToken(bnfParser.ID, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ruleid;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof bnfListener) ((bnfListener) listener).enterRuleid(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof bnfListener) ((bnfListener) listener).exitRuleid(this);
        }
    }
}