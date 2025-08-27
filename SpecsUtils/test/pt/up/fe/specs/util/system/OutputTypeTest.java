package pt.up.fe.specs.util.system;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for the OutputType enum.
 * Tests output type behavior, printing functionality, and enum properties.
 * 
 * @author Generated Tests
 */
public class OutputTypeTest {

    private PrintStream originalOut;
    private PrintStream originalErr;
    private ByteArrayOutputStream capturedOut;
    private ByteArrayOutputStream capturedErr;

    @BeforeEach
    void setUp() {
        // Capture original streams
        originalOut = System.out;
        originalErr = System.err;

        // Create capture streams
        capturedOut = new ByteArrayOutputStream();
        capturedErr = new ByteArrayOutputStream();

        // Redirect System streams
        System.setOut(new PrintStream(capturedOut));
        System.setErr(new PrintStream(capturedErr));
    }

    @AfterEach
    void tearDown() {
        // Restore original streams
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Nested
    @DisplayName("Enum Basic Properties Tests")
    class EnumBasicPropertiesTests {

        @Test
        @DisplayName("Should have exactly two enum values")
        void testEnumValues() {
            OutputType[] values = OutputType.values();

            assertThat(values).hasSize(2);
            assertThat(values).containsExactlyInAnyOrder(OutputType.StdOut, OutputType.StdErr);
        }

        @Test
        @DisplayName("Should support valueOf operation")
        void testValueOf() {
            assertThat(OutputType.valueOf("StdOut")).isEqualTo(OutputType.StdOut);
            assertThat(OutputType.valueOf("StdErr")).isEqualTo(OutputType.StdErr);
        }

        @Test
        @DisplayName("Should throw exception for invalid valueOf")
        void testInvalidValueOf() {
            assertThatCode(() -> OutputType.valueOf("Invalid"))
                    .isInstanceOf(IllegalArgumentException.class);

            assertThatCode(() -> OutputType.valueOf("stdout"))
                    .isInstanceOf(IllegalArgumentException.class);

            assertThatCode(() -> OutputType.valueOf("STDOUT"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should have consistent toString representation")
        void testToString() {
            assertThat(OutputType.StdOut.toString()).isEqualTo("StdOut");
            assertThat(OutputType.StdErr.toString()).isEqualTo("StdErr");
        }

        @Test
        @DisplayName("Should have consistent name representation")
        void testName() {
            assertThat(OutputType.StdOut.name()).isEqualTo("StdOut");
            assertThat(OutputType.StdErr.name()).isEqualTo("StdErr");
        }

        @Test
        @DisplayName("Should support ordinal values")
        void testOrdinal() {
            // Test that ordinals are assigned consistently
            assertThat(OutputType.StdOut.ordinal()).isEqualTo(0);
            assertThat(OutputType.StdErr.ordinal()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("StdOut Print Functionality Tests")
    class StdOutPrintTests {

        @Test
        @DisplayName("Should print simple string to stdout")
        void testStdOutSimpleString() {
            String message = "Hello stdout";

            OutputType.StdOut.print(message);

            assertThat(capturedOut.toString()).isEqualTo(message);
            assertThat(capturedErr.toString()).isEmpty();
        }

        @Test
        @DisplayName("Should print empty string to stdout")
        void testStdOutEmptyString() {
            OutputType.StdOut.print("");

            assertThat(capturedOut.toString()).isEmpty();
            assertThat(capturedErr.toString()).isEmpty();
        }

        @Test
        @DisplayName("Should handle null string in stdout")
        void testStdOutNullString() {
            OutputType.StdOut.print(null);

            assertThat(capturedOut.toString()).isEqualTo("null");
            assertThat(capturedErr.toString()).isEmpty();
        }

        @Test
        @DisplayName("Should print multiline text to stdout")
        void testStdOutMultilineText() {
            String multiline = "Line 1\nLine 2\nLine 3";

            OutputType.StdOut.print(multiline);

            assertThat(capturedOut.toString()).isEqualTo(multiline);
            assertThat(capturedErr.toString()).isEmpty();
        }

        @Test
        @DisplayName("Should print special characters to stdout")
        void testStdOutSpecialCharacters() {
            String special = "Tab:\tNewline:\nCarriage return:\rUnicode:\u03B1\u03B2\u03B3";

            OutputType.StdOut.print(special);

            assertThat(capturedOut.toString()).isEqualTo(special);
            assertThat(capturedErr.toString()).isEmpty();
        }

        @Test
        @DisplayName("Should handle very long strings in stdout")
        void testStdOutLongString() {
            StringBuilder longString = new StringBuilder();
            for (int i = 0; i < 10000; i++) {
                longString.append("Character ").append(i).append(" ");
            }

            OutputType.StdOut.print(longString.toString());

            assertThat(capturedOut.toString()).isEqualTo(longString.toString());
            assertThat(capturedErr.toString()).isEmpty();
        }

        @Test
        @DisplayName("Should handle multiple consecutive prints to stdout")
        void testStdOutMultiplePrints() {
            OutputType.StdOut.print("First");
            OutputType.StdOut.print("Second");
            OutputType.StdOut.print("Third");

            assertThat(capturedOut.toString()).isEqualTo("FirstSecondThird");
            assertThat(capturedErr.toString()).isEmpty();
        }
    }

    @Nested
    @DisplayName("StdErr Print Functionality Tests")
    class StdErrPrintTests {

        @Test
        @DisplayName("Should print simple string to stderr")
        void testStdErrSimpleString() {
            String message = "Hello stderr";

            OutputType.StdErr.print(message);

            assertThat(capturedErr.toString()).isEqualTo(message);
            assertThat(capturedOut.toString()).isEmpty();
        }

        @Test
        @DisplayName("Should print empty string to stderr")
        void testStdErrEmptyString() {
            OutputType.StdErr.print("");

            assertThat(capturedErr.toString()).isEmpty();
            assertThat(capturedOut.toString()).isEmpty();
        }

        @Test
        @DisplayName("Should handle null string in stderr")
        void testStdErrNullString() {
            OutputType.StdErr.print(null);

            assertThat(capturedErr.toString()).isEqualTo("null");
            assertThat(capturedOut.toString()).isEmpty();
        }

        @Test
        @DisplayName("Should print multiline text to stderr")
        void testStdErrMultilineText() {
            String multiline = "Error line 1\nError line 2\nError line 3";

            OutputType.StdErr.print(multiline);

            assertThat(capturedErr.toString()).isEqualTo(multiline);
            assertThat(capturedOut.toString()).isEmpty();
        }

        @Test
        @DisplayName("Should print special characters to stderr")
        void testStdErrSpecialCharacters() {
            String special = "Error Tab:\tError Newline:\nError Unicode:\u2603\u2764";

            OutputType.StdErr.print(special);

            assertThat(capturedErr.toString()).isEqualTo(special);
            assertThat(capturedOut.toString()).isEmpty();
        }

        @Test
        @DisplayName("Should handle very long strings in stderr")
        void testStdErrLongString() {
            StringBuilder longString = new StringBuilder();
            for (int i = 0; i < 5000; i++) {
                longString.append("Error ").append(i).append(" ");
            }

            OutputType.StdErr.print(longString.toString());

            assertThat(capturedErr.toString()).isEqualTo(longString.toString());
            assertThat(capturedOut.toString()).isEmpty();
        }

        @Test
        @DisplayName("Should handle multiple consecutive prints to stderr")
        void testStdErrMultiplePrints() {
            OutputType.StdErr.print("Error1");
            OutputType.StdErr.print("Error2");
            OutputType.StdErr.print("Error3");

            assertThat(capturedErr.toString()).isEqualTo("Error1Error2Error3");
            assertThat(capturedOut.toString()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Mixed Output Tests")
    class MixedOutputTests {

        @Test
        @DisplayName("Should handle alternating stdout and stderr prints")
        void testAlternatingPrints() {
            OutputType.StdOut.print("Out1");
            OutputType.StdErr.print("Err1");
            OutputType.StdOut.print("Out2");
            OutputType.StdErr.print("Err2");

            assertThat(capturedOut.toString()).isEqualTo("Out1Out2");
            assertThat(capturedErr.toString()).isEqualTo("Err1Err2");
        }

        @Test
        @DisplayName("Should maintain separation between stdout and stderr")
        void testOutputSeparation() {
            String stdoutMessage = "This goes to stdout";
            String stderrMessage = "This goes to stderr";

            OutputType.StdOut.print(stdoutMessage);
            OutputType.StdErr.print(stderrMessage);

            assertThat(capturedOut.toString()).isEqualTo(stdoutMessage);
            assertThat(capturedErr.toString()).isEqualTo(stderrMessage);

            // Verify no cross-contamination
            assertThat(capturedOut.toString()).doesNotContain(stderrMessage);
            assertThat(capturedErr.toString()).doesNotContain(stdoutMessage);
        }

        @Test
        @DisplayName("Should handle simultaneous output types")
        void testSimultaneousOutput() {
            // Simulate what might happen in multi-threaded environment
            for (int i = 0; i < 100; i++) {
                OutputType.StdOut.print("O" + i);
                OutputType.StdErr.print("E" + i);
            }

            String stdoutContent = capturedOut.toString();
            String stderrContent = capturedErr.toString();

            // Verify all stdout messages are present
            for (int i = 0; i < 100; i++) {
                assertThat(stdoutContent).contains("O" + i);
            }

            // Verify all stderr messages are present
            for (int i = 0; i < 100; i++) {
                assertThat(stderrContent).contains("E" + i);
            }

            // Verify no cross-contamination
            assertThat(stdoutContent).doesNotContain("E");
            assertThat(stderrContent).doesNotContain("O");
        }
    }

    @Nested
    @DisplayName("Abstract Method Implementation Tests")
    class AbstractMethodImplementationTests {

        @Test
        @DisplayName("Should implement abstract print method correctly for StdOut")
        void testStdOutAbstractImplementation() {
            // Verify that StdOut properly implements the abstract print method
            assertThatCode(() -> OutputType.StdOut.print("test"))
                    .doesNotThrowAnyException();

            assertThat(capturedOut.toString()).isEqualTo("test");
        }

        @Test
        @DisplayName("Should implement abstract print method correctly for StdErr")
        void testStdErrAbstractImplementation() {
            // Verify that StdErr properly implements the abstract print method
            assertThatCode(() -> OutputType.StdErr.print("test"))
                    .doesNotThrowAnyException();

            assertThat(capturedErr.toString()).isEqualTo("test");
        }

        @Test
        @DisplayName("Should handle method calls through enum references")
        void testMethodCallsThroughReferences() {
            OutputType stdout = OutputType.StdOut;
            OutputType stderr = OutputType.StdErr;

            stdout.print("reference_out");
            stderr.print("reference_err");

            assertThat(capturedOut.toString()).isEqualTo("reference_out");
            assertThat(capturedErr.toString()).isEqualTo("reference_err");
        }

        @Test
        @DisplayName("Should support polymorphic method calls")
        void testPolymorphicMethodCalls() {
            OutputType[] types = { OutputType.StdOut, OutputType.StdErr };
            String[] messages = { "poly_out", "poly_err" };

            for (int i = 0; i < types.length; i++) {
                types[i].print(messages[i]);
            }

            assertThat(capturedOut.toString()).isEqualTo("poly_out");
            assertThat(capturedErr.toString()).isEqualTo("poly_err");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Boundary Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle rapid successive calls")
        void testRapidSuccessiveCalls() {
            // Test rapid calls to ensure no buffering issues
            for (int i = 0; i < 1000; i++) {
                OutputType.StdOut.print(String.valueOf(i));
            }

            String output = capturedOut.toString();
            for (int i = 0; i < 1000; i++) {
                assertThat(output).contains(String.valueOf(i));
            }
        }

        @Test
        @DisplayName("Should handle strings with only whitespace")
        void testWhitespaceOnlyStrings() {
            String spaces = "    ";
            String tabs = "\t\t\t";
            String newlines = "\n\n\n";
            String mixed = " \t\n \t\n ";

            OutputType.StdOut.print(spaces);
            OutputType.StdOut.print(tabs);
            OutputType.StdErr.print(newlines);
            OutputType.StdErr.print(mixed);

            assertThat(capturedOut.toString()).isEqualTo(spaces + tabs);
            assertThat(capturedErr.toString()).isEqualTo(newlines + mixed);
        }

        @Test
        @DisplayName("Should handle binary data represented as strings")
        void testBinaryDataStrings() {
            // Test strings that might contain binary-like data
            String binaryLike = "\0\1\2\3\4\5\6\7\b\t\n\u000B\f\r";

            OutputType.StdOut.print(binaryLike);

            assertThat(capturedOut.toString()).isEqualTo(binaryLike);
        }

        @Test
        @DisplayName("Should handle Unicode edge cases")
        void testUnicodeEdgeCases() {
            // Test various Unicode ranges
            String unicode = "\u0000\u007F\u0080\u00FF\u0100\u017F\u0180\u024F" +
                    "\u1E00\u1EFF\u2000\u206F\u2070\u209F\u20A0\u20CF" +
                    "\uFB00\uFB4F\uFE20\uFE2F\uFE30\uFE4F\uFE50\uFE6F";

            OutputType.StdErr.print(unicode);

            assertThat(capturedErr.toString()).isEqualTo(unicode);
        }

        @Test
        @DisplayName("Should handle extremely large single string")
        void testExtremeleLargeString() {
            // Create a very large string to test memory handling
            StringBuilder huge = new StringBuilder();
            String pattern = "0123456789ABCDEF";
            for (int i = 0; i < 100000; i++) {
                huge.append(pattern);
            }

            String hugeString = huge.toString();
            OutputType.StdOut.print(hugeString);

            assertThat(capturedOut.toString()).isEqualTo(hugeString);
            assertThat(capturedOut.toString().length()).isEqualTo(hugeString.length());
        }
    }

    @Nested
    @DisplayName("Enum Consistency Tests")
    class EnumConsistencyTests {

        @Test
        @DisplayName("Should maintain enum singleton property")
        void testEnumSingleton() {
            OutputType stdout1 = OutputType.StdOut;
            OutputType stdout2 = OutputType.valueOf("StdOut");
            OutputType stderr1 = OutputType.StdErr;
            OutputType stderr2 = OutputType.valueOf("StdErr");

            assertThat(stdout1).isSameAs(stdout2);
            assertThat(stderr1).isSameAs(stderr2);
        }

        @Test
        @DisplayName("Should support equality operations")
        void testEnumEquality() {
            assertThat(OutputType.StdOut).isEqualTo(OutputType.StdOut);
            assertThat(OutputType.StdErr).isEqualTo(OutputType.StdErr);
            assertThat(OutputType.StdOut).isNotEqualTo(OutputType.StdErr);
            assertThat(OutputType.StdErr).isNotEqualTo(OutputType.StdOut);
        }

        @Test
        @DisplayName("Should support hashCode consistency")
        void testHashCodeConsistency() {
            assertThat(OutputType.StdOut.hashCode()).isEqualTo(OutputType.StdOut.hashCode());
            assertThat(OutputType.StdErr.hashCode()).isEqualTo(OutputType.StdErr.hashCode());

            // Hash codes should be different for different enum values
            assertThat(OutputType.StdOut.hashCode()).isNotEqualTo(OutputType.StdErr.hashCode());
        }
    }
}
