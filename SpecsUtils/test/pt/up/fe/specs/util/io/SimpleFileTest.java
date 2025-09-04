package pt.up.fe.specs.util.io;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for SimpleFile interface.
 * 
 * Tests the SimpleFile interface implementation including file content
 * management, filename handling, factory methods, and edge cases for file
 * representation.
 * 
 * @author Generated Tests
 */
@DisplayName("SimpleFile Tests")
class SimpleFileTest {

    @Nested
    @DisplayName("newInstance Factory Method Tests")
    class NewInstanceTests {

        @Test
        @DisplayName("Should create SimpleFile with valid filename and content")
        void testNewInstanceBasic() {
            // Given
            String filename = "test.txt";
            String contents = "Hello, World!";

            // When
            SimpleFile simpleFile = SimpleFile.newInstance(filename, contents);

            // Then
            assertThat(simpleFile).isNotNull();
            assertThat(simpleFile.getFilename()).isEqualTo(filename);
            assertThat(simpleFile.getContents()).isEqualTo(contents);
        }

        @Test
        @DisplayName("Should create SimpleFile with empty content")
        void testNewInstanceEmptyContent() {
            // Given
            String filename = "empty.txt";
            String contents = "";

            // When
            SimpleFile simpleFile = SimpleFile.newInstance(filename, contents);

            // Then
            assertThat(simpleFile).isNotNull();
            assertThat(simpleFile.getFilename()).isEqualTo(filename);
            assertThat(simpleFile.getContents()).isEmpty();
        }

        @Test
        @DisplayName("Should create SimpleFile with null content")
        void testNewInstanceNullContent() {
            // Given
            String filename = "nullcontent.txt";
            String contents = null;

            // When
            SimpleFile simpleFile = SimpleFile.newInstance(filename, contents);

            // Then
            assertThat(simpleFile).isNotNull();
            assertThat(simpleFile.getFilename()).isEqualTo(filename);
            assertThat(simpleFile.getContents()).isNull();
        }

        @Test
        @DisplayName("Should create SimpleFile with null filename")
        void testNewInstanceNullFilename() {
            // Given
            String filename = null;
            String contents = "some content";

            // When
            SimpleFile simpleFile = SimpleFile.newInstance(filename, contents);

            // Then
            assertThat(simpleFile).isNotNull();
            assertThat(simpleFile.getFilename()).isNull();
            assertThat(simpleFile.getContents()).isEqualTo(contents);
        }

        @Test
        @DisplayName("Should create SimpleFile with both null parameters")
        void testNewInstanceBothNull() {
            // Given
            String filename = null;
            String contents = null;

            // When
            SimpleFile simpleFile = SimpleFile.newInstance(filename, contents);

            // Then
            assertThat(simpleFile).isNotNull();
            assertThat(simpleFile.getFilename()).isNull();
            assertThat(simpleFile.getContents()).isNull();
        }
    }

    @Nested
    @DisplayName("File Content Tests")
    class FileContentTests {

        @Test
        @DisplayName("Should handle multiline content")
        void testMultilineContent() {
            // Given
            String filename = "multiline.txt";
            String contents = "Line 1\nLine 2\nLine 3\n";

            // When
            SimpleFile simpleFile = SimpleFile.newInstance(filename, contents);

            // Then
            assertThat(simpleFile.getContents()).isEqualTo(contents);
            assertThat(simpleFile.getContents()).contains("\n");
        }

        @Test
        @DisplayName("Should handle special characters in content")
        void testSpecialCharacters() {
            // Given
            String filename = "special.txt";
            String contents = "Special chars: !@#$%^&*()_+{}|:\"<>?[]\\;',./ and unicode: αβγδε";

            // When
            SimpleFile simpleFile = SimpleFile.newInstance(filename, contents);

            // Then
            assertThat(simpleFile.getContents()).isEqualTo(contents);
            assertThat(simpleFile.getContents()).contains("αβγδε");
        }

        @Test
        @DisplayName("Should handle large content")
        void testLargeContent() {
            // Given
            String filename = "large.txt";
            StringBuilder largeContent = new StringBuilder();
            for (int i = 0; i < 10000; i++) {
                largeContent.append("This is line ").append(i).append("\n");
            }
            String contents = largeContent.toString();

            // When
            SimpleFile simpleFile = SimpleFile.newInstance(filename, contents);

            // Then
            assertThat(simpleFile.getContents()).isEqualTo(contents);
            assertThat(simpleFile.getContents().length()).isGreaterThan(100000);
        }

