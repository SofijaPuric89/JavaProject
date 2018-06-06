package com.etf.rti.p1.ui.questions;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

class QuestionTypeComboBoxModel implements ComboBoxModel<QuestionModelElement> {

    private QuestionModelElement selectedItem;
    private final List<QuestionModelElement> items = new ArrayList<>();

    @Override
    public void setSelectedItem(Object anItem) {
        selectedItem = (QuestionModelElement) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selectedItem;
    }

    @Override
    public int getSize() {
        return items.size();
    }

    @Override
    public QuestionModelElement getElementAt(int index) {
        return items.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
    }

    public void add(QuestionGrammarGivenType questionGrammarGivenType, QuestionAskedForType questionAskedForType, String text) {
        items.add(new QuestionModelElement(questionGrammarGivenType, questionAskedForType, text));
    }
}

