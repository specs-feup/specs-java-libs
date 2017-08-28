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

import javax.swing.JTextField;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.gui.KeyPanel;

/**
 *
 * @author Joao Bispo
 */
public class StringPanel extends KeyPanel<String> {

    private static final long serialVersionUID = 1L;

    /**
     * INSTANCE VARIABLES
     */
    private final JTextField textField;

    public StringPanel(DataKey<String> key, DataStore data) {
	super(key, data);

	textField = new JTextField();

	setLayout(new BorderLayout());
	add(textField, BorderLayout.CENTER);

    }

    public void setText(String text) {
	textField.setText(text);
    }

    public String getText() {
	return textField.getText();
    }

    @Override
    public String getValue() {
	return getText();
    }

    @Override
    public void setValue(String value) {
	setText(value);
    }
}
