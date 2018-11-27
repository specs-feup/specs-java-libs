/**
 * Copyright 2018 SPeCS.
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.suikasoft.jOptions.Datakey.DataKey;

/**
 * Maps keys of a StoreDefinition to an index.
 * 
 * @author JoaoBispo
 *
 */
public class StoreDefinitionIndexes {

    private final Map<String, Integer> keysToIndexes;

    public StoreDefinitionIndexes(StoreDefinition definition) {
        this.keysToIndexes = new HashMap<>();

        List<DataKey<?>> keys = definition.getKeys();
        for (int i = 0; i < keys.size(); i++) {
            keysToIndexes.put(keys.get(i).getName(), i);
        }
    }

    public int getIndex(DataKey<?> key) {
        return getIndex(key.getName());
    }

    public int getIndex(String key) {
        Integer index = keysToIndexes.get(key);

        if (index == null) {
            throw new RuntimeException("Key '" + key + "' not present in this definition: " + keysToIndexes.keySet());
        }

        return index;
    }

    public boolean hasIndex(DataKey<?> key) {
        return hasIndex(key.getName());
    }

    public boolean hasIndex(String key) {
        return keysToIndexes.containsKey(key);
    }

}
