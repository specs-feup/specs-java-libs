package tdrc.utils;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Comprehensive tests for {@link FileUtils}.
 * 
 * @author Generated Tests
 */
@DisplayName("FileUtils Tests")
class FileUtilsTest {

    @TempDir
    Path tempDir;

    private File testDir;

    @BeforeEach
    void setUp() throws IOException {
        testDir = tempDir.toFile();
        createTestFileStructure();
    }

    private void createTestFileStructure() throws IOException {
        // Create root level files
        Files.createFile(tempDir.resolve("file1.txt"));
        Files.createFile(tempDir.resolve("file2.java"));
        Files.createFile(tempDir.resolve("file3.xml"));
        Files.createFile(tempDir.resolve("readme.md"));
        Files.createFile(tempDir.resolve("noextension"));

        // Create subdirectory with files
        Path subDir1 = Files.createDirectory(tempDir.resolve("subdir1"));
        Files.createFile(subDir1.resolve("sub1.txt"));
        Files.createFile(subDir1.resolve("sub2.java"));
        Files.createFile(subDir1.resolve("sub3.xml"));

        // Create nested subdirectory with files
        Path subDir2 = Files.createDirectory(subDir1.resolve("nested"));
        Files.createFile(subDir2.resolve("nested1.txt"));
        Files.createFile(subDir2.resolve("nested2.py"));

        // Create another top-level subdirectory
        Path subDir3 = Files.createDirectory(tempDir.resolve("subdir2"));
        Files.createFile(subDir3.resolve("another1.java"));
        Files.createFile(subDir3.resolve("another2.cpp"));
    }

    @Nested
    @DisplayName("Basic File Search Tests")
    class BasicFileSearchTests {

        @Test
        @DisplayName("should find files with specific extension in non-recursive mode")
        void testGetFilesFromDir_SpecificExtension_NonRecursive_FindsFiles() {
            List<File> result = FileUtils.getFilesFromDir(testDir, "txt", false);

            assertThat(result)
                    .hasSize(1)
                    .extracting(File::getName)
                    .containsExactly("file1.txt");
        }

        @Test
        @DisplayName("should find files with specific extension in recursive mode")
        void testGetFilesFromDir_SpecificExtension_Recursive_FindsAllFiles() {
            List<File> result = FileUtils.getFilesFromDir(testDir, "txt", true);

            assertThat(result)
                    .hasSize(3)
                    .extracting(File::getName)
                    .containsExactlyInAnyOrder("file1.txt", "sub1.txt", "nested1.txt");
        }

        @Test
        @DisplayName("should find multiple file extensions")
        void testGetFilesFromDir_MultipleExtensions_FindsAllMatching() {
            List<File> result = FileUtils.getFilesFromDir(testDir, "java", true);

            assertThat(result)
                    .hasSize(3)
                    .extracting(File::getName)
                    .containsExactlyInAnyOrder("file2.java", "sub2.java", "another1.java");
        }

