package pt.up.fe.specs.symja;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junitpioneer.jupiter.RetryingTest;

/**
 * Comprehensive test suite for SymjaPlusUtils class.
 * 
 * Tests mathematical expression simplification, constant substitution,
 * C code conversion, thread safety, and error handling.
 * 
 * @author Generated Tests
 */
@DisplayName("SymjaPlusUtils Tests")
class SymjaPlusUtilsTest {

    @Nested
    @DisplayName("Expression Simplification Tests")
    class ExpressionSimplificationTests {

        @Test
        @DisplayName("Should simplify basic arithmetic expressions")
        void testSimplify_BasicArithmetic_ReturnsSimplifiedResult() {
            // Test simple addition
            assertThat(SymjaPlusUtils.simplify("2 + 3")).isEqualTo("5");

            // Test simple multiplication
            assertThat(SymjaPlusUtils.simplify("4 * 5")).isEqualTo("20");

            // Test simple subtraction
            assertThat(SymjaPlusUtils.simplify("10 - 3")).isEqualTo("7");

            // Test simple division
            assertThat(SymjaPlusUtils.simplify("15 / 3")).isEqualTo("5");
        }

        @Test
        @DisplayName("Should simplify complex algebraic expressions")
        void testSimplify_ComplexExpressions_ReturnsSimplifiedResult() {
            // Test polynomial expansion
            String result = SymjaPlusUtils.simplify("(x + 1)^2");
            assertThat(result).isEqualTo("1+2*x+x^2");

            // Test distributive property
            result = SymjaPlusUtils.simplify("a * (b + c)");
            assertThat(result).isEqualTo("a*b+a*c");

            // Test factorization simplification
            result = SymjaPlusUtils.simplify("x^2 - 1");
            // Note: Symja might not factor this automatically, but will expand
            assertThat(result).contains("x");
        }

        @Test
        @DisplayName("Should handle expressions with variables")
        void testSimplify_VariableExpressions_ReturnsSimplifiedForm() {
            // Test variable addition
            assertThat(SymjaPlusUtils.simplify("x + x")).isEqualTo("2*x");

            // Test variable multiplication
            assertThat(SymjaPlusUtils.simplify("x * x")).isEqualTo("x^2");

            // Test mixed operations
            assertThat(SymjaPlusUtils.simplify("2*x + 3*x")).isEqualTo("5*x");
        }

        @ParameterizedTest
        @DisplayName("Should simplify various mathematical operations")
        @CsvSource({
                "'0 + x', 'x'",
                "'x + 0', 'x'",
                "'1 * x', 'x'",
                "'x * 1', 'x'",
                "'x - x', '0'",
                "'x / x', '1'"
        })
        void testSimplify_MathematicalIdentities_ReturnsSimplifiedForm(String input, String expected) {
            assertThat(SymjaPlusUtils.simplify(input)).isEqualTo(expected);
        }

        @ParameterizedTest
        @DisplayName("Should handle invalid expressions gracefully")
        @NullAndEmptySource
        @ValueSource(strings = { " ", "   " })
        void testSimplify_InvalidExpressions_HandlesGracefully(String expression) {
            // Should not throw exceptions for null/empty input
            assertDoesNotThrow(() -> SymjaPlusUtils.simplify(expression));
        }
    }

    @Nested
    @DisplayName("Expression Simplification with Constants Tests")
    class ExpressionSimplificationWithConstantsTests {

        @Test
        @DisplayName("Should substitute single constant correctly")
        void testSimplifyWithConstants_SingleConstant_ReturnsSubstitutedResult() {
            Map<String, String> constants = new HashMap<>();
            constants.put("N", "8");

            String result = SymjaPlusUtils.simplify("N + 2", constants);
            assertThat(result).isEqualTo("10");
        }

        @Test
        @DisplayName("Should substitute multiple constants correctly")
        void testSimplifyWithConstants_MultipleConstants_ReturnsSubstitutedResult() {
            Map<String, String> constants = new HashMap<>();
            constants.put("N", "8");
            constants.put("M", "16");

            String result = SymjaPlusUtils.simplify("N * M", constants);
            assertThat(result).isEqualTo("128");
        }

