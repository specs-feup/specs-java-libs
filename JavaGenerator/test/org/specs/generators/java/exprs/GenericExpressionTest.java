package org.specs.generators.java.exprs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.specs.generators.java.IGenerate;

import static org.assertj.core.api.Assertions.*;

/**
 * Test class for {@link GenericExpression}.
 * 
 * Tests the GenericExpression implementation following the comprehensive
 * testing methodology used across the JavaGenerator project.
 * 
 * @author Generated Tests
 */
@DisplayName("GenericExpression Tests")
class GenericExpressionTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create GenericExpression with valid string")
        void shouldCreateGenericExpressionWithValidString() {
            String expression = "x + y";
            GenericExpression genericExpr = new GenericExpression(expression);

            assertThat(genericExpr).isNotNull();
            assertThat(genericExpr.toString()).isEqualTo(expression);
        }

        @Test
        @DisplayName("Should create GenericExpression with empty string")
        void shouldCreateGenericExpressionWithEmptyString() {
            String expression = "";
            GenericExpression genericExpr = new GenericExpression(expression);

            assertThat(genericExpr).isNotNull();
            assertThat(genericExpr.toString()).isEqualTo("");
        }

        @Test
        @DisplayName("Should create GenericExpression with null")
        void shouldCreateGenericExpressionWithNull() {
            GenericExpression genericExpr = new GenericExpression(null);

            assertThat(genericExpr).isNotNull();
            assertThat(genericExpr.toString()).isEqualTo("null");
        }
    }

    @Nested
    @DisplayName("Factory Method Tests")
    class FactoryMethodTests {

        @Test
        @DisplayName("Should create GenericExpression using fromString method")
        void shouldCreateGenericExpressionUsingFromString() {
            String expression = "Math.max(a, b)";
            GenericExpression genericExpr = GenericExpression.fromString(expression);

            assertThat(genericExpr).isNotNull();
            assertThat(genericExpr.toString()).isEqualTo(expression);
        }

        @Test
        @DisplayName("Should create equivalent expressions using constructor and factory method")
        void shouldCreateEquivalentExpressionsUsingConstructorAndFactoryMethod() {
            String expression = "array[index]";
            GenericExpression constructorExpr = new GenericExpression(expression);
            GenericExpression factoryExpr = GenericExpression.fromString(expression);

            assertThat(constructorExpr.toString()).isEqualTo(factoryExpr.toString());
            assertThat(constructorExpr.generateCode(0).toString())
                    .isEqualTo(factoryExpr.generateCode(0).toString());
        }
    }

    @Nested
    @DisplayName("Interface Implementation Tests")
    class InterfaceImplementationTests {

        @Test
        @DisplayName("Should implement IExpression interface")
        void shouldImplementIExpressionInterface() {
            GenericExpression expression = new GenericExpression("test");

            assertThat(expression).isInstanceOf(IExpression.class);
        }

        @Test
        @DisplayName("Should implement IGenerate interface")
        void shouldImplementIGenerateInterface() {
            GenericExpression expression = new GenericExpression("test");

            assertThat(expression).isInstanceOf(IGenerate.class);
        }

        @Test
        @DisplayName("Should provide ln method from IGenerate")
        void shouldProvideLnMethodFromIGenerate() {
            GenericExpression expression = new GenericExpression("test");

            String lineSeparator = expression.ln();

            assertThat(lineSeparator).isNotNull();
            assertThat(lineSeparator).isEqualTo(System.lineSeparator());
        }
    }

    @Nested
    @DisplayName("Code Generation Tests")
    class CodeGenerationTests {

        @Test
        @DisplayName("Should generate code with no indentation")
        void shouldGenerateCodeWithNoIndentation() {
            String expression = "x + y * z";
            GenericExpression genericExpr = new GenericExpression(expression);

            StringBuilder result = genericExpr.generateCode(0);

            assertThat(result.toString()).isEqualTo(expression);
        }

        @ParameterizedTest
        @DisplayName("Should generate code with different indentation levels")
        @ValueSource(ints = { 1, 2, 3, 4, 5 })
        void shouldGenerateCodeWithDifferentIndentationLevels(int indentation) {
            String expression = "method()";
            GenericExpression genericExpr = new GenericExpression(expression);

            StringBuilder result = genericExpr.generateCode(indentation);

            String expectedSpaces = "    ".repeat(indentation); // Utils uses 4 spaces per indentation level
            String expected = expectedSpaces + expression;
            assertThat(result.toString()).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should generate code with large indentation")
        void shouldGenerateCodeWithLargeIndentation() {
            String expression = "deeply.nested.call()";
            GenericExpression genericExpr = new GenericExpression(expression);
            int largeIndentation = 10;

            StringBuilder result = genericExpr.generateCode(largeIndentation);

            String expectedSpaces = "    ".repeat(largeIndentation); // Utils uses 4 spaces per indentation level
            String expected = expectedSpaces + expression;
            assertThat(result.toString()).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("Expression Content Tests")
    class ExpressionContentTests {

        @ParameterizedTest
        @DisplayName("Should handle various Java expressions")
        @CsvSource({
                "'x + y', 'x + y'",
                "'method()', 'method()'",
                "'object.field', 'object.field'",
                "'array[index]', 'array[index]'",
                "'(int) value', '(int) value'",
                "'x > 0 ? x : -x', 'x > 0 ? x : -x'",
                "'new ArrayList<>()', 'new ArrayList<>()'",
                "'lambda -> result', 'lambda -> result'",
                "'Math::abs', 'Math::abs'"
        })
        void shouldHandleVariousJavaExpressions(String input, String expected) {
            GenericExpression expression = new GenericExpression(input);

            assertThat(expression.toString()).isEqualTo(expected);
            assertThat(expression.generateCode(0).toString()).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should handle complex multi-line-like expressions")
        void shouldHandleComplexMultiLineLikeExpressions() {
            String complexExpression = "stream.filter(x -> x > 0).map(String::valueOf).collect(Collectors.toList())";
            GenericExpression expression = new GenericExpression(complexExpression);

            assertThat(expression.toString()).isEqualTo(complexExpression);
            assertThat(expression.generateCode(0).toString()).isEqualTo(complexExpression);
        }

        @Test
        @DisplayName("Should handle expressions with special characters")
        void shouldHandleExpressionsWithSpecialCharacters() {
            String specialExpression = "\"Hello, World!\" + '\\n' + '\t'";
            GenericExpression expression = new GenericExpression(specialExpression);

            assertThat(expression.toString()).isEqualTo(specialExpression);
            assertThat(expression.generateCode(0).toString()).isEqualTo(specialExpression);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should return string representation without indentation")
        void shouldReturnStringRepresentationWithoutIndentation() {
            String expression = "calculateResult(x, y)";
            GenericExpression genericExpr = new GenericExpression(expression);

            String result = genericExpr.toString();

            assertThat(result).isEqualTo(expression);
            // toString should be equivalent to generateCode(0).toString()
            assertThat(result).isEqualTo(genericExpr.generateCode(0).toString());
        }

        @Test
        @DisplayName("Should handle empty string in toString")
        void shouldHandleEmptyStringInToString() {
            GenericExpression expression = new GenericExpression("");

            String result = expression.toString();

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should handle null in toString")
        void shouldHandleNullInToString() {
            GenericExpression expression = new GenericExpression(null);

            String result = expression.toString();

            assertThat(result).isEqualTo("null");
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle whitespace-only expressions")
        void shouldHandleWhitespaceOnlyExpressions() {
            String whitespaceExpr = "   \t   ";
            GenericExpression expression = new GenericExpression(whitespaceExpr);

            assertThat(expression.toString()).isEqualTo(whitespaceExpr);
            assertThat(expression.generateCode(0).toString()).isEqualTo(whitespaceExpr);
        }

        @Test
        @DisplayName("Should handle expressions with only line breaks")
        void shouldHandleExpressionsWithOnlyLineBreaks() {
            String lineBreakExpr = "\n\n\r\n";
            GenericExpression expression = new GenericExpression(lineBreakExpr);

            assertThat(expression.toString()).isEqualTo(lineBreakExpr);
            assertThat(expression.generateCode(0).toString()).isEqualTo(lineBreakExpr);
        }

        @Test
        @DisplayName("Should handle very long expressions")
        void shouldHandleVeryLongExpressions() {
            String longExpression = "very.long.chain.of.method.calls.that.goes.on.and.on.with.many.parameters(a, b, c, d, e, f, g).andMore().andEvenMore()";
            GenericExpression expression = new GenericExpression(longExpression);

            assertThat(expression.toString()).isEqualTo(longExpression);
            assertThat(expression.generateCode(0).toString()).isEqualTo(longExpression);
        }
    }

    @Nested
    @DisplayName("Immutability Tests")
    class ImmutabilityTests {

        @Test
        @DisplayName("Should not be affected by changes to original string")
        void shouldNotBeAffectedByChangesToOriginalString() {
            StringBuilder sb = new StringBuilder("original");
            String originalString = sb.toString();
            GenericExpression expression = new GenericExpression(originalString);

            // Modify the original StringBuilder (though this doesn't affect the string)
            sb.append(" modified");

            // The expression should still have the original value
            assertThat(expression.toString()).isEqualTo("original");
        }

        @Test
        @DisplayName("Should return consistent results across multiple calls")
        void shouldReturnConsistentResultsAcrossMultipleCalls() {
            String expressionString = "consistent.value()";
            GenericExpression expression = new GenericExpression(expressionString);

            String firstCall = expression.toString();
            String secondCall = expression.toString();
            StringBuilder firstGenerate = expression.generateCode(2);
            StringBuilder secondGenerate = expression.generateCode(2);

            assertThat(firstCall).isEqualTo(secondCall);
            assertThat(firstGenerate.toString()).isEqualTo(secondGenerate.toString());
        }
    }
}
