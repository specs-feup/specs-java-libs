package tdrc.tuple;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import tdrc.utils.Pair;

/**
 * Comprehensive unit tests for TupleUtils class.
 * Tests utility methods for tuple operations including normalization and
 * distance calculations.
 * 
 * @author Generated Tests
 */
@DisplayName("TupleUtils Tests")
public class TupleUtilsTest {

    @Nested
    @DisplayName("createNormalizedMap Tests")
    class CreateNormalizedMapTests {

        @Test
        @DisplayName("Should create normalized map for valid tuples")
        void testCreateNormalizedMap() {
            Collection<Tuple<Integer>> tuples = List.of(
                    Tuple.newInstance(1, 5, 3),
                    Tuple.newInstance(3, 1, 7),
                    Tuple.newInstance(5, 3, 1));

            Map<Tuple<Integer>, Tuple<Float>> normalizedMap = TupleUtils.createNormalizedMap(tuples, 3);

            assertThat(normalizedMap).hasSize(3);

            // Check normalization: (value - min) / (max - min)
            // For element 0: min=1, max=5, range=4
            // For element 1: min=1, max=5, range=4
            // For element 2: min=1, max=7, range=6

            Tuple<Integer> tuple1 = Tuple.newInstance(1, 5, 3);
            Tuple<Float> normalized1 = normalizedMap.get(tuple1);
            assertThat(normalized1.get(0)).isCloseTo(0.0f, within(1e-6f)); // (1-1)/4 = 0
            assertThat(normalized1.get(1)).isCloseTo(1.0f, within(1e-6f)); // (5-1)/4 = 1
            assertThat(normalized1.get(2)).isCloseTo(0.333333f, within(1e-6f)); // (3-1)/6 = 0.333

            Tuple<Integer> tuple2 = Tuple.newInstance(3, 1, 7);
            Tuple<Float> normalized2 = normalizedMap.get(tuple2);
            assertThat(normalized2.get(0)).isCloseTo(0.5f, within(1e-6f)); // (3-1)/4 = 0.5
            assertThat(normalized2.get(1)).isCloseTo(0.0f, within(1e-6f)); // (1-1)/4 = 0
            assertThat(normalized2.get(2)).isCloseTo(1.0f, within(1e-6f)); // (7-1)/6 = 1

            Tuple<Integer> tuple3 = Tuple.newInstance(5, 3, 1);
            Tuple<Float> normalized3 = normalizedMap.get(tuple3);
            assertThat(normalized3.get(0)).isCloseTo(1.0f, within(1e-6f)); // (5-1)/4 = 1
            assertThat(normalized3.get(1)).isCloseTo(0.5f, within(1e-6f)); // (3-1)/4 = 0.5
            assertThat(normalized3.get(2)).isCloseTo(0.0f, within(1e-6f)); // (1-1)/6 = 0
        }

