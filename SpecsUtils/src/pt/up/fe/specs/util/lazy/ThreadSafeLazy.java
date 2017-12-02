/**
 * Copyright 2015 SPeCS Research Group.
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

package pt.up.fe.specs.util.lazy;

import java.util.function.Supplier;

/**
 * Encapsulates an object which has an expensive initialization.
 * 
 * 
 * @author Luis Cubal
 *
 * @param <T>
 */
public final class ThreadSafeLazy<T> implements Lazy<T> {
    private T value;
    private final Supplier<T> provider;
    private boolean isInitialized;

    public ThreadSafeLazy(Supplier<T> provider) {
        this.provider = provider;
        this.value = null;
        this.isInitialized = false;
    }

    @Override
    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * The same as the method get().
     * 
     * @return
     */
    public T getValue() {
        return get();
    }

    /* (non-Javadoc)
     * @see pt.up.fe.specs.util.Utilities.Lazy#get()
     */
    @Override
    public T get() {
        // First, check if it is initialized
        // Reading a boolean is atomic and should be safe according to the spec:
        //
        // "Reads and writes are atomic for reference variables and for most primitive
        // variables (all types except long and double)."
        //
        // http://docs.oracle.com/javase/tutorial/essential/concurrency/atomic.html
        //
        if (isInitialized) {
            return value;
        }

        // If it is not initialized, call synchronized method that initializes it
        return initAndGetValue();
    }

    private synchronized T initAndGetValue() {
        // Synchronized check if is initialized
        if (isInitialized) {
            return value;
        }

        value = provider.get();
        isInitialized = true;

        return value;
    }

}
