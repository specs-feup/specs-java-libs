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

package org.suikasoft.jOptions.streamparser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import pt.up.fe.specs.util.SpecsEnums;
import pt.up.fe.specs.util.collections.MultiMap;
import pt.up.fe.specs.util.enums.EnumHelperWithValue;
import pt.up.fe.specs.util.providers.StringProvider;
import pt.up.fe.specs.util.utilities.LineStream;

/**
 * Utility methods for parsing general-purpose information from a
 * {@link pt.up.fe.specs.util.utilities.LineStream}.
 */
public class LineStreamParsers {

    /**
     * Parses a boolean from a string, accepting "1" as true and "0" as false.
     *
     * @param aBoolean the string to parse
     * @return the boolean value
     * @throws RuntimeException if the value is not "1" or "0"
     */
    public static boolean oneOrZero(String aBoolean) {
        if (aBoolean.equals("1")) {
            return true;
        }
        if (aBoolean.equals("0")) {
            return false;
        }
        throw new RuntimeException("Unexpected value: '" + aBoolean + "'");
    }

    /**
     * Parses a boolean from the next line of the stream, accepting "1" as true and
     * "0" as false.
     *
     * @param lines the line stream
     * @return the boolean value
     */
    public static boolean oneOrZero(LineStream lines) {
        return oneOrZero(lines.nextLine());
    }

    /**
     * Parses an integer from the next line of the stream.
     *
     * @param lines the line stream
     * @return the integer value
     */
    public static int integer(LineStream lines) {
        return Integer.parseInt(lines.nextLine());
    }

    /**
     * Parses a long from the next line of the stream.
     *
     * @param lines the line stream
     * @return the long value
     */
    public static long longInt(LineStream lines) {
        return Long.parseLong(lines.nextLine());
    }

    /**
     * Parses an enum from its ordinal value in the next line of the stream.
     *
     * @param enumClass the class of the enum
     * @param lines     the line stream
     * @return the enum value
     */
    public static <T extends Enum<T>> T enumFromOrdinal(Class<T> enumClass, LineStream lines) {
        return SpecsEnums.fromOrdinal(enumClass, integer(lines));
    }

    /**
     * Parses an enum from its name in the next line of the stream.
     *
     * @param enumClass the class of the enum
     * @param lines     the line stream
     * @return the enum value
     */
    public static <T extends Enum<T>> T enumFromName(Class<T> enumClass, LineStream lines) {
        return SpecsEnums.fromName(enumClass, lines.nextLine());
    }

    /**
     * Parses an enum from its integer value in the next line of the stream.
     *
     * @param helper the enum helper
     * @param lines  the line stream
     * @return the enum value
     */
    public static <T extends Enum<T> & StringProvider> T enumFromInt(EnumHelperWithValue<T> helper,
            LineStream lines) {
        return helper.fromValue(integer(lines));
    }

    /**
     * Parses an enum from its name in the next line of the stream.
     *
     * @param helper the enum helper
     * @param lines  the line stream
     * @return the enum value
     */
    public static <T extends Enum<T> & StringProvider> T enumFromName(EnumHelperWithValue<T> helper,
            LineStream lines) {
        return helper.fromName(lines.nextLine());
    }

    /**
     * Parses an enum from its value in the next line of the stream.
     *
     * @param helper the enum helper
     * @param lines  the line stream
     * @return the enum value
     */
    public static <T extends Enum<T> & StringProvider> T enumFromValue(EnumHelperWithValue<T> helper,
            LineStream lines) {
        String value = lines.nextLine();
        return helper.fromValue(value);
    }

    /**
     * Parses a list of enums from their names in the stream. The first line
     * represents the number of enums to parse.
     *
     * @param helper the enum helper
     * @param lines  the line stream
     * @return the list of enums
     */
    public static <T extends Enum<T> & StringProvider> List<T> enumListFromName(EnumHelperWithValue<T> helper,
            LineStream lines) {
        int numEnums = integer(lines);
        List<T> enums = new ArrayList<>(numEnums);
        for (int i = 0; i < numEnums; i++) {
            enums.add(enumFromName(helper, lines));
        }
        return enums;
    }

    /**
     * Checks for duplicate keys in a map and throws an exception if a duplicate is
     * found.
     *
     * @param id    the identifier
     * @param key   the key to check
     * @param value the value associated with the key
     * @param map   the map to check
     * @param <K>   the type of the key
     */
    public static <K> void checkDuplicate(String id, K key, Object value, Map<K, ?> map) {
        Object currentObject = map.get(key);
        if (currentObject != null) {
            throw new RuntimeException("Duplicate value for id '" + id + "', key '" + key + "'.\nCurrent value:" + value
                    + "\nPrevious value:" + currentObject);
        }
    }

