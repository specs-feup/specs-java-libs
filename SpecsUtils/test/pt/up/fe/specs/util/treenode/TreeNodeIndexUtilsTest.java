package pt.up.fe.specs.util.treenode;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

import java.util.*;

/**
 * Test suite for TreeNodeIndexUtils utility class.
 * 
 * @author Generated Tests
 */
@DisplayName("TreeNodeIndexUtils Tests")
class TreeNodeIndexUtilsTest {

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
    @DisplayName("Index Finding Tests")
    class IndexFindingTests {

        @Test
        @DisplayName("indexesOf() should return correct indices for matching type")
        void testIndexesOf_ReturnsCorrectIndices() {
            List<TestTreeNode> children = root.getChildren();
            List<Integer> indices = TreeNodeIndexUtils.indexesOf(children, TestTreeNode.class);

            assertThat(indices).containsExactly(0, 1);
        }

        @Test
        @DisplayName("indexesOf() should return empty list for non-matching type")
        void testIndexesOf_NonMatchingType_ReturnsEmpty() {
            List<TestTreeNode> children = root.getChildren();
            List<Integer> indices = TreeNodeIndexUtils.indexesOf(children, SpecialTestTreeNode.class);

            assertThat(indices).isEmpty();
        }

        @Test
        @DisplayName("lastIndexOf() should return last occurrence of type")
        void testLastIndexOf_ReturnsLastOccurrence() {
            List<TestTreeNode> children = root.getChildren();
            Optional<Integer> lastIndex = TreeNodeIndexUtils.lastIndexOf(children, TestTreeNode.class);

            assertThat(lastIndex).isPresent();
            assertThat(lastIndex.get()).isEqualTo(1); // Index of child2
        }

        @Test
        @DisplayName("lastIndexOf() should return empty for non-existent type")
        void testLastIndexOf_NonExistentType_ReturnsEmpty() {
            List<TestTreeNode> children = root.getChildren();
            Optional<Integer> lastIndex = TreeNodeIndexUtils.lastIndexOf(children, SpecialTestTreeNode.class);

            assertThat(lastIndex).isEmpty();
        }
    }

    @Nested
    @DisplayName("Child Navigation Tests")
    class ChildNavigationTests {

        @Test
        @DisplayName("getChild() with single index should return correct child")
        void testGetChild_SingleIndex_ReturnsCorrectChild() {
            TestTreeNode result = TreeNodeIndexUtils.getChild(root, 0);

            assertThat(result).isSameAs(child1);
        }

        @Test
        @DisplayName("getChild() with multiple indices should navigate path")
        void testGetChild_MultipleIndices_NavigatesPath() {
            TestTreeNode result = TreeNodeIndexUtils.getChild(root, 0, 0);

            assertThat(result).isSameAs(grandchild1);
        }

        @Test
        @DisplayName("getChild() with index list should work")
        void testGetChild_IndexList_Works() {
            List<Integer> path = Arrays.asList(0, 0);
            TestTreeNode result = TreeNodeIndexUtils.getChild(root, path);

            assertThat(result).isSameAs(grandchild1);
        }

        @Test
        @DisplayName("getChild() with invalid index should throw exception")
        void testGetChild_InvalidIndex_ThrowsException() {
            assertThatThrownBy(() -> TreeNodeIndexUtils.getChild(root, 999))
                    .isInstanceOf(Exception.class);
        }

        @Test
        @DisplayName("getChild() with empty path should return original node")
        void testGetChild_EmptyPath_ReturnsOriginal() {
            List<Integer> emptyPath = Collections.emptyList();
            TestTreeNode result = TreeNodeIndexUtils.getChild(root, emptyPath);

            assertThat(result).isSameAs(root);
        }
    }

    @Nested
    @DisplayName("Tree Manipulation Tests")
    class TreeManipulationTests {

        @Test
        @DisplayName("replaceChild() should replace child at specified path")
        void testReplaceChild_ReplacesCorrectChild() {
            TestTreeNode replacement = new TestTreeNode("replacement");
            List<Integer> path = Arrays.asList(1); // Replace child2

            TreeNodeIndexUtils.replaceChild(root, replacement, path);

            assertThat(root.getChild(1)).isSameAs(replacement);
            assertThat(replacement.getParent()).isSameAs(root);
        }

        @Test
        @DisplayName("replaceChild() with deep path should work")
        void testReplaceChild_DeepPath_Works() {
            TestTreeNode replacement = new TestTreeNode("deepReplacement");
            List<Integer> path = Arrays.asList(0, 0); // Replace grandchild1

            TreeNodeIndexUtils.replaceChild(root, replacement, path);

            TestTreeNode result = TreeNodeIndexUtils.getChild(root, 0, 0);
            assertThat(result).isSameAs(replacement);
            assertThat(replacement.getParent()).isSameAs(child1);
        }

        @Test
        @DisplayName("replaceChild() with invalid path should throw exception")
        void testReplaceChild_InvalidPath_ThrowsException() {
            TestTreeNode replacement = new TestTreeNode("replacement");
            List<Integer> invalidPath = Arrays.asList(999);

            assertThatThrownBy(() -> TreeNodeIndexUtils.replaceChild(root, replacement, invalidPath))
                    .isInstanceOf(Exception.class);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("Operations on empty lists should handle gracefully")
        void testOperationsOnEmptyList_HandleGracefully() {
            List<TestTreeNode> emptyList = Collections.emptyList();

            List<Integer> indices = TreeNodeIndexUtils.indexesOf(emptyList, TestTreeNode.class);
            assertThat(indices).isEmpty();

            Optional<Integer> lastIndex = TreeNodeIndexUtils.lastIndexOf(emptyList, TestTreeNode.class);
            assertThat(lastIndex).isEmpty();
        }

        @Test
        @DisplayName("lastIndexExcept() should work correctly")
        void testLastIndexExcept_WorksCorrectly() {
            List<TestTreeNode> nodes = Arrays.asList(child1, child2, grandchild1);
            Collection<Class<? extends TestTreeNode>> exceptTypes = Arrays.asList();

            Optional<Integer> lastIndex = TreeNodeIndexUtils.lastIndexExcept(nodes, exceptTypes);

            assertThat(lastIndex).isPresent();
            assertThat(lastIndex.get()).isEqualTo(2); // Last index
        }

        @Test
        @DisplayName("Operations on single-element lists should work")
        void testOperationsOnSingleElement_Work() {
            List<TestTreeNode> singleList = Collections.singletonList(child1);

            List<Integer> indices = TreeNodeIndexUtils.indexesOf(singleList, TestTreeNode.class);
            assertThat(indices).containsExactly(0);

            Optional<Integer> lastIndex = TreeNodeIndexUtils.lastIndexOf(singleList, TestTreeNode.class);
            assertThat(lastIndex).isPresent();
            assertThat(lastIndex.get()).isEqualTo(0);
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
