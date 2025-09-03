package pt.up.fe.specs.util.io;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import pt.up.fe.specs.util.enums.EnumHelper;

/**
 * Comprehensive test suite for PathFilter enum.
 * 
 * Tests the PathFilter enum functionality including file filtering
 * capabilities, directory filtering, enum helper integration, and path
 * validation logic.
 * 
 * @author Generated Tests
 */
@DisplayName("PathFilter Tests")
class PathFilterTest {

    @Nested
    @DisplayName("Enum Constants Tests")
    class EnumConstantsTests {

        @Test
        @DisplayName("Should have correct enum constants")
        void testEnumConstants() {
            // When/Then
            assertThat(PathFilter.values()).hasSize(3);
            assertThat(PathFilter.values()).containsExactly(
                    PathFilter.FILES,
                    PathFilter.FOLDERS,
                    PathFilter.FILES_AND_FOLDERS);
        }

        @Test
        @DisplayName("Should have correct enum ordering")
        void testEnumOrdering() {
            // When
            PathFilter[] values = PathFilter.values();

            // Then
            assertThat(values[0]).isEqualTo(PathFilter.FILES);
            assertThat(values[1]).isEqualTo(PathFilter.FOLDERS);
            assertThat(values[2]).isEqualTo(PathFilter.FILES_AND_FOLDERS);
        }

        @Test
        @DisplayName("Should support valueOf method")
        void testValueOf() {
            // When/Then
            assertThat(PathFilter.valueOf("FILES")).isEqualTo(PathFilter.FILES);
            assertThat(PathFilter.valueOf("FOLDERS")).isEqualTo(PathFilter.FOLDERS);
            assertThat(PathFilter.valueOf("FILES_AND_FOLDERS")).isEqualTo(PathFilter.FILES_AND_FOLDERS);
        }

