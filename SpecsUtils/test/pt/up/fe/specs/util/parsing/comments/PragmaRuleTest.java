package pt.up.fe.specs.util.parsing.comments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test suite for PragmaRule class.
 * Tests the concrete implementation of TextParserRule for pragma directives.
 * 
 * @author Generated Tests
 */
@DisplayName("PragmaRule Tests")
@ExtendWith(MockitoExtension.class)
public class PragmaRuleTest {

    @Mock
    private Iterator<String> mockIterator;

    private PragmaRule rule;

    @BeforeEach
    void setUp() {
        rule = new PragmaRule();
    }

    @Nested
    @DisplayName("Interface Implementation Tests")
    class InterfaceImplementationTests {

        @Test
        @DisplayName("Should properly implement TextParserRule interface")
        void testInterface_Implementation_Correct() {
            // Assert
            assertThat(rule).isInstanceOf(TextParserRule.class);
            assertThat(PragmaRule.class.getInterfaces()).contains(TextParserRule.class);
        }

        @Test
        @DisplayName("Should have correct method signature")
        void testInterface_MethodSignature_Correct() throws NoSuchMethodException {
            // Act & Assert
            var method = PragmaRule.class.getMethod("apply", String.class, Iterator.class);
            assertThat(method.getReturnType()).isEqualTo(Optional.class);
        }

        @Test
        @DisplayName("Should properly override interface method")
        void testInterface_MethodOverride_Correct() throws NoSuchMethodException {
            // Act & Assert - Verify method exists and is correctly overridden
            var method = PragmaRule.class.getDeclaredMethod("apply", String.class, Iterator.class);
            assertThat(method).isNotNull();
            assertThat(method.getDeclaringClass()).isEqualTo(PragmaRule.class);

            // Verify that it properly implements the interface method
            var interfaceMethod = TextParserRule.class.getMethod("apply", String.class, Iterator.class);
            assertThat(method.getName()).isEqualTo(interfaceMethod.getName());
            assertThat(method.getReturnType()).isEqualTo(interfaceMethod.getReturnType());
        }
    }

    @Nested
    @DisplayName("Single Line Pragma Tests")
    class SingleLinePragmaTests {

        @Test
        @DisplayName("Should detect simple pragma directive")
        void testApply_SimplePragma_Detected() {
            // Act
            Optional<TextElement> result = rule.apply("#pragma once", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getType()).isEqualTo(TextElementType.PRAGMA);
            assertThat(result.get().getText()).isEqualTo("once");

            // Verify iterator not used for single line pragma
            verifyNoInteractions(mockIterator);
        }

        @Test
        @DisplayName("Should detect pragma with multiple parameters")
        void testApply_PragmaWithParameters_Detected() {
            // Act
            Optional<TextElement> result = rule.apply("#pragma pack(push, 1)", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getType()).isEqualTo(TextElementType.PRAGMA);
            assertThat(result.get().getText()).isEqualTo("pack(push, 1)");
        }

        @Test
        @DisplayName("Should detect pragma with indentation")
        void testApply_IndentedPragma_Detected() {
            // Act
            Optional<TextElement> result = rule.apply("    #pragma warning(disable: 4996)", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getType()).isEqualTo(TextElementType.PRAGMA);
            assertThat(result.get().getText()).isEqualTo("warning(disable: 4996)");
        }

        @Test
        @DisplayName("Should detect pragma with case insensitive matching")
        void testApply_CaseInsensitivePragma_Detected() {
            // Act & Assert
            var pragmaVariations = List.of(
                    "#PRAGMA once",
                    "#Pragma pack",
                    "#pragma ONCE",
                    "#PrAgMa warning");

            pragmaVariations.forEach(pragma -> {
                Optional<TextElement> result = rule.apply(pragma, mockIterator);
                assertThat(result).isPresent();
                assertThat(result.get().getType()).isEqualTo(TextElementType.PRAGMA);
            });
        }

        @Test
        @DisplayName("Should handle pragma with no content")
        void testApply_PragmaNoContent_Detected() {
            // Act
            Optional<TextElement> result = rule.apply("#pragma", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getType()).isEqualTo(TextElementType.PRAGMA);
            assertThat(result.get().getText()).isEmpty();
        }

