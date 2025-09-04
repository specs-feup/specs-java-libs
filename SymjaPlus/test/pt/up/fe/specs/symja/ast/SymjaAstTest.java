package pt.up.fe.specs.symja.ast;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.matheclipse.parser.client.ast.FunctionNode;
import org.matheclipse.parser.client.ast.IntegerNode;
import org.matheclipse.parser.client.ast.SymbolNode;

/**
 * Unit tests for {@link SymjaAst}.
 * 
 * @author Generated Tests
 */
@DisplayName("SymjaAst")
class SymjaAstTest {

    @Nested
    @DisplayName("Expression Parsing Tests")
    class ExpressionParsingTests {

        @Test
        @DisplayName("Should parse simple integer expressions")
        void testParse_SimpleInteger_ReturnsSymjaIntegerNode() {
            SymjaNode result = SymjaAst.parse("42");

            assertThat(result).isInstanceOf(SymjaInteger.class);
            SymjaInteger integerNode = (SymjaInteger) result;
            assertThat(integerNode.get(SymjaInteger.VALUE_STRING)).isEqualTo("42");
        }

        @Test
        @DisplayName("Should parse simple symbol expressions")
        void testParse_SimpleSymbol_ReturnsSymjaSymbolNode() {
            SymjaNode result = SymjaAst.parse("x");

            assertThat(result).isInstanceOf(SymjaSymbol.class);
            SymjaSymbol symbolNode = (SymjaSymbol) result;
            assertThat(symbolNode.get(SymjaSymbol.SYMBOL)).isEqualTo("x");
        }

        @Test
        @DisplayName("Should parse simple function expressions")
        void testParse_SimpleFunction_ReturnsSymjaFunctionNode() {
            SymjaNode result = SymjaAst.parse("Plus[a, b]");

            assertThat(result).isInstanceOf(SymjaFunction.class);
            SymjaFunction functionNode = (SymjaFunction) result;
            assertThat(functionNode.getNumChildren()).isEqualTo(3); // operator + 2 operands

            // First child should be the operator
            assertThat(functionNode.getChild(0)).isInstanceOf(SymjaOperator.class);
            SymjaOperator operatorNode = (SymjaOperator) functionNode.getChild(0);
            assertThat(operatorNode.get(SymjaOperator.OPERATOR)).isEqualTo(Operator.Plus);

            // Remaining children should be the operands
            assertThat(functionNode.getChild(1)).isInstanceOf(SymjaSymbol.class);
            assertThat(functionNode.getChild(2)).isInstanceOf(SymjaSymbol.class);
        }

        @Test
        @DisplayName("Should parse nested function expressions")
        void testParse_NestedFunction_ReturnsCorrectStructure() {
            SymjaNode result = SymjaAst.parse("Times[Plus[a, b], c]");

            assertThat(result).isInstanceOf(SymjaFunction.class);
            SymjaFunction outerFunction = (SymjaFunction) result;

            // Should have Times operator + 2 operands
            assertThat(outerFunction.getNumChildren()).isEqualTo(3);
            assertThat(outerFunction.getChild(0)).isInstanceOf(SymjaOperator.class);

            // First operand should be nested Plus function
            assertThat(outerFunction.getChild(1)).isInstanceOf(SymjaFunction.class);
            SymjaFunction innerFunction = (SymjaFunction) outerFunction.getChild(1);
            assertThat(innerFunction.getNumChildren()).isEqualTo(3); // Plus[a, b]

            // Second operand should be symbol
            assertThat(outerFunction.getChild(2)).isInstanceOf(SymjaSymbol.class);
        }

        @ParameterizedTest
        @DisplayName("Should parse various mathematical expressions")
        @ValueSource(strings = {
                "123",
                "variable",
                "Plus[x, y]",
                "Times[a, b]",
                "Power[x, 2]"
        })
        void testParse_VariousExpressions_ParsesSuccessfully(String expression) {
            assertThatCode(() -> {
                SymjaNode result = SymjaAst.parse(expression);
                assertThat(result).isNotNull();
            }).doesNotThrowAnyException();
        }

