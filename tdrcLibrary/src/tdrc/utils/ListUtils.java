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
 * specific language governing permissions and limitations under the License.
 */

package tdrc.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import tdrc.tuple.TupleList;

/**
 * Utility class for list operations in tdrcLibrary.
 */
public class ListUtils {
    /**
     * Combine N arrays to create a list of N-tuples.
     * 
     * @param arraysToCombine Arrays to be combined into tuples.
     * @param <T>             The type of elements in the arrays.
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
     * @param <T>             The type of elements in the lists.
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
     * @param position        Current position in the recursion.
     * @param tuple           Current tuple being constructed.
     * @param tuples          TupleList to store the resulting tuples.
     * @param tupleSize       Size of the tuples.
     * @param <T>             The type of elements in the lists.
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
     * @param <T>             The type of elements in the lists.
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
     * @param <T>             The type of elements in the lists.
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
     * @param position        Current position in the recursion.
     * @param tuple           Current tuple being constructed.
     * @param tuples          TupleList to store the resulting tuples.
     * @param tupleSize       Size of the tuples.
     * @param <T>             The type of elements in the lists.
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
}
