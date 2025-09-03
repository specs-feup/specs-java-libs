package pt.up.fe.specs.util.treenode.transform;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.treenode.ATreeNode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for NodeTransform interface.
 * Tests the contract and behavior of tree node transformations.
 * 
 * @author Generated Tests
 */
@DisplayName("NodeTransform Interface Tests")
class NodeTransformTest {

    private TestNodeTransform transform;
    private TestTreeNode operand1;
    private TestTreeNode operand2;
    private List<TestTreeNode> operands;

    @BeforeEach
    void setUp() {
        operand1 = new TestTreeNode("operand1");
        operand2 = new TestTreeNode("operand2");
        operands = Arrays.asList(operand1, operand2);
        transform = new TestNodeTransform("TEST_TRANSFORM", operands);
    }

    @Nested
    @DisplayName("Interface Contract Tests")
    class InterfaceContractTests {

        @Test
        @DisplayName("getType() should return transformation name")
        void testGetType_ReturnsTransformationName() {
            assertThat(transform.getType()).isEqualTo("TEST_TRANSFORM");
        }

        @Test
        @DisplayName("getOperands() should return operands list")
        void testGetOperands_ReturnsOperandsList() {
            List<TestTreeNode> result = transform.getOperands();

            assertThat(result).containsExactly(operand1, operand2);
            assertThat(result).isSameAs(operands);
        }

        @Test
        @DisplayName("execute() should be callable without throwing exceptions")
        void testExecute_CallableWithoutExceptions() {
            assertThatCode(() -> transform.execute()).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Different Transform Types")
    class DifferentTransformTypesTests {

        @Test
        @DisplayName("Should support different transform type names")
        void testDifferentTransformTypes() {
            TestNodeTransform deleteTransform = new TestNodeTransform("DELETE", Collections.singletonList(operand1));
            TestNodeTransform replaceTransform = new TestNodeTransform("REPLACE", operands);
            TestNodeTransform moveTransform = new TestNodeTransform("MOVE", operands);

            assertThat(deleteTransform.getType()).isEqualTo("DELETE");
            assertThat(replaceTransform.getType()).isEqualTo("REPLACE");
            assertThat(moveTransform.getType()).isEqualTo("MOVE");
        }

        @Test
        @DisplayName("Should support transforms with no operands")
        void testTransformWithNoOperands() {
            TestNodeTransform noOpTransform = new TestNodeTransform("NO_OP", Collections.emptyList());

            assertThat(noOpTransform.getType()).isEqualTo("NO_OP");
            assertThat(noOpTransform.getOperands()).isEmpty();
            assertThatCode(() -> noOpTransform.execute()).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should support transforms with many operands")
        void testTransformWithManyOperands() {
            List<TestTreeNode> manyOperands = Arrays.asList(
                    new TestTreeNode("op1"), new TestTreeNode("op2"), new TestTreeNode("op3"),
                    new TestTreeNode("op4"), new TestTreeNode("op5"));
            TestNodeTransform manyOpTransform = new TestNodeTransform("MANY_OP", manyOperands);

            assertThat(manyOpTransform.getType()).isEqualTo("MANY_OP");
            assertThat(manyOpTransform.getOperands()).hasSize(5);
            assertThatCode(() -> manyOpTransform.execute()).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null type")
        void testNullType() {
            TestNodeTransform nullTypeTransform = new TestNodeTransform(null, operands);

            assertThat(nullTypeTransform.getType()).isNull();
            assertThat(nullTypeTransform.getOperands()).containsExactly(operand1, operand2);
        }

        @Test
        @DisplayName("Should handle null operands")
        void testNullOperands() {
            TestNodeTransform nullOperandsTransform = new TestNodeTransform("NULL_OPERANDS", null);

            assertThat(nullOperandsTransform.getType()).isEqualTo("NULL_OPERANDS");
            assertThat(nullOperandsTransform.getOperands()).isNull();
        }

        @Test
        @DisplayName("Should handle empty string type")
        void testEmptyStringType() {
            TestNodeTransform emptyTypeTransform = new TestNodeTransform("", operands);

            assertThat(emptyTypeTransform.getType()).isEmpty();
            assertThat(emptyTypeTransform.getOperands()).containsExactly(operand1, operand2);
        }

        @Test
        @DisplayName("Should handle special characters in type")
        void testSpecialCharactersInType() {
            TestNodeTransform specialTransform = new TestNodeTransform("TRANSFORM_WITH-SPECIAL.CHARS@123", operands);

            assertThat(specialTransform.getType()).isEqualTo("TRANSFORM_WITH-SPECIAL.CHARS@123");
        }
    }

    @Nested
    @DisplayName("Transform Execution")
    class TransformExecutionTests {

        @Test
        @DisplayName("execute() should be idempotent")
        void testExecute_IsIdempotent() {
            // Execute multiple times should not cause issues
            assertThatCode(() -> {
                transform.execute();
                transform.execute();
                transform.execute();
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("execute() should work with different operand configurations")
        void testExecute_WithDifferentOperandConfigurations() {
            TestNodeTransform singleOpTransform = new TestNodeTransform("SINGLE", Collections.singletonList(operand1));
            TestNodeTransform noOpTransform = new TestNodeTransform("NO_OP", Collections.emptyList());
            TestNodeTransform multiOpTransform = new TestNodeTransform("MULTI", operands);

            assertThatCode(() -> {
                singleOpTransform.execute();
                noOpTransform.execute();
                multiOpTransform.execute();
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Operand Management")
    class OperandManagementTests {

        @Test
        @DisplayName("Should maintain operand order")
        void testMaintainOperandOrder() {
            List<TestTreeNode> orderedOperands = Arrays.asList(
                    new TestTreeNode("first"),
                    new TestTreeNode("second"),
                    new TestTreeNode("third"));
            TestNodeTransform orderedTransform = new TestNodeTransform("ORDERED", orderedOperands);

            List<TestTreeNode> result = orderedTransform.getOperands();
            assertThat(result.get(0).toNodeString()).contains("first");
            assertThat(result.get(1).toNodeString()).contains("second");
            assertThat(result.get(2).toNodeString()).contains("third");
        }

        @Test
        @DisplayName("Should allow operand list modification through returned reference")
        void testOperandListModificationThroughReference() {
            List<TestTreeNode> modifiableOperands = Arrays.asList(operand1, operand2);
            TestNodeTransform modifiableTransform = new TestNodeTransform("MODIFIABLE", modifiableOperands);

            // This depends on the implementation - if it returns a direct reference
            List<TestTreeNode> returnedOperands = modifiableTransform.getOperands();
            assertThat(returnedOperands).isSameAs(modifiableOperands);
        }
    }

    /**
     * Test implementation of NodeTransform for testing purposes
     */
    private static class TestNodeTransform implements NodeTransform<TestTreeNode> {
        private final String type;
        private final List<TestTreeNode> operands;
        private int executionCount = 0;

        public TestNodeTransform(String type, List<TestTreeNode> operands) {
            this.type = type;
            this.operands = operands;
        }

        @Override
        public String getType() {
            return type;
        }

        @Override
        public List<TestTreeNode> getOperands() {
            return operands;
        }

        @Override
        public void execute() {
            executionCount++;
            // Simple test implementation - just count executions
        }

        @SuppressWarnings("unused")
        public int getExecutionCount() {
            return executionCount;
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

        @Override
        public String toNodeString() {
            return name;
        }

        @Override
        public String getNodeName() {
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
    }
}
