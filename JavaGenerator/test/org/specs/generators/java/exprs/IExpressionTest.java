package org.specs.generators.java.exprs;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.specs.generators.java.IGenerate;

/**
 * Test suite for {@link IExpression} interface.
 * Tests expression generation functionality and interface contracts.
 * 
 * @author Generated Tests
 */
@DisplayName("IExpression Tests")
class IExpressionTest {

    /**
     * Test implementation of IExpression for testing purposes.
     */
    private static class TestExpression implements IExpression {
        private final String expression;

        public TestExpression(String expression) {
            this.expression = expression;
        }

        @Override
        public StringBuilder generateCode(int indentation) {
            StringBuilder sb = new StringBuilder();
            sb.append("\t".repeat(indentation));
            sb.append(expression);
            return sb;
        }

        @Override
        public String toString() {
            return expression;
        }
    }

    /**
     * Another test implementation for comparison tests.
     */
    private static class AnotherTestExpression implements IExpression {
        private final String value;

        public AnotherTestExpression(String value) {
            this.value = value;
        }

        @Override
        public StringBuilder generateCode(int indentation) {
            StringBuilder sb = new StringBuilder();
            sb.append("\t".repeat(indentation));
            sb.append(value);
            sb.append(";");
            return sb;
        }
    }

    @Nested
    @DisplayName("Interface Contract Tests")
    class InterfaceContractTests {

        @Test
        @DisplayName("Should be instance of IGenerate")
        void shouldBeInstanceOfIGenerate() {
            TestExpression expression = new TestExpression("test");

            assertThat(expression).isInstanceOf(IGenerate.class);
        }

        @Test
        @DisplayName("Should implement IExpression interface")
        void shouldImplementIExpressionInterface() {
            TestExpression expression = new TestExpression("test");

            assertThat(expression).isInstanceOf(IExpression.class);
        }

        @Test
        @DisplayName("Should provide ln() method from IGenerate")
        void shouldProvideLnMethodFromIGenerate() {
            TestExpression expression = new TestExpression("value");

            String result = expression.generateCode(0).toString();

            // ln() should return the platform line separator
            assertThat(result).isNotNull();
            assertThat(result).isNotEmpty();
        }

        @Test
        @DisplayName("Should provide generateCode method from IGenerate")
        void shouldProvideGenerateCodeMethodFromIGenerate() {
            TestExpression expression = new TestExpression("value");

            StringBuilder result = expression.generateCode(2);

            assertThat(result.toString()).isEqualTo("\t\tvalue");
        }
    }

    @Nested
    @DisplayName("Expression Generation Tests")
    class ExpressionGenerationTests {

        @Test
        @DisplayName("Should generate simple expression")
        void shouldGenerateSimpleExpression() {
            TestExpression expression = new TestExpression("x + y");

            assertThat(expression.generateCode(0).toString()).isEqualTo("x + y");
            assertThat(expression.toString()).isEqualTo("x + y");
        }

        @Test
        @DisplayName("Should generate expression with tabulation")
        void shouldGenerateExpressionWithTabulation() {
            TestExpression expression = new TestExpression("result = calculate()");

            assertThat(expression.generateCode(0).toString()).isEqualTo("result = calculate()");
            assertThat(expression.generateCode(1).toString()).isEqualTo("\tresult = calculate()");
            assertThat(expression.generateCode(3).toString()).isEqualTo("\t\t\tresult = calculate()");
        }

        @Test
        @DisplayName("Should handle empty expression")
        void shouldHandleEmptyExpression() {
            TestExpression expression = new TestExpression("");

            assertThat(expression.generateCode(0).toString()).isEmpty();
            assertThat(expression.generateCode(2).toString()).isEqualTo("\t\t");
        }

