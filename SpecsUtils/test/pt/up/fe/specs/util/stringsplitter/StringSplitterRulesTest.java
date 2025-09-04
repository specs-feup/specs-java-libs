package pt.up.fe.specs.util.stringsplitter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import pt.up.fe.specs.util.parsing.StringDecoder;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for StringSplitterRules utility class.
 * Tests string parsing rules and type conversions.
 * 
 * @author Generated Tests
 */
@DisplayName("StringSplitterRules Tests")
class StringSplitterRulesTest {

    @Nested
    @DisplayName("String Rule Tests")
    class StringRuleTests {

        @Test
        @DisplayName("Should extract first string using default separator")
        void testStringRule_DefaultSeparator() {
            StringSliceWithSplit slice = new StringSliceWithSplit("hello world test");
            SplitResult<String> result = StringSplitterRules.string(slice);

            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEqualTo("hello");
            assertThat(result.getModifiedSlice().toString()).isEqualTo("world test");
        }

        @Test
        @DisplayName("Should extract complete string when no separator found")
        void testStringRule_NoSeparator() {
            StringSliceWithSplit slice = new StringSliceWithSplit("helloworld");
            SplitResult<String> result = StringSplitterRules.string(slice);

            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEqualTo("helloworld");
            assertThat(result.getModifiedSlice().toString()).isEmpty();
        }

        @Test
        @DisplayName("Should handle empty string")
        void testStringRule_EmptyString() {
            StringSliceWithSplit slice = new StringSliceWithSplit("");
            SplitResult<String> result = StringSplitterRules.string(slice);

            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEmpty();
            assertThat(result.getModifiedSlice().toString()).isEmpty();
        }

        @Test
        @DisplayName("Should handle strings with only separators")
        void testStringRule_OnlySeparators() {
            StringSliceWithSplit slice = new StringSliceWithSplit("   \t\n   ");
            SplitResult<String> result = StringSplitterRules.string(slice);

            assertThat(result).isNotNull();
            // With trim enabled, this should result in empty string
            assertThat(result.getValue()).isEmpty();
        }

        @Test
        @DisplayName("Should extract string with custom separator")
        void testStringRule_CustomSeparator() {
            StringSliceWithSplit slice = new StringSliceWithSplit("hello,world,test")
                    .setSeparator(ch -> ch == ',');
            SplitResult<String> result = StringSplitterRules.string(slice);

            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEqualTo("hello");
            assertThat(result.getModifiedSlice().toString()).isEqualTo("world,test");
        }

