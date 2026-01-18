package pt.up.fe.specs.util.utilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for {@link ProgressCounter} class.
 * Tests progress tracking utility with max count and current count management.
 * 
 * @author Generated Tests
 */
class ProgressCounterTest {

    private ProgressCounter counter;
    private static final int DEFAULT_MAX_COUNT = 5;

    @BeforeEach
    void setUp() {
        counter = new ProgressCounter(DEFAULT_MAX_COUNT);
    }

    @Nested
    @DisplayName("Constructor and Initial State")
    class ConstructorTests {

        @Test
        @DisplayName("Should create counter with specified max count")
        void testCounterCreation() {
            ProgressCounter customCounter = new ProgressCounter(10);

            assertThat(customCounter).isNotNull();
            assertThat(customCounter.getMaxCount()).isEqualTo(10);
            assertThat(customCounter.getCurrentCount()).isEqualTo(0);
            assertThat(customCounter.hasNext()).isTrue();
        }

        @Test
        @DisplayName("Should initialize with zero current count")
        void testInitialState() {
            assertThat(counter.getCurrentCount()).isEqualTo(0);
            assertThat(counter.getMaxCount()).isEqualTo(DEFAULT_MAX_COUNT);
            assertThat(counter.hasNext()).isTrue();
        }

        @Test
        @DisplayName("Should handle zero max count")
        void testZeroMaxCount() {
            ProgressCounter zeroCounter = new ProgressCounter(0);

            assertThat(zeroCounter.getMaxCount()).isEqualTo(0);
            assertThat(zeroCounter.getCurrentCount()).isEqualTo(0);
            assertThat(zeroCounter.hasNext()).isFalse();
        }

        @Test
        @DisplayName("Should handle single max count")
        void testSingleMaxCount() {
            ProgressCounter singleCounter = new ProgressCounter(1);

            assertThat(singleCounter.getMaxCount()).isEqualTo(1);
            assertThat(singleCounter.getCurrentCount()).isEqualTo(0);
            assertThat(singleCounter.hasNext()).isTrue();
        }

        @Test
        @DisplayName("Should handle large max count")
        void testLargeMaxCount() {
            ProgressCounter largeCounter = new ProgressCounter(1000000);

            assertThat(largeCounter.getMaxCount()).isEqualTo(1000000);
            assertThat(largeCounter.getCurrentCount()).isEqualTo(0);
            assertThat(largeCounter.hasNext()).isTrue();
        }
    }

    @Nested
    @DisplayName("Next Operation and String Messages")
    class NextOperationTests {

