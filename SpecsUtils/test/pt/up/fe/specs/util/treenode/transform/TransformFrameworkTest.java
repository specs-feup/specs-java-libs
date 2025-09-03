package pt.up.fe.specs.util.treenode.transform;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

import java.util.*;
import pt.up.fe.specs.util.treenode.*;
import pt.up.fe.specs.util.treenode.transform.transformations.*;
import pt.up.fe.specs.util.treenode.transform.util.TraversalStrategy;
import pt.up.fe.specs.util.treenode.transform.impl.DefaultTransformResult;

/**
 * Test suite for the TreeNode transformation framework.
 * Tests TransformQueue, NodeTransform implementations, TransformRule,
 * TransformResult, and TraversalStrategy.
 * 
 * @author Generated Tests
 */
@DisplayName("TreeNode Transform Framework Tests")
class TransformFrameworkTest {

    private TestTreeNode root;
    private TestTreeNode child1;
    private TestTreeNode child2;
    private TestTreeNode grandchild1;

    @BeforeEach
    void setUp() {
        // Create test tree structure:
        //     root
        //    /    \
        //  child1  child2
        //  /
        // grandchild1
        grandchild1 = new TestTreeNode("grandchild1");
        child1 = new TestTreeNode("child1", Collections.singletonList(grandchild1));
        child2 = new TestTreeNode("child2");
        root = new TestTreeNode("root", Arrays.asList(child1, child2));
    }

    @Nested
    @DisplayName("TransformQueue Tests")
    class TransformQueueTests {

        @Test
        @DisplayName("TransformQueue should queue transformations without executing them")
        void testTransformQueue_QueuesWithoutExecuting() {
            TransformQueue<TestTreeNode> queue = new TransformQueue<>("test");

            String originalName = child1.toContentString();
            TestTreeNode newChild = new TestTreeNode("newChild1");

            // Queue a replacement
            queue.replace(child1, newChild);

            // Should not be executed yet
            assertThat(child1.toContentString()).isEqualTo(originalName);
            assertThat(queue.getTransforms()).hasSize(1);
            assertThat(queue.getTransforms().get(0)).isInstanceOf(ReplaceTransform.class);
        }

        @Test
        @DisplayName("TransformQueue should execute replace transformation")
        void testTransformQueue_ExecutesReplace() {
            TransformQueue<TestTreeNode> queue = new TransformQueue<>("test");
            TestTreeNode newChild = new TestTreeNode("replacedChild");

            queue.replace(child1, newChild);
            queue.apply();

            // child1 should be replaced with newChild in root
            assertThat(root.getChildren()).contains(newChild);
            assertThat(root.getChildren()).doesNotContain(child1);
            assertThat(newChild.getParent()).isSameAs(root);
        }

        @Test
        @DisplayName("TransformQueue should execute delete transformation")
        void testTransformQueue_ExecutesDelete() {
            TransformQueue<TestTreeNode> queue = new TransformQueue<>("test");
            int originalChildCount = root.getNumChildren();

            queue.delete(child2);
            queue.apply();

            assertThat(root.getNumChildren()).isEqualTo(originalChildCount - 1);
            assertThat(root.getChildren()).doesNotContain(child2);
        }

        @Test
        @DisplayName("TransformQueue should execute addChild transformation")
        void testTransformQueue_ExecutesAddChild() {
            TransformQueue<TestTreeNode> queue = new TransformQueue<>("test");
            TestTreeNode newChild = new TestTreeNode("addedChild");
            int originalChildCount = root.getNumChildren();

            queue.addChild(root, newChild);
            queue.apply();

            assertThat(root.getNumChildren()).isEqualTo(originalChildCount + 1);
            assertThat(root.getChildren()).contains(newChild);
            assertThat(newChild.getParent()).isSameAs(root);
        }

        @Test
        @DisplayName("TransformQueue should execute swap transformation")
        void testTransformQueue_ExecutesSwap() {
            TransformQueue<TestTreeNode> queue = new TransformQueue<>("test");

            // Get original positions
            int child1Index = root.indexOfChild(child1);
            int child2Index = root.indexOfChild(child2);

            queue.swap(child1, child2);
            queue.apply();

            // Positions should be swapped
            assertThat(root.indexOfChild(child1)).isEqualTo(child2Index);
            assertThat(root.indexOfChild(child2)).isEqualTo(child1Index);
        }

