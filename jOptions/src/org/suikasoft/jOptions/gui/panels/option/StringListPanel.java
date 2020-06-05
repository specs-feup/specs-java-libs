/*
 * Copyright 2010 SPeCS Research Group.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package org.suikasoft.jOptions.gui.panels.option;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.gui.KeyPanel;

import pt.up.fe.specs.util.SpecsSwing;
import pt.up.fe.specs.util.utilities.StringList;

/**
 * 
 * @author Joao Bispo
 */
public class StringListPanel extends KeyPanel<StringList> {

    private static final long serialVersionUID = 1L;

    /**
     * INSTANCE VARIABLES
     */

    // private JComboBox<String> comboBoxValues;
    // private final JComboBox<String> comboBoxValues;
    private final JTextField possibleValue;
    private final JButton removeButton;
    private final JButton addButton;

    private final JList<String> jListValues;
    private final DefaultListModel<String> values;

    private final JComboBox<String> predefinedList;
    private final JButton addPredefinedButton;

    private List<String> predefinedLabels;
    private List<String> predefinedValues;
    boolean isPredefinedEnabled;

    public static StringListPanel newInstance(DataKey<StringList> key, DataStore data) {
        return newInstance(key, data, Collections.emptyList());
        // return new StringListPanel(key, data);
    }

    public static StringListPanel newInstance(DataKey<StringList> key, DataStore data,
            List<String> predefinedLabelsValues) {

        var panel = new StringListPanel(key, data);
        panel.setPredefinedValues(predefinedLabelsValues);
        return panel;
    }

