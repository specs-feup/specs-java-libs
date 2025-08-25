package org.suikasoft.jOptions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.suikasoft.jOptions.Interfaces.DataStore;

/**
 * Unit tests for {@link JOptionKeys}.
 * 
 * Tests the common DataKeys and utility methods for jOptions context path
 * management, including path resolution with working directories and relative
 * path handling.
 * 
 * @author Generated Tests
 */
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@DisplayName("JOptionKeys")
class JOptionKeysTest {

    @TempDir
    File tempDir;

    private DataStore mockDataStore;

    @BeforeEach
    void setUp() {
        mockDataStore = mock(DataStore.class);
    }

    @Nested
    @DisplayName("DataKey Constants")
    class DataKeyConstantsTests {

        @Test
        @DisplayName("CURRENT_FOLDER_PATH has correct name and type")
        void testCurrentFolderPath_HasCorrectNameAndType() {
            assertThat(JOptionKeys.CURRENT_FOLDER_PATH.getName())
                    .isEqualTo("joptions_current_folder_path");
            assertThat(JOptionKeys.CURRENT_FOLDER_PATH.getValueClass())
                    .isEqualTo(Optional.class);
        }

        @Test
        @DisplayName("USE_RELATIVE_PATHS has correct name and type")
        void testUseRelativePaths_HasCorrectNameAndType() {
            assertThat(JOptionKeys.USE_RELATIVE_PATHS.getName())
                    .isEqualTo("joptions_use_relative_paths");
            assertThat(JOptionKeys.USE_RELATIVE_PATHS.getValueClass())
                    .isEqualTo(Boolean.class);
        }

        @Test
        @DisplayName("DataKeys are properly typed")
        void testDataKeys_AreProperlyTyped() {
            // Test that the keys have proper generic types
            assertThat(JOptionKeys.CURRENT_FOLDER_PATH)
                    .isNotNull()
                    .satisfies(key -> assertThat(key.getName()).isNotEmpty());

            assertThat(JOptionKeys.USE_RELATIVE_PATHS)
                    .isNotNull()
                    .satisfies(key -> assertThat(key.getName()).isNotEmpty());
        }
    }

    @Nested
    @DisplayName("Context Path Resolution with File")
    class ContextPathResolutionFileTests {

        @Test
        @DisplayName("getContextPath returns original file when no working folder is set")
        void testGetContextPath_NoWorkingFolder_ReturnsOriginalFile() {
            File testFile = new File("test.txt");

            when(mockDataStore.get(JOptionKeys.CURRENT_FOLDER_PATH))
                    .thenReturn(Optional.empty());

            File result = JOptionKeys.getContextPath(testFile, mockDataStore);

            assertThat(result).isSameAs(testFile);
        }

        @Test
        @DisplayName("getContextPath returns original file when file is absolute")
        void testGetContextPath_AbsoluteFile_ReturnsOriginalFile() {
            File absoluteFile = new File(tempDir, "absolute.txt").getAbsoluteFile();
            String workingFolder = tempDir.getAbsolutePath();

            when(mockDataStore.get(JOptionKeys.CURRENT_FOLDER_PATH))
                    .thenReturn(Optional.of(workingFolder));

            File result = JOptionKeys.getContextPath(absoluteFile, mockDataStore);

            assertThat(result).isSameAs(absoluteFile);
        }

        @Test
        @DisplayName("getContextPath resolves relative file with working folder")
        void testGetContextPath_RelativeFile_ResolvesWithWorkingFolder() {
            File relativeFile = new File("relative.txt");
            String workingFolder = tempDir.getAbsolutePath();

            when(mockDataStore.get(JOptionKeys.CURRENT_FOLDER_PATH))
                    .thenReturn(Optional.of(workingFolder));

            File result = JOptionKeys.getContextPath(relativeFile, mockDataStore);

            assertThat(result.getParentFile().getAbsolutePath())
                    .isEqualTo(workingFolder);
            assertThat(result.getName()).isEqualTo("relative.txt");
        }

