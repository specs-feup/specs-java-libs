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

import javax.swing.JCheckBox;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.gui.KeyPanel;

import pt.up.fe.specs.util.SpecsSwing;

/**
 * Panel for editing boolean values using a JCheckBox.
 *
 * <p>
 * This panel provides a checkbox for boolean DataKey values in the GUI.
 */
public class BooleanPanel extends KeyPanel<Boolean> {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * INSTANCE VARIABLES
     */
    private final JCheckBox checkBox;

    /**
     * Constructs a BooleanPanel for the given DataKey, DataStore, and label.
     *
     * @param key   the DataKey
     * @param data  the DataStore
     * @param label the label for the checkbox
     */
    public BooleanPanel(DataKey<Boolean> key, DataStore data, String label) {
        super(key, data);

        checkBox = new JCheckBox();

        setLayout(new BorderLayout());
        add(checkBox, BorderLayout.CENTER);
    }

    /**
     * Constructs a BooleanPanel for the given DataKey and DataStore.
     *
     * @param key  the DataKey
     * @param data the DataStore
     */
    public BooleanPanel(DataKey<Boolean> key, DataStore data) {
        this(key, data, key.getName());
    }

    /**
     * Returns the JCheckBox component.
     *
     * @return the JCheckBox
     */
    public JCheckBox getCheckBox() {
        return checkBox;
    }

    /**
     * Returns the current boolean value of the checkbox.
     *
     * @return true if selected, false otherwise
     */
    @Override
    public Boolean getValue() {
        return checkBox.isSelected();
    }

    /**
     * Sets the value of the checkbox.
     *
     * @param value the boolean value to set
     */
    @Override
    public void setValue(Boolean value) {
        SpecsSwing.runOnSwing(() -> checkBox.setSelected(value));
    }
}
