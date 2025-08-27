package pt.up.fe.specs.util.parsing.arguments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.utilities.StringSlice;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for {@link Escape} class.
 * Tests escape sequence handling functionality for parsing frameworks.
 * 
 * @author Generated Tests
 */
@DisplayName("Escape Tests")
class EscapeTest {

    @Nested
    @DisplayName("Factory Method Tests")
    class FactoryMethodTests {

        @Test
        @DisplayName("Should create slash char escape with correct configuration")
        void testFactory_NewSlashChar_CorrectConfiguration() {
            // Act
            Escape escape = Escape.newSlashChar();

            // Assert
            assertThat(escape).isNotNull();

            // Test that it captures backslash escape sequences
            StringSlice slice = new StringSlice("\\n test");
            Optional<StringSlice> result = escape.captureEscape(slice);

            assertThat(result).isPresent();
            assertThat(result.get().toString()).isEqualTo("\\n");
        }

        @Test
        @DisplayName("Should handle empty escape start")
        void testFactory_EmptyEscapeStart_ValidConfiguration() {
            // Act
            Escape escape = new Escape("", slice -> slice.substring(0, 1));

            // Assert
            assertThat(escape).isNotNull();

            StringSlice slice = new StringSlice("test");
            Optional<StringSlice> result = escape.captureEscape(slice);

            assertThat(result).isPresent();
            assertThat(result.get().toString()).isEqualTo("t");
        }
    }

    @Nested
    @DisplayName("Escape Capture Tests")
    class EscapeCaptureTests {

        private Escape slashEscape;

        @BeforeEach
        void setUp() {
            slashEscape = Escape.newSlashChar();
        }

        @Test
        @DisplayName("Should capture basic escape sequence")
        void testCaptureEscape_BasicSequence_CapturedCorrectly() {
            // Act
            StringSlice slice = new StringSlice("\\t remaining");
            Optional<StringSlice> result = slashEscape.captureEscape(slice);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().toString()).isEqualTo("\\t");
        }