        @Test
        @DisplayName("Should handle binary-like content")
        void testBinaryLikeContent() {
            // Given
            String filename = "binary.dat";
            String contents = "\0\1\2\3\4\5\255\254\253";

            // When
            SimpleFile simpleFile = SimpleFile.newInstance(filename, contents);

            // Then
            assertThat(simpleFile.getContents()).isEqualTo(contents);
        }

        @Test
        @DisplayName("Should handle content with different line endings")
        void testDifferentLineEndings() {
            // Given
            String filename = "lineendings.txt";
            String contents = "Unix\nWindows\r\nMac\rMixed\r\n\n";

            // When
            SimpleFile simpleFile = SimpleFile.newInstance(filename, contents);

            // Then
            assertThat(simpleFile.getContents()).isEqualTo(contents);
            assertThat(simpleFile.getContents()).contains("\n");
            assertThat(simpleFile.getContents()).contains("\r\n");
            assertThat(simpleFile.getContents()).contains("\r");
        }
    }

    @Nested
    @DisplayName("Filename Tests")
    class FilenameTests {

        @Test
        @DisplayName("Should handle simple filename")
        void testSimpleFilename() {
            // Given
            String filename = "document.txt";
            String contents = "content";

            // When
            SimpleFile simpleFile = SimpleFile.newInstance(filename, contents);

            // Then
            assertThat(simpleFile.getFilename()).isEqualTo(filename);
        }

        @Test
        @DisplayName("Should handle filename with path")
        void testFilenameWithPath() {
            // Given
            String filename = "/path/to/document.txt";
            String contents = "content";

            // When
            SimpleFile simpleFile = SimpleFile.newInstance(filename, contents);

            // Then
            assertThat(simpleFile.getFilename()).isEqualTo(filename);
        }

        @Test
        @DisplayName("Should handle filename without extension")
        void testFilenameWithoutExtension() {
            // Given
            String filename = "README";
            String contents = "readme content";

            // When
            SimpleFile simpleFile = SimpleFile.newInstance(filename, contents);

            // Then
            assertThat(simpleFile.getFilename()).isEqualTo(filename);
        }

        @Test
        @DisplayName("Should handle filename with multiple extensions")
        void testFilenameMultipleExtensions() {
            // Given
            String filename = "archive.tar.gz";
            String contents = "compressed content";

            // When
            SimpleFile simpleFile = SimpleFile.newInstance(filename, contents);

            // Then
            assertThat(simpleFile.getFilename()).isEqualTo(filename);
        }

        @Test
        @DisplayName("Should handle filename with spaces and special chars")
        void testFilenameSpecialChars() {
            // Given
            String filename = "file with spaces & symbols!@#.txt";
            String contents = "content";

            // When
            SimpleFile simpleFile = SimpleFile.newInstance(filename, contents);

            // Then
            assertThat(simpleFile.getFilename()).isEqualTo(filename);
        }

        @Test
        @DisplayName("Should handle very long filename")
        void testVeryLongFilename() {
            // Given
            StringBuilder longName = new StringBuilder();
            for (int i = 0; i < 100; i++) {
                longName.append("very_long_name_");
            }
            longName.append(".txt");
            String filename = longName.toString();
            String contents = "content";

            // When
            SimpleFile simpleFile = SimpleFile.newInstance(filename, contents);

            // Then
            assertThat(simpleFile.getFilename()).isEqualTo(filename);
            assertThat(simpleFile.getFilename().length()).isGreaterThan(1000);
        }

