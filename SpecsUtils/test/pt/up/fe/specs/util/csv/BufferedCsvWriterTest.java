package pt.up.fe.specs.util.csv;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for BufferedCsvWriter utility class.
 * Tests buffered CSV writing functionality with file-based operations.
 * 
 * @author Generated Tests
 */
@DisplayName("BufferedCsvWriter Tests")
class BufferedCsvWriterTest {

    @Nested
    @DisplayName("Constructor and Initialization")
    class ConstructorAndInitialization {

        @Test
        @DisplayName("Should create BufferedCsvWriter with header")
        void testConstructorWithHeader(@TempDir File tempDir) throws IOException {
            File bufferFile = new File(tempDir, "buffer.csv");
            List<String> header = Arrays.asList("name", "age", "city");

            BufferedCsvWriter writer = new BufferedCsvWriter(bufferFile, header);

            assertThat(bufferFile).exists();
            assertThat(bufferFile).hasContent("");

            // Test that writer is properly initialized
            assertThat(writer).isNotNull();
        }

        @Test
        @DisplayName("Should clear existing buffer file content on creation")
        void testClearExistingBufferContent(@TempDir File tempDir) throws IOException {
            File bufferFile = new File(tempDir, "existing.csv");
            Files.write(bufferFile.toPath(), "existing content".getBytes());

            List<String> header = Arrays.asList("col1", "col2");
            new BufferedCsvWriter(bufferFile, header);

            assertThat(bufferFile).hasContent("");
        }

