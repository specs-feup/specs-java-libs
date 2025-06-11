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
 * specific language governing permissions and limitations under the License.
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

/**
 * Parses and manages command-line arguments for jOptions-based applications.
 */
public class ArgumentsParser {

    /**
     * If true, just the help message instead of running the tool (--help, -h).
     */
    private static final DataKey<Boolean> SHOW_HELP = KeyFactory.bool("arguments_parser_show_help")
            .setLabel("Shows this help message");

    /**
     * Executes the program using the given file representing a serialized DataStore instance.
     */
    private static final DataKey<File> DATASTORE_FILE = KeyFactory.file("arguments_parser_datastore_file")
            .setLabel("Executes the program using the given file representing a serialized DataStore instance");

    /**
     * Executes the program using the given text file containing command-line options.
     */
    private static final DataKey<File> CONFIG_FILE = KeyFactory.file("arguments_parser_config_file")
            .setLabel("Executes the program using the given text file containing command-line options");

    private final Map<String, BiConsumer<ListParser<String>, DataStore>> parsers;
    private final MultiMap<DataKey<?>, String> datakeys;
    private final Map<DataKey<?>, Integer> consumedArgs;
    private final Set<String> ignoreFlags;

    /**
     * Constructs an ArgumentsParser instance and initializes default parsers and flags.
     */
    public ArgumentsParser() {
        parsers = new LinkedHashMap<>();
        datakeys = new MultiMap<>(() -> new LinkedHashMap<>());
        consumedArgs = new HashMap<>();
        ignoreFlags = new HashSet<>();

        // Automatically add help flags (-h, --help)
        addBool(SHOW_HELP, "--help", "-h");
        ignoreFlags.add("//");
    }

    /**
     * Executes the application kernel with the parsed arguments.
     *
     * @param kernel the application kernel to execute
     * @param args   the list of command-line arguments
     * @return the exit code of the application
     */
    public int execute(AppKernel kernel, List<String> args) {
        DataStore config = parse(args);

        // If --help, show message and return
        if (config.get(SHOW_HELP)) {
            printHelpMessage();
            return 0;
        }

        return kernel.execute(config);
    }

    /**
     * Prints the help message for the command-line arguments, listing all supported flags and their descriptions.
     */
    private void printHelpMessage() {
        StringBuilder message = new StringBuilder();
        for (DataKey<?> key : datakeys.keySet()) {
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

    /**
     * Parses the given list of command-line arguments into a DataStore instance.
     *
     * @param args the list of command-line arguments
     * @return a DataStore instance containing the parsed arguments
     */
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
     * Adds a boolean key to the parser, associating it with the given flags.
     *
     * @param key   the DataKey representing the boolean value
     * @param flags the flags associated with the key
     * @return the updated ArgumentsParser instance
     */
    public ArgumentsParser addBool(DataKey<Boolean> key, String... flags) {
        return addPrivate(key, list -> true, 0, flags);
    }

    /**
     * Adds a key that uses the next argument as a value, associating it with the given flags.
     *
     * @param key   the DataKey representing the string value
     * @param flags the flags associated with the key
     * @return the updated ArgumentsParser instance
     */
    public ArgumentsParser addString(DataKey<String> key, String... flags) {
        return addPrivate(key, list -> list.popSingle(), 1, flags);
    }

    /**
     * Uses the key's decoder to parse the next argument, associating it with the given flags.
     *
     * @param key   the DataKey representing the value
     * @param flags the flags associated with the key
     * @param <V>   the value type
     * @return the updated ArgumentsParser instance
     */
    public <V> ArgumentsParser add(DataKey<V> key, String... flags) {
        return add(key, list -> key.getDecoder().get().decode(list.popSingle()), 1, flags);
    }

    /**
     * Accepts a custom parser for the next argument, associating it with the given flags.
     *
     * @param key          the DataKey representing the value
     * @param parser       the custom parser function
     * @param consumedArgs the number of arguments consumed by the parser
     * @param flags        the flags associated with the key
     * @param <V>          the value type
     * @return the updated ArgumentsParser instance
     */
    @SuppressWarnings("unchecked")
    public <V> ArgumentsParser add(DataKey<V> key, Function<ListParser<String>, V> parser, Integer consumedArgs,
            String... flags) {

        // Check if value of the key is of type Boolean
        if (key.getValueClass().equals(Boolean.class)) {
            return addBool((DataKey<Boolean>) key, flags);
        }

        // Check if value of the key is of type String
        if (key.getValueClass().equals(String.class)) {
            return addString((DataKey<String>) key, flags);
        }

        return addPrivate(key, parser, consumedArgs, flags);
    }

    /**
     * Adds a key with a custom parser and flags (internal helper).
     *
     * @param key          the DataKey representing the value
     * @param parser       the custom parser function
     * @param consumedArgs the number of arguments consumed by the parser
     * @param flags        the flags associated with the key
     * @param <V>          the value type
     * @return the updated ArgumentsParser instance
     */
    private <V> ArgumentsParser addPrivate(DataKey<V> key, Function<ListParser<String>, V> parser, Integer consumedArgs,
            String... flags) {

        for (String flag : flags) {
            if (parsers.containsKey(flag)) {
                throw new RuntimeException("There is already a mapping for flag '" + flag + "'");
            }

            parsers.put(flag, (list, dataStore) -> dataStore.add(key, parser.apply(list)));
        }

        datakeys.put(key, Arrays.asList(flags));
        this.consumedArgs.put(key, consumedArgs);

        return this;
    }

    /**
     * Adds flags to be ignored during parsing.
     *
     * @param ignoreFlags the flags to ignore
     * @return the updated ArgumentsParser instance
     */
    public ArgumentsParser addIgnore(String... ignoreFlags) {
        for (String ignoreFlag : ignoreFlags) {
            this.ignoreFlags.add(ignoreFlag);
        }

        return this;
    }
}
