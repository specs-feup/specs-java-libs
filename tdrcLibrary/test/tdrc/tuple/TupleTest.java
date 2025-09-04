package tdrc.tuple;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.function.BiFunction;

/**
 * Comprehensive unit tests for Tuple class.
 * Tests generic tuple functionality including creation, modification,
 * comparison, and distance calculation.
 * 
 * @author Generated Tests
 */
@DisplayName("Tuple Tests")
public class TupleTest {

    @Nested
    @DisplayName("Factory Methods and Constructors")
    class FactoryMethodsTests {

        @Test
        @DisplayName("Should create empty tuple via newInstance()")
        void testEmptyTuple() {
            Tuple<String> empty = Tuple.newInstance();

            assertThat(empty.size()).isZero();
        }

        @Test
        @DisplayName("Should create tuple from list via newInstance(List)")
        void testFromList() {
            List<Integer> list = List.of(1, 2, 3, 4, 5);
            Tuple<Integer> tuple = Tuple.newInstance(list);

            assertThat(tuple.size()).isEqualTo(5);
            assertThat(tuple.get(0)).isEqualTo(1);
            assertThat(tuple.get(4)).isEqualTo(5);
            assertThat((List<Integer>) tuple).containsExactly(1, 2, 3, 4, 5);
        }

        @Test
        @DisplayName("Should create tuple from varargs via newInstance(T...)")
        void testFromVarargs() {
            Tuple<String> tuple = Tuple.newInstance("a", "b", "c");

            assertThat(tuple.size()).isEqualTo(3);
            assertThat(tuple.get(0)).isEqualTo("a");
            assertThat(tuple.get(1)).isEqualTo("b");
            assertThat(tuple.get(2)).isEqualTo("c");
            assertThat((List<String>) tuple).containsExactly("a", "b", "c");
        }

        @Test
        @DisplayName("Should create tuple with single element")
        void testSingleElement() {
            Tuple<Integer> tuple = Tuple.newInstance(42);

            assertThat(tuple.size()).isEqualTo(1);
            assertThat(tuple.get(0)).isEqualTo(42);
        }

        @Test
        @DisplayName("Should handle empty varargs")
        void testEmptyVarargs() {
            Tuple<String> tuple = Tuple.newInstance();

            assertThat(tuple.size()).isZero();
        }

        @Test
        @DisplayName("Should handle null values in creation")
        void testNullValues() {
            Tuple<String> tuple = Tuple.newInstance("a", null, "c");

            assertThat(tuple.size()).isEqualTo(3);
            assertThat(tuple.get(0)).isEqualTo("a");
            assertThat(tuple.get(1)).isNull();
            assertThat(tuple.get(2)).isEqualTo("c");
        }
    }

    @Nested
    @DisplayName("List Operations")
    class ListOperationsTests {

        @Test
        @DisplayName("Should get elements by index")
        void testGet() {
            Tuple<String> tuple = Tuple.newInstance("first", "second", "third");

            assertThat(tuple.get(0)).isEqualTo("first");
            assertThat(tuple.get(1)).isEqualTo("second");
            assertThat(tuple.get(2)).isEqualTo("third");
        }

        @Test
        @DisplayName("Should throw IndexOutOfBoundsException for invalid indices")
        void testGetInvalidIndex() {
            Tuple<String> tuple = Tuple.newInstance("a", "b");

            assertThatThrownBy(() -> tuple.get(-1))
                    .isInstanceOf(IndexOutOfBoundsException.class);
            assertThatThrownBy(() -> tuple.get(2))
                    .isInstanceOf(IndexOutOfBoundsException.class);
        }

