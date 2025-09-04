package pt.up.fe.specs.util.utilities;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Comprehensive test suite for StringLines utility class.
 * Tests line-by-line iteration, empty line handling, iterator interface, and
 * stream support.
 * 
 * @author Generated Tests
 */
@DisplayName("StringLines Tests")
class StringLinesTest {

    private static final String MULTILINE_TEXT = """
            First line
            Second line

            Fourth line (third was empty)
            Fifth line
            """;

    private static final String SIMPLE_TEXT = """
            Line 1
            Line 2
            Line 3
            """;

    private static final String EMPTY_LINES_TEXT = """



            Only line with content


            """;

    @Nested
    @DisplayName("Construction Tests")
    class ConstructionTests {

        @Test
        @DisplayName("newInstance should create StringLines from string")
        void testNewInstanceFromString() {
            // Execute
            StringLines lines = StringLines.newInstance(SIMPLE_TEXT);

            // Verify
            assertThat(lines).isNotNull();
            assertThat(lines.hasNextLine()).isTrue();
        }

        @Test
        @DisplayName("should handle empty string")
        void testEmptyString() {
            // Execute
            StringLines lines = StringLines.newInstance("");

            // Verify
            assertThat(lines.hasNextLine()).isFalse();
            assertThat(lines.nextLine()).isNull();
        }

        @Test
        @DisplayName("should handle single line")
        void testSingleLine() {
            // Execute
            StringLines lines = StringLines.newInstance("single line");

            // Verify
            assertThat(lines.hasNextLine()).isTrue();
            assertThat(lines.nextLine()).isEqualTo("single line");
            assertThat(lines.hasNextLine()).isFalse();
        }

