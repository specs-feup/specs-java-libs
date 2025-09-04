/*
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
 * @deprecated This class was created when the code base was still using Java
 *             5.0. With Java 7, the Diamond Operator and the Collections
 *             methods, this class should no longer be used.
 *             Consider using Guava classes in com.google.common.collect, such
 *             as Maps, Lists, etc.
 *
 *             Factory methods for common objects, such as the ones in Java
 *             Collections.
 *
 *             <p>
 *             The purpose of theses methods is to avoid writing the generic
 *             type when creating a new class.
 *
 *             <p>
 *             IMPORTANT: Instead of using this class, consider using Guava
 *             classes in com.google.common.collect, such as Maps, Lists, etc.
 *
 *             <p>
 *             PS.: This class was created when the code base was still using
 *             Java 5.0. With Java 7, the Diamond Operator and the Collections
 *             methods, this class should no longer be used.
 *
 * @author Joao Bispo
 */
@Deprecated
public class SpecsFactory {

    /**
     * Creates a list of the given class type, containing 'elements'.
     *
     * @param listClass the class type of the list elements
     * @param elements  the elements to be added to the list
     * @return a list containing the given elements
     */
    public static <T> List<T> asList(Class<T> listClass, Object... elements) {
        List<T> list = new ArrayList<>();

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

    /**
     * Creates a new ArrayList.
     *
     * @param <T> the type of elements in the list
     * @return a new ArrayList
     */
    public static <T> List<T> newArrayList() {
        return new ArrayList<>();
    }

    /**
     * Creates a new ArrayList with the specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the list
     * @param <T>             the type of elements in the list
     * @return a new ArrayList
     */
    public static <T> List<T> newArrayList(int initialCapacity) {
        return new ArrayList<>(initialCapacity);
    }

    /**
     * Creates a new ArrayList containing the elements of the specified collection.
     *
     * @param elements the collection whose elements are to be placed into the list
     * @param <T>      the type of elements in the list
     * @return a new ArrayList
     */
    public static <T> List<T> newArrayList(Collection<? extends T> elements) {
        return new ArrayList<>(elements);
    }

    /**
     * Creates a new LinkedList.
     *
     * @param <T> the type of elements in the list
     * @return a new LinkedList
     */
    public static <T> List<T> newLinkedList() {
        return new LinkedList<>();
    }

    /**
     * Creates a new LinkedList containing the elements of the specified collection.
     *
     * @param elements the collection whose elements are to be placed into the list
     * @param <T>      the type of elements in the list
     * @return a new LinkedList
     */
    public static <T> List<T> newLinkedList(Collection<? extends T> elements) {
        return new LinkedList<>(elements);
    }

    /**
     * Creates a new HashMap.
     *
     * @param <K> the type of keys in the map
     * @param <V> the type of values in the map
     * @return a new HashMap
     */
    public static <K, V> Map<K, V> newHashMap() {
        return new HashMap<>();
    }

    /**
     * Creates a new HashMap containing the mappings of the specified map.
     *
     * @param map the map whose mappings are to be placed into the new map
     * @param <K> the type of keys in the map
     * @param <V> the type of values in the map
     * @return a new HashMap
     */
    public static <K, V> Map<K, V> newHashMap(Map<? extends K, ? extends V> map) {
        if (map == null) {
            return Collections.emptyMap();
        }

        return new HashMap<>(map);
    }

    /**
     * Creates a new LinkedHashMap.
     *
     * @param <K> the type of keys in the map
     * @param <V> the type of values in the map
     * @return a new LinkedHashMap
     */
    public static <K, V> Map<K, V> newLinkedHashMap() {
        return new LinkedHashMap<>();
    }

    /**
     * Creates a new EnumMap for the specified key class.
     *
     * @param keyClass the class of the keys in the map
     * @param <K>      the type of keys in the map
     * @param <V>      the type of values in the map
     * @return a new EnumMap
     */
    public static <K extends Enum<K>, V> Map<K, V> newEnumMap(Class<K> keyClass) {
        return new EnumMap<>(keyClass);
    }

    /**
     * Creates a new HashSet containing the elements of the specified collection.
     *
     * @param elements the collection whose elements are to be placed into the set
     * @param <T>      the type of elements in the set
     * @return a new HashSet
     */
    public static <T> Set<T> newHashSet(Collection<? extends T> elements) {
        return new HashSet<>(elements);
    }

    /**
     * Creates a new HashSet.
     *
     * @param <T> the type of elements in the set
     * @return a new HashSet
     */
    public static <T> Set<T> newHashSet() {
        return new HashSet<>();
    }

    /**
     * Creates a new LinkedHashMap containing the mappings of the specified map.
     *
     * @param elements the map whose mappings are to be placed into the new map
     * @param <K>      the type of keys in the map
     * @param <V>      the type of values in the map
     * @return a new LinkedHashMap
     */
    public static <K, V> Map<K, V> newLinkedHashMap(Map<? extends K, ? extends V> elements) {
        return new LinkedHashMap<>(elements);
    }

    /**
     * Creates a new LinkedHashSet.
     *
     * @param <T> the type of elements in the set
     * @return a new LinkedHashSet
     */
    public static <T> Set<T> newLinkedHashSet() {
        return new LinkedHashSet<>();
    }

    /**
     * Creates a new LinkedHashSet containing the elements of the specified
     * collection.
     *
     * @param elements the collection whose elements are to be placed into the set
     * @param <T>      the type of elements in the set
     * @return a new LinkedHashSet
     */
    public static <T> Set<T> newLinkedHashSet(Collection<? extends T> elements) {
        return new LinkedHashSet<>(elements);
    }

    /**
     * Returns an InputStream for the specified file.
     *
     * @param file the file to be read
     * @return an InputStream for the file, or null if the file is not found
     */
    public static InputStream getStream(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            SpecsLogs.warn("Could not find file '" + file + "'");
            return null;
        }
    }

