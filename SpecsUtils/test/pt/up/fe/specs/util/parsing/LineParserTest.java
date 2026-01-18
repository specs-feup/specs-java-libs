package pt.up.fe.specs.util.parsing;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Comprehensive test suite for LineParser utility class.
 * Tests command line parsing with support for separators, quotes, and comments.
 * 
 * @author Generated Tests
 */
@DisplayName("LineParser Tests")
class LineParserTest {

    @Nested
    @DisplayName("Default Parser Tests")
    class DefaultParserTests {

        @Test
        @DisplayName("should create default parser with correct settings")
        void testDefaultParser() {
            // Execute
            LineParser parser = LineParser.getDefaultLineParser();

            // Verify
            assertThat(parser.getSplittingString()).isEqualTo(" ");
            assertThat(parser.getJoinerString()).isEqualTo("\"");
            assertThat(parser.getOneLineComment()).isEqualTo("//");
        }

        @Test
        @DisplayName("should split simple space-separated command")
        void testSimpleSpaceSeparated() {
            // Setup
            LineParser parser = LineParser.getDefaultLineParser();

            // Execute
            List<String> result = parser.splitCommand("arg1 arg2 arg3");

            // Verify
            assertThat(result).containsExactly("arg1", "arg2", "arg3");
        }

        @Test
        @DisplayName("should handle quoted strings with spaces")
        void testQuotedStrings() {
            // Setup
            LineParser parser = LineParser.getDefaultLineParser();

            // Execute
            List<String> result = parser.splitCommand("arg1 \"quoted string with spaces\" arg3");

            // Verify - fixed behavior: no empty strings after quotes
            assertThat(result).containsExactly("arg1", "quoted string with spaces", "arg3");
        }

        @Test
        @DisplayName("should handle comment lines")
        void testCommentLines() {
            // Setup
            LineParser parser = LineParser.getDefaultLineParser();

            // Execute
            List<String> result1 = parser.splitCommand("// this is a comment");
            List<String> result2 = parser.splitCommand("//another comment");

            // Verify
            assertThat(result1).isEmpty();
            assertThat(result2).isEmpty();
        }
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("should create parser with custom settings")
        void testCustomParser() {
            // Execute
            LineParser parser = new LineParser(",", "'", "#");

            // Verify
            assertThat(parser.getSplittingString()).isEqualTo(",");
            assertThat(parser.getJoinerString()).isEqualTo("'");
            assertThat(parser.getOneLineComment()).isEqualTo("#");
        }

        @Test
        @DisplayName("should handle empty joiner string")
        void testEmptyJoiner() {
            // Execute
            LineParser parser = new LineParser(" ", "", "//");

            // Verify
            assertThat(parser.getJoinerString()).isEmpty();

            // Test behavior - should not treat quotes specially
            List<String> result = parser.splitCommand("arg1 \"quoted\" arg3");
            assertThat(result).containsExactly("arg1", "\"quoted\"", "arg3");
        }

        @Test
        @DisplayName("should warn about empty comment prefix")
        void testEmptyCommentPrefix() {
            // Execute - This should log a warning but still work
            LineParser parser = new LineParser(" ", "\"", "");

            // Verify
            assertThat(parser.getOneLineComment()).isEmpty();

            // Test behavior - empty comment prefix means ALL lines are treated as comments
            // because command.startsWith("") is always true
            List<String> result = parser.splitCommand("normal line");
            assertThat(result).isEmpty(); // Implementation treats everything as comment
        }
    }

    @Nested
    @DisplayName("Basic Splitting Tests")
    class BasicSplittingTests {

        @Test
        @DisplayName("should split by space")
        void testSpaceSplitting() {
            // Setup
            LineParser parser = new LineParser(" ", "", "//");

            // Execute
            List<String> result = parser.splitCommand("one two three four");

            // Verify
            assertThat(result).containsExactly("one", "two", "three", "four");
        }

        @Test
        @DisplayName("should split by comma")
        void testCommaSplitting() {
            // Setup
            LineParser parser = new LineParser(",", "", "#");

            // Execute
            List<String> result = parser.splitCommand("apple,banana,cherry,date");

            // Verify
            assertThat(result).containsExactly("apple", "banana", "cherry", "date");
        }

