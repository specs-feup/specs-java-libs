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
 * specific language governing permissions and limitations under the License.
 */

package org.suikasoft.jOptions.gui.panels.option;

import java.awt.BorderLayout;
import java.io.Serial;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.JComboBox;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.gui.KeyPanel;

import pt.up.fe.specs.util.SpecsEnums;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsSwing;

/**
 * Panel for selecting enum values from a combo box.
 *
 * <p>
 * This panel provides a combo box for selecting enum DataKey values in the GUI.
 *
 * @param <T> the enum type
 */
public class EnumMultipleChoicePanel<T extends Enum<T>> extends KeyPanel<T> {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * INSTANCE VARIABLES
     */
    private final JComboBox<String> comboBoxValues;
    private final Collection<T> availableChoices;

    /**
     * Constructs an EnumMultipleChoicePanel for the given DataKey and DataStore.
     *
     * @param key  the DataKey
     * @param data the DataStore
     */
    public EnumMultipleChoicePanel(DataKey<T> key, DataStore data) {
        super(key, data);

        comboBoxValues = new JComboBox<>();

        T[] enumConstants = key.getValueClass().getEnumConstants();

        availableChoices = new HashSet<>(Arrays.asList(enumConstants));

        for (T choice : enumConstants) {
            comboBoxValues.addItem(valueToString(choice));
        }

        // Check if there is a default value
        getKey().getDefault()
                .map(this::valueToString)
                .ifPresent(comboBoxValues::setSelectedItem);

        setLayout(new BorderLayout());
        add(comboBoxValues, BorderLayout.CENTER);
    }

    /**
     * Converts an enum value to its string representation using the key's decoder
     * if present.
     *
     * @param value the enum value
     * @return the string representation
     */
    private String valueToString(T value) {
        return getKey().getDecoder()
                .map(codec -> codec.encode(value))
                .orElse(value.name());
    }

    /**
     * Returns the combo box component for selecting values.
     *
     * @return the combo box
     */
    private JComboBox<String> getValues() {
        return comboBoxValues;
    }

    /**
     * Returns the currently selected enum value.
     *
     * @return the selected enum value
     */
    @Override
    public T getValue() {
        var stringValue = getValues().getItemAt(getValues().getSelectedIndex());

        return getKey().getDecoder()
                .map(codec -> codec.decode(stringValue))
                .orElseGet(() -> SpecsEnums.valueOf(getKey().getValueClass(), stringValue));
    }

    /**
     * Sets the selected value in the combo box.
     *
     * @param value the value to set
     * @param <ET>  the enum type
     */
    @Override
    public <ET extends T> void setValue(ET value) {
        // Choose first if value is null
        T currentValue = value;

        if (currentValue == null) {
            currentValue = getKey().getValueClass().getEnumConstants()[0];
        }

        if (!availableChoices.contains(currentValue)) {
            SpecsLogs.warn(
                    "Could not find choice '" + currentValue + "'. Available " + "choices: " + availableChoices);
            currentValue = getKey().getValueClass().getEnumConstants()[0];
        }

        T finalValue = currentValue;

        SpecsSwing.runOnSwing(() -> comboBoxValues.setSelectedItem(valueToString(finalValue)));
    }

}
