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
 * specific language governing permissions and limitations under the License. under the License.
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

public class StoreDefinitionBuilder {

    private final String appName;
    // private final List<DataKey<?>> options;
    private final List<StoreSection> sections;
    private StoreSectionBuilder currentSection;

    private final Set<String> addedKeys;
    private DataStore defaultData;

    // private StoreDefinitionBuilder(String appName, Set<String> addedKeys,
    // DataStore defaultData) {
    //
    // //super(appName, new ArrayList<>());
    // this.appName = appName;
    // this.
    //
    // this.addedKeys = addedKeys;
    // this.defaultData = defaultData;
    // }

    // private StoreDefinitionBuilder(String appName, List<DataKey<?>> options, Set<String> addedKeys,
    // DataStore defaultData) {
    //
    // this.appName = appName;
    // this.options = options;
    // this.addedKeys = addedKeys;
    // this.defaultData = defaultData;
    // }

    public StoreDefinitionBuilder(String appName) {
        this.appName = appName;
        sections = new ArrayList<>();
        currentSection = null;

        addedKeys = new HashSet<>();
        defaultData = null;
        // this(appName, new ArrayList<>(), new HashSet<>(), null);
    }

    /**
     * Helper method to add several keys.
     * 
     * @param keys
     * @return
     */
    public StoreDefinitionBuilder addKeys(DataKey<?>... keys) {
        return addKeys(Arrays.asList(keys));
    }

    public StoreDefinitionBuilder addKeys(Collection<DataKey<?>> keys) {
        for (DataKey<?> key : keys) {
            addKey(key);
        }

        return this;
    }

    public StoreDefinitionBuilder addKey(DataKey<?> key) {
        // Check if key is not already added
        if (addedKeys.contains(key.getName())) {
            SpecsLogs.msgWarn("Duplicated key while building Store Definition: '" + key.getName() + "'");
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

    public StoreDefinitionBuilder startSection(String name) {
        // If current section not null, store it
        if (currentSection != null) {
            sections.add(currentSection.build());
        }

        // Create new section builder
        currentSection = new StoreSectionBuilder(name);

        return this;
    }

    /*
    @Override
    public DataStore getDefaultValues() {
    if (defaultData != null) {
        return defaultData;
    }
    
    return super.getDefaultValues();
    }
    */

    // @Override
    public StoreDefinitionBuilder setDefaultValues(DataStore data) {
        defaultData = data;
        return this;
        // StoreDefinitionBuilder builder = new StoreDefinitionBuilder(getName(), addedKeys, data);
        // builder.getKeys().addAll(getKeys());
        //
        // return builder;
    }

    public StoreDefinition build() {
        // Save last section
        if (currentSection != null) {
            sections.add(currentSection.build());
        }

        return new GenericStoreDefinition(appName, sections, defaultData);
    }

    public void addDefinition(StoreDefinition storeDefinition) {
        addDefinitionPrivate(storeDefinition, false);
    }

    public void addNamedDefinition(StoreDefinition storeDefinition) {
        addDefinitionPrivate(storeDefinition, true);
    }

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
