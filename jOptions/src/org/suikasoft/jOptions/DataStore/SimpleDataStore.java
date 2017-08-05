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

package org.suikasoft.jOptions.DataStore;

import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

public class SimpleDataStore extends ADataStore {

    public SimpleDataStore(String name) {
        super(name);
    }

    public SimpleDataStore(String name, DataStore dataStore) {
        super(name, dataStore);
        // super(name, dataStore.getStoreDefinition().orElse(null));
        /*
        	// Add values
        	Optional<StoreDefinition> storeDefinition = dataStore.getStoreDefinition();
        	if (!storeDefinition.isPresent()) {
        	    LoggingUtils.msgInfo("StoreDefinition is not present, doing raw copy without key information");
        	    setValuesMap(getValues());
        	    return;
        	}
        
        for (DataKey<?> key : storeDefinition.get().getKeys()) {
            Object value = dataStore.get(key);
            setRaw(key, value);
        }
        */
    }

    /*
    public SimpleDataStore(String name, DataView setup) {
    super(name, setup);
    }
    */

    public SimpleDataStore(StoreDefinition storeDefinition) {
        super(storeDefinition);
    }

}
