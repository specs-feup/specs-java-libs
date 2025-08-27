package pt.up.fe.specs.util.treenode;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Comprehensive test suite for TreeNode interface and its default
 * implementations.
 * Tests core tree functionality, navigation, manipulation, and edge cases.
 * 
 * @author Generated Tests
 */
@DisplayName("TreeNode Interface Tests")
class TreeNodeTest {

    @TempDir
    File tempDir;

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
    @DisplayName("Basic Tree Structure Tests")
    class BasicStructureTests {

        @Test
        @DisplayName("hasChildren() should return true for nodes with children")
        void testHasChildren_WithChildren_ReturnsTrue() {
            assertThat(root.hasChildren()).isTrue();
            assertThat(child1.hasChildren()).isTrue();
        }

        @Test
        @DisplayName("hasChildren() should return false for leaf nodes")
        void testHasChildren_WithoutChildren_ReturnsFalse() {
            assertThat(child2.hasChildren()).isFalse();
            assertThat(grandchild1.hasChildren()).isFalse();
        }

        @Test
        @DisplayName("getNumChildren() should return correct count")
        void testGetNumChildren_ReturnsCorrectCount() {
            assertThat(root.getNumChildren()).isEqualTo(2);
            assertThat(child1.getNumChildren()).isEqualTo(1);
            assertThat(child2.getNumChildren()).isEqualTo(0);
            assertThat(grandchild1.getNumChildren()).isEqualTo(0);
        }

        @Test
        @DisplayName("getChildren() should return correct children")
        void testGetChildren_ReturnsCorrectChildren() {
            List<TestTreeNode> rootChildren = root.getChildren();
            assertThat(rootChildren).containsExactly(child1, child2);

            List<TestTreeNode> child1Children = child1.getChildren();
            assertThat(child1Children).containsExactly(grandchild1);

            List<TestTreeNode> leafChildren = child2.getChildren();
            assertThat(leafChildren).isEmpty();
        }
    }

    @Nested
    @DisplayName("Tree Navigation Tests")
    class NavigationTests {

        @Test
        @DisplayName("getParent() should return correct parent")
        void testGetParent_ReturnsCorrectParent() {
            assertThat(root.getParent()).isNull();
            assertThat(child1.getParent()).isSameAs(root);
            assertThat(child2.getParent()).isSameAs(root);
            assertThat(grandchild1.getParent()).isSameAs(child1);
        }

        @Test
        @DisplayName("hasParent() should return correct value")
        void testHasParent_ReturnsCorrectValue() {
            assertThat(root.hasParent()).isFalse();
            assertThat(child1.hasParent()).isTrue();
            assertThat(child2.hasParent()).isTrue();
            assertThat(grandchild1.hasParent()).isTrue();
        }

        @Test
        @DisplayName("getRoot() should return the root node")
        void testGetRoot_ReturnsRootNode() {
            assertThat(root.getRoot()).isSameAs(root);
            assertThat(child1.getRoot()).isSameAs(root);
            assertThat(child2.getRoot()).isSameAs(root);
            assertThat(grandchild1.getRoot()).isSameAs(root);
        }

        @Test
        @DisplayName("getChild() with valid index should return correct child")
        void testGetChild_ValidIndex_ReturnsCorrectChild() {
            assertThat(root.getChild(0)).isSameAs(child1);
            assertThat(root.getChild(1)).isSameAs(child2);
            assertThat(child1.getChild(0)).isSameAs(grandchild1);
        }

        @Test
        @DisplayName("getChild() behavior with invalid indices")
        void testGetChild_InvalidIndex_MixedBehavior() {
            // When node has children but invalid index, throws IndexOutOfBoundsException
            assertThatThrownBy(() -> root.getChild(-1))
                    .isInstanceOf(IndexOutOfBoundsException.class);

            assertThatThrownBy(() -> root.getChild(2))
                    .isInstanceOf(IndexOutOfBoundsException.class);

            // When node has no children, returns null instead of throwing
            TestTreeNode result = child2.getChild(0);
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("getChildTry() should return Optional with valid index")
        void testGetChildTry_ValidIndex_ReturnsOptional() {
            assertThat(root.getChildTry(TestTreeNode.class, 0)).contains(child1);
            assertThat(root.getChildTry(TestTreeNode.class, 1)).contains(child2);
            assertThat(child1.getChildTry(TestTreeNode.class, 0)).contains(grandchild1);
        }

        @Test
        @DisplayName("getChildTry() behavior with invalid indices (BUG #2)")
        void testGetChildTry_InvalidIndex_ThrowsException() {
            // BUG: getChildTry() should return Optional.empty() for invalid indices
            // but has mixed behavior based on underlying getChild() implementation

            // When node has children but invalid index, getChild() throws
            // IndexOutOfBoundsException
            assertThatThrownBy(() -> root.getChildTry(TestTreeNode.class, -1))
                    .isInstanceOf(IndexOutOfBoundsException.class);

            assertThatThrownBy(() -> root.getChildTry(TestTreeNode.class, 2))
                    .isInstanceOf(IndexOutOfBoundsException.class);

            // When node has no children, getChild() returns null, then SpecsCheck throws
            // RuntimeException
            assertThatThrownBy(() -> child2.getChildTry(TestTreeNode.class, 0))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("No child at index 0");
        }
    }

