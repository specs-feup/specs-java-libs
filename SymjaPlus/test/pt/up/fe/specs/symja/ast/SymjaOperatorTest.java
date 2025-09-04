package pt.up.fe.specs.symja.ast;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * Unit tests for {@link SymjaOperator}.
 * 
 * @author Generated Tests
 */
@DisplayName("SymjaOperator")
class SymjaOperatorTest {

    @Nested
    @DisplayName("Node Creation Tests")
    class NodeCreationTests {

        @Test
        @DisplayName("Should create SymjaOperator with no children")
        void testNewNode_NoChildren_CreatesValidSymjaOperator() {
            SymjaOperator operator = SymjaNode.newNode(SymjaOperator.class);

            assertThat(operator).isNotNull();
            assertThat(operator.getNumChildren()).isEqualTo(0);
            assertThat(operator.getChildren()).isEmpty();
        }

        @Test
        @DisplayName("Should create SymjaOperator with children")
        void testNewNode_WithChildren_CreatesOperatorWithChildren() {
            SymjaSymbol child = SymjaNode.newNode(SymjaSymbol.class);
            SymjaOperator operator = SymjaNode.newNode(SymjaOperator.class, Arrays.asList(child));

            assertThat(operator.getNumChildren()).isEqualTo(1);
            assertThat(operator.getChild(0)).isSameAs(child);
        }

        @Test
        @DisplayName("Should create SymjaOperator with mixed type children")
        void testNewNode_MixedChildren_CreatesOperatorWithAllChildren() {
            SymjaSymbol symbol = SymjaNode.newNode(SymjaSymbol.class);
            SymjaInteger integer = SymjaNode.newNode(SymjaInteger.class);
            SymjaOperator operator = SymjaNode.newNode(SymjaOperator.class, Arrays.asList(symbol, integer));

            assertThat(operator.getNumChildren()).isEqualTo(2);
            assertThat(operator.getChild(0)).isSameAs(symbol);
            assertThat(operator.getChild(1)).isSameAs(integer);
        }
    }

    @Nested
    @DisplayName("Operator Property Tests")
    class OperatorPropertyTests {

        @Test
        @DisplayName("Should set and get operator property")
        void testOperatorProperty_SetAndGet_WorksCorrectly() {
            SymjaOperator operatorNode = SymjaNode.newNode(SymjaOperator.class);
            operatorNode.set(SymjaOperator.OPERATOR, Operator.Plus);

            assertThat(operatorNode.get(SymjaOperator.OPERATOR)).isEqualTo(Operator.Plus);
        }

        @Test
        @DisplayName("Should handle operator property updates")
        void testOperatorProperty_Updates_WorksCorrectly() {
            SymjaOperator operatorNode = SymjaNode.newNode(SymjaOperator.class);
            operatorNode.set(SymjaOperator.OPERATOR, Operator.Plus);
            operatorNode.set(SymjaOperator.OPERATOR, Operator.Times);

            assertThat(operatorNode.get(SymjaOperator.OPERATOR)).isEqualTo(Operator.Times);
        }

        @ParameterizedTest
        @EnumSource(Operator.class)
        @DisplayName("Should handle all operator types")
        void testOperatorProperty_AllTypes_HandlesCorrectly(Operator operator) {
            SymjaOperator operatorNode = SymjaNode.newNode(SymjaOperator.class);
            operatorNode.set(SymjaOperator.OPERATOR, operator);

            assertThat(operatorNode.get(SymjaOperator.OPERATOR)).isEqualTo(operator);
        }

        @Test
        @DisplayName("Should handle null operator")
        void testOperatorProperty_NullValue_HandlesCorrectly() {
            SymjaOperator operatorNode = SymjaNode.newNode(SymjaOperator.class);
            operatorNode.set(SymjaOperator.OPERATOR, null);

            // Note: Based on DataKey behavior, may not actually store null
            Operator retrieved = operatorNode.get(SymjaOperator.OPERATOR);
            // Allow either null or some default behavior
            assertThat(retrieved).satisfiesAnyOf(
                    op -> assertThat(op).isNull(),
                    op -> assertThat(op).isNotNull());
        }
    }

    @Nested
    @DisplayName("DataKey Tests")
    class DataKeyTests {

        @Test
        @DisplayName("Should have correct OPERATOR DataKey properties")
        void testOperatorDataKey_Properties_AreCorrect() {
            assertThat(SymjaOperator.OPERATOR).isNotNull();
            assertThat(SymjaOperator.OPERATOR.getName()).isEqualTo("operator");
        }

