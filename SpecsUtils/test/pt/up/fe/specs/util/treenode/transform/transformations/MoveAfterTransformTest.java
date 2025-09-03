package pt.up.fe.specs.util.treenode.transform.transformations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.treenode.ATreeNode;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for MoveAfterTransform class.
 * Tests the specific implementation of move after transformation operations.
 * 
 * @author Generated Tests
 */
@DisplayName("MoveAfterTransform Tests")
class MoveAfterTransformTest {

    private TestTreeNode baseNode;
    private TestTreeNode moveNode;
    private MoveAfterTransform<TestTreeNode> moveAfterTransform;

    @BeforeEach
    void setUp() {
        baseNode = new TestTreeNode("base");
        moveNode = new TestTreeNode("move");
        moveAfterTransform = new MoveAfterTransform<>(baseNode, moveNode);
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

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Constructor should initialize with correct type and operands")
        void testConstructor_SetsCorrectTypeAndOperands() {
            assertThat(moveAfterTransform.getType()).isEqualTo("move-after");
            assertThat(moveAfterTransform.getOperands()).hasSize(2);
            assertThat(moveAfterTransform.getOperands().get(0)).isSameAs(baseNode);
            assertThat(moveAfterTransform.getOperands().get(1)).isSameAs(moveNode);
        }

        @Test
        @DisplayName("Constructor should handle null baseNode gracefully")
        void testConstructor_WithNullBaseNode() {
            MoveAfterTransform<TestTreeNode> transform = new MoveAfterTransform<>(null, moveNode);
            assertThat(transform.getOperands().get(0)).isNull();
            assertThat(transform.getOperands().get(1)).isSameAs(moveNode);
        }

        @Test
        @DisplayName("Constructor should handle null moveNode gracefully")
        void testConstructor_WithNullMoveNode() {
            MoveAfterTransform<TestTreeNode> transform = new MoveAfterTransform<>(baseNode, null);
            assertThat(transform.getOperands().get(0)).isSameAs(baseNode);
            assertThat(transform.getOperands().get(1)).isNull();
        }

        @Test
        @DisplayName("Constructor should handle both nodes as null")
        void testConstructor_WithBothNodesNull() {
            MoveAfterTransform<TestTreeNode> transform = new MoveAfterTransform<>(null, null);
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
            assertThat(moveAfterTransform.getNode1()).isSameAs(baseNode);
        }

        @Test
        @DisplayName("getNode2 should return the move node")
        void testGetNode2_ReturnsMoveNode() {
            assertThat(moveAfterTransform.getNode2()).isSameAs(moveNode);
        }

        @Test
        @DisplayName("toString should contain both node hash codes")
        void testToString_ContainsBothNodeHashes() {
            String result = moveAfterTransform.toString();
            String baseHex = Integer.toHexString(baseNode.hashCode());
            String moveHex = Integer.toHexString(moveNode.hashCode());

            assertThat(result).contains("move-after");
            assertThat(result).contains("node1(" + baseHex + ")");
            assertThat(result).contains("node2(" + moveHex + ")");
        }
    }

    @Nested
    @DisplayName("Execution Tests")
    class ExecutionTests {

        @Test
        @DisplayName("execute should move node to position after base node")
        void testExecute_MovesNodeAfterBase() {
            TestTreeNode parent = new TestTreeNode("parent");
            TestTreeNode sibling1 = new TestTreeNode("sibling1");
            TestTreeNode sibling2 = new TestTreeNode("sibling2");
            TestTreeNode otherParent = new TestTreeNode("otherParent");

            // Setup: parent has [sibling1, baseNode, sibling2]
            parent.addChild(sibling1);
            parent.addChild(baseNode);
            parent.addChild(sibling2);

            // moveNode is child of otherParent
            otherParent.addChild(moveNode);

            // Verify initial state
            assertThat(parent.getNumChildren()).isEqualTo(3);
            assertThat(parent.getChild(1)).isSameAs(baseNode);
            assertThat(moveNode.getParent()).isSameAs(otherParent);

            // Execute the move after operation
            moveAfterTransform.execute();

            // Verify moveNode is now after baseNode
            assertThat(parent.getNumChildren()).isEqualTo(4);
            assertThat(parent.getChild(0)).isSameAs(sibling1);
            assertThat(parent.getChild(1)).isSameAs(baseNode);
            assertThat(parent.getChild(2)).isSameAs(moveNode);
            assertThat(parent.getChild(3)).isSameAs(sibling2);

            // Verify moveNode was moved from otherParent
            assertThat(otherParent.getNumChildren()).isEqualTo(0);
            assertThat(moveNode.getParent()).isSameAs(parent);
        }