        @Test
        @DisplayName("should throw NPE for null input")
        void testNullInput() {
            // Execute & Verify - StringReader constructor throws NPE for null
            assertThatThrownBy(() -> StringLines.newInstance(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Line Navigation Tests")
    class LineNavigationTests {

        @Test
        @DisplayName("nextLine should return lines in order")
        void testNextLineOrder() {
            // Setup
            StringLines lines = StringLines.newInstance(SIMPLE_TEXT);

            // Execute & Verify
            assertThat(lines.nextLine()).isEqualTo("Line 1");
            assertThat(lines.nextLine()).isEqualTo("Line 2");
            assertThat(lines.nextLine()).isEqualTo("Line 3");
            assertThat(lines.nextLine()).isNull();
        }

        @Test
        @DisplayName("hasNextLine should return correct status")
        void testHasNextLine() {
            // Setup
            StringLines lines = StringLines.newInstance("line1\nline2");

            // Execute & Verify
            assertThat(lines.hasNextLine()).isTrue();
            lines.nextLine();
            assertThat(lines.hasNextLine()).isTrue();
            lines.nextLine();
            assertThat(lines.hasNextLine()).isFalse();
        }

        @Test
        @DisplayName("should handle empty lines correctly")
        void testEmptyLines() {
            // Setup
            StringLines lines = StringLines.newInstance("line1\n\nline3");

            // Execute & Verify
            assertThat(lines.nextLine()).isEqualTo("line1");
            assertThat(lines.nextLine()).isEmpty();
            assertThat(lines.nextLine()).isEqualTo("line3");
        }

        @Test
        @DisplayName("getLastLineIndex should track line numbers")
        void testLineIndexTracking() {
            // Setup
            StringLines lines = StringLines.newInstance(SIMPLE_TEXT);

            // Execute & Verify
            assertThat(lines.getLastLineIndex()).isEqualTo(0);

            lines.nextLine();
            assertThat(lines.getLastLineIndex()).isEqualTo(1);

            lines.nextLine();
            assertThat(lines.getLastLineIndex()).isEqualTo(2);

            lines.nextLine();
            assertThat(lines.getLastLineIndex()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("Non-Empty Line Tests")
    class NonEmptyLineTests {

        @Test
        @DisplayName("nextNonEmptyLine should skip empty lines")
        void testNextNonEmptyLineSkipping() {
            // Setup
            StringLines lines = StringLines.newInstance(EMPTY_LINES_TEXT);

            // Execute
            String firstNonEmpty = lines.nextNonEmptyLine();
            String secondNonEmpty = lines.nextNonEmptyLine();

            // Verify
            assertThat(firstNonEmpty).isEqualTo("Only line with content");
            assertThat(secondNonEmpty).isNull(); // No more non-empty lines
        }

        @Test
        @DisplayName("nextNonEmptyLine should return null when no non-empty lines exist")
        void testNextNonEmptyLineAllEmpty() {
            // Setup
            StringLines lines = StringLines.newInstance("\n\n\n");

            // Execute
            String result = lines.nextNonEmptyLine();

            // Verify
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("nextNonEmptyLine should work with mixed content")
        void testNextNonEmptyLineMixed() {
            // Setup
            StringLines lines = StringLines.newInstance(MULTILINE_TEXT);

            // Execute & Verify
            assertThat(lines.nextNonEmptyLine()).isEqualTo("First line");
            assertThat(lines.nextNonEmptyLine()).isEqualTo("Second line");
            assertThat(lines.nextNonEmptyLine()).isEqualTo("Fourth line (third was empty)");
            assertThat(lines.nextNonEmptyLine()).isEqualTo("Fifth line");
            assertThat(lines.nextNonEmptyLine()).isNull();
        }
    }

    @Nested
    @DisplayName("Iterator Interface Tests")
    class IteratorTests {

        @Test
        @DisplayName("should implement Iterable correctly")
        void testIterableInterface() {
            // Setup
            StringLines lines = StringLines.newInstance(SIMPLE_TEXT);
            List<String> collected = new ArrayList<>();

            // Execute
            for (String line : lines) {
                collected.add(line);
            }

            // Verify
            assertThat(collected).containsExactly("Line 1", "Line 2", "Line 3");
        }

        @Test
        @DisplayName("iterator should support hasNext and next")
        void testIteratorMethods() {
            // Setup
            StringLines lines = StringLines.newInstance("line1\nline2");
            Iterator<String> iterator = lines.iterator();

            // Execute & Verify
            assertThat(iterator.hasNext()).isTrue();
            assertThat(iterator.next()).isEqualTo("line1");
            assertThat(iterator.hasNext()).isTrue();
            assertThat(iterator.next()).isEqualTo("line2");
            assertThat(iterator.hasNext()).isFalse();
        }

        @Test
        @DisplayName("iterator should throw UnsupportedOperationException on remove")
        void testIteratorRemoveNotSupported() {
            // Setup
            StringLines lines = StringLines.newInstance("test");
            Iterator<String> iterator = lines.iterator();

            // Execute & Verify
            assertThatThrownBy(iterator::remove)
                    .isInstanceOf(UnsupportedOperationException.class)
                    .hasMessageContaining("LineReader does not support 'remove'");
        }
    }

    @Nested
    @DisplayName("Stream Support Tests")
    class StreamTests {

        @Test
        @DisplayName("stream should return all lines")
        void testStreamAllLines() {
            // Setup
            StringLines lines = StringLines.newInstance(SIMPLE_TEXT);

            // Execute
            List<String> collected = lines.stream().collect(Collectors.toList());

            // Verify
            assertThat(collected).containsExactly("Line 1", "Line 2", "Line 3");
        }

        @Test
        @DisplayName("stream should work with filtering")
        void testStreamFiltering() {
            // Setup
            StringLines lines = StringLines.newInstance(MULTILINE_TEXT);

            // Execute
            List<String> nonEmpty = lines.stream()
                    .filter(line -> !line.isEmpty())
                    .collect(Collectors.toList());

            // Verify
            assertThat(nonEmpty).containsExactly(
                    "First line",
                    "Second line",
                    "Fourth line (third was empty)",
                    "Fifth line");
        }

        @Test
        @DisplayName("stream should handle empty string")
        void testStreamEmpty() {
            // Setup
            StringLines lines = StringLines.newInstance("");

            // Execute
            List<String> collected = lines.stream().collect(Collectors.toList());

            // Verify
            assertThat(collected).isEmpty();
        }
    }

    @Nested
    @DisplayName("Static Utility Methods Tests")
    class StaticMethodsTests {

        @Test
        @DisplayName("getLines should return all lines as list")
        void testGetLinesFromString() {
            // Execute
            List<String> lines = StringLines.getLines(SIMPLE_TEXT);

            // Verify
            assertThat(lines).containsExactly("Line 1", "Line 2", "Line 3");
        }

        @Test
        @DisplayName("getLines should handle empty string")
        void testGetLinesEmpty() {
            // Execute
            List<String> lines = StringLines.getLines("");

            // Verify
            assertThat(lines).isEmpty();
        }

        @Test
        @DisplayName("getLines should preserve empty lines")
        void testGetLinesPreservesEmpty() {
            // Execute
            List<String> lines = StringLines.getLines("line1\n\nline3");

            // Verify
            assertThat(lines).containsExactly("line1", "", "line3");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @ParameterizedTest
        @ValueSource(strings = { "\n", "\r\n" })
        @DisplayName("should handle different line separators")
        void testLineSeparators(String lineSeparator) {
            // Setup
            String text = "line1" + lineSeparator + "line2";
            StringLines lines = StringLines.newInstance(text);

            // Execute & Verify
            assertThat(lines.nextLine()).isEqualTo("line1");
            assertThat(lines.nextLine()).isEqualTo("line2");
        }

        @Test
        @DisplayName("should handle very long lines")
        void testLongLines() {
            // Setup
            String longLine = "a".repeat(10000);
            StringLines lines = StringLines.newInstance(longLine);

            // Execute & Verify
            assertThat(lines.nextLine()).hasSize(10000);
            assertThat(lines.hasNextLine()).isFalse();
        }

        @Test
        @DisplayName("should handle special characters")
        void testSpecialCharacters() {
            // Setup
            String specialText = "line with üñíçødé\ntab\tcharacter\nspecial: !@#$%^&*()";
            StringLines lines = StringLines.newInstance(specialText);

            // Execute & Verify
            assertThat(lines.nextLine()).isEqualTo("line with üñíçødé");
            assertThat(lines.nextLine()).isEqualTo("tab\tcharacter");
            assertThat(lines.nextLine()).isEqualTo("special: !@#$%^&*()");
        }

        @Test
        @DisplayName("should handle text ending with newline")
        void testTextEndingWithNewline() {
            // Setup
            StringLines lines = StringLines.newInstance("line1\nline2\n");

            // Execute & Verify
            assertThat(lines.nextLine()).isEqualTo("line1");
            assertThat(lines.nextLine()).isEqualTo("line2");
            assertThat(lines.hasNextLine()).isFalse();
        }
    }

    @Nested
    @DisplayName("Performance and Resource Tests")
    class PerformanceTests {

        @Test
        @DisplayName("should handle large number of lines efficiently")
        void testManyLines() {
            // Setup
            StringBuilder sb = new StringBuilder();
            int lineCount = 1000;
            for (int i = 0; i < lineCount; i++) {
                sb.append("Line ").append(i).append("\n");
            }

            StringLines lines = StringLines.newInstance(sb.toString());

            // Execute
            int count = 0;
            while (lines.hasNextLine()) {
                lines.nextLine();
                count++;
            }

            // Verify
            assertThat(count).isEqualTo(lineCount);
        }

        @Test
        @DisplayName("multiple operations should work correctly")
        void testMultipleOperations() {
            // Setup
            StringLines lines = StringLines.newInstance(MULTILINE_TEXT);

            // Execute - mix different operations
            String first = lines.nextLine();
            boolean hasNext = lines.hasNextLine();
            String nonEmpty = lines.nextNonEmptyLine();
            int lineIndex = lines.getLastLineIndex();

            // Verify
            assertThat(first).isEqualTo("First line");
            assertThat(hasNext).isTrue();
            assertThat(nonEmpty).isEqualTo("Second line");
            assertThat(lineIndex).isGreaterThan(0);
        }
    }
}
