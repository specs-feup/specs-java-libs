package pt.up.fe.specs.util.treenode;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

import java.util.*;

/**
 * Test suite for NodeInsertUtils.
 * Tests static utility methods for tree node insertion, replacement, and
 * manipulation operations.
 * 
 * @author Generated Tests
 */
@DisplayName("NodeInsertUtils Tests")
class NodeInsertUtilsTest {

    private TestTreeNode root;
    private TestTreeNode child1;
    private TestTreeNode child2;
    private TestTreeNode grandchild1;
    private TestTreeNode grandchild2;

    @BeforeEach
    void setUp() {
        // Create test tree structure:
        //       root
        //      /    \
        //   child1  child2
        //   /    \
        // gc1    gc2
        grandchild1 = new TestTreeNode("grandchild1");
        grandchild2 = new TestTreeNode("grandchild2");
        child1 = new TestTreeNode("child1", Arrays.asList(grandchild1, grandchild2));
        child2 = new TestTreeNode("child2");
        root = new TestTreeNode("root", Arrays.asList(child1, child2));
    }

    @Nested
    @DisplayName("Replace Operations")
    class ReplaceOperationsTests {

        @Test
        @DisplayName("replace should replace node in parent's children")
        void testReplace_ReplacesNodeInParent() {
            TestTreeNode replacement = new TestTreeNode("replacement");

            NodeInsertUtils.replace(child1, replacement, true);

            assertThat(root.getChildren()).contains(replacement);
            assertThat(root.getChildren()).doesNotContain(child1);
            assertThat(replacement.getParent()).isSameAs(root);
        }

        @Test
        @DisplayName("replace should work with move=true")
        void testReplace_WorksWithMoveTrue() {
            TestTreeNode replacement = new TestTreeNode("replacement");

            NodeInsertUtils.replace(child1, replacement, true);

            assertThat(root.getChildren()).contains(replacement);
            assertThat(root.getChildren()).doesNotContain(child1);
            assertThat(replacement.getParent()).isSameAs(root);
        }

        @Test
        @DisplayName("replace should work with move=false")
        void testReplace_WorksWithMoveFalse() {
            TestTreeNode replacement = new TestTreeNode("replacement");

            NodeInsertUtils.replace(child1, replacement, false);

            assertThat(root.getChildren()).contains(replacement);
            assertThat(root.getChildren()).doesNotContain(child1);
            assertThat(replacement.getParent()).isSameAs(root);
        }

        @Test
        @DisplayName("replace should handle root node")
        void testReplace_HandlesRootNode() {
            TestTreeNode newRoot = new TestTreeNode("newRoot");

            // Replace root (no parent) - should just return newRoot and log warning
            TestTreeNode result = NodeInsertUtils.replace(root, newRoot, true);

            // Should return the newRoot but no actual replacement happens
            assertThat(result).isSameAs(newRoot);
            assertThat(newRoot.hasParent()).isFalse();
        }

        @Test
        @DisplayName("set should preserve children from baseToken")
        void testSet_PreservesChildrenFromBase() {
            TestTreeNode replacement = new TestTreeNode("replacement");
            int originalChildCount = child1.getNumChildren();

            NodeInsertUtils.set(child1, replacement);

            // set() should transfer children from baseToken to replacement
            assertThat(replacement.getNumChildren()).isEqualTo(originalChildCount);

            // Check the children were transferred correctly
            assertThat(replacement.getChildren()).hasSize(originalChildCount);
            assertThat(replacement.getChild(0).toContentString()).isEqualTo("grandchild1");
            assertThat(replacement.getChild(1).toContentString()).isEqualTo("grandchild2");

            // Check parent relationships
            assertThat(replacement.getChild(0).getParent()).isSameAs(replacement);
            assertThat(replacement.getChild(1).getParent()).isSameAs(replacement);

            // replacement should be in root's children
            assertThat(root.getChildren()).extracting(TestTreeNode::toContentString)
                    .contains("replacement");
        }

        @Test
        @DisplayName("replace should maintain original position in parent")
        void testReplace_MaintainsPositionInParent() {
            TestTreeNode replacement = new TestTreeNode("replacement");
            int originalIndex = root.indexOfChild(child1);

            NodeInsertUtils.replace(child1, replacement, true);

            assertThat(root.indexOfChild(replacement)).isEqualTo(originalIndex);
        }
    }

    @Nested
    @DisplayName("Insert Operations")
    class InsertOperationsTests {

