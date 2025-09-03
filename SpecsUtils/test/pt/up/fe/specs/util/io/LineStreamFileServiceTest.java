package pt.up.fe.specs.util.io;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Comprehensive test suite for LineStreamFileService class.
 * 
 * Tests the LineStreamFileService implementation including line-based file
 * streaming, caching functionality, resource management, and integration with
 * the FileService interface.
 * 
 * @author Generated Tests
 */
@DisplayName("LineStreamFileService Tests")
class LineStreamFileServiceTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create LineStreamFileService successfully")
        void testConstructor() {
            // When
            LineStreamFileService service = new LineStreamFileService();

            // Then
            assertThat(service).isNotNull();
            assertThat(service).isInstanceOf(FileService.class);
        }

        @Test
        @DisplayName("Should initialize empty cache")
        void testInitialCacheState() throws Exception {
            // Given
            LineStreamFileService service = new LineStreamFileService();

            // When/Then - Should work without any cached files
            try (service) {
                // Cache is initially empty, so no cached files to test
                assertThat(service).isNotNull();
            }
        }
    }

    @Nested
    @DisplayName("getLine Method Tests")
    class GetLineMethodTests {

        @Test
        @DisplayName("Should read first line from file")
        void testReadFirstLine(@TempDir Path tempDir) throws Exception {
            // Given
            Path testFile = tempDir.resolve("test.txt");
            Files.write(testFile, "First line\nSecond line\nThird line".getBytes());

            // When
            try (LineStreamFileService service = new LineStreamFileService()) {
                String result = service.getLine(testFile.toFile(), 1);

                // Then
                assertThat(result).isEqualTo("First line");
            }
        }

        @Test
        @DisplayName("Should read specific line from file")
        void testReadSpecificLine(@TempDir Path tempDir) throws Exception {
            // Given
            Path testFile = tempDir.resolve("multiline.txt");
            Files.write(testFile, "Line 1\nLine 2\nLine 3\nLine 4\nLine 5".getBytes());

            // When
            try (LineStreamFileService service = new LineStreamFileService()) {
                String result = service.getLine(testFile.toFile(), 3);

                // Then
                assertThat(result).isEqualTo("Line 3");
            }
        }

        @Test
        @DisplayName("Should read sequential lines efficiently")
        void testReadSequentialLineReading(@TempDir Path tempDir) throws Exception {
            // Given
            Path testFile = tempDir.resolve("sequential.txt");
            Files.write(testFile, "Line 1\nLine 2\nLine 3\nLine 4\nLine 5".getBytes());

            // When
            try (LineStreamFileService service = new LineStreamFileService()) {
                String line1 = service.getLine(testFile.toFile(), 1);
                String line2 = service.getLine(testFile.toFile(), 2);
                String line3 = service.getLine(testFile.toFile(), 3);

                // Then
                assertThat(line1).isEqualTo("Line 1");
                assertThat(line2).isEqualTo("Line 2");
                assertThat(line3).isEqualTo("Line 3");
            }
        }

        @Test
        @DisplayName("Should handle backward line access by reloading file")
        void testBackwardLineAccess(@TempDir Path tempDir) throws Exception {
            // Given
            Path testFile = tempDir.resolve("backward.txt");
            Files.write(testFile, "Line 1\nLine 2\nLine 3\nLine 4\nLine 5".getBytes());

            // When
            try (LineStreamFileService service = new LineStreamFileService()) {
                String line4 = service.getLine(testFile.toFile(), 4);
                String line2 = service.getLine(testFile.toFile(), 2); // Should reload file

                // Then
                assertThat(line4).isEqualTo("Line 4");
                assertThat(line2).isEqualTo("Line 2");
            }
        }

        @Test
        @DisplayName("Should handle empty lines")
        void testEmptyLines(@TempDir Path tempDir) throws Exception {
            // Given
            Path testFile = tempDir.resolve("emptylines.txt");
            Files.write(testFile, "Line 1\n\nLine 3\n\nLine 5".getBytes());

            // When
            try (LineStreamFileService service = new LineStreamFileService()) {
                String line1 = service.getLine(testFile.toFile(), 1);
                String line2 = service.getLine(testFile.toFile(), 2);
                String line3 = service.getLine(testFile.toFile(), 3);
                String line4 = service.getLine(testFile.toFile(), 4);
                String line5 = service.getLine(testFile.toFile(), 5);

                // Then
                assertThat(line1).isEqualTo("Line 1");
                assertThat(line2).isEmpty();
                assertThat(line3).isEqualTo("Line 3");
                assertThat(line4).isEmpty();
                assertThat(line5).isEqualTo("Line 5");
            }
        }

        @Test
        @DisplayName("Should handle large line numbers")
        void testLargeLineNumbers(@TempDir Path tempDir) throws Exception {
            // Given
            Path testFile = tempDir.resolve("large.txt");
            StringBuilder content = new StringBuilder();
            for (int i = 1; i <= 1000; i++) {
                content.append("Line ").append(i).append("\n");
            }
            Files.write(testFile, content.toString().getBytes());

            // When
            try (LineStreamFileService service = new LineStreamFileService()) {
                String line500 = service.getLine(testFile.toFile(), 500);
                String line1000 = service.getLine(testFile.toFile(), 1000);

                // Then
                assertThat(line500).isEqualTo("Line 500");
                assertThat(line1000).isEqualTo("Line 1000");
            }
        }

        @Test
        @DisplayName("Should handle line number beyond file length")
        void testLineNumberBeyondFile(@TempDir Path tempDir) throws Exception {
            // Given
            Path testFile = tempDir.resolve("short.txt");
            Files.write(testFile, "Only line".getBytes());

            // When
            try (LineStreamFileService service = new LineStreamFileService()) {
                String line1 = service.getLine(testFile.toFile(), 1);
                String line2 = service.getLine(testFile.toFile(), 2);

                // Then
                assertThat(line1).isEqualTo("Only line");
                assertThat(line2).isNull(); // Beyond file length
            }
        }
    }

    @Nested
    @DisplayName("Caching Behavior Tests")
    class CachingBehaviorTests {

        @Test
        @DisplayName("Should cache file for sequential access")
        void testFileCaching(@TempDir Path tempDir) throws Exception {
            // Given
            Path testFile = tempDir.resolve("cached.txt");
            Files.write(testFile, "Line 1\nLine 2\nLine 3".getBytes());

            // When
            try (LineStreamFileService service = new LineStreamFileService()) {
                // First access should cache the file
                String line1First = service.getLine(testFile.toFile(), 1);
                String line2First = service.getLine(testFile.toFile(), 2);

                // Second access should use cached version
                String line1Second = service.getLine(testFile.toFile(), 1); // This should reload
                String line2Second = service.getLine(testFile.toFile(), 2);

                // Then
                assertThat(line1First).isEqualTo("Line 1");
                assertThat(line2First).isEqualTo("Line 2");
                assertThat(line1Second).isEqualTo("Line 1");
                assertThat(line2Second).isEqualTo("Line 2");
            }
        }

        @Test
        @DisplayName("Should handle multiple files in cache")
        void testMultipleFilesCaching(@TempDir Path tempDir) throws Exception {
            // Given
            Path file1 = tempDir.resolve("file1.txt");
            Path file2 = tempDir.resolve("file2.txt");
            Files.write(file1, "File1 Line1\nFile1 Line2".getBytes());
            Files.write(file2, "File2 Line1\nFile2 Line2".getBytes());

            // When
            try (LineStreamFileService service = new LineStreamFileService()) {
                String file1Line1 = service.getLine(file1.toFile(), 1);
                String file2Line1 = service.getLine(file2.toFile(), 1);
                String file1Line2 = service.getLine(file1.toFile(), 2);
                String file2Line2 = service.getLine(file2.toFile(), 2);

                // Then
                assertThat(file1Line1).isEqualTo("File1 Line1");
                assertThat(file2Line1).isEqualTo("File2 Line1");
                assertThat(file1Line2).isEqualTo("File1 Line2");
                assertThat(file2Line2).isEqualTo("File2 Line2");
            }
        }

        @Test
        @DisplayName("Should reload file when accessing previous line")
        void testFileReloading(@TempDir Path tempDir) throws Exception {
            // Given
            Path testFile = tempDir.resolve("reload.txt");
            Files.write(testFile, "Line 1\nLine 2\nLine 3\nLine 4".getBytes());

            // When
            try (LineStreamFileService service = new LineStreamFileService()) {
                String line3 = service.getLine(testFile.toFile(), 3);
                String line1 = service.getLine(testFile.toFile(), 1); // Should trigger reload

                // Then
                assertThat(line3).isEqualTo("Line 3");
                assertThat(line1).isEqualTo("Line 1");
            }
        }
    }

    @Nested
    @DisplayName("Resource Management Tests")
    class ResourceManagementTests {

        @Test
        @DisplayName("Should implement AutoCloseable correctly")
        void testAutoCloseable(@TempDir Path tempDir) throws Exception {
            // Given
            Path testFile = tempDir.resolve("closeable.txt");
            Files.write(testFile, "Test content".getBytes());

            // When/Then - Should not throw exception
            assertThatCode(() -> {
                try (LineStreamFileService service = new LineStreamFileService()) {
                    service.getLine(testFile.toFile(), 1);
                }
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should close all cached streams on close")
        void testCloseAllStreams(@TempDir Path tempDir) throws Exception {
            // Given
            Path file1 = tempDir.resolve("file1.txt");
            Path file2 = tempDir.resolve("file2.txt");
            Files.write(file1, "Content 1".getBytes());
            Files.write(file2, "Content 2".getBytes());

            LineStreamFileService service = new LineStreamFileService();

            // When
            service.getLine(file1.toFile(), 1); // Cache file1
            service.getLine(file2.toFile(), 1); // Cache file2

            // Then - Close should not throw exception
            assertThatCode(() -> service.close()).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle close with no cached files")
        void testCloseWithNoCachedFiles() {
            // Given
            LineStreamFileService service = new LineStreamFileService();

            // When/Then
            assertThatCode(() -> service.close()).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle multiple close calls")
        void testMultipleCloseCalls(@TempDir Path tempDir) throws Exception {
            // Given
            Path testFile = tempDir.resolve("multiple_close.txt");
            Files.write(testFile, "Test content".getBytes());

            LineStreamFileService service = new LineStreamFileService();
            service.getLine(testFile.toFile(), 1);

            // When/Then - Multiple closes should not throw
            assertThatCode(() -> {
                service.close();
                service.close();
                service.close();
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle empty file")
        void testEmptyFile(@TempDir Path tempDir) throws Exception {
            // Given
            Path emptyFile = tempDir.resolve("empty.txt");
            Files.createFile(emptyFile);

            // When
            try (LineStreamFileService service = new LineStreamFileService()) {
                String result = service.getLine(emptyFile.toFile(), 1);

                // Then
                assertThat(result).isNull();
            }
        }

        @Test
        @DisplayName("Should handle file with only newlines")
        void testFileWithOnlyNewlines(@TempDir Path tempDir) throws Exception {
            // Given
            Path newlineFile = tempDir.resolve("newlines.txt");
            Files.write(newlineFile, "\n\n\n".getBytes());

            // When
            try (LineStreamFileService service = new LineStreamFileService()) {
                String line1 = service.getLine(newlineFile.toFile(), 1);
                String line2 = service.getLine(newlineFile.toFile(), 2);
                String line3 = service.getLine(newlineFile.toFile(), 3);
                String line4 = service.getLine(newlineFile.toFile(), 4);

                // Then
                assertThat(line1).isEmpty();
                assertThat(line2).isEmpty();
                assertThat(line3).isEmpty();
                assertThat(line4).isNull(); // Beyond file
            }
        }

        @Test
        @DisplayName("Should handle special characters")
        void testSpecialCharacters(@TempDir Path tempDir) throws Exception {
            // Given
            Path specialFile = tempDir.resolve("special.txt");
            Files.write(specialFile, "Special: αβγδε\nUnicode: 你好\nSymbols: !@#$%".getBytes());

            // When
            try (LineStreamFileService service = new LineStreamFileService()) {
                String line1 = service.getLine(specialFile.toFile(), 1);
                String line2 = service.getLine(specialFile.toFile(), 2);
                String line3 = service.getLine(specialFile.toFile(), 3);

                // Then
                assertThat(line1).isEqualTo("Special: αβγδε");
                assertThat(line2).isEqualTo("Unicode: 你好");
                assertThat(line3).isEqualTo("Symbols: !@#$%");
            }
        }

        @Test
        @DisplayName("Should handle very long lines")
        void testVeryLongLines(@TempDir Path tempDir) throws Exception {
            // Given
            StringBuilder longLine = new StringBuilder();
            for (int i = 0; i < 10000; i++) {
                longLine.append("word").append(i).append(" ");
            }
            Path longLineFile = tempDir.resolve("longline.txt");
            Files.write(longLineFile, (longLine.toString() + "\nShort line").getBytes());

            // When
            try (LineStreamFileService service = new LineStreamFileService()) {
                String line1 = service.getLine(longLineFile.toFile(), 1);
                String line2 = service.getLine(longLineFile.toFile(), 2);

                // Then
                assertThat(line1).startsWith("word0 word1");
                assertThat(line1).endsWith("word9999 ");
                assertThat(line1.length()).isGreaterThan(50000);
                assertThat(line2).isEqualTo("Short line");
            }
        }

        @Test
        @DisplayName("Should handle different line endings")
        void testDifferentLineEndings(@TempDir Path tempDir) throws Exception {
            // Given
            Path lineEndingFile = tempDir.resolve("lineendings.txt");
            Files.write(lineEndingFile, "Unix\nWindows\r\nMac\rMixed\r\n".getBytes());

            // When
            try (LineStreamFileService service = new LineStreamFileService()) {
                String line1 = service.getLine(lineEndingFile.toFile(), 1);
                String line2 = service.getLine(lineEndingFile.toFile(), 2);
                String line3 = service.getLine(lineEndingFile.toFile(), 3);
                String line4 = service.getLine(lineEndingFile.toFile(), 4);

                // Then
                assertThat(line1).isEqualTo("Unix");
                assertThat(line2).isEqualTo("Windows");
                assertThat(line3).isEqualTo("Mac");
                assertThat(line4).isEqualTo("Mixed");
            }
        }

        @Test
        @DisplayName("Should handle non-existent file gracefully")
        void testNonExistentFile() {
            // Given
            File nonExistentFile = new File("/non/existent/file.txt");

            // When/Then
            try (LineStreamFileService service = new LineStreamFileService()) {
                assertThatThrownBy(() -> service.getLine(nonExistentFile, 1))
                        .isInstanceOf(RuntimeException.class);
            } catch (Exception e) {
                // Expected for resource management
            }
        }
    }

    @Nested
    @DisplayName("Performance and Integration Tests")
    class PerformanceIntegrationTests {

        @Test
        @DisplayName("Should handle concurrent file access")
        void testConcurrentAccess(@TempDir Path tempDir) throws Exception {
            // Given
            Path testFile = tempDir.resolve("concurrent.txt");
            StringBuilder content = new StringBuilder();
            for (int i = 1; i <= 100; i++) {
                content.append("Line ").append(i).append("\n");
            }
            Files.write(testFile, content.toString().getBytes());

            // When
            try (LineStreamFileService service = new LineStreamFileService()) {
                Thread[] threads = new Thread[10];
                String[] results = new String[10];

                for (int i = 0; i < threads.length; i++) {
                    final int index = i;
                    threads[i] = new Thread(() -> {
                        results[index] = service.getLine(testFile.toFile(), (index % 10) + 1);
                    });
                    threads[i].start();
                }

                for (Thread thread : threads) {
                    thread.join();
                }

                // Then
                for (int i = 0; i < results.length; i++) {
                    int expectedLine = (i % 10) + 1;
                    assertThat(results[i]).isEqualTo("Line " + expectedLine);
                }
            }
        }

        @Test
        @DisplayName("Should handle random access patterns efficiently")
        void testRandomAccessPatterns(@TempDir Path tempDir) throws Exception {
            // Given
            Path testFile = tempDir.resolve("random.txt");
            StringBuilder content = new StringBuilder();
            for (int i = 1; i <= 50; i++) {
                content.append("Line ").append(i).append("\n");
            }
            Files.write(testFile, content.toString().getBytes());

            // When
            try (LineStreamFileService service = new LineStreamFileService()) {
                int[] accessPattern = { 25, 1, 50, 10, 30, 5, 45, 15, 35, 20 };

                for (int lineNum : accessPattern) {
                    String result = service.getLine(testFile.toFile(), lineNum);
                    assertThat(result).isEqualTo("Line " + lineNum);
                }
            }
        }

        @Test
        @DisplayName("Should maintain consistent behavior across multiple operations")
        void testConsistentBehavior(@TempDir Path tempDir) throws Exception {
            // Given
            Path testFile = tempDir.resolve("consistent.txt");
            Files.write(testFile, "Consistent Line 1\nConsistent Line 2\nConsistent Line 3".getBytes());

            // When
            try (LineStreamFileService service = new LineStreamFileService()) {
                // Multiple accesses to same line should return same result
                for (int i = 0; i < 5; i++) {
                    String line1 = service.getLine(testFile.toFile(), 1);
                    String line2 = service.getLine(testFile.toFile(), 2);
                    String line3 = service.getLine(testFile.toFile(), 3);

                    assertThat(line1).isEqualTo("Consistent Line 1");
                    assertThat(line2).isEqualTo("Consistent Line 2");
                    assertThat(line3).isEqualTo("Consistent Line 3");
                }
            }
        }
    }
}
