package pt.up.fe.specs.util.treenode.transform.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.treenode.ATreeNode;
import pt.up.fe.specs.util.treenode.transform.TransformQueue;
import pt.up.fe.specs.util.treenode.transform.TransformResult;
import pt.up.fe.specs.util.treenode.transform.TransformRule;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for TraversalStrategy enum.
 * Tests both PRE_ORDER and POST_ORDER traversal strategies.
 * 
 * @author Generated Tests
 */
@DisplayName("TraversalStrategy Tests")
class TraversalStrategyTest {

    private TestTreeNode root;
    private TestTreeNode child1;
    private TestTreeNode child2;
    private TestTreeNode grandchild1;
    private TestTreeNode grandchild2;
    private TrackingTransformRule trackingRule;

    @BeforeEach
    void setUp() {
        // Create tree structure: root -> [child1, child2], child1 -> [grandchild1,
        // grandchild2]
        root = new TestTreeNode("root");
        child1 = new TestTreeNode("child1");
        child2 = new TestTreeNode("child2");
        grandchild1 = new TestTreeNode("grandchild1");
        grandchild2 = new TestTreeNode("grandchild2");

        root.addChild(child1);
        root.addChild(child2);
        child1.addChild(grandchild1);
        child1.addChild(grandchild2);

        trackingRule = new TrackingTransformRule();
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
     * Test transform result implementation.
     */
    private static class TestTransformResult implements TransformResult {
        private final boolean visitChildren;

        public TestTransformResult(boolean visitChildren) {
            this.visitChildren = visitChildren;
        }

        @Override
        public boolean visitChildren() {
            return visitChildren;
        }
    }

    /**
     * Transform rule that tracks the order of node visits.
     */
    private static class TrackingTransformRule implements TransformRule<TestTreeNode, TestTransformResult> {
        private final List<String> visitOrder = new ArrayList<>();
        private final TraversalStrategy strategy;
        private final boolean continueVisiting;

        public TrackingTransformRule() {
            this(TraversalStrategy.PRE_ORDER, true);
        }

        public TrackingTransformRule(TraversalStrategy strategy) {
            this(strategy, true);
        }

        public TrackingTransformRule(TraversalStrategy strategy, boolean continueVisiting) {
            this.strategy = strategy;
            this.continueVisiting = continueVisiting;
        }

        @Override
        public TestTransformResult apply(TestTreeNode node, TransformQueue<TestTreeNode> queue) {
            visitOrder.add(node.toContentString());
            return new TestTransformResult(continueVisiting);
        }

        @Override
        public TraversalStrategy getTraversalStrategy() {
            return strategy;
        }

        public List<String> getVisitOrder() {
            return new ArrayList<>(visitOrder);
        }

        @SuppressWarnings("unused")
        public void reset() {
            visitOrder.clear();
        }
    }

    @Nested
    @DisplayName("Enum Constants Tests")
    class EnumConstantsTests {

        @Test
        @DisplayName("Should have PRE_ORDER constant")
        void testPreOrderConstant() {
            assertThat(TraversalStrategy.PRE_ORDER).isNotNull();
            assertThat(TraversalStrategy.PRE_ORDER.name()).isEqualTo("PRE_ORDER");
        }

        @Test
        @DisplayName("Should have POST_ORDER constant")
        void testPostOrderConstant() {
            assertThat(TraversalStrategy.POST_ORDER).isNotNull();
            assertThat(TraversalStrategy.POST_ORDER.name()).isEqualTo("POST_ORDER");
        }

        @Test
        @DisplayName("Should have exactly two enum constants")
        void testEnumConstantsCount() {
            TraversalStrategy[] values = TraversalStrategy.values();

            assertThat(values).hasSize(2);
            assertThat(values).contains(TraversalStrategy.PRE_ORDER, TraversalStrategy.POST_ORDER);
        }

        @Test
        @DisplayName("valueOf should work correctly")
        void testValueOf() {
            assertThat(TraversalStrategy.valueOf("PRE_ORDER")).isSameAs(TraversalStrategy.PRE_ORDER);
            assertThat(TraversalStrategy.valueOf("POST_ORDER")).isSameAs(TraversalStrategy.POST_ORDER);

            assertThatThrownBy(() -> TraversalStrategy.valueOf("INVALID"))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("PRE_ORDER Traversal Tests")
    class PreOrderTraversalTests {

        @Test
        @DisplayName("Should visit nodes in pre-order sequence")
        void testPreOrderSequence() {
            TrackingTransformRule rule = new TrackingTransformRule(TraversalStrategy.PRE_ORDER);

            TraversalStrategy.PRE_ORDER.apply(root, rule);

            List<String> visitOrder = rule.getVisitOrder();
            assertThat(visitOrder).containsExactly("root", "child1", "grandchild1", "grandchild2", "child2");
        }

        @Test
        @DisplayName("Should respect visitChildren result")
        void testPreOrderRespectsVisitChildren() {
            // Rule that stops visiting children after root
            TrackingTransformRule rule = new TrackingTransformRule(TraversalStrategy.PRE_ORDER) {
                @Override
                public TestTransformResult apply(TestTreeNode node, TransformQueue<TestTreeNode> queue) {
                    super.apply(node, queue);
                    // Only visit children of root
                    return new TestTransformResult(!node.toContentString().equals("child1"));
                }
            };

            TraversalStrategy.PRE_ORDER.apply(root, rule);

            List<String> visitOrder = rule.getVisitOrder();
            // Should visit root, child1 (but not its children), child2
            assertThat(visitOrder).containsExactly("root", "child1", "child2");
        }

        @Test
        @DisplayName("Should work with single node")
        void testPreOrderWithSingleNode() {
            TestTreeNode singleNode = new TestTreeNode("single");
            TrackingTransformRule rule = new TrackingTransformRule(TraversalStrategy.PRE_ORDER);

            TraversalStrategy.PRE_ORDER.apply(singleNode, rule);

            assertThat(rule.getVisitOrder()).containsExactly("single");
        }

        @Test
        @DisplayName("Should work with linear tree")
        void testPreOrderWithLinearTree() {
            TestTreeNode linear1 = new TestTreeNode("linear1");
            TestTreeNode linear2 = new TestTreeNode("linear2");
            TestTreeNode linear3 = new TestTreeNode("linear3");

            linear1.addChild(linear2);
            linear2.addChild(linear3);

            TrackingTransformRule rule = new TrackingTransformRule(TraversalStrategy.PRE_ORDER);
            TraversalStrategy.PRE_ORDER.apply(linear1, rule);

            assertThat(rule.getVisitOrder()).containsExactly("linear1", "linear2", "linear3");
        }
    }

    @Nested
    @DisplayName("POST_ORDER Traversal Tests")
    class PostOrderTraversalTests {

        @Test
        @DisplayName("Should visit nodes in post-order sequence")
        void testPostOrderSequence() {
            TrackingTransformRule rule = new TrackingTransformRule(TraversalStrategy.POST_ORDER);

            TraversalStrategy.POST_ORDER.apply(root, rule);

            List<String> visitOrder = rule.getVisitOrder();
            assertThat(visitOrder).containsExactly("grandchild1", "grandchild2", "child1", "child2", "root");
        }

        @Test
        @DisplayName("Should ignore visitChildren result in post-order")
        void testPostOrderIgnoresVisitChildren() {
            // Rule that returns false for visitChildren
            TrackingTransformRule rule = new TrackingTransformRule(TraversalStrategy.POST_ORDER, false);

            TraversalStrategy.POST_ORDER.apply(root, rule);

            List<String> visitOrder = rule.getVisitOrder();
            // Post-order should visit all nodes regardless of visitChildren result
            assertThat(visitOrder).containsExactly("grandchild1", "grandchild2", "child1", "child2", "root");
        }

        @Test
        @DisplayName("Should work with single node")
        void testPostOrderWithSingleNode() {
            TestTreeNode singleNode = new TestTreeNode("single");
            TrackingTransformRule rule = new TrackingTransformRule(TraversalStrategy.POST_ORDER);

            TraversalStrategy.POST_ORDER.apply(singleNode, rule);

            assertThat(rule.getVisitOrder()).containsExactly("single");
        }

        @Test
        @DisplayName("Should work with linear tree")
        void testPostOrderWithLinearTree() {
            TestTreeNode linear1 = new TestTreeNode("linear1");
            TestTreeNode linear2 = new TestTreeNode("linear2");
            TestTreeNode linear3 = new TestTreeNode("linear3");

            linear1.addChild(linear2);
            linear2.addChild(linear3);

            TrackingTransformRule rule = new TrackingTransformRule(TraversalStrategy.POST_ORDER);
            TraversalStrategy.POST_ORDER.apply(linear1, rule);

            assertThat(rule.getVisitOrder()).containsExactly("linear3", "linear2", "linear1");
        }
    }

    @Nested
    @DisplayName("getTransformations Method Tests")
    class GetTransformationsMethodTests {

        @Test
        @DisplayName("Should return TransformQueue with correct ID")
        void testGetTransformationsReturnsQueueWithCorrectId() {
            TransformQueue<TestTreeNode> queue = TraversalStrategy.PRE_ORDER.getTransformations(root, trackingRule);

            assertThat(queue).isNotNull();
            assertThat(queue.getId()).isEqualTo("TrackingTransformRule");
        }

        @Test
        @DisplayName("Should collect transformations in pre-order")
        void testGetTransformationsPreOrder() {
            TrackingTransformRule rule = new TrackingTransformRule(TraversalStrategy.PRE_ORDER);
            TransformQueue<TestTreeNode> queue = TraversalStrategy.PRE_ORDER.getTransformations(root, rule);

            assertThat(queue).isNotNull();
            List<String> visitOrder = rule.getVisitOrder();
            assertThat(visitOrder).containsExactly("root", "child1", "grandchild1", "grandchild2", "child2");
        }

        @Test
        @DisplayName("Should collect transformations in post-order")
        void testGetTransformationsPostOrder() {
            TrackingTransformRule rule = new TrackingTransformRule(TraversalStrategy.POST_ORDER);
            TransformQueue<TestTreeNode> queue = TraversalStrategy.POST_ORDER.getTransformations(root, rule);

            assertThat(queue).isNotNull();
            List<String> visitOrder = rule.getVisitOrder();
            assertThat(visitOrder).containsExactly("grandchild1", "grandchild2", "child1", "child2", "root");
        }

        @Test
        @DisplayName("Should not execute transformations automatically")
        void testGetTransformationsDoesNotExecute() {
            // Rule that would modify the tree
            TransformRule<TestTreeNode, TestTransformResult> modifyingRule = new TransformRule<TestTreeNode, TestTransformResult>() {
                @Override
                public TestTransformResult apply(TestTreeNode node, TransformQueue<TestTreeNode> queue) {
                    if (node.toContentString().equals("child1")) {
                        queue.delete(node);
                    }
                    return new TestTransformResult(true);
                }

                @Override
                public TraversalStrategy getTraversalStrategy() {
                    return TraversalStrategy.PRE_ORDER;
                }
            };

            int initialChildCount = root.getNumChildren();
            TransformQueue<TestTreeNode> queue = TraversalStrategy.PRE_ORDER.getTransformations(root, modifyingRule);

            // Tree should be unchanged
            assertThat(root.getNumChildren()).isEqualTo(initialChildCount);
            assertThat(queue.getTransforms()).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("Apply Method Tests")
    class ApplyMethodTests {

        @Test
        @DisplayName("Should execute transformations automatically in pre-order")
        void testApplyExecutesTransformationsPreOrder() {
            TransformRule<TestTreeNode, TestTransformResult> deletingRule = new TransformRule<TestTreeNode, TestTransformResult>() {
                @Override
                public TestTransformResult apply(TestTreeNode node, TransformQueue<TestTreeNode> queue) {
                    if (node.toContentString().equals("grandchild1")) {
                        queue.delete(node);
                    }
                    return new TestTransformResult(true);
                }

                @Override
                public TraversalStrategy getTraversalStrategy() {
                    return TraversalStrategy.PRE_ORDER;
                }
            };

            assertThat(child1.getNumChildren()).isEqualTo(2);

            TraversalStrategy.PRE_ORDER.apply(root, deletingRule);

            // grandchild1 should be deleted
            assertThat(child1.getNumChildren()).isEqualTo(1);
            assertThat(child1.getChild(0)).isSameAs(grandchild2);
        }

        @Test
        @DisplayName("Should execute transformations automatically in post-order")
        void testApplyExecutesTransformationsPostOrder() {
            TransformRule<TestTreeNode, TestTransformResult> replacingRule = new TransformRule<TestTreeNode, TestTransformResult>() {
                @Override
                public TestTransformResult apply(TestTreeNode node, TransformQueue<TestTreeNode> queue) {
                    if (node.toContentString().equals("child2")) {
                        TestTreeNode replacement = new TestTreeNode("child2_replaced");
                        queue.replace(node, replacement);
                    }
                    return new TestTransformResult(true);
                }

                @Override
                public TraversalStrategy getTraversalStrategy() {
                    return TraversalStrategy.POST_ORDER;
                }
            };

            TraversalStrategy.POST_ORDER.apply(root, replacingRule);

            // child2 should be replaced
            assertThat(root.getChild(1).toContentString()).isEqualTo("child2_replaced");
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null node gracefully")
        void testWithNullNode() {
            // Null nodes should throw NullPointerException
            assertThatThrownBy(() -> TraversalStrategy.PRE_ORDER.apply(null, trackingRule))
                    .isInstanceOf(NullPointerException.class);

            assertThatThrownBy(() -> TraversalStrategy.POST_ORDER.apply(null, trackingRule))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle null rule gracefully")
        void testWithNullRule() {
            assertThatThrownBy(() -> TraversalStrategy.PRE_ORDER.apply(root, null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle empty tree")
        void testWithEmptyTree() {
            TestTreeNode emptyRoot = new TestTreeNode("empty");
            // No children added

            TrackingTransformRule rule = new TrackingTransformRule(TraversalStrategy.PRE_ORDER);
            TraversalStrategy.PRE_ORDER.apply(emptyRoot, rule);

            assertThat(rule.getVisitOrder()).containsExactly("empty");
        }

        @Test
        @DisplayName("Should handle deep nesting")
        void testWithDeepNesting() {
            TestTreeNode current = new TestTreeNode("level0");
            TestTreeNode root = current;

            // Create deep nesting
            for (int i = 1; i < 10; i++) {
                TestTreeNode next = new TestTreeNode("level" + i);
                current.addChild(next);
                current = next;
            }

            TrackingTransformRule rule = new TrackingTransformRule(TraversalStrategy.PRE_ORDER);
            TraversalStrategy.PRE_ORDER.apply(root, rule);

            List<String> visitOrder = rule.getVisitOrder();
            assertThat(visitOrder).hasSize(10);
            assertThat(visitOrder.get(0)).isEqualTo("level0");
            assertThat(visitOrder.get(9)).isEqualTo("level9");
        }

        @Test
        @DisplayName("Should handle wide trees")
        void testWithWideTree() {
            TestTreeNode wideRoot = new TestTreeNode("wide_root");

            // Add many children
            for (int i = 0; i < 100; i++) {
                wideRoot.addChild(new TestTreeNode("child_" + i));
            }

            TrackingTransformRule rule = new TrackingTransformRule(TraversalStrategy.PRE_ORDER);
            TraversalStrategy.PRE_ORDER.apply(wideRoot, rule);

            List<String> visitOrder = rule.getVisitOrder();
            assertThat(visitOrder).hasSize(101); // root + 100 children
            assertThat(visitOrder.get(0)).isEqualTo("wide_root");
            assertThat(visitOrder.get(1)).isEqualTo("child_0");
            assertThat(visitOrder.get(100)).isEqualTo("child_99");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with complex transformation rules")
        void testWithComplexTransformationRules() {
            TransformRule<TestTreeNode, TestTransformResult> complexRule = new TransformRule<TestTreeNode, TestTransformResult>() {
                @Override
                public TestTransformResult apply(TestTreeNode node, TransformQueue<TestTreeNode> queue) {
                    String content = node.toContentString();

                    if (content.startsWith("grandchild")) {
                        // Replace grandchildren with new nodes
                        TestTreeNode replacement = new TestTreeNode(content + "_new");
                        queue.replace(node, replacement);
                    } else if (content.equals("child2")) {
                        // Add a child to child2
                        TestTreeNode newChild = new TestTreeNode("added_child");
                        queue.addChild(node, newChild);
                    }

                    return new TestTransformResult(true);
                }

                @Override
                public TraversalStrategy getTraversalStrategy() {
                    return TraversalStrategy.PRE_ORDER;
                }
            };

            TraversalStrategy.PRE_ORDER.apply(root, complexRule);

            // Verify transformations were applied
            assertThat(child1.getChild(0).toContentString()).isEqualTo("grandchild1_new");
            assertThat(child1.getChild(1).toContentString()).isEqualTo("grandchild2_new");
            assertThat(child2.getNumChildren()).isEqualTo(1);
            assertThat(child2.getChild(0).toContentString()).isEqualTo("added_child");
        }

        @Test
        @DisplayName("Should work with different traversal strategies")
        void testWithDifferentTraversalStrategies() {
            TrackingTransformRule preOrderRule = new TrackingTransformRule(TraversalStrategy.PRE_ORDER);
            TrackingTransformRule postOrderRule = new TrackingTransformRule(TraversalStrategy.POST_ORDER);

            // Use on same tree structure
            TestTreeNode tree1 = createTestTree();
            TestTreeNode tree2 = createTestTree();

            TraversalStrategy.PRE_ORDER.apply(tree1, preOrderRule);
            TraversalStrategy.POST_ORDER.apply(tree2, postOrderRule);

            List<String> preOrder = preOrderRule.getVisitOrder();
            List<String> postOrder = postOrderRule.getVisitOrder();

            assertThat(preOrder).isNotEqualTo(postOrder);
            assertThat(preOrder).hasSize(postOrder.size());

            // First element of pre-order should be last element of post-order (root)
            assertThat(preOrder.get(0)).isEqualTo(postOrder.get(postOrder.size() - 1));
        }

        private TestTreeNode createTestTree() {
            TestTreeNode r = new TestTreeNode("root");
            TestTreeNode c1 = new TestTreeNode("child1");
            TestTreeNode c2 = new TestTreeNode("child2");
            TestTreeNode gc1 = new TestTreeNode("grandchild1");
            TestTreeNode gc2 = new TestTreeNode("grandchild2");

            r.addChild(c1);
            r.addChild(c2);
            c1.addChild(gc1);
            c1.addChild(gc2);

            return r;
        }

        @Test
        @DisplayName("Should work with rule that uses strategy from itself")
        void testWithSelfReferencingRule() {
            TransformRule<TestTreeNode, TestTransformResult> selfReferencingRule = new TransformRule<TestTreeNode, TestTransformResult>() {
                @Override
                public TestTransformResult apply(TestTreeNode node, TransformQueue<TestTreeNode> queue) {
                    // Use the rule's own strategy in the logic
                    TraversalStrategy strategy = getTraversalStrategy();

                    if (strategy == TraversalStrategy.PRE_ORDER && node.toContentString().equals("root")) {
                        // Only process root in pre-order
                        return new TestTransformResult(true);
                    }

                    return new TestTransformResult(true);
                }

                @Override
                public TraversalStrategy getTraversalStrategy() {
                    return TraversalStrategy.PRE_ORDER;
                }
            };

            // Should not throw exceptions
            assertThatCode(() -> TraversalStrategy.PRE_ORDER.apply(root, selfReferencingRule))
                    .doesNotThrowAnyException();
        }
    }
}
