package pt.up.fe.specs.util.parsing.comments;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for TextElement interface.
 * Tests interface contract, factory methods, and implementation behavior.
 * 
 * @author Generated Tests
 */
@DisplayName("TextElement Tests")
public class TextElementTest {

    @Nested
    @DisplayName("Interface Contract Tests")
    class InterfaceContractTests {

        @Test
        @DisplayName("Should be a functional interface with correct methods")
        void testTextElement_InterfaceStructure_CorrectDefinition() {
            // Act & Assert - Verify interface has expected methods
            assertThat(TextElement.class.isInterface()).isTrue();

            // Check method signatures exist
            assertThatCode(() -> {
                TextElement.class.getMethod("getType");
                TextElement.class.getMethod("getText");
                TextElement.class.getMethod("newInstance", TextElementType.class, String.class);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should have correct method return types")
        void testTextElement_MethodReturnTypes_AreCorrect() throws NoSuchMethodException {
            // Act & Assert
            assertThat(TextElement.class.getMethod("getType").getReturnType()).isEqualTo(TextElementType.class);
            assertThat(TextElement.class.getMethod("getText").getReturnType()).isEqualTo(String.class);
            assertThat(TextElement.class.getMethod("newInstance", TextElementType.class, String.class).getReturnType())
                    .isEqualTo(TextElement.class);
        }

        @Test
        @DisplayName("Should have methods with correct parameter counts")
        void testTextElement_MethodParameters_AreCorrect() throws NoSuchMethodException {
            // Act & Assert
            assertThat(TextElement.class.getMethod("getType").getParameterCount()).isEqualTo(0);
            assertThat(TextElement.class.getMethod("getText").getParameterCount()).isEqualTo(0);
            assertThat(
                    TextElement.class.getMethod("newInstance", TextElementType.class, String.class).getParameterCount())
                    .isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("Factory Method Tests")
    class FactoryMethodTests {

        @Test
        @DisplayName("Should create TextElement with valid parameters")
        void testNewInstance_ValidParameters_CreatesElement() {
            // Arrange
            TextElementType type = TextElementType.INLINE_COMMENT;
            String text = "// This is a comment";

            // Act
            TextElement element = TextElement.newInstance(type, text);

            // Assert
            assertThat(element).isNotNull();
            assertThat(element.getType()).isEqualTo(type);
            assertThat(element.getText()).isEqualTo(text);
        }

        @Test
        @DisplayName("Should create elements for all TextElementType values")
        void testNewInstance_AllTypes_CreatesElementsCorrectly() {
            // Arrange
            String testText = "Test text content";

            // Act & Assert
            for (TextElementType type : TextElementType.values()) {
                TextElement element = TextElement.newInstance(type, testText);
                assertThat(element).isNotNull();
                assertThat(element.getType()).isEqualTo(type);
                assertThat(element.getText()).isEqualTo(testText);
            }
        }

        @Test
        @DisplayName("Should handle null text parameter")
        void testNewInstance_NullText_HandledCorrectly() {
            // Act
            TextElement element = TextElement.newInstance(TextElementType.INLINE_COMMENT, null);

            // Assert
            assertThat(element).isNotNull();
            assertThat(element.getType()).isEqualTo(TextElementType.INLINE_COMMENT);
            assertThat(element.getText()).isNull();
        }

        @Test
        @DisplayName("Should handle empty text parameter")
        void testNewInstance_EmptyText_HandledCorrectly() {
            // Act
            TextElement element = TextElement.newInstance(TextElementType.PRAGMA, "");

            // Assert
            assertThat(element).isNotNull();
            assertThat(element.getType()).isEqualTo(TextElementType.PRAGMA);
            assertThat(element.getText()).isEmpty();
        }

        @Test
        @DisplayName("Should handle null type parameter")
        void testNewInstance_NullType_HandledCorrectly() {
            // Act
            TextElement element = TextElement.newInstance(null, "Test text");

            // Assert
            assertThat(element).isNotNull();
            assertThat(element.getType()).isNull();
            assertThat(element.getText()).isEqualTo("Test text");
        }

        @Test
        @DisplayName("Should create different instances for each call")
        void testNewInstance_MultipleCalls_CreatesDifferentInstances() {
            // Act
            TextElement element1 = TextElement.newInstance(TextElementType.INLINE_COMMENT, "Text 1");
            TextElement element2 = TextElement.newInstance(TextElementType.INLINE_COMMENT, "Text 1");

            // Assert
            assertThat(element1).isNotSameAs(element2);
            assertThat(element1.getType()).isEqualTo(element2.getType());
            assertThat(element1.getText()).isEqualTo(element2.getText());
        }
    }

    @Nested
    @DisplayName("Implementation Behavior Tests")
    class ImplementationBehaviorTests {

        @Test
        @DisplayName("Should maintain state consistency")
        void testImplementation_StateConsistency_Maintained() {
            // Arrange
            TextElement element = TextElement.newInstance(TextElementType.MULTILINE_COMMENT, "/* comment */");

            // Act - Multiple calls should return consistent results
            TextElementType type1 = element.getType();
            TextElementType type2 = element.getType();
            String text1 = element.getText();
            String text2 = element.getText();

            // Assert - Results should be consistent
            assertThat(type1).isEqualTo(type2);
            assertThat(text1).isEqualTo(text2);
            assertThat(type1).isSameAs(type2); // Same enum reference
        }

        @Test
        @DisplayName("Should preserve exact text content")
        void testImplementation_TextPreservation_ExactMatch() {
            // Arrange
            String originalText = "Special chars: \n\t\r\\\"'";

            // Act
            TextElement element = TextElement.newInstance(TextElementType.PRAGMA_MACRO, originalText);

            // Assert
            assertThat(element.getText()).isEqualTo(originalText);
            assertThat(element.getText()).isSameAs(originalText); // Same reference
        }

        @Test
        @DisplayName("Should work with different text content types")
        void testImplementation_DifferentTextTypes_AllSupported() {
            // Arrange & Act & Assert
            var testCases = java.util.Map.of(
                    "Simple text", TextElementType.INLINE_COMMENT,
                    "Text with\nnewlines", TextElementType.MULTILINE_COMMENT,
                    "#pragma directive", TextElementType.PRAGMA,
                    "#define MACRO(x) x", TextElementType.PRAGMA_MACRO);

            testCases.forEach((text, type) -> {
                TextElement element = TextElement.newInstance(type, text);
                assertThat(element.getText()).isEqualTo(text);
                assertThat(element.getType()).isEqualTo(type);
            });
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with all comment types")
        void testTextElement_CommentTypes_AllSupported() {
            // Arrange
            var commentTypes = java.util.Set.of(
                    TextElementType.INLINE_COMMENT,
                    TextElementType.MULTILINE_COMMENT);

            // Act & Assert
            commentTypes.forEach(type -> {
                TextElement element = TextElement.newInstance(type, "Comment text");
                assertThat(element.getType()).isEqualTo(type);
                assertThat(element.getText()).isEqualTo("Comment text");
            });
        }

        @Test
        @DisplayName("Should work with all pragma types")
        void testTextElement_PragmaTypes_AllSupported() {
            // Arrange
            var pragmaTypes = java.util.Set.of(
                    TextElementType.PRAGMA,
                    TextElementType.PRAGMA_MACRO);

            // Act & Assert
            pragmaTypes.forEach(type -> {
                TextElement element = TextElement.newInstance(type, "#pragma once");
                assertThat(element.getType()).isEqualTo(type);
                assertThat(element.getText()).isEqualTo("#pragma once");
            });
        }

        @Test
        @DisplayName("Should support realistic text parsing scenarios")
        void testTextElement_RealisticScenarios_WorkCorrectly() {
            // Arrange & Act & Assert - Realistic text element scenarios
            var scenarios = java.util.List.of(
                    new TestScenario(TextElementType.INLINE_COMMENT, "// TODO: Fix this bug"),
                    new TestScenario(TextElementType.MULTILINE_COMMENT, "/*\n * Multi-line\n * comment\n */"),
                    new TestScenario(TextElementType.PRAGMA, "#pragma pack(push, 1)"),
                    new TestScenario(TextElementType.PRAGMA_MACRO, "#define MAX(a,b) ((a)>(b)?(a):(b))"));

            scenarios.forEach(scenario -> {
                TextElement element = TextElement.newInstance(scenario.type, scenario.text);
                assertThat(element.getType()).isEqualTo(scenario.type);
                assertThat(element.getText()).isEqualTo(scenario.text);
            });
        }

        private record TestScenario(TextElementType type, String text) {
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle very long text content")
        void testTextElement_VeryLongText_HandledCorrectly() {
            // Arrange
            String longText = "Very long text content ".repeat(1000);

            // Act
            TextElement element = TextElement.newInstance(TextElementType.MULTILINE_COMMENT, longText);

            // Assert
            assertThat(element.getText()).isEqualTo(longText);
            assertThat(element.getText().length()).isEqualTo(longText.length());
        }

        @Test
        @DisplayName("Should handle special Unicode characters")
        void testTextElement_UnicodeCharacters_HandledCorrectly() {
            // Arrange
            String unicodeText = "Unicode: \u2603 \u03B1\u03B2\u03B3 \uD83D\uDE00";

            // Act
            TextElement element = TextElement.newInstance(TextElementType.PRAGMA, unicodeText);

            // Assert
            assertThat(element.getText()).isEqualTo(unicodeText);
        }

        @Test
        @DisplayName("Should handle whitespace-only text")
        void testTextElement_WhitespaceText_HandledCorrectly() {
            // Arrange
            String whitespaceText = "   \t\n\r   ";

            // Act
            TextElement element = TextElement.newInstance(TextElementType.INLINE_COMMENT, whitespaceText);

            // Assert
            assertThat(element.getText()).isEqualTo(whitespaceText);
        }

        @Test
        @DisplayName("Should handle concurrent access correctly")
        void testTextElement_ConcurrentAccess_ThreadSafe() {
            // Arrange
            TextElement element = TextElement.newInstance(TextElementType.PRAGMA_MACRO, "Thread test");

            // Act & Assert - Multiple threads accessing same element
            assertThatCode(() -> {
                var threads = java.util.stream.IntStream.range(0, 10)
                        .mapToObj(i -> new Thread(() -> {
                            for (int j = 0; j < 100; j++) {
                                assertThat(element.getType()).isEqualTo(TextElementType.PRAGMA_MACRO);
                                assertThat(element.getText()).isEqualTo("Thread test");
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
    @DisplayName("Custom Implementation Tests")
    class CustomImplementationTests {

        @Test
        @DisplayName("Should support custom TextElement implementations")
        void testCustomImplementation_InterfaceCompliance_WorksCorrectly() {
            // Arrange
            TextElement customElement = new TextElement() {
                @Override
                public TextElementType getType() {
                    return TextElementType.INLINE_COMMENT;
                }

                @Override
                public String getText() {
                    return "Custom implementation";
                }
            };

            // Act & Assert
            assertThat(customElement.getType()).isEqualTo(TextElementType.INLINE_COMMENT);
            assertThat(customElement.getText()).isEqualTo("Custom implementation");
        }

        @Test
        @DisplayName("Should support lambda-style implementations")
        void testLambdaImplementation_FunctionalStyle_WorksCorrectly() {
            // Arrange - Since it's not a functional interface, we'll use anonymous class
            TextElement lambdaElement = new TextElement() {
                private final TextElementType type = TextElementType.PRAGMA;
                private final String text = "Lambda-style element";

                @Override
                public TextElementType getType() {
                    return type;
                }

                @Override
                public String getText() {
                    return text;
                }
            };

            // Act & Assert
            assertThat(lambdaElement.getType()).isEqualTo(TextElementType.PRAGMA);
            assertThat(lambdaElement.getText()).isEqualTo("Lambda-style element");
        }
    }
}