        @Test
        @DisplayName("execute should handle base node at end of children list")
        void testExecute_WithBaseNodeAtEnd() {
            TestTreeNode parent = new TestTreeNode("parent");
            TestTreeNode sibling = new TestTreeNode("sibling");
            TestTreeNode otherParent = new TestTreeNode("otherParent");

            // Setup: parent has [sibling, baseNode] (baseNode at end)
            parent.addChild(sibling);
            parent.addChild(baseNode);
            otherParent.addChild(moveNode);

            moveAfterTransform.execute();

            // moveNode should be inserted after baseNode (at the end)
            assertThat(parent.getNumChildren()).isEqualTo(3);
            assertThat(parent.getChild(0)).isSameAs(sibling);
            assertThat(parent.getChild(1)).isSameAs(baseNode);
            assertThat(parent.getChild(2)).isSameAs(moveNode);
        }

        @Test
        @DisplayName("execute should handle base node as only child")
        void testExecute_WithBaseNodeAsOnlyChild() {
            TestTreeNode parent = new TestTreeNode("parent");
            TestTreeNode otherParent = new TestTreeNode("otherParent");

            // Setup: parent has only baseNode
            parent.addChild(baseNode);
            otherParent.addChild(moveNode);

            moveAfterTransform.execute();

            // moveNode should be inserted after baseNode
            assertThat(parent.getNumChildren()).isEqualTo(2);
            assertThat(parent.getChild(0)).isSameAs(baseNode);
            assertThat(parent.getChild(1)).isSameAs(moveNode);
        }

        @Test
        @DisplayName("execute should handle move node without parent")
        void testExecute_WithMoveNodeWithoutParent() {
            TestTreeNode parent = new TestTreeNode("parent");
            TestTreeNode sibling = new TestTreeNode("sibling");

            parent.addChild(sibling);
            parent.addChild(baseNode);
            // moveNode has no parent

            assertThat(moveNode.getParent()).isNull();

            moveAfterTransform.execute();

            assertThat(parent.getNumChildren()).isEqualTo(3);
            assertThat(parent.getChild(1)).isSameAs(baseNode);
            assertThat(parent.getChild(2)).isSameAs(moveNode);
            assertThat(moveNode.getParent()).isSameAs(parent);
        }