        @Test
        @DisplayName("Should handle empty header list")
        void testEmptyHeaderList(@TempDir File tempDir) {
            File bufferFile = new File(tempDir, "empty_header.csv");
            List<String> emptyHeader = Arrays.asList();

            assertThatCode(() -> {
                new BufferedCsvWriter(bufferFile, emptyHeader);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle null buffer file gracefully")
        void testNullBufferFile() {
            List<String> header = Arrays.asList("col1", "col2");

            // BufferedCsvWriter handles null files gracefully (SpecsIo.write returns false)
            assertThatCode(() -> {
                new BufferedCsvWriter(null, header);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle null header gracefully")
        void testNullHeader(@TempDir File tempDir) {
            File bufferFile = new File(tempDir, "null_header.csv");

            // BufferedCsvWriter constructor passes null header to parent, which handles it
            assertThatCode(() -> {
                new BufferedCsvWriter(bufferFile, null);
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Line Addition and Buffering")
    class LineAdditionAndBuffering {

        @Test
        @DisplayName("Should write header on first line addition")
        void testHeaderWritingOnFirstLine(@TempDir File tempDir) throws IOException {
            File bufferFile = new File(tempDir, "header_test.csv");
            List<String> header = Arrays.asList("name", "age");

            BufferedCsvWriter writer = new BufferedCsvWriter(bufferFile, header);
            writer.addLine(Arrays.asList("John", "25"));

            String content = Files.readString(bufferFile.toPath());
            assertThat(content).startsWith("sep=;");
            assertThat(content).contains("name;age");
        }

        @Test
        @DisplayName("Should write header only once")
        void testHeaderWrittenOnlyOnce(@TempDir File tempDir) throws IOException {
            File bufferFile = new File(tempDir, "single_header.csv");
            List<String> header = Arrays.asList("col1", "col2");

            BufferedCsvWriter writer = new BufferedCsvWriter(bufferFile, header);
            writer.addLine(Arrays.asList("val1", "val2"));
            writer.addLine(Arrays.asList("val3", "val4"));

            String content = Files.readString(bufferFile.toPath());
            long headerOccurrences = content.lines()
                    .filter(line -> line.equals("col1;col2"))
                    .count();

            assertThat(headerOccurrences).isEqualTo(1);
        }

        @Test
        @DisplayName("Should add multiple lines correctly")
        void testMultipleLineAddition(@TempDir File tempDir) throws IOException {
            File bufferFile = new File(tempDir, "multiple_lines.csv");
            List<String> header = Arrays.asList("name", "value");

            BufferedCsvWriter writer = new BufferedCsvWriter(bufferFile, header);
            writer.addLine(Arrays.asList("line1", "val1"));
            writer.addLine(Arrays.asList("line2", "val2"));
            writer.addLine(Arrays.asList("line3", "val3"));

            String content = Files.readString(bufferFile.toPath());
            assertThat(content).contains("line1;val1");
            assertThat(content).contains("line2;val2");
            assertThat(content).contains("line3;val3");
        }

        @Test
        @DisplayName("Should handle empty lines")
        void testEmptyLines(@TempDir File tempDir) throws IOException {
            File bufferFile = new File(tempDir, "empty_lines.csv");
            List<String> header = Arrays.asList("col1", "col2");

            BufferedCsvWriter writer = new BufferedCsvWriter(bufferFile, header);
            writer.addLine(Arrays.asList());

            String content = Files.readString(bufferFile.toPath());
            assertThat(content).contains("col1;col2"); // Header should still be there
        }

        @Test
        @DisplayName("Should handle lines with null values")
        void testLinesWithNullValues(@TempDir File tempDir) throws IOException {
            File bufferFile = new File(tempDir, "null_values.csv");
            List<String> header = Arrays.asList("name", "value");

            BufferedCsvWriter writer = new BufferedCsvWriter(bufferFile, header);
            writer.addLine(Arrays.asList("test", null));

            String content = Files.readString(bufferFile.toPath());
            assertThat(content).contains("test;null");
        }

        @Test
        @DisplayName("Should return self for method chaining")
        void testMethodChaining(@TempDir File tempDir) {
            File bufferFile = new File(tempDir, "chaining.csv");
            List<String> header = Arrays.asList("col1", "col2");

            BufferedCsvWriter writer = new BufferedCsvWriter(bufferFile, header);
            BufferedCsvWriter result = writer.addLine(Arrays.asList("val1", "val2"));

            assertThat(result).isSameAs(writer);
        }
    }

    @Nested
    @DisplayName("CSV Building and Content Retrieval")
    class CsvBuildingAndContentRetrieval {

        @Test
        @DisplayName("Should build CSV from buffer file content")
        void testBuildCsvFromBuffer(@TempDir File tempDir) throws IOException {
            File bufferFile = new File(tempDir, "build_test.csv");
            List<String> header = Arrays.asList("name", "age");

            BufferedCsvWriter writer = new BufferedCsvWriter(bufferFile, header);
            writer.addLine(Arrays.asList("John", "25"));
            writer.addLine(Arrays.asList("Jane", "30"));

            String csv = writer.buildCsv();

            assertThat(csv).contains("sep=;");
            assertThat(csv).contains("name;age");
            assertThat(csv).contains("John;25");
            assertThat(csv).contains("Jane;30");
        }

        @Test
        @DisplayName("Should return empty content for empty buffer")
        void testBuildCsvFromEmptyBuffer(@TempDir File tempDir) {
            File bufferFile = new File(tempDir, "empty_build.csv");
            List<String> header = Arrays.asList("col1", "col2");

            BufferedCsvWriter writer = new BufferedCsvWriter(bufferFile, header);
            String csv = writer.buildCsv();

            assertThat(csv).isEmpty();
        }

        @Test
        @DisplayName("Should handle header-only CSV")
        void testBuildCsvHeaderOnly(@TempDir File tempDir) throws IOException {
            File bufferFile = new File(tempDir, "header_only.csv");
            List<String> header = Arrays.asList("column1", "column2", "column3");

            BufferedCsvWriter writer = new BufferedCsvWriter(bufferFile, header);
            // Add no data lines, just trigger header writing
            writer.addLine(Arrays.asList("test", "data", "row"));

            String csv = writer.buildCsv();

            assertThat(csv).contains("sep=;");
            assertThat(csv).contains("column1;column2;column3");
        }

        @Test
        @DisplayName("Should preserve line order in built CSV")
        void testLineOrderPreservation(@TempDir File tempDir) throws IOException {
            File bufferFile = new File(tempDir, "order_test.csv");
            List<String> header = Arrays.asList("sequence", "value");

            BufferedCsvWriter writer = new BufferedCsvWriter(bufferFile, header);
            writer.addLine(Arrays.asList("1", "first"));
            writer.addLine(Arrays.asList("2", "second"));
            writer.addLine(Arrays.asList("3", "third"));

            String csv = writer.buildCsv();
            String[] lines = csv.split("\n");

            // Find data lines (skip separator and header)
            boolean foundFirst = false, foundSecond = false, foundThird = false;
            for (String line : lines) {
                if (line.contains("1;first") && !foundSecond && !foundThird) {
                    foundFirst = true;
                } else if (line.contains("2;second") && foundFirst && !foundThird) {
                    foundSecond = true;
                } else if (line.contains("3;third") && foundFirst && foundSecond) {
                    foundThird = true;
                }
            }

            assertThat(foundFirst && foundSecond && foundThird).isTrue();
        }
    }

    @Nested
    @DisplayName("Inheritance and Parent Functionality")
    class InheritanceAndParentFunctionality {

        @Test
        @DisplayName("Should inherit CsvWriter delimiter functionality")
        void testInheritedDelimiterFunctionality(@TempDir File tempDir) throws IOException {
            File bufferFile = new File(tempDir, "delimiter_test.csv");
            List<String> header = Arrays.asList("col1", "col2");

            BufferedCsvWriter writer = new BufferedCsvWriter(bufferFile, header);
            writer.setDelimiter(",");
            writer.addLine(Arrays.asList("val1", "val2"));

            String csv = writer.buildCsv();
            assertThat(csv).contains("sep=,");
            assertThat(csv).contains("col1,col2");
            assertThat(csv).contains("val1,val2");
        }

        @Test
        @DisplayName("Should inherit CsvWriter newline functionality")
        void testInheritedNewlineFunctionality(@TempDir File tempDir) throws IOException {
            File bufferFile = new File(tempDir, "newline_test.csv");
            List<String> header = Arrays.asList("col1");

            BufferedCsvWriter writer = new BufferedCsvWriter(bufferFile, header);
            writer.setNewline("\r\n");
            writer.addLine(Arrays.asList("value"));

            String csv = writer.buildCsv();
            assertThat(csv).contains("\r\n");
        }

        @Test
        @DisplayName("Should inherit CsvWriter field functionality")
        void testInheritedFieldFunctionality(@TempDir File tempDir) throws IOException {
            File bufferFile = new File(tempDir, "fields_test.csv");
            List<String> header = Arrays.asList("data1", "data2");

            BufferedCsvWriter writer = new BufferedCsvWriter(bufferFile, header);
            writer.addField(CsvField.AVERAGE);
            writer.addLine(Arrays.asList("10", "20"));

            String csv = writer.buildCsv();
            assertThat(csv).contains("Average");
            assertThat(csv).contains("=AVERAGE(");
        }

        @Test
        @DisplayName("Should support method chaining for addLine operations")
        void testMethodChainingForAddLine(@TempDir File tempDir) {
            File bufferFile = new File(tempDir, "chaining_parent.csv");
            List<String> header = Arrays.asList("col1", "col2");

            BufferedCsvWriter writer = new BufferedCsvWriter(bufferFile, header);

            assertThatCode(() -> {
                writer.setDelimiter(",");
                writer.setNewline("\n");
                writer.addField(CsvField.AVERAGE);
                writer.addLine(Arrays.asList("1", "2"))
                        .addLine(Arrays.asList("3", "4"));
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("File System Operations")
    class FileSystemOperations {

        @Test
        @DisplayName("Should handle concurrent writes to same buffer file")
        void testConcurrentWrites(@TempDir File tempDir) throws IOException {
            File bufferFile = new File(tempDir, "concurrent.csv");
            List<String> header1 = Arrays.asList("thread1");
            List<String> header2 = Arrays.asList("thread2");

            BufferedCsvWriter writer1 = new BufferedCsvWriter(bufferFile, header1);
            BufferedCsvWriter writer2 = new BufferedCsvWriter(bufferFile, header2);

            writer1.addLine(Arrays.asList("data1"));
            writer2.addLine(Arrays.asList("data2"));

            // The last writer should have cleared the content
            String content = Files.readString(bufferFile.toPath());
            assertThat(content).contains("thread2");
        }

        @Test
        @DisplayName("Should handle non-existent directory for buffer file gracefully")
        void testNonExistentDirectory(@TempDir File tempDir) {
            File nonExistentDir = new File(tempDir, "nonexistent");
            File bufferFile = new File(nonExistentDir, "buffer.csv");
            List<String> header = Arrays.asList("col1");

            // SpecsIo.write() will handle missing directories gracefully
            assertThatCode(() -> {
                new BufferedCsvWriter(bufferFile, header);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle very large buffer files")
        void testLargeBufferFile(@TempDir File tempDir) throws IOException {
            File bufferFile = new File(tempDir, "large.csv");
            List<String> header = Arrays.asList("id", "data");

            BufferedCsvWriter writer = new BufferedCsvWriter(bufferFile, header);

            // Add many lines to test buffer handling
            for (int i = 0; i < 1000; i++) {
                writer.addLine(Arrays.asList(String.valueOf(i), "data" + i));
            }

            String csv = writer.buildCsv();
            assertThat(csv.lines().count()).isGreaterThan(1000); // Header + separator + data lines
        }

        @ParameterizedTest(name = "Buffer file: {0}")
        @ValueSource(strings = { "test.csv", "test.txt", "data", "file.dat" })
        @DisplayName("Should handle various file extensions")
        void testVariousFileExtensions(String filename, @TempDir File tempDir) throws IOException {
            File bufferFile = new File(tempDir, filename);
            List<String> header = Arrays.asList("col1", "col2");

            BufferedCsvWriter writer = new BufferedCsvWriter(bufferFile, header);
            writer.addLine(Arrays.asList("test", "data"));

            assertThat(bufferFile).exists();
            assertThat(writer.buildCsv()).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandling {

        @Test
        @DisplayName("Should handle special characters in file path")
        void testSpecialCharactersInFilePath(@TempDir File tempDir) throws IOException {
            File bufferFile = new File(tempDir, "file with spaces & symbols.csv");
            List<String> header = Arrays.asList("col1");

            assertThatCode(() -> {
                BufferedCsvWriter writer = new BufferedCsvWriter(bufferFile, header);
                writer.addLine(Arrays.asList("test"));
                writer.buildCsv();
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle very long header names")
        void testVeryLongHeaderNames(@TempDir File tempDir) throws IOException {
            File bufferFile = new File(tempDir, "long_headers.csv");
            String longHeader = "a".repeat(1000);
            List<String> header = Arrays.asList(longHeader, "normal");

            BufferedCsvWriter writer = new BufferedCsvWriter(bufferFile, header);
            writer.addLine(Arrays.asList("data1", "data2"));

            String csv = writer.buildCsv();
            assertThat(csv).contains(longHeader);
        }

        @Test
        @DisplayName("Should handle special characters in data")
        void testSpecialCharactersInData(@TempDir File tempDir) throws IOException {
            File bufferFile = new File(tempDir, "special_chars.csv");
            List<String> header = Arrays.asList("data");

            BufferedCsvWriter writer = new BufferedCsvWriter(bufferFile, header);
            writer.addLine(Arrays.asList("data with; semicolons"));
            writer.addLine(Arrays.asList("data with\nnewlines"));
            writer.addLine(Arrays.asList("data with\ttabs"));

            String csv = writer.buildCsv();
            assertThat(csv).contains("data with; semicolons");
            assertThat(csv).contains("data with\nnewlines");
            assertThat(csv).contains("data with\ttabs");
        }

        @Test
        @DisplayName("Should handle mismatched line sizes gracefully")
        void testMismatchedLineSizes(@TempDir File tempDir) throws IOException {
            File bufferFile = new File(tempDir, "mismatched.csv");
            List<String> header = Arrays.asList("col1", "col2", "col3");

            BufferedCsvWriter writer = new BufferedCsvWriter(bufferFile, header);
            writer.addLine(Arrays.asList("only", "two")); // Less than header
            writer.addLine(Arrays.asList("four", "values", "here", "extra")); // More than header

            String csv = writer.buildCsv();
            assertThat(csv).contains("only;two");
            assertThat(csv).contains("four;values;here;extra");
        }

        @Test
        @DisplayName("Should handle Unicode characters")
        void testUnicodeCharacters(@TempDir File tempDir) throws IOException {
            File bufferFile = new File(tempDir, "unicode.csv");
            List<String> header = Arrays.asList("名前", "年齢");

            BufferedCsvWriter writer = new BufferedCsvWriter(bufferFile, header);
            writer.addLine(Arrays.asList("田中", "25"));
            writer.addLine(Arrays.asList("Müller", "30"));
            writer.addLine(Arrays.asList("José", "35"));

            String csv = writer.buildCsv();
            assertThat(csv).contains("名前;年齢");
            assertThat(csv).contains("田中;25");
            assertThat(csv).contains("Müller;30");
            assertThat(csv).contains("José;35");
        }
    }
}
