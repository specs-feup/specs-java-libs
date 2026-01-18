package pt.up.fe.specs.util.treenode.transform;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.treenode.ATreeNode;
import pt.up.fe.specs.util.treenode.transform.impl.DefaultTransformResult;

/**
 * Comprehensive unit tests for transform framework components.
 * Tests ANodeTransform, TransformResult, TwoOperandTransform, and
 * DefaultTransformResult classes.
 * 
 * @author Generated Tests
 */
@DisplayName("Transform Components Tests")
class TransformComponentsTest {

    private TestTreeNode node1;
    private TestTreeNode node2;

    @BeforeEach
    void setUp() {
        node1 = new TestTreeNode("node1");
        node2 = new TestTreeNode("node2");
    }

    @Nested
    @DisplayName("DefaultTransformResult Tests")
    class DefaultTransformResultTests {

        @Test
        @DisplayName("Should create result with visit children true")
        void shouldCreateResultWithVisitChildrenTrue() {
            DefaultTransformResult result = new DefaultTransformResult(true);

            assertThat(result.visitChildren()).isTrue();
        }

        @Test
        @DisplayName("Should create result with visit children false")
        void shouldCreateResultWithVisitChildrenFalse() {
            DefaultTransformResult result = new DefaultTransformResult(false);

            assertThat(result.visitChildren()).isFalse();
        }

        @Test
        @DisplayName("Should implement TransformResult interface")
        void shouldImplementTransformResultInterface() {
            DefaultTransformResult result = new DefaultTransformResult(true);

            assertThat(result).isInstanceOf(TransformResult.class);
        }

        @Test
        @DisplayName("Should create result using factory method")
        void shouldCreateResultUsingFactoryMethod() {
            TransformResult result = TransformResult.empty();

            assertThat(result.visitChildren()).isTrue(); // Default behavior
            assertThat(result).isInstanceOf(DefaultTransformResult.class);
        }
    }

    @Nested
    @DisplayName("ANodeTransform Tests")
    class ANodeTransformTests {

        private TestANodeTransform transform;

        @BeforeEach
        void setUp() {
            transform = new TestANodeTransform("testTransform", Arrays.asList(node1, node2));
        }

        @Test
        @DisplayName("Should implement NodeTransform interface")
        void shouldImplementNodeTransformInterface() {
            assertThat(transform).isInstanceOf(NodeTransform.class);
        }

        @Test
        @DisplayName("Should store and return transformation type")
        void shouldStoreAndReturnTransformationType() {
            assertThat(transform.getType()).isEqualTo("testTransform");
        }

        @Test
        @DisplayName("Should store and return operands")
        void shouldStoreAndReturnOperands() {
            List<TestTreeNode> operands = transform.getOperands();

            assertThat(operands).containsExactly(node1, node2);
        }

        @Test
        @DisplayName("Should handle empty operands list")
        void shouldHandleEmptyOperandsList() {
            TestANodeTransform emptyTransform = new TestANodeTransform("empty", Collections.emptyList());

            assertThat(emptyTransform.getOperands()).isEmpty();
        }

        @Test
        @DisplayName("Should provide meaningful toString representation")
        void shouldProvideMeaningfulToStringRepresentation() {
            String toString = transform.toString();

            assertThat(toString).contains("testTransform");
            assertThat(toString).contains(Integer.toHexString(node1.hashCode()));
            assertThat(toString).contains(Integer.toHexString(node2.hashCode()));
        }

        @Test
        @DisplayName("Should require execute method implementation")
        void shouldRequireExecuteMethodImplementation() {
            // The execute method should be implemented by subclasses
            assertThatCode(() -> transform.execute()).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle single operand")
        void shouldHandleSingleOperand() {
            TestANodeTransform singleOperandTransform = new TestANodeTransform("single", Arrays.asList(node1));

            assertThat(singleOperandTransform.getOperands()).containsExactly(node1);
            assertThat(singleOperandTransform.getType()).isEqualTo("single");
        }
    }

    @Nested
    @DisplayName("TwoOperandTransform Tests")
    class TwoOperandTransformTests {

        private TestTwoOperandTransform transform;

        @BeforeEach
        void setUp() {
            transform = new TestTwoOperandTransform("twoOpTransform", node1, node2);
        }

        @Test
        @DisplayName("Should extend ANodeTransform")
        void shouldExtendANodeTransform() {
            assertThat(transform).isInstanceOf(ANodeTransform.class);
            assertThat(transform).isInstanceOf(NodeTransform.class);
        }

        @Test
        @DisplayName("Should store exactly two operands")
        void shouldStoreExactlyTwoOperands() {
            assertThat(transform.getOperands()).hasSize(2);
            assertThat(transform.getOperands()).containsExactly(node1, node2);
        }

        @Test
        @DisplayName("Should provide convenient access to first node")
        void shouldProvideConvenientAccessToFirstNode() {
            assertThat(transform.getNode1()).isEqualTo(node1);
        }

        @Test
        @DisplayName("Should provide convenient access to second node")
        void shouldProvideConvenientAccessToSecondNode() {
            assertThat(transform.getNode2()).isEqualTo(node2);
        }

