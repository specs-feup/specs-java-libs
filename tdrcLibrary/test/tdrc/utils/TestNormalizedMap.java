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
 * specific language governing permissions and limitations under the License.
 */

package tdrc.utils;

import java.util.Map;

import tdrc.tuple.Tuple;
import tdrc.tuple.TupleList;
import tdrc.tuple.TupleUtils;

public class TestNormalizedMap {

    public static void main(String[] args) {
        Integer[] tile = { 32, 64, 128, 256, 512 };
        Integer[] unroll = { 0, 2, 4, 6, 8, 10, -1 };
        TupleList<Integer> tuples = ListUtils.createTuples(tile, unroll);
        Map<Tuple<Integer>, Tuple<Float>> createNormalizedMap = TupleUtils.createNormalizedMap(tuples, 2);
        System.out.println(createNormalizedMap
                .entrySet().stream()
                .map(entry -> entry.getKey().toString() + "=" + entry.getValue().toString())
                .sorted()
                .collect(java.util.stream.Collectors.joining("\n")));

        Map<Tuple<Float>, Map<Tuple<Float>, Float>> eucledianMap = TupleUtils
                .eucledianDistances(createNormalizedMap.values());
        System.out.println("--------------------");
        System.out.println(eucledianMap
                .entrySet().stream()
                .map(entry -> entry.getKey().toString() + "=" + entry.getValue().toString())
                .sorted()
                .collect(java.util.stream.Collectors.joining("\n")));
    }
}