        @Test
        @DisplayName("Should handle empty collection")
        void testEmptyCollection() {
            Collection<Tuple<Integer>> emptyTuples = List.of();

            Map<Tuple<Integer>, Tuple<Float>> result = TupleUtils.createNormalizedMap(emptyTuples, 3);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should handle single tuple")
        void testSingleTuple() {
            Collection<Tuple<Double>> tuples = List.of(
                    Tuple.newInstance(2.5, 4.0, 1.5));

            Map<Tuple<Double>, Tuple<Float>> result = TupleUtils.createNormalizedMap(tuples, 3);

            assertThat(result).hasSize(1);

            Tuple<Float> normalized = result.values().iterator().next();
            // When there's only one tuple, min=max, so (value-min)/(max-min) = 0/0 = NaN
            assertThat(normalized.get(0)).isNaN();
            assertThat(normalized.get(1)).isNaN();
            assertThat(normalized.get(2)).isNaN();
        }

        @Test
        @DisplayName("Should handle identical values in a dimension")
        void testIdenticalValues() {
            Collection<Tuple<Integer>> tuples = List.of(
                    Tuple.newInstance(1, 5, 3),
                    Tuple.newInstance(2, 5, 3),
                    Tuple.newInstance(3, 5, 3));

            Map<Tuple<Integer>, Tuple<Float>> result = TupleUtils.createNormalizedMap(tuples, 3);

            assertThat(result).hasSize(3);

            // First dimension should normalize properly (1,2,3 -> 0,0.5,1)
            // Second and third dimensions have identical values, so should result in NaN
            for (Tuple<Float> normalized : result.values()) {
                assertThat(normalized.get(1)).isNaN(); // All 5s
                assertThat(normalized.get(2)).isNaN(); // All 3s
            }
        }

        @Test
        @DisplayName("Should throw exception for mismatched tuple size")
        void testMismatchedTupleSize() {
            Collection<Tuple<Integer>> tuples = List.of(
                    Tuple.newInstance(1, 2, 3),
                    Tuple.newInstance(4, 5) // Wrong size!
            );

            assertThatThrownBy(() -> TupleUtils.createNormalizedMap(tuples, 3))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Unexpected tuple size: given 2, expected 3");
        }

        @Test
        @DisplayName("Should throw exception for first tuple size mismatch")
        void testFirstTupleSizeMismatch() {
            Collection<Tuple<Integer>> tuples = List.of(
                    Tuple.newInstance(1, 2) // Wrong size from the start!
            );

            assertThatThrownBy(() -> TupleUtils.createNormalizedMap(tuples, 3))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Unexpected tuple size: given 2, expected 3");
        }

        @Test
        @DisplayName("Should handle different number types")
        void testDifferentNumberTypes() {
            Collection<Tuple<Number>> tuples = List.of(
                    Tuple.newInstance(1, 2.5f, 3L),
                    Tuple.newInstance(4.0, 1, 6.5));

            Map<Tuple<Number>, Tuple<Float>> result = TupleUtils.createNormalizedMap(tuples, 3);

            assertThat(result).hasSize(2);
            // Should handle mixed number types correctly
            assertThat(result.values()).allSatisfy(tuple -> {
                assertThat(tuple.size()).isEqualTo(3);
                assertThat(tuple.get(0)).isBetween(0.0f, 1.0f);
                assertThat(tuple.get(1)).isBetween(0.0f, 1.0f);
                assertThat(tuple.get(2)).isBetween(0.0f, 1.0f);
            });
        }

        @Test
        @DisplayName("Should handle zero tuple size")
        void testZeroTupleSize() {
            Collection<Tuple<Integer>> tuples = List.of(
                    Tuple.newInstance(),
                    Tuple.newInstance());

            Map<Tuple<Integer>, Tuple<Float>> result = TupleUtils.createNormalizedMap(tuples, 0);

            // Note: Since both tuples are identical (empty), HashMap will only have 1 entry
            assertThat(result).hasSize(1);
            result.values().forEach(tuple -> assertThat(tuple.size()).isZero());
        }
    }

    @Nested
    @DisplayName("eucledianDistances Tests")
    class EuclideanDistancesTests {

        @Test
        @DisplayName("Should calculate distances between all tuple pairs")
        void testEuclideanDistances() {
            Collection<Tuple<Float>> tuples = List.of(
                    Tuple.newInstance(0.0f, 0.0f),
                    Tuple.newInstance(3.0f, 4.0f),
                    Tuple.newInstance(1.0f, 1.0f));

            Map<Tuple<Float>, Map<Tuple<Float>, Float>> distances = TupleUtils.eucledianDistances(tuples);

            assertThat(distances).hasSize(3);

            // Check specific distances
            Tuple<Float> origin = Tuple.newInstance(0.0f, 0.0f);
            Tuple<Float> point345 = Tuple.newInstance(3.0f, 4.0f);
            Tuple<Float> point11 = Tuple.newInstance(1.0f, 1.0f);

            // Distance from origin to (3,4) should be 5
            assertThat(distances.get(origin).get(point345)).isCloseTo(5.0f, within(1e-6f));

            // Distance from origin to (1,1) should be sqrt(2) ≈ 1.414
            assertThat(distances.get(origin).get(point11)).isCloseTo(1.4142135f, within(1e-6f));

            // Distance from (3,4) to (1,1) should be sqrt((3-1)² + (4-1)²) = sqrt(4+9) =
            // sqrt(13) ≈ 3.606
            assertThat(distances.get(point345).get(point11)).isCloseTo(3.6055512f, within(1e-6f));

            // Distance from tuple to itself should be 0
            assertThat(distances.get(origin).get(origin)).isZero();
            assertThat(distances.get(point345).get(point345)).isZero();
            assertThat(distances.get(point11).get(point11)).isZero();
        }

