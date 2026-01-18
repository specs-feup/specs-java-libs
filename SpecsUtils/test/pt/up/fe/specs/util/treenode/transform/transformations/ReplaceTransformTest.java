package pt.up.fe.specs.util.treenode.transform.transformations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.treenode.ATreeNode;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for ReplaceTransform class.
 * Tests the specific implementation of replace transformation operations.
 * 
 * @author Generated Tests
 */
@DisplayName("ReplaceTransform Tests")
class ReplaceTransformTest {

    private TestTreeNode baseNode;
    private TestTreeNode newNode;
    private ReplaceTransform<TestTreeNode> replaceTransform;

    @BeforeEach
    void setUp() {
        baseNode = new TestTreeNode("base");
        newNode = new TestTreeNode("replacement");
        replaceTransform = new ReplaceTransform<>(baseNode, newNode);
    }

    /**
     * Test tree node implementation for testing purposes.
     */
    private static class TestTreeNode extends ATreeNode<TestTreeNode> {
        private final String value;

        public TestTreeNode(String value) {
            super(null); // Call parent constructor with no children
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

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Constructor should initialize with correct type and operands")
        void testConstructor_SetsCorrectTypeAndOperands() {
            assertThat(replaceTransform.getType()).isEqualTo("replace");
            assertThat(replaceTransform.getOperands()).hasSize(2);
            assertThat(replaceTransform.getOperands().get(0)).isSameAs(baseNode);
            assertThat(replaceTransform.getOperands().get(1)).isSameAs(newNode);
        }

        @Test
        @DisplayName("Constructor should handle null baseNode gracefully")
        void testConstructor_WithNullBaseNode() {
            ReplaceTransform<TestTreeNode> transform = new ReplaceTransform<>(null, newNode);
            assertThat(transform.getOperands().get(0)).isNull();
            assertThat(transform.getOperands().get(1)).isSameAs(newNode);
        }

        @Test
        @DisplayName("Constructor should handle null newNode gracefully")
        void testConstructor_WithNullNewNode() {
            ReplaceTransform<TestTreeNode> transform = new ReplaceTransform<>(baseNode, null);
            assertThat(transform.getOperands().get(0)).isSameAs(baseNode);
            assertThat(transform.getOperands().get(1)).isNull();
        }

        @Test
        @DisplayName("Constructor should handle both nodes as null")
        void testConstructor_WithBothNodesNull() {
            ReplaceTransform<TestTreeNode> transform = new ReplaceTransform<>(null, null);
            assertThat(transform.getOperands()).hasSize(2);
            assertThat(transform.getOperands().get(0)).isNull();
            assertThat(transform.getOperands().get(1)).isNull();
        }
    }

    @Nested
    @DisplayName("Inherited Method Tests")
    class InheritedMethodTests {

        @Test
        @DisplayName("getNode1 should return the base node")
        void testGetNode1_ReturnsBaseNode() {
            assertThat(replaceTransform.getNode1()).isSameAs(baseNode);
        }

        @Test
        @DisplayName("getNode2 should return the new node")
        void testGetNode2_ReturnsNewNode() {
            assertThat(replaceTransform.getNode2()).isSameAs(newNode);
        }

        @Test
        @DisplayName("toString should contain both node hash codes")
        void testToString_ContainsBothNodeHashes() {
            String result = replaceTransform.toString();
            String baseHex = Integer.toHexString(baseNode.hashCode());
            String newHex = Integer.toHexString(newNode.hashCode());

            assertThat(result).contains("replace");
            assertThat(result).contains("node1(" + baseHex + ")");
            assertThat(result).contains("node2(" + newHex + ")");
        }
    }

    @Nested
    @DisplayName("Execution Tests")
    class ExecutionTests {

        @Test
        @DisplayName("execute should perform replacement using NodeInsertUtils")
        void testExecute_PerformsReplacement() {
            // Create a parent with the base node as child
            TestTreeNode parent = new TestTreeNode("parent");
            parent.addChild(baseNode);

            // Verify initial state
            assertThat(parent.getNumChildren()).isEqualTo(1);
            assertThat(parent.getChild(0)).isSameAs(baseNode);
            assertThat(baseNode.getParent()).isSameAs(parent);

            // Execute the replacement
            replaceTransform.execute();

            // Verify replacement occurred
            assertThat(parent.getNumChildren()).isEqualTo(1);
            assertThat(parent.getChild(0)).isSameAs(newNode);
            assertThat(newNode.getParent()).isSameAs(parent);
            assertThat(baseNode.getParent()).isNull(); // Old node should be detached
        }

        @Test
        @DisplayName("execute should handle node without parent")
        void testExecute_WithNodeWithoutParent() {
            // Base node has no parent
            assertThat(baseNode.getParent()).isNull();

            // Execute should not throw exception
            assertThatCode(() -> replaceTransform.execute()).doesNotThrowAnyException();

            // State should remain the same since replacement requires a parent
            assertThat(baseNode.getParent()).isNull();
            assertThat(newNode.getParent()).isNull();
        }