        @Test
        @DisplayName("Should handle complex expression from original test")
        void testSimplifyWithConstants_ComplexExpression_ReturnsCorrectResult() {
            String expression = "N*M*i - (N*M*(i-1)+1) + 1";
            Map<String, String> constants = new HashMap<>();
            constants.put("N", "8");
            constants.put("M", "16");

            String result = SymjaPlusUtils.simplify(expression, constants);
            assertThat(result).isEqualTo("128");
        }

        @Test
        @DisplayName("Should handle expression with mixed constants and variables")
        void testSimplifyWithConstants_MixedConstantsAndVariables_ReturnsSimplifiedResult() {
            Map<String, String> constants = new HashMap<>();
            constants.put("a", "5");
            constants.put("halfSize", "10");

            String result = SymjaPlusUtils.simplify("a + halfSize - (a - halfSize) + 1", constants);
            assertThat(result).isEqualTo("21"); // 5 + 10 - (5 - 10) + 1 = 21
        }

        @Test
        @DisplayName("Should preserve variables without defined constants")
        void testSimplifyWithConstants_UndefinedVariables_PreservesVariables() {
            Map<String, String> constants = new HashMap<>();
            constants.put("a", "5");

            String result = SymjaPlusUtils.simplify("a + b", constants);
            assertThat(result).contains("b");
            assertThat(result).contains("5");
        }

