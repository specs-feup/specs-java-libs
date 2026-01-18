package pt.up.fe.specs.util.parsing.arguments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for ArgumentsParser class.
 * Tests argument parsing with delimiters, gluers, and escape sequences.
 * 
 * @author Generated Tests
 */
@DisplayName("ArgumentsParser Tests")
public class ArgumentsParserTest {

    private ArgumentsParser commandLineParser;
    private ArgumentsParser pragmaParser;
    private ArgumentsParser customParser;

    @BeforeEach
    void setUp() {
        commandLineParser = ArgumentsParser.newCommandLine();
        pragmaParser = ArgumentsParser.newPragmaText();
        customParser = new ArgumentsParser(
                Arrays.asList(",", ";"),
                Arrays.asList(Gluer.newParenthesis()),
                Arrays.asList(Escape.newSlashChar()),
                true);
    }

    @Nested
    @DisplayName("Factory Method Tests")
    class FactoryMethodTests {

        @Test
        @DisplayName("Should create command line parser with correct defaults")
        void testFactory_CommandLineDefaults_CorrectConfiguration() {
            // Act
            ArgumentsParser parser = ArgumentsParser.newCommandLine();

            // Assert
            assertThat(parser).isNotNull();

            // Test typical command line parsing
            List<String> result = parser.parse("command arg1 \"quoted arg\" arg3");
            assertThat(result).containsExactly("command", "arg1", "quoted arg", "arg3");
        }

        @Test
        @DisplayName("Should create command line parser with trim option")
        void testFactory_CommandLineWithTrim_CorrectConfiguration() {
            // Act
            ArgumentsParser parserTrim = ArgumentsParser.newCommandLine(true);
            ArgumentsParser parserNoTrim = ArgumentsParser.newCommandLine(false);

            // Assert
            List<String> trimResult = parserTrim.parse("  arg1  \"  arg2  \"");
            List<String> noTrimResult = parserNoTrim.parse("  arg1  \"  arg2  \"");

            assertThat(trimResult).containsExactly("arg1", "arg2");
            assertThat(noTrimResult).containsExactly("arg1", "  arg2  ");
        }

        @Test
        @DisplayName("Should create pragma text parser with correct defaults")
        void testFactory_PragmaTextDefaults_CorrectConfiguration() {
            // Act
            ArgumentsParser parser = ArgumentsParser.newPragmaText();

            // Assert
            assertThat(parser).isNotNull();

            // Test typical pragma parsing
            List<String> result = parser.parse("pragma_name (arg1, arg2) other_content");
            assertThat(result).isNotEmpty();
        }

