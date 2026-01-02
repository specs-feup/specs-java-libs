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
import java.util.Collections;

import org.suikasoft.jOptions.Datakey.DataKey;

/**
 * A read-only view of a DataStore.
 */
public interface DataView {

    /**
     * Returns the name of the data store.
     *
     * @return the name
     */
    String getName();

    /**
     * Returns the value mapped to the given key.
     *
     * @param key the key
     * @return the value mapped to the key
     */
    <T> T getValue(DataKey<T> key);

    /**
     * Returns the value mapped to the given key id.
     *
     * @param id the key id
     * @return the value mapped to the key id
     */
    Object getValueRaw(String id);

    /**
     * Returns the DataKeys that have values in this view.
     *
     * @return a collection of DataKeys with values
     */
    Collection<DataKey<?>> getDataKeysWithValues();

    /**
     * Returns the key ids that have values in this view.
     *
     * @return a collection of key ids with values
     */
    Collection<String> getKeysWithValues();

    /**
     * Returns the value mapped to the given DataKey, as an Object.
     *
     * @param key the DataKey
     * @return the value mapped to the key
     */
    default Object getValueRaw(DataKey<?> key) {
        return getValueRaw(key.getName());
    }

    /**
     * Checks if the store contains a value for the given key.
     *
     * @param key the key
     * @return true if the store contains a value for the key
     */
    <T> boolean hasValue(DataKey<T> key);

    /**
     * Returns a new DataView instance backed by the given DataStore.
     *
     * @param dataStore the DataStore
     * @return a new DataView instance
     */
    public static DataView newInstance(DataStore dataStore) {
        return new DefaultCleanSetup(dataStore);
    }

    /**
     * Returns an empty DataView instance.
     *
     * @return an empty DataView
     */
    public static DataView empty() {
        return new DataView() {
            @Override
            public <T> T getValue(DataKey<T> key) {
                return null;
            }

            @Override
            public String getName() {
                return "<empty>";
            }

            @Override
            public <T> boolean hasValue(DataKey<T> key) {
                return false;
            }

            @Override
            public Object getValueRaw(String id) {
                return null;
            }

            @Override
            public Collection<DataKey<?>> getDataKeysWithValues() {
                return Collections.emptyList();
            }

            @Override
            public Collection<String> getKeysWithValues() {
                return Collections.emptyList();
            }
        };
    }

    /**
     * Converts this DataView to a DataStore.
     *
     * @return a new DataStore
     */
    default DataStore toDataStore() {
        return DataStore.newInstance(this);
    }
}
