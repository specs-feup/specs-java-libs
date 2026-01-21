package tdrc.utils;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
}
