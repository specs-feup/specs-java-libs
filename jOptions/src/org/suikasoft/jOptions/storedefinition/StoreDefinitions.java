/**
 * Copyright 2018 SPeCS.
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

package org.suikasoft.jOptions.storedefinition;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.suikasoft.jOptions.Datakey.DataKey;

import pt.up.fe.specs.util.utilities.CachedItems;

public class StoreDefinitions {

    private static final boolean ENABLE_STORE_DEFINITIONS_CACHE = true;
    private static final CachedItems<Class<?>, StoreDefinition> STORE_DEFINITIONS_CACHE = new CachedItems<>(
            StoreDefinitions::fromInterfacePrivate, true);

    public static CachedItems<Class<?>, StoreDefinition> getStoreDefinitionsCache() {
        return STORE_DEFINITIONS_CACHE;
    }

    /**
     * Collects all public static DataKey fields and builds a StoreDefinition with those fields.
     * 
     * @param aClass
     * @return
     */
    public static StoreDefinition fromInterface(Class<?> aClass) {
        if (ENABLE_STORE_DEFINITIONS_CACHE) {
            return STORE_DEFINITIONS_CACHE.get(aClass);
        }

        return fromInterfacePrivate(aClass);
    }

    private static StoreDefinition fromInterfacePrivate(Class<?> aClass) {

        StoreDefinitionBuilder builder = new StoreDefinitionBuilder(aClass.getSimpleName());

        for (Field field : aClass.getFields()) {
            if (!DataKey.class.isAssignableFrom(field.getType())) {
                continue;
            }

            if (!Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            try {
                builder.addKey((DataKey<?>) field.get(null));
            } catch (Exception e) {
                throw new RuntimeException("Could not retrive value of field: " + field);
            }
        }

        return builder.build();
    }

}
