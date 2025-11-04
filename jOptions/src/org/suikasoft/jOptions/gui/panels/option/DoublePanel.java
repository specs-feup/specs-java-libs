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

import java.awt.FlowLayout;
import java.io.Serial;

import javax.swing.JTextField;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.gui.KeyPanel;

import pt.up.fe.specs.util.SpecsStrings;

/**
 * Panel for editing double values using a JTextField.
 *
 * <p>
 * This panel provides a text field for double DataKey values in the GUI.
 */
public class DoublePanel extends KeyPanel<Double> {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * INSTANCE VARIABLES
     */
    private final JTextField value;

    /**
     * Constructs a DoublePanel for the given DataKey and DataStore.
     *
     * @param key  the DataKey
     * @param data the DataStore
     */
    public DoublePanel(DataKey<Double> key, DataStore data) {
        super(key, data);

        value = new JTextField(5);

        add(value);
        setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    /**
     * Sets the text of the text field.
     *
     * @param text the text to set
     */
    private void setText(String text) {
        value.setText(text);
    }

    /**
     * Gets the text from the text field.
     *
     * @return the text in the field
     */
    private String getText() {
        return value.getText();
    }

    /**
     * Returns the current double value from the text field.
     *
     * @return the double value
     */
    @Override
    public Double getValue() {
        String stringValue = getText();

        // Check if empty string
        if (stringValue.isEmpty()) {
            stringValue = getKey().getDefault().orElse(0.0).toString();
        }

        return SpecsStrings.decodeDouble(stringValue, () -> getKey().getDefault().orElse(0.0));
    }

    /**
     * Sets the value of the text field.
     *
     * @param value the double value to set
     */
    @Override
    public void setValue(Double value) {
        setText(value.toString());
    }

}
