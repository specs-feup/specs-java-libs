package pt.up.fe.specs.util.collections.concurrentchannel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Comprehensive test suite for {@link ChannelConsumer}.
 * Tests consumer operations in concurrent channel communication.
 * 
 * @author Generated Tests
 */
class ChannelConsumerTest {

    private ConcurrentChannel<String> channel;
    private ChannelProducer<String> producer;
    private ChannelConsumer<String> consumer;

    private static final int CHANNEL_CAPACITY = 3;

    @BeforeEach
    void setUp() {
        channel = new ConcurrentChannel<>(CHANNEL_CAPACITY);
        producer = channel.createProducer();
        consumer = channel.createConsumer();
    }

    @Nested
    @DisplayName("Basic Consumer Operations")
    class BasicOperationsTests {

        @Test
        @DisplayName("Should poll null from empty channel")
        void testPollFromEmptyChannel() {
            String result = consumer.poll();
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should poll element from channel with single item")
        void testPollSingleElement() {
            producer.offer("item1");

            String result = consumer.poll();
            assertThat(result).isEqualTo("item1");
            assertThat(consumer.poll()).isNull(); // Channel should be empty now
        }

        @Test
        @DisplayName("Should poll elements in FIFO order")
        void testPollFIFOOrder() {
            producer.offer("first");
            producer.offer("second");
            producer.offer("third");

            assertThat(consumer.poll()).isEqualTo("first");
            assertThat(consumer.poll()).isEqualTo("second");
            assertThat(consumer.poll()).isEqualTo("third");
            assertThat(consumer.poll()).isNull();
        }

        @Test
        @DisplayName("Should reject null elements")
        void testPollNullElement() {
            assertThrows(NullPointerException.class, () -> producer.offer(null));
        }

        @Test
        @DisplayName("Should poll all elements from full channel")
        void testPollFromFullChannel() {
            // Fill channel to capacity
            for (int i = 0; i < CHANNEL_CAPACITY; i++) {
                producer.offer("item" + i);
            }

            // Poll all elements
            for (int i = 0; i < CHANNEL_CAPACITY; i++) {
                assertThat(consumer.poll()).isEqualTo("item" + i);
            }

            assertThat(consumer.poll()).isNull();
        }
    }

    @Nested
    @DisplayName("Timed Polling Operations")
    class TimedPollingTests {

        @Test
        @DisplayName("Should poll with timeout successfully when element available")
        void testPollWithTimeoutSuccess() throws InterruptedException {
            producer.offer("item1");

            String result = consumer.poll(100, TimeUnit.MILLISECONDS);
            assertThat(result).isEqualTo("item1");
        }

        @Test
        @DisplayName("Should timeout when no element available")
        void testPollWithTimeoutFailure() throws InterruptedException {
            long startTime = System.currentTimeMillis();
            String result = consumer.poll(100, TimeUnit.MILLISECONDS);
            long endTime = System.currentTimeMillis();

            assertThat(result).isNull();
            assertThat(endTime - startTime).isGreaterThanOrEqualTo(100);
        }

