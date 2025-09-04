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

package org.suikasoft.jOptions.storedefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.suikasoft.jOptions.DataStore.DataKeyProvider;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.util.exceptions.NotImplementedException;

/**
 * Represents a configuration, based on a name and a set of keys.
 */
public interface StoreDefinition {

    /**
     * Returns the name of the store definition.
     *
     * @return the name
     */
    String getName();

    /**
     * Returns the list of keys in this store definition.
     *
     * @return the list of keys
     */
    List<DataKey<?>> getKeys();

    /**
     * Returns the sections of the store definition. By default, returns a list with a single unnamed section.
     *
     * @return the sections
     */
    default List<StoreSection> getSections() {
        return List.of(StoreSection.newInstance(getKeys()));
    }

    /**
     * Returns a map from key name to DataKey, maintaining the order of the keys.
     *
     * @return the key map
     */
    default Map<String, DataKey<?>> getKeyMap() {
        return getKeys().stream()
                .collect(Collectors.toMap(DataKey::getName, key -> key));
    }

    /**
     * Creates a new StoreDefinition from an enum class implementing DataKeyProvider.
     *
     * @param aClass the enum class
     * @return a new StoreDefinition
     */
    public static <T extends Enum<T> & DataKeyProvider> StoreDefinition newInstance(Class<T> aClass) {
        List<DataKey<?>> keys = new ArrayList<>();
        for (T key : aClass.getEnumConstants()) {
            keys.add(key.getDataKey());
        }
        return new GenericStoreDefinition(aClass.getSimpleName(), keys);
    }

    /**
     * Creates a new StoreDefinition with the given name and keys.
     *
     * @param name the name
     * @param keys the keys
     * @return a new StoreDefinition
     */
    public static StoreDefinition newInstance(String name, DataKey<?>... keys) {
        return new GenericStoreDefinition(name, Arrays.asList(keys));
    }

    /**
     * Creates a new StoreDefinition with the given name and collection of keys.
     *
     * @param appName the name
     * @param keys the keys
     * @return a new StoreDefinition
     */
    public static GenericStoreDefinition newInstance(String appName, Collection<DataKey<?>> keys) {
        return new GenericStoreDefinition(appName, new ArrayList<>(keys));
    }

    /**
     * Creates a new StoreDefinition from a class interface.
     *
     * @param aClass the class
     * @return a new StoreDefinition
     */
    public static GenericStoreDefinition newInstanceFromInterface(Class<?> aClass) {
        return StoreDefinition.newInstance(aClass.getSimpleName(), StoreDefinitions.fromInterface(aClass).getKeys());
    }

    /**
     * Returns the DataKey with the given name. Throws an exception if not found.
     *
     * @param key the key name
     * @return the DataKey
     */
    default DataKey<?> getKey(String key) {
        DataKey<?> dataKey = getKeyMap().get(key);
        if (dataKey == null) {
            throw new RuntimeException("Key '" + key + "' not found in store definition:" + this);
        }

        return dataKey;
    }

    /**
     * Returns the raw DataKey with the given name. Throws an exception if not found.
     *
     * @param key the key name
     * @return the raw DataKey
     */
    @SuppressWarnings("unchecked") // It is always Object
    default DataKey<Object> getKeyRaw(String key) {
        return (DataKey<Object>) getKey(key);
    }

    /**
     * Returns a DataStore with the default values for this store definition.
     *
     * @return the DataStore with default values
     */
    default DataStore getDefaultValues() {
        DataStore data = DataStore.newInstance(getName());

        for (DataKey<?> key : getKeys()) {
            if (key.hasDefaultValue()) {
                data.setRaw(key, key.getDefault().get());
            }
        }

        return data;
    }

    /**
     * Sets the default values in the given DataStore.
     *
     * @param data the DataStore
     * @return the updated StoreDefinition
     */
    default StoreDefinition setDefaultValues(DataStore data) {
        throw new NotImplementedException(getClass());
    }

    /**
     * Checks if the store definition contains a key with the given name.
     *
     * @param keyName the key name
     * @return true if the key exists, false otherwise
     */
    default boolean hasKey(String keyName) {
        return getKeyMap().containsKey(keyName);
    }

}
