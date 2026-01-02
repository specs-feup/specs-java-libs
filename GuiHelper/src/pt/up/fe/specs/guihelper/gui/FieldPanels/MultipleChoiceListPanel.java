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
 * 
 * @author Joao Bispo
 */
public class MultipleChoiceListPanel extends FieldPanel {

    private static final long serialVersionUID = 1L;

    /**
     * INSTANCE VARIABLES
     */
    private final JLabel label;
    private final JLabel helper;
    // private JComboBox<String> selectedValues;
    // private JComboBox<String> possibleValues;
    private final JComboBox<String> selectedValues;
    private final JComboBox<String> possibleValues;
    private final JButton removeButton;
    private final JButton addButton;

    private List<String> possibleValuesShadow;
    private List<String> selectedValuesShadow;

    private final Collection<String> originalChoices;

    public MultipleChoiceListPanel(String labelName, Collection<String> choices) {
	label = new JLabel(labelName + ":");
	helper = new JLabel("| Options:");
	// removeButton = new JButton("X");
	removeButton = new JButton("Remove");
	addButton = new JButton("Add");

	originalChoices = choices;
	// selectedValues = new JComboBox<String>();
	// possibleValues = new JComboBox<String>();
	selectedValues = new JComboBox<>();
	possibleValues = new JComboBox<>();
	resetChoiceLists();

	addButton.addActionListener(evt -> addButtonActionPerformed(evt));

	removeButton.addActionListener(evt -> removeButtonActionPerformed(evt));

	add(label);
	add(selectedValues);
	add(removeButton);
	add(helper);
	add(possibleValues);
	add(addButton);

	setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    private void resetChoiceLists() {
	selectedValues.removeAllItems();
	possibleValues.removeAllItems();
	// selectedValues = new JComboBox();
	// possibleValues = new JComboBox();

	selectedValuesShadow = new ArrayList<>();
	possibleValuesShadow = new ArrayList<>();

	// Add possible values
	for (String choice : originalChoices) {
	    possibleValues.addItem(choice);
	    possibleValuesShadow.add(choice);
	}
    }

    /**
     * Moves one value from possibleValues to selectedValues. This method is not thread-safe.
     * 
     * @param valueName
     * @return
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
     * Moves one value from selectedValues to possibleValues. This method is not thread-safe.
     * 
     * @param valueName
     * @return
     */
    private boolean removeValue(String valueName) {
	if (valueName == null && selectedValuesShadow.isEmpty()) {
	    return true;
	}
	// Check if value is selected
	if (!selectedValuesShadow.contains(valueName)) {
	    SpecsLogs.warn(
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
     * Adds the option from the avaliable list to selected list.
     * 
     * @param evt
     */
    private void addButtonActionPerformed(ActionEvent evt) {
	// Check if there is text in the textfield
	final String selectedValue = (String) possibleValues.getSelectedItem();
	addValue(selectedValue);
    }

    /**
     * Removes the option from the selected list to the available list.
     * 
     * @param evt
     */
    private void removeButtonActionPerformed(ActionEvent evt) {
	// Check if there is text in the textfield
	final String selectedValue = (String) selectedValues.getSelectedItem();
	removeValue(selectedValue);
    }

    /**
     * The currently selected values.
     * 
     * @return currently selected values.
     */
    public List<String> getSelectedValues() {
	return Collections.unmodifiableList(selectedValuesShadow);
    }

    /**
     * For each element in the value list, add it to the selected items.
     * 
     * @param value
     */
    // public void updatePanel(FieldValue value) {
    @Override
    public void updatePanel(Object value) {
	// Reset current lists
	resetChoiceLists();

	StringList values = (StringList) value;

	for (String valueName : values.getStringList()) {
	    // Check if it is not already in the selected list.
	    if (selectedValuesShadow.contains(valueName)) {
		continue;
	    }
	    addValue(valueName);
	}
    }

    @Override
    public FieldType getType() {
	return FieldType.multipleChoiceStringList;
    }

    @Override
    public FieldValue getOption() {
	List<String> values = getSelectedValues();
	// return FieldValue.create(values, getType());
	return FieldValue.create(new StringList(values), getType());
    }

    @Override
    public JLabel getLabel() {
	return label;
    }

}
