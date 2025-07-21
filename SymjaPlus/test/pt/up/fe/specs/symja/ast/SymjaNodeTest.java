package pt.up.fe.specs.symja.ast;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Unit tests for {@link SymjaNode}.
 * 
 * @author Generated Tests
 */
@DisplayName("SymjaNode")
class SymjaNodeTest {

    @Nested
    @DisplayName("Node Creation Tests")
    class NodeCreationTests {

        @Test
        @DisplayName("Should create node with no children using newNode factory method")
        void testNewNode_NoChildren_CreatesValidNode() {
            SymjaNode node = SymjaNode.newNode(SymjaNode.class);

            assertThat(node).isNotNull();
            assertThat(node.getNumChildren()).isEqualTo(0);
            assertThat(node.getChildren()).isEmpty();
        }

        @Test
        @DisplayName("Should create node with children using newNode factory method")
        void testNewNode_WithChildren_CreatesValidNodeWithChildren() {
            SymjaNode child1 = SymjaNode.newNode(SymjaSymbol.class);
            SymjaNode child2 = SymjaNode.newNode(SymjaInteger.class);
            var children = Arrays.asList(child1, child2);

            SymjaNode parent = SymjaNode.newNode(SymjaFunction.class, children);

            assertThat(parent).isNotNull();
            assertThat(parent.getNumChildren()).isEqualTo(2);
            assertThat(parent.getChildren()).containsExactly(child1, child2);
            assertThat(parent.getChild(0)).isEqualTo(child1);
            assertThat(parent.getChild(1)).isEqualTo(child2);
        }

        @Test
        @DisplayName("Should create node with empty children collection")
        void testNewNode_EmptyChildren_CreatesValidNode() {
            SymjaNode node = SymjaNode.newNode(SymjaNode.class, Collections.emptyList());

            assertThat(node).isNotNull();
            assertThat(node.getNumChildren()).isEqualTo(0);
            assertThat(node.getChildren()).isEmpty();
        }

        @Test
        @DisplayName("Should handle null class parameter gracefully")
        void testNewNode_NullClass_ThrowsException() {
            assertThatThrownBy(() -> SymjaNode.newNode(null))
                    .isInstanceOf(Exception.class);
        }

        @Test
        @DisplayName("Should handle null children collection gracefully")
        void testNewNode_NullChildren_ThrowsException() {
            assertThatThrownBy(() -> SymjaNode.newNode(SymjaNode.class, null))
                    .isInstanceOf(Exception.class);
        }
    }

    @Nested
    @DisplayName("Node Type Tests")
    class NodeTypeTests {

        @Test
        @DisplayName("Should create specific node types correctly")
        void testNewNode_SpecificTypes_CreatesCorrectTypes() {
            SymjaSymbol symbolNode = SymjaNode.newNode(SymjaSymbol.class);
            SymjaInteger integerNode = SymjaNode.newNode(SymjaInteger.class);
            SymjaFunction functionNode = SymjaNode.newNode(SymjaFunction.class);
            SymjaOperator operatorNode = SymjaNode.newNode(SymjaOperator.class);

            assertThat(symbolNode).isInstanceOf(SymjaSymbol.class);
            assertThat(integerNode).isInstanceOf(SymjaInteger.class);
            assertThat(functionNode).isInstanceOf(SymjaFunction.class);
            assertThat(operatorNode).isInstanceOf(SymjaOperator.class);
        }

        @Test
        @DisplayName("Should maintain inheritance hierarchy")
        void testNodeTypes_Inheritance_MaintainsHierarchy() {
            SymjaSymbol symbolNode = SymjaNode.newNode(SymjaSymbol.class);
            SymjaInteger integerNode = SymjaNode.newNode(SymjaInteger.class);
            SymjaFunction functionNode = SymjaNode.newNode(SymjaFunction.class);

            assertThat(symbolNode).isInstanceOf(SymjaNode.class);
            assertThat(integerNode).isInstanceOf(SymjaNode.class);
            assertThat(functionNode).isInstanceOf(SymjaNode.class);
        }
    }

    @Nested
    @DisplayName("Base Class and Type Methods")
    class BaseClassAndTypeTests {

        @Test
        @DisplayName("Should return correct base class")
        void testGetBaseClass_ReturnsCorrectClass() {
            SymjaNode node = SymjaNode.newNode(SymjaNode.class);

            Class<SymjaNode> baseClass = node.getBaseClass();

            assertThat(baseClass).isEqualTo(SymjaNode.class);
        }

        @Test
        @DisplayName("Should return this instance correctly")
        void testGetThis_ReturnsThisInstance() {
            SymjaNode node = SymjaNode.newNode(SymjaNode.class);

            SymjaNode thisInstance = node.getThis();

            assertThat(thisInstance).isSameAs(node);
        }
    }

