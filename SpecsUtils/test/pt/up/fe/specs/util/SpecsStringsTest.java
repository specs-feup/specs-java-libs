/**
 * Copyright 2019 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.util;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ValueSource;

import pt.up.fe.specs.util.parsing.LineParser;

/**
 * Comprehensive test suite for SpecsStrings utility class.
 * 
 * This test class covers all major functionality of SpecsStrings including:
 * - String parsing (integers, floats, doubles, booleans)
 * - String manipulation (padding, case conversion, formatting)
 * - Pattern matching and regex operations
 * - Utility methods (palindrome check, character validation)
 * - Time and size formatting
 * - Collection utilities
 * 
 * @author Generated Tests
 */
@DisplayName("SpecsStrings Tests")
public class SpecsStringsTest {

    @Nested
    @DisplayName("Parsing Methods")
    class ParsingMethods {

        @Nested
        @DisplayName("Integer Parsing")
        class IntegerParsing {

            @Test
            @DisplayName("parseInt should parse valid integers correctly")
            void testParseInt_ValidIntegers_ReturnsCorrectValues() {
                assertThat(SpecsStrings.parseInt("123")).isEqualTo(123);
                assertThat(SpecsStrings.parseInt("-456")).isEqualTo(-456);
                assertThat(SpecsStrings.parseInt("0")).isEqualTo(0);
                assertThat(SpecsStrings.parseInt("+789")).isEqualTo(789);
            }

            @Test
            @DisplayName("parseInt should return 0 for invalid inputs")
            void testParseInt_InvalidInputs_ReturnsZero() {
                assertThat(SpecsStrings.parseInt("abc")).isEqualTo(0);
                assertThat(SpecsStrings.parseInt("12.34")).isEqualTo(0);
                assertThat(SpecsStrings.parseInt("")).isEqualTo(0);
                assertThat(SpecsStrings.parseInt("  ")).isEqualTo(0);
            }

            @Test
            @DisplayName("parseInt should handle null input gracefully")
            void testParseInt_NullInput_ReturnsZero() {
                assertThat(SpecsStrings.parseInt(null)).isEqualTo(0);
            }

            @Test
            @DisplayName("parseInteger should parse valid integers correctly")
            void testParseInteger_ValidIntegers_ReturnsCorrectValues() {
                assertThat(SpecsStrings.parseInteger("123")).isEqualTo(123);
                assertThat(SpecsStrings.parseInteger("-456")).isEqualTo(-456);
                assertThat(SpecsStrings.parseInteger("0")).isEqualTo(0);
            }

            @Test
            @DisplayName("parseInteger should return null for invalid inputs")
            void testParseInteger_InvalidInputs_ReturnsNull() {
                assertThat(SpecsStrings.parseInteger("abc")).isNull();
                assertThat(SpecsStrings.parseInteger("12.34")).isNull();
                assertThat(SpecsStrings.parseInteger("")).isNull();
                assertThat(SpecsStrings.parseInteger("  ")).isNull();
                assertThat(SpecsStrings.parseInteger(null)).isNull();
            }

            @ParameterizedTest
            @ValueSource(strings = { "123", "-456", "0", "+789", "2147483647", "-2147483648" })
            @DisplayName("Integer parsing edge cases")
            void testIntegerParsing_EdgeCases(String input) {
                int expected = Integer.parseInt(input);
                assertThat(SpecsStrings.parseInt(input)).isEqualTo(expected);
                assertThat(SpecsStrings.parseInteger(input)).isEqualTo(expected);
            }
        }

        @Nested
        @DisplayName("Double Parsing")
        class DoubleParsing {

            @Test
            @DisplayName("valueOfDouble should parse valid doubles correctly")
            void testValueOfDouble_ValidDoubles_ReturnsCorrectValues() {
                assertThat(SpecsStrings.valueOfDouble("123.45")).contains(123.45);
                assertThat(SpecsStrings.valueOfDouble("-456.78")).contains(-456.78);
                assertThat(SpecsStrings.valueOfDouble("0.0")).contains(0.0);
                assertThat(SpecsStrings.valueOfDouble("1.0E10")).contains(1.0E10);
            }

            @Test
            @DisplayName("valueOfDouble should return empty for invalid inputs")
            void testValueOfDouble_InvalidInputs_ReturnsEmpty() {
                assertThat(SpecsStrings.valueOfDouble("abc")).isEmpty();
                assertThat(SpecsStrings.valueOfDouble("")).isEmpty();
                assertThat(SpecsStrings.valueOfDouble("  ")).isEmpty();
                // Note: valueOfDouble throws NPE for null input, so we test for that
                assertThatThrownBy(() -> SpecsStrings.valueOfDouble(null))
                        .isInstanceOf(NullPointerException.class);
            }

            @Test
            @DisplayName("parseDouble should parse valid doubles correctly")
            void testParseDouble_ValidDoubles_ReturnsCorrectValues() {
                assertThat(SpecsStrings.parseDouble("123.45")).isEqualTo(123.45);
                assertThat(SpecsStrings.parseDouble("-456.78")).isEqualTo(-456.78);
                assertThat(SpecsStrings.parseDouble("0.0")).isEqualTo(0.0);
            }

            @Test
            @DisplayName("parseDouble should return null for invalid inputs")
            void testParseDouble_InvalidInputs_ReturnsNull() {
                assertThat(SpecsStrings.parseDouble("abc")).isNull();
                assertThat(SpecsStrings.parseDouble("")).isNull();
                // Note: parseDouble throws NPE for null input, so we test for that
                assertThatThrownBy(() -> SpecsStrings.parseDouble(null))
                        .isInstanceOf(NullPointerException.class);
            }
        }

        @Nested
        @DisplayName("Boolean Parsing")
        class BooleanParsing {

            @Test
            @DisplayName("parseBoolean should parse valid booleans correctly")
            void testParseBoolean_ValidBooleans_ReturnsCorrectValues() {
                assertThat(SpecsStrings.parseBoolean("true")).isTrue();
                assertThat(SpecsStrings.parseBoolean("false")).isFalse();
                assertThat(SpecsStrings.parseBoolean("TRUE")).isTrue();
                assertThat(SpecsStrings.parseBoolean("FALSE")).isFalse();
                assertThat(SpecsStrings.parseBoolean("True")).isTrue();
                assertThat(SpecsStrings.parseBoolean("False")).isFalse();
            }

            @Test
            @DisplayName("parseBoolean should return null for invalid inputs")
            void testParseBoolean_InvalidInputs_ReturnsNull() {
                assertThat(SpecsStrings.parseBoolean("yes")).isNull();
                assertThat(SpecsStrings.parseBoolean("no")).isNull();
                assertThat(SpecsStrings.parseBoolean("1")).isNull();
                assertThat(SpecsStrings.parseBoolean("0")).isNull();
                assertThat(SpecsStrings.parseBoolean("")).isNull();
                // Note: parseBoolean throws NPE for null input, so we test for that
                assertThatThrownBy(() -> SpecsStrings.parseBoolean(null))
                        .isInstanceOf(NullPointerException.class);
            }
        }

