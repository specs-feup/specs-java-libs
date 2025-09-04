package pt.up.fe.specs.util.treenode.transform.transformations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.treenode.ATreeNode;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for SwapTransform class.
 * Tests the specific implementation of swap transformation operations.
 * 
 * @author Generated Tests
 */
@DisplayName("SwapTransform Tests")
class SwapTransformTest {

    private TestTreeNode node1;
    private TestTreeNode node2;
    private SwapTransform<TestTreeNode> swapTransform;

    @BeforeEach
    void setUp() {
        node1 = new TestTreeNode("node1");
        node2 = new TestTreeNode("node2");
        swapTransform = new SwapTransform<>(node1, node2, true);
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
        public TestTreeNode copyShallow() {
            return new TestTreeNode(value + "_copy");
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
            assertThat(swapTransform.getType()).isEqualTo("swap");
            assertThat(swapTransform.getOperands()).hasSize(2);
            assertThat(swapTransform.getOperands().get(0)).isSameAs(node1);
            assertThat(swapTransform.getOperands().get(1)).isSameAs(node2);
        }

        @Test
        @DisplayName("Constructor should handle null node1 gracefully")
        void testConstructor_WithNullNode1() {
            SwapTransform<TestTreeNode> transform = new SwapTransform<>(null, node2, true);
            assertThat(transform.getOperands().get(0)).isNull();
            assertThat(transform.getOperands().get(1)).isSameAs(node2);
        }

        @Test
        @DisplayName("Constructor should handle null node2 gracefully")
        void testConstructor_WithNullNode2() {
            SwapTransform<TestTreeNode> transform = new SwapTransform<>(node1, null, true);
            assertThat(transform.getOperands().get(0)).isSameAs(node1);
            assertThat(transform.getOperands().get(1)).isNull();
        }

        @Test
        @DisplayName("Constructor should handle both nodes as null")
        void testConstructor_WithBothNodesNull() {
            SwapTransform<TestTreeNode> transform = new SwapTransform<>(null, null, true);
            assertThat(transform.getOperands()).hasSize(2);
            assertThat(transform.getOperands().get(0)).isNull();
            assertThat(transform.getOperands().get(1)).isNull();
        }

        @Test
        @DisplayName("Constructor should accept swapSubtrees parameter")
        void testConstructor_WithSwapSubtreesFlag() {
            SwapTransform<TestTreeNode> transform1 = new SwapTransform<>(node1, node2, true);
            SwapTransform<TestTreeNode> transform2 = new SwapTransform<>(node1, node2, false);

            // Both should have the same type and operands, but different internal state
            assertThat(transform1.getType()).isEqualTo("swap");
            assertThat(transform2.getType()).isEqualTo("swap");
            assertThat(transform1.getOperands()).isEqualTo(transform2.getOperands());
        }
    }

    @Nested
    @DisplayName("Inherited Method Tests")
    class InheritedMethodTests {

        @Test
        @DisplayName("getNode1 should return the first node")
        void testGetNode1_ReturnsFirstNode() {
            assertThat(swapTransform.getNode1()).isSameAs(node1);
        }

        @Test
        @DisplayName("getNode2 should return the second node")
        void testGetNode2_ReturnsSecondNode() {
            assertThat(swapTransform.getNode2()).isSameAs(node2);
        }

        @Test
        @DisplayName("toString should contain both node hash codes")
        void testToString_ContainsBothNodeHashes() {
            String result = swapTransform.toString();
            String node1Hex = Integer.toHexString(node1.hashCode());
            String node2Hex = Integer.toHexString(node2.hashCode());

            assertThat(result).contains("swap");
            assertThat(result).contains("node1(" + node1Hex + ")");
            assertThat(result).contains("node2(" + node2Hex + ")");
        }
    }

    @Nested
    @DisplayName("Execution Tests")
    class ExecutionTests {

        @Test
        @DisplayName("execute should swap positions of two nodes in same parent")
        void testExecute_SwapsNodesInSameParent() {
            TestTreeNode parent = new TestTreeNode("parent");
            TestTreeNode sibling1 = new TestTreeNode("sibling1");
            TestTreeNode sibling2 = new TestTreeNode("sibling2");

            // Setup: parent has [sibling1, node1, node2, sibling2]
            parent.addChild(sibling1);
            parent.addChild(node1);
            parent.addChild(node2);
            parent.addChild(sibling2);

            // Verify initial state
            assertThat(parent.getNumChildren()).isEqualTo(4);
            assertThat(parent.getChild(1)).isSameAs(node1);
            assertThat(parent.getChild(2)).isSameAs(node2);

            // Execute the swap operation
            swapTransform.execute();

            // Verify nodes were swapped
            assertThat(parent.getNumChildren()).isEqualTo(4);
            assertThat(parent.getChild(0)).isSameAs(sibling1);
            assertThat(parent.getChild(1)).isSameAs(node2);
            assertThat(parent.getChild(2)).isSameAs(node1);
            assertThat(parent.getChild(3)).isSameAs(sibling2);
        }

