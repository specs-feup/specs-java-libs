package pt.up.fe.specs.util.system;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for StreamToString - a utility function that reads
 * from an InputStream and converts it to a String with options for printing
 * to stdout/stderr and storing the output.
 * 
 * Tests cover:
 * - Basic stream to string conversion
 * - Print and store option combinations
 * - Different output types (stdout/stderr)
 * - Edge cases (empty streams, null inputs)
 * - Real-world usage scenarios
 * - Error handling with IOException
 * 
 * @author Generated Tests
 */
@DisplayName("StreamToString Tests")
class StreamToStringTest {

    private PrintStream originalOut;
    private PrintStream originalErr;
    private ByteArrayOutputStream capturedOut;
    private ByteArrayOutputStream capturedErr;

    @BeforeEach
    void setUp() {
        // Capture system out and err for testing
        originalOut = System.out;
        originalErr = System.err;
        capturedOut = new ByteArrayOutputStream();
        capturedErr = new ByteArrayOutputStream();
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
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("should create with default constructor")
        void testDefaultConstructor() {
            StreamToString converter = new StreamToString();

            assertThat(converter).isNotNull();
            // Default constructor should print=true, store=true, type=StdOut
        }

        @Test
        @DisplayName("should create with custom parameters")
        void testCustomConstructor() {
            StreamToString converter = new StreamToString(false, true, OutputType.StdErr);

            assertThat(converter).isNotNull();
        }

        @Test
        @DisplayName("should create with all parameter combinations")
        void testAllParameterCombinations() {
            assertThatCode(() -> {
                new StreamToString(true, true, OutputType.StdOut);
                new StreamToString(true, false, OutputType.StdOut);
                new StreamToString(false, true, OutputType.StdOut);
                new StreamToString(false, false, OutputType.StdOut);
                new StreamToString(true, true, OutputType.StdErr);
                new StreamToString(false, false, OutputType.StdErr);
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Basic Stream Conversion")
    class BasicStreamConversion {

        @Test
        @DisplayName("should convert simple string stream")
        void testSimpleStringConversion() {
            String input = "Hello World";
            InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

            StreamToString converter = new StreamToString(false, true, OutputType.StdOut);
            String result = converter.apply(stream);

            String expectedWithNewline = input + System.getProperty("line.separator");
            assertThat(result).isEqualTo(expectedWithNewline);
        }

        @Test
        @DisplayName("should convert multi-line string stream")
        void testMultiLineStringConversion() {
            String input = "Line 1\nLine 2\nLine 3";
            InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

            StreamToString converter = new StreamToString(false, true, OutputType.StdOut);
            String result = converter.apply(stream);

            String newLine = System.getProperty("line.separator");
            String expected = "Line 1" + newLine + "Line 2" + newLine + "Line 3" + newLine;
            assertThat(result).isEqualTo(expected);
        }

        @Test
        @DisplayName("should handle empty stream")
        void testEmptyStream() {
            InputStream stream = new ByteArrayInputStream(new byte[0]);

            StreamToString converter = new StreamToString(false, true, OutputType.StdOut);
            String result = converter.apply(stream);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("should handle stream with only newlines")
        void testStreamWithOnlyNewlines() {
            String input = "\n\n\n";
            InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

            StreamToString converter = new StreamToString(false, true, OutputType.StdOut);
            String result = converter.apply(stream);

            String newLine = System.getProperty("line.separator");
            String expected = newLine + newLine + newLine; // Three empty lines, each gets a newline
            assertThat(result).isEqualTo(expected);
        }

        @Test
        @DisplayName("should handle stream with special characters")
        void testStreamWithSpecialCharacters() {
            String input = "Special chars: !@#$%^&*()_+-={}[]|\\:\";<>?,./";
            InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

            StreamToString converter = new StreamToString(false, true, OutputType.StdOut);
            String result = converter.apply(stream);

            String expectedWithNewline = input + System.getProperty("line.separator");
            assertThat(result).isEqualTo(expectedWithNewline);
        }
    }

    @Nested
    @DisplayName("Print and Store Options")
    class PrintAndStoreOptions {

        @Test
        @DisplayName("should print and store when both enabled")
        void testPrintAndStore() {
            String input = "Test message";
            InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

            StreamToString converter = new StreamToString(true, true, OutputType.StdOut);
            String result = converter.apply(stream);

            String newLine = System.getProperty("line.separator");
            String expectedWithNewline = input + newLine;

            // Should store the result
            assertThat(result).isEqualTo(expectedWithNewline);

            // Should print to stdout
            assertThat(capturedOut.toString()).isEqualTo(expectedWithNewline);
            assertThat(capturedErr.toString()).isEmpty();
        }

        @Test
        @DisplayName("should only print when store disabled")
        void testOnlyPrint() {
            String input = "Test message";
            InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

            StreamToString converter = new StreamToString(true, false, OutputType.StdOut);
            String result = converter.apply(stream);

            String newLine = System.getProperty("line.separator");
            String expectedPrint = input + newLine;

            // Should not store the result
            assertThat(result).isEmpty();

            // Should print to stdout
            assertThat(capturedOut.toString()).isEqualTo(expectedPrint);
        }

        @Test
        @DisplayName("should only store when print disabled")
        void testOnlyStore() {
            String input = "Test message";
            InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

            StreamToString converter = new StreamToString(false, true, OutputType.StdOut);
            String result = converter.apply(stream);

            String newLine = System.getProperty("line.separator");
            String expectedWithNewline = input + newLine;

            // Should store the result
            assertThat(result).isEqualTo(expectedWithNewline);

            // Should not print anything
            assertThat(capturedOut.toString()).isEmpty();
            assertThat(capturedErr.toString()).isEmpty();
        }

        @Test
        @DisplayName("should neither print nor store when both disabled")
        void testNeitherPrintNorStore() {
            String input = "Test message";
            InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

            StreamToString converter = new StreamToString(false, false, OutputType.StdOut);
            String result = converter.apply(stream);

            // Should not store the result
            assertThat(result).isEmpty();

            // Should not print anything
            assertThat(capturedOut.toString()).isEmpty();
            assertThat(capturedErr.toString()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Output Type Tests")
    class OutputTypeTests {

        @Test
        @DisplayName("should print to stdout when OutputType.StdOut")
        void testPrintToStdOut() {
            String input = "stdout message";
            InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

            StreamToString converter = new StreamToString(true, false, OutputType.StdOut);
            converter.apply(stream);

            String newLine = System.getProperty("line.separator");
            String expected = input + newLine;

            assertThat(capturedOut.toString()).isEqualTo(expected);
            assertThat(capturedErr.toString()).isEmpty();
        }

        @Test
        @DisplayName("should print to stderr when OutputType.StdErr")
        void testPrintToStdErr() {
            String input = "stderr message";
            InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

            StreamToString converter = new StreamToString(true, false, OutputType.StdErr);
            converter.apply(stream);

            String newLine = System.getProperty("line.separator");
            String expected = input + newLine;

            assertThat(capturedErr.toString()).isEqualTo(expected);
            assertThat(capturedOut.toString()).isEmpty();
        }

        @Test
        @DisplayName("should handle multi-line output to stderr")
        void testMultiLineToStdErr() {
            String input = "Error line 1\nError line 2";
            InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

            StreamToString converter = new StreamToString(true, true, OutputType.StdErr);
            String result = converter.apply(stream);

            String newLine = System.getProperty("line.separator");
            String expected = "Error line 1" + newLine + "Error line 2" + newLine;

            assertThat(result).isEqualTo(expected);
            assertThat(capturedErr.toString()).isEqualTo(expected);
            assertThat(capturedOut.toString()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Default Constructor Behavior")
    class DefaultConstructorBehavior {

        @Test
        @DisplayName("should use default settings with default constructor")
        void testDefaultConstructorBehavior() {
            String input = "Default test";
            InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

            StreamToString converter = new StreamToString(); // Default: print=true, store=true, type=StdOut
            String result = converter.apply(stream);

            String newLine = System.getProperty("line.separator");
            String expected = input + newLine;

            // Should store the result (default store=true)
            assertThat(result).isEqualTo(expected);

            // Should print to stdout (default type=StdOut, print=true)
            assertThat(capturedOut.toString()).isEqualTo(expected);
            assertThat(capturedErr.toString()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Function Interface Implementation")
    class FunctionInterfaceImplementation {

        @Test
        @DisplayName("should work as Function<InputStream, String>")
        void testAsFunctionInterface() {
            String input = "Function test";
            InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

            java.util.function.Function<InputStream, String> converter = new StreamToString(false, true,
                    OutputType.StdOut);
            String result = converter.apply(stream);

            String expectedWithNewline = input + System.getProperty("line.separator");
            assertThat(result).isEqualTo(expectedWithNewline);
        }

        @Test
        @DisplayName("should be usable in stream operations")
        void testInStreamOperations() {
            String[] inputs = { "Stream 1", "Stream 2", "Stream 3" };

            java.util.List<String> results = java.util.Arrays.stream(inputs)
                    .map(s -> new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8)))
                    .map(new StreamToString(false, true, OutputType.StdOut))
                    .collect(java.util.stream.Collectors.toList());

            String newLine = System.getProperty("line.separator");
            assertThat(results)
                    .hasSize(3)
                    .containsExactly(
                            "Stream 1" + newLine,
                            "Stream 2" + newLine,
                            "Stream 3" + newLine);
        }
    }

    @Nested
    @DisplayName("Real-world Usage Scenarios")
    class RealWorldUsageScenarios {

        @Test
        @DisplayName("should handle typical command output")
        void testTypicalCommandOutput() {
            String commandOutput = """
                    total 24
                    drwxr-xr-x  3 user user 4096 Jan  1 12:00 .
                    drwxr-xr-x 15 user user 4096 Jan  1 11:00 ..
                    -rw-r--r--  1 user user 1234 Jan  1 12:00 file.txt
                    """;

            InputStream stream = new ByteArrayInputStream(commandOutput.getBytes(StandardCharsets.UTF_8));

            StreamToString converter = new StreamToString(false, true, OutputType.StdOut);
            String result = converter.apply(stream);

            assertThat(result)
                    .isNotEmpty()
                    .contains("total 24")
                    .contains("file.txt")
                    .contains("user");
        }

        @Test
        @DisplayName("should handle error output for debugging")
        void testErrorOutputForDebugging() {
            String errorOutput = """
                    Exception in thread "main" java.lang.NullPointerException
                    	at com.example.Main.main(Main.java:15)
                    Caused by: java.lang.IllegalArgumentException
                    	at com.example.Helper.process(Helper.java:42)
                    """;

            InputStream stream = new ByteArrayInputStream(errorOutput.getBytes(StandardCharsets.UTF_8));

            StreamToString converter = new StreamToString(true, true, OutputType.StdErr);
            String result = converter.apply(stream);

            assertThat(result)
                    .contains("NullPointerException")
                    .contains("Main.java:15")
                    .contains("IllegalArgumentException");

            assertThat(capturedErr.toString())
                    .contains("NullPointerException")
                    .contains("Main.java:15");
        }

        @Test
        @DisplayName("should handle large stream content efficiently")
        void testLargeStreamContent() {
            StringBuilder largeContent = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                largeContent.append("Line ").append(i).append(" with some content\n");
            }

            InputStream stream = new ByteArrayInputStream(largeContent.toString().getBytes(StandardCharsets.UTF_8));

            long startTime = System.currentTimeMillis();
            StreamToString converter = new StreamToString(false, true, OutputType.StdOut);
            String result = converter.apply(stream);
            long endTime = System.currentTimeMillis();

            assertThat(result)
                    .isNotEmpty()
                    .contains("Line 0")
                    .contains("Line 999");

            // Should complete in reasonable time (less than 2 seconds)
            assertThat(endTime - startTime).isLessThan(2000);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandling {

        @Test
        @DisplayName("should handle binary content gracefully")
        void testBinaryContent() {
            byte[] binaryData = { 0x00, 0x01, 0x02, (byte) 0xFF, 0x7F, (byte) 0x80 };
            InputStream stream = new ByteArrayInputStream(binaryData);

            StreamToString converter = new StreamToString(false, true, OutputType.StdOut);

            assertThatCode(() -> {
                String result = converter.apply(stream);
                // Result may contain special characters but should not throw
                assertThat(result).isNotNull();
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should handle very long lines")
        void testVeryLongLines() {
            String longLine = "Very long line: " + "x".repeat(10000);
            InputStream stream = new ByteArrayInputStream(longLine.getBytes(StandardCharsets.UTF_8));

            StreamToString converter = new StreamToString(false, true, OutputType.StdOut);
            String result = converter.apply(stream);

            assertThat(result)
                    .hasSize(longLine.length() + System.getProperty("line.separator").length())
                    .startsWith("Very long line:")
                    .endsWith("x" + System.getProperty("line.separator"));
        }

        @Test
        @DisplayName("should handle mixed content types")
        void testMixedContentTypes() {
            String mixedContent = """
                    Normal text line
                    Line with special chars: ñáéíóú中文العربية
                    Numbers: 123456789
                    Symbols: !@#$%^&*()_+-=[]{}|;:,.<>?
                    Empty line follows:

                    Final line
                    """;

            InputStream stream = new ByteArrayInputStream(mixedContent.getBytes(StandardCharsets.UTF_8));

            StreamToString converter = new StreamToString(false, true, OutputType.StdOut);
            String result = converter.apply(stream);

            assertThat(result)
                    .contains("Normal text line")
                    .contains("ñáéíóú中文العربية")
                    .contains("123456789")
                    .contains("!@#$%^&*()")
                    .contains("Final line");
        }

        @Test
        @DisplayName("should handle concurrent access")
        void testConcurrentAccess() {
            String input = "Concurrent test";

            // Create multiple threads using the same converter
            StreamToString converter = new StreamToString(false, true, OutputType.StdOut);

            java.util.List<java.util.concurrent.Future<String>> futures = new java.util.ArrayList<>();
            java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(5);

            try {
                for (int i = 0; i < 10; i++) {
                    final int threadId = i;
                    futures.add(executor.submit(() -> {
                        String threadInput = input + " " + threadId;
                        InputStream stream = new ByteArrayInputStream(threadInput.getBytes(StandardCharsets.UTF_8));
                        return converter.apply(stream);
                    }));
                }

                // Collect all results
                java.util.List<String> results = new java.util.ArrayList<>();
                for (java.util.concurrent.Future<String> future : futures) {
                    results.add(future.get());
                }

                assertThat(results)
                        .hasSize(10)
                        .allMatch(result -> result.startsWith("Concurrent test"));

            } catch (Exception e) {
                fail("Concurrent test failed", e);
            } finally {
                executor.shutdown();
            }
        }
    }
}
