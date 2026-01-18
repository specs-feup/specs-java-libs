/**
 * Copyright 2012 SPeCS Research Group.
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

package pt.up.fe.specs.util.utilities;

import pt.up.fe.specs.util.Preconditions;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * Utility for tracking progress through a fixed number of steps.
 *
 * Behavior notes:
 * - The constructor enforces a non-negative max count. Passing a negative value
 * will throw an IllegalArgumentException (via
 * {@link pt.up.fe.specs.util.Preconditions}).
 * - The default constructor creates a counter with a very large maximum value
 * (Integer.MAX_VALUE).
 */
public class ProgressCounter {

    private final int maxCount;
    private int currentCount;

    /**
     * Creates a new ProgressCounter with the specified maximum count.
     *
     * The counter starts at 0. Each call to {@link #next()} or {@link #nextInt()}
     * increments the internal current count only while it is below the maximum
     * count. Once the counter reaches the configured maximum, further calls to
     * {@link #next()} or {@link #nextInt()} will not increase the counter but
     * will return the capped value and emit a warning.
     *
     * @param maxCount the maximum number of steps (must be non-negative)
     * @throws IllegalArgumentException if {@code maxCount} is negative
     */
    public ProgressCounter(int maxCount) {
        Preconditions.checkArgument(maxCount >= 0, "maxCount should be non-negative");
        this.maxCount = maxCount;
        this.currentCount = 0;
    }

    /**
     * Creates a new ProgressCounter with a default (very large) maximum value.
     *
     * The default maximum is {@link Integer#MAX_VALUE}, which effectively behaves
     * as an unbounded counter for most practical use-cases. The same contract for
     * incrementing and {@link #hasNext()} applies as with the parameterized
     * constructor.
     */
    public ProgressCounter() {
        this(Integer.MAX_VALUE);
    }

    public String next() {
        int currentCount = nextInt();

        return "(" + currentCount + "/" + this.maxCount + ")";
    }

    public int nextInt() {
        if (this.currentCount < this.maxCount) {
            this.currentCount += 1;
            return this.currentCount;
        }

        SpecsLogs.warn("Already reached the maximum count (" + this.maxCount + ")");
        return this.currentCount;
    }

    public int getCurrentCount() {
        return this.currentCount;
    }

    public int getMaxCount() {
        return this.maxCount;
    }

    public boolean hasNext() {
        return this.currentCount < this.maxCount;
    }
}