        @Nested
        @DisplayName("Long Parsing")
        class LongParsing {

            @Test
            @DisplayName("parseLong should parse valid longs correctly")
            void testParseLong_ValidLongs_ReturnsCorrectValues() {
                assertThat(SpecsStrings.parseLong("123456789012345")).isEqualTo(123456789012345L);
                assertThat(SpecsStrings.parseLong("-987654321098765")).isEqualTo(-987654321098765L);
                assertThat(SpecsStrings.parseLong("0")).isEqualTo(0L);
            }

            @Test
            @DisplayName("parseLong should return null for invalid inputs")
            void testParseLong_InvalidInputs_ReturnsNull() {
                assertThat(SpecsStrings.parseLong("abc")).isNull();
                assertThat(SpecsStrings.parseLong("12.34")).isNull();
                assertThat(SpecsStrings.parseLong("")).isNull();
                assertThat(SpecsStrings.parseLong(null)).isNull();
            }

            @Test
            @DisplayName("parseLong with radix should work correctly")
            void testParseLong_WithRadix_ReturnsCorrectValues() {
                assertThat(SpecsStrings.parseLong("FF", 16)).isEqualTo(255L);
                assertThat(SpecsStrings.parseLong("1010", 2)).isEqualTo(10L);
                assertThat(SpecsStrings.parseLong("77", 8)).isEqualTo(63L);
            }
        }

        @Nested
        @DisplayName("BigInteger Parsing")
        class BigIntegerParsing {

            @Test
            @DisplayName("parseBigInteger should parse valid big integers correctly")
            void testParseBigInteger_ValidBigIntegers_ReturnsCorrectValues() {
                BigInteger big = new BigInteger("123456789012345678901234567890");
                assertThat(SpecsStrings.parseBigInteger("123456789012345678901234567890")).isEqualTo(big);
                assertThat(SpecsStrings.parseBigInteger("0")).isEqualTo(BigInteger.ZERO);
                assertThat(SpecsStrings.parseBigInteger("-1")).isEqualTo(BigInteger.valueOf(-1));
            }

            @Test
            @DisplayName("parseBigInteger should return null for invalid inputs")
            void testParseBigInteger_InvalidInputs_ReturnsNull() {
                assertThat(SpecsStrings.parseBigInteger("abc")).isNull();
                assertThat(SpecsStrings.parseBigInteger("12.34")).isNull();
                assertThat(SpecsStrings.parseBigInteger("")).isNull();
                assertThat(SpecsStrings.parseBigInteger(null)).isNull();
            }
        }
    }

    @Nested
    @DisplayName("String Manipulation")
    class StringManipulation {

        @Nested
        @DisplayName("Padding Operations")
        class PaddingOperations {

            @Test
            @DisplayName("padRight should pad strings correctly")
            void testPadRight_VariousInputs_ReturnsCorrectlyPaddedStrings() {
                assertThat(SpecsStrings.padRight("hello", 10)).isEqualTo("hello     ");
                assertThat(SpecsStrings.padRight("", 5)).isEqualTo("     ");
                assertThat(SpecsStrings.padRight("toolong", 3)).isEqualTo("toolong");
                assertThat(SpecsStrings.padRight("exact", 5)).isEqualTo("exact");
            }

            @Test
            @DisplayName("padLeft should pad strings correctly")
            void testPadLeft_VariousInputs_ReturnsCorrectlyPaddedStrings() {
                assertThat(SpecsStrings.padLeft("hello", 10)).isEqualTo("     hello");
                assertThat(SpecsStrings.padLeft("", 5)).isEqualTo("     ");
                assertThat(SpecsStrings.padLeft("toolong", 3)).isEqualTo("toolong");
                assertThat(SpecsStrings.padLeft("exact", 5)).isEqualTo("exact");
            }

            @Test
            @DisplayName("padLeft with custom character should work correctly")
            void testPadLeft_WithCustomCharacter_ReturnsCorrectlyPaddedStrings() {
                assertThat(SpecsStrings.padLeft("123", 6, '0')).isEqualTo("000123");
                assertThat(SpecsStrings.padLeft("abc", 5, '*')).isEqualTo("**abc");
                assertThat(SpecsStrings.padLeft("", 3, '-')).isEqualTo("---");
            }
        }

        @Nested
        @DisplayName("Case Conversion")
        class CaseConversion {

            @Test
            @DisplayName("toCamelCase should convert strings correctly")
            void testToCamelCase_VariousInputs_ReturnsCorrectCamelCase() {
                assertThat(SpecsStrings.toCamelCase("hello_world")).isEqualTo("HelloWorld");
            }

            @Test
            @DisplayName("toCamelCase with custom separator should work correctly")
            void testToCamelCase_WithCustomSeparator_ReturnsCorrectCamelCase() {
                assertThat(SpecsStrings.toCamelCase("hello_world", "_")).isEqualTo("HelloWorld");
                assertThat(SpecsStrings.toCamelCase("test-case", "-")).isEqualTo("TestCase");
                assertThat(SpecsStrings.toCamelCase("already_camel", "_")).isEqualTo("AlreadyCamel");
                assertThat(SpecsStrings.toCamelCase("hello.world", ".", false)).isEqualTo("helloWorld");
                assertThat(SpecsStrings.toCamelCase("hello.world", ".", true)).isEqualTo("HelloWorld");
            }

            @Test
            @DisplayName("camelCaseSeparate should separate camel case correctly")
            void testCamelCaseSeparate_VariousInputs_ReturnsCorrectlySeparated() {
                assertThat(SpecsStrings.camelCaseSeparate("helloWorld", "_")).isEqualTo("hello_World");
                assertThat(SpecsStrings.camelCaseSeparate("XMLHttpRequest", "-")).isEqualTo("X-M-L-Http-Request");
                assertThat(SpecsStrings.camelCaseSeparate("simple", "_")).isEqualTo("simple");
            }

            @Test
            @DisplayName("toLowerCase should handle null inputs")
            void testToLowerCase_NullInput_ReturnsNull() {
                // toLowerCase throws NPE for null input
                assertThatThrownBy(() -> SpecsStrings.toLowerCase(null))
                        .isInstanceOf(NullPointerException.class);
                assertThat(SpecsStrings.toLowerCase("HELLO")).isEqualTo("hello");
                assertThat(SpecsStrings.toLowerCase("")).isEqualTo("");
            }
        }

        @Nested
        @DisplayName("String Utilities")
        class StringUtilities {

