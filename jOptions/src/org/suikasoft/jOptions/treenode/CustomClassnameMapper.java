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

package org.suikasoft.jOptions.treenode;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.suikasoft.jOptions.Interfaces.DataStore;

public class CustomClassnameMapper<T> {

    private final Map<String, Function<DataStore, Class<? extends T>>> customMaps;

    private final DataStore data;

    public CustomClassnameMapper(DataStore data) {
        this.data = data;
        this.customMaps = new HashMap<>();
    }

    public CustomClassnameMapper() {
        this(DataStore.newInstance("Empty CustomClassMapper DataStore"));
    }

    public Class<? extends T> getClass(String classname) {
        if (!customMaps.containsKey(classname)) {
            return null;
        }

        return customMaps.get(classname).apply(data);
    }

    public CustomClassnameMapper<T> add(String classname, Function<DataStore, Class<? extends T>> mapper) {

        customMaps.put(classname, mapper);

        return this;
    }

}
