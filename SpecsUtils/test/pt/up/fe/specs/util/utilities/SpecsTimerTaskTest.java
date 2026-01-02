package pt.up.fe.specs.util.utilities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Timer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Unit tests for {@link SpecsTimerTask}.
 * 
 * Tests timer task wrapper that executes a Runnable.
 * 
 * @author Generated Tests
 */
@DisplayName("SpecsTimerTask")
class SpecsTimerTaskTest {

    @Nested
    @DisplayName("Construction")
    class Construction {

        @Test
        @DisplayName("should create with runnable")
        void shouldCreateWithRunnable() {
            Runnable runnable = mock(Runnable.class);

            SpecsTimerTask task = new SpecsTimerTask(runnable);

            assertThat(task).isNotNull();
            assertThat(task).isInstanceOf(java.util.TimerTask.class);
        }

        @Test
        @DisplayName("should accept null runnable")
        void shouldAcceptNullRunnable() {
            SpecsTimerTask task = new SpecsTimerTask(null);

            assertThat(task).isNotNull();
        }
    }

    @Nested
    @DisplayName("Task Execution")
    class TaskExecution {

        @Test
        @DisplayName("should execute runnable when run is called")
        void shouldExecuteRunnableWhenRunIsCalled() {
            Runnable runnable = mock(Runnable.class);
            SpecsTimerTask task = new SpecsTimerTask(runnable);

            task.run();

            verify(runnable, times(1)).run();
        }

        @Test
        @DisplayName("should execute runnable multiple times")
        void shouldExecuteRunnableMultipleTimes() {
            Runnable runnable = mock(Runnable.class);
            SpecsTimerTask task = new SpecsTimerTask(runnable);

            task.run();
            task.run();
            task.run();

            verify(runnable, times(3)).run();
        }

        @Test
        @DisplayName("should handle runnable that modifies state")
        void shouldHandleRunnableThatModifiesState() {
            AtomicInteger counter = new AtomicInteger(0);
            Runnable runnable = counter::incrementAndGet;
            SpecsTimerTask task = new SpecsTimerTask(runnable);

            task.run();
            task.run();

            assertThat(counter.get()).isEqualTo(2);
        }

        @Test
        @DisplayName("should throw exception when runnable is null")
        void shouldThrowExceptionWhenRunnableIsNull() {
            SpecsTimerTask task = new SpecsTimerTask(null);

            assertThatThrownBy(task::run)
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("should propagate exceptions from runnable")
        void shouldPropagateExceptionsFromRunnable() {
            RuntimeException expectedException = new RuntimeException("Test exception");
            Runnable runnable = () -> {
                throw expectedException;
            };
            SpecsTimerTask task = new SpecsTimerTask(runnable);

            assertThatThrownBy(task::run)
                    .isSameAs(expectedException);
        }
    }

    @Nested
    @DisplayName("Timer Integration")
    class TimerIntegration {

        @Test
        @DisplayName("should work with Timer schedule")
        void shouldWorkWithTimerSchedule() throws InterruptedException {
            AtomicBoolean executed = new AtomicBoolean(false);
            CountDownLatch latch = new CountDownLatch(1);

            Runnable runnable = () -> {
                executed.set(true);
                latch.countDown();
            };

            SpecsTimerTask task = new SpecsTimerTask(runnable);
            Timer timer = new Timer();

            try {
                timer.schedule(task, 50); // 50ms delay

                boolean completed = latch.await(1000, TimeUnit.MILLISECONDS);
                assertThat(completed).isTrue();
                assertThat(executed.get()).isTrue();
            } finally {
                timer.cancel();
            }
        }

