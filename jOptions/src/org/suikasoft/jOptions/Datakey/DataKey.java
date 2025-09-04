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
 * specific language governing permissions and limitations under the License.
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
 * Keys for values with an associated type. DataKey equality is based only on the string name.
 *
 * <p>This interface defines the contract for keys that are associated with a value type, including methods for retrieving the key name, value class, decoder, and for copying or setting properties.
 *
 * @param <T> the type of value associated with this key
 * @see KeyFactory
 */
public interface DataKey<T> extends KeyProvider<String> {

    /**
     * Retrieves the key name.
     *
     * @return the name of the key
     */
    @Override
    default String getKey() {
        return getName();
    }

    /**
     * Retrieves the class type of the value associated with this key.
     *
     * @return the class type of the value
     */
    Class<T> getValueClass();

    /**
     * Sets the class type of the value associated with this key.
     *
     * @param valueClass the class type to set
     * @return the updated DataKey instance
     */
    DataKey<T> setValueClass(Class<?> valueClass);

    /**
     * Retrieves the name of the key.
     *
     * @return the name of the key
     */
    String getName();

    /**
     * Retrieves the simple name of the class type of the value associated with this key.
     *
     * @return the simple name of the class type
     */
    default String getTypeName() {
        return getValueClass().getSimpleName();
    }

    /**
     * Retrieves the optional decoder for this key.
     *
     * @return an Optional containing the decoder, if present
     */
    Optional<StringCodec<T>> getDecoder();

    /**
     * Creates a copy of this key with the specified decoder.
     *
     * @param decoder the decoder to set
     * @return the updated DataKey instance
     */
    DataKey<T> setDecoder(StringCodec<T> decoder);

    /**
     * Decodes the given encoded value using the decoder associated with this key.
     *
     * @param encodedValue the encoded value to decode
     * @return the decoded value
     * @throws RuntimeException if no decoder is set
     */
    default T decode(String encodedValue) {
        return getDecoder()
                .map(codec -> codec.decode(encodedValue))
                .orElseThrow(() -> new RuntimeException("No encoder/decoder set"));
    }

    /**
     * Encodes the given value using the decoder associated with this key.
     *
     * @param value the value to encode
     * @return the encoded value
     * @throws RuntimeException if no decoder is set
     */
    default String encode(T value) {
        return getDecoder()
                .map(codec -> codec.encode(value))
                .orElseThrow(() -> new RuntimeException("No encoder/decoder set"));
    }

    /**
     * Retrieves the optional default value for this key.
     *
     * @return an Optional containing the default value, if present
     */
    Optional<T> getDefault();

    /**
     * Checks if this key has a default value.
     *
     * @return true if a default value is present, false otherwise
     */
    boolean hasDefaultValue();

    /**
     * Creates a copy of this key with the specified default value supplier.
     *
     * @param defaultValue the supplier for the default value
     * @return the updated DataKey instance
     */
    DataKey<T> setDefault(Supplier<? extends T> defaultValue);

    /**
     * Creates a copy of this key with the specified raw default value supplier.
     *
     * @param defaultValue the raw supplier for the default value
     * @return the updated DataKey instance
     */
    DataKey<T> setDefaultRaw(Supplier<?> defaultValue);

    /**
     * Creates a copy of this key with the specified default value as a string.
     *
     * @param stringValue the default value as a string
     * @return the updated DataKey instance
     * @throws RuntimeException if no decoder is set
     */
    default DataKey<T> setDefaultString(String stringValue) {
        if (getDecoder().isEmpty()) {
            throw new RuntimeException("Can only use this method if a decoder was set before");
        }

        return this.setDefault(() -> getDecoder().get().decode(stringValue));
    }

    /**
     * Creates a copy of this key with the specified custom getter.
     *
     * @param defaultValue the custom getter to set
     * @return the updated DataKey instance
     */
    DataKey<T> setCustomGetter(CustomGetter<T> defaultValue);

    /**
     * Retrieves the optional custom getter for this key.
     *
     * @return an Optional containing the custom getter, if present
     */
    Optional<CustomGetter<T>> getCustomGetter();

    /**
     * Creates a copy of this key with the specified custom setter.
     *
     * @param setProcessing the custom setter to set
     * @return the updated DataKey instance
     */
    DataKey<T> setCustomSetter(CustomGetter<T> setProcessing);

    /**
     * Retrieves the optional custom setter for this key.
     *
     * @return an Optional containing the custom setter, if present
     */
    Optional<CustomGetter<T>> getCustomSetter();

    /**
     * Creates a copy of this key with the specified key panel provider.
     *
     * @param panelProvider the key panel provider to set
     * @return the updated DataKey instance
     */
    DataKey<T> setKeyPanelProvider(KeyPanelProvider<T> panelProvider);