        @Test
        @DisplayName("execute should handle multiple children scenario")
        void testExecute_WithMultipleChildren() {
            TestTreeNode parent = new TestTreeNode("parent");
            TestTreeNode sibling1 = new TestTreeNode("sibling1");
            TestTreeNode sibling2 = new TestTreeNode("sibling2");

            parent.addChild(sibling1);
            parent.addChild(baseNode);
            parent.addChild(sibling2);

            assertThat(parent.getNumChildren()).isEqualTo(3);
            assertThat(parent.getChild(1)).isSameAs(baseNode);

            replaceTransform.execute();

            assertThat(parent.getNumChildren()).isEqualTo(3);
            assertThat(parent.getChild(0)).isSameAs(sibling1);
            assertThat(parent.getChild(1)).isSameAs(newNode);
            assertThat(parent.getChild(2)).isSameAs(sibling2);
        }

        @Test
        @DisplayName("execute should be idempotent when called multiple times")
        void testExecute_IsIdempotent() {
            TestTreeNode parent = new TestTreeNode("parent");
            parent.addChild(baseNode);

            // Execute first time
            replaceTransform.execute();
            assertThat(parent.getChild(0)).isSameAs(newNode);

            // Execute second time - should not change anything since baseNode is no longer
            // in tree
            assertThatCode(() -> replaceTransform.execute()).doesNotThrowAnyException();
            assertThat(parent.getChild(0)).isSameAs(newNode);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with different node types")
        void testWithDifferentNodeTypes() {
            TestTreeNode node1 = new TestTreeNode("original");
            TestTreeNode node2 = new TestTreeNode("replacement");

            ReplaceTransform<TestTreeNode> transform = new ReplaceTransform<>(node1, node2);

            assertThat(transform.getType()).isEqualTo("replace");
            assertThat(transform.getNode1()).isSameAs(node1);
            assertThat(transform.getNode2()).isSameAs(node2);
        }

        @Test
        @DisplayName("Should maintain tree structure integrity after replacement")
        void testTreeStructureIntegrity() {
            // Build a small tree: root -> parent -> baseNode
            TestTreeNode root = new TestTreeNode("root");
            TestTreeNode parent = new TestTreeNode("parent");
            root.addChild(parent);
            parent.addChild(baseNode);

            replaceTransform.execute();

            // Verify tree structure is maintained
            assertThat(root.getNumChildren()).isEqualTo(1);
            assertThat(root.getChild(0)).isSameAs(parent);
            assertThat(parent.getNumChildren()).isEqualTo(1);
            assertThat(parent.getChild(0)).isSameAs(newNode);
            assertThat(newNode.getParent()).isSameAs(parent);

            // Verify old node is completely detached
            assertThat(baseNode.getParent()).isNull();
        }

        @Test
        @DisplayName("Should work correctly when replacing with node that has children")
        void testReplaceWithNodeWithChildren() {
            TestTreeNode child1 = new TestTreeNode("child1");
            TestTreeNode child2 = new TestTreeNode("child2");
            newNode.addChild(child1);
            newNode.addChild(child2);

            TestTreeNode parent = new TestTreeNode("parent");
            parent.addChild(baseNode);

            replaceTransform.execute();

            assertThat(parent.getChild(0)).isSameAs(newNode);
            assertThat(newNode.getNumChildren()).isEqualTo(2);
            assertThat(newNode.getChild(0)).isSameAs(child1);
            assertThat(newNode.getChild(1)).isSameAs(child2);
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle replacement when base node is root")
        void testReplaceRootNode() {
            // When baseNode has no parent (is root), replacement should handle gracefully
            assertThat(baseNode.getParent()).isNull();

            assertThatCode(() -> replaceTransform.execute()).doesNotThrowAnyException();

            // Since there's no parent, the nodes should remain unchanged
            assertThat(baseNode.getParent()).isNull();
            assertThat(newNode.getParent()).isNull();
        }

        @Test
        @DisplayName("Should handle self-replacement scenario")
        void testSelfReplacement() {
            ReplaceTransform<TestTreeNode> selfTransform = new ReplaceTransform<>(baseNode, baseNode);
            TestTreeNode parent = new TestTreeNode("parent");
            parent.addChild(baseNode);

            // Self-replacement causes issues due to tree node constraints where
            // a node cannot be its own replacement in the same position
            assertThatThrownBy(() -> selfTransform.execute())
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Token does not have children");
        }

        @Test
        @DisplayName("Should handle concurrent modification scenarios gracefully")
        void testConcurrentModification() {
            TestTreeNode parent = new TestTreeNode("parent");
            parent.addChild(baseNode);

            // Simulate concurrent modification by removing the base node
            baseNode.detach();

            // Execute should not fail
            assertThatCode(() -> replaceTransform.execute()).doesNotThrowAnyException();

            // Parent should be empty since base node was already removed
            assertThat(parent.getNumChildren()).isEqualTo(0);
        }
    }
}
