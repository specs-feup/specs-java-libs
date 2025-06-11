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

import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.parsing.StringCodec;

/**
 * Special DataKey implementation for advanced or dynamic key scenarios.
 *
 * <p>This class is intended for use cases where the key type or behavior is determined dynamically or requires special handling.
 *
 * @param <T> the type of value associated with this key
 */
class MagicKey<T> extends ADataKey<T> {

    /**
     * Constructs a MagicKey with the given id.
     *
     * @param id the key id
     */
    public MagicKey(String id) {
        this(id, null, null);
    }

    /**
     * Constructs a MagicKey with the given id, default value, and decoder.
     *
     * @param id the key id
     * @param defaultValue the default value provider
     * @param decoder the string decoder
     */
    private MagicKey(String id, Supplier<T> defaultValue, StringCodec<T> decoder) {
        this(id, defaultValue, decoder, null, null, null, null, null, null, null);
    }

    /**
     * Full constructor for MagicKey with all options.
     *
     * @param id the key id
     * @param defaultValueProvider the default value provider
     * @param decoder the string decoder
     * @param customGetter the custom getter
     * @param panelProvider the panel provider
     * @param label the label
     * @param definition the store definition
     * @param copyFunction the copy function
     * @param customSetter the custom setter
     * @param extraData extra data for the key
     */
    private MagicKey(String id, Supplier<T> defaultValueProvider, StringCodec<T> decoder,
            CustomGetter<T> customGetter, KeyPanelProvider<T> panelProvider, String label, StoreDefinition definition,
            Function<T, T> copyFunction, CustomGetter<T> customSetter, DataKeyExtraData extraData) {
        super(id, defaultValueProvider, decoder, customGetter, panelProvider, label, definition, copyFunction,
                customSetter, extraData);
    }

    /**
     * Returns the value class for this key, inferred from the generic type parameter.
     *
     * @return the value class
     */
    @SuppressWarnings("unchecked")
    @Override
    public Class<T> getValueClass() {
        return (Class<T>) SpecsStrings.getSuperclassTypeParameter(this.getClass());
    }

    /**
     * Creates a copy of this MagicKey with the given parameters.
     *
     * @param id the key id
     * @param defaultValueProvider the default value provider
     * @param decoder the string decoder
     * @param customGetter the custom getter
     * @param panelProvider the panel provider
     * @param label the label
     * @param definition the store definition
     * @param copyFunction the copy function
     * @param customSetter the custom setter
     * @param extraData extra data for the key
     * @return a new MagicKey instance
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected DataKey<T> copy(String id, Supplier<? extends T> defaultValueProvider, StringCodec<T> decoder,
            CustomGetter<T> customGetter, KeyPanelProvider<T> panelProvider, String label, StoreDefinition definition,
            Function<T, T> copyFunction, CustomGetter<T> customSetter, DataKeyExtraData extraData) {
        return new MagicKey(id, defaultValueProvider, decoder, customGetter, panelProvider, label,
                definition, copyFunction, customSetter, extraData) {
        };
    }
}
