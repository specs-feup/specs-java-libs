/**
 * Copyright 2016 SPeCS.
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
 * <p>
 * This implementation is similar to {@link ThreadSafeLazy}, but the get() method is not synchronized.
 * 
 * @author JoaoBispo
 *
 * @param <T>
 */
public class SynchronizedLazy<T> implements Lazy<T> {

    private final Supplier<T> provider;
    private T value;

    public SynchronizedLazy(Supplier<T> supplier) {
        this.provider = supplier;
    }

    public synchronized T getValue() {
        if (this.value == null) {
            return this.value = this.provider.get();
        }
        return this.value;
    }

    @Override
    public T get() {
        return getValue();
    }

}