        @Test
        @DisplayName("Should capture escaped backslash")
        void testCaptureEscape_EscapedBackslash_CapturedCorrectly() {
            // Act
            StringSlice slice = new StringSlice("\\\\ remaining");
            Optional<StringSlice> result = slashEscape.captureEscape(slice);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().toString()).isEqualTo("\\\\");
        }

        @Test
        @DisplayName("Should capture escaped quote")
        void testCaptureEscape_EscapedQuote_CapturedCorrectly() {
            // Act
            StringSlice slice = new StringSlice("\\\" remaining");
            Optional<StringSlice> result = slashEscape.captureEscape(slice);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().toString()).isEqualTo("\\\"");
        }

        @Test
        @DisplayName("Should handle escape at end of string")
        void testCaptureEscape_EscapeAtEnd_CapturedCorrectly() {
            // Act
            StringSlice slice = new StringSlice("\\n");
            Optional<StringSlice> result = slashEscape.captureEscape(slice);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().toString()).isEqualTo("\\n");
        }

        @Test
        @DisplayName("Should not capture when escape start not found")
        void testCaptureEscape_NoEscapeStart_NotCaptured() {
            // Act
            StringSlice slice = new StringSlice("normal text");
            Optional<StringSlice> result = slashEscape.captureEscape(slice);

            // Assert
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should not capture when only escape start")
        void testCaptureEscape_OnlyEscapeStart_NotCaptured() {
            // Act
            StringSlice slice = new StringSlice("\\");

            // Assert - Implementation throws IndexOutOfBoundsException for incomplete
            // escape
            assertThatThrownBy(() -> slashEscape.captureEscape(slice))
                    .isInstanceOf(IndexOutOfBoundsException.class);
        }
    }

    @Nested
    @DisplayName("Custom Escape Tests")
    class CustomEscapeTests {

        @Test
        @DisplayName("Should handle custom escape start character")
        void testCustomEscape_DifferentStartChar_WorksCorrectly() {
            // Arrange
            Escape customEscape = new Escape("^", slice -> slice.substring(0, 2));

            // Act
            StringSlice slice = new StringSlice("^n test");
            Optional<StringSlice> result = customEscape.captureEscape(slice);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().toString()).isEqualTo("^n");
        }

        @Test
        @DisplayName("Should handle multi-character escape start")
        void testCustomEscape_MultiCharStart_WorksCorrectly() {
            // Arrange
            Escape customEscape = new Escape("$$", slice -> slice.substring(0, 3));

            // Act
            StringSlice slice = new StringSlice("$$n test");
            Optional<StringSlice> result = customEscape.captureEscape(slice);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().toString()).isEqualTo("$$n");
        }

        @Test
        @DisplayName("Should handle variable length capture")
        void testCustomEscape_VariableLength_WorksCorrectly() {
            // Arrange - Escape until space or end
            Escape variableEscape = new Escape("&", slice -> {
                int spaceIndex = slice.toString().indexOf(' ');
                int endIndex = spaceIndex == -1 ? slice.length() : spaceIndex;
                return slice.substring(0, Math.max(1, endIndex));
            });

            // Act
            StringSlice slice = new StringSlice("&variable test");
            Optional<StringSlice> result = variableEscape.captureEscape(slice);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().toString()).isEqualTo("&variable");
        }

        @Test
        @DisplayName("Should handle null escape capturer gracefully")
        void testCustomEscape_NullCapturer_ThrowsException() {
            // Act - Implementation allows null during construction
            Escape escape = new Escape("\\", null);

            // Assert - Exception only thrown when captureEscape is used
            StringSlice slice = new StringSlice("\\t test");
            assertThatThrownBy(() -> escape.captureEscape(slice))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle empty string input")
        void testCaptureEscape_EmptyString_NotCaptured() {
            // Arrange
            Escape escape = Escape.newSlashChar();

            // Act
            StringSlice slice = new StringSlice("");
            Optional<StringSlice> result = escape.captureEscape(slice);

            // Assert
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should handle special unicode characters")
        void testCaptureEscape_UnicodeChars_CapturedCorrectly() {
            // Arrange
            Escape escape = Escape.newSlashChar();

            // Act
            StringSlice slice = new StringSlice("\\u2603 snowman");
            Optional<StringSlice> result = escape.captureEscape(slice);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().toString()).isEqualTo("\\u");
        }

        @Test
        @DisplayName("Should handle escape start at various positions")
        void testCaptureEscape_EscapeAtPosition_OnlyFirstCaptured() {
            // Arrange
            Escape escape = Escape.newSlashChar();

            // Act
            StringSlice slice = new StringSlice("abc\\def\\ghi");
            Optional<StringSlice> result = escape.captureEscape(slice);

            // Assert - Should not capture since escape start is not at beginning
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should handle consecutive escape sequences")
        void testCaptureEscape_ConsecutiveEscapes_OnlyFirstCaptured() {
            // Arrange
            Escape escape = Escape.newSlashChar();

            // Act
            StringSlice slice = new StringSlice("\\\\\\n test");
            Optional<StringSlice> result = escape.captureEscape(slice);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().toString()).isEqualTo("\\\\");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with StringSlice operations")
        void testIntegration_WithStringSlice_WorksCorrectly() {
            // Arrange
            Escape escape = Escape.newSlashChar();
            StringSlice originalSlice = new StringSlice("prefix \\n suffix");

            // Act
            StringSlice substringSlice = originalSlice.substring(7); // Start at "\n suffix"
            Optional<StringSlice> result = escape.captureEscape(substringSlice);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().toString()).isEqualTo("\\n");
        }

        @Test
        @DisplayName("Should handle multiple escape types together")
        void testIntegration_MultipleEscapeTypes_WorkIndependently() {
            // Arrange
            Escape backslashEscape = Escape.newSlashChar();
            Escape caretEscape = new Escape("^", slice -> slice.substring(0, 2));

            // Act
            StringSlice backslashSlice = new StringSlice("\\t test");
            StringSlice caretSlice = new StringSlice("^n test");

            Optional<StringSlice> backslashResult = backslashEscape.captureEscape(backslashSlice);
            Optional<StringSlice> caretResult = caretEscape.captureEscape(caretSlice);

            // Assert
            assertThat(backslashResult).isPresent();
            assertThat(backslashResult.get().toString()).isEqualTo("\\t");

            assertThat(caretResult).isPresent();
            assertThat(caretResult.get().toString()).isEqualTo("^n");
        }

        @Test
        @DisplayName("Should maintain slice position information")
        void testIntegration_SlicePosition_MaintainedCorrectly() {
            // Arrange
            Escape escape = Escape.newSlashChar();
            StringSlice slice = new StringSlice("\\t remaining text");

            // Act
            Optional<StringSlice> capturedEscape = escape.captureEscape(slice);

            // Assert
            assertThat(capturedEscape).isPresent();
            assertThat(capturedEscape.get().toString()).isEqualTo("\\t");

            // Remaining slice should be after the captured escape
            StringSlice remaining = slice.substring(capturedEscape.get().length());
            assertThat(remaining.toString()).isEqualTo(" remaining text");
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should handle large strings efficiently")
        void testPerformance_LargeString_ProcessedEfficiently() {
            // Arrange
            Escape escape = Escape.newSlashChar();
            StringBuilder largeString = new StringBuilder("\\n");
            for (int i = 0; i < 10000; i++) {
                largeString.append("x");
            }

            // Act
            long startTime = System.nanoTime();
            StringSlice slice = new StringSlice(largeString.toString());
            Optional<StringSlice> result = escape.captureEscape(slice);
            long endTime = System.nanoTime();

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().toString()).isEqualTo("\\n");
            assertThat(endTime - startTime).isLessThan(1_000_000); // Less than 1ms
        }

        @Test
        @DisplayName("Should handle many small captures efficiently")
        void testPerformance_ManyCaptures_ProcessedEfficiently() {
            // Arrange
            Escape escape = Escape.newSlashChar();

            // Act
            long startTime = System.nanoTime();
            for (int i = 0; i < 1000; i++) {
                StringSlice slice = new StringSlice("\\t test");
                Optional<StringSlice> result = escape.captureEscape(slice);
                assertThat(result).isPresent();
            }
            long endTime = System.nanoTime();

            // Assert
            assertThat(endTime - startTime).isLessThan(10_000_000); // Less than 10ms
        }
    }
}
