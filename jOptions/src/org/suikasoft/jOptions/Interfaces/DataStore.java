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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.suikasoft.jOptions.DataStore.DataClass;
import org.suikasoft.jOptions.DataStore.DataClassUtils;
import org.suikasoft.jOptions.DataStore.DataStoreContainer;
import org.suikasoft.jOptions.DataStore.ListDataStore;
import org.suikasoft.jOptions.DataStore.SimpleDataStore;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;
import org.suikasoft.jOptions.storedefinition.StoreDefinitionProvider;
import org.suikasoft.jOptions.storedefinition.StoreDefinitions;

import com.google.common.base.Preconditions;

import pt.up.fe.specs.util.SpecsCheck;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * A key-value store for arbitrary objects, with type-safe keys.
 * 
 * @author JoaoBispo
 * @see SimpleDataStore
 *
 */
public interface DataStore extends DataClass<DataStore> {

    // <T, E extends T> Optional<T> set(DataKey<T> key, E value);
    @Override
    <T, E extends T> DataStore set(DataKey<T> key, E value);

    default <T, E extends T> DataStore put(DataKey<T> key, E value) {
        set(key, value);
        return this;
    }

    /**
     * Only sets the value if there is not a value yet for the given key.
     * 
     * @param key
     * @param value
     */
    default <T, E extends T> void setIfNotPresent(DataKey<T> key, E value) {
        if (hasValue(key)) {
            return;
        }

        set(key, value);
    }

    /**
     * Helper method when we do not have the key, only its id.
     * 
     * @param key
     * @param value
     * @return
     */
    Optional<Object> setRaw(String key, Object value);

    /**
     * Helper method for when the type of the DataKey is unknown (e.g., when working with DataKeys in bulk).
     * 
     * @param key
     * @param value
     * @return
     */
    // Check is being done manually
    @SuppressWarnings("unchecked")
    // default Optional<Object> setRaw(DataKey<?> key, Object value) {
    default DataStore setRaw(DataKey<?> key, Object value) {
        // Do not allow the storage of other DataStores
        // if (value instanceof DataStore) {
        // LoggingUtils.msgWarn("Key '" + key.getName()
        // + "' uses DataStore as its type. Storing directly DataStore objects is discouraged");
        // // throw new RuntimeException("Storing other DataStore objects directly is not allowed");
        // }

        // Check value is instance compatible with key
        if (!key.getValueClass().isInstance(value)) {
            throw new RuntimeException("Value '" + value + "' of type '" + value.getClass()
                    + "' is not compatible with key '" + key + "'");
        }

        return set((DataKey<Object>) key, value);
    }

    /**
     * Uses the decoder in the key to decode the string. In no decoder is found, throws an exception.
     * 
     * @param key
     * @param value
     * @return
     */
    // default <T> Optional<T> setString(DataKey<T> key, String value) {
    default <T> DataStore setString(DataKey<T> key, String value) {
        if (!key.getDecoder().isPresent()) {
            throw new RuntimeException("No decoder set for key '" + key + "'");
        }

        return set(key, key.getDecoder().get().decode(value));
    }

    /**
     * Adds the values of the given setup.
     * <p>
     * TODO: Change to DataStore
     * 
     * @param dataStore
     */
    // @Override
    // DataStore set(DataStore dataStore);
    @Override
    default DataStore set(DataStore dataStore) {

        StoreDefinition definition = getStoreDefinitionTry().orElse(null);

        // for (DataKey<?> key : dataStore.keysWithValues()) {
        for (String key : dataStore.getKeysWithValues()) {

            // Check if key exists
            if (definition != null && isClosed() && !definition.hasKey(key)) {
                SpecsLogs.debug(
                        "set(DataStore): value with key '" + key + "' not part of this definition: " + definition);
                continue;
            }

            setRaw(key, dataStore.get(key));
        }

        return this;
    }

    /**
     * Configures the current DataStore to behave strictly, i.e., can only access values with keys that have been added
     * before.
     * 
     * <p>
     * For instance, if a get() is performed for a key that was not used before, or if the value is null, instead of
     * returning a default value, throws an exception.
     * 
     * @param value
     */
    void setStrict(boolean value);

    /**
     * Sets a StoreDefinition for this DataStore.
     * 
     * <p>
     * Can only set a StoreDefinition once, subsequent calls to this function will throw an exception.
     * 
     * @param storeDefinition
     */
    // void setStoreDefinition(StoreDefinition storeDefinition);

    /**
     * 
     * @return an Optional containing a StoreDefinition, if defined
     */
    // Optional<StoreDefinition> getStoreDefinition();

    void setStoreDefinition(StoreDefinition definition);

    default void setDefinition(Class<?> aClass) {
        setStoreDefinition(StoreDefinitions.fromInterface(aClass));
    }

