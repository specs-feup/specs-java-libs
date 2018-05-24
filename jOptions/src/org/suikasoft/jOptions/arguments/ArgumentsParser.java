/**
 * Copyright 2018 SPeCS.
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

package org.suikasoft.jOptions.arguments;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.app.AppKernel;

import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.collections.MultiMap;
import pt.up.fe.specs.util.parsing.ListParser;

public class ArgumentsParser {

    /**
     * If true, just the help message instead of running the tool (--help, -h).
     */
    private static final DataKey<Boolean> SHOW_HELP = KeyFactory.bool("arguments_parser_show_help")
            .setLabel("Shows this help message");

    private static final DataKey<File> DATASTORE_FILE = KeyFactory.file("arguments_parser_datastore_file")
            .setLabel("Executes the program using the given file representing a serialized DataStore instance");

    private static final DataKey<File> CONFIG_FILE = KeyFactory.file("arguments_parser_config_file")
            .setLabel("Executes the program using the given text file containig command-line options");

    private final Map<String, BiConsumer<ListParser<String>, DataStore>> parsers;
    private final MultiMap<DataKey<?>, String> datakeys;
    private final Map<DataKey<?>, Integer> consumedArgs;
    private final Set<String> ignoreFlags;

    public ArgumentsParser() {
        parsers = new LinkedHashMap<>();
        datakeys = new MultiMap<>(() -> new LinkedHashMap<>());
        consumedArgs = new HashMap<>();
        ignoreFlags = new HashSet<>();

        // Automatically add help flags (-h, --help)
        addBool(SHOW_HELP, "--help", "-h");
        ignoreFlags.add("//");
    }

    public int execute(AppKernel kernel, List<String> args) {
        DataStore config = parse(args);

        // If --help, show message and return
        if (config.get(SHOW_HELP)) {
            printHelpMessage();
            return 0;
        }

        return kernel.execute(config);
    }

    private void printHelpMessage() {
        StringBuilder message = new StringBuilder();
        // message.append("EclipseBuild - Generates and runs ANT scripts for Eclipse Java projects\n\n");
        // message.append("Usage: <folder> [-i <ivySetting>] [-u <userLibraries>] <folder> [-i...\n\n");
        // message.append(
        // "Default files that will be searched for in the root of the repository folders if no flag is specified:\n");
        // message.append(" ").append(getDefaultUserLibraries()).append(" - Eclipse user libraries\n");
        // message.append(" ").append(getDefaultIvySettingsFile()).append(" - Ivy settings file\n");
        // message.append(" ").append(getDefaultIgnoreProjectsFile())
        // .append(" - Text file with list of projects to ignore (one project name per line)\n");
        //
        // message.append("\nAdditional options:\n");
        for (DataKey<?> key : datakeys.keySet()) {
            // for (String key : parsers.keySet()) {
            String flags = datakeys.get(key).stream().collect(Collectors.joining(", "));
            message.append(" ").append(flags);

            Integer consumedArgs = this.consumedArgs.get(key);
            for (int i = 0; i < consumedArgs; i++) {
                message.append(" <arg").append(i + 1).append(">");
            }

            String label = key.getLabel();
            if (!label.isEmpty()) {
                message.append(": ").append(label);
            }
            message.append("\n");
        }

        // Print message
        SpecsLogs.msgInfo(message.toString());
    }

    public DataStore parse(List<String> args) {
        DataStore parsedData = DataStore.newInstance("ArgumentsParser Data");

        // List with items that will be consumed during parsing of arguments
        ListParser<String> currentArgs = new ListParser<>(args);
        while (!currentArgs.isEmpty()) {
            String currentArg = currentArgs.popSingle();

            // Check if there is a flag for the current string
            if (parsers.containsKey(currentArg)) {
                parsers.get(currentArg).accept(currentArgs, parsedData);
                continue;
            }

            // Check if ignore flag
            if (ignoreFlags.contains(currentArg)) {
                // Discard next element and continue
                currentArgs.popSingle();
                continue;
            }

            // No mapping found, throw exception
            SpecsLogs.msgInfo("Command-line option not supported: '" + currentArg + "'");
            SpecsLogs.msgInfo("Use argument -h or --help to see available options.");
        }

        return parsedData;
    }

    /**
     * Adds a boolean key.
     * 
     * @param key
     * @param flags
     * @return
     */
    public ArgumentsParser addBool(DataKey<Boolean> key, String... flags) {
        return add(key, list -> true, 0, flags);
        // for (String flag : flags) {
        // parsers.put(flag, addValue(key, true));
        // }
        //
        // return this;
    }

    /**
     * Adds a key that uses the next argument as value.
     * 
     * @param key
     * @param flags
     * @return
     */
    public ArgumentsParser addString(DataKey<String> key, String... flags) {
        return add(key, list -> list.popSingle(), 1, flags);
        // for (String flag : flags) {
        // parsers.put(flag, addValueFromList(key, ListParser::popSingle));
        // }
        //
        // return this;
    }

    /**
     * Uses the key's decoder to parse the next argument.
     * 
     * @param key
     * @param flags
     * @return
     */
    public <V> ArgumentsParser add(DataKey<V> key, String... flags) {
        return add(key, list -> key.getDecoder().get().decode(list.popSingle()), 1, flags);

        // for (String flag : flags) {
        // parsers.put(flag, (list, dataStore) -> dataStore.add(key, key.getDecoder().get().decode(list.popSingle())));
        // }
        //
        // return this;
    }

    /**
     * Accepts a custom parser for the next argument.
     * 
     * @param key
     * @param parser
     * @param flags
     * @return
     */
    public <V> ArgumentsParser add(DataKey<V> key, Function<ListParser<String>, V> parser, Integer consumedArgs,
            String... flags) {
        for (String flag : flags) {
            if (parsers.containsKey(flag)) {
                throw new RuntimeException("There is already a mapping for flag '" + flag + "'");
            }

            parsers.put(flag, (list, dataStore) -> dataStore.add(key, parser.apply(list)));
            // datakeys.put(flag, key);
            // this.consumedArgs.put(flag, consumedArgs);
        }

        datakeys.put(key, Arrays.asList(flags));
        this.consumedArgs.put(key, consumedArgs);

        return this;
    }

    public ArgumentsParser addIgnore(String... ignoreFlags) {
        for (String ignoreFlag : ignoreFlags) {
            this.ignoreFlags.add(ignoreFlag);
        }

        return this;
    }

    /**
     * Helper method for options that do not consume parameters and just return a value for a given key.
     * 
     * @param key
     * @param value
     * @return
     */
    // private static <V> BiConsumer<ListParser<String>, DataStore> addValue(DataKey<V> key, V value) {
    // return (list, dataStore) -> dataStore.add(key, value);
    // }

    /**
     * Helper method for options that consume parameters.
     * 
     * @param key
     * @param processArgs
     * @return
     */
    // private static <V> BiConsumer<ListParser<String>, DataStore> addValueFromList(DataKey<V> key,
    // Function<ListParser<String>, V> processArgs) {
    //
    // return (list, dataStore) -> {
    // V value = processArgs.apply(list);
    // dataStore.add(key, value);
    // };
    //
    // }

}
