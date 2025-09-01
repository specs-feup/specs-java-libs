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

package pt.up.fe.specs.util.collections.concurrentchannel;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Can only be created by a ConcurrentChannel object, and represents a consumer
 * end of that channel.
 * 
 * @author Joao Bispo
 */
public class ChannelConsumer<T> {

    private final BlockingQueue<T> channel;

    ChannelConsumer(BlockingQueue<T> channel) {
        this.channel = channel;
    }

    /**
     * Retrieves and removes the head of this queue, or returns null if this queue
     * is empty.
     *
     * @return the head of this queue, or null if this queue is empty
     */
    public T poll() {
        return this.channel.poll();
    }

    /**
     * Retrieves and removes the head of this queue, waiting up to the specified
     * wait time if necessary for an element to become available.
     *
     * @param timeout how long to wait before giving up, in units of unit.
     *                Negative values are treated as zero timeout (immediate
     *                return).
     *                Extremely large values are capped to prevent overflow issues.
     * @param unit    a TimeUnit determining how to interpret the timeout parameter
     * @return the head of this queue, or null if the specified waiting time elapses
     *         before an element is available
     * @throws InterruptedException if interrupted while waiting
     */
    public T poll(long timeout, TimeUnit unit) throws InterruptedException {
        // Handle negative timeout as zero timeout (immediate return)
        if (timeout < 0) {
            return this.channel.poll(0, unit);
        }

        // Handle extremely large timeout values to prevent overflow and excessive
        // waiting
        // Define a maximum reasonable timeout of 60 seconds for any operation
        long maxReasonableTimeout = 60;
        TimeUnit maxReasonableUnit = TimeUnit.SECONDS;

        // Check if the requested timeout exceeds the maximum reasonable timeout
        try {
            long timeoutNanos = unit.toNanos(timeout);
            long maxReasonableNanos = maxReasonableUnit.toNanos(maxReasonableTimeout);

            if (timeoutNanos > maxReasonableNanos) {
                return this.channel.poll(maxReasonableTimeout, maxReasonableUnit);
            }
        } catch (ArithmeticException e) {
            // Overflow occurred, use maximum reasonable timeout
            return this.channel.poll(maxReasonableTimeout, maxReasonableUnit);
        }

        return this.channel.poll(timeout, unit);
    }

    /**
     * Retrieves and removes the head of this queue, waiting if necessary until an
     * element becomes available.
     *
     * @return the head of this queue
     * @throws InterruptedException if interrupted while waiting
     */
    public T take() throws InterruptedException {
        return this.channel.take();
    }

}