    /**
     * Adds a new key and value.
     * 
     * <p>
     * Throws an exception if there already is a value for the given key.
     * 
     * @param key
     */
    default <T, E extends T> DataStore add(DataKey<T> key, E value) {
        Preconditions.checkArgument(key != null);
        SpecsCheck.checkArgument(!hasValue(key), () -> "Attempting to add value already in PassData: " + key);

        set(key, value);

        return this;
    }

    default DataStore addAll(DataView values) {

        // for (DataKey<?> key : values.keysWithValues()) {
        for (String key : values.getKeysWithValues()) {
            setRaw(key, values.getValueRaw(key));
        }

        // Map<String, Object> rawValues = values.getValuesMap();
        //
        // for (String key : rawValues.keySet()) {
        // DataKey<Object> datakey = KeyFactory.object(key, Object.class);
        // set(datakey, rawValues.get(key));
        // }

        return this;
        // for (DataKey<?> key : values.getKeys()) {
        // add((DataKey<Object>) key, values.getValue(key));
        // }
    }

    /**
     * Sets all values of the given DataStore in the given DataStore.
     * 
     * @param values
     * @return
     */
    default DataStore addAll(DataStore values) {
        addAll(DataView.newInstance(values));

        return this;
    }

    default DataStore addAll(DataClass<?> values) {

        for (DataKey<?> key : values.getDataKeysWithValues()) {
            Object value = values.get(key);
            setRaw(key, value);
        }

        return this;
    }

    /**
     * Replaces the value of an existing key.
     * 
     * <p>
     * Throws an exception if there is no value for the given key.
     * 
     * @param key
     */
    default <T, E extends T> void replace(DataKey<T> key, E value) {
        Preconditions.checkArgument(key != null);
        Preconditions.checkArgument(hasValue(key), "Attempting to replace value for key not yet in PassData: " + key);

        set(key, value);
    }

    // default StoreDefinition getDefinition() {
    // return StoreDefinition.newInstance(getName(), getKeys());
    // }

    /**
     * Convenience method for interfacing with DataViews.
     * 
     * @param setup
     */
    /*
    default void setValues(DataView setup) {
    if (setup instanceof DataStoreContainer) {
        setValues(((DataStoreContainer) setup).getDataStore());
        return;
    }
    
    setValues(new SimpleDataStore("temp_setup", setup));
    }
     */

    /**
     * The name of the setup.
     * 
     * @return
     */
    String getName();

    @Override
    default String getDataClassName() {
        return getName();
    }

    /**
     * Returns the value associated with the given key. If there is no value for the key, returns the default value
     * defined by the key.
     * 
     * <p>
     * This method always returns a value, and throws an exception if it can't.
     * 
     * 
     * This method is safe, when using DataKey<T>, it can only store objects of the key type.
     * 
     * @param key
     * @return
     */
    @Override
    <T> T get(DataKey<T> key);

    /**
     * Removes a value from the DataStore.
     * 
     * @param key
     * @return
     */
    <T> Optional<T> remove(DataKey<T> key);

    /**
     * 
     * @param key
     * @return true, if it contains a non-null value for the given key, not considering default values
     */
    @Override
    <T> boolean hasValue(DataKey<T> key);

    // default boolean hasValueRaw(String key) {
    // return getValuesMap().get(key) != null;
    // }

    /**
     * Tries to return a value from the DataStore.
     * 
     * <p>
     * Does not use default values. If the key is not in the map, or there is no value mapped to the given key, returns
     * an empty Optional.
     * 
     * @param key
     * @return
     */
    default <T> Optional<T> getTry(DataKey<T> key) {
        if (!hasValue(key)) {
            return Optional.empty();
        }

        return Optional.of(get(key));
    }

    // Map<String, Object> getValuesMap();

    default Collection<Object> getValues() {
        List<Object> values = new ArrayList<>();

        for (String key : getKeysWithValues()) {
            values.add(get(key));
        }

        return values;
    }

    /**
     * 
     * @param id
     * @return the value mapped to the given key id, or null if no value is mapped
     */
    Object get(String id);

    /**
     * Helper method for when the type of the DataKey is unknown (e.g., when working with DataKeys in bulk).
     * 
     * @param key
     * @param value
     * @return
     */
    // Check is being done manually
    @SuppressWarnings("unchecked")
    default Object getRaw(DataKey<?> key) {

        Object value = get((DataKey<Object>) key);

        // Check value is instance compatible with key
        if (!key.getValueClass().isInstance(value)) {
            throw new RuntimeException("Value '" + value + "' of type '" + value.getClass()
                    + "' is not compatible with key '" + key + "'");
        }

        return value;
    }

    // default Object get(String id) {
    // return getValuesMap().get(id);
    // }

