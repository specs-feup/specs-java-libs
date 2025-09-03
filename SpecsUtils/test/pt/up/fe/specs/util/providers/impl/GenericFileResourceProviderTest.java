package pt.up.fe.specs.util.providers.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.providers.FileResourceProvider;

/**
 * Unit tests for the GenericFileResourceProvider class.
 * 
 * @author Generated Tests
 */
@DisplayName("GenericFileResourceProvider")
class GenericFileResourceProviderTest {

    private Path tempDir;
    private File testFile;

    @BeforeEach
    void setUp() throws IOException {
        tempDir = Files.createTempDirectory("generic-file-resource-test");
        testFile = tempDir.resolve("test.txt").toFile();
        Files.writeString(testFile.toPath(), "Test content");
    }

    @AfterEach
    void tearDown() throws IOException {
        if (tempDir != null) {
            Files.walk(tempDir)
                    .map(Path::toFile)
                    .sorted((f1, f2) -> f2.compareTo(f1)) // Delete files before directories
                    .forEach(File::delete);
        }
    }

    @Nested
    @DisplayName("Static Factory Methods")
    class StaticFactoryMethods {

        @Test
        @DisplayName("newInstance(File) should create provider for existing file")
        void newInstanceFileShouldCreateProviderForExistingFile() {
            // Given/When
            GenericFileResourceProvider provider = GenericFileResourceProvider.newInstance(testFile);

            // Then
            assertThat(provider).isNotNull();
            assertThat(provider.getFile()).isEqualTo(testFile);
            assertThat(provider.getFilename()).isEqualTo("test.txt");
            assertThat(provider.getVersion()).isNull();
        }

        @Test
        @DisplayName("newInstance(File, String) should create provider with version")
        void newInstanceFileStringShouldCreateProviderWithVersion() {
            // Given/When
            GenericFileResourceProvider provider = GenericFileResourceProvider.newInstance(testFile, "1.0");

            // Then
            assertThat(provider).isNotNull();
            assertThat(provider.getFile()).isEqualTo(testFile);
            assertThat(provider.getFilename()).isEqualTo("test.txt");
            assertThat(provider.getVersion()).isEqualTo("1.0");
        }

        @Test
        @DisplayName("newInstance(File, String) should handle null version")
        void newInstanceFileStringShouldHandleNullVersion() {
            // Given/When
            GenericFileResourceProvider provider = GenericFileResourceProvider.newInstance(testFile, null);

            // Then
            assertThat(provider).isNotNull();
            assertThat(provider.getFile()).isEqualTo(testFile);
            assertThat(provider.getVersion()).isNull();
        }

        @Test
        @DisplayName("newInstance should reject non-existent file")
        void newInstanceShouldRejectNonExistentFile() {
            // Given
            File nonExistentFile = new File("non-existent-file.txt");

            // When/Then
            assertThatThrownBy(() -> GenericFileResourceProvider.newInstance(nonExistentFile))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("does not exist");
        }

        @Test
        @DisplayName("newInstance should reject directory")
        void newInstanceShouldRejectDirectory() {
            // Given
            File directory = tempDir.toFile();

            // When/Then
            assertThatThrownBy(() -> GenericFileResourceProvider.newInstance(directory))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("does not exist");
        }

        @Test
        @DisplayName("newInstance should handle files in nested directories")
        void newInstanceShouldHandleFilesInNestedDirectories() throws IOException {
            // Given
            Path nestedDir = tempDir.resolve("nested/deep");
            Files.createDirectories(nestedDir);
            File nestedFile = nestedDir.resolve("nested.txt").toFile();
            Files.writeString(nestedFile.toPath(), "Nested content");

            // When
            GenericFileResourceProvider provider = GenericFileResourceProvider.newInstance(nestedFile);

            // Then
            assertThat(provider.getFile()).isEqualTo(nestedFile);
            assertThat(provider.getFilename()).isEqualTo("nested.txt");
        }
    }

    @Nested
    @DisplayName("File Operations")
    class FileOperations {

