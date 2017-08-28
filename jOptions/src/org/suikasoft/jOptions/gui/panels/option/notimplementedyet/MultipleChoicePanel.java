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
import java.util.Collection;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import pt.up.fe.specs.guihelper.FieldType;
import pt.up.fe.specs.guihelper.BaseTypes.FieldValue;
import pt.up.fe.specs.guihelper.gui.FieldPanel;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsSwing;

/**
 * 
 * @author Joao Bispo
 */
public class MultipleChoicePanel extends FieldPanel {

    private static final long serialVersionUID = 1L;

    /**
     * INSTANCE VARIABLES
     */
    private JLabel label;
    // private JComboBox<String> comboBoxValues;
    private JComboBox<String> comboBoxValues;
    private Collection<String> availableChoices;

    public MultipleChoicePanel(String labelName, Collection<String> choices) {
	label = new JLabel(labelName + ":");
	// comboBoxValues = new JComboBox<String>();
	comboBoxValues = new JComboBox<>();
	availableChoices = choices;

	for (String choice : choices) {
	    comboBoxValues.addItem(choice);
	}

	add(label);
	add(comboBoxValues);

	setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    public JComboBox<String> getValues() {
	// public JComboBox getValues() {
	return comboBoxValues;
    }

    @Override
    public FieldValue getOption() {
	String selectedString = getValues().getItemAt(getValues().getSelectedIndex());
	// String selectedString = getValues().getSelectedItem();
	return FieldValue.create(selectedString, getType());
    }

    /**
     * Selects the option in AppValue object. If the option could not be found, selects the first option.
     * 
     * @param value
     * @return true if the option is one of the available choices and could be selected, false otherwise
     */
    @Override
    public void updatePanel(Object value) {
	// Check if the value in FieldValue is one of the possible choices
	// final String currentChoice = BaseUtils.getString(value);

	// final String currentChoice = (String) value;
	String stringValue = (String) value;
	if (stringValue.isEmpty()) {
	    stringValue = availableChoices.iterator().next();
	}

	final String currentChoice = stringValue;
	/*
	String currentChoice = (String) value;
	if(currentChoice.isEmpty()) {
	   currentChoice = availableChoices.iterator().next();
	}
	 *
	 */

	boolean foundChoice = availableChoices.contains(currentChoice);

	if (!foundChoice) {
	    SpecsLogs.getLogger().warning(
		    "Could not find choice '" + currentChoice + "'. Available " + "choices: " + availableChoices);
	    return;
	}

	SpecsSwing.runOnSwing(new Runnable() {

	    @Override
	    public void run() {
		comboBoxValues.setSelectedItem(currentChoice);
	    }
	});
    }

    @Override
    public FieldType getType() {
	return FieldType.multipleChoice;
    }

    @Override
    public JLabel getLabel() {
	return label;
    }

}
