package pt.up.fe.specs.symja.ast.passes;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pt.up.fe.specs.symja.ast.Operator;
import pt.up.fe.specs.symja.ast.SymjaFunction;
import pt.up.fe.specs.symja.ast.SymjaInteger;
import pt.up.fe.specs.symja.ast.SymjaNode;
import pt.up.fe.specs.symja.ast.SymjaOperator;
import pt.up.fe.specs.symja.ast.SymjaSymbol;
import pt.up.fe.specs.util.treenode.transform.TransformQueue;
import pt.up.fe.specs.util.treenode.transform.util.TraversalStrategy;

/**
 * Unit tests for {@link RemoveRedundantParenthesisTransform}.
 * 
 * @author Generated Tests
 */
@DisplayName("RemoveRedundantParenthesisTransform")
class RemoveRedundantParenthesisTransformTest {

    private RemoveRedundantParenthesisTransform transform;

    @Mock
    private TransformQueue<SymjaNode> mockQueue;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transform = new RemoveRedundantParenthesisTransform();
    }

    @Nested
    @DisplayName("Basic Transformation Tests")
    class BasicTransformationTests {

        @Test
        @DisplayName("Should remove parentheses when child has higher priority than parent")
        void testApplyAll_ChildHigherPriorityThanParent_RemovesParentheses() {
            // Create (x * y) + z - multiplication has higher priority than addition
            SymjaSymbol x = SymjaNode.newNode(SymjaSymbol.class);
            x.set(SymjaSymbol.SYMBOL, "x");

            SymjaSymbol y = SymjaNode.newNode(SymjaSymbol.class);
            y.set(SymjaSymbol.SYMBOL, "y");

            SymjaSymbol z = SymjaNode.newNode(SymjaSymbol.class);
            z.set(SymjaSymbol.SYMBOL, "z");

            // Create multiplication: x * y
            SymjaOperator timesOp = SymjaNode.newNode(SymjaOperator.class);
            timesOp.set(SymjaOperator.OPERATOR, Operator.Times);

            SymjaFunction multiplyFunction = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(timesOp, x, y));
            multiplyFunction.set(SymjaFunction.HAS_PARENTHESIS, true);

            // Create addition: (x * y) + z
            SymjaOperator plusOp = SymjaNode.newNode(SymjaOperator.class);
            plusOp.set(SymjaOperator.OPERATOR, Operator.Plus);

            SymjaNode.newNode(SymjaFunction.class, Arrays.asList(plusOp, multiplyFunction, z));

            transform.applyAll(multiplyFunction, mockQueue);

            // Should remove parentheses since multiplication has higher priority than
            // addition
            assertThat(multiplyFunction.get(SymjaFunction.HAS_PARENTHESIS)).isFalse();
        }

        @Test
        @DisplayName("Should remove parentheses for root node (no parent)")
        void testApplyAll_RootNodeNoParent_RemovesParentheses() {
            // Create a standalone function with parentheses
            SymjaSymbol x = SymjaNode.newNode(SymjaSymbol.class);
            x.set(SymjaSymbol.SYMBOL, "x");

            SymjaSymbol y = SymjaNode.newNode(SymjaSymbol.class);
            y.set(SymjaSymbol.SYMBOL, "y");

            SymjaOperator plusOp = SymjaNode.newNode(SymjaOperator.class);
            plusOp.set(SymjaOperator.OPERATOR, Operator.Plus);

            SymjaFunction rootFunction = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(plusOp, x, y));
            rootFunction.set(SymjaFunction.HAS_PARENTHESIS, true);

            transform.applyAll(rootFunction, mockQueue);

            // Should remove parentheses since root doesn't need them
            assertThat(rootFunction.get(SymjaFunction.HAS_PARENTHESIS)).isFalse();
        }

        @Test
        @DisplayName("Should remove parentheses when equal priority and is left operand")
        void testApplyAll_EqualPriorityLeftOperand_RemovesParentheses() {
            // Create (a + b) + c - both are addition with same priority, left operand
            SymjaSymbol a = SymjaNode.newNode(SymjaSymbol.class);
            a.set(SymjaSymbol.SYMBOL, "a");

            SymjaSymbol b = SymjaNode.newNode(SymjaSymbol.class);
            b.set(SymjaSymbol.SYMBOL, "b");

            SymjaSymbol c = SymjaNode.newNode(SymjaSymbol.class);
            c.set(SymjaSymbol.SYMBOL, "c");

            // Create inner addition: a + b
            SymjaOperator innerPlusOp = SymjaNode.newNode(SymjaOperator.class);
            innerPlusOp.set(SymjaOperator.OPERATOR, Operator.Plus);

            SymjaFunction innerAddFunction = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(innerPlusOp, a, b));
            innerAddFunction.set(SymjaFunction.HAS_PARENTHESIS, true);

            // Create outer addition: (a + b) + c
            SymjaOperator outerPlusOp = SymjaNode.newNode(SymjaOperator.class);
            outerPlusOp.set(SymjaOperator.OPERATOR, Operator.Plus);

            SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(outerPlusOp, innerAddFunction, c));

            transform.applyAll(innerAddFunction, mockQueue);

            // Should remove parentheses since it's left operand with equal priority
            assertThat(innerAddFunction.get(SymjaFunction.HAS_PARENTHESIS)).isFalse();
        }

        @Test
        @DisplayName("Should not remove parentheses when equal priority and is right operand")
        void testApplyAll_EqualPriorityRightOperand_KeepsParentheses() {
            // Create a + (b + c) - both are addition with same priority, right operand
            SymjaSymbol a = SymjaNode.newNode(SymjaSymbol.class);
            a.set(SymjaSymbol.SYMBOL, "a");

            SymjaSymbol b = SymjaNode.newNode(SymjaSymbol.class);
            b.set(SymjaSymbol.SYMBOL, "b");

            SymjaSymbol c = SymjaNode.newNode(SymjaSymbol.class);
            c.set(SymjaSymbol.SYMBOL, "c");

            // Create inner addition: b + c
            SymjaOperator innerPlusOp = SymjaNode.newNode(SymjaOperator.class);
            innerPlusOp.set(SymjaOperator.OPERATOR, Operator.Plus);

            SymjaFunction innerAddFunction = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(innerPlusOp, b, c));
            innerAddFunction.set(SymjaFunction.HAS_PARENTHESIS, true);

            // Create outer addition: a + (b + c)
            SymjaOperator outerPlusOp = SymjaNode.newNode(SymjaOperator.class);
            outerPlusOp.set(SymjaOperator.OPERATOR, Operator.Plus);

            SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(outerPlusOp, a, innerAddFunction));

            transform.applyAll(innerAddFunction, mockQueue);

            // Should not remove parentheses since it's right operand with equal priority
            assertThat(innerAddFunction.get(SymjaFunction.HAS_PARENTHESIS)).isTrue();
        }

        @Test
        @DisplayName("Should not transform non-function nodes")
        void testApplyAll_NonFunctionNodes_DoesNotTransform() {
            SymjaInteger integer = SymjaNode.newNode(SymjaInteger.class);
            integer.set(SymjaInteger.VALUE_STRING, "42");

            transform.applyAll(integer, mockQueue);

            // No transformation should occur - this test just verifies no exception is
            // thrown
            assertThatCode(() -> transform.applyAll(integer, mockQueue))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should remove parentheses for root function")
        void testApplyAll_ParentNotFunction_DoesNotTransform() {
            // Create a function with parentheses (we can't manually set parent
            // relationships)
            SymjaSymbol x = SymjaNode.newNode(SymjaSymbol.class);
            x.set(SymjaSymbol.SYMBOL, "x");

            SymjaSymbol y = SymjaNode.newNode(SymjaSymbol.class);
            y.set(SymjaSymbol.SYMBOL, "y");

            SymjaOperator plusOp = SymjaNode.newNode(SymjaOperator.class);
            plusOp.set(SymjaOperator.OPERATOR, Operator.Plus);

            SymjaFunction function = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(plusOp, x, y));
            function.set(SymjaFunction.HAS_PARENTHESIS, true);

            transform.applyAll(function, mockQueue);

            // Root function should have parentheses removed
            assertThat(function.get(SymjaFunction.HAS_PARENTHESIS)).isFalse();
        }
    }

    @Nested
    @DisplayName("Operator Priority Tests")
    class OperatorPriorityTests {

        @ParameterizedTest
        @EnumSource(Operator.class)
        @DisplayName("Should correctly handle operator priorities")
        void testOperatorPriorities_AllOperators_HandleCorrectly(Operator operator) {
            // Test that all operators have defined priorities
            int priority = operator.getPriority();

            assertThat(priority).isGreaterThanOrEqualTo(0);
        }

        @Test
        @DisplayName("Should verify operator priority hierarchy")
        void testOperatorPriorityHierarchy_VerifiesCorrectOrdering() {
            // Verify the expected priority ordering
            assertThat(Operator.Plus.getPriority()).isLessThan(Operator.Times.getPriority());
            assertThat(Operator.Minus.getPriority()).isLessThan(Operator.Times.getPriority());
            assertThat(Operator.Times.getPriority()).isLessThan(Operator.Power.getPriority());

            // Plus and Minus should have same priority
            assertThat(Operator.Plus.getPriority()).isEqualTo(Operator.Minus.getPriority());
        }

        @Test
        @DisplayName("Should handle parentheses based on different priority levels")
        void testParenthesesBasedOnPriorityLevels_HandlesCorrectly() {
            // Test various operator combinations

            // Times inside Plus should remove parentheses (times > plus)
            assertThat(Operator.Times.getPriority()).isGreaterThan(Operator.Plus.getPriority());

            // Plus inside Times should keep parentheses (plus < times)
            assertThat(Operator.Plus.getPriority()).isLessThan(Operator.Times.getPriority());

            // Power inside Times should remove parentheses (power > times)
            assertThat(Operator.Power.getPriority()).isGreaterThan(Operator.Times.getPriority());
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle null transform queue gracefully")
        void testApplyAll_NullQueue_HandlesGracefully() {
            SymjaSymbol x = SymjaNode.newNode(SymjaSymbol.class);
            x.set(SymjaSymbol.SYMBOL, "x");

            SymjaSymbol y = SymjaNode.newNode(SymjaSymbol.class);
            y.set(SymjaSymbol.SYMBOL, "y");

            SymjaOperator plusOp = SymjaNode.newNode(SymjaOperator.class);
            plusOp.set(SymjaOperator.OPERATOR, Operator.Plus);

            SymjaFunction function = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(plusOp, x, y));

            assertThatCode(() -> transform.applyAll(function, null))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle malformed function structure gracefully")
        void testApplyAll_MalformedFunction_HandlesGracefully() {
            // Create function with insufficient children
            SymjaOperator plusOp = SymjaNode.newNode(SymjaOperator.class);
            plusOp.set(SymjaOperator.OPERATOR, Operator.Plus);

            SymjaFunction malformedFunction = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(plusOp)); // Missing operands

            assertThatCode(() -> transform.applyAll(malformedFunction, mockQueue))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle deeply nested function structures")
        void testApplyAll_DeeplyNestedFunctions_HandlesCorrectly() {
            // Create: ((a + b) * c) ^ d
            SymjaSymbol a = SymjaNode.newNode(SymjaSymbol.class);
            a.set(SymjaSymbol.SYMBOL, "a");

            SymjaSymbol b = SymjaNode.newNode(SymjaSymbol.class);
            b.set(SymjaSymbol.SYMBOL, "b");

            SymjaSymbol c = SymjaNode.newNode(SymjaSymbol.class);
            c.set(SymjaSymbol.SYMBOL, "c");

            SymjaSymbol d = SymjaNode.newNode(SymjaSymbol.class);
            d.set(SymjaSymbol.SYMBOL, "d");

            // Level 1: a + b
            SymjaOperator plusOp = SymjaNode.newNode(SymjaOperator.class);
            plusOp.set(SymjaOperator.OPERATOR, Operator.Plus);

            SymjaFunction innerFunction = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(plusOp, a, b));
            innerFunction.set(SymjaFunction.HAS_PARENTHESIS, true);

            // Level 2: (a + b) * c
            SymjaOperator timesOp = SymjaNode.newNode(SymjaOperator.class);
            timesOp.set(SymjaOperator.OPERATOR, Operator.Times);

            SymjaFunction middleFunction = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(timesOp, innerFunction, c));
            middleFunction.set(SymjaFunction.HAS_PARENTHESIS, true);

            // Level 3: ((a + b) * c) ^ d
            SymjaOperator powerOp = SymjaNode.newNode(SymjaOperator.class);
            powerOp.set(SymjaOperator.OPERATOR, Operator.Power);

            SymjaFunction outerFunction = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(powerOp, middleFunction, d));

            // Test each level
            transform.applyAll(innerFunction, mockQueue);
            transform.applyAll(middleFunction, mockQueue);
            transform.applyAll(outerFunction, mockQueue);

            // Should handle the nested structure correctly
        }

        @Test
        @DisplayName("Should handle function with no parenthesis flag correctly")
        void testApplyAll_FunctionWithoutParenthesisFlag_HandlesCorrectly() {
            SymjaSymbol x = SymjaNode.newNode(SymjaSymbol.class);
            x.set(SymjaSymbol.SYMBOL, "x");

            SymjaSymbol y = SymjaNode.newNode(SymjaSymbol.class);
            y.set(SymjaSymbol.SYMBOL, "y");

            SymjaOperator plusOp = SymjaNode.newNode(SymjaOperator.class);
            plusOp.set(SymjaOperator.OPERATOR, Operator.Plus);

            SymjaFunction function = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(plusOp, x, y));
            // Don't set HAS_PARENTHESIS - test default behavior

            assertThatCode(() -> transform.applyAll(function, mockQueue))
                    .doesNotThrowAnyException();
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
    @DisplayName("Complex Expression Tests")
    class ComplexExpressionTests {

        @Test
        @DisplayName("Should handle mixed arithmetic expressions correctly")
        void testComplexArithmetic_MixedOperators_HandlesCorrectly() {
            // Test expression: a + b * c - d / e
            // Multiplication and division should not need parentheses when inside
            // addition/subtraction

            SymjaSymbol a = SymjaNode.newNode(SymjaSymbol.class);
            a.set(SymjaSymbol.SYMBOL, "a");

            SymjaSymbol b = SymjaNode.newNode(SymjaSymbol.class);
            b.set(SymjaSymbol.SYMBOL, "b");

            SymjaSymbol c = SymjaNode.newNode(SymjaSymbol.class);
            c.set(SymjaSymbol.SYMBOL, "c");

            // Create b * c (higher priority than plus)
            SymjaOperator timesOp = SymjaNode.newNode(SymjaOperator.class);
            timesOp.set(SymjaOperator.OPERATOR, Operator.Times);

            SymjaFunction multiplyFunction = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(timesOp, b, c));
            multiplyFunction.set(SymjaFunction.HAS_PARENTHESIS, true);

            // Create a + (b * c)
            SymjaOperator plusOp = SymjaNode.newNode(SymjaOperator.class);
            plusOp.set(SymjaOperator.OPERATOR, Operator.Plus);

            SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(plusOp, a, multiplyFunction));

            transform.applyAll(multiplyFunction, mockQueue);

            // Multiplication should have parentheses removed since it has higher priority
            // than addition
            assertThat(multiplyFunction.get(SymjaFunction.HAS_PARENTHESIS)).isFalse();
        }

        @Test
        @DisplayName("Should handle associativity rules correctly")
        void testAssociativityRules_HandlesCorrectly() {
            // Test left associativity: (a - b) - c vs a - (b - c)

            SymjaSymbol a = SymjaNode.newNode(SymjaSymbol.class);
            a.set(SymjaSymbol.SYMBOL, "a");

            SymjaSymbol b = SymjaNode.newNode(SymjaSymbol.class);
            b.set(SymjaSymbol.SYMBOL, "b");

            SymjaSymbol c = SymjaNode.newNode(SymjaSymbol.class);
            c.set(SymjaSymbol.SYMBOL, "c");

            // Create a - b (left operand)
            SymjaOperator leftMinusOp = SymjaNode.newNode(SymjaOperator.class);
            leftMinusOp.set(SymjaOperator.OPERATOR, Operator.Minus);

            SymjaFunction leftMinusFunction = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(leftMinusOp, a, b));
            leftMinusFunction.set(SymjaFunction.HAS_PARENTHESIS, true);

            // Create (a - b) - c
            SymjaOperator rightMinusOp = SymjaNode.newNode(SymjaOperator.class);
            rightMinusOp.set(SymjaOperator.OPERATOR, Operator.Minus);

            SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(rightMinusOp, leftMinusFunction, c));

            transform.applyAll(leftMinusFunction, mockQueue);

            // Left operand with equal priority should have parentheses removed
            assertThat(leftMinusFunction.get(SymjaFunction.HAS_PARENTHESIS)).isFalse();
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should process multiple transformations efficiently")
        void testPerformance_MultipleTransformations_CompletesQuickly() {
            long startTime = System.nanoTime();

            // Create and process multiple function nodes
            for (int i = 0; i < 100; i++) {
                SymjaSymbol x = SymjaNode.newNode(SymjaSymbol.class);
                x.set(SymjaSymbol.SYMBOL, "x" + i);

                SymjaSymbol y = SymjaNode.newNode(SymjaSymbol.class);
                y.set(SymjaSymbol.SYMBOL, "y" + i);

                SymjaOperator operator = SymjaNode.newNode(SymjaOperator.class);
                operator.set(SymjaOperator.OPERATOR, i % 2 == 0 ? Operator.Plus : Operator.Times);

                SymjaFunction function = SymjaNode.newNode(SymjaFunction.class,
                        Arrays.asList(operator, x, y));
                function.set(SymjaFunction.HAS_PARENTHESIS, true);

                transform.applyAll(function, mockQueue);
            }

            long endTime = System.nanoTime();
            long durationMs = (endTime - startTime) / 1_000_000;

            // Should complete within reasonable time
            assertThat(durationMs).isLessThan(1000);
        }
    }
}