    /**
     * 
     * 
     * 
     * @return All the keys in the DataStore that are mapped to a value
     */
    Collection<String> getKeysWithValues();

    /**
     * If DataStore has a StoreDefinition, uses the copy function defined in the DataKeys.
     * 
     * <p>
     * Otherwise, throws exception.
     * 
     * @return
     */
    default DataStore copy() {
        // Otherwise, tries to use the
        // object copy constructor.
        if (!getStoreDefinitionTry().isPresent()) {
            throw new RuntimeException("No StoreDefinition defined, cannot copy. DataStore: " + this);
            // DataStore copy = DataStore.newInstance(getName());
            //
            // for (String key : getKeysWithValues()) {
            // copy.setRaw(key, SpecsSystem.copy(get(key)));
            // }
            //
            // return copy;
        }

        StoreDefinition def = getStoreDefinitionTry().get();
        // .orElseThrow(
        // () -> new RuntimeException("Can only copy DataStores that have defined a StoreDefinition"));

        // Create new DataStore with same StoreDefinition
        DataStore copy = DataStore.newInstance(def, isClosed());

        for (DataKey<?> key : def.getKeys()) {
            // Skip keys without values
            if (!hasValue(key)) {
                continue;
            }

            Object value = get(key.getName());
            copy.setRaw(key, key.copyRaw(value));
        }

        return copy;
    }

    /*
     * CONSTRUCTORS
     */
    /**
     * 
     * @param name
     * @return
     */
    public static DataStore newInstance(String name) {
        return new SimpleDataStore(name);
    }

    public static DataStore newInstance(StoreDefinition storeDefinition) {
        return newInstance(storeDefinition, false);
    }

    /**
     * 
     * @param storeDefinition
     * @param closed
     *            if true, no other keys besides the ones defined in the StoreDefinition can be added. This allows a
     *            more efficient implementation of DataStore to be used.
     * @return
     */
    public static DataStore newInstance(StoreDefinition storeDefinition, boolean closed) {
        if (closed) {
            return new ListDataStore(storeDefinition);
        } else {
            return new SimpleDataStore(storeDefinition);
        }

    }

    public static DataStore newInstance(StoreDefinitionProvider storeProvider) {
        return newInstance(storeProvider.getStoreDefinition());
    }

    public static DataStore newInstance(DataView dataView) {
        if (dataView instanceof DataStoreContainer) {
            return ((DataStoreContainer) dataView).getDataStore();
        }

        throw new RuntimeException("Not implemented yet.");
    }

    public static DataStore newInstance(String name, DataStore dataStore) {
        return new SimpleDataStore(name, dataStore);
    }

    /**
     * Creates a DataStore from all the public static DataKeys that can be found in the class.
     * 
     * @param aClass
     * @return
     */
    public static DataStore newInstance(Class<?> aClass) {
        return newInstance(StoreDefinitions.fromInterface(aClass));
    }

    @Override
    default String toInlinedString() {
        Collection<String> keys = getKeysWithValues();

        //
        // getStoreDefinition().map(def -> def.getKeys().stream()
        // .filter(key -> hasValue(key))
        // .map(key -> key.getName())
        // .collect((Collection<String>) Collectors.toList()))
        // .orElse(getKeysWithValues());

        if (getStoreDefinitionTry().isPresent()) {
            keys = getStoreDefinitionTry().get().getKeys().stream()
                    .filter(key -> hasValue(key))
                    .map(key -> key.getName())
                    .collect(Collectors.toList());
        }

        // return getKeysWithValues().stream()
        /*
        StringBuilder builder = new StringBuilder();
        builder.append(getName() + " [");
        boolean firstTime = true;
        for (String key : keys) {
            if (firstTime) {
                firstTime = false;
            } else {
                builder.append(", ");
            }
        
            Object object = get(key);
            if (object instanceof DataClass) {
                ((DataClass<?>) object).
                continue;
            }
        
            String keyString = key + ":" + get(key);
            builder.append(keyString);
        }
        builder.append("]");
        
        return builder.toString();
        */

        return keys.stream()
                .map(key -> key + ": " + DataClassUtils.toString(get(key)))
                .collect(Collectors.joining(", ", getName() + " [", "]"));

    }

    @Override
    default Collection<DataKey<?>> getDataKeysWithValues() {
        StoreDefinition storeDefinition = getStoreDefinitionTry().orElse(null);

        if (storeDefinition == null) {
            // SpecsLogs.msgInfo(
            // "keysWithValues: current DataStore does not have a StoreDefinition, returning empty list");
            return Collections.emptyList();
        }

        List<DataKey<?>> keysWithValues = new ArrayList<>();
        for (String keyId : getKeysWithValues()) {
            keysWithValues.add(storeDefinition.getKey(keyId));
        }

        return keysWithValues;
    }

}
