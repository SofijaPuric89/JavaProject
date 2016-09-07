package com.etf.rti.p1.ui.highlight;

import org.fife.ui.autocomplete.*;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class BNFCodeCompletionSupport {

    private BNFCodeCompletionSupport() {
    }

    public static void enable(RSyntaxTextArea textArea) {
        CompletionProvider provider = createCompletionProvider();
        AutoCompletion autoCompletion = new AutoCompletion(provider);
        autoCompletion.setParameterAssistanceEnabled(true);
        autoCompletion.install(textArea);
    }

    private static CompletionProvider createCompletionProvider() {
        DefaultCompletionProvider provider = new DefaultCompletionProvider() {
            @Override
            protected boolean isValidChar(char ch) {
                // because DefaultCompletionProvider doesn't consider these to be valid chars
                return super.isValidChar(ch) || (ch == ':') || (ch == '|') || (ch == '<');
            }
        };

        provider.addCompletion(new BasicCompletion(provider, "::=", "is defined as"));
        provider.addCompletion(new BasicCompletion(provider, "|", "or"));

        provider.addCompletion(new TemplateCompletion(provider, "rule", "rule - new rule definition", "<${nt}> ::= ${cursor}"));
        provider.addCompletion(new TemplateCompletion(provider, "<", "< - new nonterminal symbol", "<${nt}> ${cursor}"));

        return provider;
    }
}
