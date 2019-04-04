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
import java.util.Optional;

import org.suikasoft.jOptions.Datakey.CustomGetter;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;
import org.suikasoft.jOptions.storedefinition.StoreDefinitionIndexes;

import pt.up.fe.specs.util.SpecsCheck;

/**
 * Implementation of DataStore that uses a List to store the data.
 * 
 * <p>
 * This implementation requires a StoreDefinition.
 * 
 * @author JoaoBispo
 *
 */
public class ListDataStore implements DataStore {

    private static final Map<StoreDefinition, StoreDefinitionIndexes> KEY_TO_INDEXES = new HashMap<>();

    private final StoreDefinition keys;
    private final List<Object> values;

    private boolean strict;

    public ListDataStore(StoreDefinition keys) {
        this.keys = keys;
        this.values = new ArrayList<>(keys.getKeys().size());

        // Fill array with nulls
        // for (int i = 0; i < keys.getKeys().size(); i++) {
        // this.values.add(null);
        // }

        this.strict = false;
    }

    private void ensureSize(int index) {
        if (index < values.size()) {
            return;
        }

        int missingElements = index - values.size() + 1;
        for (int i = 0; i < missingElements; i++) {
            this.values.add(null);
        }
    }

    private Object get(int index) {
        ensureSize(index);
        return values.get(index);
    }

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
            if (other.values != null)
                return false;
        } else if (!values.equals(other.values))
            return false;
        return true;
    }

    @Override
    public <T, E extends T> DataStore set(DataKey<T> key, E value) {
        SpecsCheck.checkNotNull(value, () -> "Tried to set a null value with key '" + key + "'. Use .remove() instead");

        // Stop if value is not compatible with class of key
        if (key.verifyValueClass() && !key.getValueClass().isInstance(value)) {
            throw new RuntimeException("Tried to add a value of type '" + value.getClass()
                    + "', with a key that supports '" + key.getValueClass() + "'");
        }

        setRaw(key.getName(), value);

        return this;
    }

    @Override
    public Optional<Object> setRaw(String key, Object value) {
        // Do not set key
        if (!keys.hasKey(key)) {
            return Optional.empty();
        }

        int index = toIndex(key);
        return Optional.ofNullable(set(index, value));
    }

    @Override
    public void setStrict(boolean value) {
        this.strict = value;
    }

    @Override
    public Optional<StoreDefinition> getStoreDefinition() {
        return Optional.of(keys);
    }

    @Override
    public void setStoreDefinition(StoreDefinition definition) {
        throw new RuntimeException(
                "This implementation does not support setting the StoreDefinition after instantiation");
    }

    @Override
    public String getName() {
        return keys.getName();
    }

    @Override
    public <T> T get(DataKey<T> key) {

        // boolean hasKey = keys.hasKey(key.getName());
        // Object valueRaw = hasKey ? values.get(toIndex(key)) : null;
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
            Optional<T> defaultValue = key.getDefault();
            if (!defaultValue.isPresent()) {
                throw new RuntimeException("No default value for key '" + key.getName() + "' in this object: " + this);
            }

            value = defaultValue.get();

            // Storing value, in case it is a mutable value (e.g., a list)
            // if (hasKey) {
            setRaw(key.getName(), value);
            // }

            // values.put(key.getName(), value);
        }

        // Check if key has custom getter
        Optional<CustomGetter<T>> getter = key.getCustomGetter();
        if (getter.isPresent()) {
            return getter.get().get(value, this);
        }

        return value;
    }

    @Override
    public <T> Optional<T> remove(DataKey<T> key) {
        Optional<T> value = getTry(key);

        // If not present, there was already no value there
        if (!value.isPresent()) {
            return Optional.empty();
        }

        set(toIndex(key), null);
        // if (values.remove(toIndex(key)) != value.get()) {
        // throw new RuntimeException("Removed wrong value");
        // }

        return value;
    }

    @Override
    public <T> boolean hasValue(DataKey<T> key) {
        // System.out.println("VALUES:" + values);
        // System.out.println("KEY:" + key);
        // System.out.println("KEy INDEX:" + toIndex(key));

        // Check if it has the index
        // if (!getIndexes().hasIndex(key)) {
        // return false;
        // }

        return get(toIndex(key)) != null;
    }

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

    private int toIndex(DataKey<?> key) {
        return toIndex(key.getName());
    }

    private int toIndex(String key) {
        StoreDefinitionIndexes indexes = getIndexes();

        return indexes.getIndex(key);
    }

    private StoreDefinitionIndexes getIndexes() {
        StoreDefinitionIndexes indexes = KEY_TO_INDEXES.get(keys);
        if (indexes == null) {
            indexes = new StoreDefinitionIndexes(keys);
            KEY_TO_INDEXES.put(keys, indexes);
        }
        return indexes;
    }

    @Override
    public Object get(String id) {
        return get(toIndex(id));
    }

    /**
     * This implementation is always closed.
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
