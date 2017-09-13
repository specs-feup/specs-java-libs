/**
 * Copyright 2017 SPeCS.
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

package tdrc.tuple;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import org.apache.commons.lang3.builder.CompareToBuilder;

public class Tuple<T> extends AbstractList<T> implements Comparable<Tuple<T>> {

    List<T> tuple;
    BiFunction<Tuple<T>, Tuple<T>, Integer> comparator;
    private BiFunction<Tuple<T>, Tuple<T>, Double> distanceCalculator;

    public static <T> Tuple<T> newInstance() {
        return new Tuple<>();
    }

    public static <T> Tuple<T> newInstance(List<T> elements) {
        return new Tuple<>(elements);
    }

    @SafeVarargs
    public static <T> Tuple<T> newInstance(T... elements) {
        return new Tuple<>(Arrays.asList(elements));
    }

    private Tuple(List<T> elements) {
        tuple = new ArrayList<>(elements);
        this.comparator = Tuple::defaultComparator;
        this.distanceCalculator = Tuple::defaultDistanceCalculator;
    }

    private Tuple() {
        tuple = new ArrayList<>();
    }

    @Override
    public T get(int index) {
        return tuple.get(index);
    }

    @Override
    public int size() {

        return tuple.size();
    }

    @Override
    public T set(int index, T element) {
        return tuple.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        tuple.add(index, element);
    }

    @Override
    public boolean remove(Object o) {
        return tuple.remove(o);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Tuple)) {
            return false;
        }
        Tuple<?> tuple = (Tuple<?>) o;
        int size = tuple.size();
        if (size != this.tuple.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!tuple.get(i).equals(this.tuple.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int compareTo(Tuple<T> o) {
        return comparator.apply(this, o);
    }

    /*@SuppressWarnings({ "rawtypes", "unchecked" })
    public int compareTo(final Tuple o) {
        
        final int tLen = this.valueArray.length;
        final Object[] oValues = o.valueArray;
        final int oLen = oValues.length;
        
        for (int i = 0; i < tLen && i < oLen; i++) {
            
            final Comparable tElement = (Comparable)this.valueArray[i];
            final Comparable oElement = (Comparable)oValues[i];
            
            final int comparison = tElement.compareTo(oElement);
            if (comparison != 0) {
                return comparison;
            }
            
        }
        
        return (Integer.valueOf(tLen)).compareTo(Integer.valueOf(oLen));
        
    }*/
    public static <T> Integer defaultComparator(Tuple<T> tuple, Tuple<T> tuple2) {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        final int tLen = tuple.size();
        final int oLen = tuple2.size();
        for (int i = 0; i < tLen && i < oLen; i++) {
            compareToBuilder.append(tuple.get(i), tuple2.get(i));
        }

        return compareToBuilder.toComparison();

        // final int tLen = tuple.size();
        // final int oLen = tuple2.size();
        //
        // for (int i = 0; i < tLen && i < oLen; i++) {
        //
        // final Comparable<T> tElement = (Comparable) tuple.get(i);
        // // final Comparable<T> oElement = (Comparable) tuple2.get(i);
        //
        // final int comparison = tElement.compareTo(tuple2.get(i));
        // if (comparison != 0) {
        // return comparison;
        // }
        // }
        // return tLen - oLen;
    }

    public double getDistance(Tuple<T> otherTuple) {
        return distanceCalculator.apply(this, otherTuple);
    }

    public static <T> Double defaultDistanceCalculator(Tuple<T> tuple, Tuple<T> tuple2) {

        final int tLen = tuple.size();
        final int oLen = tuple2.size();
        double sum = 0;
        for (int i = 0; i < tLen && i < oLen; i++) {
            T t = tuple.get(i);
            T t2 = tuple2.get(i);
            if (!(t instanceof Number && t2 instanceof Number)) {
                throw new RuntimeException("Cannot calculate distance if the elements of the tuple are not numbers");
            }
            double tD = ((Number) t).doubleValue() - ((Number) t2).doubleValue();
            sum += tD * tD;
        }

        return Math.sqrt(sum);

        // final int tLen = tuple.size();
        // final int oLen = tuple2.size();
        //
        // for (int i = 0; i < tLen && i < oLen; i++) {
        //
        // final Comparable<T> tElement = (Comparable) tuple.get(i);
        // // final Comparable<T> oElement = (Comparable) tuple2.get(i);
        //
        // final int comparison = tElement.compareTo(tuple2.get(i));
        // if (comparison != 0) {
        // return comparison;
        // }
        // }
        // return tLen - oLen;
    }

    public BiFunction<Tuple<T>, Tuple<T>, Double> getDistanceCalculator() {
        return distanceCalculator;
    }

    public void setDistanceCalculator(BiFunction<Tuple<T>, Tuple<T>, Double> distanceCalculator) {
        this.distanceCalculator = distanceCalculator;
    }
}
