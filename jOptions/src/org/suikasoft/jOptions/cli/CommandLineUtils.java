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
import java.util.Collection;
import java.util.List;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.app.App;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

import com.google.common.base.Preconditions;

import pt.up.fe.specs.util.SpecsLogs;

/**
 * @author Joao Bispo
 * 
 */
public class CommandLineUtils {

    private static final String ARG_WRITE = "write";
    private static final String ARG_HELP = "--help";

    private final StoreDefinition definition;

    public CommandLineUtils(StoreDefinition definition) {
        this.definition = definition;
    }

    /**
     * @param arg
     * @return
     */
    private static String parseValue(String arg) {
        int index = arg.indexOf("=");
        String value = arg.substring(index + 1);
        return value;
    }

    // /**
    // * @param arg
    // * @return
    // */
    // private static List<String> parseKey(String arg) {
    // int index = arg.indexOf("=");
    // if (index == -1) {
    // LoggingUtils.msgInfo("Problem in key-value '" + arg
    // + "'. Check if key-value is separated by a '='.");
    // return null;
    // }
    //
    // String keyString = arg.substring(0, index);
    // List<String> key = Arrays.asList(keyString.split("/"));
    //
    // return key;
    // }

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
     * @return
     */
    /*
    public static Map<Class<?>, Object> getDefaultValues() {
    return CommandLineUtils.DEFAULT_VALUES;
    }
    **/

    /**
     * Launches an application on command-line mode.
     * 
     * @param app
     * @param args
     */
    // public static boolean launch(App app, String... args) {
    // return launch(app, Arrays.asList(args));
    // }

    public static boolean launch(App app, List<String> args) {

        // Check for some special commands
        boolean accepted = processSpecialCommands(app, args);
        if (accepted) {
            return true;
        }

        // If at least one argument, launch application.
        // if (args.length > 0) {
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
     * @param app
     * @param args
     * @return
     */
    // private static boolean processSpecialCommands(App app, String... args) {
    // return processSpecialCommands(app, Arrays.asList(args));
    // }

    private static boolean processSpecialCommands(App app, List<String> args) {
        // if (args.length == 0) {
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

            // setValue(setupData, keyString, stringValue, definition);

            // // Discover type of key
            // DataKey<?> key = OptionUtils.getKey(setupData, keyString);
            // // Option option = OptionUtils.getOption(setupData, keyString);
            // if (key == null) {
            // LoggingUtils.msgInfo("Could not find option with key '" + keyString + "'");
            // if (setupData.getStoreDefinition().isPresent()) {
            // LoggingUtils.msgInfo("Base Keys:" + setupData.getStoreDefinition().get().getKeys());
            // }
            //
            // continue;
            // }
            //
            // // Get key to reach setup
            // List<String> keyToSetup = keyString.subList(0, keyString.size() - 1);
            //
            // // Set option
            // OptionUtils.setRawOption(setupData, keyToSetup, key, stringValue);
        }

    }

    public static String getHelp(StoreDefinition setupDef) {
        StringBuilder builder = new StringBuilder();

        builder.append("Use: <OPTION>/<SUBOPTION1>/...=<VALUE> <OPTION>...\n\n");
        builder.append("Available options:\n(Reference - <NAME> (<TYPE> [=<DEFAULT_VALUE>]) )\n\n"
                + getHelpString(setupDef));

        return builder.toString();
    }

    private static String getHelpString(StoreDefinition setupDefinition) {
        return getHelp(setupDefinition.getKeys());
    }

    /**
     * @param setupDef
     * @return
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

    // private static void setValue(DataStore setup, List<String> keyString, String stringValue,
    // StoreDefinition definition) {
    //
    // Preconditions.checkArgument(!keyString.isEmpty(), "Passed empty key string");
    //
    // // Get key corresponding to the string
    // DataKey<?> key = definition.getKeyMap().get(keyString.get(0));
    // if (key == null) {
    // LoggingUtils.msgInfo("Key '" + keyString.get(0) + "' not found in store definition '"
    // + definition.getName() + "'. Keys: " + definition.getKeys());
    // return;
    // }
    //
    // // If only one string, set value
    // if (keyString.size() == 1) {
    //
    // // Decode value
    // if (!key.getDecoder().isPresent()) {
    // LoggingUtils.msgInfo("No decoder found for key '" + key + "'");
    // return;
    // }
    //
    // Object value = key.getDecoder().get().decode(stringValue);
    //
    // setup.setRaw(key, value);
    // return;
    // }
    //
    // // If key has more than one string:
    // // 1) Next key must return a StoreDefinition
    // // 2) Key must store a DataStoreProvider
    // if (!key.getStoreDefinition().isPresent()) {
    // LoggingUtils.msgInfo("Key '" + key + "' is part of a chain, must define a StoreDefinition");
    // return;
    // }
    //
    // Optional<?> valueTry = setup.getTry(key);
    //
    // // If no value yet, invoke constructor from Definition and put back into setup
    // if (!valueTry.isPresent()) {
    // DataStoreProvider provider = key.getStoreDefinition().get().getStore();
    // setup.setRaw(key, provider);
    //
    // valueTry = Optional.of(provider);
    // }
    //
    // Object value = valueTry.get();
    //
    // if (!(value instanceof DataStoreProvider)) {
    // LoggingUtils.msgInfo("Key '" + key + "' is part of a chain, corresponding value of class '"
    // + key.getValueClass() + "' must implement DataStoreProvider");
    // return;
    // }
    //
    // StoreDefinition nextDefinition = key.getStoreDefinition().get();
    // DataStore nextDataStore = ((DataStoreProvider) value).getDataStore();
    // List<String> nextKeyString = keyString.subList(1, keyString.size());
    //
    // setValue(nextDataStore, nextKeyString, stringValue, nextDefinition);
    // }
}
