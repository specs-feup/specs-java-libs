/**
 * Copyright 2013 SPeCS Research Group.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
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
 *
 * @author Joao Bispo
 */
public class SpecsCollections {

    /**
     * Returns the elements from the given index, until the end of the list.
     *
     */
    public static <E> List<E> subList(List<E> list, int startIndex) {
        return list.subList(startIndex, list.size());
    }

    public static <K, V> Map<V, K> invertMap(Map<K, V> map) {
        Map<V, K> invertedMap = new HashMap<>();

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
     * Returns the last element of the list, or an empty Optional if the list is
     * empty.
     *
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
     */
    public static <K> Iterable<K> iterable(final Iterator<K> iterator) {
        return () -> iterator;
    }

    @SafeVarargs
    public static <T> Set<T> asSet(T... a) {
        return new HashSet<>(Arrays.asList(a));
    }

    /**
     * If an element is null, ignores it.
     *
     */
    public static <T> List<T> asListT(Class<T> superClass, Object... elements) {
        List<T> list = new ArrayList<>();

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

    public static <T extends KeyProvider<K>, K> List<K> getKeyList(List<T> providers) {
        List<K> list = new ArrayList<>();

        for (T provider : providers) {
            list.add(provider.getKey());
        }

        return list;
    }

    /**
     * Creates a new list sorted list from the given collection.
     *
     */
    public static <T extends Comparable<? super T>> List<T> newSorted(Collection<T> collection) {
        // Create list
        List<T> list = new ArrayList<>(collection);

        // Sort list
        Collections.sort(list);

        return list;
    }

    /**
     * Removes the tokens from the list from startIndex, inclusive, to endIndex,
     * exclusive.
     *
     */
    public static <T> List<T> remove(List<T> list, int startIndex, int endIndex) {
        List<T> removedElements = new ArrayList<>();

        for (int i = endIndex - 1; i >= startIndex; i--) {
            removedElements.add(list.remove(i));
        }

        return removedElements;
    }

    public static <T> List<T> remove(List<T> list, int startIndex) {
        return remove(list, startIndex, list.size());
    }

    public static <T> List<T> remove(List<T> list, List<Integer> indexes) {
        // Sort indexes
        Collections.sort(indexes);

        List<T> removedElements = new ArrayList<>();

        for (int i = indexes.size() - 1; i >= 0; i--) {
            int index = indexes.get(i);
            removedElements.add(list.remove(index));
        }

        return removedElements;
    }

    /**
     * Removes from the list the elements that match the predicate, returns the
     * removed elements.
     *
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

    public static <T, U extends T> List<U> remove(List<T> list, Class<U> targetClass) {
        return castUnchecked(remove(list, targetClass::isInstance), targetClass);
    }

    public static <T> T removeLast(List<T> list) {
        List<T> lastElement = remove(list, list.size() - 1, list.size());
        Preconditions.checkArgument(lastElement.size() == 1);
        return lastElement.get(0);
    }

    public static <T, U extends T> U removeLast(List<T> list, Class<U> targetClass) {
        if (list.isEmpty()) {
            throw new RuntimeException("This method should not be called with empty lists");
        }

        T last = removeLast(list);

        return targetClass.cast(last);
    }

    /**
     * Returns the first index of object that is an instance of the given class.
     * Returns -1 if no object is found that is instance of the class.
     *
     */
    public static <T> int getFirstIndex(List<? super T> list, Class<T> aClass) {
        if (list == null || list.isEmpty()) {
            return -1;
        }

        var comparator = (aClass == null) ? (Predicate<Object>) (Objects::isNull)
                : (Predicate<Object>) aClass::isInstance;

        // Find first index that matches the class
        for (int i = 0; i < list.size(); i++) {
            if (comparator.test(list.get(i))) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Returns the first object that is an instance of the given class. Returns null
     * if no object is found that is instance of the class.
     *
     */
    public static <T> T getFirst(List<? super T> list, Class<T> aClass) {

        for (Object obj : list) {
            if (aClass.isInstance(obj)) {
                return aClass.cast(obj);
            }
        }

        return null;
    }

    /**
     * Returns true if all the elements in the list are instances of the given
     * class.
     *
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
     * Adds the elements of the provider collection to the receiver collection.
     * Returns the receiver collection.
     *
     */
    public static <U, T extends Collection<U>> T add(T receiver, T provider) {
        receiver.addAll(provider);
        return receiver;
    }

    /**
     * Casts a list of one type to another type, and checks if all elements can be
     * cast to the target type.
     *
     */
    public static <T> SpecsList<T> cast(List<?> list, Class<T> aClass) {
        // Verify if all elements implement the type of the class
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

    public static <T> T[] cast(Object[] array, Class<T> targetClass) {
        return Arrays.stream(array)
                .map(targetClass::cast) // Cast to String
                .toArray(i -> (T[]) Array.newInstance(targetClass, i));
    }

    /**
     * Casts a list of one type to another type, without checking if the elements
     * can be cast to the target type.
     *
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> castUnchecked(List<?> list, Class<T> aClass) {
        return (List<T>) list;
    }

    /**
     * Helper method which receives an element.
     *
     * <p>
     * If the element is null, list remains the same.
     *
     */
    public static <K> SpecsList<K> concat(Collection<? extends K> list, K element) {
        return concat(list, ofNullable(element));
    }

    /**
     * Helper method which receives an element.
     *
     * <p>
     * If the element is null, list remains the same.
     *
     */
    public static <K> SpecsList<K> concat(K element, Collection<? extends K> list) {
        return concat(ofNullable(element), list);
    }

    public static <K> SpecsList<K> concat(Collection<? extends K> list1, Collection<? extends K> list2) {

        List<K> newList = new ArrayList<>(list1.size() + list2.size());
        newList.addAll(list1);
        newList.addAll(list2);

        return SpecsList.convert(newList);
    }

    /**
     * If the list is modifiable, adds directly to it.
     *
     */
    public static <K> List<K> concatList(List<? extends K> list, K element) {
        try {
            @SuppressWarnings("unchecked")
            List<K> castedList = ((List<K>) list);
            castedList.add(element);
            return castedList;
        } catch (UnsupportedOperationException e) {
            // Fallback to generic copy concat
            return concat(list, element);
        }
    }

    /**
     * If the first list is modifiable, adds directly to it.
     *
     */
    public static <K> List<K> concatList(List<? extends K> list1, List<? extends K> list2) {
        try {
            @SuppressWarnings("unchecked")
            List<K> castedList = ((List<K>) list1);
            castedList.addAll(list2);
            return castedList;
        } catch (UnsupportedOperationException e) {
            // Fallback to generic copy concat
            return concat(list1, list2);
        }
    }

    /**
     * Creates a list with the elements from the given collections.
     *
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
     */
    public static <O, D> D[] convert(O[] origin, D[] destination, Function<O, D> converter) {
        for (int i = 0; i < origin.length; i++) {
            destination[i] = converter.apply(origin[i]);
        }

        return destination;
    }

    /**
     * Turns an Optional<T> into a Stream<T> of length zero or one depending upon
     * whether a value is present.
     *
     * <p>
     * Source:
     * <a href="http://stackoverflow.com/questions/22725537/using-java-8s-optional-with-streamflatmap">...</a>
     */
    public static <T> Stream<T> toStream(Optional<T> opt) {
        return opt.stream();

    }

    /**
     * Filters the elements of a Collection according to a map function over the
     * elements of that collection.
     *
     */
    public static <T, F> List<T> filter(Collection<T> elements, Function<T, F> mapFunction) {
        return filter(elements.stream(), mapFunction);
    }

    /**
     * Filters the elements of a Stream according to a map function over the
     * elements of that collection.
     *
     */
    public static <T, F> List<T> filter(Stream<T> elements, Function<T, F> mapFunction) {

        Set<F> seenElements = new HashSet<>();
        return elements
                // Add to set. If already in set, will return false and filter the node
                .filter(element -> seenElements.add(mapFunction.apply(element)))
                .collect(Collectors.toList());

    }

    public static <T, M> List<M> map(Collection<T> list, Function<T, M> mapper) {
        return list.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    /**
     * Removes all the elements at the head that are an instance of the given class,
     * returns a new list with those elements.
     *
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
     */
    public static <T, ET extends T> ET popSingle(List<T> list, Class<ET> aClass) {
        if (list.isEmpty()) {
            throw new RuntimeException("List is empty");
        }

        return aClass.cast(list.remove(0));
    }

    /**
     * Returns all the elements at the head that are an instance of the given class,
     * returns a new list with those elements.
     *
     */
    public static <T, ET extends T> List<ET> peek(List<T> list, Class<ET> aClass) {
        if (list.isEmpty()) {
            return Collections.emptyList();
        }

        List<ET> newList = new ArrayList<>();

        // Starting on the first element, add elements until it finds an element that is
        // not of the type
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
        return optional.map(Arrays::asList).orElse(Collections.emptyList());

    }

    /**
     * Checks if the given object is an instance of any of the given classes.
     *
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
     * Creates a list with the given element, unless it is null. In that case,
     * returns an empty list.
     *
     */
    public static <T> List<T> ofNullable(T element) {
        if (element == null) {
            return Collections.emptyList();
        }

        return List.of(element);
    }

    /**
     * Accepts lists that have at most one element, return the element if present,
     * or null otherwise.
     *
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

    @SafeVarargs
    public static <K> Set<K> newHashSet(K... elements) {
        return new HashSet<>(Arrays.asList(elements));
    }

    /**
     * Adds to the list if element is present, and does nothing otherwise.
     *
     */
    public static <T> void addOptional(Collection<T> includes, Optional<T> element) {

        if (element.isEmpty()) {
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

    /**
     * @return a stream of the elements of the list, in reverse order
     */
    public static <T> Stream<T> reverseStream(List<T> list) {
        return reverseIndexStream(list).mapToObj(list::get);
    }

    /**
     * @return a stream of indexes to the list, in reverse order
     */
    public static <T> IntStream reverseIndexStream(List<T> list) {
        int from = 0;
        int to = list.size();

        return IntStream.range(from, to).map(i -> to - i + from - 1);
    }

    /**
     * Collects all instances of the given class from the stream.
     *
     */
    public static <T> List<T> toList(Stream<? super T> stream, Class<T> aClass) {
        return stream.filter(aClass::isInstance)
                .map(aClass::cast)
                .collect(Collectors.toList());
    }

    /**
     * Converts a list of String providers to a String array.
     *
     */
    public static <T extends KeyProvider<String>> String[] toStringArray(Collection<T> values) {
        return values.stream()
                .map(KeyProvider::getKey).toArray(String[]::new);
    }

    /**
     * Converts a collection to a set, applying the given mapper to each of the
     * elements.
     *
     */
    public static <T, R> Set<R> toSet(Collection<T> collection, Function<? super T, R> mapper) {
        return collection.stream().map(mapper).collect(Collectors.toSet());
    }

    public static <T> Stream<T> getStream(Collection<T> collection, boolean isParallel) {
        if (isParallel) {
            return collection.parallelStream();
        }

        return collection.stream();
    }

    public static BitSet copy(BitSet bitSet) {
        var copy = new BitSet();
        copy.or(bitSet);

        return copy;
    }

    public static <T> T[] newArray(Class<T> targetClass, int size) {
        @SuppressWarnings("unchecked")
        var newArray = (T[]) Array.newInstance(targetClass, size);
        return newArray;
    }

    public static <T1, T2> List<T2> toList(T1[] array, Function<T1, T2> mapper) {
        return Arrays.stream(array)
                .map(mapper)
                .collect(Collectors.toList());
    }

    /**
     * @return a list with the elements that are an instance of the given class
     */
    public static <T> List<T> get(List<? super T> list, Class<T> targetClass) {
        return list.stream()
                .filter(targetClass::isInstance)
                .map(targetClass::cast)
                .collect(Collectors.toList());
    }

    /**
     * Converts the definition to an optional. If the list contains more than one
     * element, throws an exception.
     *
     */
    public static <K> Optional<K> toOptional(Collection<K> collection) {
        SpecsCheck.checkArgument(collection.size() < 2,
                () -> "Expected list to only have one element or none, it has " + collection.size() + ": "
                        + collection);

        return collection.stream().findFirst();
    }

    /**
     * @return a set with the elements common to all given collections
     */
    public static <K> Set<K> and(Collection<Collection<K>> collections) {
        if (collections.isEmpty()) {
            return Collections.emptySet();
        }

        var commonElements = (Set<K>) null;

        for (var collection : collections) {

            // Initialize with first collection
            if (commonElements == null) {
                commonElements = new HashSet<>(collection);
                continue;
            }

            // Only keep common elements
            commonElements.retainAll(collection);
        }

        return commonElements;
    }

    @SafeVarargs
    public static <K> Set<K> and(Collection<K>... collections) {
        return and(Arrays.asList(collections));
    }

    /**
     * @return a set with the elements of all given collections
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

    @SafeVarargs
    public static <K> Set<K> or(Collection<K>... collections) {
        return or(Arrays.asList(collections));
    }

    /**
     * If the key has a mapping different than null, just returns the value,
     * otherwise uses the given Supplier to create the first value, associates it in
     * the map, and returns it.
     *
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
