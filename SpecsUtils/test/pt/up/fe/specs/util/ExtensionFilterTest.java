package pt.up.fe.specs.util;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Comprehensive test suite for {@link ExtensionFilter} deprecated file filter
 * utility class.
 * Tests file extension filtering and FilenameFilter integration.
 *
 * Note: ExtensionFilter is deprecated and implements FilenameFilter with simple
 * constructor.
 * 
 * @author Generated Tests
 */
@SuppressWarnings("deprecation")
@DisplayName("ExtensionFilter Utility Tests")
class ExtensionFilterTest {

    private ExtensionFilter txtFilter;
    private ExtensionFilter javaFilter;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        txtFilter = new ExtensionFilter("txt");
        javaFilter = new ExtensionFilter("java");
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("should create filter with single extension")
        void testSingleExtensionConstructor() {
            ExtensionFilter filter = new ExtensionFilter("xml");

            assertThat(filter).isNotNull();
            assertThat(filter).isInstanceOf(FilenameFilter.class);
        }

        @Test
        @DisplayName("should create filter with followSymlinks parameter")
        void testConstructorWithFollowSymlinks() {
            ExtensionFilter filter1 = new ExtensionFilter("txt", true);
            ExtensionFilter filter2 = new ExtensionFilter("txt", false);

            assertThat(filter1).isNotNull();
            assertThat(filter2).isNotNull();
        }

