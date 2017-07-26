/**
 * Copyright 2014 SPeCS Research Group.
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
import java.util.stream.Stream;

public class MixedPushingQueue<T> implements PushingQueue<T> {

    private final static int LINKED_THRESHOLD = 40;

    private final PushingQueue<T> queue;

    public MixedPushingQueue(int capacity) {
	if (capacity < MixedPushingQueue.LINKED_THRESHOLD) {
	    this.queue = new ArrayPushingQueue<>(capacity);
	} else {
	    this.queue = new LinkedPushingQueue<>(capacity);
	}
    }

    @Override
    public void insertElement(T element) {
	this.queue.insertElement(element);
    }

    @Override
    public T getElement(int index) {
	return this.queue.getElement(index);
    }

    @Override
    public int size() {
	return this.queue.size();
    }

    @Override
    public int currentSize() {
	return this.queue.currentSize();
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
	return toString(element -> element.toString());
    }

}