        @Test
        @DisplayName("Should handle pragma with extra spaces")
        void testApply_PragmaWithSpaces_Detected() {
            // Act
            Optional<TextElement> result = rule.apply("  #pragma   pack   (1)  ", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo("pack   (1)");
        }
    }

    @Nested
    @DisplayName("Multi-Line Pragma Tests")
    class MultiLinePragmaTests {

        @Test
        @DisplayName("Should detect two-line pragma with backslash continuation")
        void testApply_TwoLinePragma_Detected() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(true);
            when(mockIterator.next()).thenReturn("    second_part");

            // Act
            Optional<TextElement> result = rule.apply("#pragma first_part \\", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getType()).isEqualTo(TextElementType.PRAGMA);
            assertThat(result.get().getText()).isEqualTo("first_part \nsecond_part");

            verify(mockIterator).hasNext();
            verify(mockIterator).next();
        }

        @Test
        @DisplayName("Should detect three-line pragma with multiple continuations")
        void testApply_ThreeLinePragma_Detected() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(true, true);
            when(mockIterator.next()).thenReturn("second_line \\", "final_line");

            // Act
            Optional<TextElement> result = rule.apply("#pragma first_line \\", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getType()).isEqualTo(TextElementType.PRAGMA);
            assertThat(result.get().getText()).isEqualTo("first_line \nsecond_line \nfinal_line");

            verify(mockIterator, times(2)).hasNext();
            verify(mockIterator, times(2)).next();
        }

        @Test
        @DisplayName("Should handle pragma with empty continuation lines")
        void testApply_EmptyContinuationLines_Handled() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(true, true);
            when(mockIterator.next()).thenReturn(" \\", "final_content");

            // Act
            Optional<TextElement> result = rule.apply("#pragma start \\", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo("start \n\nfinal_content");
        }

        @Test
        @DisplayName("Should handle pragma with whitespace around backslash")
        void testApply_WhitespaceAroundBackslash_Handled() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(true);
            when(mockIterator.next()).thenReturn("  continuation  ");

            // Act - Input will be trimmed, so spaces after pragma are not relevant
            Optional<TextElement> result = rule.apply("#pragma start\\", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo("start\ncontinuation");
        }

        @Test
        @DisplayName("Should handle complex multi-line pragma directive")
        void testApply_ComplexMultiLinePragma_Handled() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(true, true, true);
            when(mockIterator.next()).thenReturn(
                    "    warning(push) \\",
                    "    warning(disable: 4996) \\",
                    "    warning(disable: 4244)");

            // Act
            Optional<TextElement> result = rule.apply("#pragma \\", mockIterator);

