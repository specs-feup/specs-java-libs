package pt.up.fe.specs.util.system;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Comprehensive test suite for ProcessOutput utility class.
 * Tests process result handling, error detection, and generic output types.
 * 
 * @author Generated Tests
 */
@DisplayName("ProcessOutput Tests")
class ProcessOutputTest {

    @Nested
    @DisplayName("Construction Tests")
    class ConstructionTests {

        @Test
        @DisplayName("should create with basic constructor")
        void testBasicConstructor() {
            // Execute
            ProcessOutput<String, String> output = new ProcessOutput<>(0, "success", "");

            // Verify
            assertThat(output.getReturnValue()).isEqualTo(0);
            assertThat(output.getStdOut()).isEqualTo("success");
            assertThat(output.getStdErr()).isEqualTo("");
            assertThat(output.getOutputException()).isEmpty();
            assertThat(output.isError()).isFalse();
        }

        @Test
        @DisplayName("should create with exception constructor")
        void testExceptionConstructor() {
            // Setup
            IOException exception = new IOException("Test exception");

            // Execute
            ProcessOutput<String, String> output = new ProcessOutput<>(1, "output", "error", exception);

            // Verify
            assertThat(output.getReturnValue()).isEqualTo(1);
            assertThat(output.getStdOut()).isEqualTo("output");
            assertThat(output.getStdErr()).isEqualTo("error");
            assertThat(output.getOutputException()).isPresent()
                    .hasValue(exception);
            assertThat(output.isError()).isTrue();
        }

        @Test
        @DisplayName("should handle null values")
        void testNullValues() {
            // Execute
            ProcessOutput<String, String> output = new ProcessOutput<>(0, null, null, null);

            // Verify
            assertThat(output.getReturnValue()).isEqualTo(0);
            assertThat(output.getStdOut()).isNull();
            assertThat(output.getStdErr()).isNull();
            assertThat(output.getOutputException()).isEmpty();
            assertThat(output.isError()).isFalse();
        }
    }

    @Nested
    @DisplayName("Error Detection Tests")
    class ErrorDetectionTests {

        @ParameterizedTest
        @ValueSource(ints = { 0 })
        @DisplayName("should not be error for return value 0")
        void testSuccessReturnValue(int returnValue) {
            // Execute
            ProcessOutput<String, String> output = new ProcessOutput<>(returnValue, "success", "");

            // Verify
            assertThat(output.isError()).isFalse();
        }

        @ParameterizedTest
        @ValueSource(ints = { -1, 1, 2, 127, 255, Integer.MAX_VALUE, Integer.MIN_VALUE })
        @DisplayName("should be error for non-zero return values")
        void testErrorReturnValues(int returnValue) {
            // Execute
            ProcessOutput<String, String> output = new ProcessOutput<>(returnValue, "output", "error");

            // Verify
            assertThat(output.isError()).isTrue();
        }

        @Test
        @DisplayName("should detect error even with exception present")
        void testErrorWithException() {
            // Setup
            Exception exception = new RuntimeException("Test error");

            // Execute
            ProcessOutput<String, String> output = new ProcessOutput<>(1, "output", "error", exception);

            // Verify
            assertThat(output.isError()).isTrue();
            assertThat(output.getOutputException()).isPresent();
        }
    }

    @Nested
    @DisplayName("Generic Type Tests")
    class GenericTypeTests {

        @Test
        @DisplayName("should work with string types")
        void testStringTypes() {
            // Execute
            ProcessOutput<String, String> output = new ProcessOutput<>(0, "stdout content", "stderr content");

            // Verify
            assertThat(output.getStdOut()).isEqualTo("stdout content");
            assertThat(output.getStdErr()).isEqualTo("stderr content");
        }

        @Test
        @DisplayName("should work with list types")
        void testListTypes() {
            // Setup
            List<String> stdOut = List.of("line1", "line2", "line3");
            List<String> stdErr = List.of("error1", "error2");

            // Execute
            ProcessOutput<List<String>, List<String>> output = new ProcessOutput<>(0, stdOut, stdErr);

            // Verify
            assertThat(output.getStdOut()).hasSize(3)
                    .containsExactly("line1", "line2", "line3");
            assertThat(output.getStdErr()).hasSize(2)
                    .containsExactly("error1", "error2");
        }

