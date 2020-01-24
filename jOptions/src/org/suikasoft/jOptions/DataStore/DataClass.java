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
import java.util.Optional;
import java.util.stream.Collectors;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

import pt.up.fe.specs.util.SpecsNumbers;

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

    default <K, E extends K> T setOptional(DataKey<Optional<K>> key, E value) {
        if (value == null) {
            return set(key, Optional.empty());
        }

        return set(key, Optional.of(value));
    }

    /**
     * 
     * @return an Optional containing a StoreDefinition, if defined. By default returns empty.
     */
    default Optional<StoreDefinition> getStoreDefinition() {
        return Optional.empty();
    }

    // default T set(DataKey<Optional<T>> key, T value) {
    // return set(key, Optional.of(value));
    // }

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
    Collection<DataKey<?>> getDataKeysWithValues();

    /**
     * If the DataClass is closed, this means that no keys are allowed besides the ones defined in the StoreDefinition.
     * 
     * <p>
     * By default, returns false.
     * 
     * @return
     */
    default boolean isClosed() {
        return false;
    }

    /**
     * Increments the value of the given key by one.
     * 
     * @param key
     * @return
     */
    default Number inc(DataKey<Number> key) {
        // if (Integer.class.isAssignableFrom(key.getValueClass())) {
        // return inc(key, (int) 1);
        // }
        return inc(key, 1);
    }

    /**
     * Increments the value of the given key by the given amount.
     * 
     * <p>
     * If there is not value for the given key, it is initialized to zero.
     * 
     * @param key
     * @param amount
     * @return
     */
    @SuppressWarnings("unchecked")
    default <N extends Number> N inc(DataKey<N> key, N amount) {
        // Check if value is already present
        if (!hasValue(key)) {
            set(key, (N) SpecsNumbers.zero(key.getValueClass()));
        }

        Number previousValue = get(key);
        set(key, (N) SpecsNumbers.add(previousValue, amount));
        return (N) previousValue;
    }

    /**
     * Increments the value of all given keys by the amounts in the given DataClass.
     * 
     * @param dataClass
     */
    @SuppressWarnings("unchecked")
    default void inc(DataClass<?> dataClass) {
        for (DataKey<?> key : dataClass.getDataKeysWithValues()) {
            if (!Number.class.isAssignableFrom(key.getValueClass())) {
                continue;
            }
            DataKey<Number> numberKey = (DataKey<Number>) key;

            Number amount = dataClass.get(numberKey);
            inc(numberKey, amount);
        }
    }

    // default Integer inc(DataKey<Integer> key, int amount) {
    // // Check if value is already present
    // if (!hasValue(key)) {
    // set(key, 0);
    // }
    //
    // Integer previousValue = get(key);
    // set(key, previousValue + amount);
    // return previousValue;
    // }

    default String toInlinedString() {
        var keys = getDataKeysWithValues();

        if (getStoreDefinition().isPresent()) {
            keys = getStoreDefinition().get().getKeys().stream()
                    .filter(key -> hasValue(key))
                    .collect(Collectors.toList());
        }

        return keys.stream()
                .map(key -> key.getName() + ": " + DataClassUtils.toString(get(key)))
                .collect(Collectors.joining(", ", "[", "]"));

    }

}