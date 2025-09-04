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
 * specific language governing permissions and limitations under the License.
 */

package org.suikasoft.jOptions.Interfaces;

import java.io.File;
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
import org.suikasoft.jOptions.app.AppPersistence;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;
import org.suikasoft.jOptions.storedefinition.StoreDefinitionProvider;
import org.suikasoft.jOptions.storedefinition.StoreDefinitions;

import com.google.common.base.Preconditions;

import pt.up.fe.specs.util.SpecsCheck;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * A key-value store for arbitrary objects, with type-safe keys.
 *
 * <p>Implements {@link DataClass} for DataStore-specific operations.
 */
public interface DataStore extends DataClass<DataStore> {

    /**
     * Sets the value for the given key.
     *
     * @param key the key
     * @param value the value to set
     * @return this DataStore
     */
    @Override
    <T, E extends T> DataStore set(DataKey<T> key, E value);

    /**
     * Helper method for setting a value, returns this DataStore.
     *
     * @param key the key
     * @param value the value to set
     * @return this DataStore
     */
    default <T, E extends T> DataStore put(DataKey<T> key, E value) {
        set(key, value);
        return this;
    }

    /**
     * Only sets the value if there is not a value yet for the given key.
     *
     * @param key the key
     * @param value the value to set if not present
     */
    default <T, E extends T> void setIfNotPresent(DataKey<T> key, E value) {
        if (hasValue(key)) {
            return;
        }
        set(key, value);
    }

    /**
     * Sets a value for a key by its string id.
     *
     * @param key the key id
     * @param value the value
     * @return an Optional containing the previous value, if any
     */
    Optional<Object> setRaw(String key, Object value);

    /**
     * Sets a value for a DataKey when the type is unknown.
     *
     * @param key the DataKey
     * @param value the value
     * @return this DataStore
     */
    @SuppressWarnings("unchecked")
    default DataStore setRaw(DataKey<?> key, Object value) {
        if (!key.getValueClass().isInstance(value)) {
            throw new IllegalArgumentException("Value is not of the correct type for key '" + key.getName() + "'");
        }
        set((DataKey<Object>) key, value);
        return this;
    }

    /**
     * Sets a value for a DataKey using its decoder to decode a string.
     *
     * @param key the DataKey
     * @param value the string value to decode and set
     * @return this DataStore
     */
    default <T> DataStore setString(DataKey<T> key, String value) {
        if (key.getDecoder().isEmpty()) {
            throw new RuntimeException("No decoder set for key '" + key + "'");
        }
        return set(key, key.getDecoder().get().decode(value));
    }

