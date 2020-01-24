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
import java.util.Optional;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;
import org.suikasoft.jOptions.storedefinition.StoreDefinitionBuilder;
import org.suikasoft.jOptions.storedefinition.StoreDefinitions;

public abstract class ADataClass<T extends DataClass<T>> implements DataClass<T> {

    private final DataStore data;
    private boolean isLocked;

    public ADataClass(DataStore data) {
        this.data = data;
        this.isLocked = false;
    }

    public ADataClass() {
        // this(DataStore.newInstance(getClass()));
        // this.data = DataStore.newInstance(getClass());

        // Cannot use previous Constructor because we cannot call 'getClass()' from whitin 'this(...)'
        this.data = DataStore.newInstance(StoreDefinitions.fromInterface(getClass()), false);
    }

    protected DataStore getDataStore() {
        return data;
    }

    @SuppressWarnings("unchecked")
    public T lock() {
        this.isLocked = true;
        return (T) this;
    }

    @Override
    public String getDataClassName() {
        return data.getName();
    }

    @Override
    public Optional<StoreDefinition> getStoreDefinition() {
        return data.getStoreDefinition()
                .map(def -> new StoreDefinitionBuilder(getDataClassName()).addDefinition(def).build());
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
        if (isLocked) {
            throw new RuntimeException("Instance of DataClass '" + getClass() + "' is locked! Cannot be changed");
        }

        data.set(key, value);
        return (T) this;
    }

    /* (non-Javadoc)
     * @see org.suikasoft.jOptions.DataStore.DataClass#set(T)
     */
    @Override
    @SuppressWarnings("unchecked")
    public T set(T instance) {
        if (isLocked) {
            throw new RuntimeException("Instance of DataClass '" + getClass() + "' is locked! Cannot be changed");
        }

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        for (var key : getDataKeysWithValues()) {
            result = prime * result + ((get(key) == null) ? 0 : get(key).hashCode());
        }

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass().isInstance(obj.getClass()))
            return false;
        ADataClass<?> other = getClass().cast(obj);

        // Check if has the same datakeys with values
        if (!other.getDataKeysWithValues().equals(getDataKeysWithValues())) {
            return false;
        }

        // Check the values of the keys
        for (var key : getDataKeysWithValues()) {
            if (get(key) == null) {
                if (other.get(key) != null)
                    return false;
            } else if (!get(key).equals(other.get(key)))
                return false;
        }

        return true;
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
