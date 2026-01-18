package org.specs.generators.java.statements;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.specs.generators.java.IGenerate;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for the {@link IStatement} interface.
 * Tests interface contracts and default behavior.
 * 
 * @author Generated Tests
 */
@DisplayName("IStatement Interface Tests")
class IStatementTest {

    /**
     * Concrete implementation of IStatement for testing purposes.
     */
    private static class TestStatement implements IStatement {
        private final String statementContent;

        public TestStatement(String statementContent) {
            this.statementContent = statementContent;
        }

        @Override
        public StringBuilder generateCode(int indentation) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < indentation; i++) {
                sb.append("    ");
            }
            sb.append(statementContent);
            return sb;
        }
    }

    @Nested
    @DisplayName("Interface Inheritance Tests")
    class InterfaceInheritanceTests {

        @Test
        @DisplayName("Should inherit from IGenerate")
        void shouldInheritFromIGenerate() {
            // Verify IStatement extends IGenerate
            assertThat(IGenerate.class.isAssignableFrom(IStatement.class)).isTrue();
        }

        @Test
        @DisplayName("Should be an interface")
        void shouldBeAnInterface() {
            assertThat(IStatement.class.isInterface()).isTrue();
        }
    }

    @Nested
    @DisplayName("Implementation Contract Tests")
    class ImplementationContractTests {

        @Test
        @DisplayName("Should support implementation of generateCode method")
        void shouldSupportImplementationOfGenerateCodeMethod() {
            String content = "System.out.println(\"Hello World\");";
            TestStatement statement = new TestStatement(content);

            StringBuilder result = statement.generateCode(0);

            assertThat(result).isNotNull();
            assertThat(result.toString()).isEqualTo(content);
        }

        @Test
        @DisplayName("Should handle indentation in generateCode implementation")
        void shouldHandleIndentationInGenerateCodeImplementation() {
            String content = "return value;";
            TestStatement statement = new TestStatement(content);

            StringBuilder result = statement.generateCode(1);

            String expected = "    " + content; // Utils uses 4 spaces per indentation level
            assertThat(result.toString()).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("Code Generation Tests")
    class CodeGenerationTests {

        @ParameterizedTest
        @DisplayName("Should generate code with different indentation levels")
        @ValueSource(ints = { 0, 1, 2, 3, 4, 5 })
        void shouldGenerateCodeWithDifferentIndentationLevels(int indentation) {
            String content = "int x = 10;";
            TestStatement statement = new TestStatement(content);

            StringBuilder result = statement.generateCode(indentation);

            String expectedSpaces = "    ".repeat(indentation); // Utils uses 4 spaces per indentation level
            String expected = expectedSpaces + content;
            assertThat(result.toString()).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should generate different statement types")
        void shouldGenerateDifferentStatementTypes() {
            // Test various types of statements
            TestStatement assignment = new TestStatement("int x = 5;");
            TestStatement methodCall = new TestStatement("methodCall();");
            TestStatement returnStmt = new TestStatement("return result;");
            TestStatement ifStmt = new TestStatement("if (condition) {");

            assertThat(assignment.generateCode(0).toString()).isEqualTo("int x = 5;");
            assertThat(methodCall.generateCode(0).toString()).isEqualTo("methodCall();");
            assertThat(returnStmt.generateCode(0).toString()).isEqualTo("return result;");
            assertThat(ifStmt.generateCode(0).toString()).isEqualTo("if (condition) {");
        }

        @Test
        @DisplayName("Should handle empty statement content")
        void shouldHandleEmptyStatementContent() {
            TestStatement statement = new TestStatement("");

            StringBuilder result = statement.generateCode(0);

            assertThat(result.toString()).isEmpty();
        }

        @Test
        @DisplayName("Should handle null statement content gracefully")
        void shouldHandleNullStatementContentGracefully() {
            TestStatement statement = new TestStatement(null);

            StringBuilder result = statement.generateCode(0);

            assertThat(result.toString()).isEqualTo("null");
        }
    }

    @Nested
    @DisplayName("Complex Statement Tests")
    class ComplexStatementTests {

        @Test
        @DisplayName("Should handle multi-line statement content")
        void shouldHandleMultiLineStatementContent() {
            String multiLineContent = "if (condition) {\n    doSomething();\n}";
            TestStatement statement = new TestStatement(multiLineContent);

            StringBuilder result = statement.generateCode(1);

            String expected = "    " + multiLineContent;
            assertThat(result.toString()).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should handle statements with special characters")
        void shouldHandleStatementsWithSpecialCharacters() {
            String specialContent = "String msg = \"Hello\\nWorld\";";
            TestStatement statement = new TestStatement(specialContent);

            StringBuilder result = statement.generateCode(0);

            assertThat(result.toString()).isEqualTo(specialContent);
        }

        @Test
        @DisplayName("Should handle very long statement content")
        void shouldHandleVeryLongStatementContent() {
            StringBuilder longContent = new StringBuilder();
            longContent.append("someVeryLongMethodNameThatExceedsNormalLengthLimitsLimits(");
            for (int i = 0; i < 10; i++) {
                if (i > 0)
                    longContent.append(", ");
                longContent.append("parameter").append(i);
            }
            longContent.append(");");

            TestStatement statement = new TestStatement(longContent.toString());

            StringBuilder result = statement.generateCode(0);

            assertThat(result.toString()).isEqualTo(longContent.toString());
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle negative indentation gracefully")
        void shouldHandleNegativeIndentationGracefully() {
            String content = "statement();";
            TestStatement statement = new TestStatement(content);

            StringBuilder result = statement.generateCode(-1);

            // Implementation should handle negative indentation (no spaces added)
            assertThat(result.toString()).isEqualTo(content);
        }

        @Test
        @DisplayName("Should handle zero indentation")
        void shouldHandleZeroIndentation() {
            String content = "noIndentation();";
            TestStatement statement = new TestStatement(content);

            StringBuilder result = statement.generateCode(0);

            assertThat(result.toString()).isEqualTo(content);
        }

        @Test
        @DisplayName("Should handle large indentation levels")
        void shouldHandleLargeIndentationLevels() {
            String content = "deeplyNested();";
            TestStatement statement = new TestStatement(content);

            StringBuilder result = statement.generateCode(20);

            String expectedSpaces = "    ".repeat(20); // Utils uses 4 spaces per indentation level
            String expected = expectedSpaces + content;
            assertThat(result.toString()).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("StringBuilder Behavior Tests")
    class StringBuilderBehaviorTests {

        @Test
        @DisplayName("Should return new StringBuilder instance")
        void shouldReturnNewStringBuilderInstance() {
            String content = "test();";
            TestStatement statement = new TestStatement(content);

            StringBuilder result1 = statement.generateCode(0);
            StringBuilder result2 = statement.generateCode(0);

            assertThat(result1).isNotSameAs(result2);
            assertThat(result1.toString()).isEqualTo(result2.toString());
        }

        @Test
        @DisplayName("Should return modifiable StringBuilder")
        void shouldReturnModifiableStringBuilder() {
            String content = "modifiable();";
            TestStatement statement = new TestStatement(content);

            StringBuilder result = statement.generateCode(0);
            result.append(" // comment");

            assertThat(result.toString()).isEqualTo(content + " // comment");
        }

        @Test
        @DisplayName("Should generate consistent results")
        void shouldGenerateConsistentResults() {
            String content = "consistent();";
            TestStatement statement = new TestStatement(content);

            StringBuilder result1 = statement.generateCode(2);
            StringBuilder result2 = statement.generateCode(2);
            StringBuilder result3 = statement.generateCode(2);

            assertThat(result1.toString()).isEqualTo(result2.toString());
            assertThat(result2.toString()).isEqualTo(result3.toString());
        }
    }
}
