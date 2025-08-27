package pt.up.fe.specs.util.jobs.execution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for JavaExecution class.
 * Tests Java runnable execution, exception handling, and state management.
 * 
 * @author Generated Tests
 */
@DisplayName("JavaExecution Tests")
public class JavaExecutionTest {

    private Runnable successRunnable;
    private Runnable failureRunnable;
    private AtomicBoolean executionFlag;
    private AtomicInteger executionCounter;

    @BeforeEach
    void setUp() {
        executionFlag = new AtomicBoolean(false);
        executionCounter = new AtomicInteger(0);

        successRunnable = () -> {
            executionFlag.set(true);
            executionCounter.incrementAndGet();
        };

        failureRunnable = () -> {
            executionCounter.incrementAndGet();
            throw new RuntimeException("Test exception");
        };
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create JavaExecution with valid runnable")
        void testConstructor_ValidRunnable_CreatesInstance() {
            // Act
            JavaExecution execution = new JavaExecution(successRunnable);

            // Assert
            assertThat(execution).isNotNull();
            assertThat(execution.isInterrupted()).isFalse();
            assertThat(execution.getDescription()).isEqualTo("Java Execution");
        }

        @Test
        @DisplayName("Should handle null runnable")
        void testConstructor_NullRunnable_CreatesInstance() {
            // Act
            JavaExecution execution = new JavaExecution(null);

            // Assert
            assertThat(execution).isNotNull();
            assertThat(execution.isInterrupted()).isFalse();
            assertThat(execution.getDescription()).isEqualTo("Java Execution");
        }

        @Test
        @DisplayName("Should initialize with default values")
        void testConstructor_DefaultValues_InitializedCorrectly() {
            // Act
            JavaExecution execution = new JavaExecution(successRunnable);

            // Assert
            assertThat(execution.isInterrupted()).isFalse();
            assertThat(execution.getDescription()).isEqualTo("Java Execution");
        }
    }

    @Nested
    @DisplayName("Execution Interface Implementation Tests")
    class ExecutionInterfaceTests {

        @Test
        @DisplayName("Should implement Execution interface correctly")
        void testJavaExecution_ExecutionInterface_ImplementedCorrectly() {
            // Arrange
            JavaExecution execution = new JavaExecution(successRunnable);

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
        @DisplayName("Should return 0 for successful execution")
        void testRun_SuccessfulExecution_ReturnsZero() {
            // Arrange
            JavaExecution execution = new JavaExecution(successRunnable);

            // Act
            int result = execution.run();

            // Assert
            assertThat(result).isEqualTo(0);
            assertThat(execution.isInterrupted()).isFalse();
            assertThat(executionFlag.get()).isTrue();
        }

        @Test
        @DisplayName("Should return -1 for failed execution")
        void testRun_FailedExecution_ReturnsMinusOne() {
            // Arrange
            JavaExecution execution = new JavaExecution(failureRunnable);

            // Act
            int result = execution.run();

            // Assert
            assertThat(result).isEqualTo(-1);
            assertThat(execution.isInterrupted()).isTrue();
            assertThat(executionCounter.get()).isEqualTo(1); // Should have attempted execution
        }
    }

    @Nested
    @DisplayName("Exception Handling Tests")
    class ExceptionHandlingTests {

        @Test
        @DisplayName("Should handle RuntimeException and set interrupted flag")
        void testRun_RuntimeException_SetsInterruptedFlag() {
            // Arrange
            JavaExecution execution = new JavaExecution(failureRunnable);

            // Act
            int result = execution.run();

            // Assert
            assertThat(result).isEqualTo(-1);
            assertThat(execution.isInterrupted()).isTrue();
        }

        @Test
        @DisplayName("Should handle checked exceptions wrapped in RuntimeException")
        void testRun_CheckedException_HandledCorrectly() {
            // Arrange
            Runnable checkedException = () -> {
                throw new RuntimeException(new IllegalStateException("Checked exception test"));
            };
            JavaExecution execution = new JavaExecution(checkedException);

            // Act
            int result = execution.run();

            // Assert
            assertThat(result).isEqualTo(-1);
            assertThat(execution.isInterrupted()).isTrue();
        }

        @Test
        @DisplayName("Should handle null pointer exceptions")
        void testRun_NullPointerException_HandledCorrectly() {
            // Arrange
            Runnable nullPointerRunnable = () -> {
                String str = null;
                @SuppressWarnings({ "null", "unused" })
                int length = str.length(); // Will throw NPE
            };
            JavaExecution execution = new JavaExecution(nullPointerRunnable);

            // Act
            int result = execution.run();

            // Assert
            assertThat(result).isEqualTo(-1);
            assertThat(execution.isInterrupted()).isTrue();
        }

