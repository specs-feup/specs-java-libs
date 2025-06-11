/**
 * Copyright 2013 SPeCS Research Group.
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

package org.suikasoft.jOptions.storedefinition;

import java.util.List;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;

/**
 * Default implementation of {@link StoreDefinition}.
 */
public class GenericStoreDefinition extends AStoreDefinition {

    /**
     * Creates a new store definition with the given name and options.
     *
     * @param appName the name of the store
     * @param options the list of keys
     */
    GenericStoreDefinition(String appName, List<DataKey<?>> options) {
        super(appName, options);
    }

    /**
     * Creates a new store definition with the given name, sections, and default values.
     *
     * @param appName the name of the store
     * @param sections the sections
     * @param defaultValues the default values
     */
    GenericStoreDefinition(String appName, List<StoreSection> sections, DataStore defaultValues) {
        super(appName, sections, defaultValues);
    }

    @Override
    public String toString() {
        return getName() + " -> " + getKeys();
    }
}
