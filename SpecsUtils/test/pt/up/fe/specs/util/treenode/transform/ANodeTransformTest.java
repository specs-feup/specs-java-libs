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
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Comprehensive test suite for ANodeTransform abstract implementation.
 * Tests the base functionality for tree node transformations.
 * 
 * @author Generated Tests
 */
@DisplayName("ANodeTransform Tests")
class ANodeTransformTest {

    private TestANodeTransform transform;
    private TestTreeNode operand1;
    private TestTreeNode operand2;
    private List<TestTreeNode> operands;

    @BeforeEach
    void setUp() {
        operand1 = new TestTreeNode("operand1");
        operand2 = new TestTreeNode("operand2");
        operands = Arrays.asList(operand1, operand2);
        transform = new TestANodeTransform("TEST_TRANSFORM", operands);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Constructor should initialize type and operands correctly")
        void testConstructor_InitializesCorrectly() {
            assertThat(transform.getType()).isEqualTo("TEST_TRANSFORM");
            assertThat(transform.getOperands()).containsExactly(operand1, operand2);
        }

        @Test
        @DisplayName("Constructor should handle empty operands list")
        void testConstructor_WithEmptyOperands() {
            TestANodeTransform emptyTransform = new TestANodeTransform("EMPTY", Collections.emptyList());

            assertThat(emptyTransform.getType()).isEqualTo("EMPTY");
            assertThat(emptyTransform.getOperands()).isEmpty();
        }

        @Test
        @DisplayName("Constructor should handle null type")
        void testConstructor_WithNullType() {
            TestANodeTransform nullTypeTransform = new TestANodeTransform(null, operands);

            assertThat(nullTypeTransform.getType()).isNull();
            assertThat(nullTypeTransform.getOperands()).containsExactly(operand1, operand2);
        }

        @Test
        @DisplayName("Constructor should handle null operands")
        void testConstructor_WithNullOperands() {
            TestANodeTransform nullOperandsTransform = new TestANodeTransform("NULL_OPERANDS", null);

            assertThat(nullOperandsTransform.getType()).isEqualTo("NULL_OPERANDS");
            assertThat(nullOperandsTransform.getOperands()).isNull();
        }
    }

    @Nested
    @DisplayName("Getter Tests")
    class GetterTests {

        @Test
        @DisplayName("getType() should return the correct type")
        void testGetType_ReturnsCorrectType() {
            assertThat(transform.getType()).isEqualTo("TEST_TRANSFORM");
        }

        @Test
        @DisplayName("getOperands() should return the original operands list")
        void testGetOperands_ReturnsOriginalList() {
            List<TestTreeNode> retrievedOperands = transform.getOperands();

            assertThat(retrievedOperands).isSameAs(operands);
            assertThat(retrievedOperands).containsExactly(operand1, operand2);
        }

        @Test
        @DisplayName("getOperands() should return the same list instance")
        void testGetOperands_ReturnsSameListInstance() {
            List<TestTreeNode> retrievedOperands = transform.getOperands();
            TestTreeNode newOperand = new TestTreeNode("new");

            // The behavior depends on whether the original list was mutable
            // Arrays.asList returns an immutable list
            assertThrows(UnsupportedOperationException.class, () -> {
                retrievedOperands.add(newOperand);
            });
        }
    }

    @Nested
    @DisplayName("toString() Tests")
    class ToStringTests {

        @Test
        @DisplayName("toString() should include type and operand hash codes")
        void testToString_IncludesTypeAndHashCodes() {
            String result = transform.toString();

            assertThat(result).startsWith("TEST_TRANSFORM ");
            assertThat(result).contains(Integer.toHexString(operand1.hashCode()));
            assertThat(result).contains(Integer.toHexString(operand2.hashCode()));
        }

        @Test
        @DisplayName("toString() should handle empty operands")
        void testToString_WithEmptyOperands() {
            TestANodeTransform emptyTransform = new TestANodeTransform("EMPTY", Collections.emptyList());

            String result = emptyTransform.toString();

            assertThat(result).isEqualTo("EMPTY ");
        }