            // Assert
            assertThat(result).isPresent();
            String text = result.get().getText();
            assertThat(text).contains("warning(push)");
            assertThat(text).contains("warning(disable: 4996)");
            assertThat(text).contains("warning(disable: 4244)");
        }
    }

    @Nested
    @DisplayName("Non-Pragma Line Tests")
    class NonPragmaLineTests {

        @Test
        @DisplayName("Should not detect pragma in regular code")
        void testApply_RegularCode_NotDetected() {
            // Act
            Optional<TextElement> result = rule.apply("int x = 5;", mockIterator);

            // Assert
            assertThat(result).isEmpty();
            verifyNoInteractions(mockIterator);
        }

        @Test
        @DisplayName("Should not detect pragma in empty line")
        void testApply_EmptyLine_NotDetected() {
            // Act
            Optional<TextElement> result = rule.apply("", mockIterator);

            // Assert
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should not detect pragma in comment")
        void testApply_PragmaInComment_NotDetected() {
            // Act
            Optional<TextElement> result = rule.apply("// #pragma once", mockIterator);

            // Assert
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should not detect pragma in string literal")
        void testApply_PragmaInString_NotDetected() {
            // Act
            Optional<TextElement> result = rule.apply("String s = \"#pragma test\";", mockIterator);

            // Assert
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should not detect partial pragma directive")
        void testApply_PartialPragma_NotDetected() {
            // Act & Assert
            var partialPragmas = List.of(
                    "#prag",
                    "#pr",
                    "#",
                    "pragma",
                    "# pragma");

            partialPragmas.forEach(partial -> {
                Optional<TextElement> result = rule.apply(partial, mockIterator);
                assertThat(result).isEmpty();
            });
        }

        @Test
        @DisplayName("Should not detect other preprocessor directives")
        void testApply_OtherPreprocessorDirectives_NotDetected() {
            // Act & Assert
            var otherDirectives = List.of(
                    "#include <stdio.h>",
                    "#define MAX_SIZE 100",
                    "#ifdef DEBUG",
                    "#ifndef RELEASE",
                    "#endif",
                    "#error \"Compilation error\"");

            otherDirectives.forEach(directive -> {
                Optional<TextElement> result = rule.apply(directive, mockIterator);
                assertThat(result).isEmpty();
            });
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should return empty when continuation line not available")
        void testApply_NoContinuationLine_ReturnsEmpty() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(false);

            // Act
            Optional<TextElement> result = rule.apply("#pragma incomplete \\", mockIterator);

            // Assert - Should return empty and log warning
            assertThat(result).isEmpty();
            verify(mockIterator).hasNext();
        }

        @Test
        @DisplayName("Should handle null line parameter")
        void testApply_NullLine_ThrowsException() {
            // Act & Assert
            assertThatThrownBy(() -> rule.apply(null, mockIterator))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle null iterator for single line pragma")
        void testApply_NullIteratorSingleLine_WorksCorrectly() {
            // Act
            Optional<TextElement> result = rule.apply("#pragma once", null);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo("once");
        }

        @Test
        @DisplayName("Should handle null iterator for multi-line pragma")
        void testApply_NullIteratorMultiLine_ThrowsException() {
            // Act & Assert
            assertThatThrownBy(() -> rule.apply("#pragma multi \\", null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle iterator exceptions gracefully")
        void testApply_IteratorException_PropagatesException() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(true);
            when(mockIterator.next()).thenThrow(new RuntimeException("Iterator error"));

            // Act & Assert
            assertThatThrownBy(() -> rule.apply("#pragma multi \\", mockIterator))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Iterator error");
        }

        @Test
        @DisplayName("Should handle very short lines gracefully")
        void testApply_VeryShortLines_HandledGracefully() {
            // Act & Assert
            var shortLines = List.of("", "#", "# ", "#p", "#pr");

            shortLines.forEach(line -> {
                Optional<TextElement> result = rule.apply(line, mockIterator);
                assertThat(result).isEmpty();
            });
        }
    }

    @Nested
    @DisplayName("Content Processing Tests")
    class ContentProcessingTests {

        @Test
        @DisplayName("Should strip pragma keyword correctly")
        void testApply_PragmaKeywordStripping_Correct() {
            // Act
            Optional<TextElement> result = rule.apply("#pragma pack(1)", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo("pack(1)");
            assertThat(result.get().getText()).doesNotContain("#pragma");
        }

        @Test
        @DisplayName("Should preserve special characters in pragma content")
        void testApply_SpecialCharacters_Preserved() {
            // Act
            Optional<TextElement> result = rule.apply("#pragma warning(disable: 4996, 4244)", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo("warning(disable: 4996, 4244)");
        }

        @Test
        @DisplayName("Should handle pragma with complex parameters")
        void testApply_ComplexParameters_Handled() {
            // Act
            Optional<TextElement> result = rule.apply("#pragma omp parallel for private(i) shared(array)",
                    mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo("omp parallel for private(i) shared(array)");
        }

        @Test
        @DisplayName("Should join multi-line pragma content with newlines")
        void testApply_MultiLineJoining_UsesNewlines() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(true, true);
            when(mockIterator.next()).thenReturn("line2 \\", "line3");

            // Act
            Optional<TextElement> result = rule.apply("#pragma line1 \\", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo("line1 \nline2 \nline3");
        }

        @Test
        @DisplayName("Should remove backslash from continuation lines")
        void testApply_BackslashRemoval_Correct() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(true);
            when(mockIterator.next()).thenReturn("final_part");

            // Act
            Optional<TextElement> result = rule.apply("#pragma first_part \\", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo("first_part \nfinal_part");
            assertThat(result.get().getText()).doesNotContain("\\");
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle pragma at exact minimum length")
        void testApply_MinimumLength_Handled() {
            // Act
            Optional<TextElement> result = rule.apply("#pragma", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEmpty();
        }

        @Test
        @DisplayName("Should handle pragma with only backslash")
        void testApply_OnlyBackslash_Handled() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(true);
            when(mockIterator.next()).thenReturn("content");

            // Act
            Optional<TextElement> result = rule.apply("#pragma \\", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo("\ncontent");
        }

        @Test
        @DisplayName("Should handle multiple consecutive backslashes")
        void testApply_MultipleBackslashes_Handled() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(true, true);
            when(mockIterator.next()).thenReturn("line2 \\", "line3");

            // Act
            Optional<TextElement> result = rule.apply("#pragma line1 \\", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo("line1 \nline2 \nline3");
        }

        @Test
        @DisplayName("Should handle pragma with Unicode characters")
        void testApply_UnicodeCharacters_Handled() {
            // Act
            Optional<TextElement> result = rule.apply("#pragma message(\"Unicode: \u2603 \u03B1\u03B2\u03B3\")",
                    mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).contains("Unicode: \u2603 \u03B1\u03B2\u03B3");
        }

        @Test
        @DisplayName("Should handle very long pragma content")
        void testApply_VeryLongContent_Handled() {
            // Arrange
            String longContent = "very_long_pragma_directive ".repeat(50);

            // Act
            Optional<TextElement> result = rule.apply("#pragma " + longContent, mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo(longContent.trim());
        }
    }

    @Nested
    @DisplayName("Real-World Scenarios")
    class RealWorldScenarios {

        @Test
        @DisplayName("Should handle OpenMP pragma directives")
        void testApply_OpenMPPragmas_Handled() {
            // Act & Assert
            var openMPPragmas = List.of(
                    "#pragma omp parallel",
                    "#pragma omp for",
                    "#pragma omp parallel for private(i)",
                    "#pragma omp critical",
                    "#pragma omp barrier");

            openMPPragmas.forEach(pragma -> {
                Optional<TextElement> result = rule.apply(pragma, mockIterator);
                assertThat(result).isPresent();
                assertThat(result.get().getType()).isEqualTo(TextElementType.PRAGMA);
                String expectedContent = pragma.substring("#pragma ".length());
                assertThat(result.get().getText()).isEqualTo(expectedContent);
            });
        }

        @Test
        @DisplayName("Should handle Microsoft Visual C++ pragmas")
        void testApply_MSVCPragmas_Handled() {
            // Act & Assert
            var msvcPragmas = List.of(
                    "#pragma once",
                    "#pragma pack(push, 1)",
                    "#pragma pack(pop)",
                    "#pragma warning(push)",
                    "#pragma warning(disable: 4996)",
                    "#pragma comment(lib, \"kernel32.lib\")");

            msvcPragmas.forEach(pragma -> {
                Optional<TextElement> result = rule.apply(pragma, mockIterator);
                assertThat(result).isPresent();
                assertThat(result.get().getType()).isEqualTo(TextElementType.PRAGMA);
            });
        }

        @Test
        @DisplayName("Should handle GCC pragma directives")
        void testApply_GCCPragmas_Handled() {
            // Act & Assert
            var gccPragmas = List.of(
                    "#pragma GCC diagnostic push",
                    "#pragma GCC diagnostic ignored \"-Wunused-variable\"",
                    "#pragma GCC diagnostic pop",
                    "#pragma GCC optimize(\"O3\")",
                    "#pragma GCC target(\"sse4.2\")");

            gccPragmas.forEach(pragma -> {
                Optional<TextElement> result = rule.apply(pragma, mockIterator);
                assertThat(result).isPresent();
                assertThat(result.get().getType()).isEqualTo(TextElementType.PRAGMA);
            });
        }

        @Test
        @DisplayName("Should handle multi-line macro-style pragma")
        void testApply_MultiLineMacroStyle_Handled() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(true, true, true);
            when(mockIterator.next()).thenReturn(
                    "    for (int i = 0; i < n; i++) { \\",
                    "        array[i] = i * 2; \\",
                    "    }");

            // Act
            Optional<TextElement> result = rule.apply("#pragma define_loop \\", mockIterator);

            // Assert
            assertThat(result).isPresent();
            String text = result.get().getText();
            assertThat(text).contains("define_loop");
            assertThat(text).contains("for (int i = 0; i < n; i++)");
            assertThat(text).contains("array[i] = i * 2;");
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should handle many pragma detections efficiently")
        void testPerformance_ManyDetections_Efficient() {
            // Act & Assert
            assertThatCode(() -> {
                for (int i = 0; i < 10000; i++) {
                    String pragma = "#pragma directive_" + i;
                    Optional<TextElement> result = rule.apply(pragma, mockIterator);
                    assertThat(result).isPresent();
                    assertThat(result.get().getText()).isEqualTo("directive_" + i);
                }
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle thread safety correctly")
        void testPerformance_ThreadSafety_Maintained() {
            // Act & Assert
            assertThatCode(() -> {
                var threads = java.util.stream.IntStream.range(0, 10)
                        .mapToObj(i -> new Thread(() -> {
                            for (int j = 0; j < 1000; j++) {
                                String pragma = "#pragma thread_" + i + "_directive_" + j;
                                Optional<TextElement> result = rule.apply(pragma, mockIterator);
                                assertThat(result).isPresent();
                                assertThat(result.get().getType()).isEqualTo(TextElementType.PRAGMA);
                            }
                        }))
                        .toList();

                threads.forEach(Thread::start);
                for (Thread thread : threads) {
                    thread.join();
                }
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work correctly with TextElement interface")
        void testIntegration_TextElementInterface_WorksCorrectly() {
            // Act
            Optional<TextElement> result = rule.apply("#pragma integration_test", mockIterator);

            // Assert
            assertThat(result).isPresent();
            TextElement element = result.get();

            // Should work through TextElement interface
            assertThat(element.getType()).isEqualTo(TextElementType.PRAGMA);
            assertThat(element.getText()).isEqualTo("integration_test");

            // Should be a GenericTextElement instance (from factory method)
            assertThat(element).isInstanceOf(GenericTextElement.class);
        }

        @Test
        @DisplayName("Should work with realistic iterator implementation")
        void testIntegration_RealisticIterator_WorksCorrectly() {
            // Arrange
            List<String> lines = List.of(
                    "    second_line \\",
                    "    third_line");
            Iterator<String> realIterator = lines.iterator();

            // Act
            Optional<TextElement> result = rule.apply("#pragma first_line \\", realIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo("first_line \nsecond_line \nthird_line");

            // Iterator should be properly consumed
            assertThat(realIterator.hasNext()).isFalse();
        }

        @Test
        @DisplayName("Should work correctly when used with other parser rules")
        void testIntegration_WithOtherRules_WorksCorrectly() {
            // Arrange
            PragmaRule pragmaRule = new PragmaRule();
            InlineCommentRule inlineRule = new InlineCommentRule();

            // Act & Assert
            // Test pragma detection
            Optional<TextElement> pragmaResult = pragmaRule.apply("#pragma once", mockIterator);
            assertThat(pragmaResult).isPresent();
            assertThat(pragmaResult.get().getType()).isEqualTo(TextElementType.PRAGMA);

            // Test that inline comment rule doesn't interfere
            Optional<TextElement> inlineResult = inlineRule.apply("#pragma once", mockIterator);
            assertThat(inlineResult).isEmpty(); // No inline comment marker

            // Test inline comment with pragma content
            Optional<TextElement> commentResult = inlineRule.apply("// #pragma once", mockIterator);
            assertThat(commentResult).isPresent();
            assertThat(commentResult.get().getType()).isEqualTo(TextElementType.INLINE_COMMENT);
        }
    }
}
