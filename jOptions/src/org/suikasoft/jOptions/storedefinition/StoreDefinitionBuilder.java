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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.util.SpecsLogs;

/**
 * Builder for creating {@link StoreDefinition} instances, supporting sections
 * and default data.
 */
public class StoreDefinitionBuilder {

    /** The name of the application or store. */
    private final String appName;
    /** The list of sections in the store definition. */
    private final List<StoreSection> sections;
    /** The section currently being built. */
    private StoreSectionBuilder currentSection;
    /** Set of key names already added, to avoid duplicates. */
    private final Set<String> addedKeys;
    /** Default data for the store definition. */
    private DataStore defaultData;

    /**
     * Creates a new builder for the given application name.
     *
     * @param appName the name of the application or store
     */
    public StoreDefinitionBuilder(String appName) {
        this.appName = appName;
        sections = new ArrayList<>();
        currentSection = null;
        addedKeys = new HashSet<>();
        defaultData = null;
    }

    /**
     * Creates a new builder and adds a definition from the given class interface.
     *
     * @param aClass the class to extract a definition from
     */
    public StoreDefinitionBuilder(Class<?> aClass) {
        this(aClass.getSimpleName());
        addDefinition(StoreDefinitions.fromInterface(aClass));
    }

    /**
     * Adds several keys to the current section or store.
     *
     * @param keys the keys to add
     * @return this builder
     */
    public StoreDefinitionBuilder addKeys(DataKey<?>... keys) {
        return addKeys(Arrays.asList(keys));
    }

    /**
     * Adds a collection of keys to the current section or store.
     *
     * @param keys the keys to add
     * @return this builder
     */
    public StoreDefinitionBuilder addKeys(Collection<DataKey<?>> keys) {
        for (DataKey<?> key : keys) {
            addKey(key);
        }
        return this;
    }

    /**
     * Adds a single key to the current section or store.
     *
     * @param key the key to add
     * @return this builder
     */
    public StoreDefinitionBuilder addKey(DataKey<?> key) {
        // Check if key is not already added
        if (addedKeys.contains(key.getName())) {
            SpecsLogs.warn("Duplicated key while building Store Definition: '" + key.getName() + "'");
            return this;
        }
        addedKeys.add(key.getName());

        // Section logic
        if (currentSection == null) {
            currentSection = new StoreSectionBuilder();
        }

        currentSection.add(key);

        return this;
    }

    /**
     * Starts a new section with the given name.
     *
     * @param name the name of the section
     * @return this builder
     */
    public StoreDefinitionBuilder startSection(String name) {
        // If current section not null, store it
        if (currentSection != null) {
            sections.add(currentSection.build());
        }

        // Create new section builder
        currentSection = new StoreSectionBuilder(name);

        return this;
    }

    /**
     * Sets the default values for the store definition.
     *
     * @param data the default data
     * @return this builder
     */
    public StoreDefinitionBuilder setDefaultValues(DataStore data) {
        defaultData = data;
        return this;
    }

    /**
     * Builds the store definition.
     *
     * @return the constructed {@link StoreDefinition}
     */
    public StoreDefinition build() {
        // Save last section
        if (currentSection != null) {
            sections.add(currentSection.build());
        }

        return new GenericStoreDefinition(appName, sections, defaultData);
    }

    /**
     * Adds a store definition to this builder.
     *
     * @param storeDefinition the store definition to add
     * @return this builder
     */
    public StoreDefinitionBuilder addDefinition(StoreDefinition storeDefinition) {
        addDefinitionPrivate(storeDefinition, false);

        return this;
    }

    /**
     * Adds a named store definition to this builder.
     *
     * @param name            the name of the store definition
     * @param storeDefinition the store definition to add
     * @return this builder
     */
    public StoreDefinitionBuilder addNamedDefinition(String name, StoreDefinition storeDefinition) {
        return addNamedDefinition(new StoreDefinitionBuilder(name).addDefinition(storeDefinition).build());
    }

    /**
     * Adds a named store definition to this builder.
     *
     * @param storeDefinition the store definition to add
     * @return this builder
     */
    public StoreDefinitionBuilder addNamedDefinition(StoreDefinition storeDefinition) {
        addDefinitionPrivate(storeDefinition, true);

        return this;
    }

    /**
     * Adds a store definition to this builder, optionally using its name as a
     * section name.
     *
     * @param storeDefinition the store definition to add
     * @param useName         whether to use the store definition's name as a
     *                        section name
     */
    private void addDefinitionPrivate(StoreDefinition storeDefinition, boolean useName) {
        if (useName) {
            startSection(storeDefinition.getName());
        } else {
            startSection(null);
        }

        for (DataKey<?> key : storeDefinition.getKeys()) {
            addKey(key);
        }
    }

}
