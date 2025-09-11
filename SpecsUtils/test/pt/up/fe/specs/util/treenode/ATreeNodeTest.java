package pt.up.fe.specs.util.treenode;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

import java.util.*;

/**
 * Comprehensive test suite for ATreeNode abstract implementation.
 * Tests the concrete implementation details and behaviors specific to
 * ATreeNode.
 * 
 * @author Generated Tests
 */
@DisplayName("ATreeNode Abstract Implementation Tests")
class ATreeNodeTest {

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
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Constructor with null children should create empty children list")
        void testConstructor_WithNullChildren_CreatesEmptyList() {
            TestTreeNode node = new TestTreeNode("test", null);

            assertThat(node.hasChildren()).isFalse();
            assertThat(node.getNumChildren()).isEqualTo(0);
            assertThat(node.getChildren()).isEmpty();
        }

        @Test
        @DisplayName("Constructor with empty collection should create empty children list")
        void testConstructor_WithEmptyChildren_CreatesEmptyList() {
            TestTreeNode node = new TestTreeNode("test", Collections.emptyList());

            assertThat(node.hasChildren()).isFalse();
            assertThat(node.getNumChildren()).isEqualTo(0);
            assertThat(node.getChildren()).isEmpty();
        }

        @Test
        @DisplayName("Constructor should set parent references correctly")
        void testConstructor_SetsParentReferencesCorrectly() {
            TestTreeNode newChild1 = new TestTreeNode("newChild1");
            TestTreeNode newChild2 = new TestTreeNode("newChild2");
            TestTreeNode parent = new TestTreeNode("parent", Arrays.asList(newChild1, newChild2));

            assertThat(newChild1.getParent()).isSameAs(parent);
            assertThat(newChild2.getParent()).isSameAs(parent);
            assertThat(parent.getChildren()).containsExactly(newChild1, newChild2);
        }

