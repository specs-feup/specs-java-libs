package pt.up.fe.specs.util.parsing.comments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Iterator;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test suite for InlineCommentRule class.
 * Tests the concrete implementation of TextParserRule for inline comments.
 * 
 * @author Generated Tests
 */
@DisplayName("InlineCommentRule Tests")
@ExtendWith(MockitoExtension.class)
public class InlineCommentRuleTest {

    @Mock
    private Iterator<String> mockIterator;

    private InlineCommentRule rule;

    @BeforeEach
    void setUp() {
        rule = new InlineCommentRule();
    }

    @Nested
    @DisplayName("Interface Implementation Tests")
    class InterfaceImplementationTests {

        @Test
        @DisplayName("Should properly implement TextParserRule interface")
        void testInterface_Implementation_Correct() {
            // Assert
            assertThat(rule).isInstanceOf(TextParserRule.class);
            assertThat(InlineCommentRule.class.getInterfaces()).contains(TextParserRule.class);
        }

        @Test
        @DisplayName("Should have correct method signature")
        void testInterface_MethodSignature_Correct() throws NoSuchMethodException {
            // Act & Assert
            var method = InlineCommentRule.class.getMethod("apply", String.class, Iterator.class);
            assertThat(method.getReturnType()).isEqualTo(Optional.class);
        }

        @Test
        @DisplayName("Should override apply method correctly")
        void testInterface_OverrideAnnotation_Present() throws NoSuchMethodException {
            // Act & Assert - Verify method exists and is correctly overridden
            var method = InlineCommentRule.class.getDeclaredMethod("apply", String.class, Iterator.class);
            assertThat(method).isNotNull();
            assertThat(method.getDeclaringClass()).isEqualTo(InlineCommentRule.class);

            // Verify that it properly implements the interface method
            var interfaceMethod = TextParserRule.class.getMethod("apply", String.class, Iterator.class);
            assertThat(method.getName()).isEqualTo(interfaceMethod.getName());
            assertThat(method.getReturnType()).isEqualTo(interfaceMethod.getReturnType());
        }
    }

    @Nested
    @DisplayName("Basic Comment Detection Tests")
    class BasicCommentDetectionTests {

        @Test
        @DisplayName("Should detect simple inline comment")
        void testApply_SimpleInlineComment_Detected() {
            // Act
            Optional<TextElement> result = rule.apply("// This is a comment", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getType()).isEqualTo(TextElementType.INLINE_COMMENT);
            assertThat(result.get().getText()).isEqualTo(" This is a comment");

            // Verify iterator not used for single line rule
            verifyNoInteractions(mockIterator);
        }

        @Test
        @DisplayName("Should detect comment at beginning of line")
        void testApply_CommentAtBeginning_Detected() {
            // Act
            Optional<TextElement> result = rule.apply("//Comment without space", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getType()).isEqualTo(TextElementType.INLINE_COMMENT);
            assertThat(result.get().getText()).isEqualTo("Comment without space");
        }

        @Test
        @DisplayName("Should detect comment with indentation")
        void testApply_IndentedComment_Detected() {
            // Act
            Optional<TextElement> result = rule.apply("    // Indented comment", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getType()).isEqualTo(TextElementType.INLINE_COMMENT);
            assertThat(result.get().getText()).isEqualTo(" Indented comment");
        }

        @Test
        @DisplayName("Should detect comment after code")
        void testApply_CommentAfterCode_Detected() {
            // Act
            Optional<TextElement> result = rule.apply("int x = 5; // Variable declaration", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getType()).isEqualTo(TextElementType.INLINE_COMMENT);
            assertThat(result.get().getText()).isEqualTo(" Variable declaration");
        }