        @Test
        @DisplayName("insertBefore should insert node before target with remove=true")
        void testInsertBefore_InsertsBeforeWithRemove() {
            TestTreeNode nodeToMove = new TestTreeNode("toMove");
            root.addChild(nodeToMove); // Add to root first

            NodeInsertUtils.insertBefore(child2, nodeToMove, true);

            // nodeToMove should be before child2 in root's children
            int nodeToMoveIndex = root.indexOfChild(nodeToMove);
            int child2Index = root.indexOfChild(child2);
            assertThat(nodeToMoveIndex).isLessThan(child2Index);
            assertThat(nodeToMove.getParent()).isSameAs(root);
        }

        @Test
        @DisplayName("insertBefore should insert node before target with remove=false")
        void testInsertBefore_InsertsBeforeWithoutRemove() {
            TestTreeNode nodeToInsert = new TestTreeNode("toInsert");
            // Don't add to tree first - should create copy

            NodeInsertUtils.insertBefore(child2, nodeToInsert, false);

            // nodeToInsert should be before child2
            int insertedIndex = root.indexOfChild(nodeToInsert);
            int child2Index = root.indexOfChild(child2);
            assertThat(insertedIndex).isLessThan(child2Index);
        }

        @Test
        @DisplayName("insertAfter should insert node after target with remove=true")
        void testInsertAfter_InsertsAfterWithRemove() {
            TestTreeNode nodeToMove = new TestTreeNode("toMove");
            root.addChild(nodeToMove); // Add to root first

            NodeInsertUtils.insertAfter(child1, nodeToMove, true);

            // nodeToMove should be after child1 in root's children
            int child1Index = root.indexOfChild(child1);
            int nodeToMoveIndex = root.indexOfChild(nodeToMove);
            assertThat(nodeToMoveIndex).isGreaterThan(child1Index);
            assertThat(nodeToMove.getParent()).isSameAs(root);
        }

        @Test
        @DisplayName("insertAfter should insert node after target with remove=false")
        void testInsertAfter_InsertsAfterWithoutRemove() {
            TestTreeNode nodeToInsert = new TestTreeNode("toInsert");

            NodeInsertUtils.insertAfter(child1, nodeToInsert, false);

            // nodeToInsert should be after child1
            int child1Index = root.indexOfChild(child1);
            int insertedIndex = root.indexOfChild(nodeToInsert);
            assertThat(insertedIndex).isGreaterThan(child1Index);
        }
    }

    @Nested
    @DisplayName("Delete Operations")
    class DeleteOperationsTests {

        @Test
        @DisplayName("delete should remove node from parent")
        void testDelete_RemovesNodeFromParent() {
            int originalChildCount = root.getNumChildren();

            NodeInsertUtils.delete(child2);

            assertThat(root.getNumChildren()).isEqualTo(originalChildCount - 1);
            assertThat(root.getChildren()).doesNotContain(child2);
            assertThat(child2.hasParent()).isFalse();
        }

        @Test
        @DisplayName("delete should handle node with children")
        void testDelete_HandlesNodeWithChildren() {
            NodeInsertUtils.delete(child1);

            assertThat(root.getChildren()).doesNotContain(child1);
            assertThat(child1.hasParent()).isFalse();
            // Children should still be attached to child1
            assertThat(child1.hasChildren()).isTrue();
            assertThat(grandchild1.getParent()).isSameAs(child1);
        }

        @Test
        @DisplayName("delete should handle root node gracefully")
        void testDelete_HandlesRootNodeGracefully() {
            // Root has no parent, so delete should just log warning and return
            NodeInsertUtils.delete(root);

            // Root should still exist and have its children
            assertThat(root.hasParent()).isFalse();
            assertThat(child1.getParent()).isSameAs(root);
            assertThat(child2.getParent()).isSameAs(root);
        }
    }

    @Nested
    @DisplayName("Swap Operations")
    class SwapOperationsTests {

        @Test
        @DisplayName("swap should exchange positions when both have same parent")
        void testSwap_ExchangesPositionsWithSameParent() {
            int child1OriginalIndex = root.indexOfChild(child1);
            int child2OriginalIndex = root.indexOfChild(child2);

            NodeInsertUtils.swap(child1, child2, true);

            assertThat(root.indexOfChild(child1)).isEqualTo(child2OriginalIndex);
            assertThat(root.indexOfChild(child2)).isEqualTo(child1OriginalIndex);
            assertThat(child1.getParent()).isSameAs(root);
            assertThat(child2.getParent()).isSameAs(root);
        }

        @Test
        @DisplayName("swap should work when nodes have different parents")
        void testSwap_WorksWithDifferentParents() {
            NodeInsertUtils.swap(child2, grandchild1, true);

            // child2 should now be child of child1
            assertThat(child1.getChildren()).contains(child2);
            assertThat(child2.getParent()).isSameAs(child1);

            // grandchild1 should now be child of root
            assertThat(root.getChildren()).contains(grandchild1);
            assertThat(grandchild1.getParent()).isSameAs(root);
        }

