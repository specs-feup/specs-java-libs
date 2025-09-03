package org.specs.generators.java;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.specs.generators.java.utils.Utils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Comprehensive test suite for the {@link IGenerate} interface.
 * Tests the fundamental code generation contract and default methods.
 * 
 * @author Generated Tests
 */
@DisplayName("IGenerate Interface Tests")
public class IGenerateTest {

    /**
     * Test implementation of IGenerate for testing purposes.
     */
    private static class TestGenerateImpl implements IGenerate {
        private final String code;

        public TestGenerateImpl(String code) {
            this.code = code;
        }

        @Override
        public StringBuilder generateCode(int indentation) {
            StringBuilder builder = new StringBuilder();
            builder.append(Utils.indent(indentation));
            builder.append(code);
            return builder;
        }
    }

    @Nested
    @DisplayName("Default Method Tests")
    class DefaultMethodTests {

        @Test
        @DisplayName("ln() should return system line separator")
        void testLn_ReturnsSystemLineSeparator() {
            // Given
            IGenerate generator = new TestGenerateImpl("test");

            // When
            String result = generator.ln();

            // Then
            assertThat(result).isEqualTo(System.lineSeparator());
        }

        @Test
        @DisplayName("ln() should be consistent across multiple calls")
        void testLn_ConsistentAcrossMultipleCalls() {
            // Given
            IGenerate generator = new TestGenerateImpl("test");

            // When
            String first = generator.ln();
            String second = generator.ln();

            // Then
            assertThat(first).isEqualTo(second);
            assertThat(first).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("Abstract Method Contract Tests")
    class AbstractMethodContractTests {

        @Test
        @DisplayName("generateCode() should handle zero indentation")
        void testGenerateCode_ZeroIndentation_ReturnsCodeWithoutIndent() {
            // Given
            IGenerate generator = new TestGenerateImpl("public class Test {}");

            // When
            StringBuilder result = generator.generateCode(0);

            // Then
            assertThat(result.toString()).isEqualTo("public class Test {}");
        }

        @Test
        @DisplayName("generateCode() should handle positive indentation")
        void testGenerateCode_PositiveIndentation_ReturnsIndentedCode() {
            // Given
            IGenerate generator = new TestGenerateImpl("public class Test {}");

            // When
            StringBuilder result = generator.generateCode(1);

            // Then
            assertThat(result.toString()).isEqualTo("    public class Test {}");
        }

        @Test
        @DisplayName("generateCode() should handle multiple levels of indentation")
        void testGenerateCode_MultipleLevels_ReturnsProperlyIndented() {
            // Given
            IGenerate generator = new TestGenerateImpl("method();");

            // When
            StringBuilder result = generator.generateCode(3);

            // Then
            assertThat(result.toString()).isEqualTo("            method();");
            assertThat(result.toString()).startsWith("            "); // 12 spaces (3 * 4)
        }

        @Test
        @DisplayName("generateCode() should return StringBuilder")
        void testGenerateCode_ReturnsStringBuilder() {
            // Given
            IGenerate generator = new TestGenerateImpl("test");

            // When
            Object result = generator.generateCode(0);

            // Then
            assertThat(result).isInstanceOf(StringBuilder.class);
        }

        @Test
        @DisplayName("generateCode() should return mutable StringBuilder")
        void testGenerateCode_ReturnsMutableStringBuilder() {
            // Given
            IGenerate generator = new TestGenerateImpl("test");

            // When
            StringBuilder result = generator.generateCode(0);
            result.append(" modified");

            // Then
            assertThat(result.toString()).isEqualTo("test modified");
        }
    }

    @Nested
    @DisplayName("Interface Contract Tests")
    class InterfaceContractTests {

        @Test
        @DisplayName("Interface should be functional with single abstract method")
        void testInterface_IsFunctional() {
            // Given/When - using lambda to implement interface
            IGenerate lambdaGenerator = (indent) -> new StringBuilder("lambda code");

            // Then
            assertThat(lambdaGenerator.generateCode(0).toString()).isEqualTo("lambda code");
            assertThat(lambdaGenerator.ln()).isEqualTo(System.lineSeparator());
        }

        @Test
        @DisplayName("Multiple implementations should work independently")
        void testMultipleImplementations_WorkIndependently() {
            // Given
            IGenerate generator1 = new TestGenerateImpl("class A {}");
            IGenerate generator2 = new TestGenerateImpl("class B {}");

            // When
            StringBuilder result1 = generator1.generateCode(0);
            StringBuilder result2 = generator2.generateCode(1);

            // Then
            assertThat(result1.toString()).isEqualTo("class A {}");
            assertThat(result2.toString()).isEqualTo("    class B {}");
        }

        @Test
        @DisplayName("Inheritance should preserve default method behavior")
        void testInheritance_PreservesDefaultBehavior() {
            // Given
            class ExtendedGenerator extends TestGenerateImpl {
                public ExtendedGenerator(String code) {
                    super(code);
                }

                // Override generateCode but inherit ln()
                @Override
                public StringBuilder generateCode(int indentation) {
                    return super.generateCode(indentation).append(" extended");
                }
            }

            IGenerate generator = new ExtendedGenerator("base");

            // When
            String lineSeparator = generator.ln();
            StringBuilder code = generator.generateCode(0);

            // Then
            assertThat(lineSeparator).isEqualTo(System.lineSeparator());
            assertThat(code.toString()).isEqualTo("base extended");
        }
    }

    @Nested
    @DisplayName("Integration with Utils Tests")
    class UtilsIntegrationTests {

        @Test
        @DisplayName("Default ln() should match Utils.ln()")
        void testDefaultLn_MatchesUtilsLn() {
            // Given
            IGenerate generator = new TestGenerateImpl("test");

            // When
            String interfaceLn = generator.ln();
            String utilsLn = Utils.ln();

            // Then
            assertThat(interfaceLn).isEqualTo(utilsLn);
        }

        @Test
        @DisplayName("generateCode() should work with Utils.indent() indirectly")
        void testGenerateCode_WorksWithUtilsIndent() {
            // Given - our test implementation uses Utils.indent()
            IGenerate generator = new TestGenerateImpl("code");

            // When
            StringBuilder result = generator.generateCode(2);

            // Then
            assertThat(result.toString()).isEqualTo("        code"); // 8 spaces (2 * 4)
        }
    }
}
