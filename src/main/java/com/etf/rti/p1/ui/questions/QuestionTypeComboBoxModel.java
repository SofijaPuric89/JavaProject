package com.etf.rti.p1.ui.questions;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

class QuestionTypeComboBoxModel implements ComboBoxModel<QuestionTypeComboBoxModelElement> {

    private QuestionTypeComboBoxModelElement selectedItem;
    private final List<QuestionTypeComboBoxModelElement> items = new ArrayList<>();

    @Override
    public void setSelectedItem(Object anItem) {
        selectedItem = (QuestionTypeComboBoxModelElement) anItem;
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
    public QuestionTypeComboBoxModelElement getElementAt(int index) {
        return items.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
    }

    public void add(QuestionGivenType questionGivenType, QuestionAskedForType questionAskedForType, String text) {
        items.add(new QuestionTypeComboBoxModelElement(questionGivenType, questionAskedForType, text));
    }
}