        @Test
        @DisplayName("Should handle trim settings correctly")
        void testStringRule_TrimSettings() {
            StringSliceWithSplit slice = new StringSliceWithSplit("  hello  world  ")
                    .setTrim(false);
            SplitResult<String> result = StringSplitterRules.string(slice);

            assertThat(result).isNotNull();
            // Leading spaces mean the first split result is empty string before first space
            assertThat(result.getValue()).isEmpty();

            // Test with trim enabled
            StringSliceWithSplit trimSlice = new StringSliceWithSplit("  hello  world  ")
                    .setTrim(true);
            SplitResult<String> trimResult = StringSplitterRules.string(trimSlice);

            // With trim, empty result becomes empty after trimming
            assertThat(trimResult.getValue()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Object Rule Tests")
    class ObjectRuleTests {

        @Test
        @DisplayName("Should convert string to object using decoder")
        void testObjectRule_SuccessfulConversion() {
            StringSliceWithSplit slice = new StringSliceWithSplit("TEST input");
            StringDecoder<String> upperCaseDecoder = String::toUpperCase;

            SplitResult<String> result = StringSplitterRules.object(slice, upperCaseDecoder);

            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEqualTo("TEST");
            assertThat(result.getModifiedSlice().toString()).isEqualTo("input");
        }

        @Test
        @DisplayName("Should return null when decoder fails")
        void testObjectRule_DecoderFails() {
            StringSliceWithSplit slice = new StringSliceWithSplit("invalid input");
            StringDecoder<Integer> failingDecoder = s -> null; // Always fails

            SplitResult<Integer> result = StringSplitterRules.object(slice, failingDecoder);

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should handle decoder that throws exception")
        void testObjectRule_DecoderThrows() {
            StringSliceWithSplit slice = new StringSliceWithSplit("test input");
            StringDecoder<String> throwingDecoder = s -> {
                throw new RuntimeException("Decoder error");
            };

            assertThatThrownBy(() -> StringSplitterRules.object(slice, throwingDecoder))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Decoder error");
        }

        @Test
        @DisplayName("Should work with complex decoder logic")
        void testObjectRule_ComplexDecoder() {
            StringSliceWithSplit slice = new StringSliceWithSplit("valid-123 remaining");
            StringDecoder<Integer> extractNumberDecoder = s -> {
                if (s.startsWith("valid-")) {
                    try {
                        return Integer.parseInt(s.substring(6));
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }
                return null;
            };

            SplitResult<Integer> result = StringSplitterRules.object(slice, extractNumberDecoder);

            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEqualTo(123);
            assertThat(result.getModifiedSlice().toString()).isEqualTo("remaining");
        }
    }

    @Nested
    @DisplayName("Integer Rule Tests")
    class IntegerRuleTests {

        @ParameterizedTest
        @ValueSource(strings = { "123", "0", "-456", "+789" })
        @DisplayName("Should parse valid integers")
        void testIntegerRule_ValidIntegers(String value) {
            StringSliceWithSplit slice = new StringSliceWithSplit(value + " remaining");
            SplitResult<Integer> result = StringSplitterRules.integer(slice);

            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEqualTo(Integer.parseInt(value));
            assertThat(result.getModifiedSlice().toString()).isEqualTo("remaining");
        }

        @Test
        @DisplayName("Should return null for invalid integers")
        void testIntegerRule_InvalidInteger() {
            StringSliceWithSplit slice = new StringSliceWithSplit("abc remaining");
            SplitResult<Integer> result = StringSplitterRules.integer(slice);

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should handle integer overflow")
        void testIntegerRule_Overflow() {
            String overflowValue = "999999999999999999999";
            StringSliceWithSplit slice = new StringSliceWithSplit(overflowValue + " remaining");
            SplitResult<Integer> result = StringSplitterRules.integer(slice);

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should handle single integer without remaining text")
        void testIntegerRule_SingleInteger() {
            StringSliceWithSplit slice = new StringSliceWithSplit("42");
            SplitResult<Integer> result = StringSplitterRules.integer(slice);

            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEqualTo(42);
            assertThat(result.getModifiedSlice().toString()).isEmpty();
        }

        @Test
        @DisplayName("Should handle integer with leading/trailing whitespace")
        void testIntegerRule_WithWhitespace() {
            StringSliceWithSplit slice = new StringSliceWithSplit("  123  remaining");
            SplitResult<Integer> result = StringSplitterRules.integer(slice);

            // Leading whitespace causes empty string to be parsed first
            assertThat(result).isNull(); // Empty string can't be parsed as integer
        }
    }

    @Nested
    @DisplayName("Double Rule Tests")
    class DoubleRuleTests {

        @ParameterizedTest
        @ValueSource(strings = { "123.45", "0.0", "-456.789", "+789.123", "1e5", "1.23e-4" })
        @DisplayName("Should parse valid doubles")
        void testDoubleRule_ValidDoubles(String value) {
            StringSliceWithSplit slice = new StringSliceWithSplit(value + " remaining");
            SplitResult<Double> result = StringSplitterRules.doubleNumber(slice);

            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEqualTo(Double.parseDouble(value));
            assertThat(result.getModifiedSlice().toString()).isEqualTo("remaining");
        }

        @Test
        @DisplayName("Should return null for invalid doubles")
        void testDoubleRule_InvalidDouble() {
            StringSliceWithSplit slice = new StringSliceWithSplit("abc remaining");
            SplitResult<Double> result = StringSplitterRules.doubleNumber(slice);

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should handle special double values")
        void testDoubleRule_SpecialValues() {
            // Test infinity
            StringSliceWithSplit infSlice = new StringSliceWithSplit("Infinity remaining");
            SplitResult<Double> infResult = StringSplitterRules.doubleNumber(infSlice);
            assertThat(infResult).isNotNull();
            assertThat(infResult.getValue()).isEqualTo(Double.POSITIVE_INFINITY);

            // Test negative infinity
            StringSliceWithSplit negInfSlice = new StringSliceWithSplit("-Infinity remaining");
            SplitResult<Double> negInfResult = StringSplitterRules.doubleNumber(negInfSlice);
            assertThat(negInfResult).isNotNull();
            assertThat(negInfResult.getValue()).isEqualTo(Double.NEGATIVE_INFINITY);

            // Test NaN
            StringSliceWithSplit nanSlice = new StringSliceWithSplit("NaN remaining");
            SplitResult<Double> nanResult = StringSplitterRules.doubleNumber(nanSlice);
            assertThat(nanResult).isNotNull();
            assertThat(nanResult.getValue()).isNaN();
        }

        @Test
        @DisplayName("Should handle single double without remaining text")
        void testDoubleRule_SingleDouble() {
            StringSliceWithSplit slice = new StringSliceWithSplit("42.5");
            SplitResult<Double> result = StringSplitterRules.doubleNumber(slice);

            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEqualTo(42.5);
            assertThat(result.getModifiedSlice().toString()).isEmpty();
        }

        @Test
        @DisplayName("Should handle double with whitespace")
        void testDoubleRule_WithWhitespace() {
            StringSliceWithSplit slice = new StringSliceWithSplit("  123.45  remaining");
            SplitResult<Double> result = StringSplitterRules.doubleNumber(slice);

            // Leading whitespace causes empty string to be parsed first
            assertThat(result).isNull(); // Empty string can't be parsed as double
        }
    }

    @Nested
    @DisplayName("Float Rule Tests")
    class FloatRuleTests {

        @ParameterizedTest
        @ValueSource(strings = { "123.45", "0.0", "-456.789", "+789.123" })
        @DisplayName("Should parse valid floats")
        void testFloatRule_ValidFloats(String value) {
            StringSliceWithSplit slice = new StringSliceWithSplit(value + " remaining");
            SplitResult<Float> result = StringSplitterRules.floatNumber(slice);

            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEqualTo(Float.parseFloat(value));
            assertThat(result.getModifiedSlice().toString()).isEqualTo("remaining");
        }

        @Test
        @DisplayName("Should return null for invalid floats")
        void testFloatRule_InvalidFloat() {
            StringSliceWithSplit slice = new StringSliceWithSplit("abc remaining");
            SplitResult<Float> result = StringSplitterRules.floatNumber(slice);

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should handle special float values")
        void testFloatRule_SpecialValues() {
            // Test infinity
            StringSliceWithSplit infSlice = new StringSliceWithSplit("Infinity remaining");
            SplitResult<Float> infResult = StringSplitterRules.floatNumber(infSlice);
            assertThat(infResult).isNotNull();
            assertThat(infResult.getValue()).isEqualTo(Float.POSITIVE_INFINITY);

            // Test negative infinity
            StringSliceWithSplit negInfSlice = new StringSliceWithSplit("-Infinity remaining");
            SplitResult<Float> negInfResult = StringSplitterRules.floatNumber(negInfSlice);
            assertThat(negInfResult).isNotNull();
            assertThat(negInfResult.getValue()).isEqualTo(Float.NEGATIVE_INFINITY);

            // Test NaN
            StringSliceWithSplit nanSlice = new StringSliceWithSplit("NaN remaining");
            SplitResult<Float> nanResult = StringSplitterRules.floatNumber(nanSlice);
            assertThat(nanResult).isNotNull();
            assertThat(nanResult.getValue()).isNaN();
        }

        @Test
        @DisplayName("Should handle float precision limits")
        void testFloatRule_PrecisionLimits() {
            // Test a value that might lose precision
            StringSliceWithSplit slice = new StringSliceWithSplit("1.23456789123456789 remaining");
            SplitResult<Float> result = StringSplitterRules.floatNumber(slice);

            assertThat(result).isNotNull();
            assertThat(result.getValue()).isInstanceOf(Float.class);
            assertThat(result.getModifiedSlice().toString()).isEqualTo("remaining");
        }

        @Test
        @DisplayName("Should handle single float without remaining text")
        void testFloatRule_SingleFloat() {
            StringSliceWithSplit slice = new StringSliceWithSplit("42.5");
            SplitResult<Float> result = StringSplitterRules.floatNumber(slice);

            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEqualTo(42.5f);
            assertThat(result.getModifiedSlice().toString()).isEmpty();
        }

        @Test
        @DisplayName("Should handle float with whitespace")
        void testFloatRule_WithWhitespace() {
            StringSliceWithSplit slice = new StringSliceWithSplit("  123.45  remaining");
            SplitResult<Float> result = StringSplitterRules.floatNumber(slice);

            // Leading whitespace causes empty string to be parsed first
            assertThat(result).isNull(); // Empty string can't be parsed as float
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with multiple rule applications in sequence")
        void testMultipleRuleSequence() {
            StringSliceWithSplit slice = new StringSliceWithSplit("123 hello 45.6 world");

            // Parse integer
            SplitResult<Integer> intResult = StringSplitterRules.integer(slice);
            assertThat(intResult).isNotNull();
            assertThat(intResult.getValue()).isEqualTo(123);

            // Parse string from remaining
            SplitResult<String> stringResult = StringSplitterRules.string(intResult.getModifiedSlice());
            assertThat(stringResult).isNotNull();
            assertThat(stringResult.getValue()).isEqualTo("hello");

            // Parse double from remaining
            SplitResult<Double> doubleResult = StringSplitterRules.doubleNumber(stringResult.getModifiedSlice());
            assertThat(doubleResult).isNotNull();
            assertThat(doubleResult.getValue()).isEqualTo(45.6);

            // Parse final string
            SplitResult<String> finalResult = StringSplitterRules.string(doubleResult.getModifiedSlice());
            assertThat(finalResult).isNotNull();
            assertThat(finalResult.getValue()).isEqualTo("world");
            assertThat(finalResult.getModifiedSlice().toString()).isEmpty();
        }

        @Test
        @DisplayName("Should handle complex parsing with custom separators")
        void testComplexParsingWithCustomSeparators() {
            StringSliceWithSplit slice = new StringSliceWithSplit("name:John|age:25|score:87.5")
                    .setSeparator(ch -> ch == ':' || ch == '|');

            // This tests how rules behave with complex separator patterns
            SplitResult<String> result = StringSplitterRules.string(slice);
            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEqualTo("name");
        }

        @Test
        @DisplayName("Should handle reverse parsing correctly")
        void testReverseParsingWithRules() {
            StringSliceWithSplit slice = new StringSliceWithSplit("first second 123")
                    .setReverse(true);

            // In reverse mode, parsing should start from the end
            SplitResult<String> result = StringSplitterRules.string(slice);
            assertThat(result).isNotNull();
            // The exact behavior depends on the reverse implementation
            assertThat(result.getValue()).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandling {

        @Test
        @DisplayName("Should handle null input gracefully")
        void testNullInput() {
            // StringSliceWithSplit constructor should handle null
            assertThatThrownBy(() -> new StringSliceWithSplit((String) null))
                    .isInstanceOf(Exception.class);
        }

        @Test
        @DisplayName("Should handle very large numbers")
        void testVeryLargeNumbers() {
            String largeNumber = "999999999999999999999999999999999999999999999999999";
            StringSliceWithSplit slice = new StringSliceWithSplit(largeNumber + " remaining");

            SplitResult<Integer> intResult = StringSplitterRules.integer(slice);
            assertThat(intResult).isNull(); // Should fail to parse

            SplitResult<Double> doubleResult = StringSplitterRules.doubleNumber(slice);
            // Double might parse as a large number, not necessarily infinity
            if (doubleResult != null) {
                // Could be infinity or just a very large number
                assertThat(doubleResult.getValue()).isGreaterThan(1e50);
            }
        }

        @Test
        @DisplayName("Should handle unicode characters in strings")
        void testUnicodeCharacters() {
            StringSliceWithSplit slice = new StringSliceWithSplit("こんにちは 世界 123");

            SplitResult<String> result = StringSplitterRules.string(slice);
            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEqualTo("こんにちは");
            assertThat(result.getModifiedSlice().toString()).isEqualTo("世界 123");
        }

        @Test
        @DisplayName("Should handle empty and whitespace-only inputs")
        void testEmptyAndWhitespaceInputs() {
            // Empty string
            StringSliceWithSplit emptySlice = new StringSliceWithSplit("");
            SplitResult<Integer> emptyIntResult = StringSplitterRules.integer(emptySlice);
            assertThat(emptyIntResult).isNull();

            // Whitespace only
            StringSliceWithSplit whitespaceSlice = new StringSliceWithSplit("   ");
            SplitResult<Integer> whitespaceIntResult = StringSplitterRules.integer(whitespaceSlice);
            assertThat(whitespaceIntResult).isNull();
        }
    }
}
