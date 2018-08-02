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
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import pt.up.fe.specs.util.SpecsStrings;

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

    private long cacheHits;
    private long cacheMisses;

    public CachedItems(Function<K, V> mapper) {
        this(mapper, false);
    }

    public CachedItems(Function<K, V> mapper, boolean isThreadSafe) {
        this.cache = isThreadSafe ? new ConcurrentHashMap<>() : new HashMap<>();
        this.mapper = mapper;
        cacheHits = 0;
        cacheMisses = 0;
    }

    public V get(K key) {
        // Check if map contains item
        V object = cache.get(key);

        // If object is not in map, create and store it
        if (object == null) {
            cacheMisses++;
            object = mapper.apply(key);
            cache.put(key, object);
        } else {
            cacheHits++;
        }

        return object;
    }

    public long getCacheHits() {
        return cacheHits;
    }

    public long getCacheMisses() {
        return cacheMisses;
    }

    public long getCacheTotalCalls() {
        return cacheMisses + cacheHits;
    }

    public long getCacheSize() {
        return cache.size();
    }

    public double getHitRatio() {
        return (double) cacheHits / (double) (cacheHits + cacheMisses);
    }

    public String getAnalytics() {
        StringBuilder builder = new StringBuilder();

        builder.append("Cache size: ").append(getCacheSize()).append("\n");
        builder.append("Total calls: ").append(getCacheTotalCalls()).append("\n");
        builder.append("Hit ratio: ").append(SpecsStrings.toPercentage(getHitRatio())).append("\n");

        return builder.toString();
    }
}
