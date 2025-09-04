package tdrc.utils;

import static org.assertj.core.api.Assertions.*;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive tests for {@link HashBag}.
 * 
 * @author Generated Tests
 */
@DisplayName("HashBag Tests")
class HashBagTest {

    private HashBag<String> hashBag;

    @BeforeEach
    void setUp() {
        hashBag = new HashBag<>();
    }

    @Nested
    @DisplayName("Construction Tests")
    class ConstructionTests {

        @Test
        @DisplayName("should create empty bag")
        void testConstructor_CreatesEmptyBag() {
            HashBag<String> bag = new HashBag<>();

            assertThat(bag.keySet()).isEmpty();
            assertThat(bag.getTotal()).isZero();
        }

        @Test
        @DisplayName("should handle different generic types")
        void testConstructor_WithDifferentTypes() {
            HashBag<Integer> intBag = new HashBag<>();
            HashBag<Object> objBag = new HashBag<>();

            assertThat(intBag.keySet()).isEmpty();
            assertThat(objBag.keySet()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Put Operations Tests")
    class PutOperationsTests {

        @Test
        @DisplayName("should add single element")
        void testPut_SingleElement_AddsElement() {
            int result = hashBag.put("test");

            assertThat(result).isEqualTo(1);
            assertThat(hashBag.get("test")).isEqualTo(1);
            assertThat(hashBag.keySet()).containsExactly("test");
        }

        @Test
        @DisplayName("should increment count for existing element")
        void testPut_ExistingElement_IncrementsCount() {
            hashBag.put("test");
            int result = hashBag.put("test");

            assertThat(result).isEqualTo(2);
            assertThat(hashBag.get("test")).isEqualTo(2);
        }

        @Test
        @DisplayName("should add multiple occurrences")
        void testPut_WithMultipleOccurrences_AddsCorrectCount() {
            int result = hashBag.put("test", 5);

            assertThat(result).isEqualTo(5);
            assertThat(hashBag.get("test")).isEqualTo(5);
        }

        @Test
        @DisplayName("should accumulate counts with multiple puts")
        void testPut_AccumulatesCounts() {
            hashBag.put("test", 3);
            int result = hashBag.put("test", 2);

            assertThat(result).isEqualTo(5);
            assertThat(hashBag.get("test")).isEqualTo(5);
        }

        @Test
        @DisplayName("should handle null elements")
        void testPut_WithNullElement_Works() {
            int result = hashBag.put(null);

            assertThat(result).isEqualTo(1);
            assertThat(hashBag.get(null)).isEqualTo(1);
            assertThat(hashBag.keySet()).containsExactly((String) null);
        }

        @Test
        @DisplayName("should handle zero occurrences")
        void testPut_WithZeroOccurrences_Works() {
            int result = hashBag.put("test", 0);

            assertThat(result).isZero();
            assertThat(hashBag.get("test")).isZero();
        }

        @Test
        @DisplayName("should handle negative occurrences")
        void testPut_WithNegativeOccurrences_Works() {
            hashBag.put("test", 5);
            int result = hashBag.put("test", -2);

            assertThat(result).isEqualTo(3);
            assertThat(hashBag.get("test")).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("Get Operations Tests")
    class GetOperationsTests {

        @Test
        @DisplayName("should return zero for non-existent element")
        void testGet_NonExistentElement_ReturnsZero() {
            int count = hashBag.get("nonexistent");

            assertThat(count).isZero();
        }

        @Test
        @DisplayName("should return correct count for existing element")
        void testGet_ExistingElement_ReturnsCorrectCount() {
            hashBag.put("test", 3);

            int count = hashBag.get("test");

            assertThat(count).isEqualTo(3);
        }

        @Test
        @DisplayName("should handle null element")
        void testGet_WithNullElement_Works() {
            hashBag.put(null, 2);

            int count = hashBag.get(null);

            assertThat(count).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("Take Operations Tests")
    class TakeOperationsTests {

        @Test
        @DisplayName("should remove single occurrence")
        void testTake_SingleOccurrence_RemovesOne() {
            hashBag.put("test", 3);

            int result = hashBag.take("test");

            assertThat(result).isEqualTo(2);
            assertThat(hashBag.get("test")).isEqualTo(2);
        }

        @Test
        @DisplayName("should remove multiple occurrences")
        void testTake_MultipleOccurrences_RemovesCorrectCount() {
            hashBag.put("test", 5);

            int result = hashBag.take("test", 3);

            assertThat(result).isEqualTo(2);
            assertThat(hashBag.get("test")).isEqualTo(2);
        }

        @Test
        @DisplayName("should remove element completely when count reaches zero")
        void testTake_CountReachesZero_RemovesElement() {
            hashBag.put("test", 2);

            int result = hashBag.take("test", 2);

            assertThat(result).isZero();
            assertThat(hashBag.get("test")).isZero();
            assertThat(hashBag.keySet()).doesNotContain("test");
        }

        @Test
        @DisplayName("should return zero when taking more than available")
        void testTake_MoreThanAvailable_ReturnsZero() {
            hashBag.put("test", 2);

            int result = hashBag.take("test", 5);

            assertThat(result).isZero();
            assertThat(hashBag.get("test")).isZero();
        }

        @Test
        @DisplayName("should return zero for non-existent element")
        void testTake_NonExistentElement_ReturnsZero() {
            int result = hashBag.take("nonexistent");

            assertThat(result).isZero();
        }

        @Test
        @DisplayName("should handle null element")
        void testTake_WithNullElement_Works() {
            hashBag.put(null, 3);

            int result = hashBag.take(null);

            assertThat(result).isEqualTo(2);
            assertThat(hashBag.get(null)).isEqualTo(2);
        }

        @Test
        @DisplayName("should handle zero take")
        void testTake_WithZero_NoChange() {
            hashBag.put("test", 3);

            int result = hashBag.take("test", 0);

            assertThat(result).isEqualTo(3);
            assertThat(hashBag.get("test")).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("Collection Operations Tests")
    class CollectionOperationsTests {

        @Test
        @DisplayName("should return correct key set")
        void testKeySet_ReturnsCorrectKeys() {
            hashBag.put("a");
            hashBag.put("b", 2);
            hashBag.put("c", 3);

            Set<String> keys = hashBag.keySet();

            assertThat(keys).containsExactlyInAnyOrder("a", "b", "c");
        }

        @Test
        @DisplayName("should return empty key set for empty bag")
        void testKeySet_EmptyBag_ReturnsEmptySet() {
            Set<String> keys = hashBag.keySet();

            assertThat(keys).isEmpty();
        }

        @Test
        @DisplayName("should return correct total count")
        void testGetTotal_ReturnsCorrectSum() {
            hashBag.put("a", 2);
            hashBag.put("b", 3);
            hashBag.put("c", 1);

            int total = hashBag.getTotal();

            assertThat(total).isEqualTo(6);
        }

        @Test
        @DisplayName("should return zero total for empty bag")
        void testGetTotal_EmptyBag_ReturnsZero() {
            int total = hashBag.getTotal();

            assertThat(total).isZero();
        }

        @Test
        @DisplayName("should return unmodifiable map view")
        void testToMap_ReturnsUnmodifiableMap() {
            hashBag.put("a", 2);
            hashBag.put("b", 3);

            Map<String, Integer> map = hashBag.toMap();

            assertThat(map).containsEntry("a", 2).containsEntry("b", 3);

            // Verify it's unmodifiable
            assertThatThrownBy(() -> map.put("c", 1))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @Nested
    @DisplayName("Clear and Reset Tests")
    class ClearResetTests {

        @Test
        @DisplayName("should clear all items")
        void testClear_RemovesAllItems() {
            hashBag.put("a", 2);
            hashBag.put("b", 3);

            hashBag.clear();

            assertThat(hashBag.keySet()).isEmpty();
            assertThat(hashBag.getTotal()).isZero();
            assertThat(hashBag.get("a")).isZero();
        }

        @Test
        @DisplayName("should reset counts to zero without removing items")
        void testReset_ResetsCountsToZero() {
            hashBag.put("a", 2);
            hashBag.put("b", 3);

            hashBag.reset();

            assertThat(hashBag.keySet()).containsExactlyInAnyOrder("a", "b");
            assertThat(hashBag.get("a")).isZero();
            assertThat(hashBag.get("b")).isZero();
            assertThat(hashBag.getTotal()).isZero();
        }
    }

    @Nested
    @DisplayName("String Representation Tests")
    class StringRepresentationTests {

        @Test
        @DisplayName("should return correct string representation")
        void testToString_ReturnsCorrectFormat() {
            hashBag.put("a", 2);
            hashBag.put("b", 1);

            String result = hashBag.toString();

            // Note: HashMap order is not guaranteed, so we check contains
            assertThat(result).startsWith("{");
            assertThat(result).endsWith("}");
            assertThat(result).contains("a=2");
            assertThat(result).contains("b=1");
        }

        @Test
        @DisplayName("should handle empty bag string representation")
        void testToString_EmptyBag_ReturnsEmptyBraces() {
            String result = hashBag.toString();

            assertThat(result).isEqualTo("{}");
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("should handle large counts")
        void testLargeCounts_HandlesCorrectly() {
            hashBag.put("test", Integer.MAX_VALUE - 1);
            int result = hashBag.put("test", 1);

            assertThat(result).isEqualTo(Integer.MAX_VALUE);
            assertThat(hashBag.get("test")).isEqualTo(Integer.MAX_VALUE);
        }

        @Test
        @DisplayName("should handle mixed operations")
        void testMixedOperations_WorkCorrectly() {
            // Complex sequence of operations
            hashBag.put("a", 5);
            hashBag.put("b", 3);
            hashBag.take("a", 2);
            hashBag.put("c", 1);
            hashBag.take("b", 3);

            assertThat(hashBag.get("a")).isEqualTo(3);
            assertThat(hashBag.get("b")).isZero();
            assertThat(hashBag.get("c")).isEqualTo(1);
            assertThat(hashBag.keySet()).containsExactlyInAnyOrder("a", "c");
            assertThat(hashBag.getTotal()).isEqualTo(4);
        }
    }
}
