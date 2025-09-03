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

package tdrc.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import tdrc.tuple.Triple;
import tdrc.tuple.TupleList;

/**
 * Utility class for list operations in tdrcLibrary.
 */
public class ListUtils {
    /**
     * Combine N arrays to create a list of N-tuples.
     * 
     * @param arraysToCombine Arrays to be combined into tuples.
     * @param <T> The type of elements in the arrays.
     * @return A TupleList containing the combined tuples.
     */
    @SafeVarargs
    public static <T> TupleList<T> createTuples(T[]... arraysToCombine) {
        return createTuples(Arrays.asList(arraysToCombine));
    }

    /**
     * Combine N lists to create a list of N-tuples.
     * 
     * @param arraysToCombine Lists to be combined into tuples.
     * @param <T> The type of elements in the lists.
     * @return A TupleList containing the combined tuples.
     */
    public static <T> TupleList<T> createTuples(List<T[]> arraysToCombine) {
        TupleList<T> tuples = TupleList.newInstance();
        int tupleSize = arraysToCombine.size();

        tuplesAux(arraysToCombine, 0, Collections.emptyList(), tuples, tupleSize);
        return tuples;
    }

    /**
     * Helper method for creating tuples from arrays.
     * 
     * @param arraysToCombine Lists to be combined into tuples.
     * @param position Current position in the recursion.
     * @param tuple Current tuple being constructed.
     * @param tuples TupleList to store the resulting tuples.
     * @param tupleSize Size of the tuples.
     * @param <T> The type of elements in the lists.
     */
    private static <T> void tuplesAux(List<T[]> arraysToCombine, int position, List<T> tuple, TupleList<T> tuples,
            int tupleSize) {
        if (position >= tupleSize) {
            tuples.add(tuple);
            return;
        }
        T[] current = arraysToCombine.get(position);
        for (T object : current) {
            List<T> newTuple = new ArrayList<>(tuple);
            newTuple.add(object);

            tuplesAux(arraysToCombine, position + 1, newTuple, tuples, tupleSize);
        }
    }

    /**
     * Combine lists to create a list of tuples.
     * 
     * @param arraysToCombine Lists to be combined into tuples.
     * @param <T> The type of elements in the lists.
     * @return A TupleList containing the combined tuples.
     */
    public static <T> TupleList<T> createTuplesFromList(List<List<T>> arraysToCombine) {
        TupleList<T> tuples = TupleList.newInstance();
        int tupleSize = arraysToCombine.size();

        tuplesFromListAux(arraysToCombine, 0, Collections.emptyList(), tuples, tupleSize);
        return tuples;
    }

    /**
     * Combine lists to create a list of tuples.
     * 
     * @param arraysToCombine Lists to be combined into tuples.
     * @param <T> The type of elements in the lists.
     * @return A TupleList containing the combined tuples.
     */
    @SafeVarargs
    public static <T> TupleList<T> createTuplesFromList(List<T>... arraysToCombine) {
        TupleList<T> tuples = TupleList.newInstance();
        int tupleSize = arraysToCombine.length;

        tuplesFromListAux(Arrays.asList(arraysToCombine), 0, Collections.emptyList(), tuples, tupleSize);
        return tuples;
    }

    /**
     * Helper method for creating tuples from lists.
     * 
     * @param arraysToCombine Lists to be combined into tuples.
     * @param position Current position in the recursion.
     * @param tuple Current tuple being constructed.
     * @param tuples TupleList to store the resulting tuples.
     * @param tupleSize Size of the tuples.
     * @param <T> The type of elements in the lists.
     */
    private static <T> void tuplesFromListAux(List<List<T>> arraysToCombine, int position, List<T> tuple,
            TupleList<T> tuples,
            int tupleSize) {
        if (position >= tupleSize) {
            tuples.add(tuple);
            return;
        }
        List<T> current = arraysToCombine.get(position);
        for (T object : current) {
            List<T> newTuple = new ArrayList<>(tuple);
            newTuple.add(object);

            tuplesFromListAux(arraysToCombine, position + 1, newTuple, tuples, tupleSize);
        }
    }

    /**
     * Combine two arrays to create a list of pairs.
     * 
     * @param left The first array.
     * @param right The second array.
     * @param <T> The type of elements in the first array.
     * @param <V> The type of elements in the second array.
     * @return A list of pairs combining elements from both arrays.
     */
    public static <T, V> List<Pair<T, V>> createPairs(T[] left, V[] right) {

        List<Pair<T, V>> pairs = new ArrayList<>(left.length * right.length);
        for (T t : left) {
            for (V v : right) {
                pairs.add(new Pair<>(t, v));
            }
        }
        return pairs;
    }

    /**
     * Combine two lists to create a list of pairs.
     * 
     * @param left The first list.
     * @param right The second list.
     * @param <T> The type of elements in the first list.
     * @param <V> The type of elements in the second list.
     * @return A list of pairs combining elements from both lists.
     */
    public static <T, V> List<Pair<T, V>> createPairs(List<T> left, List<V> right) {

        List<Pair<T, V>> pairs = new ArrayList<>(left.size() * right.size());
        left.forEach(l -> right.forEach(r -> pairs.add(new Pair<>(l, r))));
        return pairs;
    }

    /**
     * Combine three arrays to create a list of triples.
     * 
     * @param xs The first array.
     * @param ys The second array.
     * @param zs The third array.
     * @param <X> The type of elements in the first array.
     * @param <Y> The type of elements in the second array.
     * @param <Z> The type of elements in the third array.
     * @return A list of triples combining elements from all three arrays.
     */
    public static <X, Y, Z> List<Triple<X, Y, Z>> createTriples(X[] xs, Y[] ys, Z[] zs) {

        List<Triple<X, Y, Z>> triples = new ArrayList<>(xs.length * ys.length * zs.length);
        for (X x : xs) {
            for (Y y : ys) {
                for (Z z : zs) {
                    triples.add(Triple.newInstance(x, y, z));
                }
            }
        }
        return triples;
    }

    /**
     * Combine three lists to create a list of triples.
     * 
     * @param xs The first list.
     * @param ys The second list.
     * @param zs The third list.
     * @param <X> The type of elements in the first list.
     * @param <Y> The type of elements in the second list.
     * @param <Z> The type of elements in the third list.
     * @return A list of triples combining elements from all three lists.
     */
    public static <X, Y, Z> List<Triple<X, Y, Z>> createTriples(List<X> xs, List<Y> ys, List<Z> zs) {

        List<Triple<X, Y, Z>> triples = new ArrayList<>(xs.size() * ys.size() * zs.size());
        xs.forEach(x -> ys.forEach(y -> zs.forEach(z -> triples.add(Triple.newInstance(x, y, z)))));
        return triples;
    }
}
