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

package org.suikasoft.jOptions.storedefinition;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.suikasoft.jOptions.Datakey.DataKey;

/**
 * Represents a section in a store definition, grouping related keys.
 */
public interface StoreSection {

    /**
     * Returns the name of the section, if present.
     *
     * @return the name of the section, or empty if unnamed
     */
    Optional<String> getName();

    /**
     * Returns the keys of the section.
     *
     * @return the keys of the section
     */
    List<DataKey<?>> getKeys();

    /**
     * Creates a new section with the given name and keys.
     *
     * @param name the section name
     * @param keys the keys in the section
     * @return a new StoreSection instance
     */
    static StoreSection newInstance(String name, List<DataKey<?>> keys) {
        return new GenericStoreSection(name, keys);
    }

    /**
     * Creates a new unnamed section with the given keys.
     *
     * @param keys the keys in the section
     * @return a new StoreSection instance
     */
    static StoreSection newInstance(List<DataKey<?>> keys) {
        return newInstance(null, keys);
    }

    /**
     * Returns a list with all the keys of the given sections.
     *
     * @param sections the sections to extract keys from
     * @return a list with all keys from the sections
     */
    static List<DataKey<?>> getAllKeys(List<StoreSection> sections) {
        return sections.stream()
                .flatMap(section -> section.getKeys().stream())
                .collect(Collectors.toList());
    }
}
