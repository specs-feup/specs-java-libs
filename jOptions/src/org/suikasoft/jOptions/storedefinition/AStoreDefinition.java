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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;

public abstract class AStoreDefinition implements StoreDefinition {

    private final String appName;
    // private final List<DataKey<?>> options;
    private final List<StoreSection> sections;
    private final DataStore defaultData;
    private final transient Map<String, DataKey<?>> keyMap = new HashMap<>();

    /**
     * @param appName
     * @param options
     */
    protected AStoreDefinition(String appName, List<DataKey<?>> options) {
        this(appName, Arrays.asList(StoreSection.newInstance(options)), null);
    }

    // protected AStoreDefinition(String appName, List<DataKey<?>> options, DataStore defaultData) {
    //
    // options = parseOptions(options);
    //
    // this.appName = appName;
    // this.options = options;
    // this.defaultData = defaultData;
    // }

    protected AStoreDefinition(String appName, List<StoreSection> sections, DataStore defaultData) {
        check(sections);

        this.appName = appName;
        this.sections = sections;
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
     * Checks if all options have different names.
     * 
     * @param options
     * @return
     */
    /*
    private static List<DataKey<?>> parseOptions(List<DataKey<?>> options) {
    Map<String, DataKey<?>> optionMap = FactoryUtils.newLinkedHashMap();
    
    for (DataKey<?> def : options) {
        DataKey<?> previousDef = optionMap.get(def.getName());
        if (previousDef != null) {
    	LoggingUtils.msgWarn("DataKey name clash between '" + previousDef
    		+ "' and '" + def + "'");
    	continue;
        }
    
        optionMap.put(def.getName(), def);
    }
    
    return FactoryUtils.newArrayList(optionMap.values());
    }
    */

    /**
     * Checks if all options have different names.
     * 
     * @param options
     * @return
     */
    private static void check(List<StoreSection> sections) {
        Set<String> seenKeys = new HashSet<>();

        List<DataKey<?>> options = StoreSection.getAllKeys(sections);

        for (DataKey<?> def : options) {
            if (seenKeys.contains(def.getName())) {
                throw new RuntimeException("DataKey clash for name '" + def.getName() + "'");
            }

            seenKeys.add(def.getName());
        }

    }

    @Override
    public String getName() {
        return appName;
    }

    @Override
    public List<DataKey<?>> getKeys() {
        return StoreSection.getAllKeys(sections);
    }

    @Override
    public List<StoreSection> getSections() {
        return sections;
    }

    @Override
    public DataStore getDefaultValues() {
        if (defaultData != null) {
            return defaultData;
        }

        return StoreDefinition.super.getDefaultValues();
    }

}
