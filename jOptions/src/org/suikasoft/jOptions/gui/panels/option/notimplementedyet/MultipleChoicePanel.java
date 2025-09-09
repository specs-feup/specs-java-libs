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
import java.io.Serial;
import java.util.Collection;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import pt.up.fe.specs.guihelper.FieldType;
import pt.up.fe.specs.guihelper.BaseTypes.FieldValue;
import pt.up.fe.specs.guihelper.gui.FieldPanel;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsSwing;

/**
 * Panel for selecting a single value from multiple choices.
 *
 * <p>
 * This panel provides a combo box for selecting one value from a set of
 * choices.
 */
public class MultipleChoicePanel extends FieldPanel {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * INSTANCE VARIABLES
     */
    private JLabel label;
    private JComboBox<String> comboBoxValues;
    private Collection<String> availableChoices;

    /**
     * Constructs a MultipleChoicePanel for the given label and choices.
     *
     * @param labelName the label for the panel
     * @param choices   the available choices
     */
    public MultipleChoicePanel(String labelName, Collection<String> choices) {
        label = new JLabel(labelName + ":");
        comboBoxValues = new JComboBox<>();
        availableChoices = choices;

        for (String choice : choices) {
            comboBoxValues.addItem(choice);
        }

        add(label);
        add(comboBoxValues);

        setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    /**
     * Returns the combo box component for selecting values.
     *
     * @return the combo box
     */
    public JComboBox<String> getValues() {
        return comboBoxValues;
    }

    /**
     * Returns the current option as a FieldValue.
     *
     * @return the FieldValue
     */
    @Override
    public FieldValue getOption() {
        String selectedString = getValues().getItemAt(getValues().getSelectedIndex());
        return FieldValue.create(selectedString, getType());
    }

    /**
     * Updates the panel to select the given value.
     *
     * @param value the value to select
     */
    @Override
    public void updatePanel(Object value) {
        String stringValue = (String) value;
        if (stringValue.isEmpty()) {
            stringValue = availableChoices.iterator().next();
        }

        final String currentChoice = stringValue;

        boolean foundChoice = availableChoices.contains(currentChoice);

        if (!foundChoice) {
            SpecsLogs.getLogger().warning(
                    "Could not find choice '" + currentChoice + "'. Available " + "choices: " + availableChoices);
            return;
        }

        SpecsSwing.runOnSwing(() -> comboBoxValues.setSelectedItem(currentChoice));
    }

    /**
     * Returns the type of the field.
     *
     * @return the FieldType
     */
    @Override
    public FieldType getType() {
        return FieldType.multipleChoice;
    }

    /**
     * Returns the label component of the panel.
     *
     * @return the label
     */
    @Override
    public JLabel getLabel() {
        return label;
    }

}
