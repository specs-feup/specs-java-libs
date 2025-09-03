package pt.up.fe.specs.util.treenode;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

import java.util.*;

/**
 * Comprehensive test suite for TokenTester functional interface.
 * Tests the token testing functionality used for filtering and validating tree
 * nodes.
 * 
 * @author Generated Tests
 */
@DisplayName("TokenTester Interface Tests")
class TokenTesterTest {

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
        grandchild1 = new TestTreeNode("grandchild1", "leaf");
        child1 = new TestTreeNode("child1", "parent", Collections.singletonList(grandchild1));
        child2 = new TestTreeNode("child2", "leaf");
        root = new TestTreeNode("root", "root", Arrays.asList(child1, child2));
    }

    @Nested
    @DisplayName("Basic TokenTester Tests")
    class BasicTokenTesterTests {

        @Test
        @DisplayName("TokenTester should test nodes correctly")
        void testTokenTester_TestsNodesCorrectly() {
            // Test for nodes with children
            TokenTester hasChildrenTester = TreeNode::hasChildren;

            assertThat(hasChildrenTester.test(root)).isTrue();
            assertThat(hasChildrenTester.test(child1)).isTrue();
            assertThat(hasChildrenTester.test(child2)).isFalse();
            assertThat(hasChildrenTester.test(grandchild1)).isFalse();
        }

        @Test
        @DisplayName("TokenTester should work with lambda expressions")
        void testTokenTester_WorksWithLambdas() {
            // Test for nodes at specific depth
            TokenTester depthZeroTester = node -> node.getDepth() == 0;
            TokenTester depthOneTester = node -> node.getDepth() == 1;
            TokenTester depthTwoTester = node -> node.getDepth() == 2;

            assertThat(depthZeroTester.test(root)).isTrue();
            assertThat(depthZeroTester.test(child1)).isFalse();

            assertThat(depthOneTester.test(child1)).isTrue();
            assertThat(depthOneTester.test(child2)).isTrue();
            assertThat(depthOneTester.test(root)).isFalse();

            assertThat(depthTwoTester.test(grandchild1)).isTrue();
            assertThat(depthTwoTester.test(child1)).isFalse();
        }

        @Test
        @DisplayName("TokenTester should work with method references")
        void testTokenTester_WorksWithMethodReferences() {
            // Test using various method references
            TokenTester hasParentTester = TreeNode::hasParent;

            assertThat(hasParentTester.test(root)).isFalse();
            assertThat(hasParentTester.test(child1)).isTrue();
            assertThat(hasParentTester.test(child2)).isTrue();
            assertThat(hasParentTester.test(grandchild1)).isTrue();
        }
    }

    @Nested
    @DisplayName("Complex TokenTester Scenarios")
    class ComplexTokenTesterTests {

        @Test
        @DisplayName("TokenTester should work with complex predicates")
        void testTokenTester_WorksWithComplexPredicates() {
            // Test for leaf nodes (no children but has parent)
            TokenTester leafNodeTester = node -> !node.hasChildren() && node.hasParent();

            assertThat(leafNodeTester.test(root)).isFalse(); // Root has children
            assertThat(leafNodeTester.test(child1)).isFalse(); // Has children
            assertThat(leafNodeTester.test(child2)).isTrue(); // Leaf node
            assertThat(leafNodeTester.test(grandchild1)).isTrue(); // Leaf node
        }

        @Test
        @DisplayName("TokenTester should work with content-based tests")
        void testTokenTester_WorksWithContentBasedTests() {
            // Test based on node type/content
            TokenTester leafTypeTester = node -> {
                if (node instanceof TestTreeNode) {
                    TestTreeNode testNode = (TestTreeNode) node;
                    return "leaf".equals(testNode.getType());
                }
                return false;
            };

            assertThat(leafTypeTester.test(root)).isFalse();
            assertThat(leafTypeTester.test(child1)).isFalse();
            assertThat(leafTypeTester.test(child2)).isTrue();
            assertThat(leafTypeTester.test(grandchild1)).isTrue();
        }

        @Test
        @DisplayName("TokenTester should work with name-based tests")
        void testTokenTester_WorksWithNameBasedTests() {
            // Test based on name patterns
            TokenTester childNameTester = node -> {
                if (node instanceof TestTreeNode) {
                    TestTreeNode testNode = (TestTreeNode) node;
                    return testNode.getName().startsWith("child");
                }
                return false;
            };

            assertThat(childNameTester.test(root)).isFalse();
            assertThat(childNameTester.test(child1)).isTrue();
            assertThat(childNameTester.test(child2)).isTrue();
            assertThat(childNameTester.test(grandchild1)).isFalse();
        }

        @Test
        @DisplayName("TokenTester should work with null-safe operations")
        void testTokenTester_WorksWithNullSafeOperations() {
            // Test that handles potential null values safely
            TokenTester nullSafeTester = node -> {
                if (node == null) {
                    return false;
                }
                return node.getNumChildren() > 0;
            };

            assertThat(nullSafeTester.test(root)).isTrue();
            assertThat(nullSafeTester.test(child1)).isTrue();
            assertThat(nullSafeTester.test(child2)).isFalse();
            assertThat(nullSafeTester.test(null)).isFalse();
        }
    }

    @Nested
    @DisplayName("TokenTester Composition Tests")
    class TokenTesterCompositionTests {

        @Test
        @DisplayName("TokenTester should compose with AND logic")
        void testTokenTester_ComposesWithAndLogic() {
            // Create compound tester using AND logic
            TokenTester hasChildrenTester = TreeNode::hasChildren;
            TokenTester hasParentTester = TreeNode::hasParent;

            // Node that has both children and parent
            TokenTester hasChildrenAndParentTester = node -> hasChildrenTester.test(node) && hasParentTester.test(node);

            assertThat(hasChildrenAndParentTester.test(root)).isFalse(); // Has children but no parent
            assertThat(hasChildrenAndParentTester.test(child1)).isTrue(); // Has both
            assertThat(hasChildrenAndParentTester.test(child2)).isFalse(); // Has parent but no children
            assertThat(hasChildrenAndParentTester.test(grandchild1)).isFalse(); // Has parent but no children
        }

        @Test
        @DisplayName("TokenTester should compose with OR logic")
        void testTokenTester_ComposesWithOrLogic() {
            // Create compound tester using OR logic
            TokenTester isRootTester = node -> !node.hasParent();
            TokenTester isLeafTester = node -> !node.hasChildren();

            // Node that is either root or leaf
            TokenTester rootOrLeafTester = node -> isRootTester.test(node) || isLeafTester.test(node);

            assertThat(rootOrLeafTester.test(root)).isTrue(); // Is root
            assertThat(rootOrLeafTester.test(child1)).isFalse(); // Neither root nor leaf
            assertThat(rootOrLeafTester.test(child2)).isTrue(); // Is leaf
            assertThat(rootOrLeafTester.test(grandchild1)).isTrue(); // Is leaf
        }

        @Test
        @DisplayName("TokenTester should compose with NOT logic")
        void testTokenTester_ComposesWithNotLogic() {
            // Create negated tester
            TokenTester hasChildrenTester = TreeNode::hasChildren;
            TokenTester hasNoChildrenTester = node -> !hasChildrenTester.test(node);

            assertThat(hasNoChildrenTester.test(root)).isFalse();
            assertThat(hasNoChildrenTester.test(child1)).isFalse();
            assertThat(hasNoChildrenTester.test(child2)).isTrue();
            assertThat(hasNoChildrenTester.test(grandchild1)).isTrue();
        }
    }

    @Nested
    @DisplayName("TokenTester Usage Patterns")
    class TokenTesterUsagePatternsTests {

        @Test
        @DisplayName("TokenTester should work for filtering tree nodes")
        void testTokenTester_WorksForFiltering() {
            TokenTester leafTester = node -> !node.hasChildren();

            List<TestTreeNode> allNodes = Arrays.asList(root, child1, child2, grandchild1);
            List<TestTreeNode> leafNodes = allNodes.stream()
                    .filter(node -> leafTester.test(node))
                    .collect(java.util.stream.Collectors.toList());

            assertThat(leafNodes).containsExactly(child2, grandchild1);
        }

        @Test
        @DisplayName("TokenTester should work for tree traversal conditions")
        void testTokenTester_WorksForTraversalConditions() {
            TokenTester stopCondition = node -> !node.hasChildren();

            // Simulate tree traversal that stops at leaf nodes
            List<TestTreeNode> traversed = new ArrayList<>();
            traverseWithCondition(root, stopCondition, traversed);

            // Should traverse until it hits leaf nodes, then stop
            assertThat(traversed).contains(root, child1);
            // Should not contain leaf nodes as we stop before processing them
        }

        private void traverseWithCondition(TestTreeNode node, TokenTester stopCondition, List<TestTreeNode> visited) {
            if (stopCondition.test(node)) {
                return; // Stop traversal
            }

            visited.add(node);
            for (TestTreeNode child : node.getChildren()) {
                traverseWithCondition(child, stopCondition, visited);
            }
        }

        @Test
        @DisplayName("TokenTester should work for tree validation")
        void testTokenTester_WorksForValidation() {
            // Validate that all nodes in tree have valid parent relationships
            TokenTester validParentTester = node -> {
                if (!node.hasParent()) {
                    return true; // Root node is valid
                }

                TreeNode<?> parent = node.getParent();
                return parent.getChildren().contains(node);
            };

            assertThat(validParentTester.test(root)).isTrue();
            assertThat(validParentTester.test(child1)).isTrue();
            assertThat(validParentTester.test(child2)).isTrue();
            assertThat(validParentTester.test(grandchild1)).isTrue();
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("TokenTester should handle empty trees gracefully")
        void testTokenTester_HandlesEmptyTreesGracefully() {
            TestTreeNode emptyNode = new TestTreeNode("empty", "empty");
            TokenTester hasChildrenTester = TreeNode::hasChildren;

            assertThat(hasChildrenTester.test(emptyNode)).isFalse();
        }

        @Test
        @DisplayName("TokenTester should handle complex tree structures")
        void testTokenTester_HandlesComplexTreeStructures() {
            // Create a more complex tree
            TestTreeNode deepChild = new TestTreeNode("deep", "deep");
            TestTreeNode mediumChild = new TestTreeNode("medium", "medium", Collections.singletonList(deepChild));
            TestTreeNode complexRoot = new TestTreeNode("complexRoot", "root", Collections.singletonList(mediumChild));

            // Test for nodes at depth >= 2
            TokenTester deepNodeTester = node -> node.getDepth() >= 2;

            assertThat(deepNodeTester.test(complexRoot)).isFalse(); // depth 0
            assertThat(deepNodeTester.test(mediumChild)).isFalse(); // depth 1
            assertThat(deepNodeTester.test(deepChild)).isTrue(); // depth 2
        }

        @Test
        @DisplayName("TokenTester should be reusable across different trees")
        void testTokenTester_IsReusableAcrossTrees() {
            // Create another tree
            TestTreeNode otherRoot = new TestTreeNode("otherRoot", "root");
            TestTreeNode otherChild = new TestTreeNode("otherChild", "leaf");
            otherRoot.addChild(otherChild);

            // Same tester should work on different trees
            TokenTester leafTester = node -> !node.hasChildren();

            // Test on original tree
            assertThat(leafTester.test(child2)).isTrue();
            assertThat(leafTester.test(grandchild1)).isTrue();

            // Test on new tree
            assertThat(leafTester.test(otherChild)).isTrue();
            assertThat(leafTester.test(otherRoot)).isFalse();
        }
    }

    /**
     * Enhanced test implementation of TreeNode for testing TokenTester
     */
    private static class TestTreeNode extends ATreeNode<TestTreeNode> {
        private final String name;
        private final String type;

        public TestTreeNode(String name, String type) {
            super(Collections.emptyList());
            this.name = name;
            this.type = type;
        }

        public TestTreeNode(String name, String type, Collection<? extends TestTreeNode> children) {
            super(children);
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toContentString() {
            return name + ":" + type;
        }

        @Override
        protected TestTreeNode copyPrivate() {
            return new TestTreeNode(name, type);
        }

        @Override
        public TestTreeNode copy() {
            List<TestTreeNode> childrenCopy = new ArrayList<>();
            for (TestTreeNode child : getChildren()) {
                childrenCopy.add(child.copy());
            }
            return new TestTreeNode(name, type, childrenCopy);
        }

        @Override
        public String toString() {
            return "TestTreeNode{name='" + name + "', type='" + type + "', children=" + getNumChildren() + "}";
        }
    }
}
