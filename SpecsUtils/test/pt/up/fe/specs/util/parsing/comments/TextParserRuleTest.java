package pt.up.fe.specs.util.parsing.comments;

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
 * Comprehensive test suite for TextParserRule interface.
 * Tests interface contract, implementation behavior, and edge cases.
 * 
 * @author Generated Tests
 */
@DisplayName("TextParserRule Tests")
@ExtendWith(MockitoExtension.class)
public class TextParserRuleTest {

    @Mock
    private Iterator<String> mockIterator;

    @Nested
    @DisplayName("Interface Contract Tests")
    class InterfaceContractTests {

        @Test
        @DisplayName("Should be a functional interface with correct method signature")
        void testTextParserRule_InterfaceStructure_CorrectDefinition() {
            // Act & Assert - Verify interface has expected method
            assertThat(TextParserRule.class.isInterface()).isTrue();

            // Check method signature exists
            assertThatCode(() -> {
                TextParserRule.class.getMethod("apply", String.class, Iterator.class);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should have correct method return type")
        void testTextParserRule_MethodReturnType_IsOptionalTextElement() throws NoSuchMethodException {
            // Act & Assert
            var method = TextParserRule.class.getMethod("apply", String.class, Iterator.class);
            assertThat(method.getReturnType()).isEqualTo(Optional.class);
        }

        @Test
        @DisplayName("Should have correct method parameter types")
        void testTextParserRule_MethodParameters_CorrectTypes() throws NoSuchMethodException {
            // Act & Assert
            var method = TextParserRule.class.getMethod("apply", String.class, Iterator.class);
            var paramTypes = method.getParameterTypes();

            assertThat(paramTypes).hasSize(2);
            assertThat(paramTypes[0]).isEqualTo(String.class);
            assertThat(paramTypes[1]).isEqualTo(Iterator.class);
        }

        @Test
        @DisplayName("Should support lambda implementations")
        void testTextParserRule_LambdaImplementation_WorksCorrectly() {
            // Arrange
            TextParserRule rule = (line, iterator) -> {
                if (line.startsWith("//")) {
                    return Optional.of(new GenericTextElement(TextElementType.INLINE_COMMENT, line));
                }
                return Optional.empty();
            };

            // Act
            Optional<TextElement> result1 = rule.apply("// Comment", mockIterator);
            Optional<TextElement> result2 = rule.apply("Not a comment", mockIterator);

            // Assert
            assertThat(result1).isPresent();
            assertThat(result1.get().type()).isEqualTo(TextElementType.INLINE_COMMENT);
            assertThat(result1.get().text()).isEqualTo("// Comment");
            assertThat(result2).isEmpty();
        }
    }

    @Nested
    @DisplayName("Single Line Rule Tests")
    class SingleLineRuleTests {

        @Test
        @DisplayName("Should process single line correctly")
        void testSingleLineRule_ValidLine_ProcessedCorrectly() {
            // Arrange
            TextParserRule rule = (line, iterator) -> {
                if (line.trim().startsWith("#pragma")) {
                    return Optional.of(new GenericTextElement(TextElementType.PRAGMA, line));
                }
                return Optional.empty();
            };

            // Act
            Optional<TextElement> result = rule.apply("#pragma once", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().type()).isEqualTo(TextElementType.PRAGMA);
            assertThat(result.get().text()).isEqualTo("#pragma once");

            // Verify iterator was not used for single line rule
            verifyNoInteractions(mockIterator);
        }

        @Test
        @DisplayName("Should return empty for non-matching lines")
        void testSingleLineRule_NonMatchingLine_ReturnsEmpty() {
            // Arrange
            TextParserRule rule = (line, iterator) -> {
                if (line.startsWith("/*")) {
                    return Optional.of(new GenericTextElement(TextElementType.MULTILINE_COMMENT, line));
                }
                return Optional.empty();
            };

            // Act
            Optional<TextElement> result = rule.apply("Regular code line", mockIterator);

            // Assert
            assertThat(result).isEmpty();
            verifyNoInteractions(mockIterator);
        }

        @Test
        @DisplayName("Should handle various single line patterns")
        void testSingleLineRule_VariousPatterns_AllHandled() {
            // Arrange
            TextParserRule inlineCommentRule = (line, iterator) -> {
                if (line.trim().startsWith("//")) {
                    return Optional.of(new GenericTextElement(TextElementType.INLINE_COMMENT, line));
                }
                return Optional.empty();
            };

            // Act & Assert
            var testCases = List.of(
                    "// Simple comment",
                    "    // Indented comment",
                    "//",
                    "// Comment with special chars: @#$%^&*()");

            testCases.forEach(testLine -> {
                Optional<TextElement> result = inlineCommentRule.apply(testLine, mockIterator);
                assertThat(result).isPresent();
                assertThat(result.get().type()).isEqualTo(TextElementType.INLINE_COMMENT);
                assertThat(result.get().text()).isEqualTo(testLine);
            });
        }
    }

    @Nested
    @DisplayName("Multi-Line Rule Tests")
    class MultiLineRuleTests {

        @Test
        @DisplayName("Should process multi-line content correctly")
        void testMultiLineRule_ValidMultiLine_ProcessedCorrectly() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(true, true, false);
            when(mockIterator.next()).thenReturn(" * Comment line 2", " */");

            TextParserRule multiLineRule = (line, iterator) -> {
                if (line.trim().startsWith("/*")) {
                    StringBuilder comment = new StringBuilder(line);

                    while (iterator.hasNext()) {
                        String nextLine = iterator.next();
                        comment.append("\n").append(nextLine);
                        if (nextLine.trim().endsWith("*/")) {
                            break;
                        }
                    }

                    return Optional.of(new GenericTextElement(TextElementType.MULTILINE_COMMENT, comment.toString()));
                }
                return Optional.empty();
            };

            // Act
            Optional<TextElement> result = multiLineRule.apply("/* Comment line 1", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().type()).isEqualTo(TextElementType.MULTILINE_COMMENT);
            assertThat(result.get().text()).isEqualTo("/* Comment line 1\n * Comment line 2\n */");

            // Verify iterator was used correctly
            verify(mockIterator, times(2)).hasNext();
            verify(mockIterator, times(2)).next();
        }

        @Test
        @DisplayName("Should handle iterator correctly for multi-line rules")
        void testMultiLineRule_IteratorUsage_HandledCorrectly() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(true, false);
            when(mockIterator.next()).thenReturn("continuation line");

            TextParserRule rule = (line, iterator) -> {
                if (line.startsWith("START")) {
                    StringBuilder content = new StringBuilder(line);

                    if (iterator.hasNext()) {
                        content.append("\n").append(iterator.next());
                    }

                    return Optional.of(new GenericTextElement(TextElementType.PRAGMA_MACRO, content.toString()));
                }
                return Optional.empty();
            };

            // Act
            Optional<TextElement> result = rule.apply("START macro", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().text()).isEqualTo("START macro\ncontinuation line");

            // Verify iterator was consumed correctly
            verify(mockIterator).hasNext();
            verify(mockIterator).next();
        }

        @Test
        @DisplayName("Should handle empty iterator for multi-line rules")
        void testMultiLineRule_EmptyIterator_HandledGracefully() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(false);

            TextParserRule rule = (line, iterator) -> {
                if (line.startsWith("#define")) {
                    StringBuilder macro = new StringBuilder(line);

                    while (iterator.hasNext() && line.endsWith("\\")) {
                        String nextLine = iterator.next();
                        macro.append("\n").append(nextLine);
                        line = nextLine; // Update for next iteration check
                    }

                    return Optional.of(new GenericTextElement(TextElementType.PRAGMA_MACRO, macro.toString()));
                }
                return Optional.empty();
            };

            // Act
            Optional<TextElement> result = rule.apply("#define SIMPLE_MACRO", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().text()).isEqualTo("#define SIMPLE_MACRO");
            verify(mockIterator).hasNext();
            verify(mockIterator, never()).next();
        }
    }