        @ParameterizedTest
        @DisplayName("Should handle invalid expressions gracefully")
        @NullAndEmptySource
        @ValueSource(strings = { " ", "   " })
        void testParse_InvalidExpressions_HandlesGracefully(String expression) {
            if (expression == null) {
                assertThatThrownBy(() -> SymjaAst.parse(expression))
                        .isInstanceOf(NullPointerException.class);
            } else {
                // Empty or whitespace expressions should not throw exceptions
                assertThatCode(() -> SymjaAst.parse(expression))
                        .doesNotThrowAnyException();
            }
        }

        @Test
        @DisplayName("Should throw exception for malformed expressions")
        void testParse_MalformedExpression_ThrowsException() {
            // Test that malformed expressions properly throw exceptions
            assertThatThrownBy(() -> SymjaAst.parse("invalid[syntax"))
                    .isInstanceOf(org.matheclipse.parser.client.SyntaxError.class)
                    .hasMessageContaining("Syntax error");
        }
    }

    @Nested
    @DisplayName("Node Conversion Tests")
    class NodeConversionTests {

        @Test
        @DisplayName("Should convert IntegerNode to SymjaInteger")
        void testToNode_IntegerNode_ReturnsSymjaInteger() {
            IntegerNode integerNode = new IntegerNode("123");

            SymjaNode result = SymjaAst.toNode(integerNode);

            assertThat(result).isInstanceOf(SymjaInteger.class);
            SymjaInteger symjaInteger = (SymjaInteger) result;
            assertThat(symjaInteger.get(SymjaInteger.VALUE_STRING)).isEqualTo("123");
        }

        @Test
        @DisplayName("Should convert SymbolNode to SymjaSymbol")
        void testToNode_SymbolNode_ReturnsSymjaSymbol() {
            SymbolNode symbolNode = new SymbolNode("myVar");

            SymjaNode result = SymjaAst.toNode(symbolNode);

            assertThat(result).isInstanceOf(SymjaSymbol.class);
            SymjaSymbol symjaSymbol = (SymjaSymbol) result;
            assertThat(symjaSymbol.get(SymjaSymbol.SYMBOL)).isEqualTo("myVar");
        }

        @Test
        @DisplayName("Should convert FunctionNode to SymjaFunction")
        void testToNode_FunctionNode_ReturnsSymjaFunction() {
            FunctionNode functionNode = new FunctionNode(new SymbolNode("Plus"));
            functionNode.add(new SymbolNode("a"));
            functionNode.add(new SymbolNode("b"));

            SymjaNode result = SymjaAst.toNode(functionNode);

            assertThat(result).isInstanceOf(SymjaFunction.class);
            SymjaFunction symjaFunction = (SymjaFunction) result;
            assertThat(symjaFunction.getNumChildren()).isEqualTo(3); // operator + 2 operands

            // First child should be operator
            assertThat(symjaFunction.getChild(0)).isInstanceOf(SymjaOperator.class);
            // Remaining children should be symbols
            assertThat(symjaFunction.getChild(1)).isInstanceOf(SymjaSymbol.class);
            assertThat(symjaFunction.getChild(2)).isInstanceOf(SymjaSymbol.class);
        }
    }

    @Nested
    @DisplayName("Complex Expression Tests")
    class ComplexExpressionTests {