    @Nested
    @DisplayName("Tree Traversal Tests")
    class TraversalTests {

        @Test
        @DisplayName("getDescendants() should return all descendants")
        void testGetDescendants_ReturnsAllDescendants() {
            List<TestTreeNode> descendants = root.getDescendants();
            assertThat(descendants).containsExactly(child1, grandchild1, child2);
        }

        @Test
        @DisplayName("getDescendantsAndSelf() should include the node itself")
        void testGetDescendantsAndSelf_IncludesSelf() {
            List<TestTreeNode> descendantsAndSelf = root.getDescendantsAndSelf(TestTreeNode.class);
            assertThat(descendantsAndSelf).containsExactly(root, child1, grandchild1, child2);
        }

        @Test
        @DisplayName("getDescendantsStream() should provide stream of descendants")
        void testGetDescendantsStream_ProvidesCorrectStream() {
            List<String> names = root.getDescendantsStream()
                    .map(TestTreeNode::getName)
                    .collect(Collectors.toList());

            assertThat(names).containsExactly("child1", "grandchild1", "child2");
        }

        @Test
        @DisplayName("getDescendantsAndSelfStream() should include self in stream")
        void testGetDescendantsAndSelfStream_IncludesSelfInStream() {
            List<String> names = root.getDescendantsAndSelfStream()
                    .map(TestTreeNode::getName)
                    .collect(Collectors.toList());

            assertThat(names).containsExactly("root", "child1", "grandchild1", "child2");
        }

        @Test
        @DisplayName("iterator() should iterate over children")
        void testIterator_IteratesOverChildren() {
            List<TestTreeNode> iteratedChildren = new ArrayList<>();
            Iterator<TestTreeNode> iterator = root.iterator();
            while (iterator.hasNext()) {
                iteratedChildren.add(iterator.next());
            }

            assertThat(iteratedChildren).containsExactly(child1, child2);
        }
    }

    @Nested
    @DisplayName("Tree Manipulation Tests")
    class ManipulationTests {

        @Test
        @DisplayName("addChild() should add child and set parent")
        void testAddChild_AddsChildAndSetsParent() {
            TestTreeNode newChild = new TestTreeNode("newChild");
            root.addChild(newChild);

            assertThat(root.getChildren()).contains(newChild);
            assertThat(newChild.getParent()).isSameAs(root);
            assertThat(root.getNumChildren()).isEqualTo(3);
        }

        @Test
        @DisplayName("addChild() with null should throw exception")
        void testAddChild_WithNull_ThrowsException() {
            assertThatThrownBy(() -> root.addChild(null))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("addChild() at specific index should insert correctly")
        void testAddChild_AtIndex_InsertsCorrectly() {
            TestTreeNode newChild = new TestTreeNode("newChild");
            root.addChild(1, newChild);

            assertThat(root.getChildren()).containsExactly(child1, newChild, child2);
            assertThat(newChild.getParent()).isSameAs(root);
        }

        @Test
        @DisplayName("removeChild() should remove child and clear parent")
        void testRemoveChild_RemovesChildAndClearsParent() {
            root.removeChild(child1);

            assertThat(root.getChildren()).doesNotContain(child1);
            assertThat(child1.getParent()).isNull();
            assertThat(root.getNumChildren()).isEqualTo(1);
        }

        @Test
        @DisplayName("setChildren() should replace all children")
        void testSetChildren_ReplacesAllChildren() {
            TestTreeNode newChild1 = new TestTreeNode("newChild1");
            TestTreeNode newChild2 = new TestTreeNode("newChild2");

            root.setChildren(Arrays.asList(newChild1, newChild2));

            assertThat(root.getChildren()).containsExactly(newChild1, newChild2);
            assertThat(child1.getParent()).isNull();
            assertThat(child2.getParent()).isNull();
            assertThat(newChild1.getParent()).isSameAs(root);
            assertThat(newChild2.getParent()).isSameAs(root);
        }
    }

