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

import javax.swing.JCheckBox;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.gui.KeyPanel;

import pt.up.fe.specs.util.SpecsSwing;

/**
 *
 * @author Joao Bispo
 */
public class BooleanPanel extends KeyPanel<Boolean> {

    private static final long serialVersionUID = 1L;

    /**
     * INSTANCE VARIABLES
     */
    private final JCheckBox checkBox;

    public BooleanPanel(DataKey<Boolean> key, DataStore data, String label) {
        super(key, data);

        checkBox = new JCheckBox();

        setLayout(new BorderLayout());
        add(checkBox, BorderLayout.CENTER);
    }

    public BooleanPanel(DataKey<Boolean> key, DataStore data) {
        this(key, data, key.getName());
    }

    public JCheckBox getCheckBox() {
        return checkBox;
    }

    @Override
    public Boolean getValue() {
        return checkBox.isSelected();
    }

    @Override
    public void setValue(Boolean value) {
        SpecsSwing.runOnSwing(() -> checkBox.setSelected(value));
    }
}
