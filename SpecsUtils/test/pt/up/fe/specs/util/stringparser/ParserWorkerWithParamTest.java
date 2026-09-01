package pt.up.fe.specs.util.stringparser;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.RetryingTest;

import pt.up.fe.specs.util.utilities.StringSlice;

/**
 * Comprehensive test suite for ParserWorkerWithParam functional interfaces.
 * Tests parameterized parsing functionality with 1-4 parameters.
 * 
 * @author Generated Tests
 */
@DisplayName("ParserWorkerWithParam Tests")
public class ParserWorkerWithParamTest {

    @Nested
    @DisplayName("ParserWorkerWithParam Tests")
    class ParserWorkerWithParamTests {

        @Test
        @DisplayName("Should implement single parameter parsing")
        void testSingleParameterParsing() {
            // Create a parser that prepends the parameter to the parsed word
            ParserWorkerWithParam<String, String> parser = (slice, prefix) -> {
                ParserResult<String> wordResult = StringParsers.parseWord(slice);
                String result = prefix + wordResult.result();
                return new ParserResult<>(wordResult.modifiedString(), result);
            };

            StringSlice input = new StringSlice("hello world");
            ParserResult<String> result = parser.apply(input, "PREFIX_");

            assertThat(result.result()).isEqualTo("PREFIX_hello");
            assertThat(result.modifiedString().toString()).isEqualTo(" world");
        }

        @Test
        @DisplayName("Should support BiFunction interface")
        void testBiFunctionInterface() {
            // ParserWorkerWithParam extends BiFunction, so we can use it as such
            ParserWorkerWithParam<String, String> parser = (slice, suffix) -> {
                ParserResult<String> wordResult = StringParsers.parseWord(slice);
                String result = wordResult.result() + suffix;
                return new ParserResult<>(wordResult.modifiedString(), result);
            };

            // Use as BiFunction
            StringSlice input = new StringSlice("test content");
            ParserResult<String> result = parser.apply(input, "_SUFFIX");

            assertThat(result.result()).isEqualTo("test_SUFFIX");
            assertThat(result.modifiedString().toString()).isEqualTo(" content");
        }

        @Test
        @DisplayName("Should handle empty parameter")
        void testEmptyParameter() {
            ParserWorkerWithParam<String, String> parser = (slice, param) -> {
                ParserResult<String> wordResult = StringParsers.parseWord(slice);
                String result = param.isEmpty() ? wordResult.result() : param + ":" + wordResult.result();
                return new ParserResult<>(wordResult.modifiedString(), result);
            };

            StringSlice input = new StringSlice("word remainder");
            ParserResult<String> result = parser.apply(input, "");

            assertThat(result.result()).isEqualTo("word");
            assertThat(result.modifiedString().toString()).isEqualTo(" remainder");
        }
    }

    @Nested
    @DisplayName("ParserWorkerWithParam2 Tests")
    class ParserWorkerWithParam2Tests {

