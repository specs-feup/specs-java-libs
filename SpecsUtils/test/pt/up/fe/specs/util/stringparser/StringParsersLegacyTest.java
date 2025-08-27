package pt.up.fe.specs.util.stringparser;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.utilities.StringSlice;

/**
 * Comprehensive test suite for StringParsersLegacy utility class.
 * Tests legacy string parsing functionality including integer parsing,
 * parenthesis parsing, and other legacy operations.
 * 
 * @author Generated Tests
 */
@DisplayName("StringParsersLegacy Tests")
public class StringParsersLegacyTest {

    @Nested
    @DisplayName("Utility Method Tests")
    class UtilityMethodTests {

        @Test
        @DisplayName("Should clear StringSlice completely")
        void testClear() {
            StringSlice input = new StringSlice("test content to clear");
            ParserResult<String> result = StringParsersLegacy.clear(input);

            assertThat(result.getResult()).isEqualTo("test content to clear");
            assertThat(result.getModifiedString().toString()).isEqualTo("");
        }

        @Test
        @DisplayName("Should clear empty StringSlice")
        void testClearEmpty() {
            StringSlice input = new StringSlice("");
            ParserResult<String> result = StringParsersLegacy.clear(input);

            assertThat(result.getResult()).isEqualTo("");
            assertThat(result.getModifiedString().toString()).isEqualTo("");
        }

        @Test
        @DisplayName("Should clear StringSlice with special characters")
        void testClearSpecialCharacters() {
            StringSlice input = new StringSlice("!@#$%^&*()_+{}[]|\\:\";<>?,./ ");
            ParserResult<String> result = StringParsersLegacy.clear(input);

            assertThat(result.getResult()).isEqualTo("!@#$%^&*()_+{}[]|\\:\";<>?,./ ");
            assertThat(result.getModifiedString().toString()).isEqualTo("");
        }
    }

    @Nested
    @DisplayName("Parenthesis Parsing Tests")
    class ParenthesisParsingTests {

