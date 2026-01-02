package pt.up.fe.specs.util.jobs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for InputMode enum.
 * Tests enum values, program extraction methods, and folder classification.
 * 
 * @author Generated Tests
 */
@DisplayName("InputMode Tests")
public class InputModeTest {

    @TempDir
    Path tempDir;

    private File sourceFile;
    private File sourceFolder;
    private List<String> extensions;

    @BeforeEach
    void setUp() throws IOException {
        sourceFile = tempDir.resolve("test.c").toFile();
        sourceFile.createNewFile();

        sourceFolder = tempDir.resolve("testFolder").toFile();
        sourceFolder.mkdirs();

        extensions = Arrays.asList("c", "java");
    }

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @Test
        @DisplayName("Should have all expected enum values")
        void testEnumValues_AllExpectedValues_Present() {
            // Act
            InputMode[] values = InputMode.values();

            // Assert
            assertThat(values).hasSize(4);
            assertThat(values).containsExactlyInAnyOrder(
                    InputMode.files,
                    InputMode.folders,
                    InputMode.singleFile,
                    InputMode.singleFolder);
        }

        @Test
        @DisplayName("Should support valueOf for all enum constants")
        void testValueOf_AllConstants_ReturnsCorrectEnums() {
            // Act & Assert
            assertThat(InputMode.valueOf("files")).isEqualTo(InputMode.files);
            assertThat(InputMode.valueOf("folders")).isEqualTo(InputMode.folders);
            assertThat(InputMode.valueOf("singleFile")).isEqualTo(InputMode.singleFile);
            assertThat(InputMode.valueOf("singleFolder")).isEqualTo(InputMode.singleFolder);
        }

