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
 * Simple implementation of {@link DataKey} for non-generic types.
 *
 * @param <T> the type of value associated with this key
 */
public class NormalKey<T> extends ADataKey<T> {

    private final Class<T> aClass;

    /**
     * Constructs a NormalKey with the given id and value class.
     *
     * @param id the key id
     * @param aClass the value class
     */
    public NormalKey(String id, Class<T> aClass) {
        this(id, aClass, () -> null);
    }

    /**
     * Constructs a NormalKey with the given id, value class, and default value provider.
     *
     * @param id the key id
     * @param aClass the value class
     * @param defaultValue the default value provider
     */
    public NormalKey(String id, Class<T> aClass, Supplier<T> defaultValue) {
        this(id, aClass, defaultValue, null, null, null, null, null, null, null, null);
    }

    /**
     * Full constructor for NormalKey with all options.
     *
     * @param id the key id
     * @param aClass the value class
     * @param defaultValueProvider the default value provider
     * @param decoder the string decoder for the value
     * @param customGetter the custom getter for the value
     * @param panelProvider the panel provider for the key
     * @param label the label for the key
     * @param definition the store definition
     * @param copyFunction the function to copy the value
     * @param customSetter the custom setter for the value
     * @param extraData additional data for the key
     */
    protected NormalKey(String id, Class<T> aClass, Supplier<? extends T> defaultValueProvider,
            StringCodec<T> decoder, CustomGetter<T> customGetter, KeyPanelProvider<T> panelProvider, String label,
            StoreDefinition definition, Function<T, T> copyFunction, CustomGetter<T> customSetter,
            DataKeyExtraData extraData) {
        super(id, defaultValueProvider, decoder, customGetter, panelProvider, label, definition, copyFunction,
                customSetter, extraData);
        this.aClass = aClass;
    }

    /**
     * Creates a copy of this NormalKey with the specified parameters.
     *
     * @param id the key id
     * @param defaultValueProvider the default value provider
     * @param decoder the string decoder for the value
     * @param customGetter the custom getter for the value
     * @param panelProvider the panel provider for the key
     * @param label the label for the key
     * @param definition the store definition
     * @param copyFunction the function to copy the value
     * @param customSetter the custom setter for the value
     * @param extraData additional data for the key
     * @return a new NormalKey instance
     */
    @Override
    protected DataKey<T> copy(String id, Supplier<? extends T> defaultValueProvider, StringCodec<T> decoder,
            CustomGetter<T> customGetter, KeyPanelProvider<T> panelProvider, String label, StoreDefinition definition,
            Function<T, T> copyFunction, CustomGetter<T> customSetter, DataKeyExtraData extraData) {
        return new NormalKey<>(id, aClass, defaultValueProvider, decoder, customGetter, panelProvider, label,
                definition, copyFunction, customSetter, extraData);
    }

    /**
     * Gets the class of the value associated with this key.
     *
     * @return the value class
     */
    @Override
    public Class<T> getValueClass() {
        return aClass;
    }

}
