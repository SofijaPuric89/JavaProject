package com.etf.rti.p1.ui.highlight;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMaker;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMap;
import org.fife.ui.rsyntaxtextarea.TokenTypes;

import javax.swing.text.Segment;

public class BNFTokenMaker extends AbstractTokenMaker {

    private int currentTokenStart;
    private int currentTokenType;

    @Override
    public TokenMap getWordsToHighlight() {
        TokenMap tokenMap = new TokenMap();

        tokenMap.put("::=", Token.RESERVED_WORD);
        tokenMap.put(")", Token.RESERVED_WORD);
        tokenMap.put("(", Token.RESERVED_WORD);
        tokenMap.put("}", Token.RESERVED_WORD);
        tokenMap.put("{", Token.RESERVED_WORD);
        tokenMap.put("[", Token.RESERVED_WORD);
        tokenMap.put("]", Token.RESERVED_WORD);
        tokenMap.put("|", Token.RESERVED_WORD);
        /*tokenMap.put("<", Token.RESERVED_WORD);
        tokenMap.put(">", Token.RESERVED_WORD);*/

        return tokenMap;
    }

    @Override
    public Token getTokenList(Segment text, int startTokenType, int startOffset) {
        resetTokenList();

        char[] array = text.array;
        int offset = text.offset;
        int count = text.count;
        int end = offset + count;

        int newStartOffset = startOffset - offset;

        currentTokenStart = offset;
        currentTokenType = startTokenType;

        for (int i = offset; i < end; i++) {
            char c = array[i];

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
                            currentTokenType = Token.IDENTIFIER;
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
                            // terminal symbol
                            addToken(text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.VARIABLE;
                            break;

                        default:
                            addToken(text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.IDENTIFIER;
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
                        case '|':
                            addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart);
                            addToken(text, i, i, Token.IDENTIFIER, newStartOffset + i);
                            currentTokenStart = i + 1;
                            currentTokenType = Token.NULL;
                            break;
                        case '<':
                            addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.VARIABLE;
                            break;
                    }
            }
        }

        switch (currentTokenType) {
            case Token.NULL:
                addNullToken();
                break;

            // No continuing on new line
            default:
                addToken(text, currentTokenStart, end - 1, currentTokenType, newStartOffset + currentTokenStart);
                addNullToken();

        }

        return firstToken;
    }

    @Override
    public void addToken(Segment segment, int start, int end, int tokenType, int startOffset) {
        if (tokenType == Token.IDENTIFIER) {
            int value = wordsToHighlight.get(segment, start, end);
            if (value != -1) {
                tokenType = value;
            }
        }
        super.addToken(segment, start, end, tokenType, startOffset);
    }
}