package pt.up.fe.specs.util.parsing.arguments;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.RetryingTest;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for {@link Gluer} class.
 * Tests delimiter pair handling functionality for parsing frameworks.
 * 
 * @author Generated Tests
 */
@DisplayName("Gluer Tests")
class GluerTest {

    @Nested
    @DisplayName("Factory Method Tests")
    class FactoryMethodTests {

        @Test
        @DisplayName("Should create double quote gluer with correct configuration")
        void testFactory_NewDoubleQuote_CorrectConfiguration() {
            // Act
            Gluer gluer = Gluer.newDoubleQuote();

            // Assert
            assertThat(gluer).isNotNull();
            assertThat(gluer.getGluerStart()).isEqualTo("\"");
            assertThat(gluer.getGluerEnd()).isEqualTo("\"");
            assertThat(gluer.keepDelimiters()).isFalse();
        }

        @Test
        @DisplayName("Should create tag gluer with correct configuration")
        void testFactory_NewTag_CorrectConfiguration() {
            // Act
            Gluer gluer = Gluer.newTag();

            // Assert
            assertThat(gluer).isNotNull();
            assertThat(gluer.getGluerStart()).isEqualTo("<");
            assertThat(gluer.getGluerEnd()).isEqualTo(">");
            assertThat(gluer.keepDelimiters()).isTrue();
        }

        @Test
        @DisplayName("Should create parenthesis gluer with correct configuration")
        void testFactory_NewParenthesis_CorrectConfiguration() {
            // Act
            Gluer gluer = Gluer.newParenthesis();

            // Assert
            assertThat(gluer).isNotNull();
            assertThat(gluer.getGluerStart()).isEqualTo("(");
            assertThat(gluer.getGluerEnd()).isEqualTo(")");
            assertThat(gluer.keepDelimiters()).isTrue();
        }
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create gluer with basic constructor")
        void testConstructor_BasicStartEnd_CorrectDefaults() {
            // Act
            Gluer gluer = new Gluer("[", "]");

            // Assert
            assertThat(gluer.getGluerStart()).isEqualTo("[");
            assertThat(gluer.getGluerEnd()).isEqualTo("]");
            assertThat(gluer.keepDelimiters()).isFalse(); // Default should be false
        }

        @Test
        @DisplayName("Should create gluer with full constructor")
        void testConstructor_FullParameters_CorrectConfiguration() {
            // Act
            Gluer gluer = new Gluer("{", "}", true);

            // Assert
            assertThat(gluer.getGluerStart()).isEqualTo("{");
            assertThat(gluer.getGluerEnd()).isEqualTo("}");
            assertThat(gluer.keepDelimiters()).isTrue();
        }

        @Test
        @DisplayName("Should handle same start and end delimiters")
        void testConstructor_SameStartEnd_WorksCorrectly() {
            // Act
            Gluer gluer = new Gluer("'", "'", false);

            // Assert
            assertThat(gluer.getGluerStart()).isEqualTo("'");
            assertThat(gluer.getGluerEnd()).isEqualTo("'");
            assertThat(gluer.keepDelimiters()).isFalse();
        }

        @Test
        @DisplayName("Should handle multi-character delimiters")
        void testConstructor_MultiCharDelimiters_WorksCorrectly() {
            // Act
            Gluer gluer = new Gluer("<!--", "-->", true);

            // Assert
            assertThat(gluer.getGluerStart()).isEqualTo("<!--");
            assertThat(gluer.getGluerEnd()).isEqualTo("-->");
            assertThat(gluer.keepDelimiters()).isTrue();
        }

        @Test
        @DisplayName("Should handle empty delimiters")
        void testConstructor_EmptyDelimiters_WorksCorrectly() {
            // Act
            Gluer gluer = new Gluer("", "", false);

            // Assert
            assertThat(gluer.getGluerStart()).isEqualTo("");
            assertThat(gluer.getGluerEnd()).isEqualTo("");
            assertThat(gluer.keepDelimiters()).isFalse();
        }

