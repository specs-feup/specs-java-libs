package tdrc.utils;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import tdrc.tuple.Triple;
import tdrc.tuple.TupleList;

/**
 * Comprehensive tests for {@link ListUtils}.
 * 
 * @author Generated Tests
 */
@DisplayName("ListUtils Tests")
class ListUtilsTest {

    @Nested
    @DisplayName("Create Tuples from Arrays Tests")
    class CreateTuplesFromArraysTests {

        @Test
        @DisplayName("should create tuples from two arrays")
        void testCreateTuples_FromTwoArrays_CreatesCorrectTuples() {
            String[] arr1 = { "a", "b" };
            String[] arr2 = { "1", "2" };

            TupleList<String> result = ListUtils.createTuples(arr1, arr2);

            assertThat(result).hasSize(4);
            // Check that all combinations are present
            assertThat((List<String>) result.get(0)).containsExactly("a", "1");
            assertThat((List<String>) result.get(1)).containsExactly("a", "2");
            assertThat((List<String>) result.get(2)).containsExactly("b", "1");
            assertThat((List<String>) result.get(3)).containsExactly("b", "2");
        }

        @Test
        @DisplayName("should create tuples from three arrays")
        void testCreateTuples_FromThreeArrays_CreatesCorrectTuples() {
            Integer[] arr1 = { 1, 2 };
            Integer[] arr2 = { 10, 20 };
            Integer[] arr3 = { 100 };

            TupleList<Integer> result = ListUtils.createTuples(arr1, arr2, arr3);

            assertThat(result).hasSize(4); // 2 * 2 * 1
            assertThat((List<Integer>) result.get(0)).containsExactly(1, 10, 100);
            assertThat((List<Integer>) result.get(1)).containsExactly(1, 20, 100);
            assertThat((List<Integer>) result.get(2)).containsExactly(2, 10, 100);
            assertThat((List<Integer>) result.get(3)).containsExactly(2, 20, 100);
        }

        @Test
        @DisplayName("should handle empty arrays")
        void testCreateTuples_WithEmptyArray_ReturnsEmptyTuples() {
            String[] arr1 = { "a", "b" };
            String[] arr2 = {};

            TupleList<String> result = ListUtils.createTuples(arr1, arr2);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("should handle single element arrays")
        void testCreateTuples_WithSingleElements_CreatesSingleTuple() {
            String[] arr1 = { "a" };
            String[] arr2 = { "1" };

            TupleList<String> result = ListUtils.createTuples(arr1, arr2);

            assertThat(result).hasSize(1);
            assertThat((List<String>) result.get(0)).containsExactly("a", "1");
        }

        @Test
        @DisplayName("should handle null elements in arrays")
        void testCreateTuples_WithNullElements_IncludesNulls() {
            String[] arr1 = { "a", null };
            String[] arr2 = { "1", null };

            TupleList<String> result = ListUtils.createTuples(arr1, arr2);

            assertThat(result).hasSize(4);
            assertThat((List<String>) result.get(0)).containsExactly("a", "1");
            assertThat((List<String>) result.get(1)).containsExactly("a", null);
            assertThat((List<String>) result.get(2)).containsExactly(null, "1");
            assertThat((List<String>) result.get(3)).containsExactly(null, null);
        }
    }

    @Nested
    @DisplayName("Create Tuples from List of Arrays Tests")
    class CreateTuplesFromListOfArraysTests {

        @Test
        @DisplayName("should create tuples from list of arrays")
        void testCreateTuples_FromListOfArrays_CreatesCorrectTuples() {
            String[] arr1 = { "a", "b" };
            String[] arr2 = { "1", "2" };
            List<String[]> arrays = Arrays.asList(arr1, arr2);

            TupleList<String> result = ListUtils.createTuples(arrays);

            assertThat(result).hasSize(4);
            assertThat((List<String>) result.get(0)).containsExactly("a", "1");
            assertThat((List<String>) result.get(1)).containsExactly("a", "2");
            assertThat((List<String>) result.get(2)).containsExactly("b", "1");
            assertThat((List<String>) result.get(3)).containsExactly("b", "2");
        }

