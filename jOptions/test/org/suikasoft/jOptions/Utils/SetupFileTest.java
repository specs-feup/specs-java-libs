package org.suikasoft.jOptions.Utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import pt.up.fe.specs.util.SpecsIo;

/**
 * Test suite for SetupFile functionality.
 * Tests file management, parent folder resolution, and method chaining
 * behavior.
 * 
 * @author Generated Tests
 */
@DisplayName("SetupFile")
class SetupFileTest {

    @TempDir
    Path tempDir;

    private File testFile;
    private File testDir;
    private File workingDir;

    @BeforeEach
    void setUp() throws IOException {
        testDir = tempDir.toFile();
        testFile = new File(testDir, "config.properties");
        Files.createFile(testFile.toPath());

        // Store current working directory for comparison
        workingDir = SpecsIo.getWorkingDir();
    }

    @Nested
    @DisplayName("Constructor and Initialization")
    class ConstructorAndInitializationTests {

        @Test
        @DisplayName("default constructor creates SetupFile with null file")
        void testDefaultConstructor_CreatesSetupFileWithNullFile() {
            SetupFile setupFile = new SetupFile();

            assertThat(setupFile).isNotNull();
            assertThat(setupFile.getFile()).isNull();
        }

        @Test
        @DisplayName("newly created SetupFile returns working directory as parent folder")
        void testNewlyCreatedSetupFile_ReturnsWorkingDirectoryAsParentFolder() {
            SetupFile setupFile = new SetupFile();

            File parentFolder = setupFile.getParentFolder();

            assertThat(parentFolder).isEqualTo(workingDir);
        }
    }

    @Nested
    @DisplayName("File Management")
    class FileManagementTests {

        @Test
        @DisplayName("setFile sets the setup file correctly")
        void testSetFile_SetsTheSetupFileCorrectly() {
            SetupFile setupFile = new SetupFile();

            SetupFile result = setupFile.setFile(testFile);

            assertThat(setupFile.getFile()).isEqualTo(testFile);
            assertThat(result).isSameAs(setupFile); // Method chaining
        }

        @Test
        @DisplayName("setFile handles null file")
        void testSetFile_HandlesNullFile() {
            SetupFile setupFile = new SetupFile();
            setupFile.setFile(testFile);

            SetupFile result = setupFile.setFile(null);

            assertThat(setupFile.getFile()).isNull();
            assertThat(result).isSameAs(setupFile); // Method chaining
        }

        @Test
        @DisplayName("getFile returns the set file")
        void testGetFile_ReturnsTheSetFile() {
            SetupFile setupFile = new SetupFile();
            setupFile.setFile(testFile);

            File result = setupFile.getFile();

            assertThat(result).isEqualTo(testFile);
        }

        @Test
        @DisplayName("getFile returns null when no file is set")
        void testGetFile_ReturnsNullWhenNoFileIsSet() {
            SetupFile setupFile = new SetupFile();

            File result = setupFile.getFile();

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("resetFile sets file back to null")
        void testResetFile_SetsFileBackToNull() {
            SetupFile setupFile = new SetupFile();
            setupFile.setFile(testFile);

            setupFile.resetFile();

            assertThat(setupFile.getFile()).isNull();
        }
    }

    @Nested
    @DisplayName("Parent Folder Resolution")
    class ParentFolderResolutionTests {

        @Test
        @DisplayName("getParentFolder returns file parent when file is set")
        void testGetParentFolder_ReturnsFileParentWhenFileIsSet() {
            SetupFile setupFile = new SetupFile();
            setupFile.setFile(testFile);

            File parentFolder = setupFile.getParentFolder();

            assertThat(parentFolder).isEqualTo(testDir);
        }

        @Test
        @DisplayName("getParentFolder returns working directory when file is null")
        void testGetParentFolder_ReturnsWorkingDirectoryWhenFileIsNull() {
            SetupFile setupFile = new SetupFile();

            File parentFolder = setupFile.getParentFolder();

            assertThat(parentFolder).isEqualTo(workingDir);
        }

        @Test
        @DisplayName("getParentFolder returns working directory when file parent is null")
        void testGetParentFolder_ReturnsWorkingDirectoryWhenFileParentIsNull() throws IOException {
            // Create a file with no parent (relative file name only)
            File relativeFile = new File("config.txt");

            SetupFile setupFile = new SetupFile();
            setupFile.setFile(relativeFile);

            File parentFolder = setupFile.getParentFolder();

            // When file.getParentFile() returns null, should return working directory
            assertThat(parentFolder).isEqualTo(workingDir);
        }

        @Test
        @DisplayName("getParentFolder handles nested directory structure")
        void testGetParentFolder_HandlesNestedDirectoryStructure() throws IOException {
            // Create nested directory structure
            File nestedDir = new File(testDir, "nested");
            nestedDir.mkdir();
            File nestedFile = new File(nestedDir, "nested-config.properties");
            Files.createFile(nestedFile.toPath());

            SetupFile setupFile = new SetupFile();
            setupFile.setFile(nestedFile);

            File parentFolder = setupFile.getParentFolder();

            assertThat(parentFolder).isEqualTo(nestedDir);
        }
    }