    /**
     * Checks for duplicate keys in a set and throws an exception if a duplicate is
     * found.
     *
     * @param id  the identifier
     * @param key the key to check
     * @param set the set to check
     * @param <K> the type of the key
     */
    public static <K> void checkDuplicate(String id, K key, Set<K> set) {
        if (set.contains(key)) {
            throw new RuntimeException("Duplicate value for id '" + id + "', key '" + key + "'");
        }
    }

    /**
     * Parses a key-value pair from the stream and adds it to the map.
     *
     * @param id         the identifier
     * @param linestream the line stream
     * @param stringMap  the map to add the key-value pair
     */
    public static void stringMap(String id, LineStream linestream, Map<String, String> stringMap) {
        String key = linestream.nextLine();
        String value = linestream.nextLine();
        LineStreamParsers.checkDuplicate(id, key, value, stringMap);
        stringMap.put(key, value);
    }

    /**
     * Parses a string from the stream and adds it to the set, checking for
     * duplicates.
     *
     * @param id         the identifier
     * @param linestream the line stream
     * @param stringSet  the set to add the string
     */
    public static void stringSet(String id, LineStream linestream, Set<String> stringSet) {
        stringSet(id, linestream, stringSet, true);
    }

    /**
     * Parses a string from the stream and adds it to the set.
     *
     * @param id             the identifier
     * @param linestream     the line stream
     * @param stringSet      the set to add the string
     * @param checkDuplicate whether to check for duplicates
     */
    public static void stringSet(String id, LineStream linestream, Set<String> stringSet, boolean checkDuplicate) {
        String key = linestream.nextLine();
        if (checkDuplicate) {
            LineStreamParsers.checkDuplicate(id, key, stringSet);
        }
        stringSet.add(key);
    }

    /**
     * Parses a key-value pair from the stream and adds it to the multi-map.
     *
     * @param lines the line stream
     * @param map   the multi-map to add the key-value pair
     */
    public static void multiMap(LineStream lines, MultiMap<String, String> map) {
        multiMap(lines, map, string -> string);
    }

    /**
     * Parses a key-value pair from the stream, applies a decoder to the value, and
     * adds it to the multi-map.
     *
     * @param lines   the line stream
     * @param map     the multi-map to add the key-value pair
     * @param decoder the decoder to apply to the value
     * @param <T>     the type of the value
     */
    public static <T> void multiMap(LineStream lines, MultiMap<String, T> map, Function<String, T> decoder) {
        String key = lines.nextLine();
        map.put(key, decoder.apply(lines.nextLine()));
    }

    /**
     * Parses a list of strings from the stream. The first line represents the
     * number of elements in the list.
     *
     * @param lines the line stream
     * @return the list of strings
     */
    public static List<String> stringList(LineStream lines) {
        return stringList(lines, LineStream::nextLine);
    }

    /**
     * Parses a list of strings from the stream using a custom parser. The first
     * line represents the number of elements in the list.
     *
     * @param lines  the line stream
     * @param parser the custom parser
     * @return the list of strings
     */
    public static List<String> stringList(LineStream lines, Function<LineStream, String> parser) {
        int numElements = integer(lines);
        List<String> strings = new ArrayList<>(numElements);
        for (int i = 0; i < numElements; i++) {
            strings.add(parser.apply(lines));
        }
        return strings;
    }

    /**
     * Parses a list of values from the stream using a custom parser. The first line
     * represents the number of elements in the list.
     *
     * @param lines  the line stream
     * @param parser the custom parser
     * @param <T>    the type of the values
     * @return the list of values
     */
    public static <T> List<T> list(LineStream lines, Function<LineStream, T> parser) {
        int numElements = integer(lines);
        List<T> values = new ArrayList<>(numElements);
        for (int i = 0; i < numElements; i++) {
            values.add(parser.apply(lines));
        }
        return values;
    }

    /**
     * Parses an optional string from the next line of the stream. If the line is
     * empty, returns an empty optional.
     *
     * @param lines the line stream
     * @return the optional string
     */
    public static Optional<String> optionalString(LineStream lines) {
        var line = lines.nextLine();
        return line.isEmpty() ? Optional.empty() : Optional.of(line);
    }
}
