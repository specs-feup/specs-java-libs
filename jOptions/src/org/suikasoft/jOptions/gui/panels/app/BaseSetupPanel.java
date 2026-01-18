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

package org.suikasoft.jOptions.gui.panels.app;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.Serial;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.gui.KeyPanel;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;
import org.suikasoft.jOptions.storedefinition.StoreSection;

import pt.up.fe.specs.util.SpecsLogs;

/**
 * Panel which contains the options for a setup, organizing KeyPanels for each
 * DataKey.
 *
 * <p>
 * This panel arranges option panels for each DataKey in a StoreDefinition,
 * supporting indentation and sectioning.
 *
 * @author Joao Bispo
 */
public class BaseSetupPanel extends JPanel {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Map<String, KeyPanel<?>> panels;
    private final StoreDefinition storeDefinition;

    /**
     * Constructs a BaseSetupPanel for the given StoreDefinition and DataStore.
     *
     * @param keys the StoreDefinition
     * @param data the DataStore
     */
    public BaseSetupPanel(StoreDefinition keys, DataStore data) {
        this(keys, data, 0);
    }

    /**
     * Constructs a BaseSetupPanel for the given StoreDefinition, DataStore, and
     * indentation level.
     *
     * @param keys            the StoreDefinition
     * @param data            the DataStore
     * @param identationLevel the indentation level
     */
    public BaseSetupPanel(StoreDefinition keys, DataStore data, int identationLevel) {
        storeDefinition = keys;
        panels = new HashMap<>();

        if (keys == null) {
            throw new RuntimeException("StoreDefinition is null.");
        }

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        GridBagConstraints labelC = new GridBagConstraints();
        labelC.anchor = GridBagConstraints.LINE_START;
        labelC.insets = new Insets(3, 10, 3, 10);
        labelC.fill = GridBagConstraints.HORIZONTAL;
        labelC.gridx = 0;
        labelC.gridy = 0;
        GridBagConstraints panelC = new GridBagConstraints();
        panelC.insets = new Insets(3, 0, 3, 10);
        panelC.fill = GridBagConstraints.HORIZONTAL;
        panelC.gridx = 1;
        panelC.gridy = 0;
        panelC.weightx = 1;

        GridBagConstraints separatorC = new GridBagConstraints();
        separatorC.gridx = 0; // aligned
        separatorC.gridwidth = 2; // 2 columns wide
        separatorC.fill = GridBagConstraints.HORIZONTAL;
        separatorC.insets = new Insets(25, 10, 15, 10);

        boolean isFirst = true;
        for (StoreSection section : keys.getSections()) {
            if (section.getName().isPresent()) {

                // Only add separators if it is not the first section
                if (!isFirst) {

                    separatorC.gridy = labelC.gridy;
                    add(new javax.swing.JSeparator(), separatorC);
                    labelC.gridy++;
                    panelC.gridy++;

                }

                JLabel test = new JLabel("(" + section.getName().get() + ")");
                Font f = test.getFont();
                test.setFont(f.deriveFont(f.getStyle() | Font.BOLD));

                add(test, labelC);
                labelC.gridy++;
                panelC.gridy++;
            }

            for (DataKey<?> key : section.getKeys()) {

                KeyPanel<?> panel = key.getPanel(data);

                JLabel label = new JLabel(key.getLabel() + ": ");
                add(label, labelC);
                add(panel, panelC);

                labelC.gridy++;
                panelC.gridy++;

                String stringKey = key.getName();
                panels.put(stringKey, panel);
            }

            isFirst = false;
        }

        GridBagConstraints paddingVC = new GridBagConstraints();
        paddingVC.gridx = 0;
        paddingVC.gridy = panelC.gridy;
        paddingVC.fill = GridBagConstraints.VERTICAL;
        paddingVC.weighty = 1;
        add(new JPanel(), paddingVC);
    }

    /**
     * Returns the map of KeyPanels associated with their respective DataKey names.
     *
     * @return a map of KeyPanels
     */
    public Map<String, KeyPanel<?>> getPanels() {
        return panels;
    }

    /**
     * Loads values from the given DataStore into the KeyPanels.
     *
     * @param map the DataStore containing values to load
     */
    public void loadValues(DataStore map) {
        if (map == null) {
            map = DataStore.newInstance("empty_map");
        }

        for (DataKey<?> key : storeDefinition.getKeys()) {
            Object value = getValue(map, key);
            KeyPanel<?> panel = panels.get(key.getName());
            uncheckedSet(panel, value);
        }

    }

    /**
     * Sets the value of a KeyPanel without type checking.
     *
     * @param <T>   the type of the KeyPanel
     * @param panel the KeyPanel
     * @param o     the value to set
     */
    @SuppressWarnings("unchecked")
    private static <T> void uncheckedSet(KeyPanel<T> panel, Object o) {
        panel.setValue((T) o);
    }

    /**
     * Retrieves the value for a DataKey from the given DataStore.
     *
     * @param map the DataStore
     * @param key the DataKey
     * @return the value for the DataKey
     */
    private static Object getValue(DataStore map, DataKey<?> key) {

        Optional<?> value = map.getTry(key);
        if (value.isPresent()) {
            return value.get();
        }

        // Return default value
        if (key.getDefault().isPresent()) {
            Object defaultValue = key.getDefault().get();
            SpecsLogs.msgInfo("Could not find a value for option '" + key.getName() + "', using default value '"
                    + defaultValue + "'");
            return defaultValue;
        }

        // Return decoder with input null
        if (key.getDecoder().isPresent()) {
            Object emptyValue = key.getDecoder().get().decode(null);
            SpecsLogs.msgLib("Could not find a value for option '" + key.getName() + "', using empty value");
            return emptyValue;
        }

        throw new RuntimeException("Could not get a value for key '" + key
                + "', please define a default value or a decoder that supports 'null' string as input");
    }

    /**
     * Collects information in all the panels and returns a DataStore with the
     * information.
     *
     * @return a DataStore containing the collected information
     */
    public DataStore getData() {
        DataStore dataStore = DataStore.newInstance(storeDefinition);

        for (KeyPanel<?> panel : panels.values()) {
            panel.store(dataStore);
        }

        return dataStore;
    }
}