        @Test
        @DisplayName("should handle null extension gracefully")
        void testNullExtension() {
            assertThatCode(() -> {
                ExtensionFilter filter = new ExtensionFilter(null);
                // Should not crash during construction
                assertThat(filter).isNotNull();
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should handle empty extension")
        void testEmptyExtension() {
            ExtensionFilter filter = new ExtensionFilter("");

            assertThat(filter).isNotNull();
        }
    }

    @Nested
    @DisplayName("File Acceptance Tests")
    class FileAcceptanceTests {

        @ParameterizedTest
        @ValueSource(strings = {"test.txt", "document.TXT", "readme.Txt", "file.tXt"})
        @DisplayName("should accept files with matching extension (case insensitive)")
        void testAcceptMatchingExtension(String filename) {
            File dir = tempDir.toFile();

            assertThat(txtFilter.accept(dir, filename)).isTrue();
        }

        @ParameterizedTest
        @ValueSource(strings = {"test.doc", "document.pdf", "readme.md", "file.xml"})
        @DisplayName("should reject files with non-matching extension")
        void testRejectNonMatchingExtension(String filename) {
            File dir = tempDir.toFile();

            assertThat(txtFilter.accept(dir, filename)).isFalse();
        }

        @Test
        @DisplayName("should handle files without extension")
        void testFilesWithoutExtension() {
            File dir = tempDir.toFile();

            assertThat(txtFilter.accept(dir, "README")).isFalse();
            assertThat(javaFilter.accept(dir, "Makefile")).isFalse();
        }

        @Test
        @DisplayName("should handle files with multiple dots")
        void testFilesWithMultipleDots() {
            File dir = tempDir.toFile();
            ExtensionFilter gzFilter = new ExtensionFilter("gz");

            assertThat(gzFilter.accept(dir, "archive.tar.gz")).isTrue();
            assertThat(txtFilter.accept(dir, "archive.tar.gz")).isFalse();
        }

        @Test
        @DisplayName("should handle hidden files")
        void testHiddenFiles() {
            File dir = tempDir.toFile();

            assertThat(txtFilter.accept(dir, ".hidden.txt")).isTrue();
            assertThat(javaFilter.accept(dir, ".gitignore")).isFalse();
        }

        @Test
        @DisplayName("should handle case sensitivity properly")
        void testCaseSensitivity() {
            File dir = tempDir.toFile();

            // Should be case-insensitive based on implementation
            assertThat(txtFilter.accept(dir, "TEST.TXT")).isTrue();
            assertThat(txtFilter.accept(dir, "test.txt")).isTrue();
            assertThat(txtFilter.accept(dir, "Test.Txt")).isTrue();
        }

        @Test
        @DisplayName("should match extension exactly")
        void testExactExtensionMatch() {
            File dir = tempDir.toFile();
            ExtensionFilter htmlFilter = new ExtensionFilter("html");

            assertThat(htmlFilter.accept(dir, "index.html")).isTrue();
            assertThat(htmlFilter.accept(dir, "index.htm")).isFalse();
            assertThat(htmlFilter.accept(dir, "page.xhtml")).isFalse();
        }
    }

    @Nested
    @DisplayName("Symlink Handling Tests")
    class SymlinkHandlingTests {

        @Test
        @DisplayName("should follow symlinks by default")
        void testFollowSymlinksDefault() throws IOException {
            if (!supportsSymlinks()) {
                return; // Skip on Windows or unsupported filesystems
            }

            // Create a regular file
            Path regularFile = tempDir.resolve("regular.txt");
            Files.createFile(regularFile);

            // Create a symlink
            Path symlink = tempDir.resolve("symlink.txt");
            Files.createSymbolicLink(symlink, regularFile);

            ExtensionFilter defaultFilter = new ExtensionFilter("txt"); // follows symlinks by default

            assertThat(defaultFilter.accept(tempDir.toFile(), "regular.txt")).isTrue();
            assertThat(defaultFilter.accept(tempDir.toFile(), "symlink.txt")).isTrue();
        }

        @Test
        @DisplayName("should not follow symlinks when configured")
        void testNotFollowSymlinks() throws IOException {
            if (!supportsSymlinks()) {
                return; // Skip on Windows or unsupported filesystems
            }

            // Create a regular file
            Path regularFile = tempDir.resolve("regular.txt");
            Files.createFile(regularFile);

            // Create a symlink
            Path symlink = tempDir.resolve("symlink.txt");
            Files.createSymbolicLink(symlink, regularFile);

            ExtensionFilter noFollowFilter = new ExtensionFilter("txt", false);

            assertThat(noFollowFilter.accept(tempDir.toFile(), "regular.txt")).isTrue();
            assertThat(noFollowFilter.accept(tempDir.toFile(), "symlink.txt")).isFalse();
        }

        @Test
        @DisplayName("should handle broken symlinks gracefully")
        void testBrokenSymlinks() throws IOException {
            if (!supportsSymlinks()) {
                return; // Skip on Windows or unsupported filesystems
            }

            // Create a symlink to non-existent file
            Path brokenSymlink = tempDir.resolve("broken.txt");
            Path nonExistent = tempDir.resolve("does_not_exist.txt");
            Files.createSymbolicLink(brokenSymlink, nonExistent);

            ExtensionFilter followFilter = new ExtensionFilter("txt", true);
            ExtensionFilter noFollowFilter = new ExtensionFilter("txt", false);

            // Both should handle broken symlinks gracefully
            assertThatCode(() -> {
                followFilter.accept(tempDir.toFile(), "broken.txt");
                noFollowFilter.accept(tempDir.toFile(), "broken.txt");
            }).doesNotThrowAnyException();
        }

        private boolean supportsSymlinks() {
            return !System.getProperty("os.name").toLowerCase().startsWith("windows");
        }
    }

    @Nested
    @DisplayName("FilenameFilter Integration Tests")
    class FilenameFilterIntegrationTests {

        @Test
        @DisplayName("should implement FilenameFilter interface correctly")
        void testFilenameFilterInterface() {
            assertThat(txtFilter).isInstanceOf(FilenameFilter.class);

            // Test through FilenameFilter interface
            FilenameFilter filter = txtFilter;
            File dir = tempDir.toFile();

            assertThat(filter.accept(dir, "test.txt")).isTrue();
            assertThat(filter.accept(dir, "test.doc")).isFalse();
        }

        @Test
        @DisplayName("should work with File.listFiles(FilenameFilter)")
        void testFileListFilesIntegration() throws IOException {
            // Create test files in temp directory
            Files.createFile(tempDir.resolve("file1.txt"));
            Files.createFile(tempDir.resolve("file2.java"));
            Files.createFile(tempDir.resolve("file3.txt"));
            Files.createFile(tempDir.resolve("readme.md"));

            File dir = tempDir.toFile();

            // Use txtFilter to filter .txt files
            File[] txtFiles = dir.listFiles(txtFilter);
            assertThat(txtFiles).hasSize(2);
            assertThat(txtFiles).extracting(File::getName)
                    .containsExactlyInAnyOrder("file1.txt", "file3.txt");

            // Use javaFilter to filter .java files
            File[] javaFiles = dir.listFiles(javaFilter);
            assertThat(javaFiles).hasSize(1);
            assertThat(javaFiles[0].getName()).isEqualTo("file2.java");
        }

        @Test
        @DisplayName("should handle null directory parameter")
        void testNullDirectoryParameter() {
            assertThatCode(() -> {
                boolean result = txtFilter.accept(null, "test.txt");
                // Should handle null directory gracefully
                assertThat(result).isEqualTo(true); // Extension matching should still work
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should throw NPE for null filename parameter")
        void testNullFilenameParameter() {
            File dir = tempDir.toFile();

            assertThatThrownBy(() -> txtFilter.accept(dir, null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Extension Matching Tests")
    class ExtensionMatchingTests {

        @Test
        @DisplayName("should use dot separator correctly")
        void testDotSeparator() {
            File dir = tempDir.toFile();

            // Should require dot separator
            assertThat(txtFilter.accept(dir, "filetxt")).isFalse();
            assertThat(txtFilter.accept(dir, "file.txt")).isTrue();
        }

        @Test
        @DisplayName("should handle extensions with special characters")
        void testSpecialCharacterExtensions() {
            ExtensionFilter specialFilter = new ExtensionFilter("c++");
            File dir = tempDir.toFile();

            assertThat(specialFilter.accept(dir, "program.c++")).isTrue();
            assertThat(specialFilter.accept(dir, "program.cpp")).isFalse();
        }

        @Test
        @DisplayName("should handle numeric extensions")
        void testNumericExtensions() {
            ExtensionFilter numericFilter = new ExtensionFilter("v2");
            File dir = tempDir.toFile();

            assertThat(numericFilter.accept(dir, "backup.v2")).isTrue();
            assertThat(numericFilter.accept(dir, "backup.v1")).isFalse();
        }

        @Test
        @DisplayName("should handle empty string extension")
        void testEmptyStringExtension() {
            ExtensionFilter emptyFilter = new ExtensionFilter("");
            File dir = tempDir.toFile();

            // Files ending with just a dot should match
            assertThat(emptyFilter.accept(dir, "file.")).isTrue();
            assertThat(emptyFilter.accept(dir, "file")).isFalse();
        }
    }

    @Nested
    @DisplayName("Edge Cases & Error Handling Tests")
    class EdgeCasesErrorHandlingTests {

        @Test
        @DisplayName("should handle very long filenames")
        void testVeryLongFilenames() {
            File dir = tempDir.toFile();
            String longName = "a".repeat(200) + ".txt";

            assertThat(txtFilter.accept(dir, longName)).isTrue();
        }

        @Test
        @DisplayName("should handle files with unusual characters")
        void testFilesWithUnusualCharacters() {
            File dir = tempDir.toFile();

            assertThat(txtFilter.accept(dir, "测试文件.txt")).isTrue();
            assertThat(txtFilter.accept(dir, "file name with spaces.txt")).isTrue();
            assertThat(txtFilter.accept(dir, "file-with_special@chars.txt")).isTrue();
        }

        @Test
        @DisplayName("should handle files ending with dots")
        void testFilesEndingWithDots() {
            File dir = tempDir.toFile();

            assertThatCode(() -> {
                txtFilter.accept(dir, "file.");
                txtFilter.accept(dir, "file...");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should handle files starting with extension")
        void testFilesStartingWithExtension() {
            File dir = tempDir.toFile();

            assertThat(txtFilter.accept(dir, "txt.file")).isFalse();
            assertThat(txtFilter.accept(dir, "txt.txt")).isTrue();
        }

        @Test
        @DisplayName("should handle case variations consistently")
        void testCaseVariations() {
            File dir = tempDir.toFile();
            ExtensionFilter upperFilter = new ExtensionFilter("TXT");
            ExtensionFilter lowerFilter = new ExtensionFilter("txt");

            // Both should handle case-insensitively based on implementation
            assertThat(upperFilter.accept(dir, "file.txt")).isTrue();
            assertThat(upperFilter.accept(dir, "file.TXT")).isTrue();
            assertThat(lowerFilter.accept(dir, "file.txt")).isTrue();
            assertThat(lowerFilter.accept(dir, "file.TXT")).isTrue();
        }

        @Test
        @DisplayName("class should be properly deprecated")
        void testDeprecatedAnnotation() {
            // Verify that the class is marked as deprecated
            assertThat(ExtensionFilter.class.isAnnotationPresent(Deprecated.class)).isTrue();
        }

        @Test
        @DisplayName("should be package-private inner class")
        void testClassVisibility() {
            // ExtensionFilter should be package-private (not public)
            int modifiers = ExtensionFilter.class.getModifiers();
            assertThat(java.lang.reflect.Modifier.isPublic(modifiers)).isFalse();
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @Test
        @DisplayName("should handle large numbers of filenames efficiently")
        void testLargeFilenameSet() {
            File dir = tempDir.toFile();
            String[] manyFilenames = new String[1000];
            for (int i = 0; i < manyFilenames.length; i++) {
                manyFilenames[i] = "file" + i + (i % 2 == 0 ? ".txt" : ".doc");
            }

            long start = System.currentTimeMillis();
            long txtCount = 0;
            for (String filename : manyFilenames) {
                if (txtFilter.accept(dir, filename)) {
                    txtCount++;
                }
            }
            long duration = System.currentTimeMillis() - start;

            assertThat(txtCount).isEqualTo(500); // Half should be .txt
            assertThat(duration).isLessThan(1000); // Should be fast
        }

        @Test
        @DisplayName("should reuse instances efficiently")
        void testInstanceReuse() {
            File dir = tempDir.toFile();

            // Create multiple instances with same parameters
            ExtensionFilter filter1 = new ExtensionFilter("txt");
            ExtensionFilter filter2 = new ExtensionFilter("txt");

            String filename = "test.txt";

            assertThat(filter1.accept(dir, filename)).isEqualTo(filter2.accept(dir, filename));
        }
    }
}
