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

package org.suikasoft.jOptions.DataStore;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.suikasoft.jOptions.Datakey.CustomGetter;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.app.AppPersistence;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

/**
 * Abstract base class for DataStore implementations.
 *
 * <p>
 * This class provides a base implementation for DataStore, including value
 * storage, definition management, and persistence support.
 */
public abstract class ADataStore implements DataStore {

    private final String name;
    private final Map<String, Object> values;
    private StoreDefinition definition;
    private AppPersistence persistence;
    private File configFile;
    private boolean strict;

    /**
     * Constructs an ADataStore with the given name, values, and store definition.
     *
     * @param name       the name of the DataStore
     * @param values     the map of values
     * @param definition the store definition
     */
    protected ADataStore(String name, Map<String, Object> values,
            StoreDefinition definition) {

        if (definition != null) {
            // If names do not agree, create new StoreDefinition with new name
            if (!name.equals(definition.getName())) {
                definition = StoreDefinition.newInstance(name, definition.getKeys());
            }
        }

        this.name = name;
        this.values = values;
        strict = false;
        this.definition = definition;
    }

    /**
     * Constructs an ADataStore with the given name and another DataStore as source.
     *
     * @param name      the name of the DataStore
     * @param dataStore the source DataStore
     */
    public ADataStore(String name, DataStore dataStore) {
        this(name, new HashMap<>(), dataStore.getStoreDefinitionTry().orElse(null));
        set(dataStore);
    }

    /**
     * Constructs an ADataStore with the given StoreDefinition.
     *
     * @param storeDefinition the store definition
     */
    public ADataStore(StoreDefinition storeDefinition) {
        this(storeDefinition.getName(), new HashMap<>(), storeDefinition);
    }

    /**
     * Constructs an ADataStore with the given name.
     *
     * @param name the name of the DataStore
     */
    public ADataStore(String name) {
        this(name, new HashMap<>(), null);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + (strict ? 1231 : 1237);
        result = prime * result + ((values == null) ? 0 : values.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        // Compares name, values and strict flag
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ADataStore other = (ADataStore) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (strict != other.strict) {
            return false;
        }
        if (values == null) {
            return other.values == null;
        } else {
            return values.equals(other.values);
        }
    }

    @Override
    public void setStrict(boolean strict) {
        this.strict = strict;
    }

    @Override
    public Optional<StoreDefinition> getStoreDefinitionTry() {
        return Optional.ofNullable(definition);
    }

    @Override
    public void setStoreDefinition(StoreDefinition definition) {
        this.definition = definition;
    }

    @Override
    public <T, E extends T> ADataStore set(DataKey<T> key, E value) {
        Objects.requireNonNull(value, () -> "Tried to set a null value with key '" + key + "'. Use .remove() instead");

        T realValue = value;

        // Check if key has custom setter
        Optional<CustomGetter<T>> setter = key.getCustomSetter();
        if (setter.isPresent()) {
            realValue = setter.get().get(value, this);
        }

        // Stop if value is not compatible with class of key
        if (key.verifyValueClass() && !key.getValueClass().isInstance(realValue)) {
            throw new RuntimeException("Tried to add a value of type '" + realValue.getClass()
                    + "', with a key that supports '" + key.getValueClass() + "'");
        }

        setRaw(key.getName(), realValue);

        return this;
    }

    @Override
    public Optional<Object> setRaw(String key, Object value) {
        return Optional.ofNullable(values.put(key, value));
    }

    @Override
    public <T> Optional<T> remove(DataKey<T> key) {
        Optional<T> value = getTry(key);

        // If not present, there was already no value there
        if (value.isEmpty()) {
            return Optional.empty();
        }

        if (values.remove(key.getName()) == null) {
            throw new RuntimeException("There was no value mapping for key '" + key + "'");
        }

        return value;
    }

    @Override
    public String getName() {
        return getStoreDefinitionTry().map(StoreDefinition::getName).orElse(name);
    }

    @Override
    public <T> T get(DataKey<T> key) {

        Object valueRaw = values.get(key.getName());
        if (strict && valueRaw == null) {
            throw new RuntimeException(
                    "No value present in DataStore '" + getName() + "' " + " for key '" + key.getName() + "'");
        }

        T value = null;
        try {
            value = key.getValueClass().cast(valueRaw);
        } catch (Exception e) {
            throw new RuntimeException("Could not retrieve value from key " + key, e);
        }

        // If value is null, use default value
        if (value == null) {
            Optional<T> defaultValue = key.getDefault();
            if (defaultValue.isEmpty()) {
                throw new RuntimeException("No default value for key '" + key.getName() + "' in this object: " + this);
            }

            value = defaultValue.get();
            values.put(key.getName(), value);
        }

        // Check if key has custom getter
        Optional<CustomGetter<T>> getter = key.getCustomGetter();
        if (getter.isPresent()) {
            return getter.get().get(value, this);
        }

        return value;
    }

    @Override
    public Object get(String id) {
        return values.get(id);
    }

    @Override
    public <T> boolean hasValue(DataKey<T> key) {
        return values.get(key.getName()) != null;
    }

    @Override
    public Collection<String> getKeysWithValues() {
        return values.keySet();
    }

    @Override
    public String toString() {
        return toInlinedString();
    }

    @Override
    public DataStore setPersistence(AppPersistence persistence) {
        this.persistence = persistence;
        return this;
    }

    @Override
    public Optional<AppPersistence> getPersistence() {
        return Optional.ofNullable(persistence);
    }

    @Override
    public Optional<File> getConfigFile() {
        return Optional.ofNullable(configFile);
    }

    @Override
    public DataStore setConfigFile(File configFile) {
        this.configFile = configFile;
        return this;
    }

}
