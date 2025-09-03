package pt.up.fe.specs.util.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for {@link AccumulatorMapL}.
 * Tests long-based accumulator map functionality for counting occurrences.
 * 
 * @author Generated Tests
 */
class AccumulatorMapLTest {

    private AccumulatorMapL<String> accMap;
    private AccumulatorMapL<Integer> intMap;

    @BeforeEach
    void setUp() {
        accMap = new AccumulatorMapL<>();
        intMap = new AccumulatorMapL<>();
    }

    @Nested
    @DisplayName("Constructor and Basic Properties")
    class ConstructorTests {

        @Test
        @DisplayName("Should create empty accumulator map")
        void testEmptyCreation() {
            assertThat(accMap.getSum()).isZero();
            assertThat(accMap.getAccMap()).isEmpty();
        }

        @Test
        @DisplayName("Should create copy of existing map")
        void testCopyConstructor() {
            accMap.add("item1");
            accMap.add("item2", 3);

            AccumulatorMapL<String> copy = new AccumulatorMapL<>(accMap);

            assertThat(copy.getSum()).isEqualTo(accMap.getSum());
            assertThat(copy.getCount("item1")).isEqualTo(accMap.getCount("item1"));
            assertThat(copy.getCount("item2")).isEqualTo(accMap.getCount("item2"));
            assertThat(copy.getAccMap()).isEqualTo(accMap.getAccMap());
        }

