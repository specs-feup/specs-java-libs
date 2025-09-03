package pt.up.fe.specs.util.jobs.execution;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for Execution interface.
 * Tests interface contract and expected behavior patterns.
 * 
 * @author Generated Tests
 */
@DisplayName("Execution Interface Tests")
public class ExecutionTest {

    @Nested
    @DisplayName("Interface Contract Tests")
    class InterfaceContractTests {

        @Test
        @DisplayName("Should be a functional interface")
        void testExecution_InterfaceStructure_CorrectDefinition() {
            // Act & Assert - Verify interface has expected methods
            assertThat(Execution.class.isInterface()).isTrue();
            assertThat(Execution.class.getMethods()).hasSize(3);

            // Check method signatures exist
            assertThatCode(() -> {
                Execution.class.getMethod("run");
                Execution.class.getMethod("isInterrupted");
                Execution.class.getMethod("getDescription");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should have correct method return types")
        void testExecution_MethodReturnTypes_AreCorrect() throws NoSuchMethodException {
            // Act & Assert
            assertThat(Execution.class.getMethod("run").getReturnType()).isEqualTo(int.class);
            assertThat(Execution.class.getMethod("isInterrupted").getReturnType()).isEqualTo(boolean.class);
            assertThat(Execution.class.getMethod("getDescription").getReturnType()).isEqualTo(String.class);
        }

        @Test
        @DisplayName("Should have methods with correct parameter counts")
        void testExecution_MethodParameters_AreCorrect() throws NoSuchMethodException {
            // Act & Assert
            assertThat(Execution.class.getMethod("run").getParameterCount()).isEqualTo(0);
            assertThat(Execution.class.getMethod("isInterrupted").getParameterCount()).isEqualTo(0);
            assertThat(Execution.class.getMethod("getDescription").getParameterCount()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("Implementation Contract Tests")
    class ImplementationContractTests {

        @Test
        @DisplayName("Should work with lambda implementation")
        void testExecution_LambdaImplementation_WorksCorrectly() {
            // Arrange
            Execution execution = new Execution() {
                @Override
                public int run() {
                    return 0;
                }

                @Override
                public boolean isInterrupted() {
                    return false;
                }

                @Override
                public String getDescription() {
                    return "Test execution";
                }
            };

            // Act
            int result = execution.run();
            boolean interrupted = execution.isInterrupted();
            String description = execution.getDescription();

            // Assert
            assertThat(result).isEqualTo(0);
            assertThat(interrupted).isFalse();
            assertThat(description).isEqualTo("Test execution");
        }

        @Test
        @DisplayName("Should support different return values")
        void testExecution_DifferentReturnValues_AllSupported() {
            // Arrange
            Execution successExecution = new TestExecution(0, false, "Success");
            Execution failureExecution = new TestExecution(-1, false, "Failure");
            Execution interruptedExecution = new TestExecution(1, true, "Interrupted");

            // Act & Assert
            assertThat(successExecution.run()).isEqualTo(0);
            assertThat(successExecution.isInterrupted()).isFalse();
            assertThat(successExecution.getDescription()).isEqualTo("Success");

            assertThat(failureExecution.run()).isEqualTo(-1);
            assertThat(failureExecution.isInterrupted()).isFalse();
            assertThat(failureExecution.getDescription()).isEqualTo("Failure");

            assertThat(interruptedExecution.run()).isEqualTo(1);
            assertThat(interruptedExecution.isInterrupted()).isTrue();
            assertThat(interruptedExecution.getDescription()).isEqualTo("Interrupted");
        }
    }

    @Nested
    @DisplayName("Behavioral Pattern Tests")
    class BehavioralPatternTests {

        @Test
        @DisplayName("Should maintain state consistency")
        void testExecution_StateConsistency_Maintained() {
            // Arrange
            TestExecution execution = new TestExecution(0, false, "Test");

            // Act - Multiple calls should return consistent results
            int result1 = execution.run();
            int result2 = execution.run();
            boolean interrupted1 = execution.isInterrupted();
            boolean interrupted2 = execution.isInterrupted();
            String desc1 = execution.getDescription();
            String desc2 = execution.getDescription();

            // Assert - Results should be consistent
            assertThat(result1).isEqualTo(result2);
            assertThat(interrupted1).isEqualTo(interrupted2);
            assertThat(desc1).isEqualTo(desc2);
        }

        @Test
        @DisplayName("Should support null description")
        void testExecution_NullDescription_Supported() {
            // Arrange
            Execution execution = new TestExecution(0, false, null);

            // Act & Assert
            assertThat(execution.getDescription()).isNull();
        }

        @Test
        @DisplayName("Should support empty description")
        void testExecution_EmptyDescription_Supported() {
            // Arrange
            Execution execution = new TestExecution(0, false, "");

            // Act & Assert
            assertThat(execution.getDescription()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Usage Pattern Tests")
    class UsagePatternTests {

        @Test
        @DisplayName("Should work in typical execution flow")
        void testExecution_TypicalFlow_WorksCorrectly() {
            // Arrange
            Execution execution = new TestExecution(0, false, "Typical execution");

            // Act - Typical usage pattern
            String description = execution.getDescription();
            int result = execution.run();
            boolean wasInterrupted = execution.isInterrupted();

            // Assert
            assertThat(description).isNotNull();
            assertThat(result).isNotNegative(); // Assuming non-negative means success
            assertThat(wasInterrupted).isFalse();
        }

        @Test
        @DisplayName("Should work with error scenarios")
        void testExecution_ErrorScenarios_HandledCorrectly() {
            // Arrange
            Execution errorExecution = new TestExecution(-1, false, "Error execution");
            Execution interruptedExecution = new TestExecution(0, true, "Interrupted execution");

            // Act & Assert - Error cases
            assertThat(errorExecution.run()).isNegative();
            assertThat(errorExecution.isInterrupted()).isFalse();

            assertThat(interruptedExecution.run()).isNotNegative();
            assertThat(interruptedExecution.isInterrupted()).isTrue();
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should integrate with Job class concepts")
        void testExecution_JobIntegration_WorksAsExpected() {
            // Arrange - Simulating usage in Job context
            Execution execution = new TestExecution(0, false, "Job execution");

            // Act - Simulate Job.run() logic
            int resultCode = execution.run();
            String description = execution.getDescription();

            // Assert - Should provide all needed information for Job
            assertThat(resultCode).isNotNull();
            assertThat(description).isNotNull();
            // isInterrupted() can be true or false, both valid - tested elsewhere
        }

        @Test
        @DisplayName("Should support multiple execution types")
        void testExecution_MultipleTypes_AllWorkCorrectly() {
            // Arrange - Different types of executions
            Execution quickExecution = new TestExecution(0, false, "Quick task");
            Execution complexExecution = new TestExecution(42, false, "Complex task");
            Execution failedExecution = new TestExecution(-1, false, "Failed task");

            // Act & Assert - All should work through the same interface
            assertThatCode(() -> {
                quickExecution.run();
                quickExecution.isInterrupted();
                quickExecution.getDescription();

                complexExecution.run();
                complexExecution.isInterrupted();
                complexExecution.getDescription();

                failedExecution.run();
                failedExecution.isInterrupted();
                failedExecution.getDescription();
            }).doesNotThrowAnyException();
        }
    }

    /**
     * Simple test implementation of Execution for testing purposes.
     */
    private static class TestExecution implements Execution {
        private final int returnCode;
        private final boolean interrupted;
        private final String description;

        public TestExecution(int returnCode, boolean interrupted, String description) {
            this.returnCode = returnCode;
            this.interrupted = interrupted;
            this.description = description;
        }

        @Override
        public int run() {
            return returnCode;
        }

        @Override
        public boolean isInterrupted() {
            return interrupted;
        }

        @Override
        public String getDescription() {
            return description;
        }
    }
}
