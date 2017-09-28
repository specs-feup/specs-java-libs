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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.suikasoft.jOptions.Datakey.CustomGetter;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

import com.google.common.base.Preconditions;

public abstract class ADataStore implements DataStore {

    // private final SimpleSetup data;
    private final String name;
    private final Map<String, Object> values;
    private StoreDefinition definition;

    // private SetupFile setupFile;
    private boolean strict;

    protected ADataStore(String name, Map<String, Object> values,
            StoreDefinition definition) {

        if (definition != null) {
            Preconditions.checkArgument(name == definition.getName(),
                    "Name of the DataStore (" + name + ") and of the definition (" + definition.getName()
                            + ") do not agree");
        }

        // data = null;
        this.name = name;
        this.values = values;
        strict = false;
        this.definition = definition;
        // setupFile = null;
    }

    public ADataStore(String name, DataStore dataStore) {
        // public ADataStore(String name, StoreDefinition definition) {
        // this(new SimpleSetup(name, dataStore), dataStore.getKeyMap());
        this(name, dataStore.getValuesMap(), dataStore.getStoreDefinition().orElse(null));
        // this(name, new HashMap<>(), definition);

        // data.setValues(dataStore);
        // keys.putAll(dataStore.getKeyMap());
    }

    public ADataStore(StoreDefinition storeDefinition) {
        // this(SimpleSetup.newInstance(storeDefinition), storeDefinition.getKeyMap());
        // this(storeDefinition.getName(), new LinkedHashMap<>(storeDefinition.getKeyMap()), new HashMap<>());
        // this(storeDefinition.getName(), storeDefinition.getKeyMap(), storeDefinition.getDefaultValues());
        this(storeDefinition.getName(), new HashMap<>(), storeDefinition);
    }

    public ADataStore(String name) {
        // this(new SimpleSetup(name), new HashMap<>());
        // this(name, new LinkedHashMap<>(), new HashMap<>());
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
        // Compares name, values and string flag
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
            if (other.values != null) {
                return false;
            }
        } else if (!values.equals(other.values)) {
            return false;
        }
        return true;
    }

    @Override
    public void setStrict(boolean strict) {
        this.strict = strict;
    }

    @Override
    public Optional<StoreDefinition> getStoreDefinition() {
        return Optional.ofNullable(definition);
    }

    @Override
    public void setStoreDefinition(StoreDefinition definition) {
        this.definition = definition;
    }

    /*
    private ADataStore(SimpleSetup data, Map<String, DataKey<?>> keys) {
    this.data = data;
    this.keys = keys;
    
    setupFile = null;
    
    throw new RuntimeException("Do not use this version");
    }
    */

    /*
    public ADataStore(String name, DataView setup) {
    //	this(new SimpleSetup(name, setup), setup.getKeyMap());
    
    
    	// data.setValues(setup);
    	// keys.putAll(setup.getKeyMap());
    }
    */
    @Override
    public <T, E extends T> Optional<T> set(DataKey<T> key, E value) {
        Preconditions.checkNotNull(value, "Tried to set a null value with key '" + key + "'. Use .remove() instead");

        // Do not replace key if it already exists
        // if (!keys.containsKey(key.getName())) {
        // keys.put(key.getName(), key);
        // }

        // return data.setValue(key, value);

        // Stop if value is not compatible with class of key
        if (!key.getValueClass().isInstance(value)) {
            throw new RuntimeException("Tried to add a value of type '" + value.getClass()
                    + "', with a key that supports '" + key.getValueClass() + "'");
        }

        Optional<Object> previousValue = setRaw(key.getName(), value);
        // Object previousValue = values.put(key.getName(), value);

        // if (previousValue == null) {
        if (!previousValue.isPresent()) {
            return Optional.empty();
        }

        return Optional.of(key.getValueClass().cast(previousValue.get()));
    }

    @Override
    public Optional<Object> setRaw(String key, Object value) {
        return Optional.ofNullable(values.put(key, value));
    }

    @Override
    public void set(DataStore setup) {
        values.putAll(setup.getValuesMap());
    }

    @Override
    public <T> Optional<T> remove(DataKey<T> key) {
        Optional<T> value = getTry(key);

        // If not present, there was already no value there
        if (!value.isPresent()) {
            return Optional.empty();
        }

        // Value is present, remove key and value from maps
        // if (keys.remove(key.getName()) == null) {
        // throw new RuntimeException("There was no key mapping for key '" + key + "'");
        // }

        if (values.remove(key.getName()) == null) {
            throw new RuntimeException("There was no value mapping for key '" + key + "'");
        }

        return value;
    }

    @Override
    public String getName() {
        // return data.getName();
        return name;
    }

    @Override
    public <T> T get(DataKey<T> key) {

        Object valueRaw = values.get(key.getName());
        if (strict && valueRaw == null) {
            throw new RuntimeException(
                    "No value present in DataStore '" + getName() + "' " + " for key '" + key.getName() + "'");
        }
        // DataKey<?> storedKey = keys.get(key.getName());
        // if (strict && storedKey == null) {
        // throw new RuntimeException("Key '" + key.getName() + "' is not present in DataStore '" + getName() + "'");
        // }

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
                throw new RuntimeException("No default value for key '" + key.getName() + "'");
            }

            value = defaultValue.get();
            values.put(key.getName(), value);
        }

        // T value = data.getValue(key);

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

    /**
     * 
     */
    @Override
    public Map<String, Object> getValuesMap() {
        return new HashMap<>(values);
    }

    @Override
    public Collection<String> getKeysWithValues() {
        return values.keySet();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DataStore (" + getName()).append(")\n");
        for (String key : values.keySet()) {
            builder.append(" - ").append(key).append(" : ").append(values.get(key)).append("\n");
        }
        return builder.toString();
    }

}
