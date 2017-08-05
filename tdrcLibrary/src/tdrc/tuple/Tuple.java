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

public class Tuple<T> extends AbstractList<T> {

    List<T> tuple;

    public static <T> Tuple<T> newInstance() {
        return new Tuple<>();
    }

    public static <T> Tuple<T> newInstance(List<T> elements) {
        return new Tuple<>(elements);
    }

    @SafeVarargs
    public static <T> Tuple<T> newInstance(T... elements) {
        return new Tuple<>(Arrays.asList(elements));
    }

    private Tuple(List<T> elements) {
        tuple = new ArrayList<>(elements);
    }

    private Tuple() {
        tuple = new ArrayList<>();
    }

    @Override
    public T get(int index) {
        return tuple.get(index);
    }

    @Override
    public int size() {

        return tuple.size();
    }

    @Override
    public T set(int index, T element) {
        return tuple.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        tuple.add(index, element);
    }

    @Override
    public boolean remove(Object o) {
        return tuple.remove(o);
    }

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
}
