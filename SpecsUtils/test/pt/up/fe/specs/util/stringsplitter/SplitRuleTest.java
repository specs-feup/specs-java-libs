package pt.up.fe.specs.util.stringsplitter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for SplitRule interface.
 * Tests functional interface behavior and implementations.
 * 
 * @author Generated Tests
 */
@DisplayName("SplitRule Interface Tests")
class SplitRuleTest {

    @Nested
    @DisplayName("Interface Contract Tests")
    class InterfaceContractTests {

        @Test
        @DisplayName("Should extend Function interface")
        void testExtendsFunction() {
            SplitRule<String> rule = slice -> new SplitResult<>(slice, "test");

            assertThat(rule).isInstanceOf(Function.class);
            assertThat(rule).isInstanceOf(SplitRule.class);
        }

        @Test
        @DisplayName("Should be functional interface")
        void testFunctionalInterface() {
            // Test lambda creation
            SplitRule<String> lambdaRule = slice -> new SplitResult<>(slice, slice.toString());

            StringSliceWithSplit slice = new StringSliceWithSplit("test");
            SplitResult<String> result = lambdaRule.apply(slice);

            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEqualTo("test");
        }

        @Test
        @DisplayName("Should work with method references")
        void testMethodReference() {
            // Test method reference usage
            SplitRule<String> methodRefRule = this::simpleSplitRule;

            StringSliceWithSplit slice = new StringSliceWithSplit("hello world");
            SplitResult<String> result = methodRefRule.apply(slice);

            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEqualTo("processed");
        }

        private SplitResult<String> simpleSplitRule(StringSliceWithSplit slice) {
            return new SplitResult<>(slice, "processed");
        }
    }

    @Nested
    @DisplayName("Lambda Implementation Tests")
    class LambdaImplementationTests {

        @Test
        @DisplayName("Should work with simple lambda")
        void testSimpleLambda() {
            SplitRule<String> rule = slice -> {
                if (slice.isEmpty()) {
                    return null;
                }
                return new SplitResult<>(slice.substring(1), slice.charAt(0) + "");
            };

            StringSliceWithSplit slice = new StringSliceWithSplit("hello");
            SplitResult<String> result = rule.apply(slice);

            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEqualTo("h");
            assertThat(result.getModifiedSlice().toString()).isEqualTo("ello");
        }

        @Test
        @DisplayName("Should handle null return values")
        void testNullReturn() {
            SplitRule<String> alwaysNullRule = slice -> null;

            StringSliceWithSplit slice = new StringSliceWithSplit("test");
            SplitResult<String> result = alwaysNullRule.apply(slice);

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should handle exceptions in lambda")
        void testExceptionInLambda() {
            SplitRule<String> throwingRule = slice -> {
                throw new RuntimeException("Rule failed");
            };

            StringSliceWithSplit slice = new StringSliceWithSplit("test");

            assertThatThrownBy(() -> throwingRule.apply(slice))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Rule failed");
        }

        @Test
        @DisplayName("Should work with complex logic")
        void testComplexLambda() {
            SplitRule<Integer> numberExtractorRule = slice -> {
                if (slice.isEmpty()) {
                    return null;
                }

                int i = 0;
                while (i < slice.length() && Character.isDigit(slice.charAt(i))) {
                    i++;
                }

                if (i == 0) {
                    return null; // No digits found
                }

                String numberStr = slice.substring(0, i).toString();
                try {
                    Integer number = Integer.parseInt(numberStr);
                    StringSliceWithSplit remaining = slice.substring(i);
                    return new SplitResult<>(remaining, number);
                } catch (NumberFormatException e) {
                    return null;
                }
            };

            StringSliceWithSplit slice = new StringSliceWithSplit("123abc");
            SplitResult<Integer> result = numberExtractorRule.apply(slice);

            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEqualTo(123);
            assertThat(result.getModifiedSlice().toString()).isEqualTo("abc");
        }
    }

    @Nested
    @DisplayName("Anonymous Class Implementation Tests")
    class AnonymousClassImplementationTests {

        @Test
        @DisplayName("Should work with anonymous class")
        void testAnonymousClass() {
            SplitRule<String> anonymousRule = new SplitRule<String>() {
                @Override
                public SplitResult<String> apply(StringSliceWithSplit slice) {
                    if (slice.isEmpty()) {
                        return null;
                    }
                    return new SplitResult<>(slice.clear(), slice.toString().toUpperCase());
                }
            };

            StringSliceWithSplit slice = new StringSliceWithSplit("hello");
            SplitResult<String> result = anonymousRule.apply(slice);

            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEqualTo("HELLO");
            assertThat(result.getModifiedSlice().toString()).isEmpty();
        }

