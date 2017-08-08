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

package pt.up.fe.specs.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pt.up.fe.specs.util.providers.KeyProvider;

/**
 * Utility methods related to Java Collections.
 * 
 * @author Joao Bispo
 * 
 */
public class SpecsCollections {

    /**
     * Returns the elements from the given index, until the end of the list.
     * 
     * @param list
     * @param startIndex
     * @return
     */
    public static <E> List<E> subList(List<E> list, int startIndex) {
        return list.subList(startIndex, list.size());
    }

    public static <K, V> Map<V, K> invertMap(Map<K, V> map) {
        Map<V, K> invertedMap = SpecsFactory.newHashMap();

        for (K key : map.keySet()) {
            V value = map.get(key);

            K previousKey = invertedMap.put(value, key);
            if (previousKey != null) {
                SpecsLogs.msgWarn("There are two keys ('" + key + "' and '" + previousKey
                        + "') for the same value '" + value + "'");
                return null;
            }
        }

        return invertedMap;
    }

    /**
     * Returns the last element of the list, or null if the list is empty.
     * 
     * @param lines
     * @return
     */
    public static <K> K last(List<K> lines) {
        if (lines.isEmpty()) {
            // TODO: Throw exception instead of returning null
            SpecsLogs.msgWarn(
                    "This method should not be called with empty lists, if the list can be empty call lastTry() instead");
            return null;
        }

        return lines.get(lines.size() - 1);
    }