        @Test
        @DisplayName("Should increment count and return formatted message")
        void testNextMessage() {
            String message = counter.next();

            assertThat(message).isEqualTo("(1/5)");
            assertThat(counter.getCurrentCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should return correct messages for sequential calls")
        void testSequentialNext() {
            assertThat(counter.next()).isEqualTo("(1/5)");
            assertThat(counter.next()).isEqualTo("(2/5)");
            assertThat(counter.next()).isEqualTo("(3/5)");
            assertThat(counter.next()).isEqualTo("(4/5)");
            assertThat(counter.next()).isEqualTo("(5/5)");
        }

        @Test
        @DisplayName("Should handle overflow beyond max count")
        void testNextOverflow() {
            // Fill to maximum
            for (int i = 0; i < DEFAULT_MAX_COUNT; i++) {
                counter.next();
            }

            // Should warn but continue incrementing
            String overflowMessage = counter.next();

            // With capped behavior, the counter should not increase beyond max
            assertThat(overflowMessage).isEqualTo("(5/5)");
            assertThat(counter.getCurrentCount()).isEqualTo(5);
        }

        @Test
        @DisplayName("Should handle next on zero max count")
        void testNextZeroMaxCount() {
            ProgressCounter zeroCounter = new ProgressCounter(0);

            String message = zeroCounter.next();

            // With capped behavior at max=0, calling next should not increment
            assertThat(message).isEqualTo("(0/0)");
            assertThat(zeroCounter.getCurrentCount()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should handle single max count correctly")
        void testNextSingleMaxCount() {
            ProgressCounter singleCounter = new ProgressCounter(1);

            String first = singleCounter.next();
            String second = singleCounter.next(); // Should trigger warning

            assertThat(first).isEqualTo("(1/1)");
            // Should remain capped at 1
            assertThat(second).isEqualTo("(1/1)");
        }
    }

    @Nested
    @DisplayName("NextInt Operation")
    class NextIntTests {

        @Test
        @DisplayName("Should increment count and return integer")
        void testNextInt() {
            int result = counter.nextInt();

            assertThat(result).isEqualTo(1);
            assertThat(counter.getCurrentCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should return correct integers for sequential calls")
        void testSequentialNextInt() {
            assertThat(counter.nextInt()).isEqualTo(1);
            assertThat(counter.nextInt()).isEqualTo(2);
            assertThat(counter.nextInt()).isEqualTo(3);
            assertThat(counter.nextInt()).isEqualTo(4);
            assertThat(counter.nextInt()).isEqualTo(5);
        }

        @Test
        @DisplayName("Should handle nextInt overflow beyond max count")
        void testNextIntOverflow() {
            // Fill to maximum
            for (int i = 0; i < DEFAULT_MAX_COUNT; i++) {
                counter.nextInt();
            }

            int result = counter.nextInt();

            // With capped behavior, the counter should not increase beyond max
            assertThat(result).isEqualTo(5);
            assertThat(counter.getCurrentCount()).isEqualTo(5);
        }

        @Test
        @DisplayName("Should synchronize next and nextInt operations")
        void testNextAndNextIntSync() {
            counter.next(); // currentCount = 1
            int intResult = counter.nextInt(); // currentCount = 2
            String strResult = counter.next(); // currentCount = 3

            assertThat(intResult).isEqualTo(2);
            assertThat(strResult).isEqualTo("(3/5)");
            assertThat(counter.getCurrentCount()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("HasNext and State Management")
    class HasNextTests {

        @Test
        @DisplayName("Should return true when under max count")
        void testHasNextTrue() {
            assertThat(counter.hasNext()).isTrue();

            counter.next();
            assertThat(counter.hasNext()).isTrue();

            // Even at max count, should still be true
            for (int i = 1; i < DEFAULT_MAX_COUNT; i++) {
                counter.next();
            }
            assertThat(counter.hasNext()).isFalse();
        }

        @Test
        @DisplayName("Should return false when at max count")
        void testHasNextFalse() {
            // Fill to maximum
            for (int i = 0; i < DEFAULT_MAX_COUNT; i++) {
                counter.next();
            }

            assertThat(counter.hasNext()).isFalse();
        }

        @Test
        @DisplayName("Should return false when over max count")
        void testHasNextOverflow() {
            // Fill beyond maximum
            for (int i = 0; i < DEFAULT_MAX_COUNT + 3; i++) {
                counter.next();
            }

            assertThat(counter.hasNext()).isFalse();
        }

        @Test
        @DisplayName("Should handle hasNext with zero max count")
        void testHasNextZeroMaxCount() {
            ProgressCounter zeroCounter = new ProgressCounter(0);

            assertThat(zeroCounter.hasNext()).isFalse();

            zeroCounter.next(); // Increment beyond zero
            assertThat(zeroCounter.hasNext()).isFalse();
        }

        @Test
        @DisplayName("Should track hasNext state during mixed operations")
        void testHasNextMixedOperations() {
            assertThat(counter.hasNext()).isTrue();

            counter.nextInt(); // 1
            assertThat(counter.hasNext()).isTrue();

            counter.next(); // 2
            assertThat(counter.hasNext()).isTrue();

            counter.nextInt(); // 3
            counter.nextInt(); // 4
            assertThat(counter.hasNext()).isTrue();

            counter.next(); // 5
            assertThat(counter.hasNext()).isFalse();
        }
    }

    @Nested
    @DisplayName("State Accessors")
    class StateAccessorTests {

        @Test
        @DisplayName("Should return correct current count")
        void testGetCurrentCount() {
            assertThat(counter.getCurrentCount()).isEqualTo(0);

            counter.next();
            assertThat(counter.getCurrentCount()).isEqualTo(1);

            counter.nextInt();
            assertThat(counter.getCurrentCount()).isEqualTo(2);
        }

        @Test
        @DisplayName("Should return correct max count")
        void testGetMaxCount() {
            assertThat(counter.getMaxCount()).isEqualTo(DEFAULT_MAX_COUNT);

            ProgressCounter customCounter = new ProgressCounter(100);
            assertThat(customCounter.getMaxCount()).isEqualTo(100);
        }

        @Test
        @DisplayName("Should maintain immutable max count")
        void testMaxCountImmutability() {
            int originalMaxCount = counter.getMaxCount();

            // Perform various operations
            counter.next();
            counter.nextInt();
            for (int i = 0; i < 10; i++) {
                counter.next();
            }

            assertThat(counter.getMaxCount()).isEqualTo(originalMaxCount);
        }

        @Test
        @DisplayName("Should accurately track current count across operations")
        void testCurrentCountAccuracy() {
            int expectedCount = 0;

            for (int i = 0; i < DEFAULT_MAX_COUNT + 3; i++) {
                if (i % 2 == 0) {
                    counter.next();
                } else {
                    counter.nextInt();
                }
                expectedCount = Math.min(expectedCount + 1, DEFAULT_MAX_COUNT);
                assertThat(counter.getCurrentCount()).isEqualTo(expectedCount);
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Conditions")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle negative max count")
        void testNegativeMaxCount() {
            // Constructor should reject negative maxCount
            assertThatThrownBy(() -> new ProgressCounter(-5))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("maxCount should be non-negative");
        }

        @Test
        @DisplayName("Should handle very large sequences")
        void testLargeSequence() {
            ProgressCounter largeCounter = new ProgressCounter(1000);

            for (int i = 0; i < 1000; i++) {
                largeCounter.nextInt();
            }

            assertThat(largeCounter.getCurrentCount()).isEqualTo(1000);
            assertThat(largeCounter.hasNext()).isFalse();

            // One more increment
            largeCounter.nextInt();
            // With capped behavior, the counter remains at the max value
            assertThat(largeCounter.getCurrentCount()).isEqualTo(1000);
            assertThat(largeCounter.hasNext()).isFalse();
        }

        @Test
        @DisplayName("Should handle alternating operations correctly")
        void testAlternatingOperations() {
            String[] expectedMessages = { "(1/5)", "(3/5)", "(5/5)" };
            int[] expectedInts = { 2, 4, 5 };

            for (int i = 0; i < 3; i++) {
                String message = counter.next();
                int intResult = counter.nextInt();

                assertThat(message).isEqualTo(expectedMessages[i]);
                assertThat(intResult).isEqualTo(expectedInts[i]);
            }

            assertThat(counter.getCurrentCount()).isEqualTo(5);
            assertThat(counter.hasNext()).isFalse();
        }

        @Test
        @DisplayName("Should maintain consistency with extreme values")
        void testExtremeValues() {
            ProgressCounter extremeCounter = new ProgressCounter(Integer.MAX_VALUE);

            assertThat(extremeCounter.getMaxCount()).isEqualTo(Integer.MAX_VALUE);
            assertThat(extremeCounter.getCurrentCount()).isEqualTo(0);
            assertThat(extremeCounter.hasNext()).isTrue();

            extremeCounter.next();
            assertThat(extremeCounter.getCurrentCount()).isEqualTo(1);
            assertThat(extremeCounter.hasNext()).isTrue();
        }
    }

    @Nested
    @DisplayName("Integration and Workflow Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should support typical progress tracking workflow")
        void testTypicalWorkflow() {
            // Simulate processing 5 items
            for (int i = 0; i < DEFAULT_MAX_COUNT; i++) {
                assertThat(counter.hasNext()).isTrue();

                String progress = counter.next();
                int currentItem = counter.getCurrentCount();

                assertThat(progress).isEqualTo("(" + currentItem + "/" + DEFAULT_MAX_COUNT + ")");

                // Simulate some work
                // ... processing item ...
            }

            assertThat(counter.hasNext()).isFalse();
            assertThat(counter.getCurrentCount()).isEqualTo(DEFAULT_MAX_COUNT);
        }

        @Test
        @DisplayName("Should handle progress tracking with error handling")
        void testWorkflowWithErrorHandling() {
            while (counter.hasNext()) {
                String progress = counter.next();

                // Simulate that progress messages are well-formed
                assertThat(progress).matches("\\(\\d+/\\d+\\)");

                // Ensure current count never exceeds expected bounds during normal operation
                if (counter.getCurrentCount() <= counter.getMaxCount()) {
                    assertThat(counter.getCurrentCount())
                            .isGreaterThan(0)
                            .isLessThanOrEqualTo(counter.getMaxCount());
                }
            }
        }

        @Test
        @DisplayName("Should work correctly in loop-based iteration")
        void testLoopBasedIteration() {
            int iterations = 0;

            while (counter.hasNext()) {
                counter.nextInt();
                iterations++;
            }

            assertThat(iterations).isEqualTo(DEFAULT_MAX_COUNT);
            assertThat(counter.getCurrentCount()).isEqualTo(DEFAULT_MAX_COUNT);
        }

        @Test
        @DisplayName("Should provide meaningful progress information")
        void testProgressInformation() {
            // Test that progress messages provide useful completion percentage
            for (int i = 1; i <= DEFAULT_MAX_COUNT; i++) {
                String progress = counter.next();

                // Calculate expected percentage
                double expectedPercentage = (double) i / DEFAULT_MAX_COUNT * 100;

                // Verify the message format contains the correct ratio
                assertThat(progress).isEqualTo("(" + i + "/" + DEFAULT_MAX_COUNT + ")");

                // Can derive percentage: i/maxCount * 100
                assertThat(expectedPercentage).isEqualTo(i * 100.0 / DEFAULT_MAX_COUNT);
            }
        }

        @Test
        @DisplayName("Should handle mixed string and integer progress tracking")
        void testMixedProgressTracking() {
            ProgressCounter mixedCounter = new ProgressCounter(6);

            String msg1 = mixedCounter.next(); // (1/6)
            int int1 = mixedCounter.nextInt(); // 2
            String msg2 = mixedCounter.next(); // (3/6)
            int int2 = mixedCounter.nextInt(); // 4
            String msg3 = mixedCounter.next(); // (5/6)
            int int3 = mixedCounter.nextInt(); // 6

            assertThat(msg1).isEqualTo("(1/6)");
            assertThat(int1).isEqualTo(2);
            assertThat(msg2).isEqualTo("(3/6)");
            assertThat(int2).isEqualTo(4);
            assertThat(msg3).isEqualTo("(5/6)");
            assertThat(int3).isEqualTo(6);

            assertThat(mixedCounter.hasNext()).isFalse();
        }
    }
}
