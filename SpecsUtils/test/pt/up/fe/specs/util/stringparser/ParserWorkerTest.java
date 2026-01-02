package pt.up.fe.specs.util.stringparser;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.RetryingTest;

import pt.up.fe.specs.util.utilities.StringSlice;

/**
 * Comprehensive test suite for {@link ParserWorker}.
 * Tests the functional interface for parsing operations, including basic
 * parsing, lambda implementations, method references, and composition
 * scenarios.
 * 
 * @author Generated Tests
 */
@DisplayName("ParserWorker Tests")
public class ParserWorkerTest {

    @Nested
    @DisplayName("Basic Implementation Tests")
    class BasicImplementationTests {

        @Test
        @DisplayName("Should implement simple string extraction worker")
        void testSimpleStringExtraction() {
            ParserWorker<String> worker = slice -> {
                String content = slice.toString();
                int spaceIndex = content.indexOf(' ');

                if (spaceIndex == -1) {
                    return new ParserResult<>(slice.clear(), content);
                }

                String result = content.substring(0, spaceIndex);
                StringSlice remaining = slice.substring(spaceIndex + 1);
                return new ParserResult<>(remaining, result);
            };

            StringSlice input = new StringSlice("hello world test");
            ParserResult<String> result = worker.apply(input);

            assertThat(result.result()).isEqualTo("hello");
            assertThat(result.modifiedString().toString()).isEqualTo("world test");
        }

        @Test
        @DisplayName("Should implement number parsing worker")
        void testNumberParsingWorker() {
            ParserWorker<Integer> worker = slice -> {
                String content = slice.toString().trim();
                StringBuilder numberStr = new StringBuilder();
                int i = 0;

                // Handle negative numbers
                if (content.startsWith("-")) {
                    numberStr.append("-");
                    i = 1;
                }

                // Extract digits
                while (i < content.length() && Character.isDigit(content.charAt(i))) {
                    numberStr.append(content.charAt(i));
                    i++;
                }

                if (numberStr.length() == 0 || (numberStr.length() == 1 && numberStr.charAt(0) == '-')) {
                    throw new NumberFormatException("No valid number found");
                }

                Integer result = Integer.parseInt(numberStr.toString());
                StringSlice remaining = slice.substring(i);
                return new ParserResult<>(remaining, result);
            };

            StringSlice input = new StringSlice("123abc");
            ParserResult<Integer> result = worker.apply(input);

            assertThat(result.result()).isEqualTo(123);
            assertThat(result.modifiedString().toString()).isEqualTo("abc");
        }

        @Test
        @DisplayName("Should implement quoted string parsing worker")
        void testQuotedStringParsingWorker() {
            ParserWorker<String> worker = slice -> {
                String content = slice.toString();

                if (!content.startsWith("\"")) {
                    throw new IllegalArgumentException("Expected quoted string");
                }

                StringBuilder result = new StringBuilder();
                int i = 1; // Skip opening quote
                boolean escaped = false;

                while (i < content.length()) {
                    char c = content.charAt(i);

                    if (escaped) {
                        switch (c) {
                            case 'n' -> result.append('\n');
                            case 't' -> result.append('\t');
                            case 'r' -> result.append('\r');
                            case '\\' -> result.append('\\');
                            case '"' -> result.append('"');
                            default -> {
                                result.append('\\');
                                result.append(c);
                            }
                        }
                        escaped = false;
                    } else if (c == '\\') {
                        escaped = true;
                    } else if (c == '"') {
                        // End of string
                        StringSlice remaining = slice.substring(i + 1);
                        return new ParserResult<>(remaining, result.toString());
                    } else {
                        result.append(c);
                    }
                    i++;
                }

                throw new IllegalArgumentException("Unterminated quoted string");
            };

            StringSlice input = new StringSlice("\"hello\\nworld\" remaining");
            ParserResult<String> result = worker.apply(input);

            assertThat(result.result()).isEqualTo("hello\nworld");
            assertThat(result.modifiedString().toString()).isEqualTo(" remaining");
        }

        @Test
        @DisplayName("Should implement list parsing worker")
        void testListParsingWorker() {
            ParserWorker<List<String>> worker = slice -> {
                String content = slice.toString();

                if (!content.startsWith("[") || !content.contains("]")) {
                    throw new IllegalArgumentException("Expected list format [item1,item2,...]");
                }

                int endIndex = content.indexOf(']');
                String listContent = content.substring(1, endIndex);

                List<String> result = listContent.isEmpty()
                        ? Arrays.asList()
                        : Arrays.asList(listContent.split(","));

                StringSlice remaining = slice.substring(endIndex + 1);
                return new ParserResult<>(remaining, result);
            };

            StringSlice input = new StringSlice("[apple,banana,cherry] after");
            ParserResult<List<String>> result = worker.apply(input);

            assertThat(result.result()).containsExactly("apple", "banana", "cherry");
            assertThat(result.modifiedString().toString()).isEqualTo(" after");
        }
    }

