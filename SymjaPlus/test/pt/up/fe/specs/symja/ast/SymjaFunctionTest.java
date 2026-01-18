package pt.up.fe.specs.symja.ast;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Unit tests for {@link SymjaFunction}.
 * 
 * @author Generated Tests
 */
@DisplayName("SymjaFunction")
class SymjaFunctionTest {

    @Nested
    @DisplayName("Node Creation Tests")
    class NodeCreationTests {

        @Test
        @DisplayName("Should create SymjaFunction with no children")
        void testNewNode_NoChildren_CreatesValidSymjaFunction() {
            SymjaFunction function = SymjaNode.newNode(SymjaFunction.class);

            assertThat(function).isNotNull();
            assertThat(function.getNumChildren()).isEqualTo(0);
            assertThat(function.getChildren()).isEmpty();
        }

        @Test
        @DisplayName("Should create SymjaFunction with single child")
        void testNewNode_SingleChild_CreatesFunctionWithChild() {
            SymjaSymbol child = SymjaNode.newNode(SymjaSymbol.class);
            SymjaFunction function = SymjaNode.newNode(SymjaFunction.class, Arrays.asList(child));

            assertThat(function.getNumChildren()).isEqualTo(1);
            assertThat(function.getChild(0)).isSameAs(child);
        }

        @Test
        @DisplayName("Should create SymjaFunction with multiple children")
        void testNewNode_MultipleChildren_CreatesFunctionWithAllChildren() {
            SymjaOperator operator = SymjaNode.newNode(SymjaOperator.class);
            SymjaSymbol arg1 = SymjaNode.newNode(SymjaSymbol.class);
            SymjaInteger arg2 = SymjaNode.newNode(SymjaInteger.class);

            SymjaFunction function = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(operator, arg1, arg2));

            assertThat(function.getNumChildren()).isEqualTo(3);
            assertThat(function.getChild(0)).isSameAs(operator);
            assertThat(function.getChild(1)).isSameAs(arg1);
            assertThat(function.getChild(2)).isSameAs(arg2);
        }