        @Test
        @DisplayName("should work with Timer periodic execution")
        void shouldWorkWithTimerPeriodicExecution() throws InterruptedException {
            AtomicInteger counter = new AtomicInteger(0);
            CountDownLatch latch = new CountDownLatch(3);

            Runnable runnable = () -> {
                counter.incrementAndGet();
                latch.countDown();
            };

            SpecsTimerTask task = new SpecsTimerTask(runnable);
            Timer timer = new Timer();

            try {
                timer.scheduleAtFixedRate(task, 0, 50); // Every 50ms

                boolean completed = latch.await(1000, TimeUnit.MILLISECONDS);
                assertThat(completed).isTrue();
                assertThat(counter.get()).isGreaterThanOrEqualTo(3);
            } finally {
                timer.cancel();
            }
        }

        @Test
        @DisplayName("should be cancellable")
        void shouldBeCancellable() throws InterruptedException {
            // Use a latch to deterministically wait for a known number of executions
            AtomicInteger counter = new AtomicInteger(0);
            CountDownLatch initialRuns = new CountDownLatch(3); // wait for 3 executions
            Runnable runnable = () -> {
                counter.incrementAndGet();
                initialRuns.countDown();
            };

            SpecsTimerTask task = new SpecsTimerTask(runnable);
            Timer timer = new Timer();

            final int periodMs = 50;

            try {
                timer.scheduleAtFixedRate(task, 0, periodMs);

                // Wait (with timeout) for the first N executions instead of relying on arbitrary sleeps
                boolean gotInitialExecutions = initialRuns.await(1000, TimeUnit.MILLISECONDS);
                assertThat(gotInitialExecutions)
                        .as("Timer did not execute the expected initial runs in time")
                        .isTrue();

                // Cancel further executions. According to TimerTask semantics, an in-flight execution may still finish.
                task.cancel();

                // Allow any in-flight execution (that started before cancel) to complete and be counted.
                Thread.sleep(periodMs + 20);
                int countAfterCancel = counter.get();

                // Wait longer than multiple periods; count should remain stable after cancellation grace window.
                Thread.sleep(periodMs * 3L);
                assertThat(counter.get())
                        .as("Counter changed after cancellation (expected stable value)")
                        .isEqualTo(countAfterCancel);
            } finally {
                timer.cancel();
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("should handle long-running runnable")
        void shouldHandleLongRunningRunnable() throws InterruptedException {
            AtomicBoolean started = new AtomicBoolean(false);
            AtomicBoolean finished = new AtomicBoolean(false);
            CountDownLatch startLatch = new CountDownLatch(1);

            Runnable runnable = () -> {
                started.set(true);
                startLatch.countDown();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                finished.set(true);
            };

            SpecsTimerTask task = new SpecsTimerTask(runnable);

            Thread executionThread = new Thread(task::run);
            executionThread.start();

            // Wait for the task to actually start
            boolean taskStarted = startLatch.await(1000, TimeUnit.MILLISECONDS);
            assertThat(taskStarted).isTrue();
            assertThat(started.get()).isTrue();
            assertThat(finished.get()).isFalse();

            executionThread.join(1000);
            assertThat(finished.get()).isTrue();
        }

        @Test
        @DisplayName("should handle runnable that interrupts thread")
        void shouldHandleRunnableThatInterruptsThread() {
            Runnable runnable = () -> Thread.currentThread().interrupt();
            SpecsTimerTask task = new SpecsTimerTask(runnable);

            assertThatCode(task::run).doesNotThrowAnyException();
            assertThat(Thread.interrupted()).isTrue(); // Clear interrupt status
        }

        @Test
        @DisplayName("should work after cancellation and recreation")
        void shouldWorkAfterCancellationAndRecreation() {
            AtomicInteger counter = new AtomicInteger(0);
            Runnable runnable = counter::incrementAndGet;

            SpecsTimerTask task1 = new SpecsTimerTask(runnable);
            task1.run();
            task1.cancel();

            SpecsTimerTask task2 = new SpecsTimerTask(runnable);
            task2.run();

            assertThat(counter.get()).isEqualTo(2);
        }
    }
}
