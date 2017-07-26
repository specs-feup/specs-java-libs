/**
 * Copyright 2012 SPeCS Research Group.
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

package pt.up.fe.specs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Factory methods for common objects, such as the ones in Java Collections.
 * 
 * <p>
 * The purpose of theses methods is to avoid writing the generic type when creating a new class.
 * 
 * <p>
 * IMPORTANT: Instead of using this class, consider using Guava classes in com.google.common.collect, such as Maps,
 * Lists, etc.
 * 
 * <p>
 * PS.: This class was created when the code base was still using Java 5.0. With Java 7, the Diamond Operator and the
 * Collections methods, this class should no longer be used.
 * 
 * @author Joao Bispo
 * 
 */
public class SpecsFactory {

    /**
     * Creates a list of the given class type, containing 'elements'.
     * 
     * @param listClass
     * @param elements
     * @return
     */
    // public static <T, U extends T> List<T> asList(Class<T> listClass, U... elements) {
    // public static <T, U extends T> List<T> asList(U... elements) {
    public static <T> List<T> asList(Class<T> listClass, Object... elements) {
        List<T> list = SpecsFactory.newArrayList();

        for (Object element : elements) {
            if (listClass.isInstance(element)) {
                list.add(listClass.cast(element));
            } else {
                throw new RuntimeException("Object '" + element + "' is not an instance of '" + listClass.getName()
                        + "'");
            }
        }

        return list;
    }

    // TODO: These classes are no longer needed after Java 8
    public static <T> List<T> newArrayList() {
        return new ArrayList<>();
    }

    public static <T> List<T> newArrayList(int initialCapacity) {
        return new ArrayList<>(initialCapacity);
    }

    public static <T> List<T> newArrayList(Collection<? extends T> elements) {
        return new ArrayList<>(elements);
    }

    public static <T> List<T> newLinkedList() {
        return new LinkedList<>();
    }

    public static <T> List<T> newLinkedList(Collection<? extends T> elements) {
        return new LinkedList<>(elements);
    }

    public static <K, V> Map<K, V> newHashMap() {
        return new HashMap<>();
    }

    /**
     * @param map
     *            if null, uses empty map
     * @return
     */
    public static <K, V> Map<K, V> newHashMap(Map<? extends K, ? extends V> map) {
        if (map == null) {
            return Collections.emptyMap();
        }

        return new HashMap<>(map);
    }

    public static <K, V> Map<K, V> newLinkedHashMap() {
        return new LinkedHashMap<>();
    }

    public static <K extends Enum<K>, V> Map<K, V> newEnumMap(Class<K> keyClass) {
        return new EnumMap<>(keyClass);
    }

    public static <T> Set<T> newHashSet(Collection<? extends T> elements) {
        return new HashSet<>(elements);
    }

    public static <T> Set<T> newHashSet() {
        return new HashSet<>();
    }

    public static <K, V> Map<K, V> newLinkedHashMap(Map<? extends K, ? extends V> elements) {
        return new LinkedHashMap<>(elements);
    }

    public static <T> Set<T> newLinkedHashSet() {
        return new LinkedHashSet<>();
    }

    public static <T> Set<T> newLinkedHashSet(Collection<? extends T> elements) {
        return new LinkedHashSet<>(elements);
    }

    public static InputStream getStream(File file) {
        try {
            InputStream stream = new FileInputStream(file);
            return stream;
        } catch (FileNotFoundException e) {
            SpecsLogs.msgWarn("Could not find file '" + file + "'");
            return null;
        }
    }

    /**
     * Returns an empty map if the given map is null
     * 
     * @param map
     * @return
     */
    public static <K, V> Map<K, V> assignMap(Map<K, V> map) {

        if (map == null) {
            return Collections.emptyMap();
        }

        return map;
    }

    /**
     * Builds a set with a sequence of integers starting at 'startIndex' and with 'size' integers.
     * 
     * @param i
     * @param size
     * @return
     */
    public static Set<Integer> newSetSequence(int startIndex, int size) {
        Set<Integer> set = SpecsFactory.newHashSet();

        for (int i = startIndex; i < startIndex + size; i++) {
            set.add(i);
        }

        return set;
    }

    /**
     * Converts an array of int to a List of Integer.
     * 
     * @param array
     *            - the original int array ( <code>int[]</code> )
     * @return a {@link List}<{@link Integer}>
     */
    public static List<Integer> fromIntArray(int[] array) {

        List<Integer> intList = SpecsFactory.newArrayList();

        for (int index = 0; index < array.length; index++) {
            intList.add(array[index]);
        }

        return intList;
    }

    /**
     * If the given value is null, returns an empty collection. Otherwise, returns an unmodifiable view of the list.
     * 
     * <p>
     * This method is useful for final fields whose contents do not need to be changed.
     * 
     * @param aList
     * @return
     */
    public static <T> List<T> getUnmodifiableList(List<T> aList) {
        if (aList == null) {
            return Collections.emptyList();
        }
        if (aList.isEmpty()) {
            return Collections.emptyList();
        }

        return Collections.unmodifiableList(aList);
    }

    /**
     * Method similar to Collections.addAll, but that accepts 'null' as the source argument.
     * 
     * <p>
     * If the source argument is null, the collection sink remains unmodified.
     * 
     * @param sink
     * @param source
     */
    public static <T> void addAll(Collection<T> sink, Collection<? extends T> source) {
        if (source == null) {
            return;
        }

        // Add all elements in source
        sink.addAll(source);
    }

    /**
     * Parses the given list and returns an unmodifiable view of the list.
     * 
     * @param aList
     * @return
     */
    /*
    public static <T> List<T> getUnmodifiableList(List<T> aList) {
    List<T> parsedList = parseList(aList);
    if (parsedList.isEmpty()) {
        return parsedList;
    }
    
    return Collections.unmodifiableList(aList);
    }
     */
}
