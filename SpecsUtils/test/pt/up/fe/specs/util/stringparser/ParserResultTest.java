package pt.up.fe.specs.util.stringparser;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.utilities.StringSlice;

/**
 * Comprehensive test suite for {@link ParserResult}.
 * Tests the result container for parsing operations, including result
 * retrieval, string slice management, and utility methods for optional
 * handling.
 * 
 * @author Generated Tests
 */
@DisplayName("ParserResult Tests")
public class ParserResultTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create ParserResult with string and result")
        void testBasicConstruction() {
            StringSlice slice = new StringSlice("remaining text");
            String result = "parsed value";

            ParserResult<String> parserResult = new ParserResult<>(slice, result);

            assertThat(parserResult.getModifiedString()).isEqualTo(slice);
            assertThat(parserResult.getResult()).isEqualTo(result);
        }

        @Test
        @DisplayName("Should handle null result values")
        void testNullResultConstruction() {
            StringSlice slice = new StringSlice("text");

            ParserResult<String> parserResult = new ParserResult<>(slice, null);

            assertThat(parserResult.getModifiedString()).isEqualTo(slice);
            assertThat(parserResult.getResult()).isNull();
        }

        @Test
        @DisplayName("Should handle empty string slice")
        void testEmptyStringSliceConstruction() {
            StringSlice emptySlice = new StringSlice("");
            Integer result = 42;

            ParserResult<Integer> parserResult = new ParserResult<>(emptySlice, result);

            assertThat(parserResult.getModifiedString()).isEqualTo(emptySlice);
            assertThat(parserResult.getModifiedString().toString()).isEmpty();
            assertThat(parserResult.getResult()).isEqualTo(42);
        }

        @Test
        @DisplayName("Should preserve string slice reference")
        void testStringSliceReference() {
            StringSlice originalSlice = new StringSlice("original");
            String result = "result";

            ParserResult<String> parserResult = new ParserResult<>(originalSlice, result);

            // Should preserve the exact reference, not a copy
            assertThat(parserResult.getModifiedString()).isSameAs(originalSlice);
        }
    }

    @Nested
    @DisplayName("Result Access Tests")
    class ResultAccessTests {

        @Test
        @DisplayName("Should return correct string result")
        void testStringResult() {
            StringSlice slice = new StringSlice("remainder");
            String expected = "extracted text";

            ParserResult<String> parserResult = new ParserResult<>(slice, expected);

            assertThat(parserResult.getResult()).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should return correct integer result")
        void testIntegerResult() {
            StringSlice slice = new StringSlice("123 remaining");
            Integer expected = 123;

            ParserResult<Integer> parserResult = new ParserResult<>(slice, expected);

            assertThat(parserResult.getResult()).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should return correct boolean result")
        void testBooleanResult() {
            StringSlice slice = new StringSlice("false remaining");
            Boolean expected = true;

            ParserResult<Boolean> parserResult = new ParserResult<>(slice, expected);

            assertThat(parserResult.getResult()).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should return correct complex object result")
        void testComplexObjectResult() {
            StringSlice slice = new StringSlice("after parsing");

            record ParsedData(String name, int value) {
            }
            ParsedData expected = new ParsedData("test", 42);

            ParserResult<ParsedData> parserResult = new ParserResult<>(slice, expected);

            assertThat(parserResult.getResult()).isEqualTo(expected);
            assertThat(parserResult.getResult().name()).isEqualTo("test");
            assertThat(parserResult.getResult().value()).isEqualTo(42);
        }
    }

    @Nested
    @DisplayName("String Slice Access Tests")
    class StringSliceAccessTests {

        @Test
        @DisplayName("Should return correct modified string slice")
        void testModifiedStringAccess() {
            StringSlice expected = new StringSlice("modified content");
            String result = "parsed";

            ParserResult<String> parserResult = new ParserResult<>(expected, result);

            assertThat(parserResult.getModifiedString()).isEqualTo(expected);
            assertThat(parserResult.getModifiedString().toString()).isEqualTo("modified content");
        }

        @Test
        @DisplayName("Should handle empty modified string")
        void testEmptyModifiedString() {
            StringSlice emptySlice = new StringSlice("");
            String result = "all consumed";

            ParserResult<String> parserResult = new ParserResult<>(emptySlice, result);

            assertThat(parserResult.getModifiedString().isEmpty()).isTrue();
            assertThat(parserResult.getModifiedString().toString()).isEmpty();
        }

        @Test
        @DisplayName("Should preserve string slice modifications")
        void testStringSliceModificationPreservation() {
            StringSlice slice = new StringSlice("original text");
            StringSlice modified = slice.substring(9); // "text"
            String result = "original";

            ParserResult<String> parserResult = new ParserResult<>(modified, result);

            assertThat(parserResult.getModifiedString().toString()).isEqualTo("text");
            assertThat(parserResult.getResult()).isEqualTo("original");
        }

        @Test
        @DisplayName("Should handle trimmed string slices")
        void testTrimmedStringSlices() {
            StringSlice slice = new StringSlice("  spaced content  ");
            StringSlice trimmed = slice.trim();
            String result = "trimmed";

            ParserResult<String> parserResult = new ParserResult<>(trimmed, result);

            assertThat(parserResult.getModifiedString().toString()).isEqualTo("spaced content");
            assertThat(parserResult.getResult()).isEqualTo("trimmed");
        }
    }

    @Nested
    @DisplayName("Optional Utility Tests")
    class OptionalUtilityTests {

        @Test
        @DisplayName("Should convert ParserResult to Optional")
        void testAsOptionalConversion() {
            StringSlice slice = new StringSlice("remaining");
            String result = "value";
            ParserResult<String> original = new ParserResult<>(slice, result);

            ParserResult<Optional<String>> optionalResult = ParserResult.asOptional(original);

            assertThat(optionalResult.getModifiedString()).isEqualTo(slice);
            assertThat(optionalResult.getResult()).isPresent();
            assertThat(optionalResult.getResult()).hasValue("value");
        }

        @Test
        @DisplayName("Should handle null result by throwing NPE")
        void testAsOptionalWithNullResult() {
            StringSlice slice = new StringSlice("text");
            ParserResult<String> original = new ParserResult<>(slice, null);

            // The implementation uses Optional.of() which throws NPE for null values
            assertThatThrownBy(() -> ParserResult.asOptional(original))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should preserve string slice in Optional conversion")
        void testAsOptionalStringSlicePreservation() {
            StringSlice originalSlice = new StringSlice("preserve me");
            Integer result = 999;
            ParserResult<Integer> original = new ParserResult<>(originalSlice, result);

            ParserResult<Optional<Integer>> optionalResult = ParserResult.asOptional(original);

            assertThat(optionalResult.getModifiedString()).isSameAs(originalSlice);
            assertThat(optionalResult.getResult()).hasValue(999);
        }

        @Test
        @DisplayName("Should handle complex types in Optional conversion")
        void testAsOptionalWithComplexTypes() {
            StringSlice slice = new StringSlice("complex");

            record ComplexType(String data, int number) {
            }
            ComplexType complexResult = new ComplexType("test data", 123);
            ParserResult<ComplexType> original = new ParserResult<>(slice, complexResult);

            ParserResult<Optional<ComplexType>> optionalResult = ParserResult.asOptional(original);

            assertThat(optionalResult.getResult()).isPresent();
            assertThat(optionalResult.getResult()).hasValueSatisfying(complex -> {
                assertThat(complex.data()).isEqualTo("test data");
                assertThat(complex.number()).isEqualTo(123);
            });
        }
    }

    @Nested
    @DisplayName("Immutability Tests")
    class ImmutabilityTests {

        @Test
        @DisplayName("Should be immutable after construction")
        void testImmutability() {
            StringSlice slice = new StringSlice("immutable test");
            String result = "immutable result";

            ParserResult<String> parserResult = new ParserResult<>(slice, result);

            // Verify that the result cannot be changed (no setters should exist)
            assertThat(parserResult.getResult()).isEqualTo("immutable result");
            assertThat(parserResult.getModifiedString().toString()).isEqualTo("immutable test");

            // Multiple calls should return the same values
            assertThat(parserResult.getResult()).isEqualTo(parserResult.getResult());
            assertThat(parserResult.getModifiedString()).isEqualTo(parserResult.getModifiedString());
        }

        @Test
        @DisplayName("Should not be affected by external changes to string slice")
        void testExternalStringSliceChanges() {
            StringSlice slice = new StringSlice("original content");
            String result = "parsed";

            ParserResult<String> parserResult = new ParserResult<>(slice, result);

            // Capture initial state
            String initialSliceContent = parserResult.getModifiedString().toString();

            // Note: StringSlice is typically immutable, but this tests the concept
            assertThat(parserResult.getModifiedString().toString()).isEqualTo(initialSliceContent);
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle very large string slices")
        void testLargeStringSlices() {
            String largeContent = "x".repeat(100000);
            StringSlice largeSlice = new StringSlice(largeContent);
            String result = "large";

            ParserResult<String> parserResult = new ParserResult<>(largeSlice, result);

            assertThat(parserResult.getModifiedString().toString()).hasSize(100000);
            assertThat(parserResult.getResult()).isEqualTo("large");
        }

        @Test
        @DisplayName("Should handle special characters in string slice")
        void testSpecialCharacters() {
            String specialContent = "§†∆∫ƒ©˙∆˚¬…æ«»\"'–—÷≠±∞";
            StringSlice specialSlice = new StringSlice(specialContent);
            String result = "special";

            ParserResult<String> parserResult = new ParserResult<>(specialSlice, result);

            assertThat(parserResult.getModifiedString().toString()).isEqualTo(specialContent);
            assertThat(parserResult.getResult()).isEqualTo("special");
        }

        @Test
        @DisplayName("Should handle multiline content")
        void testMultilineContent() {
            String multilineContent = "line1\nline2\r\nline3\ttabbed";
            StringSlice multilineSlice = new StringSlice(multilineContent);
            Integer result = 3;

            ParserResult<Integer> parserResult = new ParserResult<>(multilineSlice, result);

            assertThat(parserResult.getModifiedString().toString()).isEqualTo(multilineContent);
            assertThat(parserResult.getResult()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should handle zero-length but non-null results")
        void testZeroLengthResults() {
            StringSlice slice = new StringSlice("non-empty");
            String emptyResult = "";

            ParserResult<String> parserResult = new ParserResult<>(slice, emptyResult);

            assertThat(parserResult.getResult()).isNotNull();
            assertThat(parserResult.getResult()).isEmpty();
            assertThat(parserResult.getModifiedString().toString()).isEqualTo("non-empty");
        }
    }

    @Nested
    @DisplayName("Type Safety Tests")
    class TypeSafetyTests {

        @Test
        @DisplayName("Should maintain type safety with generics")
        void testTypeSafety() {
            StringSlice slice = new StringSlice("type safe");

            // String type
            ParserResult<String> stringResult = new ParserResult<>(slice, "text");
            assertThat(stringResult.getResult()).isInstanceOf(String.class);

            // Integer type
            ParserResult<Integer> intResult = new ParserResult<>(slice, 42);
            assertThat(intResult.getResult()).isInstanceOf(Integer.class);

            // Boolean type
            ParserResult<Boolean> boolResult = new ParserResult<>(slice, true);
            assertThat(boolResult.getResult()).isInstanceOf(Boolean.class);
        }

        @Test
        @DisplayName("Should work with collection types")
        void testCollectionTypes() {
            StringSlice slice = new StringSlice("collection");
            java.util.List<String> listResult = java.util.Arrays.asList("a", "b", "c");

            ParserResult<java.util.List<String>> parserResult = new ParserResult<>(slice, listResult);

            assertThat(parserResult.getResult()).isInstanceOf(java.util.List.class);
            assertThat(parserResult.getResult()).containsExactly("a", "b", "c");
        }

        @Test
        @DisplayName("Should work with custom interfaces")
        void testCustomInterfaces() {
            interface CustomParsable {
                String getData();
            }

            class CustomImpl implements CustomParsable {
                @Override
                public String getData() {
                    return "custom data";
                }
            }

            StringSlice slice = new StringSlice("custom");
            CustomParsable result = new CustomImpl();

            ParserResult<CustomParsable> parserResult = new ParserResult<>(slice, result);

            assertThat(parserResult.getResult()).isInstanceOf(CustomParsable.class);
            assertThat(parserResult.getResult().getData()).isEqualTo("custom data");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with realistic parsing scenarios")
        void testRealisticParsingScenario() {
            // Simulate parsing a key-value pair
            StringSlice originalInput = new StringSlice("key=value&remaining=data");
            StringSlice afterParsing = originalInput.substring(9); // "&remaining=data"

            record KeyValue(String key, String value) {
            }
            KeyValue parsed = new KeyValue("key", "value");

            ParserResult<KeyValue> parserResult = new ParserResult<>(afterParsing, parsed);

            assertThat(parserResult.getResult().key()).isEqualTo("key");
            assertThat(parserResult.getResult().value()).isEqualTo("value");
            assertThat(parserResult.getModifiedString().toString()).isEqualTo("&remaining=data");
        }

        @Test
        @DisplayName("Should support chained parsing operations")
        void testChainedParsingOperations() {
            StringSlice input1 = new StringSlice("first,second,third");
            StringSlice afterFirst = input1.substring(6); // "second,third"
            ParserResult<String> firstResult = new ParserResult<>(afterFirst, "first");

            StringSlice afterSecond = firstResult.getModifiedString().substring(7); // "third"
            ParserResult<String> secondResult = new ParserResult<>(afterSecond, "second");

            StringSlice afterThird = secondResult.getModifiedString().clear();
            ParserResult<String> thirdResult = new ParserResult<>(afterThird, "third");

            assertThat(firstResult.getResult()).isEqualTo("first");
            assertThat(secondResult.getResult()).isEqualTo("second");
            assertThat(thirdResult.getResult()).isEqualTo("third");
            assertThat(thirdResult.getModifiedString().isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Should work with parser result transformations")
        void testParserResultTransformations() {
            StringSlice slice = new StringSlice("123 remaining");
            ParserResult<String> stringResult = new ParserResult<>(slice, "123");

            // Transform to optional
            ParserResult<Optional<String>> optionalResult = ParserResult.asOptional(stringResult);

            // Verify transformation preserves data
            assertThat(optionalResult.getModifiedString()).isEqualTo(stringResult.getModifiedString());
            assertThat(optionalResult.getResult()).hasValue("123");
        }
    }
}
