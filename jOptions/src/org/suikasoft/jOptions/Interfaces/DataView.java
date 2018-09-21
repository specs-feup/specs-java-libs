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
 * specific language governing permissions and limitations under the License. under the License.
 */

package org.suikasoft.jOptions.Interfaces;

import java.util.Collection;
import java.util.Collections;

import org.suikasoft.jOptions.Datakey.DataKey;

/**
 * 
 * A read-only view of a DataStore.
 * 
 * @author JoaoBispo
 * @see DefaultCleanSetup
 *
 */

public interface DataView {

    /**
     * The name of the data store.
     * 
     * @return
     */
    String getName();

    /**
     * Returns the value mapped to the given key.
     * 
     * @param key
     * @return
     */
    <T> T getValue(DataKey<T> key);

    Object getValueRaw(String id);

    Collection<DataKey<?>> keysWithValues();

    default Object getValueRaw(DataKey<?> key) {
        return getValueRaw(key.getName());
    }

    /**
     * 
     * @param key
     * @return true if the store contains a value for the given key
     */
    <T> boolean hasValue(DataKey<T> key);

    /**
     * 
     * @return the objects mapped to the key ids
     */
    // Map<String, Object> getValuesMap();

    public static DataView newInstance(DataStore dataStore) {
        return new DefaultCleanSetup(dataStore);
    }

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

            // @Override
            // public Map<String, Object> getValuesMap() {
            // return Collections.emptyMap();
            // }

            @Override
            public <T> boolean hasValue(DataKey<T> key) {
                return false;
            }

            @Override
            public Object getValueRaw(String id) {
                return null;
            }

            @Override
            public Collection<DataKey<?>> keysWithValues() {
                return Collections.emptyList();
            }
        };
    }

    default DataStore toDataStore() {
        return DataStore.newInstance(this);
    }
}
