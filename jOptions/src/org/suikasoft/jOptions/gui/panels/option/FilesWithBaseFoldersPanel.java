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
import java.io.File;
import java.io.Serial;
import java.util.Map;

import javax.swing.JTextField;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.gui.KeyPanel;

/**
 * Panel for editing mappings of files to base folders using a text field.
 *
 * <p>
 * This panel provides a text field for DataKey values of type Map<File, File>
 * in the GUI.
 */
public class FilesWithBaseFoldersPanel extends KeyPanel<Map<File, File>> {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * INSTANCE VARIABLES
     */
    private final JTextField textField;

    /**
     * Constructs a FilesWithBaseFoldersPanel for the given DataKey and DataStore.
     *
     * @param key  the DataKey
     * @param data the DataStore
     */
    public FilesWithBaseFoldersPanel(DataKey<Map<File, File>> key, DataStore data) {
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

    /**
     * Returns the current value as a map of files to base folders.
     *
     * @return the map value
     */
    @Override
    public Map<File, File> getValue() {
        return getKey().decode(getText());
    }

    /**
     * Sets the value of the text field from a map.
     *
     * @param value the map value to set
     * @param <ET>  the type of value (extends Map<File, File>)
     */
    @Override
    public <ET extends Map<File, File>> void setValue(ET value) {
        var simplifiedValue = getKey().getCustomSetter().get().get(value, getData());
        setText(getKey().encode(simplifiedValue));
    }

}