    /**
     * Adds the values of the given DataStore to this DataStore.
     *
     * @param dataStore the DataStore to add values from
     * @return this DataStore
     */
    @Override
    default DataStore set(DataStore dataStore) {
        StoreDefinition definition = getStoreDefinitionTry().orElse(null);

        for (String key : dataStore.getKeysWithValues()) {
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
     * Configures the current DataStore to behave strictly.
     *
     * <p>Strict mode means that only keys that have been added before can be accessed.
     *
     * @param value true to enable strict mode, false to disable
     */
    void setStrict(boolean value);

    /**
     * Sets a StoreDefinition for this DataStore.
     *
     * <p>Can only set a StoreDefinition once, subsequent calls to this function will throw an exception.
     *
     * @param definition the StoreDefinition to set
     */
    void setStoreDefinition(StoreDefinition definition);

    /**
     * Sets a StoreDefinition for this DataStore using a class.
     *
     * @param aClass the class to derive the StoreDefinition from
     */
    default void setDefinition(Class<?> aClass) {
        setStoreDefinition(StoreDefinitions.fromInterface(aClass));
    }

    /**
     * Adds a new key and value to the DataStore.
     *
     * <p>Throws an exception if there already is a value for the given key.
     *
     * @param key the key
     * @param value the value to add
     * @return this DataStore
     */
    default <T, E extends T> DataStore add(DataKey<T> key, E value) {
        Preconditions.checkArgument(key != null);
        SpecsCheck.checkArgument(!hasValue(key), () -> "Attempting to add value already in PassData: " + key);
        set(key, value);
        return this;
    }

    /**
     * Adds all values from a DataView to this DataStore.
     *
     * @param values the DataView to add values from
     * @return this DataStore
     */
    default DataStore addAll(DataView values) {
        for (String key : values.getKeysWithValues()) {
            setRaw(key, values.getValueRaw(key));
        }
        return this;
    }

    /**
     * Adds all values from another DataStore to this DataStore.
     *
     * @param values the DataStore to add values from
     * @return this DataStore
     */
    default DataStore addAll(DataStore values) {
        addAll(DataView.newInstance(values));
        return this;
    }

    /**
     * Adds all values from a DataClass to this DataStore.
     *
     * @param values the DataClass to add values from
     * @return this DataStore
     */
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
     * <p>Throws an exception if there is no value for the given key.
     *
     * @param key the key
     * @param value the new value to set
     */
    default <T, E extends T> void replace(DataKey<T> key, E value) {
        Preconditions.checkArgument(key != null);
        Preconditions.checkArgument(hasValue(key), "Attempting to replace value for key not yet in PassData: " + key);
        set(key, value);
    }

    /**
     * Returns the name of the DataStore.
     *
     * @return the name of the DataStore
     */
    String getName();

    @Override
    default String getDataClassName() {
        return getName();
    }

    /**
     * Returns the value associated with the given key.
     *
     * <p>If there is no value for the key, returns the default value defined by the key.
     *
     * @param key the key
     * @return the value associated with the key
     */
    @Override
    <T> T get(DataKey<T> key);

    /**
     * Removes a value from the DataStore.
     *
     * @param key the key
     * @return an Optional containing the removed value, if any
     */
    <T> Optional<T> remove(DataKey<T> key);

    /**
     * Checks if the DataStore contains a non-null value for the given key.
     *
     * @param key the key
     * @return true if the DataStore contains a non-null value for the key, false otherwise
     */
    @Override
    <T> boolean hasValue(DataKey<T> key);

    /**
     * Tries to return a value from the DataStore.
     *
     * <p>Does not use default values. If the key is not in the map, or there is no value mapped to the given key,
     * returns an empty Optional.
     *
     * @param key the key
     * @return an Optional containing the value, if present
     */
    default <T> Optional<T> getTry(DataKey<T> key) {
        if (!hasValue(key)) {
            return Optional.empty();
        }
        return Optional.of(get(key));
    }

    /**
     * Returns the value mapped to the given key id, or null if no value is mapped.
     *
     * @param id the key id
     * @return the value mapped to the key id, or null if no value is mapped
     */
    Object get(String id);

    /**
     * Returns all the keys in the DataStore that are mapped to a value.
     *
     * @return a collection of keys with values
     */
    Collection<String> getKeysWithValues();

    /**
     * Creates a copy of this DataStore.
     *
     * <p>If the DataStore has a StoreDefinition, uses the copy function defined in the DataKeys.
     *
     * @return a copy of this DataStore
     */
    default DataStore copy() {
        if (getStoreDefinitionTry().isEmpty()) {
            throw new RuntimeException("No StoreDefinition defined, cannot copy. DataStore: " + this);
        }

        StoreDefinition def = getStoreDefinitionTry().get();
        DataStore copy = DataStore.newInstance(def, isClosed());

        for (DataKey<?> key : def.getKeys()) {
            if (!hasValue(key)) {
                continue;
            }

            Object value = get(key.getName());
            copy.setRaw(key, key.copyRaw(value));
        }

        return copy;
    }

    /**
     * Creates a new DataStore instance with the given name.
     *
     * @param name the name of the DataStore
     * @return a new DataStore instance
     */
    public static DataStore newInstance(String name) {
        return new SimpleDataStore(name);
    }

    /**
     * Creates a new DataStore instance with the given StoreDefinition.
     *
     * @param storeDefinition the StoreDefinition
     * @return a new DataStore instance
     */
    public static DataStore newInstance(StoreDefinition storeDefinition) {
        return newInstance(storeDefinition, false);
    }

    /**
     * Creates a new DataStore instance with the given StoreDefinition and closed state.
     *
     * @param storeDefinition the StoreDefinition
     * @param closed if true, no other keys besides the ones defined in the StoreDefinition can be added
     * @return a new DataStore instance
     */
    public static DataStore newInstance(StoreDefinition storeDefinition, boolean closed) {
        if (closed) {
            return new ListDataStore(storeDefinition);
        } else {
            return new SimpleDataStore(storeDefinition);
        }
    }

    /**
     * Creates a new DataStore instance from a StoreDefinitionProvider.
     *
     * @param storeProvider the StoreDefinitionProvider
     * @return a new DataStore instance
     */
    public static DataStore newInstance(StoreDefinitionProvider storeProvider) {
        return newInstance(storeProvider.getStoreDefinition());
    }

    /**
     * Creates a new DataStore instance from a DataView.
     *
     * @param dataView the DataView
     * @return a new DataStore instance
     */
    public static DataStore newInstance(DataView dataView) {
        if (dataView instanceof DataStoreContainer) {
            return ((DataStoreContainer) dataView).getDataStore();
        }
        throw new RuntimeException("Not implemented yet.");
    }

    /**
     * Creates a new DataStore instance with the given name and values from another DataStore.
     *
     * @param name the name of the DataStore
     * @param dataStore the DataStore to copy values from
     * @return a new DataStore instance
     */
    public static DataStore newInstance(String name, DataStore dataStore) {
        return new SimpleDataStore(name, dataStore);
    }

    /**
     * Creates a DataStore from all the public static DataKeys that can be found in the class.
     *
     * @param aClass the class to derive the DataStore from
     * @return a new DataStore instance
     */
    public static DataStore newInstance(Class<?> aClass) {
        return newInstance(StoreDefinitions.fromInterface(aClass));
    }

    @Override
    default String toInlinedString() {
        Collection<String> keys = getKeysWithValues();

        if (getStoreDefinitionTry().isPresent()) {
            keys = getStoreDefinitionTry().get().getKeys().stream()
                    .filter(this::hasValue)
                    .map(DataKey::getName)
                    .toList();
        }

        return keys.stream()
                .map(key -> key + ": " + DataClassUtils.toString(get(key)))
                .collect(Collectors.joining(", ", getName() + " [", "]"));
    }

    @Override
    default Collection<DataKey<?>> getDataKeysWithValues() {
        StoreDefinition storeDefinition = getStoreDefinitionTry().orElse(null);

        if (storeDefinition == null) {
            return Collections.emptyList();
        }

        List<DataKey<?>> keysWithValues = new ArrayList<>();
        for (String keyId : getKeysWithValues()) {
            keysWithValues.add(storeDefinition.getKey(keyId));
        }

        return keysWithValues;
    }

    /**
     * Returns the AppPersistence instance that was used to load this DataStore, if any.
     *
     * @return an Optional containing the AppPersistence instance, if present
     */
    default Optional<AppPersistence> getPersistence() {
        return Optional.empty();
    }

    /**
     * Sets the AppPersistence instance that was used to load this DataStore.
     *
     * @param persistence the AppPersistence instance
     * @return this DataStore
     */
    default DataStore setPersistence(AppPersistence persistence) {
        return this;
    }

    /**
     * Returns the configuration file that was used to load this DataStore, if any.
     *
     * @return an Optional containing the configuration file, if present
     */
    default Optional<File> getConfigFile() {
        return Optional.empty();
    }

    /**
     * Sets the configuration file that was used to load this DataStore.
     *
     * @param configFile the configuration file
     * @return this DataStore
     */
    default DataStore setConfigFile(File configFile) {
        return this;
    }
}