        @Test
        @DisplayName("Should handle null delimiters")
        void testConstructor_NullDelimiters_WorksCorrectly() {
            // Act
            Gluer gluer = new Gluer(null, null, true);

            // Assert
            assertThat(gluer.getGluerStart()).isNull();
            assertThat(gluer.getGluerEnd()).isNull();
            assertThat(gluer.keepDelimiters()).isTrue();
        }
    }

    @Nested
    @DisplayName("Getter Method Tests")
    class GetterMethodTests {

        @Test
        @DisplayName("Should return correct gluer start")
        void testGetGluerStart_ReturnsCorrectValue() {
            // Arrange
            Gluer gluer = new Gluer("START", "END");

            // Act & Assert
            assertThat(gluer.getGluerStart()).isEqualTo("START");
        }

        @Test
        @DisplayName("Should return correct gluer end")
        void testGetGluerEnd_ReturnsCorrectValue() {
            // Arrange
            Gluer gluer = new Gluer("START", "END");

            // Act & Assert
            assertThat(gluer.getGluerEnd()).isEqualTo("END");
        }

        @Test
        @DisplayName("Should return correct keep delimiters flag")
        void testKeepDelimiters_ReturnsCorrectValue() {
            // Arrange
            Gluer gluerKeep = new Gluer("(", ")", true);
            Gluer gluerDiscard = new Gluer("\"", "\"", false);

            // Act & Assert
            assertThat(gluerKeep.keepDelimiters()).isTrue();
            assertThat(gluerDiscard.keepDelimiters()).isFalse();
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle special characters as delimiters")
        void testEdgeCases_SpecialCharacters_WorksCorrectly() {
            // Act
            Gluer gluer = new Gluer("\\n", "\\t", true);

            // Assert
            assertThat(gluer.getGluerStart()).isEqualTo("\\n");
            assertThat(gluer.getGluerEnd()).isEqualTo("\\t");
            assertThat(gluer.keepDelimiters()).isTrue();
        }

        @Test
        @DisplayName("Should handle unicode characters as delimiters")
        void testEdgeCases_UnicodeCharacters_WorksCorrectly() {
            // Act
            Gluer gluer = new Gluer("«", "»", false);

            // Assert
            assertThat(gluer.getGluerStart()).isEqualTo("«");
            assertThat(gluer.getGluerEnd()).isEqualTo("»");
            assertThat(gluer.keepDelimiters()).isFalse();
        }

        @Test
        @DisplayName("Should handle asymmetric delimiters")
        void testEdgeCases_AsymmetricDelimiters_WorksCorrectly() {
            // Act
            Gluer gluer = new Gluer("BEGIN", "END", true);

            // Assert
            assertThat(gluer.getGluerStart()).isEqualTo("BEGIN");
            assertThat(gluer.getGluerEnd()).isEqualTo("END");
            assertThat(gluer.keepDelimiters()).isTrue();
        }

        @Test
        @DisplayName("Should handle very long delimiters")
        void testEdgeCases_LongDelimiters_WorksCorrectly() {
            // Act
            String longStart = "START".repeat(100);
            String longEnd = "END".repeat(100);
            Gluer gluer = new Gluer(longStart, longEnd, false);

            // Assert
            assertThat(gluer.getGluerStart()).isEqualTo(longStart);
            assertThat(gluer.getGluerEnd()).isEqualTo(longEnd);
            assertThat(gluer.keepDelimiters()).isFalse();
        }
    }

    @Nested
    @DisplayName("Factory Comparison Tests")
    class FactoryComparisonTests {