    public StringListPanel(DataKey<StringList> key, DataStore data) {
        super(key, data);

        predefinedLabels = new ArrayList<>();
        predefinedValues = new ArrayList<>();
        isPredefinedEnabled = false;

        jListValues = new JList<>();
        jListValues.setModel(values = new DefaultListModel<>());
        // jFistValues.setCellRenderer(new CellRenderer());
        removeButton = new JButton("Remove");
        addButton = new JButton("Add");
        possibleValue = new JTextField();

        predefinedList = new JComboBox<>();
        addPredefinedButton = new JButton("Add");

        addButton.addActionListener(this::addButtonActionPerformed);
        removeButton.addActionListener(this::removeButtonActionPerformed);
        possibleValue.addActionListener(this::addButtonActionPerformed);
        addPredefinedButton.addActionListener(this::addPredefinedButtonActionPerformed);

        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(possibleValue, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.gridheight = 2;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        add(new JScrollPane(jListValues), c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.gridx = 1;
        c.gridy = 0;
        add(addButton, c);
        c.gridy = 1;
        add(removeButton, c);

        GridBagConstraints preLabelsConstrains = new GridBagConstraints();

        // Predefined list
        // TODO: Are these necessary?
        // c.gridwidth = 2;
        // c.gridheight = 2;

        preLabelsConstrains.weightx = 1;
        preLabelsConstrains.weighty = 0;
        preLabelsConstrains.gridx = 0;
        preLabelsConstrains.gridy = 3;
        preLabelsConstrains.fill = GridBagConstraints.HORIZONTAL;
        predefinedList.setVisible(false);
        add(predefinedList, preLabelsConstrains);

        // Add button
        GridBagConstraints addConstrains = new GridBagConstraints();

        addConstrains.fill = GridBagConstraints.HORIZONTAL;
        addConstrains.gridheight = 1;
        addConstrains.weightx = 0;
        addConstrains.weighty = 0;
        addConstrains.gridx = 2;
        addConstrains.gridy = 3;
        addPredefinedButton.setVisible(false);
        add(addPredefinedButton, addConstrains);

        // c.gridy = 3;
        // add(addPredefinedButton, c);

        /*
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.gridx = 1;
        c.gridy = 0;
        add(predefinedList, c);
        c.gridy = 1;
        add(removeButton, c);
        */
        // comboBoxValues = new JComboBox<>();
        // removeButton = new JButton("X");
        // // removeButton = new JButton("Remove");
        // possibleValue = new JTextField(10);
        // addButton = new JButton("Add");

        // addButton.addActionListener(evt -> addButtonActionPerformed(evt));
        //
        // removeButton.addActionListener(evt -> removeButtonActionPerformed(evt));

        // add(comboBoxValues);
        // add(removeButton);
        // add(possibleValue);
        // add(addButton);

    }

    private void initPredefinedValues(List<String> labels, List<String> values) {
        if (!isPredefinedEnabled) {
            isPredefinedEnabled = true;

            // System.out.println("SET VISIBLE");

            predefinedList.setVisible(true);
            addPredefinedButton.setVisible(true);
            // System.out.println("SWING THREAD");

            repaint();
            revalidate();

        }

        // Remove previous values, if present
        predefinedList.removeAllItems();

        // Update values
        this.predefinedLabels = new ArrayList<>(labels);
        this.predefinedValues = new ArrayList<>(values);

        for (var label : predefinedLabels) {
            predefinedList.addItem(label);
        }
        SpecsSwing.runOnSwing(() -> {
            predefinedList.revalidate();
            predefinedList.repaint();
        });
        //
        // revalidate();
        // repaint();
    }

    public void setPredefinedValues(List<String> labelValuePairs) {

        // If empty and not initialized, just return
        if (labelValuePairs.isEmpty() && !isPredefinedEnabled) {
            return;
        }

        if (labelValuePairs.size() % 2 != 0) {
            throw new RuntimeException("Expected an even number of label-value pairs, got " + labelValuePairs.size()
                    + ": " + labelValuePairs);
        }

        List<String> labels = new ArrayList<>();
        List<String> values = new ArrayList<>();
        for (int i = 0; i < labelValuePairs.size(); i += 2) {
            labels.add(labelValuePairs.get(i));
            values.add(labelValuePairs.get(i + 1));
        }

        initPredefinedValues(labels, values);
    }

    /**
     * Adds the text in the textfield to the combo box
     * 
     * @param evt
     */
    private void addButtonActionPerformed(ActionEvent evt) {
        // System.out.println("Current item number:"+values.getSelectedIndex());
        // Check if there is text in the textfield
        String newValueTrimmed = possibleValue.getText().trim();
        if (newValueTrimmed.isEmpty()) {
            return;
        }

        addValue(newValueTrimmed);
    }

    /**
     * Adds the predefined value to the list if not present yet
     * 
     * @param evt
     */
    private void addPredefinedButtonActionPerformed(ActionEvent evt) {

        // Check selected predefined value
        var selectedItemIndex = predefinedList.getSelectedIndex();

        var value = predefinedValues.get(selectedItemIndex);

        // If already contains value, do nothing
        if (values.contains(value)) {
            return;
        }

        // Add value
        addValue(value);

    }

    private void addValue(String newValue) {
        values.addElement(newValue);
        jListValues.setSelectedIndex(values.size() - 1);
    }

    /**
     * Removes the currently selected element of the list.
     * 
     * @param evt
     */
    private void removeButtonActionPerformed(ActionEvent evt) {
        int valueIndex = jListValues.getSelectedIndex();
        if (valueIndex == -1) {
            return;
        }

        values.remove(valueIndex);
        if (values.size() > valueIndex) {
            jListValues.setSelectedIndex(valueIndex);
        } else {
            if (!values.isEmpty()) {
                jListValues.setSelectedIndex(values.size() - 1);
            }
        }
    }

    // public JComboBox<String> getValues() {
    /*
    public JComboBox<String> getValues() {
    return comboBoxValues;
    }
    */

    // public void setValues(JComboBox<String> values) {
    /*
    public void setValues(JComboBox<String> values) {
    this.comboBoxValues = values;
    }
    */

    @Override
    public StringList getValue() {
        List<String> newValues = new ArrayList<>();

        for (int i = 0; i < values.size(); i++) {
            newValues.add(values.getElementAt(i));
        }

        return StringList.newInstance(newValues.toArray(new String[0]));
    }

    @Override
    public <ET extends StringList> void setValue(ET stringList) {

        SpecsSwing.runOnSwing(() -> {
            values.clear();
            for (String v : stringList) {
                values.addElement(v);
            }
        });
    }

}
