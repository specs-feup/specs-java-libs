package pt.up.fe.specs.util.treenode;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

import java.util.*;

/**
 * Test suite for ChildrenIterator utility class.
 * 
 * 
 * @author Generated Tests
 */
@DisplayName("ChildrenIterator Tests")
class ChildrenIteratorTest {

    private TestTreeNode root;
    private TestTreeNode child1;
    private TestTreeNode child2;
    private TestTreeNode child3;

    @BeforeEach
    void setUp() {
        // Create test tree structure with multiple children
        child1 = new TestTreeNode("child1");
        child2 = new TestTreeNode("child2");
        child3 = new TestTreeNode("child3");
        root = new TestTreeNode("root", Arrays.asList(child1, child2, child3));
    }

    @Nested
    @DisplayName("Iterator Functionality Tests")
    class IteratorFunctionalityTests {

        @Test
        @DisplayName("hasNext() should return true when more children exist")
        void testHasNext_MoreChildrenExist_ReturnsTrue() {
            ChildrenIterator<TestTreeNode> iterator = root.getChildrenIterator();

            assertThat(iterator.hasNext()).isTrue();
        }

        @Test
        @DisplayName("hasNext() should return false when no more children")
        void testHasNext_NoMoreChildren_ReturnsFalse() {
            TestTreeNode emptyNode = new TestTreeNode("empty");
            ChildrenIterator<TestTreeNode> iterator = emptyNode.getChildrenIterator();

            assertThat(iterator.hasNext()).isFalse();
        }

        @Test
        @DisplayName("next() should return children in order")
        void testNext_ReturnsChildrenInOrder() {
            ChildrenIterator<TestTreeNode> iterator = root.getChildrenIterator();

            assertThat(iterator.next()).isSameAs(child1);
            assertThat(iterator.next()).isSameAs(child2);
            assertThat(iterator.next()).isSameAs(child3);
        }

        @Test
        @DisplayName("next() should throw exception when no more elements")
        void testNext_NoMoreElements_ThrowsException() {
            TestTreeNode emptyNode = new TestTreeNode("empty");
            ChildrenIterator<TestTreeNode> iterator = emptyNode.getChildrenIterator();

            assertThatThrownBy(iterator::next)
                    .isInstanceOf(Exception.class);
        }
    }

    @Nested
    @DisplayName("Iterator Modification Tests")
    class IteratorModificationTests {

        @Test
        @DisplayName("remove() should remove current element")
        void testRemove_RemovesCurrentElement() {
            ChildrenIterator<TestTreeNode> iterator = root.getChildrenIterator();

            // Move to first element and remove it
            TestTreeNode first = iterator.next();
            iterator.remove();

            assertThat(root.getChildren()).doesNotContain(first);
            assertThat(root.getNumChildren()).isEqualTo(2);
            assertThat(first.getParent()).isNull();
        }

        @Test
        @DisplayName("remove() should work in middle of iteration")
        void testRemove_MiddleOfIteration_Works() {
            ChildrenIterator<TestTreeNode> iterator = root.getChildrenIterator();

            // Skip first, remove second
            iterator.next(); // child1
            TestTreeNode second = iterator.next(); // child2
            iterator.remove();

            assertThat(root.getChildren()).containsExactly(child1, child3);
            assertThat(second.getParent()).isNull();
        }

        @Test
        @DisplayName("remove() without next() should throw exception")
        void testRemove_WithoutNext_ThrowsException() {
            ChildrenIterator<TestTreeNode> iterator = root.getChildrenIterator();

            assertThatThrownBy(iterator::remove)
                    .isInstanceOf(Exception.class);
        }

        @Test
        @DisplayName("remove() twice should throw exception")
        void testRemove_Twice_ThrowsException() {
            ChildrenIterator<TestTreeNode> iterator = root.getChildrenIterator();

            iterator.next();
            iterator.remove();

            assertThatThrownBy(iterator::remove)
                    .isInstanceOf(Exception.class);
        }
    }

    @Nested
    @DisplayName("Iterator Edge Cases")
    class IteratorEdgeCasesTests {

        @Test
        @DisplayName("Iterator should handle single child correctly")
        void testIterator_SingleChild_HandlesCorrectly() {
            TestTreeNode singleChild = new TestTreeNode("single");
            TestTreeNode parent = new TestTreeNode("parent", Collections.singletonList(singleChild));

            ChildrenIterator<TestTreeNode> iterator = parent.getChildrenIterator();

            assertThat(iterator.hasNext()).isTrue();
            assertThat(iterator.next()).isSameAs(singleChild);
            assertThat(iterator.hasNext()).isFalse();
        }

        @Test
        @DisplayName("Iterator should work after removing all elements")
        void testIterator_RemoveAll_Works() {
            ChildrenIterator<TestTreeNode> iterator = root.getChildrenIterator();

            // Remove all children
            while (iterator.hasNext()) {
                iterator.next();
                iterator.remove();
            }

            assertThat(root.getNumChildren()).isEqualTo(0);
            assertThat(root.hasChildren()).isFalse();
        }

        @Test
        @DisplayName("Multiple iterators should work independently")
        void testMultipleIterators_WorkIndependently() {
            ChildrenIterator<TestTreeNode> iterator1 = root.getChildrenIterator();
            ChildrenIterator<TestTreeNode> iterator2 = root.getChildrenIterator();

            // Advance first iterator
            iterator1.next();
            iterator1.next();

            // Second iterator should start from beginning
            assertThat(iterator2.next()).isSameAs(child1);
        }

        @Test
        @DisplayName("Iterator state should be consistent after modifications")
        void testIterator_StateConsistentAfterModifications() {
            ChildrenIterator<TestTreeNode> iterator = root.getChildrenIterator();

            iterator.next(); // child1
            iterator.remove();

            // Should continue with next element (originally child2, now at index 0)
            assertThat(iterator.hasNext()).isTrue();
            TestTreeNode next = iterator.next();
            assertThat(next).isSameAs(child2);
        }
    }

    @Nested
    @DisplayName("Iterator Integration Tests")
    class IteratorIntegrationTests {

        @Test
        @DisplayName("Iterator should work with manual iteration")
        void testIterator_ManualIteration_Works() {
            List<TestTreeNode> visited = new ArrayList<>();

            Iterator<TestTreeNode> iterator = root.iterator();
            while (iterator.hasNext()) {
                visited.add(iterator.next());
            }

            assertThat(visited).containsExactly(child1, child2, child3);
        }

        @Test
        @DisplayName("Iterator removal should update parent correctly")
        void testIterator_RemovalUpdatesParent_Correctly() {
            ChildrenIterator<TestTreeNode> iterator = root.getChildrenIterator();
            TestTreeNode toRemove = iterator.next();
            iterator.remove();

            // Parent should be updated
            assertThat(toRemove.getParent()).isNull();
            assertThat(root.getChildren()).doesNotContain(toRemove);

            // Remaining children should still have correct parent
            for (TestTreeNode child : root.getChildren()) {
                assertThat(child.getParent()).isSameAs(root);
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