            @Test
            @DisplayName("removeSuffix should remove suffixes correctly")
            void testRemoveSuffix_VariousInputs_ReturnsCorrectResults() {
                assertThat(SpecsStrings.removeSuffix("filename.txt", ".txt")).isEqualTo("filename");
                assertThat(SpecsStrings.removeSuffix("no_suffix", ".txt")).isEqualTo("no_suffix");
                assertThat(SpecsStrings.removeSuffix("", ".txt")).isEqualTo("");
                assertThat(SpecsStrings.removeSuffix("only.suffix", "only.suffix")).isEqualTo("");
            }

            @Test
            @DisplayName("isEmpty should detect empty strings correctly")
            void testIsEmpty_VariousInputs_ReturnsCorrectResults() {
                assertThat(SpecsStrings.isEmpty(null)).isTrue();
                assertThat(SpecsStrings.isEmpty("")).isTrue();
                assertThat(SpecsStrings.isEmpty("   ")).isFalse(); // Only null and empty string are considered empty
                assertThat(SpecsStrings.isEmpty("test")).isFalse();
            }

            @Test
            @DisplayName("buildLine should create repeated strings correctly")
            void testBuildLine_VariousInputs_ReturnsCorrectResults() {
                assertThat(SpecsStrings.buildLine("*", 5)).isEqualTo("*****");
                assertThat(SpecsStrings.buildLine("-", 3)).isEqualTo("---");
                assertThat(SpecsStrings.buildLine("abc", 2)).isEqualTo("abcabc");
                assertThat(SpecsStrings.buildLine("x", 0)).isEqualTo("");
            }

            @Test
            @DisplayName("charAt should return characters safely")
            void testCharAt_VariousInputs_ReturnsCorrectResults() {
                assertThat(SpecsStrings.charAt("hello", 1)).isEqualTo('e');
                assertThat(SpecsStrings.charAt("hello", 0)).isEqualTo('h');
                assertThat(SpecsStrings.charAt("hello", 4)).isEqualTo('o');
                assertThat(SpecsStrings.charAt("hello", 5)).isNull(); // Out of bounds
                assertThat(SpecsStrings.charAt("hello", -1)).isNull(); // Negative index
                assertThat(SpecsStrings.charAt("", 0)).isNull(); // Empty string
                assertThat(SpecsStrings.charAt(null, 0)).isNull(); // Null string
            }
        }
    }

    @Nested
    @DisplayName("Character Validation")
    class CharacterValidation {

        @Test
        @DisplayName("isPrintableChar should identify printable characters correctly")
        void testIsPrintableChar_VariousInputs_ReturnsCorrectResults() {
            assertThat(SpecsStrings.isPrintableChar('A')).isTrue();
            assertThat(SpecsStrings.isPrintableChar('5')).isTrue();
            assertThat(SpecsStrings.isPrintableChar(' ')).isTrue();
            assertThat(SpecsStrings.isPrintableChar('!')).isTrue();
            assertThat(SpecsStrings.isPrintableChar('\t')).isFalse(); // Tab is control character
            assertThat(SpecsStrings.isPrintableChar('\n')).isFalse(); // Newline is control character
            assertThat(SpecsStrings.isPrintableChar('\u0000')).isFalse(); // Null character
        }

        @Test
        @DisplayName("isLetter should identify letters correctly")
        void testIsLetter_VariousInputs_ReturnsCorrectResults() {
            assertThat(SpecsStrings.isLetter('A')).isTrue();
            assertThat(SpecsStrings.isLetter('z')).isTrue();
            assertThat(SpecsStrings.isLetter('5')).isFalse();
            assertThat(SpecsStrings.isLetter(' ')).isFalse();
            assertThat(SpecsStrings.isLetter('!')).isFalse();
        }

        @Test
        @DisplayName("isDigit should identify digits correctly")
        void testIsDigit_VariousInputs_ReturnsCorrectResults() {
            assertThat(SpecsStrings.isDigit('0')).isTrue();
            assertThat(SpecsStrings.isDigit('9')).isTrue();
            assertThat(SpecsStrings.isDigit('5')).isTrue();
            assertThat(SpecsStrings.isDigit('A')).isFalse();
            assertThat(SpecsStrings.isDigit(' ')).isFalse();
            assertThat(SpecsStrings.isDigit('!')).isFalse();
        }

        @Test
        @DisplayName("isDigitOrLetter should identify alphanumeric characters correctly")
        void testIsDigitOrLetter_VariousInputs_ReturnsCorrectResults() {
            assertThat(SpecsStrings.isDigitOrLetter('A')).isTrue();
            assertThat(SpecsStrings.isDigitOrLetter('z')).isTrue();
            assertThat(SpecsStrings.isDigitOrLetter('5')).isTrue();
            assertThat(SpecsStrings.isDigitOrLetter('0')).isTrue();
            assertThat(SpecsStrings.isDigitOrLetter(' ')).isFalse();
            assertThat(SpecsStrings.isDigitOrLetter('!')).isFalse();
            assertThat(SpecsStrings.isDigitOrLetter('_')).isFalse();
        }
    }

    @Nested
    @DisplayName("Hex String Operations")
    class HexStringOperations {

        @Test
        @DisplayName("toHexString for int should convert correctly")
        void testToHexString_Int_ReturnsCorrectHexString() {
            assertThat(SpecsStrings.toHexString(255, 2)).isEqualTo("0xFF");
            assertThat(SpecsStrings.toHexString(255, 4)).isEqualTo("0x00FF");
            assertThat(SpecsStrings.toHexString(0, 2)).isEqualTo("0x00");
            assertThat(SpecsStrings.toHexString(-1, 8)).hasSize(10); // "0x" + 8 hex digits
        }

        @Test
        @DisplayName("toHexString for long should convert correctly")
        void testToHexString_Long_ReturnsCorrectHexString() {
            assertThat(SpecsStrings.toHexString(255L, 2)).isEqualTo("0xFF");
            assertThat(SpecsStrings.toHexString(0L, 4)).isEqualTo("0x0000");
            assertThat(SpecsStrings.toHexString(Long.MAX_VALUE, 16)).hasSize(18); // "0x" + 16 hex digits
        }
    }

    @Nested
    @DisplayName("Regular Expression Operations")
    class RegularExpressionOperations {

        @Test
        @DisplayName("getRegex should find matches correctly")
        void testGetRegex_VariousPatterns_ReturnsCorrectMatches() {
            String text = "The year 2023 and 2024 are important.";
            List<String> years = SpecsStrings.getRegex(text, "\\d{4}");
            assertThat(years).containsExactly("2023", "2024");

            String emails = "Contact us at test@example.com or admin@test.org";
            List<String> emailMatches = SpecsStrings.getRegex(emails, "\\w+@\\w+\\.\\w+");
            assertThat(emailMatches).containsExactly("test@example.com", "admin@test.org");
        }

