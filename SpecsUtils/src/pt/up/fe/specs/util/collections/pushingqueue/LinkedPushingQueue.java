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
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.util.collections.pushingqueue;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.stream.Stream;

/**
 * "Pushing Queue" implementations with a LinkedList.
 * 
 * 
 * @author Joao Bispo
 */
public class LinkedPushingQueue<T> implements PushingQueue<T> {

    /**
     * INSTANCE VARIABLES
     */
    private final LinkedList<T> queue;

    private final int maxSize;

    /**
     * Creates a PushingQueue with the specified size.
     *
     * @param capacity
     *            the size of the queue
     */
    public LinkedPushingQueue(int capacity) {
	this.maxSize = capacity;
	this.queue = new LinkedList<>();
    }

    /**
     * Inserts an element at the head of the queue, pushing all other elements one position forward. If the queue is
     * full, the last element is dropped.
     *
     * @param element
     *            an element to insert in the queue
     */
    @Override
    public void insertElement(T element) {
	// Insert element at the head
	this.queue.add(0, element);

	// If size exceed capacity, remove last element
	while (this.queue.size() > this.maxSize) {
	    Iterator<T> iterator = this.queue.descendingIterator();
	    iterator.next();
	    iterator.remove();
	}

    }

    /**
     * Returns the element at the specified position in this queue.
     *
     * @param index
     *            index of the element to return
     * @return the element at the specified position in this queue
     */
    @Override
    public T getElement(int index) {
	if (index >= this.queue.size()) {
	    return null;
	}

	return this.queue.get(index);
    }

    /**
     * Returns the capacity of the queue.
     *
     * @return the capacity of the queue
     */
    @Override
    public int size() {
	return this.maxSize;
    }

    /**
     * 
     * @return the number of inserted elements
     */
    @Override
    public int currentSize() {
	return this.queue.size();
    }

    @Override
    public Iterator<T> iterator() {
	return this.queue.iterator();
    }

    @Override
    public Stream<T> stream() {
	return this.queue.stream();
    }

    @Override
    public String toString() {
	if (this.maxSize == 0) {
	    return "[]";
	}

	StringBuilder builder = new StringBuilder();

	builder.append("[").append(getElement(0));

	for (int i = 1; i < this.maxSize; i++) {
	    builder.append(", ").append(getElement(i));
	}
	builder.append("]");

	return builder.toString();

    }

}
