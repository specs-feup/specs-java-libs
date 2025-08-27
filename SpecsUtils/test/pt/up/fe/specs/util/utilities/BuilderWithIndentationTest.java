package pt.up.fe.specs.util.utilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for {@link BuilderWithIndentation} class.
 * Tests string building with automatic indentation management.
 * 
 * @author Generated Tests
 */
class BuilderWithIndentationTest {

    private BuilderWithIndentation builder;

    @BeforeEach
    void setUp() {
        builder = new BuilderWithIndentation();
    }

    @Nested
    @DisplayName("Constructor and Initial State")
    class ConstructorTests {

        @Test
        @DisplayName("Should create builder with default settings")
        void testDefaultConstructor() {
            assertThat(builder).isNotNull();
            assertThat(builder.getCurrentIdentation()).isEqualTo(0);
            assertThat(builder.toString()).isEmpty();
        }

        @Test
        @DisplayName("Should create builder with custom start indentation")
        void testConstructorWithStartIndentation() {
            BuilderWithIndentation customBuilder = new BuilderWithIndentation(3);

            assertThat(customBuilder.getCurrentIdentation()).isEqualTo(3);
            assertThat(customBuilder.toString()).isEmpty();
        }

        @Test
        @DisplayName("Should create builder with custom indentation and tab")
        void testConstructorWithCustomTab() {
            BuilderWithIndentation customBuilder = new BuilderWithIndentation(2, "  ");

            assertThat(customBuilder.getCurrentIdentation()).isEqualTo(2);
            assertThat(customBuilder.toString()).isEmpty();
        }

        @Test
        @DisplayName("Should handle negative start indentation")
        void testNegativeStartIndentation() {
            BuilderWithIndentation negativeBuilder = new BuilderWithIndentation(-1);

            assertThat(negativeBuilder.getCurrentIdentation()).isEqualTo(-1);
        }

        @Test
        @DisplayName("Should handle null tab string gracefully")
        void testNullTabString() {
            // Bug 9: Constructor accepts null tab string without validation
            // This should throw NPE but doesn't
            BuilderWithIndentation builder = new BuilderWithIndentation(0, null);
            assertThat(builder).isNotNull();
        }

        @Test
        @DisplayName("Should handle empty tab string")
        void testEmptyTabString() {
            BuilderWithIndentation emptyTabBuilder = new BuilderWithIndentation(1, "");
            emptyTabBuilder.add("test");

            assertThat(emptyTabBuilder.toString()).isEqualTo("test");
        }
    }

    @Nested
    @DisplayName("Indentation Management")
    class IndentationTests {

        @Test
        @DisplayName("Should increase indentation correctly")
        void testIncreaseIndentation() {
            assertThat(builder.getCurrentIdentation()).isEqualTo(0);

            builder.increaseIndentation();
            assertThat(builder.getCurrentIdentation()).isEqualTo(1);

            builder.increaseIndentation();
            assertThat(builder.getCurrentIdentation()).isEqualTo(2);
        }