        @Test
        @DisplayName("Should handle empty collection")
        void testEmptyCollection() {
            Collection<Tuple<Float>> emptyTuples = List.of();

            Map<Tuple<Float>, Map<Tuple<Float>, Float>> result = TupleUtils.eucledianDistances(emptyTuples);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should handle single tuple")
        void testSingleTuple() {
            Collection<Tuple<Float>> tuples = List.of(
                    Tuple.newInstance(1.0f, 2.0f, 3.0f));

            Map<Tuple<Float>, Map<Tuple<Float>, Float>> result = TupleUtils.eucledianDistances(tuples);

            assertThat(result).hasSize(1);

            Tuple<Float> tuple = tuples.iterator().next();
            assertThat(result.get(tuple)).hasSize(1);
            assertThat(result.get(tuple).get(tuple)).isZero();
        }

        @Test
        @DisplayName("Should handle high-dimensional tuples")
        void testHighDimensional() {
            Collection<Tuple<Float>> tuples = List.of(
                    Tuple.newInstance(1.0f, 2.0f, 3.0f, 4.0f, 5.0f),
                    Tuple.newInstance(2.0f, 3.0f, 4.0f, 5.0f, 6.0f));

            Map<Tuple<Float>, Map<Tuple<Float>, Float>> result = TupleUtils.eucledianDistances(tuples);

            assertThat(result).hasSize(2);

            // Distance should be sqrt(5) since each dimension differs by 1
            Tuple<Float> tuple1 = tuples.iterator().next();
            Tuple<Float> tuple2 = Tuple.newInstance(2.0f, 3.0f, 4.0f, 5.0f, 6.0f);

            float expectedDistance = (float) Math.sqrt(5); // sqrt(1² + 1² + 1² + 1² + 1²)
            assertThat(result.get(tuple1).get(tuple2)).isCloseTo(expectedDistance, within(1e-6f));
        }
    }

    @Nested
    @DisplayName("getDistance Tests")
    class GetDistanceTests {

        @Test
        @DisplayName("Should calculate Euclidean distance correctly")
        void testGetDistance() {
            Tuple<Integer> tuple1 = Tuple.newInstance(0, 0, 0);
            Tuple<Integer> tuple2 = Tuple.newInstance(3, 4, 0);

            double distance = TupleUtils.getDistance(tuple1, tuple2);

            assertThat(distance).isCloseTo(5.0, within(1e-10)); // sqrt(3² + 4² + 0²) = 5
        }

        @Test
        @DisplayName("Should handle different number types")
        void testMixedNumberTypes() {
            Tuple<Number> tuple1 = Tuple.newInstance(1, 2.5f, 3L);
            Tuple<Number> tuple2 = Tuple.newInstance(1.0, 2.5, 3);

            double distance = TupleUtils.getDistance(tuple1, tuple2);

            assertThat(distance).isCloseTo(0.0, within(1e-10)); // Should be identical
        }

        @Test
        @DisplayName("Should calculate distance for single element tuples")
        void testSingleElement() {
            Tuple<Double> tuple1 = Tuple.newInstance(1.0);
            Tuple<Double> tuple2 = Tuple.newInstance(4.0);

            double distance = TupleUtils.getDistance(tuple1, tuple2);

            assertThat(distance).isCloseTo(3.0, within(1e-10)); // |4.0 - 1.0| = 3.0
        }

        @Test
        @DisplayName("Should return zero for identical tuples")
        void testIdenticalTuples() {
            Tuple<Integer> tuple1 = Tuple.newInstance(1, 2, 3, 4, 5);
            Tuple<Integer> tuple2 = Tuple.newInstance(1, 2, 3, 4, 5);

            double distance = TupleUtils.getDistance(tuple1, tuple2);

            assertThat(distance).isCloseTo(0.0, within(1e-10));
        }

        @Test
        @DisplayName("Should handle negative numbers")
        void testNegativeNumbers() {
            Tuple<Integer> tuple1 = Tuple.newInstance(-3, -4);
            Tuple<Integer> tuple2 = Tuple.newInstance(0, 0);

            double distance = TupleUtils.getDistance(tuple1, tuple2);

            assertThat(distance).isCloseTo(5.0, within(1e-10)); // sqrt((-3-0)² + (-4-0)²) = sqrt(9+16) = 5
        }
    }

    @Nested
    @DisplayName("eucledianDistancesByClosest Tests")
    class EuclideanDistancesByClosestTests {