        @Test
        @DisplayName("swap should handle swapSubtrees parameter correctly")
        void testSwap_HandlesSwapSubtreesParameter() {
            // Test with swapSubtrees=false should not swap if one is ancestor of other
            // This test checks the boundary condition
            assertThatCode(() -> {
                NodeInsertUtils.swap(root, child1, false);
            }).doesNotThrowAnyException(); // Should handle gracefully
        }

        @Test
        @DisplayName("swap should work with leaf nodes")
        void testSwap_WorksWithLeafNodes() {
            int gc1OriginalIndex = child1.indexOfChild(grandchild1);
            int gc2OriginalIndex = child1.indexOfChild(grandchild2);

            NodeInsertUtils.swap(grandchild1, grandchild2, true);

            assertThat(child1.indexOfChild(grandchild1)).isEqualTo(gc2OriginalIndex);
            assertThat(child1.indexOfChild(grandchild2)).isEqualTo(gc1OriginalIndex);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("operations should handle null nodes according to implementation")
        void testOperations_HandleNullNodesAccordingToImplementation() {
            // NodeInsertUtils.delete(null) will throw NPE as expected
            assertThatThrownBy(() -> {
                NodeInsertUtils.delete(null);
            }).isInstanceOf(NullPointerException.class);

            // Replace operations with null should also fail appropriately
            assertThatThrownBy(() -> {
                NodeInsertUtils.replace(null, child1, true);
            }).isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("operations should handle detached nodes")
        void testOperations_HandleDetachedNodes() {
            TestTreeNode detachedNode = new TestTreeNode("detached");
            TestTreeNode replacement = new TestTreeNode("replacement");

            // Should handle detached nodes without errors
            assertThatCode(() -> {
                NodeInsertUtils.replace(detachedNode, replacement, true);
            }).doesNotThrowAnyException();

            assertThatCode(() -> {
                NodeInsertUtils.delete(detachedNode);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("operations should preserve tree integrity")
        void testOperations_PreserveTreeIntegrity() {
            // After any operation, tree should maintain proper parent-child relationships
            TestTreeNode newNode = new TestTreeNode("newNode");

            NodeInsertUtils.insertAfter(child1, newNode, false);

            // Check integrity
            for (TestTreeNode child : root.getChildren()) {
                assertThat(child.getParent()).isSameAs(root);
                for (TestTreeNode grandchild : child.getChildren()) {
                    assertThat(grandchild.getParent()).isSameAs(child);
                }
            }
        }
    }

    @Nested
    @DisplayName("Complex Operations")
    class ComplexOperationsTests {

        @Test
        @DisplayName("multiple operations should work together")
        void testMultipleOperations_WorkTogether() {
            TestTreeNode newNode1 = new TestTreeNode("new1");
            TestTreeNode newNode2 = new TestTreeNode("new2");
            TestTreeNode replacement = new TestTreeNode("replacement");

            // Sequence of operations
            NodeInsertUtils.insertBefore(child1, newNode1, false);
            NodeInsertUtils.insertAfter(child2, newNode2, false);
            NodeInsertUtils.replace(child1, replacement, true);

            // Verify final state
            assertThat(root.getChildren()).containsExactly(newNode1, replacement, child2, newNode2);
            // Replacement should not have children since replace() doesn't transfer them
            assertThat(replacement.hasChildren()).isFalse();
        }

        @Test
        @DisplayName("operations should work with deep trees")
        void testOperations_WorkWithDeepTrees() {
            // Create deeper tree structure
            TestTreeNode level3 = new TestTreeNode("level3");
            TestTreeNode level4 = new TestTreeNode("level4");
            level3.addChild(level4);
            grandchild1.addChild(level3);

            TestTreeNode replacement = new TestTreeNode("deepReplacement");

            NodeInsertUtils.replace(level4, replacement, true);

            assertThat(level3.getChildren()).contains(replacement);
            assertThat(replacement.getParent()).isSameAs(level3);
        }
    }

    /**
     * Test implementation of TreeNode for testing purposes
     */
    private static class TestTreeNode extends ATreeNode<TestTreeNode> {
        private final String name;

        public TestTreeNode(String name) {
            super(Collections.emptyList());
            this.name = name;
        }

        public TestTreeNode(String name, Collection<? extends TestTreeNode> children) {
            super(children);
            this.name = name;
        }

        @Override
        public String toContentString() {
            return name;
        }

        @Override
        public String toString() {
            return "TestTreeNode{name='" + name + "'}";
        }

        @Override
        protected TestTreeNode copyPrivate() {
            return new TestTreeNode(name);
        }
    }
}
