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
 * Interface for classes that replace fields with public static DataKey
 * instances.
 *
 * <p>
 * This interface defines the contract for data classes that use DataKeys
 * instead of fields, supporting get/set operations and store definition access.
 *
 * @param <T> the type of the DataClass
 */
public interface DataClass<T extends DataClass<T>> {

    /**
     * Returns the name of this DataClass.
     *
     * @return the name of the DataClass
     */
    String getDataClassName();

    /**
     * Gets the value for the given DataKey.
     *
     * @param key the DataKey
     * @param <K> the value type
     * @return the value for the key
     */
    <K> K get(DataKey<K> key);

    /**
     * Sets the value for the given DataKey.
     *
     * @param key   the DataKey
     * @param value the value to set
     * @param <K>   the value type
     * @param <E>   the value type (extends K)
     * @return this instance
     */
    <K, E extends K> T set(DataKey<K> key, E value);

    /**
     * Sets a boolean DataKey to true.
     *
     * @param key the boolean DataKey
     * @return this instance
     */
    default T set(DataKey<Boolean> key) {
        return set(key, true);
    }

    /**
     * Sets an Optional DataKey to the given value, or empty if value is null.
     *
     * @param key   the Optional DataKey
     * @param value the value to set
     * @param <K>   the value type
     * @param <E>   the value type (extends K)
     * @return this instance
     */
    default <K, E extends K> T setOptional(DataKey<Optional<K>> key, E value) {
        if (value == null) {
            return set(key, Optional.empty());
        }

        return set(key, Optional.of(value));
    }

    /**
     * Gets the value for the given key name (String).
     *
     * @param key the key name
     * @return the value for the key
     */
    default Object getValue(String key) {
        var def = getStoreDefinitionTry().orElseThrow(
                () -> new RuntimeException(".getValue() only supported if DataClass has a StoreDefinition"));

        var datakey = def.getKey(key);

        return get(datakey);
    }

    /**
     * Sets the value for the given key name (String).
     *
     * @param key   the key name
     * @param value the value to set
     * @return the previous value
     */
    default Object setValue(String key, Object value) {
        var def = getStoreDefinitionTry().orElseThrow(
                () -> new RuntimeException(".setValue() only supported if DataClass has a StoreDefinition"));

        @SuppressWarnings("unchecked")
        var datakey = (DataKey<Object>) def.getKey(key);

        return set(datakey, datakey.getValueClass().cast(value));
    }

    /**
     * Returns an Optional containing the StoreDefinition, if defined.
     *
     * @return an Optional with the StoreDefinition, or empty if not present
     */
    default Optional<StoreDefinition> getStoreDefinitionTry() {
        return Optional.empty();
    }

    /**
     * Returns the StoreDefinition, or throws if not defined.
     *
     * @return the StoreDefinition
     */
    default StoreDefinition getStoreDefinition() {
        return getStoreDefinitionTry().orElseThrow(() -> new RuntimeException("No StoreDefinition defined"));
    }

    /**
     * Sets all values from another DataClass instance.
     *
     * @param instance the instance to copy from
     * @return this instance
     */
    T set(T instance);

    /**
     * Checks if this DataClass has a non-null value for the given key (not
     * considering defaults).
     *
     * @param key  the DataKey
     * @param <VT> the value type
     * @return true if a value is present, false otherwise
     */
    <VT> boolean hasValue(DataKey<VT> key);

    /**
     * Returns all DataKeys that are mapped to a value.
     *
     * @return a collection of DataKeys with values
     */
    Collection<DataKey<?>> getDataKeysWithValues();

    /**
     * If the DataClass is closed, this means that no keys are allowed besides the
     * ones defined in the StoreDefinition.
     * 
     * <p>
     * By default, returns false.
     * 
     * @return true if the DataClass is closed, false otherwise
     */
    default boolean isClosed() {
        return false;
    }

    /**
     * Increments the value of the given key by one.
     * 
     * @param key the DataKey
     * @return the previous value
     */
    default Number inc(DataKey<? extends Number> key) {
        return inc(key, 1);
    }

    /**
     * Increments the value of the given key by the given amount.
     * 
     * <p>
     * If there is not value for the given key, it is initialized to zero.
     * 
     * @param key    the DataKey
     * @param amount the amount to increment
     * @param <N1>   the type of the key's value
     * @param <N2>   the type of the amount
     * @return the previous value
     */
    @SuppressWarnings("unchecked")
    default <N1 extends Number, N2 extends Number> N1 inc(DataKey<N1> key, N2 amount) {
        if (!hasValue(key)) {
            set(key, (N1) SpecsNumbers.zero(key.getValueClass()));
        }

        N1 previousValue = get(key);
        set(key, (N1) SpecsNumbers.add(previousValue, amount));
        return previousValue;
    }

    /**
     * Increments the value of all given keys by the amounts in the given DataClass.
     * 
     * @param dataClass the DataClass containing the keys and amounts
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

    /**
     * Increments the value of the given integer key by one.
     * 
     * @param key the DataKey
     * @return the previous value
     */
    default Integer incInt(DataKey<Integer> key) {
        return incInt(key, 1);
    }

    /**
     * Increments the value of the given integer key by the given amount.
     * 
     * @param key    the DataKey
     * @param amount the amount to increment
     * @return the previous value
     */
    default Integer incInt(DataKey<Integer> key, int amount) {
        Integer previousValue = get(key);
        set(key, previousValue + amount);
        return previousValue;
    }

    /**
     * Returns a string representation of this DataClass in an inline format.
     * 
     * @return the inline string representation
     */
    default String toInlinedString() {
        var keys = getDataKeysWithValues();

        if (getStoreDefinitionTry().isPresent()) {
            keys = getStoreDefinitionTry().get().getKeys().stream()
                    .filter(this::hasValue)
                    .toList();
        }

        return keys.stream()
                .map(key -> key.getName() + ": " + DataClassUtils.toString(get(key)))
                .collect(Collectors.joining(", ", "[", "]"));
    }

    /**
     * Makes a shallow copy of the value that has the same mapping in the given
     * source.
     * 
     * <p>
     * This function should be safe to use as long as the keys refer to immutable
     * objects.
     * 
     * @param key    the DataKey
     * @param source the source DataClass
     * @param <K>    the type of the key's value
     * @param <E>    the type of the value (extends K)
     * @return this instance
     */
    default <K, E extends K> T copyValue(DataKey<K> key, T source) {
        var value = source.get(key);
        return set(key, value);
    }
}