        @Test
        @DisplayName("Should allow state in anonymous class")
        void testAnonymousClassWithState() {
            SplitRule<String> statefulRule = new SplitRule<String>() {
                private int callCount = 0;

                @Override
                public SplitResult<String> apply(StringSliceWithSplit slice) {
                    callCount++;
                    return new SplitResult<>(slice, "call_" + callCount);
                }
            };

            StringSliceWithSplit slice = new StringSliceWithSplit("test");

            SplitResult<String> result1 = statefulRule.apply(slice);
            assertThat(result1.getValue()).isEqualTo("call_1");

            SplitResult<String> result2 = statefulRule.apply(slice);
            assertThat(result2.getValue()).isEqualTo("call_2");
        }
    }

    @Nested
    @DisplayName("Generic Type Tests")
    class GenericTypeTests {

        @Test
        @DisplayName("Should work with different return types")
        void testDifferentReturnTypes() {
            // String rule
            SplitRule<String> stringRule = slice -> new SplitResult<>(slice, "string");

            // Integer rule
            SplitRule<Integer> intRule = slice -> new SplitResult<>(slice, 42);

            // Boolean rule
            SplitRule<Boolean> boolRule = slice -> new SplitResult<>(slice, true);

            StringSliceWithSplit slice = new StringSliceWithSplit("test");

            assertThat(stringRule.apply(slice).getValue()).isEqualTo("string");
            assertThat(intRule.apply(slice).getValue()).isEqualTo(42);
            assertThat(boolRule.apply(slice).getValue()).isEqualTo(true);
        }

        @Test
        @DisplayName("Should work with complex generic types")
        void testComplexGenericTypes() {
            SplitRule<java.util.List<String>> listRule = slice -> {
                java.util.List<String> list = java.util.Arrays.asList(slice.toString().split("\\s+"));
                return new SplitResult<>(slice.clear(), list);
            };

            StringSliceWithSplit slice = new StringSliceWithSplit("hello world test");
            SplitResult<java.util.List<String>> result = listRule.apply(slice);

            assertThat(result).isNotNull();
            assertThat(result.getValue()).containsExactly("hello", "world", "test");
        }

        @Test
        @DisplayName("Should handle wildcards and bounds")
        void testWildcardsAndBounds() {
            SplitRule<? extends Number> numberRule = slice -> {
                try {
                    return new SplitResult<>(slice, Integer.parseInt(slice.toString()));
                } catch (NumberFormatException e) {
                    return null;
                }
            };

            StringSliceWithSplit slice = new StringSliceWithSplit("123");
            SplitResult<? extends Number> result = numberRule.apply(slice);

            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEqualTo(123);
        }
    }

    @Nested
    @DisplayName("Composition and Chaining Tests")
    class CompositionAndChainingTests {

        @Test
        @DisplayName("Should compose with other functions")
        void testComposition() {
            SplitRule<String> baseRule = slice -> new SplitResult<>(slice, slice.toString());
            Function<String, String> upperCaseFunction = String::toUpperCase;

            // Compose the rule with another function
            Function<StringSliceWithSplit, String> composedFunction = baseRule.andThen(result -> {
                if (result == null)
                    return null;
                return upperCaseFunction.apply(result.getValue());
            });

            StringSliceWithSplit slice = new StringSliceWithSplit("hello");
            String result = composedFunction.apply(slice);

            assertThat(result).isEqualTo("HELLO");
        }

        @Test
        @DisplayName("Should chain multiple rules")
        void testRuleChaining() {
            SplitRule<String> firstRule = slice -> {
                if (slice.isEmpty())
                    return null;
                return new SplitResult<>(slice.substring(1), slice.charAt(0) + "");
            };

            SplitRule<String> secondRule = slice -> {
                if (slice.isEmpty())
                    return null;
                return new SplitResult<>(slice.substring(1), slice.charAt(0) + "");
            };

            StringSliceWithSplit slice = new StringSliceWithSplit("hello");

            SplitResult<String> first = firstRule.apply(slice);
            assertThat(first.getValue()).isEqualTo("h");

            SplitResult<String> second = secondRule.apply(first.getModifiedSlice());
            assertThat(second.getValue()).isEqualTo("e");
            assertThat(second.getModifiedSlice().toString()).isEqualTo("llo");
        }

        @Test
        @DisplayName("Should handle conditional rule application")
        void testConditionalRules() {
            SplitRule<String> conditionalRule = slice -> {
                if (slice.toString().startsWith("valid")) {
                    return new SplitResult<>(slice.substring(5), "matched");
                }
                return null;
            };

            StringSliceWithSplit validSlice = new StringSliceWithSplit("validtest");
            StringSliceWithSplit invalidSlice = new StringSliceWithSplit("invalid");

            SplitResult<String> validResult = conditionalRule.apply(validSlice);
            assertThat(validResult).isNotNull();
            assertThat(validResult.getValue()).isEqualTo("matched");

            SplitResult<String> invalidResult = conditionalRule.apply(invalidSlice);
            assertThat(invalidResult).isNull();
        }
    }

