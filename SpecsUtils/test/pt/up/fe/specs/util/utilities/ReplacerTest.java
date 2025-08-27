package pt.up.fe.specs.util.utilities;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import pt.up.fe.specs.util.providers.ResourceProvider;

/**
 * Test class for Replacer utility.
 * 
 * Tests string replacement functionality including:
 * - Constructor variations with string and resource input
 * - Basic string replacement operations
 * - Integer replacement with automatic conversion
 * - Generic object replacement with toString() conversion
 * - Regular expression replacement
 * - Method chaining capabilities
 * - Null parameter handling and edge cases
 * 
 * @author Generated Tests
 */
@DisplayName("Replacer Tests")
class ReplacerTest {

    @TempDir
    Path tempDir;

    private final String sampleText = "Hello [NAME], welcome to [PLACE]. You have [COUNT] messages.";
    private final String expectedAfterReplacement = "Hello John, welcome to Paris. You have 5 messages.";

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create Replacer with string")
        void testStringConstructor() {
            Replacer replacer = new Replacer(sampleText);

            assertThat(replacer).isNotNull();
            assertThat(replacer.toString()).isEqualTo(sampleText);
        }

        @Test
        @DisplayName("Should create Replacer with ResourceProvider")
        void testResourceConstructor() throws IOException {
            // Create a temporary resource file
            Path resourceFile = Files.createTempFile(tempDir, "test", ".txt");
            Files.write(resourceFile, sampleText.getBytes());

            ResourceProvider resource = () -> resourceFile.toString();

            // SpecsIo.getResource() might return null for file-based ResourceProviders
            // since they're typically used for classpath resources
            assertThatCode(() -> new Replacer(resource))
                    .doesNotThrowAnyException();

            Replacer replacer = new Replacer(resource);
            assertThat(replacer).isNotNull();

            // The result is null because SpecsIo.getResource() returns null for file paths
            // This is expected behavior - ResourceProvider is for classpath resources
            assertThat(replacer.toString()).isNull();
        }

        @Test
        @DisplayName("Should handle null string parameter")
        void testNullString() {
            // The Replacer constructor accepts null strings without validation
            assertThatCode(() -> new Replacer((String) null))
                    .doesNotThrowAnyException();

            Replacer replacer = new Replacer((String) null);
            // toString() should handle null gracefully
            assertThat(replacer.toString()).isNull();
        }