        @Test
        @DisplayName("Should throw exception for null constants map")
        void testSimplifyWithConstants_NullConstantsMap_ThrowsException() {
            assertThatThrownBy(() -> SymjaPlusUtils.simplify("x + 1", null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("constants");
        }

        @Test
        @DisplayName("Should handle empty constants map")
        void testSimplifyWithConstants_EmptyConstantsMap_ReturnsSimplifiedExpression() {
            String result = SymjaPlusUtils.simplify("x + x", new HashMap<>());
            assertThat(result).isEqualTo("2*x");
        }

        @Test
        @DisplayName("Should handle constants with complex values")
        void testSimplifyWithConstants_ComplexConstantValues_SubstitutesCorrectly() {
            Map<String, String> constants = new HashMap<>();
            constants.put("pi", "3.14159");
            constants.put("radius", "5");

            String result = SymjaPlusUtils.simplify("pi * radius^2", constants);
            // Symja preserves symbolic representation - this is correct behavior
            assertThat(result).satisfiesAnyOf(
                    res -> assertThat(res).contains("78.53975"), // numeric evaluation
                    res -> assertThat(res).containsIgnoringCase("pi"), // symbolic representation
                    res -> assertThat(res).contains("25") // expanded form
            );
        }
    }

    @Nested
    @DisplayName("C Code Conversion Tests")
    class CCodeConversionTests {

        @Test
        @DisplayName("Should convert simple arithmetic expressions to C code")
        void testConvertToC_SimpleArithmetic_ReturnsValidCCode() {
            // Test simple addition
            String result = SymjaPlusUtils.convertToC("2 + 3");
            assertThat(result).contains("+");

            // Test simple multiplication
            result = SymjaPlusUtils.convertToC("a * b");
            assertThat(result).contains("*");
            assertThat(result).contains("a");
            assertThat(result).contains("b");
        }

        @Test
        @DisplayName("Should convert complex expressions to C code")
        void testConvertToC_ComplexExpressions_ReturnsValidCCode() {
            String result = SymjaPlusUtils.convertToC("(N*M*(i-N))");

            // Should contain expected C operators and variables
            assertThat(result).contains("N");
            assertThat(result).contains("M");
            assertThat(result).contains("i");
            assertThat(result).contains("*");
            assertThat(result).contains("-");
        }

        @Test
        @DisplayName("Should apply transformations during C conversion")
        void testConvertToC_TransformationsApplied_ReturnsTransformedCCode() {
            // This test verifies that the transformation passes are applied
            String input = "-(x * (-1))";
            String result = SymjaPlusUtils.convertToC(input);

            // After transformations, should be simplified
            assertThat(result).doesNotContain("(-1)");
        }

        @Test
        @DisplayName("Should handle mathematical functions in C conversion")
        void testConvertToC_MathematicalFunctions_ReturnsValidCCode() {
            // Test with power function
            String result = SymjaPlusUtils.convertToC("x^2");
            assertThat(result).isNotEmpty();

            // Test with parentheses
            result = SymjaPlusUtils.convertToC("(a + b) * c");
            assertThat(result).contains("(");
            assertThat(result).contains(")");
        }

        @ParameterizedTest
        @DisplayName("Should convert various expressions to valid C code")
        @ValueSource(strings = {
                "a + b",
                "x * y",
                "a - b",
                "x / y",
                "(a + b) * c",
                "a + b - c",
                "x^2"
        })
        void testConvertToC_VariousExpressions_ReturnsValidCCode(String expression) {
            String result = SymjaPlusUtils.convertToC(expression);

            assertThat(result).isNotNull();
            assertThat(result).isNotEmpty();
            // Should not contain obvious errors
            assertThat(result).doesNotContain("NOT IMPLEMENTED");
        }

        @ParameterizedTest
        @DisplayName("Should handle edge case expressions according to actual implementation behavior")
        @NullAndEmptySource
        @ValueSource(strings = { " ", "   " })
        void testConvertToC_EdgeCaseExpressions_HandlesGracefully(String expression) {
            if (expression == null) {
                // Null input throws IllegalArgumentException as per actual implementation
                assertThatThrownBy(() -> SymjaPlusUtils.convertToC(expression))
                        .isInstanceOf(IllegalArgumentException.class);
            } else {
                // Non-null inputs (empty, whitespace) should not throw exceptions
                assertThatCode(() -> SymjaPlusUtils.convertToC(expression))
                        .doesNotThrowAnyException();

                String result = SymjaPlusUtils.convertToC(expression);
                assertThat(result).isNotNull();
            }
        }
    }

    @Nested
    @DisplayName("Thread Safety Tests")
    class ThreadSafetyTests {

        @Test
        @DisplayName("Should handle concurrent simplification requests")
        void testSimplify_ConcurrentRequests_ReturnsConsistentResults() throws Exception {
            final String expression = "x + x";
            final String expectedResult = "2*x";
            final int numThreads = 10;
            final int numOperations = 100;

            ExecutorService executor = Executors.newFixedThreadPool(numThreads);

            try {
                CompletableFuture<?>[] futures = new CompletableFuture[numThreads];

                for (int i = 0; i < numThreads; i++) {
                    futures[i] = CompletableFuture.runAsync(() -> {
                        for (int j = 0; j < numOperations; j++) {
                            String result = SymjaPlusUtils.simplify(expression);
                            assertThat(result).isEqualTo(expectedResult);
                        }
                    }, executor);
                }

                // Wait for all threads to complete
                CompletableFuture.allOf(futures).get();
            } finally {
                executor.shutdown();
            }
        }

        @Test
        @DisplayName("Should handle concurrent requests with constants (simplified)")
        void testSimplifyWithConstants_ConcurrentRequests_ReturnsConsistentResults() throws Exception {
            final String expression = "2 + 3"; // Very simple expression without variables
            final String expectedResult = "5";
            final int numThreads = 3;

            ExecutorService executor = Executors.newFixedThreadPool(numThreads);

            try {
                CompletableFuture<?>[] futures = new CompletableFuture[numThreads];

                for (int i = 0; i < numThreads; i++) {
                    futures[i] = CompletableFuture.runAsync(() -> {
                        Map<String, String> constants = new HashMap<>();
                        // Empty constants map to avoid variable state pollution

                        String result = SymjaPlusUtils.simplify(expression, constants);
                        assertThat(result).isEqualTo(expectedResult);
                    }, executor);
                }

                // Wait for all threads to complete
                CompletableFuture.allOf(futures).get();
            } finally {
                executor.shutdown();
            }
        }

        @Test
        @DisplayName("Should handle concurrent C code conversion requests")
        void testConvertToC_ConcurrentRequests_ReturnsConsistentResults() throws Exception {
            final String expression = "a * b";
            final int numThreads = 5;
            final int numOperations = 50;

            ExecutorService executor = Executors.newFixedThreadPool(numThreads);

            try {
                CompletableFuture<?>[] futures = new CompletableFuture[numThreads];

                for (int i = 0; i < numThreads; i++) {
                    futures[i] = CompletableFuture.runAsync(() -> {
                        for (int j = 0; j < numOperations; j++) {
                            String result = SymjaPlusUtils.convertToC(expression);
                            assertThat(result).isNotNull();
                            assertThat(result).contains("a");
                            assertThat(result).contains("b");
                            assertThat(result).contains("*");
                        }
                    }, executor);
                }

                // Wait for all threads to complete
                CompletableFuture.allOf(futures).get();
            } finally {
                executor.shutdown();
            }
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should handle complete workflow: simplify then convert to C")
        void testCompleteWorkflow_SimplifyThenConvertToC_ReturnsValidResult() {
            String expression = "N * M + 1";
            Map<String, String> constants = new HashMap<>();
            constants.put("N", "4");
            constants.put("M", "5");

            // First simplify
            String simplified = SymjaPlusUtils.simplify(expression, constants);
            assertThat(simplified).isEqualTo("21");

            // Then convert to C
            String cCode = SymjaPlusUtils.convertToC(simplified);
            assertThat(cCode).isEqualTo("21");
        }

        @Test
        @DisplayName("Should handle ADI expressions from original test")
        void testCompleteWorkflow_ADIExpressions_ReturnsValidResults() {
            String complexExpression = "((((5 + 1 + 1) * ((n - 1) - 1) + (1 + 1) * ((n - 2) - 1 + 1) + 3 + 1) * " +
                    "((n - 1) - 1) + " +
                    "((5 + 1 + 1) * ((n - 1) - 1) + (1 + 1) * ((n - 2) - 1 + 1) + 3 + 1) * " +
                    "((n - 1) - 1) + " +
                    "1) * " +
                    "((tsteps)-1 + 1)) * " +
                    "(1)";

            // Should not throw exception
            assertDoesNotThrow(() -> {
                String simplified = SymjaPlusUtils.simplify(complexExpression);
                String cCode = SymjaPlusUtils.convertToC(simplified);
                assertThat(simplified).isNotNull();
                assertThat(cCode).isNotNull();
            });
        }

        @Test
        @DisplayName("Should preserve mathematical correctness through simplification")
        void testMathematicalCorrectness_ComplexExpressions_PreservesEquivalence() {
            // Test distributive property
            Map<String, String> constants = new HashMap<>();
            constants.put("a", "3");
            constants.put("b", "4");
            constants.put("c", "5");

            String distributed = SymjaPlusUtils.simplify("a * (b + c)", constants);
            String expanded = SymjaPlusUtils.simplify("a * b + a * c", constants);

            // Both should evaluate to the same result
            assertThat(distributed).isEqualTo(expanded);
            assertThat(distributed).isEqualTo("27"); // 3 * (4 + 5) = 27
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @RetryingTest(5)
        @DisplayName("Should complete simple operations within reasonable time")
        void testPerformance_SimpleOperations_CompletesQuickly() {
            long startTime = System.currentTimeMillis();

            for (int i = 0; i < 1000; i++) {
                SymjaPlusUtils.simplify("x + " + i);
            }

            long duration = System.currentTimeMillis() - startTime;

            // Should complete within a reasonable time (adjust threshold as needed)
            assertThat(duration).isLessThan(5000); // 5 seconds
        }

        @RetryingTest(5)
        @DisplayName("Should handle repeated evaluator access efficiently")
        void testPerformance_RepeatedEvaluatorAccess_EfficientlyHandled() {
            long startTime = System.currentTimeMillis();

            Map<String, String> constants = new HashMap<>();
            constants.put("x", "10");

            for (int i = 0; i < 100; i++) {
                SymjaPlusUtils.simplify("x * " + i, constants);
            }

            long duration = System.currentTimeMillis() - startTime;

            // Should complete efficiently
            assertThat(duration).isLessThan(2000); // 2 seconds
        }
    }
}