        @Test
        @DisplayName("Should create SymjaFunction with nested functions")
        void testNewNode_NestedFunctions_CreatesNestedStructure() {
            SymjaFunction innerFunction = SymjaNode.newNode(SymjaFunction.class);
            SymjaFunction outerFunction = SymjaNode.newNode(SymjaFunction.class, Arrays.asList(innerFunction));

            assertThat(outerFunction.getNumChildren()).isEqualTo(1);
            assertThat(outerFunction.getChild(0)).isInstanceOf(SymjaFunction.class);
            assertThat(outerFunction.getChild(0)).isSameAs(innerFunction);
        }
    }

    @Nested
    @DisplayName("Has Parenthesis Property Tests")
    class HasParenthesisPropertyTests {

        @Test
        @DisplayName("Should set and get hasParenthesis property")
        void testHasParenthesisProperty_SetAndGet_WorksCorrectly() {
            SymjaFunction function = SymjaNode.newNode(SymjaFunction.class);
            function.set(SymjaFunction.HAS_PARENTHESIS, true);

            assertThat(function.get(SymjaFunction.HAS_PARENTHESIS)).isTrue();
        }

        @Test
        @DisplayName("Should handle hasParenthesis property updates")
        void testHasParenthesisProperty_Updates_WorksCorrectly() {
            SymjaFunction function = SymjaNode.newNode(SymjaFunction.class);
            function.set(SymjaFunction.HAS_PARENTHESIS, false);
            function.set(SymjaFunction.HAS_PARENTHESIS, true);

            assertThat(function.get(SymjaFunction.HAS_PARENTHESIS)).isTrue();
        }

        @Test
        @DisplayName("Should handle null hasParenthesis property")
        void testHasParenthesisProperty_NullValue_HandlesCorrectly() {
            SymjaFunction function = SymjaNode.newNode(SymjaFunction.class);
            function.set(SymjaFunction.HAS_PARENTHESIS, null);

            assertThat(function.get(SymjaFunction.HAS_PARENTHESIS)).isEqualTo(true);
        }

        @ParameterizedTest
        @ValueSource(booleans = { true, false })
        @DisplayName("Should handle both parenthesis values")
        void testHasParenthesisProperty_BothValues_HandlesCorrectly(Boolean hasParenthesis) {
            SymjaFunction function = SymjaNode.newNode(SymjaFunction.class);
            function.set(SymjaFunction.HAS_PARENTHESIS, hasParenthesis);

            assertThat(function.get(SymjaFunction.HAS_PARENTHESIS)).isEqualTo(hasParenthesis);
        }

        @Test
        @DisplayName("Should have default value for hasParenthesis")
        void testHasParenthesisProperty_DefaultValue_IsTrue() {
            SymjaFunction function = SymjaNode.newNode(SymjaFunction.class);

            // Should have default value of true
            assertThat(function.get(SymjaFunction.HAS_PARENTHESIS)).isTrue();
        }
    }

    @Nested
    @DisplayName("DataKey Tests")
    class DataKeyTests {

        @Test
        @DisplayName("Should have correct HAS_PARENTHESIS DataKey properties")
        void testHasParenthesisDataKey_Properties_AreCorrect() {
            assertThat(SymjaFunction.HAS_PARENTHESIS).isNotNull();
            assertThat(SymjaFunction.HAS_PARENTHESIS.getName()).isEqualTo("hasParenthesis");
        }

        @Test
        @DisplayName("Should retrieve hasParenthesis via DataKey")
        void testHasParenthesisDataKey_Retrieval_WorksCorrectly() {
            SymjaFunction function = SymjaNode.newNode(SymjaFunction.class);
            function.set(SymjaFunction.HAS_PARENTHESIS, true);

            Boolean retrievedValue = function.get(SymjaFunction.HAS_PARENTHESIS);
            assertThat(retrievedValue).isTrue();
        }

        @Test
        @DisplayName("Should handle has() check for hasParenthesis property")
        void testHasParenthesisDataKey_HasCheck_WorksCorrectly() {
            SymjaFunction function = SymjaNode.newNode(SymjaFunction.class);

            assertThat(function.hasValue(SymjaFunction.HAS_PARENTHESIS)).isFalse();

            function.set(SymjaFunction.HAS_PARENTHESIS, false);
            assertThat(function.hasValue(SymjaFunction.HAS_PARENTHESIS)).isTrue();
        }
    }

    @Nested
    @DisplayName("Node Hierarchy Tests")
    class NodeHierarchyTests {

        @Test
        @DisplayName("Should correctly identify as SymjaFunction instance")
        void testInstanceType_SymjaFunction_IdentifiesCorrectly() {
            SymjaFunction function = SymjaNode.newNode(SymjaFunction.class);

            assertThat(function).isInstanceOf(SymjaNode.class);
            assertThat(function).isInstanceOf(SymjaFunction.class);
            assertThat(function).isNotInstanceOf(SymjaSymbol.class);
            assertThat(function).isNotInstanceOf(SymjaInteger.class);
        }

        @Test
        @DisplayName("Should support parent-child relationships")
        void testParentChildRelationships_SymjaFunction_WorksCorrectly() {
            SymjaFunction parent = SymjaNode.newNode(SymjaFunction.class);
            SymjaFunction child = SymjaNode.newNode(SymjaFunction.class);

            parent = SymjaNode.newNode(SymjaFunction.class, Arrays.asList(child));

            assertThat(parent.getNumChildren()).isEqualTo(1);
            assertThat(parent.getChild(0)).isSameAs(child);
            assertThat(child.getParent()).isSameAs(parent);
        }

        @Test
        @DisplayName("Should support mixed node type children")
        void testMixedNodeTypes_AsChildren_WorksCorrectly() {
            SymjaOperator operator = SymjaNode.newNode(SymjaOperator.class);
            SymjaSymbol symbol = SymjaNode.newNode(SymjaSymbol.class);
            SymjaInteger integer = SymjaNode.newNode(SymjaInteger.class);

            SymjaFunction function = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(operator, symbol, integer));

            assertThat(function.getNumChildren()).isEqualTo(3);
            assertThat(function.getChild(0)).isInstanceOf(SymjaOperator.class);
            assertThat(function.getChild(1)).isInstanceOf(SymjaSymbol.class);
            assertThat(function.getChild(2)).isInstanceOf(SymjaInteger.class);
        }
    }

    @Nested
    @DisplayName("Tree Operations Tests")
    class TreeOperationsTests {

        @Test
        @DisplayName("Should support tree traversal operations")
        void testTreeTraversal_SymjaFunction_WorksCorrectly() {
            SymjaFunction root = SymjaNode.newNode(SymjaFunction.class);

            SymjaSymbol child1 = SymjaNode.newNode(SymjaSymbol.class);
            SymjaInteger child2 = SymjaNode.newNode(SymjaInteger.class);

            root = SymjaNode.newNode(SymjaFunction.class, Arrays.asList(child1, child2));
            root.set(SymjaFunction.HAS_PARENTHESIS, true);

            // Test tree structure
            assertThat(root.getDescendants()).hasSize(2);
            assertThat(root.getDescendants()).contains(child1, child2);
        }

        @Test
        @DisplayName("Should support node copying operations")
        void testNodeCopying_SymjaFunction_WorksCorrectly() {
            SymjaFunction original = SymjaNode.newNode(SymjaFunction.class);
            original.set(SymjaFunction.HAS_PARENTHESIS, true);

            SymjaFunction copy = (SymjaFunction) original.copy();

            assertThat(copy).isNotSameAs(original);
            assertThat(copy.get(SymjaFunction.HAS_PARENTHESIS)).isTrue();
            assertThat(copy.getClass()).isEqualTo(SymjaFunction.class);
        }

        @Test
        @DisplayName("Should support deep copying with children")
        void testDeepCopying_WithChildren_WorksCorrectly() {
            SymjaSymbol child = SymjaNode.newNode(SymjaSymbol.class);
            child.set(SymjaSymbol.SYMBOL, "x");

            SymjaFunction parent = SymjaNode.newNode(SymjaFunction.class, Arrays.asList(child));
            parent.set(SymjaFunction.HAS_PARENTHESIS, false);

            SymjaFunction parentCopy = (SymjaFunction) parent.copy();

            assertThat(parentCopy).isNotSameAs(parent);
            assertThat(parentCopy.getNumChildren()).isEqualTo(1);
            assertThat(parentCopy.getChild(0)).isNotSameAs(child);
            assertThat(((SymjaSymbol) parentCopy.getChild(0)).get(SymjaSymbol.SYMBOL)).isEqualTo("x");
            assertThat(parentCopy.get(SymjaFunction.HAS_PARENTHESIS)).isFalse();
        }
    }

    @Nested
    @DisplayName("Mathematical Function Tests")
    class MathematicalFunctionTests {

        @Test
        @DisplayName("Should represent arithmetic operations")
        void testArithmeticOperations_Function_WorksCorrectly() {
            // Create function: Plus(x, 5)
            SymjaOperator plusOp = SymjaNode.newNode(SymjaOperator.class);
            plusOp.set(SymjaOperator.OPERATOR, Operator.Plus);

            SymjaSymbol x = SymjaNode.newNode(SymjaSymbol.class);
            x.set(SymjaSymbol.SYMBOL, "x");

            SymjaInteger five = SymjaNode.newNode(SymjaInteger.class);
            five.set(SymjaInteger.VALUE_STRING, "5");

            SymjaFunction function = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(plusOp, x, five));

            assertThat(function.getNumChildren()).isEqualTo(3);
            assertThat(((SymjaOperator) function.getChild(0)).get(SymjaOperator.OPERATOR)).isEqualTo(Operator.Plus);
            assertThat(((SymjaSymbol) function.getChild(1)).get(SymjaSymbol.SYMBOL)).isEqualTo("x");
            assertThat(((SymjaInteger) function.getChild(2)).get(SymjaInteger.VALUE_STRING)).isEqualTo("5");
        }

        @Test
        @DisplayName("Should handle parenthesis in complex expressions")
        void testParenthesisHandling_ComplexExpressions_WorksCorrectly() {
            // Create expression: (a + b) * c
            SymjaSymbol a = SymjaNode.newNode(SymjaSymbol.class);
            a.set(SymjaSymbol.SYMBOL, "a");

            SymjaSymbol b = SymjaNode.newNode(SymjaSymbol.class);
            b.set(SymjaSymbol.SYMBOL, "b");

            SymjaSymbol c = SymjaNode.newNode(SymjaSymbol.class);
            c.set(SymjaSymbol.SYMBOL, "c");

            SymjaOperator plusOp = SymjaNode.newNode(SymjaOperator.class);
            plusOp.set(SymjaOperator.OPERATOR, Operator.Plus);

            SymjaFunction innerFunction = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(plusOp, a, b));
            innerFunction.set(SymjaFunction.HAS_PARENTHESIS, true); // (a + b)

            SymjaOperator timesOp = SymjaNode.newNode(SymjaOperator.class);
            timesOp.set(SymjaOperator.OPERATOR, Operator.Times);

            SymjaFunction outerFunction = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(timesOp, innerFunction, c));
            outerFunction.set(SymjaFunction.HAS_PARENTHESIS, false);

            // Verify parenthesis settings
            assertThat(innerFunction.get(SymjaFunction.HAS_PARENTHESIS)).isTrue();
            assertThat(outerFunction.get(SymjaFunction.HAS_PARENTHESIS)).isFalse();
        }

        @Test
        @DisplayName("Should represent function calls")
        void testFunctionCalls_Representation_WorksCorrectly() {
            // Create function call: f(x, y, z)
            SymjaSymbol functionName = SymjaNode.newNode(SymjaSymbol.class);
            functionName.set(SymjaSymbol.SYMBOL, "f");

            SymjaSymbol x = SymjaNode.newNode(SymjaSymbol.class);
            x.set(SymjaSymbol.SYMBOL, "x");

            SymjaSymbol y = SymjaNode.newNode(SymjaSymbol.class);
            y.set(SymjaSymbol.SYMBOL, "y");

            SymjaSymbol z = SymjaNode.newNode(SymjaSymbol.class);
            z.set(SymjaSymbol.SYMBOL, "z");

            SymjaFunction functionCall = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(functionName, x, y, z));

            assertThat(functionCall.getNumChildren()).isEqualTo(4);
            assertThat(((SymjaSymbol) functionCall.getChild(0)).get(SymjaSymbol.SYMBOL)).isEqualTo("f");
            assertThat(((SymjaSymbol) functionCall.getChild(1)).get(SymjaSymbol.SYMBOL)).isEqualTo("x");
            assertThat(((SymjaSymbol) functionCall.getChild(2)).get(SymjaSymbol.SYMBOL)).isEqualTo("y");
            assertThat(((SymjaSymbol) functionCall.getChild(3)).get(SymjaSymbol.SYMBOL)).isEqualTo("z");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work correctly in deeply nested expressions")
        void testDeeplyNestedExpressions_Integration_WorksCorrectly() {
            // Create expression: ((a + b) * c) / d
            SymjaSymbol a = SymjaNode.newNode(SymjaSymbol.class);
            a.set(SymjaSymbol.SYMBOL, "a");

            SymjaSymbol b = SymjaNode.newNode(SymjaSymbol.class);
            b.set(SymjaSymbol.SYMBOL, "b");

            SymjaSymbol c = SymjaNode.newNode(SymjaSymbol.class);
            c.set(SymjaSymbol.SYMBOL, "c");

            SymjaSymbol d = SymjaNode.newNode(SymjaSymbol.class);
            d.set(SymjaSymbol.SYMBOL, "d");

            // Build from inside out
            SymjaOperator plusOp = SymjaNode.newNode(SymjaOperator.class);
            plusOp.set(SymjaOperator.OPERATOR, Operator.Plus);

            SymjaFunction innermost = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(plusOp, a, b));
            innermost.set(SymjaFunction.HAS_PARENTHESIS, true);

            SymjaOperator timesOp = SymjaNode.newNode(SymjaOperator.class);
            timesOp.set(SymjaOperator.OPERATOR, Operator.Times);

            SymjaFunction middle = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(timesOp, innermost, c));
            middle.set(SymjaFunction.HAS_PARENTHESIS, true);

            SymjaOperator powerOp = SymjaNode.newNode(SymjaOperator.class);
            powerOp.set(SymjaOperator.OPERATOR, Operator.Power);

            SymjaFunction outermost = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(powerOp, middle, d));

            // Verify structure
            assertThat(outermost.getDescendants()).hasSize(9); // All nested nodes

            // Verify all symbols are accessible
            java.util.List<SymjaSymbol> symbols = outermost.getDescendantsStream()
                    .filter(SymjaSymbol.class::isInstance)
                    .map(SymjaSymbol.class::cast)
                    .toList();

            java.util.Set<String> symbolNames = symbols.stream()
                    .map(s -> s.get(SymjaSymbol.SYMBOL))
                    .collect(java.util.stream.Collectors.toSet());
            assertThat(symbolNames).containsExactlyInAnyOrder("a", "b", "c", "d");
        }

        @Test
        @DisplayName("Should support function composition")
        void testFunctionComposition_Integration_WorksCorrectly() {
            // Create expression: f(g(x))
            SymjaSymbol f = SymjaNode.newNode(SymjaSymbol.class);
            f.set(SymjaSymbol.SYMBOL, "f");

            SymjaSymbol g = SymjaNode.newNode(SymjaSymbol.class);
            g.set(SymjaSymbol.SYMBOL, "g");

            SymjaSymbol x = SymjaNode.newNode(SymjaSymbol.class);
            x.set(SymjaSymbol.SYMBOL, "x");

            // Inner function: g(x)
            SymjaFunction innerFunction = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(g, x));

            // Outer function: f(g(x))
            SymjaFunction outerFunction = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(f, innerFunction));

            assertThat(outerFunction.getNumChildren()).isEqualTo(2);
            assertThat(outerFunction.getChild(0)).isInstanceOf(SymjaSymbol.class);
            assertThat(outerFunction.getChild(1)).isInstanceOf(SymjaFunction.class);

            SymjaFunction inner = (SymjaFunction) outerFunction.getChild(1);
            assertThat(inner.getNumChildren()).isEqualTo(2);
            assertThat(((SymjaSymbol) inner.getChild(0)).get(SymjaSymbol.SYMBOL)).isEqualTo("g");
            assertThat(((SymjaSymbol) inner.getChild(1)).get(SymjaSymbol.SYMBOL)).isEqualTo("x");
        }

        @Test
        @DisplayName("Should handle empty functions gracefully")
        void testEmptyFunctions_Integration_WorksCorrectly() {
            SymjaFunction emptyFunction = SymjaNode.newNode(SymjaFunction.class);
            emptyFunction.set(SymjaFunction.HAS_PARENTHESIS, false);

            assertThat(emptyFunction.getNumChildren()).isEqualTo(0);
            assertThat(emptyFunction.get(SymjaFunction.HAS_PARENTHESIS)).isFalse();
            assertThat(emptyFunction.getDescendants()).isEmpty();
        }

        @Test
        @DisplayName("Should work correctly with all node types as children")
        void testAllNodeTypes_AsChildren_WorksCorrectly() {
            // Create a function containing all types of nodes
            SymjaOperator operator = SymjaNode.newNode(SymjaOperator.class);
            operator.set(SymjaOperator.OPERATOR, Operator.Plus);

            SymjaSymbol symbol = SymjaNode.newNode(SymjaSymbol.class);
            symbol.set(SymjaSymbol.SYMBOL, "variable");

            SymjaInteger integer = SymjaNode.newNode(SymjaInteger.class);
            integer.set(SymjaInteger.VALUE_STRING, "42");

            SymjaFunction nestedFunction = SymjaNode.newNode(SymjaFunction.class);
            nestedFunction.set(SymjaFunction.HAS_PARENTHESIS, true);

            SymjaFunction mainFunction = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(operator, symbol, integer, nestedFunction));

            assertThat(mainFunction.getNumChildren()).isEqualTo(4);
            assertThat(mainFunction.getChild(0)).isInstanceOf(SymjaOperator.class);
            assertThat(mainFunction.getChild(1)).isInstanceOf(SymjaSymbol.class);
            assertThat(mainFunction.getChild(2)).isInstanceOf(SymjaInteger.class);
            assertThat(mainFunction.getChild(3)).isInstanceOf(SymjaFunction.class);
        }
    }
}
