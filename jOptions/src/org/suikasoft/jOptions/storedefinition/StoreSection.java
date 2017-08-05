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
import java.util.stream.Collectors;

import org.suikasoft.jOptions.Datakey.DataKey;

public interface StoreSection {

    /**
     * 
     * @return the name of the section
     */
    Optional<String> getName();

    /**
     * 
     * @return the keys of the section
     */
    List<DataKey<?>> getKeys();

    static StoreSection newInstance(String name, List<DataKey<?>> keys) {
	return new GenericStoreSection(name, keys);
    }

    static StoreSection newInstance(List<DataKey<?>> keys) {
	return newInstance(null, keys);
    }

    /**
     * 
     * @param sections
     * @return a list with all the keys of the given sections
     */
    static List<DataKey<?>> getAllKeys(List<StoreSection> sections) {
	return sections.stream()
		.flatMap(section -> section.getKeys().stream())
		.collect(Collectors.toList());
    }
}
