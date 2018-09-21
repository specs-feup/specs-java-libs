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

import org.suikasoft.jOptions.DataStore.DataStoreContainer;
import org.suikasoft.jOptions.Datakey.DataKey;

/**
 * Default implementation of a {code CleanSetup}, backed-up by a {code CleanSetupBuilder}.
 * 
 * @author JoaoBispo
 *
 */
public class DefaultCleanSetup implements DataView, DataStoreContainer {

    private final DataStore data;

    public DefaultCleanSetup(DataStore data) {
        this.data = data;
    }

    @Override
    public String getName() {
        return data.getName();
    }

    @Override
    public <T> T getValue(DataKey<T> key) {
        return data.get(key);
    }

    /**
     * 
     * @return the DataStore backing the view
     */
    @Override
    public DataStore getDataStore() {
        return data;
    }

    @Override
    public String toString() {
        return data.toString();
    }

    // @Override
    // public Map<String, Object> getValuesMap() {
    // return data.getValuesMap();
    // }

    @Override
    public <T> boolean hasValue(DataKey<T> key) {
        return data.hasValue(key);
    }

    @Override
    public Object getValueRaw(String id) {
        return data.get(id);
    }

    @Override
    public Collection<DataKey<?>> keysWithValues() {
        return data.keysWithValues();
    }

    @Override
    public Collection<String> keysWithValuesRaw() {
        return data.getKeysWithValues();
    }
}
