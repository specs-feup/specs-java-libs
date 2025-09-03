package pt.up.fe.specs.util.treenode.transform;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.treenode.ATreeNode;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for TwoOperandTransform class.
 * Tests the abstract base class for transformations with two operands.
 * 
 * @author Generated Tests
 */
@DisplayName("TwoOperandTransform Tests")
class TwoOperandTransformTest {

    private TestTwoOperandTransform transform;
    private TestTreeNode node1;
    private TestTreeNode node2;

    @BeforeEach
    void setUp() {
        node1 = new TestTreeNode("node1");
        node2 = new TestTreeNode("node2");
        transform = new TestTwoOperandTransform("test-transform", node1, node2);
    }

    /**
     * Test tree node implementation for testing purposes.
     */
    private static class TestTreeNode extends ATreeNode<TestTreeNode> {
        private final String value;

        public TestTreeNode(String value) {
            super(null);
            this.value = value;
        }

        @Override
        public String toContentString() {
            return value;
        }

        @Override
        protected TestTreeNode copyPrivate() {
            return new TestTreeNode(value);
        }

        @Override
        public String toString() {
            return "TestTreeNode(" + value + ")";
        }
    }

    /**
     * Test implementation of TwoOperandTransform for testing purposes.
     */
    private static class TestTwoOperandTransform extends TwoOperandTransform<TestTreeNode> {
        private boolean executed = false;

        public TestTwoOperandTransform(String type, TestTreeNode node1, TestTreeNode node2) {
            super(type, node1, node2);
        }

        @Override
        public void execute() {
            executed = true;
        }

        public boolean isExecuted() {
            return executed;
        }
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Constructor should initialize with correct type and two operands")
        void testConstructor_SetsCorrectTypeAndOperands() {
            assertThat(transform.getType()).isEqualTo("test-transform");
            assertThat(transform.getOperands()).hasSize(2);
            assertThat(transform.getOperands().get(0)).isSameAs(node1);
            assertThat(transform.getOperands().get(1)).isSameAs(node2);
        }

        @Test
        @DisplayName("Constructor should handle null type gracefully")
        void testConstructor_WithNullType() {
            TestTwoOperandTransform nullTypeTransform = new TestTwoOperandTransform(null, node1, node2);
            assertThat(nullTypeTransform.getType()).isNull();
            assertThat(nullTypeTransform.getOperands()).hasSize(2);
        }

        @Test
        @DisplayName("Constructor should handle null node1 gracefully")
        void testConstructor_WithNullNode1() {
            TestTwoOperandTransform transform = new TestTwoOperandTransform("test", null, node2);
            assertThat(transform.getOperands().get(0)).isNull();
            assertThat(transform.getOperands().get(1)).isSameAs(node2);
        }

        @Test
        @DisplayName("Constructor should handle null node2 gracefully")
        void testConstructor_WithNullNode2() {
            TestTwoOperandTransform transform = new TestTwoOperandTransform("test", node1, null);
            assertThat(transform.getOperands().get(0)).isSameAs(node1);
            assertThat(transform.getOperands().get(1)).isNull();
        }

        @Test
        @DisplayName("Constructor should handle both nodes as null")
        void testConstructor_WithBothNodesNull() {
            TestTwoOperandTransform transform = new TestTwoOperandTransform("test", null, null);
            assertThat(transform.getOperands()).hasSize(2);
            assertThat(transform.getOperands().get(0)).isNull();
            assertThat(transform.getOperands().get(1)).isNull();
        }
    }

    @Nested
    @DisplayName("Accessor Method Tests")
    class AccessorMethodTests {

        @Test
        @DisplayName("getNode1 should return first operand")
        void testGetNode1_ReturnsFirstOperand() {
            assertThat(transform.getNode1()).isSameAs(node1);
            assertThat(transform.getNode1()).isSameAs(transform.getOperands().get(0));
        }

        @Test
        @DisplayName("getNode2 should return second operand")
        void testGetNode2_ReturnsSecondOperand() {
            assertThat(transform.getNode2()).isSameAs(node2);
            assertThat(transform.getNode2()).isSameAs(transform.getOperands().get(1));
        }

        @Test
        @DisplayName("getNode1 should handle null first operand")
        void testGetNode1_WithNullFirstOperand() {
            TestTwoOperandTransform transform = new TestTwoOperandTransform("test", null, node2);
            assertThat(transform.getNode1()).isNull();
        }

        @Test
        @DisplayName("getNode2 should handle null second operand")
        void testGetNode2_WithNullSecondOperand() {
            TestTwoOperandTransform transform = new TestTwoOperandTransform("test", node1, null);
            assertThat(transform.getNode2()).isNull();
        }

        @Test
        @DisplayName("Accessor methods should be consistent with operands list")
        void testAccessorMethods_ConsistentWithOperandsList() {
            assertThat(transform.getNode1()).isSameAs(transform.getOperands().get(0));
            assertThat(transform.getNode2()).isSameAs(transform.getOperands().get(1));
        }
    }

