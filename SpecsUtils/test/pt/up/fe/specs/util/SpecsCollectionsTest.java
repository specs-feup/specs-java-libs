package pt.up.fe.specs.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import pt.up.fe.specs.util.collections.SpecsList;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for SpecsCollections utility class.
 * Tests core collection utility methods including list manipulation, type
 * casting, map operations, filtering, and advanced collection operations.
 * 
 * @author Generated Tests
 */
@DisplayName("SpecsCollections Tests")
class SpecsCollectionsTest {

    @Nested
    @DisplayName("List Manipulation Operations")
    class ListManipulationTests {

        @Test
        @DisplayName("subList should return elements from start index to end")
        void testSubList() {
            // Arrange
            List<String> list = Arrays.asList("a", "b", "c", "d", "e");

            // Execute
            List<String> result = SpecsCollections.subList(list, 2);

            // Verify
            assertThat(result).containsExactly("c", "d", "e");
            assertThat(result).hasSize(3);
        }

        @Test
        @DisplayName("subList should handle edge cases")
        void testSubListEdgeCases() {
            // Empty list
            List<String> empty = Collections.emptyList();
            assertThat(SpecsCollections.subList(empty, 0)).isEmpty();

            // Start at last element
            List<String> list = Arrays.asList("a", "b", "c");
            assertThat(SpecsCollections.subList(list, 2)).containsExactly("c");

            // Start at end
            assertThat(SpecsCollections.subList(list, 3)).isEmpty();
        }

        @Test
        @DisplayName("last should return the last element")
        void testLast() {
            // Arrange
            List<String> list = Arrays.asList("first", "middle", "last");

            // Execute
            String result = SpecsCollections.last(list);

            // Verify
            assertThat(result).isEqualTo("last");
        }

        @Test
        @DisplayName("last should return null for empty list (actual behavior)")
        void testLastEmpty() {
            // Arrange
            List<String> empty = Collections.emptyList();

            // Execute
            String result = SpecsCollections.last(empty);

            // Verify - returns null, not throwing exception
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("lastTry should return Optional of last element")
        void testLastTry() {
            // Non-empty list
            List<String> list = Arrays.asList("first", "middle", "last");
            Optional<String> result = SpecsCollections.lastTry(list);
            assertThat(result).isPresent().contains("last");

            // Empty list
            List<String> empty = Collections.emptyList();
            assertThat(SpecsCollections.lastTry(empty)).isEmpty();
        }
    }

    @Nested
    @DisplayName("Collection Creation")
    class CollectionCreationTests {

        @Test
        @DisplayName("asSet should create set from varargs")
        void testAsSet() {
            // Execute
            Set<String> result = SpecsCollections.asSet("a", "b", "c", "a");

            // Verify - duplicates should be removed
            assertThat(result).containsExactlyInAnyOrder("a", "b", "c");
            assertThat(result).hasSize(3);
        }

        @Test
        @DisplayName("asSet should handle empty varargs")
        void testAsSetEmpty() {
            // Execute
            Set<String> result = SpecsCollections.asSet();

            // Verify
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("List Removal Operations")
    class RemovalOperationsTests {

        @Test
        @DisplayName("remove should return removed elements by index range")
        void testRemoveByRange() {
            // Arrange
            List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e"));

            // Execute - remove from index 1 to 3 (exclusive), returns removed elements
            List<String> removed = SpecsCollections.remove(list, 1, 3);

            // Verify - method returns the removed elements in reverse order
            assertThat(removed).containsExactly("c", "b");
            // And the original list has those elements removed
            assertThat(list).containsExactly("a", "d", "e");
        }

        @Test
        @DisplayName("remove should return removed elements from start index to end")
        void testRemoveFromIndex() {
            // Arrange
            List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e"));

            // Execute - returns removed elements in reverse order
            List<String> removed = SpecsCollections.remove(list, 2);

            // Verify - removed elements are returned in reverse order
            assertThat(removed).containsExactly("e", "d", "c");
            // Original list keeps only first elements
            assertThat(list).containsExactly("a", "b");
        }

        @Test
        @DisplayName("remove should return removed elements by specific indexes")
        void testRemoveByIndexes() {
            // Arrange
            List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e"));
            List<Integer> indexes = Arrays.asList(1, 3); // Remove "b" and "d"

            // Execute - returns removed elements in reverse order of removal
            List<String> removed = SpecsCollections.remove(list, indexes);

            // Verify - removed elements returned in reverse order
            assertThat(removed).containsExactly("d", "b");
            // Original list has those elements removed
            assertThat(list).containsExactly("a", "c", "e");
        }

        @Test
        @DisplayName("remove should return removed elements matching predicate")
        void testRemoveByPredicate() {
            // Arrange
            List<String> list = new ArrayList<>(Arrays.asList("apple", "banana", "apricot", "cherry"));
            Predicate<String> startsWithA = s -> s.startsWith("a");

            // Execute - returns removed elements
            List<String> removed = SpecsCollections.remove(list, startsWithA);

            // Verify - returns the removed elements
            assertThat(removed).containsExactly("apple", "apricot");
            // Original list keeps non-matching elements
            assertThat(list).containsExactly("banana", "cherry");
        }

        @Test
        @DisplayName("removeLast should remove and return last element")
        void testRemoveLast() {
            // Arrange
            List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));

            // Execute
            String removed = SpecsCollections.removeLast(list);

            // Verify
            assertThat(removed).isEqualTo("c");
            assertThat(list).containsExactly("a", "b");
        }
    }