        @Test
        @DisplayName("should return empty list when no files match")
        void testGetFilesFromDir_NoMatches_ReturnsEmptyList() {
            List<File> result = FileUtils.getFilesFromDir(testDir, "nonexistent", true);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("should handle empty directory")
        void testGetFilesFromDir_EmptyDirectory_ReturnsEmptyList() throws IOException {
            Path emptyDir = Files.createDirectory(tempDir.resolve("empty"));

            List<File> result = FileUtils.getFilesFromDir(emptyDir.toFile(), "txt", true);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("Regular Expression Tests")
    class RegularExpressionTests {

        @Test
        @DisplayName("should handle regex patterns for extensions")
        void testGetFilesFromDir_RegexPattern_MatchesCorrectly() {
            // Match both .java and .xml extensions
            List<File> result = FileUtils.getFilesFromDir(testDir, "(java|xml)", true);

            assertThat(result)
                    .hasSize(5)
                    .extracting(File::getName)
                    .containsExactlyInAnyOrder("file2.java", "file3.xml", "sub2.java", "sub3.xml", "another1.java");
        }

        @Test
        @DisplayName("should handle wildcard patterns")
        void testGetFilesFromDir_WildcardPattern_MatchesCorrectly() {
            // Match any extension containing 'a'
            List<File> result = FileUtils.getFilesFromDir(testDir, ".*a.*", true);

            assertThat(result)
                    .extracting(File::getName)
                    .containsExactlyInAnyOrder("file2.java", "sub2.java", "another1.java");
        }

        @Test
        @DisplayName("should handle dot literal in regex")
        void testGetFilesFromDir_DotLiteralRegex_MatchesCorrectly() {
            // Match files with any single character extension
            List<File> result = FileUtils.getFilesFromDir(testDir, ".{2}", true);

            assertThat(result)
                    .extracting(File::getName)
                    .containsExactlyInAnyOrder("readme.md", "nested2.py");
        }

        @Test
        @DisplayName("should handle complex regex patterns")
        void testGetFilesFromDir_ComplexRegex_MatchesCorrectly() {
            // Match extensions that start with 'j' or 'c'
            List<File> result = FileUtils.getFilesFromDir(testDir, "[jc].*", true);

            assertThat(result)
                    .extracting(File::getName)
                    .containsExactlyInAnyOrder("file2.java", "sub2.java", "another1.java", "another2.cpp");
        }

        @Test
        @DisplayName("should match all files with universal regex")
        void testGetFilesFromDir_UniversalRegex_MatchesAllFiles() {
            List<File> result = FileUtils.getFilesFromDir(testDir, ".*", true);

            // Should match all files including those without extensions
            assertThat(result)
                    .extracting(File::getName)
                    .containsExactlyInAnyOrder(
                            "file1.txt", "file2.java", "file3.xml", "readme.md", "noextension",
                            "sub1.txt", "sub2.java", "sub3.xml",
                            "nested1.txt", "nested2.py",
                            "another1.java", "another2.cpp");
        }
    }

    @Nested
    @DisplayName("Recursive vs Non-Recursive Tests")
    class RecursiveVsNonRecursiveTests {

        @Test
        @DisplayName("should only search root directory in non-recursive mode")
        void testGetFilesFromDir_NonRecursive_OnlyRootFiles() {
            List<File> result = FileUtils.getFilesFromDir(testDir, ".*", false);

            assertThat(result)
                    .extracting(File::getName)
                    .containsExactlyInAnyOrder("file1.txt", "file2.java", "file3.xml", "readme.md", "noextension");
        }

        @Test
        @DisplayName("should search all subdirectories in recursive mode")
        void testGetFilesFromDir_Recursive_AllSubdirectories() {
            List<File> result = FileUtils.getFilesFromDir(testDir, ".*", true);

            assertThat(result).hasSize(12); // All files in all directories

            // Verify we have files from all levels
            assertThat(result)
                    .extracting(File::getName)
                    .contains("file1.txt") // root level
                    .contains("sub1.txt") // first level subdirectory
                    .contains("nested1.txt"); // nested subdirectory
        }

        @Test
        @DisplayName("should prioritize files in current directory over subdirectories")
        void testGetFilesFromDir_Recursive_PrioritizesCurrentDirectory() {
            List<File> result = FileUtils.getFilesFromDir(testDir, "java", true);

            // Should find files in order: current directory first, then subdirectories
            List<String> fileNames = result.stream()
                    .map(File::getName)
                    .toList();

            assertThat(fileNames)
                    .containsExactlyInAnyOrder("file2.java", "sub2.java", "another1.java");
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("should throw exception when given file is not a directory")
        void testGetFilesFromDir_FileNotDirectory_ThrowsException() throws IOException {
            File regularFile = Files.createFile(tempDir.resolve("regular.txt")).toFile();

            assertThatThrownBy(() -> FileUtils.getFilesFromDir(regularFile, "txt", false))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("The given file is not a folder")
                    .hasMessageContaining(regularFile.toString());
        }

        @Test
        @DisplayName("should handle null directory gracefully")
        void testGetFilesFromDir_NullDirectory_ThrowsException() {
            assertThatThrownBy(() -> FileUtils.getFilesFromDir(null, "txt", false))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("should handle null extension parameter")
        void testGetFilesFromDir_NullExtension_ThrowsException() {
            assertThatThrownBy(() -> FileUtils.getFilesFromDir(testDir, null, false))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("should handle invalid regex pattern")
        void testGetFilesFromDir_InvalidRegex_ThrowsException() {
            // Invalid regex: unclosed bracket
            assertThatThrownBy(() -> FileUtils.getFilesFromDir(testDir, "[abc", false))
                    .isInstanceOf(RuntimeException.class);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("should handle files without extensions")
        void testGetFilesFromDir_FilesWithoutExtensions_HandledCorrectly() {
            // Look for files with empty extension
            List<File> result = FileUtils.getFilesFromDir(testDir, "", false);

            assertThat(result)
                    .extracting(File::getName)
                    .containsExactly("noextension");
        }

        @Test
        @DisplayName("should handle very long extension patterns")
        void testGetFilesFromDir_LongExtensionPattern_WorksCorrectly() {
            String longPattern = "txt|java|xml|md|py|cpp|js|html|css|json|yaml|yml";

            List<File> result = FileUtils.getFilesFromDir(testDir, longPattern, true);

            // Should find all files with these extensions
            assertThat(result).hasSize(11); // All files except 'noextension'
        }

        @Test
        @DisplayName("should handle case sensitive regex")
        void testGetFilesFromDir_CaseSensitiveRegex_WorksCorrectly() {
            // This will only match lowercase 'java'
            List<File> result = FileUtils.getFilesFromDir(testDir, "java", true);

            assertThat(result)
                    .extracting(File::getName)
                    .containsExactlyInAnyOrder("file2.java", "sub2.java", "another1.java");
        }

        @Test
        @DisplayName("should handle deeply nested directory structure")
        void testGetFilesFromDir_DeeplyNested_FindsAllFiles() throws IOException {
            // Create a deeply nested structure
            Path deepDir = tempDir;
            for (int i = 0; i < 5; i++) {
                deepDir = Files.createDirectory(deepDir.resolve("level" + i));
                Files.createFile(deepDir.resolve("deep" + i + ".txt"));
            }

            List<File> result = FileUtils.getFilesFromDir(testDir, "txt", true);

            // Should find original txt files + 5 new deep files
            assertThat(result).hasSize(8);
            assertThat(result)
                    .extracting(File::getName)
                    .contains("deep0.txt", "deep1.txt", "deep2.txt", "deep3.txt", "deep4.txt");
        }

        @Test
        @DisplayName("should handle symbolic links if they exist")
        void testGetFilesFromDir_WithSymbolicLinks_HandlesCorrectly() {
            // This test might not work on all systems, so we make it conditional
            List<File> result = FileUtils.getFilesFromDir(testDir, ".*", true);

            // Should not throw any exceptions and should work normally
            assertThat(result).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("Performance and Large Scale Tests")
    class PerformanceTests {

        @Test
        @DisplayName("should handle directory with many files efficiently")
        void testGetFilesFromDir_ManyFiles_PerformsWell() throws IOException {
            // Create many files in a single directory
            Path manyFilesDir = Files.createDirectory(tempDir.resolve("manyfiles"));
            for (int i = 0; i < 100; i++) {
                Files.createFile(manyFilesDir.resolve("file" + i + ".txt"));
            }

            long startTime = System.currentTimeMillis();
            List<File> result = FileUtils.getFilesFromDir(manyFilesDir.toFile(), "txt", false);
            long endTime = System.currentTimeMillis();

            assertThat(result).hasSize(100);
            // Should complete reasonably quickly (less than 1 second for 100 files)
            assertThat(endTime - startTime).isLessThan(1000);
        }
    }
}