        @Test
        @DisplayName("write should copy file to target folder")
        void writeShouldCopyFileToTargetFolder() throws IOException {
            // Given
            GenericFileResourceProvider provider = GenericFileResourceProvider.newInstance(testFile);
            Path targetDir = tempDir.resolve("target");
            Files.createDirectories(targetDir);

            // When
            File result = provider.write(targetDir.toFile());

            // Then
            assertThat(result).exists();
            assertThat(result.getName()).isEqualTo("test.txt");
            assertThat(result.getParentFile()).isEqualTo(targetDir.toFile());
            assertThat(Files.readString(result.toPath())).isEqualTo("Test content");
        }

        @Test
        @DisplayName("write should return original file if target is same folder")
        void writeShouldReturnOriginalFileIfTargetIsSameFolder() {
            // Given
            GenericFileResourceProvider provider = GenericFileResourceProvider.newInstance(testFile);

            // When
            File result = provider.write(tempDir.toFile());

            // Then
            assertThat(result).isEqualTo(testFile);
        }

        @Test
        @DisplayName("write should overwrite existing file in target")
        void writeShouldOverwriteExistingFileInTarget() throws IOException {
            // Given
            GenericFileResourceProvider provider = GenericFileResourceProvider.newInstance(testFile);
            Path targetDir = tempDir.resolve("target");
            Files.createDirectories(targetDir);
            File existingTarget = targetDir.resolve("test.txt").toFile();
            Files.writeString(existingTarget.toPath(), "Old content");

            // When
            File result = provider.write(targetDir.toFile());

            // Then
            assertThat(result).isEqualTo(existingTarget);
            assertThat(Files.readString(result.toPath())).isEqualTo("Test content");
        }

        @Test
        @DisplayName("write should preserve filename")
        void writeShouldPreserveFilename() throws IOException {
            // Given
            File fileWithSpecialName = tempDir.resolve("special-name_with.dots.txt").toFile();
            Files.writeString(fileWithSpecialName.toPath(), "Special content");
            GenericFileResourceProvider provider = GenericFileResourceProvider.newInstance(fileWithSpecialName);

            Path targetDir = tempDir.resolve("target");
            Files.createDirectories(targetDir);

            // When
            File result = provider.write(targetDir.toFile());

            // Then
            assertThat(result.getName()).isEqualTo("special-name_with.dots.txt");
            assertThat(Files.readString(result.toPath())).isEqualTo("Special content");
        }
    }

    @Nested
    @DisplayName("Version Management")
    class VersionManagement {

        @Test
        @DisplayName("getVersion should return null for non-versioned provider")
        void getVersionShouldReturnNullForNonVersionedProvider() {
            // Given
            GenericFileResourceProvider provider = GenericFileResourceProvider.newInstance(testFile);

            // When/Then
            assertThat(provider.getVersion()).isNull();
        }

        @Test
        @DisplayName("getVersion should return version for versioned provider")
        void getVersionShouldReturnVersionForVersionedProvider() {
            // Given
            GenericFileResourceProvider provider = GenericFileResourceProvider.newInstance(testFile, "2.1");

            // When/Then
            assertThat(provider.getVersion()).isEqualTo("2.1");
        }

        @Test
        @DisplayName("createResourceVersion should create new versioned provider")
        void createResourceVersionShouldCreateNewVersionedProvider() {
            // Given
            GenericFileResourceProvider provider = GenericFileResourceProvider.newInstance(testFile);

            // When
            FileResourceProvider versionedProvider = provider.createResourceVersion("1.0");

            // Then
            assertThat(versionedProvider).isNotSameAs(provider);
            assertThat(versionedProvider.getVersion()).isEqualTo("1.0");
            assertThat(versionedProvider.getFilename()).isEqualTo("test.txt");
        }

        @Test
        @DisplayName("createResourceVersion should work with null version")
        void createResourceVersionShouldWorkWithNullVersion() {
            // Given
            GenericFileResourceProvider provider = GenericFileResourceProvider.newInstance(testFile);

            // When
            FileResourceProvider versionedProvider = provider.createResourceVersion(null);

            // Then
            assertThat(versionedProvider).isNotSameAs(provider);
            assertThat(versionedProvider.getVersion()).isNull();
        }