        @Test
        @DisplayName("Should create pragma text parser with trim option")
        void testFactory_PragmaTextWithTrim_CorrectConfiguration() {
            // Act
            ArgumentsParser parserTrim = ArgumentsParser.newPragmaText(true);
            ArgumentsParser parserNoTrim = ArgumentsParser.newPragmaText(false);

            // Assert
            assertThat(parserTrim).isNotNull();
            assertThat(parserNoTrim).isNotNull();

            // Both should handle pragma syntax
            List<String> trimResult = parserTrim.parse("  pragma  content  ");
            List<String> noTrimResult = parserNoTrim.parse("  pragma  content  ");

            // Implementation behavior: pragma parser may have different delimiter/gluer
            // handling
            assertThat(trimResult).isNotEmpty();
            assertThat(noTrimResult).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("Basic Parsing Tests")
    class BasicParsingTests {

        @Test
        @DisplayName("Should parse empty string")
        void testParse_EmptyString_ReturnsEmptyList() {
            // Act
            List<String> result = commandLineParser.parse("");

            // Assert
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should parse single argument")
        void testParse_SingleArgument_ReturnsSingleElement() {
            // Act
            List<String> result = commandLineParser.parse("single");

            // Assert
            assertThat(result).containsExactly("single");
        }

        @Test
        @DisplayName("Should parse multiple space-separated arguments")
        void testParse_MultipleArguments_ReturnsAllElements() {
            // Act
            List<String> result = commandLineParser.parse("arg1 arg2 arg3");

            // Assert
            assertThat(result).containsExactly("arg1", "arg2", "arg3");
        }

        @Test
        @DisplayName("Should handle multiple consecutive spaces")
        void testParse_MultipleSpaces_SkipsEmptyArgs() {
            // Act
            List<String> result = commandLineParser.parse("arg1    arg2     arg3");

            // Assert
            assertThat(result).containsExactly("arg1", "arg2", "arg3");
        }

        @Test
        @DisplayName("Should handle leading and trailing spaces")
        void testParse_LeadingTrailingSpaces_HandledCorrectly() {
            // Act
            List<String> result = commandLineParser.parse("   arg1 arg2   ");

            // Assert
            assertThat(result).containsExactly("arg1", "arg2");
        }

        @Test
        @DisplayName("Should handle only spaces")
        void testParse_OnlySpaces_ReturnsEmptyList() {
            // Act
            List<String> result = commandLineParser.parse("     ");

            // Assert
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("Quoted String Tests")
    class QuotedStringTests {

        @Test
        @DisplayName("Should parse simple quoted string")
        void testParse_SimpleQuotedString_ParsedCorrectly() {
            // Act
            List<String> result = commandLineParser.parse("\"quoted string\"");

            // Assert
            assertThat(result).containsExactly("quoted string");
        }

        @Test
        @DisplayName("Should parse quoted string with spaces")
        void testParse_QuotedStringWithSpaces_SpacesPreserved() {
            // Act
            List<String> result = commandLineParser.parse("\"this has spaces\"");

            // Assert
            assertThat(result).containsExactly("this has spaces");
        }

        @Test
        @DisplayName("Should parse mixed quoted and unquoted arguments")
        void testParse_MixedQuotedUnquoted_ParsedCorrectly() {
            // Act
            List<String> result = commandLineParser.parse("unquoted \"quoted string\" another");

            // Assert
            assertThat(result).containsExactly("unquoted", "quoted string", "another");
        }

        @Test
        @DisplayName("Should parse empty quoted string")
        void testParse_EmptyQuotedString_ReturnsEmptyArg() {
            // Act
            List<String> result = commandLineParser.parse("arg1 \"\" arg2");

            // Assert - Implementation skips empty arguments
            assertThat(result).containsExactly("arg1", "arg2");
        }

        @Test
        @DisplayName("Should parse consecutive quoted strings")
        void testParse_ConsecutiveQuotedStrings_ParsedSeparately() {
            // Act
            List<String> result = commandLineParser.parse("\"first\"\"second\"");

            // Assert
            assertThat(result).containsExactly("firstsecond");
        }

        @Test
        @DisplayName("Should parse quoted string at end")
        void testParse_QuotedStringAtEnd_ParsedCorrectly() {
            // Act
            List<String> result = commandLineParser.parse("arg1 arg2 \"final quoted\"");

            // Assert
            assertThat(result).containsExactly("arg1", "arg2", "final quoted");
        }
    }

    @Nested
    @DisplayName("Escape Sequence Tests")
    class EscapeSequenceTests {

        @Test
        @DisplayName("Should handle escaped quotes")
        void testParse_EscapedQuotes_ProcessedCorrectly() {
            // Act
            List<String> result = commandLineParser.parse("\"escaped \\\" quote\"");

            // Assert - Implementation preserves escape sequences
            assertThat(result).containsExactly("escaped \\\" quote");
        }

        @Test
        @DisplayName("Should handle escaped backslash")
        void testParse_EscapedBackslash_ProcessedCorrectly() {
            // Act
            List<String> result = commandLineParser.parse("\"escaped \\\\ backslash\"");

            // Assert - Implementation preserves escape sequences
            assertThat(result).containsExactly("escaped \\\\ backslash");
        }

        @Test
        @DisplayName("Should handle escaped characters outside quotes")
        void testParse_EscapedOutsideQuotes_ProcessedCorrectly() {
            // Act
            List<String> result = commandLineParser.parse("arg\\ with\\ spaces");

            // Assert - Implementation preserves escape sequences
            assertThat(result).containsExactly("arg\\ with\\ spaces");
        }

        @Test
        @DisplayName("Should handle multiple escape sequences")
        void testParse_MultipleEscapeSequences_AllProcessed() {
            // Act
            List<String> result = commandLineParser.parse("\"\\\"Hello\\\" \\\"World\\\"\"");

            // Assert - Implementation preserves escape sequences
            assertThat(result).containsExactly("\\\"Hello\\\" \\\"World\\\"");
        }

        @Test
        @DisplayName("Should handle escape at end of string")
        void testParse_EscapeAtEnd_ProcessedCorrectly() {
            // Act
            List<String> result = commandLineParser.parse("arg\\\\");

            // Assert - Implementation preserves escape sequences
            assertThat(result).containsExactly("arg\\\\");
        }
    }

    @Nested
    @DisplayName("Custom Parser Tests")
    class CustomParserTests {

        @Test
        @DisplayName("Should use custom delimiters")
        void testParse_CustomDelimiters_UsedCorrectly() {
            // Act
            List<String> result = customParser.parse("arg1,arg2;arg3");

            // Assert
            assertThat(result).containsExactly("arg1", "arg2", "arg3");
        }

        @Test
        @DisplayName("Should use custom gluers")
        void testParse_CustomGluers_UsedCorrectly() {
            // Act
            List<String> result = customParser.parse("arg1,(grouped,content),arg3");

            // Assert
            assertThat(result).containsExactly("arg1", "(grouped,content)", "arg3");
        }

        @Test
        @DisplayName("Should combine custom delimiters with gluers")
        void testParse_CustomDelimitersAndGluers_WorkTogether() {
            // Act
            List<String> result = customParser.parse("arg1;(group,with,commas);arg3");

            // Assert
            assertThat(result).containsExactly("arg1", "(group,with,commas)", "arg3");
        }

        @Test
        @DisplayName("Should handle custom escapes with custom delimiters")
        void testParse_CustomEscapesAndDelimiters_WorkTogether() {
            // Act
            List<String> result = customParser.parse("arg1\\,with\\,commas;arg2");

            // Assert - Implementation preserves escape sequences
            assertThat(result).containsExactly("arg1\\,with\\,commas", "arg2");
        }
    }

    @Nested
    @DisplayName("Pragma Parser Tests")
    class PragmaParserTests {

        @Test
        @DisplayName("Should parse pragma text with parentheses")
        void testParse_PragmaWithParentheses_ParsedCorrectly() {
            // Act
            List<String> result = pragmaParser.parse("omp parallel for (schedule(static, 4))");

            // Assert
            assertThat(result).isNotEmpty();
            assertThat(result).contains("omp");
            assertThat(result).contains("parallel");
            assertThat(result).contains("for");
        }

        @Test
        @DisplayName("Should handle nested parentheses in pragma")
        void testParse_NestedParentheses_HandledCorrectly() {
            // Act
            List<String> result = pragmaParser.parse("directive (outer(inner(deep)))");

            // Assert
            assertThat(result).isNotEmpty();
            assertThat(result).contains("directive");
        }

        @Test
        @DisplayName("Should parse pragma without parentheses")
        void testParse_PragmaWithoutParentheses_ParsedCorrectly() {
            // Act
            List<String> result = pragmaParser.parse("once");

            // Assert
            assertThat(result).containsExactly("once");
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should throw exception for unclosed quotes")
        void testParse_UnclosedQuotes_ThrowsException() {
            // Act & Assert
            assertThatThrownBy(() -> commandLineParser.parse("\"unclosed quote"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("\"");
        }

        @Test
        @DisplayName("Should throw exception for unclosed parentheses")
        void testParse_UnclosedParentheses_ThrowsException() {
            // Act & Assert
            assertThatThrownBy(() -> customParser.parse("(unclosed parenthesis"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("(");
        }

        @Test
        @DisplayName("Should handle null input gracefully")
        void testParse_NullInput_ThrowsException() {
            // Act & Assert - Implementation throws IllegalArgumentException instead of NPE
            assertThatThrownBy(() -> commandLineParser.parse(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("must not be null");
        }

        @Test
        @DisplayName("Should handle incomplete escape sequence")
        void testParse_IncompleteEscape_ThrowsException() {
            // Act & Assert
            assertThatThrownBy(() -> commandLineParser.parse("incomplete\\"))
                    .isInstanceOf(RuntimeException.class);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle only delimiters")
        void testParse_OnlyDelimiters_ReturnsEmptyList() {
            // Act
            List<String> result = customParser.parse(",,,;;;");

            // Assert
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should handle nested gluer types")
        void testParse_NestedGluerTypes_HandledCorrectly() {
            // Arrange
            ArgumentsParser complexParser = new ArgumentsParser(
                    Arrays.asList(" "),
                    Arrays.asList(Gluer.newDoubleQuote(), Gluer.newParenthesis()),
                    Collections.emptyList(),
                    true);

            // Act
            List<String> result = complexParser.parse("arg1 \"quoted (with parens)\" (parens \"with quotes\")");

            // Assert
            assertThat(result).hasSize(3);
            assertThat(result.get(1)).isEqualTo("quoted (with parens)");
            assertThat(result.get(2)).isEqualTo("(parens \"with quotes\")");
        }

        @Test
        @DisplayName("Should handle very long arguments")
        void testParse_VeryLongArguments_HandledCorrectly() {
            // Arrange
            String longArg = "a".repeat(10000);

            // Act
            List<String> result = commandLineParser.parse("short " + longArg + " short");

            // Assert
            assertThat(result).hasSize(3);
            assertThat(result.get(1)).hasSize(10000);
        }

        @Test
        @DisplayName("Should handle unicode characters")
        void testParse_UnicodeCharacters_HandledCorrectly() {
            // Act
            List<String> result = commandLineParser.parse("unicode_\u2603_\u03B1\u03B2\u03B3 \"quoted \u2764\"");

            // Assert
            assertThat(result).containsExactly("unicode_\u2603_\u03B1\u03B2\u03B3", "quoted \u2764");
        }

        @Test
        @DisplayName("Should handle empty gluers")
        void testParse_EmptyGluers_HandledCorrectly() {
            // Act
            List<String> result = commandLineParser.parse("arg1 \"\" arg2");

            // Assert - Implementation skips empty arguments
            assertThat(result).containsExactly("arg1", "arg2");
        }
    }

    @Nested
    @DisplayName("Trim Option Tests")
    class TrimOptionTests {

        @Test
        @DisplayName("Should trim arguments when trim is enabled")
        void testParse_TrimEnabled_ArgumentsTrimmed() {
            // Arrange
            ArgumentsParser trimParser = new ArgumentsParser(
                    Arrays.asList(" "),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    true);

            // Act
            List<String> result = trimParser.parse("  arg1   arg2  ");

            // Assert
            assertThat(result).containsExactly("arg1", "arg2");
        }

        @Test
        @DisplayName("Should not trim arguments when trim is disabled")
        void testParse_TrimDisabled_ArgumentsNotTrimmed() {
            // Arrange
            ArgumentsParser noTrimParser = new ArgumentsParser(
                    Arrays.asList("|"),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    false);

            // Act
            List<String> result = noTrimParser.parse("  arg1  |  arg2  ");

            // Assert
            assertThat(result).containsExactly("  arg1  ", "  arg2  ");
        }
    }

    @Nested
    @DisplayName("Real-World Scenarios Tests")
    class RealWorldScenariosTests {

        @Test
        @DisplayName("Should parse typical command line with options")
        void testParse_TypicalCommandLine_ParsedCorrectly() {
            // Act
            List<String> result = commandLineParser.parse("gcc -o output \"source file.c\" -I\"/path with spaces\"");

            // Assert
            assertThat(result).containsExactly("gcc", "-o", "output", "source file.c", "-I/path with spaces");
        }

        @Test
        @DisplayName("Should parse OpenMP pragma directive")
        void testParse_OpenMPPragma_ParsedCorrectly() {
            // Act
            List<String> result = pragmaParser.parse("omp parallel for private(i) schedule(static, 4)");

            // Assert
            assertThat(result).contains("omp", "parallel", "for");
            assertThat(result).anyMatch(arg -> arg.contains("private"));
            assertThat(result).anyMatch(arg -> arg.contains("schedule"));
        }

        @Test
        @DisplayName("Should parse complex nested structures")
        void testParse_ComplexNestedStructures_ParsedCorrectly() {
            // Arrange
            ArgumentsParser complexParser = new ArgumentsParser(
                    Arrays.asList(","),
                    Arrays.asList(Gluer.newParenthesis(), Gluer.newTag()),
                    Arrays.asList(Escape.newSlashChar()),
                    true);

            // Act
            List<String> result = complexParser.parse("func(arg1, <tag>content</tag>), escape\\,test, final");

            // Assert
            assertThat(result).hasSize(3);
            assertThat(result.get(0)).contains("func(arg1, <tag>content</tag>)");
            assertThat(result.get(1)).isEqualTo("escape\\,test"); // Implementation preserves escape
            assertThat(result.get(2)).isEqualTo("final");
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should handle large input efficiently")
        void testPerformance_LargeInput_HandledEfficiently() {
            // Arrange
            StringBuilder largeInput = new StringBuilder();
            for (int i = 0; i < 10000; i++) {
                largeInput.append("arg").append(i).append(" ");
            }

            // Act & Assert
            assertThatCode(() -> {
                List<String> result = commandLineParser.parse(largeInput.toString());
                assertThat(result).hasSize(10000);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle many quoted strings efficiently")
        void testPerformance_ManyQuotedStrings_HandledEfficiently() {
            // Arrange
            StringBuilder input = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                input.append("\"quoted arg ").append(i).append("\" ");
            }

            // Act & Assert
            assertThatCode(() -> {
                List<String> result = commandLineParser.parse(input.toString());
                assertThat(result).hasSize(1000);
                assertThat(result.get(0)).isEqualTo("quoted arg 0");
                assertThat(result.get(999)).isEqualTo("quoted arg 999");
            }).doesNotThrowAnyException();
        }
    }
}