    /**
     * Retrieves the optional key panel provider for this key.
     *
     * @return an Optional containing the key panel provider, if present
     */
    Optional<KeyPanelProvider<T>> getKeyPanelProvider();

    /**
     * Retrieves the key panel for this key using the given data store.
     *
     * @param data the data store to use
     * @return the key panel
     * @throws RuntimeException if no panel provider is defined
     */
    default KeyPanel<T> getPanel(DataStore data) {
        return getKeyPanelProvider()
                .orElseThrow(() -> new RuntimeException(
                        "No panel defined for key '" + getName() + "' of type '" + getValueClass() + "'"))
                .getPanel(this, data);
    }

    /**
     * Creates a copy of this key with the specified store definition.
     *
     * @param definition the store definition to set
     * @return the updated DataKey instance
     */
    DataKey<T> setStoreDefinition(StoreDefinition definition);

    /**
     * Retrieves the optional store definition for this key.
     *
     * @return an Optional containing the store definition, if present
     */
    Optional<StoreDefinition> getStoreDefinition();

    /**
     * Creates a copy of this key with the specified label.
     *
     * @param label the label to set
     * @return the updated DataKey instance
     */
    DataKey<T> setLabel(String label);

    /**
     * Retrieves the label for this key.
     *
     * @return the label
     */
    String getLabel();

    /**
     * Retrieves the optional extra data for this key.
     *
     * @return an Optional containing the extra data, if present
     */
    Optional<DataKeyExtraData> getExtraData();

    /**
     * Creates a copy of this key with the specified extra data.
     *
     * @param extraData the extra data to set
     * @return the updated DataKey instance
     */
    DataKey<T> setExtraData(DataKeyExtraData extraData);

    /**
     * Converts the given key to a string representation.
     *
     * @param key the key to convert
     * @return the string representation of the key
     */
    static String toString(DataKey<?> key) {
        StringBuilder builder = new StringBuilder();

        builder.append(key.getName()).append(" (").append(key.getValueClass().getSimpleName());

        Optional<?> defaultValue = key.getDefault();

        if (defaultValue.isPresent()) {
            Object value = defaultValue.get();

            if (value instanceof DataStore dataStoreValue) {
                if (dataStoreValue.getStoreDefinitionTry().isPresent()) {
                    builder.append(")");

                    String dataStoreString = DataKey.toString(dataStoreValue.getStoreDefinitionTry().get().getKeys());
                    for (String line : StringLines.newInstance(dataStoreString)) {
                        builder.append("\n").append("   ").append(line);
                    }
                } else {
                    builder.append(" - Undefined DataStore)");
                }

            } else {
                String defaultValueString = value.toString();
                if (StringLines.getLines(defaultValueString).size() == 1) {
                    builder.append(" = ").append(value).append(")");
                } else {
                    builder.append(" - has default value, but spans several lines)");
                }

            }
        } else {
            builder.append(")");
        }

        return builder.toString();
    }

    /**
     * Converts the given collection of keys to a string representation.
     *
     * @param keys the collection of keys to convert
     * @return the string representation of the keys
     */
    static String toString(Collection<DataKey<?>> keys) {
        StringBuilder builder = new StringBuilder();

        for (DataKey<?> option : keys) {
            if (option == null) {
                throw new IllegalArgumentException("DataKey collection contains null element");
            }
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
     * @param object the object to copy
     * @return the copied object
     */
    default T copy(T object) {
        return getCopyFunction()
                .map(copyFunction -> copyFunction.apply(object))
                .orElse(object);
    }

    /**
     * Copies the given object as a raw value.
     *
     * @param object the object to copy
     * @return the copied object
     */
    default Object copyRaw(Object object) {
        return copy(getValueClass().cast(object));
    }

    /**
     * Creates a copy of this key with the specified copy function.
     *
     * @param copyFunction the copy function to set
     * @return the updated DataKey instance
     */
    DataKey<T> setCopyFunction(Function<T, T> copyFunction);

    /**
     * Retrieves the optional copy function for this key.
     *
     * @return an Optional containing the copy function, if present
     */
    Optional<Function<T, T>> getCopyFunction();

    /**
     * Creates a copy of this key with a default copy constructor.
     *
     * @return the updated DataKey instance
     */
    default DataKey<T> setCopyConstructor() {
        return setCopyFunction(SpecsSystem::copy);
    }

    /**
     * Checks if the class of a value being set is compatible with the value class of the key.
     *
     * @return true if the class is compatible, false otherwise
     */
    default boolean verifyValueClass() {
        return true;
    }

}
