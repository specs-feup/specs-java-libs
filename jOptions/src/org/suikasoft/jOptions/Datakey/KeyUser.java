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
 * specific language governing permissions and limitations under the License. under the License.
 */

package org.suikasoft.jOptions.Datakey;

import java.util.Collection;
import java.util.Collections;

import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

/**
 * Represents a class that uses DataKeys (for reading and/or setting values).
 * 
 * @author JoaoBispo
 *
 */
public interface KeyUser {

    /**
     * 
     * @return a list of keys needed by the implementing class
     */
    default Collection<DataKey<?>> getReadKeys() {
	return Collections.emptyList();
    }

    /**
     * 
     * @return a list of keys that will be written by the implementing class
     */
    default Collection<DataKey<?>> getWriteKeys() {
	return Collections.emptyList();
    }

    /**
     * Checks if the given DataStore contains values for all the keys used by the implementing class. If there is no
     * value for any given key, an exception is thrown.
     * 
     * <p>
     * This method can only be called for DataStores that have a {@link StoreDefinition}. If no definition is present,
     * throws an exception.
     * <p>
     * If 'noDefaults' is true, it forces a value to exist in the table, even if the key has a default value.
     * 
     * @param data
     * @param noDefaults
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
