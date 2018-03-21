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

package pt.up.fe.specs.util.utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Caches items that can be built with a mapper function.
 * 
 * @author JoaoBispo
 *
 * @param <K>
 * @param <V>
 */
public class CachedItems<K, V> {

    private final Map<K, V> cache;
    private final Function<K, V> mapper;

    public CachedItems(Function<K, V> mapper) {
        this.cache = new HashMap<>();
        this.mapper = mapper;
    }

    public V get(K key) {
        // Check if map contains item
        V object = cache.get(key);

        // If object is not in map, create and store it
        if (object == null) {
            object = mapper.apply(key);
            cache.put(key, object);
        }

        return object;
    }

}
