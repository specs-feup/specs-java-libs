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
import java.io.Serial;

import javax.swing.JTextField;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.gui.KeyPanel;

/**
 * Abstract panel for editing string-based values using a JTextField.
 *
 * <p>This panel provides a text field for DataKey values of type T, to be extended for specific types.
 *
 * @param <T> the type of value handled by the panel
 */
public abstract class GenericStringPanel<T> extends KeyPanel<T> {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * INSTANCE VARIABLES
     */
    private final JTextField textField;

    /**
     * Constructs a GenericStringPanel for the given DataKey and DataStore.
     *
     * @param key the DataKey
     * @param data the DataStore
     */
    public GenericStringPanel(DataKey<T> key, DataStore data) {
        super(key, data);

        textField = new JTextField();

        setLayout(new BorderLayout());
        add(textField, BorderLayout.CENTER);
    }

    /**
     * Sets the text of the text field.
     *
     * @param text the text to set
     */
    public void setText(String text) {
        textField.setText(text);
    }

    /**
     * Gets the text from the text field.
     *
     * @return the text in the field
     */
    public String getText() {
        return textField.getText();
    }

}