        @Test
        @DisplayName("should work with byte array types")
        void testByteArrayTypes() {
            // Setup
            byte[] stdOut = "binary output".getBytes();
            byte[] stdErr = "binary error".getBytes();

            // Execute
            ProcessOutput<byte[], byte[]> output = new ProcessOutput<>(0, stdOut, stdErr);

            // Verify
            assertThat(output.getStdOut()).isEqualTo(stdOut);
            assertThat(output.getStdErr()).isEqualTo(stdErr);
        }

        @Test
        @DisplayName("should work with mixed types")
        void testMixedTypes() {
            // Setup
            String stdOut = "string output";
            List<String> stdErr = List.of("error line 1", "error line 2");

            // Execute
            ProcessOutput<String, List<String>> output = new ProcessOutput<>(0, stdOut, stdErr);

            // Verify
            assertThat(output.getStdOut()).isEqualTo("string output");
            assertThat(output.getStdErr()).hasSize(2)
                    .containsExactly("error line 1", "error line 2");
        }
    }

    @Nested
    @DisplayName("Exception Handling Tests")
    class ExceptionHandlingTests {

        @Test
        @DisplayName("should handle no exception")
        void testNoException() {
            // Execute
            ProcessOutput<String, String> output = new ProcessOutput<>(0, "output", "error");

            // Verify
            assertThat(output.getOutputException()).isEmpty();
        }

        @Test
        @DisplayName("should store and retrieve exception")
        void testWithException() {
            // Setup
            IOException exception = new IOException("Process failed");

            // Execute
            ProcessOutput<String, String> output = new ProcessOutput<>(1, "output", "error", exception);

            // Verify
            Optional<Exception> retrievedException = output.getOutputException();
            assertThat(retrievedException).isPresent();
            assertThat(retrievedException.get()).isInstanceOf(IOException.class)
                    .hasMessage("Process failed");
        }

        @Test
        @DisplayName("should handle different exception types")
        void testDifferentExceptionTypes() {
            // Test RuntimeException
            ProcessOutput<String, String> output1 = new ProcessOutput<>(1, "out", "err",
                    new RuntimeException("Runtime error"));
            assertThat(output1.getOutputException().get()).isInstanceOf(RuntimeException.class);

            // Test InterruptedException
            ProcessOutput<String, String> output2 = new ProcessOutput<>(1, "out", "err",
                    new InterruptedException("Interrupted"));
            assertThat(output2.getOutputException().get()).isInstanceOf(InterruptedException.class);

            // Test custom exception
            ProcessOutput<String, String> output3 = new ProcessOutput<>(1, "out", "err",
                    new CustomProcessException("Custom error"));
            assertThat(output3.getOutputException().get()).isInstanceOf(CustomProcessException.class);
        }

        // Helper custom exception
        private static class CustomProcessException extends Exception {
            public CustomProcessException(String message) {
                super(message);
            }
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("should format basic output correctly")
        void testBasicToString() {
            // Execute
            ProcessOutput<String, String> output = new ProcessOutput<>(0, "success output", "warning message");

            // Verify
            String result = output.toString();
            assertThat(result)
                    .contains("Return value: 0")
                    .contains("StdOut: success output")
                    .contains("StdErr: warning message")
                    .doesNotContain("Exception:");
        }

        @Test
        @DisplayName("should format output with exception")
        void testToStringWithException() {
            // Setup
            Exception exception = new RuntimeException("Test exception");

            // Execute
            ProcessOutput<String, String> output = new ProcessOutput<>(1, "output", "error", exception);

            // Verify
            String result = output.toString();
            assertThat(result)
                    .contains("Return value: 1")
                    .contains("StdOut: output")
                    .contains("StdErr: error")
                    .contains("Exception: java.lang.RuntimeException: Test exception");
        }

        @Test
        @DisplayName("should handle null values in toString")
        void testToStringWithNulls() {
            // Execute
            ProcessOutput<String, String> output = new ProcessOutput<>(2, null, null);

            // Verify
            String result = output.toString();
            assertThat(result)
                    .contains("Return value: 2")
                    .contains("StdOut: null")
                    .contains("StdErr: null")
                    .doesNotContain("Exception:");
        }

        @Test
        @DisplayName("should format multiline output correctly")
        void testToStringWithMultilineOutput() {
            // Setup
            String multilineOut = "line1\nline2\nline3";
            String multilineErr = "error1\nerror2";

            // Execute
            ProcessOutput<String, String> output = new ProcessOutput<>(0, multilineOut, multilineErr);

            // Verify
            String result = output.toString();
            assertThat(result)
                    .contains("Return value: 0")
                    .contains("StdOut: line1\nline2\nline3")
                    .contains("StdErr: error1\nerror2");
        }
    }

