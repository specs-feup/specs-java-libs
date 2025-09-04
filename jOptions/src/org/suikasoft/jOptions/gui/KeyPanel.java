/**
 * Copyright 2016 SPeCS.
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

package org.suikasoft.jOptions.gui;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;

import javax.swing.JPanel;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;

/**
 * A GUI panel that returns a value of a type for a DataKey.
 *
 * <p>This abstract class provides the base for panels that interact with DataKeys and DataStores in the GUI.
 *
 * @param <T> the type of value handled by the panel
 */
public abstract class KeyPanel<T> extends JPanel {

    private final DataKey<T> key;
    private final DataStore data;

    /**
     * 
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a KeyPanel for the given DataKey and DataStore.
     *
     * @param key the DataKey
     * @param data the DataStore
     */
    protected KeyPanel(DataKey<T> key, DataStore data) {
        this.key = key;
        this.data = data;
    }

    /**
     * Returns the current value that the panel has.
     *
     * @return the current value
     */
    public abstract T getValue();

    /**
     * Returns the DataKey associated with this panel.
     *
     * @return the DataKey
     */
    public DataKey<T> getKey() {
        return this.key;
    }

    /**
     * Returns the DataStore associated with this panel.
     *
     * @return the DataStore
     */
    public DataStore getData() {
        return data;
    }

    /**
     * Stores the value in the panel in the given DataStore, using the corresponding key.
     *
     * @param data the DataStore to store the value in
     */
    public void store(DataStore data) {
        data.set(getKey(), getValue());
    }

    /**
     * Updates the panel with the given value.
     *
     * @param value the value to set
     * @param <ET> the type of value (extends T)
     */
    public abstract <ET extends T> void setValue(ET value);

    /**
     * The default label name is the name of the key.
     *
     * @return the default label name
     */
    protected String getDefaultLabelName() {
        return getKey().getName();
    }

    /**
     * The nested panels this panel has, if any. By default, returns an empty list.
     * 
     * @return a collection of nested panels
     */
    public Collection<KeyPanel<?>> getPanels() {
        return Collections.emptyList();
    }
}