        @Test
        @DisplayName("getContextPath handles nested relative paths")
        void testGetContextPath_NestedRelativePath_ResolvesCorrectly() {
            File nestedFile = new File("subdir/nested.txt");
            String workingFolder = tempDir.getAbsolutePath();

            when(mockDataStore.get(JOptionKeys.CURRENT_FOLDER_PATH))
                    .thenReturn(Optional.of(workingFolder));

            File result = JOptionKeys.getContextPath(nestedFile, mockDataStore);

            assertThat(result.getPath())
                    .endsWith("subdir/nested.txt".replace("/", File.separator));
            assertThat(result.getPath())
                    .startsWith(workingFolder);
        }

        @Test
        @DisplayName("getContextPath handles empty file name")
        void testGetContextPath_EmptyFileName_HandlesGracefully() {
            File emptyFile = new File("");
            String workingFolder = tempDir.getAbsolutePath();

            when(mockDataStore.get(JOptionKeys.CURRENT_FOLDER_PATH))
                    .thenReturn(Optional.of(workingFolder));

            File result = JOptionKeys.getContextPath(emptyFile, mockDataStore);

            // When creating File(parent, "") where parent is non-empty, the result inherits
            // the parent's name
            assertThat(result.getParent()).isNotNull();
            // The resulting file will have the working folder as its base
            assertThat(result.getPath()).startsWith(workingFolder);
        }
    }

    @Nested
    @DisplayName("Context Path Resolution with String")
    class ContextPathResolutionStringTests {

        @Test
        @DisplayName("getContextPath with String delegates to File version")
        void testGetContextPath_WithString_DelegatesToFileVersion() {
            String testPath = "test.txt";

            when(mockDataStore.get(JOptionKeys.CURRENT_FOLDER_PATH))
                    .thenReturn(Optional.empty());

            File result = JOptionKeys.getContextPath(testPath, mockDataStore);

            assertThat(result.getPath()).isEqualTo(testPath);
        }

        @Test
        @DisplayName("getContextPath with absolute string path returns absolute file")
        void testGetContextPath_AbsoluteStringPath_ReturnsAbsoluteFile() {
            String absolutePath = new File(tempDir, "absolute.txt").getAbsolutePath();
            String workingFolder = tempDir.getAbsolutePath();

            when(mockDataStore.get(JOptionKeys.CURRENT_FOLDER_PATH))
                    .thenReturn(Optional.of(workingFolder));

            File result = JOptionKeys.getContextPath(absolutePath, mockDataStore);

            assertThat(result.getAbsolutePath()).isEqualTo(absolutePath);
        }

        @Test
        @DisplayName("getContextPath with relative string resolves with working folder")
        void testGetContextPath_RelativeString_ResolvesWithWorkingFolder() {
            String relativePath = "relative.txt";
            String workingFolder = tempDir.getAbsolutePath();

            when(mockDataStore.get(JOptionKeys.CURRENT_FOLDER_PATH))
                    .thenReturn(Optional.of(workingFolder));

            File result = JOptionKeys.getContextPath(relativePath, mockDataStore);

            assertThat(result.getParentFile().getAbsolutePath())
                    .isEqualTo(workingFolder);
            assertThat(result.getName()).isEqualTo("relative.txt");
        }

