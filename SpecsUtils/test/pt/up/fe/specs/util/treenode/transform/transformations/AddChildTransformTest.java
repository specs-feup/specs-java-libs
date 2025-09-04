package pt.up.fe.specs.util.treenode.transform.transformations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.treenode.ATreeNode;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for AddChildTransform class.
 * Tests the specific implementation of add child transformation operations.
 * 
 * @author Generated Tests
 */
@DisplayName("AddChildTransform Tests")
class AddChildTransformTest {

    private TestTreeNode parentNode;
    private TestTreeNode childNode;
    private AddChildTransform<TestTreeNode> addChildTransform;

    @BeforeEach
    void setUp() {
        parentNode = new TestTreeNode("parent");
        childNode = new TestTreeNode("child");
        addChildTransform = new AddChildTransform<>(parentNode, childNode);
    }

    /**
     * Test tree node implementation for testing purposes.
     */
    private static class TestTreeNode extends ATreeNode<TestTreeNode> {
        private final String value;

        public TestTreeNode(String value) {
            super(null); // Call parent constructor with no children
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

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Two-parameter constructor should initialize with correct type and operands")
        void testTwoParameterConstructor_SetsCorrectTypeAndOperands() {
            assertThat(addChildTransform.getType()).isEqualTo("add-child");
            assertThat(addChildTransform.getOperands()).hasSize(2);
            assertThat(addChildTransform.getOperands().get(0)).isSameAs(parentNode);
            assertThat(addChildTransform.getOperands().get(1)).isSameAs(childNode);
        }

        @Test
        @DisplayName("Three-parameter constructor should accept position parameter")
        void testThreeParameterConstructor_AcceptsPosition() {
            AddChildTransform<TestTreeNode> transform = new AddChildTransform<>(parentNode, childNode, 0);

            assertThat(transform.getType()).isEqualTo("add-child");
            assertThat(transform.getOperands()).hasSize(2);
            assertThat(transform.getOperands().get(0)).isSameAs(parentNode);
            assertThat(transform.getOperands().get(1)).isSameAs(childNode);
        }

        @Test
        @DisplayName("Constructor should handle null baseNode gracefully")
        void testConstructor_WithNullBaseNode() {
            AddChildTransform<TestTreeNode> transform = new AddChildTransform<>(null, childNode);
            assertThat(transform.getOperands().get(0)).isNull();
            assertThat(transform.getOperands().get(1)).isSameAs(childNode);
        }

        @Test
        @DisplayName("Constructor should handle null childNode gracefully")
        void testConstructor_WithNullChildNode() {
            AddChildTransform<TestTreeNode> transform = new AddChildTransform<>(parentNode, null);
            assertThat(transform.getOperands().get(0)).isSameAs(parentNode);
            assertThat(transform.getOperands().get(1)).isNull();
        }

        @Test
        @DisplayName("Constructor should handle null position gracefully")
        void testConstructor_WithNullPosition() {
            AddChildTransform<TestTreeNode> transform = new AddChildTransform<>(parentNode, childNode, null);
            assertThat(transform.getOperands()).hasSize(2);
            assertThat(transform.getOperands().get(0)).isSameAs(parentNode);
            assertThat(transform.getOperands().get(1)).isSameAs(childNode);
        }
    }

    @Nested
    @DisplayName("Inherited Method Tests")
    class InheritedMethodTests {

        @Test
        @DisplayName("getNode1 should return the parent node")
        void testGetNode1_ReturnsParentNode() {
            assertThat(addChildTransform.getNode1()).isSameAs(parentNode);
        }

        @Test
        @DisplayName("getNode2 should return the child node")
        void testGetNode2_ReturnsChildNode() {
            assertThat(addChildTransform.getNode2()).isSameAs(childNode);
        }

