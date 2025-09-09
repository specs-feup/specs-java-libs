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

package org.suikasoft.jOptions.DataStore;

import java.util.ArrayList;
import java.util.List;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;
import org.suikasoft.jOptions.storedefinition.StoreDefinitionProvider;

/**
 * Interface for enums that provide a DataKey and a StoreDefinition.
 *
 * <p>
 * This interface is designed for enums that need to provide data keys and store
 * definitions in a type-safe manner. It combines the functionality of
 * {@link DataKeyProvider} and {@link StoreDefinitionProvider}.
 *
 * @param <T> the type of the enum implementing this interface
 */
public interface EnumDataKeyProvider<T extends Enum<T> & EnumDataKeyProvider<T>>
        extends DataKeyProvider, StoreDefinitionProvider {

    /**
     * Returns the DataKey associated with this enum constant.
     *
     * @return the DataKey associated with the enum constant
     */
    @Override
    DataKey<?> getDataKey();

    /**
     * Returns the class of the enum implementing this interface.
     *
     * @return the enum class
     */
    Class<T> getEnumClass();

    /**
     * Returns the StoreDefinition for the enum implementing this interface.
     *
     * <p>
     * The StoreDefinition contains all {@link DataKey}s provided by the enum
     * constants. This method aggregates all data keys from the enum constants into
     * a single store definition.
     *
     * @return the StoreDefinition for the enum
     */
    @Override
    default StoreDefinition getStoreDefinition() {
        List<DataKey<?>> keys = new ArrayList<>();
        for (T enumValue : getEnumClass().getEnumConstants()) {
            keys.add(enumValue.getDataKey());
        }
        return StoreDefinition.newInstance(getEnumClass().getSimpleName(), keys);
    }

}
