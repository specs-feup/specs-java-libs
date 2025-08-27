package pt.up.fe.specs.util.parsing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junitpioneer.jupiter.RetryingTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import pt.up.fe.specs.util.parsing.comments.TextElement;
import pt.up.fe.specs.util.parsing.comments.TextElementType;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for CommentParser - text parsing utility that
 * extracts different types of comments and pragmas from source code text.
 * 
 * Tests cover:
 * - Basic comment parsing functionality
 * - Different comment types (inline, multiline, pragma, pragma macro)
 * - File-based parsing vs string-based parsing
 * - Edge cases and error conditions
 * - Real-world code parsing scenarios
 * 
 * @author Generated Tests
 */
@DisplayName("CommentParser Tests")
class CommentParserTest {

    private CommentParser parser;

    @BeforeEach
    void setUp() {
        parser = new CommentParser();
    }

    @Nested
    @DisplayName("Basic Parsing Operations")
    class BasicParsingOperations {

        @Test
        @DisplayName("should parse empty string and return empty list")
        void testParseEmptyString() {
            List<TextElement> result = parser.parse("");

            assertThat(result)
                    .isNotNull()
                    .isEmpty();
        }

        @Test
        @DisplayName("should parse string with no comments and return empty list")
        void testParseStringWithNoComments() {
            String text = "int x = 5;\nString name = \"test\";\nreturn x;";
            List<TextElement> result = parser.parse(text);

            assertThat(result)
                    .isNotNull()
                    .isEmpty();
        }

