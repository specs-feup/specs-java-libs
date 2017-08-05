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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
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
 */

public class RangeMapTest {

	RangeMap<Double, String> map;

	@Test
	public void testPut() {

		map = new RangeMap<>();
		assertEquals(0, map.size());

		map.put(1.0, 2.9, "A");
		assertEquals(1, map.size());

		map.put(4.0, 6.0, "B");
		assertEquals(2, map.size());

		map.put(6.5, 10.0, "C");
		assertEquals(3, map.size());
	}

	@Test
	public void testGet() {

		testPut();

		assertEquals(null, map.get(0.9));
		assertEquals("A", map.get(1.0));
		assertEquals("A", map.get(2.0));
		assertEquals("A", map.get(2.9));
		assertEquals(null, map.get(3.0));

		assertEquals(null, map.get(10.1));
	}

	@Test
	public void testRemove() {

		testPut();
		assertEquals(3, map.size());
		assertEquals(6, map.elements());
		assertEquals("A", map.get(2.0));

		assertEquals("A", map.remove(1.0));
		assertEquals(2, map.size());
		assertEquals(4, map.elements());
		assertEquals(null, map.get(2.0));

		/* this is not a range -- above all others */
		assertEquals(null, map.remove(15.0));
		assertEquals(2, map.size());
		assertEquals(4, map.elements());

		/* this is not a range -- bellow all others */
		assertEquals(null, map.remove(-1.0));
		assertEquals(2, map.size());
		assertEquals(4, map.elements());

		/* this is not a range -- between two ranges */
		assertEquals(null, map.remove(6.2));
		assertEquals(2, map.size());
		assertEquals(4, map.elements());
	}

	@Test
	public void testClear() {

		testPut();
		assertEquals(3, map.size());

		map.clear();
		assertEquals(0, map.size());
	}
}
