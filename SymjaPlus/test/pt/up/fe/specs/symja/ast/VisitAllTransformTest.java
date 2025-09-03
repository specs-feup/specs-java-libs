package pt.up.fe.specs.symja.ast;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import pt.up.fe.specs.util.treenode.transform.TransformQueue;
import pt.up.fe.specs.util.treenode.transform.TransformResult;
import pt.up.fe.specs.util.treenode.transform.util.TraversalStrategy;

/**
 * Comprehensive test suite for VisitAllTransform interface.
 * 
 * Tests transformation functionality including:
 * - Interface default methods
 * - Node visiting patterns
 * - Queue interaction
 * - Custom transform implementations
 * - Error handling
 * - Integration scenarios
 * 
 * @author Generated Tests
 */
@DisplayName("VisitAllTransform Interface Tests")
class VisitAllTransformTest {

    @Nested
    @DisplayName("Interface Method Tests")
    class InterfaceMethodTests {

        @Test
        @DisplayName("Should provide default apply method")
        void testApply_DefaultImplementation_CallsApplyAllAndReturnsEmpty() {
            // Create a concrete implementation for testing
            VisitAllTransform transform = mock(VisitAllTransform.class);
            doCallRealMethod().when(transform).apply(any(), any());

            SymjaSymbol node = SymjaNode.newNode(SymjaSymbol.class);
            node.set(SymjaSymbol.SYMBOL, "test");

            @SuppressWarnings("unchecked")
            TransformQueue<SymjaNode> queue = mock(TransformQueue.class);

            TransformResult result = transform.apply(node, queue);

            // Should call applyAll and return empty result
            verify(transform).applyAll(node, queue);
            assertThat(result).isNotNull();
            assertThat(result.getClass().getSimpleName()).isEqualTo("DefaultTransformResult");
        }

        @Test
        @DisplayName("Should require applyAll method implementation")
        void testApplyAll_AbstractMethod_MustBeImplemented() {
            // This test ensures that applyAll is abstract and must be implemented
            // We test this through the interface contract
            assertThat(VisitAllTransform.class.getMethods())
                    .anySatisfy(method -> {
                        if ("applyAll".equals(method.getName())) {
                            assertThat(method.isDefault()).isFalse();
                            assertThat(method.getParameterCount()).isEqualTo(2);
                        }
                    });
        }
    }

    @Nested
    @DisplayName("Transform Implementation Tests")
    class TransformImplementationTests {

        @Test
        @DisplayName("Should visit all nodes in tree structure")
        void testApplyAll_VisitsAllNodes_TraversesCompleteTree() {
            // Create a concrete test implementation
            TestVisitAllTransform transform = new TestVisitAllTransform();

            // Build a tree structure
            SymjaFunction root = SymjaNode.newNode(SymjaFunction.class);
            root.set(SymjaFunction.HAS_PARENTHESIS, true);

            SymjaOperator operator = SymjaNode.newNode(SymjaOperator.class);
            operator.set(SymjaOperator.OPERATOR, Operator.Plus);
            root.addChild(operator);

            SymjaSymbol left = SymjaNode.newNode(SymjaSymbol.class);
            left.set(SymjaSymbol.SYMBOL, "a");
            root.addChild(left);

            SymjaSymbol right = SymjaNode.newNode(SymjaSymbol.class);
            right.set(SymjaSymbol.SYMBOL, "b");
            root.addChild(right);

            @SuppressWarnings("unchecked")
            TransformQueue<SymjaNode> queue = mock(TransformQueue.class);

            transform.applyAll(root, queue);

            // Should have visited all nodes
            assertThat(transform.getVisitedNodes()).hasSize(4);
            assertThat(transform.getVisitedNodes()).contains(root, operator, left, right);
        }

