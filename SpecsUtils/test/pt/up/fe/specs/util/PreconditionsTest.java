package pt.up.fe.specs.util;

import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Comprehensive test suite for {@link Preconditions} utility class.
 * Tests validation methods for null checks, argument validation, and state
 * checks.
 * 
 * @author Generated Tests
 */
@DisplayName("Preconditions Utility Tests")
class PreconditionsTest {

    @Nested
    @DisplayName("Null Check Tests")
    class NullCheckTests {

        @Test
        @DisplayName("checkNotNull should return non-null object")
        void testCheckNotNullValid() {
            // Setup
            String validString = "test";
            List<String> validList = Arrays.asList("a", "b", "c");

            // Execute & Verify
            assertThat(Preconditions.checkNotNull(validString)).isEqualTo(validString);
            assertThat(Preconditions.checkNotNull(validList)).isEqualTo(validList);
        }

        @Test
        @DisplayName("checkNotNull should throw NPE for null input")
        void testCheckNotNullWithNull() {
            assertThatThrownBy(() -> Preconditions.checkNotNull(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("checkNotNull with message should throw NPE with custom message")
        void testCheckNotNullWithCustomMessage() {
            String message = "Object cannot be null";

            assertThatThrownBy(() -> Preconditions.checkNotNull(null, message))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(message);
        }

        @Test
        @DisplayName("checkNotNull with object message should use toString")
        void testCheckNotNullWithObjectMessage() {
            Supplier<String> messageSupplier = () -> "Custom null message";

            assertThatThrownBy(() -> Preconditions.checkNotNull(null, messageSupplier))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("Lambda"); // toString() of lambda
        }

        @Test
        @DisplayName("checkNotNull should handle empty collections")
        void testCheckNotNullWithEmptyCollection() {
            List<String> emptyList = new ArrayList<>();

            assertThat(Preconditions.checkNotNull(emptyList)).isEqualTo(emptyList);
        }
    }

    @Nested
    @DisplayName("Argument Validation Tests")
    class ArgumentValidationTests {

        @Test
        @DisplayName("checkArgument should pass for true condition")
        void testCheckArgumentTrue() {
            // Execute & Verify - should not throw
            assertThatCode(() -> {
                Preconditions.checkArgument(true);
                Preconditions.checkArgument(5 > 3);
                Preconditions.checkArgument("test".length() == 4);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("checkArgument should throw IAE for false condition")
        void testCheckArgumentFalse() {
            assertThatThrownBy(() -> Preconditions.checkArgument(false))
                    .isInstanceOf(IllegalArgumentException.class);

            assertThatThrownBy(() -> Preconditions.checkArgument(5 < 3))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("checkArgument with message should throw IAE with custom message")
        void testCheckArgumentWithMessage() {
            String message = "Invalid argument provided";

            assertThatThrownBy(() -> Preconditions.checkArgument(false, message))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(message);
        }

        @Test
        @DisplayName("checkArgument with formatted message should handle %s placeholders")
        void testCheckArgumentWithFormattedMessage() {
            assertThatThrownBy(
                    () -> Preconditions.checkArgument(false, "Value %s is not between %s and %s", "10", "1", "5"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Value 10 is not between 1 and 5");
        }

        @ParameterizedTest
        @ValueSource(ints = { 1, 5, 10 })
        @DisplayName("checkArgument should validate range conditions")
        void testCheckArgumentRangeValidation(int value) {
            // Valid range
            assertThatCode(
                    () -> Preconditions.checkArgument(value >= 1 && value <= 10, "Value must be between 1 and 10"))
                    .doesNotThrowAnyException();

            // Invalid range
            assertThatThrownBy(() -> Preconditions.checkArgument(value > 10, "Value must be greater than 10"))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("State Validation Tests")
    class StateValidationTests {

        @Test
        @DisplayName("checkState should pass for valid state")
        void testCheckStateValid() {
            boolean isInitialized = true;

            assertThatCode(() -> Preconditions.checkState(isInitialized))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("checkState should throw ISE for invalid state")
        void testCheckStateInvalid() {
            boolean isInitialized = false;

            assertThatThrownBy(() -> Preconditions.checkState(isInitialized))
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        @DisplayName("checkState with message should throw ISE with custom message")
        void testCheckStateWithMessage() {
            String message = "Object not in valid state";

            assertThatThrownBy(() -> Preconditions.checkState(false, message))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage(message);
        }

        @Test
        @DisplayName("checkState should validate object states")
        void testCheckStateWithObjectState() {
            List<String> list = new ArrayList<>();

            // Valid state
            assertThatCode(() -> Preconditions.checkState(list.isEmpty(), "List should be empty"))
                    .doesNotThrowAnyException();

            list.add("item");

            // Invalid state
            assertThatThrownBy(() -> Preconditions.checkState(list.isEmpty(), "List should be empty"))
                    .isInstanceOf(IllegalStateException.class);
        }
    }

    @Nested
    @DisplayName("Index Validation Tests")
    class IndexValidationTests {

        @Test
        @DisplayName("checkElementIndex should pass for valid indices")
        void testCheckElementIndexValid() {
            assertThatCode(() -> {
                Preconditions.checkElementIndex(0, 5);
                Preconditions.checkElementIndex(4, 5);
                Preconditions.checkElementIndex(2, 10);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("checkElementIndex should throw IOOBE for invalid indices")
        void testCheckElementIndexInvalid() {
            assertThatThrownBy(() -> Preconditions.checkElementIndex(-1, 5))
                    .isInstanceOf(IndexOutOfBoundsException.class);

            assertThatThrownBy(() -> Preconditions.checkElementIndex(5, 5))
                    .isInstanceOf(IndexOutOfBoundsException.class);

            assertThatThrownBy(() -> Preconditions.checkElementIndex(10, 5))
                    .isInstanceOf(IndexOutOfBoundsException.class);
        }

        @Test
        @DisplayName("checkPositionIndex should pass for valid positions")
        void testCheckPositionIndexValid() {
            assertThatCode(() -> {
                Preconditions.checkPositionIndex(0, 5);
                Preconditions.checkPositionIndex(5, 5); // Position can equal size
                Preconditions.checkPositionIndex(3, 10);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("checkPositionIndex should throw IOOBE for invalid positions")
        void testCheckPositionIndexInvalid() {
            assertThatThrownBy(() -> Preconditions.checkPositionIndex(-1, 5))
                    .isInstanceOf(IndexOutOfBoundsException.class);

            assertThatThrownBy(() -> Preconditions.checkPositionIndex(6, 5))
                    .isInstanceOf(IndexOutOfBoundsException.class);
        }

        @ParameterizedTest
        @ValueSource(ints = { 0, 1, 2, 3, 4 })
        @DisplayName("checkElementIndex should handle array-like access patterns")
        void testCheckElementIndexArrayAccess(int index) {
            int arraySize = 5;

            assertThatCode(() -> Preconditions.checkElementIndex(index, arraySize))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Range Validation Tests")
    class RangeValidationTests {

        @Test
        @DisplayName("checkPositionIndexes should pass for valid ranges")
        void testCheckPositionIndexesValid() {
            assertThatCode(() -> {
                Preconditions.checkPositionIndexes(0, 3, 5);
                Preconditions.checkPositionIndexes(1, 4, 10);
                Preconditions.checkPositionIndexes(2, 2, 5); // start == end is valid
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("checkPositionIndexes should throw IOOBE for invalid ranges")
        void testCheckPositionIndexesInvalid() {
            // start > end
            assertThatThrownBy(() -> Preconditions.checkPositionIndexes(3, 1, 5))
                    .isInstanceOf(IndexOutOfBoundsException.class);

            // start < 0
            assertThatThrownBy(() -> Preconditions.checkPositionIndexes(-1, 3, 5))
                    .isInstanceOf(IndexOutOfBoundsException.class);

            // end > size
            assertThatThrownBy(() -> Preconditions.checkPositionIndexes(1, 6, 5))
                    .isInstanceOf(IndexOutOfBoundsException.class);
        }

        @Test
        @DisplayName("checkPositionIndexes should validate substring-like operations")
        void testCheckPositionIndexesSubstring() {
            String text = "Hello World";
            int length = text.length();

            // Valid substrings
            assertThatCode(() -> {
                Preconditions.checkPositionIndexes(0, 5, length); // "Hello"
                Preconditions.checkPositionIndexes(6, 11, length); // "World"
                Preconditions.checkPositionIndexes(0, length, length); // entire string
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("should chain validation methods")
        void testChainedValidation() {
            String input = "test";
            int index = 2;

            assertThatCode(() -> {
                String validated = Preconditions.checkNotNull(input, "Input cannot be null");
                Preconditions.checkArgument(validated.length() > 0, "Input cannot be empty");
                Preconditions.checkElementIndex(index, validated.length());
                Preconditions.checkState(validated.charAt(index) == 's', "Expected 's' at index %d", index);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should validate method parameters comprehensively")
        void testMethodParameterValidation() {
            // Simulate a method that processes a list with range validation
            List<String> items = Arrays.asList("a", "b", "c", "d", "e");
            int start = 1;
            int end = 4;

            assertThatCode(() -> {
                Preconditions.checkNotNull(items, "Items list cannot be null");
                Preconditions.checkArgument(!items.isEmpty(), "Items list cannot be empty");
                Preconditions.checkPositionIndexes(start, end, items.size());

                // Process sublist
                List<String> sublist = items.subList(start, end);
                Preconditions.checkState(sublist.size() == (end - start), "Sublist size mismatch");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should handle complex object validation")
        void testComplexObjectValidation() {
            Map<String, Integer> data = new HashMap<>();
            data.put("count", 5);
            data.put("limit", 10);

            assertThatCode(() -> {
                Preconditions.checkNotNull(data, "Data map cannot be null");
                Preconditions.checkArgument(data.containsKey("count"), "Data must contain 'count' key");
                Preconditions.checkArgument(data.containsKey("limit"), "Data must contain 'limit' key");

                int count = data.get("count");
                int limit = data.get("limit");

                Preconditions.checkArgument(count >= 0, "Count must be non-negative: %d", count);
                Preconditions.checkArgument(limit > 0, "Limit must be positive: %d", limit);
                Preconditions.checkState(count <= limit, "Count (%d) cannot exceed limit (%d)", count, limit);
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Edge Cases & Error Handling Tests")
    class EdgeCasesErrorHandlingTests {

        @Test
        @DisplayName("should handle zero-sized collections")
        void testZeroSizedCollections() {
            assertThatCode(() -> {
                Preconditions.checkElementIndex(0, 0); // Should throw - no valid indices
            }).isInstanceOf(IndexOutOfBoundsException.class);

            assertThatCode(() -> {
                Preconditions.checkPositionIndex(0, 0); // Should pass - position 0 in size 0 is valid
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should handle boundary conditions")
        void testBoundaryConditions() {
            int maxSize = Integer.MAX_VALUE;

            assertThatCode(() -> {
                Preconditions.checkElementIndex(maxSize - 1, maxSize);
                Preconditions.checkPositionIndex(maxSize, maxSize);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should handle null message suppliers gracefully")
        void testNullMessageSupplier() {
            Supplier<String> nullSupplier = null;

            assertThatThrownBy(() -> Preconditions.checkNotNull(null, nullSupplier))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("null"); // toString() of null supplier
        }

        @Test
        @DisplayName("should handle format string edge cases")
        void testFormatStringEdgeCases() {
            // Empty format arguments
            assertThatThrownBy(() -> Preconditions.checkArgument(false, "Error occurred"))
                    .hasMessage("Error occurred");

            // Extra format arguments (appended in square brackets)
            assertThatThrownBy(() -> Preconditions.checkArgument(false, "Error: %s", "value", "extra"))
                    .hasMessage("Error: value [extra]");
        }

        @Test
        @DisplayName("utility class should have private constructor")
        void testUtilityClassNotInstantiable() {
            // Verify Preconditions constructor is private (but doesn't throw an exception)
            assertThatCode(() -> {
                Constructor<Preconditions> constructor = Preconditions.class.getDeclaredConstructor();
                assertThat(java.lang.reflect.Modifier.isPrivate(constructor.getModifiers())).isTrue();
                constructor.setAccessible(true);
                Preconditions instance = constructor.newInstance();
                assertThat(instance).isNotNull(); // Can be instantiated via reflection
            }).doesNotThrowAnyException();
        }
    }
}
