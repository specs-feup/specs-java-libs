package pt.up.fe.specs.util.stringparser;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;
import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.stringsplitter.StringSliceWithSplit;
import pt.up.fe.specs.util.utilities.StringSlice;

/**
 * Comprehensive test suite for {@link StringParser}.
 * Tests the core parsing functionality including parser application, string
 * manipulation, and trim behavior configuration.
 * 
 * @author Generated Tests
 */
@DisplayName("StringParser Tests")
public class StringParserTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create StringParser from String")
        void testStringConstructor() {
            String input = "hello world";
            StringParser parser = new StringParser(input);

            assertThat(parser.toString()).isEqualTo(input);
            assertThat(parser.isEmpty()).isFalse();
        }

        @Test
        @DisplayName("Should create StringParser from StringSlice")
        void testStringSliceConstructor() {
            StringSlice slice = new StringSlice("test string");
            StringParser parser = new StringParser(slice);

            assertThat(parser.toString()).isEqualTo("test string");
            assertThat(parser.getCurrentString()).isNotNull();
        }

        @Test
        @DisplayName("Should create StringParser from StringSliceWithSplit")
        void testStringSliceWithSplitConstructor() {
            StringSliceWithSplit slice = new StringSliceWithSplit(new StringSlice("split test"));
            StringParser parser = new StringParser(slice);

            assertThat(parser.toString()).isEqualTo("split test");
        }

        @Test
        @DisplayName("Should create StringParser with trim configuration")
        void testTrimConfigConstructor() {
            StringSliceWithSplit slice = new StringSliceWithSplit(new StringSlice("  trimmed  "));
            StringParser parser = new StringParser(slice, false); // No auto-trim

            assertThat(parser.toString()).isEqualTo("  trimmed  ");
        }

        @Test
        @DisplayName("Should handle empty string construction")
        void testEmptyStringConstruction() {
            StringParser parser = new StringParser("");

            assertThat(parser.isEmpty()).isTrue();
            assertThat(parser.toString()).isEmpty();
        }
    }

    @Nested
    @DisplayName("ParserWorker Application Tests")
    class ParserWorkerTests {

        @Test
        @DisplayName("Should apply basic ParserWorker")
        void testBasicParserWorker() {
            StringParser parser = new StringParser("hello world");

            ParserWorker<String> worker = slice -> {
                String result = slice.substring(0, 5).toString();
                StringSlice remaining = slice.substring(5);
                return new ParserResult<>(remaining, result);
            };

            String result = parser.apply(worker);

            assertThat(result).isEqualTo("hello");
            assertThat(parser.toString().trim()).isEqualTo("world");
        }

        @Test
        @DisplayName("Should apply ParserWorkerWithParam")
        void testParserWorkerWithParam() {
            StringParser parser = new StringParser("123-456-789");

            ParserWorkerWithParam<String, String> worker = (slice, separator) -> {
                String str = slice.toString();
                int index = str.indexOf(separator);
                if (index == -1) {
                    return new ParserResult<>(slice.clear(), str);
                }
                String result = str.substring(0, index);
                StringSlice remaining = slice.substring(index + separator.length());
                return new ParserResult<>(remaining, result);
            };

            String result = parser.apply(worker, "-");

            assertThat(result).isEqualTo("123");
            assertThat(parser.toString().trim()).isEqualTo("456-789");
        }

        @Test
        @DisplayName("Should apply ParserWorkerWithParam2")
        void testParserWorkerWithParam2() {
            StringParser parser = new StringParser("a,b,c,d");

            ParserWorkerWithParam2<String[], String, Integer> worker = (slice, separator, count) -> {
                String str = slice.toString();
                String[] parts = str.split(separator, count + 1);
                String[] result = new String[Math.min(count, parts.length)];
                System.arraycopy(parts, 0, result, 0, result.length);

                StringSlice remaining = parts.length > count
                        ? new StringSlice(parts[count])
                        : slice.clear();

                return new ParserResult<>(remaining, result);
            };

            String[] result = parser.apply(worker, ",", 2);

            assertThat(result).containsExactly("a", "b");
            assertThat(parser.toString().trim()).isEqualTo("c,d");
        }

        @Test
        @DisplayName("Should apply ParserWorkerWithParam3")
        void testParserWorkerWithParam3() {
            StringParser parser = new StringParser("prefix:middle:suffix");

            ParserWorkerWithParam3<String, String, String, Boolean> worker = (slice, start, end, includeDelims) -> {
                String str = slice.toString();
                int startIdx = str.indexOf(start);
                int endIdx = str.indexOf(end, startIdx + start.length());

                if (startIdx == -1 || endIdx == -1) {
                    return new ParserResult<>(slice.clear(), str);
                }

                String result = includeDelims
                        ? str.substring(startIdx, endIdx + end.length())
                        : str.substring(startIdx + start.length(), endIdx);

                StringSlice remaining = slice.substring(endIdx + end.length());
                return new ParserResult<>(remaining, result);
            };

            String result = parser.apply(worker, ":", ":", false);

            assertThat(result).isEqualTo("middle");
            assertThat(parser.toString().trim()).isEqualTo("suffix");
        }

        @Test
        @DisplayName("Should apply ParserWorkerWithParam4")
        void testParserWorkerWithParam4() {
            StringParser parser = new StringParser("key=value&other=data");

            ParserWorkerWithParam4<String, String, String, String, Boolean> worker = (slice, keyPrefix, valueSeparator,
                    entrySeparator, caseSensitive) -> {
                String str = slice.toString();
                if (!caseSensitive) {
                    str = str.toLowerCase();
                    keyPrefix = keyPrefix.toLowerCase();
                }

                String[] entries = str.split(entrySeparator);
                for (String entry : entries) {
                    if (entry.startsWith(keyPrefix)) {
                        String[] keyValue = entry.split(valueSeparator, 2);
                        if (keyValue.length == 2) {
                            StringSlice remaining = slice.substring(slice.toString().indexOf(entry) + entry.length());
                            return new ParserResult<>(remaining, keyValue[1]);
                        }
                    }
                }

                return new ParserResult<>(slice.clear(), "");
            };

            String result = parser.apply(worker, "key", "=", "&", true);

            assertThat(result).isEqualTo("value");
        }

        @Test
        @DisplayName("Should apply function-based worker")
        void testFunctionWorker() {
            StringParser parser = new StringParser("   test   ");

            Function<StringParser, Integer> worker = p -> {
                p.trim();
                return p.toString().length();
            };

            Integer result = parser.applyFunction(worker);

            assertThat(result).isEqualTo(4);
            assertThat(parser.toString()).isEqualTo("test");
        }
    }

    @Nested
    @DisplayName("String Manipulation Tests")
    class StringManipulationTests {

        @Test
        @DisplayName("Should extract substring by index")
        void testSubstring() {
            StringParser parser = new StringParser("hello world");

            String consumed = parser.substring(5);

            assertThat(consumed).isEqualTo("hello");
            assertThat(parser.toString()).isEqualTo(" world");
        }

        @Test
        @DisplayName("Should safely try substring extraction")
        void testSubstringTry() {
            StringParser parser = new StringParser("short");

            Optional<String> validResult = parser.substringTry(3);
            Optional<String> invalidResult = parser.substringTry(10);

            assertThat(validResult).isPresent().get().isEqualTo("sho");
            assertThat(invalidResult).isEmpty();
            assertThat(parser.toString()).isEqualTo("rt");
        }

        @Test
        @DisplayName("Should clear parser content")
        void testClear() {
            StringParser parser = new StringParser("clear me");

            String cleared = parser.clear();

            assertThat(cleared).isEqualTo("clear me");
            assertThat(parser.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Should trim whitespace")
        void testTrim() {
            StringParser parser = new StringParser("  whitespace  ");

            parser.trim();

            assertThat(parser.toString()).isEqualTo("whitespace");
        }

        @Test
        @DisplayName("Should check if empty after trimming")
        void testCheckEmpty() {
            StringParser emptyParser = new StringParser("   ");
            StringParser nonEmptyParser = new StringParser("not empty");

            assertThatCode(() -> emptyParser.checkEmpty()).doesNotThrowAnyException();
            assertThatThrownBy(() -> nonEmptyParser.checkEmpty())
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("StringParser not empty");
        }

        @Test
        @DisplayName("Should report isEmpty status correctly")
        void testIsEmpty() {
            assertThat(new StringParser("").isEmpty()).isTrue();
            assertThat(new StringParser("content").isEmpty()).isFalse();
            assertThat(new StringParser("   ").isEmpty()).isFalse(); // Whitespace is not empty
        }
    }

    @Nested
    @DisplayName("Trim Behavior Tests")
    class TrimBehaviorTests {

        @Test
        @DisplayName("Should auto-trim after modifications when enabled")
        void testAutoTrimEnabled() {
            StringSliceWithSplit slice = new StringSliceWithSplit(new StringSlice("  hello world  "));
            StringParser parser = new StringParser(slice, true); // Auto-trim enabled

            ParserWorker<String> worker = s -> {
                String result = s.substring(0, 2).toString();
                StringSlice remaining = s.substring(2);
                return new ParserResult<>(remaining, result);
            };

            parser.apply(worker);

            // Should be trimmed automatically after modification
            assertThat(parser.toString()).isEqualTo("hello world");
        }

        @Test
        @DisplayName("Should not auto-trim when disabled")
        void testAutoTrimDisabled() {
            StringSliceWithSplit slice = new StringSliceWithSplit(new StringSlice("  hello world  "));
            StringParser parser = new StringParser(slice, false); // Auto-trim disabled

            ParserWorker<String> worker = s -> {
                String result = s.substring(0, 2).toString();
                StringSlice remaining = s.substring(2);
                return new ParserResult<>(remaining, result);
            };

            parser.apply(worker);

            // Should preserve whitespace when auto-trim is disabled
            assertThat(parser.toString()).isEqualTo("hello world  ");
        }

        @Test
        @DisplayName("Should not trim when no modifications occur")
        void testNoTrimWithoutModification() {
            StringParser parser = new StringParser("  unchanged  ");

            ParserWorker<String> worker = s -> new ParserResult<>(s, "result");

            parser.apply(worker);

            assertThat(parser.toString()).isEqualTo("  unchanged  ");
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null-like operations gracefully")
        void testNullHandling() {
            StringParser parser = new StringParser("");

            assertThat(parser.substring(0)).isEmpty();
            assertThat(parser.substringTry(0)).hasValueSatisfying(s -> assertThat(s).isEmpty());
            assertThat(parser.clear()).isEmpty();
        }

        @Test
        @DisplayName("Should handle large string operations")
        void testLargeStrings() {
            StringBuilder largeString = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                largeString.append("chunk").append(i).append(" ");
            }

            StringParser parser = new StringParser(largeString.toString());

            String result = parser.substring(10);

            assertThat(result).hasSize(10);
            assertThat(parser.toString()).hasSize(largeString.length() - 10);
        }

        @Test
        @DisplayName("Should handle boundary conditions")
        void testBoundaryConditions() {
            StringParser parser = new StringParser("test");

            // Extract entire string
            String whole = parser.substring(4);
            assertThat(whole).isEqualTo("test");
            assertThat(parser.isEmpty()).isTrue();

            // Reset for next test
            parser = new StringParser("boundary");
            String partial = parser.substring(8); // Exact length
            assertThat(partial).isEqualTo("boundary");
            assertThat(parser.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Should maintain internal state consistency")
        void testInternalStateConsistency() {
            StringParser parser = new StringParser("state test");

            // Verify initial state
            assertThat(parser.getCurrentString()).isNotNull();
            assertThat(parser.toString()).isEqualTo("state test");

            // Modify and verify consistency
            parser.substring(5);
            assertThat(parser.getCurrentString().toString()).isEqualTo(parser.toString());

            parser.trim();
            assertThat(parser.getCurrentString().toString()).isEqualTo(parser.toString());
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should chain multiple operations correctly")
        void testOperationChaining() {
            StringParser parser = new StringParser("  first,second,third  ");

            // Trim, extract first, then second
            parser.trim();
            String first = parser.substring(parser.toString().indexOf(','));
            parser.substring(1); // Skip comma
            String second = parser.substring(parser.toString().indexOf(','));

            assertThat(first).isEqualTo("first");
            assertThat(second).isEqualTo("second");
            // The actual behavior includes the comma due to trim implementation
            assertThat(parser.toString().trim()).isEqualTo(",third");
        }

        @Test
        @DisplayName("Should work with complex parsing scenarios")
        void testComplexParsing() {
            StringParser parser = new StringParser("function(arg1, arg2) { body }");

            // Extract function name
            String funcName = parser.substring(parser.toString().indexOf('('));
            assertThat(funcName).isEqualTo("function");

            // Skip opening parenthesis
            parser.substring(1);

            // Extract arguments
            String args = parser.substring(parser.toString().indexOf(')'));
            assertThat(args).isEqualTo("arg1, arg2");

            // Verify remaining structure
            assertThat(parser.toString().trim()).isEqualTo(") { body }");
        }

        @Test
        @DisplayName("Should handle mixed worker applications")
        void testMixedWorkerApplications() {
            StringParser parser = new StringParser("key1=value1;key2=value2");

            // Use different worker types in sequence
            ParserWorkerWithParam<String, String> keyWorker = (slice, delim) -> {
                String str = slice.toString();
                int idx = str.indexOf(delim);
                if (idx == -1)
                    return new ParserResult<>(slice.clear(), str);

                String result = str.substring(0, idx);
                StringSlice remaining = slice.substring(idx + delim.length());
                return new ParserResult<>(remaining, result);
            };

            String key1 = parser.apply(keyWorker, "=");
            String value1 = parser.apply(keyWorker, ";");
            String key2 = parser.apply(keyWorker, "=");
            String value2 = parser.toString();

            assertThat(key1).isEqualTo("key1");
            assertThat(value1).isEqualTo("value1");
            assertThat(key2).isEqualTo("key2");
            assertThat(value2).isEqualTo("value2");
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should handle repeated operations efficiently")
        void testRepeatedOperations() {
            StringParser parser = new StringParser("a".repeat(1000));

            long startTime = System.nanoTime();

            for (int i = 0; i < 100; i++) {
                if (!parser.isEmpty()) {
                    parser.substring(1);
                }
            }

            long duration = System.nanoTime() - startTime;

            // Should complete within reasonable time (adjust threshold as needed)
            assertThat(duration).isLessThan(10_000_000L); // 10ms
            assertThat(parser.toString()).hasSize(900);
        }

        @Test
        @DisplayName("Should handle large worker applications efficiently")
        void testLargeWorkerApplications() {
            String largeInput = "word ".repeat(10000);
            StringParser parser = new StringParser(largeInput);

            ParserWorker<Integer> countWorker = slice -> {
                int count = slice.toString().split("\\s+").length;
                return new ParserResult<>(slice.clear(), count);
            };

            long startTime = System.nanoTime();
            Integer count = parser.apply(countWorker);
            long duration = System.nanoTime() - startTime;

            assertThat(count).isEqualTo(10000);
            assertThat(duration).isLessThan(50_000_000L); // 50ms
        }
    }
}