        @Test
        @DisplayName("Should have specialized toString representation")
        void shouldHaveSpecializedToStringRepresentation() {
            String toString = transform.toString();

            assertThat(toString).contains("twoOpTransform");
            assertThat(toString).contains("node1(");
            assertThat(toString).contains("node2(");
            assertThat(toString).contains(Integer.toHexString(node1.hashCode()));
            assertThat(toString).contains(Integer.toHexString(node2.hashCode()));
        }

        @Test
        @DisplayName("Should maintain operand order")
        void shouldMaintainOperandOrder() {
            // Verify order is preserved
            assertThat(transform.getNode1()).isEqualTo(node1);
            assertThat(transform.getNode2()).isEqualTo(node2);

            // Create reverse order transform
            TestTwoOperandTransform reverse = new TestTwoOperandTransform("reverse", node2, node1);
            assertThat(reverse.getNode1()).isEqualTo(node2);
            assertThat(reverse.getNode2()).isEqualTo(node1);
        }
    }

    @Nested
    @DisplayName("TransformResult Interface Tests")
    class TransformResultInterfaceTests {

        @Test
        @DisplayName("Should provide visitChildren method")
        void shouldProvideVisitChildrenMethod() {
            TransformResult trueResult = new DefaultTransformResult(true);
            TransformResult falseResult = new DefaultTransformResult(false);

            assertThat(trueResult.visitChildren()).isTrue();
            assertThat(falseResult.visitChildren()).isFalse();
        }

        @Test
        @DisplayName("Should provide empty factory method")
        void shouldProvideEmptyFactoryMethod() {
            TransformResult result = TransformResult.empty();

            assertThat(result).isNotNull();
            assertThat(result.visitChildren()).isTrue(); // Default behavior
        }

        @Test
        @DisplayName("Should allow different implementations")
        void shouldAllowDifferentImplementations() {
            TransformResult customResult = new TransformResult() {
                @Override
                public boolean visitChildren() {
                    return false;
                }
            };

            assertThat(customResult.visitChildren()).isFalse();
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work correctly with transform queue")
        void shouldWorkCorrectlyWithTransformQueue() {
            TransformQueue<TestTreeNode> queue = new TransformQueue<>("test");

            // Use actual TransformQueue methods instead of non-existent add() method
            queue.replace(node1, node2);
            queue.delete(node2);

            assertThat(queue.getTransforms()).hasSize(2);
        }

        @Test
        @DisplayName("Should support different transform types")
        void shouldSupportDifferentTransformTypes() {
            // Test that both ANodeTransform and TwoOperandTransform work together
            TestANodeTransform singleOp = new TestANodeTransform("single", Arrays.asList(node1));
            TestTwoOperandTransform twoOp = new TestTwoOperandTransform("two", node1, node2);

            assertThat(singleOp.getType()).isEqualTo("single");
            assertThat(twoOp.getType()).isEqualTo("two");

            assertThat(singleOp.getOperands()).hasSize(1);
            assertThat(twoOp.getOperands()).hasSize(2);
        }

        @Test
        @DisplayName("Should handle execution of transforms")
        void shouldHandleExecutionOfTransforms() {
            TestANodeTransform transform1 = new TestANodeTransform("exec1", Arrays.asList(node1));
            TestTwoOperandTransform transform2 = new TestTwoOperandTransform("exec2", node1, node2);

            // Should execute without throwing exceptions
            assertThatCode(() -> {
                transform1.execute();
                transform2.execute();
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should support transform result usage")
        void shouldSupportTransformResultUsage() {
            TransformResult result1 = new DefaultTransformResult(true);
            TransformResult result2 = TransformResult.empty();

            // Both should indicate whether to visit children
            boolean shouldVisit1 = result1.visitChildren();
            boolean shouldVisit2 = result2.visitChildren();

            assertThat(shouldVisit1).isTrue();
            assertThat(shouldVisit2).isTrue();
        }
    }

    // Test implementation classes
    private static class TestANodeTransform extends ANodeTransform<TestTreeNode> {
        private boolean executed = false;

        public TestANodeTransform(String type, List<TestTreeNode> operands) {
            super(type, operands);
        }

        @Override
        public void execute() {
            executed = true;
        }

        @SuppressWarnings("unused")
        public boolean isExecuted() {
            return executed;
        }
    }

    private static class TestTwoOperandTransform extends TwoOperandTransform<TestTreeNode> {
        private boolean executed = false;

        public TestTwoOperandTransform(String type, TestTreeNode node1, TestTreeNode node2) {
            super(type, node1, node2);
        }

        @Override
        public void execute() {
            executed = true;
        }

        @SuppressWarnings("unused")
        public boolean isExecuted() {
            return executed;
        }
    }

    // Simple test tree node implementation
    private static class TestTreeNode extends ATreeNode<TestTreeNode> {
        private String value;

        public TestTreeNode(String value) {
            super(java.util.Collections.emptyList());
            this.value = value;
        }

        @SuppressWarnings("unused")
        public String getValue() {
            return value;
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
        public TestTreeNode copy() {
            List<TestTreeNode> childrenCopy = new java.util.ArrayList<>();
            for (TestTreeNode child : getChildren()) {
                childrenCopy.add(child.copy());
            }
            TestTreeNode copy = new TestTreeNode(value);
            copy.setChildren(childrenCopy);
            return copy;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!(obj instanceof TestTreeNode))
                return false;
            TestTreeNode other = (TestTreeNode) obj;
            return value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public String toString() {
            return "TestTreeNode(" + value + ")";
        }
    }
}
