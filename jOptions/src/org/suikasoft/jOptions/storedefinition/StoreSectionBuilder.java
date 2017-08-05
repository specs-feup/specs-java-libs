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

package org.suikasoft.jOptions.storedefinition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.suikasoft.jOptions.Datakey.DataKey;

public class StoreSectionBuilder {

    private final String name;
    private final List<DataKey<?>> keys;
    private final Set<String> keysCheck;

    public StoreSectionBuilder() {
	this(null);
    }

    public StoreSectionBuilder(String name) {
	this.name = name;
	keys = new ArrayList<>();
	keysCheck = new HashSet<>();
    }

    public StoreSectionBuilder add(DataKey<?> key) {
	// Check
	if (keysCheck.contains(key.getName())) {
	    throw new RuntimeException("Datakey clash for name '" + key.getName() + "'");
	}
	keysCheck.add(key.getName());

	keys.add(key);

	return this;
    }

    public StoreSection build() {
	return StoreSection.newInstance(name, keys);
    }

}