        @Test
        @DisplayName("Should have different keep delimiter settings for different factories")
        void testFactories_KeepDelimiterSettings_DifferCorrectly() {
            // Act
            Gluer doubleQuote = Gluer.newDoubleQuote();
            Gluer tag = Gluer.newTag();
            Gluer parenthesis = Gluer.newParenthesis();

            // Assert
            assertThat(doubleQuote.keepDelimiters()).isFalse();
            assertThat(tag.keepDelimiters()).isTrue();
            assertThat(parenthesis.keepDelimiters()).isTrue();
        }

        @Test
        @DisplayName("Should create independent instances")
        void testFactories_IndependentInstances_NotSameObject() {
            // Act
            Gluer gluer1 = Gluer.newDoubleQuote();
            Gluer gluer2 = Gluer.newDoubleQuote();

            // Assert
            assertThat(gluer1).isNotSameAs(gluer2);
            assertThat(gluer1.getGluerStart()).isEqualTo(gluer2.getGluerStart());
            assertThat(gluer1.getGluerEnd()).isEqualTo(gluer2.getGluerEnd());
            assertThat(gluer1.keepDelimiters()).isEqualTo(gluer2.keepDelimiters());
        }
    }

    @Nested
    @DisplayName("Real World Usage Tests")
    class RealWorldUsageTests {

        @Test
        @DisplayName("Should support common programming language delimiters")
        void testRealWorld_ProgrammingLanguages_SupportedCorrectly() {
            // Arrange & Act
            Gluer stringLiteral = new Gluer("\"", "\"", false);
            Gluer charLiteral = new Gluer("'", "'", false);
            Gluer codeBlock = new Gluer("{", "}", true);
            Gluer arrayBrackets = new Gluer("[", "]", true);
            Gluer functionCall = new Gluer("(", ")", true);

            // Assert
            assertThat(stringLiteral.getGluerStart()).isEqualTo("\"");
            assertThat(stringLiteral.keepDelimiters()).isFalse();

            assertThat(charLiteral.getGluerStart()).isEqualTo("'");
            assertThat(charLiteral.keepDelimiters()).isFalse();

            assertThat(codeBlock.getGluerStart()).isEqualTo("{");
            assertThat(codeBlock.keepDelimiters()).isTrue();

            assertThat(arrayBrackets.getGluerStart()).isEqualTo("[");
            assertThat(arrayBrackets.keepDelimiters()).isTrue();

            assertThat(functionCall.getGluerStart()).isEqualTo("(");
            assertThat(functionCall.keepDelimiters()).isTrue();
        }

        @Test
        @DisplayName("Should support markup language delimiters")
        void testRealWorld_MarkupLanguages_SupportedCorrectly() {
            // Arrange & Act
            Gluer xmlTag = new Gluer("<tag>", "</tag>", true);
            Gluer htmlComment = new Gluer("<!--", "-->", true);
            Gluer xmlCdata = new Gluer("<![CDATA[", "]]>", true);

            // Assert
            assertThat(xmlTag.getGluerStart()).isEqualTo("<tag>");
            assertThat(xmlTag.getGluerEnd()).isEqualTo("</tag>");
            assertThat(xmlTag.keepDelimiters()).isTrue();

            assertThat(htmlComment.getGluerStart()).isEqualTo("<!--");
            assertThat(htmlComment.getGluerEnd()).isEqualTo("-->");
            assertThat(htmlComment.keepDelimiters()).isTrue();

            assertThat(xmlCdata.getGluerStart()).isEqualTo("<![CDATA[");
            assertThat(xmlCdata.getGluerEnd()).isEqualTo("]]>");
            assertThat(xmlCdata.keepDelimiters()).isTrue();
        }