        @Test
        @DisplayName("Should handle empty filename")
        void testEmptyFilename() {
            // Given
            String filename = "";
            String contents = "content";

            // When
            SimpleFile simpleFile = SimpleFile.newInstance(filename, contents);

            // Then
            assertThat(simpleFile.getFilename()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Interface Contract Tests")
    class InterfaceContractTests {

        @Test
        @DisplayName("Should implement SimpleFile interface correctly")
        void testInterfaceImplementation() {
            // Given
            String filename = "test.java";
            String contents = "public class Test {}";

            // When
            SimpleFile simpleFile = SimpleFile.newInstance(filename, contents);

            // Then
            assertThat(simpleFile).isInstanceOf(SimpleFile.class);

            // Verify methods are callable and return expected types
            assertThat(simpleFile.getFilename()).isInstanceOf(String.class);
            assertThat(simpleFile.getContents()).isInstanceOf(String.class);
        }

        @Test
        @DisplayName("Should return consistent values across multiple calls")
        void testConsistentValues() {
            // Given
            String filename = "consistent.txt";
            String contents = "consistent content";
            SimpleFile simpleFile = SimpleFile.newInstance(filename, contents);

            // When/Then - Multiple calls should return same values
            for (int i = 0; i < 10; i++) {
                assertThat(simpleFile.getFilename()).isEqualTo(filename);
                assertThat(simpleFile.getContents()).isEqualTo(contents);
            }
        }

        @Test
        @DisplayName("Should handle immutable behavior")
        void testImmutableBehavior() {
            // Given
            String filename = "immutable.txt";
            String contents = "original content";
            SimpleFile simpleFile = SimpleFile.newInstance(filename, contents);

            // When - Get references to the values
            String retrievedFilename = simpleFile.getFilename();
            String retrievedContents = simpleFile.getContents();

            // Then - Values should be the same across calls
            assertThat(simpleFile.getFilename()).isSameAs(retrievedFilename);
            assertThat(simpleFile.getContents()).isSameAs(retrievedContents);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Integration Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle file types with different extensions")
        void testDifferentFileTypes() {
            // Test various file types
            String[][] testCases = {
                    { "document.txt", "text content" },
                    { "source.java", "public class Test {}" },
                    { "config.xml", "<?xml version=\"1.0\"?><root/>" },
                    { "data.json", "{\"key\": \"value\"}" },
                    { "script.sh", "#!/bin/bash\necho hello" },
                    { "style.css", "body { margin: 0; }" },
                    { "page.html", "<html><body>Hello</body></html>" },
                    { "query.sql", "SELECT * FROM users;" },
                    { "binary.dat", "\0\1\2\3" }
            };

            for (String[] testCase : testCases) {
                String filename = testCase[0];
                String contents = testCase[1];

                SimpleFile simpleFile = SimpleFile.newInstance(filename, contents);

                assertThat(simpleFile.getFilename()).isEqualTo(filename);
                assertThat(simpleFile.getContents()).isEqualTo(contents);
            }
        }

        @Test
        @DisplayName("Should handle concurrent access safely")
        void testConcurrentAccess() throws InterruptedException {
            // Given
            String filename = "concurrent.txt";
            String contents = "concurrent content";
            SimpleFile simpleFile = SimpleFile.newInstance(filename, contents);

            // When - Access from multiple threads
            Thread[] threads = new Thread[10];
            boolean[] results = new boolean[10];

            for (int i = 0; i < threads.length; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    try {
                        results[index] = filename.equals(simpleFile.getFilename()) &&
                                contents.equals(simpleFile.getContents());
                    } catch (Exception e) {
                        results[index] = false;
                    }
                });
                threads[i].start();
            }

            // Wait for all threads to complete
            for (Thread thread : threads) {
                thread.join();
            }

            // Then - All threads should get consistent results
            for (boolean result : results) {
                assertThat(result).isTrue();
            }
        }

        @Test
        @DisplayName("Should handle memory efficient large content")
        void testMemoryEfficiency() {
            // Given - Create multiple files with shared content
            String sharedContent = "This content is shared across multiple files";
            SimpleFile[] files = new SimpleFile[100];

            // When
            for (int i = 0; i < files.length; i++) {
                files[i] = SimpleFile.newInstance("file" + i + ".txt", sharedContent);
            }

            // Then - All files should have the correct content
            for (int i = 0; i < files.length; i++) {
                assertThat(files[i].getFilename()).isEqualTo("file" + i + ".txt");
                assertThat(files[i].getContents()).isEqualTo(sharedContent);
            }
        }

        @Test
        @DisplayName("Should work with file-like operations")
        void testFileOperationsSimulation() {
            // Given
            SimpleFile textFile = SimpleFile.newInstance("document.txt", "Hello World");
            SimpleFile emptyFile = SimpleFile.newInstance("empty.txt", "");
            SimpleFile binaryFile = SimpleFile.newInstance("binary.dat", "\0\1\2\3");

            // When/Then - Simulate common file operations

            // Size check
            assertThat(textFile.getContents().length()).isEqualTo(11);
            assertThat(emptyFile.getContents().length()).isZero();
            assertThat(binaryFile.getContents().length()).isEqualTo(4);

            // Extension check
            assertThat(textFile.getFilename()).endsWith(".txt");
            assertThat(binaryFile.getFilename()).endsWith(".dat");

            // Content analysis
            assertThat(textFile.getContents()).contains("World");
            assertThat(emptyFile.getContents()).isEmpty();
            assertThat(binaryFile.getContents()).startsWith("\0");
        }

        @Test
        @DisplayName("Should handle toString behavior gracefully")
        void testToStringBehavior() {
            // Given
            SimpleFile simpleFile = SimpleFile.newInstance("test.txt", "content");

            // When/Then - toString should not throw exceptions
            assertThatCode(() -> simpleFile.toString()).doesNotThrowAnyException();

            String stringRepresentation = simpleFile.toString();
            assertThat(stringRepresentation).isNotNull();
        }
    }
}