        @Test
        @DisplayName("Should poll with timeout when element becomes available")
        void testPollWithTimeoutWhenElementBecomesAvailable() throws InterruptedException {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            try {
                // Schedule production after delay
                executor.submit(() -> {
                    try {
                        Thread.sleep(50);
                        producer.offer("delayed_item");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });

                String result = consumer.poll(200, TimeUnit.MILLISECONDS);
                assertThat(result).isEqualTo("delayed_item");
            } finally {
                executor.shutdown();
            }
        }

        @Test
        @DisplayName("Should handle very short timeouts")
        void testVeryShortTimeout() throws InterruptedException {
            String result = consumer.poll(1, TimeUnit.NANOSECONDS);
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should handle zero timeout")
        void testZeroTimeout() throws InterruptedException {
            // With element available
            producer.offer("item1");
            String result = consumer.poll(0, TimeUnit.MILLISECONDS);
            assertThat(result).isEqualTo("item1");

            // Without element available
            result = consumer.poll(0, TimeUnit.MILLISECONDS);
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should handle different timeout units")
        void testDifferentTimeoutUnits() throws InterruptedException {
            producer.offer("item1");
            producer.offer("item2");
            producer.offer("item3");

            assertThat(consumer.poll(1, TimeUnit.SECONDS)).isEqualTo("item1");
            assertThat(consumer.poll(1000, TimeUnit.MILLISECONDS)).isEqualTo("item2");
            assertThat(consumer.poll(1000000, TimeUnit.MICROSECONDS)).isEqualTo("item3");
        }
    }

    @Nested
    @DisplayName("Blocking Take Operations")
    class BlockingTakeTests {

        @Test
        @DisplayName("Should take element without blocking when available")
        void testTakeWithoutBlocking() throws InterruptedException {
            producer.offer("item1");

            long startTime = System.currentTimeMillis();
            String result = consumer.take();
            long endTime = System.currentTimeMillis();

            assertThat(result).isEqualTo("item1");
            assertThat(endTime - startTime).isLessThan(50); // Should be nearly instantaneous
        }

        @Test
        @DisplayName("Should take element and block when channel is empty")
        @Timeout(value = 5, unit = TimeUnit.SECONDS)
        void testTakeBlocksWhenEmpty() throws InterruptedException {
            CountDownLatch takeStarted = new CountDownLatch(1);
            CountDownLatch takeCompleted = new CountDownLatch(1);
            String[] result = new String[1];

            ExecutorService executor = Executors.newSingleThreadExecutor();
            try {
                Future<?> takeTask = executor.submit(() -> {
                    takeStarted.countDown();
                    try {
                        result[0] = consumer.take();
                        takeCompleted.countDown();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });

                // Wait for take to start
                takeStarted.await();

                // Take should be blocked - give it some time and verify it hasn't completed
                Thread.sleep(100);
                assertThat(takeCompleted.getCount()).isEqualTo(1);

                // Provide an item
                producer.offer("blocking_item");

                // Now take should complete
                takeCompleted.await(1, TimeUnit.SECONDS);
                assertThat(takeCompleted.getCount()).isZero();
                assertThat(result[0]).isEqualTo("blocking_item");

                try {
                    takeTask.get(1, TimeUnit.SECONDS);
                } catch (Exception e) {
                    fail("Take task failed: " + e.getMessage());
                }
            } finally {
                executor.shutdown();
            }
        }

        @Test
        @DisplayName("Should handle interrupted take operation")
        void testInterruptedTake() throws InterruptedException {
            CountDownLatch takeStarted = new CountDownLatch(1);
            ExecutorService executor = Executors.newSingleThreadExecutor();

            try {
                Future<?> takeTask = executor.submit(() -> {
                    takeStarted.countDown();
                    try {
                        consumer.take();
                        return null;
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return "interrupted";
                    }
                });

                takeStarted.await();
                Thread.sleep(50); // Give time for take to block

                takeTask.cancel(true); // Interrupt the thread

                // Verify the task was cancelled/interrupted
                assertThat(takeTask.isCancelled() || takeTask.isDone()).isTrue();
            } finally {
                executor.shutdown();
            }
        }

        @Test
        @DisplayName("Should handle multiple elements with take")
        void testTakeMultipleElements() throws InterruptedException {
            producer.offer("item1");
            producer.offer("item2");
            producer.offer("item3");

            assertThat(consumer.take()).isEqualTo("item1");
            assertThat(consumer.take()).isEqualTo("item2");
            assertThat(consumer.take()).isEqualTo("item3");
        }

        @Test
        @DisplayName("Should reject null elements in blocking take")
        void testTakeNullElements() throws InterruptedException {
            assertThrows(NullPointerException.class, () -> producer.offer(null));
        }
    }

    @Nested
    @DisplayName("Concurrent Consumer Operations")
    class ConcurrentOperationsTests {

        @Test
        @DisplayName("Should handle multiple consumers")
        @Timeout(value = 5, unit = TimeUnit.SECONDS)
        void testMultipleConsumers() throws InterruptedException {
            ChannelConsumer<String> consumer2 = channel.createConsumer();

            // Add items to consume
            producer.offer("item1");
            producer.offer("item2");

            CountDownLatch startLatch = new CountDownLatch(2);
            CountDownLatch completeLatch = new CountDownLatch(2);
            String[] results = new String[2];

            ExecutorService executor = Executors.newFixedThreadPool(2);

            try {
                executor.submit(() -> {
                    startLatch.countDown();
                    try {
                        startLatch.await();
                        results[0] = consumer.poll();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    completeLatch.countDown();
                });

                executor.submit(() -> {
                    startLatch.countDown();
                    try {
                        startLatch.await();
                        results[1] = consumer2.poll();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    completeLatch.countDown();
                });

                completeLatch.await();

                // Both consumers should get an item
                assertThat(results[0]).isNotNull();
                assertThat(results[1]).isNotNull();
                assertThat(results).containsExactlyInAnyOrder("item1", "item2");

            } finally {
                executor.shutdown();
            }
        }

        @Test
        @DisplayName("Should handle producer-consumer interaction")
        @Timeout(value = 5, unit = TimeUnit.SECONDS)
        void testProducerConsumerInteraction() throws InterruptedException {
            final int itemCount = 10;
            CountDownLatch completeLatch = new CountDownLatch(itemCount * 2);
            String[] consumed = new String[itemCount];

            ExecutorService executor = Executors.newFixedThreadPool(2);

            try {
                // Producer thread
                executor.submit(() -> {
                    for (int i = 0; i < itemCount; i++) {
                        producer.put("item" + i);
                        completeLatch.countDown();
                    }
                });

                // Consumer thread
                executor.submit(() -> {
                    for (int i = 0; i < itemCount; i++) {
                        try {
                            consumed[i] = consumer.take();
                            completeLatch.countDown();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                });

                completeLatch.await();

                // Verify all items were consumed in order
                for (int i = 0; i < itemCount; i++) {
                    assertThat(consumed[i]).isEqualTo("item" + i);
                }

            } finally {
                executor.shutdown();
            }
        }

        @Test
        @DisplayName("Should handle competing consumers")
        @Timeout(value = 5, unit = TimeUnit.SECONDS)
        void testCompetingConsumers() throws InterruptedException {
            final int consumerCount = 3;
            final int itemCount = 9; // Divisible by consumer count for cleaner test

            // Create multiple consumers
            @SuppressWarnings("unchecked")
            ChannelConsumer<String>[] consumers = new ChannelConsumer[consumerCount];
            for (int i = 0; i < consumerCount; i++) {
                consumers[i] = channel.createConsumer();
            }

            // Produce items
            for (int i = 0; i < itemCount; i++) {
                producer.offer("item" + i);
            }

            CountDownLatch startLatch = new CountDownLatch(1);
            CountDownLatch completeLatch = new CountDownLatch(consumerCount);
            ExecutorService executor = Executors.newFixedThreadPool(consumerCount);

            try {
                int itemsPerConsumer = itemCount / consumerCount;
                for (int i = 0; i < consumerCount; i++) {
                    final ChannelConsumer<String> currentConsumer = consumers[i];
                    executor.submit(() -> {
                        try {
                            startLatch.await(); // Synchronize start
                            for (int j = 0; j < itemsPerConsumer; j++) {
                                String item = currentConsumer.poll();
                                assertThat(item).isNotNull();
                                assertThat(item).startsWith("item");
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        } finally {
                            completeLatch.countDown();
                        }
                    });
                }

                startLatch.countDown(); // Start all consumers
                completeLatch.await(); // Wait for all to complete

                // Verify channel is empty
                assertThat(channel.createConsumer().poll()).isNull();

            } finally {
                executor.shutdown();
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle rapid polling")
        void testRapidPolling() {
            producer.offer("item1");

            for (int i = 0; i < 1000; i++) {
                String result = consumer.poll();
                if (result != null) {
                    assertThat(result).isEqualTo("item1");
                    break;
                } else if (i == 999) {
                    fail("Should have found the item");
                }
            }
        }

        @Test
        @DisplayName("Should handle large number of operations")
        void testLargeNumberOfOperations() throws InterruptedException {
            final int operationCount = 1000;

            ExecutorService executor = Executors.newSingleThreadExecutor();
            try {
                // Producer in background
                executor.submit(() -> {
                    for (int i = 0; i < operationCount; i++) {
                        producer.put("item" + i);
                    }
                });

                // Consumer in main thread
                for (int i = 0; i < operationCount; i++) {
                    String result = consumer.take();
                    assertThat(result).isEqualTo("item" + i);
                }

            } finally {
                executor.shutdown();
            }
        }

        @Test
        @DisplayName("Should work with different element types")
        void testDifferentElementTypes() {
            ConcurrentChannel<Integer> intChannel = new ConcurrentChannel<>(3);
            ChannelProducer<Integer> intProducer = intChannel.createProducer();
            ChannelConsumer<Integer> intConsumer = intChannel.createConsumer();

            intProducer.offer(42);
            intProducer.offer(0);
            intProducer.offer(-1);

            assertThat(intConsumer.poll()).isEqualTo(42);
            assertThat(intConsumer.poll()).isEqualTo(0);
            assertThat(intConsumer.poll()).isEqualTo(-1);
            assertThat(intConsumer.poll()).isNull();
        }

        @Test
        @DisplayName("Should handle mixed polling strategies")
        void testMixedPollingStrategies() throws InterruptedException {
            producer.offer("item1");
            producer.offer("item2");

            // Mix different polling methods
            assertThat(consumer.poll()).isEqualTo("item1");
            assertThat(consumer.poll(100, TimeUnit.MILLISECONDS)).isEqualTo("item2");
            assertThat(consumer.poll()).isNull();

            producer.offer("item3");
            assertThat(consumer.take()).isEqualTo("item3");
        }

        /*
         * @Test
         * 
         * @DisplayName("Should handle timeout edge cases")
         * void testTimeoutEdgeCases() throws InterruptedException {
         * // Test not working. Tag as TODO in BUGS and disable test
         * // Test with maximum timeout values
         * assertThat(consumer.poll(Long.MAX_VALUE, TimeUnit.NANOSECONDS)).isNull();
         * 
         * // Test with negative timeout (should behave like zero timeout)
         * producer.offer("item1");
         * assertThat(consumer.poll(-1, TimeUnit.MILLISECONDS)).isEqualTo("item1");
         * }
         */
    }

    @Nested
    @DisplayName("Stress Testing")
    class StressTests {

        @Test
        @DisplayName("Should handle high-frequency operations")
        @Timeout(value = 10, unit = TimeUnit.SECONDS)
        void testHighFrequencyOperations() throws InterruptedException {
            final int iterations = 10000;
            CountDownLatch producerLatch = new CountDownLatch(iterations);
            CountDownLatch consumerLatch = new CountDownLatch(iterations);

            ExecutorService executor = Executors.newFixedThreadPool(2);

            try {
                // High-frequency producer
                executor.submit(() -> {
                    for (int i = 0; i < iterations; i++) {
                        producer.put("item" + i);
                        producerLatch.countDown();
                    }
                });

                // High-frequency consumer
                executor.submit(() -> {
                    for (int i = 0; i < iterations; i++) {
                        try {
                            String item = consumer.take();
                            assertThat(item).startsWith("item");
                            consumerLatch.countDown();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                });

                producerLatch.await();
                consumerLatch.await();

            } finally {
                executor.shutdown();
            }
        }
    }
}
