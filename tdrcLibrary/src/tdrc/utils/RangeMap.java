/**
 * Copyright 2015 SPeCS.
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

package tdrc.utils;

import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Represents a map of ranges to values.
 * 
 * Assumes non-overlapping ranges.
 * 
 * @author ---
 *
 * @param <K> the type of the range bounds, must extend Number
 * @param <V> the type of the values associated with ranges
 */
public class RangeMap<K extends Number, V> {

    private final TreeMap<K, V> map;

    /**
     * Constructs an empty RangeMap.
     */
    public RangeMap() {
        map = new TreeMap<>();
    }

    /**
     * Retrieves the value associated with the range containing the given key.
     * 
     * @param key the key to search for
     * @return the value associated with the range containing the key, or null if no such range exists
     */
    public V get(K key) {
        if (key == null) {
            return null;
        }
        Entry<K, V> e = getLowerEntry(key);
        return e == null ? null : e.getValue();
    }

    /**
     * Retrieves the entry with the largest key less than or equal to the given key.
     * 
     * @param key the key to search for
     * @return the entry with the largest key less than or equal to the given key, or null if no such entry exists
     */
    private Entry<K, V> getLowerEntry(K key) {
        if (key == null) {
            return null;
        }
        Entry<K, V> e = map.floorEntry(key);
        if (e != null && e.getValue() == null) {
            e = map.lowerEntry(key);
        }
        return e;
    }

    /**
     * Adds a range to the map.
     * 
     * @param lower the lower bound of the range
     * @param upper the upper bound of the range
     * @param value the value to associate with the range
     */
    public void put(K lower, K upper, V value) {
        map.put(lower, value);
        map.put(upper, null);
    }

    /**
     * Removes all ranges from the map.
     */
    public void clear() {
        map.clear();
    }

    /**
     * Removes the range starting at the given lower bound.
     * 
     * @param lower the lower bound of the range to remove
     * @return the value associated with the removed range, or null if no such range exists
     */
    public V remove(K lower) {
        Entry<K, V> lowerEntry = getLowerEntry(lower);

        if (lowerEntry == null || lowerEntry.getValue() == null) {
            return null;
        }

        Entry<K, V> upperEntry = map.higherEntry(lower);

        if (upperEntry == null) {
            return null;
        }

        map.remove(upperEntry.getKey());
        return map.remove(lowerEntry.getKey());
    }

    /**
     * Returns the number of ranges in the map.
     * 
     * @return the number of ranges in the map
     */
    public int size() {
        return map.size() / 2;
    }

    /**
     * Returns the total number of elements in the map.
     * 
     * @return the total number of elements in the map
     */
    int elements() {
        return map.size();
    }
}