    @Nested
    @DisplayName("Tree Structure Tests")
    class TreeStructureTests {

        @Test
        @DisplayName("Should handle complex tree structures")
        void testComplexTreeStructure_CreatesCorrectHierarchy() {
            // Create leaf nodes
            SymjaSymbol symbolX = SymjaNode.newNode(SymjaSymbol.class);
            symbolX.set(SymjaSymbol.SYMBOL, "x");

            SymjaInteger integer2 = SymjaNode.newNode(SymjaInteger.class);
            integer2.set(SymjaInteger.VALUE_STRING, "2");

            // Create operator
            SymjaOperator plusOp = SymjaNode.newNode(SymjaOperator.class);
            plusOp.set(SymjaOperator.OPERATOR, Operator.Plus);

            // Create function node (x + 2)
            SymjaFunction addFunction = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(plusOp, symbolX, integer2));

            assertThat(addFunction.getNumChildren()).isEqualTo(3);
            assertThat(addFunction.getChild(0)).isInstanceOf(SymjaOperator.class);
            assertThat(addFunction.getChild(1)).isInstanceOf(SymjaSymbol.class);
            assertThat(addFunction.getChild(2)).isInstanceOf(SymjaInteger.class);
        }

        @Test
        @DisplayName("Should handle nested function structures")
        void testNestedFunctionStructure_CreatesCorrectNesting() {
            // Create inner function: x + 1
            SymjaSymbol symbolX = SymjaNode.newNode(SymjaSymbol.class);
            symbolX.set(SymjaSymbol.SYMBOL, "x");

            SymjaInteger integer1 = SymjaNode.newNode(SymjaInteger.class);
            integer1.set(SymjaInteger.VALUE_STRING, "1");

            SymjaOperator plusOp = SymjaNode.newNode(SymjaOperator.class);
            plusOp.set(SymjaOperator.OPERATOR, Operator.Plus);

            SymjaFunction innerFunction = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(plusOp, symbolX, integer1));

            // Create outer function: (x + 1) * 2
            SymjaInteger integer2 = SymjaNode.newNode(SymjaInteger.class);
            integer2.set(SymjaInteger.VALUE_STRING, "2");

            SymjaOperator timesOp = SymjaNode.newNode(SymjaOperator.class);
            timesOp.set(SymjaOperator.OPERATOR, Operator.Times);

            SymjaFunction outerFunction = SymjaNode.newNode(SymjaFunction.class,
                    Arrays.asList(timesOp, innerFunction, integer2));

            assertThat(outerFunction.getNumChildren()).isEqualTo(3);
            assertThat(outerFunction.getChild(1)).isEqualTo(innerFunction);
            assertThat(innerFunction.getNumChildren()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle creating many child nodes")
        void testManyChildren_HandlesLargeNumberOfChildren() {
            var children = Arrays.asList(
                    SymjaNode.newNode(SymjaSymbol.class),
                    SymjaNode.newNode(SymjaInteger.class),
                    SymjaNode.newNode(SymjaOperator.class),
                    SymjaNode.newNode(SymjaSymbol.class),
                    SymjaNode.newNode(SymjaInteger.class));

            SymjaNode parent = SymjaNode.newNode(SymjaFunction.class, children);

            assertThat(parent.getNumChildren()).isEqualTo(5);
            assertThat(parent.getChildren()).hasSize(5);
        }

        @Test
        @DisplayName("Should maintain node consistency after creation")
        void testNodeConsistency_MaintainsConsistentState() {
            SymjaSymbol symbol = SymjaNode.newNode(SymjaSymbol.class);
            symbol.set(SymjaSymbol.SYMBOL, "test");

            assertThat(symbol.get(SymjaSymbol.SYMBOL)).isEqualTo("test");
            assertThat(symbol).isInstanceOf(SymjaSymbol.class);
            assertThat(symbol).isInstanceOf(SymjaNode.class);
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @ParameterizedTest
        @ValueSource(ints = { 10, 50, 100 })
        @DisplayName("Should create nodes efficiently at different scales")
        void testNodeCreation_Performance_HandlesMultipleCreations(int nodeCount) {
            long startTime = System.nanoTime();

            for (int i = 0; i < nodeCount; i++) {
                SymjaNode node = SymjaNode.newNode(SymjaNode.class);
                assertThat(node).isNotNull();
            }

            long endTime = System.nanoTime();
            long durationMs = (endTime - startTime) / 1_000_000;

            // Should complete within reasonable time (less than 1 second for 100 nodes)
            assertThat(durationMs).isLessThan(1000);
        }
    }
}
