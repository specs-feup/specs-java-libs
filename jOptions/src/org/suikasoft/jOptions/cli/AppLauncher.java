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

package org.suikasoft.jOptions.cli;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.app.App;

import pt.up.fe.specs.util.SpecsCollections;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * Utility class for launching jOptions-based applications from the command line.
 */
public class AppLauncher {

    private final App app;
    private final List<String> resources;
    private File baseFolder;

    /**
     * Constructs an AppLauncher instance for the given application.
     *
     * @param app the application to be launched
     */
    public AppLauncher(App app) {
        this.app = app;
        resources = new ArrayList<>();
        baseFolder = null;
    }

    /**
     * Adds resources to the launcher.
     *
     * @param resources a collection of resource paths
     */
    public void addResources(Collection<String> resources) {
        this.resources.addAll(resources);
    }

    /**
     * Retrieves the application associated with this launcher.
     *
     * @return the application instance
     */
    public App getApp() {
        return app;
    }

    /**
     * Launches the application with the given arguments.
     *
     * @param args an array of command-line arguments
     * @return true if the application launched successfully, false otherwise
     */
    public boolean launch(String[] args) {
        return launch(Arrays.asList(args));
    }

    /**
     * Launches the application with the given arguments.
     *
     * @param args a list of command-line arguments
     * @return true if the application launched successfully, false otherwise
     */
    public boolean launch(List<String> args) {
        if (args.isEmpty()) {
            SpecsLogs.msgInfo("No arguments found. Please enter a configuration file, or key/value pairs.");
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
     * Parses special arguments such as base folder configuration.
     *
     * @param args a list of command-line arguments
     * @return the modified list of arguments
     */
    private List<String> parseSpecialArguments(List<String> args) {
        // If first argument is base_folder="path", create temporary file there and remove option
        String firstArg = args.get(0);
        if (firstArg.startsWith("base_folder=")) {
            firstArg = firstArg.substring("base_folder=".length());
            baseFolder = SpecsIo.existingFolder(null, firstArg);
            args = new ArrayList<>(args);
            args.remove(0);
        }

        return args;
    }

    /**
     * Launches the application in command-line mode without a setup file.
     *
     * @param args a list of command-line arguments
     * @return true if the application launched successfully, false otherwise
     */
    private boolean launchCommandLineNoSetup(List<String> args) {
        // Create empty setup data
        DataStore data = DataStore.newInstance(app.getDefinition());

        // Execute command-line mode
        commandLineWithSetup(args, data);

        return true;
    }

    /**
     * Executes the application with the given setup data and arguments.
     *
     * @param args a list of command-line arguments
     * @param setupData the setup data for the application
     */
    private void commandLineWithSetup(List<String> args, DataStore setupData) {
        new CommandLineUtils(app.getDefinition()).addArgs(setupData, args);

        File tempFile = new File(baseFolder, app.getClass().getSimpleName() + "__temp_config.data");

        app.getPersistence().saveData(tempFile, setupData, true);

        // Execute
        SpecsLogs.msgInfo("Executing application '" + app.getName() + "'.");

        execute(tempFile);

        tempFile.delete();
    }

    /**
     * Executes the application with the given setup file.
     *
     * @param setupFile the setup file containing configuration data
     * @return the result of the application execution
     */
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
            SpecsLogs.warn("(Program Exception) - " + e.getMessage(), e);
        }

        return result;
    }
}
