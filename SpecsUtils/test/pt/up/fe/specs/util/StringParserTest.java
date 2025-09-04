/**
 * Copyright 2017 SPeCS.
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

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.stringparser.StringParser;
import pt.up.fe.specs.util.stringparser.StringParsers;
import pt.up.fe.specs.util.stringsplitter.StringSplitter;
import pt.up.fe.specs.util.stringsplitter.StringSplitterRules;

/**
 * Test suite for StringParser and StringSplitter utility classes.
 * 
 * This test class covers string parsing functionality including:
 * - Double quoted string parsing
 * - Word parsing and splitting
 * - Number parsing (integers, doubles, floats)
 * - Custom separators and reverse parsing
 */
@DisplayName("StringParser Tests")
public class StringParserTest {

    @Nested
    @DisplayName("Double Quoted String Parsing")
    class DoubleQuotedStringParsing {

        @Test
        @DisplayName("parseDoubleQuotedString should parse simple quoted strings correctly")
        void testDoubleQuotedString_SimpleQuotes_ReturnsCorrectString() {
            String result = new StringParser("\"hello\"").apply(StringParsers::parseDoubleQuotedString);
            assertThat(result).isEqualTo("hello");
        }

        @Test
        @DisplayName("parseDoubleQuotedString should handle escaped quotes correctly")
        void testDoubleQuotedString_EscapedQuotes_ReturnsCorrectString() {
            String result = new StringParser("\"hel\\\"lo\"").apply(StringParsers::parseDoubleQuotedString);
            assertThat(result).isEqualTo("hel\\\"lo");
        }

        @Test
        @DisplayName("parseDoubleQuotedString should handle empty quotes")
        void testDoubleQuotedString_EmptyQuotes_ReturnsEmptyString() {
            String result = new StringParser("\"\"").apply(StringParsers::parseDoubleQuotedString);
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("parseDoubleQuotedString should throw exception for malformed quotes")
        void testDoubleQuotedString_MalformedQuotes_ShouldThrowException() {
            assertThatThrownBy(() -> {
                new StringParser("\"unclosed").apply(StringParsers::parseDoubleQuotedString);
            }).isInstanceOf(RuntimeException.class)
              .hasMessageContaining("Could not find a valid end delimiter");
        }
    }

    @Nested
    @DisplayName("Word Parsing")
    class WordParsing {

        @Test
        @DisplayName("StringSplitter should parse words correctly with various whitespace")
        void testWord_VariousWhitespace_ParsesCorrectly() {
            String testString = "word1 word2\tword3  word4";
            StringSplitter parser = new StringSplitter(testString);

            String word1 = parser.parse(StringSplitterRules::string);
            assertThat(word1).isEqualTo("word1");
            assertThat(parser.toString()).isEqualTo("word2\tword3  word4");

            Optional<String> word2 = parser.parseTry(StringSplitterRules::string);
            assertThat(word2).hasValue("word2");
            assertThat(parser.toString()).isEqualTo("word3  word4");

            Optional<String> failedCheck = parser.parseIf(StringSplitterRules::string,
                    string -> string.equals("non-existing word"));
            assertThat(failedCheck).isEmpty();
            assertThat(parser.toString()).isEqualTo("word3  word4");

            Optional<String> word3 = parser.parseIf(StringSplitterRules::string,
                    string -> string.equals("word3"));
            assertThat(word3).hasValue("word3");
            assertThat(parser.toString()).isEqualTo("word4");

            Optional<String> word4 = parser.peekIf(StringSplitterRules::string,
                    string -> string.equals("word4"));
            assertThat(word4).hasValue("word4");
            assertThat(parser.toString()).isEqualTo("word4");

            boolean hasWord4 = parser.check(StringSplitterRules::string, string -> string.equals("word4"));
            assertThat(hasWord4).isTrue();
            assertThat(parser.isEmpty()).isTrue();
        }
    }

    @Nested
    @DisplayName("Number Parsing")
    class NumberParsing {

        @Test
        @DisplayName("StringSplitter should parse different number types correctly")
        void testNumbers_DifferentTypes_ParsesCorrectly() {
            String testString = "1 2.0 3.0f";
            StringSplitter parser = new StringSplitter(testString);

            Integer integer = parser.parse(StringSplitterRules::integer);
            assertThat(integer).isEqualTo(1);
            assertThat(parser.toString()).isEqualTo("2.0 3.0f");

            Double aDouble = parser.parse(StringSplitterRules::doubleNumber);
            assertThat(aDouble).isEqualTo(2.0);
            assertThat(parser.toString()).isEqualTo("3.0f");

            Float aFloat = parser.parse(StringSplitterRules::floatNumber);
            assertThat(aFloat).isEqualTo(3.0f);
            assertThat(parser.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("StringSplitter should handle invalid numbers gracefully")
        void testNumbers_InvalidNumbers_ShouldHandleGracefully() {
            StringSplitter parser = new StringSplitter("not-a-number");

            assertThatCode(() -> {
                parser.parseTry(StringSplitterRules::integer);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("StringSplitter should handle empty input for numbers")
        void testNumbers_EmptyInput_ShouldHandleGracefully() {
            StringSplitter parser = new StringSplitter("");

            assertThatCode(() -> {
                parser.parseTry(StringSplitterRules::integer);
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Custom Separators and Reverse Parsing")
    class CustomSeparatorsAndReverseParsing {

        @Test
        @DisplayName("StringSplitter should handle custom separators and reverse parsing")
        void testReverseAndSeparator_CustomConfiguration_ParsesCorrectly() {
            String testString = "word1 word2,word3, word4";
            StringSplitter parser = new StringSplitter(testString);

            String word1 = parser.parse(StringSplitterRules::string);
            assertThat(word1).isEqualTo("word1");
            assertThat(parser.toString()).isEqualTo("word2,word3, word4");

            parser.setSeparator(aChar -> aChar == ',');

            String word2 = parser.parse(StringSplitterRules::string);
            assertThat(word2).isEqualTo("word2");
            assertThat(parser.toString()).isEqualTo("word3, word4");

            parser.setReverse(true);

            String word4 = parser.parse(StringSplitterRules::string);
            assertThat(word4).isEqualTo("word4");
            assertThat(parser.toString()).isEqualTo("word3");

            String word3 = parser.parse(StringSplitterRules::string);
            assertThat(word3).isEqualTo("word3");
            assertThat(parser.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("StringSplitter should handle empty strings with custom separators")
        void testCustomSeparator_EmptyString_ShouldHandleGracefully() {
            StringSplitter parser = new StringSplitter("");
            parser.setSeparator(aChar -> aChar == ',');

            assertThatCode(() -> {
                parser.parseTry(StringSplitterRules::string);
            }).doesNotThrowAnyException();
        }
    }

}
