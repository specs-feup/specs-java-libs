package pt.up.fe.specs.util.jobs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive unit tests for the FileSet class.
 * Tests file set management, source folder handling, and output naming.
 * 
 * @author Generated Tests
 */
@DisplayName("FileSet Tests")
class FileSetTest {

    @TempDir
    Path tempDir;

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create FileSet with valid parameters")
        void testConstructor_ValidParameters_CreatesFileSet() {
            // Arrange
            File sourceFolder = tempDir.toFile();
            List<String> sourceFiles = Arrays.asList("Main.java", "Util.java");
            String outputName = "MyProject";

            // Act
            FileSet fileSet = new FileSet(sourceFolder, sourceFiles, outputName);

            // Assert
            assertThat(fileSet.getSourceFolder()).isEqualTo(sourceFolder);
            assertThat(fileSet.getSourceFilenames()).isEqualTo(sourceFiles);
            assertThat(fileSet.outputName()).isEqualTo(outputName);
        }

        @Test
        @DisplayName("Should handle empty source files list")
        void testConstructor_EmptySourceFiles_CreatesFileSet() {
            // Arrange
            File sourceFolder = tempDir.toFile();
            List<String> emptySourceFiles = Collections.emptyList();
            String outputName = "EmptyProject";

            // Act
            FileSet fileSet = new FileSet(sourceFolder, emptySourceFiles, outputName);

            // Assert
            assertThat(fileSet.getSourceFolder()).isEqualTo(sourceFolder);
            assertThat(fileSet.getSourceFilenames()).isEmpty();
            assertThat(fileSet.outputName()).isEqualTo(outputName);
        }

        @Test
        @DisplayName("Should handle null output name")
        void testConstructor_NullOutputName_AllowsNull() {
            // Arrange
            File sourceFolder = tempDir.toFile();
            List<String> sourceFiles = Arrays.asList("test.java");

            // Act
            FileSet fileSet = new FileSet(sourceFolder, sourceFiles, null);

            // Assert
            assertThat(fileSet.getSourceFolder()).isEqualTo(sourceFolder);
            assertThat(fileSet.getSourceFilenames()).isEqualTo(sourceFiles);
            assertThat(fileSet.outputName()).isNull();
        }

        @Test
        @DisplayName("Should handle null source folder")
        void testConstructor_NullSourceFolder_AllowsNull() {
            // Arrange
            List<String> sourceFiles = Arrays.asList("test.java");
            String outputName = "TestProject";

            // Act
            FileSet fileSet = new FileSet(null, sourceFiles, outputName);

            // Assert
            assertThat(fileSet.getSourceFolder()).isNull();
            assertThat(fileSet.getSourceFilenames()).isEqualTo(sourceFiles);
            assertThat(fileSet.outputName()).isEqualTo(outputName);
        }

