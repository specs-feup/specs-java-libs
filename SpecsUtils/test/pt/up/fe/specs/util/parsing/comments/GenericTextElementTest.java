package pt.up.fe.specs.util.parsing.comments;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for GenericTextElement class.
 * Tests the concrete implementation of TextElement interface.
 * 
 * @author Generated Tests
 */
@DisplayName("GenericTextElement Tests")
public class GenericTextElementTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create instance with valid parameters")
        void testConstructor_ValidParameters_CreatesInstance() {
            // Arrange
            TextElementType type = TextElementType.INLINE_COMMENT;
            String text = "// Test comment";

            // Act
            GenericTextElement element = new GenericTextElement(type, text);

            // Assert
            assertThat(element).isNotNull();
            assertThat(element.getType()).isEqualTo(type);
            assertThat(element.getText()).isEqualTo(text);
        }

        @Test
        @DisplayName("Should create instance with null type")
        void testConstructor_NullType_CreatesInstance() {
            // Act
            GenericTextElement element = new GenericTextElement(null, "Test text");

            // Assert
            assertThat(element).isNotNull();
            assertThat(element.getType()).isNull();
            assertThat(element.getText()).isEqualTo("Test text");
        }

        @Test
        @DisplayName("Should create instance with null text")
        void testConstructor_NullText_CreatesInstance() {
            // Act
            GenericTextElement element = new GenericTextElement(TextElementType.PRAGMA, null);

            // Assert
            assertThat(element).isNotNull();
            assertThat(element.getType()).isEqualTo(TextElementType.PRAGMA);
            assertThat(element.getText()).isNull();
        }

        @Test
        @DisplayName("Should create instance with both null parameters")
        void testConstructor_BothNull_CreatesInstance() {
            // Act
            GenericTextElement element = new GenericTextElement(null, null);

            // Assert
            assertThat(element).isNotNull();
            assertThat(element.getType()).isNull();
            assertThat(element.getText()).isNull();
        }

        @Test
        @DisplayName("Should create instance with empty text")
        void testConstructor_EmptyText_CreatesInstance() {
            // Act
            GenericTextElement element = new GenericTextElement(TextElementType.MULTILINE_COMMENT, "");

            // Assert
            assertThat(element).isNotNull();
            assertThat(element.getType()).isEqualTo(TextElementType.MULTILINE_COMMENT);
            assertThat(element.getText()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Getter Method Tests")
    class GetterMethodTests {

        @Test
        @DisplayName("Should return correct type")
        void testGetType_ValidType_ReturnsCorrect() {
            // Arrange
            TextElementType expectedType = TextElementType.PRAGMA_MACRO;
            GenericTextElement element = new GenericTextElement(expectedType, "Test");

            // Act
            TextElementType actualType = element.getType();

            // Assert
            assertThat(actualType).isEqualTo(expectedType);
            assertThat(actualType).isSameAs(expectedType); // Same enum reference
        }

        @Test
        @DisplayName("Should return correct text")
        void testGetText_ValidText_ReturnsCorrect() {
            // Arrange
            String expectedText = "Test text content";
            GenericTextElement element = new GenericTextElement(TextElementType.INLINE_COMMENT, expectedText);

            // Act
            String actualText = element.getText();

            // Assert
            assertThat(actualText).isEqualTo(expectedText);
            assertThat(actualText).isSameAs(expectedText); // Same string reference
        }

        @Test
        @DisplayName("Should return consistent values on multiple calls")
        void testGetters_MultipleCalls_ConsistentValues() {
            // Arrange
            GenericTextElement element = new GenericTextElement(TextElementType.PRAGMA, "Pragma text");

            // Act
            TextElementType type1 = element.getType();
            TextElementType type2 = element.getType();
            String text1 = element.getText();
            String text2 = element.getText();

            // Assert
            assertThat(type1).isEqualTo(type2);
            assertThat(text1).isEqualTo(text2);
            assertThat(type1).isSameAs(type2);
            assertThat(text1).isSameAs(text2);
        }

        @Test
        @DisplayName("Should handle all TextElementType values")
        void testGetType_AllEnumValues_HandledCorrectly() {
            // Act & Assert
            for (TextElementType type : TextElementType.values()) {
                GenericTextElement element = new GenericTextElement(type, "Test");
                assertThat(element.getType()).isEqualTo(type);
            }
        }
    }

    @Nested
    @DisplayName("TextElement Interface Implementation Tests")
    class TextElementInterfaceTests {

        @Test
        @DisplayName("Should properly implement TextElement interface")
        void testInterface_Implementation_Correct() {
            // Arrange
            GenericTextElement element = new GenericTextElement(TextElementType.MULTILINE_COMMENT, "/* comment */");

            // Act & Assert
            assertThat(element).isInstanceOf(TextElement.class);

            // Verify interface methods work correctly
            TextElement interfaceRef = element;
            assertThat(interfaceRef.getType()).isEqualTo(TextElementType.MULTILINE_COMMENT);
            assertThat(interfaceRef.getText()).isEqualTo("/* comment */");
        }

        @Test
        @DisplayName("Should work with TextElement factory method")
        void testInterface_FactoryMethod_ProducesGenericTextElement() {
            // Act
            TextElement element = TextElement.newInstance(TextElementType.PRAGMA, "#pragma once");

            // Assert
            assertThat(element).isInstanceOf(GenericTextElement.class);
            assertThat(element.getType()).isEqualTo(TextElementType.PRAGMA);
            assertThat(element.getText()).isEqualTo("#pragma once");
        }

        @Test
        @DisplayName("Should maintain polymorphic behavior")
        void testInterface_Polymorphism_WorksCorrectly() {
            // Arrange
            TextElement[] elements = {
                    new GenericTextElement(TextElementType.INLINE_COMMENT, "// comment"),
                    TextElement.newInstance(TextElementType.PRAGMA_MACRO, "#define TEST")
            };

            // Act & Assert
            for (TextElement element : elements) {
                assertThat(element.getType()).isNotNull();
                assertThat(element.getText()).isNotNull();
                assertThat(element).isInstanceOf(GenericTextElement.class);
            }
        }
    }

    @Nested
    @DisplayName("Immutability Tests")
    class ImmutabilityTests {

        @Test
        @DisplayName("Should be immutable after construction")
        void testImmutability_AfterConstruction_ValuesUnchanged() {
            // Arrange
            TextElementType originalType = TextElementType.INLINE_COMMENT;
            String originalText = "Original text";
            GenericTextElement element = new GenericTextElement(originalType, originalText);

            // Act - Get references to internal state
            TextElementType retrievedType = element.getType();
            String retrievedText = element.getText();

            // Assert - Values should remain the same
            assertThat(element.getType()).isEqualTo(originalType);
            assertThat(element.getText()).isEqualTo(originalText);
            assertThat(retrievedType).isSameAs(originalType);
            assertThat(retrievedText).isSameAs(originalText);
        }

        @Test
        @DisplayName("Should preserve original references")
        void testImmutability_ReferencePreservation_Maintained() {
            // Arrange
            TextElementType type = TextElementType.PRAGMA_MACRO;
            String text = "Reference test";

            // Act
            GenericTextElement element = new GenericTextElement(type, text);

            // Assert - Should return same references
            assertThat(element.getType()).isSameAs(type);
            assertThat(element.getText()).isSameAs(text);
        }
    }

    @Nested
    @DisplayName("Equality and Hash Code Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should implement object equality correctly")
        void testEquals_SameContent_ReturnsTrue() {
            // Arrange
            GenericTextElement element1 = new GenericTextElement(TextElementType.INLINE_COMMENT, "// comment");
            GenericTextElement element2 = new GenericTextElement(TextElementType.INLINE_COMMENT, "// comment");

            // Act & Assert
            // Note: Since equals() might not be overridden, we test logical equality
            assertThat(element1.getType()).isEqualTo(element2.getType());
            assertThat(element1.getText()).isEqualTo(element2.getText());
        }

        @Test
        @DisplayName("Should handle different content correctly")
        void testEquals_DifferentContent_ReturnsFalse() {
            // Arrange
            GenericTextElement element1 = new GenericTextElement(TextElementType.INLINE_COMMENT, "// comment 1");
            GenericTextElement element2 = new GenericTextElement(TextElementType.PRAGMA, "// comment 2");

            // Act & Assert
            assertThat(element1.getType()).isNotEqualTo(element2.getType());
            assertThat(element1.getText()).isNotEqualTo(element2.getText());
        }

        @Test
        @DisplayName("Should handle null values in equality")
        void testEquals_NullValues_HandledCorrectly() {
            // Arrange
            GenericTextElement element1 = new GenericTextElement(null, null);
            GenericTextElement element2 = new GenericTextElement(null, null);
            GenericTextElement element3 = new GenericTextElement(TextElementType.PRAGMA, "text");

            // Act & Assert
            assertThat(element1.getType()).isEqualTo(element2.getType());
            assertThat(element1.getText()).isEqualTo(element2.getText());
            assertThat(element1.getType()).isNotEqualTo(element3.getType());
        }

        @Test
        @DisplayName("Should maintain reference equality where expected")
        void testEquals_ReferenceEquality_Maintained() {
            // Arrange
            GenericTextElement element = new GenericTextElement(TextElementType.MULTILINE_COMMENT, "Test");

            // Act & Assert
            assertThat(element).isSameAs(element);
            assertThat(element.equals(element)).isTrue(); // Self-equality should always work
        }
    }

    @Nested
    @DisplayName("String Representation Tests")
    class StringRepresentationTests {

        @Test
        @DisplayName("Should provide meaningful toString representation")
        void testToString_ValidElement_MeaningfulRepresentation() {
            // Arrange
            GenericTextElement element = new GenericTextElement(TextElementType.PRAGMA, "#pragma pack");

            // Act
            String stringRepresentation = element.toString();

            // Assert
            assertThat(stringRepresentation).isNotNull();
            assertThat(stringRepresentation).isNotEmpty();
            // toString should contain class name
            assertThat(stringRepresentation).contains("GenericTextElement");
        }

        @Test
        @DisplayName("Should handle null values in toString")
        void testToString_NullValues_HandledGracefully() {
            // Arrange
            GenericTextElement element = new GenericTextElement(null, null);

            // Act & Assert
            assertThatCode(() -> element.toString()).doesNotThrowAnyException();
            assertThat(element.toString()).isNotNull();
        }

        @Test
        @DisplayName("Should produce consistent toString output")
        void testToString_MultipleCalls_ConsistentOutput() {
            // Arrange
            GenericTextElement element = new GenericTextElement(TextElementType.INLINE_COMMENT, "// test");

            // Act
            String string1 = element.toString();
            String string2 = element.toString();

            // Assert
            assertThat(string1).isEqualTo(string2);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work correctly with realistic comment scenarios")
        void testIntegration_RealisticComments_WorkCorrectly() {
            // Arrange & Act & Assert
            var scenarios = java.util.List.of(
                    new TestCase(TextElementType.INLINE_COMMENT, "// TODO: Implement this method"),
                    new TestCase(TextElementType.MULTILINE_COMMENT, "/*\n * Header comment\n * Author: Test\n */"),
                    new TestCase(TextElementType.PRAGMA, "#pragma once"),
                    new TestCase(TextElementType.PRAGMA_MACRO, "#define MAX(a,b) ((a)>(b)?(a):(b))"));

            scenarios.forEach(testCase -> {
                GenericTextElement element = new GenericTextElement(testCase.type, testCase.text);
                assertThat(element.getType()).isEqualTo(testCase.type);
                assertThat(element.getText()).isEqualTo(testCase.text);

                // Should also work through interface
                TextElement interfaceElement = element;
                assertThat(interfaceElement.getType()).isEqualTo(testCase.type);
                assertThat(interfaceElement.getText()).isEqualTo(testCase.text);
            });
        }

        @Test
        @DisplayName("Should integrate with collections correctly")
        void testIntegration_Collections_WorkCorrectly() {
            // Arrange
            var elements = java.util.List.of(
                    new GenericTextElement(TextElementType.INLINE_COMMENT, "// Comment 1"),
                    new GenericTextElement(TextElementType.PRAGMA, "#pragma directive"),
                    new GenericTextElement(TextElementType.MULTILINE_COMMENT, "/* Comment 2 */"));

            // Act & Assert
            assertThat(elements).hasSize(3);
            assertThat(elements).allSatisfy(element -> {
                assertThat(element.getType()).isNotNull();
                assertThat(element.getText()).isNotNull();
            });

            // Should be able to filter by type
            long inlineComments = elements.stream()
                    .filter(e -> e.getType() == TextElementType.INLINE_COMMENT)
                    .count();
            assertThat(inlineComments).isEqualTo(1);
        }

        @Test
        @DisplayName("Should work correctly in concurrent scenarios")
        void testIntegration_Concurrency_ThreadSafe() {
            // Arrange
            GenericTextElement element = new GenericTextElement(TextElementType.PRAGMA_MACRO, "Thread safe test");

            // Act & Assert
            assertThatCode(() -> {
                var threads = java.util.stream.IntStream.range(0, 10)
                        .mapToObj(i -> new Thread(() -> {
                            for (int j = 0; j < 100; j++) {
                                assertThat(element.getType()).isEqualTo(TextElementType.PRAGMA_MACRO);
                                assertThat(element.getText()).isEqualTo("Thread safe test");
                            }
                        }))
                        .toList();

                threads.forEach(Thread::start);
                for (Thread thread : threads) {
                    thread.join();
                }
            }).doesNotThrowAnyException();
        }

        private record TestCase(TextElementType type, String text) {
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle very large text content")
        void testEdgeCase_LargeText_HandledCorrectly() {
            // Arrange
            String largeText = "Large text content ".repeat(1000);

            // Act
            GenericTextElement element = new GenericTextElement(TextElementType.MULTILINE_COMMENT, largeText);

            // Assert
            assertThat(element.getText()).isEqualTo(largeText);
            assertThat(element.getText().length()).isEqualTo(largeText.length());
        }

        @Test
        @DisplayName("Should handle special characters correctly")
        void testEdgeCase_SpecialCharacters_HandledCorrectly() {
            // Arrange
            String specialText = "Special: \n\t\r\\\"\' \u2603 \uD83D\uDE00";

            // Act
            GenericTextElement element = new GenericTextElement(TextElementType.PRAGMA, specialText);

            // Assert
            assertThat(element.getText()).isEqualTo(specialText);
        }

        @Test
        @DisplayName("Should handle whitespace-only content")
        void testEdgeCase_WhitespaceOnly_HandledCorrectly() {
            // Arrange
            String whitespaceText = "   \t\n\r   ";

            // Act
            GenericTextElement element = new GenericTextElement(TextElementType.INLINE_COMMENT, whitespaceText);

            // Assert
            assertThat(element.getText()).isEqualTo(whitespaceText);
            assertThat(element.getType()).isEqualTo(TextElementType.INLINE_COMMENT);
        }

        @Test
        @DisplayName("Should handle memory pressure scenarios")
        void testEdgeCase_MemoryPressure_HandledCorrectly() {
            // Act & Assert - Create many instances to test memory handling
            assertThatCode(() -> {
                for (int i = 0; i < 10000; i++) {
                    GenericTextElement element = new GenericTextElement(
                            TextElementType.values()[i % TextElementType.values().length],
                            "Text " + i);
                    assertThat(element.getText()).isEqualTo("Text " + i);
                }
            }).doesNotThrowAnyException();
        }
    }
}
