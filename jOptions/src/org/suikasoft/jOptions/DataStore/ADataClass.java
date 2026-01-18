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

import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.providers.StringProvider;

/**
 * Abstract base class for DataClass implementations.
 *
 * <p>
 * This class provides a base implementation for data classes backed by a
 * DataStore, supporting locking and common get/set operations.
 *
 * @param <T> the type of the DataClass
 */
public abstract class ADataClass<T extends DataClass<T>> implements DataClass<T>, StringProvider {

    private final DataStore data;
    private boolean isLocked;

    /**
     * Constructs an ADataClass backed by the given DataStore.
     *
     * @param data the DataStore backing this DataClass
     * @throws IllegalArgumentException if data is null
     */
    public ADataClass(DataStore data) {
        if (data == null) {
            throw new IllegalArgumentException("DataStore cannot be null");
        }
        this.data = data;
        this.isLocked = false;
    }

    /**
     * Constructs an ADataClass with a new DataStore based on the class interface.
     */
    public ADataClass() {
        this.data = DataStore.newInstance(StoreDefinitions.fromInterface(getClass()), false);
    }

    /**
     * Returns the backing DataStore.
     *
     * @return the DataStore backing this DataClass
     */
    protected DataStore getDataStore() {
        return data;
    }

    /**
     * Locks this DataClass, preventing further modifications.
     *
     * @return this instance, locked
     */
    @SuppressWarnings("unchecked")
    public T lock() {
        this.isLocked = true;
        return (T) this;
    }

    /**
     * Returns the name of this DataClass.
     *
     * @return the name of this DataClass
     */
    @Override
    public String getDataClassName() {
        return data.getName();
    }

    /**
     * Returns an Optional containing the StoreDefinition, if present.
     *
     * @return an Optional with the StoreDefinition, or empty if not present
     */
    @Override
    public Optional<StoreDefinition> getStoreDefinitionTry() {
        return data.getStoreDefinitionTry()
                .map(def -> new StoreDefinitionBuilder(getDataClassName()).addDefinition(def).build());
    }

    /**
     * Gets the value for the given DataKey.
     *
     * @param key the DataKey
     * @param <K> the value type
     * @return the value for the key
     */
    @Override
    public <K> K get(DataKey<K> key) {
        return data.get(key);
    }

    /**
     * Sets the value for the given DataKey.
     *
     * @param key   the DataKey
     * @param value the value to set
     * @param <K>   the value type
     * @param <E>   the value type (extends K)
     * @return this instance
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

    /**
     * Sets all values from another DataClass instance.
     *
     * @param instance the instance to copy from
     * @return this instance
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

    /**
     * Checks if the given DataKey has a value.
     *
     * @param key  the DataKey
     * @param <VT> the value type
     * @return true if the key has a value, false otherwise
     */
    @Override
    public <VT> boolean hasValue(DataKey<VT> key) {
        return data.hasValue(key);
    }

    /**
     * Returns a collection of DataKeys that have values.
     *
     * @return a collection of DataKeys with values
     */
    @Override
    public Collection<DataKey<?>> getDataKeysWithValues() {
        Optional<StoreDefinition> storeDefinitionOpt = data.getStoreDefinitionTry();
        if (storeDefinitionOpt.isEmpty()) {
            SpecsLogs.warn("getDataKeysWithValues(): No StoreDefinition available");
            return new ArrayList<>();
        }

        StoreDefinition storeDefinition = storeDefinitionOpt.get();

        List<DataKey<?>> keysWithValues = new ArrayList<>();
        for (String keyId : data.getKeysWithValues()) {
            if (!storeDefinition.hasKey(keyId)) {
                SpecsLogs.info("getDataKeysWithValues(): found value with key that does not belong to this DataClass ('"
                        + keyId + "')");
                continue;
            }

            keysWithValues.add(storeDefinition.getKey(keyId));
        }

        return keysWithValues;
    }

    /**
     * Computes the hash code for this DataClass.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        for (var key : getDataKeysWithValues()) {
            result = prime * result + ((get(key) == null) ? 0 : get(key).hashCode());
        }

        return result;
    }

    /**
     * Checks if this DataClass is equal to another object.
     *
     * @param obj the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!getClass().isInstance(obj))
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

    /**
     * Returns the string representation of this DataClass.
     *
     * @return the string representation
     */
    @Override
    public String getString() {
        return toString();
    }

    /**
     * Returns the string representation of this DataClass.
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        return toInlinedString();
    }
}
