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

package pt.up.fe.specs.util.collections.concurrentchannel;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import pt.up.fe.specs.util.SpecsLogs;

/**
 * Can only be created by a ConcurrentChannel objects, and represents a producer
 * end of that channel.
 *
 * @author Joao Bispo
 */
public record ChannelProducer<T>(BlockingQueue<T> channel) {

    /**
     * Inserts the specified element into this queue if it is possible to do so
     * immediately without violating capacity restrictions, returning true upon
     * success and false if no space is currently available. When using a
     * capacity-restricted queue, this method is generally preferable to
     * BlockingQueue.add, which can fail to insert an element only by throwing an
     * exception.
     *
     * @param e the element to add
     * @return true if the element was added to this queue, else false
     */
    public boolean offer(T e) {
        return this.channel.offer(e);
    }

    /**
     * inserts the specified element into this queue, waiting up to the specified
     * wait time if necessary for space to become available.
     *
     * @param e       the element to add
     * @param timeout how long to wait before giving up, in units of unit
     * @param unit    a TimeUnit determining how to interpret the timeout parameter
     * @return true if successful, or false if the specified waiting time elapses
     *         before space is available
     */
    public boolean offer(T e, long timeout, TimeUnit unit) throws InterruptedException {
        return this.channel.offer(e, timeout, unit);
    }

    /**
     * Inserts the specified element into this queue, waiting if necessary for space
     * to become available.
     *
     */
    public void put(T e) {
        try {
            this.channel.put(e);
        } catch (InterruptedException ex) {
            SpecsLogs.info("Interrupted while ChannelProducer.put(): " + ex.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Empties the channel.
     */
    public void clear() {
        T t = this.channel.poll();

        while (t != null) {
            t = this.channel.poll();
        }
    }

}