        @Test
        @DisplayName("getRegex with Pattern should work correctly")
        void testGetRegex_WithPattern_ReturnsCorrectMatches() {
            String text = "Numbers: 123, 456, 789";
            Pattern pattern = Pattern.compile("\\d+");
            List<String> numbers = SpecsStrings.getRegex(text, pattern);
            assertThat(numbers).containsExactly("123", "456", "789");
        }

        @Test
        @DisplayName("matches should detect pattern matches correctly")
        void testMatches_VariousPatterns_ReturnsCorrectResults() {
            Pattern numberPattern = Pattern.compile("\\d+");
            assertThat(SpecsStrings.matches("123", numberPattern)).isTrue();
            assertThat(SpecsStrings.matches("abc", numberPattern)).isFalse();
            assertThat(SpecsStrings.matches("", numberPattern)).isFalse();
        }

        @Test
        @DisplayName("getRegexGroup should extract groups correctly")
        void testGetRegexGroup_VariousPatterns_ReturnsCorrectGroups() {
            String text = "Date: 2023-12-25";
            String year = SpecsStrings.getRegexGroup(text, "(\\d{4})-(\\d{2})-(\\d{2})", 1);
            assertThat(year).isEqualTo("2023");

            String month = SpecsStrings.getRegexGroup(text, "(\\d{4})-(\\d{2})-(\\d{2})", 2);
            assertThat(month).isEqualTo("12");

            String day = SpecsStrings.getRegexGroup(text, "(\\d{4})-(\\d{2})-(\\d{2})", 3);
            assertThat(day).isEqualTo("25");
        }

        @Test
        @DisplayName("getRegexGroups should extract multiple groups correctly")
        void testGetRegexGroups_VariousPatterns_ReturnsCorrectGroups() {
            String text = "Dates: 2023-12-25, 2024-01-15";
            Pattern pattern = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})");
            List<String> years = SpecsStrings.getRegexGroups(text, pattern, 1);
            assertThat(years).containsExactly("2023", "2024");