    @Nested
    @DisplayName("Method Chaining")
    class MethodChainingTests {

        @Test
        @DisplayName("setFile returns same instance for method chaining")
        void testSetFile_ReturnsSameInstanceForMethodChaining() {
            SetupFile setupFile = new SetupFile();

            SetupFile result = setupFile.setFile(testFile);

            assertThat(result).isSameAs(setupFile);
        }

        @Test
        @DisplayName("multiple setFile calls can be chained")
        void testMultipleSetFileCalls_CanBeChained() {
            SetupFile setupFile = new SetupFile();

            // This should work if setFile returns the same instance
            SetupFile result = setupFile.setFile(testFile).setFile(null).setFile(testFile);

            assertThat(result).isSameAs(setupFile);
            assertThat(setupFile.getFile()).isEqualTo(testFile);
        }
    }

    @Nested
    @DisplayName("Edge Cases and State Transitions")
    class EdgeCasesAndStateTransitionsTests {

        @Test
        @DisplayName("setup file state changes correctly through operations")
        void testSetupFileState_ChangesThroughOperations() {
            SetupFile setupFile = new SetupFile();

            // Initial state
            assertThat(setupFile.getFile()).isNull();
            assertThat(setupFile.getParentFolder()).isEqualTo(workingDir);

            // Set file
            setupFile.setFile(testFile);
            assertThat(setupFile.getFile()).isEqualTo(testFile);
            assertThat(setupFile.getParentFolder()).isEqualTo(testDir);

            // Reset file
            setupFile.resetFile();
            assertThat(setupFile.getFile()).isNull();
            assertThat(setupFile.getParentFolder()).isEqualTo(workingDir);
        }

        @Test
        @DisplayName("handles non-existent file")
        void testHandles_NonExistentFile() {
            File nonExistentFile = new File(testDir, "non-existent.properties");

            SetupFile setupFile = new SetupFile();
            setupFile.setFile(nonExistentFile);

            // Should still work with non-existent files
            assertThat(setupFile.getFile()).isEqualTo(nonExistentFile);
            assertThat(setupFile.getParentFolder()).isEqualTo(testDir);
        }

        @Test
        @DisplayName("handles directory instead of file")
        void testHandles_DirectoryInsteadOfFile() {
            SetupFile setupFile = new SetupFile();
            setupFile.setFile(testDir);

            // Should work with directories too
            assertThat(setupFile.getFile()).isEqualTo(testDir);
            assertThat(setupFile.getParentFolder()).isEqualTo(testDir.getParentFile());
        }

        @Test
        @DisplayName("multiple resetFile calls work correctly")
        void testMultipleResetFileCalls_WorkCorrectly() {
            SetupFile setupFile = new SetupFile();
            setupFile.setFile(testFile);

            setupFile.resetFile();
            setupFile.resetFile(); // Second reset should be safe

            assertThat(setupFile.getFile()).isNull();
            assertThat(setupFile.getParentFolder()).isEqualTo(workingDir);
        }

        @Test
        @DisplayName("works with absolute and relative paths")
        void testWorks_WithAbsoluteAndRelativePaths() {
            SetupFile setupFile = new SetupFile();

            // Test with absolute path
            setupFile.setFile(testFile.getAbsoluteFile());
            assertThat(setupFile.getFile()).isEqualTo(testFile.getAbsoluteFile());
            assertThat(setupFile.getParentFolder()).isEqualTo(testDir.getAbsoluteFile());

            // Test with relative path (if parent is null, returns working dir)
            File relativePath = new File("relative-config.txt");
            setupFile.setFile(relativePath);
            assertThat(setupFile.getFile()).isEqualTo(relativePath);
            assertThat(setupFile.getParentFolder()).isEqualTo(workingDir);
        }
    }

    @Nested
    @DisplayName("Integration with SpecsIo")
    class IntegrationWithSpecsIoTests {

        @Test
        @DisplayName("getParentFolder correctly uses SpecsIo.getWorkingDir")
        void testGetParentFolder_CorrectlyUsesSpecsIoGetWorkingDir() {
            SetupFile setupFile = new SetupFile();

            File parentFolder = setupFile.getParentFolder();
            File specsIoWorkingDir = SpecsIo.getWorkingDir();

            assertThat(parentFolder).isEqualTo(specsIoWorkingDir);
        }

        @Test
        @DisplayName("working directory fallback works consistently")
        void testWorkingDirectoryFallback_WorksConsistently() {
            SetupFile setupFile1 = new SetupFile();
            SetupFile setupFile2 = new SetupFile();

            // Both should return the same working directory
            assertThat(setupFile1.getParentFolder()).isEqualTo(setupFile2.getParentFolder());
            assertThat(setupFile1.getParentFolder()).isEqualTo(SpecsIo.getWorkingDir());
        }
    }
}
