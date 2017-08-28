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

package org.suikasoft.jOptions.cli;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.app.App;

import pt.up.fe.specs.util.SpecsCollections;
import pt.up.fe.specs.util.SpecsFactory;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * @author Joao Bispo
 * 
 */
public class AppLauncher {

    private final App app;
    // private final AppKernel app;
    // private final String appName;
    // private final SetupDefinition setupDefition;

    private final List<String> resources;
    // private final Map<Class<?>, Object> defaultValues;

    private File baseFolder;

    // private final AppPersistence persistence;

    /*
    public AppLauncher(AppKernel app, String appName,
        Class<? extends Enum<? extends SetupProvider>> setupDefinitionClass,
        AppPersistence persistence) {
    
    this(app, appName, getDefinition(setupDefinitionClass), persistence);
    }
    */
    /*
    private static SetupDefinition getDefinition(
        Class<? extends Enum<? extends SetupProvider>> setupDefinitionClass) {
    Enum<? extends SetupProvider> enums[] = setupDefinitionClass.getEnumConstants();
    if (enums.length == 0) {
        throw new RuntimeException("Given enum class '" + setupDefinitionClass
    	    + "' has zero enums.");
    }
    
    return ((SetupProvider) enums[0]).getSetupDefinition();
    }
    */
    /**
     * @param app
     */
    // public AppLauncher(AppKernel app, String appName, SetupDefinition setupDefinition,
    // AppPersistence persistence) {
    public AppLauncher(App app) {

        this.app = app;
        // this.app = app;
        // this.appName = appName;
        // this.setupDefition = setupDefinition;

        resources = SpecsFactory.newArrayList();
        // defaultValues = CommandLineUtils.getDefaultValues();

        // persistence = new XmlPersistence(setupDefinition.getOptions());
        // this.persistence = persistence;
        baseFolder = null;
    }

    public void addResources(Collection<String> resources) {
        this.resources.addAll(resources);
    }

    /**
     * @return the app
     */
    public App getApp() {
        return app;
    }

    /**
     * @return the appName
     */
    /*
    public String getAppName() {
    return appName;
    }
    */

    /**
     * Helper method with String array.
     * 
     * @param args
     * @return
     */
    public boolean launch(String[] args) {
        return launch(Arrays.asList(args));
    }

    /**
     * Parse the input arguments looking for a configuration file in the first argument.
     * 
     * <p>
     * If found, parses other arguments as key-value pairs, to be replaced in the given setup file, and launches the
     * program returning true upon completion.
     * 
     * If the first argument is not a configuration file, applies default values to the non-defined parameters.
     * 
     * @param args
     * @return
     */
    public boolean launch(List<String> args) {
        if (args.isEmpty()) {
            SpecsLogs
                    .msgInfo("No arguments found. Please enter a configuration file, or key/value pairs.");
            return false;
        }

        args = parseSpecialArguments(args);

        // Get first argument, check if it is an option.
        if (args.get(0).indexOf("=") != -1) {
            return launchCommandLineNoSetup(args);
        }

        File setupFile = new File(args.get(0));
        DataStore setupData = app.getPersistence().loadData(setupFile);
        if (setupData == null) {
            return false;
        }

        List<String> argsWithoutSetup = SpecsCollections.subList(args, 1);
        commandLineWithSetup(argsWithoutSetup, setupData);

        return true;
    }

    /**
     * @param args
     * @return
     */
    private List<String> parseSpecialArguments(List<String> args) {
        // If first argument is base_folder="path", create temporary file there and remove option
        String firstArg = args.get(0);
        if (firstArg.startsWith("base_folder=")) {
            firstArg = firstArg.substring("base_folder=".length());
            baseFolder = SpecsIo.existingFolder(null, firstArg);
            args = SpecsFactory.newArrayList(args);
            args.remove(0);
        }

        return args;
    }

    /**
     * Adds a default value for options of the given class.
     * 
     * @param aClass
     * @param value
     */
    /*
    public void addDefaultValue(Class<?> aClass, Object value) {
    Object previousValue = defaultValues.get(aClass);
    if (previousValue != null) {
        LoggingUtils.msgInfo("Replacing previous default value for class '"
    	    + aClass.getSimpleName() + "': " + previousValue + " -> " + value);
    }
    
    defaultValues.put(aClass, value);
    }
    */

    private boolean launchCommandLineNoSetup(List<String> args) {

        // Create empty setup data
        DataStore data = DataStore.newInstance(app.getDefinition());

        // SimpleSetup setupData = SimpleSetup.newInstance(app.getDefinition(), defaultValues);

        // Execute command-line mode
        commandLineWithSetup(args, data);

        return true;
    }

    private void commandLineWithSetup(List<String> args, DataStore setupData) {

        new CommandLineUtils(app.getDefinition()).addArgs(setupData, args);

        File tempFile = new File(baseFolder, app.getClass().getSimpleName() + "__temp_config.xml");
        app.getPersistence().saveData(tempFile, setupData, true);

        // Execute
        SpecsLogs.msgInfo("Executing application '" + app.getName() + "'.");
        execute(tempFile);

        tempFile.delete();
    }

    public int execute(File setupFile) {
        DataStore setupData = app.getPersistence().loadData(setupFile);

        if (setupData == null) {
            SpecsLogs.msgLib("Could not build configuration data.");
            return -1;
        }

        int result = -1;
        try {
            result = app.getKernel().execute(setupData);
        } catch (Exception e) {
            SpecsLogs.msgWarn("(Program Exception) - " + e.getMessage(), e);
        }

        return result;
    }
}