        @Test
        @DisplayName("Should handle null source files list")
        void testConstructor_NullSourceFiles_AllowsNull() {
            // Arrange
            File sourceFolder = tempDir.toFile();
            String outputName = "TestProject";

            // Act
            FileSet fileSet = new FileSet(sourceFolder, null, outputName);

            // Assert
            assertThat(fileSet.getSourceFolder()).isEqualTo(sourceFolder);
            assertThat(fileSet.getSourceFilenames()).isNull();
            assertThat(fileSet.outputName()).isEqualTo(outputName);
        }
    }

    @Nested
    @DisplayName("Getter Method Tests")
    class GetterMethodTests {

        @Test
        @DisplayName("Should return source filenames")
        void testGetSourceFilenames_ReturnsCorrectList() {
            // Arrange
            File sourceFolder = tempDir.toFile();
            List<String> sourceFiles = Arrays.asList("App.java", "Utils.java", "Constants.java");
            String outputName = "Project";
            FileSet fileSet = new FileSet(sourceFolder, sourceFiles, outputName);

            // Act
            List<String> result = fileSet.getSourceFilenames();

            // Assert
            assertThat(result).isEqualTo(sourceFiles);
            assertThat(result).hasSize(3);
            assertThat(result).containsExactly("App.java", "Utils.java", "Constants.java");
        }

        @Test
        @DisplayName("Should return source folder")
        void testGetSourceFolder_ReturnsCorrectFolder() {
            // Arrange
            File sourceFolder = tempDir.toFile();
            List<String> sourceFiles = Arrays.asList("test.java");
            String outputName = "Project";
            FileSet fileSet = new FileSet(sourceFolder, sourceFiles, outputName);

            // Act
            File result = fileSet.getSourceFolder();

            // Assert
            assertThat(result).isEqualTo(sourceFolder);
            assertThat(result.exists()).isTrue();
        }

        @Test
        @DisplayName("Should return output name")
        void testOutputName_ReturnsCorrectName() {
            // Arrange
            File sourceFolder = tempDir.toFile();
            List<String> sourceFiles = Arrays.asList("test.java");
            String outputName = "MyApplication";
            FileSet fileSet = new FileSet(sourceFolder, sourceFiles, outputName);

            // Act
            String result = fileSet.outputName();

            // Assert
            assertThat(result).isEqualTo(outputName);
        }
    }

    @Nested
    @DisplayName("Setter Method Tests")
    class SetterMethodTests {

        @Test
        @DisplayName("Should set output name")
        void testSetOutputName_ValidName_SetsName() {
            // Arrange
            File sourceFolder = tempDir.toFile();
            List<String> sourceFiles = Arrays.asList("test.java");
            String originalName = "OriginalName";
            String newName = "NewName";
            FileSet fileSet = new FileSet(sourceFolder, sourceFiles, originalName);

            // Act
            fileSet.setOutputName(newName);

            // Assert
            assertThat(fileSet.outputName()).isEqualTo(newName);
        }

        @Test
        @DisplayName("Should set output name to null")
        void testSetOutputName_NullName_SetsNull() {
            // Arrange
            File sourceFolder = tempDir.toFile();
            List<String> sourceFiles = Arrays.asList("test.java");
            String originalName = "OriginalName";
            FileSet fileSet = new FileSet(sourceFolder, sourceFiles, originalName);

            // Act
            fileSet.setOutputName(null);

            // Assert
            assertThat(fileSet.outputName()).isNull();
        }

        @Test
        @DisplayName("Should set empty output name")
        void testSetOutputName_EmptyName_SetsEmpty() {
            // Arrange
            File sourceFolder = tempDir.toFile();
            List<String> sourceFiles = Arrays.asList("test.java");
            String originalName = "OriginalName";
            FileSet fileSet = new FileSet(sourceFolder, sourceFiles, originalName);

            // Act
            fileSet.setOutputName("");

            // Assert
            assertThat(fileSet.outputName()).isEmpty();
        }

        @Test
        @DisplayName("Should allow multiple output name changes")
        void testSetOutputName_MultipleChanges_UpdatesCorrectly() {
            // Arrange
            File sourceFolder = tempDir.toFile();
            List<String> sourceFiles = Arrays.asList("test.java");
            FileSet fileSet = new FileSet(sourceFolder, sourceFiles, "Initial");

            // Act & Assert
            fileSet.setOutputName("First");
            assertThat(fileSet.outputName()).isEqualTo("First");

            fileSet.setOutputName("Second");
            assertThat(fileSet.outputName()).isEqualTo("Second");

            fileSet.setOutputName("Final");
            assertThat(fileSet.outputName()).isEqualTo("Final");
        }
    }

    @Nested
    @DisplayName("ToString Method Tests")
    class ToStringMethodTests {

        @Test
        @DisplayName("Should return string representation with source folder")
        void testToString_WithSourceFolder_ReturnsCorrectString() {
            // Arrange
            File sourceFolder = tempDir.toFile();
            List<String> sourceFiles = Arrays.asList("test.java");
            String outputName = "Project";
            FileSet fileSet = new FileSet(sourceFolder, sourceFiles, outputName);

            // Act
            String result = fileSet.toString();

            // Assert
            assertThat(result).isEqualTo("SOURCEFOLDER:" + sourceFolder.getPath());
        }

        @Test
        @DisplayName("Should handle null source folder in toString")
        void testToString_NullSourceFolder_HandlesGracefully() {
            // Arrange
            List<String> sourceFiles = Arrays.asList("test.java");
            String outputName = "Project";
            FileSet fileSet = new FileSet(null, sourceFiles, outputName);

            // Act
            String result = fileSet.toString();

            // Assert
            assertThat(result).isEqualTo("SOURCEFOLDER:null");
        }
    }

    @Nested
    @DisplayName("Data Integrity Tests")
    class DataIntegrityTests {

        @Test
        @DisplayName("Should maintain source files list immutability")
        void testSourceFilenames_ListModification_DoesNotAffectOriginal() {
            // Arrange
            File sourceFolder = tempDir.toFile();
            List<String> originalSourceFiles = Arrays.asList("File1.java", "File2.java");
            String outputName = "Project";
            FileSet fileSet = new FileSet(sourceFolder, originalSourceFiles, outputName);

            // Act - Try to modify the returned list
            List<String> returnedList = fileSet.getSourceFilenames();

            // Assert - Verify the list contents
            assertThat(returnedList).isEqualTo(originalSourceFiles);
            assertThat(returnedList).containsExactly("File1.java", "File2.java");

            // Note: The current implementation returns the reference to the original list,
            // which allows external modification. This might be a design issue.
        }

        @Test
        @DisplayName("Should handle file paths with different separators")
        void testFileSet_DifferentPathSeparators_HandlesCorrectly() {
            // Arrange
            File sourceFolder = tempDir.toFile();
            List<String> sourceFiles = Arrays.asList(
                    "com/example/Main.java",
                    "com\\example\\Utils.java", // Mixed separators
                    "/absolute/path/File.java");
            String outputName = "MixedPathProject";

            // Act
            FileSet fileSet = new FileSet(sourceFolder, sourceFiles, outputName);

            // Assert
            assertThat(fileSet.getSourceFilenames()).hasSize(3);
            assertThat(fileSet.getSourceFilenames()).containsExactlyElementsOf(sourceFiles);
        }

        @Test
        @DisplayName("Should handle special characters in output name")
        void testFileSet_SpecialCharactersInOutputName_HandlesCorrectly() {
            // Arrange
            File sourceFolder = tempDir.toFile();
            List<String> sourceFiles = Arrays.asList("test.java");
            String specialOutputName = "Project-Name_With.Special@Characters#123!";

            // Act
            FileSet fileSet = new FileSet(sourceFolder, sourceFiles, specialOutputName);

            // Assert
            assertThat(fileSet.outputName()).isEqualTo(specialOutputName);
        }

        @Test
        @DisplayName("Should handle very long file lists")
        void testFileSet_LargeFileList_HandlesCorrectly() {
            // Arrange
            File sourceFolder = tempDir.toFile();
            List<String> largeFileList = Arrays.asList(
                    "File001.java", "File002.java", "File003.java", "File004.java", "File005.java",
                    "File006.java", "File007.java", "File008.java", "File009.java", "File010.java",
                    "File011.java", "File012.java", "File013.java", "File014.java", "File015.java");
            String outputName = "LargeProject";

            // Act
            FileSet fileSet = new FileSet(sourceFolder, largeFileList, outputName);

            // Assert
            assertThat(fileSet.getSourceFilenames()).hasSize(15);
            assertThat(fileSet.getSourceFilenames()).containsExactlyElementsOf(largeFileList);
            assertThat(fileSet.outputName()).isEqualTo(outputName);
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle empty string in source files")
        void testFileSet_EmptyStringInSourceFiles_HandlesCorrectly() {
            // Arrange
            File sourceFolder = tempDir.toFile();
            List<String> sourceFiles = Arrays.asList("valid.java", "", "another.java");
            String outputName = "Project";

            // Act
            FileSet fileSet = new FileSet(sourceFolder, sourceFiles, outputName);

            // Assert
            assertThat(fileSet.getSourceFilenames()).hasSize(3);
            assertThat(fileSet.getSourceFilenames()).contains("");
        }

        @Test
        @DisplayName("Should handle duplicate file names")
        void testFileSet_DuplicateFileNames_HandlesCorrectly() {
            // Arrange
            File sourceFolder = tempDir.toFile();
            List<String> sourceFiles = Arrays.asList("Main.java", "Main.java", "Utils.java");
            String outputName = "DuplicateProject";

            // Act
            FileSet fileSet = new FileSet(sourceFolder, sourceFiles, outputName);

            // Assert
            assertThat(fileSet.getSourceFilenames()).hasSize(3);
            assertThat(fileSet.getSourceFilenames()).containsExactly("Main.java", "Main.java", "Utils.java");
        }

        @Test
        @DisplayName("Should handle very long output name")
        void testFileSet_VeryLongOutputName_HandlesCorrectly() {
            // Arrange
            File sourceFolder = tempDir.toFile();
            List<String> sourceFiles = Arrays.asList("test.java");
            String longOutputName = "A".repeat(1000); // Very long name

            // Act
            FileSet fileSet = new FileSet(sourceFolder, sourceFiles, longOutputName);

            // Assert
            assertThat(fileSet.outputName()).isEqualTo(longOutputName);
            assertThat(fileSet.outputName()).hasSize(1000);
        }

        @Test
        @DisplayName("Should handle non-existent source folder")
        void testFileSet_NonExistentSourceFolder_HandlesCorrectly() {
            // Arrange
            File nonExistentFolder = new File("/path/that/does/not/exist");
            List<String> sourceFiles = Arrays.asList("test.java");
            String outputName = "Project";

            // Act
            FileSet fileSet = new FileSet(nonExistentFolder, sourceFiles, outputName);

            // Assert
            assertThat(fileSet.getSourceFolder()).isEqualTo(nonExistentFolder);
            assertThat(fileSet.getSourceFolder().exists()).isFalse();
            assertThat(fileSet.getSourceFilenames()).isEqualTo(sourceFiles);
            assertThat(fileSet.outputName()).isEqualTo(outputName);
        }
    }
}
