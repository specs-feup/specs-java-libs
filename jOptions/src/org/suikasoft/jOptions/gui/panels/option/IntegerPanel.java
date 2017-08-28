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

import java.awt.FlowLayout;

import javax.swing.JTextField;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.gui.KeyPanel;

import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsStrings;

/**
 *
 * @author Joao Bispo
 */
public class IntegerPanel extends KeyPanel<Integer> {

    private static final long serialVersionUID = 1L;

    /**
     * INSTANCE VARIABLES
     */
    private final JTextField value;

    public IntegerPanel(DataKey<Integer> key, DataStore data) {

        super(key, data);

        value = new JTextField(5);

        add(value);
        setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    private void setText(String text) {
        value.setText(text);
    }

    private String getText() {
        return value.getText();
    }

    @Override
    public Integer getValue() {
        String stringValue = getText();

        // If empty string assume 0
        if (stringValue.isEmpty()) {
            stringValue = getKey().getDefault().orElse(0).toString();
        }

        Integer result = SpecsStrings.decodeInteger(stringValue, () -> getKey().getDefault().orElse(0));

        if (result == null) {
            SpecsLogs.msgWarn("IS NULL!");
        }

        return result;
    }

    @Override
    public void setValue(Integer value) {
        setText(value.toString());
    }

}
