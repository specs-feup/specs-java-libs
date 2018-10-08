/**
 * Copyright 2018 SPeCS.
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

package org.suikasoft.jOptions.DataStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;
import org.suikasoft.jOptions.storedefinition.StoreDefinitions;

public abstract class ADataClass<T extends DataClass<T>> implements DataClass<T> {

    private final DataStore data;

    public ADataClass(DataStore data) {
        this.data = data;
    }

    public ADataClass() {
        // this(DataStore.newInstance(getClass()));
        // this.data = DataStore.newInstance(getClass());
        this.data = DataStore.newInstance(StoreDefinitions.fromInterface(getClass()), false);
    }

    @Override
    public String getDataClassName() {
        return data.getName();
    }

    // public DataClass(T instance) {
    // this.data = DataStore.newInstance(getClass());
    // this.data.addAll(((DataClass<?>) instance).data);
    // }

    /* (non-Javadoc)
     * @see org.suikasoft.jOptions.DataStore.DataClass#get(org.suikasoft.jOptions.Datakey.DataKey)
     */
    @Override
    public <K> K get(DataKey<K> key) {
        return data.get(key);
    }

    /* (non-Javadoc)
     * @see org.suikasoft.jOptions.DataStore.DataClass#set(org.suikasoft.jOptions.Datakey.DataKey, E)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <K, E extends K> T set(DataKey<K> key, E value) {
        data.set(key, value);
        return (T) this;
    }

    /* (non-Javadoc)
     * @see org.suikasoft.jOptions.DataStore.DataClass#set(T)
     */
    @Override
    @SuppressWarnings("unchecked")
    public T set(T instance) {
        this.data.addAll(((ADataClass<?>) instance).data);
        return (T) this;
    }

    @Override
    public String toString() {
        return data.toString();
    }

    @Override
    public <VT> boolean hasValue(DataKey<VT> key) {
        return data.hasValue(key);
    }

    @Override
    public Collection<DataKey<?>> getDataKeysWithValues() {
        StoreDefinition storeDefinition = data.getStoreDefinition().get();

        List<DataKey<?>> keysWithValues = new ArrayList<>();
        for (String keyId : data.getKeysWithValues()) {
            keysWithValues.add(storeDefinition.getKey(keyId));
        }

        return keysWithValues;
    }

    // @Override
    // public DataStore getData() {
    // return data;
    // }

    /*
    public DataStore getData() {
        return data;
    }
    */
}
