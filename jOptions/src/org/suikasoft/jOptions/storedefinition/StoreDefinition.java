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
 * 
 * @author JoaoBispo
 *
 */
public interface StoreDefinition {

    String getName();

    /**
     * TODO: Return Collection instead of List
     * 
     * @return
     */
    List<DataKey<?>> getKeys();

    /**
     * Sections of the StoreDefinition.
     * 
     * <p>
     * By default, returns a List with a single unnamed section.
     * 
     * @return
     */
    default List<StoreSection> getSections() {
        return Arrays.asList(StoreSection.newInstance(getKeys()));
    }

    /**
     * Maps the name of the key to the key itself.
     * 
     * @return
     */
    default Map<String, DataKey<?>> getKeyMap() {
        // To maintain the order of the keys
        // Map<String, DataKey<?>> keyMap = new LinkedHashMap<>();
        // getKeys().stream().forEach(key -> keyMap.put(getName(), key));
        //
        // return keyMap;
        return getKeys().stream()
                .collect(Collectors.toMap(key -> key.getName(), key -> key));
    }

    public static <T extends Enum<T> & DataKeyProvider> StoreDefinition newInstance(Class<T> aClass) {
        List<DataKey<?>> keys = new ArrayList<>();
        for (T key : aClass.getEnumConstants()) {
            keys.add(key.getDataKey());
        }

        return new GenericStoreDefinition(aClass.getSimpleName(), keys);
    }

    public static StoreDefinition newInstance(String name, DataKey<?>... keys) {
        return new GenericStoreDefinition(name, Arrays.asList(keys));
    }

    public static GenericStoreDefinition newInstance(String appName, Collection<DataKey<?>> keys) {
        return new GenericStoreDefinition(appName, new ArrayList<>(keys));
    }

    public static GenericStoreDefinition newInstanceFromInterface(Class<?> aClass) {
        return StoreDefinition.newInstance(aClass.getSimpleName(), StoreDefinitions.fromInterface(aClass).getKeys());
    }

    /**
     * 
     * @param key
     * @return the datakey with the same name as the given String. Throws an exception if no key is found with the given
     *         name
     */
    default DataKey<?> getKey(String key) {
        DataKey<?> dataKey = getKeyMap().get(key);
        if (dataKey == null) {
            throw new RuntimeException("Key '" + key + "' not found in store definition:" + toString());
        }

        return dataKey;
    }

    @SuppressWarnings("unchecked") // It is always Object
    default DataKey<Object> getKeyRaw(String key) {
        return (DataKey<Object>) getKey(key);
    }

    default DataStore getDefaultValues() {
        DataStore data = DataStore.newInstance(getName());

        for (DataKey<?> key : getKeys()) {
            if (key.hasDefaultValue()) {
                data.setRaw(key, key.getDefault().get());
            }
        }

        return data;
    }

    default StoreDefinition setDefaultValues(DataStore data) {
        throw new NotImplementedException(getClass());
    }

    default boolean hasKey(String keyName) {
        return getKeyMap().containsKey(keyName);
    }

}
