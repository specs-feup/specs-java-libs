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

import pt.up.fe.specs.util.parsing.StringCodec;

/**
 * Implementation of DataKey that supports types with generics.
 *
 * @author JoaoBispo
 *
 * @param <T>
 */
public class GenericKey<T> extends ADataKey<T> {

    private final T exampleInstance;
    private transient Class<T> valueClass = null;

    /**
     *
     * @param id
     * @param aClass
     * @param defaultValue
     */
    public GenericKey(String id, T exampleInstance, Supplier<? extends T> defaultValue) {
        this(id, exampleInstance, defaultValue, null, null, null, null, null, null, null, null);
    }

    public GenericKey(String id, T exampleInstance) {
        this(id, exampleInstance, () -> null);
    }

    protected GenericKey(String id, T exampleInstance, Supplier<? extends T> defaultValueProvider,
            StringCodec<T> decoder, CustomGetter<T> customGetter, KeyPanelProvider<T> panelProvider, String label,
            StoreDefinition definition, Function<T, T> copyFunction, CustomGetter<T> customSetter,
            DataKeyExtraData extraData) {

        super(id, defaultValueProvider, decoder, customGetter, panelProvider, label, definition, copyFunction,
                customSetter, extraData);

        this.exampleInstance = exampleInstance;
    }

    // 'exampleInstance' should return the correct class, the check has been done in the constructor
    @SuppressWarnings("unchecked")
    @Override
    public Class<T> getValueClass() {
        return valueClass != null ? valueClass : (Class<T>) exampleInstance.getClass();
    }

    @SuppressWarnings("unchecked")
    @Override
    public DataKey<T> setValueClass(Class<?> valueClass) {
        this.valueClass = (Class<T>) valueClass;
        return this;
    }

    @Override
    protected DataKey<T> copy(String id, Supplier<? extends T> defaultValueProvider, StringCodec<T> decoder,
            CustomGetter<T> customGetter, KeyPanelProvider<T> panelProvider, String label,
            StoreDefinition definition, Function<T, T> copyFunction, CustomGetter<T> customSetter,
            DataKeyExtraData extraData) {

        return new GenericKey<>(id, this.exampleInstance, defaultValueProvider, decoder, customGetter, panelProvider,
                label, definition, copyFunction, customSetter, extraData);
    }

    /**
     * Due to the way Java implements generics, it is not possible to verify if a value is compatible based only on the
     * class of the example instance.
     * 
     */
    @Override
    public boolean verifyValueClass() {
        return false;
    }

}
