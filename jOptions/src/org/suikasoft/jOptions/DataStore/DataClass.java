/**
 * Copyright 2018 SPeCS.
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

package org.suikasoft.jOptions.DataStore;

import java.util.Collection;

import org.suikasoft.jOptions.Datakey.DataKey;

/**
 * A class that replaces fields with public static DataKey instances.
 * 
 * @author JoaoBispo
 *
 * @param <T>
 */
public interface DataClass<T extends DataClass<T>> {

    String getDataClassName();

    <K> K get(DataKey<K> key);

    <K, E extends K> T set(DataKey<K> key, E value);

    default T set(DataKey<Boolean> key) {
        return set(key, true);
    }

    T set(T instance);

    /**
     * 
     * @param key
     * @return true, if it contains a non-null value for the given key, not considering default values
     */
    <VT> boolean hasValue(DataKey<VT> key);

    /**
     * 
     * @return All the keys that are mapped to a value
     */
    Collection<DataKey<?>> keysWithValues();

    // DataStore getData();
}