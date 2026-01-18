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
 * Represents a list of several SetupData objects, providing methods to manage
 * and access them.
 *
 * @author Joao Bispo
 */
public class SetupList implements DataStore {
    private final String setupListName;
    private final Map<String, DataStore> mapOfSetups;
    private final List<String> keys;
    private String preferredSetupName;

    /**
     * Constructs a SetupList with the given name and collection of DataStore
     * objects.
     *
     * @param setupListName the name of the setup list
     * @param listOfSetups  the collection of DataStore objects to include in the
     *                      setup list
     */
    public SetupList(String setupListName, Collection<DataStore> listOfSetups) {
        this.setupListName = setupListName;
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

    /**
     * Creates a new SetupList instance with the given name and store definitions.
     *
     * @param setupListName    the name of the setup list
     * @param storeDefinitions the store definitions to include in the setup list
     * @return a new SetupList instance
     */
    public static SetupList newInstance(String setupListName, StoreDefinition... storeDefinitions) {
        return newInstance(setupListName, Arrays.asList(storeDefinitions));
    }

    /**
     * Creates a new SetupList instance with the given name and list of store
     * definitions.
     *
     * @param setupListName    the name of the setup list
     * @param storeDefinitions the list of store definitions to include in the setup
     *                         list
     * @return a new SetupList instance
     */
    public static SetupList newInstance(String setupListName, List<StoreDefinition> storeDefinitions) {
        List<DataStore> listOfSetups = new ArrayList<>();
        for (StoreDefinition definition : storeDefinitions) {
            DataStore aSetup = DataStore.newInstance(definition);
            listOfSetups.add(aSetup);
        }
        return new SetupList(setupListName, listOfSetups);
    }

    /**
     * Returns the map of setups.
     *
     * @return the map of setups
     */
    private Map<String, DataStore> getMap() {
        return mapOfSetups;
    }

    /**
     * Returns the collection of DataStore objects in this SetupList.
     *
     * @return the collection of DataStore objects
     */
    public Collection<DataStore> getDataStores() {
        return keys.stream()
                .map(mapOfSetups::get)
                .collect(Collectors.toList());
    }

    /**
     * Sets the preferred setup by its name.
     *
     * @param setupName the name of the preferred setup
     */
    public void setPreferredSetup(String setupName) {
        if (!mapOfSetups.containsKey(setupName)) {
            SpecsLogs
                    .msgInfo("!Tried to set preferred setup of SetupList '" + getSetupListName() + "' to '" + setupName
                            + "', but SetupList does not have that setup. Available setups:" + mapOfSetups.keySet());
        }
        preferredSetupName = setupName;
    }

    /**
     * Returns the preferred setup.
     *
     * @return the preferred setup, or null if no setups are available
     */
    public DataStore getPreferredSetup() {
        if (mapOfSetups.isEmpty()) {
            SpecsLogs.warn("There are no setups.");
            return null;
        }
        if (preferredSetupName == null) {
            String firstSetup = getFirstSetup();
            return mapOfSetups.get(firstSetup);
        }
        String setupName = mapOfSetups.get(preferredSetupName).getName();
        return mapOfSetups.get(setupName);
    }

    /**
     * Returns the name of the first setup in the list.
     *
     * @return the name of the first setup
     * @throws IllegalArgumentException if there are no setups
     */
    private String getFirstSetup() {
        SpecsCheck.checkArgument(!keys.isEmpty(), () -> "There are no setups!");
        return keys.get(0);
    }

    /**
     * Returns the number of setups in this SetupList.
     *
     * @return the number of setups
     */
    public int getNumSetups() {
        return keys.size();
    }

    /**
     * Returns the name of the preferred setup.
     *
     * @return the name of the preferred setup
     */
    @Override
    public String getName() {
        return getPreferredSetup().getName();
    }

    /**
     * Returns a comma-separated string of all setup names in this list.
     *
     * @return a string representation of the setup list
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (var key : keys) {
            DataStore setup = mapOfSetups.get(key);
            if (!builder.isEmpty()) {
                builder.append(", ");
            }
            builder.append(setup.getName());
        }
        return builder.toString();
    }

    /**
     * Returns the setup with the given name.
     *
     * @param storeName the name of the setup
     * @return the setup with the given name
     * @throws RuntimeException if the setup is not found
     */
    public DataStore getSetup(String storeName) {
        DataStore setup = mapOfSetups.get(storeName);
        if (setup == null) {
            throw new RuntimeException("Could not find setup '" + storeName + "', provided by class '");
        }
        return setup;
    }

    /**
     * Returns the name of this setup list.
     *
     * @return the name of the setup list
     */
    public String getSetupListName() {
        return setupListName;
    }

    /**
     * Returns the DataStore object with the given name.
     *
     * @param dataStoreName the name of the DataStore object
     * @return the DataStore object with the given name, or null if not found
     */
    public DataStore getDataStore(String dataStoreName) {
        DataStore innerSetup = getMap().get(dataStoreName);
        if (innerSetup == null) {
            SpecsLogs.msgInfo("SetupList does not contain inner setup '" + dataStoreName + "'. Available setups: "
                    + this);
            return null;
        }
        return innerSetup;
    }

    /**
     * Sets a value for the given DataKey in the preferred setup.
     *
     * @param key   the DataKey
     * @param value the value to set
     * @return this SetupList for chaining
     */
    @Override
    public <T, E extends T> SetupList set(DataKey<T> key, E value) {
        getPreferredSetup().set(key, value);
        return this;
    }

    /**
     * Sets a raw value for the given key in the preferred setup.
     *
     * @param key   the key
     * @param value the value to set
     * @return an Optional containing the previous value, if any
     */
    @Override
    public Optional<Object> setRaw(String key, Object value) {
        return getPreferredSetup().setRaw(key, value);
    }

    /**
     * Gets a value for the given DataKey from the preferred setup.
     *
     * @param key the DataKey
     * @return the value associated with the key
     */
    @Override
    public <T> T get(DataKey<T> key) {
        return getPreferredSetup().get(key);
    }

    /**
     * Gets a value for the given string key from the preferred setup.
     *
     * @param id the key
     * @return the value associated with the key
     */
    @Override
    public Object get(String id) {
        return getPreferredSetup().get(id);
    }

    /**
     * Checks if the preferred setup has a value for the given DataKey.
     *
     * @param key the DataKey
     * @return true if the value is present, false otherwise
     */
    @Override
    public <T> boolean hasValue(DataKey<T> key) {
        return getPreferredSetup().hasValue(key);
    }

    /**
     * Sets strict mode for the preferred setup.
     *
     * @param value true to enable strict mode, false otherwise
     */
    @Override
    public void setStrict(boolean value) {
        getPreferredSetup().setStrict(value);
    }

    /**
     * Removes the value for the given DataKey from the preferred setup.
     *
     * @param key the DataKey
     * @return an Optional containing the removed value, if any
     */
    @Override
    public <T> Optional<T> remove(DataKey<T> key) {
        return getPreferredSetup().remove(key);
    }

    /**
     * Gets the store definition from the preferred setup, if available.
     *
     * @return an Optional containing the StoreDefinition, if present
     */
    @Override
    public Optional<StoreDefinition> getStoreDefinitionTry() {
        return getPreferredSetup().getStoreDefinitionTry();
    }

    /**
     * Sets the store definition for the preferred setup.
     *
     * @param definition the StoreDefinition to set
     */
    @Override
    public void setStoreDefinition(StoreDefinition definition) {
        getPreferredSetup().setStoreDefinition(definition);
    }

    /**
     * Returns the collection of keys with values in the preferred setup.
     *
     * @return the collection of keys with values
     */
    @Override
    public Collection<String> getKeysWithValues() {
        return getPreferredSetup().getKeysWithValues();
    }
}
