package pt.up.fe.specs.util.treenode.transform.transformations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.treenode.ATreeNode;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for DeleteTransform class.
 * Tests the specific implementation of delete transformation operations.
 * 
 * @author Generated Tests
 */
@DisplayName("DeleteTransform Tests")
class DeleteTransformTest {

    private TestTreeNode targetNode;
    private DeleteTransform<TestTreeNode> deleteTransform;

    @BeforeEach
    void setUp() {
        targetNode = new TestTreeNode("target");
        deleteTransform = new DeleteTransform<>(targetNode);
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
        @DisplayName("Constructor should initialize with correct type and operand")
        void testConstructor_SetsCorrectTypeAndOperand() {
            assertThat(deleteTransform.getType()).isEqualTo("delete");
            assertThat(deleteTransform.getOperands()).hasSize(1);
            assertThat(deleteTransform.getOperands().get(0)).isSameAs(targetNode);
        }

        @Test
        @DisplayName("Constructor should handle null node gracefully")
        void testConstructor_WithNullNode() {
            DeleteTransform<TestTreeNode> transform = new DeleteTransform<>(null);
            assertThat(transform.getOperands()).hasSize(1);
            assertThat(transform.getOperands().get(0)).isNull();
        }

        @Test
        @DisplayName("Constructor should create immutable operands list")
        void testConstructor_CreatesImmutableOperandsList() {
            // Arrays.asList returns a fixed-size list that cannot be modified
            assertThatThrownBy(() -> deleteTransform.getOperands().add(new TestTreeNode("extra")))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @Nested
    @DisplayName("Inherited Method Tests")
    class InheritedMethodTests {

        @Test
        @DisplayName("getNode should return the target node")
        void testGetNode_ReturnsTargetNode() {
            assertThat(deleteTransform.getNode()).isSameAs(targetNode);
        }

        @Test
        @DisplayName("toString should contain node information")
        void testToString_ContainsNodeInformation() {
            String result = deleteTransform.toString();
            String nodeHex = Integer.toHexString(targetNode.hashCode());

            assertThat(result).contains("delete");
            assertThat(result).contains(nodeHex);
        }

        @Test
        @DisplayName("toString with null node should throw NullPointerException")
        void testToString_WithNullNode() {
            DeleteTransform<TestTreeNode> transform = new DeleteTransform<>(null);
            assertThatThrownBy(() -> transform.toString())
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Execution Tests")
    class ExecutionTests {

        @Test
        @DisplayName("execute should remove node from parent")
        void testExecute_RemovesNodeFromParent() {
            TestTreeNode parent = new TestTreeNode("parent");
            parent.addChild(targetNode);

            // Verify initial state
            assertThat(parent.getNumChildren()).isEqualTo(1);
            assertThat(parent.getChild(0)).isSameAs(targetNode);
            assertThat(targetNode.getParent()).isSameAs(parent);

            // Execute the deletion
            deleteTransform.execute();

            // Verify deletion occurred
            assertThat(parent.getNumChildren()).isEqualTo(0);
            assertThat(targetNode.getParent()).isNull();
        }

        @Test
        @DisplayName("execute should handle node without parent")
        void testExecute_WithNodeWithoutParent() {
            // Target node has no parent
            assertThat(targetNode.getParent()).isNull();

            // Execute should not throw exception
            assertThatCode(() -> deleteTransform.execute()).doesNotThrowAnyException();

            // State should remain the same
            assertThat(targetNode.getParent()).isNull();
        }

        @Test
        @DisplayName("execute should handle multiple children scenario")
        void testExecute_WithMultipleChildren() {
            TestTreeNode parent = new TestTreeNode("parent");
            TestTreeNode sibling1 = new TestTreeNode("sibling1");
            TestTreeNode sibling2 = new TestTreeNode("sibling2");

            parent.addChild(sibling1);
            parent.addChild(targetNode);
            parent.addChild(sibling2);

            assertThat(parent.getNumChildren()).isEqualTo(3);
            assertThat(parent.getChild(1)).isSameAs(targetNode);

            deleteTransform.execute();

            assertThat(parent.getNumChildren()).isEqualTo(2);
            assertThat(parent.getChild(0)).isSameAs(sibling1);
            assertThat(parent.getChild(1)).isSameAs(sibling2);
            assertThat(targetNode.getParent()).isNull();
        }

        @Test
        @DisplayName("execute should be idempotent when called multiple times")
        void testExecute_IsIdempotent() {
            TestTreeNode parent = new TestTreeNode("parent");
            parent.addChild(targetNode);

            // Execute first time
            deleteTransform.execute();
            assertThat(parent.getNumChildren()).isEqualTo(0);
            assertThat(targetNode.getParent()).isNull();

            // Execute second time - should not change anything
            assertThatCode(() -> deleteTransform.execute()).doesNotThrowAnyException();
            assertThat(parent.getNumChildren()).isEqualTo(0);
            assertThat(targetNode.getParent()).isNull();
        }

        @Test
        @DisplayName("execute should throw NullPointerException with null node")
        void testExecute_WithNullNode() {
            DeleteTransform<TestTreeNode> transform = new DeleteTransform<>(null);

            // Should throw NPE when trying to execute with null node
            assertThatThrownBy(() -> transform.execute())
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with different node types")
        void testWithDifferentNodeTypes() {
            TestTreeNode node = new TestTreeNode("test");
            DeleteTransform<TestTreeNode> transform = new DeleteTransform<>(node);

            assertThat(transform.getType()).isEqualTo("delete");
            assertThat(transform.getNode()).isSameAs(node);
        }

        @Test
        @DisplayName("Should maintain tree structure integrity after deletion")
        void testTreeStructureIntegrity() {
            // Build a tree: root -> parent -> targetNode
            TestTreeNode root = new TestTreeNode("root");
            TestTreeNode parent = new TestTreeNode("parent");
            root.addChild(parent);
            parent.addChild(targetNode);

            deleteTransform.execute();

            // Verify tree structure is maintained
            assertThat(root.getNumChildren()).isEqualTo(1);
            assertThat(root.getChild(0)).isSameAs(parent);
            assertThat(parent.getNumChildren()).isEqualTo(0);

            // Verify deleted node is completely detached
            assertThat(targetNode.getParent()).isNull();
        }

        @Test
        @DisplayName("Should properly delete node with children")
        void testDeleteNodeWithChildren() {
            TestTreeNode child1 = new TestTreeNode("child1");
            TestTreeNode child2 = new TestTreeNode("child2");
            targetNode.addChild(child1);
            targetNode.addChild(child2);

            TestTreeNode parent = new TestTreeNode("parent");
            parent.addChild(targetNode);

            assertThat(targetNode.getNumChildren()).isEqualTo(2);

            deleteTransform.execute();

            // Target node should be removed from parent
            assertThat(parent.getNumChildren()).isEqualTo(0);
            assertThat(targetNode.getParent()).isNull();

            // Children should still be attached to the deleted node
            assertThat(targetNode.getNumChildren()).isEqualTo(2);
            assertThat(child1.getParent()).isSameAs(targetNode);
            assertThat(child2.getParent()).isSameAs(targetNode);
        }

        @Test
        @DisplayName("Should work correctly in complex tree structures")
        void testComplexTreeStructure() {
            // Create a complex tree structure
            TestTreeNode root = new TestTreeNode("root");
            TestTreeNode branch1 = new TestTreeNode("branch1");
            TestTreeNode branch2 = new TestTreeNode("branch2");
            TestTreeNode leaf1 = new TestTreeNode("leaf1");
            TestTreeNode leaf2 = new TestTreeNode("leaf2");

            root.addChild(branch1);
            root.addChild(branch2);
            branch1.addChild(targetNode);
            branch1.addChild(leaf1);
            branch2.addChild(leaf2);

            deleteTransform.execute();

            // Verify structure integrity
            assertThat(root.getNumChildren()).isEqualTo(2);
            assertThat(branch1.getNumChildren()).isEqualTo(1);
            assertThat(branch1.getChild(0)).isSameAs(leaf1);
            assertThat(branch2.getNumChildren()).isEqualTo(1);
            assertThat(branch2.getChild(0)).isSameAs(leaf2);
            assertThat(targetNode.getParent()).isNull();
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle deletion of root node")
        void testDeleteRootNode() {
            // When targetNode has no parent (is root), deletion should handle gracefully
            assertThat(targetNode.getParent()).isNull();

            assertThatCode(() -> deleteTransform.execute()).doesNotThrowAnyException();

            // Since there's no parent, the node should remain unchanged
            assertThat(targetNode.getParent()).isNull();
        }

        @Test
        @DisplayName("Should handle concurrent modification scenarios")
        void testConcurrentModification() {
            TestTreeNode parent = new TestTreeNode("parent");
            parent.addChild(targetNode);

            // Simulate concurrent modification by manually removing the target node
            targetNode.detach();

            // Execute should not fail
            assertThatCode(() -> deleteTransform.execute()).doesNotThrowAnyException();

            // State should remain the same since node was already detached
            assertThat(parent.getNumChildren()).isEqualTo(0);
            assertThat(targetNode.getParent()).isNull();
        }

        @Test
        @DisplayName("Should handle node already detached from different parent")
        void testNodeDetachedFromDifferentParent() {
            TestTreeNode originalParent = new TestTreeNode("original");
            TestTreeNode newParent = new TestTreeNode("new");

            originalParent.addChild(targetNode);
            DeleteTransform<TestTreeNode> transform = new DeleteTransform<>(targetNode);

            // Move node to different parent before executing delete
            targetNode.detach();
            newParent.addChild(targetNode);

            transform.execute();

            // Node should be deleted from its current parent
            assertThat(newParent.getNumChildren()).isEqualTo(0);
            assertThat(targetNode.getParent()).isNull();
        }

        @Test
        @DisplayName("Should handle deletion in deeply nested structure")
        void testDeeplyNestedDeletion() {
            TestTreeNode current = new TestTreeNode("root");
            TestTreeNode target = targetNode;

            // Create a deep nested structure
            for (int i = 0; i < 10; i++) {
                TestTreeNode next = new TestTreeNode("level" + i);
                current.addChild(next);
                current = next;
            }
            current.addChild(target);

            assertThat(target.getParent()).isSameAs(current);

            deleteTransform.execute();

            assertThat(current.getNumChildren()).isEqualTo(0);
            assertThat(target.getParent()).isNull();
        }
    }
}