    @Nested
    @DisplayName("Real-world Usage Patterns")
    class RealWorldUsagePatterns {

        @Test
        @DisplayName("Should implement word boundary rule")
        void testWordBoundaryRule() {
            SplitRule<String> wordRule = slice -> {
                if (slice.isEmpty())
                    return null;

                int i = 0;
                while (i < slice.length() && Character.isLetter(slice.charAt(i))) {
                    i++;
                }

                if (i == 0)
                    return null;

                String word = slice.substring(0, i).toString();
                StringSliceWithSplit remaining = slice.substring(i);
                return new SplitResult<>(remaining, word);
            };

            StringSliceWithSplit slice = new StringSliceWithSplit("hello123world");
            SplitResult<String> result = wordRule.apply(slice);

            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEqualTo("hello");
            assertThat(result.getModifiedSlice().toString()).isEqualTo("123world");
        }

        @Test
        @DisplayName("Should implement quoted string rule")
        void testQuotedStringRule() {
            SplitRule<String> quotedRule = slice -> {
                if (slice.isEmpty() || slice.charAt(0) != '"') {
                    return null;
                }

                int i = 1;
                while (i < slice.length() && slice.charAt(i) != '"') {
                    i++;
                }

                if (i >= slice.length()) {
                    return null; // No closing quote
                }

                String quoted = slice.substring(1, i).toString();
                StringSliceWithSplit remaining = slice.substring(i + 1);
                return new SplitResult<>(remaining, quoted);
            };

            StringSliceWithSplit slice = new StringSliceWithSplit("\"hello world\" remaining");
            SplitResult<String> result = quotedRule.apply(slice);

            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEqualTo("hello world");
            assertThat(result.getModifiedSlice().toString()).isEqualTo(" remaining");
        }

        @ParameterizedTest
        @ValueSource(strings = { "123", "456", "789", "0" })
        @DisplayName("Should implement number parsing rule")
        void testNumberParsingRule(String number) {
            SplitRule<Integer> numberRule = slice -> {
                if (slice.isEmpty())
                    return null;

                int i = 0;
                if (slice.charAt(0) == '-' || slice.charAt(0) == '+') {
                    i = 1;
                }

                while (i < slice.length() && Character.isDigit(slice.charAt(i))) {
                    i++;
                }

                if (i == 0 || (i == 1 && (slice.charAt(0) == '-' || slice.charAt(0) == '+'))) {
                    return null;
                }

                try {
                    String numStr = slice.substring(0, i).toString();
                    Integer parsed = Integer.parseInt(numStr);
                    StringSliceWithSplit remaining = slice.substring(i);
                    return new SplitResult<>(remaining, parsed);
                } catch (NumberFormatException e) {
                    return null;
                }
            };

            StringSliceWithSplit slice = new StringSliceWithSplit(number + " text");
            SplitResult<Integer> result = numberRule.apply(slice);

            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEqualTo(Integer.parseInt(number));
            assertThat(result.getModifiedSlice().toString()).isEqualTo(" text");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandling {

        @Test
        @DisplayName("Should handle empty input")
        void testEmptyInput() {
            SplitRule<String> rule = slice -> {
                if (slice.isEmpty()) {
                    return new SplitResult<>(slice, "empty");
                }
                return new SplitResult<>(slice, "not_empty");
            };

            StringSliceWithSplit emptySlice = new StringSliceWithSplit("");
            SplitResult<String> result = rule.apply(emptySlice);

            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEqualTo("empty");
        }

        @Test
        @DisplayName("Should handle null slice gracefully")
        void testNullSlice() {
            SplitRule<String> rule = slice -> {
                if (slice == null) {
                    return null;
                }
                return new SplitResult<>(slice, "non_null");
            };

            SplitResult<String> result = rule.apply(null);
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should handle recursive rule application")
        void testRecursiveRule() {
            SplitRule<Integer> recursiveRule = new SplitRule<Integer>() {
                @Override
                public SplitResult<Integer> apply(StringSliceWithSplit slice) {
                    if (slice.isEmpty()) {
                        return new SplitResult<>(slice, 0);
                    }

                    if (slice.length() == 1) {
                        return new SplitResult<>(slice.clear(), 1);
                    }

                    // Recursive call (should be careful with this pattern)
                    SplitResult<Integer> subResult = apply(slice.substring(1));
                    return new SplitResult<>(subResult.getModifiedSlice(), subResult.getValue() + 1);
                }
            };

            StringSliceWithSplit slice = new StringSliceWithSplit("hello");
            SplitResult<Integer> result = recursiveRule.apply(slice);

            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEqualTo(5); // Length of "hello"
            assertThat(result.getModifiedSlice().toString()).isEmpty();
        }
    }
}