        @Test
        @DisplayName("Should return correct size")
        void testSize() {
            assertThat(Tuple.newInstance().size()).isZero();
            assertThat(Tuple.newInstance("a").size()).isEqualTo(1);
            assertThat(Tuple.newInstance("a", "b", "c").size()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should set elements at index")
        void testSet() {
            Tuple<String> tuple = Tuple.newInstance("a", "b", "c");

            String old = tuple.set(1, "newValue");

            assertThat(old).isEqualTo("b");
            assertThat(tuple.get(1)).isEqualTo("newValue");
            assertThat((List<String>) tuple).containsExactly("a", "newValue", "c");
        }

        @Test
        @DisplayName("Should add elements at index")
        void testAddAtIndex() {
            Tuple<String> tuple = Tuple.newInstance("a", "c");

            tuple.add(1, "b");

            assertThat(tuple.size()).isEqualTo(3);
            assertThat((List<String>) tuple).containsExactly("a", "b", "c");
        }

        @Test
        @DisplayName("Should add elements at beginning and end")
        void testAddAtBoundaries() {
            Tuple<Integer> tuple = Tuple.newInstance(2, 3);

            tuple.add(0, 1); // Add at beginning
            tuple.add(tuple.size(), 4); // Add at end

            assertThat((List<Integer>) tuple).containsExactly(1, 2, 3, 4);
        }

        @Test
        @DisplayName("Should remove elements")
        void testRemove() {
            Tuple<String> tuple = Tuple.newInstance("a", "b", "c", "b");

            boolean removed = tuple.remove("b"); // Should remove first occurrence

            assertThat(removed).isTrue();
            assertThat((List<String>) tuple).containsExactly("a", "c", "b");

            boolean notRemoved = tuple.remove("x");
            assertThat(notRemoved).isFalse();
        }
    }

    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal to tuple with same elements")
        void testEqualTuples() {
            Tuple<Integer> tuple1 = Tuple.newInstance(1, 2, 3);
            Tuple<Integer> tuple2 = Tuple.newInstance(1, 2, 3);

            assertThat((Object) tuple1).isEqualTo(tuple2);
            assertThat((Object) tuple2).isEqualTo(tuple1);
            assertThat(tuple1.equals(tuple2)).isTrue();
        }

        @Test
        @DisplayName("Should not be equal to tuple with different elements")
        void testUnequalTuples() {
            Tuple<Integer> tuple1 = Tuple.newInstance(1, 2, 3);
            Tuple<Integer> tuple2 = Tuple.newInstance(1, 2, 4);

            assertThat((Object) tuple1).isNotEqualTo(tuple2);
            assertThat(tuple1.equals(tuple2)).isFalse();
        }

        @Test
        @DisplayName("Should not be equal to tuple with different size")
        void testDifferentSizes() {
            Tuple<Integer> tuple1 = Tuple.newInstance(1, 2);
            Tuple<Integer> tuple2 = Tuple.newInstance(1, 2, 3);

            assertThat((Object) tuple1).isNotEqualTo(tuple2);
            assertThat(tuple1.equals(tuple2)).isFalse();
        }

        @Test
        @DisplayName("Should not be equal to non-tuple objects")
        void testNotEqualToOtherObjects() {
            Tuple<String> tuple = Tuple.newInstance("a", "b");

            assertThat(tuple.equals(null)).isFalse();
            assertThat(tuple.equals("not a tuple")).isFalse();
            assertThat(tuple.equals(List.of("a", "b"))).isFalse();
        }

        @Test
        @DisplayName("Should handle null elements in equality")
        void testEqualityWithNulls() {
            Tuple<String> tuple1 = Tuple.newInstance("a", null, "c");
            Tuple<String> tuple2 = Tuple.newInstance("a", null, "c");
            Tuple<String> tuple3 = Tuple.newInstance("a", "b", "c");

            assertThat((Object) tuple1).isEqualTo(tuple2);
            assertThat((Object) tuple1).isNotEqualTo(tuple3);
        }

        @Test
        @DisplayName("Should be equal to itself")
        void testReflexivity() {
            Tuple<String> tuple = Tuple.newInstance("a", "b", "c");

            assertThat((Object) tuple).isEqualTo(tuple);
            assertThat(tuple.equals(tuple)).isTrue();
        }
    }

    @Nested
    @DisplayName("Comparison Tests")
    class ComparisonTests {

        @Test
        @DisplayName("Should compare tuples using default comparator")
        void testDefaultComparison() {
            Tuple<Integer> tuple1 = Tuple.newInstance(1, 2, 3);
            Tuple<Integer> tuple2 = Tuple.newInstance(1, 2, 4);
            Tuple<Integer> tuple3 = Tuple.newInstance(1, 2, 3);

            assertThat(tuple1.compareTo(tuple2)).isNegative();
            assertThat(tuple2.compareTo(tuple1)).isPositive();
            assertThat(tuple1.compareTo(tuple3)).isZero();
        }

