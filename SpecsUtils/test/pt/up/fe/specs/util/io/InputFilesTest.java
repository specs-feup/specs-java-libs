package pt.up.fe.specs.util.io;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assumptions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Comprehensive test suite for InputFiles class.
 * 
 * Tests input file management functionality including single file processing,
 * folder processing, error handling, and edge cases for file path operations.
 * 
 * @author Generated Tests
 */
@DisplayName("InputFiles Tests")
class InputFilesTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create InputFiles with valid parameters")
        void testConstructor() {
            // Given
            boolean isSingleFile = true;
            File inputPath = new File("test.txt");
            List<File> inputFilesList = List.of(new File("file1.txt"), new File("file2.txt"));

            // When
            InputFiles result = new InputFiles(isSingleFile, inputPath, inputFilesList);

            // Then
            assertThat(result.isSingleFile).isTrue();
            assertThat(result.inputPath).isEqualTo(inputPath);
            assertThat(result.inputFiles).hasSize(2);
            assertThat(result.inputFiles).containsExactly(new File("file1.txt"), new File("file2.txt"));
        }

        @Test
        @DisplayName("Should create InputFiles for folder mode")
        void testConstructorFolderMode() {
            // Given
            boolean isSingleFile = false;
            File inputPath = new File("/some/folder");
            List<File> inputFilesList = List.of(
                    new File("/some/folder/file1.txt"),
                    new File("/some/folder/subdir/file2.txt"));

            // When
            InputFiles result = new InputFiles(isSingleFile, inputPath, inputFilesList);

            // Then
            assertThat(result.isSingleFile).isFalse();
            assertThat(result.inputPath).isEqualTo(inputPath);
            assertThat(result.inputFiles).hasSize(2);
        }
    }

    @Nested
    @DisplayName("newInstance Tests")
    class NewInstanceTests {

        @Test
        @DisplayName("Should create InputFiles for single file")
        void testNewInstanceSingleFile(@TempDir Path tempDir) throws IOException {
            // Given
            Path testFile = tempDir.resolve("test.txt");
            Files.write(testFile, "test content".getBytes());

            // When
            InputFiles result = InputFiles.newInstance(testFile.toString());

            // Then
            assertThat(result).isNotNull();
            assertThat(result.isSingleFile).isTrue();
            assertThat(result.inputPath).isEqualTo(testFile.toFile());
            assertThat(result.inputFiles).hasSize(1);
            assertThat(result.inputFiles.get(0)).isEqualTo(testFile.toFile());
        }

        @Test
        @DisplayName("Should create InputFiles for folder with files")
        void testNewInstanceFolder(@TempDir Path tempDir) throws IOException {
            // Given
            Path file1 = tempDir.resolve("file1.txt");
            Path file2 = tempDir.resolve("file2.txt");
            Path subDir = tempDir.resolve("subdir");
            Files.createDirectory(subDir);
            Path file3 = subDir.resolve("file3.txt");

            Files.write(file1, "content1".getBytes());
            Files.write(file2, "content2".getBytes());
            Files.write(file3, "content3".getBytes());

            // When
            InputFiles result = InputFiles.newInstance(tempDir.toString());

            // Then
            assertThat(result).isNotNull();
            assertThat(result.isSingleFile).isFalse();
            assertThat(result.inputPath).isEqualTo(tempDir.toFile());
            assertThat(result.inputFiles).hasSize(3); // Recursive file collection
            assertThat(result.inputFiles).extracting(File::getName)
                    .containsExactlyInAnyOrder("file1.txt", "file2.txt", "file3.txt");
        }

        @Test
        @DisplayName("Should create InputFiles for empty folder")
        void testNewInstanceEmptyFolder(@TempDir Path tempDir) {
            // When
            InputFiles result = InputFiles.newInstance(tempDir.toString());

            // Then
            assertThat(result).isNotNull();
            assertThat(result.isSingleFile).isFalse();
            assertThat(result.inputPath).isEqualTo(tempDir.toFile());
            assertThat(result.inputFiles).isEmpty();
        }

        @Test
        @DisplayName("Should return null for non-existent path")
        void testNewInstanceNonExistentPath() {
            // Given
            String nonExistentPath = "/non/existent/path";

            // When
            InputFiles result = InputFiles.newInstance(nonExistentPath);

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should handle symbolic links correctly")
        void testNewInstanceWithSymbolicLink(@TempDir Path tempDir) throws IOException {
            // Given
            Path originalFile = tempDir.resolve("original.txt");
            Files.write(originalFile, "original content".getBytes());

            Path linkFile = tempDir.resolve("link.txt");
            try {
                Files.createSymbolicLink(linkFile, originalFile);

                // When
                InputFiles result = InputFiles.newInstance(linkFile.toString());

                // Then
                assertThat(result).isNotNull();
                assertThat(result.isSingleFile).isTrue();
                assertThat(result.inputFiles).hasSize(1);
            } catch (UnsupportedOperationException e) {
                // Skip test if symbolic links are not supported on this system
                assumeThat(false).as("Symbolic links not supported on this system").isTrue();
            }
        }
    }

    @Nested
    @DisplayName("getFiles Private Method Tests")
    class GetFilesTests {

        @Test
        @DisplayName("Should handle non-existent file scenarios")
        void testGetFilesNonExistentFile() {
            // Given
            String nonExistentFile = "/non/existent/file.txt";

            // When
            InputFiles result = InputFiles.newInstance(nonExistentFile);

            // Then
            assertThat(result).isNull(); // newInstance returns null for non-existent paths
        }

        @Test
        @DisplayName("Should handle existing folder in folder mode")
        void testGetFilesFolderMode(@TempDir Path tempDir) throws IOException {
            // Given
            Path subFolder = tempDir.resolve("existingfolder");
            Files.createDirectory(subFolder); // Create the folder first
            String folderPath = subFolder.toString();

            // When
            InputFiles result = InputFiles.newInstance(folderPath);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.isSingleFile).isFalse();
            assertThat(Files.exists(subFolder)).isTrue();
            assertThat(Files.isDirectory(subFolder)).isTrue();
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should handle complex folder structure")
        void testComplexFolderStructure(@TempDir Path tempDir) throws IOException {
            // Given - Create complex folder structure
            Path dir1 = tempDir.resolve("dir1");
            Path dir2 = tempDir.resolve("dir2");
            Path dir1_sub = dir1.resolve("subdir");

            Files.createDirectories(dir1_sub);
            Files.createDirectory(dir2);

            Path file1 = tempDir.resolve("root.txt");
            Path file2 = dir1.resolve("dir1_file.txt");
            Path file3 = dir1_sub.resolve("sub_file.txt");
            Path file4 = dir2.resolve("dir2_file.txt");

            Files.write(file1, "root content".getBytes());
            Files.write(file2, "dir1 content".getBytes());
            Files.write(file3, "sub content".getBytes());
            Files.write(file4, "dir2 content".getBytes());

            // When
            InputFiles result = InputFiles.newInstance(tempDir.toString());

            // Then
            assertThat(result).isNotNull();
            assertThat(result.isSingleFile).isFalse();
            assertThat(result.inputFiles).hasSize(4);
            assertThat(result.inputFiles).extracting(File::getName)
                    .containsExactlyInAnyOrder("root.txt", "dir1_file.txt", "sub_file.txt", "dir2_file.txt");
        }

        @Test
        @DisplayName("Should handle files with different extensions")
        void testDifferentFileExtensions(@TempDir Path tempDir) throws IOException {
            // Given
            Path txtFile = tempDir.resolve("document.txt");
            Path javaFile = tempDir.resolve("Source.java");
            Path xmlFile = tempDir.resolve("config.xml");
            Path noExtFile = tempDir.resolve("README");

            Files.write(txtFile, "text".getBytes());
            Files.write(javaFile, "java code".getBytes());
            Files.write(xmlFile, "<xml/>".getBytes());
            Files.write(noExtFile, "readme".getBytes());

            // When
            InputFiles result = InputFiles.newInstance(tempDir.toString());

            // Then
            assertThat(result).isNotNull();
            assertThat(result.inputFiles).hasSize(4);
            assertThat(result.inputFiles).extracting(File::getName)
                    .containsExactlyInAnyOrder("document.txt", "Source.java", "config.xml", "README");
        }

        @Test
        @DisplayName("Should handle large number of files")
        void testLargeNumberOfFiles(@TempDir Path tempDir) throws IOException {
            // Given
            int fileCount = 100;
            for (int i = 0; i < fileCount; i++) {
                Path file = tempDir.resolve("file_" + i + ".txt");
                Files.write(file, ("content " + i).getBytes());
            }

            // When
            InputFiles result = InputFiles.newInstance(tempDir.toString());

            // Then
            assertThat(result).isNotNull();
            assertThat(result.inputFiles).hasSize(fileCount);
            assertThat(result.inputFiles).allMatch(file -> file.getName().startsWith("file_"));
            assertThat(result.inputFiles).allMatch(file -> file.getName().endsWith(".txt"));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle empty file")
        void testEmptyFile(@TempDir Path tempDir) throws IOException {
            // Given
            Path emptyFile = tempDir.resolve("empty.txt");
            Files.createFile(emptyFile);

            // When
            InputFiles result = InputFiles.newInstance(emptyFile.toString());

            // Then
            assertThat(result).isNotNull();
            assertThat(result.isSingleFile).isTrue();
            assertThat(result.inputFiles).hasSize(1);
            assertThat(result.inputFiles.get(0)).isEqualTo(emptyFile.toFile());
        }

        @Test
        @DisplayName("Should handle file with special characters in name")
        void testFileWithSpecialCharacters(@TempDir Path tempDir) throws IOException {
            // Given
            Path specialFile = tempDir.resolve("file with spaces & symbols!@#.txt");
            Files.write(specialFile, "special content".getBytes());

            // When
            InputFiles result = InputFiles.newInstance(specialFile.toString());

            // Then
            assertThat(result).isNotNull();
            assertThat(result.isSingleFile).isTrue();
            assertThat(result.inputFiles).hasSize(1);
            assertThat(result.inputFiles.get(0).getName()).isEqualTo("file with spaces & symbols!@#.txt");
        }

        @Test
        @DisplayName("Should handle very long file paths")
        void testLongFilePath(@TempDir Path tempDir) throws IOException {
            // Given
            StringBuilder longName = new StringBuilder();
            for (int i = 0; i < 50; i++) {
                longName.append("very_long_name_");
            }
            longName.append(".txt");

            Path longFile = tempDir.resolve(longName.toString());
            try {
                Files.write(longFile, "content".getBytes());

                // When
                InputFiles result = InputFiles.newInstance(longFile.toString());

                // Then
                assertThat(result).isNotNull();
                assertThat(result.isSingleFile).isTrue();
                assertThat(result.inputFiles).hasSize(1);
            } catch (IOException e) {
                // Skip test if file system doesn't support very long names
                assumeThat(false).as("File system doesn't support very long file names").isTrue();
            }
        }

        @Test
        @DisplayName("Should handle folder with only subdirectories")
        void testFolderWithOnlySubdirectories(@TempDir Path tempDir) throws IOException {
            // Given
            Path subDir1 = tempDir.resolve("subdir1");
            Path subDir2 = tempDir.resolve("subdir2");
            Files.createDirectory(subDir1);
            Files.createDirectory(subDir2);

            // When
            InputFiles result = InputFiles.newInstance(tempDir.toString());

            // Then
            assertThat(result).isNotNull();
            assertThat(result.isSingleFile).isFalse();
            assertThat(result.inputFiles).isEmpty(); // No files, only directories
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle null input path gracefully")
        void testNullInputPath() {
            // When/Then
            assertThatThrownBy(() -> InputFiles.newInstance(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle empty string input path")
        void testEmptyStringInputPath() {
            // When
            InputFiles result = InputFiles.newInstance("");

            // Then
            assertThat(result).isNull(); // Empty string doesn't exist as a path
        }

        @Test
        @DisplayName("Should handle permission denied scenarios")
        void testPermissionDenied(@TempDir Path tempDir) throws IOException {
            // Given
            Path restrictedFile = tempDir.resolve("restricted.txt");
            Files.write(restrictedFile, "content".getBytes());

            // Try to make file unreadable (may not work on all systems)
            boolean madeUnreadable = restrictedFile.toFile().setReadable(false);

            if (madeUnreadable) {
                try {
                    // When
                    InputFiles result = InputFiles.newInstance(restrictedFile.toString());

                    // Then - behavior may vary by system
                    // On some systems, the file might still be readable by owner
                    if (result != null) {
                        assertThat(result.isSingleFile).isTrue();
                    }
                } finally {
                    // Restore permissions for cleanup
                    restrictedFile.toFile().setReadable(true);
                }
            }
        }
    }
}