        @Test
        @DisplayName("Should implement two parameter parsing")
        void testTwoParameterParsing() {
            // Create a parser that formats the parsed word with two parameters
            ParserWorkerWithParam2<String, String, String> parser = (slice, prefix, suffix) -> {
                ParserResult<String> wordResult = StringParsers.parseWord(slice);
                String result = prefix + wordResult.result() + suffix;
                return new ParserResult<>(wordResult.modifiedString(), result);
            };

            StringSlice input = new StringSlice("middle remainder");
            ParserResult<String> result = parser.apply(input, "<<", ">>");

            assertThat(result.result()).isEqualTo("<<middle>>");
            assertThat(result.modifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should handle mixed parameter types")
        void testMixedParameterTypes() {
            // Create a parser that uses string and numeric parameters
            ParserWorkerWithParam2<String, Integer, String> parser = (slice, count, delimiter) -> {
                ParserResult<String> wordResult = StringParsers.parseWord(slice);
                StringBuilder result = new StringBuilder();
                for (int i = 0; i < count; i++) {
                    if (i > 0)
                        result.append(delimiter);
                    result.append(wordResult.result());
                }
                return new ParserResult<>(wordResult.modifiedString(), result.toString());
            };

            StringSlice input = new StringSlice("test remainder");
            ParserResult<String> result = parser.apply(input, 3, "-");

            assertThat(result.result()).isEqualTo("test-test-test");
            assertThat(result.modifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should handle null parameters gracefully")
        void testNullParameters() {
            ParserWorkerWithParam2<String, String, String> parser = (slice, param1, param2) -> {
                ParserResult<String> wordResult = StringParsers.parseWord(slice);
                String result = (param1 != null ? param1 : "") + wordResult.result()
                        + (param2 != null ? param2 : "");
                return new ParserResult<>(wordResult.modifiedString(), result);
            };

            StringSlice input = new StringSlice("word remainder");
            ParserResult<String> result = parser.apply(input, null, "_end");

            assertThat(result.result()).isEqualTo("word_end");
            assertThat(result.modifiedString().toString()).isEqualTo(" remainder");
        }
    }

    @Nested
    @DisplayName("ParserWorkerWithParam3 Tests")
    class ParserWorkerWithParam3Tests {

        @Test
        @DisplayName("Should implement three parameter parsing")
        void testThreeParameterParsing() {
            // Create a parser that formats with three parameters
            ParserWorkerWithParam3<String, String, String, String> parser = (slice, prefix, suffix, separator) -> {
                ParserResult<String> wordResult = StringParsers.parseWord(slice);
                String word = wordResult.result();
                String result = prefix + separator + word + separator + suffix;
                return new ParserResult<>(wordResult.modifiedString(), result);
            };

            StringSlice input = new StringSlice("content remainder");
            ParserResult<String> result = parser.apply(input, "START", "END", "|");

            assertThat(result.result()).isEqualTo("START|content|END");
            assertThat(result.modifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should handle complex parameter combinations")
        void testComplexParameterCombinations() {
            // Create a parser that uses boolean, integer, and string parameters
            ParserWorkerWithParam3<String, Boolean, Integer, String> parser = (slice, uppercase, repeat, prefix) -> {
                ParserResult<String> wordResult = StringParsers.parseWord(slice);
                String word = wordResult.result();

                if (uppercase) {
                    word = word.toUpperCase();
                }

                StringBuilder result = new StringBuilder();
                for (int i = 0; i < repeat; i++) {
                    result.append(prefix).append(word);
                }

                return new ParserResult<>(wordResult.modifiedString(), result.toString());
            };

            StringSlice input = new StringSlice("hello remainder");
            ParserResult<String> result = parser.apply(input, true, 2, ">");

            assertThat(result.result()).isEqualTo(">HELLO>HELLO");
            assertThat(result.modifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should handle all string parameters")
        void testAllStringParameters() {
            ParserWorkerWithParam3<String, String, String, String> parser = (slice, param1, param2, param3) -> {
                ParserResult<String> wordResult = StringParsers.parseWord(slice);
                String result = String.join(":", param1, param2, param3, wordResult.result());
                return new ParserResult<>(wordResult.modifiedString(), result);
            };

            StringSlice input = new StringSlice("word remainder");
            ParserResult<String> result = parser.apply(input, "A", "B", "C");

            assertThat(result.result()).isEqualTo("A:B:C:word");
            assertThat(result.modifiedString().toString()).isEqualTo(" remainder");
        }
    }

    @Nested
    @DisplayName("ParserWorkerWithParam4 Tests")
    class ParserWorkerWithParam4Tests {

        @Test
        @DisplayName("Should implement four parameter parsing")
        void testFourParameterParsing() {
            // Create a parser that formats with four parameters
            ParserWorkerWithParam4<String, String, String, String, String> parser = (slice, p1, p2, p3, p4) -> {
                ParserResult<String> wordResult = StringParsers.parseWord(slice);
                String word = wordResult.result();
                String result = String.format("%s[%s|%s|%s]%s", p1, p2, word, p3, p4);
                return new ParserResult<>(wordResult.modifiedString(), result);
            };

            StringSlice input = new StringSlice("center remainder");
            ParserResult<String> result = parser.apply(input, "START", "LEFT", "RIGHT", "END");

            assertThat(result.result()).isEqualTo("START[LEFT|center|RIGHT]END");
            assertThat(result.modifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should handle mixed parameter types with four parameters")
        void testMixedFourParameterTypes() {
            // Create a parser using Integer, Boolean, String, and Character types
            ParserWorkerWithParam4<String, Integer, Boolean, String, Character> parser = (slice, num, flag, prefix,
                    separator) -> {
                ParserResult<String> wordResult = StringParsers.parseWord(slice);
                String word = wordResult.result();

                StringBuilder result = new StringBuilder();
                for (int i = 0; i < num; i++) {
                    if (i > 0)
                        result.append(separator);
                    result.append(prefix);
                    result.append(flag ? word.toUpperCase() : word);
                }

                return new ParserResult<>(wordResult.modifiedString(), result.toString());
            };

            StringSlice input = new StringSlice("test remainder");
            ParserResult<String> result = parser.apply(input, 3, true, ">>", '-');

            assertThat(result.result()).isEqualTo(">>TEST->>TEST->>TEST");
            assertThat(result.modifiedString().toString()).isEqualTo(" remainder");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should chain different parameter count parsers")
        void testChainedParameterParsers() {
            StringSlice input = new StringSlice("hello world test");

            // Use single parameter parser
            ParserWorkerWithParam<String, String> parser1 = (slice, prefix) -> {
                ParserResult<String> wordResult = StringParsers.parseWord(slice);
                return new ParserResult<>(wordResult.modifiedString(), prefix + wordResult.result());
            };

            ParserResult<String> result1 = parser1.apply(input, "1:");
            assertThat(result1.result()).isEqualTo("1:hello");

            // Use two parameter parser on remaining
            ParserWorkerWithParam2<String, String, String> parser2 = (slice, prefix, suffix) -> {
                ParserResult<String> wordResult = StringParsers.parseWord(slice.trim());
                return new ParserResult<>(wordResult.modifiedString(), prefix + wordResult.result() + suffix);
            };

            ParserResult<String> result2 = parser2.apply(result1.modifiedString(), "2[", "]");
            assertThat(result2.result()).isEqualTo("2[world]");

            // Use three parameter parser on remaining
            ParserWorkerWithParam3<String, String, String, String> parser3 = (slice, p1, p2, p3) -> {
                ParserResult<String> wordResult = StringParsers.parseWord(slice.trim());
                return new ParserResult<>(wordResult.modifiedString(), p1 + p2 + wordResult.result() + p3);
            };

            ParserResult<String> result3 = parser3.apply(result2.modifiedString(), "3", "(", ")");
            assertThat(result3.result()).isEqualTo("3(test)");
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle empty input gracefully")
        void testEmptyInput() {
            ParserWorkerWithParam<String, String> parser = (slice, param) -> {
                if (slice.isEmpty()) {
                    return new ParserResult<>(slice, param + "EMPTY");
                }
                ParserResult<String> wordResult = StringParsers.parseWord(slice);
                return new ParserResult<>(wordResult.modifiedString(), param + wordResult.result());
            };

            StringSlice input = new StringSlice("");
            ParserResult<String> result = parser.apply(input, "PREFIX_");

            assertThat(result.result()).isEqualTo("PREFIX_EMPTY");
            assertThat(result.modifiedString().toString()).isEqualTo("");
        }

        @Test
        @DisplayName("Should handle very long parameter lists")
        void testLongParameterValues() {
            StringBuilder longParam = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                longParam.append("A");
            }

            ParserWorkerWithParam<String, String> parser = (slice, param) -> {
                ParserResult<String> wordResult = StringParsers.parseWord(slice);
                return new ParserResult<>(wordResult.modifiedString(), param + ":" + wordResult.result());
            };

            StringSlice input = new StringSlice("word remainder");
            ParserResult<String> result = parser.apply(input, longParam.toString());

            assertThat(result.result()).startsWith("AAAA");
            assertThat(result.result()).endsWith(":word");
            assertThat(result.result()).hasSize(1005); // 1000 A's + ":" + "word"
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @RetryingTest(5)
        @DisplayName("Should handle repeated parsing efficiently")
        void testRepeatedParsingPerformance() {
            ParserWorkerWithParam<String, String> parser = (slice, prefix) -> {
                ParserResult<String> wordResult = StringParsers.parseWord(slice);
                return new ParserResult<>(wordResult.modifiedString(), prefix + wordResult.result());
            };

            StringSlice input = new StringSlice("word remainder");

            long startTime = System.nanoTime();

            for (int i = 0; i < 10000; i++) {
                parser.apply(new StringSlice(input), "prefix_");
            }

            long duration = System.nanoTime() - startTime;
            assertThat(duration).isLessThan(50_000_000L); // 50ms
        }

        @RetryingTest(5)
        @DisplayName("Should handle complex multi-parameter parsing efficiently")
        void testComplexMultiParameterPerformance() {
            ParserWorkerWithParam4<String, String, Integer, Boolean, Character> parser = (slice, prefix, repeat,
                    uppercase, separator) -> {
                ParserResult<String> wordResult = StringParsers.parseWord(slice);
                String word = uppercase ? wordResult.result().toUpperCase() : wordResult.result();

                StringBuilder result = new StringBuilder();
                for (int i = 0; i < repeat; i++) {
                    if (i > 0)
                        result.append(separator);
                    result.append(prefix).append(word);
                }

                return new ParserResult<>(wordResult.modifiedString(), result.toString());
            };

            StringSlice input = new StringSlice("test remainder");

            long startTime = System.nanoTime();

            for (int i = 0; i < 1000; i++) {
                parser.apply(new StringSlice(input), ">>", 3, true, '-');
            }

            long duration = System.nanoTime() - startTime;
            assertThat(duration).isLessThan(100_000_000L); // 100ms
        }
    }
}
