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

package org.suikasoft.jOptions.Datakey;

import org.suikasoft.jOptions.Interfaces.DataStore;

/**
 * Functional interface for custom value retrieval from a {@link org.suikasoft.jOptions.Interfaces.DataStore}.
 *
 * <p>Implement this interface to provide custom logic for retrieving values from a DataStore.
 *
 * @param <T> the type of value
 */
@FunctionalInterface
public interface CustomGetter<T> {
    /**
     * Returns a value for the given key and DataStore.
     *
     * @param value the current value
     * @param dataStore the DataStore
     * @return the value to use
     */
    T get(T value, DataStore dataStore);
}