        @Test
        @DisplayName("Should parse simple parentheses content")
        void testParseSimpleParentheses() {
            StringSlice input = new StringSlice("(content) remainder");
            ParserResult<String> result = StringParsersLegacy.parseParenthesis(input);

            assertThat(result.getResult()).isEqualTo("content");
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should parse empty parentheses")
        void testParseEmptyParentheses() {
            StringSlice input = new StringSlice("() remainder");
            ParserResult<String> result = StringParsersLegacy.parseParenthesis(input);

            assertThat(result.getResult()).isEqualTo("");
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should parse nested parentheses")
        void testParseNestedParentheses() {
            StringSlice input = new StringSlice("(outer (inner) content) remainder");
            ParserResult<String> result = StringParsersLegacy.parseParenthesis(input);

            assertThat(result.getResult()).isEqualTo("outer (inner) content");
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should parse parentheses at end of string")
        void testParseParenthesesAtEnd() {
            StringSlice input = new StringSlice("(final content)");
            ParserResult<String> result = StringParsersLegacy.parseParenthesis(input);

            assertThat(result.getResult()).isEqualTo("final content");
            assertThat(result.getModifiedString().toString()).isEqualTo("");
        }

        @Test
        @DisplayName("Should handle parentheses with special characters")
        void testParseParenthesesWithSpecialChars() {
            StringSlice input = new StringSlice("(content with $pecial ch@rs!) remainder");
            ParserResult<String> result = StringParsersLegacy.parseParenthesis(input);

            assertThat(result.getResult()).isEqualTo("content with $pecial ch@rs!");
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
        }
    }

    @Nested
    @DisplayName("Integer Parsing Tests")
    class IntegerParsingTests {

        @Test
        @DisplayName("Should parse positive integer")
        void testParsePositiveInteger() {
            StringSlice input = new StringSlice("123 remainder");
            ParserResult<Integer> result = StringParsersLegacy.parseInt(input);

            assertThat(result.getResult()).isEqualTo(123);
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should parse negative integer")
        void testParseNegativeInteger() {
            StringSlice input = new StringSlice("-456 remainder");
            ParserResult<Integer> result = StringParsersLegacy.parseInt(input);

            assertThat(result.getResult()).isEqualTo(-456);
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should parse zero")
        void testParseZero() {
            StringSlice input = new StringSlice("0 remainder");
            ParserResult<Integer> result = StringParsersLegacy.parseInt(input);

            assertThat(result.getResult()).isEqualTo(0);
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should parse hexadecimal integer")
        void testParseHexadecimalInteger() {
            StringSlice input = new StringSlice("0xFF remainder");
            ParserResult<Integer> result = StringParsersLegacy.parseInt(input);

            assertThat(result.getResult()).isEqualTo(255);
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should parse octal integer")
        void testParseOctalInteger() {
            StringSlice input = new StringSlice("0777 remainder");
            ParserResult<Integer> result = StringParsersLegacy.parseInt(input);

            assertThat(result.getResult()).isEqualTo(511);
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should parse integer at end of string")
        void testParseIntegerAtEnd() {
            StringSlice input = new StringSlice("789");
            ParserResult<Integer> result = StringParsersLegacy.parseInt(input);

            assertThat(result.getResult()).isEqualTo(789);
            assertThat(result.getModifiedString().toString()).isEqualTo("");
        }

        @Test
        @DisplayName("Should handle empty string gracefully")
        void testParseIntegerEmpty() {
            StringSlice input = new StringSlice("");
            ParserResult<Integer> result = StringParsersLegacy.parseInt(input);

            // Based on the implementation, parseInt returns 0 for empty strings
            assertThat(result.getResult()).isEqualTo(0);
            assertThat(result.getModifiedString().toString()).isEqualTo("");
        }

        @Test
        @DisplayName("Should parse large integer values")
        void testParseLargeInteger() {
            StringSlice input = new StringSlice("2147483647 remainder");
            ParserResult<Integer> result = StringParsersLegacy.parseInt(input);

            assertThat(result.getResult()).isEqualTo(Integer.MAX_VALUE);
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should handle invalid integer gracefully")
        void testParseInvalidInteger() {
            StringSlice input = new StringSlice("invalid123 remainder");

            assertThatThrownBy(() -> StringParsersLegacy.parseInt(input))
                    .isInstanceOf(NumberFormatException.class);
        }
    }

    @Nested
    @DisplayName("Decoded Word Parsing Tests")
    class DecodedWordParsingTests {

        @Test
        @DisplayName("Should apply decoder function to parsed word")
        void testParseDecodedWord() {
            StringSlice input = new StringSlice("hello world");
            ParserResult<String> result = StringParsersLegacy.parseDecodedWord(input,
                    String::toUpperCase, "default");

            assertThat(result.getResult()).isEqualTo("HELLO");
            assertThat(result.getModifiedString().toString()).isEqualTo(" world");
        }

        @Test
        @DisplayName("Should use empty value for empty word")
        void testParseDecodedWordEmpty() {
            StringSlice input = new StringSlice("");
            ParserResult<String> result = StringParsersLegacy.parseDecodedWord(input,
                    String::toUpperCase, "DEFAULT");

            assertThat(result.getResult()).isEqualTo("DEFAULT");
            assertThat(result.getModifiedString().toString()).isEqualTo("");
        }

        @Test
        @DisplayName("Should apply numeric decoder")
        void testParseDecodedWordNumeric() {
            StringSlice input = new StringSlice("42 remainder");
            ParserResult<Integer> result = StringParsersLegacy.parseDecodedWord(input,
                    Integer::parseInt, -1);

            assertThat(result.getResult()).isEqualTo(42);
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should handle decoder exceptions")
        void testParseDecodedWordException() {
            StringSlice input = new StringSlice("notanumber remainder");

            assertThatThrownBy(() -> StringParsersLegacy.parseDecodedWord(input,
                    Integer::parseInt, -1))
                    .isInstanceOf(NumberFormatException.class);
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle very long input strings")
        void testVeryLongStrings() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 10000; i++) {
                sb.append("a");
            }
            String longString = sb.toString();

            StringSlice input = new StringSlice("(" + longString + ") remainder");
            ParserResult<String> result = StringParsersLegacy.parseParenthesis(input);

            assertThat(result.getResult()).isEqualTo(longString);
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should handle Unicode characters")
        void testUnicodeCharacters() {
            StringSlice input = new StringSlice("(héllo wörld 日本語) remainder");
            ParserResult<String> result = StringParsersLegacy.parseParenthesis(input);

            assertThat(result.getResult()).isEqualTo("héllo wörld 日本語");
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should handle special whitespace characters")
        void testSpecialWhitespace() {
            StringSlice input = new StringSlice("(content\t\n\r) remainder");
            ParserResult<String> result = StringParsersLegacy.parseParenthesis(input);

            assertThat(result.getResult()).isEqualTo("content\t\n\r");
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should handle deeply nested parentheses")
        void testDeeplyNestedParentheses() {
            StringSlice input = new StringSlice("(a(b(c(d(e)f)g)h)i) remainder");
            ParserResult<String> result = StringParsersLegacy.parseParenthesis(input);

            assertThat(result.getResult()).isEqualTo("a(b(c(d(e)f)g)h)i");
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should chain multiple legacy operations")
        void testChainedLegacyOperations() {
            StringSlice input = new StringSlice("(123) remainder content");

            // Parse parentheses first
            ParserResult<String> parenthesesResult = StringParsersLegacy.parseParenthesis(input);
            assertThat(parenthesesResult.getResult()).isEqualTo("123");

            // Parse integer from parentheses content
            StringSlice intInput = new StringSlice(parenthesesResult.getResult());
            ParserResult<Integer> intResult = StringParsersLegacy.parseInt(intInput);
            assertThat(intResult.getResult()).isEqualTo(123);

            // Verify remaining content
            assertThat(parenthesesResult.getModifiedString().toString()).isEqualTo(" remainder content");
        }

        @Test
        @DisplayName("Should handle complex parsing scenarios")
        void testComplexMixedContent() {
            StringSlice input = new StringSlice("(0xFF) (nested (content)) (42)");

            // Parse first parentheses (hex)
            ParserResult<String> hex = StringParsersLegacy.parseParenthesis(input);
            assertThat(hex.getResult()).isEqualTo("0xFF");

            // Parse second parentheses (nested)
            ParserResult<String> nested = StringParsersLegacy.parseParenthesis(hex.getModifiedString().trim());
            assertThat(nested.getResult()).isEqualTo("nested (content)");

            // Parse third parentheses (decimal)
            ParserResult<String> decimal = StringParsersLegacy.parseParenthesis(nested.getModifiedString().trim());
            assertThat(decimal.getResult()).isEqualTo("42");
        }

        @Test
        @DisplayName("Should work with StringParser integration")
        void testStringParserIntegration() {
            StringSlice input = new StringSlice("prefix (content) suffix");

            // Use StringParsers.parseWord to get prefix
            ParserResult<String> wordResult = StringParsers.parseWord(input);
            // parseWord only stops at spaces, so it takes "prefix"
            assertThat(wordResult.getResult()).isEqualTo("prefix");

            // Use StringParsersLegacy to parse parentheses from remaining
            StringSlice remaining = wordResult.getModifiedString().trim();
            ParserResult<String> parenthesesResult = StringParsersLegacy.parseParenthesis(remaining);
            assertThat(parenthesesResult.getResult()).isEqualTo("content");
            assertThat(parenthesesResult.getModifiedString().toString()).isEqualTo(" suffix");
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should handle large input efficiently")
        void testLargeInputPerformance() {
            StringBuilder sb = new StringBuilder("(");
            for (int i = 0; i < 50000; i++) {
                sb.append("content");
            }
            sb.append(") remainder");

            StringSlice input = new StringSlice(sb.toString());

            long startTime = System.nanoTime();
            ParserResult<String> result = StringParsersLegacy.parseParenthesis(input);
            long duration = System.nanoTime() - startTime;

            assertThat(result.getResult()).hasSize(350000); // 50000 * 7 chars
            assertThat(duration).isLessThan(100_000_000L); // 100ms
        }

        @Test
        @DisplayName("Should handle repeated parsing efficiently")
        void testRepeatedParsingPerformance() {
            StringSlice input = new StringSlice("(content) remainder");

            long startTime = System.nanoTime();

            for (int i = 0; i < 10000; i++) {
                StringParsersLegacy.parseParenthesis(new StringSlice(input));
            }

            long duration = System.nanoTime() - startTime;

            assertThat(duration).isLessThan(50_000_000L); // 50ms
        }

        @Test
        @DisplayName("Should handle repeated integer parsing efficiently")
        void testRepeatedIntegerParsingPerformance() {
            StringSlice input = new StringSlice("12345 remainder");

            long startTime = System.nanoTime();

            for (int i = 0; i < 10000; i++) {
                StringParsersLegacy.parseInt(new StringSlice(input));
            }

            long duration = System.nanoTime() - startTime;

            assertThat(duration).isLessThan(100_000_000L); // 100ms
        }
    }

    @Nested
    @DisplayName("Hex Parsing Tests")
    class HexParsingTests {

        @Test
        @DisplayName("Should parse hexadecimal values")
        void testParseHex_ValidHex_ReturnsCorrectValue() {
            StringSlice input = new StringSlice("0xFF remainder");
            ParserResult<Long> result = StringParsersLegacy.parseHex(input);

            assertThat(result.getResult()).isEqualTo(255L);
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should handle hex without 0x prefix")
        void testParseHex_NoPrefix_ReturnsMinusOne() {
            StringSlice input = new StringSlice("FF remainder");
            ParserResult<Long> result = StringParsersLegacy.parseHex(input);

            assertThat(result.getResult()).isEqualTo(-1L);
            assertThat(result.getModifiedString().toString()).isEqualTo("FF remainder");
        }

        @Test
        @DisplayName("Should parse large hex values")
        void testParseHex_LargeValues_ReturnsCorrectValue() {
            StringSlice input = new StringSlice("0x1ABCDEF remainder");
            ParserResult<Long> result = StringParsersLegacy.parseHex(input);

            assertThat(result.getResult()).isEqualTo(0x1ABCDEFL);
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should parse zero hex value")
        void testParseHex_Zero_ReturnsZero() {
            StringSlice input = new StringSlice("0x0 remainder");
            ParserResult<Long> result = StringParsersLegacy.parseHex(input);

            assertThat(result.getResult()).isEqualTo(0L);
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should handle reverse hex parsing")
        void testReverseHex_ValidHex_ReturnsCorrectValue() {
            StringSlice input = new StringSlice("some text 0xFF");
            ParserResult<Long> result = StringParsersLegacy.reverseHex(input);

            assertThat(result.getResult()).isEqualTo(255L);
            assertThat(result.getModifiedString().toString()).isEqualTo("some text");
        }

        @Test
        @DisplayName("Should handle reverse hex without space - returns failure")
        void testReverseHex_NoSpace_ReturnsFailure() {
            StringSlice input = new StringSlice("0x123");
            ParserResult<Long> result = StringParsersLegacy.reverseHex(input);

            // This is buggy behavior - the method fails when there's no space
            // because it tries to extract from position 1, getting "x123" instead of "0x123"
            assertThat(result.getResult()).isEqualTo(-1L);
            assertThat(result.getModifiedString().toString()).isEqualTo("0x123"); // String unchanged on failure
        }
    }

    @Nested
    @DisplayName("String Validation Tests")
    class StringValidationTests {

        @Test
        @DisplayName("Should check string starts with prefix")
        void testCheckStringStarts_ValidPrefix_ReturnsTrue() {
            StringSlice input = new StringSlice("prefix remainder");
            ParserResult<Boolean> result = StringParsersLegacy.checkStringStarts(input, "prefix");

            assertThat(result.getResult()).isTrue();
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should handle case-insensitive prefix check")
        void testCheckStringStarts_CaseInsensitive_ReturnsTrue() {
            StringSlice input = new StringSlice("PREFIX remainder");
            ParserResult<Boolean> result = StringParsersLegacy.checkStringStarts(input, "prefix", false);

            assertThat(result.getResult()).isTrue();
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should return false for non-matching prefix")
        void testCheckStringStarts_NonMatching_ReturnsFalse() {
            StringSlice input = new StringSlice("different remainder");
            ParserResult<Boolean> result = StringParsersLegacy.checkStringStarts(input, "prefix");

            assertThat(result.getResult()).isFalse();
            assertThat(result.getModifiedString().toString()).isEqualTo("different remainder");
        }

        @Test
        @DisplayName("Should check string ends with suffix")
        void testCheckStringEnds_ValidSuffix_ReturnsTrue() {
            StringSlice input = new StringSlice("beginning suffix");
            ParserResult<Boolean> result = StringParsersLegacy.checkStringEnds(input, "suffix");

            assertThat(result.getResult()).isTrue();
            assertThat(result.getModifiedString().toString()).isEqualTo("beginning ");
        }

        @Test
        @DisplayName("Should return false for non-matching suffix")
        void testCheckStringEnds_NonMatching_ReturnsFalse() {
            StringSlice input = new StringSlice("beginning different");
            ParserResult<Boolean> result = StringParsersLegacy.checkStringEnds(input, "suffix");

            assertThat(result.getResult()).isFalse();
            assertThat(result.getModifiedString().toString()).isEqualTo("beginning different");
        }

        @Test
        @DisplayName("Should ensure string starts with prefix")
        void testEnsureStringStarts_ValidPrefix_ReturnsTrue() {
            StringSlice input = new StringSlice("prefix remainder");
            ParserResult<Boolean> result = StringParsersLegacy.ensureStringStarts(input, "prefix");

            assertThat(result.getResult()).isTrue();
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should throw exception for non-matching ensure prefix")
        void testEnsureStringStarts_NonMatching_ThrowsException() {
            StringSlice input = new StringSlice("different remainder");

            assertThatThrownBy(() -> StringParsersLegacy.ensureStringStarts(input, "prefix"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Expected string to start with 'prefix'");
        }

        @Test
        @DisplayName("Should check word boundaries")
        void testCheckWord_ValidWord_ReturnsTrue() {
            StringSlice input = new StringSlice("word remainder");
            ParserResult<Boolean> result = StringParsersLegacy.checkWord(input, "word");

            assertThat(result.getResult()).isTrue();
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should handle word at end of string")
        void testCheckWord_WordAtEnd_ReturnsTrue() {
            StringSlice input = new StringSlice("word");
            ParserResult<Boolean> result = StringParsersLegacy.checkWord(input, "word");

            assertThat(result.getResult()).isTrue();
            assertThat(result.getModifiedString().toString()).isEqualTo("");
        }

        @Test
        @DisplayName("Should return false for partial word match")
        void testCheckWord_PartialMatch_ReturnsFalse() {
            StringSlice input = new StringSlice("wordy remainder");
            ParserResult<Boolean> result = StringParsersLegacy.checkWord(input, "word");

            assertThat(result.getResult()).isFalse();
            assertThat(result.getModifiedString().toString()).isEqualTo("wordy remainder");
        }

        @Test
        @DisplayName("Should check last string in input")
        void testCheckLastString_ValidLastWord_ReturnsTrue() {
            StringSlice input = new StringSlice("beginning middle last");
            ParserResult<Boolean> result = StringParsersLegacy.checkLastString(input, "last");

            assertThat(result.getResult()).isTrue();
            assertThat(result.getModifiedString().toString()).isEqualTo("beginning middle ");
        }

        @Test
        @DisplayName("Should handle single word for last string check")
        void testCheckLastString_SingleWord_ReturnsTrue() {
            StringSlice input = new StringSlice("word");
            ParserResult<Boolean> result = StringParsersLegacy.checkLastString(input, "word");

            assertThat(result.getResult()).isTrue();
            assertThat(result.getModifiedString().toString()).isEqualTo("");
        }
    }

    @Nested
    @DisplayName("Arrow Parsing Tests")
    class ArrowParsingTests {

        @Test
        @DisplayName("Should parse arrow operator")
        void testCheckArrow_ArrowOperator_ReturnsTrue() {
            StringSlice input = new StringSlice("-> remainder");
            ParserResult<Boolean> result = StringParsersLegacy.checkArrow(input);

            assertThat(result.getResult()).isTrue();
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should parse dot operator")
        void testCheckArrow_DotOperator_ReturnsFalse() {
            StringSlice input = new StringSlice(". remainder");
            ParserResult<Boolean> result = StringParsersLegacy.checkArrow(input);

            assertThat(result.getResult()).isFalse();
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should throw exception for invalid operator")
        void testCheckArrow_InvalidOperator_ThrowsException() {
            StringSlice input = new StringSlice("+ remainder");

            assertThatThrownBy(() -> StringParsersLegacy.checkArrow(input))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Expected string to start with either -> or .");
        }
    }

    @Nested
    @DisplayName("Reverse Nested Parsing Tests")
    class ReverseNestedParsingTests {

        @Test
        @DisplayName("Should parse reverse nested content")
        void testReverseNested_ValidNested_ReturnsContent() {
            StringSlice input = new StringSlice("prefix <content>");
            ParserResult<String> result = StringParsersLegacy.reverseNested(input, '<', '>');

            assertThat(result.getResult()).isEqualTo("content");
            assertThat(result.getModifiedString().toString()).isEqualTo("prefix ");
        }

        @Test
        @DisplayName("Should handle nested reverse content")
        void testReverseNested_NestedContent_ReturnsContent() {
            StringSlice input = new StringSlice("prefix <outer <inner> content>");
            ParserResult<String> result = StringParsersLegacy.reverseNested(input, '<', '>');

            assertThat(result.getResult()).isEqualTo("outer <inner> content");
            assertThat(result.getModifiedString().toString()).isEqualTo("prefix ");
        }

        @Test
        @DisplayName("Should return empty for no closing delimiter")
        void testReverseNested_NoClosing_ReturnsEmpty() {
            StringSlice input = new StringSlice("prefix content");
            ParserResult<String> result = StringParsersLegacy.reverseNested(input, '<', '>');

            assertThat(result.getResult()).isEqualTo("");
            assertThat(result.getModifiedString().toString()).isEqualTo("prefix content");
        }

        @Test
        @DisplayName("Should handle single character content")
        void testReverseNested_SingleChar_ReturnsContent() {
            StringSlice input = new StringSlice("prefix <x>");
            ParserResult<String> result = StringParsersLegacy.reverseNested(input, '<', '>');

            assertThat(result.getResult()).isEqualTo("x");
            assertThat(result.getModifiedString().toString()).isEqualTo("prefix ");
        }
    }

    @Nested
    @DisplayName("Prime Separated Parsing Tests")
    class PrimeSeparatedParsingTests {

        @Test
        @DisplayName("Should parse prime-separated single element")
        void testParsePrimesSeparated_SingleElement_ReturnsCorrectList() {
            StringSlice input = new StringSlice("'element' remainder");
            ParserResult<List<String>> result = StringParsersLegacy.parsePrimesSeparatedByString(input, ",");

            assertThat(result.getResult()).containsExactly("element");
            assertThat(result.getModifiedString().toString()).isEqualTo("remainder");
        }

        @Test
        @DisplayName("Should parse prime-separated multiple elements")
        void testParsePrimesSeparated_MultipleElements_ReturnsCorrectList() {
            StringSlice input = new StringSlice("'first','second','third' remainder");
            ParserResult<List<String>> result = StringParsersLegacy.parsePrimesSeparatedByString(input, ",");

            assertThat(result.getResult()).containsExactly("first", "second", "third");
            assertThat(result.getModifiedString().toString()).isEqualTo("remainder");
        }

        @Test
        @DisplayName("Should handle empty prime-separated input")
        void testParsePrimesSeparated_EmptyInput_ReturnsEmptyList() {
            StringSlice input = new StringSlice("");
            ParserResult<List<String>> result = StringParsersLegacy.parsePrimesSeparatedByString(input, ",");

            assertThat(result.getResult()).isEmpty();
            assertThat(result.getModifiedString().toString()).isEqualTo("");
        }

        @Test
        @DisplayName("Should throw exception for non-prime start")
        void testParsePrimesSeparated_NonPrimeStart_ThrowsException() {
            StringSlice input = new StringSlice("element remainder");

            assertThatThrownBy(() -> StringParsersLegacy.parsePrimesSeparatedByString(input, ","))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Given string does not start with quote");
        }

        @Test
        @DisplayName("Should handle different separators")
        void testParsePrimesSeparated_DifferentSeparator_ReturnsCorrectList() {
            StringSlice input = new StringSlice("'a';'b';'c' remainder");
            ParserResult<List<String>> result = StringParsersLegacy.parsePrimesSeparatedByString(input, ";");

            assertThat(result.getResult()).containsExactly("a", "b", "c");
            assertThat(result.getModifiedString().toString()).isEqualTo("remainder");
        }
    }

    @Nested
    @DisplayName("Remaining String Tests")  
    class RemainingStringTests {

        @Test
        @DisplayName("Should parse remaining string")
        void testParseRemaining_StandardInput_ReturnsCorrectResult() {
            StringSlice input = new StringSlice("content to parse");
            ParserResult<String> result = StringParsersLegacy.parseRemaining(input);

            assertThat(result.getResult()).isEqualTo("content to parse");
            assertThat(result.getModifiedString().toString()).isEqualTo("");
        }

        @Test
        @DisplayName("Should handle empty remaining string")
        void testParseRemaining_EmptyInput_ReturnsEmpty() {
            StringSlice input = new StringSlice("");
            ParserResult<String> result = StringParsersLegacy.parseRemaining(input);

            assertThat(result.getResult()).isEqualTo("");
            assertThat(result.getModifiedString().toString()).isEqualTo("");
        }

        @Test
        @DisplayName("Should parse very long remaining string")
        void testParseRemaining_LongInput_ReturnsCorrectResult() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                sb.append("content ");
            }
            String longString = sb.toString().trim();
            
            StringSlice input = new StringSlice(longString);
            ParserResult<String> result = StringParsersLegacy.parseRemaining(input);

            assertThat(result.getResult()).isEqualTo(longString);
            assertThat(result.getModifiedString().toString()).isEqualTo("");
        }
    }
}
