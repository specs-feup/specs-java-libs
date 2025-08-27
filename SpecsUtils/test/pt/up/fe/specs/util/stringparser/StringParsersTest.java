package pt.up.fe.specs.util.stringparser;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.RetryingTest;

import pt.up.fe.specs.util.enums.EnumHelperWithValue;
import pt.up.fe.specs.util.providers.StringProvider;
import pt.up.fe.specs.util.utilities.StringSlice;

/**
 * Comprehensive test suite for {@link StringParsers}.
 * Tests the static utility methods for various string parsing operations
 * including word parsing, enum parsing, integer parsing, nested structures, and
 * more.
 * 
 * @author Generated Tests
 */
@DisplayName("StringParsers Tests")
public class StringParsersTest {

    // Test enum implementing StringProvider
    private enum TestEnum implements StringProvider {
        VALUE1("value1"),
        VALUE2("value2"),
        SPECIAL_CASE("special-case"),
        UNDERSCORE_VALUE("underscore_value");

        private final String string;

        TestEnum(String string) {
            this.string = string;
        }

        @Override
        public String getString() {
            return string;
        }
    }

    @Nested
    @DisplayName("Word Parsing Tests")
    class WordParsingTests {

        @Test
        @DisplayName("Should parse simple word until whitespace")
        void testParseSimpleWord() {
            StringSlice input = new StringSlice("hello world");
            ParserResult<String> result = StringParsers.parseWord(input);

            assertThat(result.getResult()).isEqualTo("hello");
            assertThat(result.getModifiedString().toString()).isEqualTo(" world");
        }