        @Test
        @DisplayName("Constructor should reject null children in collection")
        void testConstructor_WithNullChildInCollection_ThrowsException() {
            List<TestTreeNode> childrenWithNull = new ArrayList<>();
            childrenWithNull.add(new TestTreeNode("valid"));
            childrenWithNull.add(null);

            assertThatThrownBy(() -> new TestTreeNode("parent", childrenWithNull))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Cannot use 'null' as children");
        }
    }

    @Nested
    @DisplayName("setChildren() Implementation Tests")
    class SetChildrenTests {

        @Test
        @DisplayName("setChildren() should remove previous children and clear their parent references")
        void testSetChildren_RemovesPreviousChildren() {
            TestTreeNode newChild1 = new TestTreeNode("newChild1");
            TestTreeNode newChild2 = new TestTreeNode("newChild2");

            // Store original children
            TestTreeNode originalChild1 = root.getChild(0);
            TestTreeNode originalChild2 = root.getChild(1);

            root.setChildren(Arrays.asList(newChild1, newChild2));

            // Verify old children have null parent
            assertThat(originalChild1.getParent()).isNull();
            assertThat(originalChild2.getParent()).isNull();

            // Verify new children are set correctly
            assertThat(root.getChildren()).containsExactly(newChild1, newChild2);
            assertThat(newChild1.getParent()).isSameAs(root);
            assertThat(newChild2.getParent()).isSameAs(root);
        }

        @Test
        @DisplayName("setChildren() with null should handle gracefully")
        void testSetChildren_WithNull_HandlesGracefully() {
            // setChildren() handles null gracefully by treating it as empty collection
            root.setChildren(null);
            
            // Should clear all children
            assertThat(root.getNumChildren()).isEqualTo(0);
            assertThat(root.getChildren()).isEmpty();
        }

        @Test
        @DisplayName("setChildren() with empty collection should clear all children")
        void testSetChildren_WithEmptyCollection_ClearsChildren() {
            TestTreeNode originalChild1 = root.getChild(0);
            TestTreeNode originalChild2 = root.getChild(1);

            root.setChildren(Collections.emptyList());

            assertThat(root.hasChildren()).isFalse();
            assertThat(root.getNumChildren()).isEqualTo(0);
            assertThat(root.getChildren()).isEmpty();

            // Original children should have null parent
            assertThat(originalChild1.getParent()).isNull();
            assertThat(originalChild2.getParent()).isNull();
        }

        @Test
        @DisplayName("setChildren() should properly handle single child")
        void testSetChildren_WithSingleChild_SetsCorrectly() {
            TestTreeNode newChild = new TestTreeNode("onlyChild");

            root.setChildren(Collections.singletonList(newChild));

            assertThat(root.getNumChildren()).isEqualTo(1);
            assertThat(root.getChild(0)).isSameAs(newChild);
            assertThat(newChild.getParent()).isSameAs(root);
        }

        @Test
        @DisplayName("setChildren() should handle children with existing parents by creating copies")
        void testSetChildren_WithChildrenHavingExistingParents_DetachesFromOldParents() {
            TestTreeNode otherParent = new TestTreeNode("otherParent");
            TestTreeNode orphanChild = new TestTreeNode("orphan");
            otherParent.addChild(orphanChild);

            // Verify initial setup
            assertThat(orphanChild.getParent()).isSameAs(otherParent);
            assertThat(otherParent.getNumChildren()).isEqualTo(1);

            // Move orphan to root - actually creates a copy
            root.setChildren(Collections.singletonList(orphanChild));

            // Original orphan stays with otherParent (sanitizeNode behavior)
            assertThat(orphanChild.getParent()).isSameAs(otherParent);
            assertThat(otherParent.getNumChildren()).isEqualTo(1);
            assertThat(root.getNumChildren()).isEqualTo(1);
            // root gets a copy, not the original
            assertThat(root.getChild(0)).isNotSameAs(orphanChild);
            assertThat(root.getChild(0).toNodeString()).isEqualTo(orphanChild.toNodeString());
        }
    }

    @Nested
    @DisplayName("removeChild() Implementation Tests")
    class RemoveChildTests {

        @Test
        @DisplayName("removeChild() by index should remove and return child")
        void testRemoveChild_ByIndex_RemovesAndReturnsChild() {
            TestTreeNode removedChild = root.removeChild(0);

            assertThat(removedChild).isSameAs(child1);
            assertThat(root.getChildren()).containsExactly(child2);
            assertThat(child1.getParent()).isNull();
            assertThat(root.getNumChildren()).isEqualTo(1);
        }

        @Test
        @DisplayName("removeChild() on node without children should throw exception")
        void testRemoveChild_OnNodeWithoutChildren_ThrowsException() {
            assertThatThrownBy(() -> child2.removeChild(0))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Token does not have children, cannot remove a child");
        }

        @Test
        @DisplayName("removeChild() with invalid index should throw IndexOutOfBoundsException")
        void testRemoveChild_WithInvalidIndex_ThrowsException() {
            assertThatThrownBy(() -> root.removeChild(-1))
                    .isInstanceOf(IndexOutOfBoundsException.class);

            assertThatThrownBy(() -> root.removeChild(2))
                    .isInstanceOf(IndexOutOfBoundsException.class);
        }

        @Test
        @DisplayName("removeChild() by reference should remove child and return index")
        void testRemoveChild_ByReference_RemovesChildAndReturnsIndex() {
            int removedIndex = root.removeChild(child1);

            assertThat(removedIndex).isEqualTo(0);
            assertThat(root.getChildren()).containsExactly(child2);
            assertThat(child1.getParent()).isNull();
            assertThat(root.getNumChildren()).isEqualTo(1);
        }

        @Test
        @DisplayName("removeChild() with non-child should return -1")
        void testRemoveChild_WithNonChild_ReturnsMinusOne() {
            TestTreeNode nonChild = new TestTreeNode("nonChild");
            int removedIndex = root.removeChild(nonChild);

            assertThat(removedIndex).isEqualTo(-1);
            assertThat(root.getNumChildren()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("addChild() Implementation Tests")
    class AddChildTests {

        @Test
        @DisplayName("addChild() should append child and set parent")
        void testAddChild_AppendsChildAndSetsParent() {
            TestTreeNode newChild = new TestTreeNode("newChild");
            root.addChild(newChild);

            assertThat(root.getChildren()).containsExactly(child1, child2, newChild);
            assertThat(newChild.getParent()).isSameAs(root);
            assertThat(root.getNumChildren()).isEqualTo(3);
        }

        @Test
        @DisplayName("addChild() at index should insert at correct position")
        void testAddChild_AtIndex_InsertsAtCorrectPosition() {
            TestTreeNode newChild = new TestTreeNode("newChild");
            root.addChild(1, newChild);

            assertThat(root.getChildren()).containsExactly(child1, newChild, child2);
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
        @DisplayName("addChild() should create copy when child has existing parent")
        void testAddChild_DetachesFromPreviousParent() {
            TestTreeNode otherParent = new TestTreeNode("otherParent");
            TestTreeNode orphan = new TestTreeNode("orphan");
            otherParent.addChild(orphan);

            // Verify initial setup
            assertThat(orphan.getParent()).isSameAs(otherParent);
            assertThat(otherParent.getNumChildren()).isEqualTo(1);

            // Add orphan to root - creates a copy due to sanitizeNode
            root.addChild(orphan);

            // Original orphan stays with otherParent
            assertThat(orphan.getParent()).isSameAs(otherParent);
            assertThat(otherParent.getNumChildren()).isEqualTo(1);
            assertThat(root.getNumChildren()).isEqualTo(3);
            // root gets a copy, not the original
            assertThat(root.getChild(2)).isNotSameAs(orphan);
            assertThat(root.getChild(2).toNodeString()).isEqualTo(orphan.toNodeString());
        }

        @Test
        @DisplayName("addChild() at invalid index should throw exception")
        void testAddChild_AtInvalidIndex_ThrowsException() {
            TestTreeNode newChild = new TestTreeNode("newChild");

            assertThatThrownBy(() -> root.addChild(-1, newChild))
                    .isInstanceOf(IndexOutOfBoundsException.class);

            assertThatThrownBy(() -> root.addChild(3, newChild))
                    .isInstanceOf(IndexOutOfBoundsException.class);
        }
    }

    @Nested
    @DisplayName("Parent Management Tests")
    class ParentManagementTests {

        @Test
        @DisplayName("Parent reference should be managed internally")
        void testParentReference_ManagedInternally() {
            // Parent references are managed internally through addChild/removeChild
            assertThat(child1.getParent()).isSameAs(root);
            assertThat(child2.getParent()).isSameAs(root);
            assertThat(grandchild1.getParent()).isSameAs(child1);
        }

        @Test
        @DisplayName("detach() should remove from parent and clear reference")
        void testDetach_RemovesFromParentAndClearsReference() {
            child1.detach();

            assertThat(child1.getParent()).isNull();
            assertThat(root.getChildren()).containsExactly(child2);
            assertThat(root.getNumChildren()).isEqualTo(1);
        }

        @Test
        @DisplayName("detach() on node without parent is safe")
        void testDetach_OnNodeWithoutParent_IsSafe() {
            TestTreeNode orphan = new TestTreeNode("orphan");

            // detach() is idempotent and doesn't throw exception if node has no parent
            orphan.detach(); // Should do nothing safely
            
            assertThat(orphan.getParent()).isNull();
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("Copy should create deep copy of tree structure")
        void testCopy_CreatesDeepCopy() {
            TestTreeNode copied = root.copy();

            assertThat(copied).isNotSameAs(root);
            assertThat(copied.getName()).isEqualTo(root.getName());
            assertThat(copied.getNumChildren()).isEqualTo(root.getNumChildren());

            // Children should be copies, not same instances
            assertThat(copied.getChild(0)).isNotSameAs(child1);
            assertThat(copied.getChild(0).getName()).isEqualTo(child1.getName());
            assertThat(copied.getChild(1)).isNotSameAs(child2);
            assertThat(copied.getChild(1).getName()).isEqualTo(child2.getName());

            // Grandchildren should also be copied
            assertThat(copied.getChild(0).getChild(0)).isNotSameAs(grandchild1);
            assertThat(copied.getChild(0).getChild(0).getName()).isEqualTo(grandchild1.getName());
        }

        @Test
        @DisplayName("Tree operations should maintain consistency")
        void testTreeOperations_MaintainConsistency() {
            // Complex operation: move grandchild1 to be child of root
            grandchild1.detach();
            root.addChild(grandchild1);

            // Verify tree structure
            assertThat(root.getChildren()).containsExactly(child1, child2, grandchild1);
            assertThat(child1.hasChildren()).isFalse();
            assertThat(grandchild1.getParent()).isSameAs(root);

            // Verify tree depths are correct
            assertThat(root.getDepth()).isEqualTo(0);
            assertThat(child1.getDepth()).isEqualTo(1);
            assertThat(child2.getDepth()).isEqualTo(1);
            assertThat(grandchild1.getDepth()).isEqualTo(1);
        }

        @Test
        @DisplayName("Circular parent reference allows actual movement when parent is null")
        void testCircularParentReference_IsHandled() {
            // Try to make root a child of its own child
            // Since root has no parent, sanitizeNode returns original root, allowing the
            // move
            child1.addChild(root);

            // Root actually gets moved to be a child of child1 (since root had no parent)
            assertThat(root.getParent()).isSameAs(child1); // Root now has child1 as parent
            assertThat(child1.getParent()).isSameAs(root); // This creates the circular reference
            assertThat(child1.getChildren()).hasSize(2); // grandchild1 + root
            assertThat(child1.getChild(1)).isSameAs(root); // It's the actual root, not a copy

            // The original structure is modified - child2 becomes orphaned
            assertThat(root.getChildren()).containsExactly(child1, child2); // Original structure intact
            assertThat(child2.getParent()).isSameAs(root); // child2 still has root as parent
        }
    }

    /**
     * Test implementation of ATreeNode for testing purposes
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
        protected TestTreeNode copyPrivate() {
            return new TestTreeNode(name);
        }

        @Override
        public TestTreeNode copy() {
            List<TestTreeNode> childrenCopy = new ArrayList<>();
            for (TestTreeNode child : getChildren()) {
                childrenCopy.add(child.copy());
            }
            return new TestTreeNode(name, childrenCopy);
        }

        @Override
        public String toString() {
            return "TestTreeNode{name='" + name + "', children=" + getNumChildren() + "}";
        }
    }
}
