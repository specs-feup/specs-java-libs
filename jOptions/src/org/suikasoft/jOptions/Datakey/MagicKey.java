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

import java.lang.reflect.Type;
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
 * <p>For reliable type information, consider using constructors that accept an explicit Class&lt;T&gt; parameter.
 * When no explicit type is provided, the class attempts to infer the type from generic parameters,
 * but this may fall back to Object.class in certain scenarios due to Java type erasure.
 *
 * @param <T> the type of value associated with this key
 */
class MagicKey<T> extends ADataKey<T> {

    /** The explicit value class, if provided */
    private final Class<T> explicitValueClass;

    /**
     * Constructs a MagicKey with the given id.
     *
     * @param id the key id
     */
    public MagicKey(String id) {
        this(id, null, null, null);
    }

    /**
     * Private constructor with explicit value class support.
     * Use static factory methods for type-safe creation with explicit types.
     *
     * @param id the key id
     * @param valueClass the explicit value class
     */
    private MagicKey(String id, Class<T> valueClass) {
        this(id, valueClass, null, null);
    }

    /**
     * Constructs a MagicKey with the given id, explicit value class, default value, and decoder.
     *
     * @param id the key id
     * @param valueClass the explicit value class (may be null for type inference)
     * @param defaultValue the default value provider
     * @param decoder the string decoder
     */
    private MagicKey(String id, Class<T> valueClass, Supplier<T> defaultValue, StringCodec<T> decoder) {
        this(id, valueClass, defaultValue, decoder, null, null, null, null, null, null, null);
    }

    /**
     * Creates a type-safe MagicKey with explicit class information.
     * This method provides a convenient way to create MagicKey instances with reliable type information.
     *
     * @param <T> the type of value associated with this key
     * @param id the key id
     * @param valueClass the value class
     * @return a new MagicKey instance with explicit type information
     */
    public static <T> MagicKey<T> create(String id, Class<T> valueClass) {
        return new MagicKey<>(id, valueClass);
    }

    /**
     * Creates a type-safe MagicKey with explicit class information and default value.
     *
     * @param <T> the type of value associated with this key
     * @param id the key id
     * @param valueClass the value class
     * @param defaultValue the default value
     * @return a new MagicKey instance with explicit type information
     */
    public static <T> MagicKey<T> create(String id, Class<T> valueClass, T defaultValue) {
        return new MagicKey<>(id, valueClass, () -> defaultValue, null);
    }

    /**
     * Full constructor for MagicKey with all options.
     *
     * @param id the key id
     * @param valueClass the explicit value class (may be null for type inference)
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
    private MagicKey(String id, Class<T> valueClass, Supplier<T> defaultValueProvider, StringCodec<T> decoder,
            CustomGetter<T> customGetter, KeyPanelProvider<T> panelProvider, String label, StoreDefinition definition,
            Function<T, T> copyFunction, CustomGetter<T> customSetter, DataKeyExtraData extraData) {
        super(id, defaultValueProvider, decoder, customGetter, panelProvider, label, definition, copyFunction,
                customSetter, extraData);
        this.explicitValueClass = valueClass;
    }

    /**
     * Legacy constructor for backward compatibility.
     * Used by reflection-based code that expects the original constructor signature.
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
    @SuppressWarnings("unused") // Used by reflection in tests
    private MagicKey(String id, Supplier<T> defaultValueProvider, StringCodec<T> decoder,
            CustomGetter<T> customGetter, KeyPanelProvider<T> panelProvider, String label, StoreDefinition definition,
            Function<T, T> copyFunction, CustomGetter<T> customSetter, DataKeyExtraData extraData) {
        this(id, null, defaultValueProvider, decoder, customGetter, panelProvider, label, definition, copyFunction,
                customSetter, extraData);
    }

    /**
     * Returns the value class for this key.
     * If an explicit value class was provided during construction, it is returned.
     * Otherwise, attempts to infer the type from the generic type parameter.
     *
     * @return the value class
     */
    @SuppressWarnings("unchecked")
    @Override
    public Class<T> getValueClass() {
        // First, check if we have an explicit value class
        if (explicitValueClass != null) {
            return explicitValueClass;
        }
        
        // Try to get type from this class (works for anonymous classes)
        Class<?> currentClass = this.getClass();
        
        // For anonymous classes, get the generic superclass type
        if (currentClass.isAnonymousClass()) {
            try {
                Type genericSuperclass = currentClass.getGenericSuperclass();
                if (genericSuperclass instanceof java.lang.reflect.ParameterizedType) {
                    java.lang.reflect.ParameterizedType pt = (java.lang.reflect.ParameterizedType) genericSuperclass;
                    Type[] actualTypes = pt.getActualTypeArguments();
                    if (actualTypes.length > 0 && actualTypes[0] instanceof Class) {
                        return (Class<T>) actualTypes[0];
                    }
                }
            } catch (Exception e) {
                // Continue to other approaches
            }
        }
        
        // Try the SpecsStrings utility for regular inheritance
        try {
            Class<?> result = SpecsStrings.getSuperclassTypeParameter(currentClass);
            if (result != null) {
                return (Class<T>) result;
            }
        } catch (RuntimeException e) {
            // Type inference failed, continue to fallback
        }
        
        // Type inference failed, fallback to Object class
        // This can happen when MagicKey is created with raw types or through reflection
        return (Class<T>) Object.class;
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
        return new MagicKey(id, this.explicitValueClass, defaultValueProvider, decoder, customGetter, panelProvider, label,
                definition, copyFunction, customSetter, extraData) {
        };
    }
}
