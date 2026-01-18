package pt.up.fe.specs.util.collections.concurrentchannel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for {@link ConcurrentChannel} and related classes.
 * Tests the bounded blocking queue wrapper with producer/consumer pattern.
 * 
 * @author Generated Tests
 */
class ConcurrentChannelTest {

    private ConcurrentChannel<String> channel;
    private static final int DEFAULT_CAPACITY = 5;

    @BeforeEach
    void setUp() {
        channel = new ConcurrentChannel<>(DEFAULT_CAPACITY);
    }

    @Nested
    @DisplayName("Constructor and Basic Properties")
    class ConstructorTests {

        @Test
        @DisplayName("Should create channel with specified capacity")
        void testChannelCreation() {
            ConcurrentChannel<Integer> intChannel = new ConcurrentChannel<>(10);

            assertThat(intChannel).isNotNull();
            assertThat(intChannel.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Should create empty channel initially")
        void testInitialState() {
            assertThat(channel.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Should handle different generic types")
        void testGenericTypes() {
            ConcurrentChannel<Integer> intChannel = new ConcurrentChannel<>(3);
            ConcurrentChannel<Object> objChannel = new ConcurrentChannel<>(3);
            ConcurrentChannel<List<String>> listChannel = new ConcurrentChannel<>(3);

            assertThat(intChannel.isEmpty()).isTrue();
            assertThat(objChannel.isEmpty()).isTrue();
            assertThat(listChannel.isEmpty()).isTrue();
        }
    }

    @Nested
    @DisplayName("Producer Creation and Basic Operations")
    class ProducerTests {

        @Test
        @DisplayName("Should create producer successfully")
        void testProducerCreation() {
            ChannelProducer<String> producer = channel.createProducer();

            assertThat(producer).isNotNull();
        }

        @Test
        @DisplayName("Should create multiple producers")
        void testMultipleProducers() {
            ChannelProducer<String> producer1 = channel.createProducer();
            ChannelProducer<String> producer2 = channel.createProducer();

            assertThat(producer1).isNotNull();
            assertThat(producer2).isNotNull();
            assertThat(producer1).isNotSameAs(producer2);
        }

        @Test
        @DisplayName("Should put elements via producer")
        void testProducerPut() throws InterruptedException {
            ChannelProducer<String> producer = channel.createProducer();

            producer.put("test1");
            assertThat(channel.isEmpty()).isFalse();

            producer.put("test2");
            assertThat(channel.isEmpty()).isFalse();
        }

        @Test
        @DisplayName("Should offer elements via producer")
        void testProducerOffer() {
            ChannelProducer<String> producer = channel.createProducer();

            boolean offered1 = producer.offer("test1");
            boolean offered2 = producer.offer("test2");

            assertThat(offered1).isTrue();
            assertThat(offered2).isTrue();
            assertThat(channel.isEmpty()).isFalse();
        }

        @Test
        @DisplayName("Should offer with timeout")
        void testProducerOfferWithTimeout() throws InterruptedException {
            ChannelProducer<String> producer = channel.createProducer();

            boolean offered = producer.offer("test", 100, TimeUnit.MILLISECONDS);

            assertThat(offered).isTrue();
            assertThat(channel.isEmpty()).isFalse();
        }
    }

    @Nested
    @DisplayName("Consumer Creation and Basic Operations")
    class ConsumerTests {

        @Test
        @DisplayName("Should create consumer successfully")
        void testConsumerCreation() {
            ChannelConsumer<String> consumer = channel.createConsumer();

            assertThat(consumer).isNotNull();
        }

        @Test
        @DisplayName("Should create multiple consumers")
        void testMultipleConsumers() {
            ChannelConsumer<String> consumer1 = channel.createConsumer();
            ChannelConsumer<String> consumer2 = channel.createConsumer();

            assertThat(consumer1).isNotNull();
            assertThat(consumer2).isNotNull();
            assertThat(consumer1).isNotSameAs(consumer2);
        }

        @Test
        @DisplayName("Should take elements via consumer")
        void testConsumerTake() throws InterruptedException {
            ChannelProducer<String> producer = channel.createProducer();
            ChannelConsumer<String> consumer = channel.createConsumer();

            producer.put("test1");
            String result = consumer.take();

            assertThat(result).isEqualTo("test1");
            assertThat(channel.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Should poll elements via consumer")
        void testConsumerPoll() throws InterruptedException {
            ChannelProducer<String> producer = channel.createProducer();
            ChannelConsumer<String> consumer = channel.createConsumer();

            producer.put("test1");
            String result = consumer.poll();

            assertThat(result).isEqualTo("test1");
            assertThat(channel.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Should poll with timeout")
        void testConsumerPollWithTimeout() throws InterruptedException {
            ChannelConsumer<String> consumer = channel.createConsumer();

            String result = consumer.poll(100, TimeUnit.MILLISECONDS);

            assertThat(result).isNull(); // Nothing to consume
        }

        @Test
        @DisplayName("Should return null when polling empty channel")
        void testConsumerPollEmpty() {
            ChannelConsumer<String> consumer = channel.createConsumer();

            String result = consumer.poll();

            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("Producer-Consumer Integration")
    class ProducerConsumerIntegrationTests {

        @Test
        @DisplayName("Should handle basic producer-consumer workflow")
        void testBasicWorkflow() throws InterruptedException {
            ChannelProducer<String> producer = channel.createProducer();
            ChannelConsumer<String> consumer = channel.createConsumer();

            // Produce some items
            producer.put("item1");
            producer.put("item2");
            producer.put("item3");

            // Consume items
            String item1 = consumer.take();
            String item2 = consumer.take();
            String item3 = consumer.take();

            assertThat(item1).isEqualTo("item1");
            assertThat(item2).isEqualTo("item2");
            assertThat(item3).isEqualTo("item3");
            assertThat(channel.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Should maintain FIFO order")
        void testFIFOOrder() throws InterruptedException {
            ChannelProducer<String> producer = channel.createProducer();
            ChannelConsumer<String> consumer = channel.createConsumer();

            List<String> produced = List.of("first", "second", "third", "fourth");

            // Produce items
            for (String item : produced) {
                producer.put(item);
            }

            // Consume items
            List<String> consumed = new ArrayList<>();
            for (int i = 0; i < produced.size(); i++) {
                consumed.add(consumer.take());
            }

            assertThat(consumed).containsExactlyElementsOf(produced);
        }

        @Test
        @DisplayName("Should handle multiple producers and consumers")
        void testMultipleProducersConsumers() throws InterruptedException {
            ChannelProducer<String> producer1 = channel.createProducer();
            ChannelProducer<String> producer2 = channel.createProducer();
            ChannelConsumer<String> consumer1 = channel.createConsumer();
            ChannelConsumer<String> consumer2 = channel.createConsumer();

            // Produce from multiple producers
            producer1.put("P1-item1");
            producer2.put("P2-item1");
            producer1.put("P1-item2");

            // Consume from multiple consumers
            String item1 = consumer1.take();
            String item2 = consumer2.take();
            String item3 = consumer1.take();

            assertThat(List.of(item1, item2, item3))
                    .containsExactlyInAnyOrder("P1-item1", "P2-item1", "P1-item2");
        }
    }

    @Nested
    @DisplayName("Capacity and Blocking Behavior")
    class CapacityTests {

        @Test
        @DisplayName("Should respect capacity limits")
        void testCapacityLimits() throws InterruptedException {
            ConcurrentChannel<Integer> smallChannel = new ConcurrentChannel<>(2);
            ChannelProducer<Integer> producer = smallChannel.createProducer();

            // Fill the channel to capacity
            producer.put(1);
            producer.put(2);

            // Try to add one more without blocking
            boolean offered = producer.offer(3);

            // Should not be able to add more than capacity
            assertThat(offered).isFalse();
        }

        @Test
        @DisplayName("Should block producer when channel is full")
        @Timeout(5)
        void testProducerBlocking() throws Exception {
            ConcurrentChannel<Integer> smallChannel = new ConcurrentChannel<>(1);
            ChannelProducer<Integer> producer = smallChannel.createProducer();
            ChannelConsumer<Integer> consumer = smallChannel.createConsumer();

            CountDownLatch producerReady = new CountDownLatch(1);
            CountDownLatch consumerReady = new CountDownLatch(1);

            ExecutorService executor = Executors.newFixedThreadPool(2);

            try {
                // Producer thread
                Future<?> producerFuture = executor.submit(() -> {
                    try {
                        producer.put(1); // This should succeed
                        producerReady.countDown();
                        producer.put(2); // This should block until consumer takes item
                    } catch (Exception e) {
                        Thread.currentThread().interrupt();
                    }
                });

                // Wait for producer to add first item
                producerReady.await();

                // Consumer thread
                Future<?> consumerFuture = executor.submit(() -> {
                    try {
                        Thread.sleep(100); // Brief delay to ensure producer is blocked
                        consumerReady.countDown();
                        Integer item = consumer.take(); // This should unblock producer
                        assertThat(item).isEqualTo(1);
                    } catch (Exception e) {
                        Thread.currentThread().interrupt();
                    }
                });

                consumerReady.await();
                producerFuture.get(2, TimeUnit.SECONDS);
                consumerFuture.get(2, TimeUnit.SECONDS);

            } finally {
                executor.shutdown();
            }
        }

        @Test
        @DisplayName("Should block consumer when channel is empty")
        @Timeout(5)
        void testConsumerBlocking() throws Exception {
            ChannelProducer<String> producer = channel.createProducer();
            ChannelConsumer<String> consumer = channel.createConsumer();

            CountDownLatch consumerStarted = new CountDownLatch(1);
            CountDownLatch itemProduced = new CountDownLatch(1);

            ExecutorService executor = Executors.newFixedThreadPool(2);

            try {
                // Consumer thread - should block waiting for item
                Future<String> consumerFuture = executor.submit(() -> {
                    try {
                        consumerStarted.countDown();
                        return consumer.take(); // This should block until producer adds item
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return null;
                    }
                });

                // Producer thread - adds item after delay
                Future<?> producerFuture = executor.submit(() -> {
                    try {
                        consumerStarted.await();
                        Thread.sleep(100); // Brief delay to ensure consumer is blocked
                        producer.put("delayed-item");
                        itemProduced.countDown();
                    } catch (Exception e) {
                        Thread.currentThread().interrupt();
                    }
                });

                itemProduced.await();
                String result = consumerFuture.get(2, TimeUnit.SECONDS);
                producerFuture.get(2, TimeUnit.SECONDS);

                assertThat(result).isEqualTo("delayed-item");

            } finally {
                executor.shutdown();
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle mixed data types for Object channel")
        void testMixedDataTypes() throws InterruptedException {
            ConcurrentChannel<Object> objChannel = new ConcurrentChannel<>(5);
            ChannelProducer<Object> producer = objChannel.createProducer();
            ChannelConsumer<Object> consumer = objChannel.createConsumer();

            producer.put("string");
            producer.put(42);
            producer.put(List.of("list", "item"));

            Object str = consumer.take();
            Object num = consumer.take();
            Object list = consumer.take();

            assertThat(str).isEqualTo("string");
            assertThat(num).isEqualTo(42);
            assertThat(list).isEqualTo(List.of("list", "item"));
        }

        @Test
        @DisplayName("Should handle very small capacity")
        void testSmallCapacity() throws InterruptedException {
            ConcurrentChannel<String> tinyChannel = new ConcurrentChannel<>(1);
            ChannelProducer<String> producer = tinyChannel.createProducer();
            ChannelConsumer<String> consumer = tinyChannel.createConsumer();

            producer.put("only-item");
            assertThat(tinyChannel.isEmpty()).isFalse();

            String result = consumer.take();
            assertThat(result).isEqualTo("only-item");
            assertThat(tinyChannel.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Should handle thread interruption gracefully")
        void testThreadInterruption() throws InterruptedException {
            ChannelConsumer<String> consumer = channel.createConsumer();

            ExecutorService executor = Executors.newSingleThreadExecutor();

            try {
                Future<Void> future = executor.submit(() -> {
                    try {
                        consumer.take(); // This will block indefinitely
                        return null;
                    } catch (Exception e) {
                        // Expected when thread is interrupted
                        Thread.currentThread().interrupt();
                        return null;
                    }
                });

                Thread.sleep(100); // Let the consumer start blocking
                future.cancel(true); // Interrupt the thread

                // The future should be cancelled
                assertThat(future.isCancelled()).isTrue();

            } finally {
                executor.shutdown();
            }
        }
    }

    @Nested
    @DisplayName("Performance and Concurrent Access")
    class PerformanceTests {

        @Test
        @DisplayName("Should handle high-throughput producer-consumer scenario")
        @Timeout(10)
        void testHighThroughput() throws InterruptedException {
            ConcurrentChannel<Integer> bigChannel = new ConcurrentChannel<>(100);
            ChannelProducer<Integer> producer = bigChannel.createProducer();
            ChannelConsumer<Integer> consumer = bigChannel.createConsumer();

            int itemCount = 1000;
            CountDownLatch producerDone = new CountDownLatch(1);
            CountDownLatch consumerDone = new CountDownLatch(1);

            ExecutorService executor = Executors.newFixedThreadPool(2);

            try {
                // Producer thread
                executor.submit(() -> {
                    try {
                        for (int i = 0; i < itemCount; i++) {
                            producer.put(i);
                        }
                        producerDone.countDown();
                    } catch (Exception e) {
                        Thread.currentThread().interrupt();
                    }
                });

                // Consumer thread
                List<Integer> consumed = new ArrayList<>();
                executor.submit(() -> {
                    try {
                        for (int i = 0; i < itemCount; i++) {
                            consumed.add(consumer.take());
                        }
                        consumerDone.countDown();
                    } catch (Exception e) {
                        Thread.currentThread().interrupt();
                    }
                });

                producerDone.await(5, TimeUnit.SECONDS);
                consumerDone.await(5, TimeUnit.SECONDS);

                assertThat(consumed).hasSize(itemCount);
                assertThat(consumed).containsExactly(
                        java.util.stream.IntStream.range(0, itemCount).boxed().toArray(Integer[]::new));

            } finally {
                executor.shutdown();
            }
        }

        @Test
        @DisplayName("Should handle multiple concurrent producers and consumers")
        @Timeout(10)
        void testMultipleConcurrentAccess() throws InterruptedException {
            ConcurrentChannel<String> sharedChannel = new ConcurrentChannel<>(50);

            int producerCount = 3;
            int consumerCount = 2;
            int itemsPerProducer = 20;

            CountDownLatch allDone = new CountDownLatch(producerCount + consumerCount);
            List<String> allConsumed = new ArrayList<>();

            ExecutorService executor = Executors.newFixedThreadPool(producerCount + consumerCount);

            try {
                // Start producers
                for (int p = 0; p < producerCount; p++) {
                    final int producerId = p;
                    executor.submit(() -> {
                        try {
                            ChannelProducer<String> producer = sharedChannel.createProducer();
                            for (int i = 0; i < itemsPerProducer; i++) {
                                producer.put("P" + producerId + "-item" + i);
                            }
                            allDone.countDown();
                        } catch (Exception e) {
                            Thread.currentThread().interrupt();
                        }
                    });
                }

                // Start consumers
                for (int c = 0; c < consumerCount; c++) {
                    executor.submit(() -> {
                        try {
                            ChannelConsumer<String> consumer = sharedChannel.createConsumer();
                            int itemsToConsume = (producerCount * itemsPerProducer) / consumerCount;
                            for (int i = 0; i < itemsToConsume; i++) {
                                synchronized (allConsumed) {
                                    allConsumed.add(consumer.take());
                                }
                            }
                            allDone.countDown();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    });
                }

                allDone.await(8, TimeUnit.SECONDS);

                assertThat(allConsumed).hasSize(producerCount * itemsPerProducer);

                // Verify all items from all producers are present
                for (int p = 0; p < producerCount; p++) {
                    for (int i = 0; i < itemsPerProducer; i++) {
                        String expectedItem = "P" + p + "-item" + i;
                        assertThat(allConsumed).contains(expectedItem);
                    }
                }

            } finally {
                executor.shutdown();
            }
        }
    }
}
