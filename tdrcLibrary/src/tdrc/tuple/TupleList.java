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

/**
 * Represents a list of Tuple objects, providing utility methods for tuple
 * management.
 * This class is an encapsulation of List<List<T>>. Does not guarantee that each
 * tuple has to be of the same length.
 * 
 * @author tiago
 *
 * @param <T> the type of elements in the tuples
 */
public class TupleList<T> extends AbstractList<Tuple<T>> {

    /**
     * The list of tuples managed by this class.
     */
    List<Tuple<T>> tuples;

    /**
     * Creates a new instance of TupleList.
     * 
     * @param <T> the type of elements in the tuples
     * @return a new instance of TupleList
     */
    public static <T> TupleList<T> newInstance() {
        return new TupleList<>();
    }

    /**
     * Creates a new instance of TupleList from multiple lists of elements.
     * 
     * @param <T>    the type of elements in the tuples
     * @param tuples the lists of elements to be converted into tuples
     * @return a new instance of TupleList containing the provided tuples
     */
    @SafeVarargs
    public static <T> TupleList<T> newInstance(List<T>... tuples) {
        TupleList<T> tupleList = new TupleList<>();
        for (List<T> list : tuples) {
            tupleList.add(list);
        }
        return tupleList;
    }

    /**
     * Creates a new instance of TupleList from multiple arrays of elements.
     * 
     * @param <T>    the type of elements in the tuples
     * @param tuples the arrays of elements to be converted into tuples
     * @return a new instance of TupleList containing the provided tuples
     */
    @SafeVarargs
    public static <T> TupleList<T> newInstance(T[]... tuples) {
        TupleList<T> tupleList = new TupleList<>();
        for (T[] list : tuples) {
            tupleList.add(Arrays.asList(list));
        }
        return tupleList;
    }

    /**
     * Private constructor to initialize the list of tuples.
     */
    private TupleList() {
        tuples = new ArrayList<>();
    }

    /**
     * Retrieves the tuple at the specified index.
     * 
     * @param index the index of the tuple to retrieve
     * @return the tuple at the specified index
     */
    @Override
    public Tuple<T> get(int index) {
        return tuples.get(index);
    }

    /**
     * Returns the number of tuples in the list.
     * 
     * @return the size of the tuple list
     */
    @Override
    public int size() {
        return tuples.size();
    }

    /**
     * Replaces the tuple at the specified index with the provided tuple.
     * 
     * @param index the index of the tuple to replace
     * @param tuple the new tuple to set at the specified index
     * @return the previous tuple at the specified index
     */
    @Override
    public Tuple<T> set(int index, Tuple<T> tuple) {
        return tuples.set(index, tuple);
    }

    /**
     * Adds a new tuple at the specified index, created from the provided list of
     * elements.
     * 
     * @param index    the index at which to add the new tuple
     * @param elements the list of elements to create the new tuple
     */
    public void add(int index, List<T> elements) {
        tuples.add(index, Tuple.newInstance(elements));
    }

    /**
     * Adds a new tuple to the end of the list, created from the provided list of
     * elements.
     * 
     * @param elements the list of elements to create the new tuple
     */
    public void add(List<T> elements) {
        tuples.add(Tuple.newInstance(elements));
    }

    /**
     * Adds a new tuple at the specified index.
     * 
     * @param index the index at which to add the new tuple
     * @param tuple the tuple to add
     */
    @Override
    public void add(int index, Tuple<T> tuple) {
        tuples.add(index, tuple);
    }

    /**
     * Removes the specified tuple from the list.
     * 
     * @param o the tuple to remove
     * @return true if the tuple was successfully removed, false otherwise
     */
    @Override
    public boolean remove(Object o) {
        return tuples.remove(o);
    }

}