    @Nested
    @DisplayName("Concatenation Operations")
    class ConcatenationTests {

        @Test
        @DisplayName("concat should concatenate collection and element")
        void testConcatCollectionElement() {
            // Arrange
            Collection<String> collection = Arrays.asList("a", "b");

            // Execute
            SpecsList<String> result = SpecsCollections.concat(collection, "c");

            // Verify
            assertThat(result).containsExactly("a", "b", "c");
        }

        @Test
        @DisplayName("concat should concatenate element and collection")
        void testConcatElementCollection() {
            // Arrange
            Collection<String> collection = Arrays.asList("b", "c");

            // Execute
            SpecsList<String> result = SpecsCollections.concat("a", collection);

            // Verify
            assertThat(result).containsExactly("a", "b", "c");
        }

        @Test
        @DisplayName("concat should concatenate two collections")
        void testConcatTwoCollections() {
            // Arrange
            Collection<String> col1 = Arrays.asList("a", "b");
            Collection<String> col2 = Arrays.asList("c", "d");

            // Execute
            SpecsList<String> result = SpecsCollections.concat(col1, col2);

            // Verify
            assertThat(result).containsExactly("a", "b", "c", "d");
        }

        @Test
        @DisplayName("concatLists should concatenate multiple collections")
        void testConcatLists() {
            // Arrange
            Collection<String> col1 = Arrays.asList("a", "b");
            Collection<String> col2 = Arrays.asList("c");
            Collection<String> col3 = Arrays.asList("d", "e");

            // Execute
            List<String> result = SpecsCollections.concatLists(col1, col2, col3);

            // Verify
            assertThat(result).containsExactly("a", "b", "c", "d", "e");
        }
    }

    @Nested
    @DisplayName("Functional Operations")
    class FunctionalOperationsTests {

        @Test
        @DisplayName("map should transform collection elements")
        void testMap() {
            // Arrange
            Collection<String> strings = Arrays.asList("apple", "banana", "cherry");
            Function<String, Integer> lengthMapper = String::length;

            // Execute
            List<Integer> lengths = SpecsCollections.map(strings, lengthMapper);

            // Verify
            assertThat(lengths).containsExactly(5, 6, 6);
        }

        @Test
        @DisplayName("map should handle empty collection")
        void testMapEmpty() {
            // Arrange
            Collection<String> empty = Collections.emptyList();
            Function<String, Integer> lengthMapper = String::length;

            // Execute
            List<Integer> result = SpecsCollections.map(empty, lengthMapper);

            // Verify
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("filter should remove duplicates based on mapping function")
        void testFilter() {
            // Arrange
            Collection<String> strings = Arrays.asList("apple", "apricot", "banana", "blueberry");
            Function<String, Character> firstCharMapper = s -> s.charAt(0);

            // Execute
            List<String> filtered = SpecsCollections.filter(strings, firstCharMapper);

            // Verify - should keep first occurrence of each first character
            assertThat(filtered).containsExactly("apple", "banana");
        }

        @Test
        @DisplayName("toStream should convert Optional to Stream")
        void testToStream() {
            // Present Optional
            Optional<String> present = Optional.of("value");
            Stream<String> presentStream = SpecsCollections.toStream(present);
            assertThat(presentStream).containsExactly("value");

            // Empty Optional
            Optional<String> empty = Optional.empty();
            Stream<String> emptyStream = SpecsCollections.toStream(empty);
            assertThat(emptyStream).isEmpty();
        }
    }

    @Nested
    @DisplayName("Casting Operations")
    class CastingOperationsTests {

        @Test
        @DisplayName("cast should create SpecsList with type checking")
        void testCast() {
            // Arrange
            List<Object> mixed = Arrays.asList("a", "b", "c");

            // Execute
            SpecsList<String> casted = SpecsCollections.cast(mixed, String.class);

            // Verify
            assertThat(casted).containsExactly("a", "b", "c");
            assertThat(casted).isInstanceOf(SpecsList.class);
        }

        @Test
        @DisplayName("castUnchecked should perform unchecked cast")
        void testCastUnchecked() {
            // Arrange
            List<Object> objects = Arrays.asList("a", "b", "c");

            // Execute
            List<String> casted = SpecsCollections.castUnchecked(objects, String.class);

            // Verify
            assertThat(casted).containsExactly("a", "b", "c");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("operations should handle null inputs gracefully")
        void testNullHandling() {
            // Most operations should handle null collections/elements appropriately
            assertThatCode(() -> SpecsCollections.map(Collections.emptyList(), null))
                    .isInstanceOf(Exception.class);
        }

        @Test
        @DisplayName("operations should preserve collection immutability when possible")
        void testImmutabilityPreservation() {
            // Arrange
            List<String> original = Arrays.asList("a", "b", "c");

            // Execute operations that should not modify original
            SpecsCollections.subList(original, 1);
            SpecsCollections.map(original, String::toUpperCase);

            // Verify original unchanged
            assertThat(original).containsExactly("a", "b", "c");
        }
    }
}