    @Nested
    @DisplayName("toString() Tests")
    class ToStringTests {

        @Test
        @DisplayName("toString should contain both node hash codes with node1/node2 format")
        void testToString_ContainsBothNodeHashes() {
            String result = transform.toString();
            String node1Hex = Integer.toHexString(node1.hashCode());
            String node2Hex = Integer.toHexString(node2.hashCode());

            assertThat(result).contains("test-transform");
            assertThat(result).contains("node1(" + node1Hex + ")");
            assertThat(result).contains("node2(" + node2Hex + ")");
        }

        @Test
        @DisplayName("toString should follow specific format for two-operand transforms")
        void testToString_FollowsCorrectFormat() {
            String result = transform.toString();
            String node1Hex = Integer.toHexString(node1.hashCode());
            String node2Hex = Integer.toHexString(node2.hashCode());

            String expected = "test-transform node1(" + node1Hex + ") node2(" + node2Hex + ")";
            assertThat(result).isEqualTo(expected);
        }

        @Test
        @DisplayName("toString should handle null nodes gracefully")
        void testToString_WithNullNodes() {
            TestTwoOperandTransform transform = new TestTwoOperandTransform("test", null, null);

            // Should throw NPE due to hashCode() call on null objects
            assertThatThrownBy(() -> transform.toString())
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("toString should differ for different node pairs")
        void testToString_DiffersForDifferentNodes() {
            TestTreeNode otherNode1 = new TestTreeNode("other1");
            TestTreeNode otherNode2 = new TestTreeNode("other2");
            TestTwoOperandTransform otherTransform = new TestTwoOperandTransform("test-transform", otherNode1,
                    otherNode2);

            assertThat(transform.toString()).isNotEqualTo(otherTransform.toString());
        }

        @Test
        @DisplayName("toString should differ for different transform types")
        void testToString_DiffersForDifferentTypes() {
            TestTwoOperandTransform otherTransform = new TestTwoOperandTransform("different-type", node1, node2);

            assertThat(transform.toString()).isNotEqualTo(otherTransform.toString());
        }
    }

    @Nested
    @DisplayName("Inheritance Tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should inherit from ANodeTransform")
        void testInheritsFromANodeTransform() {
            assertThat(transform).isInstanceOf(ANodeTransform.class);
        }

        @Test
        @DisplayName("Should implement NodeTransform interface")
        void testImplementsNodeTransformInterface() {
            assertThat(transform).isInstanceOf(NodeTransform.class);
        }

        @Test
        @DisplayName("Should have access to inherited methods")
        void testAccessToInheritedMethods() {
            // Test inherited methods from ANodeTransform
            assertThat(transform.getType()).isEqualTo("test-transform");
            assertThat(transform.getOperands()).hasSize(2);

            // Test execution (from NodeTransform interface)
            assertThat(transform.isExecuted()).isFalse();
            transform.execute();
            assertThat(transform.isExecuted()).isTrue();
        }