    @Nested
    @DisplayName("Rule Combination Tests")
    class RuleCombinationTests {

        @Test
        @DisplayName("Should support chaining multiple rules")
        void testRuleCombination_ChainedRules_WorkCorrectly() {
            // Arrange
            TextParserRule inlineRule = (line, iterator) -> line.startsWith("//")
                    ? Optional.of(new GenericTextElement(TextElementType.INLINE_COMMENT, line))
                    : Optional.empty();

            TextParserRule pragmaRule = (line, iterator) -> line.startsWith("#pragma")
                    ? Optional.of(new GenericTextElement(TextElementType.PRAGMA, line))
                    : Optional.empty();

            TextParserRule combinedRule = (line, iterator) -> {
                Optional<TextElement> result = inlineRule.apply(line, iterator);
                if (result.isPresent()) {
                    return result;
                }
                return pragmaRule.apply(line, iterator);
            };

            // Act & Assert
            Optional<TextElement> commentResult = combinedRule.apply("// Comment", mockIterator);
            assertThat(commentResult).isPresent();
            assertThat(commentResult.get().type()).isEqualTo(TextElementType.INLINE_COMMENT);

            Optional<TextElement> pragmaResult = combinedRule.apply("#pragma once", mockIterator);
            assertThat(pragmaResult).isPresent();
            assertThat(pragmaResult.get().type()).isEqualTo(TextElementType.PRAGMA);

            Optional<TextElement> nothingResult = combinedRule.apply("regular code", mockIterator);
            assertThat(nothingResult).isEmpty();
        }

