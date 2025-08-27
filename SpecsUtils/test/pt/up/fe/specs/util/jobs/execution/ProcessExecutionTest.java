package pt.up.fe.specs.util.jobs.execution;

import org.junit.jupiter.api.BeforeEach;
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
 * Comprehensive test suite for ProcessExecution class.
 * Tests process execution functionality, command handling, and error scenarios.
 * 
 * @author Generated Tests
 */
@DisplayName("ProcessExecution Tests")
public class ProcessExecutionTest {

    @TempDir
    Path tempDir;

    private List<String> validCommand;
    private String workingDirectory;

    @BeforeEach
    void setUp() {
        // Use a simple command that should work on most systems
        validCommand = Arrays.asList("echo", "test");
        workingDirectory = tempDir.toString();
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create ProcessExecution with valid parameters")
        void testConstructor_ValidParameters_CreatesInstance() {
            // Act
            ProcessExecution execution = new ProcessExecution(validCommand, workingDirectory);

            // Assert
            assertThat(execution).isNotNull();
            assertThat(execution.isInterrupted()).isFalse();
            assertThat(execution.getDescription()).contains("echo");
        }

        @Test
        @DisplayName("Should handle empty command list")
        void testConstructor_EmptyCommand_CreatesInstance() {
            // Arrange
            List<String> emptyCommand = Collections.emptyList();

            // Act
            ProcessExecution execution = new ProcessExecution(emptyCommand, workingDirectory);

            // Assert
            assertThat(execution).isNotNull();
            assertThat(execution.isInterrupted()).isFalse();
        }

        @Test
        @DisplayName("Should handle null working directory")
        void testConstructor_NullWorkingDirectory_CreatesInstance() {
            // Act
            ProcessExecution execution = new ProcessExecution(validCommand, null);

            // Assert
            assertThat(execution).isNotNull();
            assertThat(execution.isInterrupted()).isFalse();
        }
    }

    @Nested
    @DisplayName("Execution Interface Implementation Tests")
    class ExecutionInterfaceTests {

