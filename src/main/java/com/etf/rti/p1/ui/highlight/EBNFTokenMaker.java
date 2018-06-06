package com.etf.rti.p1.ui.highlight;

import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMap;

import javax.swing.text.Segment;

public class EBNFTokenMaker extends AbstractGrammarNotationTokenMaker {

    @Override
    public TokenMap getWordsToHighlight() {
        TokenMap tokenMap = new TokenMap();

        tokenMap.put("=", Token.RESERVED_WORD);
        tokenMap.put(")", Token.RESERVED_WORD);
        tokenMap.put("(", Token.RESERVED_WORD);
        tokenMap.put("}", Token.RESERVED_WORD);
        tokenMap.put("{", Token.RESERVED_WORD);
        tokenMap.put("[", Token.RESERVED_WORD);
        tokenMap.put("]", Token.RESERVED_WORD);
        tokenMap.put("|", Token.RESERVED_WORD);
        tokenMap.put(".", Token.RESERVED_WORD);

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
                    case '=':
                    case '|':
                    case '{':
                    case '}':
                    case '(':
                    case ')':
                    case '[':
                    case ']':
                    case '.':
                        addToken(text, i, i, Token.IDENTIFIER, newStartOffset + i);
                        currentTokenStart = i + 1;
                        currentTokenType = Token.NULL;
                        break;
                    case '"':
                        currentTokenType = Token.IDENTIFIER;
                        currentTokenStart = i;
                        break;
                    default:
                        currentTokenType = Token.VARIABLE;
                }
                break;

            case Token.WHITESPACE:
                switch (c) {
                    case ' ':
                    case '\t':
                    case '\r':
                    case '\n':
                        break;

                    case '"':
                        // terminal symbol
                        addToken(text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart);
                        currentTokenStart = i;
                        currentTokenType = Token.IDENTIFIER;
                        break;

                    default:
                        addToken(text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart);
                        currentTokenStart = i;
                        currentTokenType = Token.VARIABLE;
                }
                break;

            case Token.VARIABLE:
                // nonterminal
                switch (c) {
                    case '=':
                    case '|':
                    case '{':
                    case '}':
                    case '(':
                    case ')':
                    case '[':
                    case ']':
                    case '.':
                        addToken(text, currentTokenStart, i - 1, Token.VARIABLE, newStartOffset + currentTokenStart);
                        addToken(text, i, i, Token.IDENTIFIER, newStartOffset + i);
                        currentTokenStart = i + 1;
                        currentTokenType = Token.NULL;
                        break;
                    case '"':
                        addToken(text, currentTokenStart, i - 1, Token.VARIABLE, newStartOffset + currentTokenStart);
                        currentTokenStart = i;
                        currentTokenType = Token.IDENTIFIER;
                }
                break;

            case Token.IDENTIFIER:
                switch (c) {
                    case '"':
                        addToken(text, currentTokenStart, i, Token.IDENTIFIER, newStartOffset + currentTokenStart);
                        currentTokenStart = i + 1;
                        currentTokenType = Token.NULL;
                        break;
                }
        }
    }
}