        @Test
        @DisplayName("TransformQueue should execute multiple transformations in order")
        void testTransformQueue_ExecutesMultipleTransformations() {
            TransformQueue<TestTreeNode> queue = new TransformQueue<>("test");
            TestTreeNode newChild3 = new TestTreeNode("child3");
            TestTreeNode newChild4 = new TestTreeNode("child4");

            queue.addChild(root, newChild3);
            queue.addChild(root, newChild4);
            queue.apply();

            assertThat(root.getNumChildren()).isEqualTo(4); // original 2 + 2 new
            assertThat(root.getChildren()).contains(newChild3, newChild4);
        }

        @Test
        @DisplayName("TransformQueue should clear after apply")
        void testTransformQueue_ClearsAfterApply() {
            TransformQueue<TestTreeNode> queue = new TransformQueue<>("test");
            TestTreeNode newChild = new TestTreeNode("newChild");

            queue.addChild(root, newChild);
            assertThat(queue.getTransforms()).hasSize(1);

            queue.apply();
            assertThat(queue.getTransforms()).isEmpty();
        }
    }

    @Nested
    @DisplayName("NodeTransform Implementations Tests")
    class NodeTransformTests {

        @Test
        @DisplayName("ReplaceTransform should have correct type and operands")
        void testReplaceTransform_HasCorrectProperties() {
            TestTreeNode newNode = new TestTreeNode("new");
            ReplaceTransform<TestTreeNode> transform = new ReplaceTransform<>(child1, newNode);

            assertThat(transform.getType()).isEqualTo("replace");
            assertThat(transform.getOperands()).containsExactly(child1, newNode);
        }

        @Test
        @DisplayName("DeleteTransform should have correct type and operands")
        void testDeleteTransform_HasCorrectProperties() {
            DeleteTransform<TestTreeNode> transform = new DeleteTransform<>(child1);

            assertThat(transform.getType()).isEqualTo("delete");
            assertThat(transform.getOperands()).containsExactly(child1);
        }

        @Test
        @DisplayName("SwapTransform should have correct type and operands")
        void testSwapTransform_HasCorrectProperties() {
            SwapTransform<TestTreeNode> transform = new SwapTransform<>(child1, child2, true);

            assertThat(transform.getType()).isEqualTo("swap");
            assertThat(transform.getOperands()).containsExactly(child1, child2);
        }

        @Test
        @DisplayName("AddChildTransform should have correct type and operands")
        void testAddChildTransform_HasCorrectProperties() {
            TestTreeNode newChild = new TestTreeNode("new");
            AddChildTransform<TestTreeNode> transform = new AddChildTransform<>(root, newChild);

            assertThat(transform.getType()).isEqualTo("add-child");
            assertThat(transform.getOperands()).containsExactly(root, newChild);
        }

        @Test
        @DisplayName("MoveBeforeTransform should have correct type and operands")
        void testMoveBeforeTransform_HasCorrectProperties() {
            MoveBeforeTransform<TestTreeNode> transform = new MoveBeforeTransform<>(child1, child2);

            assertThat(transform.getType()).isEqualTo("move-before");
            assertThat(transform.getOperands()).containsExactly(child1, child2);
        }

        @Test
        @DisplayName("MoveAfterTransform should have correct type and operands")
        void testMoveAfterTransform_HasCorrectProperties() {
            MoveAfterTransform<TestTreeNode> transform = new MoveAfterTransform<>(child1, child2);

            assertThat(transform.getType()).isEqualTo("move-after");
            assertThat(transform.getOperands()).containsExactly(child1, child2);
        }
    }

    @Nested
    @DisplayName("TransformResult Tests")
    class TransformResultTests {

        @Test
        @DisplayName("DefaultTransformResult should respect visitChildren parameter")
        void testDefaultTransformResult_RespectsVisitChildren() {
            DefaultTransformResult visitChildren = new DefaultTransformResult(true);
            DefaultTransformResult skipChildren = new DefaultTransformResult(false);

            assertThat(visitChildren.visitChildren()).isTrue();
            assertThat(skipChildren.visitChildren()).isFalse();
        }

        @Test
        @DisplayName("TransformResult.empty should return non-null result")
        void testTransformResult_EmptyReturnsNonNull() {
            TransformResult result = TransformResult.empty();

            assertThat(result).isNotNull();
            assertThat(result.visitChildren()).isTrue(); // Default behavior
        }
    }

    @Nested
    @DisplayName("TransformRule Tests")
    class TransformRuleTests {

