package pt.up.fe.specs.util.utilities;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.junitpioneer.jupiter.RetryingTest;

import java.io.File;
import java.nio.file.Path;

import pt.up.fe.specs.util.SpecsIo;

/**
 * Test for BufferedStringBuilder - Buffered string building with file output
 * 
 * Tests buffered writing functionality, auto-flushing, file management, and
 * resource cleanup.
 * 
 * @author Generated Tests
 */
public class BufferedStringBuilderTest {

    @TempDir
    Path tempDir;

    private File outputFile;

    @BeforeEach
    void setUp() {
        outputFile = tempDir.resolve("test_output.txt").toFile();
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create with default buffer capacity")
        void testDefaultConstructor() {
            try (BufferedStringBuilder builder = new BufferedStringBuilder(outputFile)) {
                assertThat(builder).isNotNull();
                assertThat(outputFile).exists();
                assertThat(SpecsIo.read(outputFile)).isEmpty(); // File should be initially empty
            }
        }

        @Test
        @DisplayName("Should create with custom buffer capacity")
        void testCustomBufferCapacity() {
            int customCapacity = 1000;
            try (BufferedStringBuilder builder = new BufferedStringBuilder(outputFile, customCapacity)) {
                assertThat(builder).isNotNull();
                assertThat(outputFile).exists();
            }
        }

        @Test
        @DisplayName("Should handle null file parameter")
        void testNullFile() {
            // Constructor should validate null file parameter
            assertThatThrownBy(() -> new BufferedStringBuilder(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Output file cannot be null");
        }

        @Test
        @DisplayName("Should erase existing file content on creation")
        void testFileErasure() {
            // Pre-populate file
            SpecsIo.write(outputFile, "existing content");

            try (BufferedStringBuilder builder = new BufferedStringBuilder(outputFile)) {
                assertThat(SpecsIo.read(outputFile)).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("Basic Append Operations")
    class BasicAppendTests {

        private BufferedStringBuilder builder;

        @BeforeEach
        void setUp() {
            builder = new BufferedStringBuilder(outputFile);
        }

        @AfterEach
        void tearDown() {
            if (builder != null) {
                builder.close();
            }
        }

        @Test
        @DisplayName("Should append strings correctly")
        void testAppendString() {
            BufferedStringBuilder result = builder.append("Hello");

            assertThat(result).isSameAs(builder); // Should return self for chaining

            builder.close();
            assertThat(SpecsIo.read(outputFile)).isEqualTo("Hello");
        }

        @Test
        @DisplayName("Should append integers correctly")
        void testAppendInteger() {
            builder.append(42);
            builder.close();

            assertThat(SpecsIo.read(outputFile)).isEqualTo("42");
        }

        @Test
        @DisplayName("Should append objects correctly")
        void testAppendObject() {
            Object testObject = new Object() {
                @Override
                public String toString() {
                    return "test-object";
                }
            };

            builder.append(testObject);
            builder.close();

            assertThat(SpecsIo.read(outputFile)).isEqualTo("test-object");
        }

        @Test
        @DisplayName("Should append newlines correctly")
        void testAppendNewline() {
            String expectedNewline = System.getProperty("line.separator");

            builder.appendNewline();
            builder.close();

            assertThat(SpecsIo.read(outputFile)).isEqualTo(expectedNewline);
        }

        @Test
        @DisplayName("Should support method chaining")
        void testMethodChaining() {
            builder.append("Hello")
                    .append(" ")
                    .append("World")
                    .appendNewline();

            builder.close();

            String expected = "Hello World" + System.getProperty("line.separator");
            assertThat(SpecsIo.read(outputFile)).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("Buffer Management Tests")
    class BufferManagementTests {

        @Test
        @DisplayName("Should auto-flush when buffer capacity is reached")
        void testAutoFlush() {
            int smallCapacity = 10;
            BufferedStringBuilder builder = new BufferedStringBuilder(outputFile, smallCapacity);

            // Add content that exceeds buffer capacity
            builder.append("1234567890"); // Exactly capacity
            builder.append("X"); // Should trigger flush

            // Content should be written to file even before close
            String fileContent = SpecsIo.read(outputFile);
            assertThat(fileContent).contains("1234567890");

            builder.close();
        }

        @Test
        @DisplayName("Should handle multiple buffer flushes")
        void testMultipleFlushes() {
            int smallCapacity = 5;
            BufferedStringBuilder builder = new BufferedStringBuilder(outputFile, smallCapacity);

            // Add content that triggers multiple flushes
            builder.append("12345"); // First flush
            builder.append("67890"); // Second flush
            builder.append("ABCDE"); // Third flush

            builder.close();

            assertThat(SpecsIo.read(outputFile)).isEqualTo("1234567890ABCDE");
        }

        @Test
        @DisplayName("Should save remaining content on close")
        void testSaveOnClose() {
            int largeCapacity = 1000;
            BufferedStringBuilder builder = new BufferedStringBuilder(outputFile, largeCapacity);

            builder.append("Small content that doesn't trigger auto-flush");

            // Content shouldn't be written yet
            assertThat(SpecsIo.read(outputFile)).isEmpty();

            builder.close();

            // Content should be written after close
            assertThat(SpecsIo.read(outputFile)).contains("Small content");
        }

        @Test
        @DisplayName("Should handle manual save operation")
        void testManualSave() {
            BufferedStringBuilder builder = new BufferedStringBuilder(outputFile);

            builder.append("Test content");
            builder.save(); // Manual save

            // Content should be written to file
            assertThat(SpecsIo.read(outputFile)).isEqualTo("Test content");

            // Should be able to continue appending
            builder.append(" more");
            builder.close();

            assertThat(SpecsIo.read(outputFile)).isEqualTo("Test content more");
        }
    }

    @Nested
    @DisplayName("Resource Management Tests")
    class ResourceManagementTests {

        @Test
        @DisplayName("Should handle close operation correctly")
        void testClose() {
            BufferedStringBuilder builder = new BufferedStringBuilder(outputFile);
            builder.append("Test content");

            builder.close();

            assertThat(SpecsIo.read(outputFile)).isEqualTo("Test content");
        }

        @Test
        @DisplayName("Should handle multiple close operations")
        void testMultipleClose() {
            BufferedStringBuilder builder = new BufferedStringBuilder(outputFile);
            builder.append("Test");

            builder.close();
            builder.close(); // Second close should not cause issues

            assertThat(SpecsIo.read(outputFile)).isEqualTo("Test");
        }

        @Test
        @DisplayName("Should handle operations after close")
        void testOperationsAfterClose() {
            BufferedStringBuilder builder = new BufferedStringBuilder(outputFile);
            builder.append("Initial");
            builder.close();

            // Append after close should return null and log warning
            BufferedStringBuilder result = builder.append("After close");

            assertThat(result).isNull();
            assertThat(SpecsIo.read(outputFile)).isEqualTo("Initial");
        }

        @Test
        @DisplayName("Should work with try-with-resources")
        void testTryWithResources() {
            try (BufferedStringBuilder builder = new BufferedStringBuilder(outputFile)) {
                builder.append("Auto-close test");
            }

            assertThat(SpecsIo.read(outputFile)).isEqualTo("Auto-close test");
        }
    }

    @Nested
    @DisplayName("Null String Builder Tests")
    class NullStringBuilderTests {

        @Test
        @DisplayName("Should create null string builder")
        void testNullStringBuilderCreation() {
            BufferedStringBuilder nullBuilder = BufferedStringBuilder.nullStringBuilder();

            assertThat(nullBuilder).isNotNull();
            assertThat(nullBuilder).isInstanceOf(NullStringBuilder.class);
        }

        @Test
        @DisplayName("Should handle append operations in null builder")
        void testNullBuilderAppend() {
            BufferedStringBuilder nullBuilder = BufferedStringBuilder.nullStringBuilder();

            BufferedStringBuilder result = nullBuilder.append("test");

            assertThat(result).isSameAs(nullBuilder); // Should return self

            // No file operations should occur
            nullBuilder.close();
        }

        @Test
        @DisplayName("Should handle save operations in null builder")
        void testNullBuilderSave() {
            BufferedStringBuilder nullBuilder = BufferedStringBuilder.nullStringBuilder();

            // Should not throw exception
            nullBuilder.save();
            nullBuilder.close();
        }
    }

    @Nested
    @DisplayName("ToString Method Tests")
    class BufferedStringBuilderToStringTest {

        @Test
        void nullStringBuilderToStringIsEmpty() {
            try (NullStringBuilder builder = new NullStringBuilder()) {
                assertThat(builder.toString()).isEmpty();

                builder.append("test");
                assertThat(builder.toString()).isEmpty();

                builder.save();
                assertThat(builder.toString()).isEmpty();
            }
        }

        @Test
        void bufferOnlyToStringShowsBuffer(@TempDir Path tempDir) {
            File out = tempDir.resolve("out.txt").toFile();

            BufferedStringBuilder builder = new BufferedStringBuilder(out);
            try {
                builder.append("hello");
                // Not saved yet
                assertThat(builder.toString()).isEqualTo("hello");
            } finally {
                builder.close();
            }
        }

        @Test
        void persistedAndBufferToString(@TempDir Path tempDir) {
            File out = tempDir.resolve("out2.txt").toFile();

            BufferedStringBuilder builder = new BufferedStringBuilder(out);
            try {
                builder.append("first");
                builder.save(); // persisted
                builder.append("second");

                assertThat(builder.toString()).isEqualTo("firstsecond");
            } finally {
                builder.close();
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle empty strings")
        void testEmptyStrings() {
            BufferedStringBuilder builder = new BufferedStringBuilder(outputFile);

            builder.append("");
            builder.append("");
            builder.close();

            assertThat(SpecsIo.read(outputFile)).isEmpty();
        }

        @Test
        @DisplayName("Should handle null string append")
        void testNullStringAppend() {
            BufferedStringBuilder builder = new BufferedStringBuilder(outputFile);

            builder.append((String) null);
            builder.close();

            assertThat(SpecsIo.read(outputFile)).contains("null");
        }

        @Test
        @DisplayName("Should handle null object append")
        void testNullObjectAppend() {
            // null objects should be converted to "null" string
            try (BufferedStringBuilder builder = new BufferedStringBuilder(outputFile)) {
                builder.append((Object) null);
                builder.close();

                String content = SpecsIo.read(outputFile);
                assertThat(content).isEqualTo("null");
            }
        }

        @Test
        @DisplayName("Should handle very large content")
        void testLargeContent() {
            BufferedStringBuilder builder = new BufferedStringBuilder(outputFile);

            // Create large string
            StringBuilder largeContent = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                largeContent.append("This is line ").append(i).append("\n");
            }

            builder.append(largeContent.toString());
            builder.close();

            String fileContent = SpecsIo.read(outputFile);
            assertThat(fileContent).contains("This is line 0");
            assertThat(fileContent).contains("This is line 999");
        }

        @Test
        @DisplayName("Should handle special characters")
        void testSpecialCharacters() {
            BufferedStringBuilder builder = new BufferedStringBuilder(outputFile);

            String specialChars = "Special chars: \t\n\r\"'\\äöü€";
            builder.append(specialChars);
            builder.close();

            assertThat(SpecsIo.read(outputFile)).isEqualTo(specialChars);
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @RetryingTest(5)
        @DisplayName("Should handle rapid successive appends")
        void testRapidAppends() {
            BufferedStringBuilder builder = new BufferedStringBuilder(outputFile);

            long startTime = System.currentTimeMillis();

            for (int i = 0; i < 10000; i++) {
                builder.append("Line " + i + "\n");
            }

            builder.close();

            long endTime = System.currentTimeMillis();
            assertThat(endTime - startTime).isLessThan(5000); // Should complete in reasonable time

            String content = SpecsIo.read(outputFile);
            assertThat(content).contains("Line 0");
            assertThat(content).contains("Line 9999");
        }

        @Test
        @DisplayName("Should handle buffer capacity edge cases")
        void testBufferCapacityEdges() {
            int capacity = 100;
            BufferedStringBuilder builder = new BufferedStringBuilder(outputFile, capacity);

            // Add exactly capacity amount
            StringBuilder exactCapacity = new StringBuilder();
            for (int i = 0; i < capacity; i++) {
                exactCapacity.append("X");
            }

            builder.append(exactCapacity.toString());
            builder.append("Y"); // Should trigger flush

            builder.close();

            String content = SpecsIo.read(outputFile);
            assertThat(content.length()).isEqualTo(capacity + 1);
            assertThat(content).endsWith("Y");
        }
    }
}