        @Test
        @DisplayName("should handle multiple separators")
        void testMultipleSeparators() {
            // Setup
            LineParser parser = new LineParser(" ", "", "//");

            // Execute
            List<String> result = parser.splitCommand("word1   word2    word3");

            // Verify - multiple spaces should be collapsed
            assertThat(result).containsExactly("word1", "word2", "word3");
        }

        @Test
        @DisplayName("should handle empty input")
        void testEmptyInput() {
            // Setup
            LineParser parser = LineParser.getDefaultLineParser();

            // Execute
            List<String> result1 = parser.splitCommand("");
            List<String> result2 = parser.splitCommand("   ");

            // Verify
            assertThat(result1).isEmpty();
            assertThat(result2).isEmpty(); // Trimmed to empty
        }
    }

    @Nested
    @DisplayName("Quote Handling Tests")
    class QuoteHandlingTests {

        @Test
        @DisplayName("should handle quoted strings at start")
        void testQuotedAtStart() {
            // Setup
            LineParser parser = LineParser.getDefaultLineParser();

            // Execute
            List<String> result = parser.splitCommand("\"first argument\" second third");

            // Verify
            assertThat(result).containsExactly("first argument", "second", "third");
        }

        @Test
        @DisplayName("should handle quoted strings at end")
        void testQuotedAtEnd() {
            // Setup
            LineParser parser = LineParser.getDefaultLineParser();

            // Execute
            List<String> result = parser.splitCommand("first second \"last argument\"");

            // Verify
            assertThat(result).containsExactly("first", "second", "last argument");
        }

        @Test
        @DisplayName("should handle multiple quoted strings")
        void testMultipleQuoted() {
            // Setup
            LineParser parser = LineParser.getDefaultLineParser();

            // Execute
            List<String> result = parser.splitCommand("\"first arg\" normal \"third arg\" final");

            // Verify - fixed behavior: no empty strings after quotes
            assertThat(result).containsExactly("first arg", "normal", "third arg", "final");
        }

        @Test
        @DisplayName("should handle unclosed quotes")
        void testUnclosedQuotes() {
            // Setup
            LineParser parser = LineParser.getDefaultLineParser();

            // Execute
            List<String> result = parser.splitCommand("\"unclosed quote and more text");

            // Verify - implementation splits on spaces, doesn't consume everything
            assertThat(result).containsExactly("unclosed", "quote", "and", "more", "text");
        }

        @Test
        @DisplayName("should handle empty quotes")
        void testEmptyQuotes() {
            // Setup
            LineParser parser = LineParser.getDefaultLineParser();

            // Execute
            List<String> result = parser.splitCommand("before \"\" after");

            // Verify - fixed behavior: empty quoted string should remain, no extra empty
            // string
            assertThat(result).containsExactly("before", "", "after");
        }

        @Test
        @DisplayName("should handle custom quote character")
        void testCustomQuoteCharacter() {
            // Setup
            LineParser parser = new LineParser(" ", "'", "//");

            // Execute
            List<String> result = parser.splitCommand("arg1 'quoted with single quotes' arg3");

            // Verify - fixed behavior: no empty strings after quotes
            assertThat(result).containsExactly("arg1", "quoted with single quotes", "arg3");
        }
    }

    @Nested
    @DisplayName("Comment Handling Tests")
    class CommentHandlingTests {

