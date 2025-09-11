package tdrc.utils;

import static org.assertj.core.api.Assertions.*;

import java.util.TreeMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive tests for {@link RangeUtils}.
 * 
 * @author Generated Tests
 */
@DisplayName("RangeUtils Tests")
class RangeUtilsTest {

    private TreeMap<Double, Character> charMap;
    private TreeMap<Integer, String> stringMap;

    @BeforeEach
    void setUp() {
        charMap = new TreeMap<>();
        stringMap = new TreeMap<>();
    }

    @Nested
    @DisplayName("Basic Range Query Tests")
    class BasicRangeQueryTests {

        @Test
        @DisplayName("should return exact match when key exists")
        void testGetValueByRangedKey_ExactMatch_ReturnsValue() {
            charMap.put(1.0, 'A');
            charMap.put(2.0, 'B');
            charMap.put(3.0, 'C');

            Character result = RangeUtils.getValueByRangedKey(charMap, 2.0);

            assertThat(result).isEqualTo('B');
        }

        @Test
        @DisplayName("should return floor value when exact key doesn't exist")
        void testGetValueByRangedKey_NoExactMatch_ReturnsFloorValue() {
            charMap.put(1.0, 'A');
            charMap.put(3.0, 'B');
            charMap.put(5.0, 'C');

            Character result = RangeUtils.getValueByRangedKey(charMap, 4.0);

            assertThat(result).isEqualTo('B');
        }

