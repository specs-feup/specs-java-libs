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
import java.io.File;
import java.util.Map;

import javax.swing.JTextField;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.gui.KeyPanel;

/**
 *
 * @author Joao Bispo
 */
public class FilesWithBaseFoldersPanel extends KeyPanel<Map<File, File>> {

    private static final long serialVersionUID = 1L;

    /**
     * INSTANCE VARIABLES
     */
    private final JTextField textField;

    public FilesWithBaseFoldersPanel(DataKey<Map<File, File>> key, DataStore data) {
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
    public Map<File, File> getValue() {
        return getKey().decode(getText());
    }

    @Override
    public <ET extends Map<File, File>> void setValue(ET value) {
        System.out.println("DATA: " + getData());
        // Simplify value before setting
        System.out.println("ORIGINAL VALUE: " + value);
        var simplifiedValue = getKey().getCustomSetter().get().get(value, getData());
        System.out.println("SIMPLIFIED VALUE:\n" + simplifiedValue);
        setText(getKey().encode(simplifiedValue));
    }

}
