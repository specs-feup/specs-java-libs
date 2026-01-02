package pt.up.fe.specs.util.system;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for the StreamCatcher class.
 * Tests stream processing, output handling, thread execution, and various
 * configurations.
 * 
 * @author Generated Tests
 */
public class StreamCatcherTest {

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

    /**
     * Helper method to create InputStream from string content.
     */
    private InputStream createInputStream(String content) {
        return new ByteArrayInputStream(content.getBytes());
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create instance with basic parameters")
        void testBasicConstructor() {
            InputStream inputStream = createInputStream("test");

            assertThatCode(() -> new StreamCatcher(inputStream, StreamCatcher.OutputType.StdOut, true, true))
                    .doesNotThrowAnyException();

            StreamCatcher catcher = new StreamCatcher(inputStream, StreamCatcher.OutputType.StdOut, true, true);
            assertThat(catcher).isNotNull();
            assertThat(catcher).isInstanceOf(Runnable.class);
        }

        @Test
        @DisplayName("Should create instance with different OutputType")
        void testConstructorWithOutputType() {
            InputStream inputStream = createInputStream("test");

            assertThatCode(() -> new StreamCatcher(inputStream, StreamCatcher.OutputType.StdErr, true, true))
                    .doesNotThrowAnyException();

            StreamCatcher catcher = new StreamCatcher(inputStream, StreamCatcher.OutputType.StdErr, true, true);
            assertThat(catcher).isNotNull();
        }

        @Test
        @DisplayName("Should handle null InputStream")
        void testNullInputStream() {
            // Constructor should not throw, but run() will fail
            assertThatCode(() -> new StreamCatcher(null, StreamCatcher.OutputType.StdOut, true, true))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle various boolean combinations")
        void testBooleanCombinations() {
            InputStream inputStream = createInputStream("test");

            assertThatCode(() -> new StreamCatcher(inputStream, StreamCatcher.OutputType.StdOut, true, true))
                    .doesNotThrowAnyException();
            assertThatCode(() -> new StreamCatcher(inputStream, StreamCatcher.OutputType.StdOut, true, false))
                    .doesNotThrowAnyException();
            assertThatCode(() -> new StreamCatcher(inputStream, StreamCatcher.OutputType.StdOut, false, true))
                    .doesNotThrowAnyException();
            assertThatCode(() -> new StreamCatcher(inputStream, StreamCatcher.OutputType.StdOut, false, false))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Output Storage Tests")
    class OutputStorageTests {

        @Test
        @DisplayName("Should store output when storeOutput is true")
        void testStoreOutput() {
            String testContent = "Line 1\nLine 2\nLine 3";
            InputStream inputStream = createInputStream(testContent);
            StreamCatcher catcher = new StreamCatcher(inputStream, StreamCatcher.OutputType.StdOut, true, false);

            catcher.run();

            String output = catcher.getOutput();
            assertThat(output).contains("Line 1");
            assertThat(output).contains("Line 2");
            assertThat(output).contains("Line 3");
        }

        @Test
        @DisplayName("Should not store output when storeOutput is false")
        void testNoStoreOutput() {
            String testContent = "Some content";
            InputStream inputStream = createInputStream(testContent);
            StreamCatcher catcher = new StreamCatcher(inputStream, StreamCatcher.OutputType.StdOut, false, false);

            catcher.run();

            String output = catcher.getOutput();
            assertThat(output).isEmpty();
        }

        @Test
        @DisplayName("Should handle empty input stream")
        void testEmptyInputStream() {
            InputStream inputStream = createInputStream("");
            StreamCatcher catcher = new StreamCatcher(inputStream, StreamCatcher.OutputType.StdOut, true, false);

            catcher.run();

            String output = catcher.getOutput();
            assertThat(output).isEmpty();
        }

        @Test
        @DisplayName("Should handle very large input")
        void testLargeInput() {
            StringBuilder largeContent = new StringBuilder();
            for (int i = 0; i < 10000; i++) {
                largeContent.append("Line ").append(i).append("\n");
            }

            InputStream inputStream = createInputStream(largeContent.toString());
            StreamCatcher catcher = new StreamCatcher(inputStream, StreamCatcher.OutputType.StdOut, true, false);

            catcher.run();

            String output = catcher.getOutput();
            assertThat(output).contains("Line 0");
            assertThat(output).contains("Line 9999");

            // Compute expected length: for each line "Line " + digits(i) + "\n"
            int expectedLength = 0;
            for (int i = 0; i < 10000; i++) {
                expectedLength += "Line ".length();
                expectedLength += Integer.toString(i).length();
                expectedLength += 1; // newline
            }

            assertThat(output.length()).isEqualTo(expectedLength);
        }

        @Test
        @DisplayName("Should preserve newlines in stored output")
        void testNewlinePreservation() {
            String testContent = "Line1\nLine2\n\nLine4";
            InputStream inputStream = createInputStream(testContent);
            StreamCatcher catcher = new StreamCatcher(inputStream, StreamCatcher.OutputType.StdOut, true, false);

            catcher.run();

            String output = catcher.getOutput();
            // Each line should have additional newline from StreamCatcher.NEW_LINE
            long newlineCount = output.chars().filter(ch -> ch == '\n').count();
            assertThat(newlineCount).isGreaterThanOrEqualTo(4); // Original + added newlines
        }
    }

    @Nested
    @DisplayName("Print Output Tests")
    class PrintOutputTests {

        @Test
        @DisplayName("Should print to stdout when printOutput is true and type is StdOut")
        void testPrintToStdOut() {
            String testContent = "Hello stdout";
            InputStream inputStream = createInputStream(testContent);
            StreamCatcher catcher = new StreamCatcher(inputStream, StreamCatcher.OutputType.StdOut, false, true);

            catcher.run();

            String printedOutput = capturedOut.toString();
            assertThat(printedOutput).contains("Hello stdout");
            assertThat(capturedErr.toString()).isEmpty();
        }

        @Test
        @DisplayName("Should print to stderr when printOutput is true and type is StdErr")
        void testPrintToStdErr() {
            String testContent = "Hello stderr";
            InputStream inputStream = createInputStream(testContent);
            StreamCatcher catcher = new StreamCatcher(inputStream, StreamCatcher.OutputType.StdErr, false, true);

            catcher.run();

            String printedOutput = capturedErr.toString();
            assertThat(printedOutput).contains("Hello stderr");
            assertThat(capturedOut.toString()).isEmpty();
        }

        @Test
        @DisplayName("Should not print when printOutput is false")
        void testNoPrint() {
            String testContent = "Should not print";
            InputStream inputStream = createInputStream(testContent);
            StreamCatcher catcher = new StreamCatcher(inputStream, StreamCatcher.OutputType.StdOut, false, false);

            catcher.run();

            assertThat(capturedOut.toString()).isEmpty();
            assertThat(capturedErr.toString()).isEmpty();
        }

        @Test
        @DisplayName("Should handle multiple lines in print output")
        void testMultiLinePrint() {
            String testContent = "Line 1\nLine 2\nLine 3";
            InputStream inputStream = createInputStream(testContent);
            StreamCatcher catcher = new StreamCatcher(inputStream, StreamCatcher.OutputType.StdOut, false, true);

            catcher.run();

            String printedOutput = capturedOut.toString();
            assertThat(printedOutput).contains("Line 1");
            assertThat(printedOutput).contains("Line 2");
            assertThat(printedOutput).contains("Line 3");
        }

        @Test
        @DisplayName("Should print remaining buffer content on stream end")
        void testPrintBufferOnEnd() {
            String testContent = "No newline at end";
            InputStream inputStream = createInputStream(testContent);
            StreamCatcher catcher = new StreamCatcher(inputStream, StreamCatcher.OutputType.StdOut, false, true);

            catcher.run();

            String printedOutput = capturedOut.toString();
            assertThat(printedOutput).contains("No newline at end");
        }
    }

    @Nested
    @DisplayName("Combined Store and Print Tests")
    class CombinedTests {

        @Test
        @DisplayName("Should both store and print when both flags are true")
        void testStoreAndPrint() {
            String testContent = "Store and print";
            InputStream inputStream = createInputStream(testContent);
            StreamCatcher catcher = new StreamCatcher(inputStream, StreamCatcher.OutputType.StdOut, true, true);

            catcher.run();

            // Check stored output
            String storedOutput = catcher.getOutput();
            assertThat(storedOutput).contains("Store and print");

            // Check printed output
            String printedOutput = capturedOut.toString();
            assertThat(printedOutput).contains("Store and print");
        }

        @Test
        @DisplayName("Should handle different content for store and print")
        void testComplexStoreAndPrint() {
            String testContent = "Line1\nLine2\nLine3\nPartialLine";
            InputStream inputStream = createInputStream(testContent);
            StreamCatcher catcher = new StreamCatcher(inputStream, StreamCatcher.OutputType.StdErr, true, true);

            catcher.run();

            // Check stored output includes all content
            String storedOutput = catcher.getOutput();
            assertThat(storedOutput).contains("Line1");
            assertThat(storedOutput).contains("Line2");
            assertThat(storedOutput).contains("Line3");
            assertThat(storedOutput).contains("PartialLine");

            // Check printed output
            String printedOutput = capturedErr.toString();
            assertThat(printedOutput).contains("Line1");
            assertThat(printedOutput).contains("Line2");
            assertThat(printedOutput).contains("Line3");
            assertThat(printedOutput).contains("PartialLine");
        }
    }

    @Nested
    @DisplayName("Thread Execution Tests")
    class ThreadExecutionTests {

        @Test
        @DisplayName("Should execute as Runnable in separate thread")
        void testThreadExecution() throws InterruptedException {
            String testContent = "Thread test content";
            InputStream inputStream = createInputStream(testContent);
            StreamCatcher catcher = new StreamCatcher(inputStream, StreamCatcher.OutputType.StdOut, true, false);

            Thread thread = new Thread(catcher);
            thread.start();
            thread.join(1000); // Wait up to 1 second

            String output = catcher.getOutput();
            assertThat(output).contains("Thread test content");
        }

        @Test
        @DisplayName("Should handle concurrent execution")
        void testConcurrentExecution() throws InterruptedException {
            String content1 = "Content 1\nLine 2";
            String content2 = "Content A\nLine B";

            InputStream inputStream1 = createInputStream(content1);
            InputStream inputStream2 = createInputStream(content2);

            StreamCatcher catcher1 = new StreamCatcher(inputStream1, StreamCatcher.OutputType.StdOut, true, false);
            StreamCatcher catcher2 = new StreamCatcher(inputStream2, StreamCatcher.OutputType.StdOut, true, false);

            Thread thread1 = new Thread(catcher1);
            Thread thread2 = new Thread(catcher2);

            thread1.start();
            thread2.start();

            thread1.join(1000);
            thread2.join(1000);

            assertThat(catcher1.getOutput()).contains("Content 1");
            assertThat(catcher2.getOutput()).contains("Content A");
        }
    }

    @Nested
    @DisplayName("OutputType Enum Tests")
    class OutputTypeEnumTests {

        @Test
        @DisplayName("Should have StdOut and StdErr values")
        void testOutputTypeValues() {
            StreamCatcher.OutputType[] values = StreamCatcher.OutputType.values();

            assertThat(values).hasSize(2);
            assertThat(values).containsExactlyInAnyOrder(
                    StreamCatcher.OutputType.StdOut,
                    StreamCatcher.OutputType.StdErr);
        }

        @Test
        @DisplayName("Should support valueOf operations")
        void testOutputTypeValueOf() {
            assertThat(StreamCatcher.OutputType.valueOf("StdOut"))
                    .isEqualTo(StreamCatcher.OutputType.StdOut);
            assertThat(StreamCatcher.OutputType.valueOf("StdErr"))
                    .isEqualTo(StreamCatcher.OutputType.StdErr);
        }

        @Test
        @DisplayName("Should have consistent string representation")
        void testOutputTypeToString() {
            assertThat(StreamCatcher.OutputType.StdOut.toString()).isEqualTo("StdOut");
            assertThat(StreamCatcher.OutputType.StdErr.toString()).isEqualTo("StdErr");
        }

        @Test
        @DisplayName("Should support print method")
        void testOutputTypePrint() {
            StreamCatcher.OutputType.StdOut.print("test stdout");
            StreamCatcher.OutputType.StdErr.print("test stderr");

            assertThat(capturedOut.toString()).contains("test stdout");
            assertThat(capturedErr.toString()).contains("test stderr");
        }
    }

    @Nested
    @DisplayName("Special Characters and Unicode Tests")
    class SpecialCharactersTests {

        @Test
        @DisplayName("Should handle Unicode characters")
        void testUnicodeCharacters() {
            String unicodeContent = "Unicode: \u03B1\u03B2\u03B3 \u3042\u3044\u3046 \uD83D\uDE00";
            InputStream inputStream = createInputStream(unicodeContent);
            StreamCatcher catcher = new StreamCatcher(inputStream, StreamCatcher.OutputType.StdOut, true, false);

            catcher.run();

            String output = catcher.getOutput();
            assertThat(output).contains("\u03B1\u03B2\u03B3");
        }

        @Test
        @DisplayName("Should handle special control characters")
        void testSpecialControlCharacters() {
            String controlContent = "Tab:\tContent\rCarriage\nNewline";
            InputStream inputStream = createInputStream(controlContent);
            StreamCatcher catcher = new StreamCatcher(inputStream, StreamCatcher.OutputType.StdOut, true, false);

            catcher.run();

            String output = catcher.getOutput();
            assertThat(output).contains("Tab:");
            assertThat(output).contains("Content");
        }

        @Test
        @DisplayName("Should handle mixed newline formats")
        void testMixedNewlineFormats() {
            String mixedContent = "Unix\nWindows\r\nMac\rEnd";
            InputStream inputStream = createInputStream(mixedContent);
            StreamCatcher catcher = new StreamCatcher(inputStream, StreamCatcher.OutputType.StdOut, true, false);

            catcher.run();

            String output = catcher.getOutput();
            assertThat(output).contains("Unix");
            assertThat(output).contains("Windows");
            assertThat(output).contains("Mac");
            assertThat(output).contains("End");
        }
    }

    @Nested
    @DisplayName("Error Handling and Edge Cases")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle very long lines without issues")
        void testVeryLongLines() {
            StringBuilder longLine = new StringBuilder();
            for (int i = 0; i < 100000; i++) {
                longLine.append("A");
            }
            longLine.append("\n");

            InputStream inputStream = createInputStream(longLine.toString());
            StreamCatcher catcher = new StreamCatcher(inputStream, StreamCatcher.OutputType.StdOut, true, false);

            catcher.run();

            String output = catcher.getOutput();
            assertThat(output.length()).isGreaterThan(100000);
        }

        @Test
        @DisplayName("Should handle binary data represented as text")
        void testBinaryData() {
            byte[] binaryData = { 0, 1, 2, 3, 127, -1, -128 };
            InputStream inputStream = new ByteArrayInputStream(binaryData);
            StreamCatcher catcher = new StreamCatcher(inputStream, StreamCatcher.OutputType.StdOut, true, false);

            catcher.run();

            String output = catcher.getOutput();
            assertThat(output).isNotNull();
        }

        @Test
        @DisplayName("Should handle rapid start and stop")
        void testRapidStartStop() throws InterruptedException {
            String testContent = "Rapid test";
            InputStream inputStream = createInputStream(testContent);
            StreamCatcher catcher = new StreamCatcher(inputStream, StreamCatcher.OutputType.StdOut, true, false);

            Thread thread = new Thread(catcher);
            thread.start();
            thread.join(100); // Very short wait

            String output = catcher.getOutput();
            assertThat(output).contains("Rapid test");
        }
    }

    @Nested
    @DisplayName("Performance and Memory Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should handle repeated small reads efficiently")
        void testRepeatedSmallReads() {
            StringBuilder content = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                content.append("Small line ").append(i).append("\n");
            }

            InputStream inputStream = createInputStream(content.toString());
            StreamCatcher catcher = new StreamCatcher(inputStream, StreamCatcher.OutputType.StdOut, true, false);

            long startTime = System.currentTimeMillis();
            catcher.run();
            long endTime = System.currentTimeMillis();

            assertThat(endTime - startTime).isLessThan(5000); // Should complete in under 5 seconds

            String output = catcher.getOutput();
            assertThat(output).contains("Small line 0");
            assertThat(output).contains("Small line 999");
        }
    }
}
