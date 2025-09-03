package pt.up.fe.specs.util.collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive tests for AccumulatorMap - a counting/histogram utility that
 * tracks how many times items are added.
 * 
 * AccumulatorMap is a collection utility that maintains a count for each
 * element added to it, along with a total accumulator of all counts. It
 * supports operations like add, remove, set, and provides statistics like
 * ratios.
 * 
 * @author Generated Tests
 */
@DisplayName("AccumulatorMap")
class AccumulatorMapTest {

    private AccumulatorMap<String> accumulator;

    @BeforeEach
    void setUp() {
        accumulator = new AccumulatorMap<>();
    }

    @Nested
    @DisplayName("Constructor and Initialization")
    class ConstructorTests {

        @Test
        @DisplayName("should create empty accumulator with default constructor")
        void shouldCreateEmptyAccumulator() {
            var map = new AccumulatorMap<String>();

            assertThat(map.getSum()).isEqualTo(0L);
            assertThat(map.keys()).isEmpty();
            assertThat(map.getAccMap()).isEmpty();
        }

        @Test
        @DisplayName("should create copy with copy constructor")
        void shouldCreateCopyWithCopyConstructor() {
            accumulator.add("test");
            accumulator.add("test");
            accumulator.add("other", 3);

            var copy = new AccumulatorMap<>(accumulator);

            assertThat(copy.getSum()).isEqualTo(5L);
            assertThat(copy.getCount("test")).isEqualTo(2);
            assertThat(copy.getCount("other")).isEqualTo(3);
            assertThat(copy.keys()).containsExactlyInAnyOrder("test", "other");

            // Verify independence - changes to original don't affect copy
            accumulator.add("test");
            assertThat(copy.getCount("test")).isEqualTo(2);
        }

