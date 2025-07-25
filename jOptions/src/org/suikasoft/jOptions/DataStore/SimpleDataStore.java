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

package org.suikasoft.jOptions.DataStore;

import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

/**
 * Simple implementation of a DataStore.
 *
 * <p>This class provides a basic DataStore backed by a map, supporting construction from a name, another DataStore, or a StoreDefinition.
 */
public class SimpleDataStore extends ADataStore {

    /**
     * Constructs a SimpleDataStore with the given name.
     *
     * @param name the name of the DataStore
     */
    public SimpleDataStore(String name) {
        super(name);
    }

    /**
     * Constructs a SimpleDataStore with the given name and another DataStore as source.
     *
     * @param name the name of the DataStore
     * @param dataStore the source DataStore
     */
    public SimpleDataStore(String name, DataStore dataStore) {
        super(name, dataStore);
    }

    /**
     * Constructs a SimpleDataStore with the given StoreDefinition.
     *
     * @param storeDefinition the store definition
     */
    public SimpleDataStore(StoreDefinition storeDefinition) {
        super(storeDefinition);
    }

}
