/*
 * Copyright 2013 SPeCS Research Group.
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

package org.suikasoft.jOptions.GenericImplementations;

import java.io.File;

import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.app.AppPersistence;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

/**
 * Dummy implementation of AppPersistence for testing purposes.
 * <p>
 * This implementation keeps the DataStore in memory and does not persist to disk.
 *
 * @author Joao Bispo
 */
public class DummyPersistence implements AppPersistence {

    private DataStore setup;

    /**
     * Constructs a DummyPersistence with the given DataStore.
     *
     * @param setup the DataStore to use
     */
    public DummyPersistence(DataStore setup) {
        this.setup = setup;
    }

    /**
     * Constructs a DummyPersistence with a new DataStore from the given StoreDefinition.
     *
     * @param setupDefinition the StoreDefinition to use
     */
    public DummyPersistence(StoreDefinition setupDefinition) {
        this(DataStore.newInstance(setupDefinition));
    }

    /**
     * Loads the DataStore. Ignores the file and returns the in-memory DataStore.
     *
     * @param file the file to load (ignored)
     * @return the in-memory DataStore
     */
    @Override
    public DataStore loadData(File file) {
        return setup;
    }

    /**
     * Saves the DataStore. Ignores the file and keeps the DataStore in memory.
     *
     * @param file the file to save (ignored)
     * @param setup the DataStore to save
     * @param keepSetupFile whether to keep the setup file (ignored)
     * @return true always
     */
    @Override
    public boolean saveData(File file, DataStore setup, boolean keepSetupFile) {
        this.setup = setup;
        return true;
    }
}
