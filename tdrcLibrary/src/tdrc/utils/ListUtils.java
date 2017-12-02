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

public class ListUtils {
    /**
     * Combine N arrays to create a list of N-tuples
     * 
     * @param tuples
     * @return
     */
    @SafeVarargs
    public static <T> TupleList<T> createTuples(T[]... arraysToCombine) {
        return createTuples(Arrays.asList(arraysToCombine));
    }

    /**
     * Combine N lists to create a list of N-tuples
     * 
     * @param tuples
     * @return
     */
    public static <T> TupleList<T> createTuples(List<T[]> arraysToCombine) {
        TupleList<T> tuples = TupleList.newInstance();
        int tupleSize = arraysToCombine.size();

        tuplesAux(arraysToCombine, 0, Collections.emptyList(), tuples, tupleSize);
        return tuples;
    }

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

    public static <T> TupleList<T> createTuplesFromList(List<List<T>> arraysToCombine) {
        TupleList<T> tuples = TupleList.newInstance();
        int tupleSize = arraysToCombine.size();

        tuplesFromListAux(arraysToCombine, 0, Collections.emptyList(), tuples, tupleSize);
        return tuples;
    }

    @SafeVarargs
    public static <T> TupleList<T> createTuplesFromList(List<T>... arraysToCombine) {
        TupleList<T> tuples = TupleList.newInstance();
        int tupleSize = arraysToCombine.length;

        tuplesFromListAux(Arrays.asList(arraysToCombine), 0, Collections.emptyList(), tuples, tupleSize);
        return tuples;
    }

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
     * Combine two arrays to create a list of pairs
     * 
     * @param left
     * @param right
     * @return
     */
    public static <T, V> List<Pair<T, V>> createPairs(T[] left, V[] right) {

        List<Pair<T, V>> pairs = new ArrayList<>(left.length * right.length);
        for (T t : left) {
            for (V v : right) {
                pairs.add(Pair.newInstance(t, v));
            }
        }
        return pairs;
    }

    /**
     * Combine two lists to create a list of pairs
     * 
     * @param left
     * @param right
     * @return
     */
    public static <T, V> List<Pair<T, V>> createPairs(List<T> left, List<V> right) {

        List<Pair<T, V>> pairs = new ArrayList<>(left.size() * right.size());
        left.forEach(l -> right.forEach(r -> pairs.add(Pair.newInstance(l, r))));
        return pairs;
    }

    /**
     * Combine three arrays to create a list of triples
     * 
     * @param xs
     * @param ys
     * @param zs
     * @return
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
     * Combine three lists to create a list of triples
     * 
     * @param xs
     * @param ys
     * @param zs
     * @return
     */
    public static <X, Y, Z> List<Triple<X, Y, Z>> createTriples(List<X> xs, List<Y> ys, List<Z> zs) {

        List<Triple<X, Y, Z>> triples = new ArrayList<>(xs.size() * ys.size() * zs.size());
        xs.forEach(x -> ys.forEach(y -> zs.forEach(z -> triples.add(Triple.newInstance(x, y, z)))));
        return triples;
    }
}