        @Test
        @DisplayName("Custom TransformRule should work with TraversalStrategy")
        void testCustomTransformRule_WorksWithTraversalStrategy() {
            TransformRule<TestTreeNode, TransformResult> rule = new TestTransformRule();

            // Should be able to get traversal strategy
            assertThat(rule.getTraversalStrategy()).isNotNull();

            // Should be able to visit nodes
            assertThatCode(() -> rule.visit(root)).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("TransformRule should apply to all nodes when visiting")
        void testTransformRule_AppliestoAllNodes() {
            CountingTransformRule rule = new CountingTransformRule();

            rule.visit(root);

            // Should visit root, child1, child2, grandchild1 = 4 nodes total
            assertThat(rule.getVisitCount()).isEqualTo(4);
        }
    }

    @Nested
    @DisplayName("TraversalStrategy Tests")
    class TraversalStrategyTests {

        @Test
        @DisplayName("PRE_ORDER strategy should exist")
        void testPreOrderStrategy_Exists() {
            assertThat(TraversalStrategy.PRE_ORDER).isNotNull();
        }

        @Test
        @DisplayName("POST_ORDER strategy should exist")
        void testPostOrderStrategy_Exists() {
            assertThat(TraversalStrategy.POST_ORDER).isNotNull();
        }

        @Test
        @DisplayName("TraversalStrategy should apply rules to nodes")
        void testTraversalStrategy_AppliesRules() {
            CountingTransformRule rule = new CountingTransformRule();

            TraversalStrategy.PRE_ORDER.apply(root, rule);

            assertThat(rule.getVisitCount()).isGreaterThan(0);
        }

        @Test
        @DisplayName("TraversalStrategy should generate transformation queue")
        void testTraversalStrategy_GeneratesTransformQueue() {
            TestTransformRule rule = new TestTransformRule();

            TransformQueue<TestTreeNode> queue = TraversalStrategy.PRE_ORDER.getTransformations(root, rule);

            assertThat(queue).isNotNull();
            assertThat(queue.getId()).contains("TestTransformRule");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Complete transformation workflow should work")
        void testCompleteTransformationWorkflow() {
            // Create a rule that adds a child to leaf nodes
            TransformRule<TestTreeNode, TransformResult> rule = new AddChildToLeafRule();

            // Apply using PRE_ORDER strategy
            TraversalStrategy.PRE_ORDER.apply(root, rule);

            // Leaf nodes should have been modified
            // grandchild1 was a leaf, should now have a child
            assertThat(grandchild1.hasChildren()).isTrue();
            // child2 was a leaf, should now have a child
            assertThat(child2.hasChildren()).isTrue();
        }

        @Test
        @DisplayName("Multiple transformation phases should work together")
        void testMultipleTransformationPhases() {
            // Phase 1: Add children to leaves
            new AddChildToLeafRule().visit(root);

            // Phase 2: Count all nodes again
            CountingTransformRule countingRule = new CountingTransformRule();
            countingRule.visit(root);

            // Should have more nodes now due to added children
            assertThat(countingRule.getVisitCount()).isGreaterThan(4);
        }
    }

    // ========== Test Helper Classes ==========

    /**
     * Test implementation of TransformRule for testing purposes
     */
    private static class TestTransformRule implements TransformRule<TestTreeNode, TransformResult> {
        @Override
        public TransformResult apply(TestTreeNode node, TransformQueue<TestTreeNode> queue) {
            // Simple rule that doesn't queue any transformations
            return TransformResult.empty();
        }

        @Override
        public TraversalStrategy getTraversalStrategy() {
            return TraversalStrategy.PRE_ORDER;
        }
    }

    /**
     * Transform rule that counts how many nodes it visits
     */
    private static class CountingTransformRule implements TransformRule<TestTreeNode, TransformResult> {
        private int visitCount = 0;

        @Override
        public TransformResult apply(TestTreeNode node, TransformQueue<TestTreeNode> queue) {
            visitCount++;
            return TransformResult.empty();
        }

        @Override
        public TraversalStrategy getTraversalStrategy() {
            return TraversalStrategy.PRE_ORDER;
        }

        public int getVisitCount() {
            return visitCount;
        }
    }

    /**
     * Transform rule that adds a child to leaf nodes
     */
    private static class AddChildToLeafRule implements TransformRule<TestTreeNode, TransformResult> {
        @Override
        public TransformResult apply(TestTreeNode node, TransformQueue<TestTreeNode> queue) {
            if (!node.hasChildren()) {
                TestTreeNode newChild = new TestTreeNode("added_to_" + node.toContentString());
                queue.addChild(node, newChild);
            }
            return TransformResult.empty();
        }

        @Override
        public TraversalStrategy getTraversalStrategy() {
            return TraversalStrategy.PRE_ORDER;
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
