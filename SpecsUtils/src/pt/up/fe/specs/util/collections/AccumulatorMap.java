/*
 * Copyright 2011 SPeCS Research Group.
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

package pt.up.fe.specs.util.collections;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import pt.up.fe.specs.util.SpecsLogs;

/**
 * Keeps track of how many items were put on the table.
 *
 * @author Joao Bispo
 */
public class AccumulatorMap<T> {

    private Map<T, Integer> accMap;
    private long accumulator;
    private boolean unmodifiable;

    public AccumulatorMap() {
        this.accMap = new HashMap<>();
        this.accumulator = 0;
        this.unmodifiable = false;
    }

    public AccumulatorMap(AccumulatorMap<T> accumulatorMap) {
        this.accMap = new HashMap<>(accumulatorMap.accMap);
        this.accumulator = accumulatorMap.accumulator;
        this.unmodifiable = false;
    }

    /**
     * Returns an unmodifiable view of this map.
     *
     * @return
     */
    public AccumulatorMap<T> getUnmodifiableMap() {
        AccumulatorMap<T> unmodMap = new AccumulatorMap<>();
        unmodMap.accMap = Collections.unmodifiableMap(this.accMap);
        unmodMap.accumulator = this.accumulator;
        unmodMap.unmodifiable = true;

        return unmodMap;
    }

    /**
     * Adds 1 to the count of this element.
     *
     * @param element
     * @return the current number of added elements. If it is the first time we are adding an element, returns 1
     */
    public Integer add(T element) {
        return add(element, 1);
    }

    /**
     * Adds a value to the count of this element.
     *
     * @param element
     * @param incrementValue
     * @return
     */
    public Integer add(T element, int incrementValue) {
        if (this.unmodifiable) {
            throw new UnsupportedOperationException("Attempting to modify an unmodifiable '" + AccumulatorMap.class
                    + "'.");
        }

        Integer value = this.accMap.get(element);
        if (value == null) {
            value = 0;
        }

        // int incrementValue = 1;

        value += incrementValue;
        this.accMap.put(element, value);
        this.accumulator += incrementValue;

        return value;
    }

    public boolean remove(T element) {
        return remove(element, 1);
    }

    public boolean remove(T element, int incrementValue) {
        if (this.unmodifiable) {
            throw new UnsupportedOperationException("Attempting to modify an unmodifiable '" + AccumulatorMap.class
                    + "'.");
        }

        Integer value = this.accMap.get(element);
        if (value == null) {
            SpecsLogs.msgWarn("Map does not contain any instance of '" + element + "'");
            return false;
        }

        // int incrementValue = 1;

        value -= incrementValue;
        this.accMap.put(element, value);
        this.accumulator -= incrementValue;

        return true;
    }

    /**
     * private void updateTable(T element, Integer value) {
     *
     * }
     */

    /**
     *
     * @param element
     * @return the number of times the given element was added to the table.
     */
    public int getCount(T element) {
        Integer count = this.accMap.get(element);
        if (count == null) {
            return 0;
        }

        // return accMap.get(element);
        return count;
    }

    /**
     *
     * @param element
     * @return the number of times the given element was added to the table.
     */
    public double getRatio(T element) {
        Integer count = getCount(element);

        return (double) count / (double) this.accumulator;
    }

    /**
     * Sums all the values in this map.
     *
     * @param histogram
     * @return
     */
    // public int getSum() {
    public long getSum() {
        /*
        int accumulator = 0;
        for(T key : accMap.keySet()) {
           accumulator += accMap.get(key);
        }
        
        return accumulator;
         *
         */
        return this.accumulator;
    }

    @Override
    public String toString() {
        return this.accMap.toString();
    }

    public Map<T, Integer> getAccMap() {
        return Collections.unmodifiableMap(this.accMap);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AccumulatorMap)) {
            return false;
        }

        AccumulatorMap<?> anotherObj = ((AccumulatorMap<?>) obj);

        if (this.accumulator != anotherObj.accumulator) {
            return false;
        }

        return this.accMap.equals(anotherObj.accMap);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.accMap != null ? this.accMap.hashCode() : 0);
        hash = 47 * hash + (int) (this.accumulator ^ (this.accumulator >>> 32));
        return hash;
    }

}
