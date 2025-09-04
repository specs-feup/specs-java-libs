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

import java.awt.LayoutManager;
import java.io.Serial;
import java.util.Collection;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.gui.KeyPanel;
import org.suikasoft.jOptions.gui.panels.app.BaseSetupPanel;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

/**
 * Panel for editing and displaying a DataStore using a nested BaseSetupPanel.
 *
 * <p>This panel provides controls for loading and displaying values for a DataKey of type DataStore.
 *
 * @author Joao Bispo
 */
public class SetupPanel extends KeyPanel<DataStore> {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * INSTANCE VARIABLES
     */
    private JPanel currentOptionsPanel;

    private final BaseSetupPanel setupOptionsPanel;

    /**
     * Constructs a SetupPanel for the given DataKey, DataStore, and StoreDefinition.
     *
     * @param key the DataKey
     * @param data the DataStore
     * @param definition the StoreDefinition
     */
    public SetupPanel(DataKey<DataStore> key, DataStore data, StoreDefinition definition) {
        super(key, data);

        // Initialize objects
        setupOptionsPanel = new BaseSetupPanel(definition, data);

        currentOptionsPanel = null;

        LayoutManager layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);
        updateSetupOptions();
    }

    /**
     * Loads the several elements from a DataStore.
     *
     * @param value the DataStore to load
     * @param <ET> the type of DataStore
     */
    @Override
    public <ET extends DataStore> void setValue(ET value) {
        // Load values
        setupOptionsPanel.loadValues(value);
    }

    /**
     * Updates the setup options panel to reflect the current state.
     */
    private void updateSetupOptions() {
        if (currentOptionsPanel != null) {
            remove(currentOptionsPanel);
        }

        currentOptionsPanel = setupOptionsPanel;
        add(currentOptionsPanel);
        currentOptionsPanel.revalidate();
    }

    /**
     * Retrieves the current value of the DataStore.
     *
     * @return the current DataStore
     */
    @Override
    public DataStore getValue() {
        return setupOptionsPanel.getData();
    }

    /**
     * Retrieves the collection of KeyPanels contained in the setup options panel.
     *
     * @return a collection of KeyPanels
     */
    @Override
    public Collection<KeyPanel<?>> getPanels() {
        return setupOptionsPanel.getPanels().values();
    }

}
