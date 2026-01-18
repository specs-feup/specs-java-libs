package pt.up.fe.specs.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for SpecsCheck utility class.
 * Tests runtime validation methods including argument checks, null checks,
 * size validations, and type checking functionality.
 * 
 * @author Generated Tests
 */
@DisplayName("SpecsCheck Tests")
class SpecsCheckTest {

    @Nested
    @DisplayName("Argument Validation")
    class ArgumentValidationTests {

        @Test
        @DisplayName("checkArgument should pass for true expression")
        void testCheckArgumentTrue() {
            // Arrange
            Supplier<String> errorMessage = () -> "This should not be called";

            // Execute & Verify - should not throw
            assertThatCode(() -> SpecsCheck.checkArgument(true, errorMessage))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("checkArgument should throw IllegalArgumentException for false expression")
        void testCheckArgumentFalse() {
            // Arrange
            String expectedMessage = "Argument validation failed";
            Supplier<String> errorMessage = () -> expectedMessage;

            // Execute & Verify
            assertThatThrownBy(() -> SpecsCheck.checkArgument(false, errorMessage))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(expectedMessage);
        }

        @Test
        @DisplayName("checkArgument should use supplier for error message")
        void testCheckArgumentLazyEvaluation() {
            // Arrange
            boolean[] supplierCalled = { false };
            Supplier<String> errorMessage = () -> {
                supplierCalled[0] = true;
                return "Error occurred";
            };

            // Execute - true condition should not call supplier
            SpecsCheck.checkArgument(true, errorMessage);

            // Verify supplier was not called
            assertThat(supplierCalled[0]).isFalse();

            // Execute - false condition should call supplier
            assertThatThrownBy(() -> SpecsCheck.checkArgument(false, errorMessage))
                    .isInstanceOf(IllegalArgumentException.class);

            // Verify supplier was called
            assertThat(supplierCalled[0]).isTrue();
        }
    }

    @Nested
    @DisplayName("Null Checks")
    class NullCheckTests {

        @Test
        @DisplayName("checkNotNull should return non-null reference")
        void testCheckNotNullValid() {
            // Arrange
            String value = "not null";
            Supplier<String> errorMessage = () -> "Should not be called";

            // Execute
            @SuppressWarnings("deprecation")
            String result = SpecsCheck.checkNotNull(value, errorMessage);

            // Verify
            assertThat(result).isSameAs(value);
        }

        @SuppressWarnings("deprecation")
        @Test
        @DisplayName("checkNotNull should throw NullPointerException for null reference")
        void testCheckNotNullWithNull() {
            // Arrange
            String expectedMessage = "Value cannot be null";
            Supplier<String> errorMessage = () -> expectedMessage;

            // Execute & Verify
            assertThatThrownBy(() -> SpecsCheck.checkNotNull(null, errorMessage))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(expectedMessage);
        }

        @Test
        @DisplayName("checkNotNull should preserve object type")
        void testCheckNotNullTypePreservation() {
            // Arrange
            List<String> list = Arrays.asList("a", "b", "c");
            Supplier<String> errorMessage = () -> "List is null";

            // Execute
            @SuppressWarnings("deprecation")
            List<String> result = SpecsCheck.checkNotNull(list, errorMessage);

            // Verify type and content preservation
            assertThat(result).isSameAs(list);
            assertThat(result).containsExactly("a", "b", "c");
        }
    }

    @Nested
    @DisplayName("Collection Size Validation")
    class CollectionSizeTests {

