/**
 * Copyright 2020 SPeCS.
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

package org.suikasoft.jOptions.DataStore;

import java.util.Collection;
import java.util.Optional;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

/**
 * Abstract wrapper for DataClass implementations.
 *
 * <p>
 * This class wraps another DataClass and delegates all operations to it,
 * allowing extension or adaptation.
 *
 * @param <T> the type of the DataClass
 */
public abstract class DataClassWrapper<T extends DataClass<T>> implements DataClass<T> {

    private final DataClass<?> data;

    /**
     * Constructs a DataClassWrapper for the given DataClass.
     *
     * @param data the DataClass to wrap
     */
    public DataClassWrapper(DataClass<?> data) {
        this.data = data;
    }

    /**
     * Returns the wrapped DataClass instance.
     *
     * @return the wrapped DataClass
     */
    protected abstract T getThis();

    /**
     * Returns the name of the wrapped DataClass.
     *
     * @return the name of the wrapped DataClass
     */
    @Override
    public String getDataClassName() {
        return data.getDataClassName();
    }

    /**
     * Retrieves the value associated with the given key from the wrapped DataClass.
     *
     * @param <K> the type of the value
     * @param key the key to retrieve the value for
     * @return the value associated with the key
     */
    @Override
    public <K> K get(DataKey<K> key) {
        return data.get(key);
    }

    /**
     * Sets the value for the given key in the wrapped DataClass.
     *
     * @param <K>   the type of the value
     * @param <E>   the type of the value to set
     * @param key   the key to set the value for
     * @param value the value to set
     * @return the current instance
     */
    @Override
    public <K, E extends K> T set(DataKey<K> key, E value) {
        data.set(key, value);
        return getThis();
    }

    /**
     * Sets all values from the given instance into the wrapped DataClass.
     *
     * @param instance the instance to copy values from
     * @return the current instance
     */
    @Override
    public T set(T instance) {
        for (var key : instance.getDataKeysWithValues()) {
            var keyString = key.getKey();
            data.setValue(keyString, instance.getValue(keyString));
        }

        return getThis();
    }

    /**
     * Checks if the wrapped DataClass has a value for the given key.
     *
     * @param <VT> the type of the value
     * @param key  the key to check
     * @return true if the wrapped DataClass has a value for the key, false
     *         otherwise
     */
    @Override
    public <VT> boolean hasValue(DataKey<VT> key) {
        return data.hasValue(key);
    }

    /**
     * Retrieves all keys with values from the wrapped DataClass.
     *
     * @return a collection of keys with values
     */
    @Override
    public Collection<DataKey<?>> getDataKeysWithValues() {
        return data.getDataKeysWithValues();
    }

    /**
     * Attempts to retrieve the store definition of the wrapped DataClass.
     *
     * @return an optional containing the store definition, or empty if not
     *         available
     */
    @Override
    public Optional<StoreDefinition> getStoreDefinitionTry() {
        return data.getStoreDefinitionTry();
    }

    /**
     * Checks if the wrapped DataClass is closed.
     *
     * @return true if the wrapped DataClass is closed, false otherwise
     */
    @Override
    public boolean isClosed() {
        return data.isClosed();
    }

    /**
     * Returns a string representation of the wrapped DataClass.
     *
     * @return a string representation of the wrapped DataClass
     */
    @Override
    public String toString() {
        return toInlinedString();
    }
}
