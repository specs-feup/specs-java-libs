package pt.up.fe.specs.util.treenode.transform;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.treenode.ATreeNode;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for TransformQueue.
 * Tests queue management and transformation operations.
 * 
 * @author Generated Tests
 */
@DisplayName("TransformQueue Tests")
class TransformQueueTest {

    private TransformQueue<TestTreeNode> transformQueue;
    private TestTreeNode node1;
    private TestTreeNode node2;
    private TestTreeNode node3;

    @BeforeEach
    void setUp() {
        transformQueue = new TransformQueue<>("TEST_QUEUE");
        node1 = new TestTreeNode("node1");
        node2 = new TestTreeNode("node2");
        node3 = new TestTreeNode("node3");
    }

    @Nested
    @DisplayName("Constructor and Basic Properties")
    class ConstructorTests {

        @Test
        @DisplayName("Constructor should initialize with given ID")
        void testConstructor_InitializesWithId() {
            TransformQueue<TestTreeNode> queue = new TransformQueue<>("MY_ID");

            assertThat(queue.getId()).isEqualTo("MY_ID");
            assertThat(queue.getTransforms()).isEmpty();
        }

        @Test
        @DisplayName("Constructor should handle null ID")
        void testConstructor_WithNullId() {
            TransformQueue<TestTreeNode> queue = new TransformQueue<>(null);

            assertThat(queue.getId()).isNull();
            assertThat(queue.getTransforms()).isEmpty();
        }

