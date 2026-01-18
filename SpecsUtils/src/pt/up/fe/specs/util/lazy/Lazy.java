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
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.util.lazy;

import java.util.Objects;
import java.util.function.Supplier;

import pt.up.fe.specs.util.function.SerializableSupplier;

public interface Lazy<T> extends Supplier<T> {

    /**
     * 
     * @return the value encapsulated by the Lazy object
     */
    @Override
    T get();

    /**
     * 
     * @return true if the value encapsulated by the Lazy object has been
     *         initialized
     */
    boolean isInitialized();

    static <T> Lazy<T> newInstance(Supplier<T> supplier) {
        Objects.requireNonNull(supplier, () -> "Supplier cannot be null");
        return new ThreadSafeLazy<>(supplier);
    }

    static <T> Lazy<T> newInstanceSerializable(SerializableSupplier<T> supplier) {
        Objects.requireNonNull(supplier, () -> "SerializableSupplier cannot be null");
        return new ThreadSafeLazy<>(supplier);
    }
}