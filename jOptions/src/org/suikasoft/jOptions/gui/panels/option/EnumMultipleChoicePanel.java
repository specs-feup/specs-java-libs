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

import java.awt.BorderLayout;
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
 * 
 * @author Joao Bispo
 */
public class EnumMultipleChoicePanel<T extends Enum<T>> extends KeyPanel<T> {

    private static final long serialVersionUID = 1L;

    /**
     * INSTANCE VARIABLES
     */
    // private final JComboBox<T> comboBoxValues;
    private final JComboBox<String> comboBoxValues;
    private final Collection<T> availableChoices;

    public EnumMultipleChoicePanel(DataKey<T> key, DataStore data) {
        super(key, data);

        // comboBoxValues = new JComboBox<String>();
        comboBoxValues = new JComboBox<>();

        T[] enumConstants = key.getValueClass().getEnumConstants();

        availableChoices = new HashSet<>(Arrays.asList(enumConstants));

        for (T choice : enumConstants) {
            // comboBoxValues.addItem(choice);
            comboBoxValues.addItem(valueToString(choice));
            // key.getDecoder()
            // .map(codec -> codec.encode(choice))
            // .orElse(choice.name()));
        }

        // Check if there is a default value
        getKey().getDefault()
                .map(defaultValue -> valueToString(defaultValue))
                .ifPresent(comboBoxValues::setSelectedItem);

        // if (getKey().getDefault().isPresent()) {
        // comboBoxValues.setSelectedItem(getKey().getDefault().get());
        // }

        setLayout(new BorderLayout());
        add(comboBoxValues, BorderLayout.CENTER);
    }

    private String valueToString(T value) {
        return getKey().getDecoder()
                .map(codec -> codec.encode(value))
                .orElse(value.name());
    }

    /*
    private JComboBox<T> getValues() {
        return comboBoxValues;
    }
    */
    private JComboBox<String> getValues() {
        return comboBoxValues;
    }

    @Override
    public T getValue() {
        var stringValue = getValues().getItemAt(getValues().getSelectedIndex());

        return getKey().getDecoder()
                .map(codec -> codec.decode(stringValue))
                .orElseGet(() -> SpecsEnums.valueOf(getKey().getValueClass(), stringValue));

    }

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
