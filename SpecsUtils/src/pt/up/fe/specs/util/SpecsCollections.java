/*
 * SpecsCollections.java
 *
 * Utility class for common operations on Java Collections, such as sublists, map inversion, filtering, and more. Provides static helper methods to simplify and extend the use of Java's collection framework in the SPeCS ecosystem.
 *
 * Copyright 2025 SPeCS Research Group.
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

import pt.up.fe.specs.util.collections.SpecsList;
import pt.up.fe.specs.util.providers.KeyProvider;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Utility methods related to Java Collections.
 * <p>
 * Provides static helper methods for working with lists, maps, sets, and other collections, including sublist extraction, map inversion, filtering, and more.
 * </p>
 *
 * @author Joao Bispo
 */
public class SpecsCollections {

    /**
     * Returns the elements from the given index, until the end of the list.
     *
     * @param list the list to extract the sublist from
     * @param startIndex the starting index of the sublist
     * @return a sublist starting from the given index to the end of the list
     */
    public static <E> List<E> subList(List<E> list, int startIndex) {
        return list.subList(startIndex, list.size());
    }

    /**
     * Inverts the keys and values of a given map.
     *
     * @param map the map to invert
     * @return a new map with inverted keys and values, or null if duplicate values are found
     */
    public static <K, V> Map<V, K> invertMap(Map<K, V> map) {
        Map<V, K> invertedMap = SpecsFactory.newHashMap();

        for (K key : map.keySet()) {
            V value = map.get(key);

            K previousKey = invertedMap.put(value, key);
            if (previousKey != null) {
                SpecsLogs.warn("There are two keys ('" + key + "' and '" + previousKey
                        + "') for the same value '" + value + "'");
                return null;
            }
        }

        return invertedMap;
    }

    /**
     * Returns the last element of the list, or null if the list is empty.
     *
     * @param lines the list to retrieve the last element from
     * @return the last element of the list, or null if the list is empty
     */
    public static <K> K last(List<K> lines) {
        if (lines.isEmpty()) {
            // TODO: Throw exception instead of returning null
            SpecsLogs.warn(
                    "This method should not be called with empty lists, if the list can be empty call lastTry() instead");
            return null;
        }

        return lines.get(lines.size() - 1);
    }

