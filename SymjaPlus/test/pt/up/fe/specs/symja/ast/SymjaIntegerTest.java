package pt.up.fe.specs.symja.ast;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Unit tests for {@link SymjaInteger}.
 * 
 * @author Generated Tests
 */
@DisplayName("SymjaInteger")
class SymjaIntegerTest {

    @Nested
    @DisplayName("Node Creation Tests")
    class NodeCreationTests {

        @Test
        @DisplayName("Should create SymjaInteger with no children")
        void testNewNode_NoChildren_CreatesValidSymjaInteger() {
            SymjaInteger integer = SymjaNode.newNode(SymjaInteger.class);

            assertThat(integer).isNotNull();
            assertThat(integer.getNumChildren()).isEqualTo(0);
            assertThat(integer.getChildren()).isEmpty();
        }

        @Test
        @DisplayName("Should create SymjaInteger with children")
        void testNewNode_WithChildren_CreatesIntegerWithChildren() {
            SymjaInteger child = SymjaNode.newNode(SymjaInteger.class);
            SymjaInteger integer = SymjaNode.newNode(SymjaInteger.class, Arrays.asList(child));

            assertThat(integer.getNumChildren()).isEqualTo(1);
            assertThat(integer.getChild(0)).isSameAs(child);
        }

        @Test
        @DisplayName("Should create SymjaInteger with mixed type children")
        void testNewNode_MixedChildren_CreatesIntegerWithAllChildren() {
            SymjaInteger childInt = SymjaNode.newNode(SymjaInteger.class);
            SymjaSymbol childSym = SymjaNode.newNode(SymjaSymbol.class);
            SymjaInteger integer = SymjaNode.newNode(SymjaInteger.class, Arrays.asList(childInt, childSym));

            assertThat(integer.getNumChildren()).isEqualTo(2);
            assertThat(integer.getChild(0)).isSameAs(childInt);
            assertThat(integer.getChild(1)).isSameAs(childSym);
        }
    }

    @Nested
    @DisplayName("Value String Property Tests")
    class ValueStringPropertyTests {

        @Test
        @DisplayName("Should set and get valueString property")
        void testValueStringProperty_SetAndGet_WorksCorrectly() {
            SymjaInteger integer = SymjaNode.newNode(SymjaInteger.class);
            integer.set(SymjaInteger.VALUE_STRING, "42");

            assertThat(integer.get(SymjaInteger.VALUE_STRING)).isEqualTo("42");
        }

        @Test
        @DisplayName("Should handle valueString updates")
        void testValueStringProperty_Updates_WorksCorrectly() {
            SymjaInteger integer = SymjaNode.newNode(SymjaInteger.class);
            integer.set(SymjaInteger.VALUE_STRING, "100");
            integer.set(SymjaInteger.VALUE_STRING, "200");

            assertThat(integer.get(SymjaInteger.VALUE_STRING)).isEqualTo("200");
        }

        @Test
        @DisplayName("Should handle null valueString")
        void testValueStringProperty_NullValue_HandlesCorrectly() {
            SymjaInteger integer = SymjaNode.newNode(SymjaInteger.class);
            integer.set(SymjaInteger.VALUE_STRING, null);

            assertThat(integer.get(SymjaInteger.VALUE_STRING)).isEqualTo("");
        }

        @ParameterizedTest
        @ValueSource(strings = { "0", "1", "42", "100", "999", "-1", "-42", "-100", "-999" })
        @DisplayName("Should handle various integer values")
        void testValueStringProperty_VariousIntegers_HandlesCorrectly(String value) {
            SymjaInteger integer = SymjaNode.newNode(SymjaInteger.class);
            integer.set(SymjaInteger.VALUE_STRING, value);

            assertThat(integer.get(SymjaInteger.VALUE_STRING)).isEqualTo(value);
        }