        @Test
        @DisplayName("Should handle null runnable gracefully")
        void testRun_NullRunnable_HandledGracefully() {
            // Arrange
            JavaExecution execution = new JavaExecution(null);

            // Act
            int result = execution.run();

            // Assert
            assertThat(result).isEqualTo(-1);
            assertThat(execution.isInterrupted()).isTrue();
        }
    }

    @Nested
    @DisplayName("Description Tests")
    class DescriptionTests {

        @Test
        @DisplayName("Should return default description when none set")
        void testGetDescription_NoDescriptionSet_ReturnsDefault() {
            // Arrange
            JavaExecution execution = new JavaExecution(successRunnable);

            // Act
            String description = execution.getDescription();

            // Assert
            assertThat(description).isEqualTo("Java Execution");
        }

        @Test
        @DisplayName("Should return custom description when set")
        void testGetDescription_CustomDescriptionSet_ReturnsCustom() {
            // Arrange
            JavaExecution execution = new JavaExecution(successRunnable);
            String customDescription = "Custom Java Task";

            // Act
            execution.setDescription(customDescription);
            String description = execution.getDescription();

            // Assert
            assertThat(description).isEqualTo(customDescription);
        }

        @Test
        @DisplayName("Should handle null description gracefully")
        void testSetDescription_NullDescription_HandledGracefully() {
            // Arrange
            JavaExecution execution = new JavaExecution(successRunnable);

            // Act
            execution.setDescription(null);
            String description = execution.getDescription();

            // Assert
            assertThat(description).isEqualTo("Java Execution"); // Should revert to default
        }

        @Test
        @DisplayName("Should handle empty description")
        void testSetDescription_EmptyDescription_HandledCorrectly() {
            // Arrange
            JavaExecution execution = new JavaExecution(successRunnable);

            // Act
            execution.setDescription("");
            String description = execution.getDescription();

            // Assert
            assertThat(description).isEmpty();
        }

        @Test
        @DisplayName("Should allow description changes")
        void testSetDescription_MultipleChanges_AllowsChanges() {
            // Arrange
            JavaExecution execution = new JavaExecution(successRunnable);

            // Act
            execution.setDescription("First Description");
            String first = execution.getDescription();

            execution.setDescription("Second Description");
            String second = execution.getDescription();

            // Assert
            assertThat(first).isEqualTo("First Description");
            assertThat(second).isEqualTo("Second Description");
        }
    }

    @Nested
    @DisplayName("State Management Tests")
    class StateManagementTests {

        @Test
        @DisplayName("Should maintain interrupted state correctly")
        void testInterruptedState_MultipleAccesses_ConsistentState() {
            // Arrange
            JavaExecution execution = new JavaExecution(failureRunnable);

            // Act
            execution.run(); // This should set interrupted = true
            boolean interrupted1 = execution.isInterrupted();
            boolean interrupted2 = execution.isInterrupted();

            // Assert
            assertThat(interrupted1).isTrue();
            assertThat(interrupted2).isTrue();
            assertThat(interrupted1).isEqualTo(interrupted2);
        }

        @Test
        @DisplayName("Should start with non-interrupted state")
        void testInterruptedState_InitialState_NotInterrupted() {
            // Arrange
            JavaExecution execution = new JavaExecution(successRunnable);

            // Act
            boolean interrupted = execution.isInterrupted();

            // Assert
            assertThat(interrupted).isFalse();
        }

        @Test
        @DisplayName("Should not reset interrupted state on successful subsequent runs")
        void testInterruptedState_SubsequentRuns_StatePreserved() {
            // Arrange
            JavaExecution execution = new JavaExecution(failureRunnable);

            // Act
            execution.run(); // This fails and sets interrupted = true
            boolean afterFailure = execution.isInterrupted();

            // Change runnable to success case
            JavaExecution successExecution = new JavaExecution(successRunnable);
            successExecution.run();
            boolean afterSuccess = successExecution.isInterrupted();

            // Assert
            assertThat(afterFailure).isTrue();
            assertThat(afterSuccess).isFalse(); // Different instance
        }
    }

    @Nested
    @DisplayName("Runnable Execution Tests")
    class RunnableExecutionTests {

