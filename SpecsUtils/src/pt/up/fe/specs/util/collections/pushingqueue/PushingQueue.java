/*
 * Copyright 2009 SPeCS Research Group.
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

package pt.up.fe.specs.util.collections.pushingqueue;

import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * "Pushing Queue" of fixed size.
 *
 * <p>
 * Elements can only be added at the head of the queue. Every time an element is
 * added, every other elements gets "pushed" (its index increments by one). If
 * an element is added when the queue is full, the last element in the queue
 * gets dropped.
 *
 * TODO: remove capacity, replace with size
 *
 * @author Joao Bispo
 */
public interface PushingQueue<T> {

    /**
     * Inserts an element at the head of the queue, pushing all other elements one
     * position forward. If the queue is full, the last element is dropped.
     *
     * @param element an element to insert in the queue
     */
    void insertElement(T element);

    /**
     * Returns the element at the specified position in this queue.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this queue
     */
    T getElement(int index);

    /**
     * Returns the capacity of the queue.
     *
     * @return the capacity of the queue
     */
    public int size();

    /**
     * 
     * @return the number of inserted elements
     */
    int currentSize();

    Iterator<T> iterator();

    Stream<T> stream();

    default String toString(Function<T, String> mapper) {
        if (this.size() == 0) {
            return "[]";
        }

        // Use a base mapper (avoid reassigning the method parameter so it remains
        // effectively final)
        final Function<T, String> baseMapper = mapper == null ? Object::toString : mapper;

        // Use a null-safe mapper so null elements don't cause a NullPointerException
        Function<T, String> safeMapper = t -> t == null ? "null" : baseMapper.apply(t);

        StringBuilder builder = new StringBuilder();

        builder.append("[").append(safeMapper.apply(getElement(0)));

        for (int i = 1; i < this.size(); i++) {
            builder.append(", ").append(safeMapper.apply(getElement(i)));
        }
        builder.append("]");

        return builder.toString();

    }
}