        @Test
        @DisplayName("Should handle null ResourceProvider parameter")
        void testNullResource() {
            assertThatThrownBy(() -> new Replacer((ResourceProvider) null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle empty string")
        void testEmptyString() {
            Replacer replacer = new Replacer("");

            assertThat(replacer.toString()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Basic Replacement Tests")
    class BasicReplacementTests {

        @Test
        @DisplayName("Should replace simple strings")
        void testSimpleStringReplacement() {
            Replacer replacer = new Replacer("Hello World");

            replacer.replace("World", "Java");

            assertThat(replacer.toString()).isEqualTo("Hello Java");
        }

        @Test
        @DisplayName("Should replace multiple occurrences")
        void testMultipleOccurrences() {
            Replacer replacer = new Replacer("test test test");

            replacer.replace("test", "demo");

            assertThat(replacer.toString()).isEqualTo("demo demo demo");
        }

        @Test
        @DisplayName("Should handle case-sensitive replacement")
        void testCaseSensitiveReplacement() {
            Replacer replacer = new Replacer("Test test TEST");

            replacer.replace("test", "demo");

            assertThat(replacer.toString()).isEqualTo("Test demo TEST");
        }

        @Test
        @DisplayName("Should handle replacement with empty string")
        void testReplacementWithEmpty() {
            Replacer replacer = new Replacer("Hello [REMOVE] World");

            replacer.replace("[REMOVE] ", "");

            assertThat(replacer.toString()).isEqualTo("Hello World");
        }

        @Test
        @DisplayName("Should handle no matches")
        void testNoMatches() {
            Replacer replacer = new Replacer("Hello World");

            replacer.replace("Java", "Python");

            assertThat(replacer.toString()).isEqualTo("Hello World");
        }
    }

    @Nested
    @DisplayName("Integer Replacement Tests")
    class IntegerReplacementTests {

        @Test
        @DisplayName("Should replace with integer values")
        void testIntegerReplacement() {
            Replacer replacer = new Replacer("You have [COUNT] items");

            replacer.replace("[COUNT]", 42);

            assertThat(replacer.toString()).isEqualTo("You have 42 items");
        }

        @Test
        @DisplayName("Should replace with negative integers")
        void testNegativeIntegerReplacement() {
            Replacer replacer = new Replacer("Temperature: [TEMP] degrees");

            replacer.replace("[TEMP]", -5);

            assertThat(replacer.toString()).isEqualTo("Temperature: -5 degrees");
        }

        @Test
        @DisplayName("Should replace with zero")
        void testZeroReplacement() {
            Replacer replacer = new Replacer("Count: [COUNT]");

            replacer.replace("[COUNT]", 0);

            assertThat(replacer.toString()).isEqualTo("Count: 0");
        }
    }

    @Nested
    @DisplayName("Regular Expression Replacement Tests")
    class RegexReplacementTests {

        @Test
        @DisplayName("Should replace using regular expressions")
        void testRegexReplacement() {
            Replacer replacer = new Replacer("Phone: 123-456-7890");

            replacer.replaceRegex("\\d{3}-\\d{3}-\\d{4}", "XXX-XXX-XXXX");

            assertThat(replacer.toString()).isEqualTo("Phone: XXX-XXX-XXXX");
        }

        @Test
        @DisplayName("Should replace multiple matches with regex")
        void testMultipleRegexMatches() {
            Replacer replacer = new Replacer("Numbers: 123, 456, 789");

            replacer.replaceRegex("\\d+", "X");

            assertThat(replacer.toString()).isEqualTo("Numbers: X, X, X");
        }

        @Test
        @DisplayName("Should handle regex with capture groups")
        void testRegexWithCaptureGroups() {
            Replacer replacer = new Replacer("Date: 2023-12-25");

            replacer.replaceRegex("(\\d{4})-(\\d{2})-(\\d{2})", "$3/$2/$1");

            assertThat(replacer.toString()).isEqualTo("Date: 25/12/2023");
        }

        @Test
        @DisplayName("Should handle regex with no matches")
        void testRegexNoMatches() {
            Replacer replacer = new Replacer("Hello World");

            replacer.replaceRegex("\\d+", "NUMBER");

            assertThat(replacer.toString()).isEqualTo("Hello World");
        }
    }

    @Nested
    @DisplayName("Method Chaining Tests")
    class MethodChainingTests {

        @Test
        @DisplayName("Should support method chaining with CharSequence methods")
        void testCharSequenceMethodChaining() {
            Replacer replacer = new Replacer(sampleText);

            String result = replacer
                    .replace("[NAME]", "John")
                    .replace("[PLACE]", "Paris")
                    .replaceRegex("\\[COUNT\\]", "5")
                    .toString();

            assertThat(result).isEqualTo(expectedAfterReplacement);
        }

        @Test
        @DisplayName("Should handle integer replacements (void return)")
        void testIntegerReplacements() {
            Replacer replacer = new Replacer(sampleText);

            // Note: replace(String, int) returns void, so we can't chain it
            replacer.replace("[COUNT]", 5);

            String result = replacer
                    .replace("[NAME]", "John")
                    .replace("[PLACE]", "Paris")
                    .toString();

            assertThat(result).isEqualTo(expectedAfterReplacement);
        }

        @Test
        @DisplayName("Should chain regex replacements")
        void testRegexChaining() {
            Replacer replacer = new Replacer("abc123def456ghi");

            String result = replacer
                    .replaceRegex("\\d+", "X")
                    .replace("X", "NUM")
                    .toString();

            assertThat(result).isEqualTo("abcNUMdefNUMghi");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Special Characters Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle special characters in replacement")
        void testSpecialCharacters() {
            Replacer replacer = new Replacer("Quote: [QUOTE]");

            replacer.replace("[QUOTE]", "\"Hello $1 & World\"");

            assertThat(replacer.toString()).isEqualTo("Quote: \"Hello $1 & World\"");
        }

        @Test
        @DisplayName("Should handle newlines and whitespace")
        void testNewlinesAndWhitespace() {
            Replacer replacer = new Replacer("Line1\n[CONTENT]\nLine3");

            replacer.replace("[CONTENT]", "Line2\tTabbed");

            assertThat(replacer.toString()).isEqualTo("Line1\nLine2\tTabbed\nLine3");
        }

        @Test
        @DisplayName("Should handle Unicode characters")
        void testUnicodeCharacters() {
            Replacer replacer = new Replacer("Symbol: [SYM]");

            replacer.replace("[SYM]", "🚀 → ∞");

            assertThat(replacer.toString()).isEqualTo("Symbol: 🚀 → ∞");
        }

        @Test
        @DisplayName("Should handle overlapping replacements")
        void testOverlappingReplacements() {
            Replacer replacer = new Replacer("abcabc");

            replacer.replace("abc", "xyz");

            assertThat(replacer.toString()).isEqualTo("xyzxyz");
        }

        @Test
        @DisplayName("Should handle replacement that creates new matches")
        void testReplacementCreatingNewMatches() {
            Replacer replacer = new Replacer("test");

            // First replacement creates pattern that could match subsequent replacement
            replacer.replace("test", "best")
                    .replace("best", "rest");

            assertThat(replacer.toString()).isEqualTo("rest");
        }
    }

    @Nested
    @DisplayName("toString Method Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should return current string content")
        void testToStringMethod() {
            Replacer replacer = new Replacer("Original content");

            assertThat(replacer.toString()).isEqualTo("Original content");
        }

        @Test
        @DisplayName("Should reflect changes after replacements")
        void testToStringAfterReplacements() {
            Replacer replacer = new Replacer("Hello [NAME]");

            replacer.replace("[NAME]", "World");

            assertThat(replacer.toString()).isEqualTo("Hello World");
        }

        @Test
        @DisplayName("Should handle multiple toString calls")
        void testMultipleToStringCalls() {
            Replacer replacer = new Replacer("Test");

            String first = replacer.toString();
            String second = replacer.toString();

            assertThat(first).isEqualTo(second).isEqualTo("Test");
        }
    }
}
