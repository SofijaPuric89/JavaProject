package com.etf.rti.p1.ui.highlight;

import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMap;

import javax.swing.text.Segment;
import java.util.Arrays;

public class BNFTokenMaker extends AbstractGrammarNotationTokenMaker {

    private static final String[] RESERVED_WORDS = new String[]{
            "::=", ")", "(", "}", "{", "[", "]", "|"
    };

    @Override
    public TokenMap getWordsToHighlight() {
        TokenMap tokenMap = new TokenMap();

        for (String reservedWord : RESERVED_WORDS) {
            tokenMap.put(reservedWord, Token.RESERVED_WORD);
        }

        return tokenMap;
    }

    @Override
    void handleCurrentToken(Segment text, int newStartOffset, int i, char c) {
        switch (currentTokenType) {
            case Token.NULL:
                // Starting a new token
                currentTokenStart = i;
                switch (c) {
                    case ' ':
                    case '\t':
                    case '\r':
                    case '\n':
                        currentTokenType = Token.WHITESPACE;
                        break;

                    case '<':
                        // terminal symbol
                        currentTokenType = Token.VARIABLE;
                        break;

                    default:
                        if (isSingleCharReservedWord(c)) {
                            addToken(text, i, i, Token.IDENTIFIER, newStartOffset + i);
                            currentTokenStart = i + 1;
                            currentTokenType = Token.NULL;
                        } else {
                            currentTokenType = Token.IDENTIFIER;
                        }
                }
                break;

            case Token.WHITESPACE:
                switch (c) {
                    case ' ':
                    case '\t':
                    case '\r':
                    case '\n':
                        break;

                    case '<':
                        // nonterminal symbol
                        addToken(text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart);
                        currentTokenStart = i;
                        currentTokenType = Token.VARIABLE;
                        break;

                    default:
                        addToken(text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart);
                        if (isSingleCharReservedWord(c)) {
                            addToken(text, i, i, Token.IDENTIFIER, newStartOffset + i);
                            currentTokenStart = i + 1;
                            currentTokenType = Token.NULL;
                        } else {
                            currentTokenStart = i;
                            currentTokenType = Token.IDENTIFIER;
                        }
                }
                break;

            case Token.VARIABLE:
                // nonterminal
                switch (c) {
                    case '>':
                        addToken(text, currentTokenStart, i, Token.VARIABLE, newStartOffset + currentTokenStart);
                        currentTokenStart = i + 1;
                        currentTokenType = Token.NULL;
                        break;
                }
                break;

            case Token.IDENTIFIER:
                switch (c) {
                    case ' ':
                    case '\t':
                    case '\r':
                    case '\n':
                        addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart);
                        currentTokenStart = i;
                        currentTokenType = Token.WHITESPACE;
                        break;
                    case '<':
                        addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart);
                        currentTokenStart = i;
                        currentTokenType = Token.VARIABLE;
                        break;
                    default:
                        if (isSingleCharReservedWord(c)) {
                            addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart);
                            addToken(text, i, i, Token.IDENTIFIER, newStartOffset + i);
                            currentTokenStart = i + 1;
                            currentTokenType = Token.NULL;
                        }
                }
        }
    }

    private boolean isSingleCharReservedWord(char c) {
        return Arrays.stream(RESERVED_WORDS).anyMatch(r -> r.equals(String.valueOf(c)));
    }
}