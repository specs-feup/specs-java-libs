/**
 * Copyright 2013 SPeCS Research Group.
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

import java.util.ArrayList;

public class PairList<K, V> extends ArrayList<Pair<K, V>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 327775886389736L;

	/**
	 * Create and add a new Pair to the list
	 * 
	 * @param left
	 * @param right
	 * @return the new pair
	 */
	public Pair<K, V> add(K left, V right) {
		final Pair<K, V> pair = new Pair<>(left, right);
		if (super.add(pair)) {
			return pair;
		}
		return null;
	}

	/**
	 * Get the last Pair in the list
	 * 
	 * @return
	 * @throws IndexOutOfBoundsException
	 *             if the list is empty
	 */
	public Pair<K, V> last() {
		if (isEmpty()) {
			throw new IndexOutOfBoundsException("The list of pairs is empty");
		}

		return get(size() - 1);
	}

	/**
	 * Get the first Pair in the list
	 * 
	 * @return
	 * @throws IndexOutOfBoundsException
	 *             if the list is empty
	 */
	public Pair<K, V> first() {
		if (isEmpty()) {
			throw new IndexOutOfBoundsException("The list of pairs is empty");
		}

		return get(0);
	}
}