        @Test
        @DisplayName("Should implement Execution interface correctly")
        void testProcessExecution_ExecutionInterface_ImplementedCorrectly() {
            // Arrange
            ProcessExecution execution = new ProcessExecution(validCommand, workingDirectory);

            // Act & Assert
            assertThat(execution).isInstanceOf(Execution.class);

            // Verify all interface methods are implemented
            assertThatCode(() -> {
                execution.run();
                execution.isInterrupted();
                execution.getDescription();
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should return consistent interrupted state")
        void testProcessExecution_InterruptedState_Consistent() {
            // Arrange
            ProcessExecution execution = new ProcessExecution(validCommand, workingDirectory);

            // Act
            boolean interrupted1 = execution.isInterrupted();
            boolean interrupted2 = execution.isInterrupted();

            // Assert
            assertThat(interrupted1).isEqualTo(interrupted2);
            assertThat(interrupted1).isFalse(); // Should start as false
        }

        @Test
        @DisplayName("Should provide meaningful description")
        void testProcessExecution_Description_Meaningful() {
            // Arrange
            ProcessExecution execution = new ProcessExecution(validCommand, workingDirectory);

            // Act
            String description = execution.getDescription();

            // Assert
            assertThat(description).isNotNull();
            assertThat(description).isNotEmpty();
            assertThat(description).contains("echo"); // Should contain command name
        }
    }

    @Nested
    @DisplayName("Command String Tests")
    class CommandStringTests {

        @Test
        @DisplayName("Should generate correct command string for single command")
        void testGetCommandString_SingleCommand_GeneratesCorrectly() {
            // Arrange
            List<String> singleCommand = Collections.singletonList("ls");
            ProcessExecution execution = new ProcessExecution(singleCommand, workingDirectory);

            // Act
            String commandString = execution.getCommandString();

            // Assert
            assertThat(commandString).isEqualTo("ls");
        }

        @Test
        @DisplayName("Should generate correct command string for multiple arguments")
        void testGetCommandString_MultipleArgs_GeneratesCorrectly() {
            // Arrange
            List<String> multiCommand = Arrays.asList("ls", "-la", "/tmp");
            ProcessExecution execution = new ProcessExecution(multiCommand, workingDirectory);

            // Act
            String commandString = execution.getCommandString();

            // Assert
            assertThat(commandString).isEqualTo("ls -la /tmp");
        }

        @Test
        @DisplayName("Should handle empty command gracefully")
        void testGetCommandString_EmptyCommand_HandlesGracefully() {
            // Arrange
            List<String> emptyCommand = Collections.emptyList();
            ProcessExecution execution = new ProcessExecution(emptyCommand, workingDirectory);

            // Act
            String commandString = execution.getCommandString();

            // Assert
            assertThat(commandString).isEmpty();
        }

        @Test
        @DisplayName("Should handle commands with spaces")
        void testGetCommandString_CommandsWithSpaces_HandlesCorrectly() {
            // Arrange
            List<String> spacedCommand = Arrays.asList("java", "-cp", "/path with spaces", "Main");
            ProcessExecution execution = new ProcessExecution(spacedCommand, workingDirectory);

            // Act
            String commandString = execution.getCommandString();

            // Assert
            assertThat(commandString).isEqualTo("java -cp /path with spaces Main");
        }
    }

    @Nested
    @DisplayName("toString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should return same as getCommandString")
        void testToString_SameAsGetCommandString_Consistent() {
            // Arrange
            ProcessExecution execution = new ProcessExecution(validCommand, workingDirectory);

            // Act
            String toString = execution.toString();
            String commandString = execution.getCommandString();

            // Assert
            assertThat(toString).isEqualTo(commandString);
        }

        @Test
        @DisplayName("Should provide meaningful string representation")
        void testToString_MeaningfulRepresentation_Provided() {
            // Arrange
            ProcessExecution execution = new ProcessExecution(validCommand, workingDirectory);

            // Act
            String toString = execution.toString();

            // Assert
            assertThat(toString).isNotNull();
            assertThat(toString).contains("echo");
            assertThat(toString).contains("test");
        }
    }

    @Nested
    @DisplayName("Run Method Tests")
    class RunMethodTests {

        @Test
        @DisplayName("Should execute simple command successfully")
        void testRun_SimpleCommand_ExecutesSuccessfully() {
            // Arrange
            ProcessExecution execution = new ProcessExecution(validCommand, workingDirectory);

            // Act
            int result = execution.run();

            // Assert
            // echo command should succeed (return 0 on most systems)
            assertThat(result).isEqualTo(0);
        }

        @Test
        @DisplayName("Should handle non-existent command")
        void testRun_NonExistentCommand_HandlesFailure() {
            // Arrange
            List<String> invalidCommand = Collections.singletonList("nonexistentcommand12345");
            ProcessExecution execution = new ProcessExecution(invalidCommand, workingDirectory);

            // Act
            int result = execution.run();

            // Assert
            // Non-existent command should return non-zero exit code
            assertThat(result).isNotEqualTo(0);
        }

        @Test
        @DisplayName("Should use working directory correctly")
        void testRun_WithWorkingDirectory_UsesCorrectDirectory() {
            // Arrange
            // Create a test file in the temp directory
            File testFile = tempDir.resolve("testfile.txt").toFile();
            try {
                testFile.createNewFile();
            } catch (Exception e) {
                fail("Failed to create test file");
            }

            // Command to list files (should see our test file)
            List<String> lsCommand = Arrays.asList("ls", testFile.getName());
            ProcessExecution execution = new ProcessExecution(lsCommand, workingDirectory);

            // Act
            int result = execution.run();

            // Assert
            // ls should succeed if working directory is set correctly
            assertThat(result).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("Description Tests")
    class DescriptionTests {

        @Test
        @DisplayName("Should generate description from first command argument")
        void testGetDescription_FirstArgument_GeneratesCorrectly() {
            // Arrange
            List<String> command = Arrays.asList("gcc", "-o", "output", "input.c");
            ProcessExecution execution = new ProcessExecution(command, workingDirectory);

            // Act
            String description = execution.getDescription();

            // Assert
            assertThat(description).contains("gcc");
            assertThat(description).startsWith("Run '");
            assertThat(description).endsWith("'");
        }

        @Test
        @DisplayName("Should handle single command in description")
        void testGetDescription_SingleCommand_HandlesCorrectly() {
            // Arrange
            List<String> singleCommand = Collections.singletonList("make");
            ProcessExecution execution = new ProcessExecution(singleCommand, workingDirectory);

            // Act
            String description = execution.getDescription();

            // Assert
            assertThat(description).isEqualTo("Run 'make'");
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle null command list gracefully")
        void testProcessExecution_NullCommandList_HandlesGracefully() {
            // Act & Assert - Constructor should handle null
            assertThatCode(() -> new ProcessExecution(null, workingDirectory))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle empty working directory string")
        void testProcessExecution_EmptyWorkingDirectory_HandlesGracefully() {
            // Act
            ProcessExecution execution = new ProcessExecution(validCommand, "");

            // Assert
            assertThat(execution).isNotNull();
            assertThat(execution.isInterrupted()).isFalse();
        }

        @Test
        @DisplayName("Should maintain interrupted state correctly")
        void testProcessExecution_InterruptedState_MaintainedCorrectly() {
            // Arrange
            ProcessExecution execution = new ProcessExecution(validCommand, workingDirectory);

            // Act - Multiple calls should return same value
            boolean initial = execution.isInterrupted();
            execution.run(); // Run shouldn't change interrupted state for successful command
            boolean afterRun = execution.isInterrupted();

            // Assert
            assertThat(initial).isEqualTo(afterRun);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with Job class pattern")
        void testProcessExecution_JobPattern_WorksCorrectly() {
            // Arrange
            ProcessExecution execution = new ProcessExecution(validCommand, workingDirectory);

            // Act - Simulate Job.run() usage pattern
            String description = execution.getDescription();
            int result = execution.run();
            boolean interrupted = execution.isInterrupted();

            // Assert
            assertThat(description).isNotNull().isNotEmpty();
            assertThat(result).isNotNull();
            assertThat(interrupted).isFalse(); // Should not be interrupted for successful command
        }

        @Test
        @DisplayName("Should maintain state across multiple method calls")
        void testProcessExecution_MultipleMethodCalls_ConsistentState() {
            // Arrange
            ProcessExecution execution = new ProcessExecution(validCommand, workingDirectory);

            // Act - Call methods multiple times
            String desc1 = execution.getDescription();
            String desc2 = execution.getDescription();
            String cmd1 = execution.getCommandString();
            String cmd2 = execution.getCommandString();
            boolean int1 = execution.isInterrupted();
            boolean int2 = execution.isInterrupted();

            // Assert - Should be consistent
            assertThat(desc1).isEqualTo(desc2);
            assertThat(cmd1).isEqualTo(cmd2);
            assertThat(int1).isEqualTo(int2);
        }
    }
}