        @Test
        @DisplayName("should ignore lines starting with comment")
        void testCommentAtStart() {
            // Setup
            LineParser parser = LineParser.getDefaultLineParser();

            // Execute
            List<String> result = parser.splitCommand("// This is a comment line");

            // Verify
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("should handle custom comment prefix")
        void testCustomCommentPrefix() {
            // Setup
            LineParser parser = new LineParser(" ", "\"", "#");

            // Execute
            List<String> result1 = parser.splitCommand("# This is a comment");
            List<String> result2 = parser.splitCommand("## Double hash comment");
            List<String> result3 = parser.splitCommand("normal line");

            // Verify
            assertThat(result1).isEmpty();
            assertThat(result2).isEmpty();
            assertThat(result3).containsExactly("normal", "line");
        }

        @Test
        @DisplayName("should handle multi-character comment prefix")
        void testMultiCharacterComment() {
            // Setup
            LineParser parser = new LineParser(" ", "\"", "<!--");

            // Execute
            List<String> result1 = parser.splitCommand("<!-- XML style comment");
            List<String> result2 = parser.splitCommand("normal line");
            List<String> result3 = parser.splitCommand("<-- not a comment");

            // Verify
            assertThat(result1).isEmpty();
            assertThat(result2).containsExactly("normal", "line");
            assertThat(result3).containsExactly("<--", "not", "a", "comment");
        }

        @Test
        @DisplayName("should handle whitespace before comment")
        void testWhitespaceBeforeComment() {
            // Setup
            LineParser parser = LineParser.getDefaultLineParser();

            // Execute
            List<String> result = parser.splitCommand("   // Comment with leading spaces");

            // Verify - Input is trimmed, so this should be treated as comment
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("should handle single character input")
        void testSingleCharacter() {
            // Setup
            LineParser parser = LineParser.getDefaultLineParser();

            // Execute
            List<String> result = parser.splitCommand("a");

            // Verify
            assertThat(result).containsExactly("a");
        }

        @Test
        @DisplayName("should handle only separators")
        void testOnlySeparators() {
            // Setup
            LineParser parser = new LineParser(",", "", "//");

            // Execute
            List<String> result = parser.splitCommand(",,,");

            // Verify - consecutive separators should be collapsed to empty result
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("should handle only quotes")
        void testOnlyQuotes() {
            // Setup
            LineParser parser = LineParser.getDefaultLineParser();

            // Execute
            List<String> result = parser.splitCommand("\"\"\"\"");

            // Verify - This should create empty quoted strings
            assertThat(result).hasSize(2).containsOnly("");
        }

        @Test
        @DisplayName("should handle special characters in arguments")
        void testSpecialCharacters() {
            // Setup
            LineParser parser = LineParser.getDefaultLineParser();

            // Execute
            List<String> result = parser.splitCommand("arg1 \"special chars: !@#$%^&*()\" arg3");

            // Verify
            assertThat(result).containsExactly("arg1", "special chars: !@#$%^&*()", "arg3");
        }

        @Test
        @DisplayName("should handle unicode characters")
        void testUnicodeCharacters() {
            // Setup
            LineParser parser = LineParser.getDefaultLineParser();

            // Execute
            List<String> result = parser.splitCommand("测试 \"κόσμος\" файл");

            // Verify - fixed behavior: no empty strings after quotes
            assertThat(result).containsExactly("测试", "κόσμος", "файл");
        }

        @ParameterizedTest
        @ValueSource(strings = { "\t", "\n", "\r", "\r\n" })
        @DisplayName("should handle whitespace separators")
        void testWhitespaceSeparators(String separator) {
            // Setup
            LineParser parser = new LineParser(separator, "", "//");

            // Execute
            List<String> result = parser.splitCommand("word1" + separator + "word2" + separator + "word3");

            // Verify
            assertThat(result).containsExactly("word1", "word2", "word3");
        }
    }

    @Nested
    @DisplayName("Complex Parsing Tests")
    class ComplexParsingTests {

        @Test
        @DisplayName("should handle nested quotes correctly")
        void testNestedQuotes() {
            // Setup
            LineParser parser = new LineParser(" ", "'", "//");

            // Execute
            List<String> result = parser.splitCommand("arg1 'text with \"inner quotes\"' arg3");

            // Verify - fixed behavior: no empty strings after quotes
            assertThat(result).containsExactly("arg1", "text with \"inner quotes\"", "arg3");
        }

        @Test
        @DisplayName("should handle quotes containing separator")
        void testQuotesWithSeparator() {
            // Setup
            LineParser parser = new LineParser(",", "\"", "#");

            // Execute
            List<String> result = parser.splitCommand("arg1,\"text,with,commas\",arg3");

            // Verify - fixed behavior: no empty strings after quotes
            assertThat(result).containsExactly("arg1", "text,with,commas", "arg3");
        }

        @Test
        @DisplayName("should handle mixed quote and separator scenarios")
        void testMixedQuoteAndSeparator() {
            // Setup
            LineParser parser = LineParser.getDefaultLineParser();

            // Execute
            List<String> result = parser
                    .splitCommand("program \"file with spaces.txt\" --output \"result file.dat\" --verbose");

            // Verify - implementation adds empty strings after quotes
            assertThat(result).containsExactly(
                    "program",
                    "file with spaces.txt",
                    "--output",
                    "result file.dat",
                    "--verbose");
        }

        @Test
        @DisplayName("should handle command line with options and quoted paths")
        void testCommandLineStyle() {
            // Setup
            LineParser parser = LineParser.getDefaultLineParser();

            // Execute
            List<String> result = parser
                    .splitCommand("java -cp \"C:\\Program Files\\Java\\lib\" -Xmx1024m Main \"input file.txt\"");

            // Verify - implementation adds empty strings after quotes
            assertThat(result).containsExactly(
                    "java",
                    "-cp",
                    "C:\\Program Files\\Java\\lib",
                    "-Xmx1024m",
                    "Main",
                    "input file.txt");
        }
    }

    @Nested
    @DisplayName("Real-world Usage Tests")
    class RealWorldUsageTests {

        @Test
        @DisplayName("should parse CSV-style with custom delimiter")
        void testCsvStyle() {
            // Setup
            LineParser parser = new LineParser(",", "\"", "#");

            // Execute
            List<String> result = parser.splitCommand("name,age,\"city, state\",country");

            // Verify - fixed behavior: no empty strings after quotes
            assertThat(result).containsExactly("name", "age", "city, state", "country");
        }

        @Test
        @DisplayName("should parse configuration file lines")
        void testConfigurationFile() {
            // Setup
            LineParser parser = new LineParser("=", "\"", "#");

            // Execute
            List<String> result1 = parser.splitCommand("database.url=\"jdbc:mysql://localhost/test\"");
            List<String> result2 = parser.splitCommand("# This is a config comment");
            List<String> result3 = parser.splitCommand("timeout=30");

            // Verify - fixed behavior: no empty strings after quotes
            assertThat(result1).containsExactly("database.url", "jdbc:mysql://localhost/test");
            assertThat(result2).isEmpty();
            assertThat(result3).containsExactly("timeout", "30");
        }

        @Test
        @DisplayName("should parse shell command lines")
        void testShellCommands() {
            // Setup
            LineParser parser = LineParser.getDefaultLineParser();

            // Execute
            List<String> result = parser.splitCommand("grep -r \"error message\" \"/var/log\" --include=\"*.log\"");

            // Verify - fixed behavior: no empty strings after quotes
            assertThat(result).containsExactly(
                    "grep",
                    "-r",
                    "error message",
                    "/var/log",
                    "*.log");
        }

        @Test
        @DisplayName("should handle SQL-like parsing")
        void testSqlLikeParsing() {
            // Setup
            LineParser parser = new LineParser(" ", "'", "--");

            // Execute
            List<String> result1 = parser.splitCommand("SELECT name FROM users WHERE city = 'New York'");
            List<String> result2 = parser.splitCommand("-- This is a SQL comment");

            // Verify
            assertThat(result1).containsExactly("SELECT", "name", "FROM", "users", "WHERE", "city", "=", "New York");
            assertThat(result2).isEmpty();
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @Test
        @DisplayName("should handle large input efficiently")
        void testLargeInput() {
            // Setup
            LineParser parser = LineParser.getDefaultLineParser();
            StringBuilder large = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                large.append("arg").append(i).append(" ");
            }

            // Execute
            List<String> result = parser.splitCommand(large.toString());

            // Verify
            assertThat(result).hasSize(1000);
            assertThat(result.get(0)).isEqualTo("arg0");
            assertThat(result.get(999)).isEqualTo("arg999");
        }

        @Test
        @DisplayName("should handle many quoted arguments")
        void testManyQuotedArguments() {
            // Setup
            LineParser parser = LineParser.getDefaultLineParser();
            StringBuilder input = new StringBuilder();
            for (int i = 0; i < 100; i++) {
                input.append("\"quoted arg ").append(i).append("\" ");
            }

            // Execute
            List<String> result = parser.splitCommand(input.toString());

            // Verify - fixed behavior: no empty strings after quotes
            assertThat(result).hasSize(100); // Just 100 quoted args, no empty strings
            assertThat(result.get(0)).isEqualTo("quoted arg 0");
            assertThat(result.get(1)).isEqualTo("quoted arg 1");
            assertThat(result.get(99)).isEqualTo("quoted arg 99");
        }
    }
}
