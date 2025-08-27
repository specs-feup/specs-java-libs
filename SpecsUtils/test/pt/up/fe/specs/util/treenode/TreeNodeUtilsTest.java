package pt.up.fe.specs.util.treenode;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

import java.util.*;

/**
 * Test suite for TreeNodeUtils utility class.
 * 
 * @author Generated Tests
 */
@DisplayName("TreeNodeUtils Tests")
class TreeNodeUtilsTest {

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
    @DisplayName("Tree String Representation Tests")
    class StringRepresentationTests {

        @Test
        @DisplayName("toString() should create readable tree representation")
        void testToString_CreatesReadableRepresentation() {
            String treeString = TreeNodeUtils.toString(root, "");

            assertThat(treeString).isNotEmpty();
            assertThat(treeString).contains("root");
            assertThat(treeString).contains("child1");
            assertThat(treeString).contains("child2");
            assertThat(treeString).contains("grandchild1");
        }

        @Test
        @DisplayName("toString() with prefix should include prefix")
        void testToString_WithPrefix_IncludesPrefix() {
            String prefix = "> ";
            String treeString = TreeNodeUtils.toString(root, prefix);

            assertThat(treeString).startsWith(prefix);
        }

        @Test
        @DisplayName("toString() for single node should work")
        void testToString_SingleNode_Works() {
            TestTreeNode singleNode = new TestTreeNode("single");
            String treeString = TreeNodeUtils.toString(singleNode, "");

            assertThat(treeString).contains("single");
        }
    }

    @Nested
    @DisplayName("Tree Traversal Utility Tests")
    class TraversalUtilityTests {

        @Test
        @DisplayName("getDescendants() should return descendants of specified type")
        void testGetDescendants_ReturnsCorrectType() {
            List<TestTreeNode> descendants = TreeNodeUtils.getDescendants(TestTreeNode.class,
                    Collections.singletonList(root));

            // Should return all descendants but not root itself
            assertThat(descendants).containsExactlyInAnyOrder(child1, child2, grandchild1);
        }

        @Test
        @DisplayName("getDescendantsAndSelves() should include input nodes")
        void testGetDescendantsAndSelves_IncludesSelf() {
            List<TestTreeNode> descendantsAndSelf = TreeNodeUtils.getDescendantsAndSelves(TestTreeNode.class,
                    Collections.singletonList(root));

            // Should return all descendants and the root itself
            assertThat(descendantsAndSelf).containsExactlyInAnyOrder(root, child1, child2, grandchild1);
        }

        @Test
        @DisplayName("getDescendants() with empty input should return empty list")
        void testGetDescendants_EmptyInput_ReturnsEmpty() {
            List<TestTreeNode> result = TreeNodeUtils.getDescendants(TestTreeNode.class, Collections.emptyList());

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("Utility Method Tests")
    class UtilityMethodTests {

        @Test
        @DisplayName("sanitizeNode() should return node if no parent")
        void testSanitizeNode_NoParent_ReturnsSame() {
            TestTreeNode result = TreeNodeUtils.sanitizeNode(root);

            assertThat(result).isSameAs(root);
        }

        @Test
        @DisplayName("sanitizeNode() should return copy if has parent")
        void testSanitizeNode_HasParent_ReturnsCopy() {
            TestTreeNode result = TreeNodeUtils.sanitizeNode(child1);

            assertThat(result).isNotSameAs(child1);
            assertThat(result.getName()).isEqualTo(child1.getName());
            assertThat(result.hasParent()).isFalse();
        }

        @Test
        @DisplayName("copy() should create copies of all nodes")
        void testCopy_CreatesCorrectCopies() {
            List<TestTreeNode> original = Arrays.asList(child1, child2);
            List<TestTreeNode> copied = TreeNodeUtils.copy(original);

            assertThat(copied).hasSize(2);
            assertThat(copied.get(0)).isNotSameAs(child1);
            assertThat(copied.get(1)).isNotSameAs(child2);
            assertThat(copied.get(0).getName()).isEqualTo(child1.getName());
            assertThat(copied.get(1).getName()).isEqualTo(child2.getName());
        }
    }

    @Nested
    @DisplayName("Tree Copy and Manipulation Tests")
    class CopyManipulationTests {

        @Test
        @DisplayName("Tree copy operations should preserve structure")
        void testTreeCopy_PreservesStructure() {
            // Test that copy operations work correctly
            TestTreeNode copied = root.copy();

            assertThat(copied).isNotSameAs(root);
            assertThat(copied.getNumChildren()).isEqualTo(root.getNumChildren());
            assertThat(copied.getName()).isEqualTo(root.getName());
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("Operations on null node should handle gracefully")
        void testOperationsOnNull_HandleGracefully() {
            assertThatThrownBy(() -> TreeNodeUtils.toString(null, ""))
                    .isInstanceOf(Exception.class);
        }

        @Test
        @DisplayName("Empty tree operations should work")
        void testEmptyTree_OperationsWork() {
            TestTreeNode emptyNode = new TestTreeNode("empty");

            List<TestTreeNode> descendants = TreeNodeUtils.getDescendants(TestTreeNode.class,
                    Collections.singletonList(emptyNode));
            assertThat(descendants).isEmpty();

            List<TestTreeNode> descendantsAndSelf = TreeNodeUtils.getDescendantsAndSelves(TestTreeNode.class,
                    Collections.singletonList(emptyNode));
            assertThat(descendantsAndSelf).containsExactly(emptyNode);
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

        public String getName() {
            return name;
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