        @Test
        @DisplayName("should return null when no floor entry exists")
        void testGetValueByRangedKey_NoFloorEntry_ReturnsNull() {
            charMap.put(5.0, 'A');
            charMap.put(10.0, 'B');

            Character result = RangeUtils.getValueByRangedKey(charMap, 3.0);

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("should return null for empty map")
        void testGetValueByRangedKey_EmptyMap_ReturnsNull() {
            Character result = RangeUtils.getValueByRangedKey(charMap, 5.0);

            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("Null Value Handling Tests")
    class NullValueHandlingTests {

        @Test
        @DisplayName("should skip null floor entry and find next lower value")
        void testGetValueByRangedKey_SkipsNullFloorEntry_FindsLowerValue() {
            // Following the example in the class documentation
            charMap.put(1.0, 'A');
            charMap.put(2.9, null);
            charMap.put(4.0, 'B');
            charMap.put(6.0, null);
            charMap.put(6.5, 'C');
            charMap.put(10.0, null);

            Character result = RangeUtils.getValueByRangedKey(charMap, 5.0);

            assertThat(result).isEqualTo('B');
        }

        @Test
        @DisplayName("should skip null and return previous non-null value")
        void testGetValueByRangedKey_ExactMatchWithNull_ReturnsNull() {
            charMap.put(1.0, 'A');
            charMap.put(2.0, null);
            charMap.put(3.0, 'C');

            Character result = RangeUtils.getValueByRangedKey(charMap, 2.0);

            assertThat(result).isEqualTo('A');
        }

        @Test
        @DisplayName("should return null when all lower entries have null values")
        void testGetValueByRangedKey_AllLowerEntriesNull_ReturnsNull() {
            charMap.put(1.0, null);
            charMap.put(2.0, null);
            charMap.put(5.0, 'A');

            Character result = RangeUtils.getValueByRangedKey(charMap, 3.0);

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("should skip multiple null entries to find valid value")
        void testGetValueByRangedKey_SkipsMultipleNulls_FindsValidValue() {
            charMap.put(1.0, 'A');
            charMap.put(2.0, null);
            charMap.put(3.0, null);
            charMap.put(4.0, null);
            charMap.put(5.0, 'B');

            Character result = RangeUtils.getValueByRangedKey(charMap, 4.5);

            assertThat(result).isEqualTo('A');
        }
    }

    @Nested
    @DisplayName("Different Data Types Tests")
    class DifferentDataTypesTests {

        @Test
        @DisplayName("should work with Integer keys and String values")
        void testGetValueByRangedKey_IntegerString_WorksCorrectly() {
            stringMap.put(10, "ten");
            stringMap.put(20, "twenty");
            stringMap.put(30, "thirty");

            String result = RangeUtils.getValueByRangedKey(stringMap, 25);

            assertThat(result).isEqualTo("twenty");
        }

        @Test
        @DisplayName("should work with String keys")
        void testGetValueByRangedKey_StringKeys_WorksCorrectly() {
            TreeMap<String, Integer> map = new TreeMap<>();
            map.put("apple", 1);
            map.put("banana", 2);
            map.put("cherry", 3);

            Integer result = RangeUtils.getValueByRangedKey(map, "ball");

            assertThat(result).isEqualTo(1); // "apple" is the floor entry for "ball"
        }

        @Test
        @DisplayName("should handle null values with different types")
        void testGetValueByRangedKey_NullValuesWithDifferentTypes_HandlesCorrectly() {
            stringMap.put(1, "one");
            stringMap.put(5, null);
            stringMap.put(10, "ten");

            String result = RangeUtils.getValueByRangedKey(stringMap, 7);

            assertThat(result).isEqualTo("one");
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("should handle single entry map")
        void testGetValueByRangedKey_SingleEntry_WorksCorrectly() {
            charMap.put(5.0, 'X');

            Character result1 = RangeUtils.getValueByRangedKey(charMap, 5.0);
            Character result2 = RangeUtils.getValueByRangedKey(charMap, 10.0);
            Character result3 = RangeUtils.getValueByRangedKey(charMap, 3.0);

            assertThat(result1).isEqualTo('X');
            assertThat(result2).isEqualTo('X');
            assertThat(result3).isNull();
        }

        @Test
        @DisplayName("should handle single null entry")
        void testGetValueByRangedKey_SingleNullEntry_ReturnsNull() {
            charMap.put(5.0, null);

            Character result1 = RangeUtils.getValueByRangedKey(charMap, 5.0);
            Character result2 = RangeUtils.getValueByRangedKey(charMap, 10.0);

            assertThat(result1).isNull();
            assertThat(result2).isNull();
        }

        @Test
        @DisplayName("should handle very large numbers")
        void testGetValueByRangedKey_LargeNumbers_WorksCorrectly() {
            TreeMap<Double, String> largeMap = new TreeMap<>();
            largeMap.put(1000000.0, "million");
            largeMap.put(2000000.0, "two million");

            String result = RangeUtils.getValueByRangedKey(largeMap, 1500000.0);

            assertThat(result).isEqualTo("million");
        }

        @Test
        @DisplayName("should handle negative numbers")
        void testGetValueByRangedKey_NegativeNumbers_WorksCorrectly() {
            TreeMap<Double, String> negMap = new TreeMap<>();
            negMap.put(-10.0, "negative ten");
            negMap.put(-5.0, "negative five");
            negMap.put(0.0, "zero");
            negMap.put(5.0, "positive five");

            String result1 = RangeUtils.getValueByRangedKey(negMap, -7.0);
            String result2 = RangeUtils.getValueByRangedKey(negMap, 3.0);
            String result3 = RangeUtils.getValueByRangedKey(negMap, -15.0);

            assertThat(result1).isEqualTo("negative ten");
            assertThat(result2).isEqualTo("zero");
            assertThat(result3).isNull();
        }
    }

    @Nested
    @DisplayName("Complex Scenario Tests")
    class ComplexScenarioTests {

        @Test
        @DisplayName("should handle complex mixed scenario from documentation example")
        void testGetValueByRangedKey_DocumentationExample_WorksCorrectly() {
            // Exact example from the class documentation
            TreeMap<Double, Character> m = new TreeMap<>();
            m.put(1.0, 'A');
            m.put(2.9, null);
            m.put(4.0, 'B');
            m.put(6.0, null);
            m.put(6.5, 'C');
            m.put(10.0, null);

            // Test all scenarios - nulls should be skipped
            assertThat(RangeUtils.getValueByRangedKey(m, 5.0)).isEqualTo('B');
            assertThat(RangeUtils.getValueByRangedKey(m, 1.0)).isEqualTo('A');
            assertThat(RangeUtils.getValueByRangedKey(m, 2.9)).isEqualTo('A'); // Should skip null and find 'A'
            assertThat(RangeUtils.getValueByRangedKey(m, 6.5)).isEqualTo('C');
            assertThat(RangeUtils.getValueByRangedKey(m, 15.0)).isEqualTo('C'); // Should skip null at 10.0 and find 'C'
            assertThat(RangeUtils.getValueByRangedKey(m, 0.5)).isNull(); // Less than 1.0
        }

        @Test
        @DisplayName("should handle alternating null and non-null values")
        void testGetValueByRangedKey_AlternatingNullNonNull_HandlesCorrectly() {
            charMap.put(1.0, 'A');
            charMap.put(2.0, null);
            charMap.put(3.0, 'C');
            charMap.put(4.0, null);
            charMap.put(5.0, 'E');
            charMap.put(6.0, null);

            assertThat(RangeUtils.getValueByRangedKey(charMap, 1.5)).isEqualTo('A');
            assertThat(RangeUtils.getValueByRangedKey(charMap, 2.5)).isEqualTo('A');
            assertThat(RangeUtils.getValueByRangedKey(charMap, 3.5)).isEqualTo('C');
            assertThat(RangeUtils.getValueByRangedKey(charMap, 4.5)).isEqualTo('C');
            assertThat(RangeUtils.getValueByRangedKey(charMap, 5.5)).isEqualTo('E');
            assertThat(RangeUtils.getValueByRangedKey(charMap, 7.0)).isEqualTo('E');
        }

        @Test
        @DisplayName("should work with custom comparable objects")
        void testGetValueByRangedKey_CustomComparableObjects_WorksCorrectly() {
            TreeMap<Integer, CustomValue> customMap = new TreeMap<>();
            customMap.put(1, new CustomValue("first"));
            customMap.put(5, new CustomValue("second"));
            customMap.put(10, null);
            customMap.put(15, new CustomValue("third"));

            CustomValue result1 = RangeUtils.getValueByRangedKey(customMap, 3);
            CustomValue result2 = RangeUtils.getValueByRangedKey(customMap, 12);

            assertThat(result1.getValue()).isEqualTo("first");
            assertThat(result2.getValue()).isEqualTo("second");
        }
    }

    /**
     * Helper class for testing with custom objects.
     */
    private static class CustomValue {
        private final String value;

        public CustomValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
