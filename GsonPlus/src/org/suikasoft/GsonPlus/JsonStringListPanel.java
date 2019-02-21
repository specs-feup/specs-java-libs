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

package org.suikasoft.GsonPlus;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.gui.KeyPanel;

import pt.up.fe.specs.util.SpecsSwing;

/**
 * 
 * @author Joao Bispo
 */
public class JsonStringListPanel extends KeyPanel<List<String>> {

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

    public static JsonStringListPanel newInstance(DataKey<List<String>> key, DataStore data) {
        return new JsonStringListPanel(key, data);
    }

    public JsonStringListPanel(DataKey<List<String>> key, DataStore data) {
        super(key, data);

        jListValues = new JList<>();
        jListValues.setModel(values = new DefaultListModel<>());
        // jFistValues.setCellRenderer(new CellRenderer());
        removeButton = new JButton("Remove");
        addButton = new JButton("Add");
        possibleValue = new JTextField();

        addButton.addActionListener(this::addButtonActionPerformed);
        removeButton.addActionListener(this::removeButtonActionPerformed);
        possibleValue.addActionListener(this::addButtonActionPerformed);
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
    public JsonStringList getValue() {
        List<String> newValues = new ArrayList<>();

        for (int i = 0; i < values.size(); i++) {
            newValues.add(values.getElementAt(i));
        }

        return JsonStringList.newInstance(newValues.toArray(new String[0]));
    }

    @Override
    public <ET extends List<String>> void setValue(ET stringList) {

        SpecsSwing.runOnSwing(() -> {
            values.clear();
            for (String v : stringList) {
                values.addElement(v);
            }
        });
    }

}