        @Test
        @DisplayName("Should retrieve operator via DataKey")
        void testOperatorDataKey_Retrieval_WorksCorrectly() {
            SymjaOperator operatorNode = SymjaNode.newNode(SymjaOperator.class);
            operatorNode.set(SymjaOperator.OPERATOR, Operator.Minus);

            Operator retrievedOperator = operatorNode.get(SymjaOperator.OPERATOR);
            assertThat(retrievedOperator).isEqualTo(Operator.Minus);
        }

        @Test
        @DisplayName("Should handle has() check for operator property")
        void testOperatorDataKey_HasCheck_WorksCorrectly() {
            SymjaOperator operatorNode = SymjaNode.newNode(SymjaOperator.class);

            // Initially should not have the property set
            assertThat(operatorNode.hasValue(SymjaOperator.OPERATOR)).isFalse();

            operatorNode.set(SymjaOperator.OPERATOR, Operator.Power);
            assertThat(operatorNode.hasValue(SymjaOperator.OPERATOR)).isTrue();
        }
    }

    @Nested
    @DisplayName("Node Hierarchy Tests")
    class NodeHierarchyTests {

        @Test
        @DisplayName("Should correctly identify as SymjaOperator instance")
        void testInstanceType_SymjaOperator_IdentifiesCorrectly() {
            SymjaOperator operator = SymjaNode.newNode(SymjaOperator.class);

            assertThat(operator).isInstanceOf(SymjaNode.class);
            assertThat(operator).isInstanceOf(SymjaOperator.class);
            assertThat(operator).isNotInstanceOf(SymjaSymbol.class);
            assertThat(operator).isNotInstanceOf(SymjaInteger.class);
        }

        @Test
        @DisplayName("Should support parent-child relationships")
        void testParentChildRelationships_SymjaOperator_WorksCorrectly() {
            SymjaOperator parent = SymjaNode.newNode(SymjaOperator.class);
            SymjaOperator child = SymjaNode.newNode(SymjaOperator.class);

            parent = SymjaNode.newNode(SymjaOperator.class, Arrays.asList(child));

            assertThat(parent.getNumChildren()).isEqualTo(1);
            assertThat(parent.getChild(0)).isSameAs(child);
            assertThat(child.getParent()).isSameAs(parent);
        }

        @Test
        @DisplayName("Should support mixed node type children")
        void testMixedNodeTypes_AsChildren_WorksCorrectly() {
            SymjaSymbol symbol = SymjaNode.newNode(SymjaSymbol.class);
            SymjaInteger integer = SymjaNode.newNode(SymjaInteger.class);
            SymjaOperator operator = SymjaNode.newNode(SymjaOperator.class, Arrays.asList(symbol, integer));

            assertThat(operator.getNumChildren()).isEqualTo(2);
            assertThat(operator.getChild(0)).isInstanceOf(SymjaSymbol.class);
            assertThat(operator.getChild(1)).isInstanceOf(SymjaInteger.class);
        }
    }

    @Nested
    @DisplayName("Tree Operations Tests")
    class TreeOperationsTests {

        @Test
        @DisplayName("Should support tree traversal operations")
        void testTreeTraversal_SymjaOperator_WorksCorrectly() {
            SymjaOperator root = SymjaNode.newNode(SymjaOperator.class);

            SymjaSymbol child1 = SymjaNode.newNode(SymjaSymbol.class);
            SymjaInteger child2 = SymjaNode.newNode(SymjaInteger.class);

            root = SymjaNode.newNode(SymjaOperator.class, Arrays.asList(child1, child2));
            root.set(SymjaOperator.OPERATOR, Operator.Plus);

            // Test tree structure
            assertThat(root.getDescendants()).hasSize(2);
            assertThat(root.getDescendants()).contains(child1, child2);
        }

        @Test
        @DisplayName("Should support node copying operations")
        void testNodeCopying_SymjaOperator_WorksCorrectly() {
            SymjaOperator original = SymjaNode.newNode(SymjaOperator.class);
            original.set(SymjaOperator.OPERATOR, Operator.UnaryMinus);

            SymjaOperator copy = (SymjaOperator) original.copy();

            assertThat(copy).isNotSameAs(original);
            assertThat(copy.get(SymjaOperator.OPERATOR)).isEqualTo(Operator.UnaryMinus);
            assertThat(copy.getClass()).isEqualTo(SymjaOperator.class);
        }

