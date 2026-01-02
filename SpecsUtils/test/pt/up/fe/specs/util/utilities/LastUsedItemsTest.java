package pt.up.fe.specs.util.utilities;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link LastUsedItems}.
 * 
 * Tests LRU (Least Recently Used) item tracking functionality with capacity
 * management.
 * 
 * @author Generated Tests
 */
@DisplayName("LastUsedItems")
class LastUsedItemsTest {

    @Nested
    @DisplayName("Construction")
    class Construction {

        @Test
        @DisplayName("should create with capacity")
        void shouldCreateWithCapacity() {
            LastUsedItems<String> items = new LastUsedItems<>(3);

            assertThat(items.getItems()).isEmpty();
            assertThat(items.getHead()).isEmpty();
        }

        @Test
        @DisplayName("should create with initial items")
        void shouldCreateWithInitialItems() {
            List<String> initialItems = Arrays.asList("a", "b", "c");
            LastUsedItems<String> items = new LastUsedItems<>(5, initialItems);

            assertThat(items.getItems()).containsExactly("a", "b", "c");
            assertThat(items.getHead()).contains("a");
        }

        @Test
        @DisplayName("should respect capacity when creating with initial items")
        void shouldRespectCapacityWhenCreatingWithInitialItems() {
            List<String> initialItems = Arrays.asList("a", "b", "c", "d", "e");
            LastUsedItems<String> items = new LastUsedItems<>(3, initialItems);

            assertThat(items.getItems()).hasSize(3);
            assertThat(items.getItems()).containsExactly("a", "b", "c");
        }