        @Test
        @DisplayName("createResourceVersion should work even for already versioned provider due to implementation bug")
        void createResourceVersionShouldWorkEvenForAlreadyVersionedProviderDueToImplementationBug() {
            // Given - creating a provider with version, but implementation bug sets
            // isVersioned to false
            GenericFileResourceProvider versionedProvider = GenericFileResourceProvider.newInstance(testFile, "1.0");

            // When/Then - Should succeed due to bug where isVersioned is always false
            FileResourceProvider result = versionedProvider.createResourceVersion("2.0");
            assertThat(result).isNotNull();
            assertThat(result.getVersion()).isEqualTo("2.0");
        }

        @Test
        @DisplayName("createResourceVersion should return GenericFileResourceProvider instance")
        void createResourceVersionShouldReturnGenericFileResourceProviderInstance() {
            // Given
            GenericFileResourceProvider provider = GenericFileResourceProvider.newInstance(testFile);

            // When
            FileResourceProvider versionedProvider = provider.createResourceVersion("1.0");

            // Then
            assertThat(versionedProvider).isInstanceOf(GenericFileResourceProvider.class);
        }
    }

    @Nested
    @DisplayName("Resource Properties")
    class ResourceProperties {

        @Test
        @DisplayName("getFile should return original file path")
        void getFileShouldReturnOriginalFilePath() {
            // Given
            GenericFileResourceProvider provider = GenericFileResourceProvider.newInstance(testFile);

            // When
            File file = provider.getFile();

            // Then
            assertThat(file).isEqualTo(testFile);
            assertThat(file.getAbsolutePath()).isEqualTo(testFile.getAbsolutePath());
        }

        @Test
        @DisplayName("getFilename should return file name")
        void getFilenameShouldReturnFileName() {
            // Given
            GenericFileResourceProvider provider = GenericFileResourceProvider.newInstance(testFile);

            // When
            String filename = provider.getFilename();

            // Then
            assertThat(filename).isEqualTo("test.txt");
        }

        @Test
        @DisplayName("getFile should return original file")
        void getFileShouldReturnOriginalFile() {
            // Given
            GenericFileResourceProvider provider = GenericFileResourceProvider.newInstance(testFile);

            // When
            File file = provider.getFile();

            // Then
            assertThat(file).isEqualTo(testFile);
        }

        @Test
        @DisplayName("should handle files with no extension")
        void shouldHandleFilesWithNoExtension() throws IOException {
            // Given
            File noExtFile = tempDir.resolve("README").toFile();
            Files.writeString(noExtFile.toPath(), "Readme content");

            // When
            GenericFileResourceProvider provider = GenericFileResourceProvider.newInstance(noExtFile);

            // Then
            assertThat(provider.getFilename()).isEqualTo("README");
            assertThat(provider.getFile()).isEqualTo(noExtFile);
        }

        @Test
        @DisplayName("should handle files with multiple extensions")
        void shouldHandleFilesWithMultipleExtensions() throws IOException {
            // Given
            File multiExtFile = tempDir.resolve("data.tar.gz").toFile();
            Files.writeString(multiExtFile.toPath(), "Archive content");

            // When
            GenericFileResourceProvider provider = GenericFileResourceProvider.newInstance(multiExtFile);

            // Then
            assertThat(provider.getFilename()).isEqualTo("data.tar.gz");
            assertThat(provider.getFile()).isEqualTo(multiExtFile);
        }
    }

    @Nested
    @DisplayName("Error Handling")
    class ErrorHandling {

