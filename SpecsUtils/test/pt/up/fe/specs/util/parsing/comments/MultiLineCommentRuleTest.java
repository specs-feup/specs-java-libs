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
 * Comprehensive test suite for MultiLineCommentRule class.
 * Tests the concrete implementation of TextParserRule for multi-line comments.
 * 
 * @author Generated Tests
 */
@DisplayName("MultiLineCommentRule Tests")
@ExtendWith(MockitoExtension.class)
public class MultiLineCommentRuleTest {

    @Mock
    private Iterator<String> mockIterator;

    private MultiLineCommentRule rule;

    @BeforeEach
    void setUp() {
        rule = new MultiLineCommentRule();
    }

    @Nested
    @DisplayName("Interface Implementation Tests")
    class InterfaceImplementationTests {

        @Test
        @DisplayName("Should properly implement TextParserRule interface")
        void testInterface_Implementation_Correct() {
            // Assert
            assertThat(rule).isInstanceOf(TextParserRule.class);
            assertThat(MultiLineCommentRule.class.getInterfaces()).contains(TextParserRule.class);
        }

        @Test
        @DisplayName("Should have correct method signature")
        void testInterface_MethodSignature_Correct() throws NoSuchMethodException {
            // Act & Assert
            var method = MultiLineCommentRule.class.getMethod("apply", String.class, Iterator.class);
            assertThat(method.getReturnType()).isEqualTo(Optional.class);
        }

        @Test
        @DisplayName("Should properly override interface method")
        void testInterface_MethodOverride_Correct() throws NoSuchMethodException {
            // Act & Assert - Verify method exists and is correctly overridden
            var method = MultiLineCommentRule.class.getDeclaredMethod("apply", String.class, Iterator.class);
            assertThat(method).isNotNull();
            assertThat(method.getDeclaringClass()).isEqualTo(MultiLineCommentRule.class);

            // Verify that it properly implements the interface method
            var interfaceMethod = TextParserRule.class.getMethod("apply", String.class, Iterator.class);
            assertThat(method.getName()).isEqualTo(interfaceMethod.getName());
            assertThat(method.getReturnType()).isEqualTo(interfaceMethod.getReturnType());
        }
    }

    @Nested
    @DisplayName("Single Line Multi-Line Comment Tests")
    class SingleLineMultiLineCommentTests {

        @Test
        @DisplayName("Should detect single line multi-line comment")
        void testApply_SingleLineComment_Detected() {
            // Act
            Optional<TextElement> result = rule.apply("/* This is a comment */", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getType()).isEqualTo(TextElementType.MULTILINE_COMMENT);
            assertThat(result.get().getText()).isEqualTo("This is a comment");

            // Verify iterator not used for single line comment
            verifyNoInteractions(mockIterator);
        }

        @Test
        @DisplayName("Should detect comment with code before")
        void testApply_CommentWithCodeBefore_Detected() {
            // Act
            Optional<TextElement> result = rule.apply("int x = 5; /* variable declaration */", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getType()).isEqualTo(TextElementType.MULTILINE_COMMENT);
            assertThat(result.get().getText()).isEqualTo("variable declaration");
        }

        @Test
        @DisplayName("Should detect empty single line comment")
        void testApply_EmptySingleLineComment_Detected() {
            // Act
            Optional<TextElement> result = rule.apply("/**/", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getType()).isEqualTo(TextElementType.MULTILINE_COMMENT);
            assertThat(result.get().getText()).isEmpty();
        }