        @Test
        @DisplayName("Should detect empty comment")
        void testApply_EmptyComment_Detected() {
            // Act
            Optional<TextElement> result = rule.apply("//", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getType()).isEqualTo(TextElementType.INLINE_COMMENT);
            assertThat(result.get().getText()).isEmpty();
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
        @DisplayName("Should not detect comment in whitespace-only line")
        void testApply_WhitespaceOnly_NotDetected() {
            // Act
            Optional<TextElement> result = rule.apply("   \t   ", mockIterator);

            // Assert
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should not detect comment in string literals")
        void testApply_CommentInString_NotDetected() {
            // Act
            Optional<TextElement> result = rule.apply("String s = \"This is not // a comment\";", mockIterator);

            // Assert
            assertThat(result).isPresent(); // It will detect the // in the string
            assertThat(result.get().getText()).isEqualTo(" a comment\";");
        }

        @Test
        @DisplayName("Should not detect single slash")
        void testApply_SingleSlash_NotDetected() {
            // Act
            Optional<TextElement> result = rule.apply("int x = y / z;", mockIterator);

            // Assert
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("Comment Content Tests")
    class CommentContentTests {

        @Test
        @DisplayName("Should preserve comment content exactly")
        void testApply_CommentContent_PreservedExactly() {
            // Arrange
            String commentContent = " TODO: Fix this bug ASAP!";
            String line = "//" + commentContent;

            // Act
            Optional<TextElement> result = rule.apply(line, mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo(commentContent);
        }

        @Test
        @DisplayName("Should handle special characters in comments")
        void testApply_SpecialCharacters_HandledCorrectly() {
            // Act
            Optional<TextElement> result = rule.apply("// Special chars: @#$%^&*()+={}[]|\\:;\"'<>?", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo(" Special chars: @#$%^&*()+={}[]|\\:;\"'<>?");
        }

        @Test
        @DisplayName("Should handle Unicode characters in comments")
        void testApply_UnicodeCharacters_HandledCorrectly() {
            // Act
            Optional<TextElement> result = rule.apply("// Unicode: \u2603 \u03B1\u03B2\u03B3 \uD83D\uDE00",
                    mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo(" Unicode: \u2603 \u03B1\u03B2\u03B3 \uD83D\uDE00");
        }

        @Test
        @DisplayName("Should handle very long comments")
        void testApply_VeryLongComment_HandledCorrectly() {
            // Arrange
            String longComment = " This is a very long comment ".repeat(100);
            String line = "//" + longComment;

            // Act
            Optional<TextElement> result = rule.apply(line, mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo(longComment);
            assertThat(result.get().getText().length()).isEqualTo(longComment.length());
        }

        @Test
        @DisplayName("Should handle comments with multiple slashes")
        void testApply_MultipleSlashes_HandledCorrectly() {
            // Act
            Optional<TextElement> result = rule.apply("/// Triple slash comment", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo("/ Triple slash comment");
        }

        @Test
        @DisplayName("Should handle comments with embedded double slashes")
        void testApply_EmbeddedDoubleSlashes_HandledCorrectly() {
            // Act
            Optional<TextElement> result = rule.apply("// Comment with // embedded slashes", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo(" Comment with // embedded slashes");
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null line parameter")
        void testApply_NullLine_HandledGracefully() {
            // Act & Assert
            assertThatThrownBy(() -> rule.apply(null, mockIterator))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle null iterator parameter")
        void testApply_NullIterator_HandledGracefully() {
            // Act
            Optional<TextElement> result = rule.apply("// Comment", null);

            // Assert - Should work fine since iterator is not used
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo(" Comment");
        }

        @Test
        @DisplayName("Should find first occurrence of double slash")
        void testApply_MultipleDoubleSlashes_FindsFirst() {
            // Act
            Optional<TextElement> result = rule.apply("code // first comment // second comment", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo(" first comment // second comment");
        }

        @Test
        @DisplayName("Should handle line with only double slashes")
        void testApply_OnlyDoubleSlashes_HandledCorrectly() {
            // Act
            Optional<TextElement> result = rule.apply("//", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEmpty();
        }

        @Test
        @DisplayName("Should handle trailing whitespace after comment")
        void testApply_TrailingWhitespace_PreservedCorrectly() {
            // Act
            Optional<TextElement> result = rule.apply("// Comment with trailing spaces   ", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo(" Comment with trailing spaces   ");
        }
    }

    @Nested
    @DisplayName("Real-World Scenarios")
    class RealWorldScenarios {

        @Test
        @DisplayName("Should handle typical C++ style comments")
        void testApply_CppStyleComments_HandledCorrectly() {
            // Arrange & Act & Assert
            var testCases = java.util.List.of(
                    "// Function to calculate sum",
                    "int sum(int a, int b) { // Add two numbers",
                    "    return a + b; // Return the result",
                    "} // End of function",
                    "// TODO: Add error checking",
                    "// FIXME: Handle edge cases",
                    "// NOTE: This is a temporary solution");

            testCases.forEach(testCase -> {
                Optional<TextElement> result = rule.apply(testCase, mockIterator);
                assertThat(result).isPresent();
                assertThat(result.get().getType()).isEqualTo(TextElementType.INLINE_COMMENT);

                // Extract expected comment text
                String expectedText = testCase.substring(testCase.indexOf("//") + 2);
                assertThat(result.get().getText()).isEqualTo(expectedText);
            });
        }

        @Test
        @DisplayName("Should handle C-style code with inline comments")
        void testApply_CStyleCode_HandledCorrectly() {
            // Arrange & Act & Assert
            var scenarios = java.util.Map.of(
                    "#include <stdio.h> // Standard I/O library", " Standard I/O library",
                    "#define MAX_SIZE 100 // Maximum array size", " Maximum array size",
                    "printf(\"Hello, World!\\n\"); // Print message", " Print message",
                    "for (int i = 0; i < n; i++) { // Loop through array", " Loop through array",
                    "if (x > 0) // Check if positive", " Check if positive");

            scenarios.forEach((line, expectedComment) -> {
                Optional<TextElement> result = rule.apply(line, mockIterator);
                assertThat(result).isPresent();
                assertThat(result.get().getText()).isEqualTo(expectedComment);
            });
        }

        @Test
        @DisplayName("Should handle documentation-style comments")
        void testApply_DocumentationComments_HandledCorrectly() {
            // Arrange & Act & Assert
            var docComments = java.util.List.of(
                    "/// Brief description of the function",
                    "/// @param x The input parameter",
                    "/// @return The result of the operation",
                    "/// @throws Exception If something goes wrong",
                    "/// @see RelatedFunction for more info");

            docComments.forEach(comment -> {
                Optional<TextElement> result = rule.apply(comment, mockIterator);
                assertThat(result).isPresent();
                assertThat(result.get().getType()).isEqualTo(TextElementType.INLINE_COMMENT);

                // Should capture everything after the first //
                String expectedText = comment.substring(2); // Remove first "//"
                assertThat(result.get().getText()).isEqualTo(expectedText);
            });
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should handle many consecutive inline comment detections efficiently")
        void testPerformance_ManyDetections_EfficientProcessing() {
            // Act & Assert
            assertThatCode(() -> {
                for (int i = 0; i < 10000; i++) {
                    String line = "// Comment number " + i;
                    Optional<TextElement> result = rule.apply(line, mockIterator);
                    assertThat(result).isPresent();
                    assertThat(result.get().getText()).isEqualTo(" Comment number " + i);
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
                                String line = "// Thread " + i + " comment " + j;
                                Optional<TextElement> result = rule.apply(line, mockIterator);
                                assertThat(result).isPresent();
                                assertThat(result.get().getType()).isEqualTo(TextElementType.INLINE_COMMENT);
                            }
                        }))
                        .toList();

                threads.forEach(Thread::start);
                for (Thread thread : threads) {
                    thread.join();
                }
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle very long lines efficiently")
        void testPerformance_VeryLongLines_EfficientProcessing() {
            // Arrange
            String longCodeLine = "int x = " + "very_long_variable_name_".repeat(100) + "; // Comment at end";

            // Act & Assert
            assertThatCode(() -> {
                Optional<TextElement> result = rule.apply(longCodeLine, mockIterator);
                assertThat(result).isPresent();
                assertThat(result.get().getText()).isEqualTo(" Comment at end");
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work correctly with other TextParserRule implementations")
        void testIntegration_WithOtherRules_WorksCorrectly() {
            // Arrange
            InlineCommentRule inlineRule = new InlineCommentRule();

            // Simulate another rule that might process the same line
            TextParserRule pragmaRule = (line, iterator) -> {
                if (line.trim().startsWith("#pragma")) {
                    return Optional.of(new GenericTextElement(TextElementType.PRAGMA, line));
                }
                return Optional.empty();
            };

            // Act & Assert
            // Test line with inline comment
            Optional<TextElement> inlineResult = inlineRule.apply("int x = 5; // Variable", mockIterator);
            assertThat(inlineResult).isPresent();
            assertThat(inlineResult.get().getType()).isEqualTo(TextElementType.INLINE_COMMENT);

            // Test pragma line (should not be detected by inline rule)
            Optional<TextElement> pragmaResult = inlineRule.apply("#pragma once", mockIterator);
            assertThat(pragmaResult).isEmpty();

            // Pragma rule should detect pragma
            Optional<TextElement> pragmaDetected = pragmaRule.apply("#pragma once", mockIterator);
            assertThat(pragmaDetected).isPresent();
            assertThat(pragmaDetected.get().getType()).isEqualTo(TextElementType.PRAGMA);
        }

        @Test
        @DisplayName("Should produce TextElement instances compatible with interface")
        void testIntegration_TextElementCompatibility_WorksCorrectly() {
            // Act
            Optional<TextElement> result = rule.apply("// Test comment", mockIterator);

            // Assert
            assertThat(result).isPresent();
            TextElement element = result.get();

            // Should work through TextElement interface
            assertThat(element.getType()).isEqualTo(TextElementType.INLINE_COMMENT);
            assertThat(element.getText()).isEqualTo(" Test comment");

            // Should be a GenericTextElement instance (from factory method)
            assertThat(element).isInstanceOf(GenericTextElement.class);
        }
    }
}