        @Test
        @DisplayName("toString() should handle single operand")
        void testToString_WithSingleOperand() {
            TestANodeTransform singleTransform = new TestANodeTransform("SINGLE",
                    List.of(operand1));

            String result = singleTransform.toString();

            assertThat(result).isEqualTo("SINGLE " + Integer.toHexString(operand1.hashCode()));
        }

        @Test
        @DisplayName("toString() should handle null type")
        void testToString_WithNullType() {
            TestANodeTransform nullTypeTransform = new TestANodeTransform(null, operands);

            String result = nullTypeTransform.toString();

            assertThat(result).startsWith("null ");
            assertThat(result).contains(Integer.toHexString(operand1.hashCode()));
        }
    }

    @Nested
    @DisplayName("Abstract Implementation Tests")
    class AbstractImplementationTests {

        @Test
        @DisplayName("Should enforce implementation of execute() method")
        void testAbstractExecuteMethod() {
            // Test that our concrete implementation works
            assertThatCode(() -> transform.execute()).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should allow different transform types")
        void testDifferentTransformTypes() {
            TestANodeTransform deleteTransform = new TestANodeTransform("DELETE",
                    List.of(operand1));
            TestANodeTransform replaceTransform = new TestANodeTransform("REPLACE",
                    Arrays.asList(operand1, operand2));

            assertThat(deleteTransform.getType()).isEqualTo("DELETE");
            assertThat(replaceTransform.getType()).isEqualTo("REPLACE");
            assertThat(deleteTransform.getOperands()).hasSize(1);
            assertThat(replaceTransform.getOperands()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle very long operand lists")
        void testLargeOperandsList() {
            List<TestTreeNode> manyOperands = Arrays.asList(
                    new TestTreeNode("op1"), new TestTreeNode("op2"), new TestTreeNode("op3"),
                    new TestTreeNode("op4"), new TestTreeNode("op5"), new TestTreeNode("op6"));

            TestANodeTransform largeTransform = new TestANodeTransform("LARGE", manyOperands);

            assertThat(largeTransform.getOperands()).hasSize(6);
            assertThat(largeTransform.toString()).contains("LARGE");
            assertThat(largeTransform.toString().split(" ")).hasSize(7); // type + 6 hash codes
        }

        @Test
        @DisplayName("Should handle special characters in type")
        void testSpecialCharactersInType() {
            TestANodeTransform specialTransform = new TestANodeTransform("TRANSFORM_WITH-SPECIAL.CHARS", operands);

            assertThat(specialTransform.getType()).isEqualTo("TRANSFORM_WITH-SPECIAL.CHARS");
            assertThat(specialTransform.toString()).startsWith("TRANSFORM_WITH-SPECIAL.CHARS ");
        }
    }

    /**
     * Concrete test implementation of ANodeTransform for testing purposes
     */
    private static class TestANodeTransform extends ANodeTransform<TestTreeNode> {

        public TestANodeTransform(String type, List<TestTreeNode> operands) {
            super(type, operands);
        }

        @Override
        public void execute() {
            // Simple test implementation - do nothing
        }
    }

    /**
     * Test implementation of TreeNode for testing purposes
     */
    private static class TestTreeNode extends ATreeNode<TestTreeNode> {
        private final String name;
        private final String type;

        public TestTreeNode(String name) {
            this(name, "default", Collections.emptyList());
        }

        public TestTreeNode(String name, String type) {
            this(name, type, Collections.emptyList());
        }

        public TestTreeNode(String name, String type, List<TestTreeNode> children) {
            super(children);
            this.name = name;
            this.type = type;
        }

        @Override
        public String toNodeString() {
            return name + "(" + type + ")";
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
            return new TestTreeNode(name, type);
        }

        @Override
        public String toString() {
            return "TestTreeNode{name='" + name + "', children=" + getNumChildren() + "}";
        }
    }
}
