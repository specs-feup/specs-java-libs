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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.suikasoft.jOptions.Datakey.CustomGetter;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;
import org.suikasoft.jOptions.storedefinition.StoreDefinitionIndexes;

/**
 * Implementation of DataStore that uses a List to store the data.
 *
 * <p>This implementation requires a StoreDefinition and stores values in a list indexed by the definition.
 *
 * @author JoaoBispo
 */
public class ListDataStore implements DataStore {

    private static final Map<StoreDefinition, StoreDefinitionIndexes> KEY_TO_INDEXES = new HashMap<>();

    private final StoreDefinition keys;
    private final List<Object> values;

    private boolean strict;

    /**
     * Constructs a ListDataStore with the given StoreDefinition.
     *
     * @param keys the StoreDefinition defining the keys
     */
    public ListDataStore(StoreDefinition keys) {
        this.keys = keys;
        this.values = new ArrayList<>(keys.getKeys().size());

        this.strict = false;
    }

    /**
     * Ensures the internal list has enough size to accommodate the given index.
     *
     * @param index the index to ensure size for
     */
    private void ensureSize(int index) {
        if (index < values.size()) {
            return;
        }

        int missingElements = index - values.size() + 1;
        for (int i = 0; i < missingElements; i++) {
            this.values.add(null);
        }
    }

    /**
     * Retrieves the value at the given index.
     *
     * @param index the index to retrieve the value from
     * @return the value at the given index
     */
    private Object get(int index) {
        ensureSize(index);
        return values.get(index);
    }

