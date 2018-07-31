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

package org.suikasoft.jOptions.streamparser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import pt.up.fe.specs.util.SpecsEnums;
import pt.up.fe.specs.util.collections.MultiMap;
import pt.up.fe.specs.util.enums.EnumHelperWithValue;
import pt.up.fe.specs.util.providers.StringProvider;
import pt.up.fe.specs.util.utilities.LineStream;

/**
 *
 * Utility methods for parsing general-purpose information (e.g., a boolean, an enum) from a LineStream.
 *
 * <p>
 * TODO: Move to project jOptions, rename to LineStreamParsers.
 * 
 * @author JoaoBispo
 *
 */
public class LineStreamParsers {

    public static boolean oneOrZero(String aBoolean) {
        if (aBoolean.equals("1")) {
            return true;
        }

        if (aBoolean.equals("0")) {
            return false;
        }

        throw new RuntimeException("Unexpected value: '" + aBoolean + "'");
    }

    public static boolean oneOrZero(LineStream lines) {
        return oneOrZero(lines.nextLine());
    }

    public static int integer(LineStream lines) {
        return Integer.parseInt(lines.nextLine());
    }

    public static long longInt(LineStream lines) {
        return Long.parseLong(lines.nextLine());
    }

    /*
    public static <T extends Enum<T> & StringProvider> T enumFromInt(EnumHelper<T> helper, T defaultValue,
            LineStream lines) {
    
        int index = parseInt(lines);
    
        if (index >= helper.getSize()) {
            return defaultValue;
        }
    
        return helper.valueOf(index);
    }
    */
    public static <T extends Enum<T>> T enumFromOrdinal(Class<T> enumClass, LineStream lines) {
        return SpecsEnums.fromOrdinal(enumClass, integer(lines));
    }

    public static <T extends Enum<T>> T enumFromName(Class<T> enumClass, LineStream lines) {
        return SpecsEnums.fromName(enumClass, lines.nextLine());
    }

    public static <T extends Enum<T> & StringProvider> T enumFromInt(EnumHelperWithValue<T> helper,
            LineStream lines) {

        return helper.fromValue(integer(lines));
    }

    public static <T extends Enum<T> & StringProvider> T enumFromName(EnumHelperWithValue<T> helper,
            LineStream lines) {

        return helper.fromName(lines.nextLine());
    }

    public static <T extends Enum<T> & StringProvider> T enumFromValue(EnumHelperWithValue<T> helper,
            LineStream lines) {

        String value = lines.nextLine();
        return helper.fromValue(value);
    }

    /**
     * First line represents the number of enums to parse, one in each succeeding line.
     * 
     * @param helper
     * @param lines
     * @return
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

    public static <K> void checkDuplicate(String id, K key, Object value, Map<K, ?> map) {
        Object currentObject = map.get(key);
        if (currentObject != null) {
            throw new RuntimeException("Duplicate value for id '" + id + "', key '" + key + "'.\nCurrent value:" + value
                    + "\nPrevious value:" + currentObject);
        }

    }

    public static <K> void checkDuplicate(String id, K key, Set<K> set) {
        if (set.contains(key)) {
            throw new RuntimeException("Duplicate value for id '" + id + "', key '" + key + "'");
        }
    }

    public static void stringMap(String id, LineStream linestream, Map<String, String> stringMap) {
        String key = linestream.nextLine();
        String value = linestream.nextLine();

        LineStreamParsers.checkDuplicate(id, key, value, stringMap);
        stringMap.put(key, value);
    }

    /**
     * Overload that sets 'checkDuplicate' to true.
     * 
     * @param id
     * @param linestream
     * @param stringSet
     */
    public static void stringSet(String id, LineStream linestream, Set<String> stringSet) {
        stringSet(id, linestream, stringSet, true);
    }

    public static void stringSet(String id, LineStream linestream, Set<String> stringSet, boolean checkDuplicate) {
        String key = linestream.nextLine();

        if (checkDuplicate) {
            LineStreamParsers.checkDuplicate(id, key, stringSet);
        }

        stringSet.add(key);
    }

    /**
     * Overload which uses the second line as value.
     * 
     * @param lines
     * @param map
     */
    public static void multiMap(LineStream lines, MultiMap<String, String> map) {
        multiMap(lines, map, string -> string);
    }

    /**
     * Reads two lines from LineStream, the first is the key, the second is the value. Applies the given decoder to the
     * value.
     * 
     * @param lines
     * @param map
     * @param decoder
     */
    public static <T> void multiMap(LineStream lines, MultiMap<String, T> map, Function<String, T> decoder) {
        String key = lines.nextLine();
        map.put(key, decoder.apply(lines.nextLine()));
    }

    /**
     * First line represents the number of elements of the list.
     * 
     * @param lines
     * @return
     */
    public static List<String> stringList(LineStream lines) {
        return stringList(lines, LineStream::nextLine);
        // int numElements = integer(lines);
        //
        // List<String> strings = new ArrayList<>(numElements);
        // for (int i = 0; i < numElements; i++) {
        // strings.add(lines.nextLine());
        // }
        //
        // return strings;
    }

    /**
     * First line represents the number of elements of the list. Then, the given parser is applies as many times as the
     * number of elements.
     * 
     * @param lines
     * @return
     */
    public static List<String> stringList(LineStream lines, Function<LineStream, String> parser) {
        int numElements = integer(lines);

        List<String> strings = new ArrayList<>(numElements);
        for (int i = 0; i < numElements; i++) {
            strings.add(parser.apply(lines));
        }

        return strings;
    }

}