        @Test
        @DisplayName("Should parse arithmetic expressions correctly")
        void testParse_ArithmeticExpressions_ReturnsCorrectStructure() {
            // Parse a complex arithmetic expression
            SymjaNode result = SymjaAst.parse("Plus[Times[a, b], c]");

            assertThat(result).isInstanceOf(SymjaFunction.class);
            SymjaFunction outerPlus = (SymjaFunction) result;

            // Should be Plus with Times as first operand
            assertThat(outerPlus.getChild(0)).isInstanceOf(SymjaOperator.class);
            SymjaOperator plusOp = (SymjaOperator) outerPlus.getChild(0);
            assertThat(plusOp.get(SymjaOperator.OPERATOR)).isEqualTo(Operator.Plus);

            // First operand should be Times function
            assertThat(outerPlus.getChild(1)).isInstanceOf(SymjaFunction.class);
            SymjaFunction timesFunction = (SymjaFunction) outerPlus.getChild(1);

            SymjaOperator timesOp = (SymjaOperator) timesFunction.getChild(0);
            assertThat(timesOp.get(SymjaOperator.OPERATOR)).isEqualTo(Operator.Times);
        }

        @Test
        @DisplayName("Should handle deeply nested expressions")
        void testParse_DeeplyNestedExpressions_HandlesCorrectly() {
            String complexExpression = "Plus[Times[Power[x, 2], y], z]";

            SymjaNode result = SymjaAst.parse(complexExpression);

            assertThat(result).isInstanceOf(SymjaFunction.class);
            // Verify the structure is parsed correctly
            assertThat(result.getNumChildren()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should preserve operator precedence information")
        void testParse_OperatorPrecedence_PreservesInformation() {
            SymjaNode result = SymjaAst.parse("Plus[a, b]");

            assertThat(result).isInstanceOf(SymjaFunction.class);
            SymjaFunction function = (SymjaFunction) result;
            SymjaOperator operator = (SymjaOperator) function.getChild(0);

            Operator op = operator.get(SymjaOperator.OPERATOR);
            assertThat(op.getPriority()).isEqualTo(2); // Plus has priority 2
            assertThat(op.getSymbol()).isEqualTo("+");
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should throw exception for unsupported operator types")
        void testToNode_UnsupportedOperator_ThrowsException() {
            // This test checks that unknown operators throw IllegalArgumentException
            assertThatThrownBy(() -> {
                // Create a FunctionNode with unknown function name
                FunctionNode unknownFunction = new FunctionNode(new SymbolNode("UnknownFunction"));
                SymjaAst.toNode(unknownFunction);
            }).isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("No enum constant pt.up.fe.specs.symja.ast.Operator.UnknownFunction");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should parse and convert complex mathematical expressions")
        void testParseAndConvert_ComplexExpressions_WorksCorrectly() {
            // Test the complete workflow: parse string -> AST -> SymjaNode
            String[] expressions = {
                    "42",
                    "x",
                    "Plus[a, b]",
                    "Times[Plus[x, y], z]",
                    "Power[a, 2]"
            };

            for (String expr : expressions) {
                assertThatCode(() -> {
                    SymjaNode result = SymjaAst.parse(expr);
                    assertThat(result).isNotNull();

                    // Verify the result has expected properties
                    if (result instanceof SymjaFunction) {
                        assertThat(result.getNumChildren()).isGreaterThan(0);
                    }
                }).as("Expression: %s", expr).doesNotThrowAnyException();
            }
        }

        @Test
        @DisplayName("Should maintain AST integrity through conversion")
        void testASTIntegrity_ConversionProcess_MaintainsStructure() {
            String expression = "Plus[Times[a, b], Power[c, 2]]";

            SymjaNode result = SymjaAst.parse(expression);

            // Verify the overall structure is maintained
            assertThat(result).isInstanceOf(SymjaFunction.class);
            SymjaFunction rootFunction = (SymjaFunction) result;

            // Should have Plus operator + 2 operands
            assertThat(rootFunction.getNumChildren()).isEqualTo(3);

            // First operand should be Times function
            assertThat(rootFunction.getChild(1)).isInstanceOf(SymjaFunction.class);

            // Second operand should be Power function
            assertThat(rootFunction.getChild(2)).isInstanceOf(SymjaFunction.class);
        }
    }
}
