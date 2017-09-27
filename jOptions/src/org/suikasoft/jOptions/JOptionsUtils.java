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

import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.app.AppPersistence;
import org.suikasoft.jOptions.persistence.XmlPersistence;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * Class with utility methods.
 * 
 * @author JoaoBispo
 *
 */
public class JOptionsUtils {

    /**
     * Helper method which uses this class as the class for the jar path.
     * 
     * @param optionsFilename
     * @param storeDefinition
     * @return
     */
    public static DataStore loadDataStore(String optionsFilename, StoreDefinition storeDefinition) {
        return loadDataStore(optionsFilename, JOptionsUtils.class, storeDefinition);
    }

    /**
     * Loads a DataStore file, from predefined places.
     *
     * <p>
     * The method will look for the file in the following places:<br>
     * 1) In the folder of the JAR of this class <br>
     * 2) In the current working folder<br>
     * 
     * <p>
     * If finds the file in multiple locations, cumulatively adds the options to the final DataStore. If the file in the
     * jar path is not found, it is created.
     * 
     * @param optionsFilename
     * @param storeDefinition
     * @return
     */

    public static DataStore loadDataStore(String optionsFilename, Class<?> classForJarPath,
            StoreDefinition storeDefinition) {

        DataStore localData = DataStore.newInstance(storeDefinition);
        XmlPersistence persistence = new XmlPersistence(storeDefinition);

        // Look for options in two places, JAR folder and current folder
        loadOptionsNearJar(classForJarPath, optionsFilename, localData, storeDefinition, persistence);

        // Try to find local options in current working folder and load them
        File localOptionsFile = new File(SpecsIo.getWorkingDir(), optionsFilename);
        if (localOptionsFile.isFile()) {
            localData.addAll(persistence.loadData(localOptionsFile));
        }

        return localData;
    }

    /**
     * Tries to load a
     * 
     * @param optionsFilename
     * @param jarFolder
     * @param localData
     * @param storeDefinition
     * @param persistence
     */
    private static void loadOptionsNearJar(Class<?> classForJarpath, String optionsFilename, DataStore localData,
            StoreDefinition storeDefinition, AppPersistence persistence) {

        // If can find jar path, try to load options near jar
        Optional<File> jarFolderTry = SpecsIo.getJarPath(classForJarpath);

        // If cannot find jar folder, just return
        if (!jarFolderTry.isPresent()) {
            return;
        }

        File localOptionsFile = new File(jarFolderTry.get(), optionsFilename);

        if (localOptionsFile.isFile()) {
            localData.addAll(persistence.loadData(localOptionsFile));
        } else {
            SpecsLogs
                    .msgInfo("Local options file not found near JAR, creating file:"
                            + SpecsIo.getCanonicalPath(localOptionsFile));
            SpecsLogs.msgInfo("Local options File can also be on working directory.");
            DataStore emptyData = DataStore.newInstance(storeDefinition);
            persistence.saveData(localOptionsFile, emptyData);
        }

    }

    public static void saveDataStore(File file, DataStore data) {
        XmlPersistence persistence = data.getStoreDefinition().map(XmlPersistence::new).orElse(new XmlPersistence());
        persistence.saveData(file, data);
    }

}