        @Test
        @DisplayName("checkSize should pass for collection with correct size")
        void testCheckSizeCollectionValid() {
            // Arrange
            Collection<String> collection = Arrays.asList("a", "b", "c");

            // Execute & Verify - should not throw
            assertThatCode(() -> SpecsCheck.checkSize(collection, 3))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("checkSize should throw for collection with incorrect size")
        void testCheckSizeCollectionInvalid() {
            // Arrange
            Collection<String> collection = Arrays.asList("a", "b");

            // Execute & Verify
            assertThatThrownBy(() -> SpecsCheck.checkSize(collection, 3))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Expected collection to have size '3'")
                    .hasMessageContaining("its current size is '2'");
        }

        @Test
        @DisplayName("checkSize should handle empty collections")
        void testCheckSizeEmptyCollection() {
            // Arrange
            Collection<String> empty = Collections.emptyList();

            // Execute & Verify - correct size
            assertThatCode(() -> SpecsCheck.checkSize(empty, 0))
                    .doesNotThrowAnyException();

            // Execute & Verify - incorrect size
            assertThatThrownBy(() -> SpecsCheck.checkSize(empty, 1))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Array Size Validation")
    class ArraySizeTests {

        @Test
        @DisplayName("checkSize should pass for array with correct size")
        void testCheckSizeArrayValid() {
            // Arrange
            String[] array = { "a", "b", "c" };

            // Execute & Verify - should not throw
            assertThatCode(() -> SpecsCheck.checkSize(array, 3))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("checkSize should throw for array with incorrect size")
        void testCheckSizeArrayInvalid() {
            // Arrange
            String[] array = { "a", "b" };

            // Execute & Verify
            assertThatThrownBy(() -> SpecsCheck.checkSize(array, 3))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Expected collection to have size '3'")
                    .hasMessageContaining("its current size is '2'");
        }

        @Test
        @DisplayName("checkSize should handle empty arrays")
        void testCheckSizeEmptyArray() {
            // Arrange
            String[] empty = new String[0];

            // Execute & Verify - correct size
            assertThatCode(() -> SpecsCheck.checkSize(empty, 0))
                    .doesNotThrowAnyException();

            // Execute & Verify - incorrect size
            assertThatThrownBy(() -> SpecsCheck.checkSize(empty, 1))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Collection Size Range Validation")
    class CollectionSizeRangeTests {

        @ParameterizedTest
        @ValueSource(ints = { 1, 2, 3 })
        @DisplayName("checkSizeRange should pass for collection within range")
        void testCheckSizeRangeCollectionValid(int size) {
            // Arrange
            Collection<String> collection = createCollectionOfSize(size);

            // Execute & Verify - should not throw
            assertThatCode(() -> SpecsCheck.checkSizeRange(collection, 1, 3))
                    .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @ValueSource(ints = { 0, 4, 5 })
        @DisplayName("checkSizeRange should throw for collection outside range")
        void testCheckSizeRangeCollectionInvalid(int size) {
            // Arrange
            Collection<String> collection = createCollectionOfSize(size);

            // Execute & Verify
            assertThatThrownBy(() -> SpecsCheck.checkSizeRange(collection, 1, 3))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Expected collection to have size between '1' and '3'")
                    .hasMessageContaining("its current size is '" + size + "'");
        }

        @Test
        @DisplayName("checkSizeRange should handle edge cases")
        void testCheckSizeRangeEdgeCases() {
            // Single element range
            Collection<String> single = Arrays.asList("a");
            assertThatCode(() -> SpecsCheck.checkSizeRange(single, 1, 1))
                    .doesNotThrowAnyException();

            // Zero minimum
            Collection<String> empty = Collections.emptyList();
            assertThatCode(() -> SpecsCheck.checkSizeRange(empty, 0, 2))
                    .doesNotThrowAnyException();
        }

        private Collection<String> createCollectionOfSize(int size) {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                list.add("element" + i);
            }
            return list;
        }
    }

    @Nested
    @DisplayName("Array Size Range Validation")
    class ArraySizeRangeTests {

        @ParameterizedTest
        @ValueSource(ints = { 1, 2, 3 })
        @DisplayName("checkSizeRange should pass for array within range")
        void testCheckSizeRangeArrayValid(int size) {
            // Arrange
            String[] array = createArrayOfSize(size);

            // Execute & Verify - should not throw
            assertThatCode(() -> SpecsCheck.checkSizeRange(array, 1, 3))
                    .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @ValueSource(ints = { 0, 4, 5 })
        @DisplayName("checkSizeRange should throw for array outside range")
        void testCheckSizeRangeArrayInvalid(int size) {
            // Arrange
            String[] array = createArrayOfSize(size);

            // Execute & Verify
            assertThatThrownBy(() -> SpecsCheck.checkSizeRange(array, 1, 3))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Expected collection to have size between '1' and '3'")
                    .hasMessageContaining("its current size is '" + size + "'");
        }

        private String[] createArrayOfSize(int size) {
            String[] array = new String[size];
            for (int i = 0; i < size; i++) {
                array[i] = "element" + i;
            }
            return array;
        }
    }

    @Nested
    @DisplayName("Type Validation")
    class TypeValidationTests {

        @Test
        @DisplayName("checkClass should pass for correct type")
        void testCheckClassValid() {
            // Arrange
            String value = "test string";

            // Execute & Verify - should not throw
            assertThatCode(() -> SpecsCheck.checkClass(value, String.class))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("checkClass should throw for incorrect type")
        void testCheckClassInvalid() {
            // Arrange
            Integer value = 42;

            // Execute & Verify
            assertThatThrownBy(() -> SpecsCheck.checkClass(value, String.class))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Expected value to be an instance of class java.lang.String")
                    .hasMessageContaining("however it is a class java.lang.Integer");
        }

        @Test
        @DisplayName("checkClass should work with inheritance")
        void testCheckClassInheritance() {
            // Arrange
            ArrayList<String> value = new ArrayList<>();

            // Execute & Verify - ArrayList is instance of List
            assertThatCode(() -> SpecsCheck.checkClass(value, List.class))
                    .doesNotThrowAnyException();

            // Execute & Verify - ArrayList is instance of Collection
            assertThatCode(() -> SpecsCheck.checkClass(value, Collection.class))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("checkClass should handle null values")
        void testCheckClassWithNull() {
            // Execute & Verify - null causes NullPointerException during error message
            // construction
            assertThatThrownBy(() -> SpecsCheck.checkClass(null, String.class))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("Cannot invoke \"Object.getClass()\"");
        }
    }

    @Nested
    @DisplayName("Error Message Generation")
    class ErrorMessageTests {

        @Test
        @DisplayName("size validation should include collection contents in error message")
        void testSizeErrorMessageContainsContent() {
            // Arrange
            Collection<String> collection = Arrays.asList("apple", "banana");

            // Execute & Verify
            assertThatThrownBy(() -> SpecsCheck.checkSize(collection, 3))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[apple, banana]");
        }

        @Test
        @DisplayName("array size validation should include array contents in error message")
        void testArraySizeErrorMessageContainsContent() {
            // Arrange
            String[] array = { "x", "y" };

            // Execute & Verify
            assertThatThrownBy(() -> SpecsCheck.checkSize(array, 3))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[x, y]");
        }

        @Test
        @DisplayName("size range validation should include bounds in error message")
        void testSizeRangeErrorMessageIncludesBounds() {
            // Arrange
            Collection<String> collection = Arrays.asList("a", "b", "c", "d", "e");

            // Execute & Verify
            assertThatThrownBy(() -> SpecsCheck.checkSizeRange(collection, 1, 3))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("between '1' and '3'")
                    .hasMessageContaining("current size is '5'");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Special Scenarios")
    class EdgeCasesTests {

        @Test
        @DisplayName("methods should handle large collections efficiently")
        void testLargeCollections() {
            // Arrange - create large collection
            Collection<Integer> large = new ArrayList<>();
            for (int i = 0; i < 10000; i++) {
                large.add(i);
            }

            // Execute & Verify - should handle efficiently
            assertThatCode(() -> SpecsCheck.checkSize(large, 10000))
                    .doesNotThrowAnyException();

            assertThatCode(() -> SpecsCheck.checkSizeRange(large, 5000, 15000))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("methods should work with different collection types")
        void testDifferentCollectionTypes() {
            // Test with Set
            Set<String> set = new HashSet<>(Arrays.asList("a", "b", "c"));
            assertThatCode(() -> SpecsCheck.checkSize(set, 3))
                    .doesNotThrowAnyException();

            // Test with Queue
            Queue<String> queue = new LinkedList<>(Arrays.asList("x", "y"));
            assertThatCode(() -> SpecsCheck.checkSizeRange(queue, 1, 3))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("type checking should work with primitives and objects")
        void testTypeCheckingVariousTypes() {
            // Primitive wrapper
            assertThatCode(() -> SpecsCheck.checkClass(42, Integer.class))
                    .doesNotThrowAnyException();

            // Custom object
            Object customObj = new Object();
            assertThatCode(() -> SpecsCheck.checkClass(customObj, Object.class))
                    .doesNotThrowAnyException();
        }
    }
}
