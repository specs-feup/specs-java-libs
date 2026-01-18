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
 * Comprehensive test suite for {@link ChannelProducer}.
 * Tests producer operations in concurrent channel communication.
 * 
 * @author Generated Tests
 */
class ChannelProducerTest {

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
    @DisplayName("Basic Producer Operations")
    class BasicOperationsTests {

        @Test
        @DisplayName("Should offer element successfully to empty channel")
        void testOfferToEmptyChannel() {
            boolean result = producer.offer("item1");

            assertThat(result).isTrue();
            assertThat(consumer.poll()).isEqualTo("item1");
        }

        @Test
        @DisplayName("Should offer multiple elements")
        void testOfferMultipleElements() {
            assertThat(producer.offer("item1")).isTrue();
            assertThat(producer.offer("item2")).isTrue();
            assertThat(producer.offer("item3")).isTrue();

            assertThat(consumer.poll()).isEqualTo("item1");
            assertThat(consumer.poll()).isEqualTo("item2");
            assertThat(consumer.poll()).isEqualTo("item3");
        }

        @Test
        @DisplayName("Should reject offer when channel is full")
        void testOfferToFullChannel() {
            // Fill the channel to capacity
            for (int i = 0; i < CHANNEL_CAPACITY; i++) {
                assertThat(producer.offer("item" + i)).isTrue();
            }

            // Next offer should fail
            assertThat(producer.offer("overflow")).isFalse();
        }

        @Test
        @DisplayName("Should put element and block if necessary")
        void testPutElement() {
            producer.put("item1");

            assertThat(consumer.poll()).isEqualTo("item1");
        }

        @Test
        @DisplayName("Should reject null elements with NullPointerException")
        void testOfferNullElement() {
            assertThrows(NullPointerException.class, () -> producer.offer(null));
        }
    }

    @Nested
    @DisplayName("Timed Operations")
    class TimedOperationsTests {

        @Test
        @DisplayName("Should offer with timeout successfully")
        void testOfferWithTimeoutSuccess() throws InterruptedException {
            boolean result = producer.offer("item1", 100, TimeUnit.MILLISECONDS);

            assertThat(result).isTrue();
            assertThat(consumer.poll()).isEqualTo("item1");
        }

        @Test
        @DisplayName("Should timeout when channel is full")
        void testOfferWithTimeoutFailure() throws InterruptedException {
            // Fill the channel
            for (int i = 0; i < CHANNEL_CAPACITY; i++) {
                producer.offer("item" + i);
            }

            long startTime = System.currentTimeMillis();
            boolean result = producer.offer("overflow", 100, TimeUnit.MILLISECONDS);
            long endTime = System.currentTimeMillis();

            assertThat(result).isFalse();
            assertThat(endTime - startTime).isGreaterThanOrEqualTo(100);
        }