    @Nested
    @DisplayName("Lambda Expression Tests")
    class LambdaExpressionTests {

        @Test
        @DisplayName("Should work with simple lambda expressions")
        void testSimpleLambda() {
            ParserWorker<String> worker = slice -> new ParserResult<>(slice.clear(), slice.toString().toUpperCase());

            StringSlice input = new StringSlice("lowercase");
            ParserResult<String> result = worker.apply(input);

            assertThat(result.result()).isEqualTo("LOWERCASE");
            assertThat(result.modifiedString().isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Should work with multi-line lambda expressions")
        void testMultiLineLambda() {
            ParserWorker<Integer> worker = slice -> {
                String content = slice.toString();
                int vowelCount = 0;

                for (char c : content.toLowerCase().toCharArray()) {
                    if ("aeiou".indexOf(c) != -1) {
                        vowelCount++;
                    }
                }

                return new ParserResult<>(slice.clear(), vowelCount);
            };

            StringSlice input = new StringSlice("Hello World");
            ParserResult<Integer> result = worker.apply(input);

            assertThat(result.result()).isEqualTo(3); // e, o, o
        }

        @Test
        @DisplayName("Should work with conditional lambda logic")
        void testConditionalLambda() {
            ParserWorker<String> worker = slice -> {
                String content = slice.toString();
                String result = content.length() > 5 ? "long" : "short";
                StringSlice remaining = slice.clear();
                return new ParserResult<>(remaining, result);
            };

            ParserResult<String> shortResult = worker.apply(new StringSlice("hi"));
            ParserResult<String> longResult = worker.apply(new StringSlice("hello world"));

            assertThat(shortResult.result()).isEqualTo("short");
            assertThat(longResult.result()).isEqualTo("long");
        }
    }

    @Nested
    @DisplayName("Method Reference Tests")
    class MethodReferenceTests {

        // Helper methods for method references
        private ParserResult<String> extractFirstWord(StringSlice slice) {
            String content = slice.toString();
            String[] words = content.split("\\s+", 2);
            String result = words[0];
            StringSlice remaining = words.length > 1
                    ? new StringSlice(words[1])
                    : slice.clear();
            return new ParserResult<>(remaining, result);
        }

        private ParserResult<Integer> countCharacters(StringSlice slice) {
            int count = slice.toString().length();
            return new ParserResult<>(slice.clear(), count);
        }

        @Test
        @DisplayName("Should work with instance method references")
        void testInstanceMethodReference() {
            ParserWorker<String> worker = this::extractFirstWord;

            StringSlice input = new StringSlice("first second third");
            ParserResult<String> result = worker.apply(input);

            assertThat(result.result()).isEqualTo("first");
            assertThat(result.modifiedString().toString()).isEqualTo("second third");
        }

        @Test
        @DisplayName("Should work with method references for different return types")
        void testDifferentReturnTypeMethodReference() {
            ParserWorker<Integer> worker = this::countCharacters;

            StringSlice input = new StringSlice("count me");
            ParserResult<Integer> result = worker.apply(input);

            assertThat(result.result()).isEqualTo(8);
            assertThat(result.modifiedString().isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Should work with static method references")
        void testStaticMethodReference() {
            ParserWorker<String> worker = MethodReferenceTests::staticParseMethod;

            StringSlice input = new StringSlice("static test");
            ParserResult<String> result = worker.apply(input);

            assertThat(result.result()).isEqualTo("STATIC");
            assertThat(result.modifiedString().toString()).isEqualTo(" test");
        }

        // Static helper method for testing
        public static ParserResult<String> staticParseMethod(StringSlice slice) {
            String content = slice.toString();
            int spaceIndex = content.indexOf(' ');

            if (spaceIndex == -1) {
                return new ParserResult<>(slice.clear(), content.toUpperCase());
            }

            String result = content.substring(0, spaceIndex).toUpperCase();
            StringSlice remaining = slice.substring(spaceIndex);
            return new ParserResult<>(remaining, result);
        }
    }

    @Nested
    @DisplayName("Function Interface Tests")
    class FunctionInterfaceTests {

        @Test
        @DisplayName("Should extend Function interface correctly")
        void testFunctionInterface() {
            ParserWorker<String> worker = slice -> new ParserResult<>(slice.clear(), "processed");

            // Should be usable as a Function
            Function<StringSlice, ParserResult<String>> function = worker;

            StringSlice input = new StringSlice("test");
            ParserResult<String> result = function.apply(input);

            assertThat(result.result()).isEqualTo("processed");
        }

        @Test
        @DisplayName("Should support Function composition")
        void testFunctionComposition() {
            ParserWorker<String> baseWorker = slice -> new ParserResult<>(slice.clear(), slice.toString().trim());

            Function<ParserResult<String>, String> extractor = result -> result.result().toUpperCase();

            Function<StringSlice, String> composed = baseWorker.andThen(extractor);

            String result = composed.apply(new StringSlice("  hello  "));

            assertThat(result).isEqualTo("HELLO");
        }

        @Test
        @DisplayName("Should work with Function utility methods")
        void testFunctionUtilities() {
            ParserWorker<String> worker = slice -> new ParserResult<>(slice.clear(), slice.toString().toLowerCase());

            // Test with chained function
            ParserWorker<String> chained = slice -> worker.apply(slice);

            StringSlice input = new StringSlice("TEST");
            ParserResult<String> result = chained.apply(input);

            assertThat(result.result()).isEqualTo("test");
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle parsing errors gracefully")
        void testParsingErrors() {
            ParserWorker<Integer> worker = slice -> {
                try {
                    String content = slice.toString();
                    Integer result = Integer.parseInt(content);
                    return new ParserResult<>(slice.clear(), result);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid number format: " + slice, e);
                }
            };

            assertThatThrownBy(() -> worker.apply(new StringSlice("not_a_number")))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Invalid number format");
        }

        @Test
        @DisplayName("Should handle empty input gracefully")
        void testEmptyInput() {
            ParserWorker<String> worker = slice -> {
                if (slice.isEmpty()) {
                    return new ParserResult<>(slice, "empty");
                }
                return new ParserResult<>(slice.clear(), slice.toString());
            };

            ParserResult<String> result = worker.apply(new StringSlice(""));

            assertThat(result.result()).isEqualTo("empty");
            assertThat(result.modifiedString().isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Should handle null results appropriately")
        void testNullResults() {
            ParserWorker<String> worker = slice -> new ParserResult<>(slice.clear(), null);

            ParserResult<String> result = worker.apply(new StringSlice("input"));

            assertThat(result.result()).isNull();
            assertThat(result.modifiedString().isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Should handle runtime exceptions during parsing")
        void testRuntimeExceptions() {
            ParserWorker<String> worker = slice -> {
                if (slice.toString().contains("error")) {
                    throw new RuntimeException("Intentional parsing error");
                }
                return new ParserResult<>(slice.clear(), "success");
            };

            assertThatThrownBy(() -> worker.apply(new StringSlice("trigger error")))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Intentional parsing error");

            // Should work normally with valid input
            ParserResult<String> result = worker.apply(new StringSlice("valid input"));
            assertThat(result.result()).isEqualTo("success");
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @RetryingTest(5)
        @DisplayName("Should handle large input efficiently")
        void testLargeInputPerformance() {
            String largeInput = "word ".repeat(10000);

            ParserWorker<Integer> worker = slice -> {
                String content = slice.toString();
                int wordCount = content.split("\\s+").length;
                return new ParserResult<>(slice.clear(), wordCount);
            };

            long startTime = System.nanoTime();
            ParserResult<Integer> result = worker.apply(new StringSlice(largeInput));
            long duration = System.nanoTime() - startTime;

            assertThat(result.result()).isEqualTo(10000);
            assertThat(duration).isLessThan(50_000_000L); // 50ms
        }

        @RetryingTest(5)
        @DisplayName("Should handle repeated applications efficiently")
        void testRepeatedApplications() {
            ParserWorker<String> worker = slice -> {
                String content = slice.toString();
                if (content.length() > 0) {
                    String result = String.valueOf(content.charAt(0));
                    StringSlice remaining = slice.substring(1);
                    return new ParserResult<>(remaining, result);
                }
                return new ParserResult<>(slice, "");
            };

            StringSlice input = new StringSlice("a".repeat(1000));

            long startTime = System.nanoTime();

            for (int i = 0; i < 100 && !input.isEmpty(); i++) {
                ParserResult<String> result = worker.apply(input);
                input = result.modifiedString();
            }

            long duration = System.nanoTime() - startTime;

            assertThat(duration).isLessThan(10_000_000L); // 10ms
            assertThat(input.toString()).hasSize(900);
        }
    }

    @Nested
    @DisplayName("Complex Parsing Tests")
    class ComplexParsingTests {

        @Test
        @DisplayName("Should parse complex data structures")
        void testComplexStructureParsing() {
            record Person(String name, int age) {
            }

            ParserWorker<Person> worker = slice -> {
                String content = slice.toString();

                // Expected format: "Name:John,Age:30"
                if (!content.startsWith("Name:")) {
                    throw new IllegalArgumentException("Expected Name: prefix");
                }

                int nameStart = 5; // After "Name:"
                int ageStart = content.indexOf(",Age:");

                if (ageStart == -1) {
                    throw new IllegalArgumentException("Expected ,Age: separator");
                }

                String name = content.substring(nameStart, ageStart);
                String ageStr = content.substring(ageStart + 5); // After ",Age:"

                // Find end of age (next space or end of string)
                int ageEnd = ageStr.indexOf(' ');
                if (ageEnd != -1) {
                    ageStr = ageStr.substring(0, ageEnd);
                }

                int age = Integer.parseInt(ageStr);
                Person result = new Person(name, age);

                StringSlice remaining = ageEnd == -1
                        ? slice.clear()
                        : slice.substring(nameStart + (ageStart - nameStart) + 5 + ageEnd);

                return new ParserResult<>(remaining, result);
            };

            StringSlice input = new StringSlice("Name:Alice,Age:25 remaining");
            ParserResult<Person> result = worker.apply(input);

            assertThat(result.result().name()).isEqualTo("Alice");
            assertThat(result.result().age()).isEqualTo(25);
            assertThat(result.modifiedString().toString()).isEqualTo(" remaining");
        }

        @Test
        @DisplayName("Should handle nested parsing scenarios")
        void testNestedParsing() {
            ParserWorker<List<Integer>> worker = slice -> {
                String content = slice.toString();

                // Parse comma-separated integers: "1,2,3,4"
                String[] parts = content.split(",");
                List<Integer> numbers = Arrays.stream(parts)
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(Integer::parseInt)
                        .toList();

                return new ParserResult<>(slice.clear(), numbers);
            };

            StringSlice input = new StringSlice("10,20,30,40");
            ParserResult<List<Integer>> result = worker.apply(input);

            assertThat(result.result()).containsExactly(10, 20, 30, 40);
        }

        @Test
        @DisplayName("Should support conditional parsing logic")
        void testConditionalParsing() {
            ParserWorker<Object> worker = slice -> {
                String content = slice.toString().trim();

                if (content.startsWith("\"") && content.endsWith("\"")) {
                    // Parse as string
                    String result = content.substring(1, content.length() - 1);
                    return new ParserResult<>(slice.clear(), result);
                } else if (content.matches("\\d+")) {
                    // Parse as integer
                    Integer result = Integer.parseInt(content);
                    return new ParserResult<>(slice.clear(), result);
                } else if (content.matches("true|false")) {
                    // Parse as boolean
                    Boolean result = Boolean.parseBoolean(content);
                    return new ParserResult<>(slice.clear(), result);
                } else {
                    throw new IllegalArgumentException("Unknown format: " + content);
                }
            };

            // Test string parsing
            ParserResult<Object> stringResult = worker.apply(new StringSlice("\"hello\""));
            assertThat(stringResult.result()).isEqualTo("hello");

            // Test integer parsing
            ParserResult<Object> intResult = worker.apply(new StringSlice("42"));
            assertThat(intResult.result()).isEqualTo(42);

            // Test boolean parsing
            ParserResult<Object> boolResult = worker.apply(new StringSlice("true"));
            assertThat(boolResult.result()).isEqualTo(true);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with StringParser integration")
        void testStringParserIntegration() {
            ParserWorker<String> worker = slice -> {
                String content = slice.toString();
                int colonIndex = content.indexOf(':');

                if (colonIndex == -1) {
                    return new ParserResult<>(slice.clear(), content);
                }

                String result = content.substring(0, colonIndex);
                StringSlice remaining = slice.substring(colonIndex + 1);
                return new ParserResult<>(remaining, result);
            };

            StringParser parser = new StringParser("key:value");
            String result = parser.apply(worker);

            assertThat(result).isEqualTo("key");
            assertThat(parser.toString()).isEqualTo("value");
        }

        @Test
        @DisplayName("Should chain with other parser workers")
        void testWorkerChaining() {
            ParserWorker<String> firstWorker = slice -> {
                String content = slice.toString();
                int spaceIndex = content.indexOf(' ');

                if (spaceIndex == -1) {
                    return new ParserResult<>(slice.clear(), content);
                }

                String result = content.substring(0, spaceIndex);
                StringSlice remaining = slice.substring(spaceIndex + 1);
                return new ParserResult<>(remaining, result);
            };

            ParserWorker<String> secondWorker = slice -> {
                String content = slice.toString();
                return new ParserResult<>(slice.clear(), content.toUpperCase());
            };

            StringSlice input = new StringSlice("hello world");

            // Chain workers manually
            ParserResult<String> firstResult = firstWorker.apply(input);
            ParserResult<String> secondResult = secondWorker.apply(firstResult.modifiedString());

            assertThat(firstResult.result()).isEqualTo("hello");
            assertThat(secondResult.result()).isEqualTo("WORLD");
        }
    }
}