        @Test
        @DisplayName("Should support rule composition")
        void testRuleCombination_RuleComposition_WorksCorrectly() {
            // Arrange
            TextParserRule baseRule = (line, iterator) -> {
                if (line.trim().isEmpty()) {
                    return Optional.empty();
                }
                // Base processing - add line number info
                return Optional.of(new GenericTextElement(TextElementType.INLINE_COMMENT, "processed: " + line));
            };

            TextParserRule enhancedRule = (line, iterator) -> {
                Optional<TextElement> baseResult = baseRule.apply(line, iterator);
                if (baseResult.isPresent()) {
                    // Enhance the result
                    String enhancedText = baseResult.get().text() + " [enhanced]";
                    return Optional.of(new GenericTextElement(baseResult.get().type(), enhancedText));
                }
                return Optional.empty();
            };

            // Act
            Optional<TextElement> result = enhancedRule.apply("test line", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().text()).isEqualTo("processed: test line [enhanced]");
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle null line parameter")
        void testErrorHandling_NullLine_HandledGracefully() {
            // Arrange
            TextParserRule rule = (line, iterator) -> {
                if (line == null) {
                    return Optional.empty();
                }
                return line.startsWith("//") ? Optional.of(new GenericTextElement(TextElementType.INLINE_COMMENT, line))
                        : Optional.empty();
            };

            // Act & Assert
            assertThatCode(() -> {
                Optional<TextElement> result = rule.apply(null, mockIterator);
                assertThat(result).isEmpty();
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle null iterator parameter")
        void testErrorHandling_NullIterator_HandledGracefully() {
            // Arrange
            TextParserRule rule = (line, iterator) -> {
                // Single line rule that doesn't use iterator
                if (line.startsWith("#pragma")) {
                    return Optional.of(new GenericTextElement(TextElementType.PRAGMA, line));
                }
                return Optional.empty();
            };

            // Act & Assert
            assertThatCode(() -> {
                Optional<TextElement> result = rule.apply("#pragma once", null);
                assertThat(result).isPresent();
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle exceptions in rule logic")
        void testErrorHandling_ExceptionsInRule_HandledCorrectly() {
            // Arrange
            TextParserRule faultyRule = (line, iterator) -> {
                if (line.equals("THROW")) {
                    throw new RuntimeException("Test exception");
                }
                return Optional.of(new GenericTextElement(TextElementType.INLINE_COMMENT, line));
            };

            // Act & Assert
            assertThatThrownBy(() -> faultyRule.apply("THROW", mockIterator))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Test exception");
        }

        @Test
        @DisplayName("Should handle iterator exceptions gracefully")
        void testErrorHandling_IteratorExceptions_HandledCorrectly() {
            // Arrange
            when(mockIterator.hasNext()).thenReturn(true);
            when(mockIterator.next()).thenThrow(new RuntimeException("Iterator error"));

            TextParserRule rule = (line, iterator) -> {
                if (line.startsWith("MULTI")) {
                    try {
                        StringBuilder content = new StringBuilder(line);
                        if (iterator.hasNext()) {
                            content.append("\n").append(iterator.next());
                        }
                        return Optional
                                .of(new GenericTextElement(TextElementType.MULTILINE_COMMENT, content.toString()));
                    } catch (RuntimeException e) {
                        // Handle iterator error gracefully
                        return Optional
                                .of(new GenericTextElement(TextElementType.MULTILINE_COMMENT, line + " [error]"));
                    }
                }
                return Optional.empty();
            };

            // Act
            Optional<TextElement> result = rule.apply("MULTI line", mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().text()).isEqualTo("MULTI line [error]");
        }
    }

    @Nested
    @DisplayName("Performance and Edge Cases")
    class PerformanceEdgeCases {

        @Test
        @DisplayName("Should handle very long lines efficiently")
        void testPerformance_VeryLongLines_HandledEfficiently() {
            // Arrange
            String longLine = "// " + "x".repeat(10000);
            TextParserRule rule = (line, iterator) -> {
                if (line.startsWith("//")) {
                    return Optional.of(new GenericTextElement(TextElementType.INLINE_COMMENT, line));
                }
                return Optional.empty();
            };

            // Act & Assert
            assertThatCode(() -> {
                Optional<TextElement> result = rule.apply(longLine, mockIterator);
                assertThat(result).isPresent();
                assertThat(result.get().text()).hasSize(longLine.length());
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle empty strings correctly")
        void testEdgeCase_EmptyStrings_HandledCorrectly() {
            // Arrange
            TextParserRule rule = (line, iterator) -> {
                if (line.trim().isEmpty()) {
                    return Optional.empty(); // Skip empty lines
                }
                return Optional.of(new GenericTextElement(TextElementType.INLINE_COMMENT, line));
            };

            // Act & Assert
            Optional<TextElement> result1 = rule.apply("", mockIterator);
            Optional<TextElement> result2 = rule.apply("   ", mockIterator);
            Optional<TextElement> result3 = rule.apply("\t\n", mockIterator);

            assertThat(result1).isEmpty();
            assertThat(result2).isEmpty();
            assertThat(result3).isEmpty();
        }

        @Test
        @DisplayName("Should handle special characters correctly")
        void testEdgeCase_SpecialCharacters_HandledCorrectly() {
            // Arrange
            String specialLine = "// Unicode: \u2603 \u03B1\u03B2\u03B3 \uD83D\uDE00";
            TextParserRule rule = (line, iterator) -> {
                if (line.startsWith("//")) {
                    return Optional.of(new GenericTextElement(TextElementType.INLINE_COMMENT, line));
                }
                return Optional.empty();
            };

            // Act
            Optional<TextElement> result = rule.apply(specialLine, mockIterator);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().text()).isEqualTo(specialLine);
        }

        @Test
        @DisplayName("Should maintain thread safety for stateless rules")
        void testPerformance_ThreadSafety_StatelessRules() {
            // Arrange
            TextParserRule statelessRule = (line, iterator) -> {
                if (line.startsWith("#")) {
                    return Optional.of(new GenericTextElement(TextElementType.PRAGMA, line));
                }
                return Optional.empty();
            };

            // Act & Assert
            assertThatCode(() -> {
                var threads = java.util.stream.IntStream.range(0, 10)
                        .mapToObj(i -> new Thread(() -> {
                            for (int j = 0; j < 100; j++) {
                                Optional<TextElement> result = statelessRule.apply("#pragma test " + j, mockIterator);
                                assertThat(result).isPresent();
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
}