        @Test
        @DisplayName("should handle empty list")
        void testCreateTuples_WithEmptyList_ReturnsEmptyTuples() {
            List<String[]> arrays = Collections.emptyList();

            TupleList<String> result = ListUtils.createTuples(arrays);

            // Empty list case generates a single empty tuple rather than no tuples
            assertThat(result).hasSize(1);
            assertThat((List<String>) result.get(0)).isEmpty();
        }
    }

    @Nested
    @DisplayName("Create Tuples from Lists Tests")
    class CreateTuplesFromListsTests {

        @Test
        @DisplayName("should create tuples from two lists")
        void testCreateTuplesFromList_FromTwoLists_CreatesCorrectTuples() {
            List<String> list1 = Arrays.asList("a", "b");
            List<String> list2 = Arrays.asList("1", "2");

            TupleList<String> result = ListUtils.createTuplesFromList(list1, list2);

            assertThat(result).hasSize(4);
            assertThat((List<String>) result.get(0)).containsExactly("a", "1");
            assertThat((List<String>) result.get(1)).containsExactly("a", "2");
            assertThat((List<String>) result.get(2)).containsExactly("b", "1");
            assertThat((List<String>) result.get(3)).containsExactly("b", "2");
        }

        @Test
        @DisplayName("should create tuples from list of lists")
        void testCreateTuplesFromList_FromListOfLists_CreatesCorrectTuples() {
            List<String> list1 = Arrays.asList("a", "b");
            List<String> list2 = Arrays.asList("1", "2");
            List<List<String>> lists = Arrays.asList(list1, list2);

            TupleList<String> result = ListUtils.createTuplesFromList(lists);

            assertThat(result).hasSize(4);
            assertThat((List<String>) result.get(0)).containsExactly("a", "1");
            assertThat((List<String>) result.get(1)).containsExactly("a", "2");
            assertThat((List<String>) result.get(2)).containsExactly("b", "1");
            assertThat((List<String>) result.get(3)).containsExactly("b", "2");
        }

        @Test
        @DisplayName("should handle empty lists")
        void testCreateTuplesFromList_WithEmptyList_ReturnsEmptyTuples() {
            List<String> list1 = Arrays.asList("a", "b");
            List<String> list2 = Collections.emptyList();

            TupleList<String> result = ListUtils.createTuplesFromList(list1, list2);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("should handle null elements in lists")
        void testCreateTuplesFromList_WithNullElements_IncludesNulls() {
            List<String> list1 = Arrays.asList("a", null);
            List<String> list2 = Arrays.asList("1", null);

            TupleList<String> result = ListUtils.createTuplesFromList(list1, list2);

            assertThat(result).hasSize(4);
            assertThat((List<String>) result.get(0)).containsExactly("a", "1");
            assertThat((List<String>) result.get(1)).containsExactly("a", null);
            assertThat((List<String>) result.get(2)).containsExactly(null, "1");
            assertThat((List<String>) result.get(3)).containsExactly(null, null);
        }
    }

    @Nested
    @DisplayName("Create Pairs Tests")
    class CreatePairsTests {

        @Test
        @DisplayName("should create pairs from two arrays")
        void testCreatePairs_FromArrays_CreatesCorrectPairs() {
            String[] left = { "a", "b" };
            Integer[] right = { 1, 2 };

            List<Pair<String, Integer>> result = ListUtils.createPairs(left, right);

            assertThat(result).hasSize(4);
            assertThat(result.get(0)).isEqualTo(new Pair<>("a", 1));
            assertThat(result.get(1)).isEqualTo(new Pair<>("a", 2));
            assertThat(result.get(2)).isEqualTo(new Pair<>("b", 1));
            assertThat(result.get(3)).isEqualTo(new Pair<>("b", 2));
        }

        @Test
        @DisplayName("should create pairs from two lists")
        void testCreatePairs_FromLists_CreatesCorrectPairs() {
            List<String> left = Arrays.asList("a", "b");
            List<Integer> right = Arrays.asList(1, 2);

            List<Pair<String, Integer>> result = ListUtils.createPairs(left, right);

            assertThat(result).hasSize(4);
            assertThat(result.get(0)).isEqualTo(new Pair<>("a", 1));
            assertThat(result.get(1)).isEqualTo(new Pair<>("a", 2));
            assertThat(result.get(2)).isEqualTo(new Pair<>("b", 1));
            assertThat(result.get(3)).isEqualTo(new Pair<>("b", 2));
        }

