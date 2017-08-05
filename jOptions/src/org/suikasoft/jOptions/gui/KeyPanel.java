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

import java.util.Collection;
import java.util.Collections;

import javax.swing.JPanel;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;

/**
 * A GUI panel that returns a value of a type.
 * 
 * @author João Bispo
 *
 * @param <T>
 */
public abstract class KeyPanel<T> extends JPanel {

    private final DataKey<T> key;
    private final DataStore data;

    // private T value;
    // private final long lastTime;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected KeyPanel(DataKey<T> key, DataStore data) {
	this.key = key;
	this.data = data;
	// this.value = value;

	// this.lastTime = -1;
    }

    /**
     * Creates a panel using the key's default value.
     * <p>
     * Will throw an exception if key does not have a default value set.
     * 
     * @param key
     */
    // protected KeyPanel(DataKey<T> key) {
    // this(key, key.getDefaultValueV2()
    // .orElseThrow(() -> new RuntimeException("No default defined for key '" + key.getName() + "'")));
    // }

    /**
     * 
     * @return the current value that panel has.
     */
    public abstract T getValue();
    // return value;
    // }

    public DataKey<T> getKey() {
	return this.key;
    }

    public DataStore getData() {
	return data;
    }

    /**
     * Stores the value in the panel in the given DataStore, using the corresponding key.
     * 
     * @param data
     */
    public void store(DataStore data) {
	data.set(getKey(), getValue());
    }

    /**
     * Updates the Panel with the given value
     *
     * @param option
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
     * @return
     */
    public Collection<KeyPanel<?>> getPanels() {
	return Collections.emptyList();
    }
}