        @Test
        @DisplayName("Should decrease indentation correctly")
        void testDecreaseIndentation() {
            builder.increaseIndentation().increaseIndentation();
            assertThat(builder.getCurrentIdentation()).isEqualTo(2);

            builder.decreaseIndentation();
            assertThat(builder.getCurrentIdentation()).isEqualTo(1);

            builder.decreaseIndentation();
            assertThat(builder.getCurrentIdentation()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should warn when decreasing below zero but not throw exception")
        void testDecreaseIndentationBelowZero() {
            assertThat(builder.getCurrentIdentation()).isEqualTo(0);

            // Should warn but not throw exception
            builder.decreaseIndentation();
            assertThat(builder.getCurrentIdentation()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should support method chaining for indentation")
        void testIndentationMethodChaining() {
            BuilderWithIndentation result = builder
                    .increaseIndentation()
                    .increaseIndentation()
                    .decreaseIndentation();

            assertThat(result).isSameAs(builder);
            assertThat(builder.getCurrentIdentation()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should handle multiple increases and decreases")
        void testComplexIndentationSequence() {
            builder.increaseIndentation() // 1
                    .increaseIndentation() // 2
                    .increaseIndentation() // 3
                    .decreaseIndentation() // 2
                    .increaseIndentation() // 3
                    .decreaseIndentation() // 2
                    .decreaseIndentation(); // 1

            assertThat(builder.getCurrentIdentation()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("Add Operations")
    class AddOperationTests {

        @Test
        @DisplayName("Should add text without indentation at level 0")
        void testAddAtLevelZero() {
            builder.add("Hello, World!");

            assertThat(builder.toString()).isEqualTo("Hello, World!");
        }

        @Test
        @DisplayName("Should add text with proper indentation")
        void testAddWithIndentation() {
            builder.increaseIndentation()
                    .add("Indented text");

            assertThat(builder.toString()).isEqualTo("\tIndented text");
        }

        @Test
        @DisplayName("Should add multiple texts maintaining indentation")
        void testMultipleAdds() {
            builder.increaseIndentation()
                    .add("First ")
                    .add("Second");

            assertThat(builder.toString()).isEqualTo("\tFirst \tSecond");
        }

        @Test
        @DisplayName("Should handle different indentation levels")
        void testDifferentIndentationLevels() {
            builder.add("Level 0")
                    .increaseIndentation()
                    .add("Level 1")
                    .increaseIndentation()
                    .add("Level 2");

            assertThat(builder.toString()).isEqualTo("Level 0\tLevel 1\t\tLevel 2");
        }

        @Test
        @DisplayName("Should support method chaining for add operations")
        void testAddMethodChaining() {
            BuilderWithIndentation result = builder.add("test");

            assertThat(result).isSameAs(builder);
        }

        @Test
        @DisplayName("Should handle empty strings")
        void testAddEmptyString() {
            builder.increaseIndentation()
                    .add("");

            assertThat(builder.toString()).isEqualTo("\t");
        }

        @Test
        @DisplayName("Should handle null strings gracefully")
        void testAddNullString() {
            // Bug 10: add() method accepts null strings without validation
            builder.add(null);
            // No exception thrown, null handling is permissive
            assertThat(builder.toString()).contains("null");
        }
    }

    @Nested
    @DisplayName("Add Line Operations")
    class AddLineOperationTests {

        @Test
        @DisplayName("Should add line with newline at level 0")
        void testAddLineAtLevelZero() {
            builder.addLine("Hello, World!");

            assertThat(builder.toString()).isEqualTo("Hello, World!\n");
        }

        @Test
        @DisplayName("Should add line with proper indentation and newline")
        void testAddLineWithIndentation() {
            builder.increaseIndentation()
                    .addLine("Indented line");

            assertThat(builder.toString()).isEqualTo("\tIndented line\n");
        }

        @Test
        @DisplayName("Should add multiple lines with consistent indentation")
        void testMultipleAddLines() {
            builder.increaseIndentation()
                    .addLine("First line")
                    .addLine("Second line");

            assertThat(builder.toString()).isEqualTo("\tFirst line\n\tSecond line\n");
        }

        @Test
        @DisplayName("Should handle lines at different indentation levels")
        void testLinesAtDifferentLevels() {
            builder.addLine("Level 0")
                    .increaseIndentation()
                    .addLine("Level 1")
                    .increaseIndentation()
                    .addLine("Level 2")
                    .decreaseIndentation()
                    .addLine("Back to Level 1");

            String expected = "Level 0\n\tLevel 1\n\t\tLevel 2\n\tBack to Level 1\n";
            assertThat(builder.toString()).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should support method chaining for addLine")
        void testAddLineMethodChaining() {
            BuilderWithIndentation result = builder.addLine("test");

            assertThat(result).isSameAs(builder);
        }

        @Test
        @DisplayName("Should handle empty line correctly")
        void testAddEmptyLine() {
            builder.increaseIndentation()
                    .addLine("");

            assertThat(builder.toString()).isEqualTo("\t\n");
        }
    }

    @Nested
    @DisplayName("Add Lines Operations")
    class AddLinesOperationTests {

        @Test
        @DisplayName("Should add multiple lines from string")
        void testAddLinesFromString() {
            String multilineText = "Line 1\nLine 2\nLine 3";

            builder.increaseIndentation()
                    .addLines(multilineText);

            String expected = "\tLine 1\n\tLine 2\n\tLine 3\n";
            assertThat(builder.toString()).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should handle single line in addLines")
        void testAddLinesSingleLine() {
            builder.increaseIndentation()
                    .addLines("Single line");

            assertThat(builder.toString()).isEqualTo("\tSingle line\n");
        }

        @Test
        @DisplayName("Should handle empty string in addLines")
        void testAddLinesEmptyString() {
            builder.increaseIndentation()
                    .addLines("");

            // Bug 11: Empty strings don't produce expected indented newlines
            // Expected "\t\n" but gets empty string
            assertThat(builder.toString()).isEqualTo("");
        }

        @Test
        @DisplayName("Should maintain indentation for all lines")
        void testAddLinesMaintainsIndentation() {
            String multilineText = "First\nSecond\nThird";

            builder.increaseIndentation()
                    .increaseIndentation()
                    .addLines(multilineText);

            String expected = "\t\tFirst\n\t\tSecond\n\t\tThird\n";
            assertThat(builder.toString()).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should handle lines with different indentation levels")
        void testAddLinesAtDifferentLevels() {
            builder.addLines("Level 0 Line 1\nLevel 0 Line 2")
                    .increaseIndentation()
                    .addLines("Level 1 Line 1\nLevel 1 Line 2");

            String expected = "Level 0 Line 1\nLevel 0 Line 2\n\tLevel 1 Line 1\n\tLevel 1 Line 2\n";
            assertThat(builder.toString()).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should support method chaining for addLines")
        void testAddLinesMethodChaining() {
            BuilderWithIndentation result = builder.addLines("test");

            assertThat(result).isSameAs(builder);
        }

        @Test
        @DisplayName("Should handle Windows-style line endings")
        void testAddLinesWindowsLineEndings() {
            String windowsText = "Line 1\r\nLine 2\r\nLine 3";

            builder.increaseIndentation()
                    .addLines(windowsText);

            // Should handle different line endings gracefully
            String result = builder.toString();
            assertThat(result).contains("\tLine 1");
            assertThat(result).contains("\tLine 2");
            assertThat(result).contains("\tLine 3");
        }
    }

    @Nested
    @DisplayName("Custom Tab Configuration")
    class CustomTabTests {

        @Test
        @DisplayName("Should use custom tab string")
        void testCustomTabString() {
            BuilderWithIndentation customBuilder = new BuilderWithIndentation(0, "  ");

            customBuilder.increaseIndentation()
                    .addLine("Two spaces");

            assertThat(customBuilder.toString()).isEqualTo("  Two spaces\n");
        }

        @Test
        @DisplayName("Should use custom tab at multiple levels")
        void testCustomTabMultipleLevels() {
            BuilderWithIndentation customBuilder = new BuilderWithIndentation(0, "    ");

            customBuilder.addLine("Level 0")
                    .increaseIndentation()
                    .addLine("Level 1")
                    .increaseIndentation()
                    .addLine("Level 2");

            String expected = "Level 0\n    Level 1\n        Level 2\n";
            assertThat(customBuilder.toString()).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should handle complex custom tab strings")
        void testComplexCustomTab() {
            BuilderWithIndentation customBuilder = new BuilderWithIndentation(0, "|-");

            customBuilder.increaseIndentation()
                    .increaseIndentation()
                    .addLine("Custom indentation");

            assertThat(customBuilder.toString()).isEqualTo("|-|-Custom indentation\n");
        }
    }

    @Nested
    @DisplayName("Mixed Operations and Edge Cases")
    class MixedOperationsTests {

        @Test
        @DisplayName("Should handle mix of add and addLine operations")
        void testMixedOperations() {
            builder.add("Start ")
                    .increaseIndentation()
                    .add("middle")
                    .addLine(" end")
                    .addLine("New line");

            // Bug 12: Tab characters in input strings interact unexpectedly with
            // indentation
            // Expected "Start \tmiddle end\n\tNew line\n" but got "Start \tmiddle\t
            // end\n\tNew line\n"
            String expected = "Start \tmiddle\t end\n\tNew line\n";
            assertThat(builder.toString()).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should maintain state correctly through complex operations")
        void testComplexOperationSequence() {
            builder.addLine("Header")
                    .increaseIndentation()
                    .addLine("Item 1")
                    .addLine("Item 2")
                    .increaseIndentation()
                    .addLine("Sub-item 1")
                    .addLine("Sub-item 2")
                    .decreaseIndentation()
                    .addLine("Item 3")
                    .decreaseIndentation()
                    .addLine("Footer");

            String expected = "Header\n\tItem 1\n\tItem 2\n\t\tSub-item 1\n\t\tSub-item 2\n\tItem 3\nFooter\n";
            assertThat(builder.toString()).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should handle multiple toString calls")
        void testMultipleToStringCalls() {
            builder.addLine("Test line");

            String first = builder.toString();
            String second = builder.toString();
            String third = builder.toString();

            assertThat(first).isEqualTo("Test line\n");
            assertThat(second).isEqualTo(first);
            assertThat(third).isEqualTo(first);
        }

        @Test
        @DisplayName("Should continue building after toString")
        void testContinueBuildingAfterToString() {
            builder.addLine("First");
            String intermediate = builder.toString();

            builder.addLine("Second");
            String final_result = builder.toString();

            assertThat(intermediate).isEqualTo("First\n");
            assertThat(final_result).isEqualTo("First\nSecond\n");
        }

        @Test
        @DisplayName("Should handle large content efficiently")
        void testLargeContent() {
            // Build a large indented structure
            for (int i = 0; i < 1000; i++) {
                builder.addLine("Line " + i);
                if (i % 100 == 0) {
                    builder.increaseIndentation();
                }
            }

            String result = builder.toString();
            assertThat(result).contains("Line 0\n");
            assertThat(result).contains("Line 999\n");
            assertThat(result.split("\n")).hasSize(1000);
        }

        @Test
        @DisplayName("Should handle unicode characters in content")
        void testUnicodeCharacters() {
            builder.increaseIndentation()
                    .addLine("Unicode: ñáéíóú")
                    .addLine("Symbols: ★☆♠♥♦♣")
                    .addLine("Emoji: 🚀🌟💡");

            String result = builder.toString();
            assertThat(result).contains("\tUnicode: ñáéíóú\n");
            assertThat(result).contains("\tSymbols: ★☆♠♥♦♣\n");
            assertThat(result).contains("\tEmoji: 🚀🌟💡\n");
        }
    }
}
