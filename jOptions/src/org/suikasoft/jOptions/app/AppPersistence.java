/**
 * Copyright 2013 SPeCS Research Group.
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

package org.suikasoft.jOptions.app;

import java.io.File;

import org.suikasoft.jOptions.Interfaces.DataStore;

/**
 * Persistence utilities for jOptions applications.
 * 
 * @author Joao Bispo
 * 
 */
public interface AppPersistence {

    /**
     * Loads data from the specified file.
     * 
     * @param file the file to load data from
     * @return the loaded data as a DataStore object
     */
    public DataStore loadData(File file);

    /**
     * Saves data to the specified file.
     * 
     * @param file           the file to save data to
     * @param data           the data to be saved
     * @param keepConfigFile whether to keep the configuration file path in the
     *                       persistent format
     * @return true if the data was successfully saved, false otherwise
     */
    public boolean saveData(File file, DataStore data, boolean keepConfigFile);

    /**
     * Helper method which does not save the config file path in the persistent
     * format.
     * 
     * @param file the file to save data to
     * @param data the data to be saved
     * @return true if the data was successfully saved, false otherwise
     */
    default boolean saveData(File file, DataStore data) {
        return saveData(file, data, false);
    }
}