        @ParameterizedTest
        @ValueSource(strings = { "123456789", "-987654321", "0000", "007", "+42", "1e10", "1.0" })
        @DisplayName("Should handle edge case integer string representations")
        void testValueStringProperty_EdgeCases_HandlesCorrectly(String value) {
            SymjaInteger integer = SymjaNode.newNode(SymjaInteger.class);
            integer.set(SymjaInteger.VALUE_STRING, value);

            assertThat(integer.get(SymjaInteger.VALUE_STRING)).isEqualTo(value);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = { " ", "   ", "abc", "not_a_number" })
        @DisplayName("Should handle invalid or empty integer strings")
        void testValueStringProperty_InvalidValues_HandlesCorrectly(String value) {
            SymjaInteger integer = SymjaNode.newNode(SymjaInteger.class);
            integer.set(SymjaInteger.VALUE_STRING, value);

            String expected = (value == null) ? "" : value;
            assertThat(integer.get(SymjaInteger.VALUE_STRING)).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("DataKey Tests")
    class DataKeyTests {

        @Test
        @DisplayName("Should have correct VALUE_STRING DataKey properties")
        void testValueStringDataKey_Properties_AreCorrect() {
            assertThat(SymjaInteger.VALUE_STRING).isNotNull();
            assertThat(SymjaInteger.VALUE_STRING.getName()).isEqualTo("valueString");
        }

        @Test
        @DisplayName("Should retrieve valueString via DataKey")
        void testValueStringDataKey_Retrieval_WorksCorrectly() {
            SymjaInteger integer = SymjaNode.newNode(SymjaInteger.class);
            integer.set(SymjaInteger.VALUE_STRING, "123");

            String retrievedValue = integer.get(SymjaInteger.VALUE_STRING);
            assertThat(retrievedValue).isEqualTo("123");
        }

        @Test
        @DisplayName("Should handle has() check for valueString property")
        void testValueStringDataKey_HasCheck_WorksCorrectly() {
            SymjaInteger integer = SymjaNode.newNode(SymjaInteger.class);

            // Initially should not have the property set
            assertThat(integer.hasValue(SymjaInteger.VALUE_STRING)).isFalse();

            integer.set(SymjaInteger.VALUE_STRING, "456");
            assertThat(integer.hasValue(SymjaInteger.VALUE_STRING)).isTrue();
        }
    }

    @Nested
    @DisplayName("Node Hierarchy Tests")
    class NodeHierarchyTests {

        @Test
        @DisplayName("Should correctly identify as SymjaInteger instance")
        void testInstanceType_SymjaInteger_IdentifiesCorrectly() {
            SymjaInteger integer = SymjaNode.newNode(SymjaInteger.class);

            assertThat(integer).isInstanceOf(SymjaNode.class);
            assertThat(integer).isInstanceOf(SymjaInteger.class);
            assertThat(integer).isNotInstanceOf(SymjaSymbol.class);
            assertThat(integer).isNotInstanceOf(SymjaFunction.class);
        }

        @Test
        @DisplayName("Should support parent-child relationships")
        void testParentChildRelationships_SymjaInteger_WorksCorrectly() {
            SymjaInteger parent = SymjaNode.newNode(SymjaInteger.class);
            SymjaInteger child = SymjaNode.newNode(SymjaInteger.class);

            parent = SymjaNode.newNode(SymjaInteger.class, Arrays.asList(child));

            assertThat(parent.getNumChildren()).isEqualTo(1);
            assertThat(parent.getChild(0)).isSameAs(child);
            assertThat(child.getParent()).isSameAs(parent);
        }

        @Test
        @DisplayName("Should support mixed node type children")
        void testMixedNodeTypes_AsChildren_WorksCorrectly() {
            SymjaInteger integer = SymjaNode.newNode(SymjaInteger.class);
            integer.set(SymjaInteger.VALUE_STRING, "42");

            SymjaSymbol symbol = SymjaNode.newNode(SymjaSymbol.class);
            symbol.set(SymjaSymbol.SYMBOL, "x");

            SymjaFunction function = SymjaNode.newNode(SymjaFunction.class, Arrays.asList(integer, symbol));

            assertThat(function.getNumChildren()).isEqualTo(2);
            assertThat(function.getChild(0)).isInstanceOf(SymjaInteger.class);
            assertThat(function.getChild(1)).isInstanceOf(SymjaSymbol.class);
        }
    }

    @Nested
    @DisplayName("Tree Operations Tests")
    class TreeOperationsTests {

        @Test
        @DisplayName("Should support tree traversal operations")
        void testTreeTraversal_SymjaInteger_WorksCorrectly() {
            SymjaInteger root = SymjaNode.newNode(SymjaInteger.class);
            root.set(SymjaInteger.VALUE_STRING, "1");

            SymjaInteger child1 = SymjaNode.newNode(SymjaInteger.class);
            child1.set(SymjaInteger.VALUE_STRING, "2");

            SymjaInteger child2 = SymjaNode.newNode(SymjaInteger.class);
            child2.set(SymjaInteger.VALUE_STRING, "3");

            root = SymjaNode.newNode(SymjaInteger.class, Arrays.asList(child1, child2));
            root.set(SymjaInteger.VALUE_STRING, "1");

            // Test tree structure
            assertThat(root.getDescendants()).hasSize(2);
            assertThat(root.getDescendants()).contains(child1, child2);
        }

        @Test
        @DisplayName("Should support node copying operations")
        void testNodeCopying_SymjaInteger_WorksCorrectly() {
            SymjaInteger original = SymjaNode.newNode(SymjaInteger.class);
            original.set(SymjaInteger.VALUE_STRING, "777");

            SymjaInteger copy = (SymjaInteger) original.copy();

            assertThat(copy).isNotSameAs(original);
            assertThat(copy.get(SymjaInteger.VALUE_STRING)).isEqualTo("777");
            assertThat(copy.getClass()).isEqualTo(SymjaInteger.class);
        }

        @Test
        @DisplayName("Should support deep copying with children")
        void testDeepCopying_WithChildren_WorksCorrectly() {
            SymjaInteger child = SymjaNode.newNode(SymjaInteger.class);
            child.set(SymjaInteger.VALUE_STRING, "10");

            SymjaInteger parent = SymjaNode.newNode(SymjaInteger.class, Arrays.asList(child));
            parent.set(SymjaInteger.VALUE_STRING, "20");

            SymjaInteger parentCopy = (SymjaInteger) parent.copy();

            assertThat(parentCopy).isNotSameAs(parent);
            assertThat(parentCopy.getNumChildren()).isEqualTo(1);
            assertThat(parentCopy.getChild(0)).isNotSameAs(child);
            assertThat(((SymjaInteger) parentCopy.getChild(0)).get(SymjaInteger.VALUE_STRING)).isEqualTo("10");
        }
    }

    @Nested
    @DisplayName("Mathematical Operations Tests")
    class MathematicalOperationsTests {

        @Test
        @DisplayName("Should work correctly in arithmetic expressions")
        void testArithmeticExpressions_Integration_WorksCorrectly() {
            // Create expression: 5 + 3
            SymjaInteger five = SymjaNode.newNode(SymjaInteger.class);
            five.set(SymjaInteger.VALUE_STRING, "5");

            SymjaInteger three = SymjaNode.newNode(SymjaInteger.class);
            three.set(SymjaInteger.VALUE_STRING, "3");

            SymjaOperator plusOp = SymjaNode.newNode(SymjaOperator.class);
            plusOp.set(SymjaOperator.OPERATOR, Operator.Plus);

            SymjaFunction expression = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(plusOp, five, three));

            // Verify structure
            assertThat(expression.getNumChildren()).isEqualTo(3);
            assertThat(expression.getChild(0)).isInstanceOf(SymjaOperator.class);
            assertThat(expression.getChild(1)).isInstanceOf(SymjaInteger.class);
            assertThat(expression.getChild(2)).isInstanceOf(SymjaInteger.class);

            SymjaInteger leftOperand = (SymjaInteger) expression.getChild(1);
            SymjaInteger rightOperand = (SymjaInteger) expression.getChild(2);
            assertThat(leftOperand.get(SymjaInteger.VALUE_STRING)).isEqualTo("5");
            assertThat(rightOperand.get(SymjaInteger.VALUE_STRING)).isEqualTo("3");
        }

        @Test
        @DisplayName("Should handle negative integers in expressions")
        void testNegativeIntegers_Integration_WorksCorrectly() {
            // Create expression: -10 * 2
            SymjaInteger minusTen = SymjaNode.newNode(SymjaInteger.class);
            minusTen.set(SymjaInteger.VALUE_STRING, "-10");

            SymjaInteger two = SymjaNode.newNode(SymjaInteger.class);
            two.set(SymjaInteger.VALUE_STRING, "2");

            SymjaOperator timesOp = SymjaNode.newNode(SymjaOperator.class);
            timesOp.set(SymjaOperator.OPERATOR, Operator.Times);

            SymjaFunction expression = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(timesOp, minusTen, two));

            SymjaInteger leftOperand = (SymjaInteger) expression.getChild(1);
            SymjaInteger rightOperand = (SymjaInteger) expression.getChild(2);
            assertThat(leftOperand.get(SymjaInteger.VALUE_STRING)).isEqualTo("-10");
            assertThat(rightOperand.get(SymjaInteger.VALUE_STRING)).isEqualTo("2");
        }

