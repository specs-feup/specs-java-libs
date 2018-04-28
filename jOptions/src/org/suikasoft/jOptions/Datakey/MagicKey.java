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

import java.util.function.Function;
import java.util.function.Supplier;

import org.suikasoft.jOptions.gui.KeyPanelProvider;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

import pt.up.fe.specs.util.SpecsStrings;

class MagicKey<T> extends ADataKey<T> {

    private MagicKey(String id, Supplier<T> defaultValue, StringCodec<T> decoder) {
        this(id, defaultValue, decoder, null, null, null, null, null, true);
    }

    private MagicKey(String id, Supplier<T> defaultValueProvider, StringCodec<T> decoder,
            CustomGetter<T> customGetter, KeyPanelProvider<T> panelProvider, String label, StoreDefinition definition,
            Function<T, T> copyFunction, boolean isByReference) {
        super(id, defaultValueProvider, decoder, customGetter, panelProvider, label, definition, copyFunction,
                isByReference);
    }

    public MagicKey(String id) {
        this(id, null, null);
        // this.id = id;
        // this.decoder = null;
    }

    /*
    public static <T> MagicKey<T> create(String id) {
    return new MagicKey<T>(id) {
    };
    }
    */

    @SuppressWarnings("unchecked")
    @Override
    public Class<T> getValueClass() {
        return (Class<T>) SpecsStrings.getSuperclassTypeParameter(this.getClass());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected DataKey<T> copy(String id, Supplier<? extends T> defaultValueProvider, StringCodec<T> decoder,
            CustomGetter<T> customGetter, KeyPanelProvider<T> panelProvider, String label, StoreDefinition definition,
            Function<T, T> copyFunction, boolean isByReference) {

        return new MagicKey(id, defaultValueProvider, decoder, customGetter, panelProvider, label,
                definition, copyFunction, isByReference) {
        };
    }

}