    /**
     * Returns the last element of the list, or an empty Optional if the list is empty.
     *
     * @param lines the list to retrieve the last element from
     * @return an Optional containing the last element, or an empty Optional if the list is empty
     */
    public static <K> Optional<K> lastTry(List<K> lines) {
        if (lines.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(lines.get(lines.size() - 1));
    }

    /**
     * Returns the element of the list, if it has exactly one. Empty otherwise.
     *
     * @param list the list to check
     * @return an Optional containing the single element, or an empty Optional if the list does not have exactly one element
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
     * @param iterator the iterator to convert
     * @return an Iterable wrapping the given iterator
     */
    public static <K> Iterable<K> iterable(final Iterator<K> iterator) {
        return () -> iterator;
    }

    /**
     * Converts an array to a Set.
     *
     * @param a the array to convert
     * @return a Set containing the elements of the array
     */
    @SafeVarargs
    public static <T> Set<T> asSet(T... a) {
        return new HashSet<>(Arrays.asList(a));
    }

    /**
     * Converts an array of objects to a list of a specific type, filtering out null elements.
     *
     * @param superClass the class of the elements to include in the list
     * @param elements the array of objects to convert
     * @return a list containing the elements of the array that match the given class
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
                SpecsLogs.warn("Could not add object of class '" + element.getClass() + "' to list of '"
                        + superClass + "'");
            }
        }

        return list;
    }

    /**
     * Extracts the keys from a list of KeyProvider objects.
     *
     * @param providers the list of KeyProvider objects
     * @return a list of keys extracted from the providers
     */
    public static <T extends KeyProvider<K>, K> List<K> getKeyList(List<T> providers) {
        List<K> list = SpecsFactory.newArrayList();

        for (T provider : providers) {
            list.add(provider.getKey());
        }

        return list;
    }

    /**
     * Creates a new sorted list from the given collection.
     *
     * @param collection the collection to sort
     * @return a new list containing the sorted elements of the collection
     */
    public static <T extends Comparable<? super T>> List<T> newSorted(Collection<T> collection) {
        List<T> list = SpecsFactory.newArrayList(collection);

        Collections.sort(list);

        return list;
    }

    /**
     * Removes the tokens from the list from startIndex, inclusive, to endIndex, exclusive.
     *
     * @param list the list to remove elements from
     * @param startIndex the starting index of the range to remove
     * @param endIndex the ending index of the range to remove
     * @return a list containing the removed elements
     */
    public static <T> List<T> remove(List<T> list, int startIndex, int endIndex) {
        List<T> removedElements = SpecsFactory.newArrayList();

        for (int i = endIndex - 1; i >= startIndex; i--) {
            removedElements.add(list.remove(i));
        }

        return removedElements;
    }

    /**
     * Removes the tokens from the list from startIndex, inclusive, to the end of the list.
     *
     * @param list the list to remove elements from
     * @param startIndex the starting index of the range to remove
     * @return a list containing the removed elements
     */
    public static <T> List<T> remove(List<T> list, int startIndex) {
        return remove(list, startIndex, list.size());
    }

    /**
     * Removes the elements at the specified indexes from the list.
     *
     * @param list the list to remove elements from
     * @param indexes the indexes of the elements to remove
     * @return a list containing the removed elements
     */
    public static <T> List<T> remove(List<T> list, List<Integer> indexes) {
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
     * @param list the list to remove elements from
     * @param filter the predicate to match elements
     * @return a list containing the removed elements
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
     * Removes from the list the elements that are instances of the given class, returns the removed elements.
     *
     * @param list the list to remove elements from
     * @param targetClass the class to match elements
     * @return a list containing the removed elements
     */
    public static <T, U extends T> List<U> remove(List<T> list, Class<U> targetClass) {
        return castUnchecked(remove(list, element -> targetClass.isInstance(element)), targetClass);
    }

    /**
     * Removes and returns the last element of the list.
     *
     * @param list the list to remove the last element from
     * @return the removed last element
     */
    public static <T> T removeLast(List<T> list) {
        List<T> lastElement = remove(list, list.size() - 1, list.size());
        Preconditions.checkArgument(lastElement.size() == 1);
        return lastElement.get(0);
    }

    /**
     * Removes and returns the last element of the list, checking if it is of the given class.
     *
     * @param list the list to remove the last element from
     * @param targetClass the class to match the last element
     * @return the removed last element
     */
    public static <T, U extends T> U removeLast(List<T> list, Class<U> targetClass) {
        if (list.isEmpty()) {
            throw new RuntimeException("This method should not be called with empty lists");
        }

        T last = removeLast(list);

        return targetClass.cast(last);
    }

    /**
     * Returns the first index of an object that is an instance of the given class. Returns -1 if no object is found that
     * is an instance of the class.
     *
     * @param list the list to search
     * @param aClass the class to match elements
     * @return the index of the first matching element, or -1 if no match is found
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
     * an instance of the class.
     *
     * @param list the list to search
     * @param aClass the class to match elements
     * @return the first matching element, or null if no match is found
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
     * Returns true if all the elements in the list are instances of the given class.
     *
     * @param list the list to check
     * @param aClass the class to match elements
     * @return true if all elements match the class, false otherwise
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
     * @param receiver the collection to add elements to
     * @param provider the collection providing elements
     * @return the receiver collection with added elements
     */
    public static <U, T extends Collection<U>> T add(T receiver, T provider) {
        receiver.addAll(provider);
        return receiver;
    }

    /**
     * Casts a list of one type to another type, and checks if all elements can be cast to the target type.
     *
     * @param list the list to cast
     * @param aClass the class to cast elements to
     * @return a list containing the cast elements
     */
    public static <T> SpecsList<T> cast(List<?> list, Class<T> aClass) {
        Optional<?> invalidElement = list.stream()
                .filter(element -> !aClass.isInstance(element))
                .findFirst();

        if (invalidElement.isPresent()) {
            throw new RuntimeException(
                    "Cannot cast list, has an element of type '" + invalidElement.get().getClass()
                            + "' which is not an instance of '" + aClass + "'");
        }

        return SpecsList.convert(castUnchecked(list, aClass));
    }

    /**
     * Casts an array of one type to another type.
     *
     * @param array the array to cast
     * @param targetClass the class to cast elements to
     * @return an array containing the cast elements
     */
    public static <T> T[] cast(Object[] array, Class<T> targetClass) {
        return Arrays.stream(array)
                .map(targetClass::cast)
                .toArray(i -> (T[]) Array.newInstance(targetClass, i));
    }

    /**
     * Casts a list of one type to another type, without checking if the elements can be cast to the target type.
     *
     * @param list the list to cast
     * @param aClass the class to cast elements to
     * @return a list containing the cast elements
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> castUnchecked(List<?> list, Class<T> aClass) {
        return (List<T>) list;
    }

    /**
     * Concatenates a collection with an element, returning a new list.
     *
     * @param list the collection to concatenate
     * @param element the element to add
     * @return a new list containing the concatenated elements
     */
    public static <K> SpecsList<K> concat(Collection<? extends K> list, K element) {
        return concat(list, ofNullable(element));
    }

    /**
     * Concatenates an element with a collection, returning a new list.
     *
     * @param element the element to add
     * @param list the collection to concatenate
     * @return a new list containing the concatenated elements
     */
    public static <K> SpecsList<K> concat(K element, Collection<? extends K> list) {
        return concat(ofNullable(element), list);
    }

    /**
     * Concatenates two collections, returning a new list.
     *
     * @param list1 the first collection
     * @param list2 the second collection
     * @return a new list containing the concatenated elements
     */
    public static <K> SpecsList<K> concat(Collection<? extends K> list1, Collection<? extends K> list2) {
        List<K> newList = new ArrayList<>(list1.size() + list2.size());
        newList.addAll(list1);
        newList.addAll(list2);

        return SpecsList.convert(newList);
    }

    /**
     * Concatenates a list with an element, modifying the list if it is modifiable.
     *
     * @param list the list to concatenate
     * @param element the element to add
     * @return the modified list, or a new list if the original list is not modifiable
     */
    public static <K> List<K> concatList(List<? extends K> list, K element) {
        try {
            @SuppressWarnings("unchecked")
            List<K> castedList = ((List<K>) list);
            castedList.add(element);
            return castedList;
        } catch (UnsupportedOperationException e) {
            return concat(list, element);
        }
    }

    /**
     * Concatenates two lists, modifying the first list if it is modifiable.
     *
     * @param list1 the first list
     * @param list2 the second list
     * @return the modified first list, or a new list if the original list is not modifiable
     */
    public static <K> List<K> concatList(List<? extends K> list1, List<? extends K> list2) {
        try {
            @SuppressWarnings("unchecked")
            List<K> castedList = ((List<K>) list1);
            castedList.addAll(list2);
            return castedList;
        } catch (UnsupportedOperationException e) {
            return concat(list1, list2);
        }
    }

    /**
     * Concatenates multiple collections into a single list.
     *
     * @param collections the collections to concatenate
     * @return a new list containing the concatenated elements
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
     * @param origin the original array
     * @param destination the destination array
     * @param converter the function to convert elements
     * @return the destination array with converted elements
     */
    public static <O, D> D[] convert(O[] origin, D[] destination, Function<O, D> converter) {
        for (int i = 0; i < origin.length; i++) {
            destination[i] = converter.apply(origin[i]);
        }

        return destination;
    }

    /**
     * Turns an Optional<T> into a Stream<T> of length zero or one depending upon whether a value is present.
     *
     * @param opt the Optional to convert
     * @return a Stream containing the value of the Optional, or an empty Stream if the Optional is empty
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
     * @param elements the collection to filter
     * @param mapFunction the function to map elements
     * @return a list containing the filtered elements
     */
    public static <T, F> List<T> filter(Collection<T> elements, Function<T, F> mapFunction) {
        return filter(elements.stream(), mapFunction);
    }

    /**
     * Filters the elements of a Stream according to a map function over the elements of that collection.
     *
     * @param elements the stream to filter
     * @param mapFunction the function to map elements
     * @return a list containing the filtered elements
     */
    public static <T, F> List<T> filter(Stream<T> elements, Function<T, F> mapFunction) {
        Set<F> seenElements = new HashSet<>();
        return elements
                .filter(element -> seenElements.add(mapFunction.apply(element)))
                .collect(Collectors.toList());
    }

    /**
     * Maps the elements of a collection to another type.
     *
     * @param list the collection to map
     * @param mapper the function to map elements
     * @return a list containing the mapped elements
     */
    public static <T, M> List<M> map(Collection<T> list, Function<T, M> mapper) {
        return list.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    /**
     * Removes all the elements at the head that are an instance of the given class, returns a new list with those
     * elements.
     *
     * @param list the list to remove elements from
     * @param aClass the class to match elements
     * @return a list containing the removed elements
     */
    public static <T, ET extends T> List<ET> pop(List<T> list, Class<ET> aClass) {
        if (list.isEmpty()) {
            return Collections.emptyList();
        }

        List<ET> newList = new ArrayList<>();

        while (aClass.isInstance(list.get(0))) {
            newList.add(aClass.cast(list.remove(0)));

            if (list.isEmpty()) {
                break;
            }
        }

        return newList;
    }

    /**
     * Removes the specified number of elements from the head of the list.
     *
     * @param elements the list to remove elements from
     * @param numElementsToPop the number of elements to remove
     * @return a list containing the removed elements
     */
    public static <T> SpecsList<T> pop(List<T> elements, int numElementsToPop) {
        Preconditions.checkArgument(elements.size() >= numElementsToPop,
                "List has " + elements.size() + " elements, and want to pop " + numElementsToPop);

        List<T> poppedElements = new ArrayList<>();
        for (int i = 0; i < numElementsToPop; i++) {
            poppedElements.add(elements.remove(0));
        }

        return SpecsList.convert(poppedElements);
    }

    /**
     * Removes the first element of the list, checking if it is of the given class.
     *
     * @param list the list to remove the first element from
     * @param aClass the class to match the first element
     * @return the removed first element
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
     * @param list the list to retrieve elements from
     * @param aClass the class to match elements
     * @return a list containing the matching elements
     */
    public static <T, ET extends T> List<ET> peek(List<T> list, Class<ET> aClass) {
        if (list.isEmpty()) {
            return Collections.emptyList();
        }

        List<ET> newList = new ArrayList<>();

        for (T element : list) {
            if (!aClass.isInstance(element)) {
                break;
            }

            newList.add(aClass.cast(element));
        }

        return newList;
    }

    /**
     * Converts an Optional to a list.
     *
     * @param optional the Optional to convert
     * @return a list containing the value of the Optional, or an empty list if the Optional is empty
     */
    public static <T> List<T> toList(Optional<T> optional) {
        if (!optional.isPresent()) {
            return Collections.emptyList();
        }

        return Arrays.asList(optional.get());
    }

    /**
     * Checks if the given object is an instance of any of the given classes.
     *
     * @param object the object to check
     * @param classes the classes to match
     * @return true if the object matches any of the classes, false otherwise
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
     * @param element the element to include in the list
     * @return a list containing the element, or an empty list if the element is null
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
     * @param list the list to check
     * @return the single element of the list, or null if the list is empty
     */
    public static <T> T orElseNull(List<T> list) {
        Preconditions.checkArgument(list.size() < 2, "Expected list size to be less than 2, it is " + list.size());

        if (list.isEmpty()) {
            return null;
        }

        return list.get(0);
    }

    /**
     * Checks if the container collection contains any of the elements in the elementsToFind collection.
     *
     * @param container the collection to search
     * @param elementsToFind the collection of elements to find
     * @return true if any element is found, false otherwise
     */
    public static <T> boolean containsAny(Collection<T> container, Collection<T> elementsToFind) {
        return elementsToFind.stream()
                .anyMatch(container::contains);
    }

    /**
     * Returns the intersection of two collections.
     *
     * @param collection1 the first collection
     * @param collection2 the second collection
     * @return a set containing the elements common to both collections
     */
    public static <T> Set<T> intersection(Collection<T> collection1, Collection<T> collection2) {
        Set<T> set = new HashSet<>(collection1);
        set.retainAll(collection2);

        return set;
    }

    /**
     * Creates a new empty ArrayList.
     *
     * @return a new empty ArrayList
     */
    public static <T> List<T> newArrayList() {
        return new ArrayList<>();
    }

    /**
     * Creates a new empty HashMap.
     *
     * @return a new empty HashMap
     */
    public static <K, V> Map<K, V> newHashMap() {
        return new HashMap<>();
    }

    /**
     * Creates a new HashSet containing the given elements.
     *
     * @param elements the elements to include in the set
     * @return a new HashSet containing the elements
     */
    @SafeVarargs
    public static <K> Set<K> newHashSet(K... elements) {
        return new HashSet<>(Arrays.asList(elements));
    }

    /**
     * Adds to the list if element is present, and does nothing otherwise.
     *
     * @param includes the collection to add the element to
     * @param element the Optional containing the element to add
     */
    public static <T> void addOptional(Collection<T> includes, Optional<T> element) {
        if (!element.isPresent()) {
            return;
        }

        includes.add(element.get());
    }

    /**
     * Returns the first non-empty element of the stream.
     *
     * @param stream the stream to search
     * @return an Optional containing the first non-empty element, or an empty Optional if no non-empty element is found
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

    /**
     * Returns a stream of the elements of the list, in reverse order.
     *
     * @param list the list to reverse
     * @return a stream of the elements in reverse order
     */
    public static <T> Stream<T> reverseStream(List<T> list) {
        return reverseIndexStream(list).mapToObj(i -> list.get(i));
    }

    /**
     * Returns a stream of indexes to the list, in reverse order.
     *
     * @param list the list to reverse
     * @return a stream of indexes in reverse order
     */
    public static <T> IntStream reverseIndexStream(List<T> list) {
        int from = 0;
        int to = list.size();

        return IntStream.range(from, to).map(i -> to - i + from - 1);
    }

    /**
     * Collects all instances of the given class from the stream.
     *
     * @param stream the stream to filter
     * @param aClass the class to match elements
     * @return a list containing the matching elements
     */
    public static <T> List<T> toList(Stream<? super T> stream, Class<T> aClass) {
        return stream.filter(aClass::isInstance)
                .map(aClass::cast)
                .collect(Collectors.toList());
    }

    /**
     * Converts a collection of String providers to a String array.
     *
     * @param values the collection of String providers
     * @return a String array containing the keys of the providers
     */
    public static <T extends KeyProvider<String>> String[] toStringArray(Collection<T> values) {
        return values.stream()
                .map(KeyProvider<String>::getKey)
                .collect(Collectors.toList())
                .toArray(new String[0]);
    }

    /**
     * Converts a collection to a set, applying the given mapper to each of the elements.
     *
     * @param collection the collection to convert
     * @param mapper the function to map elements
     * @return a set containing the mapped elements
     */
    public static <T, R> Set<R> toSet(Collection<T> collection, Function<? super T, R> mapper) {
        return collection.stream().map(mapper).collect(Collectors.toSet());
    }

    /**
     * Returns a stream of the collection, either parallel or sequential.
     *
     * @param collection the collection to convert
     * @param isParallel whether the stream should be parallel
     * @return a stream of the collection
     */
    public static <T> Stream<T> getStream(Collection<T> collection, boolean isParallel) {
        if (isParallel) {
            return collection.parallelStream();
        }

        return collection.stream();
    }

    /**
     * Creates a copy of a BitSet.
     *
     * @param bitSet the BitSet to copy
     * @return a copy of the BitSet
     */
    public static BitSet copy(BitSet bitSet) {
        var copy = new BitSet();
        copy.or(bitSet);

        return copy;
    }

    /**
     * Creates a new array of the given type and size.
     *
     * @param targetClass the class of the array elements
     * @param size the size of the array
     * @return a new array of the given type and size
     */
    public static <T> T[] newArray(Class<T> targetClass, int size) {
        @SuppressWarnings("unchecked")
        var newArray = (T[]) Array.newInstance(targetClass, size);

        return newArray;
    }

    /**
     * Maps the elements of an array to another type.
     *
     * @param array the array to map
     * @param mapper the function to map elements
     * @return a list containing the mapped elements
     */
    public static <T1, T2> List<T2> toList(T1[] array, Function<T1, T2> mapper) {
        return Arrays.stream(array)
                .map(value -> mapper.apply(value))
                .collect(Collectors.toList());
    }

    /**
     * Returns a list with the elements that are an instance of the given class.
     *
     * @param list the list to filter
     * @param targetClass the class to match elements
     * @return a list containing the matching elements
     */
    public static <T> List<T> get(List<? super T> list, Class<T> targetClass) {
        return list.stream()
                .filter(targetClass::isInstance)
                .map(targetClass::cast)
                .collect(Collectors.toList());
    }

    /**
     * Converts a collection to an optional. If the collection contains more than one element, throws an exception.
     *
     * @param collection the collection to convert
     * @return an Optional containing the single element, or an empty Optional if the collection is empty
     */
    public static <K> Optional<K> toOptional(Collection<K> collection) {
        SpecsCheck.checkArgument(collection.size() < 2,
                () -> "Expected list to only have one element or none, it has " + collection.size() + ": "
                        + collection);

        return collection.stream().findFirst();
    }

    /**
     * Returns a set with the elements common to all given collections.
     *
     * @param collections the collections to intersect
     * @return a set containing the common elements
     */
    public static <K> Set<K> and(Collection<Collection<K>> collections) {
        if (collections.isEmpty()) {
            return Collections.emptySet();
        }

        var commonElements = (Set<K>) null;

        for (var collection : collections) {
            if (commonElements == null) {
                commonElements = new HashSet<>(collection);
                continue;
            }

            commonElements.retainAll(collection);
        }

        return commonElements;
    }

    /**
     * Returns a set with the elements common to all given collections.
     *
     * @param collections the collections to intersect
     * @return a set containing the common elements
     */
    @SafeVarargs
    public static <K> Set<K> and(Collection<K>... collections) {
        return and(Arrays.asList(collections));
    }

    /**
     * Returns a set with the elements of all given collections.
     *
     * @param collections the collections to union
     * @return a set containing all elements
     */
    public static <K> Set<K> or(Collection<Collection<K>> collections) {
        if (collections.isEmpty()) {
            return Collections.emptySet();
        }

        var allElements = new HashSet<K>();

        for (var collection : collections) {
            allElements.addAll(collection);
        }

        return allElements;
    }

    /**
     * Returns a set with the elements of all given collections.
     *
     * @param collections the collections to union
     * @return a set containing all elements
     */
    @SafeVarargs
    public static <K> Set<K> or(Collection<K>... collections) {
        return or(Arrays.asList(collections));
    }

    /**
     * If the key has a mapping different than null, just returns the value, otherwise uses the given Supplier to create
     * the first value, associates it in the map, and returns it.
     *
     * @param map the map to retrieve or set the value
     * @param key the key to retrieve or set the value
     * @param defaultValue the Supplier to create the default value
     * @return the value associated with the key
     */
    public static <K, V> V getOrSet(Map<K, V> map, K key, Supplier<V> defaultValue) {
        var value = map.get(key);
        if (value == null) {
            value = defaultValue.get();
            map.put(key, value);
        }

        return value;
    }
}
