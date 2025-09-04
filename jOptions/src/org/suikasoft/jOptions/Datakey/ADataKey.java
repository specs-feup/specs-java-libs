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

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import org.suikasoft.jOptions.gui.KeyPanelProvider;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

import pt.up.fe.specs.util.exceptions.NotImplementedException;
import pt.up.fe.specs.util.parsing.StringCodec;

/**
 * Abstract base class for {@link DataKey} implementations.
 *
 * <p>This class provides the foundational implementation for data keys, including support for default values, decoders, custom getters/setters, and extra data.
 *
 * @param <T> the type of value associated with this key
 */
public abstract class ADataKey<T> implements DataKey<T> {

    private final String id;
    private transient final Supplier<? extends T> defaultValueProvider;
    private transient final StringCodec<T> decoder;
    private transient final CustomGetter<T> customGetter;
    private transient final KeyPanelProvider<T> panelProvider;
    private transient final String label;
    private transient final StoreDefinition definition;
    private transient final Function<T, T> copyFunction;
    private transient final CustomGetter<T> customSetter;
    private transient final DataKeyExtraData extraData;

    /**
     * Constructs an instance of {@code ADataKey} with the specified parameters.
     *
     * @param id the unique identifier for this key
     * @param defaultValueProvider a supplier for the default value of this key
     * @param decoder a codec for encoding and decoding values
     * @param customGetter a custom getter for retrieving values
     * @param panelProvider a provider for GUI panels associated with this key
     * @param label a label for this key
     * @param definition the store definition associated with this key
     * @param copyFunction a function for copying values
     * @param customSetter a custom setter for setting values
     * @param extraData additional data associated with this key
     */
    protected ADataKey(String id, Supplier<? extends T> defaultValueProvider, StringCodec<T> decoder,
            CustomGetter<T> customGetter, KeyPanelProvider<T> panelProvider, String label,
            StoreDefinition definition, Function<T, T> copyFunction, CustomGetter<T> customSetter,
            DataKeyExtraData extraData) {

        assert id != null;

        this.id = id;
        this.defaultValueProvider = defaultValueProvider;
        this.decoder = decoder;
        this.customGetter = customGetter;
        this.panelProvider = panelProvider;
        this.label = label;
        this.definition = definition;
        this.copyFunction = copyFunction;
        this.customSetter = customSetter;
        this.extraData = extraData;
    }

    /**
     * Constructs an instance of {@code ADataKey} with the specified identifier and default value provider.
     *
     * @param id the unique identifier for this key
     * @param defaultValue a supplier for the default value of this key
     */
    protected ADataKey(String id, Supplier<T> defaultValue) {
        this(id, defaultValue, null, null, null, null, null, null, null, null);
    }

    /**
     * Returns the name of this key.
     *
     * @return the name of this key
     */
    @Override
    public String getName() {
        return id;
    }