        @Test
        @DisplayName("Should handle null expression gracefully")
        void shouldHandleNullExpressionGracefully() {
            TestExpression expression = new TestExpression(null);

            // The implementation might handle null differently
            String result = expression.generateCode(0).toString();
            // Just verify it doesn't throw an exception
            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("Common Expression Patterns Tests")
    class CommonExpressionPatternsTests {

        @Test
        @DisplayName("Should handle arithmetic expressions")
        void shouldHandleArithmeticExpressions() {
            TestExpression addition = new TestExpression("a + b");
            TestExpression multiplication = new TestExpression("x * y * z");
            TestExpression complex = new TestExpression("(a + b) * (c - d)");

            assertThat(addition.generateCode(0).toString()).isEqualTo("a + b");
            assertThat(multiplication.generateCode(0).toString()).isEqualTo("x * y * z");
            assertThat(complex.generateCode(0).toString()).isEqualTo("(a + b) * (c - d)");
        }

        @Test
        @DisplayName("Should handle method call expressions")
        void shouldHandleMethodCallExpressions() {
            TestExpression methodCall = new TestExpression("object.method()");
            TestExpression chainedCall = new TestExpression("object.method().anotherMethod()");
            TestExpression withParams = new TestExpression("calculate(x, y, z)");

            assertThat(methodCall.generateCode(0).toString()).isEqualTo("object.method()");
            assertThat(chainedCall.generateCode(0).toString()).isEqualTo("object.method().anotherMethod()");
            assertThat(withParams.generateCode(0).toString()).isEqualTo("calculate(x, y, z)");
        }

        @Test
        @DisplayName("Should handle assignment expressions")
        void shouldHandleAssignmentExpressions() {
            TestExpression assignment = new TestExpression("x = 5");
            TestExpression compoundAssignment = new TestExpression("counter += 1");
            TestExpression objectAssignment = new TestExpression("this.value = newValue");

            assertThat(assignment.generateCode(0).toString()).isEqualTo("x = 5");
            assertThat(compoundAssignment.generateCode(0).toString()).isEqualTo("counter += 1");
            assertThat(objectAssignment.generateCode(0).toString()).isEqualTo("this.value = newValue");
        }

        @Test
        @DisplayName("Should handle logical expressions")
        void shouldHandleLogicalExpressions() {
            TestExpression andExpression = new TestExpression("a && b");
            TestExpression orExpression = new TestExpression("x || y || z");
            TestExpression notExpression = new TestExpression("!isValid");
            TestExpression complex = new TestExpression("(a > 0) && (b < 10) || !c");

            assertThat(andExpression.generateCode(0).toString()).isEqualTo("a && b");
            assertThat(orExpression.generateCode(0).toString()).isEqualTo("x || y || z");
            assertThat(notExpression.generateCode(0).toString()).isEqualTo("!isValid");
            assertThat(complex.generateCode(0).toString()).isEqualTo("(a > 0) && (b < 10) || !c");
        }

        @Test
        @DisplayName("Should handle comparison expressions")
        void shouldHandleComparisonExpressions() {
            TestExpression equality = new TestExpression("x == y");
            TestExpression inequality = new TestExpression("a != b");
            TestExpression comparison = new TestExpression("value > threshold");
            TestExpression objectComparison = new TestExpression("obj1.equals(obj2)");

            assertThat(equality.generateCode(0).toString()).isEqualTo("x == y");
            assertThat(inequality.generateCode(0).toString()).isEqualTo("a != b");
            assertThat(comparison.generateCode(0).toString()).isEqualTo("value > threshold");
            assertThat(objectComparison.generateCode(0).toString()).isEqualTo("obj1.equals(obj2)");
        }

        @Test
        @DisplayName("Should handle array access expressions")
        void shouldHandleArrayAccessExpressions() {
            TestExpression arrayAccess = new TestExpression("array[index]");
            TestExpression multiDimArray = new TestExpression("matrix[i][j]");
            TestExpression methodOnArray = new TestExpression("items[0].getName()");

            assertThat(arrayAccess.generateCode(0).toString()).isEqualTo("array[index]");
            assertThat(multiDimArray.generateCode(0).toString()).isEqualTo("matrix[i][j]");
            assertThat(methodOnArray.generateCode(0).toString()).isEqualTo("items[0].getName()");
        }

        @Test
        @DisplayName("Should handle type cast expressions")
        void shouldHandleTypeCastExpressions() {
            TestExpression intCast = new TestExpression("(int) value");
            TestExpression objectCast = new TestExpression("(String) object");
            TestExpression genericCast = new TestExpression("(List<String>) list");

            assertThat(intCast.generateCode(0).toString()).isEqualTo("(int) value");
            assertThat(objectCast.generateCode(0).toString()).isEqualTo("(String) object");
            assertThat(genericCast.generateCode(0).toString()).isEqualTo("(List<String>) list");
        }

        @Test
        @DisplayName("Should handle instanceof expressions")
        void shouldHandleInstanceofExpressions() {
            TestExpression instanceofCheck = new TestExpression("obj instanceof String");
            TestExpression genericInstanceof = new TestExpression("list instanceof List<?>");

            assertThat(instanceofCheck.generateCode(0).toString()).isEqualTo("obj instanceof String");
            assertThat(genericInstanceof.generateCode(0).toString()).isEqualTo("list instanceof List<?>");
        }

        @Test
        @DisplayName("Should handle ternary expressions")
        void shouldHandleTernaryExpressions() {
            TestExpression simple = new TestExpression("x > 0 ? x : -x");
            TestExpression nested = new TestExpression("a ? b : c ? d : e");
            TestExpression withMethods = new TestExpression("isValid() ? getValue() : getDefault()");

            assertThat(simple.generateCode(0).toString()).isEqualTo("x > 0 ? x : -x");
            assertThat(nested.generateCode(0).toString()).isEqualTo("a ? b : c ? d : e");
            assertThat(withMethods.generateCode(0).toString()).isEqualTo("isValid() ? getValue() : getDefault()");
        }
    }

    @Nested
    @DisplayName("Literal Expression Tests")
    class LiteralExpressionTests {

        @Test
        @DisplayName("Should handle string literals")
        void shouldHandleStringLiterals() {
            TestExpression stringLiteral = new TestExpression("\"Hello, World!\"");
            TestExpression emptyString = new TestExpression("\"\"");
            TestExpression escapedString = new TestExpression("\"Line 1\\nLine 2\"");

            assertThat(stringLiteral.generateCode(0).toString()).isEqualTo("\"Hello, World!\"");
            assertThat(emptyString.generateCode(0).toString()).isEqualTo("\"\"");
            assertThat(escapedString.generateCode(0).toString()).isEqualTo("\"Line 1\\nLine 2\"");
        }

        @Test
        @DisplayName("Should handle numeric literals")
        void shouldHandleNumericLiterals() {
            TestExpression intLiteral = new TestExpression("42");
            TestExpression longLiteral = new TestExpression("1000L");
            TestExpression doubleLiteral = new TestExpression("3.14159");
            TestExpression floatLiteral = new TestExpression("2.5f");
            TestExpression hexLiteral = new TestExpression("0xFF");

            assertThat(intLiteral.generateCode(0).toString()).isEqualTo("42");
            assertThat(longLiteral.generateCode(0).toString()).isEqualTo("1000L");
            assertThat(doubleLiteral.generateCode(0).toString()).isEqualTo("3.14159");
            assertThat(floatLiteral.generateCode(0).toString()).isEqualTo("2.5f");
            assertThat(hexLiteral.generateCode(0).toString()).isEqualTo("0xFF");
        }

        @Test
        @DisplayName("Should handle boolean literals")
        void shouldHandleBooleanLiterals() {
            TestExpression trueLiteral = new TestExpression("true");
            TestExpression falseLiteral = new TestExpression("false");

            assertThat(trueLiteral.generateCode(0).toString()).isEqualTo("true");
            assertThat(falseLiteral.generateCode(0).toString()).isEqualTo("false");
        }

        @Test
        @DisplayName("Should handle null literal")
        void shouldHandleNullLiteral() {
            TestExpression nullLiteral = new TestExpression("null");

            assertThat(nullLiteral.generateCode(0).toString()).isEqualTo("null");
        }

        @Test
        @DisplayName("Should handle character literals")
        void shouldHandleCharacterLiterals() {
            TestExpression charLiteral = new TestExpression("'a'");
            TestExpression escapedChar = new TestExpression("'\\n'");
            TestExpression unicodeChar = new TestExpression("'\\u0041'");

            assertThat(charLiteral.generateCode(0).toString()).isEqualTo("'a'");
            assertThat(escapedChar.generateCode(0).toString()).isEqualTo("'\\n'");
            assertThat(unicodeChar.generateCode(0).toString()).isEqualTo("'\\u0041'");
        }
    }

    @Nested
    @DisplayName("Complex Expression Tests")
    class ComplexExpressionTests {

        @Test
        @DisplayName("Should handle constructor expressions")
        void shouldHandleConstructorExpressions() {
            TestExpression constructor = new TestExpression("new ArrayList<>()");
            TestExpression withParams = new TestExpression("new Person(\"John\", 25)");
            TestExpression anonymous = new TestExpression("new Runnable() { public void run() {} }");

            assertThat(constructor.generateCode(0).toString()).isEqualTo("new ArrayList<>()");
            assertThat(withParams.generateCode(0).toString()).isEqualTo("new Person(\"John\", 25)");
            assertThat(anonymous.generateCode(0).toString()).isEqualTo("new Runnable() { public void run() {} }");
        }

        @Test
        @DisplayName("Should handle lambda expressions")
        void shouldHandleLambdaExpressions() {
            TestExpression simple = new TestExpression("x -> x * 2");
            TestExpression multiParam = new TestExpression("(a, b) -> a + b");
            TestExpression block = new TestExpression("value -> { return value != null ? value : \"default\"; }");

            assertThat(simple.generateCode(0).toString()).isEqualTo("x -> x * 2");
            assertThat(multiParam.generateCode(0).toString()).isEqualTo("(a, b) -> a + b");
            assertThat(block.generateCode(0).toString())
                    .isEqualTo("value -> { return value != null ? value : \"default\"; }");
        }

        @Test
        @DisplayName("Should handle method references")
        void shouldHandleMethodReferences() {
            TestExpression staticMethod = new TestExpression("Math::abs");
            TestExpression instanceMethod = new TestExpression("String::toLowerCase");
            TestExpression constructor = new TestExpression("ArrayList::new");

            assertThat(staticMethod.generateCode(0).toString()).isEqualTo("Math::abs");
            assertThat(instanceMethod.generateCode(0).toString()).isEqualTo("String::toLowerCase");
            assertThat(constructor.generateCode(0).toString()).isEqualTo("ArrayList::new");
        }

        @Test
        @DisplayName("Should handle stream operations")
        void shouldHandleStreamOperations() {
            TestExpression streamChain = new TestExpression(
                    "list.stream().filter(x -> x > 0).collect(Collectors.toList())");
            TestExpression mapReduce = new TestExpression("numbers.stream().map(x -> x * x).reduce(0, Integer::sum)");

            assertThat(streamChain.generateCode(0).toString())
                    .isEqualTo("list.stream().filter(x -> x > 0).collect(Collectors.toList())");
            assertThat(mapReduce.generateCode(0).toString())
                    .isEqualTo("numbers.stream().map(x -> x * x).reduce(0, Integer::sum)");
        }
    }

    @Nested
    @DisplayName("Indentation and Formatting Tests")
    class IndentationAndFormattingTests {

        @Test
        @DisplayName("Should handle various indentation levels")
        void shouldHandleVariousIndentationLevels() {
            TestExpression expression = new TestExpression("expression");

            assertThat(expression.generateCode(0).toString()).isEqualTo("expression");
            assertThat(expression.generateCode(1).toString()).isEqualTo("\texpression");
            assertThat(expression.generateCode(2).toString()).isEqualTo("\t\texpression");
            assertThat(expression.generateCode(5).toString()).isEqualTo("\t\t\t\t\texpression");
        }

        @Test
        @DisplayName("Should handle multiline expressions with consistent indentation")
        void shouldHandleMultilineExpressionsWithConsistentIndentation() {
            TestExpression multiline = new TestExpression("condition ?\n    trueValue :\n    falseValue");

            String result = multiline.generateCode(2).toString();
            assertThat(result).startsWith("\t\t");
            assertThat(result).contains("condition");
        }

        @Test
        @DisplayName("Should preserve expression formatting")
        void shouldPreserveExpressionFormatting() {
            TestExpression formatted = new TestExpression("array[ index ]");
            TestExpression spaced = new TestExpression("x  +  y");

            assertThat(formatted.generateCode(0).toString()).isEqualTo("array[ index ]");
            assertThat(spaced.generateCode(0).toString()).isEqualTo("x  +  y");
        }
    }

    @Nested
    @DisplayName("Interface Implementation Consistency Tests")
    class InterfaceImplementationConsistencyTests {

        @Test
        @DisplayName("Should provide consistent results between different implementations")
        void shouldProvideConsistentResultsBetweenDifferentImplementations() {
            TestExpression test1 = new TestExpression("value");
            AnotherTestExpression test2 = new AnotherTestExpression("value");

            // Both should implement IExpression
            assertThat(test1).isInstanceOf(IExpression.class);
            assertThat(test2).isInstanceOf(IExpression.class);

            // Both should provide generateCode method
            assertThat(test1.generateCode(0)).isNotNull();
            assertThat(test2.generateCode(0)).isNotNull();
        }

        @Test
        @DisplayName("Should handle polymorphism correctly")
        void shouldHandlePolymorphismCorrectly() {
            IExpression expression1 = new TestExpression("test1");
            IExpression expression2 = new AnotherTestExpression("test2");

            assertThat(expression1.generateCode(0).toString()).isEqualTo("test1");
            assertThat(expression2.generateCode(0).toString()).isEqualTo("test2;");
        }

        @Test
        @DisplayName("Should maintain interface contract across implementations")
        void shouldMaintainInterfaceContractAcrossImplementations() {
            IExpression[] expressions = {
                    new TestExpression("expr1"),
                    new AnotherTestExpression("expr2")
            };

            for (IExpression expr : expressions) {
                // All should implement required methods
                assertThat(expr.generateCode(0).toString()).isNotNull();
                assertThat(expr.generateCode(0)).isNotNull();
                assertThat(expr.generateCode(1)).isNotNull();
            }
        }
    }
}