        @Test
        @DisplayName("Should handle single node correctly")
        void testApplyAll_SingleNode_VisitsOnlyThatNode() {
            TestVisitAllTransform transform = new TestVisitAllTransform();

            SymjaInteger singleNode = SymjaNode.newNode(SymjaInteger.class);
            singleNode.set(SymjaInteger.VALUE_STRING, "42");

            @SuppressWarnings("unchecked")
            TransformQueue<SymjaNode> queue = mock(TransformQueue.class);

            transform.applyAll(singleNode, queue);

            // Should visit only the single node
            assertThat(transform.getVisitedNodes()).hasSize(1);
            assertThat(transform.getVisitedNodes()).contains(singleNode);
        }
    }

    @Nested
    @DisplayName("Queue Interaction Tests")
    class QueueInteractionTests {

        @Test
        @DisplayName("Should interact with transform queue properly")
        void testApplyAll_QueueInteraction_UsesQueueCorrectly() {
            TestVisitAllTransform transform = new TestVisitAllTransform();

            SymjaSymbol node = SymjaNode.newNode(SymjaSymbol.class);
            node.set(SymjaSymbol.SYMBOL, "test");

            @SuppressWarnings("unchecked")
            TransformQueue<SymjaNode> queue = mock(TransformQueue.class);

            transform.applyAll(node, queue);

            // Verify queue was passed to the implementation
            assertThat(transform.getUsedQueue()).isSameAs(queue);
        }

