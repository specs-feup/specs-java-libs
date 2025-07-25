/**
 * Copyright 2015 SPeCS.
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
import java.util.Collections;

import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

/**
 * Interface for classes that use {@link DataKey} instances for reading and/or setting values.
 *
 * <p>This interface provides methods to retrieve the keys that are read or written by the implementing class, and to validate a {@link DataStore} against the keys required by the class.
 */
public interface KeyUser {

    /**
     * Retrieves a collection of keys that are read by the implementing class.
     *
     * <p>This method returns an empty collection by default, indicating that the class does not read any keys.
     *
     * @return a collection of {@link DataKey} instances that are read by the class
     */
    default Collection<DataKey<?>> getReadKeys() {
        return Collections.emptyList();
    }

    /**
     * Retrieves a collection of keys that are written by the implementing class.
     *
     * <p>This method returns an empty collection by default, indicating that the class does not write any keys.
     *
     * @return a collection of {@link DataKey} instances that are written by the class
     */
    default Collection<DataKey<?>> getWriteKeys() {
        return Collections.emptyList();
    }

    /**
     * Validates that the given {@link DataStore} contains values for all the keys required by the implementing class.
     *
     * <p>If any required key is missing, an exception is thrown. This method requires the {@link DataStore} to have a {@link StoreDefinition}. If no definition is present, an exception is thrown.
     *
     * <p>If the {@code noDefaults} parameter is set to {@code true}, the validation will require explicit values for all keys, even if the keys have default values.
     *
     * @param data the {@link DataStore} to validate
     * @param noDefaults whether to enforce explicit values for all keys, ignoring default values
     * @throws RuntimeException if the {@link DataStore} does not contain values for all required keys
     */
    default void check(DataStore data, boolean noDefaults) {
        if (!data.getStoreDefinitionTry().isPresent()) {
            throw new RuntimeException("This method requires that the DataStore has a StoreDefinition");
        }

        for (DataKey<?> key : data.getStoreDefinitionTry().get().getKeys()) {
            // Check if the key is present
            if (data.hasValue(key)) {
                continue;
            }

            // If defaults allowed, check if key has a default
            if (!noDefaults && key.hasDefaultValue()) {
                continue;
            }

            final String defaultStatus = noDefaults ? "disabled" : "enabled";

            // Check failed, throw exception
            throw new RuntimeException("DataStore check failed, class needs a definition for '" + key.getName()
                    + "' that is not present in the StoreDefinition (defaults are " + defaultStatus + ")");
        }
    }
}
