package pt.up.fe.specs.util.treenode;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

import java.util.*;

/**
 * Test suite for IteratorUtils and TokenTester classes.
 * 
 * @author Generated Tests
 */
@DisplayName("IteratorUtils and TokenTester Tests")
class IteratorUtilsTest {

    private TestTreeNode root;
    private TestTreeNode child1;
    private TestTreeNode child2;
    private TestTreeNode grandchild1;
    private SpecialTestTreeNode specialChild;

    @BeforeEach
    void setUp() {
        // Create test tree structure:
        //     root
        //    /    \
        //  child1  child2
        //  /         |
        // grandchild1  specialChild
        grandchild1 = new TestTreeNode("grandchild1");
        specialChild = new SpecialTestTreeNode("special");
        child1 = new TestTreeNode("child1", Collections.singletonList(grandchild1));
        child2 = new TestTreeNode("child2", Collections.singletonList(specialChild));
        root = new TestTreeNode("root", Arrays.asList(child1, child2));
    }

    @Nested
    @DisplayName("TokenTester Functionality Tests")
    class TokenTesterTests {

        @Test
        @DisplayName("TokenTester should work with type checking")
        void testTokenTester_TypeChecking_Works() {
            TokenTester specialTester = IteratorUtils.newTypeTest(SpecialTestTreeNode.class);

            assertThat(specialTester.test(specialChild)).isTrue();
            assertThat(specialTester.test(child1)).isFalse();
            assertThat(specialTester.test(root)).isFalse();
        }

        @Test
        @DisplayName("TokenTester should work with custom lambda")
        void testTokenTester_CustomLambda_Works() {
            TokenTester leafTester = node -> !node.hasChildren();

            assertThat(leafTester.test(grandchild1)).isTrue();
            assertThat(leafTester.test(specialChild)).isTrue();
            assertThat(leafTester.test(child1)).isFalse();
            assertThat(leafTester.test(root)).isFalse();
        }

        @Test
        @DisplayName("TokenTester should work with name matching")
        void testTokenTester_NameMatching_Works() {
            TokenTester nameTester = node -> {
                if (node instanceof TestTreeNode) {
                    return ((TestTreeNode) node).toContentString().contains("child");
                }
                return false;
            };

            assertThat(nameTester.test(child1)).isTrue();
            assertThat(nameTester.test(child2)).isTrue();
            assertThat(nameTester.test(grandchild1)).isTrue();
            assertThat(nameTester.test(specialChild)).isFalse(); // Special type
            assertThat(nameTester.test(root)).isFalse();
        }
    }

    @Nested
    @DisplayName("IteratorUtils Depth Iterator Tests")
    class DepthIteratorTests {

        @Test
        @DisplayName("getDepthIterator() should traverse all descendants")
        void testGetDepthIterator_TraversesAllDescendants() {
            TokenTester acceptAll = node -> true;
            Iterator<TestTreeNode> iterator = IteratorUtils.getDepthIterator(root, acceptAll);

            List<TestTreeNode> visited = new ArrayList<>();
            while (iterator.hasNext()) {
                visited.add(iterator.next());
            }

            // Should visit all descendants but not root itself
            assertThat(visited).contains(child1, child2, grandchild1, specialChild);
            assertThat(visited).doesNotContain(root);
        }

        @Test
        @DisplayName("getDepthIterator() should filter by TokenTester")
        void testGetDepthIterator_FiltersByTokenTester() {
            TokenTester leafOnly = node -> !node.hasChildren();
            Iterator<TestTreeNode> iterator = IteratorUtils.getDepthIterator(root, leafOnly);

            List<TestTreeNode> visited = new ArrayList<>();
            while (iterator.hasNext()) {
                visited.add(iterator.next());
            }

            // Should only visit leaf nodes
            assertThat(visited).containsExactlyInAnyOrder(grandchild1, specialChild);
        }

