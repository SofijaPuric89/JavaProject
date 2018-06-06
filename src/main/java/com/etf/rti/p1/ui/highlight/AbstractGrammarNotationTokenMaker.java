package com.etf.rti.p1.ui.highlight;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMaker;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMap;

import javax.swing.text.Segment;

abstract class AbstractGrammarNotationTokenMaker extends AbstractTokenMaker {

    protected int currentTokenStart;
    protected int currentTokenType;

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

            handleCurrentToken(text, newStartOffset, i, c);
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

    abstract void handleCurrentToken(Segment text, int newStartOffset, int i, char c);

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
