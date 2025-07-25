/**
 * Copyright 2016 SPeCS.
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;

/**
 * Abstract base class for {@link StoreDefinition} implementations.
 */
public abstract class AStoreDefinition implements StoreDefinition {

    private final String appName;
    private final List<StoreSection> sections;
    private final DataStore defaultData;
    private final Map<String, DataKey<?>> keyMap = new HashMap<>();

    /**
     * Creates a new store definition with the given name and options.
     *
     * @param appName the name of the store
     * @param options the list of keys
     */
    protected AStoreDefinition(String appName, List<DataKey<?>> options) {
        this(appName, Arrays.asList(StoreSection.newInstance(options)), null);
    }

    /**
     * Creates a new store definition with the given name, sections, and default data.
     *
     * @param appName the name of the store
     * @param sections the sections
     * @param defaultData the default data
     */
    protected AStoreDefinition(String appName, List<StoreSection> sections, DataStore defaultData) {
        check(sections);
        this.appName = appName;
        this.sections = new ArrayList<>(sections);
        this.defaultData = defaultData;
    }

    @Override
    public Map<String, DataKey<?>> getKeyMap() {
        if (keyMap.isEmpty()) {
            keyMap.putAll(StoreDefinition.super.getKeyMap());
        }
        return keyMap;
    }

    /**
     * Checks if all sections have different key names.
     *
     * @param sections the sections to check
     */
    private static void check(List<StoreSection> sections) {
        Set<String> keyNames = new HashSet<>();
        for (StoreSection section : sections) {
            for (DataKey<?> key : section.getKeys()) {
                if (!keyNames.add(key.getName())) {
                    throw new RuntimeException("Duplicate key name: '" + key.getName() + "'");
                }
            }
        }
    }

    @Override
    public String getName() {
        return appName;
    }

    /**
     * Retrieves all keys from the store definition.
     *
     * @return a list of keys
     */
    @Override
    public List<DataKey<?>> getKeys() {
        return StoreSection.getAllKeys(sections);
    }

    /**
     * Retrieves all sections from the store definition.
     *
     * @return a list of sections
     */
    @Override
    public List<StoreSection> getSections() {
        return sections;
    }

    /**
     * Retrieves the default values for the store definition.
     *
     * @return the default data store
     */
    @Override
    public DataStore getDefaultValues() {
        if (defaultData != null) {
            return defaultData;
        }
        return StoreDefinition.super.getDefaultValues();
    }
}