        @Test
        @DisplayName("Should throw exception for invalid valueOf")
        void testInvalidValueOf() {
            // When/Then
            assertThatThrownBy(() -> PathFilter.valueOf("INVALID"))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("EnumHelper Integration Tests")
    class EnumHelperTests {

        @Test
        @DisplayName("Should provide EnumHelper instance")
        void testGetHelper() {
            // When
            EnumHelper<PathFilter> helper = PathFilter.getHelper();

            // Then
            assertThat(helper).isNotNull();
            assertThat(helper).isInstanceOf(EnumHelper.class);
        }

        @Test
        @DisplayName("Should return same EnumHelper instance on multiple calls")
        void testGetHelperConsistency() {
            // When
            EnumHelper<PathFilter> helper1 = PathFilter.getHelper();
            EnumHelper<PathFilter> helper2 = PathFilter.getHelper();

            // Then
            assertThat(helper1).isSameAs(helper2);
        }

        @Test
        @DisplayName("Should integrate properly with EnumHelper functionality")
        void testEnumHelperIntegration() {
            // When
            EnumHelper<PathFilter> helper = PathFilter.getHelper();

            // Then
            assertThat(helper.getEnumClass()).isEqualTo(PathFilter.class);
            assertThat(helper.getSize()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("isAllowedTry Method Tests")
    class IsAllowedTryTests {

        @Test
        @DisplayName("FILES filter should allow files only")
        void testFilesFilterAllowsFiles(@TempDir Path tempDir) throws IOException {
            // Given
            Path testFile = tempDir.resolve("test.txt");
            Files.createFile(testFile);
            File file = testFile.toFile();
            File directory = tempDir.toFile();

            // When
            Optional<Boolean> fileResult = PathFilter.FILES.isAllowedTry(file);
            Optional<Boolean> dirResult = PathFilter.FILES.isAllowedTry(directory);

            // Then
            assertThat(fileResult).isPresent();
            assertThat(fileResult.get()).isTrue();
            assertThat(dirResult).isPresent();
            assertThat(dirResult.get()).isFalse();
        }

        @Test
        @DisplayName("FOLDERS filter should allow directories only")
        void testFoldersFilterAllowsFolders(@TempDir Path tempDir) throws IOException {
            // Given
            Path testFile = tempDir.resolve("test.txt");
            Files.createFile(testFile);
            File file = testFile.toFile();
            File directory = tempDir.toFile();

            // When
            Optional<Boolean> fileResult = PathFilter.FOLDERS.isAllowedTry(file);
            Optional<Boolean> dirResult = PathFilter.FOLDERS.isAllowedTry(directory);

            // Then
            assertThat(fileResult).isPresent();
            assertThat(fileResult.get()).isFalse();
            assertThat(dirResult).isPresent();
            assertThat(dirResult.get()).isTrue();
        }

        @Test
        @DisplayName("FILES_AND_FOLDERS filter should allow both")
        void testFilesAndFoldersFilterAllowsBoth(@TempDir Path tempDir) throws IOException {
            // Given
            Path testFile = tempDir.resolve("test.txt");
            Files.createFile(testFile);
            File file = testFile.toFile();
            File directory = tempDir.toFile();

            // When
            Optional<Boolean> fileResult = PathFilter.FILES_AND_FOLDERS.isAllowedTry(file);
            Optional<Boolean> dirResult = PathFilter.FILES_AND_FOLDERS.isAllowedTry(directory);

            // Then
            assertThat(fileResult).isPresent();
            assertThat(fileResult.get()).isTrue();
            assertThat(dirResult).isPresent();
            assertThat(dirResult.get()).isTrue();
        }

        @Test
        @DisplayName("Should return empty Optional for non-existent file")
        void testNonExistentFile() {
            // Given
            File nonExistentFile = new File("/non/existent/path");

            // When
            Optional<Boolean> filesResult = PathFilter.FILES.isAllowedTry(nonExistentFile);
            Optional<Boolean> foldersResult = PathFilter.FOLDERS.isAllowedTry(nonExistentFile);
            Optional<Boolean> bothResult = PathFilter.FILES_AND_FOLDERS.isAllowedTry(nonExistentFile);

            // Then
            assertThat(filesResult).isEmpty();
            assertThat(foldersResult).isEmpty();
            assertThat(bothResult).isEmpty();
        }

        @Test
        @DisplayName("Should handle complex directory structures")
        void testComplexDirectoryStructure(@TempDir Path tempDir) throws IOException {
            // Given
            Path subDir = tempDir.resolve("subdir");
            Files.createDirectory(subDir);
            Path fileInSubDir = subDir.resolve("file.txt");
            Files.createFile(fileInSubDir);

            // When/Then
            // Test files
            assertThat(PathFilter.FILES.isAllowedTry(fileInSubDir.toFile()).get()).isTrue();
            assertThat(PathFilter.FOLDERS.isAllowedTry(fileInSubDir.toFile()).get()).isFalse();
            assertThat(PathFilter.FILES_AND_FOLDERS.isAllowedTry(fileInSubDir.toFile()).get()).isTrue();

            // Test directories
            assertThat(PathFilter.FILES.isAllowedTry(subDir.toFile()).get()).isFalse();
            assertThat(PathFilter.FOLDERS.isAllowedTry(subDir.toFile()).get()).isTrue();
            assertThat(PathFilter.FILES_AND_FOLDERS.isAllowedTry(subDir.toFile()).get()).isTrue();
        }
    }

    @Nested
    @DisplayName("isAllowed Method Tests")
    class IsAllowedTests {

        @Test
        @DisplayName("Should return true for allowed file types")
        void testAllowedFileTypes(@TempDir Path tempDir) throws IOException {
            // Given
            Path testFile = tempDir.resolve("test.txt");
            Files.createFile(testFile);
            File file = testFile.toFile();
            File directory = tempDir.toFile();

            // When/Then
            assertThat(PathFilter.FILES.isAllowed(file)).isTrue();
            assertThat(PathFilter.FILES.isAllowed(directory)).isFalse();

            assertThat(PathFilter.FOLDERS.isAllowed(file)).isFalse();
            assertThat(PathFilter.FOLDERS.isAllowed(directory)).isTrue();

            assertThat(PathFilter.FILES_AND_FOLDERS.isAllowed(file)).isTrue();
            assertThat(PathFilter.FILES_AND_FOLDERS.isAllowed(directory)).isTrue();
        }

        @Test
        @DisplayName("Should throw RuntimeException for non-existent file")
        void testNonExistentFileThrowsException() {
            // Given
            File nonExistentFile = new File("/non/existent/path");

            // When/Then
            assertThatThrownBy(() -> PathFilter.FILES.isAllowed(nonExistentFile))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Could not find path");

            assertThatThrownBy(() -> PathFilter.FOLDERS.isAllowed(nonExistentFile))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Could not find path");

            assertThatThrownBy(() -> PathFilter.FILES_AND_FOLDERS.isAllowed(nonExistentFile))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Could not find path");
        }

        @Test
        @DisplayName("Should handle different file extensions")
        void testDifferentFileExtensions(@TempDir Path tempDir) throws IOException {
            // Given
            String[] extensions = { ".txt", ".java", ".xml", ".json", ".log", "" };

            for (String extension : extensions) {
                Path testFile = tempDir.resolve("test" + extension);
                Files.createFile(testFile);

                // When/Then
                assertThat(PathFilter.FILES.isAllowed(testFile.toFile())).isTrue();
                assertThat(PathFilter.FOLDERS.isAllowed(testFile.toFile())).isFalse();
                assertThat(PathFilter.FILES_AND_FOLDERS.isAllowed(testFile.toFile())).isTrue();
            }
        }

        @Test
        @DisplayName("Should handle nested directory structures")
        void testNestedDirectories(@TempDir Path tempDir) throws IOException {
            // Given
            Path level1 = tempDir.resolve("level1");
            Path level2 = level1.resolve("level2");
            Path level3 = level2.resolve("level3");
            Files.createDirectories(level3);

            // When/Then
            for (Path dir : new Path[] { level1, level2, level3 }) {
                assertThat(PathFilter.FILES.isAllowed(dir.toFile())).isFalse();
                assertThat(PathFilter.FOLDERS.isAllowed(dir.toFile())).isTrue();
                assertThat(PathFilter.FILES_AND_FOLDERS.isAllowed(dir.toFile())).isTrue();
            }
        }
    }

    @Nested
    @DisplayName("Filter Logic Consistency Tests")
    class FilterLogicConsistencyTests {

        @Test
        @DisplayName("isAllowed and isAllowedTry should be consistent for existing files")
        void testMethodConsistency(@TempDir Path tempDir) throws IOException {
            // Given
            Path testFile = tempDir.resolve("consistency.txt");
            Files.createFile(testFile);
            File file = testFile.toFile();
            File directory = tempDir.toFile();

            // When/Then - isAllowed should match isAllowedTry.get() for existing paths
            for (PathFilter filter : PathFilter.values()) {
                Optional<Boolean> tryResultFile = filter.isAllowedTry(file);
                Optional<Boolean> tryResultDir = filter.isAllowedTry(directory);

                assertThat(tryResultFile).isPresent();
                assertThat(tryResultDir).isPresent();

                assertThat(filter.isAllowed(file)).isEqualTo(tryResultFile.get());
                assertThat(filter.isAllowed(directory)).isEqualTo(tryResultDir.get());
            }
        }

        @Test
        @DisplayName("Should maintain filter exclusivity")
        void testFilterExclusivity(@TempDir Path tempDir) throws IOException {
            // Given
            Path testFile = tempDir.resolve("exclusive.txt");
            Files.createFile(testFile);
            File file = testFile.toFile();
            File directory = tempDir.toFile();

            // When/Then - FILES and FOLDERS should be mutually exclusive
            assertThat(PathFilter.FILES.isAllowed(file)).isTrue();
            assertThat(PathFilter.FOLDERS.isAllowed(file)).isFalse();

            assertThat(PathFilter.FILES.isAllowed(directory)).isFalse();
            assertThat(PathFilter.FOLDERS.isAllowed(directory)).isTrue();

            // FILES_AND_FOLDERS should accept both
            assertThat(PathFilter.FILES_AND_FOLDERS.isAllowed(file)).isTrue();
            assertThat(PathFilter.FILES_AND_FOLDERS.isAllowed(directory)).isTrue();
        }

        @Test
        @DisplayName("Should handle all possible file system entity types")
        void testAllFileSystemTypes(@TempDir Path tempDir) throws IOException {
            // Given
            Path regularFile = tempDir.resolve("regular.txt");
            Files.createFile(regularFile);

            Path directory = tempDir.resolve("directory");
            Files.createDirectory(directory);

            // When/Then - Test all combinations
            PathFilter[] filters = PathFilter.values();
            File[] entities = { regularFile.toFile(), directory.toFile() };

            for (PathFilter filter : filters) {
                for (File entity : entities) {
                    // Should not throw exception for existing entities
                    assertThatCode(() -> filter.isAllowed(entity)).doesNotThrowAnyException();
                    assertThatCode(() -> filter.isAllowedTry(entity)).doesNotThrowAnyException();

                    // isAllowedTry should always return present Optional for existing entities
                    assertThat(filter.isAllowedTry(entity)).isPresent();
                }
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle null file parameter gracefully")
        void testNullFileParameter() {
            // When/Then - Should throw NullPointerException
            for (PathFilter filter : PathFilter.values()) {
                assertThatThrownBy(() -> filter.isAllowed(null))
                        .isInstanceOf(NullPointerException.class);

                assertThatThrownBy(() -> filter.isAllowedTry(null))
                        .isInstanceOf(NullPointerException.class);
            }
        }

        @Test
        @DisplayName("Should handle files with special characters")
        void testSpecialCharacterFiles(@TempDir Path tempDir) throws IOException {
            // Given
            String[] specialNames = {
                    "file with spaces.txt",
                    "file@with#symbols$.txt",
                    "file_with_underscores.txt",
                    "file-with-dashes.txt",
                    "file.with.dots.txt",
                    "file(with)parentheses.txt"
            };

            for (String name : specialNames) {
                Path specialFile = tempDir.resolve(name);
                Files.createFile(specialFile);

                // When/Then
                assertThat(PathFilter.FILES.isAllowed(specialFile.toFile())).isTrue();
                assertThat(PathFilter.FOLDERS.isAllowed(specialFile.toFile())).isFalse();
                assertThat(PathFilter.FILES_AND_FOLDERS.isAllowed(specialFile.toFile())).isTrue();
            }
        }

        @Test
        @DisplayName("Should handle very long file paths")
        void testVeryLongFilePaths(@TempDir Path tempDir) throws IOException {
            // Given
            StringBuilder longPath = new StringBuilder();
            for (int i = 0; i < 50; i++) {
                longPath.append("very_long_directory_name_");
            }

            try {
                Path longDir = tempDir.resolve(longPath.toString());
                Files.createDirectory(longDir);

                // When/Then
                assertThat(PathFilter.FOLDERS.isAllowed(longDir.toFile())).isTrue();
                assertThat(PathFilter.FILES.isAllowed(longDir.toFile())).isFalse();
            } catch (Exception e) {
                // Skip test if file system doesn't support very long paths
                // This is expected behavior on some systems
            }
        }

        @Test
        @DisplayName("Should handle empty directory")
        void testEmptyDirectory(@TempDir Path tempDir) {
            // When/Then
            assertThat(PathFilter.FOLDERS.isAllowed(tempDir.toFile())).isTrue();
            assertThat(PathFilter.FILES.isAllowed(tempDir.toFile())).isFalse();
            assertThat(PathFilter.FILES_AND_FOLDERS.isAllowed(tempDir.toFile())).isTrue();
        }

        @Test
        @DisplayName("Should handle directory with many files")
        void testDirectoryWithManyFiles(@TempDir Path tempDir) throws IOException {
            // Given
            for (int i = 0; i < 100; i++) {
                Path file = tempDir.resolve("file_" + i + ".txt");
                Files.createFile(file);
            }

            // When/Then - Directory behavior should not change based on contents
            assertThat(PathFilter.FOLDERS.isAllowed(tempDir.toFile())).isTrue();
            assertThat(PathFilter.FILES.isAllowed(tempDir.toFile())).isFalse();
            assertThat(PathFilter.FILES_AND_FOLDERS.isAllowed(tempDir.toFile())).isTrue();
        }
    }

    @Nested
    @DisplayName("Integration and Performance Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with standard File operations")
        void testFileOperationsIntegration(@TempDir Path tempDir) throws IOException {
            // Given
            Path testFile = tempDir.resolve("integration.txt");
            Files.write(testFile, "test content".getBytes());
            File file = testFile.toFile();

            // When/Then - Should integrate well with File methods
            assertThat(file.exists()).isTrue();
            assertThat(file.isFile()).isTrue();
            assertThat(file.isDirectory()).isFalse();

            assertThat(PathFilter.FILES.isAllowed(file)).isEqualTo(file.isFile());
            assertThat(PathFilter.FOLDERS.isAllowed(file)).isEqualTo(file.isDirectory());
            assertThat(PathFilter.FILES_AND_FOLDERS.isAllowed(file)).isTrue();
        }

        @Test
        @DisplayName("Should handle concurrent access")
        void testConcurrentAccess(@TempDir Path tempDir) throws IOException, InterruptedException {
            // Given
            Path testFile = tempDir.resolve("concurrent.txt");
            Files.createFile(testFile);
            File file = testFile.toFile();

            // When
            Thread[] threads = new Thread[10];
            boolean[] results = new boolean[10];

            for (int i = 0; i < threads.length; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    results[index] = PathFilter.FILES.isAllowed(file);
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            // Then
            for (boolean result : results) {
                assertThat(result).isTrue();
            }
        }

        @Test
        @DisplayName("Should maintain performance with many filter operations")
        void testPerformance(@TempDir Path tempDir) throws IOException {
            // Given
            Path testFile = tempDir.resolve("performance.txt");
            Files.createFile(testFile);
            File file = testFile.toFile();

            // When/Then - Should handle many operations efficiently
            for (int i = 0; i < 1000; i++) {
                assertThat(PathFilter.FILES.isAllowed(file)).isTrue();
                assertThat(PathFilter.FOLDERS.isAllowed(tempDir.toFile())).isTrue();
                assertThat(PathFilter.FILES_AND_FOLDERS.isAllowed(file)).isTrue();
            }
        }
    }
}
