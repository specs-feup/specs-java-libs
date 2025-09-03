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
import java.util.Collection;
import java.util.List;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.app.App;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

import com.google.common.base.Preconditions;

import pt.up.fe.specs.util.SpecsLogs;

/**
 * Utility methods for parsing and handling command-line arguments for jOptions-based applications.
 */
public class CommandLineUtils {

    private static final String ARG_WRITE = "write";
    private static final String ARG_HELP = "--help";

    private final StoreDefinition definition;

    /**
     * Constructs a CommandLineUtils instance with the given store definition.
     *
     * @param definition the store definition to be used for parsing command-line arguments
     */
    public CommandLineUtils(StoreDefinition definition) {
        this.definition = definition;
    }

    /**
     * Parses the value from a key-value argument string.
     *
     * @param arg the key-value argument string
     * @return the parsed value
     */
    private static String parseValue(String arg) {
        int index = arg.indexOf("=");
        String value = arg.substring(index + 1);
        return value;
    }

    /**
     * Parses the key from a key-value argument string.
     *
     * @param arg the key-value argument string
     * @return the parsed key, or null if the argument is invalid
     */
    private static String parseSimpleKey(String arg) {
        int index = arg.indexOf("=");
        if (index == -1) {
            SpecsLogs.msgInfo("Problem in key-value '" + arg
                    + "'. Check if key-value is separated by a '='.");
            return null;
        }

        return arg.substring(0, index);
    }

    /**
     * Launches an application in command-line mode.
     *
     * @param app  the application to be launched
     * @param args the command-line arguments
     * @return true if the application was successfully launched or a special command was processed, false otherwise
     */
    public static boolean launch(App app, List<String> args) {

        // Check for some special commands
        boolean accepted = processSpecialCommands(app, args);
        if (accepted) {
            return true;
        }

        // If at least one argument, launch application.
        if (!args.isEmpty()) {
            AppLauncher launcher = new AppLauncher(app);
            return launcher.launch(args);
        }

        // Show help message
        SpecsLogs.msgInfo(app.getName());
        SpecsLogs.msgInfo(CommandLineUtils.getHelp(app.getDefinition()));

        return false;
    }

    /**
     * Processes special commands such as "write" or "--help".
     *
     * @param app  the application instance
     * @param args the command-line arguments
     * @return true if a special command was processed, false otherwise
     */
    private static boolean processSpecialCommands(App app, List<String> args) {
        if (args.isEmpty()) {
            return false;
        }

        // Check if first argument is WRITE
        if (args.get(0).toLowerCase().equals(CommandLineUtils.ARG_WRITE)) {
            File config = new File("default.matisse");

            app.getPersistence().saveData(config, DataStore.newInstance(app.getDefinition()), false);

            SpecsLogs
                    .msgInfo("Writing default configuration file to '" + config.getName() + "'");

            return true;
        }

        boolean hasHelp = args.stream()
                .filter(arg -> arg.equals(CommandLineUtils.ARG_HELP))
                .findFirst()
                .map(arg -> true).orElse(false);

        if (hasHelp) {
            // Show help message
            SpecsLogs.msgInfo(app.getName());
            SpecsLogs.msgInfo(CommandLineUtils.getHelp(app.getDefinition()));
            return true;
        }

        return false;
    }

    /**
     * Adds command-line arguments to the given DataStore.
     *
     * @param setupData the DataStore to be updated
     * @param args      the command-line arguments
     */
    public void addArgs(DataStore setupData, List<String> args) {
        // Iterate over each argument
        for (String arg : args) {

            String keyString = parseSimpleKey(arg);
            if (keyString == null) {
                continue;
            }

            // Get string value
            String stringValue = parseValue(arg);

            Preconditions.checkArgument(!keyString.isEmpty(), "Passed empty key string");

            // Get key corresponding to the string
            DataKey<?> key = definition.getKeyMap().get(keyString);
            if (key == null) {
                SpecsLogs.msgInfo("Key '" + keyString + "' not found in store definition '"
                        + definition.getName() + "'. Keys: " + definition.getKeys());
                continue;
            }

            // Decode value
            if (!key.getDecoder().isPresent()) {
                SpecsLogs.msgInfo("No decoder found for key '" + key + "'");
                continue;
            }

            Object value = key.getDecoder().get().decode(stringValue);

            // Set value
            setupData.setRaw(key, value);
        }
    }

    /**
     * Generates a help message for the given store definition.
     *
     * @param setupDef the store definition
     * @return the help message
     */
    public static String getHelp(StoreDefinition setupDef) {
        StringBuilder builder = new StringBuilder();

        builder.append("Use: <OPTION>/<SUBOPTION1>/...=<VALUE> <OPTION>...\n\n");
        builder.append("Available options:\n(Reference - <NAME> (<TYPE> [=<DEFAULT_VALUE>]) )\n\n"
                + getHelpString(setupDef));

        return builder.toString();
    }

    /**
     * Generates a help message for the given store definition.
     *
     * @param setupDefinition the store definition
     * @return the help message
     */
    private static String getHelpString(StoreDefinition setupDefinition) {
        return getHelp(setupDefinition.getKeys());
    }

    /**
     * Generates a help message for the given collection of DataKeys.
     *
     * @param optionDefs the collection of DataKeys
     * @return the help message
     */
    private static String getHelp(Collection<DataKey<?>> optionDefs) {
        StringBuilder builder = new StringBuilder();

        for (DataKey<?> option : optionDefs) {
            builder.append(option);
            var label = option.getLabel();
            if (!label.isBlank()) {
                builder.append(" - ").append(label);
            }
            builder.append("\n");

        }

        return builder.toString();
    }
}
