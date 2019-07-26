/**
 * Copyright 2019 SPeCS.
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import pt.up.fe.specs.util.SpecsCheck;

/**
 * Implements the Buffer strategy, for an arbitrary number of buffers.
 * 
 * @author JoaoBispo
 *
 * @param <T>
 */
public class Buffer<T> {

    private final int numBuffers;
    private final List<T> buffers;
    private final Supplier<T> constructor;

    private int currentBuffer;

    public Buffer(int numBuffers, Supplier<T> constructor) {
        SpecsCheck.checkArgument(numBuffers > 0, () -> "Number of buffers must be greater than one");

        this.numBuffers = numBuffers;
        this.buffers = new ArrayList<>(numBuffers);
        this.constructor = constructor;

        this.currentBuffer = -1;
    }

    /**
     * Returns the buffer according to the relative index. If index is 0, returns the current buffer.
     * 
     * @param index
     * @return
     */
    public T get(int relativeIndex) {
        int absoluteIndex = translateIndex(relativeIndex);

        // Check if position is initialized

        while (absoluteIndex >= buffers.size()) {
            buffers.add(constructor.get());
        }

        return buffers.get(absoluteIndex);
    }

    /**
     * The same as get(0).
     * 
     * @return
     */
    public T getCurrent() {
        return get(0);
    }

    /**
     * Moves the relative index of the current buffer to the next buffer, returns the next buffer.
     * 
     * @return
     */
    public T next() {
        currentBuffer++;
        while (currentBuffer >= numBuffers) {
            currentBuffer -= numBuffers;
        }

        return getCurrent();
    }

    /**
     * Translates a relative index to the absolute index of the internal list.
     * 
     * @return
     */
    private int translateIndex(int relativeIndex) {
        if (currentBuffer == -1) {
            currentBuffer = 0;
        }

        int realIndex = currentBuffer + relativeIndex;

        // Adjust new Index
        while (realIndex >= numBuffers) {
            realIndex -= numBuffers;
        }

        return realIndex;
    }
}