        @Test
        @DisplayName("Should create independent copy")
        void testIndependentCopy() {
            accMap.add("item1");

            AccumulatorMapL<String> copy = new AccumulatorMapL<>(accMap);
            copy.add("item2");

            assertThat(accMap.getCount("item2")).isZero();
            assertThat(copy.getCount("item2")).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("Adding Elements")
    class AddingElementsTests {

        @Test
        @DisplayName("Should add single element")
        void testAddSingleElement() {
            Long count = accMap.add("item1");

            assertThat(count).isEqualTo(1L);
            assertThat(accMap.getCount("item1")).isEqualTo(1L);
            assertThat(accMap.getSum()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Should add multiple occurrences of same element")
        void testAddMultipleOccurrences() {
            accMap.add("item1");
            accMap.add("item1");
            Long count = accMap.add("item1");

            assertThat(count).isEqualTo(3L);
            assertThat(accMap.getCount("item1")).isEqualTo(3L);
            assertThat(accMap.getSum()).isEqualTo(3L);
        }

        @Test
        @DisplayName("Should add different elements")
        void testAddDifferentElements() {
            accMap.add("item1");
            accMap.add("item2");
            accMap.add("item3");

            assertThat(accMap.getCount("item1")).isEqualTo(1L);
            assertThat(accMap.getCount("item2")).isEqualTo(1L);
            assertThat(accMap.getCount("item3")).isEqualTo(1L);
            assertThat(accMap.getSum()).isEqualTo(3L);
        }

        @Test
        @DisplayName("Should add with custom increment value")
        void testAddWithIncrementValue() {
            Long count = accMap.add("item1", 5L);

            assertThat(count).isEqualTo(5L);
            assertThat(accMap.getCount("item1")).isEqualTo(5L);
            assertThat(accMap.getSum()).isEqualTo(5L);
        }

        @Test
        @DisplayName("Should accumulate with different increment values")
        void testAccumulateWithDifferentIncrements() {
            accMap.add("item1", 3L);
            accMap.add("item1", 7L);

            assertThat(accMap.getCount("item1")).isEqualTo(10L);
            assertThat(accMap.getSum()).isEqualTo(10L);
        }

        @Test
        @DisplayName("Should handle null elements")
        void testAddNullElement() {
            Long count = accMap.add(null);

            assertThat(count).isEqualTo(1L);
            assertThat(accMap.getCount(null)).isEqualTo(1L);
            assertThat(accMap.getSum()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Should handle zero increment value")
        void testZeroIncrement() {
            Long count = accMap.add("item1", 0L);

            assertThat(count).isZero();
            assertThat(accMap.getCount("item1")).isZero();
            assertThat(accMap.getSum()).isZero();
        }

        @Test
        @DisplayName("Should handle negative increment values")
        void testNegativeIncrement() {
            accMap.add("item1", 10L);
            Long count = accMap.add("item1", -3L);

            assertThat(count).isEqualTo(7L);
            assertThat(accMap.getCount("item1")).isEqualTo(7L);
            assertThat(accMap.getSum()).isEqualTo(7L);
        }
    }

    @Nested
    @DisplayName("Removing Elements")
    class RemovingElementsTests {

        @Test
        @DisplayName("Should remove single occurrence")
        void testRemoveSingleOccurrence() {
            accMap.add("item1", 3L);
            boolean result = accMap.remove("item1");

            assertThat(result).isTrue();
            assertThat(accMap.getCount("item1")).isEqualTo(2L);
            assertThat(accMap.getSum()).isEqualTo(2L);
        }

        @Test
        @DisplayName("Should remove multiple occurrences")
        void testRemoveMultipleOccurrences() {
            accMap.add("item1", 5L);
            boolean result = accMap.remove("item1", 3);

            assertThat(result).isTrue();
            assertThat(accMap.getCount("item1")).isEqualTo(2L);
            assertThat(accMap.getSum()).isEqualTo(2L);
        }

        @Test
        @DisplayName("Should return false for non-existent element")
        void testRemoveNonExistentElement() {
            boolean result = accMap.remove("nonexistent");

            assertThat(result).isFalse();
            assertThat(accMap.getSum()).isZero();
        }

        @Test
        @DisplayName("Should handle removing more than available")
        void testRemoveMoreThanAvailable() {
            accMap.add("item1", 2L);
            boolean result = accMap.remove("item1", 5);

            assertThat(result).isTrue();
            assertThat(accMap.getCount("item1")).isEqualTo(-3L);
            assertThat(accMap.getSum()).isEqualTo(-3L);
        }

        @Test
        @DisplayName("Should remove all occurrences")
        void testRemoveAllOccurrences() {
            accMap.add("item1", 3L);
            accMap.remove("item1", 3);

            assertThat(accMap.getCount("item1")).isZero();
            assertThat(accMap.getSum()).isZero();
        }
    }

    @Nested
    @DisplayName("Count and Ratio Operations")
    class CountRatioTests {

        @Test
        @DisplayName("Should return zero count for non-existent element")
        void testZeroCountForNonExistent() {
            assertThat(accMap.getCount("nonexistent")).isZero();
        }

        @Test
        @DisplayName("Should calculate correct ratios")
        void testRatioCalculation() {
            accMap.add("item1", 3L);
            accMap.add("item2", 7L);

            assertThat(accMap.getRatio("item1")).isEqualTo(0.3);
            assertThat(accMap.getRatio("item2")).isEqualTo(0.7);
        }

        @Test
        @DisplayName("Should handle ratio calculation with single element")
        void testRatioSingleElement() {
            accMap.add("item1", 5L);

            assertThat(accMap.getRatio("item1")).isEqualTo(1.0);
        }

        @Test
        @DisplayName("Should return zero ratio for non-existent element")
        void testZeroRatioForNonExistent() {
            accMap.add("item1", 5L);

            assertThat(accMap.getRatio("nonexistent")).isZero();
        }

        @Test
        @DisplayName("Should calculate ratio correctly after modifications")
        void testRatioAfterModifications() {
            accMap.add("item1", 4L);
            accMap.add("item2", 6L);
            accMap.remove("item1", 2);

            // item1: 2, item2: 6, total: 8
            assertThat(accMap.getRatio("item1")).isEqualTo(0.25);
            assertThat(accMap.getRatio("item2")).isEqualTo(0.75);
        }
    }

    @Nested
    @DisplayName("Sum Operations")
    class SumOperationsTests {

        @Test
        @DisplayName("Should maintain correct sum with additions")
        void testSumWithAdditions() {
            accMap.add("item1", 3L);
            accMap.add("item2", 5L);
            accMap.add("item1", 2L);

            assertThat(accMap.getSum()).isEqualTo(10L);
        }

        @Test
        @DisplayName("Should maintain correct sum with removals")
        void testSumWithRemovals() {
            accMap.add("item1", 10L);
            accMap.add("item2", 5L);
            accMap.remove("item1", 3);

            assertThat(accMap.getSum()).isEqualTo(12L);
        }

        @Test
        @DisplayName("Should handle negative sums")
        void testNegativeSum() {
            accMap.add("item1", 5L);
            accMap.remove("item1", 10);

            assertThat(accMap.getSum()).isEqualTo(-5L);
        }

        @Test
        @DisplayName("Should maintain sum consistency")
        void testSumConsistency() {
            accMap.add("a", 3L);
            accMap.add("b", 7L);
            accMap.add("c", 2L);

            long manualSum = accMap.getCount("a") + accMap.getCount("b") + accMap.getCount("c");
            assertThat(accMap.getSum()).isEqualTo(manualSum);
        }
    }

    @Nested
    @DisplayName("Unmodifiable Map Operations")
    class UnmodifiableMapTests {

        @Test
        @DisplayName("Should create unmodifiable view")
        void testUnmodifiableView() {
            accMap.add("item1", 3L);
            accMap.add("item2", 5L);

            AccumulatorMapL<String> unmodifiable = accMap.getUnmodifiableMap();

            assertThat(unmodifiable.getSum()).isEqualTo(accMap.getSum());
            assertThat(unmodifiable.getCount("item1")).isEqualTo(accMap.getCount("item1"));
            assertThat(unmodifiable.getCount("item2")).isEqualTo(accMap.getCount("item2"));
        }

        @Test
        @DisplayName("Should throw exception on modification attempts")
        void testUnmodifiableThrowsException() {
            accMap.add("item1", 3L);
            AccumulatorMapL<String> unmodifiable = accMap.getUnmodifiableMap();

            assertThatThrownBy(() -> unmodifiable.add("item2"))
                    .isInstanceOf(UnsupportedOperationException.class)
                    .hasMessageContaining("unmodifiable");
        }

        @Test
        @DisplayName("Should create independent unmodifiable snapshot")
        void testUnmodifiableIsSnapshot() {
            accMap.add("item1", 3L);
            AccumulatorMapL<String> unmodifiable = accMap.getUnmodifiableMap();

            // Changes to original should not affect the unmodifiable snapshot
            accMap.add("item1", 2L);

            assertThat(unmodifiable.getSum()).isEqualTo(3L); // Should remain the snapshot value
            assertThat(accMap.getSum()).isEqualTo(5L); // Original should have changed
        }
    }

    @Nested
    @DisplayName("Map Access")
    class MapAccessTests {

        @Test
        @DisplayName("Should provide unmodifiable map access")
        void testGetAccMap() {
            accMap.add("item1", 3L);
            accMap.add("item2", 5L);

            Map<String, Long> map = accMap.getAccMap();

            assertThat(map).hasSize(2);
            assertThat(map).containsEntry("item1", 3L);
            assertThat(map).containsEntry("item2", 5L);
        }

        @Test
        @DisplayName("Should return unmodifiable map")
        void testAccMapIsUnmodifiable() {
            accMap.add("item1", 3L);
            Map<String, Long> map = accMap.getAccMap();

            assertThatThrownBy(() -> map.put("item2", 5L))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("Should reflect changes in acc map")
        void testAccMapReflectsChanges() {
            Map<String, Long> map = accMap.getAccMap();
            accMap.add("item1", 3L);

            // The unmodifiable map should reflect changes in the original
            assertThat(map).containsEntry("item1", 3L);
        }
    }

    @Nested
    @DisplayName("Equality and HashCode")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal to itself")
        void testSelfEquality() {
            accMap.add("item1", 3L);

            assertThat(accMap).isEqualTo(accMap);
            assertThat(accMap.hashCode()).isEqualTo(accMap.hashCode());
        }

        @Test
        @DisplayName("Should be equal to equivalent map")
        void testEquivalentMaps() {
            accMap.add("item1", 3L);
            accMap.add("item2", 5L);

            AccumulatorMapL<String> other = new AccumulatorMapL<>();
            other.add("item1", 3L);
            other.add("item2", 5L);

            assertThat(accMap).isEqualTo(other);
            assertThat(accMap.hashCode()).isEqualTo(other.hashCode());
        }

        @Test
        @DisplayName("Should not be equal to different map")
        void testDifferentMaps() {
            accMap.add("item1", 3L);

            AccumulatorMapL<String> other = new AccumulatorMapL<>();
            other.add("item1", 5L);

            assertThat(accMap).isNotEqualTo(other);
        }

        @Test
        @DisplayName("Should not be equal to different types")
        void testDifferentTypes() {
            assertThat(accMap).isNotEqualTo("string");
            assertThat(accMap).isNotEqualTo(null);
            assertThat(accMap).isNotEqualTo(42);
        }

        @Test
        @DisplayName("Should be equal despite different addition order")
        void testOrderIndependent() {
            accMap.add("item1", 2L);
            accMap.add("item1", 1L);

            AccumulatorMapL<String> other = new AccumulatorMapL<>();
            other.add("item1", 3L);

            assertThat(accMap).isEqualTo(other);
        }
    }

    @Nested
    @DisplayName("String Representation")
    class StringRepresentationTests {

        @Test
        @DisplayName("Should have string representation")
        void testToString() {
            accMap.add("item1", 3L);
            accMap.add("item2", 5L);

            String result = accMap.toString();

            assertThat(result).isNotNull();
            assertThat(result).contains("item1");
            assertThat(result).contains("item2");
        }

        @Test
        @DisplayName("Should handle empty map string representation")
        void testEmptyToString() {
            String result = accMap.toString();

            assertThat(result).isNotNull();
            assertThat(result).contains("{}");
        }
    }

    @Nested
    @DisplayName("Different Element Types")
    class DifferentTypesTests {

        @Test
        @DisplayName("Should work with integer elements")
        void testIntegerElements() {
            intMap.add(1, 3L);
            intMap.add(2, 5L);
            intMap.add(1, 2L);

            assertThat(intMap.getCount(1)).isEqualTo(5L);
            assertThat(intMap.getCount(2)).isEqualTo(5L);
            assertThat(intMap.getSum()).isEqualTo(10L);
        }

        @Test
        @DisplayName("Should work with custom objects")
        void testCustomObjects() {
            AccumulatorMapL<TestObject> objMap = new AccumulatorMapL<>();
            TestObject obj1 = new TestObject("test1");
            TestObject obj2 = new TestObject("test2");

            objMap.add(obj1, 3L);
            objMap.add(obj2, 7L);

            assertThat(objMap.getCount(obj1)).isEqualTo(3L);
            assertThat(objMap.getCount(obj2)).isEqualTo(7L);
            assertThat(objMap.getSum()).isEqualTo(10L);
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle very large numbers")
        void testLargeNumbers() {
            accMap.add("item1", Long.MAX_VALUE - 1);
            accMap.add("item1", 1L);

            assertThat(accMap.getCount("item1")).isEqualTo(Long.MAX_VALUE);
        }

        @Test
        @DisplayName("Should handle many different elements")
        void testManyElements() {
            for (int i = 0; i < 1000; i++) {
                accMap.add("item" + i, (long) i);
            }

            assertThat(accMap.getAccMap()).hasSize(1000);
            assertThat(accMap.getCount("item500")).isEqualTo(500L);

            // Sum should be 0 + 1 + 2 + ... + 999 = 999 * 1000 / 2
            long expectedSum = 999L * 1000L / 2L;
            assertThat(accMap.getSum()).isEqualTo(expectedSum);
        }
    }

    /**
     * Simple test object for testing custom types.
     */
    private static class TestObject {
        private final String value;

        public TestObject(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            TestObject that = (TestObject) o;
            return java.util.Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(value);
        }

        @Override
        public String toString() {
            return "TestObject{" + "value='" + value + '\'' + '}';
        }
    }
}
