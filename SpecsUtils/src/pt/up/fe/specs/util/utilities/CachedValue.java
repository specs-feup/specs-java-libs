/**
 * Copyright 2021 SPeCS.
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

import java.lang.ref.SoftReference;
import java.util.function.Supplier;

public class CachedValue<T> {

    private final Supplier<T> supplier;
    private volatile SoftReference<T> value;

    public CachedValue(Supplier<T> supplier) {
        this.supplier = supplier;
        // Initialize value eagerly to keep previous behaviour
        value = new SoftReference<>(supplier.get());
    }

    public T getValue() {

        // Fast path: try without synchronization
        SoftReference<T> ref = value;
        T val = (ref == null) ? null : ref.get();
        if (val != null) {
            return val;
        }

        // Slow path: synchronize and double-check
        synchronized (this) {
            ref = value;
            val = (ref == null) ? null : ref.get();
            if (val == null) {
                val = supplier.get();
                value = new SoftReference<>(val);
            }
            return val;
        }
    }

    /**
     * Mark cache as stale
     */
    public void stale() {
        // Refresh the cached value atomically
        synchronized (this) {
            value = new SoftReference<>(supplier.get());
        }
    }

}
