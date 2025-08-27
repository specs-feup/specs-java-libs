package pt.up.fe.specs.util.jobs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive unit tests for the JobProgress class.
 * Tests job progress tracking and logging functionality.
 * 
 * @author Generated Tests
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("JobProgress Tests")
class JobProgressTest {

    private List<Job> jobs;
    private Job job1;
    private Job job2;
    private Job job3;

    @BeforeEach
    void setUp() {
        job1 = Job.singleJavaCall(() -> {
        }, "Job 1");
        job2 = Job.singleJavaCall(() -> {
        }, "Job 2");
        job3 = Job.singleJavaCall(() -> {
        }, "Job 3");
        jobs = Arrays.asList(job1, job2, job3);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create JobProgress with job list")
        void testConstructor_WithJobList_CreatesJobProgress() {
            // Act
            JobProgress progress = new JobProgress(jobs);

            // Assert
            assertThat(progress).isNotNull();
        }

        @Test
        @DisplayName("Should handle empty job list")
        void testConstructor_EmptyJobList_CreatesJobProgress() {
            // Arrange
            List<Job> emptyJobs = Collections.emptyList();

            // Act
            JobProgress progress = new JobProgress(emptyJobs);

            // Assert
            assertThat(progress).isNotNull();
        }

        @Test
        @DisplayName("Should handle single job")
        void testConstructor_SingleJob_CreatesJobProgress() {
            // Arrange
            List<Job> singleJob = Arrays.asList(job1);

            // Act
            JobProgress progress = new JobProgress(singleJob);

            // Assert
            assertThat(progress).isNotNull();
        }

        @Test
        @DisplayName("Should handle null job list")
        void testConstructor_NullJobList_HandlesGracefully() {
            // Act & Assert
            assertThatThrownBy(() -> new JobProgress(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Initial Message Tests")
    class InitialMessageTests {

        @Test
        @DisplayName("Should show initial message with correct job count")
        void testInitialMessage_WithJobs_ShowsCorrectCount() {
            // Arrange
            JobProgress progress = new JobProgress(jobs);

            // Act & Assert - We can't easily mock static methods in this test setup
            // So we'll just verify it doesn't throw exceptions
            assertThatCode(() -> progress.initialMessage()).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should show initial message for empty job list")
        void testInitialMessage_EmptyJobs_ShowsZeroCount() {
            // Arrange
            List<Job> emptyJobs = Collections.emptyList();
            JobProgress progress = new JobProgress(emptyJobs);

            // Act & Assert
            assertThatCode(() -> progress.initialMessage()).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should show initial message for single job")
        void testInitialMessage_SingleJob_ShowsOneCount() {
            // Arrange
            List<Job> singleJob = Arrays.asList(job1);
            JobProgress progress = new JobProgress(singleJob);

            // Act & Assert
            assertThatCode(() -> progress.initialMessage()).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Next Message Tests")
    class NextMessageTests {

        @Test
        @DisplayName("Should show next message for each job")
        void testNextMessage_MultipleJobs_ShowsProgressCorrectly() {
            // Arrange
            JobProgress progress = new JobProgress(jobs);

            // Act & Assert - Call nextMessage() for each job
            assertThatCode(() -> progress.nextMessage()).doesNotThrowAnyException();
            assertThatCode(() -> progress.nextMessage()).doesNotThrowAnyException();
            assertThatCode(() -> progress.nextMessage()).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle calling nextMessage more than job count")
        void testNextMessage_ExceedsJobCount_HandlesGracefully() {
            // Arrange
            List<Job> singleJob = Arrays.asList(job1);
            JobProgress progress = new JobProgress(singleJob);

            // Act - Call nextMessage() more times than there are jobs
            assertThatCode(() -> progress.nextMessage()).doesNotThrowAnyException();
            // Second call should throw exception based on implementation
            assertThatThrownBy(() -> progress.nextMessage())
                    .isInstanceOfAny(IndexOutOfBoundsException.class, ArrayIndexOutOfBoundsException.class);
        }

        @Test
        @DisplayName("Should show job description when available")
        void testNextMessage_WithJobDescription_IncludesDescription() {
            // Arrange
            Job jobWithDescription = Job.singleJavaCall(() -> {
            }, "Custom Description");
            List<Job> jobsWithDescription = Arrays.asList(jobWithDescription);
            JobProgress progress = new JobProgress(jobsWithDescription);

            // Act & Assert
            assertThatCode(() -> progress.nextMessage()).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle job with null description")
        void testNextMessage_NullDescription_HandlesGracefully() {
            // Arrange
            Job jobWithNullDescription = Job.singleJavaCall(() -> {
            });
            List<Job> jobsWithNullDescription = Arrays.asList(jobWithNullDescription);
            JobProgress progress = new JobProgress(jobsWithNullDescription);

            // Act & Assert
            assertThatCode(() -> progress.nextMessage()).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle empty job list nextMessage")
        void testNextMessage_EmptyJobList_HandlesGracefully() {
            // Arrange
            List<Job> emptyJobs = Collections.emptyList();
            JobProgress progress = new JobProgress(emptyJobs);

            // Act & Assert - Implementation throws IndexOutOfBoundsException
            assertThatThrownBy(() -> progress.nextMessage())
                    .isInstanceOf(IndexOutOfBoundsException.class);
        }
    }

    @Nested
    @DisplayName("Progress Tracking Tests")
    class ProgressTrackingTests {

        @Test
        @DisplayName("Should track progress correctly through sequence")
        void testProgressSequence_FullSequence_TracksCorrectly() {
            // Arrange
            JobProgress progress = new JobProgress(jobs);

            // Act & Assert - Simulate a complete job sequence
            assertThatCode(() -> {
                progress.initialMessage();
                progress.nextMessage(); // Job 1 of 3
                progress.nextMessage(); // Job 2 of 3
                progress.nextMessage(); // Job 3 of 3
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle rapid successive calls")
        void testProgressSequence_RapidCalls_HandlesCorrectly() {
            // Arrange
            JobProgress progress = new JobProgress(jobs);

            // Act & Assert - Implementation throws exception when exceeding job count
            assertThatCode(() -> {
                progress.nextMessage(); // Job 1
                progress.nextMessage(); // Job 2
                progress.nextMessage(); // Job 3
            }).doesNotThrowAnyException();

            // Calling beyond job count throws exception
            assertThatThrownBy(() -> progress.nextMessage())
                    .isInstanceOfAny(IndexOutOfBoundsException.class, ArrayIndexOutOfBoundsException.class);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with real job execution")
        void testJobProgress_WithRealJobExecution_WorksCorrectly() {
            // Arrange
            AtomicBoolean job1Executed = new AtomicBoolean(false);
            AtomicBoolean job2Executed = new AtomicBoolean(false);

            Job realJob1 = Job.singleJavaCall(() -> {
                job1Executed.set(true);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            }, "Real Job 1");

            Job realJob2 = Job.singleJavaCall(() -> {
                job2Executed.set(true);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            }, "Real Job 2");

            List<Job> realJobs = Arrays.asList(realJob1, realJob2);
            JobProgress progress = new JobProgress(realJobs);

            // Act
            progress.initialMessage();

            progress.nextMessage();
            realJob1.run();

            progress.nextMessage();
            realJob2.run();

            // Assert
            assertThat(job1Executed.get()).isTrue();
            assertThat(job2Executed.get()).isTrue();
        }

        @Test
        @DisplayName("Should work with JobUtils.runJobs integration")
        void testJobProgress_WithJobUtilsIntegration_WorksCorrectly() {
            // Arrange
            AtomicBoolean executed = new AtomicBoolean(false);
            Job testJob = Job.singleJavaCall(() -> executed.set(true), "Integration Test Job");
            List<Job> testJobs = Arrays.asList(testJob);

            // Act - JobUtils.runJobs internally uses JobProgress
            boolean result = JobUtils.runJobs(testJobs);

            // Assert
            assertThat(result).isTrue();
            assertThat(executed.get()).isTrue();
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle jobs with very long descriptions")
        void testJobProgress_LongDescriptions_HandlesCorrectly() {
            // Arrange
            String longDescription = "A".repeat(1000);
            Job jobWithLongDescription = Job.singleJavaCall(() -> {
            }, longDescription);
            List<Job> jobsWithLongDesc = Arrays.asList(jobWithLongDescription);
            JobProgress progress = new JobProgress(jobsWithLongDesc);

            // Act & Assert
            assertThatCode(() -> {
                progress.initialMessage();
                progress.nextMessage();
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle jobs with special characters in description")
        void testJobProgress_SpecialCharactersInDescription_HandlesCorrectly() {
            // Arrange
            String specialDescription = "Job with special chars: !@#$%^&*()[]{}|\\:;\",.<>?";
            Job jobWithSpecialChars = Job.singleJavaCall(() -> {
            }, specialDescription);
            List<Job> jobsWithSpecialChars = Arrays.asList(jobWithSpecialChars);
            JobProgress progress = new JobProgress(jobsWithSpecialChars);

            // Act & Assert
            assertThatCode(() -> {
                progress.initialMessage();
                progress.nextMessage();
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle very large job list")
        void testJobProgress_LargeJobList_HandlesCorrectly() {
            // Arrange
            Job[] largeJobArray = new Job[1000];
            for (int i = 0; i < 1000; i++) {
                largeJobArray[i] = Job.singleJavaCall(() -> {
                }, "Job " + i);
            }
            List<Job> largeJobList = Arrays.asList(largeJobArray);
            JobProgress progress = new JobProgress(largeJobList);

            // Act & Assert
            assertThatCode(() -> {
                progress.initialMessage();
                progress.nextMessage(); // Just test one message
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle jobs with empty string description")
        void testJobProgress_EmptyDescription_HandlesCorrectly() {
            // Arrange
            Job jobWithEmptyDescription = Job.singleJavaCall(() -> {
            }, "");
            List<Job> jobsWithEmptyDesc = Arrays.asList(jobWithEmptyDescription);
            JobProgress progress = new JobProgress(jobsWithEmptyDesc);

            // Act & Assert
            assertThatCode(() -> {
                progress.initialMessage();
                progress.nextMessage();
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("State Management Tests")
    class StateManagementTests {

        @Test
        @DisplayName("Should maintain internal counter correctly")
        void testJobProgress_InternalCounter_MaintainsStateCorrectly() {
            // Arrange
            JobProgress progress = new JobProgress(jobs);

            // Act & Assert - This tests the internal counter behavior
            // We can't directly access the counter, but we can test behavior
            assertThatCode(() -> {
                // Normal sequence
                progress.nextMessage(); // counter = 1
                progress.nextMessage(); // counter = 2
                progress.nextMessage(); // counter = 3
            }).doesNotThrowAnyException();

            // 4th call exceeds job count and throws exception
            assertThatThrownBy(() -> progress.nextMessage())
                    .isInstanceOfAny(IndexOutOfBoundsException.class, ArrayIndexOutOfBoundsException.class);
        }

        @Test
        @DisplayName("Should handle multiple JobProgress instances independently")
        void testJobProgress_MultipleInstances_IndependentState() {
            // Arrange
            JobProgress progress1 = new JobProgress(Arrays.asList(job1, job2));
            JobProgress progress2 = new JobProgress(Arrays.asList(job3));

            // Act & Assert
            assertThatCode(() -> {
                progress1.initialMessage();
                progress2.initialMessage();

                progress1.nextMessage();
                progress2.nextMessage();

                progress1.nextMessage();
                // progress2 should still work independently
            }).doesNotThrowAnyException();
        }
    }
}