        @Test
        @DisplayName("toString should contain both node hash codes")
        void testToString_ContainsBothNodeHashes() {
            String result = addChildTransform.toString();
            String parentHex = Integer.toHexString(parentNode.hashCode());
            String childHex = Integer.toHexString(childNode.hashCode());

            assertThat(result).contains("add-child");
            assertThat(result).contains("node1(" + parentHex + ")");
            assertThat(result).contains("node2(" + childHex + ")");
        }
    }

    @Nested
    @DisplayName("Execution Tests - Default Position")
    class ExecutionDefaultPositionTests {

        @Test
        @DisplayName("execute should add child to end of parent's children list")
        void testExecute_AddsChildToEnd() {
            // Verify initial state
            assertThat(parentNode.getNumChildren()).isEqualTo(0);
            assertThat(childNode.getParent()).isNull();

            // Execute the add child operation
            addChildTransform.execute();

            // Verify child was added
            assertThat(parentNode.getNumChildren()).isEqualTo(1);
            assertThat(parentNode.getChild(0)).isSameAs(childNode);
            assertThat(childNode.getParent()).isSameAs(parentNode);
        }

        @Test
        @DisplayName("execute should add child after existing children")
        void testExecute_AddsAfterExistingChildren() {
            TestTreeNode existingChild1 = new TestTreeNode("existing1");
            TestTreeNode existingChild2 = new TestTreeNode("existing2");

            parentNode.addChild(existingChild1);
            parentNode.addChild(existingChild2);

            assertThat(parentNode.getNumChildren()).isEqualTo(2);

            addChildTransform.execute();

            assertThat(parentNode.getNumChildren()).isEqualTo(3);
            assertThat(parentNode.getChild(0)).isSameAs(existingChild1);
            assertThat(parentNode.getChild(1)).isSameAs(existingChild2);
            assertThat(parentNode.getChild(2)).isSameAs(childNode);
        }

        @Test
        @DisplayName("execute should not add duplicate references when called multiple times")
        void testExecute_DetachesAndReadds() {
            addChildTransform.execute();
            assertThat(parentNode.getNumChildren()).isEqualTo(1);
            assertThat(parentNode.getChild(0)).isSameAs(childNode);

            // Execute again - since child already has parent, it will be copied
            addChildTransform.execute();
            assertThat(parentNode.getNumChildren()).isEqualTo(2);
            // The second child will be a copy, not the same instance
            assertThat(parentNode.getChild(0)).isSameAs(childNode);
            assertThat(parentNode.getChild(1)).isNotSameAs(childNode);
            // But they should have the same content
            assertThat(parentNode.getChild(1).toContentString()).isEqualTo(childNode.toContentString());
        }
    }

    @Nested
    @DisplayName("Execution Tests - Specific Position")
    class ExecutionSpecificPositionTests {

        @Test
        @DisplayName("execute should add child at specified position 0")
        void testExecute_AddsAtPositionZero() {
            AddChildTransform<TestTreeNode> transform = new AddChildTransform<>(parentNode, childNode, 0);

            transform.execute();

            assertThat(parentNode.getNumChildren()).isEqualTo(1);
            assertThat(parentNode.getChild(0)).isSameAs(childNode);
        }

        @Test
        @DisplayName("execute should insert child at specified position between existing children")
        void testExecute_InsertsAtPosition() {
            TestTreeNode child1 = new TestTreeNode("child1");
            TestTreeNode child2 = new TestTreeNode("child2");
            TestTreeNode child3 = new TestTreeNode("child3");

            parentNode.addChild(child1);
            parentNode.addChild(child3);

            AddChildTransform<TestTreeNode> transform = new AddChildTransform<>(parentNode, child2, 1);
            transform.execute();

            assertThat(parentNode.getNumChildren()).isEqualTo(3);
            assertThat(parentNode.getChild(0)).isSameAs(child1);
            assertThat(parentNode.getChild(1)).isSameAs(child2);
            assertThat(parentNode.getChild(2)).isSameAs(child3);
        }

