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
 * specific language governing permissions and limitations under the License.
 */

package org.suikasoft.jOptions;

import java.io.File;
import java.util.List;
import java.util.Optional;

import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.app.App;
import org.suikasoft.jOptions.app.AppKernel;
import org.suikasoft.jOptions.app.AppPersistence;
import org.suikasoft.jOptions.cli.CommandLineUtils;
import org.suikasoft.jOptions.gui.SimpleGui;
import org.suikasoft.jOptions.persistence.XmlPersistence;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * Utility class with static methods for jOptions operations.
 *
 * @author JoaoBispo
 */
public class JOptionsUtils {

    /**
     * Helper method which uses this class as the class for the jar path.
     * 
     * @param optionsFilename the name of the options file
     * @param storeDefinition the definition of the data store
     * @return the loaded DataStore instance
     */
    public static DataStore loadDataStore(String optionsFilename, StoreDefinition storeDefinition) {
        return loadDataStore(optionsFilename, JOptionsUtils.class, storeDefinition);
    }

    /**
     * Helper method which uses standard XmlPersistence as the default
     * AppPersistence.
     * 
     * @param optionsFilename the name of the options file
     * @param classForJarPath the class used to determine the jar path
     * @param storeDefinition the definition of the data store
     * @return the loaded DataStore instance
     */
    public static DataStore loadDataStore(String optionsFilename, Class<?> classForJarPath,
            StoreDefinition storeDefinition) {
        XmlPersistence persistence = new XmlPersistence(storeDefinition);

        return loadDataStore(optionsFilename, classForJarPath, storeDefinition, persistence);
    }

    /**
     * Loads a DataStore file from predefined locations.
     *
     * <p>
     * The method will look for the file in the following places:<br>
     * 1) In the folder of the JAR of this class <br>
     * 2) In the current working folder<br>
     * 
     * <p>
     * If the file is found in multiple locations, options are cumulatively added to
     * the final DataStore. If the file in the jar path is not found, it is created.
     * 
     * @param optionsFilename the name of the options file
     * @param classForJarPath the class used to determine the jar path
     * @param storeDefinition the definition of the data store
     * @param persistence     the persistence mechanism to use
     * @return the loaded DataStore instance
     */
    public static DataStore loadDataStore(String optionsFilename, Class<?> classForJarPath,
            StoreDefinition storeDefinition, AppPersistence persistence) {

        // Look for options in two places, JAR folder and current folder
        DataStore localData = loadOptionsNearJar(classForJarPath, optionsFilename, storeDefinition,
                persistence);

        // Try to find local options in current working folder and load them
        File localOptionsFile = new File(SpecsIo.getWorkingDir(), optionsFilename);
        if (localOptionsFile.isFile()) {
            if (localData.getConfigFile().isEmpty()) {
                localData.setConfigFile(localOptionsFile);
            }

            localData.addAll(persistence.loadData(localOptionsFile));
        }

        return localData;
    }

    /**
     * Tries to load options near the JAR file.
     * 
     * @param classForJarpath the class used to determine the jar path
     * @param optionsFilename the name of the options file
     * @param storeDefinition the definition of the data store
     * @param persistence     the persistence mechanism to use
     * @return the loaded DataStore instance
     */
    private static DataStore loadOptionsNearJar(Class<?> classForJarpath, String optionsFilename,
            StoreDefinition storeDefinition, AppPersistence persistence) {

        var localData = DataStore.newInstance(storeDefinition);

        // If can find jar path, try to load options near jar
        Optional<File> jarFolderTry = SpecsIo.getJarPath(classForJarpath);

        // If cannot find jar folder, just return an empty DataStore
        if (jarFolderTry.isEmpty()) {
            return localData;
        }

        File jarFolder = jarFolderTry.get();

        File localOptionsFile = new File(jarFolder, optionsFilename);
        localData.setConfigFile(localOptionsFile);

        if (localOptionsFile.isFile()) {
            SpecsLogs.debug(() -> "Loading options in file '" + SpecsIo.getCanonicalPath(localOptionsFile) + "'");
            localData.addAll(persistence.loadData(localOptionsFile));
        }
        // Only create default local_options.xml near the JAR if it is in a folder that
        // can be written
        else if (SpecsIo.canWriteFolder(jarFolder)) {
            SpecsLogs
                    .msgInfo("Options file '" + optionsFilename + "' not found near JAR, creating empty file:"
                            + SpecsIo.getCanonicalPath(localOptionsFile));
            SpecsLogs.msgInfo("This file can also be on the working directory.");
            DataStore emptyData = DataStore.newInstance(storeDefinition);
            persistence.saveData(localOptionsFile, emptyData);
        }

        return localData;
    }

    /**
     * Saves the given DataStore to a file.
     * 
     * @param file the file to save the DataStore to
     * @param data the DataStore instance to save
     */
    public static void saveDataStore(File file, DataStore data) {
        XmlPersistence persistence = data.getStoreDefinitionTry().map(XmlPersistence::new).orElse(new XmlPersistence());
        persistence.saveData(file, data);
    }

    /**
     * Executes the application. If no arguments are passed, launches the GUI mode;
     * otherwise, executes the CLI mode.
     * 
     * @param app  the application instance
     * @param args the list of arguments
     * @return the exit code (0 for success, -1 for failure)
     */
    public static int executeApp(App app, List<String> args) {

        // If no arguments, launch GUI mode
        if (args.isEmpty()) {
            new SimpleGui(app).execute();
            return 0;
        }

        // Otherwise, launch command-line mode
        boolean success = CommandLineUtils.launch(app, args);
        return success ? 0 : -1;
    }

    /**
     * Executes the application kernel. If no arguments are passed, launches the GUI
     * mode; otherwise, executes the CLI mode.
     * 
     * @param app  the application kernel instance
     * @param args the list of arguments
     * @return the exit code (0 for success, -1 for failure)
     */
    public static int executeApp(AppKernel app, List<String> args) {
        // Instantiate App from AppKernel
        return executeApp(App.newInstance(app), args);
    }

}
