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

package org.suikasoft.jOptions;

import java.io.File;
import java.util.Optional;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

public interface JOptionKeys {

    DataKey<Optional<String>> CURRENT_FOLDER_PATH = KeyFactory.optional("joptions_current_folder_path");
    DataKey<Boolean> USE_RELATIVE_PATHS = KeyFactory.bool("joptions_use_relative_paths");

    /**
     * If the path is not absolute and CURRENT_FOLDER_PATH is set, returns a path relative to that set folder.
     * 
     * @param currentFile
     * @param dataStore
     * @return
     */
    public static File getContextPath(File currentFile, DataStore dataStore) {
        Optional<String> workingFolder = dataStore.get(JOptionKeys.CURRENT_FOLDER_PATH);

        // No folder set, just return
        if (!workingFolder.isPresent()) {
            return currentFile;
        }

        // Path is absolute, respect that
        if (currentFile.isAbsolute()) {
            return currentFile;

        }

        // Path is relative, create new file with set folder as parent
        File parentFolder = new File(workingFolder.get());
        return new File(parentFolder, currentFile.getPath());

    }

    /**
     * Overload that accepts a String instead of a File.
     * 
     * @param currentPath
     * @param dataStore
     * @return
     */
    public static File getContextPath(String currentPath, DataStore dataStore) {
        return getContextPath(new File(currentPath), dataStore);
    }
}