        @Test
        @DisplayName("should handle empty arrays")
        void testCreatePairs_WithEmptyArray_ReturnsEmptyList() {
            String[] left = { "a", "b" };
            Integer[] right = {};

            List<Pair<String, Integer>> result = ListUtils.createPairs(left, right);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("should handle empty lists")
        void testCreatePairs_WithEmptyList_ReturnsEmptyList() {
            List<String> left = Arrays.asList("a", "b");
            List<Integer> right = Collections.emptyList();

            List<Pair<String, Integer>> result = ListUtils.createPairs(left, right);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("should handle single elements")
        void testCreatePairs_WithSingleElements_CreatesSinglePair() {
            String[] left = { "a" };
            Integer[] right = { 1 };

            List<Pair<String, Integer>> result = ListUtils.createPairs(left, right);

            assertThat(result).hasSize(1);
            assertThat(result.get(0)).isEqualTo(new Pair<>("a", 1));
        }

        @Test
        @DisplayName("should handle null elements")
        void testCreatePairs_WithNullElements_IncludesNulls() {
            String[] left = { "a", null };
            Integer[] right = { 1, null };

            List<Pair<String, Integer>> result = ListUtils.createPairs(left, right);

            assertThat(result).hasSize(4);
            assertThat(result.get(0)).isEqualTo(new Pair<>("a", 1));
            assertThat(result.get(1)).isEqualTo(new Pair<>("a", null));
            assertThat(result.get(2)).isEqualTo(new Pair<>(null, 1));
            assertThat(result.get(3)).isEqualTo(new Pair<>(null, null));
        }
    }

    @Nested
    @DisplayName("Create Triples Tests")
    class CreateTriplesTests {

        @Test
        @DisplayName("should create triples from three arrays")
        void testCreateTriples_FromArrays_CreatesCorrectTriples() {
            String[] xs = { "a", "b" };
            Integer[] ys = { 1, 2 };
            Boolean[] zs = { true };

            List<Triple<String, Integer, Boolean>> result = ListUtils.createTriples(xs, ys, zs);

            assertThat(result).hasSize(4); // 2 * 2 * 1

            // Check individual values since Triple doesn't override equals
            assertThat(result.get(0).getX()).isEqualTo("a");
            assertThat(result.get(0).getY()).isEqualTo(1);
            assertThat(result.get(0).getZ()).isEqualTo(true);

            assertThat(result.get(1).getX()).isEqualTo("a");
            assertThat(result.get(1).getY()).isEqualTo(2);
            assertThat(result.get(1).getZ()).isEqualTo(true);

            assertThat(result.get(2).getX()).isEqualTo("b");
            assertThat(result.get(2).getY()).isEqualTo(1);
            assertThat(result.get(2).getZ()).isEqualTo(true);

            assertThat(result.get(3).getX()).isEqualTo("b");
            assertThat(result.get(3).getY()).isEqualTo(2);
            assertThat(result.get(3).getZ()).isEqualTo(true);
        }

        @Test
        @DisplayName("should create triples from three lists")
        void testCreateTriples_FromLists_CreatesCorrectTriples() {
            List<String> xs = Arrays.asList("a", "b");
            List<Integer> ys = Arrays.asList(1, 2);
            List<Boolean> zs = Arrays.asList(true);

            List<Triple<String, Integer, Boolean>> result = ListUtils.createTriples(xs, ys, zs);

            assertThat(result).hasSize(4); // 2 * 2 * 1
            assertThat(result.get(0).getX()).isEqualTo("a");
            assertThat(result.get(0).getY()).isEqualTo(1);
            assertThat(result.get(0).getZ()).isEqualTo(true);

            assertThat(result.get(1).getX()).isEqualTo("a");
            assertThat(result.get(1).getY()).isEqualTo(2);
            assertThat(result.get(1).getZ()).isEqualTo(true);

            assertThat(result.get(2).getX()).isEqualTo("b");
            assertThat(result.get(2).getY()).isEqualTo(1);
            assertThat(result.get(2).getZ()).isEqualTo(true);

            assertThat(result.get(3).getX()).isEqualTo("b");
            assertThat(result.get(3).getY()).isEqualTo(2);
            assertThat(result.get(3).getZ()).isEqualTo(true);
        }

        @Test
        @DisplayName("should handle empty arrays")
        void testCreateTriples_WithEmptyArray_ReturnsEmptyList() {
            String[] xs = { "a", "b" };
            Integer[] ys = { 1, 2 };
            Boolean[] zs = {};

            List<Triple<String, Integer, Boolean>> result = ListUtils.createTriples(xs, ys, zs);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("should handle empty lists")
        void testCreateTriples_WithEmptyList_ReturnsEmptyList() {
            List<String> xs = Arrays.asList("a", "b");
            List<Integer> ys = Arrays.asList(1, 2);
            List<Boolean> zs = Collections.emptyList();

            List<Triple<String, Integer, Boolean>> result = ListUtils.createTriples(xs, ys, zs);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("should handle single elements")
        void testCreateTriples_WithSingleElements_CreatesSingleTriple() {
            String[] xs = { "a" };
            Integer[] ys = { 1 };
            Boolean[] zs = { true };

            List<Triple<String, Integer, Boolean>> result = ListUtils.createTriples(xs, ys, zs);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getX()).isEqualTo("a");
            assertThat(result.get(0).getY()).isEqualTo(1);
            assertThat(result.get(0).getZ()).isEqualTo(true);
        }

        @Test
        @DisplayName("should handle null elements")
        void testCreateTriples_WithNullElements_IncludesNulls() {
            String[] xs = { "a", null };
            Integer[] ys = { 1 };
            Boolean[] zs = { null };

            List<Triple<String, Integer, Boolean>> result = ListUtils.createTriples(xs, ys, zs);

            assertThat(result).hasSize(2); // 2 * 1 * 1
            assertThat(result.get(0).getX()).isEqualTo("a");
            assertThat(result.get(0).getY()).isEqualTo(1);
            assertThat(result.get(0).getZ()).isNull();

            assertThat(result.get(1).getX()).isNull();
            assertThat(result.get(1).getY()).isEqualTo(1);
            assertThat(result.get(1).getZ()).isNull();
        }

        @Test
        @DisplayName("should handle large combinations")
        void testCreateTriples_WithLargeArrays_CreatesAllCombinations() {
            String[] xs = { "a", "b", "c" };
            Integer[] ys = { 1, 2, 3 };
            Boolean[] zs = { true, false };

            List<Triple<String, Integer, Boolean>> result = ListUtils.createTriples(xs, ys, zs);

            assertThat(result).hasSize(18); // 3 * 3 * 2

            // Test first few combinations to verify correctness
            assertThat(result.get(0).getX()).isEqualTo("a");
            assertThat(result.get(0).getY()).isEqualTo(1);
            assertThat(result.get(0).getZ()).isEqualTo(true);

            assertThat(result.get(1).getX()).isEqualTo("a");
            assertThat(result.get(1).getY()).isEqualTo(1);
            assertThat(result.get(1).getZ()).isEqualTo(false);

            assertThat(result.get(2).getX()).isEqualTo("a");
            assertThat(result.get(2).getY()).isEqualTo(2);
            assertThat(result.get(2).getZ()).isEqualTo(true);

            assertThat(result.get(3).getX()).isEqualTo("a");
            assertThat(result.get(3).getY()).isEqualTo(2);
            assertThat(result.get(3).getZ()).isEqualTo(false);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("should handle different types in combinations")
        void testMixedTypes_HandlesCorrectly() {
            String[] strings = { "hello" };
            Integer[] numbers = { 42 };

            List<Pair<String, Integer>> pairs = ListUtils.createPairs(strings, numbers);

            assertThat(pairs).hasSize(1);
            assertThat(pairs.get(0)).isEqualTo(new Pair<>("hello", 42));
        }

        @Test
        @DisplayName("should handle large input sizes")
        void testLargeInputs_WorksCorrectly() {
            String[] left = new String[10];
            Integer[] right = new Integer[10];

            for (int i = 0; i < 10; i++) {
                left[i] = "item" + i;
                right[i] = i;
            }

            List<Pair<String, Integer>> result = ListUtils.createPairs(left, right);

            assertThat(result).hasSize(100); // 10 * 10
            assertThat(result.get(0)).isEqualTo(new Pair<>("item0", 0));
            assertThat(result.get(99)).isEqualTo(new Pair<>("item9", 9));
        }
    }
}
