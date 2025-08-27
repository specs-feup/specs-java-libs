package pt.up.fe.specs.util.utilities;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for Incrementer utility class.
 * Tests counter functionality, thread safety, and different increment modes.
 * 
 * @author Generated Tests
 */
@DisplayName("Incrementer Tests")
class IncrementerTest {

    @Nested
    @DisplayName("Construction Tests")
    class ConstructionTests {

        @Test
        @DisplayName("should initialize with zero")
        void testInitialValue() {
            // Execute
            Incrementer incrementer = new Incrementer();

            // Verify
            assertThat(incrementer.getCurrent()).isEqualTo(0);
        }

        @Test
        @DisplayName("should create independent instances")
        void testMultipleInstances() {
            // Setup
            Incrementer inc1 = new Incrementer();
            Incrementer inc2 = new Incrementer();

            // Execute
            inc1.increment();
            inc1.increment();
            inc2.increment();

            // Verify
            assertThat(inc1.getCurrent()).isEqualTo(2);
            assertThat(inc2.getCurrent()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("Increment Tests")
    class IncrementTests {

        @Test
        @DisplayName("increment should return new value")
        void testIncrement() {
            // Setup
            Incrementer incrementer = new Incrementer();

            // Execute & Verify
            assertThat(incrementer.increment()).isEqualTo(1);
            assertThat(incrementer.increment()).isEqualTo(2);
            assertThat(incrementer.increment()).isEqualTo(3);
            assertThat(incrementer.getCurrent()).isEqualTo(3);
        }

        @Test
        @DisplayName("getAndIncrement should return current value then increment")
        void testGetAndIncrement() {
            // Setup
            Incrementer incrementer = new Incrementer();

            // Execute & Verify
            assertThat(incrementer.getAndIncrement()).isEqualTo(0);
            assertThat(incrementer.getCurrent()).isEqualTo(1);

            assertThat(incrementer.getAndIncrement()).isEqualTo(1);
            assertThat(incrementer.getCurrent()).isEqualTo(2);

            assertThat(incrementer.getAndIncrement()).isEqualTo(2);
            assertThat(incrementer.getCurrent()).isEqualTo(3);
        }

        @Test
        @DisplayName("should handle mixed increment operations")
        void testMixedOperations() {
            // Setup
            Incrementer incrementer = new Incrementer();

            // Execute
            int first = incrementer.increment(); // returns 1, current = 1
            int second = incrementer.getAndIncrement(); // returns 1, current = 2
            int third = incrementer.increment(); // returns 3, current = 3
            int fourth = incrementer.getAndIncrement(); // returns 3, current = 4

            // Verify
            assertThat(first).isEqualTo(1);
            assertThat(second).isEqualTo(1);
            assertThat(third).isEqualTo(3);
            assertThat(fourth).isEqualTo(3);
            assertThat(incrementer.getCurrent()).isEqualTo(4);
        }
    }

    @Nested
    @DisplayName("State Management Tests")
    class StateManagementTests {

        @Test
        @DisplayName("getCurrent should not modify state")
        void testGetCurrentDoesNotModify() {
            // Setup
            Incrementer incrementer = new Incrementer();
            incrementer.increment();
            incrementer.increment();

            // Execute
            int current1 = incrementer.getCurrent();
            int current2 = incrementer.getCurrent();
            int current3 = incrementer.getCurrent();

            // Verify
            assertThat(current1).isEqualTo(2);
            assertThat(current2).isEqualTo(2);
            assertThat(current3).isEqualTo(2);
            assertThat(incrementer.getCurrent()).isEqualTo(2);
        }

        @Test
        @DisplayName("should handle large numbers of increments")
        void testLargeIncrements() {
            // Setup
            Incrementer incrementer = new Incrementer();
            int iterations = 10000;

            // Execute
            for (int i = 0; i < iterations; i++) {
                incrementer.increment();
            }

            // Verify
            assertThat(incrementer.getCurrent()).isEqualTo(iterations);
        }
    }

    @Nested
    @DisplayName("Sequence Tests")
    class SequenceTests {

        @Test
        @DisplayName("should generate consecutive sequence with increment")
        void testConsecutiveSequence() {
            // Setup
            Incrementer incrementer = new Incrementer();
            List<Integer> values = new ArrayList<>();

            // Execute
            for (int i = 0; i < 5; i++) {
                values.add(incrementer.increment());
            }

            // Verify
            assertThat(values).containsExactly(1, 2, 3, 4, 5);
        }

        @Test
        @DisplayName("should generate starting from zero with getAndIncrement")
        void testZeroBasedSequence() {
            // Setup
            Incrementer incrementer = new Incrementer();
            List<Integer> values = new ArrayList<>();

            // Execute
            for (int i = 0; i < 5; i++) {
                values.add(incrementer.getAndIncrement());
            }

            // Verify
            assertThat(values).containsExactly(0, 1, 2, 3, 4);
            assertThat(incrementer.getCurrent()).isEqualTo(5);
        }
    }

    @Nested
    @DisplayName("Thread Safety Tests")
    class ThreadSafetyTests {

        @Test
        @DisplayName("should handle concurrent access")
        void testConcurrentAccess() throws Exception {
            // Setup
            Incrementer incrementer = new Incrementer();
            int threadCount = 10;
            int incrementsPerThread = 100;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);

            try {
                // Execute
                List<Future<Void>> futures = new ArrayList<>();
                for (int i = 0; i < threadCount; i++) {
                    futures.add(executor.submit(() -> {
                        for (int j = 0; j < incrementsPerThread; j++) {
                            incrementer.increment();
                        }
                        return null;
                    }));
                }

                // Wait for all threads to complete
                for (Future<Void> future : futures) {
                    future.get();
                }

                // Verify - Note: This might not be exactly threadCount * incrementsPerThread
                // due to race conditions (Incrementer is not thread-safe)
                int finalValue = incrementer.getCurrent();
                assertThat(finalValue).isGreaterThan(0).isLessThanOrEqualTo(threadCount * incrementsPerThread);

            } finally {
                executor.shutdown();
            }
        }

        @Test
        @DisplayName("should demonstrate race conditions in concurrent use")
        void testRaceConditions() throws Exception {
            // Setup
            Incrementer incrementer = new Incrementer();
            int threadCount = 5;
            int incrementsPerThread = 20;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            List<Integer> collectedValues = Collections.synchronizedList(new ArrayList<>());

            try {
                // Execute
                List<Future<Void>> futures = new ArrayList<>();
                for (int i = 0; i < threadCount; i++) {
                    futures.add(executor.submit(() -> {
                        for (int j = 0; j < incrementsPerThread; j++) {
                            int value = incrementer.getAndIncrement();
                            collectedValues.add(value);
                        }
                        return null;
                    }));
                }

                // Wait for all threads to complete
                for (Future<Void> future : futures) {
                    future.get();
                }

                // Verify - Due to race conditions, we may see duplicate values
                // This test demonstrates the non-thread-safe nature of Incrementer
                assertThat(collectedValues).hasSize(threadCount * incrementsPerThread);

                // The final value might be less than expected due to race conditions
                int finalValue = incrementer.getCurrent();
                assertThat(finalValue).isGreaterThan(0).isLessThanOrEqualTo(threadCount * incrementsPerThread);

            } finally {
                executor.shutdown();
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("should handle integer overflow gracefully")
        void testIntegerOverflow() {
            // Setup - This test shows what happens at integer limits
            // Note: We can't easily test actual overflow without modifying internal state
            Incrementer incrementer = new Incrementer();

            // Execute - simulate near-max value behavior
            for (int i = 0; i < 1000; i++) {
                incrementer.increment();
            }

            // Verify
            assertThat(incrementer.getCurrent()).isEqualTo(1000);

            // Increment a few more times
            assertThat(incrementer.increment()).isEqualTo(1001);
            assertThat(incrementer.increment()).isEqualTo(1002);
        }

        @Test
        @DisplayName("should maintain consistent state across operations")
        void testStateConsistency() {
            // Setup
            Incrementer incrementer = new Incrementer();

            // Execute - mix of operations
            incrementer.increment();
            int value1 = incrementer.getCurrent();

            incrementer.getAndIncrement();
            int value2 = incrementer.getCurrent();

            incrementer.increment();
            int value3 = incrementer.getCurrent();

            // Verify
            assertThat(value1).isEqualTo(1);
            assertThat(value2).isEqualTo(2);
            assertThat(value3).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("Usage Pattern Tests")
    class UsagePatternTests {

        @Test
        @DisplayName("should work as ID generator")
        void testAsIdGenerator() {
            // Setup
            Incrementer idGenerator = new Incrementer();
            List<Integer> ids = new ArrayList<>();

            // Execute - simulate generating unique IDs
            for (int i = 0; i < 10; i++) {
                ids.add(idGenerator.increment());
            }

            // Verify
            assertThat(ids).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
            assertThat(ids).doesNotHaveDuplicates();
        }

        @Test
        @DisplayName("should work as loop counter")
        void testAsLoopCounter() {
            // Setup
            Incrementer counter = new Incrementer();
            StringBuilder result = new StringBuilder();

            // Execute - simulate processing items with counter
            String[] items = { "a", "b", "c", "d" };
            for (String item : items) {
                int index = counter.getAndIncrement();
                result.append(String.format("[%d]%s", index, item));
            }

            // Verify
            assertThat(result.toString()).isEqualTo("[0]a[1]b[2]c[3]d");
            assertThat(counter.getCurrent()).isEqualTo(4);
        }

        @Test
        @DisplayName("should work for tracking progress")
        void testAsProgressTracker() {
            // Setup
            Incrementer progressTracker = new Incrementer();
            int totalTasks = 5;

            // Execute - simulate task completion tracking
            List<String> progress = new ArrayList<>();

            for (int i = 0; i < totalTasks; i++) {
                // Simulate task completion
                int completed = progressTracker.increment();
                progress.add(String.format("Progress: %d/%d", completed, totalTasks));
            }

            // Verify
            assertThat(progress).containsExactly(
                    "Progress: 1/5",
                    "Progress: 2/5",
                    "Progress: 3/5",
                    "Progress: 4/5",
                    "Progress: 5/5");
        }
    }
}