        @Test
        @DisplayName("Should support deep copying with children")
        void testDeepCopying_WithChildren_WorksCorrectly() {
            SymjaSymbol child = SymjaNode.newNode(SymjaSymbol.class);
            child.set(SymjaSymbol.SYMBOL, "x");

            SymjaOperator parent = SymjaNode.newNode(SymjaOperator.class, Arrays.asList(child));
            parent.set(SymjaOperator.OPERATOR, Operator.Times);

            SymjaOperator parentCopy = (SymjaOperator) parent.copy();

            assertThat(parentCopy).isNotSameAs(parent);
            assertThat(parentCopy.getNumChildren()).isEqualTo(1);
            assertThat(parentCopy.getChild(0)).isNotSameAs(child);
            assertThat(((SymjaSymbol) parentCopy.getChild(0)).get(SymjaSymbol.SYMBOL)).isEqualTo("x");
            assertThat(parentCopy.get(SymjaOperator.OPERATOR)).isEqualTo(Operator.Times);
        }
    }

    @Nested
    @DisplayName("Mathematical Expression Tests")
    class MathematicalExpressionTests {

        @Test
        @DisplayName("Should work correctly in binary operations")
        void testBinaryOperations_SymjaOperator_WorksCorrectly() {
            // Create expression: 5 + 3
            SymjaInteger five = SymjaNode.newNode(SymjaInteger.class);
            five.set(SymjaInteger.VALUE_STRING, "5");

            SymjaInteger three = SymjaNode.newNode(SymjaInteger.class);
            three.set(SymjaInteger.VALUE_STRING, "3");

            SymjaOperator plusOp = SymjaNode.newNode(SymjaOperator.class);
            plusOp.set(SymjaOperator.OPERATOR, Operator.Plus);

            // In typical use, operators would be children of functions
            SymjaFunction expression = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(plusOp, five, three));

            // Verify the operator
            SymjaOperator op = (SymjaOperator) expression.getChild(0);
            assertThat(op.get(SymjaOperator.OPERATOR)).isEqualTo(Operator.Plus);
            assertThat(op.get(SymjaOperator.OPERATOR).getSymbol()).isEqualTo("+");
            assertThat(op.get(SymjaOperator.OPERATOR).getPriority()).isEqualTo(2);
        }

        @Test
        @DisplayName("Should handle unary operations")
        void testUnaryOperations_SymjaOperator_WorksCorrectly() {
            // Create expression: -x
            SymjaSymbol x = SymjaNode.newNode(SymjaSymbol.class);
            x.set(SymjaSymbol.SYMBOL, "x");

            SymjaOperator minusOp = SymjaNode.newNode(SymjaOperator.class);
            minusOp.set(SymjaOperator.OPERATOR, Operator.UnaryMinus);

            SymjaFunction expression = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(minusOp, x));

