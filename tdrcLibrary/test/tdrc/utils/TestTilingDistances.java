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

package tdrc.utils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import pt.up.fe.specs.util.SpecsIo;
import tdrc.tuple.Tuple;
import tdrc.tuple.TupleList;
import tdrc.tuple.TupleUtils;

public class TestTilingDistances {

    public static void main(String[] args) {
        tuplesDistancesByClosest();
        // IoUtils.append(out, eucledianDistances.toString());
        // System.out.println(eucledianDistances);
    }

    private static void tuplesDistances() {
        File out = new File("out.txt");
        Integer[] values = { 8, 16, 32, 64, 128, 256, 512, 1024, 2048 };
        TupleList<Integer> tupleList = ListUtils.createTuples(values, values, values);
        SpecsIo.write(out, tupleList.toString());
        Map<Tuple<Integer>, Tuple<Float>> normMap = TupleUtils.createNormalizedMap(tupleList, 3);
        Map<Tuple<Float>, Tuple<Integer>> invertedNormMap = new HashMap<>();
        normMap.entrySet().forEach(entry -> invertedNormMap.put(entry.getValue(), entry.getKey()));
        System.out.println("-------------------------");
        SpecsIo.append(out, "\n");
        SpecsIo.append(out, normMap.toString());
        SpecsIo.append(out, "\n");
        System.out.println("-------------------------");
        Map<Tuple<Float>, Map<Tuple<Float>, Float>> eucledianDistances = TupleUtils
                .eucledianDistances(normMap.values());
        Optional<Entry<Tuple<Float>, Map<Tuple<Float>, Float>>> first = eucledianDistances.entrySet().stream()
                .findFirst();
        List<Tuple<Integer>> rows = first.get().getValue().keySet().stream().map(invertedNormMap::get)
                .collect(Collectors.toList());
        Function<Tuple<Integer>, String> tuple2String = tuple -> "("
                + StringUtils.join(tuple, val -> String.format("%1$04d", val), ";") + ")";
        String separator = " ,";
        SpecsIo.append(out, "\n\n------- EUCLEDIAN DISTANCES --------\n");
        SpecsIo.append(out, separator + StringUtils.join(rows, tuple2String, separator));
        for (Entry<Tuple<Float>, Map<Tuple<Float>, Float>> entry : eucledianDistances.entrySet()) {
            SpecsIo.append(out,
                    "\n" + tuple2String.apply(invertedNormMap.get(entry.getKey())) + separator
                            + StringUtils.join(entry.getValue().values(), separator));
        }
    }

    private static void tuplesDistancesByClosest() {
        File out = new File("out.txt");
        Integer[] values = { 8, 16, 32, 64, 128, 256, 512, 1024, 2048 };
        TupleList<Integer> tupleList = ListUtils.createTuples(values, values, values);
        SpecsIo.write(out, "");
        // IoUtils.write(out, tupleList.toString());
        Map<Tuple<Integer>, Tuple<Float>> normMap = TupleUtils.createNormalizedMap(tupleList, 3);
        Map<Tuple<Float>, Tuple<Integer>> invertedNormMap = new HashMap<>();
        normMap.entrySet().forEach(entry -> invertedNormMap.put(entry.getValue(), entry.getKey()));
        // System.out.println("-------------------------");
        // IoUtils.append(out, "\n");
        // IoUtils.append(out, normMap.toString());
        // IoUtils.append(out, "\n");
        // System.out.println("-------------------------");
        Map<Tuple<Float>, List<Pair<Tuple<Float>, Float>>> eucledianDistances = TupleUtils
                .eucledianDistancesByClosest(normMap.values());

        Function<Tuple<Integer>, String> tuple2String = tuple -> "("
                + StringUtils.join(tuple, val -> String.format("%1$04d", val), ";") + ")";
        String separator = " ,";
        SpecsIo.append(out, "\n\n------- EUCLEDIAN DISTANCES --------\n");
        StringBuilder builder = new StringBuilder();
        for (Entry<Tuple<Float>, List<Pair<Tuple<Float>, Float>>> entry : eucledianDistances.entrySet()) {
            String firstTuple = "\n" + tuple2String.apply(invertedNormMap.get(entry.getKey())) + separator;
            for (Pair<Tuple<Float>, Float> tuple2 : entry.getValue()) {
                builder.append(firstTuple);
                builder.append(tuple2String.apply(invertedNormMap.get(tuple2.left())));
                builder.append(separator);
                builder.append(tuple2.right());
            }
        }
        SpecsIo.append(out, builder.toString());

        SpecsIo.append(out, "\n\n------- Order By Closest --------\n");
        builder = new StringBuilder();
        for (Entry<Tuple<Float>, List<Pair<Tuple<Float>, Float>>> entry : eucledianDistances.entrySet()) {
            String firstTuple = "\n" + tuple2String.apply(invertedNormMap.get(entry.getKey())) + separator;
            builder.append(firstTuple);
            builder.append(StringUtils.join(entry.getValue(), p -> tuple2String.apply(invertedNormMap.get(p.left())),
                    separator));
        }
        SpecsIo.append(out, builder.toString());
    }
}