    /**
     * Returns a string representation of this key.
     *
     * @return a string representation of this key
     */
    @Override
    public String toString() {
        return DataKey.toString(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        ADataKey<?> other = (ADataKey<?>) obj;
        if (id == null) {
            return other.id == null;
        } else {
            return id.equals(other.id);
        }
    }

    /**
     * Creates a copy of this {@code DataKey} with the specified parameters.
     *
     * @param id the unique identifier for the new key
     * @param defaultValueProvider a supplier for the default value of the new key
     * @param decoder a codec for encoding and decoding values
     * @param customGetter a custom getter for retrieving values
     * @param panelProvider a provider for GUI panels associated with the new key
     * @param label a label for the new key
     * @param definition the store definition associated with the new key
     * @param copyFunction a function for copying values
     * @param customSetter a custom setter for setting values
     * @param extraData additional data associated with the new key
     * @return a new {@code DataKey} instance
     */
    abstract protected DataKey<T> copy(String id, Supplier<? extends T> defaultValueProvider, StringCodec<T> decoder,
            CustomGetter<T> customGetter, KeyPanelProvider<T> panelProvider, String label,
            StoreDefinition definition, Function<T, T> copyFunction, CustomGetter<T> customSetter,
            DataKeyExtraData extraData);

    /**
     * Returns the decoder associated with this key, if present.
     *
     * @return an {@code Optional} containing the decoder, or an empty {@code Optional} if no decoder is set
     */
    @Override
    public Optional<StringCodec<T>> getDecoder() {
        return Optional.ofNullable(decoder);
    }

    /**
     * Sets the decoder for this key.
     *
     * @param decoder the new decoder
     * @return a new {@code DataKey} instance with the updated decoder
     */
    @Override
    public DataKey<T> setDecoder(StringCodec<T> decoder) {
        StringCodec<T> serializableDecoder = getSerializableDecoder(decoder);
        return copy(id, defaultValueProvider, serializableDecoder, customGetter,
                panelProvider, label, definition, copyFunction, customSetter, extraData);
    }

    private static <T> StringCodec<T> getSerializableDecoder(StringCodec<T> decoder) {
        if (decoder instanceof Serializable) {
            return decoder;
        }

        return StringCodec.newInstance(decoder::encode, decoder::decode);
    }

    /**
     * Returns the default value for this key, if present.
     *
     * @return an {@code Optional} containing the default value, or an empty {@code Optional} if no default value is set
     */
    @Override
    public Optional<T> getDefault() {
        if (defaultValueProvider != null) {
            return Optional.ofNullable(defaultValueProvider.get());
        }

        return Optional.empty();
    }

    /**
     * Checks if this key has a default value.
     *
     * @return {@code true} if this key has a default value, {@code false} otherwise
     */
    @Override
    public boolean hasDefaultValue() {
        return defaultValueProvider != null;
    }

    /**
     * Sets the default value provider for this key.
     *
     * @param defaultValueProvider the new default value provider
     * @return a new {@code DataKey} instance with the updated default value provider
     */
    @Override
    public DataKey<T> setDefault(Supplier<? extends T> defaultValueProvider) {
        return copy(id, defaultValueProvider, decoder, customGetter, panelProvider, label, definition, copyFunction,
                customSetter, extraData);
    }

    @SuppressWarnings("unchecked")
    @Override
    public DataKey<T> setDefaultRaw(Supplier<?> defaultValueProvider) {
        return copy(id, (Supplier<? extends T>) defaultValueProvider, decoder, customGetter, panelProvider, label,
                definition, copyFunction,
                customSetter, extraData);
    }

    /**
     * Sets a custom getter for this key.
     *
     * @param customGetter the new custom getter
     * @return a new {@code DataKey} instance with the updated custom getter
     */
    @Override
    public DataKey<T> setCustomGetter(CustomGetter<T> customGetter) {
        @SuppressWarnings("unchecked")
        CustomGetter<T> serializableGetter = (CustomGetter<T> & Serializable) (value, dataStore) -> customGetter
                .get(value, dataStore);

        return copy(id, defaultValueProvider, decoder, serializableGetter, panelProvider, label, definition,
                copyFunction, customSetter, extraData);
    }

    /**
     * Sets a custom setter for this key.
     *
     * @param customSetter the new custom setter
     * @return a new {@code DataKey} instance with the updated custom setter
     */
    @Override
    public DataKey<T> setCustomSetter(CustomGetter<T> customSetter) {
        @SuppressWarnings("unchecked")
        CustomGetter<T> serializableSetter = (CustomGetter<T> & Serializable) (value, dataStore) -> customSetter
                .get(value, dataStore);

        return copy(id, defaultValueProvider, decoder, customGetter, panelProvider, label, definition,
                copyFunction, serializableSetter, extraData);
    }

    /**
     * Returns the custom getter associated with this key, if present.
     *
     * @return an {@code Optional} containing the custom getter, or an empty {@code Optional} if no custom getter is set
     */
    @Override
    public Optional<CustomGetter<T>> getCustomGetter() {
        return Optional.ofNullable(customGetter);
    }

    /**
     * Returns the custom setter associated with this key, if present.
     *
     * @return an {@code Optional} containing the custom setter, or an empty {@code Optional} if no custom setter is set
     */
    @Override
    public Optional<CustomGetter<T>> getCustomSetter() {
        return Optional.ofNullable(customSetter);
    }

    /**
     * Sets the panel provider for this key.
     *
     * @param panelProvider the new panel provider
     * @return a new {@code DataKey} instance with the updated panel provider
     */
    @Override
    public DataKey<T> setKeyPanelProvider(KeyPanelProvider<T> panelProvider) {
        return copy(id, defaultValueProvider, decoder, customGetter, panelProvider, label, definition, copyFunction,
                customSetter, extraData);
    }

    /**
     * Returns the panel provider associated with this key, if present.
     *
     * @return an {@code Optional} containing the panel provider, or an empty {@code Optional} if no panel provider is set
     */
    @Override
    public Optional<KeyPanelProvider<T>> getKeyPanelProvider() {
        return Optional.ofNullable(panelProvider);
    }

    /**
     * Sets the label for this key.
     *
     * @param label the new label
     * @return a new {@code DataKey} instance with the updated label
     */
    @Override
    public DataKey<T> setLabel(String label) {
        return copy(id, defaultValueProvider, decoder, customGetter, panelProvider, label, definition, copyFunction,
                customSetter, extraData);
    }

    /**
     * Returns the label for this key. If no label is set, returns the name of the key.
     *
     * @return the label for this key
     */
    @Override
    public String getLabel() {
        if (label == null) {
            return getName();
        }

        return label;
    }

    /**
     * Sets the store definition for this key.
     *
     * @param definition the new store definition
     * @return a new {@code DataKey} instance with the updated store definition
     */
    @Override
    public DataKey<T> setStoreDefinition(StoreDefinition definition) {
        return copy(id, defaultValueProvider, decoder, customGetter, panelProvider, label, definition, copyFunction,
                customSetter, extraData);
    }

    /**
     * Returns the store definition associated with this key, if present.
     *
     * @return an {@code Optional} containing the store definition, or an empty {@code Optional} if no store definition is set
     */
    @Override
    public Optional<StoreDefinition> getStoreDefinition() {
        return Optional.ofNullable(definition);
    }

    /**
     * Sets the copy function for this key.
     *
     * @param copyFunction the new copy function
     * @return a new {@code DataKey} instance with the updated copy function
     */
    @Override
    public DataKey<T> setCopyFunction(Function<T, T> copyFunction) {
        return copy(id, defaultValueProvider, decoder, customGetter, panelProvider, label, definition, copyFunction,
                customSetter, extraData);
    }

    @Override
    public DataKey<T> setValueClass(Class<?> valueClass) {
        throw new NotImplementedException(this);
    }

    /**
     * Returns the copy function associated with this key, if present. If no copy function is set, uses the encoder/decoder by default.
     *
     * @return an {@code Optional} containing the copy function, or an empty {@code Optional} if no copy function is set
     */
    @Override
    public Optional<Function<T, T>> getCopyFunction() {
        if (copyFunction == null && getDecoder().isPresent()) {
            var codec = getDecoder().get();
            Function<T, T> copy = value -> ADataKey.copy(value, codec);
            return Optional.of(copy);
        }

        return Optional.ofNullable(copyFunction);
    }

    private static <T> T copy(T value, StringCodec<T> codec) {
        var encodedValue = codec.encode(value);
        return codec.decode(encodedValue);
    }

    /**
     * Returns the extra data associated with this key, if present.
     *
     * @return an {@code Optional} containing the extra data, or an empty {@code Optional} if no extra data is set
     */
    @Override
    public Optional<DataKeyExtraData> getExtraData() {
        return Optional.ofNullable(extraData);
    }

    /**
     * Sets the extra data for this key.
     *
     * @param extraData the new extra data
     * @return a new {@code DataKey} instance with the updated extra data
     */
    @Override
    public DataKey<T> setExtraData(DataKeyExtraData extraData) {
        return copy(id, defaultValueProvider, decoder, customGetter, panelProvider, label, definition, copyFunction,
                customSetter, extraData);
    }
}