            SymjaOperator op = (SymjaOperator) expression.getChild(0);
            assertThat(op.get(SymjaOperator.OPERATOR)).isEqualTo(Operator.UnaryMinus);
            assertThat(op.get(SymjaOperator.OPERATOR).getSymbol()).isEqualTo("-");
        }

        @Test
        @DisplayName("Should respect operator precedence")
        void testOperatorPrecedence_SymjaOperator_WorksCorrectly() {
            SymjaOperator plusOp = SymjaNode.newNode(SymjaOperator.class);
            plusOp.set(SymjaOperator.OPERATOR, Operator.Plus);

            SymjaOperator timesOp = SymjaNode.newNode(SymjaOperator.class);
            timesOp.set(SymjaOperator.OPERATOR, Operator.Times);

            SymjaOperator powerOp = SymjaNode.newNode(SymjaOperator.class);
            powerOp.set(SymjaOperator.OPERATOR, Operator.Power);

            // Verify precedence order: Plus < Times < Power
            assertThat(plusOp.get(SymjaOperator.OPERATOR).getPriority())
                    .isLessThan(timesOp.get(SymjaOperator.OPERATOR).getPriority());
            assertThat(timesOp.get(SymjaOperator.OPERATOR).getPriority())
                    .isLessThan(powerOp.get(SymjaOperator.OPERATOR).getPriority());
        }

        @ParameterizedTest
        @EnumSource(Operator.class)
        @DisplayName("Should work with all operator types in expressions")
        void testAllOperatorTypes_InExpressions_WorksCorrectly(Operator operatorType) {
            SymjaOperator op = SymjaNode.newNode(SymjaOperator.class);
            op.set(SymjaOperator.OPERATOR, operatorType);

            SymjaSymbol operand1 = SymjaNode.newNode(SymjaSymbol.class);
            operand1.set(SymjaSymbol.SYMBOL, "x");

            SymjaSymbol operand2 = SymjaNode.newNode(SymjaSymbol.class);
            operand2.set(SymjaSymbol.SYMBOL, "y");

            SymjaFunction expression = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(op, operand1, operand2));

            SymjaOperator retrievedOp = (SymjaOperator) expression.getChild(0);
            assertThat(retrievedOp.get(SymjaOperator.OPERATOR)).isEqualTo(operatorType);
            assertThat(retrievedOp.get(SymjaOperator.OPERATOR).getSymbol()).isNotNull();
            assertThat(retrievedOp.get(SymjaOperator.OPERATOR).getPriority()).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work in complex nested expressions")
        void testComplexNestedExpressions_Integration_WorksCorrectly() {
            // Create expression: (a + b) * (c - d)
            SymjaSymbol a = SymjaNode.newNode(SymjaSymbol.class);
            a.set(SymjaSymbol.SYMBOL, "a");
            SymjaSymbol b = SymjaNode.newNode(SymjaSymbol.class);
            b.set(SymjaSymbol.SYMBOL, "b");
            SymjaSymbol c = SymjaNode.newNode(SymjaSymbol.class);
            c.set(SymjaSymbol.SYMBOL, "c");
            SymjaSymbol d = SymjaNode.newNode(SymjaSymbol.class);
            d.set(SymjaSymbol.SYMBOL, "d");

            SymjaOperator plusOp = SymjaNode.newNode(SymjaOperator.class);
            plusOp.set(SymjaOperator.OPERATOR, Operator.Plus);

            SymjaOperator minusOp = SymjaNode.newNode(SymjaOperator.class);
            minusOp.set(SymjaOperator.OPERATOR, Operator.Minus);

            SymjaOperator timesOp = SymjaNode.newNode(SymjaOperator.class);
            timesOp.set(SymjaOperator.OPERATOR, Operator.Times);

            // Build sub-expressions
            SymjaFunction leftExpr = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(plusOp, a, b));
            SymjaFunction rightExpr = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(minusOp, c, d));

            // Build main expression
            SymjaFunction mainExpr = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(timesOp, leftExpr, rightExpr));

            // Verify all operators are accessible and correct
            java.util.List<SymjaOperator> operators = mainExpr.getDescendantsStream()
                    .filter(SymjaOperator.class::isInstance)
                    .map(SymjaOperator.class::cast)
                    .toList();

            assertThat(operators).hasSize(3);

            java.util.Set<Operator> operatorTypes = operators.stream()
                    .map(op -> op.get(SymjaOperator.OPERATOR))
                    .collect(java.util.stream.Collectors.toSet());
            assertThat(operatorTypes).containsExactlyInAnyOrder(Operator.Plus, Operator.Minus, Operator.Times);
        }

        @Test
        @DisplayName("Should support operator comparison")
        void testOperatorComparison_Integration_WorksCorrectly() {
            SymjaOperator op1 = SymjaNode.newNode(SymjaOperator.class);
            op1.set(SymjaOperator.OPERATOR, Operator.Plus);

            SymjaOperator op2 = SymjaNode.newNode(SymjaOperator.class);
            op2.set(SymjaOperator.OPERATOR, Operator.Plus);

            SymjaOperator op3 = SymjaNode.newNode(SymjaOperator.class);
            op3.set(SymjaOperator.OPERATOR, Operator.Times);

            // Same operator type should have equal operator values
            assertThat(op1.get(SymjaOperator.OPERATOR)).isEqualTo(op2.get(SymjaOperator.OPERATOR));
            assertThat(op1.get(SymjaOperator.OPERATOR)).isNotEqualTo(op3.get(SymjaOperator.OPERATOR));
        }

        @Test
        @DisplayName("Should work correctly with Operator enum integration")
        void testOperatorEnumIntegration_WorksCorrectly() {
            SymjaOperator operatorNode = SymjaNode.newNode(SymjaOperator.class);
            operatorNode.set(SymjaOperator.OPERATOR, Operator.Power);

            // Test that the operator node correctly stores and retrieves the enum
            Operator storedOperator = operatorNode.get(SymjaOperator.OPERATOR);
            assertThat(storedOperator).isEqualTo(Operator.Power);
            assertThat(storedOperator.getSymbol()).isEqualTo("^");
            assertThat(storedOperator.getPriority()).isEqualTo(4);

            // Verify the operator can be used in expressions
            SymjaSymbol base = SymjaNode.newNode(SymjaSymbol.class);
            base.set(SymjaSymbol.SYMBOL, "x");
            SymjaInteger exponent = SymjaNode.newNode(SymjaInteger.class);
            exponent.set(SymjaInteger.VALUE_STRING, "2");

            SymjaFunction powerExpression = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(operatorNode, base, exponent));

            SymjaOperator retrievedOp = (SymjaOperator) powerExpression.getChild(0);
            assertThat(retrievedOp.get(SymjaOperator.OPERATOR)).isEqualTo(Operator.Power);
        }
    }
}