        @Test
        @DisplayName("Should handle zero values correctly")
        void testZeroValues_Integration_WorksCorrectly() {
            SymjaInteger zero1 = SymjaNode.newNode(SymjaInteger.class);
            zero1.set(SymjaInteger.VALUE_STRING, "0");

            SymjaInteger zero2 = SymjaNode.newNode(SymjaInteger.class);
            zero2.set(SymjaInteger.VALUE_STRING, "0000");

            assertThat(zero1.get(SymjaInteger.VALUE_STRING)).isEqualTo("0");
            assertThat(zero2.get(SymjaInteger.VALUE_STRING)).isEqualTo("0000");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work correctly in complex mathematical expressions")
        void testComplexExpressions_Integration_WorksCorrectly() {
            // Create expression: (2 + 3) * 4
            SymjaInteger two = SymjaNode.newNode(SymjaInteger.class);
            two.set(SymjaInteger.VALUE_STRING, "2");

            SymjaInteger three = SymjaNode.newNode(SymjaInteger.class);
            three.set(SymjaInteger.VALUE_STRING, "3");

            SymjaInteger four = SymjaNode.newNode(SymjaInteger.class);
            four.set(SymjaInteger.VALUE_STRING, "4");

            SymjaOperator plusOp = SymjaNode.newNode(SymjaOperator.class);
            plusOp.set(SymjaOperator.OPERATOR, Operator.Plus);

            SymjaFunction innerExpr = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(plusOp, two, three));

            SymjaOperator timesOp = SymjaNode.newNode(SymjaOperator.class);
            timesOp.set(SymjaOperator.OPERATOR, Operator.Times);

            SymjaFunction outerExpr = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(timesOp, innerExpr, four));