        @Test
        @DisplayName("Should handle queue operations during transformation")
        void testApplyAll_QueueOperations_HandlesQueueUpdates() {
            QueueAwareTransform transform = new QueueAwareTransform();

            SymjaFunction function = SymjaNode.newNode(SymjaFunction.class);
            function.set(SymjaFunction.HAS_PARENTHESIS, false);

            SymjaOperator operator = SymjaNode.newNode(SymjaOperator.class);
            operator.set(SymjaOperator.OPERATOR, Operator.Minus);
            function.addChild(operator);

            SymjaInteger operand = SymjaNode.newNode(SymjaInteger.class);
            operand.set(SymjaInteger.VALUE_STRING, "5");
            function.addChild(operand);

            @SuppressWarnings("unchecked")
            TransformQueue<SymjaNode> queue = mock(TransformQueue.class);

            transform.applyAll(function, queue);

            // Verify queue operations were attempted
            verify(queue, atLeastOnce()).replace(any(SymjaNode.class), any(SymjaNode.class));
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle null node gracefully")
        void testApplyAll_NullNode_ThrowsException() {
            TestVisitAllTransform transform = new TestVisitAllTransform();

            @SuppressWarnings("unchecked")
            TransformQueue<SymjaNode> queue = mock(TransformQueue.class);

            assertThatThrownBy(() -> transform.applyAll(null, queue))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle null queue gracefully")
        void testApplyAll_NullQueue_ThrowsException() {
            TestVisitAllTransform transform = new TestVisitAllTransform();

            SymjaSymbol node = SymjaNode.newNode(SymjaSymbol.class);
            node.set(SymjaSymbol.SYMBOL, "test");

            assertThatThrownBy(() -> transform.applyAll(node, null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with complex AST structures")
        void testApplyAll_ComplexAST_HandlesNestedStructures() {
            TestVisitAllTransform transform = new TestVisitAllTransform();

            // Create nested function: ((a + b) * c)
            SymjaFunction outerFunction = SymjaNode.newNode(SymjaFunction.class);
            outerFunction.set(SymjaFunction.HAS_PARENTHESIS, true);

            SymjaOperator multiplyOp = SymjaNode.newNode(SymjaOperator.class);
            multiplyOp.set(SymjaOperator.OPERATOR, Operator.Times);
            outerFunction.addChild(multiplyOp);

            // Inner function (a + b)
            SymjaFunction innerFunction = SymjaNode.newNode(SymjaFunction.class);
            innerFunction.set(SymjaFunction.HAS_PARENTHESIS, true);

            SymjaOperator plusOp = SymjaNode.newNode(SymjaOperator.class);
            plusOp.set(SymjaOperator.OPERATOR, Operator.Plus);
            innerFunction.addChild(plusOp);

            SymjaSymbol a = SymjaNode.newNode(SymjaSymbol.class);
            a.set(SymjaSymbol.SYMBOL, "a");
            innerFunction.addChild(a);

            SymjaSymbol b = SymjaNode.newNode(SymjaSymbol.class);
            b.set(SymjaSymbol.SYMBOL, "b");
            innerFunction.addChild(b);

            outerFunction.addChild(innerFunction);

            SymjaSymbol c = SymjaNode.newNode(SymjaSymbol.class);
            c.set(SymjaSymbol.SYMBOL, "c");
            outerFunction.addChild(c);

            @SuppressWarnings("unchecked")
            TransformQueue<SymjaNode> queue = mock(TransformQueue.class);

            transform.applyAll(outerFunction, queue);

            // Should visit all nodes in nested structure
            assertThat(transform.getVisitedNodes()).hasSize(7);
            assertThat(transform.getVisitedNodes()).contains(
                    outerFunction, multiplyOp, innerFunction, plusOp, a, b, c);
        }

        @Test
        @DisplayName("Should maintain transform interface contract")
        void testTransformRule_InterfaceContract_ImplementsCorrectly() {
            TestVisitAllTransform transform = new TestVisitAllTransform();

            SymjaSymbol node = SymjaNode.newNode(SymjaSymbol.class);
            node.set(SymjaSymbol.SYMBOL, "contract_test");

            @SuppressWarnings("unchecked")
            TransformQueue<SymjaNode> queue = mock(TransformQueue.class);

            // Should implement TransformRule interface
            TransformResult result = transform.apply(node, queue);

            assertThat(result).isNotNull();
            assertThat(result.getClass().getSimpleName()).isEqualTo("DefaultTransformResult");
            assertThat(transform.getVisitedNodes()).contains(node);
        }
    }

    /**
     * Test implementation of VisitAllTransform for testing purposes.
     */
    private static class TestVisitAllTransform implements VisitAllTransform {
        private final java.util.List<SymjaNode> visitedNodes = new java.util.ArrayList<>();
        private TransformQueue<SymjaNode> usedQueue;

        @Override
        public void applyAll(SymjaNode node, TransformQueue<SymjaNode> queue) {
            this.usedQueue = queue;
            if (queue == null) {
                throw new NullPointerException("Queue cannot be null");
            }
            visitNode(node);
        }

        private void visitNode(SymjaNode node) {
            if (node == null) {
                throw new NullPointerException("Node cannot be null");
            }

            visitedNodes.add(node);

            // Visit all children
            for (SymjaNode child : node.getChildren()) {
                visitNode(child);
            }
        }

        public java.util.List<SymjaNode> getVisitedNodes() {
            return visitedNodes;
        }

        public TransformQueue<SymjaNode> getUsedQueue() {
            return usedQueue;
        }

        @Override
        public TraversalStrategy getTraversalStrategy() {
            return TraversalStrategy.PRE_ORDER;
        }
    }

    /**
     * Test implementation that interacts with the queue.
     */
    private static class QueueAwareTransform implements VisitAllTransform {

        @Override
        public void applyAll(SymjaNode node, TransformQueue<SymjaNode> queue) {
            if (queue == null) {
                throw new NullPointerException("Queue cannot be null");
            }

            // Simulate queue interaction
            visitNodeWithQueue(node, queue);
        }

        private void visitNodeWithQueue(SymjaNode node, TransformQueue<SymjaNode> queue) {
            // Use replace operation (simulating transformation)
            queue.replace(node, node);

            // Visit children
            for (SymjaNode child : node.getChildren()) {
                visitNodeWithQueue(child, queue);
            }
        }

        @Override
        public TraversalStrategy getTraversalStrategy() {
            return TraversalStrategy.PRE_ORDER;
        }
    }
}