    /**
     * Returns an empty map if the given map is null.
     *
     * @param map the map to be checked
     * @param <K> the type of keys in the map
     * @param <V> the type of values in the map
     * @return the original map, or an empty map if the original map is null
     */
    public static <K, V> Map<K, V> assignMap(Map<K, V> map) {

        if (map == null) {
            return Collections.emptyMap();
        }

        return map;
    }

    /**
     * Builds a set with a sequence of integers starting at 'startIndex' and with
     * 'size' integers.
     *
     * @param startIndex the starting index of the sequence
     * @param size       the number of integers in the sequence
     * @return a set containing the sequence of integers
     */
    public static Set<Integer> newSetSequence(int startIndex, int size) {
        Set<Integer> set = new HashSet<>();

        for (int i = startIndex; i < startIndex + size; i++) {
            set.add(i);
        }

        return set;
    }

    /**
     * Converts an array of int to a List of Integer.
     *
     * @param array the original int array
     * @return a List of Integer
     */
    public static List<Integer> fromIntArray(int[] array) {

        List<Integer> intList = new ArrayList<>();

        for (int i : array) {
            intList.add(i);
        }

        return intList;
    }

    /**
     * If the given value is null, returns an empty collection. Otherwise, returns
     * an unmodifiable view of the list.
     *
     * <p>
     * This method is useful for final fields whose contents do not need to be
     * changed.
     *
     * @param aList the list to be checked
     * @param <T>   the type of elements in the list
     * @return an unmodifiable view of the list, or an empty list if the original
     *         list is null or empty
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
     * Method similar to Collections.addAll, but that accepts 'null' as the source
     * argument.
     *
     * <p>
     * If the source argument is null, the collection sink remains unmodified.
     *
     * @param sink   the collection to which elements are to be added
     * @param source the collection whose elements are to be added to the sink
     * @param <T>    the type of elements in the collections
     */
    public static <T> void addAll(Collection<T> sink, Collection<? extends T> source) {
        if (source == null) {
            return;
        }

        // Add all elements in source
        sink.addAll(source);
    }
}