        @Test
        @DisplayName("should handle empty accumulator in copy constructor")
        void shouldHandleEmptyAccumulatorInCopyConstructor() {
            var copy = new AccumulatorMap<>(accumulator);

            assertThat(copy.getSum()).isEqualTo(0L);
            assertThat(copy.keys()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Add Operations")
    class AddOperationTests {

        @Test
        @DisplayName("should add single element")
        void shouldAddSingleElement() {
            Integer result = accumulator.add("test");

            assertThat(result).isEqualTo(1);
            assertThat(accumulator.getCount("test")).isEqualTo(1);
            assertThat(accumulator.getSum()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should increment count on repeated adds")
        void shouldIncrementCountOnRepeatedAdds() {
            Integer first = accumulator.add("test");
            Integer second = accumulator.add("test");
            Integer third = accumulator.add("test");

            assertThat(first).isEqualTo(1);
            assertThat(second).isEqualTo(2);
            assertThat(third).isEqualTo(3);
            assertThat(accumulator.getCount("test")).isEqualTo(3);
            assertThat(accumulator.getSum()).isEqualTo(3L);
        }

        @Test
        @DisplayName("should add with custom increment value")
        void shouldAddWithCustomIncrementValue() {
            Integer result = accumulator.add("test", 5);

            assertThat(result).isEqualTo(5);
            assertThat(accumulator.getCount("test")).isEqualTo(5);
            assertThat(accumulator.getSum()).isEqualTo(5L);
        }

        @Test
        @DisplayName("should accumulate custom increment values")
        void shouldAccumulateCustomIncrementValues() {
            accumulator.add("test", 3);
            Integer result = accumulator.add("test", 4);

            assertThat(result).isEqualTo(7);
            assertThat(accumulator.getCount("test")).isEqualTo(7);
            assertThat(accumulator.getSum()).isEqualTo(7L);
        }

        @Test
        @DisplayName("should handle multiple different elements")
        void shouldHandleMultipleDifferentElements() {
            accumulator.add("apple", 2);
            accumulator.add("banana", 3);
            accumulator.add("apple", 1);

            assertThat(accumulator.getCount("apple")).isEqualTo(3);
            assertThat(accumulator.getCount("banana")).isEqualTo(3);
            assertThat(accumulator.getSum()).isEqualTo(6L);
            assertThat(accumulator.keys()).containsExactlyInAnyOrder("apple", "banana");
        }

        @Test
        @DisplayName("should handle zero increment value")
        void shouldHandleZeroIncrementValue() {
            accumulator.add("test", 5);
            Integer result = accumulator.add("test", 0);

            assertThat(result).isEqualTo(5);
            assertThat(accumulator.getCount("test")).isEqualTo(5);
            assertThat(accumulator.getSum()).isEqualTo(5L);
        }

        @Test
        @DisplayName("should handle negative increment value")
        void shouldHandleNegativeIncrementValue() {
            accumulator.add("test", 10);
            Integer result = accumulator.add("test", -3);

            assertThat(result).isEqualTo(7);
            assertThat(accumulator.getCount("test")).isEqualTo(7);
            assertThat(accumulator.getSum()).isEqualTo(7L);
        }

        @Test
        @DisplayName("should handle null elements")
        void shouldHandleNullElements() {
            Integer result = accumulator.add(null);

            assertThat(result).isEqualTo(1);
            assertThat(accumulator.getCount(null)).isEqualTo(1);
            assertThat(accumulator.getSum()).isEqualTo(1L);
            assertThat(accumulator.keys()).contains((String) null);
        }
    }

    @Nested
    @DisplayName("Set Operations")
    class SetOperationTests {

        @Test
        @DisplayName("should set value for new element")
        void shouldSetValueForNewElement() {
            Integer previousValue = accumulator.set("test", 5);

            assertThat(previousValue).isEqualTo(0);
            assertThat(accumulator.getCount("test")).isEqualTo(5);
            assertThat(accumulator.getSum()).isEqualTo(5L);
        }

        @Test
        @DisplayName("should set value for existing element")
        void shouldSetValueForExistingElement() {
            accumulator.add("test", 3);
            Integer previousValue = accumulator.set("test", 7);

            assertThat(previousValue).isEqualTo(3);
            assertThat(accumulator.getCount("test")).isEqualTo(7);
            assertThat(accumulator.getSum()).isEqualTo(7L);
        }

        @Test
        @DisplayName("should adjust accumulator correctly when setting values")
        void shouldAdjustAccumulatorCorrectlyWhenSettingValues() {
            accumulator.add("apple", 5);
            accumulator.add("banana", 3);
            assertThat(accumulator.getSum()).isEqualTo(8L);

            accumulator.set("apple", 2); // -5 + 2 = -3
            assertThat(accumulator.getSum()).isEqualTo(5L);

            accumulator.set("banana", 8); // -3 + 8 = +5
            assertThat(accumulator.getSum()).isEqualTo(10L);
        }

        @Test
        @DisplayName("should handle setting zero value")
        void shouldHandleSettingZeroValue() {
            accumulator.add("test", 5);
            Integer previousValue = accumulator.set("test", 0);

            assertThat(previousValue).isEqualTo(5);
            assertThat(accumulator.getCount("test")).isEqualTo(0);
            assertThat(accumulator.getSum()).isEqualTo(0L);
            // Note: element still exists in map with count 0
            assertThat(accumulator.keys()).contains("test");
        }

        @Test
        @DisplayName("should handle setting negative value")
        void shouldHandleSettingNegativeValue() {
            accumulator.add("test", 5);
            Integer previousValue = accumulator.set("test", -3);

            assertThat(previousValue).isEqualTo(5);
            assertThat(accumulator.getCount("test")).isEqualTo(-3);
            assertThat(accumulator.getSum()).isEqualTo(-3L);
        }
    }

    @Nested
    @DisplayName("Remove Operations")
    class RemoveOperationTests {

        @Test
        @DisplayName("should remove single count from element")
        void shouldRemoveSingleCountFromElement() {
            accumulator.add("test", 5);
            boolean result = accumulator.remove("test");

            assertThat(result).isTrue();
            assertThat(accumulator.getCount("test")).isEqualTo(4);
            assertThat(accumulator.getSum()).isEqualTo(4L);
        }

        @Test
        @DisplayName("should remove custom count from element")
        void shouldRemoveCustomCountFromElement() {
            accumulator.add("test", 10);
            boolean result = accumulator.remove("test", 3);

            assertThat(result).isTrue();
            assertThat(accumulator.getCount("test")).isEqualTo(7);
            assertThat(accumulator.getSum()).isEqualTo(7L);
        }

        @Test
        @DisplayName("should remove element completely when count reaches zero")
        void shouldRemoveElementCompletelyWhenCountReachesZero() {
            accumulator.add("test", 5);
            boolean result = accumulator.remove("test", 5);

            assertThat(result).isTrue();
            assertThat(accumulator.getCount("test")).isEqualTo(0);
            assertThat(accumulator.getSum()).isEqualTo(0L);
            assertThat(accumulator.keys()).doesNotContain("test");
        }

        @Test
        @DisplayName("should handle removing more than current count")
        void shouldHandleRemovingMoreThanCurrentCount() {
            accumulator.add("test", 3);
            boolean result = accumulator.remove("test", 5);

            assertThat(result).isTrue();
            assertThat(accumulator.getCount("test")).isEqualTo(-2);
            assertThat(accumulator.getSum()).isEqualTo(-2L);
            // Element still exists with negative count
            assertThat(accumulator.keys()).contains("test");
        }

        @Test
        @DisplayName("should return false when removing non-existent element")
        void shouldReturnFalseWhenRemovingNonExistentElement() {
            boolean result = accumulator.remove("nonexistent");

            assertThat(result).isFalse();
            assertThat(accumulator.getSum()).isEqualTo(0L);
        }

        @Test
        @DisplayName("should return false when removing from non-existent element with custom count")
        void shouldReturnFalseWhenRemovingFromNonExistentElementWithCustomCount() {
            boolean result = accumulator.remove("nonexistent", 5);

            assertThat(result).isFalse();
            assertThat(accumulator.getSum()).isEqualTo(0L);
        }

        @Test
        @DisplayName("should handle removing zero count")
        void shouldHandleRemovingZeroCount() {
            accumulator.add("test", 5);
            boolean result = accumulator.remove("test", 0);

            assertThat(result).isTrue();
            assertThat(accumulator.getCount("test")).isEqualTo(5);
            assertThat(accumulator.getSum()).isEqualTo(5L);
        }

        @Test
        @DisplayName("should handle negative remove values")
        void shouldHandleNegativeRemoveValues() {
            accumulator.add("test", 5);
            boolean result = accumulator.remove("test", -3);

            assertThat(result).isTrue();
            assertThat(accumulator.getCount("test")).isEqualTo(8); // 5 - (-3) = 8
            assertThat(accumulator.getSum()).isEqualTo(8L);
        }
    }

    @Nested
    @DisplayName("Query Operations")
    class QueryOperationTests {

        @Test
        @DisplayName("should return zero count for non-existent element")
        void shouldReturnZeroCountForNonExistentElement() {
            int count = accumulator.getCount("nonexistent");

            assertThat(count).isEqualTo(0);
        }

        @Test
        @DisplayName("should return correct count for existing element")
        void shouldReturnCorrectCountForExistingElement() {
            accumulator.add("test", 7);

            int count = accumulator.getCount("test");

            assertThat(count).isEqualTo(7);
        }

        @Test
        @DisplayName("should calculate correct ratio for element")
        void shouldCalculateCorrectRatioForElement() {
            accumulator.add("apple", 6);
            accumulator.add("banana", 4);

            double appleRatio = accumulator.getRatio("apple");
            double bananaRatio = accumulator.getRatio("banana");

            assertThat(appleRatio).isEqualTo(0.6); // 6/10
            assertThat(bananaRatio).isEqualTo(0.4); // 4/10
        }

        @Test
        @DisplayName("should handle ratio calculation for zero total")
        void shouldHandleRatioCalculationForZeroTotal() {
            double ratio = accumulator.getRatio("test");

            assertThat(ratio).isNaN(); // 0/0 = NaN
        }

        @Test
        @DisplayName("should handle ratio calculation for non-existent element")
        void shouldHandleRatioCalculationForNonExistentElement() {
            accumulator.add("test", 5);

            double ratio = accumulator.getRatio("nonexistent");

            assertThat(ratio).isEqualTo(0.0); // 0/5
        }

        @Test
        @DisplayName("should return correct sum")
        void shouldReturnCorrectSum() {
            accumulator.add("a", 3);
            accumulator.add("b", 7);
            accumulator.add("c", 2);

            long sum = accumulator.getSum();

            assertThat(sum).isEqualTo(12L);
        }

        @Test
        @DisplayName("should return correct keys")
        void shouldReturnCorrectKeys() {
            accumulator.add("apple");
            accumulator.add("banana", 2);
            accumulator.add("cherry", 0);

            Set<String> keys = accumulator.keys();

            assertThat(keys).containsExactlyInAnyOrder("apple", "banana", "cherry");
        }

        @Test
        @DisplayName("should return unmodifiable map view")
        void shouldReturnUnmodifiableMapView() {
            accumulator.add("test", 5);

            Map<String, Integer> accMap = accumulator.getAccMap();

            assertThat(accMap).containsEntry("test", 5);
            assertThatThrownBy(() -> accMap.put("new", 1))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @Nested
    @DisplayName("Unmodifiable View")
    class UnmodifiableViewTests {

        @Test
        @DisplayName("should create unmodifiable view")
        void shouldCreateUnmodifiableView() {
            accumulator.add("test", 5);

            AccumulatorMap<String> unmodifiable = accumulator.getUnmodifiableMap();

            assertThat(unmodifiable.getCount("test")).isEqualTo(5);
            assertThat(unmodifiable.getSum()).isEqualTo(5L);
        }

        @Test
        @DisplayName("should throw exception when modifying unmodifiable view with add")
        void shouldThrowExceptionWhenModifyingUnmodifiableViewWithAdd() {
            AccumulatorMap<String> unmodifiable = accumulator.getUnmodifiableMap();

            assertThatThrownBy(() -> unmodifiable.add("test"))
                    .isInstanceOf(UnsupportedOperationException.class)
                    .hasMessageContaining("unmodifiable");
        }

        @Test
        @DisplayName("should throw exception when modifying unmodifiable view with add with value")
        void shouldThrowExceptionWhenModifyingUnmodifiableViewWithAddWithValue() {
            AccumulatorMap<String> unmodifiable = accumulator.getUnmodifiableMap();

            assertThatThrownBy(() -> unmodifiable.add("test", 5))
                    .isInstanceOf(UnsupportedOperationException.class)
                    .hasMessageContaining("unmodifiable");
        }

        @Test
        @DisplayName("should throw exception when modifying unmodifiable view with remove")
        void shouldThrowExceptionWhenModifyingUnmodifiableViewWithRemove() {
            accumulator.add("test", 5);
            AccumulatorMap<String> unmodifiable = accumulator.getUnmodifiableMap();

            assertThatThrownBy(() -> unmodifiable.remove("test"))
                    .isInstanceOf(UnsupportedOperationException.class)
                    .hasMessageContaining("unmodifiable");
        }

        @Test
        @DisplayName("should throw exception when modifying unmodifiable view with remove with value")
        void shouldThrowExceptionWhenModifyingUnmodifiableViewWithRemoveWithValue() {
            accumulator.add("test", 5);
            AccumulatorMap<String> unmodifiable = accumulator.getUnmodifiableMap();

            assertThatThrownBy(() -> unmodifiable.remove("test", 2))
                    .isInstanceOf(UnsupportedOperationException.class)
                    .hasMessageContaining("unmodifiable");
        }

        @Test
        @DisplayName("should reflect changes in unmodifiable view when original is modified")
        void shouldReflectChangesInUnmodifiableViewWhenOriginalIsModified() {
            accumulator.add("test", 5);
            AccumulatorMap<String> unmodifiable = accumulator.getUnmodifiableMap();

            accumulator.add("test", 3);

            // Unmodifiable view reflects changes to original (shares underlying map
            // reference)
            assertThat(unmodifiable.getCount("test")).isEqualTo(8);
            // Note: accumulator value is copied, not shared, so it shows original state
            assertThat(unmodifiable.getSum()).isEqualTo(5L);
        }
    }

    @Nested
    @DisplayName("Generic Type Support")
    class GenericTypeSupportTests {

        @Test
        @DisplayName("should work with integer keys")
        void shouldWorkWithIntegerKeys() {
            var intAccumulator = new AccumulatorMap<Integer>();

            intAccumulator.add(1, 5);
            intAccumulator.add(2, 3);
            intAccumulator.add(1, 2);

            assertThat(intAccumulator.getCount(1)).isEqualTo(7);
            assertThat(intAccumulator.getCount(2)).isEqualTo(3);
            assertThat(intAccumulator.getSum()).isEqualTo(10L);
        }

        @Test
        @DisplayName("should work with custom object keys")
        void shouldWorkWithCustomObjectKeys() {
            record TestRecord(String name, int id) {
            }

            var objectAccumulator = new AccumulatorMap<TestRecord>();
            var record1 = new TestRecord("test1", 1);
            var record2 = new TestRecord("test2", 2);

            objectAccumulator.add(record1, 3);
            objectAccumulator.add(record2, 4);
            objectAccumulator.add(record1, 2);

            assertThat(objectAccumulator.getCount(record1)).isEqualTo(5);
            assertThat(objectAccumulator.getCount(record2)).isEqualTo(4);
            assertThat(objectAccumulator.getSum()).isEqualTo(9L);
        }

        @Test
        @DisplayName("should handle enum keys")
        void shouldHandleEnumKeys() {
            enum TestEnum {
                OPTION1, OPTION2, OPTION3
            }

            var enumAccumulator = new AccumulatorMap<TestEnum>();

            enumAccumulator.add(TestEnum.OPTION1, 2);
            enumAccumulator.add(TestEnum.OPTION2, 5);
            enumAccumulator.add(TestEnum.OPTION1, 3);

            assertThat(enumAccumulator.getCount(TestEnum.OPTION1)).isEqualTo(5);
            assertThat(enumAccumulator.getCount(TestEnum.OPTION2)).isEqualTo(5);
            assertThat(enumAccumulator.getCount(TestEnum.OPTION3)).isEqualTo(0);
            assertThat(enumAccumulator.getSum()).isEqualTo(10L);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("should handle very large increment values")
        void shouldHandleVeryLargeIncrementValues() {
            int largeValue = Integer.MAX_VALUE - 1000;
            accumulator.add("test", largeValue);

            assertThat(accumulator.getCount("test")).isEqualTo(largeValue);
            assertThat(accumulator.getSum()).isEqualTo((long) largeValue);
        }

        @Test
        @DisplayName("should handle integer overflow in individual counts")
        void shouldHandleIntegerOverflowInIndividualCounts() {
            accumulator.add("test", Integer.MAX_VALUE);
            accumulator.add("test", 1); // This should overflow

            // The behavior depends on integer overflow behavior
            assertThat(accumulator.getCount("test")).isEqualTo(Integer.MIN_VALUE);
        }

        @Test
        @DisplayName("should handle accumulator overflow to long range")
        void shouldHandleAccumulatorOverflowToLongRange() {
            // Add values that exceed int range but fit in long
            accumulator.add("test1", Integer.MAX_VALUE);
            accumulator.add("test2", Integer.MAX_VALUE);

            long expectedSum = (long) Integer.MAX_VALUE + (long) Integer.MAX_VALUE;
            assertThat(accumulator.getSum()).isEqualTo(expectedSum);
        }

        @Test
        @DisplayName("should handle empty string keys")
        void shouldHandleEmptyStringKeys() {
            accumulator.add("", 5);

            assertThat(accumulator.getCount("")).isEqualTo(5);
            assertThat(accumulator.keys()).contains("");
        }

        @Test
        @DisplayName("should handle many different keys")
        void shouldHandleManyDifferentKeys() {
            for (int i = 0; i < 1000; i++) {
                accumulator.add("key" + i, i % 10 + 1);
            }

            assertThat(accumulator.keys()).hasSize(1000);
            assertThat(accumulator.getCount("key500")).isEqualTo(1); // 500 % 10 + 1 = 1
            assertThat(accumulator.getSum()).isGreaterThan(0L);
        }
    }

    @Nested
    @DisplayName("Equals and HashCode")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("should be equal to itself")
        void shouldBeEqualToItself() {
            accumulator.add("test", 5);

            assertThat(accumulator).isEqualTo(accumulator);
            assertThat(accumulator.hashCode()).isEqualTo(accumulator.hashCode());
        }

        @Test
        @DisplayName("should be equal to equivalent accumulator")
        void shouldBeEqualToEquivalentAccumulator() {
            accumulator.add("apple", 3);
            accumulator.add("banana", 2);

            var other = new AccumulatorMap<String>();
            other.add("apple", 3);
            other.add("banana", 2);

            assertThat(accumulator).isEqualTo(other);
            assertThat(accumulator.hashCode()).isEqualTo(other.hashCode());
        }

        @Test
        @DisplayName("should not be equal to accumulator with different counts")
        void shouldNotBeEqualToAccumulatorWithDifferentCounts() {
            accumulator.add("test", 5);

            var other = new AccumulatorMap<String>();
            other.add("test", 3);

            assertThat(accumulator).isNotEqualTo(other);
        }

        @Test
        @DisplayName("should not be equal to accumulator with different keys")
        void shouldNotBeEqualToAccumulatorWithDifferentKeys() {
            accumulator.add("apple", 5);

            var other = new AccumulatorMap<String>();
            other.add("banana", 5);

            assertThat(accumulator).isNotEqualTo(other);
        }

        @Test
        @DisplayName("should not be equal to accumulator with different accumulator values")
        void shouldNotBeEqualToAccumulatorWithDifferentAccumulatorValues() {
            accumulator.add("test", 5);

            var other = new AccumulatorMap<String>();
            other.add("test", 3);
            other.add("other", 2);

            assertThat(accumulator).isNotEqualTo(other);
        }

        @Test
        @DisplayName("should not be equal to null")
        void shouldNotBeEqualToNull() {
            assertThat(accumulator).isNotEqualTo(null);
        }

        @Test
        @DisplayName("should not be equal to different type")
        void shouldNotBeEqualToDifferentType() {
            assertThat(accumulator).isNotEqualTo("string");
        }

        @Test
        @DisplayName("should handle equals with same content added in different order")
        void shouldHandleEqualsWithSameContentAddedInDifferentOrder() {
            accumulator.add("a", 2);
            accumulator.add("b", 3);
            accumulator.add("a", 1);

            var other = new AccumulatorMap<String>();
            other.add("b", 3);
            other.add("a", 3);

            assertThat(accumulator).isEqualTo(other);
        }
    }

    @Nested
    @DisplayName("ToString")
    class ToStringTests {

        @Test
        @DisplayName("should return string representation")
        void shouldReturnStringRepresentation() {
            accumulator.add("apple", 3);
            accumulator.add("banana", 2);

            String result = accumulator.toString();

            assertThat(result).contains("apple");
            assertThat(result).contains("banana");
            assertThat(result).contains("3");
            assertThat(result).contains("2");
        }

        @Test
        @DisplayName("should handle empty accumulator in toString")
        void shouldHandleEmptyAccumulatorInToString() {
            String result = accumulator.toString();

            assertThat(result).isEqualTo("{}");
        }

        @Test
        @DisplayName("should handle single element in toString")
        void shouldHandleSingleElementInToString() {
            accumulator.add("test", 5);

            String result = accumulator.toString();

            assertThat(result).contains("test");
            assertThat(result).contains("5");
        }
    }

    @Nested
    @DisplayName("Real-world Usage Scenarios")
    class RealWorldUsageTests {

        @Test
        @DisplayName("should work as word frequency counter")
        void shouldWorkAsWordFrequencyCounter() {
            String text = "the quick brown fox jumps over the lazy dog the fox";
            String[] words = text.split(" ");

            var wordCount = new AccumulatorMap<String>();
            for (String word : words) {
                wordCount.add(word);
            }

            assertThat(wordCount.getCount("the")).isEqualTo(3);
            assertThat(wordCount.getCount("fox")).isEqualTo(2);
            assertThat(wordCount.getCount("quick")).isEqualTo(1);
            assertThat(wordCount.getSum()).isEqualTo(words.length);
        }

        @Test
        @DisplayName("should work as event counter with different weights")
        void shouldWorkAsEventCounterWithDifferentWeights() {
            var eventCounter = new AccumulatorMap<String>();

            // Simulate different event severities
            eventCounter.add("info", 1); // Low weight
            eventCounter.add("warning", 3); // Medium weight
            eventCounter.add("error", 10); // High weight
            eventCounter.add("info", 1);
            eventCounter.add("error", 10);

            assertThat(eventCounter.getCount("info")).isEqualTo(2);
            assertThat(eventCounter.getCount("warning")).isEqualTo(3);
            assertThat(eventCounter.getCount("error")).isEqualTo(20);
            assertThat(eventCounter.getSum()).isEqualTo(25L);

            // Check ratios
            assertThat(eventCounter.getRatio("error")).isEqualTo(0.8); // 20/25
            assertThat(eventCounter.getRatio("warning")).isEqualTo(0.12); // 3/25
        }

        @Test
        @DisplayName("should work as inventory tracking system")
        void shouldWorkAsInventoryTrackingSystem() {
            var inventory = new AccumulatorMap<String>();

            // Initial stock
            inventory.add("apples", 100);
            inventory.add("bananas", 75);
            inventory.add("oranges", 50);

            // Sales (negative additions)
            inventory.add("apples", -20);
            inventory.add("bananas", -15);

            // New shipment
            inventory.add("apples", 30);

            assertThat(inventory.getCount("apples")).isEqualTo(110); // 100 - 20 + 30
            assertThat(inventory.getCount("bananas")).isEqualTo(60); // 75 - 15
            assertThat(inventory.getCount("oranges")).isEqualTo(50); // unchanged
            assertThat(inventory.getSum()).isEqualTo(220L);
        }

        @Test
        @DisplayName("should work as histogram for data analysis")
        void shouldWorkAsHistogramForDataAnalysis() {
            var histogram = new AccumulatorMap<String>();

            // Simulate grade distribution
            String[] grades = { "A", "B", "B", "C", "A", "B", "D", "C", "A", "B", "C" };
            for (String grade : grades) {
                histogram.add(grade);
            }

            assertThat(histogram.getCount("A")).isEqualTo(3);
            assertThat(histogram.getCount("B")).isEqualTo(4);
            assertThat(histogram.getCount("C")).isEqualTo(3);
            assertThat(histogram.getCount("D")).isEqualTo(1);

            // Verify percentages
            double totalGrades = histogram.getSum();
            assertThat(histogram.getRatio("A")).isEqualTo(3.0 / totalGrades);
            assertThat(histogram.getRatio("B")).isEqualTo(4.0 / totalGrades);
        }

        @Test
        @DisplayName("should support statistical analysis operations")
        void shouldSupportStatisticalAnalysisOperations() {
            var stats = new AccumulatorMap<String>();

            stats.add("category1", 10);
            stats.add("category2", 20);
            stats.add("category3", 30);
            stats.add("category4", 40);

            // Calculate statistics
            long total = stats.getSum();
            assertThat(total).isEqualTo(100L);

            // Find most frequent category
            String mostFrequent = stats.keys().stream()
                    .max((a, b) -> Integer.compare(stats.getCount(a), stats.getCount(b)))
                    .orElse(null);
            assertThat(mostFrequent).isEqualTo("category4");

            // Calculate cumulative percentages
            double cumulative = 0;
            for (String category : stats.keys()) {
                cumulative += stats.getRatio(category);
            }
            assertThat(cumulative).isEqualTo(1.0, within(0.0001));
        }
    }
}
