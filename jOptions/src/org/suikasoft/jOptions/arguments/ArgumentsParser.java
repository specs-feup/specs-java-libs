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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.util.parsing.ListParser;

public class ArgumentsParser {

    private final Map<String, BiConsumer<ListParser<String>, DataStore>> parsers;

    public ArgumentsParser() {
        this.parsers = new LinkedHashMap<>();
    }

    /**
     * Adds a boolean key.
     * 
     * @param key
     * @param flags
     * @return
     */
    public ArgumentsParser addBool(DataKey<Boolean> key, String... flags) {
        return add(key, list -> true, flags);
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
        return add(key, list -> list.popSingle());
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
        return add(key, list -> key.getDecoder().get().decode(list.popSingle()));

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
    public <V> ArgumentsParser add(DataKey<V> key, Function<ListParser<String>, V> parser, String... flags) {
        for (String flag : flags) {
            parsers.put(flag, (list, dataStore) -> dataStore.add(key, parser.apply(list)));
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
