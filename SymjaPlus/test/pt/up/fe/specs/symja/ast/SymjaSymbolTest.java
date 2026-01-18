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
 * Unit tests for {@link SymjaSymbol}.
 * 
 * @author Generated Tests
 */
@DisplayName("SymjaSymbol")
class SymjaSymbolTest {

    @Nested
    @DisplayName("Node Creation Tests")
    class NodeCreationTests {

        @Test
        @DisplayName("Should create SymjaSymbol with no children")
        void testNewNode_NoChildren_CreatesValidSymjaSymbol() {
            SymjaSymbol symbol = SymjaNode.newNode(SymjaSymbol.class);

            assertThat(symbol).isNotNull();
            assertThat(symbol.getNumChildren()).isEqualTo(0);
            assertThat(symbol.getChildren()).isEmpty();
        }

        @Test
        @DisplayName("Should create SymjaSymbol with children")
        void testNewNode_WithChildren_CreatesSymbolWithChildren() {
            SymjaSymbol child = SymjaNode.newNode(SymjaSymbol.class);
            SymjaSymbol symbol = SymjaNode.newNode(SymjaSymbol.class, Arrays.asList(child));

            assertThat(symbol.getNumChildren()).isEqualTo(1);
            assertThat(symbol.getChild(0)).isSameAs(child);
        }

        @Test
        @DisplayName("Should create SymjaSymbol with multiple children")
        void testNewNode_MultipleChildren_CreatesSymbolWithAllChildren() {
            SymjaSymbol child1 = SymjaNode.newNode(SymjaSymbol.class);
            SymjaSymbol child2 = SymjaNode.newNode(SymjaSymbol.class);
            SymjaSymbol symbol = SymjaNode.newNode(SymjaSymbol.class, Arrays.asList(child1, child2));

            assertThat(symbol.getNumChildren()).isEqualTo(2);
            assertThat(symbol.getChild(0)).isSameAs(child1);
            assertThat(symbol.getChild(1)).isSameAs(child2);
        }
    }

    @Nested
    @DisplayName("Symbol Property Tests")
    class SymbolPropertyTests {

        @Test
        @DisplayName("Should set and get symbol string")
        void testSymbolProperty_SetAndGet_WorksCorrectly() {
            SymjaSymbol symbol = SymjaNode.newNode(SymjaSymbol.class);
            symbol.set(SymjaSymbol.SYMBOL, "myVariable");

            assertThat(symbol.get(SymjaSymbol.SYMBOL)).isEqualTo("myVariable");
        }

        @Test
        @DisplayName("Should handle symbol string updates")
        void testSymbolProperty_Updates_WorksCorrectly() {
            SymjaSymbol symbol = SymjaNode.newNode(SymjaSymbol.class);
            symbol.set(SymjaSymbol.SYMBOL, "initialValue");
            symbol.set(SymjaSymbol.SYMBOL, "updatedValue");

            assertThat(symbol.get(SymjaSymbol.SYMBOL)).isEqualTo("updatedValue");
        }

        @Test
        @DisplayName("Should handle null symbol string")
        void testSymbolProperty_NullValue_HandlesCorrectly() {
            SymjaSymbol symbol = SymjaNode.newNode(SymjaSymbol.class);
            symbol.set(SymjaSymbol.SYMBOL, null);

            assertThat(symbol.get(SymjaSymbol.SYMBOL)).isEqualTo("");
        }