        @Test
        @DisplayName("execute should handle move node within same parent")
        void testExecute_WithMoveNodeInSameParent() {
            TestTreeNode parent = new TestTreeNode("parent");
            TestTreeNode sibling1 = new TestTreeNode("sibling1");
            TestTreeNode sibling2 = new TestTreeNode("sibling2");

            // Setup: parent has [sibling1, moveNode, baseNode, sibling2]
            parent.addChild(sibling1);
            parent.addChild(moveNode);
            parent.addChild(baseNode);
            parent.addChild(sibling2);

            moveAfterTransform.execute();

            // Due to the order of operations in NodeInsertUtils.insertAfter:
            // 1. Calculate insert index (baseNode was at 2, so insert at 3)
            // 2. Remove moveNode: [sibling1, baseNode, sibling2]
            // 3. Insert at index 3: [sibling1, baseNode, sibling2, moveNode]
            assertThat(parent.getNumChildren()).isEqualTo(4);
            assertThat(parent.getChild(0)).isSameAs(sibling1);
            assertThat(parent.getChild(1)).isSameAs(baseNode);
            assertThat(parent.getChild(2)).isSameAs(sibling2);
            assertThat(parent.getChild(3)).isSameAs(moveNode);
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("execute should throw exception with null base node")
        void testExecute_WithNullBaseNode() {
            MoveAfterTransform<TestTreeNode> transform = new MoveAfterTransform<>(null, moveNode);

            assertThatThrownBy(() -> transform.execute())
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("execute should throw exception with null move node")
        void testExecute_WithNullMoveNode() {
            TestTreeNode parent = new TestTreeNode("parent");
            parent.addChild(baseNode);

            MoveAfterTransform<TestTreeNode> transform = new MoveAfterTransform<>(baseNode, null);

            assertThatThrownBy(() -> transform.execute())
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("execute should handle base node without parent gracefully")
        void testExecute_WithBaseNodeWithoutParent() {
            // baseNode has no parent
            assertThat(baseNode.getParent()).isNull();

            // This should handle gracefully by warning and returning
            assertThatCode(() -> moveAfterTransform.execute())
                    .doesNotThrowAnyException();

            // Verify nothing changed
            assertThat(baseNode.getParent()).isNull();
            assertThat(moveNode.getParent()).isNull();
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with different node types")
        void testWithDifferentNodeTypes() {
            TestTreeNode node1 = new TestTreeNode("base");
            TestTreeNode node2 = new TestTreeNode("move");

            MoveAfterTransform<TestTreeNode> transform = new MoveAfterTransform<>(node1, node2);

            assertThat(transform.getType()).isEqualTo("move-after");
            assertThat(transform.getNode1()).isSameAs(node1);
            assertThat(transform.getNode2()).isSameAs(node2);
        }

        @Test
        @DisplayName("Should maintain tree structure integrity after move")
        void testTreeStructureIntegrity() {
            // Build complex tree structure
            TestTreeNode root = new TestTreeNode("root");
            TestTreeNode branch1 = new TestTreeNode("branch1");
            TestTreeNode branch2 = new TestTreeNode("branch2");
            TestTreeNode leaf1 = new TestTreeNode("leaf1");
            TestTreeNode leaf2 = new TestTreeNode("leaf2");

            root.addChild(branch1);
            root.addChild(branch2);
            branch1.addChild(baseNode);
            branch1.addChild(leaf1);
            branch2.addChild(moveNode);
            branch2.addChild(leaf2);

            MoveAfterTransform<TestTreeNode> transform = new MoveAfterTransform<>(baseNode, moveNode);
            transform.execute();

            // Verify tree structure integrity
            assertThat(root.getNumChildren()).isEqualTo(2);
            assertThat(branch1.getNumChildren()).isEqualTo(3); // baseNode, moveNode, leaf1
            assertThat(branch2.getNumChildren()).isEqualTo(1); // only leaf2

            assertThat(branch1.getChild(0)).isSameAs(baseNode);
            assertThat(branch1.getChild(1)).isSameAs(moveNode);
            assertThat(branch1.getChild(2)).isSameAs(leaf1);
            assertThat(branch2.getChild(0)).isSameAs(leaf2);
        }

        @Test
        @DisplayName("Should work correctly when move node has children")
        void testMoveNodeWithChildren() {
            TestTreeNode parent = new TestTreeNode("parent");
            TestTreeNode otherParent = new TestTreeNode("otherParent");
            TestTreeNode child1 = new TestTreeNode("child1");
            TestTreeNode child2 = new TestTreeNode("child2");

            parent.addChild(baseNode);
            otherParent.addChild(moveNode);
            moveNode.addChild(child1);
            moveNode.addChild(child2);

            assertThat(moveNode.getNumChildren()).isEqualTo(2);

            moveAfterTransform.execute();

            // moveNode should be moved with its children
            assertThat(parent.getNumChildren()).isEqualTo(2);
            assertThat(parent.getChild(1)).isSameAs(moveNode);
            assertThat(moveNode.getNumChildren()).isEqualTo(2);
            assertThat(moveNode.getChild(0)).isSameAs(child1);
            assertThat(moveNode.getChild(1)).isSameAs(child2);
            assertThat(child1.getParent()).isSameAs(moveNode);
            assertThat(child2.getParent()).isSameAs(moveNode);
        }

        @Test
        @DisplayName("Should work in deeply nested structures")
        void testDeeplyNestedStructures() {
            TestTreeNode root = new TestTreeNode("root");
            TestTreeNode current = root;

            // Create deep nesting
            for (int i = 0; i < 5; i++) {
                TestTreeNode next = new TestTreeNode("level" + i);
                current.addChild(next);
                current = next;
            }

            current.addChild(baseNode);
            TestTreeNode deepMoveNode = new TestTreeNode("deepMove");
            current.addChild(deepMoveNode);

            MoveAfterTransform<TestTreeNode> transform = new MoveAfterTransform<>(baseNode, deepMoveNode);
            transform.execute();

            // Should work correctly even in deep structure
            assertThat(current.getNumChildren()).isEqualTo(2);
            assertThat(current.getChild(0)).isSameAs(baseNode);
            assertThat(current.getChild(1)).isSameAs(deepMoveNode);
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle move after operation when nodes are same")
        void testMoveAfterSameNode() {
            TestTreeNode parent = new TestTreeNode("parent");
            parent.addChild(baseNode);

            MoveAfterTransform<TestTreeNode> transform = new MoveAfterTransform<>(baseNode, baseNode);

            // This might cause issues since we're moving a node after itself
            assertThatThrownBy(() -> transform.execute())
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should be idempotent when called multiple times")
        void testIdempotent() {
            TestTreeNode parent = new TestTreeNode("parent");
            TestTreeNode sibling = new TestTreeNode("sibling");
            TestTreeNode otherParent = new TestTreeNode("otherParent");

            parent.addChild(sibling);
            parent.addChild(baseNode);
            otherParent.addChild(moveNode);

            // Execute first time
            moveAfterTransform.execute();

            assertThat(parent.getNumChildren()).isEqualTo(3);
            assertThat(parent.getChild(2)).isSameAs(moveNode);

            // Execute second time - should not change anything
            moveAfterTransform.execute();

            assertThat(parent.getNumChildren()).isEqualTo(3);
            assertThat(parent.getChild(0)).isSameAs(sibling);
            assertThat(parent.getChild(1)).isSameAs(baseNode);
            assertThat(parent.getChild(2)).isSameAs(moveNode);
        }

        @Test
        @DisplayName("Should handle multiple consecutive move operations")
        void testMultipleConsecutiveMoves() {
            TestTreeNode parent = new TestTreeNode("parent");
            TestTreeNode node1 = new TestTreeNode("node1");
            TestTreeNode node2 = new TestTreeNode("node2");
            TestTreeNode node3 = new TestTreeNode("node3");
            TestTreeNode otherParent = new TestTreeNode("otherParent");

            parent.addChild(node1);
            parent.addChild(baseNode);
            otherParent.addChild(node2);
            otherParent.addChild(node3);

            MoveAfterTransform<TestTreeNode> move1 = new MoveAfterTransform<>(baseNode, node2);
            MoveAfterTransform<TestTreeNode> move2 = new MoveAfterTransform<>(node2, node3);

            move1.execute();
            move2.execute();

            // Final order should be: node1, baseNode, node2, node3
            assertThat(parent.getNumChildren()).isEqualTo(4);
            assertThat(parent.getChild(0)).isSameAs(node1);
            assertThat(parent.getChild(1)).isSameAs(baseNode);
            assertThat(parent.getChild(2)).isSameAs(node2);
            assertThat(parent.getChild(3)).isSameAs(node3);
            assertThat(otherParent.getNumChildren()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should handle empty parent scenarios")
        void testEmptyParentScenarios() {
            TestTreeNode parent = new TestTreeNode("parent");
            TestTreeNode emptyParent = new TestTreeNode("emptyParent");

            parent.addChild(baseNode);
            // moveNode has no parent initially

            assertThat(emptyParent.getNumChildren()).isEqualTo(0);

            moveAfterTransform.execute();

            assertThat(parent.getNumChildren()).isEqualTo(2);
            assertThat(parent.getChild(0)).isSameAs(baseNode);
            assertThat(parent.getChild(1)).isSameAs(moveNode);
            assertThat(emptyParent.getNumChildren()).isEqualTo(0);
        }
    }
}