        @Test
        @DisplayName("Should compare tuples of different lengths")
        void testDifferentLengthComparison() {
            Tuple<Integer> short1 = Tuple.newInstance(1, 2);
            Tuple<Integer> long1 = Tuple.newInstance(1, 2, 3);

            // Should compare based on common elements since short tuple is prefix of long
            int comparison = short1.compareTo(long1);
            assertThat(comparison).isNotPositive(); // Could be 0 or negative depending on implementation
        }

        @Test
        @DisplayName("Should handle empty tuple comparisons")
        void testEmptyTupleComparison() {
            Tuple<String> empty1 = Tuple.newInstance();
            Tuple<String> empty2 = Tuple.newInstance();
            Tuple<String> nonEmpty = Tuple.newInstance("a");

            assertThat(empty1.compareTo(empty2)).isZero();
            assertThat(empty1.compareTo(nonEmpty)).isNotPositive();
            assertThat(nonEmpty.compareTo(empty1)).isNotNegative();
        }

        @Test
        @DisplayName("Should use static defaultComparator method")
        void testStaticComparator() {
            Tuple<String> tuple1 = Tuple.newInstance("a", "b");
            Tuple<String> tuple2 = Tuple.newInstance("a", "c");

            Integer result = Tuple.defaultComparator(tuple1, tuple2);

            assertThat(result).isNegative();
            assertThat(result).isEqualTo(tuple1.compareTo(tuple2));
        }
    }

    @Nested
    @DisplayName("Distance Calculation Tests")
    class DistanceCalculationTests {

        @Test
        @DisplayName("Should calculate Euclidean distance for numeric tuples")
        void testEuclideanDistance() {
            Tuple<Integer> tuple1 = Tuple.newInstance(0, 0, 0);
            Tuple<Integer> tuple2 = Tuple.newInstance(3, 4, 0);

            double distance = tuple1.getDistance(tuple2);

            // Distance should be sqrt(3^2 + 4^2 + 0^2) = 5
            assertThat(distance).isCloseTo(5.0, within(1e-10));
        }

        @Test
        @DisplayName("Should calculate distance for single element tuples")
        void testSingleElementDistance() {
            Tuple<Double> tuple1 = Tuple.newInstance(1.0);
            Tuple<Double> tuple2 = Tuple.newInstance(4.0);

            double distance = tuple1.getDistance(tuple2);

            // Distance should be |4.0 - 1.0| = 3.0
            assertThat(distance).isCloseTo(3.0, within(1e-10));
        }

        @Test
        @DisplayName("Should return zero distance for identical tuples")
        void testZeroDistance() {
            Tuple<Integer> tuple1 = Tuple.newInstance(1, 2, 3);
            Tuple<Integer> tuple2 = Tuple.newInstance(1, 2, 3);

            double distance = tuple1.getDistance(tuple2);

            assertThat(distance).isCloseTo(0.0, within(1e-10));
        }

        @Test
        @DisplayName("Should handle different numeric types")
        void testMixedNumericTypes() {
            Tuple<Number> tuple1 = Tuple.newInstance(1, 2.5, 3L);
            Tuple<Number> tuple2 = Tuple.newInstance(1.0, 2.5f, 3);

            double distance = tuple1.getDistance(tuple2);

            assertThat(distance).isCloseTo(0.0, within(1e-6)); // Small tolerance for float precision
        }

        @Test
        @DisplayName("Should throw exception for non-numeric elements")
        void testNonNumericDistance() {
            Tuple<String> tuple1 = Tuple.newInstance("a", "b");
            Tuple<String> tuple2 = Tuple.newInstance("c", "d");

            assertThatThrownBy(() -> tuple1.getDistance(tuple2))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Cannot calculate distance if the elements of the tuple are not numbers");
        }

        @Test
        @DisplayName("Should use static defaultDistanceCalculator method")
        void testStaticDistanceCalculator() {
            Tuple<Integer> tuple1 = Tuple.newInstance(0, 0);
            Tuple<Integer> tuple2 = Tuple.newInstance(3, 4);

            Double result = Tuple.defaultDistanceCalculator(tuple1, tuple2);

            assertThat(result).isCloseTo(5.0, within(1e-10));
            assertThat(result).isEqualTo(tuple1.getDistance(tuple2));
        }
    }