        @Test
        @DisplayName("Should throw exception for invalid valueOf")
        void testValueOf_InvalidName_ThrowsException() {
            // Act & Assert
            assertThatThrownBy(() -> InputMode.valueOf("invalid"))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("isFolder Method Tests")
    class IsFolderTests {

        @Test
        @DisplayName("Should return false for singleFile mode")
        void testIsFolder_SingleFile_ReturnsFalse() {
            // Act & Assert
            assertThat(InputMode.singleFile.isFolder()).isFalse();
        }

        @Test
        @DisplayName("Should return true for folder-based modes")
        void testIsFolder_FolderBasedModes_ReturnsTrue() {
            // Act & Assert
            assertThat(InputMode.files.isFolder()).isTrue();
            assertThat(InputMode.folders.isFolder()).isTrue();
            assertThat(InputMode.singleFolder.isFolder()).isTrue();
        }
    }

    @Nested
    @DisplayName("getPrograms Method Tests")
    class GetProgramsTests {

        @Test
        @DisplayName("Should delegate to JobUtils.getSourcesFilesMode for files mode")
        void testGetPrograms_FilesMode_DelegatesToJobUtils() {
            // Act
            List<FileSet> result = InputMode.files.getPrograms(sourceFolder, extensions, null);

            // Assert - Should not throw and return a list (even if empty)
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Should delegate to JobUtils.getSourcesFoldersMode for folders mode")
        void testGetPrograms_FoldersMode_DelegatesToJobUtils() {
            // Act
            List<FileSet> result = InputMode.folders.getPrograms(sourceFolder, extensions, 1);

            // Assert - Should not throw and return a list
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Should delegate to JobUtils.getSourcesSingleFileMode for singleFile mode")
        void testGetPrograms_SingleFileMode_DelegatesToJobUtils() {
            // Act
            List<FileSet> result = InputMode.singleFile.getPrograms(sourceFile, extensions, null);

            // Assert - Should not throw and return a list
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Should delegate to JobUtils.getSourcesSingleFolderMode for singleFolder mode")
        void testGetPrograms_SingleFolderMode_DelegatesToJobUtils() {
            // Act
            List<FileSet> result = InputMode.singleFolder.getPrograms(sourceFolder, extensions, null);

            // Assert - Should not throw and return a list
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Should handle null extensions based on mode behavior")
        void testGetPrograms_NullExtensions_HandlesGracefully() {
            // Act & Assert - Different modes have different null handling behavior
            // singleFile mode doesn't use extensions parameter, so no exception
            assertThatCode(() -> InputMode.singleFile.getPrograms(sourceFile, null, null))
                    .doesNotThrowAnyException();

            // Other modes properly validate null extensions with clear error messages
            assertThatThrownBy(() -> InputMode.files.getPrograms(sourceFolder, null, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Extensions collection cannot be null");
        }

        @Test
        @DisplayName("Should handle empty extensions gracefully")
        void testGetPrograms_EmptyExtensions_HandlesGracefully() {
            // Act & Assert - Should not throw
            assertThatCode(() -> {
                List<String> emptyExtensions = Collections.emptyList();
                InputMode.files.getPrograms(sourceFolder, emptyExtensions, null);
                InputMode.folders.getPrograms(sourceFolder, emptyExtensions, 1);
                InputMode.singleFile.getPrograms(sourceFile, emptyExtensions, null);
                InputMode.singleFolder.getPrograms(sourceFolder, emptyExtensions, null);
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with real file structure")
        void testInputMode_WithRealFileStructure_WorksCorrectly() throws IOException {
            // Arrange - Create a more complex file structure
            File subFolder = new File(sourceFolder, "subfolder");
            subFolder.mkdirs();

            File cFile = new File(sourceFolder, "test.c");
            File javaFile = new File(sourceFolder, "Test.java");
            File subCFile = new File(subFolder, "sub.c");

            cFile.createNewFile();
            javaFile.createNewFile();
            subCFile.createNewFile();

            // Act & Assert - All modes should work without throwing
            assertThatCode(() -> {
                InputMode.files.getPrograms(sourceFolder, extensions, null);
                InputMode.folders.getPrograms(sourceFolder, extensions, 1);
                InputMode.singleFile.getPrograms(cFile, extensions, null);
                InputMode.singleFolder.getPrograms(sourceFolder, extensions, null);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should maintain consistent behavior across multiple calls")
        void testInputMode_MultipleCalls_ConsistentBehavior() {
            // Act
            List<FileSet> result1 = InputMode.files.getPrograms(sourceFolder, extensions, null);
            List<FileSet> result2 = InputMode.files.getPrograms(sourceFolder, extensions, null);

            // Assert - Results should be consistent
            assertThat(result1).isEqualTo(result2);
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle non-existent file paths")
        void testGetPrograms_NonExistentPath_HandlesGracefully() {
            // Arrange
            File nonExistentFile = new File(tempDir.toFile(), "nonexistent.txt");

            // Act & Assert - Should not crash
            assertThatCode(() -> {
                InputMode.files.getPrograms(nonExistentFile, extensions, null);
                InputMode.singleFile.getPrograms(nonExistentFile, extensions, null);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle folder level parameter variations")
        void testGetPrograms_VariousFolderLevels_HandlesCorrectly() {
            // Act & Assert - Should handle different folder levels
            assertThatCode(() -> {
                InputMode.folders.getPrograms(sourceFolder, extensions, 0);
                InputMode.folders.getPrograms(sourceFolder, extensions, 1);
                InputMode.folders.getPrograms(sourceFolder, extensions, 5);
            }).doesNotThrowAnyException();

            // Null folder level throws proper IllegalArgumentException for folders mode
            assertThatThrownBy(() -> InputMode.folders.getPrograms(sourceFolder, extensions, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("FolderLevel cannot be null for folders mode");
        }
    }

    @Nested
    @DisplayName("Documentation Validation")
    class DocumentationValidationTests {

        @Test
        @DisplayName("Should match documented behavior patterns")
        void testInputMode_DocumentedBehavior_MatchesImplementation() {
            // Based on the class documentation:
            // files: Each .c file inside the source folder is a program
            // folders: Each folder inside the source folder is a program
            // singleFile: the source folder is interpreted as a single file
            // singleFolder: The files inside the source folder is a program

            // Act & Assert - Verify the behavior aligns with documentation
            assertThat(InputMode.files.isFolder()).isTrue(); // Works with folders
            assertThat(InputMode.folders.isFolder()).isTrue(); // Works with folders
            assertThat(InputMode.singleFile.isFolder()).isFalse(); // Works with files
            assertThat(InputMode.singleFolder.isFolder()).isTrue(); // Works with folders

            // Verify getPrograms delegates correctly (tested through no exceptions)
            assertThatCode(() -> {
                InputMode.files.getPrograms(sourceFolder, Arrays.asList("c"), null);
                InputMode.folders.getPrograms(sourceFolder, Arrays.asList("c"), 1);
                InputMode.singleFile.getPrograms(sourceFile, Arrays.asList("c"), null);
                InputMode.singleFolder.getPrograms(sourceFolder, Arrays.asList("c"), null);
            }).doesNotThrowAnyException();
        }
    }
}
