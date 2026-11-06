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

package org.suikasoft.jOptions.persistence;

import java.util.LinkedHashMap;
import java.util.Map;

import org.suikasoft.XStreamPlus.ObjectXml;
import org.suikasoft.jOptions.DataStore.SimpleDataStore;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

import pt.up.fe.specs.util.utilities.StringList;

public class DataStoreXml extends ObjectXml<DataStore> {

    private static final Map<String, Class<?>> LIBRARY_CLASSES;

    static {
        LIBRARY_CLASSES = new LinkedHashMap<>();

        LIBRARY_CLASSES.put("StringList", StringList.class);
        LIBRARY_CLASSES.put("SimpleDataStore", SimpleDataStore.class);
    }

    public DataStoreXml(StoreDefinition storeDefinition) {
        if (storeDefinition == null) {
            throw new NullPointerException("StoreDefinition cannot be null");
        }
        addMappings(LIBRARY_CLASSES);
    }

    @Override
    public Class<DataStore> getTargetClass() {
        return DataStore.class;
    }

    // Retain the typed entry point for callers compiled against the existing API.
    public String toXml(DataStore dataStore) {
        return toXml((Object) dataStore);
    }

    @Override
    public String toXml(Object object) {
        if (object != null && !(object instanceof DataStore)) {
            return super.toXml(object);
        }
        DataStore dataStore = (DataStore) object;
        if (dataStore == null) {
            throw new NullPointerException("DataStore cannot be null");
        }

        // Save only the keys of the store definition, like the save operation
        // of the AppPersistence does
        return super.toXml(XmlPersistence.getDataStoreToSave(dataStore));
    }

}
