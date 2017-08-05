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
 * specific language governing permissions and limitations under the License. under the License.
 */

package org.suikasoft.jOptions.app;

import java.io.File;

import org.suikasoft.jOptions.Interfaces.DataStore;

/**
 * @author Joao Bispo
 * 
 */
public interface AppPersistence {

    public DataStore loadData(File file);

    /**
     * 
     * @param file
     * @param setup
     * @param keepSetupFile
     * @return
     */
    public boolean saveData(File file, DataStore data, boolean keepConfigFile);

    /**
     * Helper method which does not save the config file path in the persistent format.
     * 
     * @param file
     * @param data
     * @return
     */
    default boolean saveData(File file, DataStore data) {
	return saveData(file, data, false);
    }
}
