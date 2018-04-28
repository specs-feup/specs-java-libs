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

/**
 * Simple implementation of DataKey.
 *
 * TODO: Make class extend ADataKey instead of DataKey.
 * 
 * @author JoaoBispo
 *
 * @param <T>
 */
class NormalKey<T> extends ADataKey<T> {

    // TODO: Temporarily removing 'transient' from aClass and default value, while phasing out Setup
    private final Class<T> aClass;

    public NormalKey(String id, Class<T> aClass) {
        this(id, aClass, () -> null);
    }

    protected NormalKey(String id, Class<T> aClass, Supplier<? extends T> defaultValueProvider,
            StringCodec<T> decoder, CustomGetter<T> customGetter, KeyPanelProvider<T> panelProvider, String label,
            StoreDefinition definition, Function<T, T> copyFunction, boolean isByReference) {
        super(id, defaultValueProvider, decoder, customGetter, panelProvider, label, definition, copyFunction,
                isByReference);

        this.aClass = aClass;
    }

    /**
     *
     * @param id
     * @param aClass
     * @param defaultValue
     */
    public NormalKey(String id, Class<T> aClass, Supplier<T> defaultValue) {
        this(id, aClass, defaultValue, null, null, null, null, null, null, true);
    }

    @Override
    protected DataKey<T> copy(String id, Supplier<? extends T> defaultValueProvider, StringCodec<T> decoder,
            CustomGetter<T> customGetter, KeyPanelProvider<T> panelProvider, String label, StoreDefinition definition,
            Function<T, T> copyFunction, boolean isByReference) {

        return new NormalKey<>(id, aClass, defaultValueProvider, decoder, customGetter, panelProvider, label,
                definition, copyFunction, isByReference);
    }

    @Override
    public Class<T> getValueClass() {
        return aClass;
    }

}