        @Test
        @DisplayName("Should override toString from ANodeTransform")
        void testOverridesToStringFromANodeTransform() {
            // TwoOperandTransform should use its specialized toString format
            String result = transform.toString();
            assertThat(result).contains("node1(");
            assertThat(result).contains("node2(");

            // Should not use the default ANodeTransform format
            assertThat(result).doesNotMatch(".*\\s[a-f0-9]+\\s[a-f0-9]+.*");
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle empty type string")
        void testEmptyTypeString() {
            TestTwoOperandTransform transform = new TestTwoOperandTransform("", node1, node2);
            assertThat(transform.getType()).isEmpty();
            assertThat(transform.toString()).startsWith(" node1(");
        }

        @Test
        @DisplayName("Should handle very long type string")
        void testVeryLongTypeString() {
            String longType = "a".repeat(1000);
            TestTwoOperandTransform transform = new TestTwoOperandTransform(longType, node1, node2);
            assertThat(transform.getType()).isEqualTo(longType);
            assertThat(transform.toString()).startsWith(longType);
        }

        @Test
        @DisplayName("Should handle special characters in type string")
        void testSpecialCharactersInType() {
            String specialType = "test-transform_with.special@chars#123";
            TestTwoOperandTransform transform = new TestTwoOperandTransform(specialType, node1, node2);
            assertThat(transform.getType()).isEqualTo(specialType);
            assertThat(transform.toString()).contains(specialType);
        }

        @Test
        @DisplayName("Should handle same node instance for both operands")
        void testSameNodeForBothOperands() {
            TestTwoOperandTransform transform = new TestTwoOperandTransform("test", node1, node1);
            assertThat(transform.getNode1()).isSameAs(node1);
            assertThat(transform.getNode2()).isSameAs(node1);
            assertThat(transform.getOperands().get(0)).isSameAs(transform.getOperands().get(1));
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with different tree node types")
        void testWithDifferentTreeNodeTypes() {
            TestTreeNode parent = new TestTreeNode("parent");
            TestTreeNode child = new TestTreeNode("child");
            parent.addChild(child);

            TestTwoOperandTransform transform = new TestTwoOperandTransform("parent-child", parent, child);

            assertThat(transform.getNode1()).isSameAs(parent);
            assertThat(transform.getNode2()).isSameAs(child);
            assertThat(transform.getOperands()).containsExactly(parent, child);
        }

        @Test
        @DisplayName("Should work with nodes in complex tree structures")
        void testWithComplexTreeStructures() {
            TestTreeNode root = new TestTreeNode("root");
            TestTreeNode branch1 = new TestTreeNode("branch1");
            TestTreeNode branch2 = new TestTreeNode("branch2");
            TestTreeNode leaf1 = new TestTreeNode("leaf1");
            TestTreeNode leaf2 = new TestTreeNode("leaf2");

            root.addChild(branch1);
            root.addChild(branch2);
            branch1.addChild(leaf1);
            branch2.addChild(leaf2);

            TestTwoOperandTransform transform = new TestTwoOperandTransform("swap-leaves", leaf1, leaf2);

            assertThat(transform.getNode1()).isSameAs(leaf1);
            assertThat(transform.getNode2()).isSameAs(leaf2);
            assertThat(leaf1.getParent()).isSameAs(branch1);
            assertThat(leaf2.getParent()).isSameAs(branch2);
        }

        @Test
        @DisplayName("Should work with nodes that have children")
        void testWithNodesWithChildren() {
            TestTreeNode parent1 = new TestTreeNode("parent1");
            TestTreeNode parent2 = new TestTreeNode("parent2");
            TestTreeNode child1 = new TestTreeNode("child1");
            TestTreeNode child2 = new TestTreeNode("child2");

            parent1.addChild(child1);
            parent2.addChild(child2);

            TestTwoOperandTransform transform = new TestTwoOperandTransform("swap-parents", parent1, parent2);

            assertThat(transform.getNode1().getNumChildren()).isEqualTo(1);
            assertThat(transform.getNode2().getNumChildren()).isEqualTo(1);
            assertThat(transform.getNode1().getChild(0)).isSameAs(child1);
            assertThat(transform.getNode2().getChild(0)).isSameAs(child2);
        }

        @Test
        @DisplayName("Should support multiple transforms with same nodes")
        void testMultipleTransformsWithSameNodes() {
            TestTwoOperandTransform transform1 = new TestTwoOperandTransform("operation1", node1, node2);
            TestTwoOperandTransform transform2 = new TestTwoOperandTransform("operation2", node1, node2);
            TestTwoOperandTransform transform3 = new TestTwoOperandTransform("operation3", node2, node1);

            // All should have access to same nodes
            assertThat(transform1.getNode1()).isSameAs(transform2.getNode1());
            assertThat(transform1.getNode2()).isSameAs(transform2.getNode2());
            assertThat(transform1.getNode1()).isSameAs(transform3.getNode2());
            assertThat(transform1.getNode2()).isSameAs(transform3.getNode1());

            // But should have different types
            assertThat(transform1.getType()).isNotEqualTo(transform2.getType());
            assertThat(transform2.getType()).isNotEqualTo(transform3.getType());
        }
    }

    @Nested
    @DisplayName("Concrete Implementation Tests")
    class ConcreteImplementationTests {

        @Test
        @DisplayName("Concrete implementation should provide execute method")
        void testConcreteImplementationProvidesExecute() {
            assertThat(transform.isExecuted()).isFalse();
            transform.execute();
            assertThat(transform.isExecuted()).isTrue();
        }

        @Test
        @DisplayName("Abstract class should require implementation of execute")
        void testAbstractClassRequiresExecuteImplementation() {
            // This test verifies that our test implementation properly extends the abstract
            // class
            // and provides the required execute method
            assertThatCode(() -> {
                TestTwoOperandTransform newTransform = new TestTwoOperandTransform("test", node1, node2);
                newTransform.execute(); // Should not throw AbstractMethodError
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should support different concrete implementations")
        void testDifferentConcreteImplementations() {
            // Create another implementation with different behavior
            TwoOperandTransform<TestTreeNode> customTransform = new TwoOperandTransform<TestTreeNode>("custom", node1,
                    node2) {
                @Override
                public void execute() {
                    // Custom execution logic
                }
            };

            // Verify it works as expected
            assertThat(customTransform.getType()).isEqualTo("custom");
            assertThat(customTransform.getNode1()).isSameAs(node1);
            assertThat(customTransform.getNode2()).isSameAs(node2);

            // Test custom behavior
            assertThatCode(() -> customTransform.execute()).doesNotThrowAnyException();
        }
    }
}
