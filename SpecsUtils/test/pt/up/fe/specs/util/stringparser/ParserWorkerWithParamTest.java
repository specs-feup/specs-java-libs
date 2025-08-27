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
                String result = prefix + wordResult.getResult();
                return new ParserResult<>(wordResult.getModifiedString(), result);
            };

            StringSlice input = new StringSlice("hello world");
            ParserResult<String> result = parser.apply(input, "PREFIX_");

            assertThat(result.getResult()).isEqualTo("PREFIX_hello");
            assertThat(result.getModifiedString().toString()).isEqualTo(" world");
        }

        @Test
        @DisplayName("Should handle numeric parameter")
        void testNumericParameter() {
            // Create a parser that multiplies parsed integer by parameter
            ParserWorkerWithParam<Integer, Integer> parser = (slice, multiplier) -> {
                ParserResult<Integer> intResult = StringParsersLegacy.parseInt(slice);
                Integer result = intResult.getResult() * multiplier;
                return new ParserResult<>(intResult.getModifiedString(), result);
            };

            StringSlice input = new StringSlice("5 remainder");
            ParserResult<Integer> result = parser.apply(input, 3);

            assertThat(result.getResult()).isEqualTo(15);
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should support BiFunction interface")
        void testBiFunctionInterface() {
            // ParserWorkerWithParam extends BiFunction, so we can use it as such
            ParserWorkerWithParam<String, String> parser = (slice, suffix) -> {
                ParserResult<String> wordResult = StringParsers.parseWord(slice);
                String result = wordResult.getResult() + suffix;
                return new ParserResult<>(wordResult.getModifiedString(), result);
            };

            // Use as BiFunction
            StringSlice input = new StringSlice("test content");
            ParserResult<String> result = parser.apply(input, "_SUFFIX");

            assertThat(result.getResult()).isEqualTo("test_SUFFIX");
            assertThat(result.getModifiedString().toString()).isEqualTo(" content");
        }

        @Test
        @DisplayName("Should handle empty parameter")
        void testEmptyParameter() {
            ParserWorkerWithParam<String, String> parser = (slice, param) -> {
                ParserResult<String> wordResult = StringParsers.parseWord(slice);
                String result = param.isEmpty() ? wordResult.getResult() : param + ":" + wordResult.getResult();
                return new ParserResult<>(wordResult.getModifiedString(), result);
            };

            StringSlice input = new StringSlice("word remainder");
            ParserResult<String> result = parser.apply(input, "");

            assertThat(result.getResult()).isEqualTo("word");
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
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
                String result = prefix + wordResult.getResult() + suffix;
                return new ParserResult<>(wordResult.getModifiedString(), result);
            };

            StringSlice input = new StringSlice("middle remainder");
            ParserResult<String> result = parser.apply(input, "<<", ">>");

            assertThat(result.getResult()).isEqualTo("<<middle>>");
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
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
                    result.append(wordResult.getResult());
                }
                return new ParserResult<>(wordResult.getModifiedString(), result.toString());
            };

            StringSlice input = new StringSlice("test remainder");
            ParserResult<String> result = parser.apply(input, 3, "-");

            assertThat(result.getResult()).isEqualTo("test-test-test");
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should handle null parameters gracefully")
        void testNullParameters() {
            ParserWorkerWithParam2<String, String, String> parser = (slice, param1, param2) -> {
                ParserResult<String> wordResult = StringParsers.parseWord(slice);
                String result = (param1 != null ? param1 : "") + wordResult.getResult()
                        + (param2 != null ? param2 : "");
                return new ParserResult<>(wordResult.getModifiedString(), result);
            };

            StringSlice input = new StringSlice("word remainder");
            ParserResult<String> result = parser.apply(input, null, "_end");

            assertThat(result.getResult()).isEqualTo("word_end");
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
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
                String word = wordResult.getResult();
                String result = prefix + separator + word + separator + suffix;
                return new ParserResult<>(wordResult.getModifiedString(), result);
            };

            StringSlice input = new StringSlice("content remainder");
            ParserResult<String> result = parser.apply(input, "START", "END", "|");

            assertThat(result.getResult()).isEqualTo("START|content|END");
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should handle complex parameter combinations")
        void testComplexParameterCombinations() {
            // Create a parser that uses boolean, integer, and string parameters
            ParserWorkerWithParam3<String, Boolean, Integer, String> parser = (slice, uppercase, repeat, prefix) -> {
                ParserResult<String> wordResult = StringParsers.parseWord(slice);
                String word = wordResult.getResult();

                if (uppercase) {
                    word = word.toUpperCase();
                }

                StringBuilder result = new StringBuilder();
                for (int i = 0; i < repeat; i++) {
                    result.append(prefix).append(word);
                }

                return new ParserResult<>(wordResult.getModifiedString(), result.toString());
            };

            StringSlice input = new StringSlice("hello remainder");
            ParserResult<String> result = parser.apply(input, true, 2, ">");

            assertThat(result.getResult()).isEqualTo(">HELLO>HELLO");
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should handle all string parameters")
        void testAllStringParameters() {
            ParserWorkerWithParam3<String, String, String, String> parser = (slice, param1, param2, param3) -> {
                ParserResult<String> wordResult = StringParsers.parseWord(slice);
                String result = String.join(":", param1, param2, param3, wordResult.getResult());
                return new ParserResult<>(wordResult.getModifiedString(), result);
            };

            StringSlice input = new StringSlice("word remainder");
            ParserResult<String> result = parser.apply(input, "A", "B", "C");

            assertThat(result.getResult()).isEqualTo("A:B:C:word");
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
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
                String word = wordResult.getResult();
                String result = String.format("%s[%s|%s|%s]%s", p1, p2, word, p3, p4);
                return new ParserResult<>(wordResult.getModifiedString(), result);
            };

            StringSlice input = new StringSlice("center remainder");
            ParserResult<String> result = parser.apply(input, "START", "LEFT", "RIGHT", "END");

            assertThat(result.getResult()).isEqualTo("START[LEFT|center|RIGHT]END");
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should handle mixed parameter types with four parameters")
        void testMixedFourParameterTypes() {
            // Create a parser using Integer, Boolean, String, and Character types
            ParserWorkerWithParam4<String, Integer, Boolean, String, Character> parser = (slice, num, flag, prefix,
                    separator) -> {
                ParserResult<String> wordResult = StringParsers.parseWord(slice);
                String word = wordResult.getResult();

                StringBuilder result = new StringBuilder();
                for (int i = 0; i < num; i++) {
                    if (i > 0)
                        result.append(separator);
                    result.append(prefix);
                    result.append(flag ? word.toUpperCase() : word);
                }

                return new ParserResult<>(wordResult.getModifiedString(), result.toString());
            };

            StringSlice input = new StringSlice("test remainder");
            ParserResult<String> result = parser.apply(input, 3, true, ">>", '-');

            assertThat(result.getResult()).isEqualTo(">>TEST->>TEST->>TEST");
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
        }

        @Test
        @DisplayName("Should handle complex computational logic")
        void testComplexComputationalLogic() {
            // Create a parser that performs complex operations with four parameters
            ParserWorkerWithParam4<Integer, Integer, Integer, String, Boolean> parser = (slice, base, multiplier,
                    operation, addLength) -> {
                ParserResult<Integer> intResult = StringParsersLegacy.parseInt(slice);
                int value = intResult.getResult();

                int result = switch (operation) {
                    case "add" -> base + value * multiplier;
                    case "subtract" -> base - value * multiplier;
                    case "multiply" -> base * value * multiplier;
                    default -> value;
                };

                if (addLength) {
                    result += slice.toString().length();
                }

                return new ParserResult<>(intResult.getModifiedString(), result);
            };

            StringSlice input = new StringSlice("5 remainder");
            ParserResult<Integer> result = parser.apply(input, 10, 3, "add", false);

            assertThat(result.getResult()).isEqualTo(25); // 10 + (5 * 3)
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
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
                return new ParserResult<>(wordResult.getModifiedString(), prefix + wordResult.getResult());
            };

            ParserResult<String> result1 = parser1.apply(input, "1:");
            assertThat(result1.getResult()).isEqualTo("1:hello");

            // Use two parameter parser on remaining
            ParserWorkerWithParam2<String, String, String> parser2 = (slice, prefix, suffix) -> {
                ParserResult<String> wordResult = StringParsers.parseWord(slice.trim());
                return new ParserResult<>(wordResult.getModifiedString(), prefix + wordResult.getResult() + suffix);
            };

            ParserResult<String> result2 = parser2.apply(result1.getModifiedString(), "2[", "]");
            assertThat(result2.getResult()).isEqualTo("2[world]");

            // Use three parameter parser on remaining
            ParserWorkerWithParam3<String, String, String, String> parser3 = (slice, p1, p2, p3) -> {
                ParserResult<String> wordResult = StringParsers.parseWord(slice.trim());
                return new ParserResult<>(wordResult.getModifiedString(), p1 + p2 + wordResult.getResult() + p3);
            };

            ParserResult<String> result3 = parser3.apply(result2.getModifiedString(), "3", "(", ")");
            assertThat(result3.getResult()).isEqualTo("3(test)");
        }

        @Test
        @DisplayName("Should support different generic type combinations")
        void testGenericTypeCombinations() {
            StringSlice input = new StringSlice("42 remainder");

            // Parser that converts string to integer with parameters
            ParserWorkerWithParam2<Integer, String, Integer> parser = (slice, prefix, multiplier) -> {
                ParserResult<Integer> intResult = StringParsersLegacy.parseInt(slice);
                Integer result = Integer.parseInt(prefix + intResult.getResult()) * multiplier;
                return new ParserResult<>(intResult.getModifiedString(), result);
            };

            ParserResult<Integer> result = parser.apply(input, "1", 2);
            assertThat(result.getResult()).isEqualTo(284); // (1 + 42) * 2 = 86, but string concat: "142" * 2 = 284
            assertThat(result.getModifiedString().toString()).isEqualTo(" remainder");
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
                return new ParserResult<>(wordResult.getModifiedString(), param + wordResult.getResult());
            };

            StringSlice input = new StringSlice("");
            ParserResult<String> result = parser.apply(input, "PREFIX_");

            assertThat(result.getResult()).isEqualTo("PREFIX_EMPTY");
            assertThat(result.getModifiedString().toString()).isEqualTo("");
        }

        @Test
        @DisplayName("Should handle exception scenarios")
        void testExceptionHandling() {
            ParserWorkerWithParam2<String, Integer, String> parser = (slice, divider, fallback) -> {
                try {
                    ParserResult<Integer> intResult = StringParsersLegacy.parseInt(slice);
                    String result = String.valueOf(intResult.getResult() / divider);
                    return new ParserResult<>(intResult.getModifiedString(), result);
                } catch (Exception e) {
                    return new ParserResult<>(slice, fallback);
                }
            };

            // Test division by zero
            StringSlice input = new StringSlice("10 remainder");
            ParserResult<String> result = parser.apply(input, 0, "ERROR");

            assertThat(result.getResult()).isEqualTo("ERROR");
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
                return new ParserResult<>(wordResult.getModifiedString(), param + ":" + wordResult.getResult());
            };

            StringSlice input = new StringSlice("word remainder");
            ParserResult<String> result = parser.apply(input, longParam.toString());

            assertThat(result.getResult()).startsWith("AAAA");
            assertThat(result.getResult()).endsWith(":word");
            assertThat(result.getResult()).hasSize(1005); // 1000 A's + ":" + "word"
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
                return new ParserResult<>(wordResult.getModifiedString(), prefix + wordResult.getResult());
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
                String word = uppercase ? wordResult.getResult().toUpperCase() : wordResult.getResult();

                StringBuilder result = new StringBuilder();
                for (int i = 0; i < repeat; i++) {
                    if (i > 0)
                        result.append(separator);
                    result.append(prefix).append(word);
                }

                return new ParserResult<>(wordResult.getModifiedString(), result.toString());
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
