/*
 * Copyright 2011 SPeCS Research Group.
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

package org.suikasoft.jOptions.values;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

import pt.up.fe.specs.util.SpecsCheck;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * Represents a list of several SetupData objects.
 * 
 * @author Joao Bispo
 */
public class SetupList implements DataStore {

    // Consider replace with LinkedHashMap
    private final String setupListName;
    // private List<SimpleSetup> setupList;
    private final Map<String, DataStore> mapOfSetups;

    // Using separate list inst of LinkedHashMap because XStream does not support Arrays.ArrayList, which LinkedHashMap
    // uses
    private final List<String> keys;

    // private Integer preferredIndex;
    private String preferredSetupName;

    // private final SetupOptions helper;

    public SetupList(String setupListName, Collection<DataStore> listOfSetups) {
        this.setupListName = setupListName;

        // mapOfSetups = SpecsFactory.newLinkedHashMap();
        mapOfSetups = new HashMap<>();
        keys = new ArrayList<>();
        for (DataStore setup : listOfSetups) {
            keys.add(setup.getName());
            DataStore previousSetup = mapOfSetups.put(setup.getName(), setup);
            if (previousSetup != null) {
                throw new RuntimeException("Could not build SetupList, two of the given setups have the same name ("
                        + setup.getName() + ")");
            }
        }

        preferredSetupName = null;
    }

    public static SetupList newInstance(String setupListName, StoreDefinition... storeDefinitions) {
        return newInstance(setupListName, Arrays.asList(storeDefinitions));
    }

    /**
     * Helper method which receives a list of enum classes that implements SetupProvider.
     * 
     * @param setupProvider
     * @return
     */
    // public static SetupList newInstanceWithEnum(String setupListName, Class<?>... setupProviders) {
    public static SetupList newInstance(String setupListName, List<StoreDefinition> storeDefinitions) {
        List<DataStore> listOfSetups = new ArrayList<>();
        // SpecsFactory.newArrayList();

        for (StoreDefinition definition : storeDefinitions) {
            DataStore aSetup = DataStore.newInstance(definition);
            listOfSetups.add(aSetup);
        }

        return new SetupList(setupListName, listOfSetups);
    }

    /*
    public List<SimpleSetup> getMapOfSetups() {
    return setupList;
    }
    */

    /**
     * @return the listOfSetups
     */
    private Map<String, DataStore> getMap() {
        return mapOfSetups;
    }

    public Collection<DataStore> getDataStores() {
        return keys.stream()
                .map(key -> mapOfSetups.get(key))
                .collect(Collectors.toList());
    }

    public void setPreferredSetup(String setupName) {
        // Check if setup list contains setup name
        if (!mapOfSetups.containsKey(setupName)) {
            SpecsLogs
                    .msgInfo("!Tried to set preferred setup of SetupList '" + getSetupListName() + "' to '" + setupName
                            + "', but SetupList does not have that setup. Available setups:" + mapOfSetups.keySet());
        }

        preferredSetupName = setupName;
    }

    public DataStore getPreferredSetup() {
        if (mapOfSetups.isEmpty()) {
            SpecsLogs.warn("There are no setups.");
            return null;
        }

        if (preferredSetupName == null) {
            // LoggingUtils.msgWarn("Preferred setup not set, returning first setup.");
            String firstSetup = getFirstSetup();
            return mapOfSetups.get(firstSetup);
        }

        String setupName = mapOfSetups.get(preferredSetupName).getName();

        return mapOfSetups.get(setupName);
    }

    /**
     * @return
     */
    private String getFirstSetup() {
        SpecsCheck.checkArgument(!keys.isEmpty(), () -> "There are no setups!");
        return keys.get(0);
        // return mapOfSetups.keySet().iterator().next();
    }

    /**
     * @return
     */
    public int getNumSetups() {
        return keys.size();
    }

    @Override
    public String getName() {
        return getPreferredSetup().getName();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (var key : keys) {
            // for (DataStore setup : mapOfSetups.values()) {
            DataStore setup = mapOfSetups.get(key);
            if (builder.length() != 0) {
                builder.append(", ");
            }

            builder.append(setup.getName());
        }

        return builder.toString();
    }

    /**
     * @param class1
     * @return
     */
    public DataStore getSetup(String storeName) {

        DataStore setup = mapOfSetups.get(storeName);
        if (setup == null) {
            throw new RuntimeException("Could not find setup '" + storeName + "', provided by class '");
        }

        return setup;
    }

    /**
     * @return the setupListName
     */
    public String getSetupListName() {
        return setupListName;
    }

    public DataStore getDataStore(String dataStoreName) {
        // Get inner setup specified by key

        // SimpleSetup innerSetup = setupList.getMap().get(innerKey);
        DataStore innerSetup = getMap().get(dataStoreName);
        if (innerSetup == null) {
            SpecsLogs.msgInfo("SetupList does not contain inner setup '" + dataStoreName + "'. Available setups: "
                    + toString());
            return null;
        }

        return innerSetup;
    }

    @Override
    // public <T, E extends T> Optional<T> set(DataKey<T> key, E value) {
    public <T, E extends T> SetupList set(DataKey<T> key, E value) {
        // return getPreferredSetup().set(key, value);
        getPreferredSetup().set(key, value);
        return this;
    }

    @Override
    public Optional<Object> setRaw(String key, Object value) {
        return getPreferredSetup().setRaw(key, value);
    }

    // @Override
    // public SetupList set(DataStore dataStore) {
    // getPreferredSetup().set(dataStore);
    //
    // return this;
    // }

    @Override
    public <T> T get(DataKey<T> key) {
        return getPreferredSetup().get(key);
    }

    @Override
    public Object get(String id) {
        return getPreferredSetup().get(id);
    }

    @Override
    public <T> boolean hasValue(DataKey<T> key) {
        return getPreferredSetup().hasValue(key);
    }

    @Override
    public void setStrict(boolean value) {
        getPreferredSetup().setStrict(value);

    }
    //
    // @Override
    // public Map<String, Object> getValuesMap() {
    // return getPreferredSetup().getValuesMap();
    // }

    @Override
    public <T> Optional<T> remove(DataKey<T> key) {
        return getPreferredSetup().remove(key);
    }

    @Override
    public Optional<StoreDefinition> getStoreDefinitionTry() {
        return getPreferredSetup().getStoreDefinitionTry();
    }

    @Override
    public void setStoreDefinition(StoreDefinition definition) {
        getPreferredSetup().setStoreDefinition(definition);
    }

    @Override
    public Collection<String> getKeysWithValues() {
        return getPreferredSetup().getKeysWithValues();
    }
}
