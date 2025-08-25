/**
 * Copyright 2015 SPeCS.
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

package org.suikasoft.jOptions.Interfaces;

import java.util.Collection;

import org.suikasoft.jOptions.DataStore.DataStoreContainer;
import org.suikasoft.jOptions.Datakey.DataKey;

/**
 * Default implementation of a {@code DataView}, backed by a {@code DataStore}.
 */
public class DefaultCleanSetup implements DataView, DataStoreContainer {

    private final DataStore data;

    /**
     * Creates a new DefaultCleanSetup backed by the given DataStore.
     *
     * @param data the DataStore to back this view
     * @throws NullPointerException if data is null
     */
    public DefaultCleanSetup(DataStore data) {
        if (data == null) {
            throw new NullPointerException("DataStore cannot be null");
        }
        this.data = data;
    }

    /**
     * Retrieves the name of the DataStore backing this view.
     *
     * @return the name of the DataStore
     */
    @Override
    public String getName() {
        return data.getName();
    }

    /**
     * Retrieves the value associated with the given DataKey.
     *
     * @param key the DataKey whose value is to be retrieved
     * @param <T> the type of the value
     * @return the value associated with the given DataKey
     */
    @Override
    public <T> T getValue(DataKey<T> key) {
        return data.get(key);
    }

    /**
     * Returns the DataStore backing this view.
     *
     * @return the DataStore
     */
    @Override
    public DataStore getDataStore() {
        return data;
    }

    /**
     * Returns a string representation of the DataStore backing this view.
     *
     * @return a string representation of the DataStore
     */
    @Override
    public String toString() {
        return data.toString();
    }

    /**
     * Checks if the given DataKey has an associated value in the DataStore.
     *
     * @param key the DataKey to check
     * @param <T> the type of the value
     * @return {@code true} if the DataKey has an associated value, {@code false} otherwise
     */
    @Override
    public <T> boolean hasValue(DataKey<T> key) {
        return data.hasValue(key);
    }

    /**
     * Retrieves the raw value associated with the given identifier.
     *
     * @param id the identifier whose value is to be retrieved
     * @return the raw value associated with the given identifier
     */
    @Override
    public Object getValueRaw(String id) {
        return data.get(id);
    }

    /**
     * Retrieves all DataKeys that have associated values in the DataStore.
     *
     * @return a collection of DataKeys with associated values
     */
    @Override
    public Collection<DataKey<?>> getDataKeysWithValues() {
        return data.getDataKeysWithValues();
    }

    /**
     * Retrieves all keys that have associated values in the DataStore.
     *
     * @return a collection of keys with associated values
     */
    @Override
    public Collection<String> getKeysWithValues() {
        return data.getKeysWithValues();
    }
}