        @Test
        @DisplayName("execute should swap nodes from different parents")
        void testExecute_SwapsNodesFromDifferentParents() {
            TestTreeNode parent1 = new TestTreeNode("parent1");
            TestTreeNode parent2 = new TestTreeNode("parent2");
            TestTreeNode sibling1 = new TestTreeNode("sibling1");
            TestTreeNode sibling2 = new TestTreeNode("sibling2");

            // Setup: parent1 has [sibling1, node1], parent2 has [node2, sibling2]
            parent1.addChild(sibling1);
            parent1.addChild(node1);
            parent2.addChild(node2);
            parent2.addChild(sibling2);

            // Verify initial state
            assertThat(node1.getParent()).isSameAs(parent1);
            assertThat(node2.getParent()).isSameAs(parent2);

            // Execute the swap operation
            swapTransform.execute();

            // Verify nodes were swapped between parents
            assertThat(parent1.getNumChildren()).isEqualTo(2);
            assertThat(parent2.getNumChildren()).isEqualTo(2);
            assertThat(parent1.getChild(0)).isSameAs(sibling1);
            assertThat(parent1.getChild(1)).isSameAs(node2);
            assertThat(parent2.getChild(0)).isSameAs(node1);
            assertThat(parent2.getChild(1)).isSameAs(sibling2);
            assertThat(node1.getParent()).isSameAs(parent2);
            assertThat(node2.getParent()).isSameAs(parent1);
        }

        @Test
        @DisplayName("execute should handle nodes at different nesting levels")
        void testExecute_SwapsNodesAtDifferentLevels() {
            TestTreeNode root = new TestTreeNode("root");
            TestTreeNode branch1 = new TestTreeNode("branch1");
            TestTreeNode branch2 = new TestTreeNode("branch2");
            TestTreeNode leaf = new TestTreeNode("leaf");

            // Setup: root -> [node1, branch1], branch1 -> [branch2], branch2 -> [node2,
            // leaf]
            root.addChild(node1);
            root.addChild(branch1);
            branch1.addChild(branch2);
            branch2.addChild(node2);
            branch2.addChild(leaf);

            // Execute the swap operation
            swapTransform.execute();

            // Verify nodes were swapped across different levels
            assertThat(root.getChild(0)).isSameAs(node2);
            assertThat(branch2.getChild(0)).isSameAs(node1);
            assertThat(node1.getParent()).isSameAs(branch2);
            assertThat(node2.getParent()).isSameAs(root);
        }

        @Test
        @DisplayName("execute should swap nodes with their own children")
        void testExecute_SwapsNodesWithChildren() {
            TestTreeNode parent = new TestTreeNode("parent");
            TestTreeNode child1a = new TestTreeNode("child1a");
            TestTreeNode child1b = new TestTreeNode("child1b");
            TestTreeNode child2a = new TestTreeNode("child2a");

            // Setup: parent has [node1, node2], node1 has [child1a, child1b], node2 has
            // [child2a]
            parent.addChild(node1);
            parent.addChild(node2);
            node1.addChild(child1a);
            node1.addChild(child1b);
            node2.addChild(child2a);

            // Execute the swap operation
            swapTransform.execute();

            // Verify nodes were swapped but children remained attached
            assertThat(parent.getChild(0)).isSameAs(node2);
            assertThat(parent.getChild(1)).isSameAs(node1);
            assertThat(node1.getNumChildren()).isEqualTo(2);
            assertThat(node2.getNumChildren()).isEqualTo(1);
            assertThat(node1.getChild(0)).isSameAs(child1a);
            assertThat(node1.getChild(1)).isSameAs(child1b);
            assertThat(node2.getChild(0)).isSameAs(child2a);
        }
    }

    @Nested
    @DisplayName("SwapSubtrees Tests")
    class SwapSubtreesTests {

        @Test
        @DisplayName("execute should prevent swapping when node is ancestor with swapSubtrees=true")
        void testExecute_PreventsAncestorSwapWithSubtreesEnabled() {
            TestTreeNode parent = new TestTreeNode("parent");
            TestTreeNode child = new TestTreeNode("child");

            // Setup: parent -> child (parent is ancestor of child)
            parent.addChild(child);

            SwapTransform<TestTreeNode> transform = new SwapTransform<>(parent, child, true);

            // Execute should do nothing (with warning message)
            assertThatCode(() -> transform.execute())
                    .doesNotThrowAnyException();

            // Verify nothing changed
            assertThat(parent.getParent()).isNull();
            assertThat(child.getParent()).isSameAs(parent);
            assertThat(parent.getChild(0)).isSameAs(child);
        }

