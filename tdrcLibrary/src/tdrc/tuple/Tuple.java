/**
 * Copyright 2017 SPeCS.
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

package tdrc.tuple;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * Represents a tuple of values.
 *
 * @param <T> the type of the elements in the tuple
 */
public class Tuple<T> extends AbstractList<T> implements Comparable<Tuple<T>> {

    private List<T> tuple;
    private BiFunction<Tuple<T>, Tuple<T>, Integer> comparator;
    private BiFunction<Tuple<T>, Tuple<T>, Double> distanceCalculator;

    /**
     * Creates a new instance of an empty tuple.
     *
     * @param <T> the type of the elements in the tuple
     * @return a new empty tuple
     */
    public static <T> Tuple<T> newInstance() {
        return new Tuple<>();
    }

    /**
     * Creates a new instance of a tuple with the given elements.
     *
     * @param <T> the type of the elements in the tuple
     * @param elements the elements to include in the tuple
     * @return a new tuple containing the given elements
     */
    public static <T> Tuple<T> newInstance(List<T> elements) {
        return new Tuple<>(elements);
    }

    /**
     * Creates a new instance of a tuple with the given elements.
     *
     * @param <T> the type of the elements in the tuple
     * @param elements the elements to include in the tuple
     * @return a new tuple containing the given elements
     */
    @SafeVarargs
    public static <T> Tuple<T> newInstance(T... elements) {
        return new Tuple<>(Arrays.asList(elements));
    }

    /**
     * Constructs a tuple with the given elements.
     *
     * @param elements the elements to include in the tuple
     */
    private Tuple(List<T> elements) {
        tuple = new ArrayList<>(elements);
        this.comparator = Tuple::defaultComparator;
        this.distanceCalculator = Tuple::defaultDistanceCalculator;
    }

    /**
     * Constructs an empty tuple.
     */
    private Tuple() {
        tuple = new ArrayList<>();
    }

    /**
     * Retrieves the element at the specified index.
     *
     * @param index the index of the element to retrieve
     * @return the element at the specified index
     */
    @Override
    public T get(int index) {
        return tuple.get(index);
    }

    /**
     * Returns the number of elements in the tuple.
     *
     * @return the size of the tuple
     */
    @Override
    public int size() {
        return tuple.size();
    }

    /**
     * Replaces the element at the specified index with the given element.
     *
     * @param index the index of the element to replace
     * @param element the new element
     * @return the previous element at the specified index
     */
    @Override
    public T set(int index, T element) {
        return tuple.set(index, element);
    }

    /**
     * Adds an element at the specified index.
     *
     * @param index the index at which to add the element
     * @param element the element to add
     */
    @Override
    public void add(int index, T element) {
        tuple.add(index, element);
    }

    /**
     * Removes the specified element from the tuple.
     *
     * @param o the element to remove
     * @return true if the element was removed, false otherwise
     */
    @Override
    public boolean remove(Object o) {
        return tuple.remove(o);
    }

    /**
     * Checks if this tuple is equal to another object.
     *
     * @param o the object to compare
     * @return true if the object is a tuple and has the same elements, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Tuple)) {
            return false;
        }
        Tuple<?> tuple = (Tuple<?>) o;
        int size = tuple.size();
        if (size != this.tuple.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!tuple.get(i).equals(this.tuple.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compares this tuple to another tuple.
     *
     * @param o the tuple to compare
     * @return a negative integer, zero, or a positive integer as this tuple is less than, equal to, or greater than the specified tuple
     */
    @Override
    public int compareTo(Tuple<T> o) {
        return comparator.apply(this, o);
    }

    /**
     * Default comparator for tuples.
     *
     * @param tuple the first tuple
     * @param tuple2 the second tuple
     * @param <T> the type of the elements in the tuples
     * @return a negative integer, zero, or a positive integer as the first tuple is less than, equal to, or greater than the second tuple
     */
    public static <T> Integer defaultComparator(Tuple<T> tuple, Tuple<T> tuple2) {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        final int tLen = tuple.size();
        final int oLen = tuple2.size();
        for (int i = 0; i < tLen && i < oLen; i++) {
            compareToBuilder.append(tuple.get(i), tuple2.get(i));
        }
        return compareToBuilder.toComparison();
    }

    /**
     * Calculates the distance between this tuple and another tuple.
     *
     * @param otherTuple the other tuple
     * @return the distance between the tuples
     */
    public double getDistance(Tuple<T> otherTuple) {
        return distanceCalculator.apply(this, otherTuple);
    }

    /**
     * Default distance calculator for tuples.
     *
     * @param tuple the first tuple
     * @param tuple2 the second tuple
     * @param <T> the type of the elements in the tuples
     * @return the distance between the tuples
     */
    public static <T> Double defaultDistanceCalculator(Tuple<T> tuple, Tuple<T> tuple2) {
        final int tLen = tuple.size();
        final int oLen = tuple2.size();
        double sum = 0;
        for (int i = 0; i < tLen && i < oLen; i++) {
            T t = tuple.get(i);
            T t2 = tuple2.get(i);
            if (!(t instanceof Number && t2 instanceof Number)) {
                throw new RuntimeException("Cannot calculate distance if the elements of the tuple are not numbers");
            }
            double tD = ((Number) t).doubleValue() - ((Number) t2).doubleValue();
            sum += tD * tD;
        }
        return Math.sqrt(sum);
    }

    /**
     * Retrieves the distance calculator for this tuple.
     *
     * @return the distance calculator
     */
    public BiFunction<Tuple<T>, Tuple<T>, Double> getDistanceCalculator() {
        return distanceCalculator;
    }

    /**
     * Sets the distance calculator for this tuple.
     *
     * @param distanceCalculator the new distance calculator
     */
    public void setDistanceCalculator(BiFunction<Tuple<T>, Tuple<T>, Double> distanceCalculator) {
        this.distanceCalculator = distanceCalculator;
    }
}