        @Test
        @DisplayName("getDepthIterator() with pruning should stop at matching nodes")
        void testGetDepthIterator_WithPruning_StopsAtMatching() {
            TokenTester acceptAll = node -> true;
            Iterator<TestTreeNode> noPruning = IteratorUtils.getDepthIterator(root, acceptAll, false);
            Iterator<TestTreeNode> withPruning = IteratorUtils.getDepthIterator(root, acceptAll, true);

            List<TestTreeNode> noPruningList = new ArrayList<>();
            while (noPruning.hasNext()) {
                noPruningList.add(noPruning.next());
            }

            List<TestTreeNode> withPruningList = new ArrayList<>();
            while (withPruning.hasNext()) {
                withPruningList.add(withPruning.next());
            }

            // Without pruning: all descendants
            assertThat(noPruningList).containsExactlyInAnyOrder(child1, child2, grandchild1, specialChild);

            // With pruning: only immediate children (since they pass the test, their
            // children are not processed)
            assertThat(withPruningList).containsExactlyInAnyOrder(child1, child2);
        }
    }

    @Nested
    @DisplayName("IteratorUtils Token Collection Tests")
    class TokenCollectionTests {

        @Test
        @DisplayName("getTokens() should collect tokens matching filter")
        void testGetTokens_CollectsMatchingTokens() {
            TokenTester leafTester = node -> !node.hasChildren();
            Iterator<TestTreeNode> iterator = IteratorUtils.getDepthIterator(root, node -> true);

            List<TestTreeNode> leafTokens = IteratorUtils.getTokens(iterator, leafTester);

            assertThat(leafTokens).containsExactlyInAnyOrder(grandchild1, specialChild);
        }

        @Test
        @DisplayName("getTokens() should return empty list when no matches")
        void testGetTokens_NoMatches_ReturnsEmptyList() {
            TokenTester impossibleTester = node -> false;
            Iterator<TestTreeNode> iterator = IteratorUtils.getDepthIterator(root, node -> true);

            List<TestTreeNode> tokens = IteratorUtils.getTokens(iterator, impossibleTester);

            assertThat(tokens).isEmpty();
        }

        @Test
        @DisplayName("getTokens() should work with type-specific filter")
        void testGetTokens_TypeSpecificFilter_Works() {
            TokenTester specialTester = IteratorUtils.newTypeTest(SpecialTestTreeNode.class);
            Iterator<TestTreeNode> iterator = IteratorUtils.getDepthIterator(root, node -> true);

            List<TestTreeNode> specialTokens = IteratorUtils.getTokens(iterator, specialTester);

            assertThat(specialTokens).containsExactly(specialChild);
        }
    }

    @Nested
    @DisplayName("Integration and Edge Cases")
    class IntegrationEdgeCasesTests {

        @Test
        @DisplayName("Empty tree should work with all operations")
        void testEmptyTree_AllOperations_Work() {
            TestTreeNode emptyNode = new TestTreeNode("empty");
            TokenTester acceptAll = node -> true;

            Iterator<TestTreeNode> iterator = IteratorUtils.getDepthIterator(emptyNode, acceptAll);
            List<TestTreeNode> tokens = IteratorUtils.getTokens(iterator, acceptAll);

            assertThat(tokens).isEmpty();
        }

        @Test
        @DisplayName("Single node tree should work correctly")
        void testSingleNodeTree_WorksCorrectly() {
            TestTreeNode singleNode = new TestTreeNode("single");
            TokenTester acceptAll = node -> true;

            Iterator<TestTreeNode> iterator = IteratorUtils.getDepthIterator(singleNode, acceptAll);
            List<TestTreeNode> tokens = IteratorUtils.getTokens(iterator, acceptAll);

            // Depth iterator doesn't include the root node itself
            assertThat(tokens).isEmpty();
        }

        @Test
        @DisplayName("Complex filtering should work correctly")
        void testComplexFiltering_WorksCorrectly() {
            // Filter for TestTreeNode (not Special) that contains "child"
            TokenTester complexTester = node -> {
                if (!(node instanceof TestTreeNode))
                    return false;
                if (node instanceof SpecialTestTreeNode)
                    return false;
                return ((TestTreeNode) node).toContentString().contains("child");
            };

            Iterator<TestTreeNode> iterator = IteratorUtils.getDepthIterator(root, node -> true);
            List<TestTreeNode> filteredTokens = IteratorUtils.getTokens(iterator, complexTester);

            assertThat(filteredTokens).containsExactlyInAnyOrder(child1, child2, grandchild1);
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

    /**
     * Special subclass for testing type-based operations
     */
    private static class SpecialTestTreeNode extends TestTreeNode {
        public SpecialTestTreeNode(String name) {
            super(name);
        }

        @Override
        protected TestTreeNode copyPrivate() {
            return new SpecialTestTreeNode(toContentString());
        }
    }
}