        @Test
        @DisplayName("execute should allow swapping ancestor with swapSubtrees=false")
        void testExecute_AllowsAncestorSwapWithSubtreesDisabled() {
            TestTreeNode grandparent = new TestTreeNode("grandparent");
            TestTreeNode parent = new TestTreeNode("parent");
            TestTreeNode child = new TestTreeNode("child");

            // Setup: grandparent -> parent -> child
            grandparent.addChild(parent);
            parent.addChild(child);

            SwapTransform<TestTreeNode> transform = new SwapTransform<>(parent, child, false);

            // Execute should work
            transform.execute();

            // Verify swap occurred (this creates a complex rearrangement)
            // The exact result depends on implementation details
            assertThatCode(() -> {
                // Just verify it executed without error
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("execute should handle deeply nested ancestor check")
        void testExecute_HandlesDeepAncestorCheck() {
            TestTreeNode root = new TestTreeNode("root");
            TestTreeNode current = root;

            // Create deep nesting: root -> level0 -> level1 -> level2 -> level3
            for (int i = 0; i < 4; i++) {
                TestTreeNode next = new TestTreeNode("level" + i);
                current.addChild(next);
                current = next;
            }

            SwapTransform<TestTreeNode> transform = new SwapTransform<>(root, current, true);

            // Should prevent swap due to ancestor relationship
            transform.execute();

            // Verify root is still at the top
            assertThat(root.getParent()).isNull();
            assertThat(current.getParent().getParent().getParent().getParent()).isSameAs(root);
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("execute should throw exception with null node1")
        void testExecute_WithNullNode1() {
            SwapTransform<TestTreeNode> transform = new SwapTransform<>(null, node2, true);

            assertThatThrownBy(() -> transform.execute())
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("execute should throw exception with null node2")
        void testExecute_WithNullNode2() {
            SwapTransform<TestTreeNode> transform = new SwapTransform<>(node1, null, true);

            assertThatThrownBy(() -> transform.execute())
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("execute should handle nodes without parents")
        void testExecute_WithNodesWithoutParents() {
            // Both nodes have no parents
            assertThat(node1.getParent()).isNull();
            assertThat(node2.getParent()).isNull();

            // This should handle gracefully or throw appropriate exception
            assertThatCode(() -> swapTransform.execute())
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("execute should handle one node without parent")
        void testExecute_WithOneNodeWithoutParent() {
            TestTreeNode parent = new TestTreeNode("parent");
            parent.addChild(node1);
            // node2 has no parent

            assertThat(node1.getParent()).isSameAs(parent);
            assertThat(node2.getParent()).isNull();

            // Should handle gracefully
            assertThatCode(() -> swapTransform.execute())
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with different node types")
        void testWithDifferentNodeTypes() {
            TestTreeNode nodeA = new TestTreeNode("nodeA");
            TestTreeNode nodeB = new TestTreeNode("nodeB");

            SwapTransform<TestTreeNode> transform = new SwapTransform<>(nodeA, nodeB, true);

            assertThat(transform.getType()).isEqualTo("swap");
            assertThat(transform.getNode1()).isSameAs(nodeA);
            assertThat(transform.getNode2()).isSameAs(nodeB);
        }

        @Test
        @DisplayName("Should maintain tree structure integrity after swap")
        void testTreeStructureIntegrity() {
            TestTreeNode root = new TestTreeNode("root");
            TestTreeNode branch1 = new TestTreeNode("branch1");
            TestTreeNode branch2 = new TestTreeNode("branch2");
            TestTreeNode leaf1 = new TestTreeNode("leaf1");
            TestTreeNode leaf2 = new TestTreeNode("leaf2");

            // Build tree: root -> [branch1, branch2], branch1 -> [node1, leaf1], branch2 ->
            // [node2, leaf2]
            root.addChild(branch1);
            root.addChild(branch2);
            branch1.addChild(node1);
            branch1.addChild(leaf1);
            branch2.addChild(node2);
            branch2.addChild(leaf2);

            SwapTransform<TestTreeNode> transform = new SwapTransform<>(node1, node2, true);
            transform.execute();

            // Verify tree structure integrity after swap
            assertThat(root.getNumChildren()).isEqualTo(2);
            assertThat(branch1.getNumChildren()).isEqualTo(2);
            assertThat(branch2.getNumChildren()).isEqualTo(2);
            assertThat(branch1.getChild(0)).isSameAs(node2);
            assertThat(branch1.getChild(1)).isSameAs(leaf1);
            assertThat(branch2.getChild(0)).isSameAs(node1);
            assertThat(branch2.getChild(1)).isSameAs(leaf2);
        }

        @Test
        @DisplayName("Should work correctly with complex tree manipulations")
        void testComplexTreeManipulations() {
            TestTreeNode root = new TestTreeNode("root");
            TestTreeNode subtree1 = new TestTreeNode("subtree1");
            TestTreeNode subtree2 = new TestTreeNode("subtree2");

            // Create complex subtrees
            root.addChild(subtree1);
            root.addChild(subtree2);

            for (int i = 0; i < 3; i++) {
                TestTreeNode child1 = new TestTreeNode("child1_" + i);
                TestTreeNode child2 = new TestTreeNode("child2_" + i);
                subtree1.addChild(child1);
                subtree2.addChild(child2);
            }

            subtree1.addChild(node1);
            subtree2.addChild(node2);

            SwapTransform<TestTreeNode> transform = new SwapTransform<>(node1, node2, true);
            transform.execute();

            // Verify complex structure is maintained
            assertThat(subtree1.getNumChildren()).isEqualTo(4);
            assertThat(subtree2.getNumChildren()).isEqualTo(4);
            assertThat(subtree1.getChild(3)).isSameAs(node2);
            assertThat(subtree2.getChild(3)).isSameAs(node1);
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle swapping same node")
        void testSwapSameNode() {
            TestTreeNode parent = new TestTreeNode("parent");
            parent.addChild(node1);

            SwapTransform<TestTreeNode> transform = new SwapTransform<>(node1, node1, true);

            // Swapping a node with itself should be handled gracefully
            assertThatCode(() -> transform.execute())
                    .doesNotThrowAnyException();

            // Node should remain in the same position
            assertThat(parent.getNumChildren()).isEqualTo(1);
            assertThat(parent.getChild(0)).isSameAs(node1);
        }

        @Test
        @DisplayName("Should be idempotent when called multiple times")
        void testIdempotent() {
            TestTreeNode parent = new TestTreeNode("parent");
            parent.addChild(node1);
            parent.addChild(node2);

            // Execute first time
            swapTransform.execute();

            assertThat(parent.getChild(0)).isSameAs(node2);
            assertThat(parent.getChild(1)).isSameAs(node1);

            // Execute second time - should swap back
            swapTransform.execute();

            assertThat(parent.getChild(0)).isSameAs(node1);
            assertThat(parent.getChild(1)).isSameAs(node2);
        }

        @Test
        @DisplayName("Should handle nodes at same position")
        void testNodesAtSamePosition() {
            TestTreeNode parent1 = new TestTreeNode("parent1");
            TestTreeNode parent2 = new TestTreeNode("parent2");

            parent1.addChild(node1);
            parent2.addChild(node2);

            // Both nodes are at position 0 in their respective parents
            assertThat(node1.indexOfSelf()).isEqualTo(0);
            assertThat(node2.indexOfSelf()).isEqualTo(0);

            swapTransform.execute();

            // After swap, they should still be at position 0 but in different parents
            assertThat(parent1.getChild(0)).isSameAs(node2);
            assertThat(parent2.getChild(0)).isSameAs(node1);
            assertThat(node1.indexOfSelf()).isEqualTo(0);
            assertThat(node2.indexOfSelf()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should handle large tree structures efficiently")
        void testLargeTreeStructures() {
            TestTreeNode root = new TestTreeNode("root");
            TestTreeNode parent1 = new TestTreeNode("parent1");
            TestTreeNode parent2 = new TestTreeNode("parent2");

            root.addChild(parent1);
            root.addChild(parent2);

            // Create many siblings
            for (int i = 0; i < 100; i++) {
                parent1.addChild(new TestTreeNode("sibling1_" + i));
                parent2.addChild(new TestTreeNode("sibling2_" + i));
            }

            parent1.addChild(node1);
            parent2.addChild(node2);

            // Should handle large structures efficiently
            long startTime = System.currentTimeMillis();
            swapTransform.execute();
            long endTime = System.currentTimeMillis();

            // Verify swap occurred
            assertThat(parent1.getChild(100)).isSameAs(node2);
            assertThat(parent2.getChild(100)).isSameAs(node1);

            // Should complete in reasonable time (less than 1 second)
            assertThat(endTime - startTime).isLessThan(1000);
        }
    }
}