    @Nested
    @DisplayName("Real-world Usage Tests")
    class RealWorldUsageTests {

        @Test
        @DisplayName("should handle successful command execution")
        void testSuccessfulExecution() {
            // Setup - Simulate successful ls command
            ProcessOutput<String, String> output = new ProcessOutput<>(
                    0,
                    "file1.txt\nfile2.txt\ndirectory/\n",
                    "");

            // Verify
            assertThat(output.isError()).isFalse();
            assertThat(output.getStdOut()).contains("file1.txt", "file2.txt", "directory/");
            assertThat(output.getStdErr()).isEmpty();
            assertThat(output.getOutputException()).isEmpty();
        }

        @Test
        @DisplayName("should handle command not found")
        void testCommandNotFound() {
            // Setup - Simulate command not found (typically exit code 127)
            ProcessOutput<String, String> output = new ProcessOutput<>(
                    127,
                    "",
                    "command not found: nonexistentcommand");

            // Verify
            assertThat(output.isError()).isTrue();
            assertThat(output.getReturnValue()).isEqualTo(127);
            assertThat(output.getStdOut()).isEmpty();
            assertThat(output.getStdErr()).contains("command not found");
        }

        @Test
        @DisplayName("should handle permission denied")
        void testPermissionDenied() {
            // Setup - Simulate permission denied (typically exit code 1)
            ProcessOutput<String, String> output = new ProcessOutput<>(
                    1,
                    "",
                    "permission denied");

            // Verify
            assertThat(output.isError()).isTrue();
            assertThat(output.getReturnValue()).isEqualTo(1);
            assertThat(output.getStdErr()).contains("permission denied");
        }

        @Test
        @DisplayName("should handle timeout scenario")
        void testTimeoutScenario() {
            // Setup - Simulate process timeout with exception
            Exception timeoutException = new RuntimeException("Process timed out after 30 seconds");
            ProcessOutput<String, String> output = new ProcessOutput<>(
                    143, // SIGTERM exit code
                    "partial output",
                    "Process terminated",
                    timeoutException);

            // Verify
            assertThat(output.isError()).isTrue();
            assertThat(output.getReturnValue()).isEqualTo(143);
            assertThat(output.getOutputException()).isPresent()
                    .hasValueSatisfying(ex -> assertThat(ex.getMessage()).contains("timed out"));
        }

        @Test
        @DisplayName("should handle binary output")
        void testBinaryOutput() {
            // Setup - Simulate binary file reading
            byte[] binaryData = { 0x00, 0x01, 0x02, (byte) 0xFF, (byte) 0xFE };
            ProcessOutput<byte[], String> output = new ProcessOutput<>(0, binaryData, "");

            // Verify
            assertThat(output.isError()).isFalse();
            assertThat(output.getStdOut()).hasSize(5)
                    .containsExactly((byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0xFF, (byte) 0xFE);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("should handle empty outputs")
        void testEmptyOutputs() {
            // Execute
            ProcessOutput<String, String> output = new ProcessOutput<>(0, "", "");

            // Verify
            assertThat(output.getStdOut()).isEmpty();
            assertThat(output.getStdErr()).isEmpty();
            assertThat(output.isError()).isFalse();
        }

        @Test
        @DisplayName("should handle very large output")
        void testLargeOutput() {
            // Setup
            String largeOutput = "a".repeat(100000); // 100k characters

            // Execute
            ProcessOutput<String, String> output = new ProcessOutput<>(0, largeOutput, "");

            // Verify
            assertThat(output.getStdOut()).hasSize(100000);
            assertThat(output.isError()).isFalse();
        }

        @Test
        @DisplayName("should handle special characters")
        void testSpecialCharacters() {
            // Setup
            String specialOut = "Special chars: ñ, é, 中文, 🚀, \t, \n, \r";
            String specialErr = "Error with special: ©, ®, €, £";

            // Execute
            ProcessOutput<String, String> output = new ProcessOutput<>(0, specialOut, specialErr);

            // Verify
            assertThat(output.getStdOut()).isEqualTo(specialOut);
            assertThat(output.getStdErr()).isEqualTo(specialErr);
        }

        @Test
        @DisplayName("should handle negative return values")
        void testNegativeReturnValues() {
            // Execute
            ProcessOutput<String, String> output = new ProcessOutput<>(-1, "output", "error");

            // Verify
            assertThat(output.isError()).isTrue();
            assertThat(output.getReturnValue()).isEqualTo(-1);
        }
    }
}