        @Test
        @DisplayName("Should execute runnable exactly once")
        void testRun_ExecutionCount_ExactlyOnce() {
            // Arrange
            JavaExecution execution = new JavaExecution(successRunnable);

            // Act
            execution.run();

            // Assert
            assertThat(executionCounter.get()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should execute runnable multiple times when run called multiple times")
        void testRun_MultipleCalls_ExecutesMultipleTimes() {
            // Arrange
            JavaExecution execution = new JavaExecution(successRunnable);

            // Act
            execution.run();
            execution.run();
            execution.run();

            // Assert
            assertThat(executionCounter.get()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should handle complex runnable operations")
        void testRun_ComplexRunnable_ExecutesCorrectly() {
            // Arrange
            AtomicInteger accumulator = new AtomicInteger(0);
            Runnable complexRunnable = () -> {
                for (int i = 0; i < 10; i++) {
                    accumulator.addAndGet(i);
                }
            };
            JavaExecution execution = new JavaExecution(complexRunnable);

            // Act
            int result = execution.run();

            // Assert
            assertThat(result).isEqualTo(0);
            assertThat(execution.isInterrupted()).isFalse();
            assertThat(accumulator.get()).isEqualTo(45); // Sum of 0..9
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with Job class pattern")
        void testJavaExecution_JobPattern_WorksCorrectly() {
            // Arrange
            JavaExecution execution = new JavaExecution(successRunnable);
            execution.setDescription("Test Job Execution");

            // Act - Simulate Job.run() usage pattern
            String description = execution.getDescription();
            int result = execution.run();
            boolean interrupted = execution.isInterrupted();

            // Assert
            assertThat(description).isEqualTo("Test Job Execution");
            assertThat(result).isEqualTo(0);
            assertThat(interrupted).isFalse();
        }

        @Test
        @DisplayName("Should work with Job class failure pattern")
        void testJavaExecution_JobFailurePattern_WorksCorrectly() {
            // Arrange
            JavaExecution execution = new JavaExecution(failureRunnable);
            execution.setDescription("Failing Job Execution");

            // Act - Simulate Job.run() usage pattern with failure
            String description = execution.getDescription();
            int result = execution.run();
            boolean interrupted = execution.isInterrupted();

            // Assert
            assertThat(description).isEqualTo("Failing Job Execution");
            assertThat(result).isEqualTo(-1);
            assertThat(interrupted).isTrue();
        }

        @Test
        @DisplayName("Should maintain consistency across multiple operations")
        void testJavaExecution_MultipleOperations_MaintainsConsistency() {
            // Arrange
            JavaExecution execution = new JavaExecution(successRunnable);
            execution.setDescription("Consistent Execution");

            // Act - Multiple operations
            String desc1 = execution.getDescription();
            int result1 = execution.run();
            boolean int1 = execution.isInterrupted();

            String desc2 = execution.getDescription();
            int result2 = execution.run();
            boolean int2 = execution.isInterrupted();

            // Assert - Should be consistent
            assertThat(desc1).isEqualTo(desc2);
            assertThat(result1).isEqualTo(result2);
            assertThat(int1).isEqualTo(int2);
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle runnable that does nothing")
        void testRun_EmptyRunnable_HandlesCorrectly() {
            // Arrange
            Runnable emptyRunnable = () -> {
            }; // Does nothing
            JavaExecution execution = new JavaExecution(emptyRunnable);

            // Act
            int result = execution.run();

            // Assert
            assertThat(result).isEqualTo(0);
            assertThat(execution.isInterrupted()).isFalse();
        }

        @Test
        @DisplayName("Should handle runnable with very long description")
        void testDescription_VeryLongDescription_HandledCorrectly() {
            // Arrange
            JavaExecution execution = new JavaExecution(successRunnable);
            String longDescription = "This is a very long description ".repeat(100);

            // Act
            execution.setDescription(longDescription);
            String description = execution.getDescription();

            // Assert
            assertThat(description).isEqualTo(longDescription);
        }

        @Test
        @DisplayName("Should handle special characters in description")
        void testDescription_SpecialCharacters_HandledCorrectly() {
            // Arrange
            JavaExecution execution = new JavaExecution(successRunnable);
            String specialDescription = "Description with special chars: áéíóú ñç @#$%^&*()";

            // Act
            execution.setDescription(specialDescription);
            String description = execution.getDescription();

            // Assert
            assertThat(description).isEqualTo(specialDescription);
        }
    }
}
