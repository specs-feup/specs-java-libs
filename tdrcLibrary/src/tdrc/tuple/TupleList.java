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
 * This class is an encapsulation of List<List<T>>. Does not garantee that each tuple has to be of the same length
 * 
 * @author tiago
 *
 * @param <T>
 */
public class TupleList<T> extends AbstractList<Tuple<T>> {

    List<Tuple<T>> tuples;

    public static <T> TupleList<T> newInstance() {
        return new TupleList<>();
    }

    @SafeVarargs
    public static <T> TupleList<T> newInstance(List<T>... tuples) {
        TupleList<T> tupleList = new TupleList<>();
        for (List<T> list : tuples) {
            tupleList.add(list);
        }
        return tupleList;
    }

    @SafeVarargs
    public static <T> TupleList<T> newInstance(T[]... tuples) {
        TupleList<T> tupleList = new TupleList<>();
        for (T[] list : tuples) {
            tupleList.add(Arrays.asList(list));
        }
        return tupleList;
    }

    private TupleList() {
        tuples = new ArrayList<>();
    }

    @Override
    public Tuple<T> get(int index) {
        return tuples.get(index);
    }

    @Override
    public int size() {

        return tuples.size();
    }

    @Override
    public Tuple<T> set(int index, Tuple<T> tuple) {
        return tuples.set(index, tuple);
    }

    public void add(int index, List<T> elements) {
        tuples.add(index, Tuple.newInstance(elements));
    }

    public void add(List<T> elements) {
        tuples.add(Tuple.newInstance(elements));
    }

    @Override
    public void add(int index, Tuple<T> tuple) {

        tuples.add(index, tuple);
    }

    @Override
    public boolean remove(Object o) {
        // TODO Auto-generated method stub
        return tuples.remove(o);
    }

}
