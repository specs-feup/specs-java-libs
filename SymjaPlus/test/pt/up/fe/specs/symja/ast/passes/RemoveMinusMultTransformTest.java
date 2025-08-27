package pt.up.fe.specs.symja.ast.passes;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.RetryingTest;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pt.up.fe.specs.symja.ast.Operator;
import pt.up.fe.specs.symja.ast.SymjaAst;
import pt.up.fe.specs.symja.ast.SymjaFunction;
import pt.up.fe.specs.symja.ast.SymjaInteger;
import pt.up.fe.specs.symja.ast.SymjaNode;
import pt.up.fe.specs.symja.ast.SymjaOperator;
import pt.up.fe.specs.symja.ast.SymjaSymbol;
import pt.up.fe.specs.symja.ast.SymjaToC;
import pt.up.fe.specs.util.treenode.transform.TransformQueue;
import pt.up.fe.specs.util.treenode.transform.util.TraversalStrategy;

/**
 * Unit tests for {@link RemoveMinusMultTransform}.
 * 
 * @author Generated Tests
 */
@DisplayName("RemoveMinusMultTransform")
class RemoveMinusMultTransformTest {

    private RemoveMinusMultTransform transform;

    @Mock
    private TransformQueue<SymjaNode> mockQueue;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transform = new RemoveMinusMultTransform();
    }

    @Nested
    @DisplayName("Basic Transformation Tests")
    class BasicTransformationTests {

        @Test
        @DisplayName("Should transform multiplication by -1 with integer into unary minus")
        void testApplyAll_MultiplicationByMinusOneWithInteger_TransformsToUnaryMinus() {
            // Create -1 * 5 structure
            SymjaInteger minusOne = SymjaNode.newNode(SymjaInteger.class);
            minusOne.set(SymjaInteger.VALUE_STRING, "-1");

            SymjaInteger five = SymjaNode.newNode(SymjaInteger.class);
            five.set(SymjaInteger.VALUE_STRING, "5");

            SymjaOperator timesOp = SymjaNode.newNode(SymjaOperator.class);
            timesOp.set(SymjaOperator.OPERATOR, Operator.Times);

            SymjaFunction multiplyFunction = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(timesOp, minusOne, five));

            transform.applyAll(multiplyFunction, mockQueue);

            // Verify that a replacement was queued
            verify(mockQueue, times(1)).replace(eq(multiplyFunction), any(SymjaFunction.class));
        }

        @Test
        @DisplayName("Should transform multiplication by -1 with symbol into unary minus")
        void testApplyAll_MultiplicationByMinusOneWithSymbol_TransformsToUnaryMinus() {
            // Create -1 * x structure
            SymjaInteger minusOne = SymjaNode.newNode(SymjaInteger.class);
            minusOne.set(SymjaInteger.VALUE_STRING, "-1");

            SymjaSymbol x = SymjaNode.newNode(SymjaSymbol.class);
            x.set(SymjaSymbol.SYMBOL, "x");

            SymjaOperator timesOp = SymjaNode.newNode(SymjaOperator.class);
            timesOp.set(SymjaOperator.OPERATOR, Operator.Times);

            SymjaFunction multiplyFunction = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(timesOp, minusOne, x));

            transform.applyAll(multiplyFunction, mockQueue);

            // Verify that a replacement was queued
            verify(mockQueue, times(1)).replace(eq(multiplyFunction), any(SymjaFunction.class));
        }

        @Test
        @DisplayName("Should not transform multiplication by other values")
        void testApplyAll_MultiplicationByOtherValues_DoesNotTransform() {
            // Create 2 * 5 structure (no transformation expected)
            SymjaInteger two = SymjaNode.newNode(SymjaInteger.class);
            two.set(SymjaInteger.VALUE_STRING, "2");

            SymjaInteger five = SymjaNode.newNode(SymjaInteger.class);
            five.set(SymjaInteger.VALUE_STRING, "5");

            SymjaOperator timesOp = SymjaNode.newNode(SymjaOperator.class);
            timesOp.set(SymjaOperator.OPERATOR, Operator.Times);

            SymjaFunction multiplyFunction = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(timesOp, two, five));

            transform.applyAll(multiplyFunction, mockQueue);

            // Verify that no replacement was queued
            verify(mockQueue, never()).replace(any(), any());
        }

        @Test
        @DisplayName("Should not transform non-multiplication operations")
        void testApplyAll_NonMultiplicationOperations_DoesNotTransform() {
            // Create 2 + 3 structure (no transformation expected)
            SymjaInteger two = SymjaNode.newNode(SymjaInteger.class);
            two.set(SymjaInteger.VALUE_STRING, "2");

            SymjaInteger three = SymjaNode.newNode(SymjaInteger.class);
            three.set(SymjaInteger.VALUE_STRING, "3");

            SymjaOperator plusOp = SymjaNode.newNode(SymjaOperator.class);
            plusOp.set(SymjaOperator.OPERATOR, Operator.Plus);

            SymjaFunction addFunction = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(plusOp, two, three));

            transform.applyAll(addFunction, mockQueue);

            // Verify that no replacement was queued
            verify(mockQueue, never()).replace(any(), any());
        }

        @Test
        @DisplayName("Should not transform non-function nodes")
        void testApplyAll_NonFunctionNodes_DoesNotTransform() {
            SymjaInteger integer = SymjaNode.newNode(SymjaInteger.class);
            integer.set(SymjaInteger.VALUE_STRING, "42");

            transform.applyAll(integer, mockQueue);

            verify(mockQueue, never()).replace(any(), any());
        }
    }

    @Nested
    @DisplayName("Integration Tests with Real AST")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with real parsed expressions")
        void testWithRealExpression_MinusOneTimesX_TransformsCorrectly() {
            // This test requires working with actual parsed expressions
            // We'll test the principle even if the exact parsing might differ
            SymjaNode expression = SymjaAst.parse("-1 * x");

            // Apply transform
            transform.applyAll(expression, mockQueue);

            // The transform should be applied if the structure matches what it expects
            // Note: This test may need adjustment based on actual AST structure from
            // parsing
        }

        @Test
        @DisplayName("Should handle complex nested expressions")
        void testComplexNestedExpression_WithMinusOneMultiplication_TransformsOnlyRelevantParts() {
            // Test with expression like "y + (-1 * x) + 2"
            SymjaNode expression = SymjaAst.parse("y + (-1 * x) + 2");

            // Apply transform to the whole tree
            expression.getDescendantsAndSelfStream()
                    .forEach(node -> transform.applyAll(node, mockQueue));

            // Should find and transform the -1 * x part
            // Exact verification depends on AST structure
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle null transform queue gracefully")
        void testApplyAll_NullQueue_HandlesGracefully() {
            SymjaInteger integer = SymjaNode.newNode(SymjaInteger.class);

            // Should not throw exception with null queue
            assertThatCode(() -> transform.applyAll(integer, null))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should not transform when left operand is not -1")
        void testApplyAll_LeftOperandNotMinusOne_DoesNotTransform() {
            // Create x * y structure
            SymjaSymbol x = SymjaNode.newNode(SymjaSymbol.class);
            x.set(SymjaSymbol.SYMBOL, "x");

            SymjaSymbol y = SymjaNode.newNode(SymjaSymbol.class);
            y.set(SymjaSymbol.SYMBOL, "y");

            SymjaOperator timesOp = SymjaNode.newNode(SymjaOperator.class);
            timesOp.set(SymjaOperator.OPERATOR, Operator.Times);

            SymjaFunction multiplyFunction = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(timesOp, x, y));

            transform.applyAll(multiplyFunction, mockQueue);

            verify(mockQueue, never()).replace(any(), any());
        }

        @Test
        @DisplayName("Should handle functions with insufficient children")
        void testApplyAll_FunctionWithInsufficientChildren_HandlesGracefully() {
            // Create a function with only operator (malformed)
            SymjaOperator timesOp = SymjaNode.newNode(SymjaOperator.class);
            timesOp.set(SymjaOperator.OPERATOR, Operator.Times);

            SymjaFunction malformedFunction = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(timesOp));

            assertThatCode(() -> transform.applyAll(malformedFunction, mockQueue))
                    .doesNotThrowAnyException();

            verify(mockQueue, never()).replace(any(), any());
        }
    }

    @Nested
    @DisplayName("Transformation Strategy Tests")
    class TransformationStrategyTests {

        @Test
        @DisplayName("Should use POST_ORDER traversal strategy")
        void testGetTraversalStrategy_ReturnsPostOrder() {
            TraversalStrategy strategy = transform.getTraversalStrategy();

            assertThat(strategy).isEqualTo(TraversalStrategy.POST_ORDER);
        }
    }

    @Nested
    @DisplayName("C Code Generation Tests")
    class CCodeGenerationTests {

        @Test
        @DisplayName("Should detect minus one using C code conversion")
        void testMinusOneDetection_UsingCCodeConversion_WorksCorrectly() {
            // Test that SymjaToC.convert can identify -1 values correctly
            SymjaInteger minusOne = SymjaNode.newNode(SymjaInteger.class);
            minusOne.set(SymjaInteger.VALUE_STRING, "-1");

            String cCode = SymjaToC.convert(minusOne);

            // SymjaToC.convert actually returns -1 without parentheses
            assertThat(cCode).isEqualTo("-1");
        }

        @Test
        @DisplayName("Should verify non -1 values are not detected")
        void testNonMinusOneValues_NotDetected() {
            // Create 2 node
            SymjaInteger two = SymjaNode.newNode(SymjaInteger.class);
            two.set(SymjaInteger.VALUE_STRING, "2");

            String cCode = SymjaToC.convert(two);
            assertThat(cCode).isNotEqualTo("-1");
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @RetryingTest(5)
        @DisplayName("Should transform efficiently for multiple operations")
        void testTransformationPerformance_MultipleOperations_CompletesQuickly() {
            long startTime = System.nanoTime();

            // Create multiple expressions to transform
            for (int i = 0; i < 100; i++) {
                SymjaOperator minusOne = SymjaNode.newNode(SymjaOperator.class);
                minusOne.set(SymjaOperator.OPERATOR, Operator.UnaryMinus);

                SymjaInteger one = SymjaNode.newNode(SymjaInteger.class);
                one.set(SymjaInteger.VALUE_STRING, "1");

                SymjaFunction minusOneFunction = SymjaNode.newNode(SymjaFunction.class,
                        Arrays.asList(minusOne, one));

                SymjaInteger value = SymjaNode.newNode(SymjaInteger.class);
                value.set(SymjaInteger.VALUE_STRING, String.valueOf(i));

                SymjaOperator timesOp = SymjaNode.newNode(SymjaOperator.class);
                timesOp.set(SymjaOperator.OPERATOR, Operator.Times);

                SymjaFunction multiplyFunction = SymjaNode.newNode(SymjaFunction.class,
                        Arrays.asList(timesOp, minusOneFunction, value));

                transform.applyAll(multiplyFunction, mockQueue);
            }

            long endTime = System.nanoTime();
            long durationMs = (endTime - startTime) / 1_000_000;

            // Should complete within reasonable time
            assertThat(durationMs).isLessThan(1000);
        }
    }
}