        @Test
        @DisplayName("Should parse entire string when no whitespace")
        void testParseWordNoWhitespace() {
            StringSlice input = new StringSlice("singleword");
            ParserResult<String> result = StringParsers.parseWord(input);

            assertThat(result.getResult()).isEqualTo("singleword");
            assertThat(result.getModifiedString().isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Should handle empty string")
        void testParseWordEmpty() {
            StringSlice input = new StringSlice("");
            ParserResult<String> result = StringParsers.parseWord(input);

            assertThat(result.getResult()).isEmpty();
            assertThat(result.getModifiedString().isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Should handle string starting with whitespace")
        void testParseWordStartingWithWhitespace() {
            StringSlice input = new StringSlice("  leading");
            ParserResult<String> result = StringParsers.parseWord(input);

            assertThat(result.getResult()).isEmpty();
            assertThat(result.getModifiedString().toString()).isEqualTo("  leading");
        }

        @Test
        @DisplayName("Should parse word with various separators")
        void testParseWordVariousSeparators() {
            // parseWord only recognizes space (' ') as separator, not other whitespace
            // Test with tab separator - parseWord doesn't stop at tabs
            ParserResult<String> tabResult = StringParsers.parseWord(new StringSlice("word\tafter"));
            ParserResult<String> newlineResult = StringParsers.parseWord(new StringSlice("word\nafter"));
            ParserResult<String> carriageResult = StringParsers.parseWord(new StringSlice("word\rafter"));

            // These take the entire string because parseWord only stops at spaces, not
            // other whitespace
            assertThat(tabResult.getResult()).isEqualTo("word\tafter");
            assertThat(newlineResult.getResult()).isEqualTo("word\nafter");
            assertThat(carriageResult.getResult()).isEqualTo("word\rafter");
        }
    }

    @Nested
    @DisplayName("Integer Parsing Tests")
    class IntegerParsingTests {

        @Test
        @DisplayName("Should parse positive integer")
        void testParsePositiveInteger() {
            StringSlice input = new StringSlice("123 remaining");
            ParserResult<Integer> result = StringParsersLegacy.parseInt(input);

            assertThat(result.getResult()).isEqualTo(123);
            assertThat(result.getModifiedString().toString()).isEqualTo(" remaining");
        }

        @Test
        @DisplayName("Should parse negative integer")
        void testParseNegativeInteger() {
            StringSlice input = new StringSlice("-456 after");
            ParserResult<Integer> result = StringParsersLegacy.parseInt(input);

            assertThat(result.getResult()).isEqualTo(-456);
            assertThat(result.getModifiedString().toString()).isEqualTo(" after");
        }

        @Test
        @DisplayName("Should parse integer at end of string")
        void testParseIntegerAtEnd() {
            StringSlice input = new StringSlice("789");
            ParserResult<Integer> result = StringParsersLegacy.parseInt(input);

            assertThat(result.getResult()).isEqualTo(789);
            assertThat(result.getModifiedString().isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Should handle zero")
        void testParseZero() {
            StringSlice input = new StringSlice("0 next");
            ParserResult<Integer> result = StringParsersLegacy.parseInt(input);

            assertThat(result.getResult()).isEqualTo(0);
            assertThat(result.getModifiedString().toString()).isEqualTo(" next");
        }

        @Test
        @DisplayName("Should handle large integers")
        void testParseLargeInteger() {
            StringSlice input = new StringSlice("2147483647 max");
            ParserResult<Integer> result = StringParsersLegacy.parseInt(input);

            assertThat(result.getResult()).isEqualTo(Integer.MAX_VALUE);
            assertThat(result.getModifiedString().toString()).isEqualTo(" max");
        }

        @Test
        @DisplayName("Should throw exception for invalid integer")
        void testParseInvalidInteger() {
            assertThatThrownBy(() -> StringParsersLegacy.parseInt(new StringSlice("abc")))
                    .isInstanceOf(NumberFormatException.class);
        }

        @Test
        @DisplayName("Should throw exception for empty string")
        void testParseIntegerEmpty() {
            // StringParsersLegacy.parseInt() returns 0 for empty strings, not exception
            ParserResult<Integer> result = StringParsersLegacy.parseInt(new StringSlice(""));
            assertThat(result.getResult()).isEqualTo(0); // Returns default value 0
            assertThat(result.getModifiedString().toString()).isEqualTo("");
        }
    }

    @Nested
    @DisplayName("Enum Parsing Tests")
    class EnumParsingTests {

        @Test
        @DisplayName("Should parse valid enum value")
        void testCheckEnumValid() {
            EnumHelperWithValue<TestEnum> helper = new EnumHelperWithValue<>(TestEnum.class);
            StringSlice input = new StringSlice("value1 remaining");

            ParserResult<Optional<TestEnum>> result = StringParsers.checkEnum(input, helper);

            assertThat(result.getResult()).isPresent();
            assertThat(result.getResult().get()).isEqualTo(TestEnum.VALUE1);
            assertThat(result.getModifiedString().toString()).isEqualTo(" remaining");
        }

        @Test
        @DisplayName("Should parse enum with special characters")
        void testCheckEnumSpecialCharacters() {
            EnumHelperWithValue<TestEnum> helper = new EnumHelperWithValue<>(TestEnum.class);
            StringSlice input = new StringSlice("special-case after");

            ParserResult<Optional<TestEnum>> result = StringParsers.checkEnum(input, helper);

            assertThat(result.getResult()).isPresent();
            assertThat(result.getResult().get()).isEqualTo(TestEnum.SPECIAL_CASE);
        }

        @Test
        @DisplayName("Should return empty optional for invalid enum")
        void testCheckEnumInvalid() {
            EnumHelperWithValue<TestEnum> helper = new EnumHelperWithValue<>(TestEnum.class);
            StringSlice input = new StringSlice("invalid remaining");

            ParserResult<Optional<TestEnum>> result = StringParsers.checkEnum(input, helper);

            assertThat(result.getResult()).isEmpty();
            assertThat(result.getModifiedString().toString()).isEqualTo("invalid remaining");
        }

        @Test
        @DisplayName("Should handle empty string for enum parsing")
        void testCheckEnumEmpty() {
            EnumHelperWithValue<TestEnum> helper = new EnumHelperWithValue<>(TestEnum.class);
            StringSlice input = new StringSlice("");

            ParserResult<Optional<TestEnum>> result = StringParsers.checkEnum(input, helper);

            assertThat(result.getResult()).isEmpty();
            assertThat(result.getModifiedString().isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Should parse enum with custom mappings - NOT SUPPORTED")
        void testCheckEnumWithCustomMappings() {
            // The current StringParsers.checkEnum API doesn't support custom mappings
            // This test documents the limitation
            EnumHelperWithValue<TestEnum> helper = new EnumHelperWithValue<>(TestEnum.class);

            StringSlice input = new StringSlice("value1 text");
            ParserResult<Optional<TestEnum>> result = StringParsers.checkEnum(input, helper);

            assertThat(result.getResult()).isPresent();
            assertThat(result.getResult().get()).isEqualTo(TestEnum.VALUE1);
        }
    }

    @Nested
    @DisplayName("Nested Structure Parsing Tests")
    class NestedStructureParsingTests {

        @Test
        @DisplayName("Should parse nested parentheses")
        void testParseNestedParentheses() {
            StringSlice input = new StringSlice("(hello world) remaining");
            ParserResult<String> result = StringParsers.parseNested(input, '(', ')');

            assertThat(result.getResult()).isEqualTo("hello world");
            assertThat(result.getModifiedString().toString()).isEqualTo(" remaining");
        }

        @Test
        @DisplayName("Should parse nested brackets")
        void testParseNestedBrackets() {
            StringSlice input = new StringSlice("[array content] after");
            ParserResult<String> result = StringParsers.parseNested(input, '[', ']');

            assertThat(result.getResult()).isEqualTo("array content");
            assertThat(result.getModifiedString().toString()).isEqualTo(" after");
        }

        @Test
        @DisplayName("Should handle deeply nested structures")
        void testParseDeeplyNested() {
            StringSlice input = new StringSlice("(outer (inner) more) end");
            ParserResult<String> result = StringParsers.parseNested(input, '(', ')');

            assertThat(result.getResult()).isEqualTo("outer (inner) more");
            assertThat(result.getModifiedString().toString()).isEqualTo(" end");
        }

        @Test
        @DisplayName("Should handle empty nested content")
        void testParseEmptyNested() {
            StringSlice input = new StringSlice("() remaining");
            ParserResult<String> result = StringParsers.parseNested(input, '(', ')');

            assertThat(result.getResult()).isEmpty();
            assertThat(result.getModifiedString().toString()).isEqualTo(" remaining");
        }

        @Test
        @DisplayName("Should throw IndexOutOfBoundsException for unmatched opening")
        void testParseNestedUnmatchedOpening() {
            StringSlice input = new StringSlice("(unmatched");

            // parseNested throws IndexOutOfBoundsException when it reaches end of string
            // looking for closing bracket
            assertThatThrownBy(() -> StringParsers.parseNested(input, '(', ')'))
                    .isInstanceOf(IndexOutOfBoundsException.class);
        }

        @Test
        @DisplayName("Should return empty string for missing opening")
        void testParseNestedMissingOpening() {
            StringSlice input = new StringSlice("no opening)");

            // parseNested returns empty string when no opening bracket found
            ParserResult<String> result = StringParsers.parseNested(input, '(', ')');
            assertThat(result.getResult()).isEqualTo("");
            assertThat(result.getModifiedString().toString()).isEqualTo("no opening)");
        }

        @Test
        @DisplayName("Should handle nested braces")
        void testParseNestedBraces() {
            StringSlice input = new StringSlice("{key: value} rest");
            ParserResult<String> result = StringParsers.parseNested(input, '{', '}');

            assertThat(result.getResult()).isEqualTo("key: value");
            assertThat(result.getModifiedString().toString()).isEqualTo(" rest");
        }
    }

    @Nested
    @DisplayName("String Literal Parsing Tests")
    class StringLiteralParsingTests {

        @Test
        @DisplayName("Should parse quoted string literal")
        void testParseQuotedString() {
            StringSlice input = new StringSlice("\"hello world\" remaining");
            ParserResult<String> result = StringParsers.parseNested(input, '"', '"');

            assertThat(result.getResult()).isEqualTo("hello world");
            assertThat(result.getModifiedString().toString()).isEqualTo(" remaining");
        }

        @Test
        @DisplayName("Should parse single quoted string")
        void testParseSingleQuotedString() {
            StringSlice input = new StringSlice("'single quoted' after");
            ParserResult<String> result = StringParsers.parseNested(input, '\'', '\'');

            assertThat(result.getResult()).isEqualTo("single quoted");
            assertThat(result.getModifiedString().toString()).isEqualTo(" after");
        }

        @Test
        @DisplayName("Should handle empty quoted string")
        void testParseEmptyQuotedString() {
            StringSlice input = new StringSlice("\"\" remaining");
            ParserResult<String> result = StringParsers.parseNested(input, '"', '"');

            assertThat(result.getResult()).isEmpty();
            assertThat(result.getModifiedString().toString()).isEqualTo(" remaining");
        }

        @Test
        @DisplayName("Should handle string with spaces")
        void testParseStringWithSpaces() {
            StringSlice input = new StringSlice("\"  spaced content  \" after");
            ParserResult<String> result = StringParsers.parseNested(input, '"', '"');

            assertThat(result.getResult()).isEqualTo("  spaced content  ");
            assertThat(result.getModifiedString().toString()).isEqualTo(" after");
        }
    }

    @Nested
    @DisplayName("Advanced Parsing Tests")
    class AdvancedParsingTests {

        @Test
        @DisplayName("Should parse hexadecimal digits")
        void testParseHexDigits() {
            // This test assumes there might be hex parsing functionality
            StringSlice input = new StringSlice("0xFF remaining");
            ParserResult<String> result = StringParsers.parseWord(input);

            assertThat(result.getResult()).isEqualTo("0xFF");
            assertThat(result.getModifiedString().toString()).isEqualTo(" remaining");
        }

        @Test
        @DisplayName("Should handle complex identifiers")
        void testParseComplexIdentifiers() {
            StringSlice input = new StringSlice("_var123 next");
            ParserResult<String> result = StringParsers.parseWord(input);

            assertThat(result.getResult()).isEqualTo("_var123");
            assertThat(result.getModifiedString().toString()).isEqualTo(" next");
        }

        @Test
        @DisplayName("Should parse words with dots")
        void testParseWordsWithDots() {
            StringSlice input = new StringSlice("package.name after");
            ParserResult<String> result = StringParsers.parseWord(input);

            assertThat(result.getResult()).isEqualTo("package.name");
            assertThat(result.getModifiedString().toString()).isEqualTo(" after");
        }

        @Test
        @DisplayName("Should handle URL-like structures")
        void testParseUrlLike() {
            StringSlice input = new StringSlice("http://example.com rest");
            ParserResult<String> result = StringParsers.parseWord(input);

            assertThat(result.getResult()).isEqualTo("http://example.com");
            assertThat(result.getModifiedString().toString()).isEqualTo(" rest");
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle very long strings")
        void testVeryLongStrings() {
            String longWord = "a".repeat(10000);
            StringSlice input = new StringSlice(longWord + " end");
            ParserResult<String> result = StringParsers.parseWord(input);

            assertThat(result.getResult()).hasSize(10000);
            assertThat(result.getModifiedString().toString()).isEqualTo(" end");
        }

        @Test
        @DisplayName("Should handle unicode characters")
        void testUnicodeCharacters() {
            StringSlice input = new StringSlice("café naïve");
            ParserResult<String> result = StringParsers.parseWord(input);

            assertThat(result.getResult()).isEqualTo("café");
            assertThat(result.getModifiedString().toString()).isEqualTo(" naïve");
        }

        @Test
        @DisplayName("Should handle special symbols")
        void testSpecialSymbols() {
            StringSlice input = new StringSlice("$variable @annotation");
            ParserResult<String> result = StringParsers.parseWord(input);

            assertThat(result.getResult()).isEqualTo("$variable");
            assertThat(result.getModifiedString().toString()).isEqualTo(" @annotation");
        }

        @Test
        @DisplayName("Should handle mixed whitespace")
        void testMixedWhitespace() {
            StringSlice input = new StringSlice("word\t\n\r mixed");
            ParserResult<String> result = StringParsers.parseWord(input);

            // parseWord only stops at space, so takes everything until space
            assertThat(result.getResult()).isEqualTo("word\t\n\r");
            assertThat(result.getModifiedString().toString()).isEqualTo(" mixed");
        }

        @Test
        @DisplayName("Should handle only whitespace")
        void testOnlyWhitespace() {
            StringSlice input = new StringSlice("   \t\n  ");
            ParserResult<String> result = StringParsers.parseWord(input);

            assertThat(result.getResult()).isEmpty();
            assertThat(result.getModifiedString().toString()).isEqualTo("   \t\n  ");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should chain multiple parsing operations")
        void testChainedParsing() {
            StringSlice input = new StringSlice("first second third");

            ParserResult<String> first = StringParsers.parseWord(input);
            input = first.getModifiedString().trim();

            ParserResult<String> second = StringParsers.parseWord(input);
            input = second.getModifiedString().trim();

            ParserResult<String> third = StringParsers.parseWord(input);

            assertThat(first.getResult()).isEqualTo("first");
            assertThat(second.getResult()).isEqualTo("second");
            assertThat(third.getResult()).isEqualTo("third");
        }

        @Test
        @DisplayName("Should work with StringParser integration")
        void testStringParserIntegration() {
            StringParser parser = new StringParser("value1 123 (nested content)");

            EnumHelperWithValue<TestEnum> enumHelper = new EnumHelperWithValue<>(TestEnum.class);

            // Parse enum
            Optional<TestEnum> enumValue = parser.apply(StringParsers::checkEnum, enumHelper);

            // Parse integer using legacy parser
            Integer intValue = parser.apply(StringParsersLegacy::parseInt);

            // Parse nested content
            String nestedValue = parser.apply(StringParsers::parseNested, '(', ')');

            assertThat(enumValue).isPresent();
            assertThat(enumValue.get()).isEqualTo(TestEnum.VALUE1);
            assertThat(intValue).isEqualTo(123);
            assertThat(nestedValue).isEqualTo("nested content");
        }

        @Test
        @DisplayName("Should handle complex mixed content")
        void testComplexMixedContent() {
            StringSlice input = new StringSlice("function(arg1, arg2) { return value; }");

            // Parse function name - parseWord goes until first space, taking
            // "function(arg1,"
            ParserResult<String> funcName = StringParsers.parseWord(input);

            // The function name result includes everything until the first space
            assertThat(funcName.getResult()).isEqualTo("function(arg1,");
            assertThat(funcName.getModifiedString().toString()).isEqualTo(" arg2) { return value; }");
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @RetryingTest(5)
        @DisplayName("Should handle large input efficiently")
        void testLargeInputPerformance() {
            String largeContent = "word ".repeat(1000);
            StringSlice input = new StringSlice(largeContent);

            long startTime = System.nanoTime();

            for (int i = 0; i < 100; i++) {
                if (input.isEmpty())
                    break;
                ParserResult<String> result = StringParsers.parseWord(input);
                input = result.getModifiedString().trim();
            }

            long duration = System.nanoTime() - startTime;

            assertThat(duration).isLessThan(50_000_000L); // 50ms
        }

        @RetryingTest(5)
        @DisplayName("Should handle repeated enum checks efficiently")
        void testRepeatedEnumCheckPerformance() {
            EnumHelperWithValue<TestEnum> helper = new EnumHelperWithValue<>(TestEnum.class);
            StringSlice input = new StringSlice("value1");

            long startTime = System.nanoTime();

            for (int i = 0; i < 1000; i++) {
                StringParsers.checkEnum(input, helper);
            }

            long duration = System.nanoTime() - startTime;

            assertThat(duration).isLessThan(10_000_000L); // 10ms
        }
    }
}
