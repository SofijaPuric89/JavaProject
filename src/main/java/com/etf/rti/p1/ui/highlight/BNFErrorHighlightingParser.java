package com.etf.rti.p1.ui.highlight;

import com.etf.rti.p1.compiler.AParser;
import com.etf.rti.p1.compiler.bnf.BNFCompiler;
import com.google.common.io.NullOutputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.parser.AbstractParser;
import org.fife.ui.rsyntaxtextarea.parser.DefaultParseResult;
import org.fife.ui.rsyntaxtextarea.parser.DefaultParserNotice;
import org.fife.ui.rsyntaxtextarea.parser.ParseResult;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class BNFErrorHighlightingParser extends AbstractParser {

    @Override
    public ParseResult parse(RSyntaxDocument doc, String s) {
        DefaultParseResult result = new DefaultParseResult(this);

        try {
            String text = doc.getText(0, doc.getLength());
            BaseErrorListener errorListener = createErrorListener(doc, result);
            BNFCompiler bnfCompiler = getBNFCompiler(text);
            bnfCompiler.getLexer().addErrorListener(errorListener);
            AParser parser = bnfCompiler.getParser();
            parser.addErrorListener(errorListener);
            parser.init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private BaseErrorListener createErrorListener(final RSyntaxDocument doc, final DefaultParseResult result) {
        return new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                result.addNotice(createNotice(doc, line, charPositionInLine, "Syntax error: " + msg));
            }
        };
    }

    private DefaultParserNotice createNotice(RSyntaxDocument doc, int line, int charPositionInLine, String message) {
        Token affectedLineToken = doc.getTokenListForLine(line  - 1);
        Token affectedToken = RSyntaxUtilities.getTokenAtOffset(affectedLineToken, affectedLineToken.getOffset() + charPositionInLine);

        if (affectedToken != null) {
            return new DefaultParserNotice(this, message, line, affectedToken.getOffset(), affectedToken.length());
        } else {
            // select whole line if we cannot identify exact token
            return new DefaultParserNotice(this, message, line, affectedLineToken.getOffset(), affectedLineToken.length());
        }
    }

    private BNFCompiler getBNFCompiler(String text) throws IOException {
        BNFCompiler compiler = new BNFCompiler(new NullOutputStream());
        compiler.setInput(new ByteArrayInputStream(text.getBytes("UTF-8")));
        return compiler;
    }
}
