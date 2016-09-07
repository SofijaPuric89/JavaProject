package com.etf.rti.p1.ui.highlight;

import org.apache.commons.lang3.StringUtils;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class BNFReplaceSupport {

    private static final Color MARK_ALL_HIGHLIGHT_COLOR = new Color(255, 236, 170);

    private BNFReplaceSupport() {
    }

    public static void enable(RSyntaxTextArea textArea) {
        textArea.setMarkAllHighlightColor(MARK_ALL_HIGHLIGHT_COLOR);
        JPopupMenu popupMenu = textArea.getPopupMenu();
        popupMenu.remove(popupMenu.getComponentCount() - 1);
        // TODO: Add "Replace" action (simple one, just text replace)
        popupMenu.add(new RenameNonterminalAction(textArea));
    }

    private final static class RenameNonterminalAction extends TextAction {

        private final RSyntaxTextArea textArea;

        RenameNonterminalAction(RSyntaxTextArea textArea) {
            super("Rename Nonterminal");
            this.putValue("AcceleratorKey", KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
            this.textArea = textArea;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JTextComponent component = getTextComponent(e);
            // we always take the selection start
            int caretPosition = component.getSelectionStart();
            Token token = textArea.modelToToken(caretPosition);
            System.out.println(token);

            if ((token != null) && (token.getType() == Token.VARIABLE)) {
                String oldName = token.getLexeme();

                SearchContext context = createInitialSearchContext(oldName);
                SearchEngine.markAll(textArea, context);

                String newName = getNewName(oldName);

                if (StringUtils.isNotBlank(oldName) && !newName.equals(oldName)) {
                    context.setReplaceWith(newName);
                    SearchEngine.replaceAll(textArea, context);
                } else {
                    // clear all marked words
                    SearchEngine.markAll(textArea, new SearchContext());
                }
            }
        }

        private SearchContext createInitialSearchContext(String oldName) {
            SearchContext context = new SearchContext();
            context.setSearchFor(oldName);
            // ??? Is it case sensitive
            context.setMatchCase(false);
            context.setWholeWord(false);
            return context;
        }

        private String getNewName(String oldName) {
            BNFRenameNonterminalDialog dialog = new BNFRenameNonterminalDialog(oldName);
            dialog.setVisible(true);
            return dialog.getDialogValue();
        }

        @Override
        public boolean isEnabled() {
            // TODO
            return super.isEnabled();
        }
    }
}
