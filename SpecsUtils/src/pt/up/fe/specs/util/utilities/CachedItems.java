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
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.util.utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
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

    private final AtomicLong cacheHits;
    private final AtomicLong cacheMisses;

    public CachedItems(Function<K, V> mapper) {
        this(mapper, false);
    }

    public CachedItems(Function<K, V> mapper, boolean isThreadSafe) {
        this.mapper = Objects.requireNonNull(mapper, "Mapper function cannot be null");
        this.cache = isThreadSafe ? new ConcurrentHashMap<>() : new HashMap<>();
        this.cacheHits = new AtomicLong(0);
        this.cacheMisses = new AtomicLong(0);
    }

    public V get(K key) {
        // For thread-safe caches, use computeIfAbsent to eliminate race conditions
        if (cache instanceof ConcurrentHashMap) {
            // Use AtomicBoolean to track if this was a cache miss
            AtomicBoolean wasCacheMiss = new AtomicBoolean(false);

            V result = cache.computeIfAbsent(key, k -> {
                wasCacheMiss.set(true);
                cacheMisses.incrementAndGet();
                return mapper.apply(k);
            });

            // If computeIfAbsent didn't call the function, it was a cache hit
            if (!wasCacheMiss.get()) {
                cacheHits.incrementAndGet();
            }

            return result;
        } else {
            // For non-thread-safe caches, use the original logic
            V object = cache.get(key);

            if (object == null) {
                cacheMisses.incrementAndGet();
                object = mapper.apply(key);
                cache.put(key, object);
            } else {
                cacheHits.incrementAndGet();
            }

            return object;
        }
    }

    public long getCacheHits() {
        return cacheHits.get();
    }

    public long getCacheMisses() {
        return cacheMisses.get();
    }

    public long getCacheTotalCalls() {
        return cacheMisses.get() + cacheHits.get();
    }

    public long getCacheSize() {
        return cache.size();
    }

    public double getHitRatio() {
        long hits = cacheHits.get();
        long misses = cacheMisses.get();
        return (double) hits / (double) (hits + misses);
    }

    public String getAnalytics() {

        String builder = "Cache size: " + getCacheSize() + "\n" +
                "Total calls: " + getCacheTotalCalls() + "\n" +
                "Hit ratio: " + SpecsStrings.toPercentage(getHitRatio()) + "\n";

        return builder;
    }
}
