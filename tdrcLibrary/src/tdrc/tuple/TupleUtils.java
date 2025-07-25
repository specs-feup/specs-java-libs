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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import tdrc.utils.Pair;

/**
 * Utility class for operations on Tuple objects.
 */
public class TupleUtils {

    /**
     * Creates a normalized map from a collection of tuples. Each tuple is normalized based on the maximum and minimum
     * values of its elements.
     * 
     * @param <T>       the type of number in the tuple
     * @param tuples    the collection of tuples to normalize
     * @param tupleSize the expected size of each tuple
     * @return a map where the keys are the original tuples and the values are the normalized tuples
     */
    public static <T extends Number> Map<Tuple<T>, Tuple<Float>> createNormalizedMap(Collection<Tuple<T>> tuples,
            int tupleSize) {
        List<Float> maxs = new ArrayList<>(tupleSize);
        List<Float> mins = new ArrayList<>(tupleSize);
        Iterator<Tuple<T>> iterator = tuples.iterator();
        HashMap<Tuple<T>, Tuple<Float>> normalizedMap = new HashMap<>();
        if (!iterator.hasNext()) {
            return normalizedMap;
        }
        Tuple<T> tuple = iterator.next();
        if (tuple.size() != tupleSize) {
            throw new RuntimeException("Unexpected tuple size: given " + tuple.size() + ", expected " + tupleSize);
        }
        // Get max and min values for each element of tuples
        for (T number : tuple) {
            maxs.add(number.floatValue());
            mins.add(number.floatValue());
        }
        while (iterator.hasNext()) {
            tuple = iterator.next();
            if (tuple.size() != tupleSize) {
                throw new RuntimeException("Unexpected tuple size: given " + tuple.size() + ", expected " + tupleSize);
            }
            for (int i = 0; i < tuple.size(); i++) {
                float value = tuple.get(i).floatValue();
                if (value > maxs.get(i)) {
                    maxs.set(i, value);
                }
                if (value < mins.get(i)) {
                    mins.set(i, value);
                }
            }
        }

        // Create normalized table
        for (Tuple<T> tpl : tuples) {
            Tuple<Float> normalizedTuple = Tuple.newInstance();
            for (int i = 0; i < tpl.size(); i++) {

                float normalizedValue = (tpl.get(i).floatValue() - mins.get(i)) / (maxs.get(i) - mins.get(i));
                normalizedTuple.add(normalizedValue);
            }
            normalizedMap.put(tpl, normalizedTuple);
        }

        return normalizedMap;
    }

    /**
     * Calculates the Euclidean distances between all pairs of tuples in the given collection.
     * 
     * @param tuples the collection of tuples
     * @return a map where the keys are tuples and the values are maps of tuples to their Euclidean distances
     */
    public static Map<Tuple<Float>, Map<Tuple<Float>, Float>> eucledianDistances(Collection<Tuple<Float>> tuples) {

        Map<Tuple<Float>, Map<Tuple<Float>, Float>> eucledianMap = new HashMap<>();
        for (Tuple<Float> tuple : tuples) {
            Map<Tuple<Float>, Float> distances = new HashMap<>();
            for (Tuple<Float> tuple2 : tuples) {
                float distance = (float) getDistance(tuple, tuple2);
                distances.put(tuple2, distance);
            }
            eucledianMap.put(tuple, distances);
        }
        return eucledianMap;
    }

    /**
     * Calculates the Euclidean distance between two tuples.
     * 
     * @param tuple  the first tuple
     * @param tuple2 the second tuple
     * @return the Euclidean distance between the two tuples
     */
    public static double getDistance(Tuple<? extends Number> tuple, Tuple<? extends Number> tuple2) {
        double sum = 0;
        for (int i = 0; i < tuple.size(); i++) {
            sum += Math.pow(tuple.get(i).doubleValue() - tuple2.get(i).doubleValue(), 2);
        }
        double distance = Math.sqrt(sum);
        return distance;
    }

    /**
     * Calculates the Euclidean distances between all pairs of tuples in the given collection, sorted by closest
     * distance.
     * 
     * @param tuples the collection of tuples
     * @return a map where the keys are tuples and the values are lists of pairs of tuples and their distances, sorted by
     *         distance
     */
    public static Map<Tuple<Float>, List<Pair<Tuple<Float>, Float>>> eucledianDistancesByClosest(
            Collection<Tuple<Float>> tuples) {

        Map<Tuple<Float>, List<Pair<Tuple<Float>, Float>>> eucledianMap = new HashMap<>();
        for (Tuple<Float> tuple : tuples) {
            List<Pair<Tuple<Float>, Float>> distances = new ArrayList<>();
            for (Tuple<Float> tuple2 : tuples) {
                if (tuple.equals(tuple2)) {
                    continue;
                }
                double sum = 0;
                for (int i = 0; i < tuple.size(); i++) {
                    sum += Math.pow(tuple.get(i) - tuple2.get(i), 2);
                }
                distances.add(new Pair<>(tuple2, (float) Math.sqrt(sum)));
            }
            Collections.sort(distances, (t1, t2) -> t1.right().compareTo(t2.right()));
            eucledianMap.put(tuple, distances);
        }
        return eucledianMap;
    }
}