    @Nested
    @DisplayName("Tree Utility Tests")
    class UtilityTests {

        @Test
        @DisplayName("indexOfSelf() should return correct index")
        void testIndexOfSelf_ReturnsCorrectIndex() {
            assertThat(root.indexOfSelf()).isEqualTo(-1); // Root has no parent
            assertThat(child1.indexOfSelf()).isEqualTo(0);
            assertThat(child2.indexOfSelf()).isEqualTo(1);
            assertThat(grandchild1.indexOfSelf()).isEqualTo(0);
        }

        @Test
        @DisplayName("indexOfChild() should return correct index")
        void testIndexOfChild_ReturnsCorrectIndex() {
            assertThat(root.indexOfChild(child1)).isEqualTo(0);
            assertThat(root.indexOfChild(child2)).isEqualTo(1);
            assertThat(child1.indexOfChild(grandchild1)).isEqualTo(0);

            // Test with non-child
            TestTreeNode nonChild = new TestTreeNode("nonChild");
            assertThat(root.indexOfChild(nonChild)).isEqualTo(-1);
        }

        @Test
        @DisplayName("getDepth() should return correct depth")
        void testGetDepth_ReturnsCorrectDepth() {
            assertThat(root.getDepth()).isEqualTo(0);
            assertThat(child1.getDepth()).isEqualTo(1);
            assertThat(child2.getDepth()).isEqualTo(1);
            assertThat(grandchild1.getDepth()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("Empty tree should handle operations gracefully")
        void testEmptyTree_HandlesOperationsGracefully() {
            TestTreeNode emptyNode = new TestTreeNode("empty");

            assertThat(emptyNode.hasChildren()).isFalse();
            assertThat(emptyNode.getNumChildren()).isEqualTo(0);
            assertThat(emptyNode.getChildren()).isEmpty();
            assertThat(emptyNode.getDescendants()).isEmpty();
            assertThat(emptyNode.getDescendantsAndSelf(TestTreeNode.class)).containsExactly(emptyNode);
        }

        @Test
        @DisplayName("setChildren() with null should throw NullPointerException (BUG #1)")
        void testSetChildren_WithNull_ThrowsException() {
            // BUG: setChildren(null) should handle null gracefully but throws NPE
            assertThatThrownBy(() -> root.setChildren(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("Cannot invoke \"java.util.Collection.iterator()\"");
        }

        @Test
        @DisplayName("setChildren() with empty collection should clear children")
        void testSetChildren_WithEmptyCollection_ClearsChildren() {
            root.setChildren(Collections.emptyList());

            assertThat(root.getChildren()).isEmpty();
            assertThat(root.getNumChildren()).isEqualTo(0);
            assertThat(root.hasChildren()).isFalse();

            // Verify old children have null parent
            assertThat(child1.getParent()).isNull();
            assertThat(child2.getParent()).isNull();
        }

        @Test
        @DisplayName("Single node tree should work correctly")
        void testSingleNodeTree_WorksCorrectly() {
            TestTreeNode singleNode = new TestTreeNode("single");

            assertThat(singleNode.getRoot()).isSameAs(singleNode);
            assertThat(singleNode.hasParent()).isFalse();
            assertThat(singleNode.getParent()).isNull();
        }

        @Test
        @DisplayName("Copy operations should work correctly")
        void testCopy_WorksCorrectly() {
            TestTreeNode copied = root.copy();

            // Should be different instances
            assertThat(copied).isNotSameAs(root);
            assertThat(copied.getName()).isEqualTo(root.getName());
            assertThat(copied.getNumChildren()).isEqualTo(root.getNumChildren());

            // Children should also be copies
            assertThat(copied.getChild(0)).isNotSameAs(child1);
            assertThat(copied.getChild(0).getName()).isEqualTo(child1.getName());
        }

        @Test
        @DisplayName("Detach should remove node from parent")
        void testDetach_RemovesFromParent() {
            child1.detach();

            assertThat(child1.getParent()).isNull();
            assertThat(root.getChildren()).doesNotContain(child1);
            assertThat(root.getNumChildren()).isEqualTo(1);
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