        @Test
        @DisplayName("Should offer with timeout when space becomes available")
        void testOfferWithTimeoutWhenSpaceBecomesAvailable() throws InterruptedException {
            // Fill the channel
            for (int i = 0; i < CHANNEL_CAPACITY; i++) {
                producer.offer("item" + i);
            }

            ExecutorService executor = Executors.newSingleThreadExecutor();
            try {
                // Schedule consumption to free space after delay
                executor.submit(() -> {
                    try {
                        Thread.sleep(50);
                        consumer.poll();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });

                boolean result = producer.offer("delayed", 200, TimeUnit.MILLISECONDS);
                assertThat(result).isTrue();
            } finally {
                executor.shutdown();
            }
        }

        @Test
        @DisplayName("Should handle very short timeouts")
        void testVeryShortTimeout() throws InterruptedException {
            // Fill the channel
            for (int i = 0; i < CHANNEL_CAPACITY; i++) {
                producer.offer("item" + i);
            }

            boolean result = producer.offer("overflow", 1, TimeUnit.NANOSECONDS);
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("Should handle zero timeout")
        void testZeroTimeout() throws InterruptedException {
            boolean result = producer.offer("item1", 0, TimeUnit.MILLISECONDS);
            assertThat(result).isTrue();

            // Fill channel and try with zero timeout
            for (int i = 0; i < CHANNEL_CAPACITY; i++) {
                producer.offer("item" + i);
            }

            result = producer.offer("overflow", 0, TimeUnit.MILLISECONDS);
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("Blocking Operations")
    class BlockingOperationsTests {

        @Test
        @DisplayName("Should put element without blocking when space available")
        void testPutWithoutBlocking() {
            long startTime = System.currentTimeMillis();
            producer.put("item1");
            long endTime = System.currentTimeMillis();

            assertThat(consumer.poll()).isEqualTo("item1");
            assertThat(endTime - startTime).isLessThan(50); // Should be nearly instantaneous
        }

        @Test
        @DisplayName("Should put element and block when channel is full")
        @Timeout(value = 5, unit = TimeUnit.SECONDS)
        void testPutBlocksWhenFull() throws InterruptedException {
            // Fill the channel
            for (int i = 0; i < CHANNEL_CAPACITY; i++) {
                producer.put("item" + i);
            }

            CountDownLatch putStarted = new CountDownLatch(1);
            CountDownLatch putCompleted = new CountDownLatch(1);

            ExecutorService executor = Executors.newSingleThreadExecutor();
            try {
                Future<?> putTask = executor.submit(() -> {
                    putStarted.countDown();
                    producer.put("blocking_item");
                    putCompleted.countDown();
                });

                // Wait for put to start
                putStarted.await();

                // Put should be blocked - give it some time and verify it hasn't completed
                Thread.sleep(100);
                assertThat(putCompleted.getCount()).isEqualTo(1);

                // Free space by consuming an item
                consumer.poll();

                // Now put should complete
                putCompleted.await(1, TimeUnit.SECONDS);
                assertThat(putCompleted.getCount()).isZero();

                try {
                    putTask.get(1, TimeUnit.SECONDS);
                } catch (Exception e) {
                    // Handle execution exceptions
                    fail("Put task failed: " + e.getMessage());
                }
            } finally {
                executor.shutdown();
            }
        }

        @Test
        @DisplayName("Should handle interrupted put operation")
        void testInterruptedPut() throws InterruptedException {
            // Fill the channel
            for (int i = 0; i < CHANNEL_CAPACITY; i++) {
                producer.put("item" + i);
            }

            CountDownLatch putStarted = new CountDownLatch(1);
            ExecutorService executor = Executors.newSingleThreadExecutor();

            try {
                Future<?> putTask = executor.submit(() -> {
                    putStarted.countDown();
                    producer.put("interrupted_item");
                    return null;
                });

                putStarted.await();
                Thread.sleep(50); // Give time for put to block

                putTask.cancel(true); // Interrupt the thread

                // Verify the task was cancelled/interrupted
                assertThat(putTask.isCancelled() || putTask.isDone()).isTrue();
            } finally {
                executor.shutdown();
            }
        }
    }

    @Nested
    @DisplayName("Channel Clearing")
    class ChannelClearingTests {

        @Test
        @DisplayName("Should clear empty channel")
        void testClearEmptyChannel() {
            producer.clear();

            assertThat(consumer.poll()).isNull();
        }

        @Test
        @DisplayName("Should clear channel with single element")
        void testClearSingleElement() {
            producer.offer("item1");
            producer.clear();

            assertThat(consumer.poll()).isNull();
        }

        @Test
        @DisplayName("Should clear channel with multiple elements")
        void testClearMultipleElements() {
            producer.offer("item1");
            producer.offer("item2");
            producer.offer("item3");

            producer.clear();

            assertThat(consumer.poll()).isNull();
        }

        @Test
        @DisplayName("Should clear full channel")
        void testClearFullChannel() {
            // Fill channel to capacity
            for (int i = 0; i < CHANNEL_CAPACITY; i++) {
                producer.offer("item" + i);
            }

            producer.clear();

            assertThat(consumer.poll()).isNull();
        }

        @Test
        @DisplayName("Should allow new elements after clearing")
        void testOfferAfterClearing() {
            producer.offer("item1");
            producer.offer("item2");
            producer.clear();

            boolean result = producer.offer("new_item");
            assertThat(result).isTrue();
            assertThat(consumer.poll()).isEqualTo("new_item");
        }

        @Test
        @DisplayName("Should handle multiple clears")
        void testMultipleClears() {
            producer.offer("item1");
            producer.clear();
            producer.clear(); // Second clear on empty channel

            assertThat(consumer.poll()).isNull();
        }
    }

    @Nested
    @DisplayName("Concurrent Producer Operations")
    class ConcurrentOperationsTests {

        @Test
        @DisplayName("Should handle multiple producers")
        @Timeout(value = 5, unit = TimeUnit.SECONDS)
        void testMultipleProducers() throws InterruptedException {
            ChannelProducer<String> producer2 = channel.createProducer();

            CountDownLatch startLatch = new CountDownLatch(2);
            CountDownLatch completeLatch = new CountDownLatch(2);

            ExecutorService executor = Executors.newFixedThreadPool(2);

            try {
                executor.submit(() -> {
                    startLatch.countDown();
                    try {
                        startLatch.await();
                        producer.offer("producer1_item");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    completeLatch.countDown();
                });

                executor.submit(() -> {
                    startLatch.countDown();
                    try {
                        startLatch.await();
                        producer2.offer("producer2_item");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    completeLatch.countDown();
                });

                completeLatch.await();

                // Both items should be in the channel
                String item1 = consumer.poll();
                String item2 = consumer.poll();

                assertThat(item1).isNotNull();
                assertThat(item2).isNotNull();
                assertThat(new String[] { item1, item2 })
                        .containsExactlyInAnyOrder("producer1_item", "producer2_item");

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
                            String item = consumer.take();
                            assertThat(item).startsWith("item");
                            completeLatch.countDown();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                });

                completeLatch.await();

            } finally {
                executor.shutdown();
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle large number of operations")
        void testLargeNumberOfOperations() {
            final int operationCount = 1000;

            for (int i = 0; i < operationCount; i++) {
                if (producer.offer("item" + i)) {
                    consumer.poll(); // Immediately consume to make space
                }
            }

            // Should complete without issues
            assertThat(consumer.poll()).isNull();
        }

        @Test
        @DisplayName("Should handle rapid offer/clear cycles")
        void testRapidOfferClearCycles() {
            for (int i = 0; i < 100; i++) {
                producer.offer("item" + i);
                if (i % 10 == 0) {
                    producer.clear();
                }
            }

            // Should not crash or deadlock
            producer.clear();
            assertThat(consumer.poll()).isNull();
        }

        @Test
        @DisplayName("Should handle different timeout units")
        void testDifferentTimeoutUnits() throws InterruptedException {
            assertThat(producer.offer("item1", 1, TimeUnit.SECONDS)).isTrue();
            assertThat(producer.offer("item2", 1000, TimeUnit.MILLISECONDS)).isTrue();
            assertThat(producer.offer("item3", 1000000, TimeUnit.MICROSECONDS)).isTrue();

            assertThat(consumer.poll()).isEqualTo("item1");
            assertThat(consumer.poll()).isEqualTo("item2");
            assertThat(consumer.poll()).isEqualTo("item3");
        }

        @Test
        @DisplayName("Should work with different element types")
        void testDifferentElementTypes() {
            ConcurrentChannel<Integer> intChannel = new ConcurrentChannel<>(3);
            ChannelProducer<Integer> intProducer = intChannel.createProducer();
            ChannelConsumer<Integer> intConsumer = intChannel.createConsumer();

            assertThat(intProducer.offer(42)).isTrue();
            assertThat(intProducer.offer(-1)).isTrue();
            assertThat(intProducer.offer(0)).isTrue();

            assertThat(intConsumer.poll()).isEqualTo(42);
            assertThat(intConsumer.poll()).isEqualTo(-1);
            assertThat(intConsumer.poll()).isEqualTo(0);
        }
    }
}
