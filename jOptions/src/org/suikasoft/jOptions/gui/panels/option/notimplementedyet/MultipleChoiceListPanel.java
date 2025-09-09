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

package org.suikasoft.jOptions.gui.panels.option.notimplementedyet;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import pt.up.fe.specs.guihelper.FieldType;
import pt.up.fe.specs.guihelper.BaseTypes.FieldValue;
import pt.up.fe.specs.guihelper.gui.FieldPanel;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.utilities.StringList;

/**
 * Deprecated panel for editing multiple choice lists.
 *
 * <p>
 * This panel was replaced with EnumMultipleChoicePanel.
 *
 * @deprecated replaced with EnumMultipleChoicePanel
 */
@Deprecated
public class MultipleChoiceListPanel extends FieldPanel {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * INSTANCE VARIABLES
     */
    private final JLabel label;
    private final JLabel helper;
    private final JComboBox<String> selectedValues;
    private final JComboBox<String> possibleValues;
    private final JButton removeButton;
    private final JButton addButton;

    private List<String> possibleValuesShadow;
    private List<String> selectedValuesShadow;

    private final Collection<String> originalChoices;

    /**
     * Constructs a MultipleChoiceListPanel for the given label and choices.
     *
     * @param labelName the label for the panel
     * @param choices   the available choices
     */
    public MultipleChoiceListPanel(String labelName, Collection<String> choices) {
        label = new JLabel(labelName + ":");
        helper = new JLabel("| Options:");
        removeButton = new JButton("Remove");
        addButton = new JButton("Add");

        originalChoices = choices;
        selectedValues = new JComboBox<>();
        possibleValues = new JComboBox<>();
        resetChoiceLists();

        addButton.addActionListener(this::addButtonActionPerformed);

        removeButton.addActionListener(this::removeButtonActionPerformed);

        add(label);
        add(selectedValues);
        add(removeButton);
        add(helper);
        add(possibleValues);
        add(addButton);

        setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    /**
     * Resets the choice lists to their original state.
     */
    private void resetChoiceLists() {
        selectedValues.removeAllItems();
        possibleValues.removeAllItems();

        selectedValuesShadow = new ArrayList<>();
        possibleValuesShadow = new ArrayList<>();

        // Add possible values
        for (String choice : originalChoices) {
            possibleValues.addItem(choice);
            possibleValuesShadow.add(choice);
        }
    }

    /**
     * Moves one value from possibleValues to selectedValues. This method is not
     * thread-safe.
     * 
     * @param valueName the name of the value to add
     * @return true if the value was successfully added, false otherwise
     */
    private boolean addValue(String valueName) {
        if (valueName == null && possibleValuesShadow.isEmpty()) {
            return true;
        }

        // Check if value is available
        if (!possibleValuesShadow.contains(valueName)) {
            SpecsLogs.msgInfo("Could not find value '" + valueName + "' in Multiple Choice "
                    + "list. Available choices:" + possibleValuesShadow);
            return false;
        }

        // Remove from possible and add to selected
        possibleValues.removeItem(valueName);
        possibleValuesShadow.remove(valueName);
        selectedValues.addItem(valueName);
        selectedValuesShadow.add(valueName);
        return true;
    }

    /**
     * Moves one value from selectedValues to possibleValues. This method is not
     * thread-safe.
     * 
     * @param valueName the name of the value to remove
     * @return true if the value was successfully removed, false otherwise
     */
    private boolean removeValue(String valueName) {
        if (valueName == null && selectedValuesShadow.isEmpty()) {
            return true;
        }
        // Check if value is selected
        if (!selectedValuesShadow.contains(valueName)) {
            SpecsLogs.getLogger().warning(
                    "Could not find value '" + valueName + "' in already "
                            + "selected choices. Currently selected choices:" + selectedValuesShadow);
            return false;
        }

        // Remove from possible and add to selected
        selectedValues.removeItem(valueName);
        selectedValuesShadow.remove(valueName);
        possibleValues.addItem(valueName);
        possibleValuesShadow.add(valueName);
        return true;
    }

    /**
     * Adds the option from the available list to the selected list.
     * 
     * @param evt the action event
     */
    private void addButtonActionPerformed(ActionEvent evt) {
        final String selectedValue = (String) possibleValues.getSelectedItem();
        addValue(selectedValue);
    }

    /**
     * Removes the option from the selected list to the available list.
     * 
     * @param evt the action event
     */
    private void removeButtonActionPerformed(ActionEvent evt) {
        final String selectedValue = (String) selectedValues.getSelectedItem();
        removeValue(selectedValue);
    }

    /**
     * Gets the currently selected values.
     * 
     * @return an unmodifiable list of currently selected values
     */
    public List<String> getSelectedValues() {
        return Collections.unmodifiableList(selectedValuesShadow);
    }

    /**
     * Updates the panel with the given value.
     * 
     * @param value the value to update the panel with
     */
    @Override
    public void updatePanel(Object value) {
        resetChoiceLists();

        StringList values = (StringList) value;

        for (String valueName : values.getStringList()) {
            if (selectedValuesShadow.contains(valueName)) {
                continue;
            }
            addValue(valueName);
        }
    }

    /**
     * Gets the type of the field.
     * 
     * @return the field type
     */
    @Override
    public FieldType getType() {
        return FieldType.multipleChoiceStringList;
    }

    /**
     * Gets the current option as a FieldValue.
     * 
     * @return the current option
     */
    @Override
    public FieldValue getOption() {
        List<String> values = getSelectedValues();
        return FieldValue.create(new StringList(values), getType());
    }

    /**
     * Gets the label of the panel.
     * 
     * @return the label
     */
    @Override
    public JLabel getLabel() {
        return label;
    }

}
