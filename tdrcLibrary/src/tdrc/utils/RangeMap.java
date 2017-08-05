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
 * specific language governing permissions and limitations under the License. under the License.
 */

package tdrc.utils;

import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * see:
 * http://stackoverflow.com/questions/13399821/data-structures-that-can-map-a-
 * range-of-values-to-a-key
 * 
 * Assumes non-overlapping ranges. Deal with it!
 * 
 * @author ---
 *
 * @param <K>
 * @param <V>
 */
public class RangeMap<K extends Number, V> {

	private final TreeMap<K, V> map;

	public RangeMap() {

		map = new TreeMap<>();
	}

	public V get(K key) {

		Entry<K, V> e = getLowerEntry(key);

		return e == null ? null : e.getValue();
	}

	private Entry<K, V> getLowerEntry(K key) {

		Entry<K, V> e = map.floorEntry(key);
		if (e != null && e.getValue() == null) {
			e = map.lowerEntry(key);
		}
		return e;
	}

	public void put(K lower, K upper, V value) {

		map.put(lower, value);
		map.put(upper, null);
	}

	public void clear() {

		map.clear();
	}

	/**
	 * 
	 * @param lower
	 *            -- the lower bound of the range
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

	public int size() {

		return map.size() / 2;
	}

	int elements() {

		return map.size();
	}
}