        @ParameterizedTest
        @ValueSource(strings = { "x", "variable", "myVar123", "a_b_c", "CONSTANT", "_underscore", "π", "αβγ" })
        @DisplayName("Should handle various valid symbol names")
        void testSymbolProperty_VariousValidNames_HandlesCorrectly(String symbolName) {
            SymjaSymbol symbol = SymjaNode.newNode(SymjaSymbol.class);
            symbol.set(SymjaSymbol.SYMBOL, symbolName);

            assertThat(symbol.get(SymjaSymbol.SYMBOL)).isEqualTo(symbolName);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = { " ", "   " })
        @DisplayName("Should handle edge case symbol values")
        void testSymbolProperty_EdgeCases_HandlesCorrectly(String symbolName) {
            SymjaSymbol symbol = SymjaNode.newNode(SymjaSymbol.class);
            symbol.set(SymjaSymbol.SYMBOL, symbolName);

            String expected = (symbolName == null) ? "" : symbolName;
            assertThat(symbol.get(SymjaSymbol.SYMBOL)).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("DataKey Tests")
    class DataKeyTests {

        @Test
        @DisplayName("Should have correct SYMBOL DataKey properties")
        void testSymbolDataKey_Properties_AreCorrect() {
            assertThat(SymjaSymbol.SYMBOL).isNotNull();
            assertThat(SymjaSymbol.SYMBOL.getName()).isEqualTo("symbol");
        }

        @Test
        @DisplayName("Should retrieve symbol via DataKey")
        void testSymbolDataKey_Retrieval_WorksCorrectly() {
            SymjaSymbol symbol = SymjaNode.newNode(SymjaSymbol.class);
            symbol.set(SymjaSymbol.SYMBOL, "testSymbol");

            String retrievedSymbol = symbol.get(SymjaSymbol.SYMBOL);
            assertThat(retrievedSymbol).isEqualTo("testSymbol");
        }

        @Test
        @DisplayName("Should handle has() check for symbol property")
        void testSymbolDataKey_HasCheck_WorksCorrectly() {
            SymjaSymbol symbol = SymjaNode.newNode(SymjaSymbol.class);

            // Initially should not have the property set
            assertThat(symbol.hasValue(SymjaSymbol.SYMBOL)).isFalse();

            symbol.set(SymjaSymbol.SYMBOL, "value");
            assertThat(symbol.hasValue(SymjaSymbol.SYMBOL)).isTrue();
        }
    }

    @Nested
    @DisplayName("Node Hierarchy Tests")
    class NodeHierarchyTests {

        @Test
        @DisplayName("Should correctly identify as SymjaSymbol instance")
        void testInstanceType_SymjaSymbol_IdentifiesCorrectly() {
            SymjaSymbol symbol = SymjaNode.newNode(SymjaSymbol.class);

            assertThat(symbol).isInstanceOf(SymjaNode.class);
            assertThat(symbol).isInstanceOf(SymjaSymbol.class);
            assertThat(symbol).isNotInstanceOf(SymjaInteger.class);
            assertThat(symbol).isNotInstanceOf(SymjaFunction.class);
        }

        @Test
        @DisplayName("Should support parent-child relationships")
        void testParentChildRelationships_SymjaSymbol_WorksCorrectly() {
            SymjaSymbol parent = SymjaNode.newNode(SymjaSymbol.class);
            SymjaSymbol child = SymjaNode.newNode(SymjaSymbol.class);

            parent = SymjaNode.newNode(SymjaSymbol.class, Arrays.asList(child));

            assertThat(parent.getNumChildren()).isEqualTo(1);
            assertThat(parent.getChild(0)).isSameAs(child);
            assertThat(child.getParent()).isSameAs(parent);
        }

        @Test
        @DisplayName("Should support mixed node type children")
        void testMixedNodeTypes_AsChildren_WorksCorrectly() {
            SymjaSymbol symbol = SymjaNode.newNode(SymjaSymbol.class);
            SymjaInteger integer = SymjaNode.newNode(SymjaInteger.class);
            SymjaFunction function = SymjaNode.newNode(SymjaFunction.class, Arrays.asList(symbol, integer));

            assertThat(function.getNumChildren()).isEqualTo(2);
            assertThat(function.getChild(0)).isInstanceOf(SymjaSymbol.class);
            assertThat(function.getChild(1)).isInstanceOf(SymjaInteger.class);
        }
    }

    @Nested
    @DisplayName("Tree Operations Tests")
    class TreeOperationsTests {

        @Test
        @DisplayName("Should support tree traversal operations")
        void testTreeTraversal_SymjaSymbol_WorksCorrectly() {
            SymjaSymbol root = SymjaNode.newNode(SymjaSymbol.class);
            root.set(SymjaSymbol.SYMBOL, "root");

            SymjaSymbol child1 = SymjaNode.newNode(SymjaSymbol.class);
            child1.set(SymjaSymbol.SYMBOL, "child1");

            SymjaSymbol child2 = SymjaNode.newNode(SymjaSymbol.class);
            child2.set(SymjaSymbol.SYMBOL, "child2");

            root = SymjaNode.newNode(SymjaSymbol.class, Arrays.asList(child1, child2));
            root.set(SymjaSymbol.SYMBOL, "root");

            // Test tree structure
            assertThat(root.getDescendants()).hasSize(2);
            assertThat(root.getDescendants()).contains(child1, child2);
        }

        @Test
        @DisplayName("Should support node copying operations")
        void testNodeCopying_SymjaSymbol_WorksCorrectly() {
            SymjaSymbol original = SymjaNode.newNode(SymjaSymbol.class);
            original.set(SymjaSymbol.SYMBOL, "originalSymbol");

            SymjaSymbol copy = (SymjaSymbol) original.copy();

            assertThat(copy).isNotSameAs(original);
            assertThat(copy.get(SymjaSymbol.SYMBOL)).isEqualTo("originalSymbol");
            assertThat(copy.getClass()).isEqualTo(SymjaSymbol.class);
        }

        @Test
        @DisplayName("Should support deep copying with children")
        void testDeepCopying_WithChildren_WorksCorrectly() {
            SymjaSymbol child = SymjaNode.newNode(SymjaSymbol.class);
            child.set(SymjaSymbol.SYMBOL, "child");

            SymjaSymbol parent = SymjaNode.newNode(SymjaSymbol.class, Arrays.asList(child));
            parent.set(SymjaSymbol.SYMBOL, "parent");

            SymjaSymbol parentCopy = (SymjaSymbol) parent.copy();

            assertThat(parentCopy).isNotSameAs(parent);
            assertThat(parentCopy.getNumChildren()).isEqualTo(1);
            assertThat(parentCopy.getChild(0)).isNotSameAs(child);
            assertThat(parentCopy.getChild(0).get(SymjaSymbol.SYMBOL)).isEqualTo("child");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work correctly in mathematical expressions")
        void testMathematicalExpressions_Integration_WorksCorrectly() {
            // Create a simple expression: x + y
            SymjaSymbol x = SymjaNode.newNode(SymjaSymbol.class);
            x.set(SymjaSymbol.SYMBOL, "x");

            SymjaSymbol y = SymjaNode.newNode(SymjaSymbol.class);
            y.set(SymjaSymbol.SYMBOL, "y");

            SymjaOperator plusOp = SymjaNode.newNode(SymjaOperator.class);
            plusOp.set(SymjaOperator.OPERATOR, Operator.Plus);

            SymjaFunction expression = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(plusOp, x, y));

            // Verify structure
            assertThat(expression.getNumChildren()).isEqualTo(3);
            assertThat(expression.getChild(0)).isInstanceOf(SymjaOperator.class);
            assertThat(expression.getChild(1)).isInstanceOf(SymjaSymbol.class);
            assertThat(expression.getChild(2)).isInstanceOf(SymjaSymbol.class);

            SymjaSymbol leftOperand = (SymjaSymbol) expression.getChild(1);
            SymjaSymbol rightOperand = (SymjaSymbol) expression.getChild(2);
            assertThat(leftOperand.get(SymjaSymbol.SYMBOL)).isEqualTo("x");
            assertThat(rightOperand.get(SymjaSymbol.SYMBOL)).isEqualTo("y");
        }

        @Test
        @DisplayName("Should support symbol comparison operations")
        void testSymbolComparison_SymbolNodes_WorksCorrectly() {
            SymjaSymbol symbol1 = SymjaNode.newNode(SymjaSymbol.class);
            symbol1.set(SymjaSymbol.SYMBOL, "variable");

            SymjaSymbol symbol2 = SymjaNode.newNode(SymjaSymbol.class);
            symbol2.set(SymjaSymbol.SYMBOL, "variable");

            SymjaSymbol symbol3 = SymjaNode.newNode(SymjaSymbol.class);
            symbol3.set(SymjaSymbol.SYMBOL, "different");

            // Symbols with same name should have equal symbol values
            assertThat(symbol1.get(SymjaSymbol.SYMBOL)).isEqualTo(symbol2.get(SymjaSymbol.SYMBOL));
            assertThat(symbol1.get(SymjaSymbol.SYMBOL)).isNotEqualTo(symbol3.get(SymjaSymbol.SYMBOL));
        }

        @Test
        @DisplayName("Should work with complex nested structures")
        void testComplexNestedStructures_Integration_WorksCorrectly() {
            // Create nested expression with symbols at various levels
            SymjaSymbol a = SymjaNode.newNode(SymjaSymbol.class);
            a.set(SymjaSymbol.SYMBOL, "a");

            SymjaSymbol b = SymjaNode.newNode(SymjaSymbol.class);
            b.set(SymjaSymbol.SYMBOL, "b");

            SymjaSymbol c = SymjaNode.newNode(SymjaSymbol.class);
            c.set(SymjaSymbol.SYMBOL, "c");

            // Create expression: (a + b) * c
            SymjaOperator plusOp = SymjaNode.newNode(SymjaOperator.class);
            plusOp.set(SymjaOperator.OPERATOR, Operator.Plus);

            SymjaFunction innerExpr = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(plusOp, a, b));

            SymjaOperator timesOp = SymjaNode.newNode(SymjaOperator.class);
            timesOp.set(SymjaOperator.OPERATOR, Operator.Times);

            SymjaFunction outerExpr = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(timesOp, innerExpr, c));

            // Verify all symbols are accessible
            java.util.List<SymjaSymbol> symbols = outerExpr.getDescendantsStream()
                    .filter(SymjaSymbol.class::isInstance)
                    .map(SymjaSymbol.class::cast)
                    .toList();

            assertThat(symbols).hasSize(3);
            java.util.Set<String> symbolNames = symbols.stream()
                    .map(s -> s.get(SymjaSymbol.SYMBOL))
                    .collect(java.util.stream.Collectors.toSet());
            assertThat(symbolNames).containsExactlyInAnyOrder("a", "b", "c");
        }
    }
}