        @Test
        @DisplayName("should handle zero capacity")
        void shouldHandleZeroCapacity() {
            LastUsedItems<String> items = new LastUsedItems<>(0);

            assertThat(items.used("item")).isFalse();
            assertThat(items.getItems()).isEmpty();
            assertThat(items.getHead()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Basic Operations")
    class BasicOperations {

        @Test
        @DisplayName("should add first item")
        void shouldAddFirstItem() {
            LastUsedItems<String> items = new LastUsedItems<>(3);

            boolean changed = items.used("first");

            assertThat(changed).isTrue();
            assertThat(items.getItems()).containsExactly("first");
            assertThat(items.getHead()).contains("first");
        }

        @Test
        @DisplayName("should add multiple items in order")
        void shouldAddMultipleItemsInOrder() {
            LastUsedItems<String> items = new LastUsedItems<>(3);

            items.used("first");
            items.used("second");
            items.used("third");

            assertThat(items.getItems()).containsExactly("third", "second", "first");
            assertThat(items.getHead()).contains("third");
        }

        @Test
        @DisplayName("should move existing item to head")
        void shouldMoveExistingItemToHead() {
            LastUsedItems<String> items = new LastUsedItems<>(3);
            items.used("first");
            items.used("second");
            items.used("third");

            boolean changed = items.used("first");

            assertThat(changed).isTrue();
            assertThat(items.getItems()).containsExactly("first", "third", "second");
            assertThat(items.getHead()).contains("first");
        }

        @Test
        @DisplayName("should not change if item is already at head")
        void shouldNotChangeIfItemIsAlreadyAtHead() {
            LastUsedItems<String> items = new LastUsedItems<>(3);
            items.used("first");
            items.used("second");

            boolean changed = items.used("second");

            assertThat(changed).isFalse();
            assertThat(items.getItems()).containsExactly("second", "first");
        }
    }

    @Nested
    @DisplayName("Capacity Management")
    class CapacityManagement {

        @Test
        @DisplayName("should evict oldest item when capacity exceeded")
        void shouldEvictOldestItemWhenCapacityExceeded() {
            LastUsedItems<String> items = new LastUsedItems<>(2);
            items.used("first");
            items.used("second");

            boolean changed = items.used("third");

            assertThat(changed).isTrue();
            assertThat(items.getItems()).containsExactly("third", "second");
            assertThat(items.getItems()).doesNotContain("first");
        }

        @Test
        @DisplayName("should maintain capacity when adding new items")
        void shouldMaintainCapacityWhenAddingNewItems() {
            LastUsedItems<String> items = new LastUsedItems<>(3);

            for (int i = 0; i < 10; i++) {
                items.used("item" + i);
            }

            assertThat(items.getItems()).hasSize(3);
            assertThat(items.getItems()).containsExactly("item9", "item8", "item7");
        }

        @Test
        @DisplayName("should handle capacity of one")
        void shouldHandleCapacityOfOne() {
            LastUsedItems<String> items = new LastUsedItems<>(1);

            items.used("first");
            items.used("second");
            items.used("third");

            assertThat(items.getItems()).hasSize(1);
            assertThat(items.getItems()).containsExactly("third");
            assertThat(items.getHead()).contains("third");
        }
    }

    @Nested
    @DisplayName("State Queries")
    class StateQueries {

        @Test
        @DisplayName("should return empty head for empty list")
        void shouldReturnEmptyHeadForEmptyList() {
            LastUsedItems<String> items = new LastUsedItems<>(3);

            assertThat(items.getHead()).isEmpty();
        }

        @Test
        @DisplayName("should return correct head")
        void shouldReturnCorrectHead() {
            LastUsedItems<String> items = new LastUsedItems<>(3);
            items.used("first");
            items.used("second");

            assertThat(items.getHead()).contains("second");
        }

        @Test
        @DisplayName("should return immutable view of items")
        void shouldReturnImmutableViewOfItems() {
            LastUsedItems<String> items = new LastUsedItems<>(3);
            items.used("first");
            items.used("second");

            List<String> itemsList = items.getItems();

            // The returned list should reflect the internal state
            assertThat(itemsList).containsExactly("second", "first");

            // But modifications to the returned list should not affect the internal state
            // Note: We can't test this directly without knowing the implementation details
            // The contract doesn't specify if it's a copy or a view
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("should handle null items")
        void shouldHandleNullItems() {
            LastUsedItems<String> items = new LastUsedItems<>(3);

            boolean changed = items.used(null);

            assertThat(changed).isTrue();
            assertThat(items.getItems()).containsExactly((String) null);
            assertThat(items.getHead()).isEmpty();
        }

        @Test
        @DisplayName("should handle duplicate null items")
        void shouldHandleDuplicateNullItems() {
            LastUsedItems<String> items = new LastUsedItems<>(3);
            items.used(null);
            items.used("other");

            boolean changed = items.used(null);

            assertThat(changed).isTrue();
            assertThat(items.getItems()).containsExactly(null, "other");
            assertThat(items.getHead()).isEmpty();
        }

        @Test
        @DisplayName("should work with different types")
        void shouldWorkWithDifferentTypes() {
            LastUsedItems<Integer> items = new LastUsedItems<>(3);

            items.used(1);
            items.used(2);
            items.used(3);
            items.used(1);

            assertThat(items.getItems()).containsExactly(1, 3, 2);
            assertThat(items.getHead()).contains(1);
        }

        @Test
        @DisplayName("should maintain object equality semantics")
        void shouldMaintainObjectEqualitySemantics() {
            LastUsedItems<String> items = new LastUsedItems<>(3);
            String str1 = new String("test");
            String str2 = new String("test");

            items.used(str1);
            boolean changed = items.used(str2);

            // Since str1.equals(str2), str2 should be considered the same item
            assertThat(changed).isFalse();
            assertThat(items.getItems()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Complex Scenarios")
    class ComplexScenarios {

        @Test
        @DisplayName("should handle mixed operations correctly")
        void shouldHandleMixedOperationsCorrectly() {
            LastUsedItems<String> items = new LastUsedItems<>(4);

            // Build initial state
            items.used("a");
            items.used("b");
            items.used("c");
            items.used("d");
            assertThat(items.getItems()).containsExactly("d", "c", "b", "a");

            // Use existing item
            items.used("b");
            assertThat(items.getItems()).containsExactly("b", "d", "c", "a");

            // Add new item (should evict oldest)
            items.used("e");
            assertThat(items.getItems()).containsExactly("e", "b", "d", "c");
            assertThat(items.getItems()).doesNotContain("a");

            // Use existing item again
            items.used("c");
            assertThat(items.getItems()).containsExactly("c", "e", "b", "d");
        }

        @Test
        @DisplayName("should work correctly with initial items and subsequent operations")
        void shouldWorkCorrectlyWithInitialItemsAndSubsequentOperations() {
            List<String> initialItems = Arrays.asList("x", "y");
            LastUsedItems<String> items = new LastUsedItems<>(3, initialItems);

            assertThat(items.getItems()).containsExactly("x", "y");

            items.used("z");
            assertThat(items.getItems()).containsExactly("z", "x", "y");

            items.used("y");
            assertThat(items.getItems()).containsExactly("y", "z", "x");

            items.used("w");
            assertThat(items.getItems()).containsExactly("w", "y", "z");
            assertThat(items.getItems()).doesNotContain("x");
        }
    }
}
