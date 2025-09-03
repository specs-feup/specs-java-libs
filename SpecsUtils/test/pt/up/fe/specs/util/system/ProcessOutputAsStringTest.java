package pt.up.fe.specs.util.system;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for the ProcessOutputAsString class.
 * Tests string-based process output handling, concatenation, and edge cases.
 * 
 * @author Generated Tests
 */
public class ProcessOutputAsStringTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create instance with valid parameters")
        void testValidConstructor() {
            ProcessOutputAsString output = new ProcessOutputAsString(0, "stdout", "stderr");

            assertThat(output).isNotNull();
            assertThat(output.getReturnValue()).isZero();
            assertThat(output.getStdOut()).isEqualTo("stdout");
            assertThat(output.getStdErr()).isEqualTo("stderr");
        }

        @Test
        @DisplayName("Should handle null stdout")
        void testNullStdout() {
            ProcessOutputAsString output = new ProcessOutputAsString(0, null, "stderr");

            assertThat(output.getStdOut()).isNull();
            assertThat(output.getStdErr()).isEqualTo("stderr");
        }

        @Test
        @DisplayName("Should handle null stderr")
        void testNullStderr() {
            ProcessOutputAsString output = new ProcessOutputAsString(0, "stdout", null);

            assertThat(output.getStdOut()).isEqualTo("stdout");
            assertThat(output.getStdErr()).isNull();
        }

        @Test
        @DisplayName("Should handle both null outputs")
        void testBothNullOutputs() {
            ProcessOutputAsString output = new ProcessOutputAsString(0, null, null);

            assertThat(output.getStdOut()).isNull();
            assertThat(output.getStdErr()).isNull();
        }

        @Test
        @DisplayName("Should handle empty strings")
        void testEmptyStrings() {
            ProcessOutputAsString output = new ProcessOutputAsString(0, "", "");

            assertThat(output.getStdOut()).isEmpty();
            assertThat(output.getStdErr()).isEmpty();
        }

        @Test
        @DisplayName("Should handle various return values")
        void testVariousReturnValues() {
            ProcessOutputAsString success = new ProcessOutputAsString(0, "out", "err");
            ProcessOutputAsString error = new ProcessOutputAsString(1, "out", "err");
            ProcessOutputAsString negativeError = new ProcessOutputAsString(-1, "out", "err");
            ProcessOutputAsString largeError = new ProcessOutputAsString(255, "out", "err");

            assertThat(success.getReturnValue()).isZero();
            assertThat(error.getReturnValue()).isEqualTo(1);
            assertThat(negativeError.getReturnValue()).isEqualTo(-1);
            assertThat(largeError.getReturnValue()).isEqualTo(255);
        }
    }

    @Nested
    @DisplayName("GetOutput Method Tests")
    class GetOutputMethodTests {

        @Test
        @DisplayName("Should concatenate stdout and stderr with newline")
        void testBasicConcatenation() {
            ProcessOutputAsString output = new ProcessOutputAsString(0, "stdout content", "stderr content");

            String result = output.getOutput();

            assertThat(result).isEqualTo("stdout content\nstderr content");
        }

        @Test
        @DisplayName("Should handle null stdout")
        void testNullStdoutInGetOutput() {
            ProcessOutputAsString output = new ProcessOutputAsString(0, null, "stderr content");

            String result = output.getOutput();

            assertThat(result).isEqualTo("null\nstderr content");
        }

        @Test
        @DisplayName("Should handle null stderr")
        void testNullStderrInGetOutput() {
            ProcessOutputAsString output = new ProcessOutputAsString(0, "stdout content", null);

            String result = output.getOutput();

            assertThat(result).isEqualTo("stdout content\nnull");
        }

        @Test
        @DisplayName("Should handle both null outputs")
        void testBothNullInGetOutput() {
            ProcessOutputAsString output = new ProcessOutputAsString(0, null, null);

            String result = output.getOutput();

            assertThat(result).isEqualTo("null\nnull");
        }

        @Test
        @DisplayName("Should handle empty stdout")
        void testEmptyStdoutInGetOutput() {
            ProcessOutputAsString output = new ProcessOutputAsString(0, "", "stderr content");

            String result = output.getOutput();

            assertThat(result).isEqualTo("\nstderr content");
        }

        @Test
        @DisplayName("Should handle empty stderr")
        void testEmptyStderrInGetOutput() {
            ProcessOutputAsString output = new ProcessOutputAsString(0, "stdout content", "");

            String result = output.getOutput();

            assertThat(result).isEqualTo("stdout content\n");
        }

        @Test
        @DisplayName("Should handle both empty outputs")
        void testBothEmptyInGetOutput() {
            ProcessOutputAsString output = new ProcessOutputAsString(0, "", "");

            String result = output.getOutput();

            assertThat(result).isEqualTo("\n");
        }

        @Test
        @DisplayName("Should preserve existing newlines in stdout")
        void testStdoutWithNewlines() {
            ProcessOutputAsString output = new ProcessOutputAsString(0, "line1\nline2\nline3", "error");

            String result = output.getOutput();

            assertThat(result).isEqualTo("line1\nline2\nline3\nerror");
        }

        @Test
        @DisplayName("Should preserve existing newlines in stderr")
        void testStderrWithNewlines() {
            ProcessOutputAsString output = new ProcessOutputAsString(0, "output", "error1\nerror2\nerror3");

            String result = output.getOutput();

            assertThat(result).isEqualTo("output\nerror1\nerror2\nerror3");
        }

        @Test
        @DisplayName("Should handle newlines in both outputs")
        void testBothWithNewlines() {
            ProcessOutputAsString output = new ProcessOutputAsString(0, "out1\nout2", "err1\nerr2");

            String result = output.getOutput();

            assertThat(result).isEqualTo("out1\nout2\nerr1\nerr2");
        }

        @Test
        @DisplayName("Should handle stdout ending with newline")
        void testStdoutEndingWithNewline() {
            ProcessOutputAsString output = new ProcessOutputAsString(0, "stdout\n", "stderr");

            String result = output.getOutput();

            assertThat(result).isEqualTo("stdout\n\nstderr");
        }

        @Test
        @DisplayName("Should handle stderr ending with newline")
        void testStderrEndingWithNewline() {
            ProcessOutputAsString output = new ProcessOutputAsString(0, "stdout", "stderr\n");

            String result = output.getOutput();

            assertThat(result).isEqualTo("stdout\nstderr\n");
        }

        @Test
        @DisplayName("Should handle both outputs ending with newlines")
        void testBothEndingWithNewlines() {
            ProcessOutputAsString output = new ProcessOutputAsString(0, "stdout\n", "stderr\n");

            String result = output.getOutput();

            assertThat(result).isEqualTo("stdout\n\nstderr\n");
        }
    }

    @Nested
    @DisplayName("Inheritance Tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should inherit from ProcessOutput")
        void testInheritance() {
            ProcessOutputAsString output = new ProcessOutputAsString(0, "stdout", "stderr");

            assertThat(output).isInstanceOf(ProcessOutput.class);
            assertThat(output).isInstanceOf(ProcessOutput.class);
        }

        @Test
        @DisplayName("Should inherit isError method")
        void testInheritedIsError() {
            ProcessOutputAsString success = new ProcessOutputAsString(0, "out", "err");
            ProcessOutputAsString error = new ProcessOutputAsString(1, "out", "err");

            assertThat(success.isError()).isFalse();
            assertThat(error.isError()).isTrue();
        }

        @Test
        @DisplayName("Should inherit toString method")
        void testInheritedToString() {
            ProcessOutputAsString output = new ProcessOutputAsString(42, "test_out", "test_err");

            String stringResult = output.toString();

            assertThat(stringResult).contains("Return value: 42");
            assertThat(stringResult).contains("StdOut: test_out");
            assertThat(stringResult).contains("StdErr: test_err");
        }

        @Test
        @DisplayName("Should inherit getOutputException method")
        void testInheritedGetOutputException() {
            ProcessOutputAsString output = new ProcessOutputAsString(0, "out", "err");

            assertThat(output.getOutputException()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Special Characters and Unicode Tests")
    class SpecialCharactersTests {

        @Test
        @DisplayName("Should handle special characters in stdout")
        void testSpecialCharactersInStdout() {
            String specialStdout = "Tab:\tNewline:\nCarriage return:\rBackspace:\b";
            ProcessOutputAsString output = new ProcessOutputAsString(0, specialStdout, "normal");

            String result = output.getOutput();

            assertThat(result).isEqualTo(specialStdout + "\nnormal");
        }

        @Test
        @DisplayName("Should handle special characters in stderr")
        void testSpecialCharactersInStderr() {
            String specialStderr = "Form feed:\fVertical tab:\u000BNull:\0";
            ProcessOutputAsString output = new ProcessOutputAsString(0, "normal", specialStderr);

            String result = output.getOutput();

            assertThat(result).isEqualTo("normal\n" + specialStderr);
        }

        @Test
        @DisplayName("Should handle Unicode characters")
        void testUnicodeCharacters() {
            String unicodeStdout = "Greek: \u03B1\u03B2\u03B3 Japanese: \u3042\u3044\u3046";
            String unicodeStderr = "Emoji: \uD83D\uDE00\uD83D\uDE01 Math: \u2200\u2203\u221E";

            ProcessOutputAsString output = new ProcessOutputAsString(0, unicodeStdout, unicodeStderr);

            String result = output.getOutput();

            assertThat(result).isEqualTo(unicodeStdout + "\n" + unicodeStderr);
        }

        @Test
        @DisplayName("Should handle mixed special and Unicode characters")
        void testMixedSpecialAndUnicode() {
            String mixed = "Mixed: \t\u03B1\n\uD83D\uDE00\r\u2603";
            ProcessOutputAsString output = new ProcessOutputAsString(0, mixed, mixed);

            String result = output.getOutput();

            assertThat(result).isEqualTo(mixed + "\n" + mixed);
        }
    }

    @Nested
    @DisplayName("Large Data Tests")
    class LargeDataTests {

        @Test
        @DisplayName("Should handle large stdout")
        void testLargeStdout() {
            StringBuilder largeStdout = new StringBuilder();
            for (int i = 0; i < 10000; i++) {
                largeStdout.append("Line ").append(i).append(" of stdout\n");
            }

            ProcessOutputAsString output = new ProcessOutputAsString(0, largeStdout.toString(), "small stderr");

            String result = output.getOutput();

            assertThat(result).startsWith(largeStdout.toString());
            assertThat(result).endsWith("\nsmall stderr");
            assertThat(result.length()).isEqualTo(largeStdout.length() + 1 + "small stderr".length());
        }

        @Test
        @DisplayName("Should handle large stderr")
        void testLargeStderr() {
            StringBuilder largeStderr = new StringBuilder();
            for (int i = 0; i < 5000; i++) {
                largeStderr.append("Error ").append(i).append(" description\n");
            }

            ProcessOutputAsString output = new ProcessOutputAsString(0, "small stdout", largeStderr.toString());

            String result = output.getOutput();

            assertThat(result).startsWith("small stdout\n");
            assertThat(result).endsWith(largeStderr.toString());
        }

        @Test
        @DisplayName("Should handle both large outputs")
        void testBothLargeOutputs() {
            StringBuilder largeOut = new StringBuilder();
            StringBuilder largeErr = new StringBuilder();

            for (int i = 0; i < 1000; i++) {
                largeOut.append("Stdout line ").append(i).append("\n");
                largeErr.append("Stderr line ").append(i).append("\n");
            }

            ProcessOutputAsString output = new ProcessOutputAsString(0, largeOut.toString(), largeErr.toString());

            String result = output.getOutput();

            assertThat(result).startsWith(largeOut.toString());
            assertThat(result).contains("\n" + largeErr.toString());
        }
    }

    @Nested
    @DisplayName("Edge Cases and Boundary Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle whitespace-only outputs")
        void testWhitespaceOnlyOutputs() {
            ProcessOutputAsString spacesOut = new ProcessOutputAsString(0, "   ", "   ");
            ProcessOutputAsString tabsOut = new ProcessOutputAsString(0, "\t\t", "\t\t");
            ProcessOutputAsString newlinesOut = new ProcessOutputAsString(0, "\n\n", "\n\n");

            assertThat(spacesOut.getOutput()).isEqualTo("   \n   ");
            assertThat(tabsOut.getOutput()).isEqualTo("\t\t\n\t\t");
            assertThat(newlinesOut.getOutput()).isEqualTo("\n\n\n\n\n");
        }

        @Test
        @DisplayName("Should handle very long single line")
        void testVeryLongSingleLine() {
            StringBuilder longLine = new StringBuilder();
            for (int i = 0; i < 100000; i++) {
                longLine.append("a");
            }

            ProcessOutputAsString output = new ProcessOutputAsString(0, longLine.toString(), "short");

            String result = output.getOutput();

            assertThat(result).hasSize(longLine.length() + 1 + 5); // +1 for newline, +5 for "short"
            assertThat(result).startsWith(longLine.toString());
            assertThat(result).endsWith("\nshort");
        }

        @Test
        @DisplayName("Should handle extreme return values")
        void testExtremeReturnValues() {
            ProcessOutputAsString maxInt = new ProcessOutputAsString(Integer.MAX_VALUE, "out", "err");
            ProcessOutputAsString minInt = new ProcessOutputAsString(Integer.MIN_VALUE, "out", "err");

            assertThat(maxInt.getReturnValue()).isEqualTo(Integer.MAX_VALUE);
            assertThat(minInt.getReturnValue()).isEqualTo(Integer.MIN_VALUE);

            // getOutput should work regardless of return value
            assertThat(maxInt.getOutput()).isEqualTo("out\nerr");
            assertThat(minInt.getOutput()).isEqualTo("out\nerr");
        }

        @Test
        @DisplayName("Should handle repeated newlines")
        void testRepeatedNewlines() {
            ProcessOutputAsString output = new ProcessOutputAsString(0, "\n\n\n", "\n\n\n");

            String result = output.getOutput();

            assertThat(result).isEqualTo("\n\n\n\n\n\n\n");

            // Count newlines
            long newlineCount = result.chars().filter(ch -> ch == '\n').count();
            assertThat(newlineCount).isEqualTo(7); // 3 + 1 (separator) + 3
        }

        @Test
        @DisplayName("Should not throw exceptions for any valid inputs")
        void testNoExceptions() {
            // Test various combinations that should never throw exceptions
            assertThatCode(() -> new ProcessOutputAsString(0, null, null)).doesNotThrowAnyException();
            assertThatCode(() -> new ProcessOutputAsString(-1, "", "")).doesNotThrowAnyException();
            assertThatCode(() -> new ProcessOutputAsString(Integer.MAX_VALUE, "test", null)).doesNotThrowAnyException();

            ProcessOutputAsString output = new ProcessOutputAsString(0, "test", "error");
            assertThatCode(() -> output.getOutput()).doesNotThrowAnyException();
            assertThatCode(() -> output.toString()).doesNotThrowAnyException();
            assertThatCode(() -> output.getStdOut()).doesNotThrowAnyException();
            assertThatCode(() -> output.getStdErr()).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("String Concatenation Behavior Tests")
    class ConcatenationBehaviorTests {

        @Test
        @DisplayName("Should always add exactly one newline between stdout and stderr")
        void testNewlineSeparatorConsistency() {
            // Test various cases to ensure exactly one newline is always added
            ProcessOutputAsString case1 = new ProcessOutputAsString(0, "no_newline", "no_newline");
            ProcessOutputAsString case2 = new ProcessOutputAsString(0, "with_newline\n", "no_newline");
            ProcessOutputAsString case3 = new ProcessOutputAsString(0, "no_newline", "with_newline\n");
            ProcessOutputAsString case4 = new ProcessOutputAsString(0, "with_newline\n", "with_newline\n");

            assertThat(case1.getOutput()).isEqualTo("no_newline\nno_newline");
            assertThat(case2.getOutput()).isEqualTo("with_newline\n\nno_newline");
            assertThat(case3.getOutput()).isEqualTo("no_newline\nwith_newline\n");
            assertThat(case4.getOutput()).isEqualTo("with_newline\n\nwith_newline\n");
        }

        @Test
        @DisplayName("Should handle stdout + null concatenation pattern")
        void testStdoutNullConcatenation() {
            ProcessOutputAsString output = new ProcessOutputAsString(0, "content", null);

            String result = output.getOutput();

            // The String.valueOf(null) behavior should result in "null"
            assertThat(result).isEqualTo("content\nnull");
        }

        @Test
        @DisplayName("Should handle null + stderr concatenation pattern")
        void testNullStderrConcatenation() {
            ProcessOutputAsString output = new ProcessOutputAsString(0, null, "error");

            String result = output.getOutput();

            // The String.valueOf(null) behavior should result in "null"
            assertThat(result).isEqualTo("null\nerror");
        }

        @Test
        @DisplayName("Should verify actual concatenation formula")
        void testConcatenationFormula() {
            String stdout = "stdout_value";
            String stderr = "stderr_value";
            ProcessOutputAsString output = new ProcessOutputAsString(0, stdout, stderr);

            String expected = String.valueOf(stdout) + "\n" + String.valueOf(stderr);
            String actual = output.getOutput();

            assertThat(actual).isEqualTo(expected);
        }
    }
}
