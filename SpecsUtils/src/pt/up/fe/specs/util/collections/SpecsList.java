/**
 * Copyright 2018 SPeCS.
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

package pt.up.fe.specs.util.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import pt.up.fe.specs.util.SpecsCollections;

/**
 * Wrapper around List interface that adds some custom methods.
 * 
 * @author JoaoBispo
 *
 * @param <T>
 */
public class SpecsList<T> implements List<T>, SpecsCollection<T> {

    private final List<T> list;

    private SpecsList(List<T> list) {
        this.list = list;
    }

    public static <T> SpecsList<T> convert(List<T> list) {
        if (list instanceof SpecsList) {
            return (SpecsList<T>) list;
        }

        return new SpecsList<>(list);
    }

    public static <T> SpecsList<T> newInstance(Class<T> targetClass) {
        return new SpecsList<>(new ArrayList<>());
    }

    /** CUSTOM METHODS **/

    public ArrayList<T> toArrayList() {
        if (list instanceof ArrayList) {
            return (ArrayList<T>) list;
        }

        return new ArrayList<>(list);
    }

    public List<T> list() {
        return list;
    }

    public <C extends T> List<C> cast(Class<C> aClass) {
        return SpecsCollections.cast(list, aClass);
    }

    /**
     * Helper method which receives an element.
     * 
     * <p>
     * If the element is null, list remains the same.
     *
     */
    public <K extends T> SpecsList<T> concat(K element) {
        return SpecsCollections.concat(list, element);
    }

    /**
     * Helper method which receives an element.
     * 
     * <p>
     * If the element is null, list remains the same.
     *
     */
    public <K extends T> SpecsList<T> prepend(K element) {
        return SpecsCollections.concat(element, list);
    }

    public SpecsList<T> concat(Collection<? extends T> list) {
        return SpecsCollections.concat(this.list, list);
    }

    public SpecsList<T> andAdd(T e) {
        list.add(e);

        return this;
    }

    /** LIST IMPLEMENTATION **/

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean add(T e) {
        return list.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return new HashSet<>(list).containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return list.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return list.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public T get(int index) {
        return list.get(index);
    }

    @Override
    public T set(int index, T element) {
        return list.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        list.add(index, element);
    }

    @Override
    public T remove(int index) {
        return list.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return list.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

    @Override
    public String toString() {
        return list.toString();
    }
}
