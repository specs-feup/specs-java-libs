package pt.up.fe.specs.util.treenode;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

import java.util.*;

/**
 * Test suite for TreeNodeWalker visitor pattern implementation.
 * 
 * @author Generated Tests
 */
@DisplayName("TreeNodeWalker Tests")
class TreeNodeWalkerTest {

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
    @DisplayName("Visitor Pattern Tests")
    class VisitorPatternTests {

        @Test
        @DisplayName("visit() should traverse all nodes")
        void testVisit_TraversesAllNodes() {
            CountingTreeNodeWalker walker = new CountingTreeNodeWalker();
            walker.visit(root);

            // Should visit all children but not root itself (default behavior)
            assertThat(walker.getVisitedNodes()).containsExactlyInAnyOrder(child1, child2, grandchild1);
        }

        @Test
        @DisplayName("visitChildren() recursively visits all descendants")
        void testVisitChildren_RecursivelyVisitsDescendants() {
            CountingTreeNodeWalker walker = new CountingTreeNodeWalker();
            walker.visitChildren(root);

            // visitChildren() calls visit() on each child, which recursively visits their
            // children
            assertThat(walker.getVisitedNodes()).containsExactlyInAnyOrder(child1, child2, grandchild1);
        }

        @Test
        @DisplayName("direct children iteration should only visit immediate children")
        void testDirectChildrenIteration_OnlyImmediate() {
            // Test the behavior with a non-recursive walker
            DirectChildrenWalker walker = new DirectChildrenWalker();
            walker.visitDirectChildren(root);

            // Should only visit direct children, not grandchildren
            assertThat(walker.getVisitedNodes()).containsExactlyInAnyOrder(child1, child2);
        }

        @Test
        @DisplayName("visit() on leaf node should not visit anything")
        void testVisit_LeafNode_VisitsNothing() {
            CountingTreeNodeWalker walker = new CountingTreeNodeWalker();
            walker.visit(child2); // Leaf node

            assertThat(walker.getVisitedNodes()).isEmpty();
        }

        @Test
        @DisplayName("Custom walker should allow custom behavior")
        void testCustomWalker_AllowsCustomBehavior() {
            CollectingTreeNodeWalker walker = new CollectingTreeNodeWalker();
            walker.visit(root);

            // Custom walker includes the visited node itself
            assertThat(walker.getCollectedNodes()).containsExactlyInAnyOrder(root, child1, child2, grandchild1);
        }
    }

    @Nested
    @DisplayName("Walker Customization Tests")
    class WalkerCustomizationTests {

        @Test
        @DisplayName("Walker should support pre-order traversal")
        void testWalker_PreOrderTraversal() {
            OrderTrackingWalker walker = new OrderTrackingWalker();
            walker.visit(root);

            // Should visit in depth-first order
            List<String> expectedOrder = Arrays.asList("child1", "grandchild1", "child2");
            assertThat(walker.getVisitOrder()).isEqualTo(expectedOrder);
        }

        @Test
        @DisplayName("Walker should handle empty trees")
        void testWalker_EmptyTree() {
            TestTreeNode emptyNode = new TestTreeNode("empty");
            CountingTreeNodeWalker walker = new CountingTreeNodeWalker();

            walker.visit(emptyNode);

            assertThat(walker.getVisitedNodes()).isEmpty();
        }

        @Test
        @DisplayName("Walker should handle single-node trees")
        void testWalker_SingleNode() {
            TestTreeNode singleNode = new TestTreeNode("single");
            CollectingTreeNodeWalker walker = new CollectingTreeNodeWalker();

            walker.visit(singleNode);

            assertThat(walker.getCollectedNodes()).containsExactly(singleNode);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("Multiple visits should accumulate results")
        void testMultipleVisits_AccumulateResults() {
            CountingTreeNodeWalker walker = new CountingTreeNodeWalker();

            walker.visit(child1);
            walker.visit(child2);

            assertThat(walker.getVisitedNodes()).containsExactly(grandchild1);
        }

        @Test
        @DisplayName("Null-safe operations should work")
        void testNullSafeOperations_Work() {
            // TreeNodeWalker should handle null gracefully in custom implementations
            SafeTreeNodeWalker walker = new SafeTreeNodeWalker();

            // This should not throw an exception
            assertThatCode(() -> walker.visitSafely(null))
                    .doesNotThrowAnyException();
        }
    }

    /**
     * Test walker that only visits direct children without recursion
     */
    private static class DirectChildrenWalker {
        private final List<TestTreeNode> visitedNodes = new ArrayList<>();

        public void visitDirectChildren(TestTreeNode node) {
            visitedNodes.addAll(node.getChildren());
        }

        public List<TestTreeNode> getVisitedNodes() {
            return new ArrayList<>(visitedNodes);
        }
    }

    /**
     * Test walker that counts visited nodes
     */
    private static class CountingTreeNodeWalker extends TreeNodeWalker<TestTreeNode> {
        private final List<TestTreeNode> visitedNodes = new ArrayList<>();

        @Override
        public void visit(TestTreeNode node) {
            super.visit(node); // Visit children
        }

        @Override
        protected void visitChildren(TestTreeNode node) {
            for (TestTreeNode child : node.getChildren()) {
                visitedNodes.add(child);
                visit(child);
            }
        }

        public List<TestTreeNode> getVisitedNodes() {
            return new ArrayList<>(visitedNodes);
        }
    }

    /**
     * Test walker that collects all nodes including the root
     */
    private static class CollectingTreeNodeWalker extends TreeNodeWalker<TestTreeNode> {
        private final List<TestTreeNode> collectedNodes = new ArrayList<>();

        @Override
        public void visit(TestTreeNode node) {
            collectedNodes.add(node);
            super.visit(node);
        }

        public List<TestTreeNode> getCollectedNodes() {
            return new ArrayList<>(collectedNodes);
        }
    }

    /**
     * Test walker that tracks visit order
     */
    private static class OrderTrackingWalker extends TreeNodeWalker<TestTreeNode> {
        private final List<String> visitOrder = new ArrayList<>();

        @Override
        protected void visitChildren(TestTreeNode node) {
            for (TestTreeNode child : node.getChildren()) {
                visitOrder.add(child.toContentString());
                visit(child);
            }
        }

        public List<String> getVisitOrder() {
            return new ArrayList<>(visitOrder);
        }
    }

    /**
     * Test walker that handles null values safely
     */
    private static class SafeTreeNodeWalker extends TreeNodeWalker<TestTreeNode> {
        public void visitSafely(TestTreeNode node) {
            if (node != null) {
                visit(node);
            }
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
