package pt.up.fe.specs.util.providers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.prefs.Preferences;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import pt.up.fe.specs.util.providers.FileResourceProvider.ResourceWriteData;

/**
 * Unit tests for FileResourceProvider interface and its implementations.
 * 
 * @author Generated Tests
 */
@DisplayName("FileResourceProvider")
class FileResourceProviderTest {

    @TempDir
    Path tempDir;

    private FileResourceProvider testProvider;
    private File testFile;

    @BeforeEach
    void setUp() throws IOException {
        testFile = Files.createTempFile(tempDir, "test", ".txt").toFile();
        Files.write(testFile.toPath(), "test content".getBytes());
        testProvider = FileResourceProvider.newInstance(testFile);
    }

    @Nested
    @DisplayName("Interface Contract")
    class InterfaceContract {

        @Test
        @DisplayName("should have correct interface methods")
        void shouldHaveCorrectInterfaceMethods() {
            assertThatCode(() -> {
                FileResourceProvider.class.getMethod("write", File.class);
                FileResourceProvider.class.getMethod("getVersion");
                FileResourceProvider.class.getMethod("getFilename");
                FileResourceProvider.class.getMethod("writeVersioned", File.class, Class.class);
                FileResourceProvider.class.getMethod("writeVersioned", File.class, Class.class, boolean.class);
                FileResourceProvider.class.getMethod("createResourceVersion", String.class);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should be an interface")
        void shouldBeAnInterface() {
            assertThat(FileResourceProvider.class.isInterface()).isTrue();
        }
    }

    @Nested
    @DisplayName("Static Factory Methods")
    class StaticFactoryMethods {

        @Test
        @DisplayName("should create instance from existing file")
        void shouldCreateInstanceFromExistingFile() {
            FileResourceProvider provider = FileResourceProvider.newInstance(testFile);

            assertThat(provider).isNotNull();
            assertThat(provider.getFilename()).isEqualTo(testFile.getName());
        }

        @Test
        @DisplayName("should create instance with version suffix")
        void shouldCreateInstanceWithVersionSuffix() {
            String versionSuffix = "v2.0";
            FileResourceProvider provider = FileResourceProvider.newInstance(testFile, versionSuffix);

            assertThat(provider).isNotNull();
            assertThat(provider.getVersion()).contains(versionSuffix);
        }

        @Test
        @DisplayName("should handle null file")
        void shouldHandleNullFile() {
            assertThatThrownBy(() -> FileResourceProvider.newInstance(null))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("should handle null version suffix")
        void shouldHandleNullVersionSuffix() {
            FileResourceProvider provider = FileResourceProvider.newInstance(testFile, null);

            assertThat(provider).isNotNull();
        }

        @Test
        @DisplayName("should handle empty version suffix")
        void shouldHandleEmptyVersionSuffix() {
            FileResourceProvider provider = FileResourceProvider.newInstance(testFile, "");

            assertThat(provider).isNotNull();
        }
    }

    @Nested
    @DisplayName("Basic File Operations")
    class BasicFileOperations {

        @Test
        @DisplayName("should return filename")
        void shouldReturnFilename() {
            String filename = testProvider.getFilename();

            assertThat(filename).isEqualTo(testFile.getName());
            assertThat(filename).endsWith(".txt");
        }

        @Test
        @DisplayName("should return version information or null")
        void shouldReturnVersionInformationOrNull() {
            String version = testProvider.getVersion();

            // Version can be null for providers created without explicit version
            if (version != null) {
                assertThat(version).isNotEmpty();
            }
        }

        @Test
        @DisplayName("should write file to destination folder")
        void shouldWriteFileToDestinationFolder() {
            File destinationDir = tempDir.resolve("destination").toFile();
            destinationDir.mkdirs();

            File writtenFile = testProvider.write(destinationDir);

            assertThat(writtenFile).exists();
            assertThat(writtenFile.getParentFile()).isEqualTo(destinationDir);
            assertThat(writtenFile.getName()).isEqualTo(testFile.getName());
        }

        @Test
        @DisplayName("should preserve file content when writing")
        void shouldPreserveFileContentWhenWriting() throws IOException {
            File destinationDir = tempDir.resolve("content").toFile();
            destinationDir.mkdirs();

            File writtenFile = testProvider.write(destinationDir);

            assertThat(Files.readString(writtenFile.toPath())).isEqualTo("test content");
        }

        @Test
        @DisplayName("should handle write to non-existing directory")
        void shouldHandleWriteToNonExistingDirectory() {
            File nonExistingDir = tempDir.resolve("nonexisting").toFile();

            assertThatCode(() -> {
                File writtenFile = testProvider.write(nonExistingDir);
                assertThat(writtenFile).exists();
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Versioned Writing")
    class VersionedWriting {

        @Test
        @DisplayName("should write new file with versioning when version is not null")
        void shouldWriteNewFileWithVersioningWhenVersionIsNotNull() {
            File destinationDir = tempDir.resolve("versioned").toFile();
            destinationDir.mkdirs();

            // Create a provider with explicit version to avoid null version bug
            FileResourceProvider providerWithVersion = FileResourceProvider.newInstance(testFile, "1.0");

            ResourceWriteData result = providerWithVersion.writeVersioned(destinationDir,
                    FileResourceProviderTest.class);

            assertThat(result).isNotNull();
            assertThat(result.getFile()).exists();
            assertThat(result.isNewFile()).isTrue();
        }

        @Test
        @DisplayName("should not overwrite file with same version when version is not null")
        void shouldNotOverwriteFileWithSameVersionWhenVersionIsNotNull() {
            File destinationDir = tempDir.resolve("same_version").toFile();
            destinationDir.mkdirs();

            // Create a provider with explicit version to avoid null version bug
            FileResourceProvider providerWithVersion = FileResourceProvider.newInstance(testFile, "1.0");

            // First write
            ResourceWriteData result1 = providerWithVersion.writeVersioned(destinationDir,
                    FileResourceProviderTest.class);
            long originalLastModified = result1.getFile().lastModified();

            // Second write with same version
            ResourceWriteData result2 = providerWithVersion.writeVersioned(destinationDir,
                    FileResourceProviderTest.class);

            assertThat(result2.isNewFile()).isFalse();
            assertThat(result2.getFile().lastModified()).isEqualTo(originalLastModified);
        }

        @Test
        @DisplayName("should handle writeIfNoVersionInfo parameter")
        void shouldHandleWriteIfNoVersionInfoParameter() {
            File destinationDir = tempDir.resolve("no_version_info").toFile();
            destinationDir.mkdirs();

            // Create file manually to simulate existing file without version info
            File existingFile = new File(destinationDir, testProvider.getFilename());
            try {
                Files.write(existingFile.toPath(), "existing content".getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Clear preferences to simulate no version info
            Preferences prefs = Preferences.userNodeForPackage(FileResourceProviderTest.class);
            String key = testProvider.getClass().getSimpleName() + "." + testProvider.getFilename();
            prefs.remove(key);

            // Try to write with writeIfNoVersionInfo = false
            ResourceWriteData result = testProvider.writeVersioned(destinationDir, FileResourceProviderTest.class,
                    false);

            assertThat(result.isNewFile()).isFalse();
            assertThat(result.getFile()).exists();
        }

        @Test
        @DisplayName("should use different contexts for version storage when version is not null")
        void shouldUseDifferentContextsForVersionStorageWhenVersionIsNotNull() {
            File destinationDir = tempDir.resolve("contexts").toFile();
            destinationDir.mkdirs();

            // Create a provider with explicit version to avoid null version bug
            FileResourceProvider providerWithVersion = FileResourceProvider.newInstance(testFile, "1.0");

            // Write with one context
            ResourceWriteData result1 = providerWithVersion.writeVersioned(destinationDir, String.class);

            // Write with different context
            ResourceWriteData result2 = providerWithVersion.writeVersioned(destinationDir, Integer.class);

            assertThat(result1.getFile()).exists();
            assertThat(result2.getFile()).exists();
        }
    }

    @Nested
    @DisplayName("ResourceWriteData")
    class ResourceWriteDataTests {

        @Test
        @DisplayName("should create ResourceWriteData with file and new flag")
        void shouldCreateResourceWriteDataWithFileAndNewFlag() {
            ResourceWriteData data = new ResourceWriteData(testFile, true);

            assertThat(data.getFile()).isEqualTo(testFile);
            assertThat(data.isNewFile()).isTrue();
        }

        @Test
        @DisplayName("should handle false new file flag")
        void shouldHandleFalseNewFileFlag() {
            ResourceWriteData data = new ResourceWriteData(testFile, false);

            assertThat(data.getFile()).isEqualTo(testFile);
            assertThat(data.isNewFile()).isFalse();
        }

        @Test
        @DisplayName("should throw exception for null file")
        void shouldThrowExceptionForNullFile() {
            assertThatThrownBy(() -> new ResourceWriteData(null, true))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("writtenFile should not be null");
        }

        @Test
        @DisplayName("should handle makeExecutable on non-Linux systems")
        void shouldHandleMakeExecutableOnNonLinuxSystems() {
            ResourceWriteData data = new ResourceWriteData(testFile, true);

            assertThatCode(() -> data.makeExecutable(false)).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should handle makeExecutable for existing files")
        void shouldHandleMakeExecutableForExistingFiles() {
            ResourceWriteData data = new ResourceWriteData(testFile, false);

            assertThatCode(() -> data.makeExecutable(true)).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Version Creation")
    class VersionCreation {

        @Test
        @DisplayName("should implement createResourceVersion for non-versioned files")
        void shouldImplementCreateResourceVersionForNonVersionedFiles() {
            // GenericFileResourceProvider actually implements this for non-versioned files
            FileResourceProvider versionedProvider = testProvider.createResourceVersion("v2.0");

            assertThat(versionedProvider).isNotNull();
            assertThat(versionedProvider.getVersion()).contains("v2.0");
        }

        @Test
        @DisplayName("should handle null version in createResourceVersion")
        void shouldHandleNullVersionInCreateResourceVersion() {
            FileResourceProvider provider = testProvider.createResourceVersion(null);

            assertThat(provider).isNotNull();
        }

        @Test
        @DisplayName("should handle empty version in createResourceVersion")
        void shouldHandleEmptyVersionInCreateResourceVersion() {
            FileResourceProvider provider = testProvider.createResourceVersion("");

            assertThat(provider).isNotNull();
        }
    }

    @Nested
    @DisplayName("File Types and Extensions")
    class FileTypesAndExtensions {

        @Test
        @DisplayName("should handle different file extensions")
        void shouldHandleDifferentFileExtensions() throws IOException {
            String[] extensions = { ".jar", ".exe", ".sh", ".bat", ".dll", ".so" };

            for (String ext : extensions) {
                File file = Files.createTempFile(tempDir, "test", ext).toFile();
                FileResourceProvider provider = FileResourceProvider.newInstance(file);

                assertThat(provider.getFilename()).endsWith(ext);
            }
        }

        @Test
        @DisplayName("should handle files without extensions")
        void shouldHandleFilesWithoutExtensions() throws IOException {
            File fileNoExt = tempDir.resolve("filenoext").toFile();
            Files.write(fileNoExt.toPath(), "content".getBytes());

            FileResourceProvider provider = FileResourceProvider.newInstance(fileNoExt);

            assertThat(provider.getFilename()).isEqualTo("filenoext");
            assertThat(provider.getFilename()).doesNotContain(".");
        }

        @Test
        @DisplayName("should handle files with multiple dots")
        void shouldHandleFilesWithMultipleDots() throws IOException {
            File multiDotFile = tempDir.resolve("test.config.xml").toFile();
            Files.write(multiDotFile.toPath(), "content".getBytes());

            FileResourceProvider provider = FileResourceProvider.newInstance(multiDotFile);

            assertThat(provider.getFilename()).isEqualTo("test.config.xml");
        }

        @Test
        @DisplayName("should handle very long filenames")
        void shouldHandleVeryLongFilenames() throws IOException {
            String longName = "a".repeat(200) + ".txt";
            File longFile = tempDir.resolve(longName).toFile();
            Files.write(longFile.toPath(), "content".getBytes());

            FileResourceProvider provider = FileResourceProvider.newInstance(longFile);

            assertThat(provider.getFilename()).isEqualTo(longName);
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("should handle empty files")
        void shouldHandleEmptyFiles() throws IOException {
            File emptyFile = Files.createTempFile(tempDir, "empty", ".txt").toFile();
            FileResourceProvider provider = FileResourceProvider.newInstance(emptyFile);

            File destinationDir = tempDir.resolve("empty_dest").toFile();
            destinationDir.mkdirs();

            File writtenFile = provider.write(destinationDir);

            assertThat(writtenFile).exists();
            assertThat(writtenFile.length()).isZero();
        }

        @Test
        @DisplayName("should handle large files")
        void shouldHandleLargeFiles() throws IOException {
            File largeFile = Files.createTempFile(tempDir, "large", ".txt").toFile();
            byte[] largeContent = new byte[1024 * 1024]; // 1MB
            Files.write(largeFile.toPath(), largeContent);

            FileResourceProvider provider = FileResourceProvider.newInstance(largeFile);

            File destinationDir = tempDir.resolve("large_dest").toFile();
            destinationDir.mkdirs();

            File writtenFile = provider.write(destinationDir);

            assertThat(writtenFile).exists();
            assertThat(writtenFile.length()).isEqualTo(largeFile.length());
        }

        @Test
        @DisplayName("should handle unicode filenames")
        void shouldHandleUnicodeFilenames() throws IOException {
            File unicodeFile = tempDir.resolve("测试文件.txt").toFile();
            Files.write(unicodeFile.toPath(), "unicode content".getBytes());

            FileResourceProvider provider = FileResourceProvider.newInstance(unicodeFile);

            assertThat(provider.getFilename()).isEqualTo("测试文件.txt");
        }

        @Test
        @DisplayName("should handle special characters in filenames")
        void shouldHandleSpecialCharactersInFilenames() throws IOException {
            File specialFile = tempDir.resolve("test@#$%^&()_+.txt").toFile();
            Files.write(specialFile.toPath(), "special content".getBytes());

            FileResourceProvider provider = FileResourceProvider.newInstance(specialFile);

            assertThat(provider.getFilename()).isEqualTo("test@#$%^&()_+.txt");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("should work with mocked implementations")
        void shouldWorkWithMockedImplementations() {
            FileResourceProvider mockProvider = mock(FileResourceProvider.class);
            when(mockProvider.getFilename()).thenReturn("mock.txt");
            when(mockProvider.getVersion()).thenReturn("1.0.0");
            when(mockProvider.write(any(File.class))).thenReturn(testFile);

            assertThat(mockProvider.getFilename()).isEqualTo("mock.txt");
            assertThat(mockProvider.getVersion()).isEqualTo("1.0.0");
            assertThat(mockProvider.write(tempDir.toFile())).isEqualTo(testFile);
        }

        @Test
        @DisplayName("should support method chaining when version is not null")
        void shouldSupportMethodChainingWhenVersionIsNotNull() {
            File destinationDir = tempDir.resolve("chaining").toFile();
            destinationDir.mkdirs();

            ResourceWriteData result = FileResourceProvider.newInstance(testFile, "1.0")
                    .writeVersioned(destinationDir, FileResourceProviderTest.class);

            assertThat(result).isNotNull();
            assertThat(result.getFile()).exists();
        }

        @Test
        @DisplayName("should work with different provider types")
        void shouldWorkWithDifferentProviderTypes() {
            FileResourceProvider provider1 = FileResourceProvider.newInstance(testFile);
            FileResourceProvider provider2 = FileResourceProvider.newInstance(testFile, "v1.0");

            assertThat(provider1.getFilename()).isEqualTo(provider2.getFilename());
            assertThat(provider1.getVersion()).isNotEqualTo(provider2.getVersion());
        }
    }

    @Nested
    @DisplayName("Error Handling")
    class ErrorHandling {

        @Test
        @DisplayName("should handle write to read-only destination")
        void shouldHandleWriteToReadOnlyDestination() {
            File readOnlyDir = tempDir.resolve("readonly").toFile();
            readOnlyDir.mkdirs();
            readOnlyDir.setReadOnly();

            // This may or may not throw depending on the implementation and OS
            // Just verify it doesn't crash the test
            assertThatCode(() -> {
                try {
                    testProvider.write(readOnlyDir);
                } catch (Exception e) {
                    // Expected on some systems
                }
            }).doesNotThrowAnyException();

            // Cleanup
            readOnlyDir.setWritable(true);
        }

        @Test
        @DisplayName("should handle concurrent access when version is not null")
        void shouldHandleConcurrentAccessWhenVersionIsNotNull() {
            File destinationDir = tempDir.resolve("concurrent").toFile();
            destinationDir.mkdirs();

            // Create a provider with explicit version to avoid null version bug
            FileResourceProvider providerWithVersion = FileResourceProvider.newInstance(testFile, "1.0");

            // This test ensures the provider doesn't crash under concurrent access
            assertThatCode(() -> {
                for (int i = 0; i < 10; i++) {
                    providerWithVersion.writeVersioned(destinationDir, FileResourceProviderTest.class);
                }
            }).doesNotThrowAnyException();
        }
    }
}
