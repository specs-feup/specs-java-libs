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
 * 
 * @author Tiago
 * 
 * @param <T>
 */
public class HashBag<T> {

    private final Map<T, Integer> bag;

    public HashBag() {

        this.bag = new HashMap<>();
    }

    /**
     * Puts one occurrence of element T and returns the total number of occurrences
     * 
     * @param element
     *            the element to add an occurrence
     * @return total number of occurrences of the element
     */
    public int put(T element) {
        return put(element, 1);
    }

    /**
     * Puts a number of occurrences of element T and returns the total number of occurrences
     * 
     * @param element
     *            the element to add occurrences
     * @param val
     *            the number of occurrences to set
     * @return total number of occurrences of the element
     */
    public int put(T element, int val) {
        final int newVal = get(element) + val;
        this.bag.put(element, newVal);
        return newVal;
    }

    /**
     * Get the number of occurrences of element T
     * 
     * @param Element
     * @return
     */
    public int get(T element) {
        final Integer occur = this.bag.get(element);
        if (occur == null) {
            return 0;
        }
        return occur;
    }

    /**
     * Takes one occurrence of element T and returns the remaining number of occurrences
     * 
     * @param element
     *            the element to add an occurrence
     * @return remaining number of occurrences of the element
     */
    public int take(T element) {
        return take(element, 1);
    }

    /**
     * Takes a number of occurrences of element T and returns the remaining number of occurrences
     * 
     * @param element
     *            the element to add an occurrence
     * @param val
     *            the number of occurrences to take;
     * @return remaining number of occurrences of the element
     */
    public int take(T element, int val) {
        final int newVal = get(element) - val;
        if (newVal == 0) {
            this.bag.remove(element);
        } else if (newVal < 0) {
            return 0;
        } else {
            this.bag.put(element, newVal);
        }
        return newVal;
    }

    /**
     * Return a set containing the keys on this bag
     * 
     * @return
     */
    public Set<T> keySet() {
        return this.bag.keySet();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        this.bag.keySet().stream().forEach(k -> sb.append(k + "=" + this.bag.get(k) + ", "));

        sb.append("}");
        return sb.toString();
    }

    /**
     * Remove all itens from the bag <br>
     * <b>NOTE:</b> actual removal of items, if you intend to reset contents use {@link HashBag#reset()}
     */
    public void clear() {
        bag.clear();
    }

    /**
     * Resets the items counters to zero <br>
     * <b>NOTE:</b> does not remove the items, just resets to zeros, if you intend to remove the items use
     * {@link HashBag#clear()}
     */
    public void reset() {
        bag.replaceAll((k, v) -> 0);
    }

    /**
     * Returns the total number of itens in the bag
     * 
     * @return
     */
    public int getTotal() {
        return bag.values().stream().reduce((a, b) -> a + b).orElse(0);
    }

    public Map<T, Integer> toMap() {

        return Collections.unmodifiableMap(bag);
    }
}
