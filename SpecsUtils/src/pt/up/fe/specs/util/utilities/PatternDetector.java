/*
 * Copyright 2010 SPeCS Research Group.
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

package pt.up.fe.specs.util.utilities;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Iterator;

import pt.up.fe.specs.util.collections.pushingqueue.MixedPushingQueue;
import pt.up.fe.specs.util.collections.pushingqueue.PushingQueue;

/**
 * Looks for patterns in integer values.
 * 
 * 
 * @author Joao Bispo
 */
public class PatternDetector {

    /**
     * INSTANCE VARIABLES
     */
    private final int maxPatternSize;
    private final BitSet[] matchQueues;
    // private PushingQueue<Integer> queue;
    private final PushingQueue<Integer> queue2;
    private int currentPatternSize;
    private PatternState state;
    private final boolean priorityToBiggerPatterns;

    /**
     * Creates a new PatternFinder which will try to find patterns of maximum size 'maxPatternSize', in the given
     * integer values.
     * 
     * @param maxPatternSize
     */
    public PatternDetector(int maxPatternSize, boolean priorityToBiggerPatterns) {
        this.currentPatternSize = 0;
        this.state = PatternState.NO_PATTERN;

        this.maxPatternSize = maxPatternSize;
        this.priorityToBiggerPatterns = priorityToBiggerPatterns;
        this.matchQueues = new BitSet[maxPatternSize];

        // Initialize matching queues
        for (int i = 0; i < maxPatternSize; i++) {
            this.matchQueues[i] = new BitSet();
        }
        // queue = new PushingQueue<Integer>(maxPatternSize + 1);
        // queue2 = new PushingQueueOld<>(maxPatternSize + 1);
        this.queue2 = new MixedPushingQueue<>(maxPatternSize + 1);

        // Initialize Queue
        for (int i = 0; i < this.queue2.size(); i++) {
            this.queue2.insertElement(null);
        }
        // for (int i = 0; i < queue.size(); i++) {
        // queue.insertElement(null);
        // }

    }

    public int getMaxPatternSize() {
        return this.maxPatternSize;
    }

    /**
     * Gives another value to check for pattern.
     * 
     * @param value
     */
    public PatternState step(Integer hashValue) {
        // Insert new element
        // queue.insertElement(hashValue);
        this.queue2.insertElement(hashValue);

        // Compare first element with all other elements and store result on
        // match queues
        // List<Integer> elements = queue.getElements(1, maxPatternSize + 1);
        Iterator<Integer> iterator = this.queue2.iterator();

        // Ignore first element of the queue
        iterator.next();

        for (int i = 0; i < this.maxPatternSize; i++) {

            // Check if there is a match
            // if (hashValue.equals(queue.getElement(i + 1))) {
            if (hashValue.equals(iterator.next())) {
                // if (hashValue.equals(elements.get(i))) {
                // We have a match.
                // Shift match queue to the left
                this.matchQueues[i] = this.matchQueues[i].get(1, i + 1);
                // Set the bit.
                this.matchQueues[i].set(i);
            } else {
                // Reset queue
                this.matchQueues[i].clear();
            }
        }

        // Put all the results in a single bit array
        BitSet bitArray = new BitSet();
        for (int i = 0; i < this.matchQueues.length; i++) {
            if (this.matchQueues[i].get(0)) {
                bitArray.set(i);
            } else {
                bitArray.clear(i);
            }
        }

        int newPatternSize = calculatePatternSize(bitArray, this.currentPatternSize, this.priorityToBiggerPatterns);
        this.state = calculateState(this.currentPatternSize, newPatternSize);
        this.currentPatternSize = newPatternSize;
        return this.state;
    }

    /**
     * Gives another value to check for pattern.
     * 
     * @param value
     */
    /*
    public PatternState step2(Integer hashValue) {
        // Insert new element
        this.queue2.insertElement(hashValue);
     
        // Ignore first element of the queue
        IntStream.range(1, queue2.size())
        .forEach(i -> );
    
        // this.queue2.stream()
        
        // .skip(1)
        // .
        // Compare first element with all other elements and store result on
        // match queues
        Iterator<Integer> iterator = this.queue2.iterator();
    
        // Ignore first element of the queue
        iterator.next();
    
        for (int i = 0; i < this.maxPatternSize; i++) {
    
            // Check if there is a match
            if (hashValue.equals(iterator.next())) {
                // We have a match.
                // Shift match queue to the left
                this.matchQueues[i] = this.matchQueues[i].get(1, i + 1);
                // Set the bit.
                this.matchQueues[i].set(i);
            } else {
                // Reset queue
                this.matchQueues[i].clear();
            }
        }
    
        // Put all the results in a single bit array
        BitSet bitArray = new BitSet();
        for (int i = 0; i < this.matchQueues.length; i++) {
            if (this.matchQueues[i].get(0)) {
                bitArray.set(i);
            } else {
                bitArray.clear(i);
            }
        }
    
        int newPatternSize = calculatePatternSize(bitArray, this.currentPatternSize, this.priorityToBiggerPatterns);
        this.state = calculateState(this.currentPatternSize, newPatternSize);
        this.currentPatternSize = newPatternSize;
        return this.state;
    }
    */

    public int getPatternSize() {
        return this.currentPatternSize;
    }

    public static int calculatePatternSize(BitSet bitArray, int previousPatternSize, boolean priorityToBiggerPatterns) {

        int firstSetSize = bitArray.nextSetBit(0) + 1;

        // Give priority to bigger patters which were previously activated
        if (!priorityToBiggerPatterns) {
            return firstSetSize;
        }

        if (previousPatternSize > firstSetSize) {
            // Check if previous pattern size is still active
            boolean previousPatternStillActive = bitArray.get(previousPatternSize - 1);
            if (previousPatternStillActive) {
                return previousPatternSize;
            }
        }

        return firstSetSize;
    }

    public PatternState getState() {
        return this.state;
    }

    public static PatternState calculateState(int previousPatternSize, int patternSize) {
        PatternState newState = null;
        // Check if pattern state has changed
        if (previousPatternSize != patternSize) {
            // If previous pattern size was 0, a new pattern started
            if (previousPatternSize == 0) {
                newState = PatternState.PATTERN_STARTED;
            } // If current pattern size is 0, the previous pattern has stopped.
            else if (patternSize == 0) {
                newState = PatternState.PATTERN_STOPED;
            } // The case that is left is that the previous pattern stopped, but
              // there is a new pattern with a different size.
            else {
                newState = PatternState.PATTERN_CHANGED_SIZES;
            }
        } // The size of the pattern hasn't changed
        else {
            if (patternSize > 0) {
                newState = PatternState.PATTERN_UNCHANGED;
            } else {
                newState = PatternState.NO_PATTERN;
            }
        }

        return newState;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PatternDetector [matchQueues=" + Arrays.toString(this.matchQueues) + ", queue=" + this.queue2
                + ", patternSize="
                + this.currentPatternSize + "]";
    }

    public enum PatternState {
        PATTERN_STOPED,
        PATTERN_STARTED,
        PATTERN_CHANGED_SIZES,
        PATTERN_UNCHANGED,
        NO_PATTERN
    }

    public PushingQueue<Integer> getQueue() {
        return queue2;
    }

}
