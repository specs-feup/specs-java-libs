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
 * Utility class for range operations in tdrcLibrary.
 */
public class RangeUtils {

	/**
	 * Retrieves the value associated with the closest key less than or equal to the given key in the provided TreeMap.
	 * If the closest key's value is null, it continues searching for the next lower key.
	 * 
	 * Example of usage: 
	 * TreeMap<Double, Character> m = new TreeMap<Double, Character>();
	 * m.put(1.0, 'A');
	 * m.put(2.9, null);
	 * m.put(4.0, 'B');
	 * m.put(6.0, null);
	 * m.put(6.5, 'C');
	 * m.put(10.0, null);
	 * getValueByRangedKey(m, 5) == 'B'
	 * 
	 * @param map the TreeMap containing the key-value pairs
	 * @param key the key to search for
	 * @return the value associated with the closest key less than or equal to the given key, or null if no such key exists
	 */
	public static <K, V> V getValueByRangedKey(TreeMap<K, V> map, K key) {
		Entry<K, V> e = map.floorEntry(key);
		if (e != null && e.getValue() == null) {
			e = map.lowerEntry(key);
		}
		return e == null ? null : e.getValue();
	}
}
