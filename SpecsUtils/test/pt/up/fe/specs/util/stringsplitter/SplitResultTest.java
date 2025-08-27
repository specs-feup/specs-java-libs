package pt.up.fe.specs.util.stringsplitter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for SplitResult class.
 * Tests result container functionality and immutability.
 * 
 * @author Generated Tests
 */
@DisplayName("SplitResult Tests")
class SplitResultTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create SplitResult with valid parameters")
        void testConstructorWithValidParameters() {
            StringSliceWithSplit slice = new StringSliceWithSplit("test");
            String value = "result";

            SplitResult<String> result = new SplitResult<>(slice, value);

            assertThat(result).isNotNull();
            assertThat(result.getModifiedSlice()).isSameAs(slice);
            assertThat(result.getValue()).isSameAs(value);
        }

        @Test
        @DisplayName("Should allow null slice parameter")
        void testConstructorWithNullSlice() {
            String value = "result";

            SplitResult<String> result = new SplitResult<>(null, value);

            assertThat(result).isNotNull();
            assertThat(result.getModifiedSlice()).isNull();
            assertThat(result.getValue()).isEqualTo(value);
        }

        @Test
        @DisplayName("Should allow null value parameter")
        void testConstructorWithNullValue() {
            StringSliceWithSplit slice = new StringSliceWithSplit("test");

            SplitResult<String> result = new SplitResult<>(slice, null);

            assertThat(result).isNotNull();
            assertThat(result.getModifiedSlice()).isSameAs(slice);
            assertThat(result.getValue()).isNull();
        }

        @Test
        @DisplayName("Should allow both parameters to be null")
        void testConstructorWithBothNull() {
            SplitResult<String> result = new SplitResult<>(null, null);

            assertThat(result).isNotNull();
            assertThat(result.getModifiedSlice()).isNull();
            assertThat(result.getValue()).isNull();
        }
    }

    @Nested
    @DisplayName("Getter Method Tests")
    class GetterMethodTests {

        @Test
        @DisplayName("Should return correct modified slice")
        void testGetModifiedSlice() {
            StringSliceWithSplit originalSlice = new StringSliceWithSplit("original");
            StringSliceWithSplit modifiedSlice = new StringSliceWithSplit("modified");
            String value = "test";

            SplitResult<String> result = new SplitResult<>(modifiedSlice, value);

            assertThat(result.getModifiedSlice()).isSameAs(modifiedSlice);
            assertThat(result.getModifiedSlice()).isNotSameAs(originalSlice);
        }

        @Test
        @DisplayName("Should return correct value")
        void testGetValue() {
            StringSliceWithSplit slice = new StringSliceWithSplit("slice");
            String value1 = "value1";
            String value2 = "value2";

            SplitResult<String> result = new SplitResult<>(slice, value1);

            assertThat(result.getValue()).isSameAs(value1);
            assertThat(result.getValue()).isNotSameAs(value2);
        }

        @Test
        @DisplayName("Should maintain immutability of contents")
        void testImmutability() {
            StringSliceWithSplit slice = new StringSliceWithSplit("test");
            String value = "original";

            SplitResult<String> result = new SplitResult<>(slice, value);

            // The returned references should be the same (immutable container)
            assertThat(result.getModifiedSlice()).isSameAs(slice);
            assertThat(result.getValue()).isSameAs(value);

            // Multiple calls should return the same references
            assertThat(result.getModifiedSlice()).isSameAs(result.getModifiedSlice());
            assertThat(result.getValue()).isSameAs(result.getValue());
        }
    }

    @Nested
    @DisplayName("Generic Type Tests")
    class GenericTypeTests {

        @Test
        @DisplayName("Should work with String type")
        void testStringType() {
            StringSliceWithSplit slice = new StringSliceWithSplit("slice");
            String value = "string value";

            SplitResult<String> result = new SplitResult<>(slice, value);

            assertThat(result.getValue()).isInstanceOf(String.class);
            assertThat(result.getValue()).isEqualTo("string value");
        }

        @Test
        @DisplayName("Should work with Integer type")
        void testIntegerType() {
            StringSliceWithSplit slice = new StringSliceWithSplit("slice");
            Integer value = 42;

            SplitResult<Integer> result = new SplitResult<>(slice, value);

            assertThat(result.getValue()).isInstanceOf(Integer.class);
            assertThat(result.getValue()).isEqualTo(42);
        }

        @Test
        @DisplayName("Should work with Boolean type")
        void testBooleanType() {
            StringSliceWithSplit slice = new StringSliceWithSplit("slice");
            Boolean value = true;

            SplitResult<Boolean> result = new SplitResult<>(slice, value);

            assertThat(result.getValue()).isInstanceOf(Boolean.class);
            assertThat(result.getValue()).isTrue();
        }

        @Test
        @DisplayName("Should work with Double type")
        void testDoubleType() {
            StringSliceWithSplit slice = new StringSliceWithSplit("slice");
            Double value = 3.14159;

            SplitResult<Double> result = new SplitResult<>(slice, value);

            assertThat(result.getValue()).isInstanceOf(Double.class);
            assertThat(result.getValue()).isEqualTo(3.14159);
        }

        @Test
        @DisplayName("Should work with Float type")
        void testFloatType() {
            StringSliceWithSplit slice = new StringSliceWithSplit("slice");
            Float value = 2.718f;

            SplitResult<Float> result = new SplitResult<>(slice, value);

            assertThat(result.getValue()).isInstanceOf(Float.class);
            assertThat(result.getValue()).isEqualTo(2.718f);
        }

        @Test
        @DisplayName("Should work with List type")
        void testListType() {
            StringSliceWithSplit slice = new StringSliceWithSplit("slice");
            List<String> value = Arrays.asList("item1", "item2", "item3");

            SplitResult<List<String>> result = new SplitResult<>(slice, value);

            assertThat(result.getValue()).isInstanceOf(List.class);
            assertThat(result.getValue()).containsExactly("item1", "item2", "item3");
        }

        @Test
        @DisplayName("Should work with custom object type")
        void testCustomObjectType() {
            StringSliceWithSplit slice = new StringSliceWithSplit("slice");
            TestObject value = new TestObject("test", 123);

            SplitResult<TestObject> result = new SplitResult<>(slice, value);

            assertThat(result.getValue()).isInstanceOf(TestObject.class);
            assertThat(result.getValue().name).isEqualTo("test");
            assertThat(result.getValue().number).isEqualTo(123);
        }

        private static class TestObject {
            final String name;
            final int number;

            TestObject(String name, int number) {
                this.name = name;
                this.number = number;
            }
        }
    }

    @Nested
    @DisplayName("Real-world Usage Scenarios")
    class RealWorldUsageScenarios {

        @Test
        @DisplayName("Should work in parsing integer scenario")
        void testIntegerParsing() {
            StringSliceWithSplit originalSlice = new StringSliceWithSplit("123 remaining text");

            // Simulate parsing an integer
            SplitResult<String> stringResult = originalSlice.split();
            Integer parsedValue = Integer.parseInt(stringResult.getValue());

            SplitResult<Integer> intResult = new SplitResult<>(stringResult.getModifiedSlice(), parsedValue);

            assertThat(intResult.getValue()).isEqualTo(123);
            assertThat(intResult.getModifiedSlice().toString()).isEqualTo("remaining text");
        }

        @Test
        @DisplayName("Should work in parsing double scenario")
        void testDoubleParsing() {
            StringSliceWithSplit originalSlice = new StringSliceWithSplit("45.67 more text");

            // Simulate parsing a double
            SplitResult<String> stringResult = originalSlice.split();
            Double parsedValue = Double.parseDouble(stringResult.getValue());

            SplitResult<Double> doubleResult = new SplitResult<>(stringResult.getModifiedSlice(), parsedValue);

            assertThat(doubleResult.getValue()).isEqualTo(45.67);
            assertThat(doubleResult.getModifiedSlice().toString()).isEqualTo("more text");
        }

        @Test
        @DisplayName("Should work in conditional parsing scenario")
        void testConditionalParsing() {
            StringSliceWithSplit slice = new StringSliceWithSplit("valid_prefix:data remaining");

            // Simulate conditional parsing
            if (slice.toString().startsWith("valid_prefix:")) {
                StringSliceWithSplit afterPrefix = slice.substring(13); // Remove "valid_prefix:"
                SplitResult<String> dataResult = afterPrefix.split();

                SplitResult<String> conditionalResult = new SplitResult<>(dataResult.getModifiedSlice(),
                        "VALIDATED:" + dataResult.getValue());

                assertThat(conditionalResult.getValue()).isEqualTo("VALIDATED:data");
                assertThat(conditionalResult.getModifiedSlice().toString()).isEqualTo("remaining");
            } else {
                fail("Should have matched valid prefix");
            }
        }

        @Test
        @DisplayName("Should work in chained parsing scenario")
        void testChainedParsing() {
            StringSliceWithSplit slice = new StringSliceWithSplit("first second 123 final");

            // Chain multiple parsing operations
            SplitResult<String> firstResult = slice.split();
            assertThat(firstResult.getValue()).isEqualTo("first");

            SplitResult<String> secondResult = firstResult.getModifiedSlice().split();
            assertThat(secondResult.getValue()).isEqualTo("second");

            SplitResult<String> thirdStringResult = secondResult.getModifiedSlice().split();
            Integer thirdValue = Integer.parseInt(thirdStringResult.getValue());
            SplitResult<Integer> thirdResult = new SplitResult<>(thirdStringResult.getModifiedSlice(), thirdValue);
            assertThat(thirdResult.getValue()).isEqualTo(123);

            SplitResult<String> finalResult = thirdResult.getModifiedSlice().split();
            assertThat(finalResult.getValue()).isEqualTo("final");
            assertThat(finalResult.getModifiedSlice().toString()).isEmpty();
        }

        @Test
        @DisplayName("Should work with error handling scenario")
        void testErrorHandlingScenario() {
            StringSliceWithSplit slice = new StringSliceWithSplit("invalid_number remaining");

            // Simulate error handling in parsing
            SplitResult<String> stringResult = slice.split();
            SplitResult<Integer> errorResult;

            try {
                Integer parsed = Integer.parseInt(stringResult.getValue());
                errorResult = new SplitResult<>(stringResult.getModifiedSlice(), parsed);
            } catch (NumberFormatException e) {
                // Return null to indicate parsing failure
                errorResult = new SplitResult<>(slice, null);
            }

            assertThat(errorResult.getValue()).isNull();
            assertThat(errorResult.getModifiedSlice()).isSameAs(slice);
        }
    }

    @Nested
    @DisplayName("Slice Modification Tests")
    class SliceModificationTests {

        @Test
        @DisplayName("Should handle empty slice results")
        void testEmptySliceResult() {
            StringSliceWithSplit emptySlice = new StringSliceWithSplit("");
            String value = "parsed from empty";

            SplitResult<String> result = new SplitResult<>(emptySlice, value);

            assertThat(result.getModifiedSlice().toString()).isEmpty();
            assertThat(result.getValue()).isEqualTo("parsed from empty");
        }

        @Test
        @DisplayName("Should handle slice with different configurations")
        void testSliceWithDifferentConfigurations() {
            StringSliceWithSplit originalSlice = new StringSliceWithSplit("  test  remaining  ");

            // Test with trim enabled
            StringSliceWithSplit trimmedSlice = originalSlice.setTrim(true);
            SplitResult<String> trimmedResult = new SplitResult<>(trimmedSlice, "value");

            assertThat(trimmedResult.getModifiedSlice()).isSameAs(trimmedSlice);

            // Test with custom separator
            StringSliceWithSplit customSepSlice = originalSlice.setSeparator(ch -> ch == 's');
            SplitResult<String> customSepResult = new SplitResult<>(customSepSlice, "value");

            assertThat(customSepResult.getModifiedSlice()).isSameAs(customSepSlice);
        }

        @Test
        @DisplayName("Should handle slice substring operations")
        void testSliceSubstringOperations() {
            StringSliceWithSplit originalSlice = new StringSliceWithSplit("hello world test");
            StringSliceWithSplit substringSlice = originalSlice.substring(6); // "world test"

            SplitResult<String> result = new SplitResult<>(substringSlice, "extracted");

            assertThat(result.getModifiedSlice().toString()).isEqualTo("world test");
            assertThat(result.getValue()).isEqualTo("extracted");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Boundary Tests")
    class EdgeCasesAndBoundaryTests {

        @ParameterizedTest
        @ValueSource(strings = { "", " ", "a", "very long string with multiple words and characters" })
        @DisplayName("Should handle various string lengths")
        void testVariousStringLengths(String input) {
            StringSliceWithSplit slice = new StringSliceWithSplit(input);
            String value = "test_value";

            SplitResult<String> result = new SplitResult<>(slice, value);

            assertThat(result.getModifiedSlice().toString()).isEqualTo(input);
            assertThat(result.getValue()).isEqualTo(value);
        }

        @Test
        @DisplayName("Should handle Unicode characters")
        void testUnicodeCharacters() {
            StringSliceWithSplit slice = new StringSliceWithSplit("こんにちは 🌍 αβγ");
            String value = "Unicode test";

            SplitResult<String> result = new SplitResult<>(slice, value);

            assertThat(result.getModifiedSlice().toString()).isEqualTo("こんにちは 🌍 αβγ");
            assertThat(result.getValue()).isEqualTo("Unicode test");
        }

        @Test
        @DisplayName("Should handle special characters")
        void testSpecialCharacters() {
            StringSliceWithSplit slice = new StringSliceWithSplit("!@#$%^&*()_+-=[]{}|;':\",./<>?`~");
            String value = "Special chars";

            SplitResult<String> result = new SplitResult<>(slice, value);

            assertThat(result.getModifiedSlice().toString()).isEqualTo("!@#$%^&*()_+-=[]{}|;':\",./<>?`~");
            assertThat(result.getValue()).isEqualTo("Special chars");
        }

        @Test
        @DisplayName("Should handle very large values")
        void testVeryLargeValues() {
            StringSliceWithSplit slice = new StringSliceWithSplit("slice");
            String largeValue = "x".repeat(10000);

            SplitResult<String> result = new SplitResult<>(slice, largeValue);

            assertThat(result.getValue()).hasSize(10000);
            assertThat(result.getValue()).isEqualTo(largeValue);
        }

        @Test
        @DisplayName("Should handle numerical edge cases")
        void testNumericalEdgeCases() {
            StringSliceWithSplit slice = new StringSliceWithSplit("slice");

            // Test with maximum integer
            SplitResult<Integer> maxIntResult = new SplitResult<>(slice, Integer.MAX_VALUE);
            assertThat(maxIntResult.getValue()).isEqualTo(Integer.MAX_VALUE);

            // Test with minimum integer
            SplitResult<Integer> minIntResult = new SplitResult<>(slice, Integer.MIN_VALUE);
            assertThat(minIntResult.getValue()).isEqualTo(Integer.MIN_VALUE);

            // Test with infinity
            SplitResult<Double> infResult = new SplitResult<>(slice, Double.POSITIVE_INFINITY);
            assertThat(infResult.getValue()).isEqualTo(Double.POSITIVE_INFINITY);

            // Test with NaN
            SplitResult<Double> nanResult = new SplitResult<>(slice, Double.NaN);
            assertThat(nanResult.getValue()).isNaN();
        }

        @Test
        @DisplayName("Should handle reference equality correctly")
        void testReferenceEquality() {
            StringSliceWithSplit slice = new StringSliceWithSplit("test");
            String value = "value";

            SplitResult<String> result1 = new SplitResult<>(slice, value);
            SplitResult<String> result2 = new SplitResult<>(slice, value);

            // Different objects
            assertThat(result1).isNotSameAs(result2);

            // But same contained references
            assertThat(result1.getModifiedSlice()).isSameAs(result2.getModifiedSlice());
            assertThat(result1.getValue()).isSameAs(result2.getValue());
        }
    }

    @Nested
    @DisplayName("Type Safety Tests")
    class TypeSafetyTests {

        @Test
        @DisplayName("Should maintain type safety with wildcards")
        void testTypeSafetyWithWildcards() {
            StringSliceWithSplit slice = new StringSliceWithSplit("slice");

            SplitResult<? extends Number> numberResult = new SplitResult<>(slice, 42);
            assertThat(numberResult.getValue()).isInstanceOf(Number.class);
            assertThat(numberResult.getValue()).isInstanceOf(Integer.class);

            SplitResult<? super String> stringResult = new SplitResult<>(slice, "test");
            assertThat(stringResult.getValue()).isEqualTo("test");
        }

        @Test
        @DisplayName("Should work with raw types (legacy compatibility)")
        void testRawTypes() {
            StringSliceWithSplit slice = new StringSliceWithSplit("slice");

            @SuppressWarnings({ "rawtypes", "unchecked" })
            SplitResult rawResult = new SplitResult(slice, "raw value");

            Object value = rawResult.getValue();

            assertThat(value).isEqualTo("raw value");
        }

        @Test
        @DisplayName("Should handle null types correctly")
        void testNullTypes() {
            StringSliceWithSplit slice = new StringSliceWithSplit("slice");

            SplitResult<String> nullStringResult = new SplitResult<>(slice, null);
            assertThat(nullStringResult.getValue()).isNull();

            SplitResult<Integer> nullIntResult = new SplitResult<>(slice, null);
            assertThat(nullIntResult.getValue()).isNull();

            SplitResult<Object> nullObjectResult = new SplitResult<>(slice, null);
            assertThat(nullObjectResult.getValue()).isNull();
        }
    }
}
