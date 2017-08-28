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

import java.util.List;
import java.util.Optional;

import org.suikasoft.jOptions.Datakey.DataKey;

class GenericStoreSection implements StoreSection {

    private final String name;
    private final List<DataKey<?>> keys;

    public GenericStoreSection(String name, List<DataKey<?>> keys) {
	this.name = name;
	this.keys = keys;
    }

    @Override
    public Optional<String> getName() {
	return Optional.ofNullable(name);
    }

    @Override
    public List<DataKey<?>> getKeys() {
	return keys;
    }

}