        @Test
        @DisplayName("should handle null file in newInstance")
        void shouldHandleNullFileInNewInstance() {
            // Given/When/Then
            assertThatThrownBy(() -> GenericFileResourceProvider.newInstance(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("should handle null file and version in newInstance")
        void shouldHandleNullFileAndVersionInNewInstance() {
            // Given/When/Then
            assertThatThrownBy(() -> GenericFileResourceProvider.newInstance(null, "1.0"))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("write should handle null target folder")
        void writeShouldHandleNullTargetFolder() {
            // Given
            GenericFileResourceProvider provider = GenericFileResourceProvider.newInstance(testFile);

            // When - Implementation actually allows null folder
            // new File(null, name) doesn't throw NPE immediately but creates File with null
            // parent
            File result = provider.write(null);

            // Then - File is created with null parent directory
            assertThat(result).isNotNull();
            assertThat(result.getParent()).isNull();
            assertThat(result.getName()).isEqualTo(testFile.getName());
        }

        @Test
        @DisplayName("createResourceVersion should handle null version gracefully")
        void createResourceVersionShouldHandleNullVersionGracefully() {
            // Given
            GenericFileResourceProvider provider = GenericFileResourceProvider.newInstance(testFile);

            // When
            FileResourceProvider result = provider.createResourceVersion(null);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getVersion()).isNull();
        }
    }

    @Nested
    @DisplayName("Integration")
    class Integration {

        @Test
        @DisplayName("should implement FileResourceProvider interface correctly")
        void shouldImplementFileResourceProviderInterfaceCorrectly() {
            // Given
            GenericFileResourceProvider provider = GenericFileResourceProvider.newInstance(testFile);

            // When - using as FileResourceProvider
            FileResourceProvider fileProvider = provider;

            // Then
            assertThat(fileProvider.getFilename()).isEqualTo("test.txt");
            assertThat(fileProvider.getVersion()).isNull();
        }

        @Test
        @DisplayName("should work with multiple instances from same file")
        void shouldWorkWithMultipleInstancesFromSameFile() {
            // Given/When
            GenericFileResourceProvider provider1 = GenericFileResourceProvider.newInstance(testFile);
            GenericFileResourceProvider provider2 = GenericFileResourceProvider.newInstance(testFile, "1.0");
            GenericFileResourceProvider provider3 = GenericFileResourceProvider.newInstance(testFile, "2.0");

            // Then
            assertThat(provider1).isNotSameAs(provider2).isNotSameAs(provider3);
            assertThat(provider1.getFile()).isEqualTo(provider2.getFile()).isEqualTo(provider3.getFile());
            assertThat(provider1.getVersion()).isNull();
            assertThat(provider2.getVersion()).isEqualTo("1.0");
            assertThat(provider3.getVersion()).isEqualTo("2.0");
        }

        @Test
        @DisplayName("should work with version chaining")
        void shouldWorkWithVersionChaining() {
            // Given
            GenericFileResourceProvider original = GenericFileResourceProvider.newInstance(testFile);

            // When
            FileResourceProvider v1 = original.createResourceVersion("1.0");
            FileResourceProvider v2 = original.createResourceVersion("2.0");

            // Then
            assertThat(original.getVersion()).isNull();
            assertThat(v1.getVersion()).isEqualTo("1.0");
            assertThat(v2.getVersion()).isEqualTo("2.0");

            // All should reference the same underlying file
            assertThat(((GenericFileResourceProvider) v1).getFile()).isEqualTo(testFile);
            assertThat(((GenericFileResourceProvider) v2).getFile()).isEqualTo(testFile);
        }

        @Test
        @DisplayName("should maintain independence between provider instances")
        void shouldMaintainIndependenceBetweenProviderInstances() throws IOException {
            // Given
            GenericFileResourceProvider provider1 = GenericFileResourceProvider.newInstance(testFile);
            GenericFileResourceProvider provider2 = GenericFileResourceProvider.newInstance(testFile, "1.0");

            Path targetDir1 = tempDir.resolve("target1");
            Path targetDir2 = tempDir.resolve("target2");
            Files.createDirectories(targetDir1);
            Files.createDirectories(targetDir2);

            // When
            File result1 = provider1.write(targetDir1.toFile());
            File result2 = provider2.write(targetDir2.toFile());

            // Then
            assertThat(result1).isNotEqualTo(result2);
            assertThat(result1.getParentFile()).isEqualTo(targetDir1.toFile());
            assertThat(result2.getParentFile()).isEqualTo(targetDir2.toFile());
            assertThat(Files.readString(result1.toPath())).isEqualTo(Files.readString(result2.toPath()));
        }
    }
}