        @Test
        @DisplayName("Should handle comment with spaces")
        void testApply_CommentWithSpaces_HandledCorrectly() {
            // Act
            Optional<TextElement> result = rule.apply("/*   spaced content   */", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo("spaced content");
        }

        @Test
        @DisplayName("Should handle nested comment markers in single line")
        void testApply_NestedMarkersInSingleLine_HandledCorrectly() {
            // Act
            Optional<TextElement> result = rule.apply("/* comment with /* nested markers */ inside */", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo("comment with /* nested markers");
        }
    }

    @Nested
    @DisplayName("Multi-Line Comment Tests")
    class MultiLineCommentTests {

        @Test
        @DisplayName("Should detect two-line multi-line comment")
        void testApply_TwoLineComment_Detected() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(true);
            when(mockIterator.next()).thenReturn(" * Second line */");

            // Act
            Optional<TextElement> result = rule.apply("/* First line", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getType()).isEqualTo(TextElementType.MULTILINE_COMMENT);
            assertThat(result.get().getText()).isEqualTo("First line\n* Second line");

            verify(mockIterator).hasNext();
            verify(mockIterator).next();
        }

        @Test
        @DisplayName("Should detect three-line multi-line comment")
        void testApply_ThreeLineComment_Detected() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(true, true);
            when(mockIterator.next()).thenReturn(" * Middle line", " * Last line */");

            // Act
            Optional<TextElement> result = rule.apply("/* First line", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getType()).isEqualTo(TextElementType.MULTILINE_COMMENT);
            assertThat(result.get().getText()).isEqualTo("First line\n* Middle line\n* Last line");

            verify(mockIterator, times(2)).hasNext();
            verify(mockIterator, times(2)).next();
        }

        @Test
        @DisplayName("Should handle comment starting with content after marker")
        void testApply_ContentAfterStartMarker_HandledCorrectly() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(true);
            when(mockIterator.next()).thenReturn("continuation line */");

            // Act
            Optional<TextElement> result = rule.apply("code /* comment start", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo("comment start\ncontinuation line");
        }

        @Test
        @DisplayName("Should handle empty lines in multi-line comment")
        void testApply_EmptyLinesInComment_HandledCorrectly() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(true, true, true);
            when(mockIterator.next()).thenReturn("", " * Content line", " */");

            // Act
            Optional<TextElement> result = rule.apply("/*", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo("\n\n* Content line\n");
        }

        @Test
        @DisplayName("Should handle typical JavaDoc style comment")
        void testApply_JavaDocStyle_HandledCorrectly() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(true, true, true, true);
            when(mockIterator.next()).thenReturn(
                    " * This is a JavaDoc comment",
                    " * @param x the parameter",
                    " * @return the result",
                    " */");