            // Verify all integers are accessible
            java.util.List<SymjaInteger> integers = outerExpr.getDescendantsStream()
                    .filter(SymjaInteger.class::isInstance)
                    .map(SymjaInteger.class::cast)
                    .toList();

            assertThat(integers).hasSize(3);
            java.util.Set<String> integerValues = integers.stream()
                    .map(i -> i.get(SymjaInteger.VALUE_STRING))
                    .collect(java.util.stream.Collectors.toSet());
            assertThat(integerValues).containsExactlyInAnyOrder("2", "3", "4");
        }

        @Test
        @DisplayName("Should support integer comparison operations")
        void testIntegerComparison_IntegerNodes_WorksCorrectly() {
            SymjaInteger int1 = SymjaNode.newNode(SymjaInteger.class);
            int1.set(SymjaInteger.VALUE_STRING, "42");

            SymjaInteger int2 = SymjaNode.newNode(SymjaInteger.class);
            int2.set(SymjaInteger.VALUE_STRING, "42");

            SymjaInteger int3 = SymjaNode.newNode(SymjaInteger.class);
            int3.set(SymjaInteger.VALUE_STRING, "43");

            // Integers with same value string should have equal values
            assertThat(int1.get(SymjaInteger.VALUE_STRING)).isEqualTo(int2.get(SymjaInteger.VALUE_STRING));
            assertThat(int1.get(SymjaInteger.VALUE_STRING)).isNotEqualTo(int3.get(SymjaInteger.VALUE_STRING));
        }

        @Test
        @DisplayName("Should work with mixed integer and symbol expressions")
        void testMixedIntegerSymbol_Integration_WorksCorrectly() {
            // Create expression: x + 5
            SymjaSymbol x = SymjaNode.newNode(SymjaSymbol.class);
            x.set(SymjaSymbol.SYMBOL, "x");

            SymjaInteger five = SymjaNode.newNode(SymjaInteger.class);
            five.set(SymjaInteger.VALUE_STRING, "5");

            SymjaOperator plusOp = SymjaNode.newNode(SymjaOperator.class);
            plusOp.set(SymjaOperator.OPERATOR, Operator.Plus);

            SymjaFunction expression = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(plusOp, x, five));

            // Verify mixed types work together
            assertThat(expression.getChild(1)).isInstanceOf(SymjaSymbol.class);
            assertThat(expression.getChild(2)).isInstanceOf(SymjaInteger.class);

            SymjaSymbol symbolOperand = (SymjaSymbol) expression.getChild(1);
            SymjaInteger integerOperand = (SymjaInteger) expression.getChild(2);
            assertThat(symbolOperand.get(SymjaSymbol.SYMBOL)).isEqualTo("x");
            assertThat(integerOperand.get(SymjaInteger.VALUE_STRING)).isEqualTo("5");
        }

        @Test
        @DisplayName("Should support large integer values")
        void testLargeIntegerValues_Integration_WorksCorrectly() {
            SymjaInteger largeInt = SymjaNode.newNode(SymjaInteger.class);
            String largeValue = "123456789012345678901234567890";
            largeInt.set(SymjaInteger.VALUE_STRING, largeValue);

            assertThat(largeInt.get(SymjaInteger.VALUE_STRING)).isEqualTo(largeValue);

            // Should work in expressions too
            SymjaInteger smallInt = SymjaNode.newNode(SymjaInteger.class);
            smallInt.set(SymjaInteger.VALUE_STRING, "1");

            SymjaOperator plusOp = SymjaNode.newNode(SymjaOperator.class);
            plusOp.set(SymjaOperator.OPERATOR, Operator.Plus);

            SymjaFunction expression = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(plusOp, largeInt, smallInt));

            SymjaInteger leftOperand = (SymjaInteger) expression.getChild(1);
            assertThat(leftOperand.get(SymjaInteger.VALUE_STRING)).isEqualTo(largeValue);
        }
    }
}
