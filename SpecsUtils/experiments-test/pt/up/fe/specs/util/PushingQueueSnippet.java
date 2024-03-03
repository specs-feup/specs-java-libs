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

package pt.up.fe.specs.util;

import org.junit.Test;

import pt.up.fe.specs.util.collections.pushingqueue.ArrayPushingQueue;
import pt.up.fe.specs.util.collections.pushingqueue.LinkedPushingQueue;
import pt.up.fe.specs.util.collections.pushingqueue.PushingQueue;

public class PushingQueueSnippet {

    @Test
    public void test() {

        int times = 100000;

        for (int i = 10; i < 100; i += 10) {
            int capacity = i;
            PushingQueue<Integer> arrayQueue = new ArrayPushingQueue<>(capacity);
            PushingQueue<Integer> linkedQueue = new LinkedPushingQueue<>(capacity);

            // Warm up
            measure(arrayQueue, capacity, times);
            measure(linkedQueue, capacity, times);

            long arrayTime = measure(arrayQueue, capacity, times);
            long linkedTime = measure(linkedQueue, capacity, times);

            System.out.println("Elements:" + capacity);
            System.out.println("ARRAY TIME:" + SpecsStrings.parseTime(arrayTime));
            System.out.println("LINKED TIME:" + SpecsStrings.parseTime(linkedTime));
        }
        /*
        	int capacity = 10000;
        
        	PushingQueueOld<Integer> queue = new PushingQueueOld<>(capacity);
        
        	long tic = System.nanoTime();
        	for (int i = 0; i < capacity; i++) {
        	    queue.insertElement(i);
        	}
        	long toc = System.nanoTime();
        	System.out.println("INIT TIME:" + ParseUtils.parseTime(toc - tic));
        
        	tic = System.nanoTime();
        	for (int i = 0; i < capacity; i++) {
        	    queue.insertElement(i);
        	}
        	toc = System.nanoTime();
        	System.out.println("PUSHING TIME:" + ParseUtils.parseTime(toc - tic));
         */
    }

    private static long measure(PushingQueue<Integer> queue, int capacity, int times) {

        // Fill queue
        for (int i = 0; i < capacity; i++) {
            queue.insertElement(i);
        }

        // Measure now that is filled
        long tic = System.nanoTime();
        for (int i = 0; i < capacity * times; i++) {
            queue.insertElement(i);
        }

        long toc = System.nanoTime();

        return toc - tic;
    }

}
