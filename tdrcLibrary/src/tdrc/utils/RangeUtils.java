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

public class RangeUtils {

	/**
	 * Example of usage: <br>
	 * TreeMap<Double, Character> m = new TreeMap<Double, Character>();<br>
	 * m.put(1.0, 'A');<br>
	 * m.put(2.9, null);<br>
	 * m.put(4.0, 'B');<br>
	 * m.put(6.0, null);<br>
	 * m.put(6.5, 'C');<br>
	 * m.put(10.0, null);<br>
	 * getValueByRangedKey(m, 5) == 'B'<br>
	 * <br>
	 * More results: <br>
	 * 0.9 null<br>
	 * 1.0 A<br>
	 * 1.1 A<br>
	 * 2.8 A<br>
	 * 2.9 A<br>
	 * 3.0 null<br>
	 * 6.4 null<br>
	 * 6.5 C<br>
	 * 6.6 C<br>
	 * 9.9 C<br>
	 * 10.0 C<br>
	 * 10.1 null<br>
	 * 
	 * @param map
	 * @param key
	 * @return
	 */
	public static <K, V> V getValueByRangedKey(TreeMap<K, V> map, K key) {
		Entry<K, V> e = map.floorEntry(key);
		if (e != null && e.getValue() == null) {
			e = map.lowerEntry(key);
		}
		return e == null ? null : e.getValue();
	}
}