        @Test
        @DisplayName("Should return distances sorted by closest")
        void testEuclideanDistancesByClosest() {
            Collection<Tuple<Float>> tuples = List.of(
                    Tuple.newInstance(0.0f, 0.0f), // Origin
                    Tuple.newInstance(1.0f, 0.0f), // Distance 1 from origin
                    Tuple.newInstance(0.0f, 2.0f), // Distance 2 from origin
                    Tuple.newInstance(3.0f, 4.0f) // Distance 5 from origin
            );

            Map<Tuple<Float>, List<Pair<Tuple<Float>, Float>>> result = TupleUtils.eucledianDistancesByClosest(tuples);

            assertThat(result).hasSize(4);

            // Check origin's distances - should be sorted by distance
            Tuple<Float> origin = Tuple.newInstance(0.0f, 0.0f);
            List<Pair<Tuple<Float>, Float>> originDistances = result.get(origin);

            assertThat(originDistances).hasSize(3); // Excludes self

            // Should be sorted by distance: (1,0) distance 1, (0,2) distance 2, (3,4)
            // distance 5
            assertThat(originDistances.get(0).right()).isCloseTo(1.0f, within(1e-6f));
            assertThat(originDistances.get(1).right()).isCloseTo(2.0f, within(1e-6f));
            assertThat(originDistances.get(2).right()).isCloseTo(5.0f, within(1e-6f));

            // Verify tuple references
            assertThat((Object) originDistances.get(0).left()).isEqualTo(Tuple.newInstance(1.0f, 0.0f));
            assertThat((Object) originDistances.get(1).left()).isEqualTo(Tuple.newInstance(0.0f, 2.0f));
            assertThat((Object) originDistances.get(2).left()).isEqualTo(Tuple.newInstance(3.0f, 4.0f));
        }

        @Test
        @DisplayName("Should exclude self from distances")
        void testExcludesSelf() {
            Collection<Tuple<Float>> tuples = List.of(
                    Tuple.newInstance(1.0f, 1.0f),
                    Tuple.newInstance(2.0f, 2.0f));

            Map<Tuple<Float>, List<Pair<Tuple<Float>, Float>>> result = TupleUtils.eucledianDistancesByClosest(tuples);

            assertThat(result).hasSize(2);

            for (List<Pair<Tuple<Float>, Float>> distances : result.values()) {
                assertThat(distances).hasSize(1); // Only the other tuple, not self
                assertThat(distances.get(0).right()).isPositive(); // Distance should be > 0
            }
        }

        @Test
        @DisplayName("Should handle empty collection")
        void testEmptyCollection() {
            Collection<Tuple<Float>> emptyTuples = List.of();

            Map<Tuple<Float>, List<Pair<Tuple<Float>, Float>>> result = TupleUtils
                    .eucledianDistancesByClosest(emptyTuples);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should handle single tuple")
        void testSingleTuple() {
            Collection<Tuple<Float>> tuples = List.of(
                    Tuple.newInstance(1.0f, 2.0f));

            Map<Tuple<Float>, List<Pair<Tuple<Float>, Float>>> result = TupleUtils.eucledianDistancesByClosest(tuples);

            assertThat(result).hasSize(1);

            List<Pair<Tuple<Float>, Float>> distances = result.values().iterator().next();
            assertThat(distances).isEmpty(); // No other tuples to compare to
        }

        @Test
        @DisplayName("Should maintain sort order with equal distances")
        void testEqualDistances() {
            Collection<Tuple<Float>> tuples = List.of(
                    Tuple.newInstance(0.0f, 0.0f), // Origin
                    Tuple.newInstance(1.0f, 0.0f), // Distance 1
                    Tuple.newInstance(0.0f, 1.0f), // Distance 1 (same as above)
                    Tuple.newInstance(-1.0f, 0.0f) // Distance 1 (same as above)
            );

            Map<Tuple<Float>, List<Pair<Tuple<Float>, Float>>> result = TupleUtils.eucledianDistancesByClosest(tuples);

            Tuple<Float> origin = Tuple.newInstance(0.0f, 0.0f);
            List<Pair<Tuple<Float>, Float>> originDistances = result.get(origin);

            assertThat(originDistances).hasSize(3);

            // All distances should be 1.0
            assertThat(originDistances).allSatisfy(pair -> assertThat(pair.right()).isCloseTo(1.0f, within(1e-6f)));
        }

        @Test
        @DisplayName("Should calculate distances consistently with getDistance method")
        void testConsistencyWithGetDistance() {
            Tuple<Float> tuple1 = Tuple.newInstance(1.0f, 2.0f, 3.0f);
            Tuple<Float> tuple2 = Tuple.newInstance(4.0f, 5.0f, 6.0f);

            Collection<Tuple<Float>> tuples = List.of(tuple1, tuple2);

            Map<Tuple<Float>, List<Pair<Tuple<Float>, Float>>> result = TupleUtils.eucledianDistancesByClosest(tuples);

            Float distanceFromMethod = result.get(tuple1).get(0).right();
            double distanceFromGetDistance = TupleUtils.getDistance(tuple1, tuple2);

            assertThat(distanceFromMethod.doubleValue()).isCloseTo(distanceFromGetDistance, within(1e-6));
        }
    }
}