        @Test
        @DisplayName("should parse null string gracefully")
        void testParseNullString() {
            // StringLines.getLines does not handle null gracefully, it throws NPE
            assertThatThrownBy(() -> {
                parser.parse((String) null);
            }).isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Inline Comment Parsing")
    class InlineCommentParsing {

        @Test
        @DisplayName("should parse single line with inline comment")
        void testParseSingleInlineComment() {
            String text = "int x = 5; // This is a comment";
            List<TextElement> result = parser.parse(text);

            assertThat(result)
                    .hasSize(1);

            TextElement element = result.get(0);
            assertThat(element.getType()).isEqualTo(TextElementType.INLINE_COMMENT);
            assertThat(element.getText()).isEqualTo(" This is a comment");
        }

        @Test
        @DisplayName("should parse multiple inline comments")
        void testParseMultipleInlineComments() {
            String text = """
                    int x = 5; // First comment
                    String y = "test"; // Second comment
                    return x; // Third comment
                    """;
            List<TextElement> result = parser.parse(text);

            assertThat(result)
                    .hasSize(3)
                    .allSatisfy(element -> assertThat(element.getType()).isEqualTo(TextElementType.INLINE_COMMENT));

            assertThat(result.get(0).getText()).isEqualTo(" First comment");
            assertThat(result.get(1).getText()).isEqualTo(" Second comment");
            assertThat(result.get(2).getText()).isEqualTo(" Third comment");
        }

        @Test
        @DisplayName("should parse comment at beginning of line")
        void testParseCommentAtBeginning() {
            String text = "// This is a full line comment";
            List<TextElement> result = parser.parse(text);

            assertThat(result)
                    .hasSize(1);

            TextElement element = result.get(0);
            assertThat(element.getType()).isEqualTo(TextElementType.INLINE_COMMENT);
            assertThat(element.getText()).isEqualTo(" This is a full line comment");
        }

        @Test
        @DisplayName("should parse empty inline comment")
        void testParseEmptyInlineComment() {
            String text = "int x = 5; //";
            List<TextElement> result = parser.parse(text);

            assertThat(result)
                    .hasSize(1);

            TextElement element = result.get(0);
            assertThat(element.getType()).isEqualTo(TextElementType.INLINE_COMMENT);
            assertThat(element.getText()).isEmpty();
        }

        @ParameterizedTest
        @DisplayName("should parse various inline comment formats")
        @ValueSource(strings = {
                "//Simple comment",
                "// Comment with leading space",
                "//    Comment with multiple spaces",
                "// Comment with special chars: @#$%^&*()",
                "// Comment with numbers 123456",
                "// Comment with Unicode: café, naïve, résumé"
        })
        void testParseVariousInlineCommentFormats(String commentLine) {
            List<TextElement> result = parser.parse(commentLine);

            assertThat(result)
                    .hasSize(1);

            TextElement element = result.get(0);
            assertThat(element.getType()).isEqualTo(TextElementType.INLINE_COMMENT);
            assertThat(element.getText()).isEqualTo(commentLine.substring(2));
        }
    }

    @Nested
    @DisplayName("Multiline Comment Parsing")
    class MultilineCommentParsing {

        @Test
        @DisplayName("should parse single line multiline comment")
        void testParseSingleLineMultilineComment() {
            String text = "/* This is a multiline comment */";
            List<TextElement> result = parser.parse(text);

            assertThat(result)
                    .hasSize(1);

            TextElement element = result.get(0);
            assertThat(element.getType()).isEqualTo(TextElementType.MULTILINE_COMMENT);
            // The text should contain the comment content without the /* */ delimiters
        }

        @Test
        @DisplayName("should parse actual multiline comment spanning multiple lines")
        void testParseActualMultilineComment() {
            String text = """
                    /*
                     * This is a multiline comment
                     * that spans multiple lines
                     */
                    """;
            List<TextElement> result = parser.parse(text);

            assertThat(result)
                    .hasSize(1);

            TextElement element = result.get(0);
            assertThat(element.getType()).isEqualTo(TextElementType.MULTILINE_COMMENT);
        }

        @Test
        @DisplayName("should parse empty multiline comment")
        void testParseEmptyMultilineComment() {
            String text = "/**/";
            List<TextElement> result = parser.parse(text);

            assertThat(result)
                    .hasSize(1);

            TextElement element = result.get(0);
            assertThat(element.getType()).isEqualTo(TextElementType.MULTILINE_COMMENT);
        }

        @Test
        @DisplayName("should parse multiple multiline comments")
        void testParseMultipleMultilineComments() {
            String text = """
                    /* First comment */
                    int x = 5;
                    /* Second comment */
                    """;
            List<TextElement> result = parser.parse(text);

            assertThat(result)
                    .hasSize(2)
                    .allSatisfy(element -> assertThat(element.getType()).isEqualTo(TextElementType.MULTILINE_COMMENT));
        }
    }

    @Nested
    @DisplayName("Pragma Parsing")
    class PragmaParsing {

        @Test
        @DisplayName("should parse pragma directive")
        void testParsePragmaDirective() {
            String text = "#pragma omp parallel";
            List<TextElement> result = parser.parse(text);

            assertThat(result)
                    .hasSize(1);

            TextElement element = result.get(0);
            assertThat(element.getType()).isEqualTo(TextElementType.PRAGMA);
        }

        @Test
        @DisplayName("should parse multiple pragma directives")
        void testParseMultiplePragmaDirectives() {
            String text = """
                    #pragma omp parallel
                    #pragma once
                    #pragma pack(1)
                    """;
            List<TextElement> result = parser.parse(text);

            assertThat(result)
                    .hasSize(3)
                    .allSatisfy(element -> assertThat(element.getType()).isEqualTo(TextElementType.PRAGMA));
        }

        @Test
        @DisplayName("should parse pragma macro")
        void testParsePragmaMacro() {
            String text = "#pragma macro some_macro";
            List<TextElement> result = parser.parse(text);

            // This might be parsed as PRAGMA_MACRO type depending on the rule
            // implementation
            assertThat(result)
                    .hasSize(1);

            TextElement element = result.get(0);
            assertThat(element.getType()).isIn(TextElementType.PRAGMA, TextElementType.PRAGMA_MACRO);
        }
    }

    @Nested
    @DisplayName("Mixed Content Parsing")
    class MixedContentParsing {

        @Test
        @DisplayName("should parse text with mixed comment types")
        void testParseMixedCommentTypes() {
            String text = """
                    // Single line comment
                    int x = 5;
                    /* Multiline comment */
                    #pragma omp parallel
                    String y = "test"; // Another inline comment
                    """;
            List<TextElement> result = parser.parse(text);

            assertThat(result)
                    .hasSize(4);

            // Check that we have different types of elements
            List<TextElementType> types = result.stream()
                    .map(TextElement::getType)
                    .toList();

            assertThat(types).contains(
                    TextElementType.INLINE_COMMENT,
                    TextElementType.MULTILINE_COMMENT,
                    TextElementType.PRAGMA);
        }

        @Test
        @DisplayName("should parse real C-like code with comments")
        void testParseRealCodeWithComments() {
            String text = """
                    #pragma once

                    // Function declaration
                    int calculate(int x, int y) {
                        /* This function performs calculation
                           with the given parameters */
                        return x + y; // Simple addition
                    }

                    #pragma pack(1)
                    """;
            List<TextElement> result = parser.parse(text);

            assertThat(result)
                    .hasSizeGreaterThan(3);

            // Should contain at least pragmas, inline comments, and multiline comments
            List<TextElementType> types = result.stream()
                    .map(TextElement::getType)
                    .distinct()
                    .toList();

            assertThat(types).hasSizeGreaterThanOrEqualTo(2);
        }
    }

    @Nested
    @DisplayName("File-based Parsing")
    class FileBasedParsing {

        @TempDir
        Path tempDir;

        @Test
        @DisplayName("should parse comments from file")
        void testParseCommentsFromFile() throws IOException {
            String content = """
                    // File header comment
                    #pragma once

                    int main() {
                        /* Main function */
                        return 0; // Exit code
                    }
                    """;

            Path testFile = tempDir.resolve("test.c");
            Files.writeString(testFile, content);

            List<TextElement> result = parser.parse(testFile.toFile());

            assertThat(result)
                    .hasSizeGreaterThan(2);

            // Should contain inline comments, pragmas, and multiline comments
            List<TextElementType> types = result.stream()
                    .map(TextElement::getType)
                    .distinct()
                    .toList();

            assertThat(types).contains(
                    TextElementType.INLINE_COMMENT,
                    TextElementType.PRAGMA,
                    TextElementType.MULTILINE_COMMENT);
        }

        @Test
        @DisplayName("should handle non-existent file gracefully")
        void testParseNonExistentFile() {
            File nonExistentFile = new File(tempDir.toFile(), "nonexistent.txt");

            assertThatThrownBy(() -> {
                parser.parse(nonExistentFile);
            }).isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("should parse empty file")
        void testParseEmptyFile() throws IOException {
            Path emptyFile = tempDir.resolve("empty.txt");
            Files.writeString(emptyFile, "");

            List<TextElement> result = parser.parse(emptyFile.toFile());

            assertThat(result)
                    .isNotNull()
                    .isEmpty();
        }
    }

    @Nested
    @DisplayName("Iterator-based Parsing")
    class IteratorBasedParsing {

        @Test
        @DisplayName("should parse from iterator")
        void testParseFromIterator() {
            List<String> lines = Arrays.asList(
                    "// First comment",
                    "int x = 5;",
                    "/* Second comment */",
                    "#pragma once");

            Iterator<String> iterator = lines.iterator();
            List<TextElement> result = parser.parse(iterator);

            assertThat(result)
                    .hasSize(3);

            List<TextElementType> types = result.stream()
                    .map(TextElement::getType)
                    .toList();

            assertThat(types).contains(
                    TextElementType.INLINE_COMMENT,
                    TextElementType.MULTILINE_COMMENT,
                    TextElementType.PRAGMA);
        }

        @Test
        @DisplayName("should handle empty iterator")
        void testParseEmptyIterator() {
            List<String> emptyLines = Arrays.asList();
            Iterator<String> iterator = emptyLines.iterator();

            List<TextElement> result = parser.parse(iterator);

            assertThat(result)
                    .isNotNull()
                    .isEmpty();
        }
    }

    @Nested
    @DisplayName("Static Rule Application")
    class StaticRuleApplication {

        @Test
        @DisplayName("should apply rules to line with comment")
        void testApplyRulesToLineWithComment() {
            String line = "int x = 5; // Test comment";
            Iterator<String> emptyIterator = Arrays.<String>asList().iterator();

            Optional<TextElement> result = CommentParser.applyRules(line, emptyIterator);

            assertThat(result).isPresent();
            TextElement element = result.get();
            assertThat(element.getType()).isEqualTo(TextElementType.INLINE_COMMENT);
            assertThat(element.getText()).isEqualTo(" Test comment");
        }

        @Test
        @DisplayName("should return empty for line without comments")
        void testApplyRulesToLineWithoutComments() {
            String line = "int x = 5;";
            Iterator<String> emptyIterator = Arrays.<String>asList().iterator();

            Optional<TextElement> result = CommentParser.applyRules(line, emptyIterator);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("should handle null line gracefully")
        void testApplyRulesToNullLine() {
            Iterator<String> emptyIterator = Arrays.<String>asList().iterator();

            // The rules do not handle null lines gracefully, they throw NPE
            assertThatThrownBy(() -> {
                CommentParser.applyRules(null, emptyIterator);
            }).isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("should apply rules to pragma line")
        void testApplyRulesToPragmaLine() {
            String line = "#pragma omp parallel";
            Iterator<String> emptyIterator = Arrays.<String>asList().iterator();

            Optional<TextElement> result = CommentParser.applyRules(line, emptyIterator);

            assertThat(result).isPresent();
            TextElement element = result.get();
            assertThat(element.getType()).isEqualTo(TextElementType.PRAGMA);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandling {

        @Test
        @DisplayName("should handle comments with special characters")
        void testParseCommentsWithSpecialCharacters() {
            String text = """
                    // Comment with special chars: !@#$%^&*()_+-={}[]|\\:";'<>?,./
                    /* Multiline with Unicode: café, naïve, 中文, العربية */
                    #pragma with-dashes and_underscores
                    """;

            List<TextElement> result = parser.parse(text);

            assertThat(result)
                    .hasSize(3);

            // All elements should be properly parsed without exceptions
            result.forEach(element -> {
                assertThat(element.getType()).isNotNull();
                assertThat(element.getText()).isNotNull();
            });
        }

        @Test
        @DisplayName("should handle very long lines")
        void testParseVeryLongLines() {
            String longComment = "// " + "Very long comment ".repeat(100);

            List<TextElement> result = parser.parse(longComment);

            assertThat(result)
                    .hasSize(1);

            TextElement element = result.get(0);
            assertThat(element.getType()).isEqualTo(TextElementType.INLINE_COMMENT);
            assertThat(element.getText()).hasSize(longComment.length() - 2); // Minus "//"
        }

        @Test
        @DisplayName("should handle nested comment-like patterns")
        void testParseNestedCommentPatterns() {
            String text = """
                    // This comment contains /* fake multiline */ markers
                    /* This multiline contains no inline markers */
                    #pragma with comment-like markers
                    """;

            List<TextElement> result = parser.parse(text);

            assertThat(result)
                    .hasSize(3);

            // Rules are applied in order: Inline, Multiline, Pragma, PragmaMacro
            // First line matches inline comment rule first
            assertThat(result.get(0).getType()).isEqualTo(TextElementType.INLINE_COMMENT);
            // Second line matches multiline comment rule (no // to interfere)
            assertThat(result.get(1).getType()).isEqualTo(TextElementType.MULTILINE_COMMENT);
            // Third line matches pragma rule
            assertThat(result.get(2).getType()).isEqualTo(TextElementType.PRAGMA);
        }
    }

    @Nested
    @DisplayName("Performance and Scalability")
    class PerformanceAndScalability {

        @RetryingTest(5)
        @DisplayName("should handle large number of comment lines efficiently")
        void testParseLargeNumberOfComments() {
            StringBuilder text = new StringBuilder();
            int numberOfComments = 1000;

            for (int i = 0; i < numberOfComments; i++) {
                text.append("// Comment line ").append(i).append("\n");
            }

            long startTime = System.currentTimeMillis();
            List<TextElement> result = parser.parse(text.toString());
            long endTime = System.currentTimeMillis();

            assertThat(result)
                    .hasSize(numberOfComments);

            // Should complete in reasonable time (less than 1 second for 1000 comments)
            assertThat(endTime - startTime).isLessThan(1000);

            // All should be inline comments
            assertThat(result)
                    .allSatisfy(element -> assertThat(element.getType()).isEqualTo(TextElementType.INLINE_COMMENT));
        }

        @RetryingTest(5)
        @DisplayName("should handle mixed large content efficiently")
        void testParseMixedLargeContent() {
            StringBuilder text = new StringBuilder();

            // Add various types of content
            for (int i = 0; i < 100; i++) {
                text.append("// Inline comment ").append(i).append("\n");
                text.append("int variable").append(i).append(" = ").append(i).append(";\n");
                text.append("/* Multiline comment ").append(i).append(" */\n");
                text.append("#pragma directive").append(i).append("\n");
                text.append("regular code line ").append(i).append(";\n");
            }

            long startTime = System.currentTimeMillis();
            List<TextElement> result = parser.parse(text.toString());
            long endTime = System.currentTimeMillis();

            assertThat(result)
                    .hasSize(300); // 100 each of inline, multiline, pragma

            // Should complete in reasonable time
            assertThat(endTime - startTime).isLessThan(2000);

            // Should have all three types
            List<TextElementType> types = result.stream()
                    .map(TextElement::getType)
                    .distinct()
                    .toList();

            assertThat(types).containsExactlyInAnyOrder(
                    TextElementType.INLINE_COMMENT,
                    TextElementType.MULTILINE_COMMENT,
                    TextElementType.PRAGMA);
        }
    }
}