            // Act
            Optional<TextElement> result = rule.apply("/**", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText())
                    .isEqualTo("*\n* This is a JavaDoc comment\n* @param x the parameter\n* @return the result\n");
        }
    }

    @Nested
    @DisplayName("Non-Comment Line Tests")
    class NonCommentLineTests {

        @Test
        @DisplayName("Should not detect comment in regular code")
        void testApply_RegularCode_NotDetected() {
            // Act
            Optional<TextElement> result = rule.apply("int x = 5;", mockIterator);

            // Assert
            assertThat(result).isEmpty();
            verifyNoInteractions(mockIterator);
        }

        @Test
        @DisplayName("Should not detect comment in empty line")
        void testApply_EmptyLine_NotDetected() {
            // Act
            Optional<TextElement> result = rule.apply("", mockIterator);

            // Assert
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should not detect comment with single asterisk")
        void testApply_SingleAsterisk_NotDetected() {
            // Act
            Optional<TextElement> result = rule.apply("int result = x * y;", mockIterator);

            // Assert
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should not detect comment with slash only")
        void testApply_SlashOnly_NotDetected() {
            // Act
            Optional<TextElement> result = rule.apply("int result = x / y;", mockIterator);

            // Assert
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should not detect inline comment markers")
        void testApply_InlineCommentMarkers_NotDetected() {
            // Act
            Optional<TextElement> result = rule.apply("// This is an inline comment", mockIterator);

            // Assert
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should throw exception when comment is not closed and iterator is exhausted")
        void testApply_UnClosedCommentEmptyIterator_ThrowsException() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(false);

            // Act & Assert
            assertThatThrownBy(() -> rule.apply("/* Unclosed comment", mockIterator))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Could not find end of multi-line comment");
        }

        @Test
        @DisplayName("Should throw exception when comment spans multiple lines but never closes")
        void testApply_NeverClosingComment_ThrowsException() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(true, true, false);
            when(mockIterator.next()).thenReturn("line 2", "line 3");

            // Act & Assert
            assertThatThrownBy(() -> rule.apply("/* Never ending comment", mockIterator))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Could not find end of multi-line comment");
        }

        @Test
        @DisplayName("Should handle null line parameter")
        void testApply_NullLine_ThrowsException() {
            // Act & Assert
            assertThatThrownBy(() -> rule.apply(null, mockIterator))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle null iterator when comment is single line")
        void testApply_NullIteratorSingleLine_WorksCorrectly() {
            // Act
            Optional<TextElement> result = rule.apply("/* Single line comment */", null);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo("Single line comment");
        }

        @Test
        @DisplayName("Should handle null iterator when comment is multi-line")
        void testApply_NullIteratorMultiLine_ThrowsException() {
            // Act & Assert
            assertThatThrownBy(() -> rule.apply("/* Multi line comment", null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle iterator exceptions gracefully")
        void testApply_IteratorException_PropagatesException() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(true);
            when(mockIterator.next()).thenThrow(new RuntimeException("Iterator error"));

            // Act & Assert
            assertThatThrownBy(() -> rule.apply("/* Multi line comment", mockIterator))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Iterator error");
        }
    }

    @Nested
    @DisplayName("Content Processing Tests")
    class ContentProcessingTests {

        @Test
        @DisplayName("Should trim lines correctly")
        void testApply_LineTrimming_HandledCorrectly() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(true);
            when(mockIterator.next()).thenReturn("   trimmed content   */");

            // Act
            Optional<TextElement> result = rule.apply("/*   first line   ", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo("first line\ntrimmed content");
        }

        @Test
        @DisplayName("Should preserve special characters in content")
        void testApply_SpecialCharacters_PreservedCorrectly() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(true);
            when(mockIterator.next()).thenReturn(" * Special: @#$%^&*()+={}[]|\\:;\"'<>? */");

            // Act
            Optional<TextElement> result = rule.apply("/* Unicode: \u2603 \u03B1\u03B2", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText())
                    .isEqualTo("Unicode: \u2603 \u03B1\u03B2\n* Special: @#$%^&*()+={}[]|\\:;\"'<>?");
        }

        @Test
        @DisplayName("Should handle very long multi-line comments")
        void testApply_VeryLongComment_HandledCorrectly() {
            // Arrange
            String longContent = "Very long line ".repeat(100);
            when(mockIterator.hasNext()).thenReturn(true);
            when(mockIterator.next()).thenReturn(longContent + " */");

            // Act
            Optional<TextElement> result = rule.apply("/* Start of long comment", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).contains("Start of long comment");
            assertThat(result.get().getText()).contains("Very long line");
        }

        @Test
        @DisplayName("Should join lines with newline character")
        void testApply_LineJoining_UsesNewlines() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(true, true);
            when(mockIterator.next()).thenReturn("line2", "line3 */");

            // Act
            Optional<TextElement> result = rule.apply("/* line1", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo("line1\nline2\nline3");
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle comment marker at end of line")
        void testApply_CommentMarkerAtEndOfLine_HandledCorrectly() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(true);
            when(mockIterator.next()).thenReturn("*/");

            // Act
            Optional<TextElement> result = rule.apply("code /*", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo("\n");
        }

        @Test
        @DisplayName("Should handle multiple comment start markers")
        void testApply_MultipleStartMarkers_FindsFirst() {
            // Act
            Optional<TextElement> result = rule.apply("/* first /* second */", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo("first /* second");
        }

        @Test
        @DisplayName("Should handle comment ending in first line after content")
        void testApply_EndInFirstLineAfterContent_HandledCorrectly() {
            // Act
            Optional<TextElement> result = rule.apply("start /* comment content */ end", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo("comment content");
        }

        @Test
        @DisplayName("Should handle whitespace-only comment content")
        void testApply_WhitespaceOnlyContent_HandledCorrectly() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(true);
            when(mockIterator.next()).thenReturn("   */");

            // Act
            Optional<TextElement> result = rule.apply("/*   ", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo("\n");
        }

        @Test
        @DisplayName("Should handle comment with only asterisks")
        void testApply_OnlyAsterisks_HandledCorrectly() {
            // Act
            Optional<TextElement> result = rule.apply("/* *** */", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo("***");
        }
    }

    @Nested
    @DisplayName("Real-World Scenarios")
    class RealWorldScenarios {

        @Test
        @DisplayName("Should handle typical C-style block comment")
        void testApply_CStyleBlockComment_HandledCorrectly() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(true, true, true);
            when(mockIterator.next()).thenReturn(
                    " * Function: calculateSum",
                    " * Purpose: Adds two integers",
                    " */");

            // Act
            Optional<TextElement> result = rule.apply("/*", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo("\n* Function: calculateSum\n* Purpose: Adds two integers\n");
        }

        @Test
        @DisplayName("Should handle license header comment")
        void testApply_LicenseHeader_HandledCorrectly() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(true, true, true);
            when(mockIterator.next()).thenReturn(
                    " * Copyright 2023 Company",
                    " * Licensed under Apache 2.0",
                    " */");

            // Act
            Optional<TextElement> result = rule.apply("/**", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).contains("Copyright 2023 Company");
            assertThat(result.get().getText()).contains("Licensed under Apache 2.0");
        }

        @Test
        @DisplayName("Should handle code documentation comment")
        void testApply_CodeDocumentation_HandledCorrectly() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(true, true, true, true);
            when(mockIterator.next()).thenReturn(
                    " * Calculates the factorial of a number.",
                    " * @param n The number to calculate factorial for",
                    " * @return The factorial value",
                    " */");

            // Act
            Optional<TextElement> result = rule.apply("/**", mockIterator);

            // Assert
            assertThat(result).isPresent();
            String text = result.get().getText();
            assertThat(text).contains("Calculates the factorial");
            assertThat(text).contains("@param n");
            assertThat(text).contains("@return The factorial");
        }

        @Test
        @DisplayName("Should handle comment with code snippets")
        void testApply_CommentWithCodeSnippets_HandledCorrectly() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(true, true);
            when(mockIterator.next()).thenReturn(
                    " * Example: int x = func(5, 10);",
                    " */");

            // Act
            Optional<TextElement> result = rule.apply("/*", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).contains("Example: int x = func(5, 10);");
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should handle many multi-line comments efficiently")
        void testPerformance_ManyComments_EfficientProcessing() {
            // Act & Assert
            assertThatCode(() -> {
                for (int i = 0; i < 1000; i++) {
                    String line = "/* Comment " + i + " */";
                    Optional<TextElement> result = rule.apply(line, mockIterator);
                    assertThat(result).isPresent();
                    assertThat(result.get().getText()).isEqualTo("Comment " + i);
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
                            for (int j = 0; j < 100; j++) {
                                String line = "/* Thread " + i + " comment " + j + " */";
                                Optional<TextElement> result = rule.apply(line, mockIterator);
                                assertThat(result).isPresent();
                                assertThat(result.get().getType()).isEqualTo(TextElementType.MULTILINE_COMMENT);
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
            Optional<TextElement> result = rule.apply("/* Integration test */", mockIterator);

            // Assert
            assertThat(result).isPresent();
            TextElement element = result.get();

            // Should work through TextElement interface
            assertThat(element.getType()).isEqualTo(TextElementType.MULTILINE_COMMENT);
            assertThat(element.getText()).isEqualTo("Integration test");

            // Should be a GenericTextElement instance (from factory method)
            assertThat(element).isInstanceOf(GenericTextElement.class);
        }

        @Test
        @DisplayName("Should work with realistic iterator implementation")
        void testIntegration_RealisticIterator_WorksCorrectly() {
            // Arrange
            List<String> lines = List.of(
                    " * Line 1 of comment",
                    " * Line 2 of comment",
                    " */");
            Iterator<String> realIterator = lines.iterator();

            // Act
            Optional<TextElement> result = rule.apply("/*", realIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo("\n* Line 1 of comment\n* Line 2 of comment\n");

            // Iterator should be properly consumed
            assertThat(realIterator.hasNext()).isFalse();
        }
    }
}