        @Test
        @DisplayName("execute should add at beginning when position is 0")
        void testExecute_AddsAtBeginning() {
            TestTreeNode existingChild = new TestTreeNode("existing");
            parentNode.addChild(existingChild);

            AddChildTransform<TestTreeNode> transform = new AddChildTransform<>(parentNode, childNode, 0);
            transform.execute();

            assertThat(parentNode.getNumChildren()).isEqualTo(2);
            assertThat(parentNode.getChild(0)).isSameAs(childNode);
            assertThat(parentNode.getChild(1)).isSameAs(existingChild);
        }

        @Test
        @DisplayName("execute should handle position at end of list")
        void testExecute_AddsAtEndPosition() {
            TestTreeNode child1 = new TestTreeNode("child1");
            TestTreeNode child2 = new TestTreeNode("child2");

            parentNode.addChild(child1);
            parentNode.addChild(child2);

            AddChildTransform<TestTreeNode> transform = new AddChildTransform<>(parentNode, childNode, 2);
            transform.execute();

            assertThat(parentNode.getNumChildren()).isEqualTo(3);
            assertThat(parentNode.getChild(2)).isSameAs(childNode);
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("execute should throw exception with null parent")
        void testExecute_WithNullParent() {
            AddChildTransform<TestTreeNode> transform = new AddChildTransform<>(null, childNode);

            assertThatThrownBy(() -> transform.execute())
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("execute should handle null child node")
        void testExecute_WithNullChild() {
            AddChildTransform<TestTreeNode> transform = new AddChildTransform<>(parentNode, null);

            // This might throw an exception depending on the TreeNode implementation
            assertThatThrownBy(() -> transform.execute())
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("execute should handle invalid position gracefully")
        void testExecute_WithInvalidPosition() {
            // Negative position might be handled differently by implementation
            AddChildTransform<TestTreeNode> transform = new AddChildTransform<>(parentNode, childNode, -1);

            assertThatThrownBy(() -> transform.execute())
                    .isInstanceOf(IndexOutOfBoundsException.class);
        }

        @Test
        @DisplayName("execute should handle position beyond list size")
        void testExecute_WithPositionBeyondSize() {
            // Position way beyond current children size
            AddChildTransform<TestTreeNode> transform = new AddChildTransform<>(parentNode, childNode, 100);

            assertThatThrownBy(() -> transform.execute())
                    .isInstanceOf(IndexOutOfBoundsException.class);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with different node types")
        void testWithDifferentNodeTypes() {
            TestTreeNode node1 = new TestTreeNode("parent");
            TestTreeNode node2 = new TestTreeNode("child");

            AddChildTransform<TestTreeNode> transform = new AddChildTransform<>(node1, node2);

            assertThat(transform.getType()).isEqualTo("add-child");
            assertThat(transform.getNode1()).isSameAs(node1);
            assertThat(transform.getNode2()).isSameAs(node2);
        }

        @Test
        @DisplayName("Should maintain tree structure integrity after adding child")
        void testTreeStructureIntegrity() {
            // Build a tree: root -> parentNode
            TestTreeNode root = new TestTreeNode("root");
            root.addChild(parentNode);

            addChildTransform.execute();

            // Verify tree structure is maintained
            assertThat(root.getNumChildren()).isEqualTo(1);
            assertThat(root.getChild(0)).isSameAs(parentNode);
            assertThat(parentNode.getNumChildren()).isEqualTo(1);
            assertThat(parentNode.getChild(0)).isSameAs(childNode);
            assertThat(childNode.getParent()).isSameAs(parentNode);
        }

        @Test
        @DisplayName("Should properly add child that already has children")
        void testAddChildWithExistingChildren() {
            TestTreeNode grandChild1 = new TestTreeNode("grandchild1");
            TestTreeNode grandChild2 = new TestTreeNode("grandchild2");
            childNode.addChild(grandChild1);
            childNode.addChild(grandChild2);

            assertThat(childNode.getNumChildren()).isEqualTo(2);

            addChildTransform.execute();

            // Child should be added to parent
            assertThat(parentNode.getNumChildren()).isEqualTo(1);
            assertThat(parentNode.getChild(0)).isSameAs(childNode);

            // Child should maintain its own children
            assertThat(childNode.getNumChildren()).isEqualTo(2);
            assertThat(grandChild1.getParent()).isSameAs(childNode);
            assertThat(grandChild2.getParent()).isSameAs(childNode);
        }

        @Test
        @DisplayName("Should work correctly in complex tree structures")
        void testComplexTreeStructure() {
            // Create a complex tree structure
            TestTreeNode root = new TestTreeNode("root");
            TestTreeNode branch1 = new TestTreeNode("branch1");
            TestTreeNode branch2 = new TestTreeNode("branch2");
            TestTreeNode leaf1 = new TestTreeNode("leaf1");

            root.addChild(branch1);
            root.addChild(branch2);
            branch1.addChild(leaf1);

            // Add child to branch2
            AddChildTransform<TestTreeNode> transform = new AddChildTransform<>(branch2, childNode);
            transform.execute();

            // Verify structure integrity
            assertThat(root.getNumChildren()).isEqualTo(2);
            assertThat(branch1.getNumChildren()).isEqualTo(1);
            assertThat(branch1.getChild(0)).isSameAs(leaf1);
            assertThat(branch2.getNumChildren()).isEqualTo(1);
            assertThat(branch2.getChild(0)).isSameAs(childNode);
            assertThat(childNode.getParent()).isSameAs(branch2);
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle adding child that was already child of same parent")
        void testAddExistingChild() {
            parentNode.addChild(childNode);
            assertThat(parentNode.getNumChildren()).isEqualTo(1);

            // Adding the same child again creates a copy since original already has parent
            addChildTransform.execute();

            // Should result in child being copied and added
            assertThat(parentNode.getNumChildren()).isEqualTo(2);
            assertThat(parentNode.getChild(0)).isSameAs(childNode);
            // Second child is a copy with same content
            assertThat(parentNode.getChild(1)).isNotSameAs(childNode);
            assertThat(parentNode.getChild(1).toContentString()).isEqualTo(childNode.toContentString());
        }

        @Test
        @DisplayName("Should handle child moving from different parent")
        void testMoveChildFromDifferentParent() {
            TestTreeNode originalParent = new TestTreeNode("original");
            originalParent.addChild(childNode);

            assertThat(childNode.getParent()).isSameAs(originalParent);
            assertThat(originalParent.getNumChildren()).isEqualTo(1);

            addChildTransform.execute();

            // Child gets copied (sanitized) when it already has a parent
            // Original child stays with original parent, copy goes to new parent
            assertThat(originalParent.getNumChildren()).isEqualTo(1);
            assertThat(originalParent.getChild(0)).isSameAs(childNode);
            assertThat(childNode.getParent()).isSameAs(originalParent);

            assertThat(parentNode.getNumChildren()).isEqualTo(1);
            // New parent gets a copy of the child
            assertThat(parentNode.getChild(0)).isNotSameAs(childNode);
            assertThat(parentNode.getChild(0).toContentString()).isEqualTo(childNode.toContentString());
        }

        @Test
        @DisplayName("Should handle multiple consecutive additions")
        void testMultipleConsecutiveAdditions() {
            TestTreeNode child2 = new TestTreeNode("child2");
            TestTreeNode child3 = new TestTreeNode("child3");

            AddChildTransform<TestTreeNode> transform1 = new AddChildTransform<>(parentNode, childNode);
            AddChildTransform<TestTreeNode> transform2 = new AddChildTransform<>(parentNode, child2);
            AddChildTransform<TestTreeNode> transform3 = new AddChildTransform<>(parentNode, child3);

            transform1.execute();
            transform2.execute();
            transform3.execute();

            assertThat(parentNode.getNumChildren()).isEqualTo(3);
            assertThat(parentNode.getChild(0)).isSameAs(childNode);
            assertThat(parentNode.getChild(1)).isSameAs(child2);
            assertThat(parentNode.getChild(2)).isSameAs(child3);
        }
    }
}