    /**
     * Sets the value at the given index.
     *
     * @param index the index to set the value at
     * @param value the value to set
     * @return the previous value at the given index
     */
    private Object set(int index, Object value) {
        ensureSize(index);
        return values.set(index, value);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((keys.getName() == null) ? 0 : keys.getName().hashCode());
        result = prime * result + ((values == null) ? 0 : values.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ListDataStore other = (ListDataStore) obj;
        if (keys.getName() == null) {
            if (other.keys.getName() != null)
                return false;
        } else if (!keys.getName().equals(other.keys.getName()))
            return false;
        if (values == null) {
            return other.values == null;
        } else {
            return values.equals(other.values);
        }
    }

    /**
     * Sets the value for the given DataKey.
     *
     * @param <T> the type of the value
     * @param <E> the type of the value to set
     * @param key the DataKey to set the value for
     * @param value the value to set
     * @return the current DataStore instance
     */
    @Override
    public <T, E extends T> DataStore set(DataKey<T> key, E value) {
        Objects.requireNonNull(value, () -> "Tried to set a null value with key '" + key + "'. Use .remove() instead");

        // Stop if value is not compatible with class of key
        if (key.verifyValueClass() && !key.getValueClass().isInstance(value)) {
            throw new RuntimeException("Tried to add a value of type '" + value.getClass()
                    + "', with a key that supports '" + key.getValueClass() + "'");
        }

        setRaw(key.getName(), value);

        return this;
    }

    /**
     * Sets the raw value for the given key.
     *
     * @param key the key to set the value for
     * @param value the value to set
     * @return an Optional containing the previous value, or empty if the key does not exist
     */
    @Override
    public Optional<Object> setRaw(String key, Object value) {
        // Do not set key
        if (!keys.hasKey(key)) {
            return Optional.empty();
        }

        int index = toIndex(key);
        return Optional.ofNullable(set(index, value));
    }

    /**
     * Sets whether the DataStore operates in strict mode.
     *
     * @param value true to enable strict mode, false otherwise
     */
    @Override
    public void setStrict(boolean value) {
        this.strict = value;
    }

    /**
     * Retrieves the StoreDefinition associated with this DataStore.
     *
     * @return an Optional containing the StoreDefinition
     */
    @Override
    public Optional<StoreDefinition> getStoreDefinitionTry() {
        return Optional.of(keys);
    }

    /**
     * Sets the StoreDefinition for this DataStore.
     *
     * @param definition the StoreDefinition to set
     * @throws RuntimeException if called, as this implementation does not support setting the StoreDefinition after instantiation
     */
    @Override
    public void setStoreDefinition(StoreDefinition definition) {
        throw new RuntimeException(
                "This implementation does not support setting the StoreDefinition after instantiation");
    }

    /**
     * Retrieves the name of this DataStore.
     *
     * @return the name of the DataStore
     */
    @Override
    public String getName() {
        return keys.getName();
    }

    /**
     * Retrieves the value for the given DataKey.
     *
     * @param <T> the type of the value
     * @param key the DataKey to retrieve the value for
     * @return the value associated with the DataKey
     */
    @Override
    public <T> T get(DataKey<T> key) {
        Object valueRaw = get(toIndex(key));
        if (strict && valueRaw == null) {
            throw new RuntimeException(
                    "No value present in DataStore '" + getName() + "' " + " for key '" + key.getName() + "'");
        }

        T value = null;
        try {
            value = key.getValueClass().cast(valueRaw);
        } catch (Exception e) {
            throw new RuntimeException("Could not retrive value from key " + key, e);
        }

        // If value is null, use default value
        if (value == null) {
            if (!key.hasDefaultValue()) {
                throw new RuntimeException("No default value for key '" + key.getName() + "' in this object: " + this);
            }

            Optional<T> defaultValue = key.getDefault();
            value = defaultValue.orElse(null);

            // Storing value, in case it is a mutable value (e.g., a list)
            setRaw(key.getName(), value);
        }

        // Check if key has custom getter
        Optional<CustomGetter<T>> getter = key.getCustomGetter();
        if (getter.isPresent()) {
            return getter.get().get(value, this);
        }

        return value;
    }

    /**
     * Removes the value associated with the given DataKey.
     *
     * @param <T> the type of the value
     * @param key the DataKey to remove the value for
     * @return an Optional containing the removed value, or empty if no value was present
     */
    @Override
    public <T> Optional<T> remove(DataKey<T> key) {
        Optional<T> value = getTry(key);

        // If not present, there was already no value there
        if (value.isEmpty()) {
            return Optional.empty();
        }

        set(toIndex(key), null);

        return value;
    }

    /**
     * Checks if the given DataKey has an associated value.
     *
     * @param <T> the type of the value
     * @param key the DataKey to check
     * @return true if the DataKey has an associated value, false otherwise
     */
    @Override
    public <T> boolean hasValue(DataKey<T> key) {
        return get(toIndex(key)) != null;
    }

    /**
     * Retrieves the keys that have associated values.
     *
     * @return a collection of keys with values
     */
    @Override
    public Collection<String> getKeysWithValues() {
        List<String> keysWithValues = new ArrayList<>();
        for (DataKey<?> key : keys.getKeys()) {
            if (get(toIndex(key)) != null) {
                keysWithValues.add(key.getName());
            }
        }

        return keysWithValues;
    }

    /**
     * Converts the given DataKey to its index in the StoreDefinition.
     *
     * @param key the DataKey to convert
     * @return the index of the DataKey
     */
    private int toIndex(DataKey<?> key) {
        return toIndex(key.getName());
    }

    /**
     * Converts the given key name to its index in the StoreDefinition.
     *
     * @param key the key name to convert
     * @return the index of the key name
     */
    private int toIndex(String key) {
        StoreDefinitionIndexes indexes = getIndexes();

        return indexes.getIndex(key);
    }

    /**
     * Retrieves the StoreDefinitionIndexes for the current StoreDefinition.
     *
     * @return the StoreDefinitionIndexes
     */
    private StoreDefinitionIndexes getIndexes() {
        StoreDefinitionIndexes indexes = KEY_TO_INDEXES.get(keys);
        if (indexes == null) {
            indexes = new StoreDefinitionIndexes(keys);
            KEY_TO_INDEXES.put(keys, indexes);
        }
        return indexes;
    }

    /**
     * Retrieves the value associated with the given key name.
     *
     * @param id the key name to retrieve the value for
     * @return the value associated with the key name
     */
    @Override
    public Object get(String id) {
        return get(toIndex(id));
    }

    /**
     * Checks if this DataStore is closed.
     *
     * @return true, as this implementation is always closed
     */
    @Override
    public boolean isClosed() {
        return true;
    }

    @Override
    public String toString() {
        return toInlinedString();
    }
}