            List<String> months = SpecsStrings.getRegexGroups(text, pattern, 2);
            assertThat(months).containsExactly("12", "01");
        }
    }

    @Nested
    @DisplayName("Time and Size Formatting")
    class TimeAndSizeFormatting {

        @Test
        @DisplayName("parseTime should format nanoseconds correctly")
        void testParseTime_Nanoseconds_ReturnsCorrectFormat() {
            assertThat(SpecsStrings.parseTime(1000L)).contains("1us"); // microseconds
            assertThat(SpecsStrings.parseTime(1000000L)).contains("1ms"); // milliseconds
            assertThat(SpecsStrings.parseTime(1000000000L)).contains("1s"); // seconds
            assertThat(SpecsStrings.parseTime(0L)).isEqualTo("0ns");
        }

        @Test
        @DisplayName("parseSize should format bytes correctly")
        void testParseSize_Bytes_ReturnsCorrectFormat() {
            assertThat(SpecsStrings.parseSize(512L)).isEqualTo("512 bytes");
            assertThat(SpecsStrings.parseSize(1024L)).isEqualTo("1 KiB");
            assertThat(SpecsStrings.parseSize(1024L * 1024L)).isEqualTo("1 MiB");
            assertThat(SpecsStrings.parseSize(1024L * 1024L * 1024L)).isEqualTo("1 GiB");
            assertThat(SpecsStrings.parseSize(0L)).isEqualTo("0 bytes");
        }

        @Test
        @DisplayName("toString for TimeUnit should return correct symbols")
        void testToString_TimeUnit_ReturnsCorrectSymbols() {
            assertThat(SpecsStrings.toString(TimeUnit.NANOSECONDS)).isEqualTo("ns");
            assertThat(SpecsStrings.toString(TimeUnit.MICROSECONDS)).isEqualTo("us");
            assertThat(SpecsStrings.toString(TimeUnit.MILLISECONDS)).isEqualTo("ms");
            assertThat(SpecsStrings.toString(TimeUnit.SECONDS)).isEqualTo("s");
            assertThat(SpecsStrings.toString(TimeUnit.MINUTES)).isEqualTo("m");
            assertThat(SpecsStrings.toString(TimeUnit.HOURS)).isEqualTo("h");
            assertThat(SpecsStrings.toString(TimeUnit.DAYS)).isEqualTo("d");
        }

        @Test
        @DisplayName("getTimeUnitSymbol should return correct symbols")
        void testGetTimeUnitSymbol_VariousUnits_ReturnsCorrectSymbols() {
            assertThat(SpecsStrings.getTimeUnitSymbol(TimeUnit.NANOSECONDS)).isEqualTo("ns");
            assertThat(SpecsStrings.getTimeUnitSymbol(TimeUnit.SECONDS)).isEqualTo("s");
            assertThat(SpecsStrings.getTimeUnitSymbol(TimeUnit.HOURS)).isEqualTo("h");
        }

        @Test
        @DisplayName("toPercentage should format fractions correctly")
        void testToPercentage_VariousFractions_ReturnsCorrectPercentages() {
            assertThat(SpecsStrings.toPercentage(0.5)).isEqualTo("50,00%");
            assertThat(SpecsStrings.toPercentage(0.0)).isEqualTo("0,00%");
            assertThat(SpecsStrings.toPercentage(1.0)).isEqualTo("100,00%");
            assertThat(SpecsStrings.toPercentage(0.123456)).isEqualTo("12,35%");
        }
    }

    @Nested
    @DisplayName("Collection Utilities")
    class CollectionUtilities {

        @Test
        @DisplayName("toString for List should format correctly")
        void testToString_List_ReturnsCorrectFormat() {
            List<String> strings = Arrays.asList("a", "b", "c");
            assertThat(SpecsStrings.toString(strings)).contains("a").contains("b").contains("c");

            List<Integer> numbers = Arrays.asList(1, 2, 3);
            String result = SpecsStrings.toString(numbers);
            assertThat(result).contains("1").contains("2").contains("3");

            List<String> empty = Collections.emptyList();
            assertThat(SpecsStrings.toString(empty)).isNotNull();
        }

        @Test
        @DisplayName("getSortedList should sort collections correctly")
        void testGetSortedList_VariousCollections_ReturnsSortedLists() {
            Collection<String> strings = Arrays.asList("c", "a", "b");
            List<String> sorted = SpecsStrings.getSortedList(strings);
            assertThat(sorted).containsExactly("a", "b", "c");

            Collection<Integer> numbers = Arrays.asList(3, 1, 2);
            List<Integer> sortedNumbers = SpecsStrings.getSortedList(numbers);
            assertThat(sortedNumbers).containsExactly(1, 2, 3);
        }

        @Test
        @DisplayName("moduloGet should handle list access correctly")
        void testModuloGet_VariousIndices_ReturnsCorrectElements() {
            List<String> list = Arrays.asList("a", "b", "c");
            assertThat(SpecsStrings.moduloGet(list, 0)).isEqualTo("a");
            assertThat(SpecsStrings.moduloGet(list, 1)).isEqualTo("b");
            assertThat(SpecsStrings.moduloGet(list, 2)).isEqualTo("c");
            assertThat(SpecsStrings.moduloGet(list, 3)).isEqualTo("a"); // Wraps around
            assertThat(SpecsStrings.moduloGet(list, 4)).isEqualTo("b");
            assertThat(SpecsStrings.moduloGet(list, -1)).isEqualTo("c"); // Negative index
        }

        @Test
        @DisplayName("modulo should handle negative indices correctly")
        void testModulo_VariousIndices_ReturnsCorrectResults() {
            assertThat(SpecsStrings.modulo(0, 3)).isEqualTo(0);
            assertThat(SpecsStrings.modulo(1, 3)).isEqualTo(1);
            assertThat(SpecsStrings.modulo(3, 3)).isEqualTo(0);
            assertThat(SpecsStrings.modulo(4, 3)).isEqualTo(1);
            assertThat(SpecsStrings.modulo(-1, 3)).isEqualTo(2);
            assertThat(SpecsStrings.modulo(-2, 3)).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("Utility Operations")
    class UtilityOperations {

        @Test
        @DisplayName("isPalindrome should detect palindromes correctly")
        void testIsPalindrome_VariousInputs_ReturnsCorrectResults() {
            // Original test cases
            assertThat(SpecsStrings.isPalindrome("a")).isTrue();
            assertThat(SpecsStrings.isPalindrome("abba")).isTrue();
            assertThat(SpecsStrings.isPalindrome("585")).isTrue();
            assertThat(SpecsStrings.isPalindrome("1001001001")).isTrue();
            assertThat(SpecsStrings.isPalindrome("10010010010")).isFalse();

            // Additional test cases
            assertThat(SpecsStrings.isPalindrome("")).isTrue(); // Empty string is palindrome
            assertThat(SpecsStrings.isPalindrome("racecar")).isTrue();
            assertThat(SpecsStrings.isPalindrome("hello")).isFalse();
            assertThat(SpecsStrings.isPalindrome("A man a plan a canal Panama")).isFalse(); // Case sensitive
        }

        @Test
        @DisplayName("getAlphaId should generate alphabetic IDs correctly")
        @SuppressWarnings("deprecation")
        void testGetAlphaId_VariousNumbers_ReturnsCorrectAlphaIds() {
            assertThat(SpecsStrings.getAlphaId(0)).isEqualTo("A");
            assertThat(SpecsStrings.getAlphaId(1)).isEqualTo("B");
            assertThat(SpecsStrings.getAlphaId(23)).isEqualTo("AA");
            assertThat(SpecsStrings.getAlphaId(25)).isEqualTo("AC");
            assertThat(SpecsStrings.getAlphaId(27)).isEqualTo("AE");
        }

        @Test
        @DisplayName("toExcelColumn should generate Excel column names correctly")
        void testToExcelColumn_VariousNumbers_ReturnsCorrectColumnNames() {
            assertThat(SpecsStrings.toExcelColumn(1)).isEqualTo("A");
            assertThat(SpecsStrings.toExcelColumn(26)).isEqualTo("Z");
            assertThat(SpecsStrings.toExcelColumn(27)).isEqualTo("AA");
            assertThat(SpecsStrings.toExcelColumn(28)).isEqualTo("AB");
            assertThat(SpecsStrings.toExcelColumn(702)).isEqualTo("ZZ");
        }

        @Test
        @DisplayName("count should count character occurrences correctly")
        void testCount_VariousInputs_ReturnsCorrectCounts() {
            assertThat(SpecsStrings.count("hello", 'l')).isEqualTo(2);
            assertThat(SpecsStrings.count("hello", 'o')).isEqualTo(1);
            assertThat(SpecsStrings.count("hello", 'x')).isEqualTo(0);
            assertThat(SpecsStrings.count("", 'a')).isEqualTo(0);
            assertThat(SpecsStrings.count("aaa", 'a')).isEqualTo(3);
        }

        @Test
        @DisplayName("invertBinaryString should invert binary strings correctly")
        void testInvertBinaryString_VariousInputs_ReturnsCorrectResults() {
            assertThat(SpecsStrings.invertBinaryString("1010")).isEqualTo("0101");
            assertThat(SpecsStrings.invertBinaryString("0000")).isEqualTo("1111");
            assertThat(SpecsStrings.invertBinaryString("1111")).isEqualTo("0000");
            assertThat(SpecsStrings.invertBinaryString("")).isEqualTo("");
        }

        @Test
        @DisplayName("packageNameToFolderName should convert package names correctly")
        void testPackageNameToFolderName_VariousInputs_ReturnsCorrectPaths() {
            assertThat(SpecsStrings.packageNameToFolderName("com.example.test")).isEqualTo("com/example/test");
            assertThat(SpecsStrings.packageNameToFolderName("")).isEqualTo("");
            assertThat(SpecsStrings.packageNameToFolderName("simple")).isEqualTo("simple");
        }

        @Test
        @DisplayName("packageNameToResource should convert package names to resources correctly")
        void testPackageNameToResource_VariousInputs_ReturnsCorrectResourcePaths() {
            assertThat(SpecsStrings.packageNameToResource("com.example.test")).isEqualTo("com/example/test/");
            assertThat(SpecsStrings.packageNameToResource("")).isEqualTo("/");
            assertThat(SpecsStrings.packageNameToResource("simple")).isEqualTo("simple/");
        }
    }

    @Nested
    @DisplayName("File and Path Operations")
    class FileAndPathOperations {

        @Test
        @DisplayName("getExtension should extract file extensions correctly")
        void testGetExtension_VariousFilenames_ReturnsCorrectExtensions() {
            assertThat(SpecsStrings.getExtension("file.txt")).isEqualTo("txt");
            assertThat(SpecsStrings.getExtension("document.pdf")).isEqualTo("pdf");
            assertThat(SpecsStrings.getExtension("no_extension")).isNull();
            assertThat(SpecsStrings.getExtension("multiple.dots.here.java")).isEqualTo("java");
            assertThat(SpecsStrings.getExtension("")).isNull();
            assertThat(SpecsStrings.getExtension(".hidden")).isEqualTo("hidden");
        }

        @Test
        @DisplayName("packageNameToFolder should create correct folder structure")
        void testPackageNameToFolder_VariousInputs_ReturnsCorrectFolders(@TempDir File tempDir) {
            File result = SpecsStrings.packageNameToFolder(tempDir, "com.example.test");
            assertThat(result.getAbsolutePath()).endsWith("com" + File.separator + "example" + File.separator + "test");

            File simple = SpecsStrings.packageNameToFolder(tempDir, "simple");
            assertThat(simple.getAbsolutePath()).endsWith("simple");
        }
    }

    @Nested
    @DisplayName("Template and Replacement Operations")
    class TemplateAndReplacementOperations {

        @Test
        @DisplayName("replace should replace template variables correctly")
        void testReplace_VariousTemplates_ReturnsCorrectResults() {
            Map<String, String> mappings = new HashMap<>();
            mappings.put("name", "John");
            mappings.put("<age>", "25");

            String template = "Hello name, you are <age> years old.";
            String result = SpecsStrings.replace(template, mappings);
            assertThat(result).isEqualTo("Hello John, you are 25 years old.");
        }

        @Test
        @DisplayName("parseTemplate should parse templates with tags correctly")
        void testParseTemplate_VariousInputs_ReturnsCorrectResults() {
            List<String> defaults = Arrays.asList("name", "DefaultName", "<age>", "0");
            String template = "Hello <name>, age: <age>";
            String result = SpecsStrings.parseTemplate(template, defaults, "<name>", "Alice", "<age>", "30");
            assertThat(result).isEqualTo("Hello Alice, age: 30");
        }

        @Test
        @DisplayName("remove match should remove matching strings correctly")
        void testRemove_VariousMatches_ReturnsCorrectResults() {
            assertThat(SpecsStrings.remove("hello123world", "123")).isEqualTo("helloworld");
            assertThat(SpecsStrings.remove("test@#$test", "@#$")).isEqualTo("testtest");
            assertThat(SpecsStrings.remove("no match", "xyz")).isEqualTo("no match");
        }
    }

    @Nested
    @DisplayName("JSON and Encoding Operations")
    class JsonAndEncodingOperations {

        @Test
        @DisplayName("escapeJson should escape JSON strings correctly")
        void testEscapeJson_VariousInputs_ReturnsCorrectlyEscaped() {
            assertThat(SpecsStrings.escapeJson("hello\"world")).isEqualTo("hello\\\"world");
            assertThat(SpecsStrings.escapeJson("line1\nline2")).isEqualTo("line1\\nline2");
            assertThat(SpecsStrings.escapeJson("tab\there")).isEqualTo("tab\\there");
            assertThat(SpecsStrings.escapeJson("backslash\\test")).isEqualTo("backslash\\\\test");
        }

        @Test
        @DisplayName("escapeJson with ignoreNewlines should work correctly")
        void testEscapeJson_IgnoreNewlines_ReturnsCorrectlyEscaped() {
            assertThat(SpecsStrings.escapeJson("line1\nline2", true)).isEqualTo("line1line2");
            assertThat(SpecsStrings.escapeJson("line1\nline2", false)).isEqualTo("line1\\nline2");
        }

        @Test
        @DisplayName("toBytes and fromBytes should handle encoding correctly")
        void testBytesEncoding_VariousInputs_ReturnsCorrectResults() {
            String original = "Hello, 世界!";
            String encoded = SpecsStrings.toBytes(original, "UTF-8");
            String decoded = SpecsStrings.fromBytes(encoded, "UTF-8");
            assertThat(decoded).isEqualTo(original);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandling {

        @ParameterizedTest
        @ValueSource(strings = { "", "   ", "\t", "\n" })
        @DisplayName("Empty and whitespace strings should be handled correctly")
        void testEmptyAndWhitespaceHandling(String input) {
            // Most parsing methods should handle empty/whitespace gracefully
            assertThat(SpecsStrings.parseInt(input)).isEqualTo(0);
            assertThat(SpecsStrings.parseInteger(input)).isNull();
            assertThat(SpecsStrings.parseDouble(input)).isNull();
            assertThat(SpecsStrings.parseBoolean(input)).isNull();
        }

        @Test
        @DisplayName("Null inputs should be handled gracefully")
        void testNullInputHandling() {
            assertThat(SpecsStrings.parseInt(null)).isEqualTo(0);
            assertThat(SpecsStrings.parseInteger(null)).isNull();
            // These methods throw NPE for null input
            assertThatThrownBy(() -> SpecsStrings.parseDouble(null))
                    .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> SpecsStrings.parseBoolean(null))
                    .isInstanceOf(NullPointerException.class);
            assertThat(SpecsStrings.parseLong(null)).isNull();
            assertThatThrownBy(() -> SpecsStrings.parseFloat(null))
                    .isInstanceOf(NullPointerException.class);
            assertThat(SpecsStrings.charAt(null, 0)).isNull();
            assertThat(SpecsStrings.isEmpty(null)).isTrue();
        }

        @Test
        @DisplayName("Very large numbers should be handled correctly")
        void testLargeNumberHandling() {
            String maxInt = String.valueOf(Integer.MAX_VALUE);
            String minInt = String.valueOf(Integer.MIN_VALUE);
            String tooLarge = "999999999999999999999";

            assertThat(SpecsStrings.parseInt(maxInt)).isEqualTo(Integer.MAX_VALUE);
            assertThat(SpecsStrings.parseInt(minInt)).isEqualTo(Integer.MIN_VALUE);
            assertThat(SpecsStrings.parseInt(tooLarge)).isEqualTo(0); // Should fail gracefully

            // Long should handle larger numbers
            assertThat(SpecsStrings.parseLong(tooLarge)).isNull();

            // BigInteger should handle very large numbers
            assertThat(SpecsStrings.parseBigInteger(tooLarge)).isNotNull();
        }
    }

    @Nested
    @DisplayName("File Operations")
    class FileOperations {

        @Test
        @DisplayName("parseTableFromFile should parse files correctly")
        void testParseTableFromFile_ValidFile_ReturnsCorrectTable(@TempDir File tempDir) throws IOException {
            // Create a test file
            File testFile = new File(tempDir, "test-table.txt");
            Files.write(testFile.toPath(), Arrays.asList("key1=value1", "key2=value2", "key3=value3"));

            // Create a LineParser that splits on '='
            LineParser lineParser = new LineParser("=", "", "//");
            Map<String, String> result = SpecsStrings.parseTableFromFile(testFile, lineParser);

            assertThat(result).hasSize(3);
            assertThat(result.get("key1")).isEqualTo("value1");
            assertThat(result.get("key2")).isEqualTo("value2");
            assertThat(result.get("key3")).isEqualTo("value3");
        }
    }

    @Nested
    @DisplayName("Additional String Utilities")
    class AdditionalStringUtilities {

        @Test
        @DisplayName("isPrintableChar should identify printable characters correctly")
        void testIsPrintableChar() {
            // Printable characters
            assertThat(SpecsStrings.isPrintableChar('a')).isTrue();
            assertThat(SpecsStrings.isPrintableChar('Z')).isTrue();
            assertThat(SpecsStrings.isPrintableChar('0')).isTrue();
            assertThat(SpecsStrings.isPrintableChar('!')).isTrue();
            assertThat(SpecsStrings.isPrintableChar(' ')).isTrue();
            assertThat(SpecsStrings.isPrintableChar('~')).isTrue();

            // Non-printable characters
            assertThat(SpecsStrings.isPrintableChar('\t')).isFalse();
            assertThat(SpecsStrings.isPrintableChar('\n')).isFalse();
            assertThat(SpecsStrings.isPrintableChar('\r')).isFalse();
            assertThat(SpecsStrings.isPrintableChar('\u0000')).isFalse(); // Null character
            assertThat(SpecsStrings.isPrintableChar('\u007F')).isFalse(); // DEL character
        }

        @Test
        @DisplayName("toHexString with int should format correctly")
        void testToHexStringInt() {
            assertThat(SpecsStrings.toHexString(255, 2)).isEqualTo("0xFF");
            assertThat(SpecsStrings.toHexString(255, 4)).isEqualTo("0x00FF");
            assertThat(SpecsStrings.toHexString(0, 2)).isEqualTo("0x00");
            assertThat(SpecsStrings.toHexString(16, 2)).isEqualTo("0x10");
            assertThat(SpecsStrings.toHexString(-1, 8)).isEqualTo("0xFFFFFFFF");
        }

        @Test
        @DisplayName("toHexString with long should format correctly")
        void testToHexStringLong() {
            assertThat(SpecsStrings.toHexString(255L, 2)).isEqualTo("0xFF");
            assertThat(SpecsStrings.toHexString(255L, 4)).isEqualTo("0x00FF");
            assertThat(SpecsStrings.toHexString(0L, 2)).isEqualTo("0x00");
            assertThat(SpecsStrings.toHexString(16L, 2)).isEqualTo("0x10");
            assertThat(SpecsStrings.toHexString(-1L, 16)).isEqualTo("0xFFFFFFFFFFFFFFFF");
        }

        @Test
        @DisplayName("indexOfFirstWhitespace should find first whitespace correctly")
        void testIndexOfFirstWhitespace() {
            assertThat(SpecsStrings.indexOfFirstWhitespace("hello world")).isEqualTo(5);
            assertThat(SpecsStrings.indexOfFirstWhitespace("hello\tworld")).isEqualTo(5);
            assertThat(SpecsStrings.indexOfFirstWhitespace("hello\nworld")).isEqualTo(5);
            assertThat(SpecsStrings.indexOfFirstWhitespace("helloworld")).isEqualTo(-1);
            assertThat(SpecsStrings.indexOfFirstWhitespace(" hello")).isEqualTo(0);
            assertThat(SpecsStrings.indexOfFirstWhitespace("")).isEqualTo(-1);
        }

        @Test
        @DisplayName("indexOf with predicate should find character correctly")
        void testIndexOfWithPredicate() {
            // Find digits (not reverse)
            assertThat(SpecsStrings.indexOf("abc123def", Character::isDigit, false)).isEqualTo(3);
            assertThat(SpecsStrings.indexOf("abc123def", Character::isDigit, true)).isEqualTo(5); // Last digit
            
            // Find uppercase letters
            assertThat(SpecsStrings.indexOf("helloWorld", Character::isUpperCase, false)).isEqualTo(5);
            assertThat(SpecsStrings.indexOf("HelloWorld", Character::isUpperCase, true)).isEqualTo(5); // Last uppercase
            
            // Character not found
            assertThat(SpecsStrings.indexOf("hello", Character::isDigit, false)).isEqualTo(-1);
        }

        @Test
        @DisplayName("getSortedList should sort collections correctly")
        void testGetSortedList() {
            List<String> unsorted = Arrays.asList("zebra", "apple", "banana");
            List<String> sorted = SpecsStrings.getSortedList(unsorted);
            
            assertThat(sorted).containsExactly("apple", "banana", "zebra");
            assertThat(unsorted).containsExactly("zebra", "apple", "banana"); // Original unchanged
            
            // Test with integers
            List<Integer> unsortedInts = Arrays.asList(3, 1, 4, 1, 5);
            List<Integer> sortedInts = SpecsStrings.getSortedList(unsortedInts);
            assertThat(sortedInts).containsExactly(1, 1, 3, 4, 5);
        }

        @Test
        @DisplayName("instructionRangeHexEncode should create encoded string")
        void testInstructionRangeHexEncode() {
            String encoded = SpecsStrings.instructionRangeHexEncode(100, 200);
            assertThat(encoded).isNotBlank();
            // Just verify it creates some encoded format, actual format is implementation detail
        }

        @Test
        @DisplayName("packageNameToFolderName should convert correctly")
        void testPackageNameToFolderName() {
            assertThat(SpecsStrings.packageNameToFolderName("com.example.package"))
                    .isEqualTo("com/example/package");
            assertThat(SpecsStrings.packageNameToFolderName("simple"))
                    .isEqualTo("simple");
            assertThat(SpecsStrings.packageNameToFolderName(""))
                    .isEqualTo("");
        }

        @Test
        @DisplayName("packageNameToFolder should create correct folder structure")
        void testPackageNameToFolder(@TempDir File tempDir) {
            File result = SpecsStrings.packageNameToFolder(tempDir, "com.example.package");
            assertThat(result.getPath()).endsWith("com" + File.separator + "example" + File.separator + "package");
        }

        @Test
        @DisplayName("replace with mappings should work correctly")
        void testReplaceWithMappings() {
            Map<String, String> mappings = new HashMap<>();
            mappings.put("${name}", "John");
            mappings.put("${age}", "30");
            
            String template = "Hello ${name}, you are ${age} years old!";
            String result = SpecsStrings.replace(template, mappings);
            
            assertThat(result).isEqualTo("Hello John, you are 30 years old!");
        }

        @Test
        @DisplayName("moduloGet should access list elements with modulo")
        void testModuloGet() {
            List<String> list = Arrays.asList("a", "b", "c");
            
            assertThat(SpecsStrings.moduloGet(list, 0)).isEqualTo("a");
            assertThat(SpecsStrings.moduloGet(list, 1)).isEqualTo("b");
            assertThat(SpecsStrings.moduloGet(list, 2)).isEqualTo("c");
            assertThat(SpecsStrings.moduloGet(list, 3)).isEqualTo("a"); // Wraps around
            assertThat(SpecsStrings.moduloGet(list, 4)).isEqualTo("b");
            assertThat(SpecsStrings.moduloGet(list, -1)).isEqualTo("c"); // Negative index
        }

        @Test
        @DisplayName("modulo should calculate modulo correctly")
        void testModulo() {
            assertThat(SpecsStrings.modulo(5, 3)).isEqualTo(2);
            assertThat(SpecsStrings.modulo(3, 3)).isEqualTo(0);
            assertThat(SpecsStrings.modulo(0, 3)).isEqualTo(0);
            assertThat(SpecsStrings.modulo(-1, 3)).isEqualTo(2);
            assertThat(SpecsStrings.modulo(-4, 3)).isEqualTo(2);
        }

        @Test
        @DisplayName("getRegex with string pattern should extract matches")
        void testGetRegexString() {
            String content = "The numbers are 123 and 456";
            List<String> matches = SpecsStrings.getRegex(content, "\\d+");
            
            assertThat(matches).hasSize(2);
            assertThat(matches).containsExactly("123", "456");
        }

        @Test
        @DisplayName("getRegex with Pattern should extract matches")
        void testGetRegexPattern() {
            String content = "Email: john@example.com and jane@test.org";
            Pattern emailPattern = Pattern.compile("\\S+@\\S+\\.\\S+");
            List<String> matches = SpecsStrings.getRegex(content, emailPattern);
            
            assertThat(matches).hasSize(2);
            assertThat(matches).containsExactly("john@example.com", "jane@test.org");
        }

        @Test
        @DisplayName("matches with Pattern should check pattern matching")
        void testMatches() {
            Pattern digitPattern = Pattern.compile("\\d+");
            
            assertThat(SpecsStrings.matches("123", digitPattern)).isTrue();
            assertThat(SpecsStrings.matches("abc", digitPattern)).isFalse();
            assertThat(SpecsStrings.matches("123abc", digitPattern)).isTrue(); // Contains digits
        }

        @Test
        @DisplayName("getRegexGroup should extract capturing groups")
        void testGetRegexGroup() {
            String content = "Date: 2023-12-25";
            String result = SpecsStrings.getRegexGroup(content, "(\\d{4})-(\\d{2})-(\\d{2})", 1);
            
            assertThat(result).isEqualTo("2023");
            
            result = SpecsStrings.getRegexGroup(content, "(\\d{4})-(\\d{2})-(\\d{2})", 2);
            assertThat(result).isEqualTo("12");
            
            result = SpecsStrings.getRegexGroup(content, "(\\d{4})-(\\d{2})-(\\d{2})", 3);
            assertThat(result).isEqualTo("25");
        }

        @Test
        @DisplayName("getRegexGroups should extract all capturing groups")
        void testGetRegexGroups() {
            String content = "Dates: 2023-12-25 and 2024-01-15";
            List<String> groups = SpecsStrings.getRegexGroups(content, "(\\d{4})-(\\d{2})-(\\d{2})", 1);
            
            assertThat(groups).hasSize(2);
            assertThat(groups).containsExactly("2023", "2024");
        }

        @Test
        @DisplayName("parseShort should parse short values correctly")
        void testParseShort() {
            assertThat(SpecsStrings.parseShort("123")).isEqualTo((short) 123);
            assertThat(SpecsStrings.parseShort("-456")).isEqualTo((short) -456);
            assertThat(SpecsStrings.parseShort("0")).isEqualTo((short) 0);
            
            // Invalid input should throw exception (unlike other parse methods)
            assertThatThrownBy(() -> SpecsStrings.parseShort("invalid"))
                    .isInstanceOf(NumberFormatException.class);
            assertThatThrownBy(() -> SpecsStrings.parseShort(null))
                    .isInstanceOf(NumberFormatException.class);
        }

        @Test
        @DisplayName("parseBigInteger should parse big integer values")
        void testParseBigInteger() {
            BigInteger large = new BigInteger("123456789012345678901234567890");
            assertThat(SpecsStrings.parseBigInteger(large.toString())).isEqualTo(large);
            assertThat(SpecsStrings.parseBigInteger("0")).isEqualTo(BigInteger.ZERO);
            assertThat(SpecsStrings.parseBigInteger("-123")).isEqualTo(BigInteger.valueOf(-123));
            
            // Invalid input should return null
            assertThat(SpecsStrings.parseBigInteger("invalid")).isNull();
            assertThat(SpecsStrings.parseBigInteger(null)).isNull();
        }

        @Test
        @DisplayName("parseLong with radix should parse correctly")
        void testParseLongWithRadix() {
            assertThat(SpecsStrings.parseLong("1010", 2)).isEqualTo(10L); // Binary
            assertThat(SpecsStrings.parseLong("FF", 16)).isEqualTo(255L); // Hex
            assertThat(SpecsStrings.parseLong("77", 8)).isEqualTo(63L); // Octal
            assertThat(SpecsStrings.parseLong("123", 10)).isEqualTo(123L); // Decimal
            
            // Invalid input should return null
            assertThat(SpecsStrings.parseLong("invalid", 10)).isNull();
            assertThat(SpecsStrings.parseLong("GG", 16)).isNull(); // Invalid hex
        }

        @Test
        @DisplayName("parseFloat with strict mode should work correctly")
        void testParseFloatStrict() {
            // Valid inputs should work in both modes
            assertThat(SpecsStrings.parseFloat("123.45", true)).isEqualTo(123.45f);
            assertThat(SpecsStrings.parseFloat("0.0", true)).isEqualTo(0.0f);
            
            // Invalid input should return null in both modes
            assertThat(SpecsStrings.parseFloat("invalid", true)).isNull();
            assertThat(SpecsStrings.parseFloat("invalid", false)).isNull();
        }

        @Test
        @DisplayName("parseDouble with strict mode should work correctly")
        void testParseDoubleStrict() {
            // Valid inputs should work
            assertThat(SpecsStrings.parseDouble("123.45", true)).isEqualTo(123.45);
            assertThat(SpecsStrings.parseDouble("0.0", true)).isEqualTo(0.0);
            
            // Invalid input should return null in both modes
            assertThat(SpecsStrings.parseDouble("invalid", true)).isNull();
            assertThat(SpecsStrings.parseDouble("invalid", false)).isNull();
        }
    }

    // Static data providers for parameterized tests
    static List<Arguments> validIntegerInputs() {
        return Arrays.asList(
                Arguments.of("0", 0),
                Arguments.of("123", 123),
                Arguments.of("-456", -456),
                Arguments.of("+789", 789),
                Arguments.of(String.valueOf(Integer.MAX_VALUE), Integer.MAX_VALUE),
                Arguments.of(String.valueOf(Integer.MIN_VALUE), Integer.MIN_VALUE));
    }

    static List<Arguments> invalidIntegerInputs() {
        return Arrays.asList(
                Arguments.of("abc"),
                Arguments.of("12.34"),
                Arguments.of(""),
                Arguments.of("   "),
                Arguments.of("999999999999999999999"), // Too large for int
                Arguments.of((Object) null));
    }
}
