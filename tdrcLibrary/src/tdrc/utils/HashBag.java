/**
 * Copyright 2014 SPeCS Research Group.
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A bag (multiset) implementation backed by a hash map.
 * 
 * @author Tiago
 * 
 * @param <T> the type of elements in the bag
 */
public class HashBag<T> {

    private final Map<T, Integer> bag;

    /**
     * Constructs an empty HashBag.
     */
    public HashBag() {
        this.bag = new HashMap<>();
    }

    /**
     * Adds one occurrence of the specified element to the bag and returns the total number of occurrences.
     * 
     * @param element the element to add
     * @return the total number of occurrences of the element
     */
    public int put(T element) {
        return put(element, 1);
    }

    /**
     * Adds the specified number of occurrences of the element to the bag and returns the total number of occurrences.
     * 
     * @param element the element to add
     * @param val the number of occurrences to add
     * @return the total number of occurrences of the element
     */
    public int put(T element, int val) {
        final int newVal = get(element) + val;
        this.bag.put(element, newVal);
        return newVal;
    }

    /**
     * Returns the number of occurrences of the specified element in the bag.
     * 
     * @param element the element to query
     * @return the number of occurrences of the element
     */
    public int get(T element) {
        final Integer occur = this.bag.get(element);
        if (occur == null) {
            return 0;
        }
        return occur;
    }

    /**
     * Removes one occurrence of the specified element from the bag and returns the remaining number of occurrences.
     * 
     * @param element the element to remove
     * @return the remaining number of occurrences of the element
     */
    public int take(T element) {
        return take(element, 1);
    }

    /**
     * Removes the specified number of occurrences of the element from the bag and returns the remaining number of occurrences.
     * 
     * @param element the element to remove
     * @param val the number of occurrences to remove
     * @return the remaining number of occurrences of the element
     */
    public int take(T element, int val) {
        final int newVal = get(element) - val;
        if (newVal <= 0) {
            this.bag.remove(element);
            return 0;
        } else {
            this.bag.put(element, newVal);
            return newVal;
        }
    }

    /**
     * Returns a set containing all the keys (elements) in the bag.
     * 
     * @return a set of keys in the bag
     */
    public Set<T> keySet() {
        return this.bag.keySet();
    }

    /**
     * Returns a string representation of the bag.
     * 
     * @return a string representation of the bag
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        this.bag.keySet().stream().forEach(k -> sb.append(k + "=" + this.bag.get(k) + ", "));
        sb.append("}");
        return sb.toString();
    }

    /**
     * Removes all items from the bag.
     * <br>
     * <b>NOTE:</b> This method performs actual removal of items. If you intend to reset contents, use {@link HashBag#reset()}.
     */
    public void clear() {
        bag.clear();
    }

    /**
     * Resets the item counters to zero without removing the items.
     * <br>
     * <b>NOTE:</b> This method does not remove the items, just resets their counts to zero. If you intend to remove the items, use {@link HashBag#clear()}.
     */
    public void reset() {
        bag.replaceAll((k, v) -> 0);
    }

    /**
     * Returns the total number of items in the bag.
     * 
     * @return the total number of items in the bag
     */
    public int getTotal() {
        return bag.values().stream().reduce((a, b) -> a + b).orElse(0);
    }

    /**
     * Returns an unmodifiable view of the bag as a map.
     * 
     * @return an unmodifiable map view of the bag
     */
    public Map<T, Integer> toMap() {
        return Collections.unmodifiableMap(bag);
    }
}
