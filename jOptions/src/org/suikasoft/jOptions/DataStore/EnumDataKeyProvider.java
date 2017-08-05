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
 * specific language governing permissions and limitations under the License. under the License.
 */

package org.suikasoft.jOptions.DataStore;

import java.util.ArrayList;
import java.util.List;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;
import org.suikasoft.jOptions.storedefinition.StoreDefinitionProvider;

public interface EnumDataKeyProvider<T extends Enum<T> & EnumDataKeyProvider<T>>
	extends DataKeyProvider, StoreDefinitionProvider {

    @Override
    DataKey<?> getDataKey();

    // <T extends Enum<T> & DataKeyProvider> Class<T> getEnumClass();
    Class<T> getEnumClass();
    /*
    default <T extends Enum<T> & DataKeyProvider> StoreDefinition getStoreDefinition() {
    	for (T enumConstant : getEnumClass().getEnumConstants()) {
    
    	}
    }
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
