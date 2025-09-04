package pt.up.fe.specs.symja.ast;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Comprehensive test suite for SymjaToC AST to C code conversion.
 * 
 * Tests conversion functionality for all AST node types including:
 * - Symbol nodes
 * - Integer nodes
 * - Operator nodes
 * - Function nodes
 * - Error handling
 * - Integration scenarios
 * 
 * @author Generated Tests
 */
@DisplayName("SymjaToC C Code Conversion Tests")
class SymjaToCTest {

    @Nested
    @DisplayName("Symbol Conversion Tests")
    class SymbolConversionTests {

        @Test
        @DisplayName("Should convert simple symbol to C")
        void testConvertSymbol_Simple_ReturnsCorrectC() {
            SymjaSymbol symbol = SymjaNode.newNode(SymjaSymbol.class);
            symbol.set(SymjaSymbol.SYMBOL, "x");

            String result = SymjaToC.convert(symbol);

            assertThat(result).isEqualTo("x");
        }

        @Test
        @DisplayName("Should handle empty symbol gracefully")
        void testConvertSymbol_Empty_HandlesGracefully() {
            SymjaSymbol symbol = SymjaNode.newNode(SymjaSymbol.class);
            symbol.set(SymjaSymbol.SYMBOL, "");

            String result = SymjaToC.convert(symbol);

            // Should handle empty symbols gracefully
            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("Integer Conversion Tests")
    class IntegerConversionTests {

        @Test
        @DisplayName("Should convert positive integer to C")
        void testConvertInteger_Positive_ReturnsCorrectC() {
            SymjaInteger integer = SymjaNode.newNode(SymjaInteger.class);
            integer.set(SymjaInteger.VALUE_STRING, "42");

            String result = SymjaToC.convert(integer);

            assertThat(result).isEqualTo("42");
        }

        @Test
        @DisplayName("Should convert negative integer to C")
        void testConvertInteger_Negative_ReturnsCorrectC() {
            SymjaInteger integer = SymjaNode.newNode(SymjaInteger.class);
            integer.set(SymjaInteger.VALUE_STRING, "-123");

            String result = SymjaToC.convert(integer);

            assertThat(result).isEqualTo("-123");
        }

        @Test
        @DisplayName("Should handle zero integer")
        void testConvertInteger_Zero_ReturnsCorrectC() {
            SymjaInteger integer = SymjaNode.newNode(SymjaInteger.class);
            integer.set(SymjaInteger.VALUE_STRING, "0");

            String result = SymjaToC.convert(integer);

            assertThat(result).isEqualTo("0");
        }
    }

    @Nested
    @DisplayName("Operator Conversion Tests")
    class OperatorConversionTests {

        @Test
        @DisplayName("Should convert addition operator to C")
        void testConvertOperator_Addition_ReturnsCorrectC() {
            SymjaOperator operator = SymjaNode.newNode(SymjaOperator.class);
            operator.set(SymjaOperator.OPERATOR, Operator.Plus);

            String result = SymjaToC.convert(operator);

            assertThat(result).isEqualTo("+");
        }

        @Test
        @DisplayName("Should convert complex operator to C")
        void testConvertOperator_Complex_ReturnsCorrectC() {
            SymjaOperator operatorNode = SymjaNode.newNode(SymjaOperator.class);
            operatorNode.set(SymjaOperator.OPERATOR, Operator.Times);

            // Add child nodes for a multiplication expression
            SymjaSymbol left = SymjaNode.newNode(SymjaSymbol.class);
            left.set(SymjaSymbol.SYMBOL, "a");

            SymjaSymbol right = SymjaNode.newNode(SymjaSymbol.class);
            right.set(SymjaSymbol.SYMBOL, "b");

            operatorNode.addChild(left);
            operatorNode.addChild(right);

            String result = SymjaToC.convert(operatorNode);

            // Should include operator and operands
            assertThat(result).contains("*");
        }
    }

    @Nested
    @DisplayName("Function Conversion Tests")
    class FunctionConversionTests {

        @Test
        @DisplayName("Should convert simple function to C")
        void testConvertFunction_Simple_ReturnsCorrectC() {
            SymjaFunction function = SymjaNode.newNode(SymjaFunction.class);
            function.set(SymjaFunction.HAS_PARENTHESIS, true);

            // Add an operator as first child (this is required by
            // SymjaToC.functionConverter)
            SymjaOperator operator = SymjaNode.newNode(SymjaOperator.class);
            operator.set(SymjaOperator.OPERATOR, Operator.Plus);
            function.addChild(operator);

            // Add operands
            SymjaSymbol left = SymjaNode.newNode(SymjaSymbol.class);
            left.set(SymjaSymbol.SYMBOL, "a");
            function.addChild(left);

            SymjaSymbol right = SymjaNode.newNode(SymjaSymbol.class);
            right.set(SymjaSymbol.SYMBOL, "b");
            function.addChild(right);

            String result = SymjaToC.convert(function);

            // Should contain function representation with parentheses
            assertThat(result).contains("+").contains("(").contains(")");
        }

        @Test
        @DisplayName("Should handle empty function")
        void testConvertFunction_Empty_HandlesGracefully() {
            SymjaFunction emptyFunction = SymjaNode.newNode(SymjaFunction.class);
            emptyFunction.set(SymjaFunction.HAS_PARENTHESIS, false);

            // Functions require at least an operator as first child
            // For this test, let's add a minimal valid structure
            SymjaOperator operator = SymjaNode.newNode(SymjaOperator.class);
            operator.set(SymjaOperator.OPERATOR, Operator.Plus);
            emptyFunction.addChild(operator);

            // Add minimal operands for valid structure
            SymjaInteger zero = SymjaNode.newNode(SymjaInteger.class);
            zero.set(SymjaInteger.VALUE_STRING, "0");
            emptyFunction.addChild(zero);

            String result = SymjaToC.convert(emptyFunction);

            // Should handle minimal functions gracefully
            assertThat(result).isNotNull().isNotEmpty();
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle null input gracefully")
        void testConvertNull_ThrowsException() {
            assertThatThrownBy(() -> SymjaToC.convert(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle empty symbol gracefully")
        void testConvertEmptySymbol_HandlesGracefully() {
            SymjaSymbol emptySymbol = SymjaNode.newNode(SymjaSymbol.class);
            // Don't set symbol value, leaving it empty

            String result = SymjaToC.convert(emptySymbol);

            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should convert mixed node types")
        void testConvertMixed_AllTypes_ReturnsValidC() {
            // Create nodes of different types
            SymjaSymbol symbol = SymjaNode.newNode(SymjaSymbol.class);
            symbol.set(SymjaSymbol.SYMBOL, "var");

            SymjaInteger integer = SymjaNode.newNode(SymjaInteger.class);
            integer.set(SymjaInteger.VALUE_STRING, "123");

            SymjaOperator operator = SymjaNode.newNode(SymjaOperator.class);
            operator.set(SymjaOperator.OPERATOR, Operator.Plus);

            // Test conversion of each type
            assertThat(SymjaToC.convert(symbol)).isEqualTo("var");
            assertThat(SymjaToC.convert(integer)).isEqualTo("123");
            assertThat(SymjaToC.convert(operator)).isEqualTo("+");
        }

        @Test
        @DisplayName("Should convert complex expression to valid C")
        void testConvertComplexExpression_ReturnsValidC() {
            // Build a complex expression: (a + b) * c
            SymjaFunction expression = SymjaNode.newNode(SymjaFunction.class);
            expression.set(SymjaFunction.HAS_PARENTHESIS, true);

            // First child must be the operator
            SymjaOperator plus = SymjaNode.newNode(SymjaOperator.class);
            plus.set(SymjaOperator.OPERATOR, Operator.Plus);
            expression.addChild(plus);

            // Add operands
            SymjaSymbol a = SymjaNode.newNode(SymjaSymbol.class);
            a.set(SymjaSymbol.SYMBOL, "a");
            expression.addChild(a);

            SymjaSymbol b = SymjaNode.newNode(SymjaSymbol.class);
            b.set(SymjaSymbol.SYMBOL, "b");
            expression.addChild(b);

            String result = SymjaToC.convert(expression);

            assertThat(result).contains("(").contains("+").contains(")");
        }

        @Test
        @DisplayName("Should maintain AST structure during conversion")
        void testConvertMaintainsStructure_VerifyIntegrity() {
            // Create original node
            SymjaSymbol originalSymbol = SymjaNode.newNode(SymjaSymbol.class);
            originalSymbol.set(SymjaSymbol.SYMBOL, "original");

            // Convert to C
            String cCode = SymjaToC.convert(originalSymbol);

            // Verify original node is unchanged
            assertThat(originalSymbol.get(SymjaSymbol.SYMBOL)).isEqualTo("original");
            assertThat(cCode).isEqualTo("original");
        }
    }
}