    /**
     * Returns the last element of the list, or an empty Optional if the list is empty.
     * 
     * @param lines
     * @return
     */
    public static <K> Optional<K> lastTry(List<K> lines) {
        if (lines.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(lines.get(lines.size() - 1));
    }

    /**
     * Returns the element of the list, if it has exactly one. Empty otherwise.
     */
    public static <T> Optional<T> singleTry(List<T> list) {
        if (list.size() != 1) {
            return Optional.empty();
        }

        return Optional.of(list.get(0));
    }

    /**
     * Creates an Iterable from an iterator, so it can be used in for:each loops.
     * 
     * @param iterator
     * @return
     */
    public static <K> Iterable<K> iterable(final Iterator<K> iterator) {
        return () -> iterator;
    }

    /*
    public static <T, K extends T> List<T> asListSame(List<K> elements) {
    List<T> list = FactoryUtils.newArrayList();
    
    for (K element : elements) {
        list.add(element);
    }
    
    return list;
    }
     */

    /**
     * 
     * @param a
     * @return
     */
    @SafeVarargs
    public static <T> Set<T> asSet(T... a) {
        return new HashSet<>(Arrays.asList(a));
    }

    /**
     * If an element is null, ignores it.
     * 
     * @param superClass
     * @param elements
     * @return
     */
    public static <T> List<T> asListT(Class<T> superClass, Object... elements) {
        List<T> list = SpecsFactory.newArrayList();

        for (Object element : elements) {
            if (element == null) {
                continue;
            }

            if (superClass.isInstance(element)) {
                list.add(superClass.cast(element));
            } else {
                SpecsLogs.msgWarn("Could not add object of class '" + element.getClass() + "' to list of '"
                        + superClass + "'");
            }
        }

        return list;
    }

    public static <T extends KeyProvider<K>, K> List<K> getKeyList(List<T> providers) {
        // public static <K> List<K> asList(List<KeyProvider<K>> providers) {
        List<K> list = SpecsFactory.newArrayList();

        for (T provider : providers) {
            list.add(provider.getKey());
        }

        return list;
    }

    /**
     * Creates a new list sorted list from the given collection.
     * 
     * @param keySet
     * @return
     */
    public static <T extends Comparable<? super T>> List<T> newSorted(Collection<T> collection) {
        // Create list
        List<T> list = SpecsFactory.newArrayList(collection);

        // Sort list
        Collections.sort(list);

        return list;
    }

    /**
     * Removes the tokens from the list from startIndex, inclusive, to endIndex, exclusive.
     * 
     * @param list
     * @param startIndex
     * @param endIndex
     */
    public static <T> List<T> remove(List<T> list, int startIndex, int endIndex) {
        List<T> removedElements = SpecsFactory.newArrayList();

        for (int i = endIndex - 1; i >= startIndex; i--) {
            removedElements.add(list.remove(i));
        }

        return removedElements;
    }

    public static <T> List<T> remove(List<T> list, List<Integer> indexes) {
        // Sort indexes
        Collections.sort(indexes);

        List<T> removedElements = SpecsFactory.newArrayList();

        for (int i = indexes.size() - 1; i >= 0; i--) {
            int index = indexes.get(i);
            removedElements.add(list.remove(index));
        }

        return removedElements;
    }

    /**
     * Removes from the list the elements that match the predicate, returns the removed elements.
     * 
     * @param list
     * @param filter
     * @return
     */
    public static <T> List<T> remove(List<T> list, Predicate<T> filter) {

        List<T> removedElements = new ArrayList<>();

        Iterator<T> iterator = list.iterator();
        while (iterator.hasNext()) {
            T element = iterator.next();
            if (filter.test(element)) {
                iterator.remove();
                removedElements.add(element);
            }
        }

        return removedElements;
    }

    /**
     * Returns the first index of object that is an instance of the given class. Returns -1 if no object is found that
     * is instance of the class.
     * 
     * @param aClass
     * @param types
     * 
     * @return
     */
    public static <T> int getFirstIndex(List<? super T> list, Class<T> aClass) {
        for (int i = 0; i < list.size(); i++) {
            if (aClass.isInstance(list.get(i))) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Returns the first object that is an instance of the given class. Returns null if no object is found that is
     * instance of the class.
     * 
     * @param aClass
     * @param types
     * 
     * @return
     */
    public static <T> T getFirst(List<? super T> list, Class<T> aClass) {

        for (int i = 0; i < list.size(); i++) {
            Object obj = list.get(i);
            if (aClass.isInstance(obj)) {
                return aClass.cast(obj);
            }
        }

        return null;
    }

    /**
     * Casts an element of a list to the given class.
     * 
     * @param aClass
     * @param list
     * @param index
     * @return
     */
    /*
    public static <T> T get(Class<T> aClass, List<? super T> list, int index) {
    
    Object element = list.get(index);
    
    return aClass.cast(element);
    }
     */

    /**
     * Returns true if all the elements in the list are instances of the given class.
     * 
     * @param inputTypes
     * @return
     */
    public static <T> boolean areOfType(Class<T> aClass, List<? super T> list) {
        for (Object object : list) {
            if (!aClass.isInstance(object)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Adds the elements of the provider collection to the receiver collection. Returns the receiver collection.
     * 
     * @param receiver
     * @param provider
     * @return
     */
    public static <U, T extends Collection<U>> T add(T receiver, T provider) {
        receiver.addAll(provider);
        return receiver;
    }

    /**
     * Casts a list of one type to another type, and checks if all elements can be cast to the target type.
     * 
     * @param list
     * @param aClass
     * 
     * @return
     */
    public static <T> List<T> cast(List<?> list, Class<T> aClass) {
        // Verify if all elements implement the type of the class
        Optional<?> invalidElement = list.stream()
                .filter(element -> !aClass.isInstance(element))
                .findFirst();

        if (invalidElement.isPresent()) {
            throw new RuntimeException(
                    "Cannot cast list, has an element of type '" + invalidElement.get().getClass()
                            + "' which is not an instance of '" + aClass + "'");
        }

        return castUnchecked(list, aClass);
    }

    /**
     * Casts a list of one type to another type, without checking if the elements can be cast to the target type.
     * 
     * @param list
     * @param aClass
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> castUnchecked(List<?> list, Class<T> aClass) {

        return (List<T>) list;
        /*
        List<T> newList = new ArrayList<>();
        
        list.forEach(element -> newList.add(aClass.cast(element)));
        
        return newList;
        */
    }

    /**
     * Helper method which receives an element.
     * 
     * <p>
     * If the element is null, list remains the same.
     * 
     * @param list
     * @param element
     * @return
     */
    public static <K> List<K> concat(Collection<? extends K> list, K element) {
        return concat(list, ofNullable(element));
    }

    /**
     * Helper method which receives an element.
     * 
     * <p>
     * If the element is null, list remains the same.
     * 
     * @param element
     * @param list
     * @return
     */
    public static <K> List<K> concat(K element, Collection<? extends K> list) {
        return concat(ofNullable(element), list);
    }

    public static <K> List<K> concat(Collection<? extends K> list1, Collection<? extends K> list2) {
        List<K> newList = new ArrayList<>(list1.size() + list2.size());
        newList.addAll(list1);
        newList.addAll(list2);

        return newList;
    }

    /**
     * Creates a list with the elements from the given collections.
     * 
     * @param collections
     * @return
     */
    @SafeVarargs
    public static <K> List<K> concatLists(Collection<? extends K>... collections) {
        int totalSize = 0;
        for (Collection<?> collection : collections) {
            totalSize += collection.size();
        }

        List<K> newList = new ArrayList<>(totalSize);
        for (Collection<? extends K> collection : collections) {
            newList.addAll(collection);
        }

        return newList;
    }

    /**
     * Converts an array from one type to another.
     * 
     * @param origin
     * @param destination
     * @param converter
     * @return
     */
    public static <O, D> D[] convert(O[] origin, D[] destination, Function<O, D> converter) {

        // D[] destination = (D[]) new Object[origin.length];

        for (int i = 0; i < origin.length; i++) {
            destination[i] = converter.apply(origin[i]);
        }

        return destination;
    }

    /**
     * Turns an Optional<T> into a Stream<T> of length zero or one depending upon whether a value is present.
     * 
     * <p>
     * Source: http://stackoverflow.com/questions/22725537/using-java-8s-optional-with-streamflatmap
     */
    public static <T> Stream<T> toStream(Optional<T> opt) {
        if (opt.isPresent()) {
            return Stream.of(opt.get());
        }

        return Stream.empty();
    }

    /**
     * Filters the elements of a Collection according to a map function over the elements of that collection.
     * 
     * @param elements
     * @param mapFunction
     * @return
     */
    public static <T, F> List<T> filter(Collection<T> elements, Function<T, F> mapFunction) {

        Set<F> seenElements = new HashSet<>();
        return elements.stream()
                // Add to set. If already in set, will return false and filter the node
                .filter(element -> seenElements.add(mapFunction.apply(element)))
                .collect(Collectors.toList());

    }

    public static <T, M> List<M> map(List<T> list, Function<T, M> mapper) {
        return list.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    /**
     * Removes all the elements at the head that are an instance of the given class, returns a new list with those
     * elements.
     * 
     * @param aClass
     * @param list
     * @return
     */
    public static <T, ET extends T> List<ET> pop(List<T> list, Class<ET> aClass) {
        if (list.isEmpty()) {
            return Collections.emptyList();
        }

        List<ET> newList = new ArrayList<>();

        // While head is of the specified type, move to the new list
        while (aClass.isInstance(list.get(0))) {
            newList.add(aClass.cast(list.remove(0)));

            if (list.isEmpty()) {
                break;
            }
        }

        return newList;
    }

    public static <T> List<T> pop(List<T> elements, int numElementsToPop) {
        Preconditions.checkArgument(elements.size() >= numElementsToPop,
                "List has " + elements.size() + " elements, and want to pop " + numElementsToPop);

        List<T> poppedElements = new ArrayList<>();
        for (int i = 0; i < numElementsToPop; i++) {
            poppedElements.add(elements.remove(0));
        }

        return poppedElements;
    }

    /**
     * Removes the first element of the list, checking if it is of the given class.
     * 
     * @param list
     * @param aClass
     * @return
     */
    public static <T, ET extends T> ET popSingle(List<T> list, Class<ET> aClass) {
        if (list.isEmpty()) {
            throw new RuntimeException("List is empty");
        }

        return aClass.cast(list.remove(0));
    }

    /**
     * Returns all the elements at the head that are an instance of the given class, returns a new list with those
     * elements.
     * 
     * @param list
     * @param aClass
     * @return
     */
    public static <T, ET extends T> List<ET> peek(List<T> list, Class<ET> aClass) {
        if (list.isEmpty()) {
            return Collections.emptyList();
        }

        List<ET> newList = new ArrayList<>();

        // Starting on the first element, add elements until it finds an element that is not of the type
        for (T element : list) {
            // Stop if element is not of type
            if (!aClass.isInstance(element)) {
                break;
            }

            newList.add(aClass.cast(element));
        }

        return newList;
    }

    public static <T> List<T> toList(Optional<T> optional) {
        if (!optional.isPresent()) {
            return Collections.emptyList();
        }

        return Arrays.asList(optional.get());
    }

    /**
     * Checks if the given object is an instance of any of the given classes.
     * 
     * @param object
     * @param classes
     * @return
     */
    public static <T> boolean instanceOf(T object, Collection<Class<? extends T>> classes) {
        for (Class<?> aClass : classes) {
            if (aClass.isInstance(object)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Creates a list with the given element, unless it is null. In that case, returns an empty list.
     * 
     * @param element
     * @return
     */
    public static <T> List<T> ofNullable(T element) {
        if (element == null) {
            return Collections.emptyList();
        }

        return Arrays.asList(element);
    }

    /**
     * Accepts lists that have at most one element, return the element if present, or null otherwise.
     * 
     * @param selectCond
     * @return
     */
    public static <T> T orElseNull(List<T> list) {
        Preconditions.checkArgument(list.size() < 2, "Expected list size to be less than 2, it is " + list.size());

        if (list.isEmpty()) {
            return null;
        }

        return list.get(0);
    }

    public static <T> boolean containsAny(Collection<T> container, Collection<T> elementsToFind) {
        return elementsToFind.stream()
                .anyMatch(container::contains);
    }

    public static <T> Set<T> intersection(Collection<T> collection1, Collection<T> collection2) {
        Set<T> set = new HashSet<>(collection1);
        set.retainAll(collection2);

        return set;
    }

    public static <T> List<T> newArrayList() {
        return new ArrayList<>();
    }

    public static <K, V> Map<K, V> newHashMap() {
        return new HashMap<>();
    }

    public static <K> Set<K> newHashSet() {
        return new HashSet<>();
    }
    /*
    public static <T> T[] toArray(List<T> list) {
        return list.toArray(new T[0]);
    }
    */

    /**
     * Adds to the list if element is present, and does nothing otherwise.
     * 
     * @param includes
     * @param element
     */
    public static <T> void addOptional(Collection<T> includes, Optional<T> element) {

        if (!element.isPresent()) {
            return;
        }

        includes.add(element.get());
    }

    /**
     * Returns the first non-empty element of the stream.
     */
    public static <T> Optional<T> findFirstNonEmpty(Stream<Optional<T>> stream) {
        Preconditions.checkArgument(stream != null, "stream must not be null");

        final Iterator<Optional<T>> iterator = stream.iterator();

        while (iterator.hasNext()) {
            final Optional<T> item = iterator.next();

            if (item.isPresent()) {
                return item;
            }
        }

        return Optional.empty();
    }

}
