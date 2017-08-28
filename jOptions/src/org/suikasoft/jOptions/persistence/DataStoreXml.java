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

package org.suikasoft.jOptions.persistence;

import java.util.Map;

import org.suikasoft.XStreamPlus.ObjectXml;
import org.suikasoft.jOptions.DataStore.SimpleDataStore;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.util.SpecsFactory;
import pt.up.fe.specs.util.utilities.StringList;

public class DataStoreXml extends ObjectXml<DataStore> {

    private static final Map<String, Class<?>> LIBRARY_CLASSES;

    static {
	LIBRARY_CLASSES = SpecsFactory.newHashMap();

	LIBRARY_CLASSES.put("StringList", StringList.class);
	LIBRARY_CLASSES.put("SimpleDataStore", SimpleDataStore.class);

    }

    public DataStoreXml() {
	addMappings(LIBRARY_CLASSES);
    }

    @Override
    public Class<DataStore> getTargetClass() {
	return DataStore.class;
    }

}
