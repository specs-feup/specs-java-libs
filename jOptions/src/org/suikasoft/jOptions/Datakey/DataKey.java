/**
 * Copyright 2014 SPeCS.
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

package org.suikasoft.jOptions.Datakey;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.gui.KeyPanel;
import org.suikasoft.jOptions.gui.KeyPanelProvider;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.parsing.StringCodec;
import pt.up.fe.specs.util.providers.KeyProvider;
import pt.up.fe.specs.util.utilities.StringLines;

/**
 * Keys for values with an associated type.
 * <p>
 * DataKey equality is done based only on the string name.
 * 
 * @see KeyFactory
 */
public interface DataKey<T> extends KeyProvider<String> {

    @Override
    default String getKey() {
        return getName();
    }

    /**
     * TODO: Rename to getType
     * 
     * @return
     */
    Class<T> getValueClass();

    String getName();

    default String getTypeName() {
        return getValueClass().getSimpleName();
    }

    /*
     * DECODER
     */
    /**
     * 
     * @return
     */
    Optional<StringCodec<T>> getDecoder();

    /**
     * A copy of this key, with decoder set.
     * 
     * @param decoder
     * @return
     */
    DataKey<T> setDecoder(StringCodec<T> decoder);

    default T decode(String encodedValue) {
        return getDecoder()
                .map(codec -> codec.decode(encodedValue))
                .orElseThrow(() -> new RuntimeException("No encoder/decoder set"));
    }

    default String encode(T value) {
        return getDecoder()
                .map(codec -> codec.encode(value))
                .orElseThrow(() -> new RuntimeException("No encoder/decoder set"));
    }

    /*
     * DEFAULT VALUE
     */
    /**
     * 
     * @return
     */
    Optional<T> getDefault();

    boolean hasDefaultValue();

    // default boolean hasDefaultValue() {
    // return getDefault().isPresent();
    // }

    /**
     * 
     * A copy of this key, with a Supplier for the default value.
     * 
     * @param defaultValue
     * @return
     */
    DataKey<T> setDefault(Supplier<? extends T> defaultValue);

    default DataKey<T> setDefaultString(String stringValue) {
        if (!getDecoder().isPresent()) {
            throw new RuntimeException("Can only use this method if a decoder was set before");
        }

        return this.setDefault(() -> getDecoder().get().decode(stringValue));
    }

    /*
     * CUSTOM GETTER
     */
    DataKey<T> setCustomGetter(CustomGetter<T> defaultValue);

    Optional<CustomGetter<T>> getCustomGetter();

    /*
     * CUSTOM SETTER
     */

    DataKey<T> setCustomSetter(CustomGetter<T> setProcessing);

    Optional<CustomGetter<T>> getCustomSetter();

    /*
     * KEY PANEL
     */
    DataKey<T> setKeyPanelProvider(KeyPanelProvider<T> panelProvider);

    Optional<KeyPanelProvider<T>> getKeyPanelProvider();

    default KeyPanel<T> getPanel(DataStore data) {

        return getKeyPanelProvider()
                .orElseThrow(() -> new RuntimeException(
                        "No panel defined for key '" + getName() + "' of type '" + getValueClass() + "'"))
                .getPanel(this, data);
    }

    /*
     * STORE DEFINITION
     * 
     * TODO: Check if this is really needed
     */
    DataKey<T> setStoreDefinition(StoreDefinition definition);

    Optional<StoreDefinition> getStoreDefinition();

    /**
     * Helper method which guarantees type safety.
     * 
     * @param data
     */
    /*
    default void updatePanel(DataStore data) {
    getPanel().setValue(data.get(this));
    }
    */

    /*
     * LABEL
     */
    DataKey<T> setLabel(String label);

    String getLabel();

    /*
     * EXTRA DATA
     */
    Optional<DataKeyExtraData> getExtraData();

    DataKey<T> setExtraData(DataKeyExtraData extraData);

    /*
     * TO STRING
     */
    /**
     * 
     * @param key
     * @return
     */
    static String toString(DataKey<?> key) {
        StringBuilder builder = new StringBuilder();

        builder.append(key.getName()).append(" (").append(key.getValueClass().getSimpleName());

        Optional<?> defaultValue = key.getDefault();

        if (defaultValue.isPresent()) {
            Object value = defaultValue.get();

            if (value instanceof DataStore) {
                DataStore dataStoreValue = (DataStore) value;
                if (dataStoreValue.getStoreDefinition().isPresent()) {
                    // Close parenthesis
                    builder.append(")");

                    String dataStoreString = DataKey.toString(dataStoreValue.getStoreDefinition().get().getKeys());
                    for (String line : StringLines.newInstance(dataStoreString)) {
                        builder.append("\n").append("   ").append(line);
                    }
                } else {
                    // Just close parenthesis, not definition of keys for this DataStore
                    builder.append(" - Undefined DataStore)");
                }

            } else {
                // Append default value only if it only occupies one line
                String defaultValueString = defaultValue.toString();
                if (StringLines.getLines(defaultValueString).size() == 1) {
                    builder.append(" = ").append(defaultValue).append(")");
                } else {
                    builder.append(" - has default value, but spans several lines)");
                }

            }
        } else {
            builder.append(")");
        }

        return builder.toString();
    }

    static String toString(Collection<DataKey<?>> keys) {
        StringBuilder builder = new StringBuilder();

        for (DataKey<?> option : keys) {
            builder.append(option);
            builder.append("\n");

        }

        return builder.toString();
    }

    /**
     * Copies the given object.
     * 
     * <p>
     * 1) Uses the defined copy function to copy the object; <br>
     * 2) Returns the object itself (shallow copy).
     * 
     * @return
     */
    // @SuppressWarnings("unchecked") // Should be ok
    default T copy(T object) {
        return getCopyFunction()
                .map(copyFunction -> copyFunction.apply(object))
                .orElse(object);
        // .orElse(object instanceof Copyable<?> ? (T) ((Copyable<?>) object).copy() : object);
    }

    default Object copyRaw(Object object) {
        return copy(getValueClass().cast(object));
        // return getCopyFunction()
        // .map(copy -> copy.apply(getValueClass().cast(object)))
        // .orElse(SpecsSystem.copy(getValueClass().cast(object)));
    }

    /**
     * A copy of this key, with decoder set.
     * 
     * @param decoder
     * @return
     */
    DataKey<T> setCopyFunction(Function<T, T> copyFunction);

    Optional<Function<T, T>> getCopyFunction();

    default DataKey<T> setCopyConstructor() {
        return setCopyFunction(object -> SpecsSystem.copy(object));
    }

    /**
     * If true, verifies if the class of a value being set is compatible with the value class of the key.
     * 
     * <p>
     * By default returns true.
     * 
     * TODO: Replace with a "customValueVerification"
     * 
     * @return
     */
    default boolean verifyValueClass() {
        return true;
    }

}
