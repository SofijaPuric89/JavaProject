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
    private static final KeyStroke SHORTCUT = KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0);

    private BNFReplaceSupport() {
    }

    public static void enable(RSyntaxTextArea textArea) {
        textArea.setMarkAllHighlightColor(MARK_ALL_HIGHLIGHT_COLOR);
        RenameNonterminalAction renameNonterminalAction = new RenameNonterminalAction(textArea);
        textArea.getInputMap().put(SHORTCUT, renameNonterminalAction);
        JPopupMenu popupMenu = textArea.getPopupMenu();
        popupMenu.remove(popupMenu.getComponentCount() - 1);
        popupMenu.add(renameNonterminalAction);
    }

    private static final class RenameNonterminalAction extends TextAction {

        private final RSyntaxTextArea textArea;

        RenameNonterminalAction(RSyntaxTextArea textArea) {
            super("Rename Nonterminal");
            this.putValue("AcceleratorKey", SHORTCUT);
            this.textArea = textArea;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JTextComponent component = getTextComponent(e);
            // we always take the selection start
            int caretPosition = component.getSelectionStart();
            Token token = textArea.modelToToken(caretPosition);

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
    }
}