        @Test
        @DisplayName("getContextPath with complex path handles correctly")
        void testGetContextPath_ComplexPath_HandlesCorrectly() {
            String complexPath = "./subdir/../file.txt";
            String workingFolder = tempDir.getAbsolutePath();

            when(mockDataStore.get(JOptionKeys.CURRENT_FOLDER_PATH))
                    .thenReturn(Optional.of(workingFolder));

            File result = JOptionKeys.getContextPath(complexPath, mockDataStore);

            assertThat(result.getPath())
                    .contains(complexPath);
            assertThat(result.getPath())
                    .startsWith(workingFolder);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandlingTests {

        @Test
        @DisplayName("getContextPath handles null file gracefully")
        void testGetContextPath_NullFile_HandlesGracefully() {
            // No need to stub since NPE is thrown before dataStore access

            // This should throw NPE as expected
            org.junit.jupiter.api.Assertions.assertThrows(
                    NullPointerException.class,
                    () -> JOptionKeys.getContextPath((File) null, mockDataStore));
        }

        @Test
        @DisplayName("getContextPath handles null string gracefully")
        void testGetContextPath_NullString_HandlesGracefully() {
            // No need to stub since NPE is thrown before dataStore access

            // This should throw NPE as expected
            org.junit.jupiter.api.Assertions.assertThrows(
                    NullPointerException.class,
                    () -> JOptionKeys.getContextPath((String) null, mockDataStore));
        }

        @Test
        @DisplayName("getContextPath handles null dataStore gracefully")
        void testGetContextPath_NullDataStore_HandlesGracefully() {
            File testFile = new File("test.txt");

            // This should throw NPE as expected
            org.junit.jupiter.api.Assertions.assertThrows(
                    NullPointerException.class,
                    () -> JOptionKeys.getContextPath(testFile, null));
        }

        @Test
        @DisplayName("getContextPath handles invalid working folder")
        void testGetContextPath_InvalidWorkingFolder_HandlesGracefully() {
            File testFile = new File("test.txt");
            String invalidFolder = "/this/path/does/not/exist";

            when(mockDataStore.get(JOptionKeys.CURRENT_FOLDER_PATH))
                    .thenReturn(Optional.of(invalidFolder));

            File result = JOptionKeys.getContextPath(testFile, mockDataStore);

            // Should still create a File object, even if path doesn't exist
            assertThat(result.getParent()).isEqualTo(invalidFolder);
            assertThat(result.getName()).isEqualTo("test.txt");
        }

        @Test
        @DisplayName("getContextPath handles empty working folder")
        void testGetContextPath_EmptyWorkingFolder_HandlesGracefully() {
            File testFile = new File("test.txt");

            when(mockDataStore.get(JOptionKeys.CURRENT_FOLDER_PATH))
                    .thenReturn(Optional.of(""));

            File result = JOptionKeys.getContextPath(testFile, mockDataStore);

            // With empty parent folder, File constructor creates "/" as parent on Unix
            // systems
            assertThat(result.getName()).isEqualTo("test.txt");
            // On Unix systems, empty string path becomes "/"
            assertThat(result.getParent()).isEqualTo("/");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Full workflow: set working folder and resolve multiple paths")
        void testFullWorkflow_SetWorkingFolderAndResolveMultiplePaths() {
            String workingFolder = tempDir.getAbsolutePath();

            when(mockDataStore.get(JOptionKeys.CURRENT_FOLDER_PATH))
                    .thenReturn(Optional.of(workingFolder));

            // Test multiple path types
            File relativePath = JOptionKeys.getContextPath("config.xml", mockDataStore);
            File nestedPath = JOptionKeys.getContextPath("data/input.txt", mockDataStore);
            File absolutePath = JOptionKeys.getContextPath(
                    new File(tempDir, "absolute.txt").getAbsolutePath(), mockDataStore);

            // Verify results
            assertThat(relativePath.getParentFile().getAbsolutePath())
                    .isEqualTo(workingFolder);
            assertThat(nestedPath.getPath())
                    .startsWith(workingFolder)
                    .endsWith("data" + File.separator + "input.txt");
            assertThat(absolutePath.getAbsolutePath())
                    .startsWith(tempDir.getAbsolutePath());
        }

        @Test
        @DisplayName("Verify datakey behavior remains consistent")
        void testDataKeyBehavior_RemainsConsistent() {
            // Test multiple calls return same keys
            assertThat(JOptionKeys.CURRENT_FOLDER_PATH)
                    .isSameAs(JOptionKeys.CURRENT_FOLDER_PATH);
            assertThat(JOptionKeys.USE_RELATIVE_PATHS)
                    .isSameAs(JOptionKeys.USE_RELATIVE_PATHS);

            // Test key equality is based on name
            assertThat(JOptionKeys.CURRENT_FOLDER_PATH.getName())
                    .isEqualTo("joptions_current_folder_path");
            assertThat(JOptionKeys.USE_RELATIVE_PATHS.getName())
                    .isEqualTo("joptions_use_relative_paths");
        }
    }
}
