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

package pt.up.fe.specs.guihelper.gui.FieldPanels;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import pt.up.fe.specs.guihelper.FieldType;
import pt.up.fe.specs.guihelper.BaseTypes.FieldValue;
import pt.up.fe.specs.guihelper.gui.FieldPanel;
import pt.up.fe.specs.util.SpecsSwing;
import pt.up.fe.specs.util.utilities.StringList;

/**
 * 
 * @author Joao Bispo
 */
public class StringListPanel extends FieldPanel {

    private static final long serialVersionUID = 1L;

    /**
     * INSTANCE VARIABLES
     */
    private final JLabel label;
    // private JComboBox<String> comboBoxValues;
    private final JComboBox<String> comboBoxValues;
    private final JTextField possibleValue;
    private final JButton removeButton;
    private final JButton addButton;

    public StringListPanel(String labelName) {
        label = new JLabel(labelName + ":");
        // comboBoxValues = new JComboBox<String>();
        comboBoxValues = new JComboBox<>();
        removeButton = new JButton("X");
        // removeButton = new JButton("Remove");
        possibleValue = new JTextField(10);
        addButton = new JButton("Add");

        addButton.addActionListener(evt -> addButtonActionPerformed(evt));

        removeButton.addActionListener(evt -> removeButtonActionPerformed(evt));

        comboBoxValues.addActionListener(evt -> comboBoxEvent(evt));

        add(label);
        add(comboBoxValues);
        add(removeButton);
        add(possibleValue);
        add(addButton);

        setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    private void comboBoxEvent(ActionEvent evt) {
        Object selectedItem = comboBoxValues.getSelectedItem();

        // selectedItem may be null when the combo box is still being populated (see updatePanel's removeAllItems).
        // In that case, selectedItem.toString() throws an NPE.
        // Objects.toString() avoids this, so that updatePanel still adds the remaining items.

        possibleValue.setText(Objects.toString(selectedItem));
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

        comboBoxValues.addItem(newValueTrimmed);
    }

    /**
     * Removes the currently selected element of the list.
     * 
     * @param evt
     */
    private void removeButtonActionPerformed(ActionEvent evt) {
        int valueIndex = comboBoxValues.getSelectedIndex();
        if (valueIndex == -1) {
            return;
        }

        comboBoxValues.removeItemAt(valueIndex);
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
    public FieldType getType() {
        return FieldType.stringList;
    }

    @Override
    public FieldValue getOption() {
        List<String> newValues = new ArrayList<>();

        for (int i = 0; i < comboBoxValues.getItemCount(); i++) {
            newValues.add((String) comboBoxValues.getItemAt(i));
        }

        return FieldValue.create(new StringList(newValues), getType());
    }

    @Override
    public void updatePanel(Object option) {
        final StringList stringList = (StringList) option;

        SpecsSwing.runOnSwing(() -> {
            comboBoxValues.removeAllItems();
            for (String v : stringList.getStringList()) {
                comboBoxValues.addItem(v);
            }
        });
    }

    @Override
    public JLabel getLabel() {
        return label;
    }

}
