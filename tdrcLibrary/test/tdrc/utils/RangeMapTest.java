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

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
 * 
 * @author Generated Tests
 */
@DisplayName("RangeMap Tests")
public class RangeMapTest {

	RangeMap<Double, String> map;

	@BeforeEach
	void setUp() {
		map = new RangeMap<>();
	}

	@Test
	@DisplayName("Should put ranges correctly")
	public void testPut() {

		assertThat(map.size()).isZero();

		map.put(1.0, 2.9, "A");
		assertThat(map.size()).isEqualTo(1);

		map.put(4.0, 6.0, "B");
		assertThat(map.size()).isEqualTo(2);

		map.put(6.5, 10.0, "C");
		assertThat(map.size()).isEqualTo(3);
	}

	@Test
	@DisplayName("Should get values from ranges correctly")
	public void testGet() {

		map.put(1.0, 2.9, "A");
		map.put(4.0, 6.0, "B");
		map.put(6.5, 10.0, "C");

		assertThat(map.get(0.9)).isNull();
		assertThat(map.get(1.0)).isEqualTo("A");
		assertThat(map.get(2.0)).isEqualTo("A");
		assertThat(map.get(2.9)).isEqualTo("A");
		assertThat(map.get(3.0)).isNull();

		assertThat(map.get(10.1)).isNull();
	}

	@Test
	@DisplayName("Should remove ranges correctly")
	public void testRemove() {

		map.put(1.0, 2.9, "A");
		map.put(4.0, 6.0, "B");
		map.put(6.5, 10.0, "C");

		assertThat(map.size()).isEqualTo(3);
		assertThat(map.elements()).isEqualTo(6);
		assertThat(map.get(2.0)).isEqualTo("A");

		assertThat(map.remove(1.0)).isEqualTo("A");
		assertThat(map.size()).isEqualTo(2);
		assertThat(map.elements()).isEqualTo(4);
		assertThat(map.get(2.0)).isNull();

		/* this is not a range -- above all others */
		assertThat(map.remove(15.0)).isNull();
		assertThat(map.size()).isEqualTo(2);
		assertThat(map.elements()).isEqualTo(4);

		/* this is not a range -- bellow all others */
		assertThat(map.remove(-1.0)).isNull();
		assertThat(map.size()).isEqualTo(2);
		assertThat(map.elements()).isEqualTo(4);

		/* this is not a range -- between two ranges */
		assertThat(map.remove(6.2)).isNull();
		assertThat(map.size()).isEqualTo(2);
		assertThat(map.elements()).isEqualTo(4);
	}

	@Test
	@DisplayName("Should clear all ranges")
	public void testClear() {

		map.put(1.0, 2.9, "A");
		map.put(4.0, 6.0, "B");
		map.put(6.5, 10.0, "C");
		assertThat(map.size()).isEqualTo(3);

		map.clear();
		assertThat(map.size()).isZero();
	}

	@Test
	@DisplayName("Should handle null values in ranges")
	public void testNullValues() {
		map.put(10.0, 20.0, null);
		map.put(30.0, 40.0, "B");

		assertThat(map.get(15.0)).isNull();
		assertThat(map.get(35.0)).isEqualTo("B");
	}

	@Test
	@DisplayName("Should handle range boundaries correctly")
	public void testRangeBoundaries() {
		map.put(10.0, 20.0, "range");

		// Within range
		assertThat(map.get(10.0)).isEqualTo("range");
		assertThat(map.get(15.0)).isEqualTo("range");

		// At upper bound - based on original test comments, ranges are inclusive
		assertThat(map.get(20.0)).isEqualTo("range");

		// Outside range
		assertThat(map.get(9.9)).isNull();
		assertThat(map.get(20.1)).isNull();
	}

	@Test
	@DisplayName("Should handle adjacent ranges")
	public void testAdjacentRanges() {
		map.put(10.0, 20.0, "range1");
		map.put(20.0, 30.0, "range2");

		assertThat(map.get(19.9)).isEqualTo("range1");
		assertThat(map.get(20.0)).isEqualTo("range2"); // Changed: overlapping bound goes to second range
		assertThat(map.get(25.0)).isEqualTo("range2");
		assertThat(map.get(30.0)).isEqualTo("range2"); // Changed: ranges are inclusive
		assertThat(map.get(30.1)).isNull();
	}

	@Test
	@DisplayName("Should handle negative ranges")
	public void testNegativeRanges() {
		map.put(-20.0, -10.0, "negative");
		map.put(-5.0, 5.0, "mixed");

		assertThat(map.get(-15.0)).isEqualTo("negative");
		assertThat(map.get(0.0)).isEqualTo("mixed");
		assertThat(map.get(-25.0)).isNull();
	}

	@Test
	@DisplayName("Should handle single point ranges")
	public void testSinglePointRanges() {
		map.put(10.0, 10.1, "point");

		assertThat(map.get(10.0)).isEqualTo("point");
		assertThat(map.get(10.05)).isEqualTo("point");
		assertThat(map.get(10.1)).isEqualTo("point"); // Changed: ranges are inclusive
		assertThat(map.get(10.2)).isNull();
	}

	@Test
	@DisplayName("Should handle overwriting ranges")
	public void testOverwriteRange() {
		map.put(10.0, 20.0, "original");
		assertThat(map.get(15.0)).isEqualTo("original");

		map.put(10.0, 20.0, "overwritten");
		assertThat(map.get(15.0)).isEqualTo("overwritten");
		assertThat(map.size()).isEqualTo(1);
	}

	@Test
	@DisplayName("Should handle null key lookups gracefully - returns null")
	public void testNullKeyLookups() {
		map.put(10.0, 20.0, "range");

		// RangeMap now handles null keys gracefully (bug fix)
		String result = map.get(null);
		assertThat(result).isNull();
	}

	@Test
	@DisplayName("Should track elements count correctly")
	public void testElementsCount() {
		assertThat(map.elements()).isZero();

		map.put(10.0, 20.0, "range1");
		assertThat(map.elements()).isEqualTo(2); // lower bound + upper marker

		map.put(30.0, 40.0, "range2");
		assertThat(map.elements()).isEqualTo(4);

		map.clear();
		assertThat(map.elements()).isZero();
	}
}