        @Test
        @DisplayName("Should support shell scripting delimiters")
        void testRealWorld_ShellScripting_SupportedCorrectly() {
            // Arrange & Act
            Gluer singleQuote = new Gluer("'", "'", false);
            Gluer doubleQuote = new Gluer("\"", "\"", false);
            Gluer backquote = new Gluer("`", "`", false);
            Gluer dollarParen = new Gluer("$(", ")", true);
            Gluer dollarBrace = new Gluer("${", "}", true);

            // Assert
            assertThat(singleQuote.getGluerStart()).isEqualTo("'");
            assertThat(singleQuote.keepDelimiters()).isFalse();

            assertThat(doubleQuote.getGluerStart()).isEqualTo("\"");
            assertThat(doubleQuote.keepDelimiters()).isFalse();

            assertThat(backquote.getGluerStart()).isEqualTo("`");
            assertThat(backquote.keepDelimiters()).isFalse();

            assertThat(dollarParen.getGluerStart()).isEqualTo("$(");
            assertThat(dollarParen.getGluerEnd()).isEqualTo(")");
            assertThat(dollarParen.keepDelimiters()).isTrue();

            assertThat(dollarBrace.getGluerStart()).isEqualTo("${");
            assertThat(dollarBrace.getGluerEnd()).isEqualTo("}");
            assertThat(dollarBrace.keepDelimiters()).isTrue();
        }
    }

    @Nested
    @DisplayName("Immutability Tests")
    class ImmutabilityTests {

        @Test
        @DisplayName("Should be immutable after construction")
        void testImmutability_NoSetters_ImmutableObject() {
            // Arrange
            Gluer gluer = new Gluer("start", "end", true);

            // Act & Assert - Verify no setter methods exist
            assertThat(gluer.getGluerStart()).isEqualTo("start");
            assertThat(gluer.getGluerEnd()).isEqualTo("end");
            assertThat(gluer.keepDelimiters()).isTrue();

            // Values should remain constant
            assertThat(gluer.getGluerStart()).isEqualTo("start");
            assertThat(gluer.getGluerEnd()).isEqualTo("end");
            assertThat(gluer.keepDelimiters()).isTrue();
        }

        @Test
        @DisplayName("Should return same values on multiple calls")
        void testImmutability_ConsistentValues_SameOnMultipleCalls() {
            // Arrange
            Gluer gluer = new Gluer("test", "value", false);

            // Act
            String start1 = gluer.getGluerStart();
            String start2 = gluer.getGluerStart();
            String end1 = gluer.getGluerEnd();
            String end2 = gluer.getGluerEnd();
            boolean keep1 = gluer.keepDelimiters();
            boolean keep2 = gluer.keepDelimiters();

            // Assert
            assertThat(start1).isEqualTo(start2);
            assertThat(end1).isEqualTo(end2);
            assertThat(keep1).isEqualTo(keep2);
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @RetryingTest(5)
        @DisplayName("Should handle many gluer creations efficiently")
        void testPerformance_ManyCreations_EfficientCreation() {
            // Act
            long startTime = System.nanoTime();
            for (int i = 0; i < 10000; i++) {
                Gluer gluer = new Gluer("start" + i, "end" + i, i % 2 == 0);
                // Use the gluer to prevent optimization
                assertThat(gluer.getGluerStart()).isNotNull();
            }
            long endTime = System.nanoTime();

            // Assert
            assertThat(endTime - startTime).isLessThan(500_000_000); // Less than 500ms
        }

        @RetryingTest(5)
        @DisplayName("Should handle getter calls efficiently")
        void testPerformance_GetterCalls_EfficientAccess() {
            // Arrange
            Gluer gluer = new Gluer("start", "end", true);

            // Act
            long startTime = System.nanoTime();
            for (int i = 0; i < 100000; i++) {
                String start = gluer.getGluerStart();
                String end = gluer.getGluerEnd();
                boolean keep = gluer.keepDelimiters();
                // Use values to prevent optimization
                assertThat(start).isNotNull();
                assertThat(end).isNotNull();
                assertThat(keep).isIn(true, false);
            }
            long endTime = System.nanoTime();

            // Assert
            assertThat(endTime - startTime).isLessThan(500_000_000); // Less than 500ms
        }
    }
}
