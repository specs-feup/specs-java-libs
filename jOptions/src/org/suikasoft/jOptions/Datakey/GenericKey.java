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

import java.util.function.Function;
import java.util.function.Supplier;

import org.suikasoft.jOptions.gui.KeyPanelProvider;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

import pt.up.fe.specs.util.parsing.StringCodec;

/**
 * Implementation of {@link DataKey} that supports types with generics.
 *
 * <p>
 * This class allows the creation of data keys for values with generic types,
 * using an example instance to infer the value class.
 *
 * @param <T> the type of value associated with this key
 */
public class GenericKey<T> extends ADataKey<T> {

    /**
     * Example instance of the value type, used for class inference.
     */
    private final T exampleInstance;

    /**
     * Cached value class, may be set explicitly or inferred from the example
     * instance.
     */
    private transient Class<T> valueClass = null;

    /**
     * Constructs a GenericKey with the given id, example instance, and default
     * value provider.
     *
     * @param id              the key id
     * @param exampleInstance an example instance of the value type
     * @param defaultValue    the default value provider
     */
    public GenericKey(String id, T exampleInstance, Supplier<? extends T> defaultValue) {
        this(id, exampleInstance, defaultValue, null, null, null, null, null, null, null, null);
    }

    /**
     * Constructs a GenericKey with the given id and example instance. The default
     * value provider returns null.
     *
     * @param id              the key id
     * @param exampleInstance an example instance of the value type
     */
    public GenericKey(String id, T exampleInstance) {
        this(id, exampleInstance, () -> null);
    }

    /**
     * Full constructor for GenericKey with all options.
     *
     * @param id                   the key id
     * @param exampleInstance      an example instance of the value type
     * @param defaultValueProvider the default value provider
     * @param decoder              the string decoder/encoder for the value type
     * @param customGetter         a custom getter for the value
     * @param panelProvider        provider for UI panels
     * @param label                a label for the key
     * @param definition           the store definition
     * @param copyFunction         function to copy values
     * @param customSetter         a custom setter for the value
     * @param extraData            extra data for the key
     */
    protected GenericKey(String id, T exampleInstance, Supplier<? extends T> defaultValueProvider,
            StringCodec<T> decoder, CustomGetter<T> customGetter, KeyPanelProvider<T> panelProvider, String label,
            StoreDefinition definition, Function<T, T> copyFunction, CustomGetter<T> customSetter,
            DataKeyExtraData extraData) {
        super(id, defaultValueProvider, decoder, customGetter, panelProvider, label, definition, copyFunction,
                customSetter, extraData);
        this.exampleInstance = exampleInstance;
    }

    /**
     * Returns the class of the value type. If not explicitly set, it is inferred
     * from the example instance.
     *
     * @return the value class
     */
    @SuppressWarnings("unchecked")
    @Override
    public Class<T> getValueClass() {
        return valueClass != null ? valueClass : (Class<T>) exampleInstance.getClass();
    }

    /**
     * Sets the value class explicitly.
     *
     * @param valueClass the class to set
     * @return this GenericKey instance
     */
    @SuppressWarnings("unchecked")
    @Override
    public DataKey<T> setValueClass(Class<?> valueClass) {
        this.valueClass = (Class<T>) valueClass;
        return this;
    }

    /**
     * Creates a copy of this key with the given parameters.
     *
     * @param id                   the key id
     * @param defaultValueProvider the default value provider
     * @param decoder              the string decoder/encoder
     * @param customGetter         a custom getter
     * @param panelProvider        provider for UI panels
     * @param label                a label for the key
     * @param definition           the store definition
     * @param copyFunction         function to copy values
     * @param customSetter         a custom setter
     * @param extraData            extra data for the key
     * @return a new GenericKey instance
     */
    @Override
    protected DataKey<T> copy(String id, Supplier<? extends T> defaultValueProvider, StringCodec<T> decoder,
            CustomGetter<T> customGetter, KeyPanelProvider<T> panelProvider, String label,
            StoreDefinition definition, Function<T, T> copyFunction, CustomGetter<T> customSetter,
            DataKeyExtraData extraData) {
        return new GenericKey<>(id, this.exampleInstance, defaultValueProvider, decoder, customGetter, panelProvider,
                label, definition, copyFunction, customSetter, extraData);
    }

    /**
     * Due to the way Java implements generics, it is not possible to verify if a
     * value is compatible based only on the class of the example instance.
     *
     * @return false always, as generic type checking is not possible at runtime
     */
    @Override
    public boolean verifyValueClass() {
        return false;
    }

}
