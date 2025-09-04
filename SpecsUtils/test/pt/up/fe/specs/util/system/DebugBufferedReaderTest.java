package pt.up.fe.specs.util.system;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for the DebugBufferedReader class.
 * Tests debug output functionality, method overrides, and reader behavior.
 * 
 * @author Generated Tests
 */
public class DebugBufferedReaderTest {

    private PrintStream originalOut;
    private ByteArrayOutputStream capturedOut;
    private PrintStream debugOut;

    @BeforeEach
    void setUp() {
        // Capture original stdout
        originalOut = System.out;
        capturedOut = new ByteArrayOutputStream();
        debugOut = new PrintStream(capturedOut);
        System.setOut(debugOut);
    }

    @AfterEach
    void tearDown() {
        // Restore original stdout
        System.setOut(originalOut);
    }

    /**
     * Helper method to create DebugBufferedReader from string content.
     */
    private DebugBufferedReader createDebugReader(String content) {
        StringReader stringReader = new StringReader(content);
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        return new DebugBufferedReader(bufferedReader);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create instance with BufferedReader")
        void testConstructorWithBufferedReader() {
            StringReader stringReader = new StringReader("test content");
            BufferedReader bufferedReader = new BufferedReader(stringReader);

            assertThatCode(() -> new DebugBufferedReader(bufferedReader))
                    .doesNotThrowAnyException();

            DebugBufferedReader debugReader = new DebugBufferedReader(bufferedReader);
            assertThat(debugReader).isNotNull();
            assertThat(debugReader).isInstanceOf(BufferedReader.class);
        }

        @Test
        @DisplayName("Should handle null reader")
        void testConstructorWithNullReader() {
            // Should not throw during construction, but will fail on read operations
            assertThatCode(() -> new DebugBufferedReader(null))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should properly extend BufferedReader")
        void testInheritance() {
            DebugBufferedReader debugReader = createDebugReader("test");

            assertThat(debugReader).isInstanceOf(BufferedReader.class);
            assertThat(debugReader).isInstanceOf(java.io.Reader.class);
        }
    }

    @Nested
    @DisplayName("Read Single Character Tests")
    class ReadSingleCharacterTests {

        @Test
        @DisplayName("Should read single character and print debug info")
        void testReadSingleCharacter() throws IOException {
            DebugBufferedReader debugReader = createDebugReader("A");

            int result = debugReader.read();

            assertThat(result).isEqualTo(65); // 'A' = 65
            assertThat(capturedOut.toString()).contains("65");
        }

        @Test
        @DisplayName("Should handle end of stream")
        void testReadEndOfStream() throws IOException {
            DebugBufferedReader debugReader = createDebugReader("");

            int result = debugReader.read();

            assertThat(result).isEqualTo(-1);
            assertThat(capturedOut.toString()).contains("-1");
        }

        @Test
        @DisplayName("Should read multiple characters sequentially")
        void testReadMultipleCharacters() throws IOException {
            DebugBufferedReader debugReader = createDebugReader("ABC");

            int char1 = debugReader.read();
            int char2 = debugReader.read();
            int char3 = debugReader.read();
            int char4 = debugReader.read(); // EOF

            assertThat(char1).isEqualTo(65); // 'A'
            assertThat(char2).isEqualTo(66); // 'B'
            assertThat(char3).isEqualTo(67); // 'C'
            assertThat(char4).isEqualTo(-1); // EOF

            String debugOutput = capturedOut.toString();
            assertThat(debugOutput).contains("65");
            assertThat(debugOutput).contains("66");
            assertThat(debugOutput).contains("67");
            assertThat(debugOutput).contains("-1");
        }

        @Test
        @DisplayName("Should handle special characters")
        void testReadSpecialCharacters() throws IOException {
            DebugBufferedReader debugReader = createDebugReader("\n\t\r");

            int newline = debugReader.read();
            int tab = debugReader.read();
            int carriageReturn = debugReader.read();

            assertThat(newline).isEqualTo(10); // '\n'
            assertThat(tab).isEqualTo(9); // '\t'
            assertThat(carriageReturn).isEqualTo(13); // '\r'

            String debugOutput = capturedOut.toString();
            assertThat(debugOutput).contains("10");
            assertThat(debugOutput).contains("9");
            assertThat(debugOutput).contains("13");
        }

        @Test
        @DisplayName("Should propagate IOException from underlying reader")
        void testReadIOException() throws IOException {
            StringReader stringReader = new StringReader("test");
            stringReader.close(); // Close to force IOException

            BufferedReader bufferedReader = new BufferedReader(stringReader);
            DebugBufferedReader debugReader = new DebugBufferedReader(bufferedReader);

            assertThatThrownBy(() -> debugReader.read())
                    .isInstanceOf(IOException.class);
        }
    }

    @Nested
    @DisplayName("Read Character Array Tests")
    class ReadCharacterArrayTests {

        @Test
        @DisplayName("Should read into character array and print debug info")
        void testReadCharacterArray() throws IOException {
            DebugBufferedReader debugReader = createDebugReader("Hello");

            char[] buffer = new char[10];
            int bytesRead = debugReader.read(buffer, 0, 5);

            assertThat(bytesRead).isEqualTo(5);
            assertThat(buffer).startsWith('H', 'e', 'l', 'l', 'o');

            String debugOutput = capturedOut.toString();
            assertThat(debugOutput).contains("5"); // bytes read
        }

        @Test
        @DisplayName("Should handle partial reads")
        void testPartialRead() throws IOException {
            DebugBufferedReader debugReader = createDebugReader("ABC");

            char[] buffer = new char[10];
            int bytesRead = debugReader.read(buffer, 0, 10); // Request more than available

            assertThat(bytesRead).isEqualTo(3);
            assertThat(buffer[0]).isEqualTo('A');
            assertThat(buffer[1]).isEqualTo('B');
            assertThat(buffer[2]).isEqualTo('C');

            String debugOutput = capturedOut.toString();
            assertThat(debugOutput).contains("3");
        }

        @Test
        @DisplayName("Should handle empty read")
        void testEmptyRead() throws IOException {
            DebugBufferedReader debugReader = createDebugReader("");

            char[] buffer = new char[5];
            int bytesRead = debugReader.read(buffer, 0, 5);

            assertThat(bytesRead).isEqualTo(-1);

            String debugOutput = capturedOut.toString();
            assertThat(debugOutput).contains("-1");
        }

        @Test
        @DisplayName("Should handle offset and length parameters")
        void testOffsetAndLength() throws IOException {
            DebugBufferedReader debugReader = createDebugReader("ABCDEFGH");

            char[] buffer = new char[10];
            int bytesRead = debugReader.read(buffer, 2, 4); // Read 4 chars starting at offset 2

            assertThat(bytesRead).isEqualTo(4);
            assertThat(buffer[2]).isEqualTo('A');
            assertThat(buffer[3]).isEqualTo('B');
            assertThat(buffer[4]).isEqualTo('C');
            assertThat(buffer[5]).isEqualTo('D');

            String debugOutput = capturedOut.toString();
            assertThat(debugOutput).contains("4");
        }

        @Test
        @DisplayName("Should handle zero length read")
        void testZeroLengthRead() throws IOException {
            DebugBufferedReader debugReader = createDebugReader("test");

            char[] buffer = new char[5];
            int bytesRead = debugReader.read(buffer, 0, 0);

            assertThat(bytesRead).isZero();

            String debugOutput = capturedOut.toString();
            assertThat(debugOutput).contains("0");
        }

        @Test
        @DisplayName("Should propagate IOException in array read")
        void testArrayReadIOException() throws IOException {
            StringReader stringReader = new StringReader("test");
            stringReader.close(); // Close to force IOException

            BufferedReader bufferedReader = new BufferedReader(stringReader);
            DebugBufferedReader debugReader = new DebugBufferedReader(bufferedReader);

            char[] buffer = new char[5];
            assertThatThrownBy(() -> debugReader.read(buffer, 0, 5))
                    .isInstanceOf(IOException.class);
        }
    }

    @Nested
    @DisplayName("ReadLine Method Tests")
    class ReadLineMethodTests {

        @Test
        @DisplayName("Should read single line and print debug info")
        void testReadSingleLine() throws IOException {
            DebugBufferedReader debugReader = createDebugReader("Hello World");

            String line = debugReader.readLine();

            assertThat(line).isEqualTo("Hello World");

            String debugOutput = capturedOut.toString();
            assertThat(debugOutput).contains("Hello World");
        }

        @Test
        @DisplayName("Should handle empty line")
        void testReadEmptyLine() throws IOException {
            DebugBufferedReader debugReader = createDebugReader("\n");

            String line = debugReader.readLine();

            assertThat(line).isEmpty();

            String debugOutput = capturedOut.toString();
            assertThat(debugOutput).contains("\"\""); // Should show empty string in debug
        }

        @Test
        @DisplayName("Should handle null line (EOF)")
        void testReadNullLine() throws IOException {
            DebugBufferedReader debugReader = createDebugReader("");

            String line = debugReader.readLine();

            assertThat(line).isNull();

            String debugOutput = capturedOut.toString();
            assertThat(debugOutput).contains("null");
        }

        @Test
        @DisplayName("Should read multiple lines")
        void testReadMultipleLines() throws IOException {
            DebugBufferedReader debugReader = createDebugReader("Line 1\nLine 2\nLine 3");

            String line1 = debugReader.readLine();
            String line2 = debugReader.readLine();
            String line3 = debugReader.readLine();
            String line4 = debugReader.readLine(); // EOF

            assertThat(line1).isEqualTo("Line 1");
            assertThat(line2).isEqualTo("Line 2");
            assertThat(line3).isEqualTo("Line 3");
            assertThat(line4).isNull();

            String debugOutput = capturedOut.toString();
            assertThat(debugOutput).contains("Line 1");
            assertThat(debugOutput).contains("Line 2");
            assertThat(debugOutput).contains("Line 3");
            assertThat(debugOutput).contains("null");
        }

        @Test
        @DisplayName("Should handle lines with special characters")
        void testReadLinesWithSpecialCharacters() throws IOException {
            DebugBufferedReader debugReader = createDebugReader("Tab:\tLine\nUnicode:\u03B1\u03B2");

            String line1 = debugReader.readLine();
            String line2 = debugReader.readLine();

            assertThat(line1).isEqualTo("Tab:\tLine");
            assertThat(line2).isEqualTo("Unicode:\u03B1\u03B2");

            String debugOutput = capturedOut.toString();
            assertThat(debugOutput).contains("Tab:\tLine");
            assertThat(debugOutput).contains("Unicode:\u03B1\u03B2");
        }

        @Test
        @DisplayName("Should handle very long lines")
        void testReadVeryLongLine() throws IOException {
            StringBuilder longLine = new StringBuilder();
            for (int i = 0; i < 10000; i++) {
                longLine.append("A");
            }

            DebugBufferedReader debugReader = createDebugReader(longLine.toString());

            String line = debugReader.readLine();

            assertThat(line).hasSize(10000);
            assertThat(line).isEqualTo(longLine.toString());

            String debugOutput = capturedOut.toString();
            assertThat(debugOutput).contains(longLine.toString());
        }

        @Test
        @DisplayName("Should propagate IOException in readLine")
        void testReadLineIOException() throws IOException {
            StringReader stringReader = new StringReader("test");
            stringReader.close(); // Close to force IOException

            BufferedReader bufferedReader = new BufferedReader(stringReader);
            DebugBufferedReader debugReader = new DebugBufferedReader(bufferedReader);

            assertThatThrownBy(() -> debugReader.readLine())
                    .isInstanceOf(IOException.class);
        }
    }

    @Nested
    @DisplayName("Debug Output Format Tests")
    class DebugOutputFormatTests {

        @Test
        @DisplayName("Should format single character read debug output")
        void testSingleCharacterDebugFormat() throws IOException {
            DebugBufferedReader debugReader = createDebugReader("X");

            debugReader.read();

            String debugOutput = capturedOut.toString();
            assertThat(debugOutput).contains("88"); // 'X' = 88
        }

        @Test
        @DisplayName("Should format array read debug output")
        void testArrayReadDebugFormat() throws IOException {
            DebugBufferedReader debugReader = createDebugReader("ABC");

            char[] buffer = new char[5];
            debugReader.read(buffer, 0, 3);

            String debugOutput = capturedOut.toString();
            assertThat(debugOutput).contains("3"); // 3 characters read
        }

        @Test
        @DisplayName("Should format readLine debug output")
        void testReadLineDebugFormat() throws IOException {
            DebugBufferedReader debugReader = createDebugReader("Debug Line");

            debugReader.readLine();

            String debugOutput = capturedOut.toString();
            assertThat(debugOutput).contains("Debug Line");
        }

        @Test
        @DisplayName("Should handle debug output with newlines in content")
        void testDebugOutputWithNewlines() throws IOException {
            DebugBufferedReader debugReader = createDebugReader("Line1\nLine2");

            debugReader.readLine();

            String debugOutput = capturedOut.toString();
            assertThat(debugOutput).contains("Line1");
        }
    }

    @Nested
    @DisplayName("Resource Management Tests")
    class ResourceManagementTests {

        @Test
        @DisplayName("Should be closeable like regular BufferedReader")
        void testCloseable() throws IOException {
            StringReader stringReader = new StringReader("test");
            BufferedReader bufferedReader = new BufferedReader(stringReader);
            DebugBufferedReader debugReader = new DebugBufferedReader(bufferedReader);

            assertThatCode(() -> debugReader.close()).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should support try-with-resources")
        void testTryWithResources() {
            assertThatCode(() -> {
                StringReader stringReader = new StringReader("test");
                BufferedReader bufferedReader = new BufferedReader(stringReader);
                try (DebugBufferedReader debugReader = new DebugBufferedReader(bufferedReader)) {
                    debugReader.readLine();
                }
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Conditions")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle reading after EOF")
        void testReadAfterEOF() throws IOException {
            DebugBufferedReader debugReader = createDebugReader("A");

            // Read the single character
            debugReader.read();

            // Try to read again after EOF
            int result = debugReader.read();
            assertThat(result).isEqualTo(-1);
        }

        @Test
        @DisplayName("Should handle mark and reset operations")
        void testMarkAndReset() throws IOException {
            DebugBufferedReader debugReader = createDebugReader("ABCDEF");

            debugReader.mark(10);
            debugReader.read(); // Read 'A'
            debugReader.read(); // Read 'B'
            debugReader.reset();

            int result = debugReader.read(); // Should be 'A' again
            assertThat(result).isEqualTo(65);
        }

        @Test
        @DisplayName("Should handle null input to constructor gracefully")
        void testNullConstructorHandling() {
            DebugBufferedReader debugReader = new DebugBufferedReader(null);
            assertThat(debugReader).isNotNull();
        }
    }
}