    @Nested
    @DisplayName("Custom Function Tests")
    class CustomFunctionTests {

        @Test
        @DisplayName("Should get and set custom distance calculator")
        void testCustomDistanceCalculator() {
            Tuple<Integer> tuple = Tuple.newInstance(1, 2);

            // Create Manhattan distance calculator
            BiFunction<Tuple<Integer>, Tuple<Integer>, Double> manhattanDistance = (t1, t2) -> {
                double sum = 0;
                int size = Math.min(t1.size(), t2.size());
                for (int i = 0; i < size; i++) {
                    sum += Math.abs(t1.get(i) - t2.get(i));
                }
                return sum;
            };

            tuple.setDistanceCalculator(manhattanDistance);
            assertThat(tuple.getDistanceCalculator()).isSameAs(manhattanDistance);

            // Test with Manhattan distance
            Tuple<Integer> other = Tuple.newInstance(4, 6);
            double distance = tuple.getDistance(other);

            // Manhattan distance: |1-4| + |2-6| = 3 + 4 = 7
            assertThat(distance).isCloseTo(7.0, within(1e-10));
        }
    }

    @Nested
    @DisplayName("Generic Type Tests")
    class GenericTypeTests {

        @Test
        @DisplayName("Should handle different generic types")
        void testDifferentTypes() {
            Tuple<String> stringTuple = Tuple.newInstance("hello", "world");
            Tuple<Integer> intTuple = Tuple.newInstance(1, 2, 3);
            Tuple<Double> doubleTuple = Tuple.newInstance(1.1, 2.2);

            assertThat(stringTuple.get(0)).isInstanceOf(String.class);
            assertThat(intTuple.get(0)).isInstanceOf(Integer.class);
            assertThat(doubleTuple.get(0)).isInstanceOf(Double.class);
        }

        @Test
        @DisplayName("Should handle complex object types")
        void testComplexTypes() {
            Tuple<List<String>> complexTuple = Tuple.newInstance(
                    List.of("a", "b"),
                    List.of("c", "d", "e"));

            assertThat(complexTuple.size()).isEqualTo(2);
            assertThat(complexTuple.get(0)).containsExactly("a", "b");
            assertThat(complexTuple.get(1)).containsExactly("c", "d", "e");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle large tuples")
        void testLargeTuple() {
            Integer[] values = new Integer[1000];
            for (int i = 0; i < 1000; i++) {
                values[i] = i;
            }

            Tuple<Integer> largeTuple = Tuple.newInstance(values);

            assertThat(largeTuple.size()).isEqualTo(1000);
            assertThat(largeTuple.get(0)).isZero();
            assertThat(largeTuple.get(999)).isEqualTo(999);
        }

        @Test
        @DisplayName("Should maintain immutability of creation parameters")
        void testImmutabilityOfParameters() {
            List<String> originalList = new java.util.ArrayList<>();
            originalList.add("a");
            originalList.add("b");

            Tuple<String> tuple = Tuple.newInstance(originalList);

            // Modify original list
            originalList.add("c");
            originalList.set(0, "modified");

            // Tuple should be unaffected
            assertThat(tuple.size()).isEqualTo(2);
            assertThat(tuple.get(0)).isEqualTo("a");
            assertThat(tuple.get(1)).isEqualTo("b");
        }

        @Test
        @DisplayName("Should support iteration")
        void testIteration() {
            Tuple<String> tuple = Tuple.newInstance("a", "b", "c");

            StringBuilder result = new StringBuilder();
            for (String element : tuple) {
                result.append(element);
            }

            assertThat(result.toString()).isEqualTo("abc");
        }

        @Test
        @DisplayName("Should handle mixed operations correctly")
        void testMixedOperations() {
            Tuple<Integer> tuple = Tuple.newInstance(1, 2, 3);

            // Mix of operations
            tuple.add(0, 0); // [0, 1, 2, 3]
            tuple.set(3, 99); // [0, 1, 2, 99]
            tuple.remove(Integer.valueOf(1)); // [0, 2, 99]

            assertThat((List<Integer>) tuple).containsExactly(0, 2, 99);
            assertThat(tuple.size()).isEqualTo(3);
        }
    }
}