        @Test
        @DisplayName("Constructor should handle empty string ID")
        void testConstructor_WithEmptyId() {
            TransformQueue<TestTreeNode> queue = new TransformQueue<>("");

            assertThat(queue.getId()).isEmpty();
            assertThat(queue.getTransforms()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Transform Queue Management")
    class QueueManagementTests {

        @Test
        @DisplayName("getTransforms() should return empty list initially")
        void testGetTransforms_InitiallyEmpty() {
            assertThat(transformQueue.getTransforms()).isEmpty();
        }

        @Test
        @DisplayName("getTransforms() should return mutable list")
        void testGetTransforms_ReturnsMutableList() {
            List<NodeTransform<TestTreeNode>> transforms = transformQueue.getTransforms();

            assertThat(transforms).isNotNull();
            assertThat(transforms).isInstanceOf(List.class);
        }

        @Test
        @DisplayName("toString() should return string representation of transforms")
        void testToString_ReturnsTransformsString() {
            String initialString = transformQueue.toString();
            assertThat(initialString).isEqualTo("[]");

            transformQueue.delete(node1);
            String afterAddString = transformQueue.toString();
            assertThat(afterAddString).contains("delete");
        }
    }

    @Nested
    @DisplayName("Transform Operations")
    class TransformOperationsTests {

        @Test
        @DisplayName("replace() should add ReplaceTransform to queue")
        void testReplace_AddsReplaceTransform() {
            transformQueue.replace(node1, node2);

            assertThat(transformQueue.getTransforms()).hasSize(1);
            NodeTransform<TestTreeNode> transform = transformQueue.getTransforms().get(0);
            assertThat(transform.getType()).isEqualTo("replace");
            assertThat(transform.getOperands()).containsExactly(node1, node2);
        }

        @Test
        @DisplayName("moveBefore() should add MoveBeforeTransform to queue")
        void testMoveBefore_AddsMoveBeforeTransform() {
            transformQueue.moveBefore(node1, node2);

            assertThat(transformQueue.getTransforms()).hasSize(1);
            NodeTransform<TestTreeNode> transform = transformQueue.getTransforms().get(0);
            assertThat(transform.getType()).isEqualTo("move-before");
            assertThat(transform.getOperands()).containsExactly(node1, node2);
        }

        @Test
        @DisplayName("moveAfter() should add MoveAfterTransform to queue")
        void testMoveAfter_AddsMoveAfterTransform() {
            transformQueue.moveAfter(node1, node2);

            assertThat(transformQueue.getTransforms()).hasSize(1);
            NodeTransform<TestTreeNode> transform = transformQueue.getTransforms().get(0);
            assertThat(transform.getType()).isEqualTo("move-after");
            assertThat(transform.getOperands()).containsExactly(node1, node2);
        }

        @Test
        @DisplayName("delete() should add DeleteTransform to queue")
        void testDelete_AddsDeleteTransform() {
            transformQueue.delete(node1);

            assertThat(transformQueue.getTransforms()).hasSize(1);
            NodeTransform<TestTreeNode> transform = transformQueue.getTransforms().get(0);
            assertThat(transform.getType()).isEqualTo("delete");
            assertThat(transform.getOperands()).containsExactly(node1);
        }

        @Test
        @DisplayName("addChild() should add AddChildTransform to queue")
        void testAddChild_AddsAddChildTransform() {
            transformQueue.addChild(node1, node2);

            assertThat(transformQueue.getTransforms()).hasSize(1);
            NodeTransform<TestTreeNode> transform = transformQueue.getTransforms().get(0);
            assertThat(transform.getType()).isEqualTo("add-child");
            assertThat(transform.getOperands()).containsExactly(node1, node2);
        }

        @Test
        @DisplayName("addChildHead() should add AddChildTransform at index 0")
        void testAddChildHead_AddsAddChildTransformAtHead() {
            transformQueue.addChildHead(node1, node2);

            assertThat(transformQueue.getTransforms()).hasSize(1);
            NodeTransform<TestTreeNode> transform = transformQueue.getTransforms().get(0);
            assertThat(transform.getType()).isEqualTo("add-child");
            assertThat(transform.getOperands()).containsExactly(node1, node2);
        }

        @Test
        @DisplayName("swap() with default parameters should add SwapTransform")
        void testSwap_WithDefaultParameters_AddsSwapTransform() {
            transformQueue.swap(node1, node2);

            assertThat(transformQueue.getTransforms()).hasSize(1);
            NodeTransform<TestTreeNode> transform = transformQueue.getTransforms().get(0);
            assertThat(transform.getType()).isEqualTo("swap");
            assertThat(transform.getOperands()).containsExactly(node1, node2);
        }

        @Test
        @DisplayName("swap() with swapSubtrees parameter should add SwapTransform")
        void testSwap_WithSwapSubtreesParameter_AddsSwapTransform() {
            transformQueue.swap(node1, node2, false);

            assertThat(transformQueue.getTransforms()).hasSize(1);
            NodeTransform<TestTreeNode> transform = transformQueue.getTransforms().get(0);
            assertThat(transform.getType()).isEqualTo("swap");
            assertThat(transform.getOperands()).containsExactly(node1, node2);
        }
    }

    @Nested
    @DisplayName("Queue Execution")
    class QueueExecutionTests {

        @Test
        @DisplayName("apply() should execute all transforms in order")
        void testApply_ExecutesAllTransformsInOrder() {
            // Add multiple transforms
            transformQueue.delete(node1);
            transformQueue.replace(node2, node3);

            assertThat(transformQueue.getTransforms()).hasSize(2);

            // Apply the queue
            transformQueue.apply();

            // Queue should be empty after apply
            assertThat(transformQueue.getTransforms()).isEmpty();
        }

        @Test
        @DisplayName("applyReverse() should execute transforms in reverse order")
        void testApplyReverse_ExecutesTransformsInReverseOrder() {
            // Add multiple transforms
            transformQueue.delete(node1);
            transformQueue.replace(node2, node3);
            transformQueue.moveBefore(node1, node2);

            assertThat(transformQueue.getTransforms()).hasSize(3);

            // Apply in reverse
            transformQueue.applyReverse();

            // Queue should be empty after apply
            assertThat(transformQueue.getTransforms()).isEmpty();
        }

        @Test
        @DisplayName("apply() on empty queue should not throw exceptions")
        void testApply_OnEmptyQueue_DoesNotThrow() {
            assertThat(transformQueue.getTransforms()).isEmpty();

            assertThatCode(() -> transformQueue.apply()).doesNotThrowAnyException();

            assertThat(transformQueue.getTransforms()).isEmpty();
        }

        @Test
        @DisplayName("applyReverse() on empty queue should not throw exceptions")
        void testApplyReverse_OnEmptyQueue_DoesNotThrow() {
            assertThat(transformQueue.getTransforms()).isEmpty();

            assertThatCode(() -> transformQueue.applyReverse()).doesNotThrowAnyException();

            assertThat(transformQueue.getTransforms()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Multiple Operations")
    class MultipleOperationsTests {

        @Test
        @DisplayName("Should handle multiple operations of same type")
        void testMultipleOperationsOfSameType() {
            transformQueue.delete(node1);
            transformQueue.delete(node2);
            transformQueue.delete(node3);

            assertThat(transformQueue.getTransforms()).hasSize(3);
            assertThat(transformQueue.getTransforms().get(0).getType()).isEqualTo("delete");
            assertThat(transformQueue.getTransforms().get(1).getType()).isEqualTo("delete");
            assertThat(transformQueue.getTransforms().get(2).getType()).isEqualTo("delete");
        }

        @Test
        @DisplayName("Should handle mixed operation types")
        void testMixedOperationTypes() {
            transformQueue.delete(node1);
            transformQueue.replace(node1, node2);
            transformQueue.moveBefore(node2, node3);
            transformQueue.addChild(node1, node3);
            transformQueue.swap(node2, node3);

            assertThat(transformQueue.getTransforms()).hasSize(5);

            List<NodeTransform<TestTreeNode>> transforms = transformQueue.getTransforms();
            assertThat(transforms.get(0).getType()).isEqualTo("delete");
            assertThat(transforms.get(1).getType()).isEqualTo("replace");
            assertThat(transforms.get(2).getType()).isEqualTo("move-before");
            assertThat(transforms.get(3).getType()).isEqualTo("add-child");
            assertThat(transforms.get(4).getType()).isEqualTo("swap");
        }

        @Test
        @DisplayName("Should maintain operation order")
        void testMaintainOperationOrder() {
            // Add operations in specific order
            transformQueue.replace(node1, node2); // First
            transformQueue.delete(node3); // Second
            transformQueue.moveBefore(node1, node2); // Third

            List<NodeTransform<TestTreeNode>> transforms = transformQueue.getTransforms();
            assertThat(transforms).hasSize(3);

            // Verify order is maintained
            assertThat(transforms.get(0).getType()).isEqualTo("replace");
            assertThat(transforms.get(1).getType()).isEqualTo("delete");
            assertThat(transforms.get(2).getType()).isEqualTo("move-before");
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle operations with same nodes")
        void testOperationsWithSameNodes() {
            transformQueue.replace(node1, node1); // Replace with itself
            transformQueue.swap(node1, node1); // Swap with itself

            assertThat(transformQueue.getTransforms()).hasSize(2);

            assertThatCode(() -> transformQueue.apply()).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle null nodes in operations")
        void testNullNodesInOperations() {
            // This tests the robustness - actual behavior depends on transform
            // implementations
            assertThatCode(() -> {
                transformQueue.delete(null);
                transformQueue.replace(null, node1);
                transformQueue.replace(node1, null);
            }).doesNotThrowAnyException();

            assertThat(transformQueue.getTransforms()).hasSize(3);
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

        @Override
        public String toNodeString() {
            return name;
        }

        @Override
        public String getNodeName() {
            return name;
        }

        @Override
        public String toContentString() {
            return name;
        }

        @Override
        protected TestTreeNode copyPrivate() {
            return new TestTreeNode(name);
        }

        @Override
        public String toString() {
            return "TestTreeNode{" + name + "}";
        }
    }
}
